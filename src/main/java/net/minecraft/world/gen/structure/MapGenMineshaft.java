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

    private double field_82673_e = 0.004D;

    public MapGenMineshaft() {}

    public String func_143025_a() {
        return "Mineshaft";
    }

    public MapGenMineshaft(Map<String, String> map) {
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("chance")) {
                this.field_82673_e = MathHelper.func_82712_a((String) entry.getValue(), this.field_82673_e);
            }
        }

    }

    protected boolean func_75047_a(int i, int j) {
        return this.field_75038_b.nextDouble() < this.field_82673_e && this.field_75038_b.nextInt(80) < Math.max(Math.abs(i), Math.abs(j));
    }

    public BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag) {
        boolean flag1 = true;
        int i = blockposition.func_177958_n() >> 4;
        int j = blockposition.func_177952_p() >> 4;

        for (int k = 0; k <= 1000; ++k) {
            for (int l = -k; l <= k; ++l) {
                boolean flag2 = l == -k || l == k;

                for (int i1 = -k; i1 <= k; ++i1) {
                    boolean flag3 = i1 == -k || i1 == k;

                    if (flag2 || flag3) {
                        int j1 = i + l;
                        int k1 = j + i1;

                        this.field_75038_b.setSeed((long) (j1 ^ k1) ^ world.func_72905_C());
                        this.field_75038_b.nextInt();
                        if (this.func_75047_a(j1, k1) && (!flag || !world.func_190526_b(j1, k1))) {
                            return new BlockPos((j1 << 4) + 8, 64, (k1 << 4) + 8);
                        }
                    }
                }
            }
        }

        return null;
    }

    protected StructureStart func_75049_b(int i, int j) {
        Biome biomebase = this.field_75039_c.func_180494_b(new BlockPos((i << 4) + 8, 64, (j << 4) + 8));
        MapGenMineshaft.Type worldgenmineshaft_type = biomebase instanceof BiomeMesa ? MapGenMineshaft.Type.MESA : MapGenMineshaft.Type.NORMAL;

        return new StructureMineshaftStart(this.field_75039_c, this.field_75038_b, i, j, worldgenmineshaft_type);
    }

    public static enum Type {

        NORMAL, MESA;

        private Type() {}

        public static MapGenMineshaft.Type func_189910_a(int i) {
            return i >= 0 && i < values().length ? values()[i] : MapGenMineshaft.Type.NORMAL;
        }
    }
}
