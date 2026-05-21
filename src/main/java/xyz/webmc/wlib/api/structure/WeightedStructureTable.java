package xyz.webmc.wlib.api.structure;

import xyz.webmc.wlib.api.util.RNGUtil;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import dev.colbster937.reflect.MirrorSafe;
import dev.colbster937.util.WeightedObjectTable;

public final class WeightedStructureTable extends WeightedObjectTable<AbstractBaseStructure> {
  public WeightedStructureTable(final long seed, final WeightedStructure ...structures) {
    super(seed, structures);
  }

  public WeightedStructureTable(final WeightedStructure ...structures) {
    this(RNGUtil.getRandomSeed(), structures);
  }

  @SafeVarargs
  public WeightedStructureTable(final long seed, final Class<? extends AbstractBaseStructure> ...structures) {
    this(seed, weigh(structures));
  }

  @SafeVarargs
  public WeightedStructureTable(final Class<? extends AbstractBaseStructure> ...structures) {
    this(weigh(structures));
  }

  public final void place(final Location loc) {
    computeRandomObject().place(loc);
  }

  @SafeVarargs
  private static WeightedStructure[] weigh(final Class<? extends AbstractBaseStructure>... structures) {
    final List<WeightedStructure> lst = new ArrayList<>();
    final int chance = 100 / structures.length;

    for (final Class<? extends AbstractBaseStructure> clazz : structures) {
      lst.add(new WeightedStructure(MirrorSafe.invokeConstructor(clazz), chance));
    }

    return lst.toArray(WeightedStructure[]::new);
  }
}
