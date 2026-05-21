package xyz.webmc.wlib.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sandrohc.schematic4j.SchematicLoader;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;

public final class SchemUtil {
  public static final Schematic readSchematic(final InputStream is) throws IOException, ParsingException {
    return SchematicLoader.load(is);
  }

  public static final Schematic readSchematic(final File file) throws IOException, ParsingException {
    return readSchematic(new FileInputStream(file));
  }
}
