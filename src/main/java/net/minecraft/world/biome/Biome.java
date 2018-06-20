package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class Biome {

    private static final Logger field_150586_aC = LogManager.getLogger();
    protected static final IBlockState field_185365_a = Blocks.field_150348_b.func_176223_P();
    protected static final IBlockState field_185366_b = Blocks.field_150350_a.func_176223_P();
    protected static final IBlockState field_185367_c = Blocks.field_150357_h.func_176223_P();
    protected static final IBlockState field_185368_d = Blocks.field_150351_n.func_176223_P();
    protected static final IBlockState field_185369_e = Blocks.field_180395_cM.func_176223_P();
    protected static final IBlockState field_185370_f = Blocks.field_150322_A.func_176223_P();
    protected static final IBlockState field_185371_g = Blocks.field_150432_aD.func_176223_P();
    protected static final IBlockState field_185372_h = Blocks.field_150355_j.func_176223_P();
    public static final ObjectIntIdentityMap<Biome> field_185373_j = new ObjectIntIdentityMap();
    protected static final NoiseGeneratorPerlin field_150605_ac = new NoiseGeneratorPerlin(new Random(1234L), 1);
    protected static final NoiseGeneratorPerlin field_180281_af = new NoiseGeneratorPerlin(new Random(2345L), 1);
    protected static final WorldGenDoublePlant field_180280_ag = new WorldGenDoublePlant();
    protected static final WorldGenTrees field_76757_N = new WorldGenTrees(false);
    protected static final WorldGenBigTree field_76758_O = new WorldGenBigTree(false);
    protected static final WorldGenSwamp field_76763_Q = new WorldGenSwamp();
    public static final RegistryNamespaced<ResourceLocation, Biome> field_185377_q = new RegistryNamespaced();
    private final String field_76791_y;
    private final float field_76748_D;
    private final float field_76749_E;
    private final float field_76750_F;
    private final float field_76751_G;
    private final int field_76759_H;
    private final boolean field_76766_R;
    private final boolean field_76765_S;
    @Nullable
    private final String field_185364_H;
    public IBlockState field_76752_A;
    public IBlockState field_76753_B;
    public BiomeDecorator field_76760_I;
    protected List<Biome.SpawnListEntry> field_76761_J;
    protected List<Biome.SpawnListEntry> field_76762_K;
    protected List<Biome.SpawnListEntry> field_76755_L;
    protected List<Biome.SpawnListEntry> field_82914_M;

    public static int func_185362_a(Biome biomebase) {
        return Biome.field_185377_q.func_148757_b(biomebase); // Paper - decompile fix
    }

    @Nullable
    public static Biome func_185357_a(int i) {
        return Biome.field_185377_q.func_148754_a(i);
    }

    @Nullable
    public static Biome func_185356_b(Biome biomebase) {
        return Biome.field_185373_j.func_148745_a(func_185362_a(biomebase));
    }

    protected Biome(Biome.a biomebase_a) {
        this.field_76752_A = Blocks.field_150349_c.func_176223_P();
        this.field_76753_B = Blocks.field_150346_d.func_176223_P();
        this.field_76761_J = Lists.newArrayList();
        this.field_76762_K = Lists.newArrayList();
        this.field_76755_L = Lists.newArrayList();
        this.field_82914_M = Lists.newArrayList();
        this.field_76791_y = biomebase_a.a;
        this.field_76748_D = biomebase_a.b;
        this.field_76749_E = biomebase_a.c;
        this.field_76750_F = biomebase_a.d;
        this.field_76751_G = biomebase_a.e;
        this.field_76759_H = biomebase_a.f;
        this.field_76766_R = biomebase_a.g;
        this.field_76765_S = biomebase_a.h;
        this.field_185364_H = biomebase_a.i;
        this.field_76760_I = this.func_76729_a();
        this.field_76762_K.add(new Biome.SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityCow.class, 8, 4, 4));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntityZombie.class, 95, 4, 4));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntityZombieVillager.class, 5, 1, 1));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntitySlime.class, 100, 4, 4));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntityWitch.class, 5, 1, 1));
        this.field_76755_L.add(new Biome.SpawnListEntry(EntitySquid.class, 10, 4, 4));
        this.field_82914_M.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
    }

    protected BiomeDecorator func_76729_a() {
        return new BiomeDecorator();
    }

    public boolean func_185363_b() {
        return this.field_185364_H != null;
    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return random.nextInt(10) == 0 ? Biome.field_76758_O : Biome.field_76757_N;
    }

    public WorldGenerator func_76730_b(Random random) {
        return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public BlockFlower.EnumFlowerType func_180623_a(Random random, BlockPos blockposition) {
        return random.nextInt(3) > 0 ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
    }

    public List<Biome.SpawnListEntry> func_76747_a(EnumCreatureType enumcreaturetype) {
        switch (enumcreaturetype) {
        case MONSTER:
            return this.field_76761_J;

        case CREATURE:
            return this.field_76762_K;

        case WATER_CREATURE:
            return this.field_76755_L;

        case AMBIENT:
            return this.field_82914_M;

        default:
            return Collections.emptyList();
        }
    }

    public boolean func_76746_c() {
        return this.func_150559_j();
    }

    public boolean func_76738_d() {
        return this.func_150559_j() ? false : this.field_76765_S;
    }

    public boolean func_76736_e() {
        return this.func_76727_i() > 0.85F;
    }

    public float func_76741_f() {
        return 0.1F;
    }

    public final float func_180626_a(BlockPos blockposition) {
        if (blockposition.func_177956_o() > 64) {
            float f = (float) (Biome.field_150605_ac.func_151601_a(blockposition.func_177958_n() / 8.0F, blockposition.func_177952_p() / 8.0F) * 4.0D);

            return this.func_185353_n() - (f + blockposition.func_177956_o() - 64.0F) * 0.05F / 30.0F;
        } else {
            return this.func_185353_n();
        }
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        this.field_76760_I.func_180292_a(world, random, this, blockposition);
    }

    public void func_180622_a(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        this.func_180628_b(world, random, chunksnapshot, i, j, d0);
    }

    public final void func_180628_b(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        int k = world.func_181545_F();
        IBlockState iblockdata = this.field_76752_A;
        IBlockState iblockdata1 = this.field_76753_B;
        int l = -1;
        int i1 = (int) (d0 / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        int j1 = i & 15;
        int k1 = j & 15;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (int l1 = 255; l1 >= 0; --l1) {
            if (l1 <= (world.paperConfig.generateFlatBedrock ? 0 : random.nextInt(5))) { // Paper - Configurable flat bedrock
                chunksnapshot.func_177855_a(k1, l1, j1, Biome.field_185367_c);
            } else {
                IBlockState iblockdata2 = chunksnapshot.func_177856_a(k1, l1, j1);

                if (iblockdata2.func_185904_a() == Material.field_151579_a) {
                    l = -1;
                } else if (iblockdata2.func_177230_c() == Blocks.field_150348_b) {
                    if (l == -1) {
                        if (i1 <= 0) {
                            iblockdata = Biome.field_185366_b;
                            iblockdata1 = Biome.field_185365_a;
                        } else if (l1 >= k - 4 && l1 <= k + 1) {
                            iblockdata = this.field_76752_A;
                            iblockdata1 = this.field_76753_B;
                        }

                        if (l1 < k && (iblockdata == null || iblockdata.func_185904_a() == Material.field_151579_a)) {
                            if (this.func_180626_a(blockposition_mutableblockposition.func_181079_c(i, l1, j)) < 0.15F) {
                                iblockdata = Biome.field_185371_g;
                            } else {
                                iblockdata = Biome.field_185372_h;
                            }
                        }

                        l = i1;
                        if (l1 >= k - 1) {
                            chunksnapshot.func_177855_a(k1, l1, j1, iblockdata);
                        } else if (l1 < k - 7 - i1) {
                            iblockdata = Biome.field_185366_b;
                            iblockdata1 = Biome.field_185365_a;
                            chunksnapshot.func_177855_a(k1, l1, j1, Biome.field_185368_d);
                        } else {
                            chunksnapshot.func_177855_a(k1, l1, j1, iblockdata1);
                        }
                    } else if (l > 0) {
                        --l;
                        chunksnapshot.func_177855_a(k1, l1, j1, iblockdata1);
                        if (l == 0 && iblockdata1.func_177230_c() == Blocks.field_150354_m && i1 > 1) {
                            l = random.nextInt(4) + Math.max(0, l1 - 63);
                            iblockdata1 = iblockdata1.func_177229_b(BlockSand.field_176504_a) == BlockSand.EnumType.RED_SAND ? Biome.field_185369_e : Biome.field_185370_f;
                        }
                    }
                }
            }
        }

    }

    public Class<? extends Biome> func_150562_l() {
        return this.getClass();
    }

    public Biome.TempCategory func_150561_m() {
        return this.func_185353_n() < 0.2D ? Biome.TempCategory.COLD : (this.func_185353_n() < 1.0D ? Biome.TempCategory.MEDIUM : Biome.TempCategory.WARM);
    }

    @Nullable
    public static Biome func_150568_d(int i) {
        return func_180276_a(i, (Biome) null);
    }

    public static Biome func_180276_a(int i, Biome biomebase) {
        Biome biomebase1 = func_185357_a(i);

        return biomebase1 == null ? biomebase : biomebase1;
    }

    public boolean func_185352_i() {
        return false;
    }

    public final float func_185355_j() {
        return this.field_76748_D;
    }

    public final float func_76727_i() {
        return this.field_76751_G;
    }

    public final float func_185360_m() {
        return this.field_76749_E;
    }

    public final float func_185353_n() {
        return this.field_76750_F;
    }

    public final boolean func_150559_j() {
        return this.field_76766_R;
    }

    public static void func_185358_q() {
        func_185354_a(0, "ocean", new BiomeOcean((new Biome.a("Ocean")).c(-1.0F).d(0.1F)));
        func_185354_a(1, "plains", new BiomePlains(false, (new Biome.a("Plains")).c(0.125F).d(0.05F).a(0.8F).b(0.4F)));
        func_185354_a(2, "desert", new BiomeDesert((new Biome.a("Desert")).c(0.125F).d(0.05F).a(2.0F).b(0.0F).a()));
        func_185354_a(3, "extreme_hills", new BiomeHills(BiomeHills.Type.NORMAL, (new Biome.a("Extreme Hills")).c(1.0F).d(0.5F).a(0.2F).b(0.3F)));
        func_185354_a(4, "forest", new BiomeForest(BiomeForest.Type.NORMAL, (new Biome.a("Forest")).a(0.7F).b(0.8F)));
        func_185354_a(5, "taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new Biome.a("Taiga")).c(0.2F).d(0.2F).a(0.25F).b(0.8F)));
        func_185354_a(6, "swampland", new BiomeSwamp((new Biome.a("Swampland")).c(-0.2F).d(0.1F).a(0.8F).b(0.9F).a(14745518)));
        func_185354_a(7, "river", new BiomeRiver((new Biome.a("River")).c(-0.5F).d(0.0F)));
        func_185354_a(8, "hell", new BiomeHell((new Biome.a("Hell")).a(2.0F).b(0.0F).a()));
        func_185354_a(9, "sky", new BiomeEnd((new Biome.a("The End")).a()));
        func_185354_a(10, "frozen_ocean", new BiomeOcean((new Biome.a("FrozenOcean")).c(-1.0F).d(0.1F).a(0.0F).b(0.5F).b()));
        func_185354_a(11, "frozen_river", new BiomeRiver((new Biome.a("FrozenRiver")).c(-0.5F).d(0.0F).a(0.0F).b(0.5F).b()));
        func_185354_a(12, "ice_flats", new BiomeSnow(false, (new Biome.a("Ice Plains")).c(0.125F).d(0.05F).a(0.0F).b(0.5F).b()));
        func_185354_a(13, "ice_mountains", new BiomeSnow(false, (new Biome.a("Ice Mountains")).c(0.45F).d(0.3F).a(0.0F).b(0.5F).b()));
        func_185354_a(14, "mushroom_island", new BiomeMushroomIsland((new Biome.a("MushroomIsland")).c(0.2F).d(0.3F).a(0.9F).b(1.0F)));
        func_185354_a(15, "mushroom_island_shore", new BiomeMushroomIsland((new Biome.a("MushroomIslandShore")).c(0.0F).d(0.025F).a(0.9F).b(1.0F)));
        func_185354_a(16, "beaches", new BiomeBeach((new Biome.a("Beach")).c(0.0F).d(0.025F).a(0.8F).b(0.4F)));
        func_185354_a(17, "desert_hills", new BiomeDesert((new Biome.a("DesertHills")).c(0.45F).d(0.3F).a(2.0F).b(0.0F).a()));
        func_185354_a(18, "forest_hills", new BiomeForest(BiomeForest.Type.NORMAL, (new Biome.a("ForestHills")).c(0.45F).d(0.3F).a(0.7F).b(0.8F)));
        func_185354_a(19, "taiga_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new Biome.a("TaigaHills")).a(0.25F).b(0.8F).c(0.45F).d(0.3F)));
        func_185354_a(20, "smaller_extreme_hills", new BiomeHills(BiomeHills.Type.EXTRA_TREES, (new Biome.a("Extreme Hills Edge")).c(0.8F).d(0.3F).a(0.2F).b(0.3F)));
        func_185354_a(21, "jungle", new BiomeJungle(false, (new Biome.a("Jungle")).a(0.95F).b(0.9F)));
        func_185354_a(22, "jungle_hills", new BiomeJungle(false, (new Biome.a("JungleHills")).c(0.45F).d(0.3F).a(0.95F).b(0.9F)));
        func_185354_a(23, "jungle_edge", new BiomeJungle(true, (new Biome.a("JungleEdge")).a(0.95F).b(0.8F)));
        func_185354_a(24, "deep_ocean", new BiomeOcean((new Biome.a("Deep Ocean")).c(-1.8F).d(0.1F)));
        func_185354_a(25, "stone_beach", new BiomeStoneBeach((new Biome.a("Stone Beach")).c(0.1F).d(0.8F).a(0.2F).b(0.3F)));
        func_185354_a(26, "cold_beach", new BiomeBeach((new Biome.a("Cold Beach")).c(0.0F).d(0.025F).a(0.05F).b(0.3F).b()));
        func_185354_a(27, "birch_forest", new BiomeForest(BiomeForest.Type.BIRCH, (new Biome.a("Birch Forest")).a(0.6F).b(0.6F)));
        func_185354_a(28, "birch_forest_hills", new BiomeForest(BiomeForest.Type.BIRCH, (new Biome.a("Birch Forest Hills")).c(0.45F).d(0.3F).a(0.6F).b(0.6F)));
        func_185354_a(29, "roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, (new Biome.a("Roofed Forest")).a(0.7F).b(0.8F)));
        func_185354_a(30, "taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new Biome.a("Cold Taiga")).c(0.2F).d(0.2F).a(-0.5F).b(0.4F).b()));
        func_185354_a(31, "taiga_cold_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new Biome.a("Cold Taiga Hills")).c(0.45F).d(0.3F).a(-0.5F).b(0.4F).b()));
        func_185354_a(32, "redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA, (new Biome.a("Mega Taiga")).a(0.3F).b(0.8F).c(0.2F).d(0.2F)));
        func_185354_a(33, "redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA, (new Biome.a("Mega Taiga Hills")).c(0.45F).d(0.3F).a(0.3F).b(0.8F)));
        func_185354_a(34, "extreme_hills_with_trees", new BiomeHills(BiomeHills.Type.EXTRA_TREES, (new Biome.a("Extreme Hills+")).c(1.0F).d(0.5F).a(0.2F).b(0.3F)));
        func_185354_a(35, "savanna", new BiomeSavanna((new Biome.a("Savanna")).c(0.125F).d(0.05F).a(1.2F).b(0.0F).a()));
        func_185354_a(36, "savanna_rock", new BiomeSavanna((new Biome.a("Savanna Plateau")).c(1.5F).d(0.025F).a(1.0F).b(0.0F).a()));
        func_185354_a(37, "mesa", new BiomeMesa(false, false, (new Biome.a("Mesa")).a(2.0F).b(0.0F).a()));
        func_185354_a(38, "mesa_rock", new BiomeMesa(false, true, (new Biome.a("Mesa Plateau F")).c(1.5F).d(0.025F).a(2.0F).b(0.0F).a()));
        func_185354_a(39, "mesa_clear_rock", new BiomeMesa(false, false, (new Biome.a("Mesa Plateau")).c(1.5F).d(0.025F).a(2.0F).b(0.0F).a()));
        func_185354_a(127, "void", new BiomeVoid((new Biome.a("The Void")).a()));
        func_185354_a(129, "mutated_plains", new BiomePlains(true, (new Biome.a("Sunflower Plains")).a("plains").c(0.125F).d(0.05F).a(0.8F).b(0.4F)));
        func_185354_a(130, "mutated_desert", new BiomeDesert((new Biome.a("Desert M")).a("desert").c(0.225F).d(0.25F).a(2.0F).b(0.0F).a()));
        func_185354_a(131, "mutated_extreme_hills", new BiomeHills(BiomeHills.Type.MUTATED, (new Biome.a("Extreme Hills M")).a("extreme_hills").c(1.0F).d(0.5F).a(0.2F).b(0.3F)));
        func_185354_a(132, "mutated_forest", new BiomeForest(BiomeForest.Type.FLOWER, (new Biome.a("Flower Forest")).a("forest").d(0.4F).a(0.7F).b(0.8F)));
        func_185354_a(133, "mutated_taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new Biome.a("Taiga M")).a("taiga").c(0.3F).d(0.4F).a(0.25F).b(0.8F)));
        func_185354_a(134, "mutated_swampland", new BiomeSwamp((new Biome.a("Swampland M")).a("swampland").c(-0.1F).d(0.3F).a(0.8F).b(0.9F).a(14745518)));
        func_185354_a(140, "mutated_ice_flats", new BiomeSnow(true, (new Biome.a("Ice Plains Spikes")).a("ice_flats").c(0.425F).d(0.45000002F).a(0.0F).b(0.5F).b()));
        func_185354_a(149, "mutated_jungle", new BiomeJungle(false, (new Biome.a("Jungle M")).a("jungle").c(0.2F).d(0.4F).a(0.95F).b(0.9F)));
        func_185354_a(151, "mutated_jungle_edge", new BiomeJungle(true, (new Biome.a("JungleEdge M")).a("jungle_edge").c(0.2F).d(0.4F).a(0.95F).b(0.8F)));
        func_185354_a(155, "mutated_birch_forest", new BiomeForestMutated((new Biome.a("Birch Forest M")).a("birch_forest").c(0.2F).d(0.4F).a(0.6F).b(0.6F)));
        func_185354_a(156, "mutated_birch_forest_hills", new BiomeForestMutated((new Biome.a("Birch Forest Hills M")).a("birch_forest_hills").c(0.55F).d(0.5F).a(0.6F).b(0.6F)));
        func_185354_a(157, "mutated_roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, (new Biome.a("Roofed Forest M")).a("roofed_forest").c(0.2F).d(0.4F).a(0.7F).b(0.8F)));
        func_185354_a(158, "mutated_taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new Biome.a("Cold Taiga M")).a("taiga_cold").c(0.3F).d(0.4F).a(-0.5F).b(0.4F).b()));
        func_185354_a(160, "mutated_redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, (new Biome.a("Mega Spruce Taiga")).a("redwood_taiga").c(0.2F).d(0.2F).a(0.25F).b(0.8F)));
        func_185354_a(161, "mutated_redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, (new Biome.a("Redwood Taiga Hills M")).a("redwood_taiga_hills").c(0.2F).d(0.2F).a(0.25F).b(0.8F)));
        func_185354_a(162, "mutated_extreme_hills_with_trees", new BiomeHills(BiomeHills.Type.MUTATED, (new Biome.a("Extreme Hills+ M")).a("extreme_hills_with_trees").c(1.0F).d(0.5F).a(0.2F).b(0.3F)));
        func_185354_a(163, "mutated_savanna", new BiomeSavannaMutated((new Biome.a("Savanna M")).a("savanna").c(0.3625F).d(1.225F).a(1.1F).b(0.0F).a()));
        func_185354_a(164, "mutated_savanna_rock", new BiomeSavannaMutated((new Biome.a("Savanna Plateau M")).a("savanna_rock").c(1.05F).d(1.2125001F).a(1.0F).b(0.0F).a()));
        func_185354_a(165, "mutated_mesa", new BiomeMesa(true, false, (new Biome.a("Mesa (Bryce)")).a("mesa").a(2.0F).b(0.0F).a()));
        func_185354_a(166, "mutated_mesa_rock", new BiomeMesa(false, true, (new Biome.a("Mesa Plateau F M")).a("mesa_rock").c(0.45F).d(0.3F).a(2.0F).b(0.0F).a()));
        func_185354_a(167, "mutated_mesa_clear_rock", new BiomeMesa(false, false, (new Biome.a("Mesa Plateau M")).a("mesa_clear_rock").c(0.45F).d(0.3F).a(2.0F).b(0.0F).a()));
    }

    private static void func_185354_a(int i, String s, Biome biomebase) {
        Biome.field_185377_q.func_177775_a(i, new ResourceLocation(s), biomebase);
        if (biomebase.func_185363_b()) {
            Biome.field_185373_j.func_148746_a(biomebase, func_185362_a(Biome.field_185377_q.func_82594_a(new ResourceLocation(biomebase.field_185364_H))));
        }

    }

    public static class a {

        private final String a;
        private float b = 0.1F;
        private float c = 0.2F;
        private float d = 0.5F;
        private float e = 0.5F;
        private int f = 16777215;
        private boolean g;
        private boolean h = true;
        @Nullable
        private String i;

        public a(String s) {
            this.a = s;
        }

        protected Biome.a a(float f) {
            if (f > 0.1F && f < 0.2F) {
                throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
            } else {
                this.d = f;
                return this;
            }
        }

        protected Biome.a b(float f) {
            this.e = f;
            return this;
        }

        protected Biome.a c(float f) {
            this.b = f;
            return this;
        }

        protected Biome.a d(float f) {
            this.c = f;
            return this;
        }

        protected Biome.a a() {
            this.h = false;
            return this;
        }

        protected Biome.a b() {
            this.g = true;
            return this;
        }

        protected Biome.a a(int i) {
            this.f = i;
            return this;
        }

        protected Biome.a a(String s) {
            this.i = s;
            return this;
        }
    }

    public static class SpawnListEntry extends WeightedRandom.Item {

        public Class<? extends EntityLiving> field_76300_b;
        public int field_76301_c;
        public int field_76299_d;

        public SpawnListEntry(Class<? extends EntityLiving> oclass, int i, int j, int k) {
            super(i);
            this.field_76300_b = oclass;
            this.field_76301_c = j;
            this.field_76299_d = k;
        }

        @Override
        public String toString() {
            return this.field_76300_b.getSimpleName() + "*(" + this.field_76301_c + "-" + this.field_76299_d + "):" + this.field_76292_a;
        }
    }

    public static enum TempCategory {

        OCEAN, COLD, MEDIUM, WARM;

        private TempCategory() {}
    }
}
