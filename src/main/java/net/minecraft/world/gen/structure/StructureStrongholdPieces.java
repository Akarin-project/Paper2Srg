package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureStrongholdPieces {

    private static final StructureStrongholdPieces.PieceWeight[] PIECE_WEIGHTS = new StructureStrongholdPieces.PieceWeight[] { new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Straight.class, 40, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Prison.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.LeftTurn.class, 20, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RightTurn.class, 20, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RoomCrossing.class, 10, 6), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.StairsStraight.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Stairs.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Crossing.class, 5, 4), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.ChestCorridor.class, 5, 4), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Library.class, 10, 2) {
        public boolean canSpawnMoreStructuresOfType(int i) {
            return super.canSpawnMoreStructuresOfType(i) && i > 4;
        }
    }, new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.PortalRoom.class, 20, 1) {
        public boolean canSpawnMoreStructuresOfType(int i) {
            return super.canSpawnMoreStructuresOfType(i) && i > 5;
        }
    }};
    private static List<StructureStrongholdPieces.PieceWeight> structurePieceList;
    private static Class<? extends StructureStrongholdPieces.Stronghold> strongComponentType;
    static int totalWeight;
    private static final StructureStrongholdPieces.Stones STRONGHOLD_STONES = new StructureStrongholdPieces.Stones(null);

    public static void registerStrongholdPieces() {
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.ChestCorridor.class, "SHCC");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Corridor.class, "SHFC");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Crossing.class, "SH5C");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.LeftTurn.class, "SHLT");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Library.class, "SHLi");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.PortalRoom.class, "SHPR");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Prison.class, "SHPH");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RightTurn.class, "SHRT");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RoomCrossing.class, "SHRC");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs.class, "SHSD");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs2.class, "SHStart");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Straight.class, "SHS");
        MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.StairsStraight.class, "SHSSD");
    }

    public static void prepareStructurePieces() {
        StructureStrongholdPieces.structurePieceList = Lists.newArrayList();
        StructureStrongholdPieces.PieceWeight[] aworldgenstrongholdpieces_worldgenstrongholdpieceweight = StructureStrongholdPieces.PIECE_WEIGHTS;
        int i = aworldgenstrongholdpieces_worldgenstrongholdpieceweight.length;

        for (int j = 0; j < i; ++j) {
            StructureStrongholdPieces.PieceWeight worldgenstrongholdpieces_worldgenstrongholdpieceweight = aworldgenstrongholdpieces_worldgenstrongholdpieceweight[j];

            worldgenstrongholdpieces_worldgenstrongholdpieceweight.instancesSpawned = 0;
            StructureStrongholdPieces.structurePieceList.add(worldgenstrongholdpieces_worldgenstrongholdpieceweight);
        }

        StructureStrongholdPieces.strongComponentType = null;
    }

    private static boolean canAddStructurePieces() {
        boolean flag = false;

        StructureStrongholdPieces.totalWeight = 0;

        StructureStrongholdPieces.PieceWeight worldgenstrongholdpieces_worldgenstrongholdpieceweight;

        for (Iterator iterator = StructureStrongholdPieces.structurePieceList.iterator(); iterator.hasNext(); StructureStrongholdPieces.totalWeight += worldgenstrongholdpieces_worldgenstrongholdpieceweight.pieceWeight) {
            worldgenstrongholdpieces_worldgenstrongholdpieceweight = (StructureStrongholdPieces.PieceWeight) iterator.next();
            if (worldgenstrongholdpieces_worldgenstrongholdpieceweight.instancesLimit > 0 && worldgenstrongholdpieces_worldgenstrongholdpieceweight.instancesSpawned < worldgenstrongholdpieces_worldgenstrongholdpieceweight.instancesLimit) {
                flag = true;
            }
        }

        return flag;
    }

    private static StructureStrongholdPieces.Stronghold findAndCreatePieceFactory(Class<? extends StructureStrongholdPieces.Stronghold> oclass, List<StructureComponent> list, Random random, int i, int j, int k, @Nullable EnumFacing enumdirection, int l) {
        Object object = null;

        if (oclass == StructureStrongholdPieces.Straight.class) {
            object = StructureStrongholdPieces.Straight.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.Prison.class) {
            object = StructureStrongholdPieces.Prison.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.LeftTurn.class) {
            object = StructureStrongholdPieces.LeftTurn.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.RightTurn.class) {
            object = StructureStrongholdPieces.RightTurn.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.RoomCrossing.class) {
            object = StructureStrongholdPieces.RoomCrossing.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.StairsStraight.class) {
            object = StructureStrongholdPieces.StairsStraight.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.Stairs.class) {
            object = StructureStrongholdPieces.Stairs.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.Crossing.class) {
            object = StructureStrongholdPieces.Crossing.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.ChestCorridor.class) {
            object = StructureStrongholdPieces.ChestCorridor.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.Library.class) {
            object = StructureStrongholdPieces.Library.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.PortalRoom.class) {
            object = StructureStrongholdPieces.PortalRoom.createPiece(list, random, i, j, k, enumdirection, l);
        }

        return (StructureStrongholdPieces.Stronghold) object;
    }

    private static StructureStrongholdPieces.Stronghold generatePieceFromSmallDoor(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        if (!canAddStructurePieces()) {
            return null;
        } else {
            if (StructureStrongholdPieces.strongComponentType != null) {
                StructureStrongholdPieces.Stronghold worldgenstrongholdpieces_worldgenstrongholdpiece = findAndCreatePieceFactory(StructureStrongholdPieces.strongComponentType, list, random, i, j, k, enumdirection, l);

                StructureStrongholdPieces.strongComponentType = null;
                if (worldgenstrongholdpieces_worldgenstrongholdpiece != null) {
                    return worldgenstrongholdpieces_worldgenstrongholdpiece;
                }
            }

            int i1 = 0;

            while (i1 < 5) {
                ++i1;
                int j1 = random.nextInt(StructureStrongholdPieces.totalWeight);
                Iterator iterator = StructureStrongholdPieces.structurePieceList.iterator();

                while (iterator.hasNext()) {
                    StructureStrongholdPieces.PieceWeight worldgenstrongholdpieces_worldgenstrongholdpieceweight = (StructureStrongholdPieces.PieceWeight) iterator.next();

                    j1 -= worldgenstrongholdpieces_worldgenstrongholdpieceweight.pieceWeight;
                    if (j1 < 0) {
                        if (!worldgenstrongholdpieces_worldgenstrongholdpieceweight.canSpawnMoreStructuresOfType(l) || worldgenstrongholdpieces_worldgenstrongholdpieceweight == worldgenstrongholdpieces_worldgenstrongholdstart.lastPlaced) {
                            break;
                        }

                        StructureStrongholdPieces.Stronghold worldgenstrongholdpieces_worldgenstrongholdpiece1 = findAndCreatePieceFactory(worldgenstrongholdpieces_worldgenstrongholdpieceweight.pieceClass, list, random, i, j, k, enumdirection, l);

                        if (worldgenstrongholdpieces_worldgenstrongholdpiece1 != null) {
                            ++worldgenstrongholdpieces_worldgenstrongholdpieceweight.instancesSpawned;
                            worldgenstrongholdpieces_worldgenstrongholdstart.lastPlaced = worldgenstrongholdpieces_worldgenstrongholdpieceweight;
                            if (!worldgenstrongholdpieces_worldgenstrongholdpieceweight.canSpawnMoreStructures()) {
                                StructureStrongholdPieces.structurePieceList.remove(worldgenstrongholdpieces_worldgenstrongholdpieceweight);
                            }

                            return worldgenstrongholdpieces_worldgenstrongholdpiece1;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = StructureStrongholdPieces.Corridor.findPieceBox(list, random, i, j, k, enumdirection);

            if (structureboundingbox != null && structureboundingbox.minY > 1) {
                return new StructureStrongholdPieces.Corridor(l, random, structureboundingbox, enumdirection);
            } else {
                return null;
            }
        }
    }

    private static StructureComponent generateAndAddPiece(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j, int k, @Nullable EnumFacing enumdirection, int l) {
        if (l > 50) {
            return null;
        } else if (Math.abs(i - worldgenstrongholdpieces_worldgenstrongholdstart.getBoundingBox().minX) <= 112 && Math.abs(k - worldgenstrongholdpieces_worldgenstrongholdstart.getBoundingBox().minZ) <= 112) {
            StructureStrongholdPieces.Stronghold worldgenstrongholdpieces_worldgenstrongholdpiece = generatePieceFromSmallDoor(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, i, j, k, enumdirection, l + 1);

            if (worldgenstrongholdpieces_worldgenstrongholdpiece != null) {
                list.add(worldgenstrongholdpieces_worldgenstrongholdpiece);
                worldgenstrongholdpieces_worldgenstrongholdstart.pendingChildren.add(worldgenstrongholdpieces_worldgenstrongholdpiece);
            }

            return worldgenstrongholdpieces_worldgenstrongholdpiece;
        } else {
            return null;
        }
    }

    static class Stones extends StructureComponent.BlockSelector {

        private Stones() {}

        public void selectBlocks(Random random, int i, int j, int k, boolean flag) {
            if (flag) {
                float f = random.nextFloat();

                if (f < 0.2F) {
                    this.blockstate = Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.CRACKED_META);
                } else if (f < 0.5F) {
                    this.blockstate = Blocks.STONEBRICK.getStateFromMeta(BlockStoneBrick.MOSSY_META);
                } else if (f < 0.55F) {
                    this.blockstate = Blocks.MONSTER_EGG.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata());
                } else {
                    this.blockstate = Blocks.STONEBRICK.getDefaultState();
                }
            } else {
                this.blockstate = Blocks.AIR.getDefaultState();
            }

        }

        Stones(Object object) {
            this();
        }
    }

    public static class PortalRoom extends StructureStrongholdPieces.Stronghold {

        private boolean hasSpawner;

        public PortalRoom() {}

        public PortalRoom(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Mob", this.hasSpawner);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.hasSpawner = nbttagcompound.getBoolean("Mob");
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            if (structurepiece != null) {
                ((StructureStrongholdPieces.Stairs2) structurepiece).strongholdPortalRoom = this;
            }

        }

        public static StructureStrongholdPieces.PortalRoom createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -1, 0, 11, 8, 16, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.PortalRoom(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 10, 7, 15, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
            byte b0 = 6;

            this.fillWithRandomizedBlocks(world, structureboundingbox, 1, b0, 1, 1, b0, 14, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(world, structureboundingbox, 9, b0, 1, 9, b0, 14, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(world, structureboundingbox, 2, b0, 1, 8, b0, 2, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(world, structureboundingbox, 2, b0, 14, 8, b0, 14, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 1, 1, 2, 1, 4, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(world, structureboundingbox, 8, 1, 1, 9, 1, 4, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 1, 1, 3, Blocks.FLOWING_LAVA.getDefaultState(), Blocks.FLOWING_LAVA.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 9, 1, 1, 9, 1, 3, Blocks.FLOWING_LAVA.getDefaultState(), Blocks.FLOWING_LAVA.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, structureboundingbox, 3, 1, 8, 7, 1, 12, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithBlocks(world, structureboundingbox, 4, 1, 9, 6, 1, 11, Blocks.FLOWING_LAVA.getDefaultState(), Blocks.FLOWING_LAVA.getDefaultState(), false);

            int i;

            for (i = 3; i < 14; i += 2) {
                this.fillWithBlocks(world, structureboundingbox, 0, 3, i, 0, 4, i, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 10, 3, i, 10, 4, i, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            }

            for (i = 2; i < 9; i += 2) {
                this.fillWithBlocks(world, structureboundingbox, i, 3, 15, i, 4, 15, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
            }

            IBlockState iblockdata = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);

            this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 5, 6, 1, 7, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 2, 6, 6, 2, 7, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
            this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 3, 7, 6, 3, 7, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);

            for (int j = 4; j <= 6; ++j) {
                this.setBlockState(world, iblockdata, j, 1, 4, structureboundingbox);
                this.setBlockState(world, iblockdata, j, 2, 5, structureboundingbox);
                this.setBlockState(world, iblockdata, j, 3, 6, structureboundingbox);
            }

            IBlockState iblockdata1 = Blocks.END_PORTAL_FRAME.getDefaultState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.NORTH);
            IBlockState iblockdata2 = Blocks.END_PORTAL_FRAME.getDefaultState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.SOUTH);
            IBlockState iblockdata3 = Blocks.END_PORTAL_FRAME.getDefaultState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.EAST);
            IBlockState iblockdata4 = Blocks.END_PORTAL_FRAME.getDefaultState().withProperty(BlockEndPortalFrame.FACING, EnumFacing.WEST);
            boolean flag = true;
            boolean[] aboolean = new boolean[12];

            for (int k = 0; k < aboolean.length; ++k) {
                aboolean[k] = random.nextFloat() > 0.9F;
                flag &= aboolean[k];
            }

            this.setBlockState(world, iblockdata1.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[0])), 4, 3, 8, structureboundingbox);
            this.setBlockState(world, iblockdata1.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[1])), 5, 3, 8, structureboundingbox);
            this.setBlockState(world, iblockdata1.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[2])), 6, 3, 8, structureboundingbox);
            this.setBlockState(world, iblockdata2.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[3])), 4, 3, 12, structureboundingbox);
            this.setBlockState(world, iblockdata2.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[4])), 5, 3, 12, structureboundingbox);
            this.setBlockState(world, iblockdata2.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[5])), 6, 3, 12, structureboundingbox);
            this.setBlockState(world, iblockdata3.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[6])), 3, 3, 9, structureboundingbox);
            this.setBlockState(world, iblockdata3.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[7])), 3, 3, 10, structureboundingbox);
            this.setBlockState(world, iblockdata3.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[8])), 3, 3, 11, structureboundingbox);
            this.setBlockState(world, iblockdata4.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[9])), 7, 3, 9, structureboundingbox);
            this.setBlockState(world, iblockdata4.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[10])), 7, 3, 10, structureboundingbox);
            this.setBlockState(world, iblockdata4.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(aboolean[11])), 7, 3, 11, structureboundingbox);
            if (flag) {
                IBlockState iblockdata5 = Blocks.END_PORTAL.getDefaultState();

                this.setBlockState(world, iblockdata5, 4, 3, 9, structureboundingbox);
                this.setBlockState(world, iblockdata5, 5, 3, 9, structureboundingbox);
                this.setBlockState(world, iblockdata5, 6, 3, 9, structureboundingbox);
                this.setBlockState(world, iblockdata5, 4, 3, 10, structureboundingbox);
                this.setBlockState(world, iblockdata5, 5, 3, 10, structureboundingbox);
                this.setBlockState(world, iblockdata5, 6, 3, 10, structureboundingbox);
                this.setBlockState(world, iblockdata5, 4, 3, 11, structureboundingbox);
                this.setBlockState(world, iblockdata5, 5, 3, 11, structureboundingbox);
                this.setBlockState(world, iblockdata5, 6, 3, 11, structureboundingbox);
            }

            if (!this.hasSpawner) {
                int l = this.getYWithOffset(3);
                BlockPos blockposition = new BlockPos(this.getXWithOffset(5, 6), l, this.getZWithOffset(5, 6));

                if (structureboundingbox.isVecInside((Vec3i) blockposition)) {
                    this.hasSpawner = true;
                    world.setBlockState(blockposition, Blocks.MOB_SPAWNER.getDefaultState(), 2);
                    TileEntity tileentity = world.getTileEntity(blockposition);

                    if (tileentity instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntitySilverfish.class));
                    }
                }
            }

            return true;
        }
    }

    public static class Crossing extends StructureStrongholdPieces.Stronghold {

        private boolean leftLow;
        private boolean leftHigh;
        private boolean rightLow;
        private boolean rightHigh;

        public Crossing() {}

        public Crossing(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
            this.leftLow = random.nextBoolean();
            this.leftHigh = random.nextBoolean();
            this.rightLow = random.nextBoolean();
            this.rightHigh = random.nextInt(3) > 0;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("leftLow", this.leftLow);
            nbttagcompound.setBoolean("leftHigh", this.leftHigh);
            nbttagcompound.setBoolean("rightLow", this.rightLow);
            nbttagcompound.setBoolean("rightHigh", this.rightHigh);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.leftLow = nbttagcompound.getBoolean("leftLow");
            this.leftHigh = nbttagcompound.getBoolean("leftHigh");
            this.rightLow = nbttagcompound.getBoolean("rightLow");
            this.rightHigh = nbttagcompound.getBoolean("rightHigh");
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = 3;
            int j = 5;
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection == EnumFacing.WEST || enumdirection == EnumFacing.NORTH) {
                i = 8 - i;
                j = 8 - j;
            }

            this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 5, 1);
            if (this.leftLow) {
                this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurepiece, list, random, i, 1);
            }

            if (this.leftHigh) {
                this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurepiece, list, random, j, 7);
            }

            if (this.rightLow) {
                this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurepiece, list, random, i, 1);
            }

            if (this.rightHigh) {
                this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurepiece, list, random, j, 7);
            }

        }

        public static StructureStrongholdPieces.Crossing createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -3, 0, 10, 9, 11, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.Crossing(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 9, 8, 10, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 4, 3, 0);
                if (this.leftLow) {
                    this.fillWithBlocks(world, structureboundingbox, 0, 3, 1, 0, 5, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                if (this.rightLow) {
                    this.fillWithBlocks(world, structureboundingbox, 9, 3, 1, 9, 5, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                if (this.leftHigh) {
                    this.fillWithBlocks(world, structureboundingbox, 0, 5, 7, 0, 7, 9, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                if (this.rightHigh) {
                    this.fillWithBlocks(world, structureboundingbox, 9, 5, 7, 9, 7, 9, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                this.fillWithBlocks(world, structureboundingbox, 5, 1, 10, 7, 3, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 2, 1, 8, 2, 6, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 5, 4, 4, 9, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 8, 1, 5, 8, 4, 9, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 4, 7, 3, 4, 9, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 1, 3, 5, 3, 3, 6, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithBlocks(world, structureboundingbox, 1, 3, 4, 3, 3, 4, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 1, 4, 6, 3, 4, 6, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 5, 1, 7, 7, 1, 8, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithBlocks(world, structureboundingbox, 5, 1, 9, 7, 1, 9, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 5, 2, 7, 7, 2, 7, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 4, 5, 7, 4, 5, 9, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 8, 5, 7, 8, 5, 9, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 5, 5, 7, 7, 5, 9, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
                this.setBlockState(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 6, 5, 6, structureboundingbox);
                return true;
            }
        }
    }

    public static class Library extends StructureStrongholdPieces.Stronghold {

        private boolean isLargeRoom;

        public Library() {}

        public Library(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
            this.isLargeRoom = structureboundingbox.getYSize() > 6;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Tall", this.isLargeRoom);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.isLargeRoom = nbttagcompound.getBoolean("Tall");
        }

        public static StructureStrongholdPieces.Library createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -1, 0, 14, 11, 15, enumdirection);

            if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(list, structureboundingbox) != null) {
                structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -1, 0, 14, 6, 15, enumdirection);
                if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(list, structureboundingbox) != null) {
                    return null;
                }
            }

            return new StructureStrongholdPieces.Library(l, random, structureboundingbox, enumdirection);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                byte b0 = 11;

                if (!this.isLargeRoom) {
                    b0 = 6;
                }

                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 13, b0 - 1, 14, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 4, 1, 0);
                this.generateMaybeBox(world, structureboundingbox, random, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.WEB.getDefaultState(), Blocks.WEB.getDefaultState(), false, 0);
                boolean flag = true;
                boolean flag1 = true;

                int i;

                for (i = 1; i <= 13; ++i) {
                    if ((i - 1) % 4 == 0) {
                        this.fillWithBlocks(world, structureboundingbox, 1, 1, i, 1, 4, i, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                        this.fillWithBlocks(world, structureboundingbox, 12, 1, i, 12, 4, i, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                        this.setBlockState(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST), 2, 3, i, structureboundingbox);
                        this.setBlockState(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST), 11, 3, i, structureboundingbox);
                        if (this.isLargeRoom) {
                            this.fillWithBlocks(world, structureboundingbox, 1, 6, i, 1, 9, i, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                            this.fillWithBlocks(world, structureboundingbox, 12, 6, i, 12, 9, i, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                        }
                    } else {
                        this.fillWithBlocks(world, structureboundingbox, 1, 1, i, 1, 4, i, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                        this.fillWithBlocks(world, structureboundingbox, 12, 1, i, 12, 4, i, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                        if (this.isLargeRoom) {
                            this.fillWithBlocks(world, structureboundingbox, 1, 6, i, 1, 9, i, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                            this.fillWithBlocks(world, structureboundingbox, 12, 6, i, 12, 9, i, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                        }
                    }
                }

                for (i = 3; i < 12; i += 2) {
                    this.fillWithBlocks(world, structureboundingbox, 3, 1, i, 4, 3, i, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 6, 1, i, 7, 3, i, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 9, 1, i, 10, 3, i, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                }

                if (this.isLargeRoom) {
                    this.fillWithBlocks(world, structureboundingbox, 1, 5, 1, 3, 5, 13, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 10, 5, 1, 12, 5, 13, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 4, 5, 1, 9, 5, 2, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 4, 5, 12, 9, 5, 13, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
                    this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 9, 5, 11, structureboundingbox);
                    this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 8, 5, 11, structureboundingbox);
                    this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 9, 5, 10, structureboundingbox);
                    this.fillWithBlocks(world, structureboundingbox, 3, 6, 2, 3, 6, 12, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 10, 6, 2, 10, 6, 10, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 4, 6, 2, 9, 6, 2, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 4, 6, 12, 8, 6, 12, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 9, 6, 11, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 8, 6, 11, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 9, 6, 10, structureboundingbox);
                    IBlockState iblockdata = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.SOUTH);

                    this.setBlockState(world, iblockdata, 10, 1, 13, structureboundingbox);
                    this.setBlockState(world, iblockdata, 10, 2, 13, structureboundingbox);
                    this.setBlockState(world, iblockdata, 10, 3, 13, structureboundingbox);
                    this.setBlockState(world, iblockdata, 10, 4, 13, structureboundingbox);
                    this.setBlockState(world, iblockdata, 10, 5, 13, structureboundingbox);
                    this.setBlockState(world, iblockdata, 10, 6, 13, structureboundingbox);
                    this.setBlockState(world, iblockdata, 10, 7, 13, structureboundingbox);
                    boolean flag2 = true;
                    boolean flag3 = true;

                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 6, 9, 7, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 7, 9, 7, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 6, 8, 7, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 7, 8, 7, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 6, 7, 7, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 7, 7, 7, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 5, 7, 7, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 8, 7, 7, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 6, 7, 6, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 6, 7, 8, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 7, 7, 6, structureboundingbox);
                    this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 7, 7, 8, structureboundingbox);
                    IBlockState iblockdata1 = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.UP);

                    this.setBlockState(world, iblockdata1, 5, 8, 7, structureboundingbox);
                    this.setBlockState(world, iblockdata1, 8, 8, 7, structureboundingbox);
                    this.setBlockState(world, iblockdata1, 6, 8, 6, structureboundingbox);
                    this.setBlockState(world, iblockdata1, 6, 8, 8, structureboundingbox);
                    this.setBlockState(world, iblockdata1, 7, 8, 6, structureboundingbox);
                    this.setBlockState(world, iblockdata1, 7, 8, 8, structureboundingbox);
                }

                this.generateChest(world, structureboundingbox, random, 3, 3, 5, LootTableList.CHESTS_STRONGHOLD_LIBRARY);
                if (this.isLargeRoom) {
                    this.setBlockState(world, Blocks.AIR.getDefaultState(), 12, 9, 1, structureboundingbox);
                    this.generateChest(world, structureboundingbox, random, 12, 8, 1, LootTableList.CHESTS_STRONGHOLD_LIBRARY);
                }

                return true;
            }
        }
    }

    public static class Prison extends StructureStrongholdPieces.Stronghold {

        public Prison() {}

        public Prison(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
        }

        public static StructureStrongholdPieces.Prison createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 9, 5, 11, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.Prison(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 8, 4, 10, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 1, 1, 0);
                this.fillWithBlocks(world, structureboundingbox, 1, 1, 10, 3, 3, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 1, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 3, 4, 3, 3, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 7, 4, 3, 7, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithRandomizedBlocks(world, structureboundingbox, 4, 1, 9, 4, 3, 9, false, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.fillWithBlocks(world, structureboundingbox, 4, 1, 4, 4, 3, 6, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 5, 1, 5, 7, 3, 5, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), 4, 3, 2, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), 4, 3, 8, structureboundingbox);
                IBlockState iblockdata = Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.WEST);
                IBlockState iblockdata1 = Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.WEST).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER);

                this.setBlockState(world, iblockdata, 4, 1, 2, structureboundingbox);
                this.setBlockState(world, iblockdata1, 4, 2, 2, structureboundingbox);
                this.setBlockState(world, iblockdata, 4, 1, 8, structureboundingbox);
                this.setBlockState(world, iblockdata1, 4, 2, 8, structureboundingbox);
                return true;
            }
        }
    }

    public static class RoomCrossing extends StructureStrongholdPieces.Stronghold {

        protected int roomType;

        public RoomCrossing() {}

        public RoomCrossing(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
            this.roomType = random.nextInt(5);
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setInteger("Type", this.roomType);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.roomType = nbttagcompound.getInteger("Type");
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 4, 1);
            this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 4);
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 4);
        }

        public static StructureStrongholdPieces.RoomCrossing createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -4, -1, 0, 11, 7, 11, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.RoomCrossing(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 10, 6, 10, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 4, 1, 0);
                this.fillWithBlocks(world, structureboundingbox, 4, 1, 10, 6, 3, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 4, 0, 3, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 10, 1, 4, 10, 3, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                int i;

                switch (this.roomType) {
                case 0:
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 5, 1, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 5, 2, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 5, 3, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST), 4, 3, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST), 6, 3, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 5, 3, 4, structureboundingbox);
                    this.setBlockState(world, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH), 5, 3, 6, structureboundingbox);
                    this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), 4, 1, 4, structureboundingbox);
                    this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), 4, 1, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), 4, 1, 6, structureboundingbox);
                    this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), 6, 1, 4, structureboundingbox);
                    this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), 6, 1, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), 6, 1, 6, structureboundingbox);
                    this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), 5, 1, 4, structureboundingbox);
                    this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState(), 5, 1, 6, structureboundingbox);
                    break;

                case 1:
                    for (i = 0; i < 5; ++i) {
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3, 1, 3 + i, structureboundingbox);
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 7, 1, 3 + i, structureboundingbox);
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3 + i, 1, 3, structureboundingbox);
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3 + i, 1, 7, structureboundingbox);
                    }

                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 5, 1, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 5, 2, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 5, 3, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.FLOWING_WATER.getDefaultState(), 5, 4, 5, structureboundingbox);
                    break;

                case 2:
                    for (i = 1; i <= 9; ++i) {
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 1, 3, i, structureboundingbox);
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 9, 3, i, structureboundingbox);
                    }

                    for (i = 1; i <= 9; ++i) {
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), i, 3, 1, structureboundingbox);
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), i, 3, 9, structureboundingbox);
                    }

                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 5, 1, 4, structureboundingbox);
                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 5, 1, 6, structureboundingbox);
                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 5, 3, 4, structureboundingbox);
                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 5, 3, 6, structureboundingbox);
                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 4, 1, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 6, 1, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 4, 3, 5, structureboundingbox);
                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 6, 3, 5, structureboundingbox);

                    for (i = 1; i <= 3; ++i) {
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 4, i, 4, structureboundingbox);
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 6, i, 4, structureboundingbox);
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 4, i, 6, structureboundingbox);
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 6, i, 6, structureboundingbox);
                    }

                    this.setBlockState(world, Blocks.TORCH.getDefaultState(), 5, 3, 5, structureboundingbox);

                    for (i = 2; i <= 8; ++i) {
                        this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 2, 3, i, structureboundingbox);
                        this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 3, 3, i, structureboundingbox);
                        if (i <= 3 || i >= 7) {
                            this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 4, 3, i, structureboundingbox);
                            this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 5, 3, i, structureboundingbox);
                            this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 6, 3, i, structureboundingbox);
                        }

                        this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 7, 3, i, structureboundingbox);
                        this.setBlockState(world, Blocks.PLANKS.getDefaultState(), 8, 3, i, structureboundingbox);
                    }

                    IBlockState iblockdata = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.WEST);

                    this.setBlockState(world, iblockdata, 9, 1, 3, structureboundingbox);
                    this.setBlockState(world, iblockdata, 9, 2, 3, structureboundingbox);
                    this.setBlockState(world, iblockdata, 9, 3, 3, structureboundingbox);
                    this.generateChest(world, structureboundingbox, random, 3, 4, 8, LootTableList.CHESTS_STRONGHOLD_CROSSING);
                }

                return true;
            }
        }
    }

    public static class RightTurn extends StructureStrongholdPieces.LeftTurn {

        public RightTurn() {}

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.EAST) {
                this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            } else {
                this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            }

        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 4, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 1, 1, 0);
                EnumFacing enumdirection = this.getCoordBaseMode();

                if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.EAST) {
                    this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                return true;
            }
        }
    }

    public static class LeftTurn extends StructureStrongholdPieces.Stronghold {

        public LeftTurn() {}

        public LeftTurn(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.EAST) {
                this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            } else {
                this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            }

        }

        public static StructureStrongholdPieces.LeftTurn createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, 5, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.LeftTurn(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 4, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 1, 1, 0);
                EnumFacing enumdirection = this.getCoordBaseMode();

                if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.EAST) {
                    this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                return true;
            }
        }
    }

    public static class StairsStraight extends StructureStrongholdPieces.Stronghold {

        public StairsStraight() {}

        public StairsStraight(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
        }

        public static StructureStrongholdPieces.StairsStraight createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -7, 0, 5, 11, 8, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.StairsStraight(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 10, 7, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 1, 7, 0);
                this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 7);
                IBlockState iblockdata = Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

                for (int i = 0; i < 6; ++i) {
                    this.setBlockState(world, iblockdata, 1, 6 - i, 1 + i, structureboundingbox);
                    this.setBlockState(world, iblockdata, 2, 6 - i, 1 + i, structureboundingbox);
                    this.setBlockState(world, iblockdata, 3, 6 - i, 1 + i, structureboundingbox);
                    if (i < 5) {
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 1, 5 - i, 1 + i, structureboundingbox);
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 2, 5 - i, 1 + i, structureboundingbox);
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3, 5 - i, 1 + i, structureboundingbox);
                    }
                }

                return true;
            }
        }
    }

    public static class ChestCorridor extends StructureStrongholdPieces.Stronghold {

        private boolean hasMadeChest;

        public ChestCorridor() {}

        public ChestCorridor(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Chest", this.hasMadeChest);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.hasMadeChest = nbttagcompound.getBoolean("Chest");
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
        }

        public static StructureStrongholdPieces.ChestCorridor createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, 7, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.ChestCorridor(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 6, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 1, 1, 0);
                this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
                this.fillWithBlocks(world, structureboundingbox, 3, 1, 2, 3, 1, 4, Blocks.STONEBRICK.getDefaultState(), Blocks.STONEBRICK.getDefaultState(), false);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4, structureboundingbox);

                for (int i = 2; i <= 4; ++i) {
                    this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1, i, structureboundingbox);
                }

                if (!this.hasMadeChest && structureboundingbox.isVecInside((Vec3i) (new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3))))) {
                    this.hasMadeChest = true;
                    this.generateChest(world, structureboundingbox, random, 3, 2, 3, LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
                }

                return true;
            }
        }
    }

    public static class Straight extends StructureStrongholdPieces.Stronghold {

        private boolean expandsX;
        private boolean expandsZ;

        public Straight() {}

        public Straight(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
            this.expandsX = random.nextInt(2) == 0;
            this.expandsZ = random.nextInt(2) == 0;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Left", this.expandsX);
            nbttagcompound.setBoolean("Right", this.expandsZ);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.expandsX = nbttagcompound.getBoolean("Left");
            this.expandsZ = nbttagcompound.getBoolean("Right");
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            if (this.expandsX) {
                this.getNextComponentX((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 2);
            }

            if (this.expandsZ) {
                this.getNextComponentZ((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 2);
            }

        }

        public static StructureStrongholdPieces.Straight createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, 7, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.Straight(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 4, 6, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 1, 1, 0);
                this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
                IBlockState iblockdata = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST);
                IBlockState iblockdata1 = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST);

                this.randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 1, 2, 1, iblockdata);
                this.randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 3, 2, 1, iblockdata1);
                this.randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 1, 2, 5, iblockdata);
                this.randomlyPlaceBlock(world, structureboundingbox, random, 0.1F, 3, 2, 5, iblockdata1);
                if (this.expandsX) {
                    this.fillWithBlocks(world, structureboundingbox, 0, 1, 2, 0, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                if (this.expandsZ) {
                    this.fillWithBlocks(world, structureboundingbox, 4, 1, 2, 4, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }

                return true;
            }
        }
    }

    public static class Stairs2 extends StructureStrongholdPieces.Stairs {

        public StructureStrongholdPieces.PieceWeight lastPlaced;
        public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;
        public List<StructureComponent> pendingChildren = Lists.newArrayList();

        public Stairs2() {}

        public Stairs2(int i, Random random, int j, int k) {
            super(0, random, j, k);
        }
    }

    public static class Stairs extends StructureStrongholdPieces.Stronghold {

        private boolean source;

        public Stairs() {}

        public Stairs(int i, Random random, int j, int k) {
            super(i);
            this.source = true;
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            this.entryDoor = StructureStrongholdPieces.Stronghold.Door.OPENING;
            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(j, 64, k, j + 5 - 1, 74, k + 5 - 1);
            } else {
                this.boundingBox = new StructureBoundingBox(j, 64, k, j + 5 - 1, 74, k + 5 - 1);
            }

        }

        public Stairs(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.source = false;
            this.setCoordBaseMode(enumdirection);
            this.entryDoor = this.getRandomDoor(random);
            this.boundingBox = structureboundingbox;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Source", this.source);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.source = nbttagcompound.getBoolean("Source");
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            if (this.source) {
                StructureStrongholdPieces.strongComponentType = StructureStrongholdPieces.Crossing.class;
            }

            this.getNextComponentNormal((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
        }

        public static StructureStrongholdPieces.Stairs createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -7, 0, 5, 11, 5, enumdirection);

            return canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureStrongholdPieces.Stairs(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                this.fillWithRandomizedBlocks(world, structureboundingbox, 0, 0, 0, 4, 10, 4, true, random, StructureStrongholdPieces.STRONGHOLD_STONES);
                this.placeDoor(world, random, structureboundingbox, this.entryDoor, 1, 7, 0);
                this.placeDoor(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 4);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 2, 6, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 1, 5, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 1, 5, 2, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 1, 4, 3, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 2, 4, 3, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3, 3, 3, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3, 3, 2, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3, 2, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 2, 2, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 1, 1, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 1, 1, 2, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3, structureboundingbox);
                return true;
            }
        }
    }

    public static class Corridor extends StructureStrongholdPieces.Stronghold {

        private int steps;

        public Corridor() {}

        public Corridor(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.steps = enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.SOUTH ? structureboundingbox.getXSize() : structureboundingbox.getZSize();
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setInteger("Steps", this.steps);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.steps = nbttagcompound.getInteger("Steps");
        }

        public static StructureBoundingBox findPieceBox(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            boolean flag = true;
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, 4, enumdirection);
            StructureComponent structurepiece = StructureComponent.findIntersecting(list, structureboundingbox);

            if (structurepiece == null) {
                return null;
            } else {
                if (structurepiece.getBoundingBox().minY == structureboundingbox.minY) {
                    for (int l = 3; l >= 1; --l) {
                        structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, l - 1, enumdirection);
                        if (!structurepiece.getBoundingBox().intersectsWith(structureboundingbox)) {
                            return StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -1, 0, 5, 5, l, enumdirection);
                        }
                    }
                }

                return null;
            }
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.isLiquidInStructureBoundingBox(world, structureboundingbox)) {
                return false;
            } else {
                for (int i = 0; i < this.steps; ++i) {
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 0, 0, i, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 1, 0, i, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 2, 0, i, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3, 0, i, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 4, 0, i, structureboundingbox);

                    for (int j = 1; j <= 3; ++j) {
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 0, j, i, structureboundingbox);
                        this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, j, i, structureboundingbox);
                        this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, j, i, structureboundingbox);
                        this.setBlockState(world, Blocks.AIR.getDefaultState(), 3, j, i, structureboundingbox);
                        this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 4, j, i, structureboundingbox);
                    }

                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 0, 4, i, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 1, 4, i, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 2, 4, i, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3, 4, i, structureboundingbox);
                    this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 4, 4, i, structureboundingbox);
                }

                return true;
            }
        }
    }

    abstract static class Stronghold extends StructureComponent {

        protected StructureStrongholdPieces.Stronghold.Door entryDoor;

        public Stronghold() {
            this.entryDoor = StructureStrongholdPieces.Stronghold.Door.OPENING;
        }

        protected Stronghold(int i) {
            super(i);
            this.entryDoor = StructureStrongholdPieces.Stronghold.Door.OPENING;
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            nbttagcompound.setString("EntryDoor", this.entryDoor.name());
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            this.entryDoor = StructureStrongholdPieces.Stronghold.Door.valueOf(nbttagcompound.getString("EntryDoor"));
        }

        protected void placeDoor(World world, Random random, StructureBoundingBox structureboundingbox, StructureStrongholdPieces.Stronghold.Door worldgenstrongholdpieces_worldgenstrongholdpiece_worldgenstrongholddoortype, int i, int j, int k) {
            switch (worldgenstrongholdpieces_worldgenstrongholdpiece_worldgenstrongholddoortype) {
            case OPENING:
                this.fillWithBlocks(world, structureboundingbox, i, j, k, i + 3 - 1, j + 3 - 1, k, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                break;

            case WOOD_DOOR:
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i, j, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i, j + 1, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i + 1, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i + 2, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i + 2, j + 1, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i + 2, j, k, structureboundingbox);
                this.setBlockState(world, Blocks.OAK_DOOR.getDefaultState(), i + 1, j, k, structureboundingbox);
                this.setBlockState(world, Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), i + 1, j + 1, k, structureboundingbox);
                break;

            case GRATES:
                this.setBlockState(world, Blocks.AIR.getDefaultState(), i + 1, j, k, structureboundingbox);
                this.setBlockState(world, Blocks.AIR.getDefaultState(), i + 1, j + 1, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), i, j, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), i, j + 1, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), i, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), i + 1, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), i + 2, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), i + 2, j + 1, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), i + 2, j, k, structureboundingbox);
                break;

            case IRON_DOOR:
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i, j, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i, j + 1, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i + 1, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i + 2, j + 2, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i + 2, j + 1, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONEBRICK.getDefaultState(), i + 2, j, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_DOOR.getDefaultState(), i + 1, j, k, structureboundingbox);
                this.setBlockState(world, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), i + 1, j + 1, k, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_BUTTON.getDefaultState().withProperty(BlockButton.FACING, EnumFacing.NORTH), i + 2, j + 1, k + 1, structureboundingbox);
                this.setBlockState(world, Blocks.STONE_BUTTON.getDefaultState().withProperty(BlockButton.FACING, EnumFacing.SOUTH), i + 2, j + 1, k - 1, structureboundingbox);
            }

        }

        protected StructureStrongholdPieces.Stronghold.Door getRandomDoor(Random random) {
            int i = random.nextInt(5);

            switch (i) {
            case 0:
            case 1:
            default:
                return StructureStrongholdPieces.Stronghold.Door.OPENING;

            case 2:
                return StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR;

            case 3:
                return StructureStrongholdPieces.Stronghold.Door.GRATES;

            case 4:
                return StructureStrongholdPieces.Stronghold.Door.IRON_DOOR;
            }
        }

        @Nullable
        protected StructureComponent getNextComponentNormal(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX + i, this.boundingBox.minY + j, this.boundingBox.minZ - 1, enumdirection, this.getComponentType());

                case SOUTH:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX + i, this.boundingBox.minY + j, this.boundingBox.maxZ + 1, enumdirection, this.getComponentType());

                case WEST:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + j, this.boundingBox.minZ + i, enumdirection, this.getComponentType());

                case EAST:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + j, this.boundingBox.minZ + i, enumdirection, this.getComponentType());
                }
            }

            return null;
        }

        @Nullable
        protected StructureComponent getNextComponentX(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.WEST, this.getComponentType());

                case SOUTH:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.WEST, this.getComponentType());

                case WEST:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());

                case EAST:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }

            return null;
        }

        @Nullable
        protected StructureComponent getNextComponentZ(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.EAST, this.getComponentType());

                case SOUTH:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.EAST, this.getComponentType());

                case WEST:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());

                case EAST:
                    return StructureStrongholdPieces.generateAndAddPiece(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }

            return null;
        }

        protected static boolean canStrongholdGoDeeper(StructureBoundingBox structureboundingbox) {
            return structureboundingbox != null && structureboundingbox.minY > 10;
        }

        public static enum Door {

            OPENING, WOOD_DOOR, GRATES, IRON_DOOR;

            private Door() {}
        }
    }

    static class PieceWeight {

        public Class<? extends StructureStrongholdPieces.Stronghold> pieceClass;
        public final int pieceWeight;
        public int instancesSpawned;
        public int instancesLimit;

        public PieceWeight(Class<? extends StructureStrongholdPieces.Stronghold> oclass, int i, int j) {
            this.pieceClass = oclass;
            this.pieceWeight = i;
            this.instancesLimit = j;
        }

        public boolean canSpawnMoreStructuresOfType(int i) {
            return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
        }

        public boolean canSpawnMoreStructures() {
            return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
        }
    }
}
