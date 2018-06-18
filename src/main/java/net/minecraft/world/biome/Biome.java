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
import net.minecraft.server.BiomeBase.a;
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

    private static final Logger LOGGER = LogManager.getLogger();
    protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    protected static final IBlockState RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
    protected static final IBlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    protected static final IBlockState ICE = Blocks.ICE.getDefaultState();
    protected static final IBlockState WATER = Blocks.WATER.getDefaultState();
    public static final ObjectIntIdentityMap<Biome> MUTATION_TO_BASE_ID_MAP = new ObjectIntIdentityMap();
    protected static final NoiseGeneratorPerlin TEMPERATURE_NOISE = new NoiseGeneratorPerlin(new Random(1234L), 1);
    protected static final NoiseGeneratorPerlin GRASS_COLOR_NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);
    protected static final WorldGenDoublePlant DOUBLE_PLANT_GENERATOR = new WorldGenDoublePlant();
    protected static final WorldGenTrees TREE_FEATURE = new WorldGenTrees(false);
    protected static final WorldGenBigTree BIG_TREE_FEATURE = new WorldGenBigTree(false);
    protected static final WorldGenSwamp SWAMP_FEATURE = new WorldGenSwamp();
    public static final RegistryNamespaced<ResourceLocation, Biome> REGISTRY = new RegistryNamespaced();
    private final String biomeName;
    private final float baseHeight;
    private final float heightVariation;
    private final float temperature;
    private final float rainfall;
    private final int waterColor;
    private final boolean enableSnow;
    private final boolean enableRain;
    @Nullable
    private final String baseBiomeRegName;
    public IBlockState topBlock;
    public IBlockState fillerBlock;
    public BiomeDecorator decorator;
    protected List<Biome.SpawnListEntry> spawnableMonsterList;
    protected List<Biome.SpawnListEntry> spawnableCreatureList;
    protected List<Biome.SpawnListEntry> spawnableWaterCreatureList;
    protected List<Biome.SpawnListEntry> spawnableCaveCreatureList;

    public static int getIdForBiome(Biome biomebase) {
        return Biome.REGISTRY.getIDForObject(biomebase); // Paper - decompile fix
    }

    @Nullable
    public static Biome getBiomeForId(int i) {
        return (Biome) Biome.REGISTRY.getObjectById(i);
    }

    @Nullable
    public static Biome getMutationForBiome(Biome biomebase) {
        return (Biome) Biome.MUTATION_TO_BASE_ID_MAP.getByValue(getIdForBiome(biomebase));
    }

    protected Biome(BiomeBase.a biomebase_a) {
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.spawnableMonsterList = Lists.newArrayList();
        this.spawnableCreatureList = Lists.newArrayList();
        this.spawnableWaterCreatureList = Lists.newArrayList();
        this.spawnableCaveCreatureList = Lists.newArrayList();
        this.biomeName = biomebase_a.a;
        this.baseHeight = biomebase_a.b;
        this.heightVariation = biomebase_a.c;
        this.temperature = biomebase_a.d;
        this.rainfall = biomebase_a.e;
        this.waterColor = biomebase_a.f;
        this.enableSnow = biomebase_a.g;
        this.enableRain = biomebase_a.h;
        this.baseBiomeRegName = biomebase_a.i;
        this.decorator = this.createBiomeDecorator();
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityCow.class, 8, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombie.class, 95, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombieVillager.class, 5, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySlime.class, 100, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityWitch.class, 5, 1, 1));
        this.spawnableWaterCreatureList.add(new Biome.SpawnListEntry(EntitySquid.class, 10, 4, 4));
        this.spawnableCaveCreatureList.add(new Biome.SpawnListEntry(EntityBat.class, 10, 8, 8));
    }

    protected BiomeDecorator createBiomeDecorator() {
        return new BiomeDecorator();
    }

    public boolean isMutation() {
        return this.baseBiomeRegName != null;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(10) == 0 ? Biome.BIG_TREE_FEATURE : Biome.TREE_FEATURE);
    }

    public WorldGenerator getRandomWorldGenForGrass(Random random) {
        return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random random, BlockPos blockposition) {
        return random.nextInt(3) > 0 ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
    }

    public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType enumcreaturetype) {
        switch (enumcreaturetype) {
        case MONSTER:
            return this.spawnableMonsterList;

        case CREATURE:
            return this.spawnableCreatureList;

        case WATER_CREATURE:
            return this.spawnableWaterCreatureList;

        case AMBIENT:
            return this.spawnableCaveCreatureList;

        default:
            return Collections.emptyList();
        }
    }

    public boolean getEnableSnow() {
        return this.isSnowyBiome();
    }

    public boolean canRain() {
        return this.isSnowyBiome() ? false : this.enableRain;
    }

    public boolean isHighHumidity() {
        return this.getRainfall() > 0.85F;
    }

    public float getSpawningChance() {
        return 0.1F;
    }

    public final float getTemperature(BlockPos blockposition) {
        if (blockposition.getY() > 64) {
            float f = (float) (Biome.TEMPERATURE_NOISE.getValue((double) ((float) blockposition.getX() / 8.0F), (double) ((float) blockposition.getZ() / 8.0F)) * 4.0D);

            return this.getDefaultTemperature() - (f + (float) blockposition.getY() - 64.0F) * 0.05F / 30.0F;
        } else {
            return this.getDefaultTemperature();
        }
    }

    public void decorate(World world, Random random, BlockPos blockposition) {
        this.decorator.decorate(world, random, this, blockposition);
    }

    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        this.generateBiomeTerrain(world, random, chunksnapshot, i, j, d0);
    }

    public final void generateBiomeTerrain(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        int k = world.getSeaLevel();
        IBlockState iblockdata = this.topBlock;
        IBlockState iblockdata1 = this.fillerBlock;
        int l = -1;
        int i1 = (int) (d0 / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        int j1 = i & 15;
        int k1 = j & 15;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (int l1 = 255; l1 >= 0; --l1) {
            if (l1 <= (world.paperConfig.generateFlatBedrock ? 0 : random.nextInt(5))) { // Paper - Configurable flat bedrock
                chunksnapshot.setBlockState(k1, l1, j1, Biome.BEDROCK);
            } else {
                IBlockState iblockdata2 = chunksnapshot.getBlockState(k1, l1, j1);

                if (iblockdata2.getMaterial() == Material.AIR) {
                    l = -1;
                } else if (iblockdata2.getBlock() == Blocks.STONE) {
                    if (l == -1) {
                        if (i1 <= 0) {
                            iblockdata = Biome.AIR;
                            iblockdata1 = Biome.STONE;
                        } else if (l1 >= k - 4 && l1 <= k + 1) {
                            iblockdata = this.topBlock;
                            iblockdata1 = this.fillerBlock;
                        }

                        if (l1 < k && (iblockdata == null || iblockdata.getMaterial() == Material.AIR)) {
                            if (this.getTemperature((BlockPos) blockposition_mutableblockposition.setPos(i, l1, j)) < 0.15F) {
                                iblockdata = Biome.ICE;
                            } else {
                                iblockdata = Biome.WATER;
                            }
                        }

                        l = i1;
                        if (l1 >= k - 1) {
                            chunksnapshot.setBlockState(k1, l1, j1, iblockdata);
                        } else if (l1 < k - 7 - i1) {
                            iblockdata = Biome.AIR;
                            iblockdata1 = Biome.STONE;
                            chunksnapshot.setBlockState(k1, l1, j1, Biome.GRAVEL);
                        } else {
                            chunksnapshot.setBlockState(k1, l1, j1, iblockdata1);
                        }
                    } else if (l > 0) {
                        --l;
                        chunksnapshot.setBlockState(k1, l1, j1, iblockdata1);
                        if (l == 0 && iblockdata1.getBlock() == Blocks.SAND && i1 > 1) {
                            l = random.nextInt(4) + Math.max(0, l1 - 63);
                            iblockdata1 = iblockdata1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Biome.RED_SANDSTONE : Biome.SANDSTONE;
                        }
                    }
                }
            }
        }

    }

    public Class<? extends Biome> getBiomeClass() {
        return this.getClass();
    }

    public Biome.TempCategory getTempCategory() {
        return (double) this.getDefaultTemperature() < 0.2D ? Biome.TempCategory.COLD : ((double) this.getDefaultTemperature() < 1.0D ? Biome.TempCategory.MEDIUM : Biome.TempCategory.WARM);
    }

    @Nullable
    public static Biome getBiome(int i) {
        return getBiome(i, (Biome) null);
    }

    public static Biome getBiome(int i, Biome biomebase) {
        Biome biomebase1 = getBiomeForId(i);

        return biomebase1 == null ? biomebase : biomebase1;
    }

    public boolean ignorePlayerSpawnSuitability() {
        return false;
    }

    public final float getBaseHeight() {
        return this.baseHeight;
    }

    public final float getRainfall() {
        return this.rainfall;
    }

    public final float getHeightVariation() {
        return this.heightVariation;
    }

    public final float getDefaultTemperature() {
        return this.temperature;
    }

    public final boolean isSnowyBiome() {
        return this.enableSnow;
    }

    public static void registerBiomes() {
        registerBiome(0, "ocean", new BiomeOcean((new BiomeBase.a("Ocean")).c(-1.0F).d(0.1F)));
        registerBiome(1, "plains", new BiomePlains(false, (new BiomeBase.a("Plains")).c(0.125F).d(0.05F).a(0.8F).b(0.4F)));
        registerBiome(2, "desert", new BiomeDesert((new BiomeBase.a("Desert")).c(0.125F).d(0.05F).a(2.0F).b(0.0F).a()));
        registerBiome(3, "extreme_hills", new BiomeHills(BiomeHills.Type.NORMAL, (new BiomeBase.a("Extreme Hills")).c(1.0F).d(0.5F).a(0.2F).b(0.3F)));
        registerBiome(4, "forest", new BiomeForest(BiomeForest.Type.NORMAL, (new BiomeBase.a("Forest")).a(0.7F).b(0.8F)));
        registerBiome(5, "taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new BiomeBase.a("Taiga")).c(0.2F).d(0.2F).a(0.25F).b(0.8F)));
        registerBiome(6, "swampland", new BiomeSwamp((new BiomeBase.a("Swampland")).c(-0.2F).d(0.1F).a(0.8F).b(0.9F).a(14745518)));
        registerBiome(7, "river", new BiomeRiver((new BiomeBase.a("River")).c(-0.5F).d(0.0F)));
        registerBiome(8, "hell", new BiomeHell((new BiomeBase.a("Hell")).a(2.0F).b(0.0F).a()));
        registerBiome(9, "sky", new BiomeEnd((new BiomeBase.a("The End")).a()));
        registerBiome(10, "frozen_ocean", new BiomeOcean((new BiomeBase.a("FrozenOcean")).c(-1.0F).d(0.1F).a(0.0F).b(0.5F).b()));
        registerBiome(11, "frozen_river", new BiomeRiver((new BiomeBase.a("FrozenRiver")).c(-0.5F).d(0.0F).a(0.0F).b(0.5F).b()));
        registerBiome(12, "ice_flats", new BiomeSnow(false, (new BiomeBase.a("Ice Plains")).c(0.125F).d(0.05F).a(0.0F).b(0.5F).b()));
        registerBiome(13, "ice_mountains", new BiomeSnow(false, (new BiomeBase.a("Ice Mountains")).c(0.45F).d(0.3F).a(0.0F).b(0.5F).b()));
        registerBiome(14, "mushroom_island", new BiomeMushroomIsland((new BiomeBase.a("MushroomIsland")).c(0.2F).d(0.3F).a(0.9F).b(1.0F)));
        registerBiome(15, "mushroom_island_shore", new BiomeMushroomIsland((new BiomeBase.a("MushroomIslandShore")).c(0.0F).d(0.025F).a(0.9F).b(1.0F)));
        registerBiome(16, "beaches", new BiomeBeach((new BiomeBase.a("Beach")).c(0.0F).d(0.025F).a(0.8F).b(0.4F)));
        registerBiome(17, "desert_hills", new BiomeDesert((new BiomeBase.a("DesertHills")).c(0.45F).d(0.3F).a(2.0F).b(0.0F).a()));
        registerBiome(18, "forest_hills", new BiomeForest(BiomeForest.Type.NORMAL, (new BiomeBase.a("ForestHills")).c(0.45F).d(0.3F).a(0.7F).b(0.8F)));
        registerBiome(19, "taiga_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new BiomeBase.a("TaigaHills")).a(0.25F).b(0.8F).c(0.45F).d(0.3F)));
        registerBiome(20, "smaller_extreme_hills", new BiomeHills(BiomeHills.Type.EXTRA_TREES, (new BiomeBase.a("Extreme Hills Edge")).c(0.8F).d(0.3F).a(0.2F).b(0.3F)));
        registerBiome(21, "jungle", new BiomeJungle(false, (new BiomeBase.a("Jungle")).a(0.95F).b(0.9F)));
        registerBiome(22, "jungle_hills", new BiomeJungle(false, (new BiomeBase.a("JungleHills")).c(0.45F).d(0.3F).a(0.95F).b(0.9F)));
        registerBiome(23, "jungle_edge", new BiomeJungle(true, (new BiomeBase.a("JungleEdge")).a(0.95F).b(0.8F)));
        registerBiome(24, "deep_ocean", new BiomeOcean((new BiomeBase.a("Deep Ocean")).c(-1.8F).d(0.1F)));
        registerBiome(25, "stone_beach", new BiomeStoneBeach((new BiomeBase.a("Stone Beach")).c(0.1F).d(0.8F).a(0.2F).b(0.3F)));
        registerBiome(26, "cold_beach", new BiomeBeach((new BiomeBase.a("Cold Beach")).c(0.0F).d(0.025F).a(0.05F).b(0.3F).b()));
        registerBiome(27, "birch_forest", new BiomeForest(BiomeForest.Type.BIRCH, (new BiomeBase.a("Birch Forest")).a(0.6F).b(0.6F)));
        registerBiome(28, "birch_forest_hills", new BiomeForest(BiomeForest.Type.BIRCH, (new BiomeBase.a("Birch Forest Hills")).c(0.45F).d(0.3F).a(0.6F).b(0.6F)));
        registerBiome(29, "roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, (new BiomeBase.a("Roofed Forest")).a(0.7F).b(0.8F)));
        registerBiome(30, "taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new BiomeBase.a("Cold Taiga")).c(0.2F).d(0.2F).a(-0.5F).b(0.4F).b()));
        registerBiome(31, "taiga_cold_hills", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new BiomeBase.a("Cold Taiga Hills")).c(0.45F).d(0.3F).a(-0.5F).b(0.4F).b()));
        registerBiome(32, "redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA, (new BiomeBase.a("Mega Taiga")).a(0.3F).b(0.8F).c(0.2F).d(0.2F)));
        registerBiome(33, "redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA, (new BiomeBase.a("Mega Taiga Hills")).c(0.45F).d(0.3F).a(0.3F).b(0.8F)));
        registerBiome(34, "extreme_hills_with_trees", new BiomeHills(BiomeHills.Type.EXTRA_TREES, (new BiomeBase.a("Extreme Hills+")).c(1.0F).d(0.5F).a(0.2F).b(0.3F)));
        registerBiome(35, "savanna", new BiomeSavanna((new BiomeBase.a("Savanna")).c(0.125F).d(0.05F).a(1.2F).b(0.0F).a()));
        registerBiome(36, "savanna_rock", new BiomeSavanna((new BiomeBase.a("Savanna Plateau")).c(1.5F).d(0.025F).a(1.0F).b(0.0F).a()));
        registerBiome(37, "mesa", new BiomeMesa(false, false, (new BiomeBase.a("Mesa")).a(2.0F).b(0.0F).a()));
        registerBiome(38, "mesa_rock", new BiomeMesa(false, true, (new BiomeBase.a("Mesa Plateau F")).c(1.5F).d(0.025F).a(2.0F).b(0.0F).a()));
        registerBiome(39, "mesa_clear_rock", new BiomeMesa(false, false, (new BiomeBase.a("Mesa Plateau")).c(1.5F).d(0.025F).a(2.0F).b(0.0F).a()));
        registerBiome(127, "void", new BiomeVoid((new BiomeBase.a("The Void")).a()));
        registerBiome(129, "mutated_plains", new BiomePlains(true, (new BiomeBase.a("Sunflower Plains")).a("plains").c(0.125F).d(0.05F).a(0.8F).b(0.4F)));
        registerBiome(130, "mutated_desert", new BiomeDesert((new BiomeBase.a("Desert M")).a("desert").c(0.225F).d(0.25F).a(2.0F).b(0.0F).a()));
        registerBiome(131, "mutated_extreme_hills", new BiomeHills(BiomeHills.Type.MUTATED, (new BiomeBase.a("Extreme Hills M")).a("extreme_hills").c(1.0F).d(0.5F).a(0.2F).b(0.3F)));
        registerBiome(132, "mutated_forest", new BiomeForest(BiomeForest.Type.FLOWER, (new BiomeBase.a("Flower Forest")).a("forest").d(0.4F).a(0.7F).b(0.8F)));
        registerBiome(133, "mutated_taiga", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new BiomeBase.a("Taiga M")).a("taiga").c(0.3F).d(0.4F).a(0.25F).b(0.8F)));
        registerBiome(134, "mutated_swampland", new BiomeSwamp((new BiomeBase.a("Swampland M")).a("swampland").c(-0.1F).d(0.3F).a(0.8F).b(0.9F).a(14745518)));
        registerBiome(140, "mutated_ice_flats", new BiomeSnow(true, (new BiomeBase.a("Ice Plains Spikes")).a("ice_flats").c(0.425F).d(0.45000002F).a(0.0F).b(0.5F).b()));
        registerBiome(149, "mutated_jungle", new BiomeJungle(false, (new BiomeBase.a("Jungle M")).a("jungle").c(0.2F).d(0.4F).a(0.95F).b(0.9F)));
        registerBiome(151, "mutated_jungle_edge", new BiomeJungle(true, (new BiomeBase.a("JungleEdge M")).a("jungle_edge").c(0.2F).d(0.4F).a(0.95F).b(0.8F)));
        registerBiome(155, "mutated_birch_forest", new BiomeForestMutated((new BiomeBase.a("Birch Forest M")).a("birch_forest").c(0.2F).d(0.4F).a(0.6F).b(0.6F)));
        registerBiome(156, "mutated_birch_forest_hills", new BiomeForestMutated((new BiomeBase.a("Birch Forest Hills M")).a("birch_forest_hills").c(0.55F).d(0.5F).a(0.6F).b(0.6F)));
        registerBiome(157, "mutated_roofed_forest", new BiomeForest(BiomeForest.Type.ROOFED, (new BiomeBase.a("Roofed Forest M")).a("roofed_forest").c(0.2F).d(0.4F).a(0.7F).b(0.8F)));
        registerBiome(158, "mutated_taiga_cold", new BiomeTaiga(BiomeTaiga.Type.NORMAL, (new BiomeBase.a("Cold Taiga M")).a("taiga_cold").c(0.3F).d(0.4F).a(-0.5F).b(0.4F).b()));
        registerBiome(160, "mutated_redwood_taiga", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, (new BiomeBase.a("Mega Spruce Taiga")).a("redwood_taiga").c(0.2F).d(0.2F).a(0.25F).b(0.8F)));
        registerBiome(161, "mutated_redwood_taiga_hills", new BiomeTaiga(BiomeTaiga.Type.MEGA_SPRUCE, (new BiomeBase.a("Redwood Taiga Hills M")).a("redwood_taiga_hills").c(0.2F).d(0.2F).a(0.25F).b(0.8F)));
        registerBiome(162, "mutated_extreme_hills_with_trees", new BiomeHills(BiomeHills.Type.MUTATED, (new BiomeBase.a("Extreme Hills+ M")).a("extreme_hills_with_trees").c(1.0F).d(0.5F).a(0.2F).b(0.3F)));
        registerBiome(163, "mutated_savanna", new BiomeSavannaMutated((new BiomeBase.a("Savanna M")).a("savanna").c(0.3625F).d(1.225F).a(1.1F).b(0.0F).a()));
        registerBiome(164, "mutated_savanna_rock", new BiomeSavannaMutated((new BiomeBase.a("Savanna Plateau M")).a("savanna_rock").c(1.05F).d(1.2125001F).a(1.0F).b(0.0F).a()));
        registerBiome(165, "mutated_mesa", new BiomeMesa(true, false, (new BiomeBase.a("Mesa (Bryce)")).a("mesa").a(2.0F).b(0.0F).a()));
        registerBiome(166, "mutated_mesa_rock", new BiomeMesa(false, true, (new BiomeBase.a("Mesa Plateau F M")).a("mesa_rock").c(0.45F).d(0.3F).a(2.0F).b(0.0F).a()));
        registerBiome(167, "mutated_mesa_clear_rock", new BiomeMesa(false, false, (new BiomeBase.a("Mesa Plateau M")).a("mesa_clear_rock").c(0.45F).d(0.3F).a(2.0F).b(0.0F).a()));
    }

    private static void registerBiome(int i, String s, Biome biomebase) {
        Biome.REGISTRY.register(i, new ResourceLocation(s), biomebase);
        if (biomebase.isMutation()) {
            Biome.MUTATION_TO_BASE_ID_MAP.put(biomebase, getIdForBiome((Biome) Biome.REGISTRY.getObject(new ResourceLocation(biomebase.baseBiomeRegName))));
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

        protected BiomeBase.a a(float f) {
            if (f > 0.1F && f < 0.2F) {
                throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
            } else {
                this.d = f;
                return this;
            }
        }

        protected BiomeBase.a b(float f) {
            this.e = f;
            return this;
        }

        protected BiomeBase.a c(float f) {
            this.b = f;
            return this;
        }

        protected BiomeBase.a d(float f) {
            this.c = f;
            return this;
        }

        protected BiomeBase.a a() {
            this.h = false;
            return this;
        }

        protected BiomeBase.a b() {
            this.g = true;
            return this;
        }

        protected BiomeBase.a a(int i) {
            this.f = i;
            return this;
        }

        protected BiomeBase.a a(String s) {
            this.i = s;
            return this;
        }
    }

    public static class SpawnListEntry extends WeightedRandom.Item {

        public Class<? extends EntityLiving> entityClass;
        public int minGroupCount;
        public int maxGroupCount;

        public SpawnListEntry(Class<? extends EntityLiving> oclass, int i, int j, int k) {
            super(i);
            this.entityClass = oclass;
            this.minGroupCount = j;
            this.maxGroupCount = k;
        }

        public String toString() {
            return this.entityClass.getSimpleName() + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
        }
    }

    public static enum TempCategory {

        OCEAN, COLD, MEDIUM, WARM;

        private TempCategory() {}
    }
}
