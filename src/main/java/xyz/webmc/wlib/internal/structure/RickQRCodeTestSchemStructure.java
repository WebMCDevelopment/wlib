package xyz.webmc.wlib.internal.structure;

import xyz.webmc.wlib.api.structure.AbstractBaseStructure;

import java.io.IOException;
import java.io.InputStream;

import net.sandrohc.schematic4j.exception.ParsingException;

public final class RickQRCodeTestSchemStructure extends AbstractBaseStructure {
  public RickQRCodeTestSchemStructure() throws IOException, ParsingException {
    super("rick_qr");
    try (InputStream is = RickQRCodeTestSchemStructure.class.getResourceAsStream("/rick.schem")) {
      super.loadSchematic(is);
    }
  }

  public static final RickQRCodeTestSchemStructure getInstance() {
    return getInstance(RickQRCodeTestSchemStructure.class);
  }
}
