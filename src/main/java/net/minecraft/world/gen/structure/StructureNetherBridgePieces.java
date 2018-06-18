package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityBlaze;
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

public class StructureNetherBridgePieces {

    private static final StructureNetherBridgePieces.PieceWeight[] PRIMARY_COMPONENTS = new StructureNetherBridgePieces.PieceWeight[] { new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Straight.class, 30, 0, true), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing3.class, 10, 4), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing.class, 10, 4), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Stairs.class, 10, 3), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Throne.class, 5, 2), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Entrance.class, 5, 1)};
    private static final StructureNetherBridgePieces.PieceWeight[] SECONDARY_COMPONENTS = new StructureNetherBridgePieces.PieceWeight[] { new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor5.class, 25, 0, true), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing2.class, 15, 5), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor2.class, 5, 10), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor.class, 5, 10), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor3.class, 10, 3, true), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor4.class, 7, 2), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.NetherStalkRoom.class, 5, 2)};

    public static void registerNetherFortressPieces() {
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing3.class, "NeBCr");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.End.class, "NeBEF");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Straight.class, "NeBS");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor3.class, "NeCCS");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor4.class, "NeCTB");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Entrance.class, "NeCE");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing2.class, "NeSCSC");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor.class, "NeSCLT");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor5.class, "NeSC");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Corridor2.class, "NeSCRT");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.NetherStalkRoom.class, "NeCSR");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Throne.class, "NeMT");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Crossing.class, "NeRC");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Stairs.class, "NeSR");
        MapGenStructureIO.registerStructureComponent(StructureNetherBridgePieces.Start.class, "NeStart");
    }

    private static StructureNetherBridgePieces.Piece findAndCreateBridgePieceFactory(StructureNetherBridgePieces.PieceWeight worldgennetherpieces_worldgennetherpieceweight, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        Class oclass = worldgennetherpieces_worldgennetherpieceweight.weightClass;
        Object object = null;

        if (oclass == StructureNetherBridgePieces.Straight.class) {
            object = StructureNetherBridgePieces.Straight.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Crossing3.class) {
            object = StructureNetherBridgePieces.Crossing3.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Crossing.class) {
            object = StructureNetherBridgePieces.Crossing.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Stairs.class) {
            object = StructureNetherBridgePieces.Stairs.createPiece(list, random, i, j, k, l, enumdirection);
        } else if (oclass == StructureNetherBridgePieces.Throne.class) {
            object = StructureNetherBridgePieces.Throne.createPiece(list, random, i, j, k, l, enumdirection);
        } else if (oclass == StructureNetherBridgePieces.Entrance.class) {
            object = StructureNetherBridgePieces.Entrance.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor5.class) {
            object = StructureNetherBridgePieces.Corridor5.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor2.class) {
            object = StructureNetherBridgePieces.Corridor2.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor.class) {
            object = StructureNetherBridgePieces.Corridor.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor3.class) {
            object = StructureNetherBridgePieces.Corridor3.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor4.class) {
            object = StructureNetherBridgePieces.Corridor4.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Crossing2.class) {
            object = StructureNetherBridgePieces.Crossing2.createPiece(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.NetherStalkRoom.class) {
            object = StructureNetherBridgePieces.NetherStalkRoom.createPiece(list, random, i, j, k, enumdirection, l);
        }

        return (StructureNetherBridgePieces.Piece) object;
    }

    public static class Corridor4 extends StructureNetherBridgePieces.Piece {

        public Corridor4() {}

        public Corridor4(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            byte b0 = 1;
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection == EnumFacing.WEST || enumdirection == EnumFacing.NORTH) {
                b0 = 5;
            }

            this.getNextComponentX((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, b0, random.nextInt(8) > 0);
            this.getNextComponentZ((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, b0, random.nextInt(8) > 0);
        }

        public static StructureNetherBridgePieces.Corridor4 createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -3, 0, 0, 9, 7, 9, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor4(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 8, 5, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 0, 1, 4, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 3, 0, 7, 4, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 4, 2, 2, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 1, 4, 7, 2, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 8, 8, 3, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 6, 0, 3, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 3, 6, 8, 3, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 5, 1, 5, 5, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 4, 5, 7, 5, 5, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);

            for (int i = 0; i <= 5; ++i) {
                for (int j = 0; j <= 8; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), j, -1, i, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Corridor3 extends StructureNetherBridgePieces.Piece {

        public Corridor3() {}

        public Corridor3(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 1, 0, true);
        }

        public static StructureNetherBridgePieces.Corridor3 createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -7, 0, 5, 14, 10, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor3(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            IBlockState iblockdata = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

            for (int i = 0; i <= 9; ++i) {
                int j = Math.max(1, 7 - i);
                int k = Math.min(Math.max(j + 5, 14 - i), 13);
                int l = i;

                this.fillWithBlocks(world, structureboundingbox, 0, 0, i, 4, j, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 1, j + 1, i, 3, k - 1, i, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                if (i <= 6) {
                    this.setBlockState(world, iblockdata, 1, j + 1, i, structureboundingbox);
                    this.setBlockState(world, iblockdata, 2, j + 1, i, structureboundingbox);
                    this.setBlockState(world, iblockdata, 3, j + 1, i, structureboundingbox);
                }

                this.fillWithBlocks(world, structureboundingbox, 0, k, i, 4, k, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 0, j + 1, i, 0, k - 1, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 4, j + 1, i, 4, k - 1, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                if ((i & 1) == 0) {
                    this.fillWithBlocks(world, structureboundingbox, 0, j + 2, i, 0, j + 3, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                    this.fillWithBlocks(world, structureboundingbox, 4, j + 2, i, 4, j + 3, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                }

                for (int i1 = 0; i1 <= 4; ++i1) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i1, -1, l, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Corridor extends StructureNetherBridgePieces.Piece {

        private boolean chest;

        public Corridor() {}

        public Corridor(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.chest = random.nextInt(3) == 0;
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.chest = nbttagcompound.getBoolean("Chest");
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Chest", this.chest);
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentX((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 1, true);
        }

        public static StructureNetherBridgePieces.Corridor createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, 0, 0, 5, 7, 5, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 3, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 3, 3, 4, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 4, 1, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 3, 3, 4, 3, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            if (this.chest && structureboundingbox.isVecInside((Vec3i) (new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3))))) {
                this.chest = false;
                this.generateChest(world, structureboundingbox, random, 3, 2, 3, LootTableList.CHESTS_NETHER_BRIDGE);
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Corridor2 extends StructureNetherBridgePieces.Piece {

        private boolean chest;

        public Corridor2() {}

        public Corridor2(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.chest = random.nextInt(3) == 0;
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.chest = nbttagcompound.getBoolean("Chest");
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Chest", this.chest);
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentZ((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 1, true);
        }

        public static StructureNetherBridgePieces.Corridor2 createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, 0, 0, 5, 7, 5, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor2(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 3, 0, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 4, 1, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 3, 3, 4, 3, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            if (this.chest && structureboundingbox.isVecInside((Vec3i) (new BlockPos(this.getXWithOffset(1, 3), this.getYWithOffset(2), this.getZWithOffset(1, 3))))) {
                this.chest = false;
                this.generateChest(world, structureboundingbox, random, 1, 2, 3, LootTableList.CHESTS_NETHER_BRIDGE);
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Crossing2 extends StructureNetherBridgePieces.Piece {

        public Crossing2() {}

        public Crossing2(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 1, 0, true);
            this.getNextComponentX((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 1, true);
            this.getNextComponentZ((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 1, true);
        }

        public static StructureNetherBridgePieces.Crossing2 createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, 0, 0, 5, 7, 5, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Crossing2(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Corridor5 extends StructureNetherBridgePieces.Piece {

        public Corridor5() {}

        public Corridor5(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 1, 0, true);
        }

        public static StructureNetherBridgePieces.Corridor5 createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, 0, 0, 5, 7, 5, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor5(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 3, 0, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 3, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 3, 3, 4, 4, 3, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class NetherStalkRoom extends StructureNetherBridgePieces.Piece {

        public NetherStalkRoom() {}

        public NetherStalkRoom(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 5, 3, true);
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 5, 11, true);
        }

        public static StructureNetherBridgePieces.NetherStalkRoom createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -5, -3, 0, 13, 14, 13, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.NetherStalkRoom(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 12, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            int i;

            for (i = 1; i <= 11; i += 2) {
                this.fillWithBlocks(world, structureboundingbox, i, 10, 0, i, 11, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, i, 10, 12, i, 11, 12, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 0, 10, i, 0, 11, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 12, 10, i, 12, 11, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), i, 13, 0, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), i, 13, 12, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), 0, 13, i, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), 12, 13, i, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), i + 1, 13, 0, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), i + 1, 13, 12, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, i + 1, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, i + 1, structureboundingbox);
            }

            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, structureboundingbox);
            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 12, structureboundingbox);
            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, structureboundingbox);
            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, 0, structureboundingbox);

            for (i = 3; i <= 9; i += 2) {
                this.fillWithBlocks(world, structureboundingbox, 1, 7, i, 1, 8, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 11, 7, i, 11, 8, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            }

            IBlockState iblockdata = Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);

            int j;
            int k;

            for (j = 0; j <= 6; ++j) {
                int l = j + 4;

                for (k = 5; k <= 7; ++k) {
                    this.setBlockState(world, iblockdata, k, 5 + j, l, structureboundingbox);
                }

                if (l >= 5 && l <= 8) {
                    this.fillWithBlocks(world, structureboundingbox, 5, 5, l, 7, j + 4, l, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                } else if (l >= 9 && l <= 10) {
                    this.fillWithBlocks(world, structureboundingbox, 5, 8, l, 7, j + 4, l, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                }

                if (j >= 1) {
                    this.fillWithBlocks(world, structureboundingbox, 5, 6 + j, l, 7, 9 + j, l, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                }
            }

            for (j = 5; j <= 7; ++j) {
                this.setBlockState(world, iblockdata, j, 12, 11, structureboundingbox);
            }

            this.fillWithBlocks(world, structureboundingbox, 5, 6, 7, 5, 7, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 6, 7, 7, 7, 7, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 13, 12, 7, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            IBlockState iblockdata1 = iblockdata.withProperty(BlockStairs.FACING, EnumFacing.EAST);
            IBlockState iblockdata2 = iblockdata.withProperty(BlockStairs.FACING, EnumFacing.WEST);

            this.setBlockState(world, iblockdata2, 4, 5, 2, structureboundingbox);
            this.setBlockState(world, iblockdata2, 4, 5, 3, structureboundingbox);
            this.setBlockState(world, iblockdata2, 4, 5, 9, structureboundingbox);
            this.setBlockState(world, iblockdata2, 4, 5, 10, structureboundingbox);
            this.setBlockState(world, iblockdata1, 8, 5, 2, structureboundingbox);
            this.setBlockState(world, iblockdata1, 8, 5, 3, structureboundingbox);
            this.setBlockState(world, iblockdata1, 8, 5, 9, structureboundingbox);
            this.setBlockState(world, iblockdata1, 8, 5, 10, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.getDefaultState(), Blocks.SOUL_SAND.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.getDefaultState(), Blocks.SOUL_SAND.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.getDefaultState(), Blocks.NETHER_WART.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.getDefaultState(), Blocks.NETHER_WART.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            int i1;

            for (k = 4; k <= 8; ++k) {
                for (i1 = 0; i1 <= 2; ++i1) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), k, -1, i1, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), k, -1, 12 - i1, structureboundingbox);
                }
            }

            for (k = 0; k <= 2; ++k) {
                for (i1 = 4; i1 <= 8; ++i1) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), k, -1, i1, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), 12 - k, -1, i1, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Entrance extends StructureNetherBridgePieces.Piece {

        public Entrance() {}

        public Entrance(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 5, 3, true);
        }

        public static StructureNetherBridgePieces.Entrance createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -5, -3, 0, 13, 14, 13, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Entrance(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 12, 13, 12, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);

            int i;

            for (i = 1; i <= 11; i += 2) {
                this.fillWithBlocks(world, structureboundingbox, i, 10, 0, i, 11, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, i, 10, 12, i, 11, 12, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 0, 10, i, 0, 11, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 12, 10, i, 12, 11, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), i, 13, 0, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), i, 13, 12, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), 0, 13, i, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), 12, 13, i, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), i + 1, 13, 0, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), i + 1, 13, 12, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, i + 1, structureboundingbox);
                this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, i + 1, structureboundingbox);
            }

            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, structureboundingbox);
            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 12, structureboundingbox);
            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 0, 13, 0, structureboundingbox);
            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 12, 13, 0, structureboundingbox);

            for (i = 3; i <= 9; i += 2) {
                this.fillWithBlocks(world, structureboundingbox, 1, 7, i, 1, 8, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
                this.fillWithBlocks(world, structureboundingbox, 11, 7, i, 11, 8, i, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            }

            this.fillWithBlocks(world, structureboundingbox, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            int j;

            for (i = 4; i <= 8; ++i) {
                for (j = 0; j <= 2; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, 12 - j, structureboundingbox);
                }
            }

            for (i = 0; i <= 2; ++i) {
                for (j = 4; j <= 8; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), 12 - i, -1, j, structureboundingbox);
                }
            }

            this.fillWithBlocks(world, structureboundingbox, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 1, 6, 6, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), 6, 0, 6, structureboundingbox);
            IBlockState iblockdata = Blocks.FLOWING_LAVA.getDefaultState();

            this.setBlockState(world, iblockdata, 6, 5, 6, structureboundingbox);
            BlockPos blockposition = new BlockPos(this.getXWithOffset(6, 6), this.getYWithOffset(5), this.getZWithOffset(6, 6));

            if (structureboundingbox.isVecInside((Vec3i) blockposition)) {
                world.immediateBlockTick(blockposition, iblockdata, random);
            }

            return true;
        }
    }

    public static class Throne extends StructureNetherBridgePieces.Piece {

        private boolean hasSpawner;

        public Throne() {}

        public Throne(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.hasSpawner = nbttagcompound.getBoolean("Mob");
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setBoolean("Mob", this.hasSpawner);
        }

        public static StructureNetherBridgePieces.Throne createPiece(List<StructureComponent> list, Random random, int i, int j, int k, int l, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -2, 0, 0, 7, 8, 9, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Throne(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 6, 7, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 1, 6, 3, structureboundingbox);
            this.setBlockState(world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), 5, 6, 3, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 0, 6, 3, 0, 6, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 6, 3, 6, 6, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 6, 8, 5, 7, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 8, 8, 4, 8, 8, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            if (!this.hasSpawner) {
                BlockPos blockposition = new BlockPos(this.getXWithOffset(3, 5), this.getYWithOffset(5), this.getZWithOffset(3, 5));

                if (structureboundingbox.isVecInside((Vec3i) blockposition)) {
                    this.hasSpawner = true;
                    world.setBlockState(blockposition, Blocks.MOB_SPAWNER.getDefaultState(), 2);
                    TileEntity tileentity = world.getTileEntity(blockposition);

                    if (tileentity instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityBlaze.class));
                    }
                }
            }

            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Stairs extends StructureNetherBridgePieces.Piece {

        public Stairs() {}

        public Stairs(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentZ((StructureNetherBridgePieces.Start) structurepiece, list, random, 6, 2, false);
        }

        public static StructureNetherBridgePieces.Stairs createPiece(List<StructureComponent> list, Random random, int i, int j, int k, int l, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -2, 0, 0, 7, 11, 7, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Stairs(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 6, 10, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 2, 0, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 3, 2, 6, 5, 2, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 3, 4, 6, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.setBlockState(world, Blocks.NETHER_BRICK.getDefaultState(), 5, 2, 5, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 8, 2, 6, 8, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 5, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);

            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Crossing extends StructureNetherBridgePieces.Piece {

        public Crossing() {}

        public Crossing(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 2, 0, false);
            this.getNextComponentX((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 2, false);
            this.getNextComponentZ((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 2, false);
        }

        public static StructureNetherBridgePieces.Crossing createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -2, 0, 0, 7, 9, 7, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Crossing(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 6, 7, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 0, 4, 5, 0, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 2, 5, 6, 4, 5, 6, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 2, 0, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 6, 5, 2, 6, 5, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);

            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Crossing3 extends StructureNetherBridgePieces.Piece {

        public Crossing3() {}

        public Crossing3(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        protected Crossing3(Random random, int i, int j) {
            super(0);
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(random));
            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(i, 64, j, i + 19 - 1, 73, j + 19 - 1);
            } else {
                this.boundingBox = new StructureBoundingBox(i, 64, j, i + 19 - 1, 73, j + 19 - 1);
            }

        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 8, 3, false);
            this.getNextComponentX((StructureNetherBridgePieces.Start) structurepiece, list, random, 3, 8, false);
            this.getNextComponentZ((StructureNetherBridgePieces.Start) structurepiece, list, random, 3, 8, false);
        }

        public static StructureNetherBridgePieces.Crossing3 createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -8, -3, 0, 19, 10, 19, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Crossing3(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 8, 5, 0, 10, 7, 18, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 8, 18, 7, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            int i;
            int j;

            for (i = 7; i <= 11; ++i) {
                for (j = 0; j <= 2; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, 18 - j, structureboundingbox);
                }
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            for (i = 0; i <= 2; ++i) {
                for (j = 7; j <= 11; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), 18 - i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class End extends StructureNetherBridgePieces.Piece {

        private int fillSeed;

        public End() {}

        public End(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
            this.fillSeed = random.nextInt();
        }

        public static StructureNetherBridgePieces.End createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -3, 0, 5, 10, 8, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.End(l, random, structureboundingbox, enumdirection) : null;
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.readStructureFromNBT(nbttagcompound, definedstructuremanager);
            this.fillSeed = nbttagcompound.getInteger("Seed");
        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
            super.writeStructureToNBT(nbttagcompound);
            nbttagcompound.setInteger("Seed", this.fillSeed);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            Random random1 = new Random((long) this.fillSeed);

            int i;
            int j;
            int k;

            for (i = 0; i <= 4; ++i) {
                for (j = 3; j <= 4; ++j) {
                    k = random1.nextInt(8);
                    this.fillWithBlocks(world, structureboundingbox, i, j, 0, i, j, k, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                }
            }

            i = random1.nextInt(8);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 0, 5, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            i = random1.nextInt(8);
            this.fillWithBlocks(world, structureboundingbox, 4, 5, 0, 4, 5, i, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            for (i = 0; i <= 4; ++i) {
                j = random1.nextInt(5);
                this.fillWithBlocks(world, structureboundingbox, i, 2, 0, i, 2, j, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            }

            for (i = 0; i <= 4; ++i) {
                for (j = 0; j <= 1; ++j) {
                    k = random1.nextInt(3);
                    this.fillWithBlocks(world, structureboundingbox, i, j, 0, i, j, k, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
                }
            }

            return true;
        }
    }

    public static class Straight extends StructureNetherBridgePieces.Piece {

        public Straight() {}

        public Straight(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        public void buildComponent(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.getNextComponentNormal((StructureNetherBridgePieces.Start) structurepiece, list, random, 1, 3, false);
        }

        public static StructureNetherBridgePieces.Straight createPiece(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(i, j, k, -1, -3, 0, 5, 10, 19, enumdirection);

            return isAboveGround(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Straight(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 1, 5, 0, 3, 7, 18, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK.getDefaultState(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 2; ++j) {
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, j, structureboundingbox);
                    this.replaceAirAndLiquidDownwards(world, Blocks.NETHER_BRICK.getDefaultState(), i, -1, 18 - j, structureboundingbox);
                }
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 4, 0, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 14, 0, 4, 14, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 17, 0, 4, 17, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 1, 1, 4, 4, 1, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 3, 4, 4, 4, 4, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 3, 14, 4, 4, 14, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            this.fillWithBlocks(world, structureboundingbox, 4, 1, 17, 4, 4, 17, Blocks.NETHER_BRICK_FENCE.getDefaultState(), Blocks.NETHER_BRICK_FENCE.getDefaultState(), false);
            return true;
        }
    }

    public static class Start extends StructureNetherBridgePieces.Crossing3 {

        public StructureNetherBridgePieces.PieceWeight lastPlaced;
        public List<StructureNetherBridgePieces.PieceWeight> primaryWeights;
        public List<StructureNetherBridgePieces.PieceWeight> secondaryWeights;
        public List<StructureComponent> pendingChildren = Lists.newArrayList();

        public Start() {}

        public Start(Random random, int i, int j) {
            super(random, i, j);
            this.primaryWeights = Lists.newArrayList();
            StructureNetherBridgePieces.PieceWeight[] aworldgennetherpieces_worldgennetherpieceweight = StructureNetherBridgePieces.PRIMARY_COMPONENTS;
            int k = aworldgennetherpieces_worldgennetherpieceweight.length;

            int l;
            StructureNetherBridgePieces.PieceWeight worldgennetherpieces_worldgennetherpieceweight;

            for (l = 0; l < k; ++l) {
                worldgennetherpieces_worldgennetherpieceweight = aworldgennetherpieces_worldgennetherpieceweight[l];
                worldgennetherpieces_worldgennetherpieceweight.placeCount = 0;
                this.primaryWeights.add(worldgennetherpieces_worldgennetherpieceweight);
            }

            this.secondaryWeights = Lists.newArrayList();
            aworldgennetherpieces_worldgennetherpieceweight = StructureNetherBridgePieces.SECONDARY_COMPONENTS;
            k = aworldgennetherpieces_worldgennetherpieceweight.length;

            for (l = 0; l < k; ++l) {
                worldgennetherpieces_worldgennetherpieceweight = aworldgennetherpieces_worldgennetherpieceweight[l];
                worldgennetherpieces_worldgennetherpieceweight.placeCount = 0;
                this.secondaryWeights.add(worldgennetherpieces_worldgennetherpieceweight);
            }

        }
    }

    abstract static class Piece extends StructureComponent {

        public Piece() {}

        protected Piece(int i) {
            super(i);
        }

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {}

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {}

        private int getTotalWeight(List<StructureNetherBridgePieces.PieceWeight> list) {
            boolean flag = false;
            int i = 0;

            StructureNetherBridgePieces.PieceWeight worldgennetherpieces_worldgennetherpieceweight;

            for (Iterator iterator = list.iterator(); iterator.hasNext(); i += worldgennetherpieces_worldgennetherpieceweight.weight) {
                worldgennetherpieces_worldgennetherpieceweight = (StructureNetherBridgePieces.PieceWeight) iterator.next();
                if (worldgennetherpieces_worldgennetherpieceweight.maxPlaceCount > 0 && worldgennetherpieces_worldgennetherpieceweight.placeCount < worldgennetherpieces_worldgennetherpieceweight.maxPlaceCount) {
                    flag = true;
                }
            }

            return flag ? i : -1;
        }

        private StructureNetherBridgePieces.Piece generatePiece(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureNetherBridgePieces.PieceWeight> list, List<StructureComponent> list1, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            int i1 = this.getTotalWeight(list);
            boolean flag = i1 > 0 && l <= 30;
            int j1 = 0;

            while (j1 < 5 && flag) {
                ++j1;
                int k1 = random.nextInt(i1);
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    StructureNetherBridgePieces.PieceWeight worldgennetherpieces_worldgennetherpieceweight = (StructureNetherBridgePieces.PieceWeight) iterator.next();

                    k1 -= worldgennetherpieces_worldgennetherpieceweight.weight;
                    if (k1 < 0) {
                        if (!worldgennetherpieces_worldgennetherpieceweight.doPlace(l) || worldgennetherpieces_worldgennetherpieceweight == worldgennetherpieces_worldgennetherpiece15.lastPlaced && !worldgennetherpieces_worldgennetherpieceweight.allowInRow) {
                            break;
                        }

                        StructureNetherBridgePieces.Piece worldgennetherpieces_worldgennetherpiece = StructureNetherBridgePieces.findAndCreateBridgePieceFactory(worldgennetherpieces_worldgennetherpieceweight, list1, random, i, j, k, enumdirection, l);

                        if (worldgennetherpieces_worldgennetherpiece != null) {
                            ++worldgennetherpieces_worldgennetherpieceweight.placeCount;
                            worldgennetherpieces_worldgennetherpiece15.lastPlaced = worldgennetherpieces_worldgennetherpieceweight;
                            if (!worldgennetherpieces_worldgennetherpieceweight.isValid()) {
                                list.remove(worldgennetherpieces_worldgennetherpieceweight);
                            }

                            return worldgennetherpieces_worldgennetherpiece;
                        }
                    }
                }
            }

            return StructureNetherBridgePieces.End.createPiece(list1, random, i, j, k, enumdirection, l);
        }

        private StructureComponent generateAndAddPiece(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureComponent> list, Random random, int i, int j, int k, @Nullable EnumFacing enumdirection, int l, boolean flag) {
            if (Math.abs(i - worldgennetherpieces_worldgennetherpiece15.getBoundingBox().minX) <= 112 && Math.abs(k - worldgennetherpieces_worldgennetherpiece15.getBoundingBox().minZ) <= 112) {
                List list1 = worldgennetherpieces_worldgennetherpiece15.primaryWeights;

                if (flag) {
                    list1 = worldgennetherpieces_worldgennetherpiece15.secondaryWeights;
                }

                StructureNetherBridgePieces.Piece worldgennetherpieces_worldgennetherpiece = this.generatePiece(worldgennetherpieces_worldgennetherpiece15, list1, list, random, i, j, k, enumdirection, l + 1);

                if (worldgennetherpieces_worldgennetherpiece != null) {
                    list.add(worldgennetherpieces_worldgennetherpiece);
                    worldgennetherpieces_worldgennetherpiece15.pendingChildren.add(worldgennetherpieces_worldgennetherpiece);
                }

                return worldgennetherpieces_worldgennetherpiece;
            } else {
                return StructureNetherBridgePieces.End.createPiece(list, random, i, j, k, enumdirection, l);
            }
        }

        @Nullable
        protected StructureComponent getNextComponentNormal(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureComponent> list, Random random, int i, int j, boolean flag) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX + i, this.boundingBox.minY + j, this.boundingBox.minZ - 1, enumdirection, this.getComponentType(), flag);

                case SOUTH:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX + i, this.boundingBox.minY + j, this.boundingBox.maxZ + 1, enumdirection, this.getComponentType(), flag);

                case WEST:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + j, this.boundingBox.minZ + i, enumdirection, this.getComponentType(), flag);

                case EAST:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + j, this.boundingBox.minZ + i, enumdirection, this.getComponentType(), flag);
                }
            }

            return null;
        }

        @Nullable
        protected StructureComponent getNextComponentX(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureComponent> list, Random random, int i, int j, boolean flag) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.WEST, this.getComponentType(), flag);

                case SOUTH:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.WEST, this.getComponentType(), flag);

                case WEST:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), flag);

                case EAST:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), flag);
                }
            }

            return null;
        }

        @Nullable
        protected StructureComponent getNextComponentZ(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureComponent> list, Random random, int i, int j, boolean flag) {
            EnumFacing enumdirection = this.getCoordBaseMode();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.EAST, this.getComponentType(), flag);

                case SOUTH:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + i, this.boundingBox.minZ + j, EnumFacing.EAST, this.getComponentType(), flag);

                case WEST:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), flag);

                case EAST:
                    return this.generateAndAddPiece(worldgennetherpieces_worldgennetherpiece15, list, random, this.boundingBox.minX + j, this.boundingBox.minY + i, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), flag);
                }
            }

            return null;
        }

        protected static boolean isAboveGround(StructureBoundingBox structureboundingbox) {
            return structureboundingbox != null && structureboundingbox.minY > 10;
        }
    }

    static class PieceWeight {

        public Class<? extends StructureNetherBridgePieces.Piece> weightClass;
        public final int weight;
        public int placeCount;
        public int maxPlaceCount;
        public boolean allowInRow;

        public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> oclass, int i, int j, boolean flag) {
            this.weightClass = oclass;
            this.weight = i;
            this.maxPlaceCount = j;
            this.allowInRow = flag;
        }

        public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> oclass, int i, int j) {
            this(oclass, i, j, false);
        }

        public boolean doPlace(int i) {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }

        public boolean isValid() {
            return this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount;
        }
    }
}
