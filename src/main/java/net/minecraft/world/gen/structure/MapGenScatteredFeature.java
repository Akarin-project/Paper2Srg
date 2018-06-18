package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Biomes;
import net.minecraft.server.WorldGenRegistration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenScatteredFeature extends MapGenStructure {

    private static final List<Biome> BIOMELIST = Arrays.asList(new Biome[] { Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.SWAMPLAND, Biomes.ICE_PLAINS, Biomes.COLD_TAIGA});
    private final List<Biome.SpawnListEntry> monsters;
    private int maxDistanceBetweenScatteredFeatures;
    private final int minDistanceBetweenScatteredFeatures;

    public MapGenScatteredFeature() {
        this.monsters = Lists.newArrayList();
        this.maxDistanceBetweenScatteredFeatures = 32;
        this.minDistanceBetweenScatteredFeatures = 8;
        this.monsters.add(new Biome.SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    public MapGenScatteredFeature(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("distance")) {
                this.maxDistanceBetweenScatteredFeatures = MathHelper.getInt((String) entry.getValue(), this.maxDistanceBetweenScatteredFeatures, 9);
            }
        }

    }

    @Override
    public String getStructureName() {
        return "Temple";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            i -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        if (j < 0) {
            j -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        int i1 = i / this.maxDistanceBetweenScatteredFeatures;
        int j1 = j / this.maxDistanceBetweenScatteredFeatures;
        Random random = this.world.setRandomSeed(i1, j1, this.world.spigotConfig.largeFeatureSeed); // Spigot

        i1 *= this.maxDistanceBetweenScatteredFeatures;
        j1 *= this.maxDistanceBetweenScatteredFeatures;
        i1 += random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);
        j1 += random.nextInt(this.maxDistanceBetweenScatteredFeatures - 8);
        if (k == i1 && l == j1) {
            Biome biomebase = this.world.getBiomeProvider().getBiome(new BlockPos(k * 16 + 8, 0, l * 16 + 8));

            if (biomebase == null) {
                return false;
            }

            Iterator iterator = MapGenScatteredFeature.BIOMELIST.iterator();

            while (iterator.hasNext()) {
                Biome biomebase1 = (Biome) iterator.next();

                if (biomebase == biomebase1) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag) {
        this.world = world;
        return findNearestStructurePosBySpacing(world, this, blockposition, this.maxDistanceBetweenScatteredFeatures, 8, this.world.spigotConfig.largeFeatureSeed, false, 100, flag); // Spigot
    }

    @Override
    protected StructureStart getStructureStart(int i, int j) {
        return new MapGenScatteredFeature.Start(this.world, this.rand, i, j);
    }

    public boolean isSwampHut(BlockPos blockposition) {
        StructureStart structurestart = this.getStructureAt(blockposition);

        if (structurestart != null && structurestart instanceof MapGenScatteredFeature.Start && !structurestart.components.isEmpty()) {
            StructureComponent structurepiece = structurestart.components.get(0);

            return structurepiece instanceof ComponentScatteredFeaturePieces.SwampHut;
        } else {
            return false;
        }
    }

    public List<Biome.SpawnListEntry> getMonsters() {
        return this.monsters;
    }

    public static class Start extends StructureStart {

        public Start() {}

        public Start(World world, Random random, int i, int j) {
            this(world, random, i, j, world.getBiome(new BlockPos(i * 16 + 8, 0, j * 16 + 8)));
        }

        public Start(World world, Random random, int i, int j, Biome biomebase) {
            super(i, j);
            if (biomebase != Biomes.JUNGLE && biomebase != Biomes.JUNGLE_HILLS) {
                if (biomebase == Biomes.SWAMPLAND) {
                    ComponentScatteredFeaturePieces.SwampHut worldgenregistration_worldgenwitchhut = new ComponentScatteredFeaturePieces.SwampHut(random, i * 16, j * 16);

                    this.components.add(worldgenregistration_worldgenwitchhut);
                } else if (biomebase != Biomes.DESERT && biomebase != Biomes.DESERT_HILLS) {
                    if (biomebase == Biomes.ICE_PLAINS || biomebase == Biomes.COLD_TAIGA) {
                        WorldGenRegistration.b worldgenregistration_b = new WorldGenRegistration.b(random, i * 16, j * 16);

                        this.components.add(worldgenregistration_b);
                    }
                } else {
                    ComponentScatteredFeaturePieces.DesertPyramid worldgenregistration_worldgenpyramidpiece = new ComponentScatteredFeaturePieces.DesertPyramid(random, i * 16, j * 16);

                    this.components.add(worldgenregistration_worldgenpyramidpiece);
                }
            } else {
                ComponentScatteredFeaturePieces.JunglePyramid worldgenregistration_worldgenjungletemple = new ComponentScatteredFeaturePieces.JunglePyramid(random, i * 16, j * 16);

                this.components.add(worldgenregistration_worldgenjungletemple);
            }

            this.updateBoundingBox();
        }
    }
}
