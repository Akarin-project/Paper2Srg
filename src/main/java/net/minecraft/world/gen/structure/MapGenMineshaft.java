package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMesa;

public class MapGenMineshaft extends MapGenStructure {

    private double chance = 0.004D;

    public MapGenMineshaft() {}

    public String getStructureName() {
        return "Mineshaft";
    }

    public MapGenMineshaft(Map<String, String> map) {
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("chance")) {
                this.chance = MathHelper.getDouble((String) entry.getValue(), this.chance);
            }
        }

    }

    protected boolean canSpawnStructureAtCoords(int i, int j) {
        return this.rand.nextDouble() < this.chance && this.rand.nextInt(80) < Math.max(Math.abs(i), Math.abs(j));
    }

    public BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag) {
        boolean flag1 = true;
        int i = blockposition.getX() >> 4;
        int j = blockposition.getZ() >> 4;

        for (int k = 0; k <= 1000; ++k) {
            for (int l = -k; l <= k; ++l) {
                boolean flag2 = l == -k || l == k;

                for (int i1 = -k; i1 <= k; ++i1) {
                    boolean flag3 = i1 == -k || i1 == k;

                    if (flag2 || flag3) {
                        int j1 = i + l;
                        int k1 = j + i1;

                        this.rand.setSeed((long) (j1 ^ k1) ^ world.getSeed());
                        this.rand.nextInt();
                        if (this.canSpawnStructureAtCoords(j1, k1) && (!flag || !world.isChunkGeneratedAt(j1, k1))) {
                            return new BlockPos((j1 << 4) + 8, 64, (k1 << 4) + 8);
                        }
                    }
                }
            }
        }

        return null;
    }

    protected StructureStart getStructureStart(int i, int j) {
        Biome biomebase = this.world.getBiome(new BlockPos((i << 4) + 8, 64, (j << 4) + 8));
        MapGenMineshaft.Type worldgenmineshaft_type = biomebase instanceof BiomeMesa ? MapGenMineshaft.Type.MESA : MapGenMineshaft.Type.NORMAL;

        return new StructureMineshaftStart(this.world, this.rand, i, j, worldgenmineshaft_type);
    }

    public static enum Type {

        NORMAL, MESA;

        private Type() {}

        public static MapGenMineshaft.Type byId(int i) {
            return i >= 0 && i < values().length ? values()[i] : MapGenMineshaft.Type.NORMAL;
        }
    }
}
