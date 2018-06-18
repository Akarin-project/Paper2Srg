package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;


public abstract class GenLayer {

    private long worldGenSeed;
    protected GenLayer parent;
    private long chunkSeed;
    protected long baseSeed;

    public static GenLayer[] initializeAllBiomeGenerators(long i, WorldType worldtype, ChunkGeneratorSettings customworldsettingsfinal) {
        GenLayerIsland layerisland = new GenLayerIsland(1L);
        GenLayerFuzzyZoom genlayerzoomfuzzy = new GenLayerFuzzyZoom(2000L, layerisland);
        GenLayerAddIsland genlayerisland = new GenLayerAddIsland(1L, genlayerzoomfuzzy);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayerisland);

        genlayerisland = new GenLayerAddIsland(2L, genlayerzoom);
        genlayerisland = new GenLayerAddIsland(50L, genlayerisland);
        genlayerisland = new GenLayerAddIsland(70L, genlayerisland);
        GenLayerRemoveTooMuchOcean genlayericeplains = new GenLayerRemoveTooMuchOcean(2L, genlayerisland);
        GenLayerAddSnow genlayertopsoil = new GenLayerAddSnow(2L, genlayericeplains);

        genlayerisland = new GenLayerAddIsland(3L, genlayertopsoil);
        GenLayerEdge genlayerspecial = new GenLayerEdge(2L, genlayerisland, GenLayerEdge.Mode.COOL_WARM);

        genlayerspecial = new GenLayerEdge(2L, genlayerspecial, GenLayerEdge.Mode.HEAT_ICE);
        genlayerspecial = new GenLayerEdge(3L, genlayerspecial, GenLayerEdge.Mode.SPECIAL);
        genlayerzoom = new GenLayerZoom(2002L, genlayerspecial);
        genlayerzoom = new GenLayerZoom(2003L, genlayerzoom);
        genlayerisland = new GenLayerAddIsland(4L, genlayerzoom);
        GenLayerAddMushroomIsland genlayermushroomisland = new GenLayerAddMushroomIsland(5L, genlayerisland);
        GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayermushroomisland);
        GenLayer genlayer = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
        int j = 4;
        int k = j;

        if (customworldsettingsfinal != null) {
            j = customworldsettingsfinal.biomeSize;
            k = customworldsettingsfinal.riverSize;
        }

        if (worldtype == WorldType.LARGE_BIOMES) {
            j = 6;
        }

        GenLayer genlayer1 = GenLayerZoom.magnify(1000L, genlayer, 0);
        GenLayerRiverInit genlayercleaner = new GenLayerRiverInit(100L, genlayer1);
        GenLayerBiome genlayerbiome = new GenLayerBiome(200L, genlayer, worldtype, customworldsettingsfinal);
        GenLayer genlayer2 = GenLayerZoom.magnify(1000L, genlayerbiome, 2);
        GenLayerBiomeEdge genlayerdesert = new GenLayerBiomeEdge(1000L, genlayer2);
        GenLayer genlayer3 = GenLayerZoom.magnify(1000L, genlayercleaner, 2);
        GenLayerHills genlayerregionhills = new GenLayerHills(1000L, genlayerdesert, genlayer3);

        genlayer1 = GenLayerZoom.magnify(1000L, genlayercleaner, 2);
        genlayer1 = GenLayerZoom.magnify(1000L, genlayer1, k);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer1);
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        Object object = new GenLayerRareBiome(1001L, genlayerregionhills);

        for (int l = 0; l < j; ++l) {
            object = new GenLayerZoom((long) (1000 + l), (GenLayer) object);
            if (l == 0) {
                object = new GenLayerAddIsland(3L, (GenLayer) object);
            }

            if (l == 1 || j == 1) {
                object = new GenLayerShore(1000L, (GenLayer) object);
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, (GenLayer) object);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerVoronoiZoom genlayerzoomvoronoi = new GenLayerVoronoiZoom(10L, genlayerrivermix);

        genlayerrivermix.initWorldGenSeed(i);
        genlayerzoomvoronoi.initWorldGenSeed(i);
        return new GenLayer[] { genlayerrivermix, genlayerzoomvoronoi, genlayerrivermix};
    }

    public GenLayer(long i) {
        this.baseSeed = i;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += i;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += i;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += i;
    }

    public void initWorldGenSeed(long i) {
        this.worldGenSeed = i;
        if (this.parent != null) {
            this.parent.initWorldGenSeed(i);
        }

        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
    }

    public void initChunkSeed(long i, long j) {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += i;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += j;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += i;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += j;
    }

    protected int nextInt(int i) {
        int j = (int) ((this.chunkSeed >> 24) % (long) i);

        if (j < 0) {
            j += i;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return j;
    }

    public abstract int[] getInts(int i, int j, int k, int l);

    protected static boolean biomesEqualOrMesaPlateau(int i, int j) {
        if (i == j) {
            return true;
        } else {
            Biome biomebase = Biome.getBiome(i);
            Biome biomebase1 = Biome.getBiome(j);

            return biomebase != null && biomebase1 != null ? (biomebase != Biomes.MESA_ROCK && biomebase != Biomes.MESA_CLEAR_ROCK ? biomebase == biomebase1 || biomebase.getBiomeClass() == biomebase1.getBiomeClass() : biomebase1 == Biomes.MESA_ROCK || biomebase1 == Biomes.MESA_CLEAR_ROCK) : false;
        }
    }

    protected static boolean isBiomeOceanic(int i) {
        Biome biomebase = Biome.getBiome(i);

        return biomebase == Biomes.OCEAN || biomebase == Biomes.DEEP_OCEAN || biomebase == Biomes.FROZEN_OCEAN;
    }

    protected int selectRandom(int... aint) {
        return aint[this.nextInt(aint.length)];
    }

    protected int selectModeOrRandom(int i, int j, int k, int l) {
        return j == k && k == l ? j : (i == j && i == k ? i : (i == j && i == l ? i : (i == k && i == l ? i : (i == j && k != l ? i : (i == k && j != l ? i : (i == l && j != k ? i : (j == k && i != l ? j : (j == l && i != k ? j : (k == l && i != j ? k : this.selectRandom(new int[] { i, j, k, l}))))))))));
    }
}
