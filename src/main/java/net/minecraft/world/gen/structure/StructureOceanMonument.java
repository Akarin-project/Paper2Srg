package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class StructureOceanMonument extends MapGenStructure {

    private int field_175800_f;
    private int field_175801_g;
    public static final List<Biome> field_175802_d = Arrays.asList(new Biome[] { Biomes.field_76771_b, Biomes.field_150575_M, Biomes.field_76781_i, Biomes.field_76776_l, Biomes.field_76777_m});
    public static final List<Biome> field_186134_b = Arrays.asList(new Biome[] { Biomes.field_150575_M});
    private static final List<Biome.SpawnListEntry> field_175803_h = Lists.newArrayList();

    public StructureOceanMonument() {
        this.field_175800_f = 32;
        this.field_175801_g = 5;
    }

    public StructureOceanMonument(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("spacing")) {
                this.field_175800_f = MathHelper.func_82714_a((String) entry.getValue(), this.field_175800_f, 1);
            } else if (((String) entry.getKey()).equals("separation")) {
                this.field_175801_g = MathHelper.func_82714_a((String) entry.getValue(), this.field_175801_g, 1);
            }
        }

    }

    public String func_143025_a() {
        return "Monument";
    }

    protected boolean func_75047_a(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            i -= this.field_175800_f - 1;
        }

        if (j < 0) {
            j -= this.field_175800_f - 1;
        }

        int i1 = i / this.field_175800_f;
        int j1 = j / this.field_175800_f;
        Random random = this.field_75039_c.func_72843_D(i1, j1, this.field_75039_c.spigotConfig.monumentSeed); // Spigot

        i1 *= this.field_175800_f;
        j1 *= this.field_175800_f;
        i1 += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
        j1 += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
        if (k == i1 && l == j1) {
            if (!this.field_75039_c.func_72959_q().func_76940_a(k * 16 + 8, l * 16 + 8, 16, StructureOceanMonument.field_186134_b)) {
                return false;
            }

            boolean flag = this.field_75039_c.func_72959_q().func_76940_a(k * 16 + 8, l * 16 + 8, 29, StructureOceanMonument.field_175802_d);

            if (flag) {
                return true;
            }
        }

        return false;
    }

    public BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag) {
        this.field_75039_c = world;
        return func_191069_a(world, this, blockposition, this.field_175800_f, this.field_175801_g, this.field_75039_c.spigotConfig.monumentSeed, true, 100, flag); // Spigot
    }

    protected StructureStart func_75049_b(int i, int j) {
        return new StructureOceanMonument.StartMonument(this.field_75039_c, this.field_75038_b, i, j);
    }

    public List<Biome.SpawnListEntry> func_175799_b() {
        return StructureOceanMonument.field_175803_h;
    }

    static {
        StructureOceanMonument.field_175803_h.add(new Biome.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }

    public static class StartMonument extends StructureStart {

        private final Set<ChunkPos> field_175791_c = Sets.newHashSet();
        private boolean field_175790_d;

        public StartMonument() {}

        public StartMonument(World world, Random random, int i, int j) {
            super(i, j);
            this.func_175789_b(world, random, i, j);
        }

        private void func_175789_b(World world, Random random, int i, int j) {
            random.setSeed(world.func_72905_C());
            long k = random.nextLong();
            long l = random.nextLong();
            long i1 = (long) i * k;
            long j1 = (long) j * l;

            random.setSeed(i1 ^ j1 ^ world.func_72905_C());
            int k1 = i * 16 + 8 - 29;
            int l1 = j * 16 + 8 - 29;
            EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.func_179518_a(random);

            this.field_75075_a.add(new StructureOceanMonumentPieces.MonumentBuilding(random, k1, l1, enumdirection));
            this.func_75072_c();
            this.field_175790_d = true;
        }

        public void func_75068_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (!this.field_175790_d) {
                this.field_75075_a.clear();
                this.func_175789_b(world, random, this.func_143019_e(), this.func_143018_f());
            }

            super.func_75068_a(world, random, structureboundingbox);
        }

        public boolean func_175788_a(ChunkPos chunkcoordintpair) {
            return this.field_175791_c.contains(chunkcoordintpair) ? false : super.func_175788_a(chunkcoordintpair);
        }

        public void func_175787_b(ChunkPos chunkcoordintpair) {
            super.func_175787_b(chunkcoordintpair);
            this.field_175791_c.add(chunkcoordintpair);
        }

        public void func_143022_a(NBTTagCompound nbttagcompound) {
            super.func_143022_a(nbttagcompound);
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.field_175791_c.iterator();

            while (iterator.hasNext()) {
                ChunkPos chunkcoordintpair = (ChunkPos) iterator.next();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.func_74768_a("X", chunkcoordintpair.field_77276_a);
                nbttagcompound1.func_74768_a("Z", chunkcoordintpair.field_77275_b);
                nbttaglist.func_74742_a(nbttagcompound1);
            }

            nbttagcompound.func_74782_a("Processed", nbttaglist);
        }

        public void func_143017_b(NBTTagCompound nbttagcompound) {
            super.func_143017_b(nbttagcompound);
            if (nbttagcompound.func_150297_b("Processed", 9)) {
                NBTTagList nbttaglist = nbttagcompound.func_150295_c("Processed", 10);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);

                    this.field_175791_c.add(new ChunkPos(nbttagcompound1.func_74762_e("X"), nbttagcompound1.func_74762_e("Z")));
                }
            }

        }
    }
}
