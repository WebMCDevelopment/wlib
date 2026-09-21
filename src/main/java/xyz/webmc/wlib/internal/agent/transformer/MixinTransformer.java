/*
 * Copyright (C) 2026 Colbster937
 *
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * See the LICENSE file for details.
 */

package xyz.webmc.wlib.internal.agent.transformer;

import xyz.webmc.wlib.api.agent.mixin.Mixin;
import xyz.webmc.wlib.api.agent.mixin.transform.Overwrite;
import xyz.webmc.wlib.api.agent.mixin.transform.Shadow;
import xyz.webmc.wlib.api.agent.transformer.WClassTransformer;
import xyz.webmc.wlib.api.util.AgentUtil;

import java.io.ByteArrayInputStream;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Opcode;

import dev.colbster937.reflect.MirrorSafe;

public final class MixinTransformer extends WClassTransformer {
  private static final Map<String, List<Class<?>>> MIXINS = new HashMap<>();

  public MixinTransformer() {
    super(false);
  }

  public static void _addMixin(Class<?> clazz) {
    final Mixin mixin = clazz.getAnnotation(Mixin.class);
    if (mixin != null) {
      final Class<?> mixinClass = mixin.value();
      final String mixinClassName = mixinClass.getName();

      List<Class<?>> lst = MIXINS.get(mixinClassName);
      if (lst == null) {
        MIXINS.put(mixinClassName, lst = new ArrayList<>());
      }

      lst.add(clazz);

      AgentUtil.onReady(() -> {
        AgentUtil.retransformClasses(mixinClass);
      });
    } else {
      throw new IllegalArgumentException(clazz.getCanonicalName());
    }
  }

  public static void _addMixins(Class<?>[] classes) {
    for (Class<?> clazz : classes) {
      _addMixin(clazz);
    }
  }

  @Override
  protected Set<String> getTransformList() {
    return Set.of("**");
  }

  @Override
  protected byte[] transform(ClassLoader loader, String name, Class<?> clazz, byte[] bytes) throws Exception {
    final List<Class<?>> mixins = MIXINS.get(clazz.getName());
    if (mixins != null) {
      CtClass ctClass = null;

      try {
        final ClassPool pool = ClassPool.getDefault();

        insertClassPath(pool, ClassLoader.getPlatformClassLoader());

        if (loader != null) {
          insertClassPath(pool, loader);
        }

        ctClass = pool.makeClass(new ByteArrayInputStream(bytes));

        for (Class<?> mixin : mixins) {
          insertClassPath(pool, mixin.getClassLoader());
          final CtClass ctMixin = pool.get(mixin.getName());

          for (Method method : mixin.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers()) && method.getAnnotation(Shadow.class) == null) {
              final Overwrite overwrite = method.getAnnotation(Overwrite.class);

              if (overwrite != null) {
                String overwriteMethod = overwrite.method();
                if (overwriteMethod.isBlank()) {
                  overwriteMethod = method.getName();
                }

                final String descriptor = getDescriptor(method);

                final CtMethod source = getMethod(ctMixin, method.getName(), descriptor);
                final CtMethod target = getMethod(ctClass, overwriteMethod, descriptor);

                if (
                  source != null &&
                    target != null &&
                    Modifier.isStatic(source.getModifiers()) == Modifier.isStatic(target.getModifiers())
                ) {
                  final CodeAttribute code = source.getMethodInfo().getCodeAttribute();
                  if (code != null) {
                    target.getMethodInfo().setCodeAttribute(
                      (CodeAttribute) code.copy(
                        target.getMethodInfo().getConstPool(),
                        null
                      )
                    );

                    target.getMethodInfo().rebuildStackMap(pool);
                  }
                }
              }
            }
          }

          for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
            final CodeAttribute code = ctMethod.getMethodInfo().getCodeAttribute();
            if (code != null) {
              final ConstPool constPool = ctMethod.getMethodInfo().getConstPool();
              final CodeIterator it = code.iterator();
              while (it.hasNext()) {
                final int index = it.next();
                final int opcode = it.byteAt(index);
                if (
                  opcode == Opcode.INVOKESTATIC ||
                    opcode == Opcode.INVOKEVIRTUAL ||
                    opcode == Opcode.INVOKESPECIAL
                ) {
                  final int methodIndex = it.u16bitAt(index + 1);
                  if (constPool.getMethodrefClassName(methodIndex).equals(mixin.getName())) {
                    final String methodName = constPool.getMethodrefName(methodIndex);
                    final String methodType = constPool.getMethodrefType(methodIndex);
                    for (Method method : mixin.getDeclaredMethods()) {
                      if (
                        method.getAnnotation(Shadow.class) != null &&
                          method.getName().equals(methodName) &&
                          getDescriptor(method).equals(methodType)
                      ) {
                        final CtMethod target = getMethod(ctClass, methodName, methodType);

                        if (
                          target != null &&
                          Modifier.isStatic(method.getModifiers()) == Modifier.isStatic(target.getModifiers())
                        ) {
                          final int classIndex = constPool.addClassInfo(ctClass.getName());
                          final int nameTypeIndex = constPool.addNameAndTypeInfo(
                            methodName,
                            methodType
                          );

                          it.write16bit(
                            constPool.addMethodrefInfo(
                              classIndex,
                              nameTypeIndex
                            ),
                            index + 1
                          );

                          if (Modifier.isStatic(target.getModifiers())) {
                            it.writeByte(Opcode.INVOKESTATIC, index);
                          } else if (Modifier.isPrivate(target.getModifiers())) {
                            it.writeByte(Opcode.INVOKESPECIAL, index);
                          } else {
                            it.writeByte(Opcode.INVOKEVIRTUAL, index);
                          }
                        }

                        break;
                      }
                    }
                  }
                } else if (
                  opcode == Opcode.GETSTATIC ||
                    opcode == Opcode.PUTSTATIC ||
                    opcode == Opcode.GETFIELD ||
                    opcode == Opcode.PUTFIELD
                ) {
                  final int fieldIndex = it.u16bitAt(index + 1);
                  if (constPool.getFieldrefClassName(fieldIndex).equals(mixin.getName())) {
                    final String fieldName = constPool.getFieldrefName(fieldIndex);
                    final String fieldType = constPool.getFieldrefType(fieldIndex);
                    for (Field field : mixin.getDeclaredFields()) {
                      if (field.getAnnotation(Shadow.class) != null && field.getName().equals(fieldName) && field.getType().descriptorString().equals(fieldType)) {
                        final CtField target = getField(ctClass, fieldName, fieldType);

                        if (
                          target != null &&
                          Modifier.isStatic(field.getModifiers()) == Modifier.isStatic(target.getModifiers())
                        ) {
                          final int classIndex = constPool.addClassInfo(ctClass.getName());
                          final int nameTypeIndex = constPool.addNameAndTypeInfo(
                            fieldName,
                            fieldType
                          );

                          it.write16bit(
                            constPool.addFieldrefInfo(
                              classIndex,
                              nameTypeIndex
                            ),
                            index + 1
                          );
                        }

                        break;
                      }
                    }
                  }
                }
              }

              ctMethod.getMethodInfo().rebuildStackMap(pool);
            }
          }
        }

        return ctClass.toBytecode();
      } finally {
        if (ctClass != null) {
          ctClass.detach();
        }
      }
    }

    return null;
  }

  private static void insertClassPath(ClassPool pool, ClassLoader loader) throws ReflectiveOperationException {
    MirrorSafe.invokeMethod(pool, "insertClassPath",
      (Object) MirrorSafe.invokeConstructor(
        Class.forName(
          LoaderClassPath.class.getName(),
          true,
          pool.getClass().getClassLoader()
        ),
        loader
      )
    );
  }

  private static CtMethod getMethod(CtClass clazz, String name, String descriptor) {
    try {
      for (CtMethod method : clazz.getDeclaredMethods(name)) {
        if (method.getMethodInfo().getDescriptor().equals(descriptor)) {
          return method;
        }
      }
    } catch (NotFoundException ex) {}

    return null;
  }

  private static CtField getField(CtClass clazz, String name, String descriptor) {
    try {
      final CtField field = clazz.getDeclaredField(name);
      if (field.getFieldInfo().getDescriptor().equals(descriptor)) {
        return field;
      }
    } catch (NotFoundException ex) {}

    return null;
  }

  private static String getDescriptor(Method method) {
    return MethodType.methodType(
      method.getReturnType(),
      method.getParameterTypes()
    ).descriptorString();
  }
}
