package xyz.webmc.wlib.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletionException;

import dev.zerite.craftlib.schematic.Schematic;
import dev.zerite.craftlib.schematic.SchematicIO;

public final class SchemUtil {
  public static final Schematic readSchematic(final InputStream is) throws IOException {
    try (is) {
      return SchematicIO.readFuture(is, false).join();
    } catch (final CompletionException ex) {
      if (ex.getCause() instanceof IOException io) {
        throw io;
      } else {
        throw ex;
      }
    }
  }

  public static final Schematic readSchematic(final File file) throws IOException {
    return readSchematic(new FileInputStream(file));
  }

  public static final void writeSchematic(final Schematic schematic, final OutputStream os) throws IOException {
    try (os) {
      SchematicIO.writeFuture(schematic, os, false).join();
    } catch (final CompletionException ex) {
      if (ex.getCause() instanceof IOException io) {
        throw io;
      } else {
        throw ex;
      }
    }
  }

  public static final void writeSchematic(final Schematic schematic, final File file) throws IOException {
    writeSchematic(schematic, new FileOutputStream(file));
  }
}
