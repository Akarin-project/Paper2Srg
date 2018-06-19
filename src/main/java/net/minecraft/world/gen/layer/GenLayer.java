package net.minecraft.world.gen.layer;
import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;


public abstract class GenLayer {

    private long field_75907_b;
    protected GenLayer field_75909_a;
    private long field_75908_c;
    protected long field_75906_d;

    public static GenLayer[] func_180781_a(long i, WorldType worldtype, ChunkGeneratorSettings customworldsettingsfinal) {
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
        GenLayer genlayer = GenLayerZoom.func_75915_a(1000L, genlayerdeepocean, 0);
        int j = 4;
        int k = j;

        if (customworldsettingsfinal != null) {
            j = customworldsettingsfinal.field_177780_G;
            k = customworldsettingsfinal.field_177788_H;
        }

        if (worldtype == WorldType.field_77135_d) {
            j = 6;
        }

        GenLayer genlayer1 = GenLayerZoom.func_75915_a(1000L, genlayer, 0);
        GenLayerRiverInit genlayercleaner = new GenLayerRiverInit(100L, genlayer1);
        GenLayerBiome genlayerbiome = new GenLayerBiome(200L, genlayer, worldtype, customworldsettingsfinal);
        GenLayer genlayer2 = GenLayerZoom.func_75915_a(1000L, genlayerbiome, 2);
        GenLayerBiomeEdge genlayerdesert = new GenLayerBiomeEdge(1000L, genlayer2);
        GenLayer genlayer3 = GenLayerZoom.func_75915_a(1000L, genlayercleaner, 2);
        GenLayerHills genlayerregionhills = new GenLayerHills(1000L, genlayerdesert, genlayer3);

        genlayer1 = GenLayerZoom.func_75915_a(1000L, genlayercleaner, 2);
        genlayer1 = GenLayerZoom.func_75915_a(1000L, genlayer1, k);
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

        genlayerrivermix.func_75905_a(i);
        genlayerzoomvoronoi.func_75905_a(i);
        return new GenLayer[] { genlayerrivermix, genlayerzoomvoronoi, genlayerrivermix};
    }

    public GenLayer(long i) {
        this.field_75906_d = i;
        this.field_75906_d *= this.field_75906_d * 6364136223846793005L + 1442695040888963407L;
        this.field_75906_d += i;
        this.field_75906_d *= this.field_75906_d * 6364136223846793005L + 1442695040888963407L;
        this.field_75906_d += i;
        this.field_75906_d *= this.field_75906_d * 6364136223846793005L + 1442695040888963407L;
        this.field_75906_d += i;
    }

    public void func_75905_a(long i) {
        this.field_75907_b = i;
        if (this.field_75909_a != null) {
            this.field_75909_a.func_75905_a(i);
        }

        this.field_75907_b *= this.field_75907_b * 6364136223846793005L + 1442695040888963407L;
        this.field_75907_b += this.field_75906_d;
        this.field_75907_b *= this.field_75907_b * 6364136223846793005L + 1442695040888963407L;
        this.field_75907_b += this.field_75906_d;
        this.field_75907_b *= this.field_75907_b * 6364136223846793005L + 1442695040888963407L;
        this.field_75907_b += this.field_75906_d;
    }

    public void func_75903_a(long i, long j) {
        this.field_75908_c = this.field_75907_b;
        this.field_75908_c *= this.field_75908_c * 6364136223846793005L + 1442695040888963407L;
        this.field_75908_c += i;
        this.field_75908_c *= this.field_75908_c * 6364136223846793005L + 1442695040888963407L;
        this.field_75908_c += j;
        this.field_75908_c *= this.field_75908_c * 6364136223846793005L + 1442695040888963407L;
        this.field_75908_c += i;
        this.field_75908_c *= this.field_75908_c * 6364136223846793005L + 1442695040888963407L;
        this.field_75908_c += j;
    }

    protected int func_75902_a(int i) {
        int j = (int) ((this.field_75908_c >> 24) % (long) i);

        if (j < 0) {
            j += i;
        }

        this.field_75908_c *= this.field_75908_c * 6364136223846793005L + 1442695040888963407L;
        this.field_75908_c += this.field_75907_b;
        return j;
    }

    public abstract int[] func_75904_a(int i, int j, int k, int l);

    protected static boolean func_151616_a(int i, int j) {
        if (i == j) {
            return true;
        } else {
            Biome biomebase = Biome.func_150568_d(i);
            Biome biomebase1 = Biome.func_150568_d(j);

            return biomebase != null && biomebase1 != null ? (biomebase != Biomes.field_150607_aa && biomebase != Biomes.field_150608_ab ? biomebase == biomebase1 || biomebase.func_150562_l() == biomebase1.func_150562_l() : biomebase1 == Biomes.field_150607_aa || biomebase1 == Biomes.field_150608_ab) : false;
        }
    }

    protected static boolean func_151618_b(int i) {
        Biome biomebase = Biome.func_150568_d(i);

        return biomebase == Biomes.field_76771_b || biomebase == Biomes.field_150575_M || biomebase == Biomes.field_76776_l;
    }

    protected int func_151619_a(int... aint) {
        return aint[this.func_75902_a(aint.length)];
    }

    protected int func_151617_b(int i, int j, int k, int l) {
        return j == k && k == l ? j : (i == j && i == k ? i : (i == j && i == l ? i : (i == k && i == l ? i : (i == j && k != l ? i : (i == k && j != l ? i : (i == l && j != k ? i : (j == k && i != l ? j : (j == l && i != k ? j : (k == l && i != j ? k : this.func_151619_a(new int[] { i, j, k, l}))))))))));
    }
}
