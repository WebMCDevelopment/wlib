package xyz.webmc.wlib.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletionException;

import dev.zerite.craftlib.schematic.Schematic;
import dev.zerite.craftlib.schematic.SchematicIO;

public final class SchemUtil {
  public static final Schematic readSchematicFile(final File file) throws IOException {
    try (FileInputStream input = new FileInputStream(file)) {
      try {
        return SchematicIO.readFuture(input, false).join();
      } catch (final CompletionException ex) {
        if (ex.getCause() instanceof IOException io) {
          throw io;
        } else {
          throw ex;
        }
      }
    }
  }

  public static final void writeSchematicFile(final Schematic schematic, final File file) throws IOException {
    try (FileOutputStream output = new FileOutputStream(file)) {
      try {
        SchematicIO.writeFuture(schematic, output, false).join();
      } catch (final CompletionException ex) {
        if (ex.getCause() instanceof IOException io) {
          throw io;
        } else {
          throw ex;
        }
      }
    }
  }
}
