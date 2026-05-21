package xyz.webmc.wlib.api.structure;

import dev.colbster937.util.WeightedObject;

public final class WeightedStructure extends WeightedObject<AbstractBaseStructure> {
  public WeightedStructure(final AbstractBaseStructure structure, final int weight) {
    super(structure, weight);
  }
}
