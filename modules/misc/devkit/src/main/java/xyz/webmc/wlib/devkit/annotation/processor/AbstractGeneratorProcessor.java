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

package xyz.webmc.wlib.devkit.annotation.processor;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

abstract class AbstractGeneratorProcessor<T extends Annotation> {
  private static final Pattern PROPERTY = Pattern.compile("\\$\\{([^}]+)}");

  protected final ProcessingEnvironment processingEnv;
  private final Properties properties;

  protected abstract Class<T> getAnnotationClass();
  protected abstract void process(TypeElement type);

  protected AbstractGeneratorProcessor(ProcessingEnvironment processingEnv, Properties properties) {
    this.processingEnv = processingEnv;
    this.properties = properties;
  }

  final void process(RoundEnvironment roundEnv) {
    final Class<T> clazz = this.getAnnotationClass();
    roundEnv.getElementsAnnotatedWith(clazz).forEach(el -> {
      if (el instanceof TypeElement type) {
        process(type);
      }
    });
  }

  protected final void write(Writer writer, int indent, String str) throws IOException {
    writer.write("  ".repeat(indent) + str + "\n");
  }

  protected final void write(Writer writer, String str) throws IOException {
    write(writer, 0, str);
  }

  protected final void writeYml(Writer writer, int indent, String key, String value) throws IOException {
    if (value != null && !value.isBlank()) {
      _writeYml(writer, indent, key, "\"" + value + "\"");
    }
  }

  protected final void writeYml(Writer writer, String key, String value) throws IOException {
    writeYml(writer, 0, key, value);
  }

  protected final void writeYml(Writer writer, int indent, String key, boolean value) throws IOException {
    _writeYml(writer, indent, key, Boolean.toString(value));
  }

  protected final void writeYml(Writer writer, String key, boolean value) throws IOException {
    writeYml(writer, 0, key, value);
  }

  protected final void writeYml(Writer writer, int indent, String key, String[] arr) throws IOException {
    if (arr.length > 0) {
      final List<String> lst = new ArrayList<>(arr.length);

      for (String str : arr) {
        lst.add("\"" + parseProperties(str) + "\"");
      }

      _writeYml(writer, indent, key, "[" + String.join(",", lst) + "]");
    }
  }

  protected final void writeYml(Writer writer, String key, String[] arr) throws IOException {
    writeYml(writer, 0, key, arr);
  }

  protected final void writeYml(Writer writer, int indent, String key) throws IOException {
    _writeYml(writer, indent, key, "");
  }

  protected final void writeYml(Writer writer, String key) throws IOException {
    writeYml(writer, 0, key);
  }

  protected final void writeYmlList(Writer writer, int indent, String[] arr) throws IOException {
    if (arr.length > 0) {
      for (String str : arr) {
        write(writer, indent, "- \"" + parseProperties(str) + "\"");
      }
    }
  }

  protected final void writeYmlList(Writer writer, String[] arr) throws IOException {
    writeYmlList(writer, 0, arr);
  }

  protected final void writeYmlList(Writer writer, int indent, String key, String[] arr) throws IOException {
    if (arr.length > 0) {
      writeYml(writer, indent, key);
      writeYmlList(writer, indent + 1, arr);
    }
  }

  protected final void writeYmlList(Writer writer, String key, String[] arr) throws IOException {
    writeYmlList(writer, 0, key, arr);
  }

  protected final String parseProperties(String str) {
    if (str != null && !str.isBlank()) {
      final Matcher matcher = PROPERTY.matcher(str);
      final StringBuffer ret = new StringBuffer();

      while (matcher.find()) {
        String replacement = properties.getProperty(matcher.group(1));

        if (replacement == null) {
          replacement = matcher.group();
        }

        matcher.appendReplacement(ret, Matcher.quoteReplacement(replacement));
      }

      matcher.appendTail(ret);

      return ret.toString();
    } else {
      return str;
    }
  }

  private void _writeYml(Writer writer, int indent, String key, String value) throws IOException {
    write(writer, indent, key + (": " + parseProperties(value)).trim());
  }

  protected static <A extends Annotation> A defaultAnnotation(Class<A> type) {
    return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, (proxy, method, args) -> {
      return method.getName().equals("annotationType") ? type : method.getDefaultValue();
    }));
  }

  protected static boolean isElementDefault(Annotation annotation, String name) {
    try {
      final Method method = annotation.annotationType().getMethod(name);
      final Object defaultValue = method.getDefaultValue();
      final Object value = method.invoke(annotation);
      if (defaultValue != null) {
        if (defaultValue.getClass().isArray()) {
          return Arrays.deepEquals(new Object[] { defaultValue }, new Object[] { value });
        } else {
          return defaultValue.equals(value);
        }
      }
    } catch (Throwable t) {}

    return false;
  }
}
