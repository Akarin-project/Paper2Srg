package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureVillagePieces {

    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House1.class, "ViBH");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field1.class, "ViDF");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field2.class, "ViF");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Torch.class, "ViL");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Hall.class, "ViPH");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House4Garden.class, "ViSH");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.WoodHut.class, "ViSmH");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Church.class, "ViST");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House2.class, "ViS");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Start.class, "ViStart");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Path.class, "ViSR");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House3.class, "ViTRH");
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Well.class, "ViW");
    }

    public static List<StructureVillagePieces.PieceWeight> getStructureVillageWeightedPieceList(Random random, int i) {
        ArrayList arraylist = Lists.newArrayList();

        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, 4, MathHelper.getInt(random, 2 + i, 4 + i * 2)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20, MathHelper.getInt(random, 0 + i, 1 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, 20, MathHelper.getInt(random, 0 + i, 2 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, 3, MathHelper.getInt(random, 2 + i, 5 + i * 3)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, 15, MathHelper.getInt(random, 0 + i, 2 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, 3, MathHelper.getInt(random, 1 + i, 4 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, 3, MathHelper.getInt(random, 2 + i, 4 + i * 2)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, 15, MathHelper.getInt(random, 0, 1 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, 8, MathHelper.getInt(random, 0 + i, 3 + i * 2)));
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            if (((StructureVillagePieces.PieceWeight) iterator.next()).villagePiecesLimit == 0) {
                iterator.remove();
            }
        }

        return arraylist;
    }

    private static int updatePieceWeight(List<StructureVillagePieces.PieceWeight> list) {
        boolean flag = false;
        int i = 0;

        StructureVillagePieces.PieceWeight worldgenvillagepieces_worldgenvillagepieceweight;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); i += worldgenvillagepieces_worldgenvillagepieceweight.villagePieceWeight) {
            worldgenvillagepieces_worldgenvillagepieceweight = (StructureVillagePieces.PieceWeight) iterator.next();
            if (worldgenvillagepieces_worldgenvillagepieceweight.villagePiecesLimit > 0 && worldgenvillagepieces_worldgenvillagepieceweight.villagePiecesSpawned < worldgenvillagepieces_worldgenvillagepieceweight.villagePiecesLimit) {
                flag = true;
            }
        }

        return flag ? i : -1;
    }

    private static StructureVillagePieces.Village findAndCreateComponentFactory(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, StructureVillagePieces.PieceWeight worldgenvillagepieces_worldgenvillagepieceweight, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        Class oclass = worldgenvillagepieces_worldgenvillagepieceweight.villagePieceClass;
        Object object = null;

        if (oclass == StructureVillagePieces.House4Garden.class) {
            object = StructureVillagePieces.House4Garden.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.Church.class) {
            object = StructureVillagePieces.Church.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.House1.class) {
            object = StructureVillagePieces.House1.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.WoodHut.class) {
            object = StructureVillagePieces.WoodHut.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.Hall.class) {
            object = StructureVillagePieces.Hall.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.Field1.class) {
            object = StructureVillagePieces.Field1.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.Field2.class) {
            object = StructureVillagePieces.Field2.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.House2.class) {
            object = StructureVillagePieces.House2.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.House3.class) {
            object = StructureVillagePieces.House3.createPiece(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        }

        return (StructureVillagePieces.Village) object;
    }

    private static StructureVillagePieces.Village generateComponent(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        int i1 = updatePieceWeight(worldgenvillagepieces_worldgenvillagestartpiece.structureVillageWeightedPieceList);

        if (i1 <= 0) {
            return null;
        } else {
            int j1 = 0;

            while (j1 < 5) {
                ++j1;
                int k1 = random.nextInt(i1);
                Iterator iterator = worldgenvillagepieces_worldgenvillagestartpiece.structureVillageWeightedPieceList.iterator();

                while (iterator.hasNext()) {
                    StructureVillagePieces.PieceWeight worldgenvillagepieces_worldgenvillagepieceweight = (StructureVillagePieces.PieceWeight) iterator.next();

                    k1 -= worldgenvillagepieces_worldgenvillagepieceweight.villagePieceWeight;
                    if (k1 < 0) {
                        if (!worldgenvillagepieces_worldgenvillagepieceweight.canSpawnMoreVillagePiecesOfType(l) || worldgenvillagepieces_worldgenvillagepieceweight == worldgenvillagepieces_worldgenvillagestartpiece.lastPlaced && worldgenvillagepieces_worldgenvillagestartpiece.structureVillageWeightedPieceList.size() > 1) {
                            break;
                        }

                        StructureVillagePieces.Village worldgenvillagepieces_worldgenvillagepiece = findAndCreateComponentFactory(worldgenvillagepieces_worldgenvillagestartpiece, worldgenvillagepieces_worldgenvillagepieceweight, list, random, i, j, k, enumdirection, l);

                        if (worldgenvillagepieces_worldgenvillagepiece != null) {
                            ++worldgenvillagepieces_worldgenvillagepieceweight.villagePiecesSpawned;
                            worldgenvillagepieces_worldgenvillagestartpiece.lastPlaced = worldgenvillagepieces_worldgenvillagepieceweight;
                            if (!worldgenvillagepieces_worldgenvillagepieceweight.canSpawnMoreVillagePieces()) {
                                worldgenvillagepieces_worldgenvillagestartpiece.structureVillageWeightedPieceList.remove(worldgenvillagepieces_worldgenvillagepieceweight);
                            }

                            return worldgenvillagepieces_worldgenvillagepiece;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = StructureVillagePieces.Torch.findPieceBox(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection);

            if (structureboundingbox != null) {
                return new StructureVillagePieces.Torch(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection);
            } else {
                return null;
            }
        }
    }

    private static StructureComponent generateAndAddComponent(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        if (l > 50) {
            return null;
        } else if (Math.abs(i - worldgenvillagepieces_worldgenvillagestartpiece.getBoundingBox().minX) <= 112 && Math.abs(k - worldgenvillagepieces_worldgenvillagestartpiece.getBoundingBox().minZ) <= 112) {
            StructureVillagePieces.Village worldgenvillagepieces_worldgenvillagepiece = generateComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l + 1);

            if (worldgenvillagepieces_worldgenvillagepiece != null) {
                list.add(worldgenvillagepieces_worldgenvillagepiece);
                worldgenvillagepieces_worldgenvillagestartpiece.pendingHouses.add(worldgenvillagepieces_worldgenvillagepiece);
                return worldgenvillagepieces_worldgenvillagepiece;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static StructureComponent generateAndAddRoadPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        if (l > 3 + worldgenvillagepieces_worldgenvillagestartpiece.terrainType) {
            return null;
        } else if (Math.abs(i - worldgenvillagepieces_worldgenvillagestartpiece.getBoundingBox().minX) <= 112 && Math.abs(k - worldgenvillagepieces_worldgenvillagestartpiece.getBoundingBox().minZ) <= 112) {
            StructureBoundingBox structureboundingbox = StructureVillagePieces.Path.findPieceBox(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection);

            if (structureboundingbox != null && structureboundingbox.minY > 10) {
                StructureVillagePieces.Path worldgenvillagepieces_worldgenvillageroad = new StructureVillagePieces.Path(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection);

                list.add(worldgenvillagepieces_worldgenvillageroad);
                worldgenvillagepieces_worldgenvillagestartpiece.pendingRoads.add(worldgenvillagepieces_worldgenvillageroad);
                return worldgenvillagepieces_worldgenvillageroad;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static class Torch extends StructureVillagePieces.Village {

        public Torch() {}

        public Torch(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public static StructureBoundingBox findPieceBox(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 3, 4, 2, enumdirection);

            return StructureComponent.findIntersecting(list, structureboundingbox) != null ? null : structureboundingbox;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(world, iblockdata, 1, 0, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 1, 1, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 1, 2, 0, structureboundingbox);
            this.setBlockState(world, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0, structureboundingbox);
            this.placeTorch(world, EnumFacing.EAST, 2, 3, 0, structureboundingbox);
            this.placeTorch(world, EnumFacing.NORTH, 1, 3, 1, structureboundingbox);
            this.placeTorch(world, EnumFacing.WEST, 0, 3, 0, structureboundingbox);
            this.placeTorch(world, EnumFacing.SOUTH, 1, 3, -1, structureboundingbox);
            return true;
        }
    }

    public static class Field1 extends StructureVillagePieces.Village {

        private Block cropTypeA;
        private Block cropTypeB;
        private Block cropTypeC;
        private Block cropTypeD;

        public Field1() {}

        public Field1(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.cropTypeA = this.getRandomCropType(random);
            this.cropTypeB = this.getRandomCropType(random);
            this.cropTypeC = this.getRandomCropType(random);
            this.cropTypeD = this.getRandomCropType(random);
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
            nbttagcompound.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
            nbttagcompound.setInteger("CC", Block.REGISTRY.getIDForObject(this.cropTypeC));
            nbttagcompound.setInteger("CD", Block.REGISTRY.getIDForObject(this.cropTypeD));
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.cropTypeA = Block.getBlockById(nbttagcompound.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(nbttagcompound.getInteger("CB"));
            this.cropTypeC = Block.getBlockById(nbttagcompound.getInteger("CC"));
            this.cropTypeD = Block.getBlockById(nbttagcompound.getInteger("CD"));
            if (!(this.cropTypeA instanceof BlockCrops)) {
                this.cropTypeA = Blocks.WHEAT;
            }

            if (!(this.cropTypeB instanceof BlockCrops)) {
                this.cropTypeB = Blocks.CARROTS;
            }

            if (!(this.cropTypeC instanceof BlockCrops)) {
                this.cropTypeC = Blocks.POTATOES;
            }

            if (!(this.cropTypeD instanceof BlockCrops)) {
                this.cropTypeD = Blocks.BEETROOTS;
            }

        }

        private Block getRandomCropType(Random random) {
            switch (random.nextInt(10)) {
            case 0:
            case 1:
                return Blocks.CARROTS;

            case 2:
            case 3:
                return Blocks.POTATOES;

            case 4:
                return Blocks.BEETROOTS;

            default:
                return Blocks.WHEAT;
            }
        }

        public static StructureVillagePieces.Field1 createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 13, 4, 9, enumdirection);

            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureVillagePieces.Field1(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 12, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 0, 1, 8, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 10, 0, 1, 11, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 0, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 0, 0, 6, 0, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 12, 0, 0, 12, 0, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 11, 0, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 8, 11, 0, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 9, 0, 1, 9, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);

            int i;
            int j;

            for (i = 1; i <= 7; ++i) {
                j = ((BlockCrops) this.cropTypeA).getMaxAge();
                int k = j / 3;

                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getInt(random, k, j)), 1, 1, i, structureboundingbox);
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getInt(random, k, j)), 2, 1, i, structureboundingbox);
                int l = ((BlockCrops) this.cropTypeB).getMaxAge();
                int i1 = l / 3;

                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getInt(random, i1, l)), 4, 1, i, structureboundingbox);
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getInt(random, i1, l)), 5, 1, i, structureboundingbox);
                int j1 = ((BlockCrops) this.cropTypeC).getMaxAge();
                int k1 = j1 / 3;

                this.setBlockState(world, this.cropTypeC.getStateFromMeta(MathHelper.getInt(random, k1, j1)), 7, 1, i, structureboundingbox);
                this.setBlockState(world, this.cropTypeC.getStateFromMeta(MathHelper.getInt(random, k1, j1)), 8, 1, i, structureboundingbox);
                int l1 = ((BlockCrops) this.cropTypeD).getMaxAge();
                int i2 = l1 / 3;

                this.setBlockState(world, this.cropTypeD.getStateFromMeta(MathHelper.getInt(random, i2, l1)), 10, 1, i, structureboundingbox);
                this.setBlockState(world, this.cropTypeD.getStateFromMeta(MathHelper.getInt(random, i2, l1)), 11, 1, i, structureboundingbox);
            }

            for (i = 0; i < 9; ++i) {
                for (j = 0; j < 13; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 4, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.DIRT.getDefaultState(), j, -1, i, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Field2 extends StructureVillagePieces.Village {

        private Block cropTypeA;
        private Block cropTypeB;

        public Field2() {}

        public Field2(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.cropTypeA = this.getRandomCropType(random);
            this.cropTypeB = this.getRandomCropType(random);
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
            nbttagcompound.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.cropTypeA = Block.getBlockById(nbttagcompound.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(nbttagcompound.getInteger("CB"));
        }

        private Block getRandomCropType(Random random) {
            switch (random.nextInt(10)) {
            case 0:
            case 1:
                return Blocks.CARROTS;

            case 2:
            case 3:
                return Blocks.POTATOES;

            case 4:
                return Blocks.BEETROOTS;

            default:
                return Blocks.WHEAT;
            }
        }

        public static StructureVillagePieces.Field2 createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 7, 4, 9, enumdirection);

            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureVillagePieces.Field2(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 6, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 0, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 0, 0, 6, 0, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 5, 0, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 8, 5, 0, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);

            int i;
            int j;

            for (i = 1; i <= 7; ++i) {
                j = ((BlockCrops) this.cropTypeA).getMaxAge();
                int k = j / 3;

                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getInt(random, k, j)), 1, 1, i, structureboundingbox);
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getInt(random, k, j)), 2, 1, i, structureboundingbox);
                int l = ((BlockCrops) this.cropTypeB).getMaxAge();
                int i1 = l / 3;

                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getInt(random, i1, l)), 4, 1, i, structureboundingbox);
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getInt(random, i1, l)), 5, 1, i, structureboundingbox);
            }

            for (i = 0; i < 9; ++i) {
                for (j = 0; j < 7; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 4, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.DIRT.getDefaultState(), j, -1, i, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class House2 extends StructureVillagePieces.Village {

        private boolean hasMadeChest;

        public House2() {}

        public House2(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public static StructureVillagePieces.House2 createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 10, 6, 7, enumdirection);

            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureVillagePieces.House2(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Chest", this.hasMadeChest);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.hasMadeChest = nbttagcompound.getBoolean("Chest");
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockdata = Blocks.COBBLESTONE.getDefaultState();
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockdata3 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            IBlockState iblockdata4 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata5 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            IBlockState iblockdata6 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 9, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 9, 0, 6, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 4, 0, 9, 4, 6, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 9, 5, 6, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 5, 1, 8, 5, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 2, 3, 0, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 4, 0, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 0, 3, 4, 0, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 6, 0, 4, 6, iblockdata5, iblockdata5, false);
            this.setBlockState(world, iblockdata3, 3, 3, 1, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 2, 3, 3, 2, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 4, 1, 3, 5, 3, 3, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 5, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 6, 5, 3, 6, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 1, 0, 5, 3, 0, iblockdata6, iblockdata6, false);
            this.fillWithBlocks(world, structureboundingbox, 9, 1, 0, 9, 3, 0, iblockdata6, iblockdata6, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 1, 4, 9, 4, 6, iblockdata, iblockdata, false);
            this.setBlockState(world, Blocks.FLOWING_LAVA.getDefaultState(), 7, 1, 5, structureboundingbox);
            this.setBlockState(world, Blocks.FLOWING_LAVA.getDefaultState(), 8, 1, 5, structureboundingbox);
            this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), 9, 2, 5, structureboundingbox);
            this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), 9, 2, 4, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 7, 2, 4, 8, 2, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(world, iblockdata, 6, 1, 3, structureboundingbox);
            this.setBlockState(world, Blocks.FURNACE.getDefaultState(), 6, 2, 3, structureboundingbox);
            this.setBlockState(world, Blocks.FURNACE.getDefaultState(), 6, 3, 3, structureboundingbox);
            this.setBlockState(world, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 8, 1, 1, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 4, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 6, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 6, structureboundingbox);
            this.setBlockState(world, iblockdata6, 2, 1, 4, structureboundingbox);
            this.setBlockState(world, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 2, 2, 4, structureboundingbox);
            this.setBlockState(world, iblockdata3, 1, 1, 5, structureboundingbox);
            this.setBlockState(world, iblockdata1, 2, 1, 5, structureboundingbox);
            this.setBlockState(world, iblockdata2, 1, 1, 4, structureboundingbox);
            if (!this.hasMadeChest && structureboundingbox.isVecInside((Vec3i) (new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5))))) {
                this.hasMadeChest = true;
                this.generateChest(world, structureboundingbox, random, 5, 1, 5, LootTableList.CHESTS_VILLAGE_BLACKSMITH);
            }

            int i;

            for (i = 6; i <= 8; ++i) {
                if (this.getBlockStateFromPos(world, i, 0, -1, structureboundingbox).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, i, -1, -1, structureboundingbox).getMaterial() != Material.AIR) {
                    this.setBlockState(world, iblockdata4, i, 0, -1, structureboundingbox);
                    if (this.getBlockStateFromPos(world, i, -1, -1, structureboundingbox).getBlock() == Blocks.GRASS_PATH) {
                        this.setBlockState(world, Blocks.GRASS.getDefaultState(), i, -1, -1, structureboundingbox);
                    }
                }
            }

            for (i = 0; i < 7; ++i) {
                for (int j = 0; j < 10; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 6, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.spawnVillagers(world, structureboundingbox, 7, 1, 1, 1);
            return true;
        }

        protected int chooseProfession(int i, int j) {
            return 3;
        }
    }

    public static class House3 extends StructureVillagePieces.Village {

        public House3() {}

        public House3(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public static StructureVillagePieces.House3 createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 9, 7, 12, enumdirection);

            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureVillagePieces.House3(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockdata3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
            IBlockState iblockdata4 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockdata5 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            IBlockState iblockdata6 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 0, 5, 8, 0, 10, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 7, 0, 4, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 3, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 8, 0, 0, 8, 3, 10, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 7, 2, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 5, 2, 1, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 0, 6, 2, 3, 10, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 0, 10, 7, 3, 10, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 7, 3, 0, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 5, 2, 3, 5, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 4, 1, 8, 4, 1, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 4, 4, 3, 4, 4, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 2, 8, 5, 3, iblockdata5, iblockdata5, false);
            this.setBlockState(world, iblockdata5, 0, 4, 2, structureboundingbox);
            this.setBlockState(world, iblockdata5, 0, 4, 3, structureboundingbox);
            this.setBlockState(world, iblockdata5, 8, 4, 2, structureboundingbox);
            this.setBlockState(world, iblockdata5, 8, 4, 3, structureboundingbox);
            this.setBlockState(world, iblockdata5, 8, 4, 4, structureboundingbox);
            IBlockState iblockdata7 = iblockdata1;
            IBlockState iblockdata8 = iblockdata2;
            IBlockState iblockdata9 = iblockdata4;
            IBlockState iblockdata10 = iblockdata3;

            int i;
            int j;

            for (i = -1; i <= 2; ++i) {
                for (j = 0; j <= 8; ++j) {
                    this.setBlockState(world, iblockdata7, j, 4 + i, i, structureboundingbox);
                    if ((i > -1 || j <= 1) && (i > 0 || j <= 3) && (i > 1 || j <= 4 || j >= 6)) {
                        this.setBlockState(world, iblockdata8, j, 4 + i, 5 - i, structureboundingbox);
                    }
                }
            }

            this.fillWithBlocks(world, structureboundingbox, 3, 4, 5, 3, 4, 10, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 4, 2, 7, 4, 10, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 4, 5, 4, 4, 5, 10, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 5, 4, 6, 5, 10, iblockdata5, iblockdata5, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 6, 3, 5, 6, 10, iblockdata5, iblockdata5, false);

            for (i = 4; i >= 1; --i) {
                this.setBlockState(world, iblockdata5, i, 2 + i, 7 - i, structureboundingbox);

                for (j = 8 - i; j <= 10; ++j) {
                    this.setBlockState(world, iblockdata10, i, 2 + i, j, structureboundingbox);
                }
            }

            this.setBlockState(world, iblockdata5, 6, 6, 3, structureboundingbox);
            this.setBlockState(world, iblockdata5, 7, 5, 4, structureboundingbox);
            this.setBlockState(world, iblockdata4, 6, 6, 4, structureboundingbox);

            for (i = 6; i <= 8; ++i) {
                for (j = 5; j <= 10; ++j) {
                    this.setBlockState(world, iblockdata9, i, 12 - i, j, structureboundingbox);
                }
            }

            this.setBlockState(world, iblockdata6, 0, 2, 1, structureboundingbox);
            this.setBlockState(world, iblockdata6, 0, 2, 4, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, structureboundingbox);
            this.setBlockState(world, iblockdata6, 4, 2, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, structureboundingbox);
            this.setBlockState(world, iblockdata6, 6, 2, 0, structureboundingbox);
            this.setBlockState(world, iblockdata6, 8, 2, 1, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, structureboundingbox);
            this.setBlockState(world, iblockdata6, 8, 2, 4, structureboundingbox);
            this.setBlockState(world, iblockdata5, 8, 2, 5, structureboundingbox);
            this.setBlockState(world, iblockdata6, 8, 2, 6, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 7, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 8, structureboundingbox);
            this.setBlockState(world, iblockdata6, 8, 2, 9, structureboundingbox);
            this.setBlockState(world, iblockdata6, 2, 2, 6, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 7, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 8, structureboundingbox);
            this.setBlockState(world, iblockdata6, 2, 2, 9, structureboundingbox);
            this.setBlockState(world, iblockdata6, 4, 4, 10, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 4, 10, structureboundingbox);
            this.setBlockState(world, iblockdata6, 6, 4, 10, structureboundingbox);
            this.setBlockState(world, iblockdata5, 5, 5, 10, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 1, 0, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 2, 0, structureboundingbox);
            this.placeTorch(world, EnumFacing.NORTH, 2, 3, 1, structureboundingbox);
            this.createVillageDoor(world, structureboundingbox, random, 2, 1, 0, EnumFacing.NORTH);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, -1, 3, 2, -1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            if (this.getBlockStateFromPos(world, 2, 0, -1, structureboundingbox).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getMaterial() != Material.AIR) {
                this.setBlockState(world, iblockdata7, 2, 0, -1, structureboundingbox);
                if (this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(world, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureboundingbox);
                }
            }

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 9; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 7, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            for (i = 5; i < 11; ++i) {
                for (j = 2; j < 9; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 7, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.spawnVillagers(world, structureboundingbox, 4, 1, 2, 2);
            return true;
        }
    }

    public static class Hall extends StructureVillagePieces.Village {

        public Hall() {}

        public Hall(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public static StructureVillagePieces.Hall createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 9, 7, 11, enumdirection);

            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureVillagePieces.Hall(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockdata3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockdata4 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            IBlockState iblockdata5 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            IBlockState iblockdata6 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 0, 6, 8, 0, 10, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
            this.setBlockState(world, iblockdata, 6, 0, 6, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 2, 1, 6, 2, 1, 10, iblockdata6, iblockdata6, false);
            this.fillWithBlocks(world, structureboundingbox, 8, 1, 6, 8, 1, 10, iblockdata6, iblockdata6, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 10, 7, 1, 10, iblockdata6, iblockdata6, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 7, 0, 4, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 3, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 8, 0, 0, 8, 3, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 7, 1, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 5, 7, 1, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 7, 3, 0, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 5, 7, 3, 5, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 4, 1, 8, 4, 1, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 4, 4, 8, 4, 4, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 2, 8, 5, 3, iblockdata4, iblockdata4, false);
            this.setBlockState(world, iblockdata4, 0, 4, 2, structureboundingbox);
            this.setBlockState(world, iblockdata4, 0, 4, 3, structureboundingbox);
            this.setBlockState(world, iblockdata4, 8, 4, 2, structureboundingbox);
            this.setBlockState(world, iblockdata4, 8, 4, 3, structureboundingbox);
            IBlockState iblockdata7 = iblockdata1;
            IBlockState iblockdata8 = iblockdata2;

            int i;
            int j;

            for (i = -1; i <= 2; ++i) {
                for (j = 0; j <= 8; ++j) {
                    this.setBlockState(world, iblockdata7, j, 4 + i, i, structureboundingbox);
                    this.setBlockState(world, iblockdata8, j, 4 + i, 5 - i, structureboundingbox);
                }
            }

            this.setBlockState(world, iblockdata5, 0, 2, 1, structureboundingbox);
            this.setBlockState(world, iblockdata5, 0, 2, 4, structureboundingbox);
            this.setBlockState(world, iblockdata5, 8, 2, 1, structureboundingbox);
            this.setBlockState(world, iblockdata5, 8, 2, 4, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, structureboundingbox);
            this.setBlockState(world, iblockdata6, 2, 1, 3, structureboundingbox);
            this.setBlockState(world, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 2, 2, 3, structureboundingbox);
            this.setBlockState(world, iblockdata4, 1, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata7, 2, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata3, 1, 1, 3, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 5, 0, 1, 7, 0, 3, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
            this.setBlockState(world, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 6, 1, 1, structureboundingbox);
            this.setBlockState(world, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 6, 1, 2, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 1, 0, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 2, 0, structureboundingbox);
            this.placeTorch(world, EnumFacing.NORTH, 2, 3, 1, structureboundingbox);
            this.createVillageDoor(world, structureboundingbox, random, 2, 1, 0, EnumFacing.NORTH);
            if (this.getBlockStateFromPos(world, 2, 0, -1, structureboundingbox).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getMaterial() != Material.AIR) {
                this.setBlockState(world, iblockdata7, 2, 0, -1, structureboundingbox);
                if (this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(world, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureboundingbox);
                }
            }

            this.setBlockState(world, Blocks.AIR.getDefaultState(), 6, 1, 5, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 6, 2, 5, structureboundingbox);
            this.placeTorch(world, EnumFacing.SOUTH, 6, 3, 4, structureboundingbox);
            this.createVillageDoor(world, structureboundingbox, random, 6, 1, 5, EnumFacing.SOUTH);

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 9; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 7, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.spawnVillagers(world, structureboundingbox, 4, 1, 2, 2);
            return true;
        }

        protected int chooseProfession(int i, int j) {
            return i == 0 ? 4 : super.chooseProfession(i, j);
        }
    }

    public static class WoodHut extends StructureVillagePieces.Village {

        private boolean isTallHouse;
        private int tablePosition;

        public WoodHut() {}

        public WoodHut(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.isTallHouse = random.nextBoolean();
            this.tablePosition = random.nextInt(3);
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setInteger("T", this.tablePosition);
            nbttagcompound.setBoolean("C", this.isTallHouse);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.tablePosition = nbttagcompound.getInteger("T");
            this.isTallHouse = nbttagcompound.getBoolean("C");
        }

        public static StructureVillagePieces.WoodHut createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 4, 6, 5, enumdirection);

            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureVillagePieces.WoodHut(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            IBlockState iblockdata2 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata3 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            IBlockState iblockdata4 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 3, 0, 4, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 2, 0, 3, Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState(), false);
            if (this.isTallHouse) {
                this.fillWithBlocks(world, structureboundingbox, 1, 4, 1, 2, 4, 3, iblockdata3, iblockdata3, false);
            } else {
                this.fillWithBlocks(world, structureboundingbox, 1, 5, 1, 2, 5, 3, iblockdata3, iblockdata3, false);
            }

            this.setBlockState(world, iblockdata3, 1, 4, 0, structureboundingbox);
            this.setBlockState(world, iblockdata3, 2, 4, 0, structureboundingbox);
            this.setBlockState(world, iblockdata3, 1, 4, 4, structureboundingbox);
            this.setBlockState(world, iblockdata3, 2, 4, 4, structureboundingbox);
            this.setBlockState(world, iblockdata3, 0, 4, 1, structureboundingbox);
            this.setBlockState(world, iblockdata3, 0, 4, 2, structureboundingbox);
            this.setBlockState(world, iblockdata3, 0, 4, 3, structureboundingbox);
            this.setBlockState(world, iblockdata3, 3, 4, 1, structureboundingbox);
            this.setBlockState(world, iblockdata3, 3, 4, 2, structureboundingbox);
            this.setBlockState(world, iblockdata3, 3, 4, 3, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 3, 0, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 0, 3, 3, 0, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 4, 0, 3, 4, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 4, 3, 3, 4, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, iblockdata1, iblockdata1, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 1, 3, 3, 3, iblockdata1, iblockdata1, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 2, 3, 0, iblockdata1, iblockdata1, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 4, 2, 3, 4, iblockdata1, iblockdata1, false);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 2, structureboundingbox);
            if (this.tablePosition > 0) {
                this.setBlockState(world, iblockdata4, this.tablePosition, 1, 3, structureboundingbox);
                this.setBlockState(world, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), this.tablePosition, 2, 3, structureboundingbox);
            }

            this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, 1, 0, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, 2, 0, structureboundingbox);
            this.createVillageDoor(world, structureboundingbox, random, 1, 1, 0, EnumFacing.NORTH);
            if (this.getBlockStateFromPos(world, 1, 0, -1, structureboundingbox).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 1, -1, -1, structureboundingbox).getMaterial() != Material.AIR) {
                this.setBlockState(world, iblockdata2, 1, 0, -1, structureboundingbox);
                if (this.getBlockStateFromPos(world, 1, -1, -1, structureboundingbox).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(world, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureboundingbox);
                }
            }

            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 4; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 6, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.spawnVillagers(world, structureboundingbox, 1, 1, 2, 1);
            return true;
        }
    }

    public static class House1 extends StructureVillagePieces.Village {

        public House1() {}

        public House1(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public static StructureVillagePieces.House1 createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 9, 9, 6, enumdirection);

            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureVillagePieces.House1(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 9 - 1, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockdata3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
            IBlockState iblockdata4 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            IBlockState iblockdata5 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata6 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 7, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 8, 0, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 8, 5, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 6, 1, 8, 6, 4, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 7, 2, 8, 7, 3, iblockdata, iblockdata, false);

            int i;

            for (int j = -1; j <= 2; ++j) {
                for (i = 0; i <= 8; ++i) {
                    this.setBlockState(world, iblockdata1, i, 6 + j, j, structureboundingbox);
                    this.setBlockState(world, iblockdata2, i, 6 + j, 5 - j, structureboundingbox);
                }
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 1, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 5, 8, 1, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 8, 1, 0, 8, 1, 4, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 1, 0, 7, 1, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 4, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 5, 0, 4, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 8, 2, 5, 8, 4, 5, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 8, 2, 0, 8, 4, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 1, 0, 4, 4, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 5, 7, 4, 5, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 8, 2, 1, 8, 4, 4, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 7, 4, 0, iblockdata4, iblockdata4, false);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 3, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 6, 3, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 3, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 3, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 5, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 1, 7, 4, 1, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 4, 7, 4, 4, iblockdata4, iblockdata4, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 4, 7, 3, 4, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            this.setBlockState(world, iblockdata4, 7, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata3, 7, 1, 3, structureboundingbox);
            this.setBlockState(world, iblockdata1, 6, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata1, 5, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata1, 4, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata1, 3, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata6, 6, 1, 3, structureboundingbox);
            this.setBlockState(world, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 6, 2, 3, structureboundingbox);
            this.setBlockState(world, iblockdata6, 4, 1, 3, structureboundingbox);
            this.setBlockState(world, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 4, 2, 3, structureboundingbox);
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 7, 1, 1, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, 1, 0, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, 2, 0, structureboundingbox);
            this.createVillageDoor(world, structureboundingbox, random, 1, 1, 0, EnumFacing.NORTH);
            if (this.getBlockStateFromPos(world, 1, 0, -1, structureboundingbox).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 1, -1, -1, structureboundingbox).getMaterial() != Material.AIR) {
                this.setBlockState(world, iblockdata5, 1, 0, -1, structureboundingbox);
                if (this.getBlockStateFromPos(world, 1, -1, -1, structureboundingbox).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(world, Blocks.GRASS.getDefaultState(), 1, -1, -1, structureboundingbox);
                }
            }

            for (i = 0; i < 6; ++i) {
                for (int k = 0; k < 9; ++k) {
                    this.clearCurrentPositionBlocksUpwards(world, k, 9, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, iblockdata, k, -1, i, structureboundingbox);
                }
            }

            this.spawnVillagers(world, structureboundingbox, 2, 1, 2, 1);
            return true;
        }

        protected int chooseProfession(int i, int j) {
            return 1;
        }
    }

    public static class Church extends StructureVillagePieces.Village {

        public Church() {}

        public Church(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public static StructureVillagePieces.Church createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 5, 12, 9, enumdirection);

            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureVillagePieces.Church(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 12 - 1, 0);
            }

            IBlockState iblockdata = Blocks.COBBLESTONE.getDefaultState();
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockdata3 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));

            this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 3, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 5, 1, 3, 9, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 3, 0, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 3, 10, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 10, 3, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 10, 3, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 4, 0, 4, 7, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 4, 0, 4, 4, 4, 7, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 8, 3, 4, 8, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 5, 4, 3, 10, 4, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 5, 5, 3, 5, 7, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 9, 0, 4, 9, 4, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 4, 0, 4, 4, 4, iblockdata, iblockdata, false);
            this.setBlockState(world, iblockdata, 0, 11, 2, structureboundingbox);
            this.setBlockState(world, iblockdata, 4, 11, 2, structureboundingbox);
            this.setBlockState(world, iblockdata, 2, 11, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 2, 11, 4, structureboundingbox);
            this.setBlockState(world, iblockdata, 1, 1, 6, structureboundingbox);
            this.setBlockState(world, iblockdata, 1, 1, 7, structureboundingbox);
            this.setBlockState(world, iblockdata, 2, 1, 7, structureboundingbox);
            this.setBlockState(world, iblockdata, 3, 1, 6, structureboundingbox);
            this.setBlockState(world, iblockdata, 3, 1, 7, structureboundingbox);
            this.setBlockState(world, iblockdata1, 1, 1, 5, structureboundingbox);
            this.setBlockState(world, iblockdata1, 2, 1, 6, structureboundingbox);
            this.setBlockState(world, iblockdata1, 3, 1, 5, structureboundingbox);
            this.setBlockState(world, iblockdata2, 1, 2, 7, structureboundingbox);
            this.setBlockState(world, iblockdata3, 3, 2, 7, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 6, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 7, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 6, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 7, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 6, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 7, 0, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 6, 4, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 7, 4, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 6, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 6, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 3, 8, structureboundingbox);
            this.placeTorch(world, EnumFacing.SOUTH, 2, 4, 7, structureboundingbox);
            this.placeTorch(world, EnumFacing.EAST, 1, 4, 6, structureboundingbox);
            this.placeTorch(world, EnumFacing.WEST, 3, 4, 6, structureboundingbox);
            this.placeTorch(world, EnumFacing.NORTH, 2, 4, 5, structureboundingbox);
            IBlockState iblockdata4 = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.WEST);

            int i;

            for (i = 1; i <= 9; ++i) {
                this.setBlockState(world, iblockdata4, 3, i, 3, structureboundingbox);
            }

            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 1, 0, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 2, 0, structureboundingbox);
            this.createVillageDoor(world, structureboundingbox, random, 2, 1, 0, EnumFacing.NORTH);
            if (this.getBlockStateFromPos(world, 2, 0, -1, structureboundingbox).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getMaterial() != Material.AIR) {
                this.setBlockState(world, iblockdata1, 2, 0, -1, structureboundingbox);
                if (this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(world, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureboundingbox);
                }
            }

            for (i = 0; i < 9; ++i) {
                for (int j = 0; j < 5; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 12, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.spawnVillagers(world, structureboundingbox, 2, 1, 2, 1);
            return true;
        }

        protected int chooseProfession(int i, int j) {
            return 2;
        }
    }

    public static class House4Garden extends StructureVillagePieces.Village {

        private boolean isRoofAccessible;

        public House4Garden() {}

        public House4Garden(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.isRoofAccessible = random.nextBoolean();
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Terrace", this.isRoofAccessible);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.isRoofAccessible = nbttagcompound.getBoolean("Terrace");
        }

        public static StructureVillagePieces.House4Garden createPiece(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 5, 6, 5, enumdirection);

            return StructureComponent.findIntersecting(list, structureboundingbox) != null ? null : new StructureVillagePieces.House4Garden(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            IBlockState iblockdata2 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockdata3 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
            IBlockState iblockdata4 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 0, 4, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 4, 0, 4, 4, 4, iblockdata3, iblockdata3, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 1, 3, 4, 3, iblockdata1, iblockdata1, false);
            this.setBlockState(world, iblockdata, 0, 1, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 0, 2, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 0, 3, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 4, 1, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 4, 2, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 4, 3, 0, structureboundingbox);
            this.setBlockState(world, iblockdata, 0, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata, 0, 2, 4, structureboundingbox);
            this.setBlockState(world, iblockdata, 0, 3, 4, structureboundingbox);
            this.setBlockState(world, iblockdata, 4, 1, 4, structureboundingbox);
            this.setBlockState(world, iblockdata, 4, 2, 4, structureboundingbox);
            this.setBlockState(world, iblockdata, 4, 3, 4, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, iblockdata1, iblockdata1, false);
            this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 3, iblockdata1, iblockdata1, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 4, 3, 3, 4, iblockdata1, iblockdata1, false);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 4, structureboundingbox);
            this.setBlockState(world, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, structureboundingbox);
            this.setBlockState(world, iblockdata1, 1, 1, 0, structureboundingbox);
            this.setBlockState(world, iblockdata1, 1, 2, 0, structureboundingbox);
            this.setBlockState(world, iblockdata1, 1, 3, 0, structureboundingbox);
            this.setBlockState(world, iblockdata1, 2, 3, 0, structureboundingbox);
            this.setBlockState(world, iblockdata1, 3, 3, 0, structureboundingbox);
            this.setBlockState(world, iblockdata1, 3, 2, 0, structureboundingbox);
            this.setBlockState(world, iblockdata1, 3, 1, 0, structureboundingbox);
            if (this.getBlockStateFromPos(world, 2, 0, -1, structureboundingbox).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getMaterial() != Material.AIR) {
                this.setBlockState(world, iblockdata2, 2, 0, -1, structureboundingbox);
                if (this.getBlockStateFromPos(world, 2, -1, -1, structureboundingbox).getBlock() == Blocks.GRASS_PATH) {
                    this.setBlockState(world, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureboundingbox);
                }
            }

            this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 3, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            if (this.isRoofAccessible) {
                this.setBlockState(world, iblockdata4, 0, 5, 0, structureboundingbox);
                this.setBlockState(world, iblockdata4, 1, 5, 0, structureboundingbox);
                this.setBlockState(world, iblockdata4, 2, 5, 0, structureboundingbox);
                this.setBlockState(world, iblockdata4, 3, 5, 0, structureboundingbox);
                this.setBlockState(world, iblockdata4, 4, 5, 0, structureboundingbox);
                this.setBlockState(world, iblockdata4, 0, 5, 4, structureboundingbox);
                this.setBlockState(world, iblockdata4, 1, 5, 4, structureboundingbox);
                this.setBlockState(world, iblockdata4, 2, 5, 4, structureboundingbox);
                this.setBlockState(world, iblockdata4, 3, 5, 4, structureboundingbox);
                this.setBlockState(world, iblockdata4, 4, 5, 4, structureboundingbox);
                this.setBlockState(world, iblockdata4, 4, 5, 1, structureboundingbox);
                this.setBlockState(world, iblockdata4, 4, 5, 2, structureboundingbox);
                this.setBlockState(world, iblockdata4, 4, 5, 3, structureboundingbox);
                this.setBlockState(world, iblockdata4, 0, 5, 1, structureboundingbox);
                this.setBlockState(world, iblockdata4, 0, 5, 2, structureboundingbox);
                this.setBlockState(world, iblockdata4, 0, 5, 3, structureboundingbox);
            }

            if (this.isRoofAccessible) {
                IBlockState iblockdata5 = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.SOUTH);

                this.setBlockState(world, iblockdata5, 3, 1, 3, structureboundingbox);
                this.setBlockState(world, iblockdata5, 3, 2, 3, structureboundingbox);
                this.setBlockState(world, iblockdata5, 3, 3, 3, structureboundingbox);
                this.setBlockState(world, iblockdata5, 3, 4, 3, structureboundingbox);
            }

            this.placeTorch(world, EnumFacing.NORTH, 2, 3, 1, structureboundingbox);

            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    this.clearCurrentPositionBlocksUpwards(world, j, 6, i, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.spawnVillagers(world, structureboundingbox, 1, 1, 2, 1);
            return true;
        }
    }

    public static class Path extends StructureVillagePieces.Road {

        private int length;

        public Path() {}

        public Path(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.length = Math.max(structureboundingbox.getXSize(), structureboundingbox.getZSize());
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setInteger("Length", this.length);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.length = nbttagcompound.getInteger("Length");
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            boolean flag = false;

            int i;
            StructureComponent structurepiece1;

            for (i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5)) {
                structurepiece1 = this.getNextComponentNN((StructureVillagePieces.Start) structurepiece, list, random, 0, i);
                if (structurepiece1 != null) {
                    i += Math.max(structurepiece1.boundingBox.getXSize(), structurepiece1.boundingBox.getZSize());
                    flag = true;
                }
            }

            for (i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5)) {
                structurepiece1 = this.getNextComponentPP((StructureVillagePieces.Start) structurepiece, list, random, 0, i);
                if (structurepiece1 != null) {
                    i += Math.max(structurepiece1.boundingBox.getXSize(), structurepiece1.boundingBox.getZSize());
                    flag = true;
                }
            }

            EnumFacing enumdirection = this.getCoordBaseMode();

            if (flag && random.nextInt(3) > 0 && enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                    break;

                case SOUTH:
                    StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                    break;

                case WEST:
                    StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    break;

                case EAST:
                    StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }

            if (flag && random.nextInt(3) > 0 && enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                    break;

                case SOUTH:
                    StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                    break;

                case WEST:
                    StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    break;

                case EAST:
                    StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }

        }

        public static StructureBoundingBox findPieceBox(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            for (int l = 7 * MathHelper.getInt(random, 3, 5); l >= 7; l -= 7) {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, 0, 0, 0, 3, 3, l, enumdirection);

                if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
                    return structureboundingbox;
                }
            }

            return null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.GRASS_PATH.getDefaultState());
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
            IBlockState iblockdata2 = this.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState());
            IBlockState iblockdata3 = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());

            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                    BlockPos blockposition = new BlockPos(i, 64, j);

                    if (structureboundingbox.isVecInside((Vec3i) blockposition)) {
                        blockposition = world.getTopSolidOrLiquidBlock(blockposition).down();
                        if (blockposition.getY() < world.getSeaLevel()) {
                            blockposition = new BlockPos(blockposition.getX(), world.getSeaLevel() - 1, blockposition.getZ());
                        }

                        while (blockposition.getY() >= world.getSeaLevel() - 1) {
                            IBlockState iblockdata4 = world.getBlockState(blockposition);

                            if (iblockdata4.getBlock() == Blocks.GRASS && world.isAirBlock(blockposition.up())) {
                                world.setBlockState(blockposition, iblockdata, 2);
                                break;
                            }

                            if (iblockdata4.getMaterial().isLiquid()) {
                                world.setBlockState(blockposition, iblockdata1, 2);
                                break;
                            }

                            if (iblockdata4.getBlock() == Blocks.SAND || iblockdata4.getBlock() == Blocks.SANDSTONE || iblockdata4.getBlock() == Blocks.RED_SANDSTONE) {
                                world.setBlockState(blockposition, iblockdata2, 2);
                                world.setBlockState(blockposition.down(), iblockdata3, 2);
                                break;
                            }

                            blockposition = blockposition.down();
                        }
                    }
                }
            }

            return true;
        }
    }

    public abstract static class Road extends StructureVillagePieces.Village {

        public Road() {}

        protected Road(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
        }
    }

    public static class Start extends StructureVillagePieces.Well {

        public BiomeProvider biomeProvider;
        public int terrainType;
        public StructureVillagePieces.PieceWeight lastPlaced;
        public List<StructureVillagePieces.PieceWeight> structureVillageWeightedPieceList;
        public List<StructureComponent> pendingHouses = Lists.newArrayList();
        public List<StructureComponent> pendingRoads = Lists.newArrayList();

        public Start() {}

        public Start(BiomeProvider worldchunkmanager, int i, Random random, int j, int k, List<StructureVillagePieces.PieceWeight> list, int l) {
            super((StructureVillagePieces.Start) null, 0, random, j, k);
            this.biomeProvider = worldchunkmanager;
            this.structureVillageWeightedPieceList = list;
            this.terrainType = l;
            Biome biomebase = worldchunkmanager.getBiome(new BlockPos(j, 0, k), Biomes.DEFAULT);

            if (biomebase instanceof BiomeDesert) {
                this.structureType = 1;
            } else if (biomebase instanceof BiomeSavanna) {
                this.structureType = 2;
            } else if (biomebase instanceof BiomeTaiga) {
                this.structureType = 3;
            }

            this.setStructureType(this.structureType);
            this.isZombieInfested = random.nextInt(50) == 0;
        }
    }

    public static class Well extends StructureVillagePieces.Village {

        public Well() {}

        public Well(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, int j, int k) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(j, 64, k, j + 6 - 1, 78, k + 6 - 1);
            } else {
                this.boundingBox = new StructureBoundingBox(j, 64, k, j + 6 - 1, 78, k + 6 - 1);
            }

        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
            StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
            StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            StructureVillagePieces.generateAndAddRoadPiece((StructureVillagePieces.Start) structurepiece, list, random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(world, structureboundingbox);
                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
            }

            IBlockState iblockdata = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());

            this.fillWithBlocks(world, structureboundingbox, 1, 0, 1, 4, 12, 4, iblockdata, Blocks.FLOWING_WATER.getDefaultState(), false);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 12, 2, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 3, 12, 2, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 12, 3, structureboundingbox);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 3, 12, 3, structureboundingbox);
            this.setBlockState(world, iblockdata1, 1, 13, 1, structureboundingbox);
            this.setBlockState(world, iblockdata1, 1, 14, 1, structureboundingbox);
            this.setBlockState(world, iblockdata1, 4, 13, 1, structureboundingbox);
            this.setBlockState(world, iblockdata1, 4, 14, 1, structureboundingbox);
            this.setBlockState(world, iblockdata1, 1, 13, 4, structureboundingbox);
            this.setBlockState(world, iblockdata1, 1, 14, 4, structureboundingbox);
            this.setBlockState(world, iblockdata1, 4, 13, 4, structureboundingbox);
            this.setBlockState(world, iblockdata1, 4, 14, 4, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 1, 15, 1, 4, 15, 4, iblockdata, iblockdata, false);

            for (int i = 0; i <= 5; ++i) {
                for (int j = 0; j <= 5; ++j) {
                    if (j == 0 || j == 5 || i == 0 || i == 5) {
                        this.setBlockState(world, iblockdata, j, 11, i, structureboundingbox);
                        this.clearCurrentPositionBlocksUpwards(world, j, 12, i, structureboundingbox);
                    }
                }
            }

            return true;
        }
    }

    abstract static class Village extends StructureComponent {

        protected int averageGroundLvl = -1;
        private int villagersSpawned;
        protected int structureType;
        protected boolean isZombieInfested;

        public Village() {}

        protected Village(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i) {
            super(i);
            if (worldgenvillagepieces_worldgenvillagestartpiece != null) {
                this.structureType = worldgenvillagepieces_worldgenvillagestartpiece.structureType;
                this.isZombieInfested = worldgenvillagepieces_worldgenvillagestartpiece.isZombieInfested;
            }

        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            nbttagcompound.setInteger("HPos", this.averageGroundLvl);
            nbttagcompound.setInteger("VCount", this.villagersSpawned);
            nbttagcompound.setByte("Type", (byte) this.structureType);
            nbttagcompound.setBoolean("Zombie", this.isZombieInfested);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            this.averageGroundLvl = nbttagcompound.getInteger("HPos");
            this.villagersSpawned = nbttagcompound.getInteger("VCount");
            this.structureType = nbttagcompound.getByte("Type");
            if (nbttagcompound.getBoolean("Desert")) {
                this.structureType = 1;
            }

            this.isZombieInfested = nbttagcompound.getBoolean("Zombie");
        }

        @Nullable
        protected StructureComponent getNextComponentNN(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    return StructureVillagePieces.generateAndAddComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.WEST, this.getComponentType());

                case SOUTH:
                    return StructureVillagePieces.generateAndAddComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.WEST, this.getComponentType());

                case WEST:
                    return StructureVillagePieces.generateAndAddComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());

                case EAST:
                    return StructureVillagePieces.generateAndAddComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            } else {
                return null;
            }
        }

        @Nullable
        protected StructureComponent getNextComponentPP(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    return StructureVillagePieces.generateAndAddComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.EAST, this.getComponentType());

                case SOUTH:
                    return StructureVillagePieces.generateAndAddComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.EAST, this.getComponentType());

                case WEST:
                    return StructureVillagePieces.generateAndAddComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());

                case EAST:
                    return StructureVillagePieces.generateAndAddComponent(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            } else {
                return null;
            }
        }

        protected int getAverageGroundLevel(World world, StructureBoundingBox structureboundingbox) {
            int i = 0;
            int j = 0;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                    blockposition_mutableblockposition.setPos(l, 64, k);
                    if (structureboundingbox.isVecInside((Vec3i) blockposition_mutableblockposition)) {
                        i += Math.max(world.getTopSolidOrLiquidBlock(blockposition_mutableblockposition).getY(), world.provider.getAverageGroundLevel() - 1);
                        ++j;
                    }
                }
            }

            if (j == 0) {
                return -1;
            } else {
                return i / j;
            }
        }

        protected static boolean canVillageGoDeeper(StructureBoundingBox structureboundingbox) {
            return structureboundingbox != null && structureboundingbox.minY > 10;
        }

        protected void spawnVillagers(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
            if (this.villagersSpawned < l) {
                for (int i1 = this.villagersSpawned; i1 < l; ++i1) {
                    int j1 = this.getXWithOffset(i + i1, k);
                    int k1 = this.getYWithOffset(j);
                    int l1 = this.getZWithOffset(i + i1, k);

                    if (!structureboundingbox.isVecInside((Vec3i) (new BlockPos(j1, k1, l1)))) {
                        break;
                    }

                    ++this.villagersSpawned;
                    if (this.isZombieInfested) {
                        EntityZombieVillager entityzombievillager = new EntityZombieVillager(world);

                        entityzombievillager.setLocationAndAngles((double) j1 + 0.5D, (double) k1, (double) l1 + 0.5D, 0.0F, 0.0F);
                        entityzombievillager.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityzombievillager)), (IEntityLivingData) null);
                        entityzombievillager.setProfession(this.chooseProfession(i1, 0));
                        entityzombievillager.enablePersistence();
                        world.addEntity(entityzombievillager, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
                    } else {
                        EntityVillager entityvillager = new EntityVillager(world);

                        entityvillager.setLocationAndAngles((double) j1 + 0.5D, (double) k1, (double) l1 + 0.5D, 0.0F, 0.0F);
                        entityvillager.setProfession(this.chooseProfession(i1, world.rand.nextInt(6)));
                        entityvillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData) null, false);
                        world.addEntity(entityvillager, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
                    }
                }

            }
        }

        protected int chooseProfession(int i, int j) {
            return j;
        }

        protected IBlockState getBiomeSpecificBlockState(IBlockState iblockdata) {
            if (this.structureType == 1) {
                if (iblockdata.getBlock() == Blocks.LOG || iblockdata.getBlock() == Blocks.LOG2) {
                    return Blocks.SANDSTONE.getDefaultState();
                }

                if (iblockdata.getBlock() == Blocks.COBBLESTONE) {
                    return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
                }

                if (iblockdata.getBlock() == Blocks.PLANKS) {
                    return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
                }

                if (iblockdata.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, iblockdata.getValue(BlockStairs.FACING));
                }

                if (iblockdata.getBlock() == Blocks.STONE_STAIRS) {
                    return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, iblockdata.getValue(BlockStairs.FACING));
                }

                if (iblockdata.getBlock() == Blocks.GRAVEL) {
                    return Blocks.SANDSTONE.getDefaultState();
                }
            } else if (this.structureType == 3) {
                if (iblockdata.getBlock() == Blocks.LOG || iblockdata.getBlock() == Blocks.LOG2) {
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, iblockdata.getValue(BlockLog.LOG_AXIS));
                }

                if (iblockdata.getBlock() == Blocks.PLANKS) {
                    return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
                }

                if (iblockdata.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, iblockdata.getValue(BlockStairs.FACING));
                }

                if (iblockdata.getBlock() == Blocks.OAK_FENCE) {
                    return Blocks.SPRUCE_FENCE.getDefaultState();
                }
            } else if (this.structureType == 2) {
                if (iblockdata.getBlock() == Blocks.LOG || iblockdata.getBlock() == Blocks.LOG2) {
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, iblockdata.getValue(BlockLog.LOG_AXIS));
                }

                if (iblockdata.getBlock() == Blocks.PLANKS) {
                    return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
                }

                if (iblockdata.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.ACACIA_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, iblockdata.getValue(BlockStairs.FACING));
                }

                if (iblockdata.getBlock() == Blocks.COBBLESTONE) {
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
                }

                if (iblockdata.getBlock() == Blocks.OAK_FENCE) {
                    return Blocks.ACACIA_FENCE.getDefaultState();
                }
            }

            return iblockdata;
        }

        protected BlockDoor biomeDoor() {
            switch (this.structureType) {
            case 2:
                return Blocks.ACACIA_DOOR;

            case 3:
                return Blocks.SPRUCE_DOOR;

            default:
                return Blocks.OAK_DOOR;
            }
        }

        protected void createVillageDoor(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, EnumFacing enumdirection) {
            if (!this.isZombieInfested) {
                this.generateDoor(world, structureboundingbox, random, i, j, k, EnumFacing.NORTH, this.biomeDoor());
            }

        }

        protected void placeTorch(World world, EnumFacing enumdirection, int i, int j, int k, StructureBoundingBox structureboundingbox) {
            if (!this.isZombieInfested) {
                this.setBlockState(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, enumdirection), i, j, k, structureboundingbox);
            }

        }

        protected void replaceAirAndLiquidDownwards(World world, IBlockState iblockdata, int i, int j, int k, StructureBoundingBox structureboundingbox) {
            IBlockState iblockdata1 = this.getBiomeSpecificBlockState(iblockdata);

            super.replaceAirAndLiquidDownwards(world, iblockdata1, i, j, k, structureboundingbox);
        }

        protected void setStructureType(int i) {
            this.structureType = i;
        }
    }

    public static class PieceWeight {

        public Class<? extends StructureVillagePieces.Village> villagePieceClass;
        public final int villagePieceWeight;
        public int villagePiecesSpawned;
        public int villagePiecesLimit;

        public PieceWeight(Class<? extends StructureVillagePieces.Village> oclass, int i, int j) {
            this.villagePieceClass = oclass;
            this.villagePieceWeight = i;
            this.villagePiecesLimit = j;
        }

        public boolean canSpawnMoreVillagePiecesOfType(int i) {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }

        public boolean canSpawnMoreVillagePieces() {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
    }
}
