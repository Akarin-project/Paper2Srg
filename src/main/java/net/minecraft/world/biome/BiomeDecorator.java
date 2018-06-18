package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeDecorator {

    protected boolean decorating;
    protected BlockPos chunkPos;
    protected ChunkGeneratorSettings chunkProviderSettings;
    protected WorldGenerator clayGen = new WorldGenClay(4);
    protected WorldGenerator sandGen;
    protected WorldGenerator gravelGen;
    protected WorldGenerator dirtGen;
    protected WorldGenerator gravelOreGen;
    protected WorldGenerator graniteGen;
    protected WorldGenerator dioriteGen;
    protected WorldGenerator andesiteGen;
    protected WorldGenerator coalGen;
    protected WorldGenerator ironGen;
    protected WorldGenerator goldGen;
    protected WorldGenerator redstoneGen;
    protected WorldGenerator diamondGen;
    protected WorldGenerator lapisGen;
    protected WorldGenFlowers flowerGen;
    protected WorldGenerator mushroomBrownGen;
    protected WorldGenerator mushroomRedGen;
    protected WorldGenerator bigMushroomGen;
    protected WorldGenerator reedGen;
    protected WorldGenerator cactusGen;
    protected WorldGenerator waterlilyGen;
    protected int waterlilyPerChunk;
    protected int treesPerChunk;
    protected float extraTreeChance;
    protected int flowersPerChunk;
    protected int grassPerChunk;
    protected int deadBushPerChunk;
    protected int mushroomsPerChunk;
    protected int reedsPerChunk;
    protected int cactiPerChunk;
    protected int gravelPatchesPerChunk;
    protected int sandPatchesPerChunk;
    protected int clayPerChunk;
    protected int bigMushroomsPerChunk;
    public boolean generateFalls;

    public BiomeDecorator() {
        this.sandGen = new WorldGenSand(Blocks.SAND, 7);
        this.gravelGen = new WorldGenSand(Blocks.GRAVEL, 6);
        this.flowerGen = new WorldGenFlowers(Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION);
        this.mushroomBrownGen = new WorldGenBush(Blocks.BROWN_MUSHROOM);
        this.mushroomRedGen = new WorldGenBush(Blocks.RED_MUSHROOM);
        this.bigMushroomGen = new WorldGenBigMushroom();
        this.reedGen = new WorldGenReed();
        this.cactusGen = new WorldGenCactus();
        this.waterlilyGen = new WorldGenWaterlily();
        this.extraTreeChance = 0.1F;
        this.flowersPerChunk = 2;
        this.grassPerChunk = 1;
        this.gravelPatchesPerChunk = 1;
        this.sandPatchesPerChunk = 3;
        this.clayPerChunk = 1;
        this.generateFalls = true;
    }

    public void decorate(World world, Random random, Biome biomebase, BlockPos blockposition) {
        if (this.decorating) {
            throw new RuntimeException("Already decorating");
        } else {
            this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(world.getWorldInfo().getGeneratorOptions()).build();
            this.chunkPos = blockposition;
            this.dirtGen = new WorldGenMinable(Blocks.DIRT.getDefaultState(), this.chunkProviderSettings.dirtSize);
            this.gravelOreGen = new WorldGenMinable(Blocks.GRAVEL.getDefaultState(), this.chunkProviderSettings.gravelSize);
            this.graniteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
            this.dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
            this.andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
            this.coalGen = new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), this.chunkProviderSettings.coalSize);
            this.ironGen = new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), this.chunkProviderSettings.ironSize);
            this.goldGen = new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), this.chunkProviderSettings.goldSize);
            this.redstoneGen = new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), this.chunkProviderSettings.redstoneSize);
            this.diamondGen = new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), this.chunkProviderSettings.diamondSize);
            this.lapisGen = new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), this.chunkProviderSettings.lapisSize);
            this.genDecorations(biomebase, world, random);
            this.decorating = false;
        }
    }

    protected void genDecorations(Biome biomebase, World world, Random random) {
        this.generateOres(world, random);

        int i;
        int j;
        int k;

        for (i = 0; i < this.sandPatchesPerChunk; ++i) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            this.sandGen.generate(world, random, world.getTopSolidOrLiquidBlock(this.chunkPos.add(j, 0, k)));
        }

        for (i = 0; i < this.clayPerChunk; ++i) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            this.clayGen.generate(world, random, world.getTopSolidOrLiquidBlock(this.chunkPos.add(j, 0, k)));
        }

        for (i = 0; i < this.gravelPatchesPerChunk; ++i) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            this.gravelGen.generate(world, random, world.getTopSolidOrLiquidBlock(this.chunkPos.add(j, 0, k)));
        }

        i = this.treesPerChunk;
        if (random.nextFloat() < this.extraTreeChance) {
            ++i;
        }

        int l;
        BlockPos blockposition;

        for (j = 0; j < i; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            WorldGenAbstractTree worldgentreeabstract = biomebase.getRandomTreeFeature(random);

            worldgentreeabstract.setDecorationDefaults();
            blockposition = world.getHeight(this.chunkPos.add(k, 0, l));
            if (worldgentreeabstract.generate(world, random, blockposition)) {
                worldgentreeabstract.generateSaplings(world, random, blockposition);
            }
        }

        for (j = 0; j < this.bigMushroomsPerChunk; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            this.bigMushroomGen.generate(world, random, world.getHeight(this.chunkPos.add(k, 0, l)));
        }

        BlockPos blockposition1;
        int i1;
        int j1;

        for (j = 0; j < this.flowersPerChunk; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            i1 = world.getHeight(this.chunkPos.add(k, 0, l)).getY() + 32;
            if (i1 > 0) {
                j1 = random.nextInt(i1);
                blockposition1 = this.chunkPos.add(k, j1, l);
                BlockFlower.EnumFlowerType blockflowers_enumflowervarient = biomebase.pickRandomFlower(random, blockposition1);
                BlockFlower blockflowers = blockflowers_enumflowervarient.getBlockType().getBlock();

                if (blockflowers.getDefaultState().getMaterial() != Material.AIR) {
                    this.flowerGen.setGeneratedBlock(blockflowers, blockflowers_enumflowervarient);
                    this.flowerGen.generate(world, random, blockposition1);
                }
            }
        }

        for (j = 0; j < this.grassPerChunk; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            i1 = world.getHeight(this.chunkPos.add(k, 0, l)).getY() * 2;
            if (i1 > 0) {
                j1 = random.nextInt(i1);
                biomebase.getRandomWorldGenForGrass(random).generate(world, random, this.chunkPos.add(k, j1, l));
            }
        }

        for (j = 0; j < this.deadBushPerChunk; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            i1 = world.getHeight(this.chunkPos.add(k, 0, l)).getY() * 2;
            if (i1 > 0) {
                j1 = random.nextInt(i1);
                (new WorldGenDeadBush()).generate(world, random, this.chunkPos.add(k, j1, l));
            }
        }

        for (j = 0; j < this.waterlilyPerChunk; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            i1 = world.getHeight(this.chunkPos.add(k, 0, l)).getY() * 2;
            if (i1 > 0) {
                j1 = random.nextInt(i1);

                BlockPos blockposition2;

                for (blockposition1 = this.chunkPos.add(k, j1, l); blockposition1.getY() > 0; blockposition1 = blockposition2) {
                    blockposition2 = blockposition1.down();
                    if (!world.isAirBlock(blockposition2)) {
                        break;
                    }
                }

                this.waterlilyGen.generate(world, random, blockposition1);
            }
        }

        for (j = 0; j < this.mushroomsPerChunk; ++j) {
            if (random.nextInt(4) == 0) {
                k = random.nextInt(16) + 8;
                l = random.nextInt(16) + 8;
                BlockPos blockposition3 = world.getHeight(this.chunkPos.add(k, 0, l));

                this.mushroomBrownGen.generate(world, random, blockposition3);
            }

            if (random.nextInt(8) == 0) {
                k = random.nextInt(16) + 8;
                l = random.nextInt(16) + 8;
                i1 = world.getHeight(this.chunkPos.add(k, 0, l)).getY() * 2;
                if (i1 > 0) {
                    j1 = random.nextInt(i1);
                    blockposition1 = this.chunkPos.add(k, j1, l);
                    this.mushroomRedGen.generate(world, random, blockposition1);
                }
            }
        }

        if (random.nextInt(4) == 0) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            l = world.getHeight(this.chunkPos.add(j, 0, k)).getY() * 2;
            if (l > 0) {
                i1 = random.nextInt(l);
                this.mushroomBrownGen.generate(world, random, this.chunkPos.add(j, i1, k));
            }
        }

        if (random.nextInt(8) == 0) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            l = world.getHeight(this.chunkPos.add(j, 0, k)).getY() * 2;
            if (l > 0) {
                i1 = random.nextInt(l);
                this.mushroomRedGen.generate(world, random, this.chunkPos.add(j, i1, k));
            }
        }

        for (j = 0; j < this.reedsPerChunk; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            i1 = world.getHeight(this.chunkPos.add(k, 0, l)).getY() * 2;
            if (i1 > 0) {
                j1 = random.nextInt(i1);
                this.reedGen.generate(world, random, this.chunkPos.add(k, j1, l));
            }
        }

        for (j = 0; j < 10; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            i1 = world.getHeight(this.chunkPos.add(k, 0, l)).getY() * 2;
            if (i1 > 0) {
                j1 = random.nextInt(i1);
                this.reedGen.generate(world, random, this.chunkPos.add(k, j1, l));
            }
        }

        if (random.nextInt(32) == 0) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            l = world.getHeight(this.chunkPos.add(j, 0, k)).getY() * 2;
            if (l > 0) {
                i1 = random.nextInt(l);
                (new WorldGenPumpkin()).generate(world, random, this.chunkPos.add(j, i1, k));
            }
        }

        for (j = 0; j < this.cactiPerChunk; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            i1 = world.getHeight(this.chunkPos.add(k, 0, l)).getY() * 2;
            if (i1 > 0) {
                j1 = random.nextInt(i1);
                this.cactusGen.generate(world, random, this.chunkPos.add(k, j1, l));
            }
        }

        if (this.generateFalls) {
            for (j = 0; j < 50; ++j) {
                k = random.nextInt(16) + 8;
                l = random.nextInt(16) + 8;
                i1 = random.nextInt(248) + 8;
                if (i1 > 0) {
                    j1 = random.nextInt(i1);
                    blockposition1 = this.chunkPos.add(k, j1, l);
                    (new WorldGenLiquids(Blocks.FLOWING_WATER)).generate(world, random, blockposition1);
                }
            }

            for (j = 0; j < 20; ++j) {
                k = random.nextInt(16) + 8;
                l = random.nextInt(16) + 8;
                i1 = random.nextInt(random.nextInt(random.nextInt(240) + 8) + 8);
                blockposition = this.chunkPos.add(k, i1, l);
                (new WorldGenLiquids(Blocks.FLOWING_LAVA)).generate(world, random, blockposition);
            }
        }

    }

    protected void generateOres(World world, Random random) {
        this.genStandardOre1(world, random, this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.gravelCount, this.gravelOreGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
        this.genStandardOre1(world, random, this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
        this.genStandardOre2(world, random, this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
    }

    protected void genStandardOre1(World world, Random random, int i, WorldGenerator worldgenerator, int j, int k) {
        int l;

        if (k < j) {
            l = j;
            j = k;
            k = l;
        } else if (k == j) {
            if (j < 255) {
                ++k;
            } else {
                --j;
            }
        }

        for (l = 0; l < i; ++l) {
            BlockPos blockposition = this.chunkPos.add(random.nextInt(16), random.nextInt(k - j) + j, random.nextInt(16));

            worldgenerator.generate(world, random, blockposition);
        }

    }

    protected void genStandardOre2(World world, Random random, int i, WorldGenerator worldgenerator, int j, int k) {
        for (int l = 0; l < i; ++l) {
            BlockPos blockposition = this.chunkPos.add(random.nextInt(16), random.nextInt(k) + random.nextInt(k) + j - k, random.nextInt(16));

            worldgenerator.generate(world, random, blockposition);
        }

    }
}
