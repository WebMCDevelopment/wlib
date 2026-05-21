package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.api.WLIB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import net.sandrohc.schematic4j.SchematicLoader;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;
import org.bukkit.Bukkit;

public final class SchemUtil {
  public static final Schematic readSchematic(final InputStream is) throws IOException, ParsingException {
    warnUnsupported();
    return SchematicLoader.load(is);
  }

  public static final Schematic readSchematic(final File file) throws IOException, ParsingException {
    return readSchematic(new FileInputStream(file));
  }

  public static final void warnUnsupported() {
    if (!WLIB.getIsModernServer()) {
      WLIB.getLogger().log(Level.WARNING, "Schematic loading is unsupported on server version {0}.", Bukkit.getVersion());
    }
  }
}
