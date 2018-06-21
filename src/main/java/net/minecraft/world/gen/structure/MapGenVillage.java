package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenVillage extends MapGenStructure {

    public static List<Biome> field_75055_e = Arrays.asList(new Biome[] { Biomes.field_76772_c, Biomes.field_76769_d, Biomes.field_150588_X, Biomes.field_76768_g});
    private int field_75054_f;
    private int field_82665_g;
    private final int field_82666_h;

    public MapGenVillage() {
        this.field_82665_g = 32;
        this.field_82666_h = 8;
    }

    public MapGenVillage(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("size")) {
                this.field_75054_f = MathHelper.func_82714_a((String) entry.getValue(), this.field_75054_f, 0);
            } else if (((String) entry.getKey()).equals("distance")) {
                this.field_82665_g = MathHelper.func_82714_a((String) entry.getValue(), this.field_82665_g, 9);
            }
        }

    }

    public String func_143025_a() {
        return "Village";
    }

    protected boolean func_75047_a(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            i -= this.field_82665_g - 1;
        }

        if (j < 0) {
            j -= this.field_82665_g - 1;
        }

        int i1 = i / this.field_82665_g;
        int j1 = j / this.field_82665_g;
        Random random = this.field_75039_c.func_72843_D(i1, j1, this.field_75039_c.spigotConfig.villageSeed); // Spigot

        i1 *= this.field_82665_g;
        j1 *= this.field_82665_g;
        i1 += random.nextInt(this.field_82665_g - 8);
        j1 += random.nextInt(this.field_82665_g - 8);
        if (k == i1 && l == j1) {
            boolean flag = this.field_75039_c.func_72959_q().func_76940_a(k * 16 + 8, l * 16 + 8, 0, MapGenVillage.field_75055_e);

            if (flag) {
                return true;
            }
        }

        return false;
    }

    public BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag) {
        this.field_75039_c = world;
        return func_191069_a(world, this, blockposition, this.field_82665_g, 8, 10387312, false, 100, flag);
    }

    protected StructureStart func_75049_b(int i, int j) {
        return new MapGenVillage.Start(this.field_75039_c, this.field_75038_b, i, j, this.field_75054_f);
    }

    public static class Start extends StructureStart {

        private boolean field_75076_c;

        public Start() {}

        public Start(World world, Random random, int i, int j, int k) {
            super(i, j);
            List list = StructureVillagePieces.func_75084_a(random, k);
            StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece = new StructureVillagePieces.Start(world.func_72959_q(), 0, random, (i << 4) + 2, (j << 4) + 2, list, k);

            this.field_75075_a.add(worldgenvillagepieces_worldgenvillagestartpiece);
            worldgenvillagepieces_worldgenvillagestartpiece.func_74861_a((StructureComponent) worldgenvillagepieces_worldgenvillagestartpiece, this.field_75075_a, random);
            List list1 = worldgenvillagepieces_worldgenvillagestartpiece.field_74930_j;
            List list2 = worldgenvillagepieces_worldgenvillagestartpiece.field_74932_i;

            int l;

            while (!list1.isEmpty() || !list2.isEmpty()) {
                StructureComponent structurepiece;

                if (list1.isEmpty()) {
                    l = random.nextInt(list2.size());
                    structurepiece = (StructureComponent) list2.remove(l);
                    structurepiece.func_74861_a((StructureComponent) worldgenvillagepieces_worldgenvillagestartpiece, this.field_75075_a, random);
                } else {
                    l = random.nextInt(list1.size());
                    structurepiece = (StructureComponent) list1.remove(l);
                    structurepiece.func_74861_a((StructureComponent) worldgenvillagepieces_worldgenvillagestartpiece, this.field_75075_a, random);
                }
            }

            this.func_75072_c();
            l = 0;
            Iterator iterator = this.field_75075_a.iterator();

            while (iterator.hasNext()) {
                StructureComponent structurepiece1 = (StructureComponent) iterator.next();

                if (!(structurepiece1 instanceof StructureVillagePieces.Road)) {
                    ++l;
                }
            }

            this.field_75076_c = l > 2;
        }

        public boolean func_75069_d() {
            return this.field_75076_c;
        }

        public void func_143022_a(NBTTagCompound nbttagcompound) {
            super.func_143022_a(nbttagcompound);
            nbttagcompound.func_74757_a("Valid", this.field_75076_c);
        }

        public void func_143017_b(NBTTagCompound nbttagcompound) {
            super.func_143017_b(nbttagcompound);
            this.field_75076_c = nbttagcompound.func_74767_n("Valid");
        }
    }
}
