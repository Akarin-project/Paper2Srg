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

    public static final List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.asList(new Biome[] { Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA});
    private int size;
    private int distance;
    private final int minTownSeparation;

    public MapGenVillage() {
        this.distance = 32;
        this.minTownSeparation = 8;
    }

    public MapGenVillage(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("size")) {
                this.size = MathHelper.getInt((String) entry.getValue(), this.size, 0);
            } else if (((String) entry.getKey()).equals("distance")) {
                this.distance = MathHelper.getInt((String) entry.getValue(), this.distance, 9);
            }
        }

    }

    public String getStructureName() {
        return "Village";
    }

    protected boolean canSpawnStructureAtCoords(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            i -= this.distance - 1;
        }

        if (j < 0) {
            j -= this.distance - 1;
        }

        int i1 = i / this.distance;
        int j1 = j / this.distance;
        Random random = this.world.setRandomSeed(i1, j1, this.world.spigotConfig.villageSeed); // Spigot

        i1 *= this.distance;
        j1 *= this.distance;
        i1 += random.nextInt(this.distance - 8);
        j1 += random.nextInt(this.distance - 8);
        if (k == i1 && l == j1) {
            boolean flag = this.world.getBiomeProvider().areBiomesViable(k * 16 + 8, l * 16 + 8, 0, MapGenVillage.VILLAGE_SPAWN_BIOMES);

            if (flag) {
                return true;
            }
        }

        return false;
    }

    public BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag) {
        this.world = world;
        return findNearestStructurePosBySpacing(world, this, blockposition, this.distance, 8, 10387312, false, 100, flag);
    }

    protected StructureStart getStructureStart(int i, int j) {
        return new MapGenVillage.Start(this.world, this.rand, i, j, this.size);
    }

    public static class Start extends StructureStart {

        private boolean hasMoreThanTwoComponents;

        public Start() {}

        public Start(World world, Random random, int i, int j, int k) {
            super(i, j);
            List list = StructureVillagePieces.getStructureVillageWeightedPieceList(random, k);
            StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece = new StructureVillagePieces.Start(world.getBiomeProvider(), 0, random, (i << 4) + 2, (j << 4) + 2, list, k);

            this.components.add(worldgenvillagepieces_worldgenvillagestartpiece);
            worldgenvillagepieces_worldgenvillagestartpiece.buildComponent((StructureComponent) worldgenvillagepieces_worldgenvillagestartpiece, this.components, random);
            List list1 = worldgenvillagepieces_worldgenvillagestartpiece.pendingRoads;
            List list2 = worldgenvillagepieces_worldgenvillagestartpiece.pendingHouses;

            int l;

            while (!list1.isEmpty() || !list2.isEmpty()) {
                StructureComponent structurepiece;

                if (list1.isEmpty()) {
                    l = random.nextInt(list2.size());
                    structurepiece = (StructureComponent) list2.remove(l);
                    structurepiece.buildComponent((StructureComponent) worldgenvillagepieces_worldgenvillagestartpiece, this.components, random);
                } else {
                    l = random.nextInt(list1.size());
                    structurepiece = (StructureComponent) list1.remove(l);
                    structurepiece.buildComponent((StructureComponent) worldgenvillagepieces_worldgenvillagestartpiece, this.components, random);
                }
            }

            this.updateBoundingBox();
            l = 0;
            Iterator iterator = this.components.iterator();

            while (iterator.hasNext()) {
                StructureComponent structurepiece1 = (StructureComponent) iterator.next();

                if (!(structurepiece1 instanceof StructureVillagePieces.Road)) {
                    ++l;
                }
            }

            this.hasMoreThanTwoComponents = l > 2;
        }

        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }

        public void writeToNBT(NBTTagCompound nbttagcompound) {
            super.writeToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }

        public void readFromNBT(NBTTagCompound nbttagcompound) {
            super.readFromNBT(nbttagcompound);
            this.hasMoreThanTwoComponents = nbttagcompound.getBoolean("Valid");
        }
    }
}
