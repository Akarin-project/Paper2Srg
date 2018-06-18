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

    private int spacing;
    private int separation;
    public static final List<Biome> WATER_BIOMES = Arrays.asList(new Biome[] { Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER});
    public static final List<Biome> SPAWN_BIOMES = Arrays.asList(new Biome[] { Biomes.DEEP_OCEAN});
    private static final List<Biome.SpawnListEntry> MONUMENT_ENEMIES = Lists.newArrayList();

    public StructureOceanMonument() {
        this.spacing = 32;
        this.separation = 5;
    }

    public StructureOceanMonument(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("spacing")) {
                this.spacing = MathHelper.getInt((String) entry.getValue(), this.spacing, 1);
            } else if (((String) entry.getKey()).equals("separation")) {
                this.separation = MathHelper.getInt((String) entry.getValue(), this.separation, 1);
            }
        }

    }

    public String getStructureName() {
        return "Monument";
    }

    protected boolean canSpawnStructureAtCoords(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            i -= this.spacing - 1;
        }

        if (j < 0) {
            j -= this.spacing - 1;
        }

        int i1 = i / this.spacing;
        int j1 = j / this.spacing;
        Random random = this.world.setRandomSeed(i1, j1, this.world.spigotConfig.monumentSeed); // Spigot

        i1 *= this.spacing;
        j1 *= this.spacing;
        i1 += (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        j1 += (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        if (k == i1 && l == j1) {
            if (!this.world.getBiomeProvider().areBiomesViable(k * 16 + 8, l * 16 + 8, 16, StructureOceanMonument.SPAWN_BIOMES)) {
                return false;
            }

            boolean flag = this.world.getBiomeProvider().areBiomesViable(k * 16 + 8, l * 16 + 8, 29, StructureOceanMonument.WATER_BIOMES);

            if (flag) {
                return true;
            }
        }

        return false;
    }

    public BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag) {
        this.world = world;
        return findNearestStructurePosBySpacing(world, this, blockposition, this.spacing, this.separation, this.world.spigotConfig.monumentSeed, true, 100, flag); // Spigot
    }

    protected StructureStart getStructureStart(int i, int j) {
        return new StructureOceanMonument.StartMonument(this.world, this.rand, i, j);
    }

    public List<Biome.SpawnListEntry> getMonsters() {
        return StructureOceanMonument.MONUMENT_ENEMIES;
    }

    static {
        StructureOceanMonument.MONUMENT_ENEMIES.add(new Biome.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }

    public static class StartMonument extends StructureStart {

        private final Set<ChunkPos> processed = Sets.newHashSet();
        private boolean wasCreated;

        public StartMonument() {}

        public StartMonument(World world, Random random, int i, int j) {
            super(i, j);
            this.create(world, random, i, j);
        }

        private void create(World world, Random random, int i, int j) {
            random.setSeed(world.getSeed());
            long k = random.nextLong();
            long l = random.nextLong();
            long i1 = (long) i * k;
            long j1 = (long) j * l;

            random.setSeed(i1 ^ j1 ^ world.getSeed());
            int k1 = i * 16 + 8 - 29;
            int l1 = j * 16 + 8 - 29;
            EnumFacing enumdirection = EnumFacing.Plane.HORIZONTAL.random(random);

            this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(random, k1, l1, enumdirection));
            this.updateBoundingBox();
            this.wasCreated = true;
        }

        public void generateStructure(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (!this.wasCreated) {
                this.components.clear();
                this.create(world, random, this.getChunkPosX(), this.getChunkPosZ());
            }

            super.generateStructure(world, random, structureboundingbox);
        }

        public boolean isValidForPostProcess(ChunkPos chunkcoordintpair) {
            return this.processed.contains(chunkcoordintpair) ? false : super.isValidForPostProcess(chunkcoordintpair);
        }

        public void notifyPostProcessAt(ChunkPos chunkcoordintpair) {
            super.notifyPostProcessAt(chunkcoordintpair);
            this.processed.add(chunkcoordintpair);
        }

        public void writeToNBT(NBTTagCompound nbttagcompound) {
            super.writeToNBT(nbttagcompound);
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.processed.iterator();

            while (iterator.hasNext()) {
                ChunkPos chunkcoordintpair = (ChunkPos) iterator.next();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setInteger("X", chunkcoordintpair.x);
                nbttagcompound1.setInteger("Z", chunkcoordintpair.z);
                nbttaglist.appendTag(nbttagcompound1);
            }

            nbttagcompound.setTag("Processed", nbttaglist);
        }

        public void readFromNBT(NBTTagCompound nbttagcompound) {
            super.readFromNBT(nbttagcompound);
            if (nbttagcompound.hasKey("Processed", 9)) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("Processed", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);

                    this.processed.add(new ChunkPos(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Z")));
                }
            }

        }
    }
}
