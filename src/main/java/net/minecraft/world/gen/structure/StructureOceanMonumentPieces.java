package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class StructureOceanMonumentPieces {

    public static void registerOceanMonumentPieces() {
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.MonumentBuilding.class, "OMB");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.MonumentCoreRoom.class, "OMCR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleXRoom.class, "OMDXR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleXYRoom.class, "OMDXYR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleYRoom.class, "OMDYR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleYZRoom.class, "OMDYZR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.DoubleZRoom.class, "OMDZR");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.EntryRoom.class, "OMEntry");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.Penthouse.class, "OMPenthouse");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.SimpleRoom.class, "OMSimple");
        MapGenStructureIO.registerStructureComponent(StructureOceanMonumentPieces.SimpleTopRoom.class, "OMSimpleT");
    }

    static class YZDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private YZDoubleRoomFitHelper() {}

        public boolean fits(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.NORTH.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.NORTH.getIndex()].claimed && worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.UP.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()].claimed) {
                StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.NORTH.getIndex()];

                return worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.UP.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker1.connections[EnumFacing.UP.getIndex()].claimed;
            } else {
                return false;
            }
        }

        public StructureOceanMonumentPieces.Piece create(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.NORTH.getIndex()].claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()].claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            return new StructureOceanMonumentPieces.DoubleYZRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        YZDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class XYDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private XYDoubleRoomFitHelper() {}

        public boolean fits(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.EAST.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.EAST.getIndex()].claimed && worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.UP.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()].claimed) {
                StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.EAST.getIndex()];

                return worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.UP.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker1.connections[EnumFacing.UP.getIndex()].claimed;
            } else {
                return false;
            }
        }

        public StructureOceanMonumentPieces.Piece create(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.EAST.getIndex()].claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()].claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            return new StructureOceanMonumentPieces.DoubleXYRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        XYDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class ZDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private ZDoubleRoomFitHelper() {}

        public boolean fits(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.NORTH.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.NORTH.getIndex()].claimed;
        }

        public StructureOceanMonumentPieces.Piece create(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker;

            if (!worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.NORTH.getIndex()] || worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.NORTH.getIndex()].claimed) {
                worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.SOUTH.getIndex()];
            }

            worldgenmonumentpieces_worldgenmonumentstatetracker1.claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker1.connections[EnumFacing.NORTH.getIndex()].claimed = true;
            return new StructureOceanMonumentPieces.DoubleZRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker1, random);
        }

        ZDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class XDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private XDoubleRoomFitHelper() {}

        public boolean fits(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.EAST.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.EAST.getIndex()].claimed;
        }

        public StructureOceanMonumentPieces.Piece create(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.EAST.getIndex()].claimed = true;
            return new StructureOceanMonumentPieces.DoubleXRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        XDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class YDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private YDoubleRoomFitHelper() {}

        public boolean fits(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.UP.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()].claimed;
        }

        public StructureOceanMonumentPieces.Piece create(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()].claimed = true;
            return new StructureOceanMonumentPieces.DoubleYRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        YDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class FitSimpleRoomTopHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private FitSimpleRoomTopHelper() {}

        public boolean fits(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return !worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.WEST.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.EAST.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.NORTH.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.SOUTH.getIndex()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.UP.getIndex()];
        }

        public StructureOceanMonumentPieces.Piece create(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.claimed = true;
            return new StructureOceanMonumentPieces.SimpleTopRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        FitSimpleRoomTopHelper(Object object) {
            this();
        }
    }

    static class FitSimpleRoomHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private FitSimpleRoomHelper() {}

        public boolean fits(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return true;
        }

        public StructureOceanMonumentPieces.Piece create(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.claimed = true;
            return new StructureOceanMonumentPieces.SimpleRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        FitSimpleRoomHelper(Object object) {
            this();
        }
    }

    interface MonumentRoomFitHelper {

        boolean fits(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker);

        StructureOceanMonumentPieces.Piece create(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random);
    }

    static class RoomDefinition {

        int index;
        StructureOceanMonumentPieces.RoomDefinition[] connections = new StructureOceanMonumentPieces.RoomDefinition[6];
        boolean[] hasOpening = new boolean[6];
        boolean claimed;
        boolean isSource;
        int scanIndex;

        public RoomDefinition(int i) {
            this.index = i;
        }

        public void setConnection(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            this.connections[enumdirection.getIndex()] = worldgenmonumentpieces_worldgenmonumentstatetracker;
            worldgenmonumentpieces_worldgenmonumentstatetracker.connections[enumdirection.getOpposite().getIndex()] = this;
        }

        public void updateOpenings() {
            for (int i = 0; i < 6; ++i) {
                this.hasOpening[i] = this.connections[i] != null;
            }

        }

        public boolean findSource(int i) {
            if (this.isSource) {
                return true;
            } else {
                this.scanIndex = i;

                for (int j = 0; j < 6; ++j) {
                    if (this.connections[j] != null && this.hasOpening[j] && this.connections[j].scanIndex != i && this.connections[j].findSource(i)) {
                        return true;
                    }
                }

                return false;
            }
        }

        public boolean isSpecial() {
            return this.index >= 75;
        }

        public int countOpenings() {
            int i = 0;

            for (int j = 0; j < 6; ++j) {
                if (this.hasOpening[j]) {
                    ++i;
                }
            }

            return i;
        }
    }

    public static class Penthouse extends StructureOceanMonumentPieces.Piece {

        public Penthouse() {}

        public Penthouse(EnumFacing enumdirection, StructureBoundingBox structureboundingbox) {
            super(enumdirection, structureboundingbox);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 2, -1, 2, 11, -1, 11, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, -1, 0, 1, -1, 11, StructureOceanMonumentPieces.Penthouse.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Penthouse.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 12, -1, 0, 13, -1, 11, StructureOceanMonumentPieces.Penthouse.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Penthouse.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, -1, 0, 11, -1, 1, StructureOceanMonumentPieces.Penthouse.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Penthouse.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, -1, 12, 11, -1, 13, StructureOceanMonumentPieces.Penthouse.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Penthouse.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 0, 0, 0, 0, 13, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 13, 0, 0, 13, 0, 13, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 0, 12, 0, 0, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 0, 13, 12, 0, 13, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);

            for (int i = 2; i <= 11; i += 3) {
                this.setBlockState(world, StructureOceanMonumentPieces.Penthouse.SEA_LANTERN, 0, 0, i, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.Penthouse.SEA_LANTERN, 13, 0, i, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.Penthouse.SEA_LANTERN, i, 0, 0, structureboundingbox);
            }

            this.fillWithBlocks(world, structureboundingbox, 2, 0, 3, 4, 0, 9, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 9, 0, 3, 11, 0, 9, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 4, 0, 9, 9, 0, 11, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.setBlockState(world, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, 5, 0, 8, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, 8, 0, 8, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, 10, 0, 10, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, 3, 0, 10, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 3, 0, 3, 3, 0, 7, StructureOceanMonumentPieces.Penthouse.DARK_PRISMARINE, StructureOceanMonumentPieces.Penthouse.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 0, 3, 10, 0, 7, StructureOceanMonumentPieces.Penthouse.DARK_PRISMARINE, StructureOceanMonumentPieces.Penthouse.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 0, 10, 7, 0, 10, StructureOceanMonumentPieces.Penthouse.DARK_PRISMARINE, StructureOceanMonumentPieces.Penthouse.DARK_PRISMARINE, false);
            byte b0 = 3;

            for (int j = 0; j < 2; ++j) {
                for (int k = 2; k <= 8; k += 3) {
                    this.fillWithBlocks(world, structureboundingbox, b0, 0, k, b0, 2, k, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
                }

                b0 = 10;
            }

            this.fillWithBlocks(world, structureboundingbox, 5, 0, 10, 5, 2, 10, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 8, 0, 10, 8, 2, 10, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Penthouse.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, -1, 7, 7, -1, 8, StructureOceanMonumentPieces.Penthouse.DARK_PRISMARINE, StructureOceanMonumentPieces.Penthouse.DARK_PRISMARINE, false);
            this.generateWaterBox(world, structureboundingbox, 6, -1, 3, 7, -1, 4, false);
            this.spawnElder(world, structureboundingbox, 6, 1, 6);
            return true;
        }
    }

    public static class WingRoom extends StructureOceanMonumentPieces.Piece {

        private int mainDesign;

        public WingRoom() {}

        public WingRoom(EnumFacing enumdirection, StructureBoundingBox structureboundingbox, int i) {
            super(enumdirection, structureboundingbox);
            this.mainDesign = i & 1;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.mainDesign == 0) {
                int i;

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 10 - i, 3 - i, 20 - i, 12 + i, 3 - i, 20, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                }

                this.fillWithBlocks(world, structureboundingbox, 7, 0, 6, 15, 0, 16, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 6, 0, 6, 6, 3, 20, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 16, 0, 6, 16, 3, 20, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 1, 7, 7, 1, 20, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 15, 1, 7, 15, 1, 20, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 1, 6, 9, 3, 6, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 13, 1, 6, 15, 3, 6, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 8, 1, 7, 9, 1, 7, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 13, 1, 7, 14, 1, 7, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 9, 0, 5, 13, 0, 5, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 10, 0, 7, 12, 0, 7, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 8, 0, 10, 8, 0, 12, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 14, 0, 10, 14, 0, 12, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, false);

                for (i = 18; i >= 7; i -= 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 6, 3, i, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 16, 3, i, structureboundingbox);
                }

                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 10, 0, 10, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 12, 0, 10, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 10, 0, 12, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 12, 0, 12, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 8, 3, 6, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 14, 3, 6, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 4, 2, 4, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 4, 1, 4, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 4, 0, 4, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 18, 2, 4, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 18, 1, 4, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 18, 0, 4, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 4, 2, 18, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 4, 1, 18, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 4, 0, 18, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 18, 2, 18, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, 18, 1, 18, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 18, 0, 18, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 9, 7, 20, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, 13, 7, 20, structureboundingbox);
                this.fillWithBlocks(world, structureboundingbox, 6, 0, 21, 7, 4, 21, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 15, 0, 21, 16, 4, 21, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.spawnElder(world, structureboundingbox, 11, 2, 16);
            } else if (this.mainDesign == 1) {
                this.fillWithBlocks(world, structureboundingbox, 9, 3, 18, 13, 3, 20, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 9, 0, 18, 9, 2, 18, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 13, 0, 18, 13, 2, 18, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                byte b0 = 9;
                boolean flag = true;
                boolean flag1 = true;

                int j;

                for (j = 0; j < 2; ++j) {
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, b0, 6, 20, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, b0, 5, 20, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, b0, 4, 20, structureboundingbox);
                    b0 = 13;
                }

                this.fillWithBlocks(world, structureboundingbox, 7, 3, 7, 15, 3, 14, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                b0 = 10;

                for (j = 0; j < 2; ++j) {
                    this.fillWithBlocks(world, structureboundingbox, b0, 0, 10, b0, 6, 10, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, b0, 0, 12, b0, 6, 12, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, b0, 0, 10, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, b0, 0, 12, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, b0, 4, 10, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.WingRoom.SEA_LANTERN, b0, 4, 12, structureboundingbox);
                    b0 = 12;
                }

                b0 = 8;

                for (j = 0; j < 2; ++j) {
                    this.fillWithBlocks(world, structureboundingbox, b0, 0, 7, b0, 2, 7, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, b0, 0, 14, b0, 2, 14, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.WingRoom.BRICKS_PRISMARINE, false);
                    b0 = 14;
                }

                this.fillWithBlocks(world, structureboundingbox, 8, 3, 8, 8, 3, 13, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 14, 3, 8, 14, 3, 13, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.WingRoom.DARK_PRISMARINE, false);
                this.spawnElder(world, structureboundingbox, 11, 5, 13);
            }

            return true;
        }
    }

    public static class MonumentCoreRoom extends StructureOceanMonumentPieces.Piece {

        public MonumentCoreRoom() {}

        public MonumentCoreRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 2, 2, 2);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.generateBoxOnFillOnly(world, structureboundingbox, 1, 8, 0, 14, 8, 14, StructureOceanMonumentPieces.MonumentCoreRoom.ROUGH_PRISMARINE);
            boolean flag = true;
            IBlockState iblockdata = StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE;

            this.fillWithBlocks(world, structureboundingbox, 0, 7, 0, 0, 7, 15, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 15, 7, 0, 15, 7, 15, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 7, 0, 15, 7, 0, iblockdata, iblockdata, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 7, 15, 14, 7, 15, iblockdata, iblockdata, false);

            int i;

            for (i = 1; i <= 6; ++i) {
                iblockdata = StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE;
                if (i == 2 || i == 6) {
                    iblockdata = StructureOceanMonumentPieces.MonumentCoreRoom.ROUGH_PRISMARINE;
                }

                for (int j = 0; j <= 15; j += 15) {
                    this.fillWithBlocks(world, structureboundingbox, j, i, 0, j, i, 1, iblockdata, iblockdata, false);
                    this.fillWithBlocks(world, structureboundingbox, j, i, 6, j, i, 9, iblockdata, iblockdata, false);
                    this.fillWithBlocks(world, structureboundingbox, j, i, 14, j, i, 15, iblockdata, iblockdata, false);
                }

                this.fillWithBlocks(world, structureboundingbox, 1, i, 0, 1, i, 0, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 6, i, 0, 9, i, 0, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 14, i, 0, 14, i, 0, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 1, i, 15, 14, i, 15, iblockdata, iblockdata, false);
            }

            this.fillWithBlocks(world, structureboundingbox, 6, 3, 6, 9, 6, 9, StructureOceanMonumentPieces.MonumentCoreRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 4, 7, 8, 5, 8, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.GOLD_BLOCK.getDefaultState(), false);

            for (i = 3; i <= 6; i += 3) {
                for (int k = 6; k <= 9; k += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentCoreRoom.SEA_LANTERN, k, i, 6, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentCoreRoom.SEA_LANTERN, k, i, 9, structureboundingbox);
                }
            }

            this.fillWithBlocks(world, structureboundingbox, 5, 1, 6, 5, 2, 6, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 1, 9, 5, 2, 9, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 1, 6, 10, 2, 6, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 1, 9, 10, 2, 9, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 1, 5, 6, 2, 5, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 9, 1, 5, 9, 2, 5, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 1, 10, 6, 2, 10, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 9, 1, 10, 9, 2, 10, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 2, 5, 5, 6, 5, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 2, 10, 5, 6, 10, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 2, 5, 10, 6, 5, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 2, 10, 10, 6, 10, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 7, 1, 5, 7, 6, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 7, 1, 10, 7, 6, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 7, 9, 5, 7, 14, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 7, 9, 10, 7, 14, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 7, 5, 6, 7, 5, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 7, 10, 6, 7, 10, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 9, 7, 5, 14, 7, 5, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 9, 7, 10, 14, 7, 10, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 1, 2, 2, 1, 3, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 2, 3, 1, 2, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 13, 1, 2, 13, 1, 3, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 12, 1, 2, 12, 1, 2, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 1, 12, 2, 1, 13, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 13, 3, 1, 13, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 13, 1, 12, 13, 1, 13, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 12, 1, 13, 12, 1, 13, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentCoreRoom.BRICKS_PRISMARINE, false);
            return true;
        }
    }

    public static class DoubleYZRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleYZRoom() {}

        public DoubleYZRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 2, 2);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.roomDefinition.connections[EnumFacing.NORTH.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.roomDefinition;
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker2 = worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker3 = worldgenmonumentpieces_worldgenmonumentstatetracker1.connections[EnumFacing.UP.getIndex()];

            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(world, structureboundingbox, 0, 8, worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.DOWN.getIndex()]);
                this.generateDefaultFloor(world, structureboundingbox, 0, 0, worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.DOWN.getIndex()]);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 8, 1, 6, 8, 7, StructureOceanMonumentPieces.DoubleYZRoom.ROUGH_PRISMARINE);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 8, 8, 6, 8, 14, StructureOceanMonumentPieces.DoubleYZRoom.ROUGH_PRISMARINE);
            }

            int i;
            IBlockState iblockdata;

            for (i = 1; i <= 7; ++i) {
                iblockdata = StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE;
                if (i == 2 || i == 6) {
                    iblockdata = StructureOceanMonumentPieces.DoubleYZRoom.ROUGH_PRISMARINE;
                }

                this.fillWithBlocks(world, structureboundingbox, 0, i, 0, 0, i, 15, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 7, i, 0, 7, i, 15, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 1, i, 0, 6, i, 0, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 1, i, 15, 6, i, 15, iblockdata, iblockdata, false);
            }

            for (i = 1; i <= 7; ++i) {
                iblockdata = StructureOceanMonumentPieces.DoubleYZRoom.DARK_PRISMARINE;
                if (i == 2 || i == 6) {
                    iblockdata = StructureOceanMonumentPieces.DoubleYZRoom.SEA_LANTERN;
                }

                this.fillWithBlocks(world, structureboundingbox, 3, i, 7, 4, i, 8, iblockdata, iblockdata, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 7, 1, 3, 7, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 15, 4, 2, 15, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 1, 11, 0, 2, 12, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 7, 1, 11, 7, 2, 12, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 5, 0, 4, 6, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 7, 5, 3, 7, 6, 4, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 4, 2, 6, 4, 5, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 6, 1, 2, 6, 3, 2, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 6, 1, 5, 6, 3, 5, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 5, 3, 0, 6, 4, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 4, 2, 2, 4, 5, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 1, 2, 1, 3, 2, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 1, 5, 1, 3, 5, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 5, 15, 4, 6, 15, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 5, 11, 0, 6, 12, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 4, 10, 2, 4, 13, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 1, 10, 1, 3, 10, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 1, 13, 1, 3, 13, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 7, 5, 11, 7, 6, 12, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 4, 10, 6, 4, 13, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 6, 1, 10, 6, 3, 10, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 6, 1, 13, 6, 3, 13, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYZRoom.BRICKS_PRISMARINE, false);
            }

            return true;
        }
    }

    public static class DoubleXYRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleXYRoom() {}

        public DoubleXYRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 2, 2, 1);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.roomDefinition.connections[EnumFacing.EAST.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.roomDefinition;
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker2 = worldgenmonumentpieces_worldgenmonumentstatetracker1.connections[EnumFacing.UP.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker3 = worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()];

            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(world, structureboundingbox, 8, 0, worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.DOWN.getIndex()]);
                this.generateDefaultFloor(world, structureboundingbox, 0, 0, worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.DOWN.getIndex()]);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 8, 1, 7, 8, 6, StructureOceanMonumentPieces.DoubleXYRoom.ROUGH_PRISMARINE);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 8, 8, 1, 14, 8, 6, StructureOceanMonumentPieces.DoubleXYRoom.ROUGH_PRISMARINE);
            }

            for (int i = 1; i <= 7; ++i) {
                IBlockState iblockdata = StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE;

                if (i == 2 || i == 6) {
                    iblockdata = StructureOceanMonumentPieces.DoubleXYRoom.ROUGH_PRISMARINE;
                }

                this.fillWithBlocks(world, structureboundingbox, 0, i, 0, 0, i, 7, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 15, i, 0, 15, i, 7, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 1, i, 0, 15, i, 0, iblockdata, iblockdata, false);
                this.fillWithBlocks(world, structureboundingbox, 1, i, 7, 14, i, 7, iblockdata, iblockdata, false);
            }

            this.fillWithBlocks(world, structureboundingbox, 2, 1, 3, 2, 7, 4, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 2, 4, 7, 2, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 5, 4, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 13, 1, 3, 13, 7, 4, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 11, 1, 2, 12, 7, 2, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 11, 1, 5, 12, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 1, 3, 5, 3, 4, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 1, 3, 10, 3, 4, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 7, 2, 10, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 5, 2, 5, 7, 2, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 5, 2, 10, 7, 2, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 5, 5, 5, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 10, 5, 5, 10, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, 6, 6, 2, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, 9, 6, 2, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, 6, 6, 5, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, 9, 6, 5, structureboundingbox);
            this.fillWithBlocks(world, structureboundingbox, 5, 4, 3, 6, 4, 4, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 9, 4, 3, 10, 4, 4, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXYRoom.BRICKS_PRISMARINE, false);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXYRoom.SEA_LANTERN, 5, 4, 2, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXYRoom.SEA_LANTERN, 5, 4, 5, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXYRoom.SEA_LANTERN, 10, 4, 2, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXYRoom.SEA_LANTERN, 10, 4, 5, structureboundingbox);
            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 7, 4, 2, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 11, 1, 0, 12, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 11, 1, 7, 12, 2, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 15, 1, 3, 15, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 5, 0, 4, 6, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 5, 7, 4, 6, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 5, 3, 0, 6, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 11, 5, 0, 12, 6, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 11, 5, 7, 12, 6, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 15, 5, 3, 15, 6, 4, false);
            }

            return true;
        }
    }

    public static class DoubleZRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleZRoom() {}

        public DoubleZRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 1, 2);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.roomDefinition.connections[EnumFacing.NORTH.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.roomDefinition;

            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(world, structureboundingbox, 0, 8, worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.DOWN.getIndex()]);
                this.generateDefaultFloor(world, structureboundingbox, 0, 0, worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.DOWN.getIndex()]);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 4, 1, 6, 4, 7, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 4, 8, 6, 4, 14, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE);
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 0, 3, 15, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 3, 0, 7, 3, 15, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 0, 7, 3, 0, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 15, 6, 3, 15, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 2, 15, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 2, 0, 7, 2, 15, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 7, 2, 0, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 15, 6, 2, 15, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 1, 15, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 1, 0, 7, 1, 15, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 7, 1, 0, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 15, 6, 1, 15, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 1, 1, 1, 2, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 1, 1, 6, 1, 2, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 1, 1, 3, 2, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 3, 1, 6, 3, 2, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 13, 1, 1, 14, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 1, 13, 6, 1, 14, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 13, 1, 3, 14, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 3, 13, 6, 3, 14, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 1, 6, 2, 3, 6, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 1, 6, 5, 3, 6, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 1, 9, 2, 3, 9, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 1, 9, 5, 3, 9, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 2, 6, 4, 2, 6, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 2, 9, 4, 2, 9, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 2, 7, 2, 2, 8, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 2, 7, 5, 2, 8, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, false);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleZRoom.SEA_LANTERN, 2, 2, 5, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleZRoom.SEA_LANTERN, 5, 2, 5, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleZRoom.SEA_LANTERN, 2, 2, 10, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleZRoom.SEA_LANTERN, 5, 2, 10, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, 2, 3, 5, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, 5, 3, 5, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, 2, 3, 10, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleZRoom.BRICKS_PRISMARINE, 5, 3, 10, structureboundingbox);
            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 7, 1, 3, 7, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 15, 4, 2, 15, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 1, 11, 0, 2, 12, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 7, 1, 11, 7, 2, 12, false);
            }

            return true;
        }
    }

    public static class DoubleXRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleXRoom() {}

        public DoubleXRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 2, 1, 1);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.roomDefinition.connections[EnumFacing.EAST.getIndex()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.roomDefinition;

            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(world, structureboundingbox, 8, 0, worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.DOWN.getIndex()]);
                this.generateDefaultFloor(world, structureboundingbox, 0, 0, worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.DOWN.getIndex()]);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 4, 1, 7, 4, 6, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 8, 4, 1, 14, 4, 6, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE);
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 0, 3, 7, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 15, 3, 0, 15, 3, 7, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 0, 15, 3, 0, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 7, 14, 3, 7, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 2, 7, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 15, 2, 0, 15, 2, 7, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 15, 2, 0, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 7, 14, 2, 7, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 1, 7, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 15, 1, 0, 15, 1, 7, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 15, 1, 0, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 7, 14, 1, 7, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 1, 0, 10, 1, 4, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 2, 0, 9, 2, 3, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.ROUGH_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 3, 0, 10, 3, 4, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleXRoom.BRICKS_PRISMARINE, false);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXRoom.SEA_LANTERN, 6, 2, 3, structureboundingbox);
            this.setBlockState(world, StructureOceanMonumentPieces.DoubleXRoom.SEA_LANTERN, 9, 2, 3, structureboundingbox);
            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 7, 4, 2, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 11, 1, 0, 12, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 11, 1, 7, 12, 2, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 15, 1, 3, 15, 2, 4, false);
            }

            return true;
        }
    }

    public static class DoubleYRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleYRoom() {}

        public DoubleYRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 2, 1);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(world, structureboundingbox, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
            }

            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.roomDefinition.connections[EnumFacing.UP.getIndex()];

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 8, 1, 6, 8, 6, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE);
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 4, 0, 0, 4, 7, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 4, 0, 7, 4, 7, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 0, 6, 4, 0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 7, 6, 4, 7, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 4, 1, 2, 4, 2, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 2, 1, 4, 2, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 4, 1, 5, 4, 2, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 4, 2, 6, 4, 2, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 2, 4, 5, 2, 4, 6, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 4, 5, 1, 4, 5, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 4, 5, 5, 4, 6, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 4, 5, 6, 4, 5, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.roomDefinition;

            for (int i = 1; i <= 5; i += 4) {
                byte b0 = 0;

                if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 2, i, b0, 2, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 5, i, b0, 5, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 3, i + 2, b0, 4, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, 0, i, b0, 7, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 0, i + 1, b0, 7, i + 1, b0, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE, false);
                }

                b0 = 7;
                if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 2, i, b0, 2, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 5, i, b0, 5, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 3, i + 2, b0, 4, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, 0, i, b0, 7, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 0, i + 1, b0, 7, i + 1, b0, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE, false);
                }

                byte b1 = 0;

                if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, b1, i, 2, b1, i + 2, 2, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, b1, i, 5, b1, i + 2, 5, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, b1, i + 2, 3, b1, i + 2, 4, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, b1, i, 0, b1, i + 2, 7, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, b1, i + 1, 0, b1, i + 1, 7, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE, false);
                }

                b1 = 7;
                if (worldgenmonumentpieces_worldgenmonumentstatetracker1.hasOpening[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, b1, i, 2, b1, i + 2, 2, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, b1, i, 5, b1, i + 2, 5, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, b1, i + 2, 3, b1, i + 2, 4, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, b1, i, 0, b1, i + 2, 7, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, b1, i + 1, 0, b1, i + 1, 7, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.DoubleYRoom.ROUGH_PRISMARINE, false);
                }

                worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker;
            }

            return true;
        }
    }

    public static class SimpleTopRoom extends StructureOceanMonumentPieces.Piece {

        public SimpleTopRoom() {}

        public SimpleTopRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 1, 1);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(world, structureboundingbox, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
            }

            if (this.roomDefinition.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 4, 1, 6, 4, 6, StructureOceanMonumentPieces.SimpleTopRoom.ROUGH_PRISMARINE);
            }

            for (int i = 1; i <= 6; ++i) {
                for (int j = 1; j <= 6; ++j) {
                    if (random.nextInt(3) != 0) {
                        int k = 2 + (random.nextInt(4) == 0 ? 0 : 1);

                        this.fillWithBlocks(world, structureboundingbox, i, k, j, i, 3, j, Blocks.SPONGE.getStateFromMeta(1), Blocks.SPONGE.getStateFromMeta(1), false);
                    }
                }
            }

            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 1, 7, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 1, 0, 7, 1, 7, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 6, 1, 0, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 7, 6, 1, 7, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 2, 7, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 2, 0, 7, 2, 7, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 6, 2, 0, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 2, 7, 6, 2, 7, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 0, 3, 7, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 3, 0, 7, 3, 7, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 0, 6, 3, 0, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 3, 7, 6, 3, 7, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 3, 0, 2, 4, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 1, 3, 7, 2, 4, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 0, 4, 2, 0, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 3, 1, 7, 4, 2, 7, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleTopRoom.DARK_PRISMARINE, false);
            if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            return true;
        }
    }

    public static class SimpleRoom extends StructureOceanMonumentPieces.Piece {

        private int mainDesign;

        public SimpleRoom() {}

        public SimpleRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 1, 1);
            this.mainDesign = random.nextInt(3);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.roomDefinition.index / 25 > 0) {
                this.generateDefaultFloor(world, structureboundingbox, 0, 0, this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()]);
            }

            if (this.roomDefinition.connections[EnumFacing.UP.getIndex()] == null) {
                this.generateBoxOnFillOnly(world, structureboundingbox, 1, 4, 1, 6, 4, 6, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE);
            }

            boolean flag = this.mainDesign != 0 && random.nextBoolean() && !this.roomDefinition.hasOpening[EnumFacing.DOWN.getIndex()] && !this.roomDefinition.hasOpening[EnumFacing.UP.getIndex()] && this.roomDefinition.countOpenings() > 1;

            if (this.mainDesign == 0) {
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 2, 1, 2, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 2, 3, 2, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 2, 2, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 2, 2, 0, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.SEA_LANTERN, 1, 2, 1, structureboundingbox);
                this.fillWithBlocks(world, structureboundingbox, 5, 1, 0, 7, 1, 2, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 3, 0, 7, 3, 2, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 2, 0, 7, 2, 2, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 2, 0, 6, 2, 0, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.SEA_LANTERN, 6, 2, 1, structureboundingbox);
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 5, 2, 1, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 3, 5, 2, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 2, 5, 0, 2, 7, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 2, 7, 2, 2, 7, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.SEA_LANTERN, 1, 2, 6, structureboundingbox);
                this.fillWithBlocks(world, structureboundingbox, 5, 1, 5, 7, 1, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 3, 5, 7, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 2, 5, 7, 2, 7, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 2, 7, 6, 2, 7, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.SEA_LANTERN, 6, 2, 6, structureboundingbox);
                if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 3, 3, 0, 4, 3, 0, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, 3, 3, 0, 4, 3, 1, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 3, 2, 0, 4, 2, 0, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 3, 1, 0, 4, 1, 1, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                }

                if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 3, 3, 7, 4, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, 3, 3, 6, 4, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 3, 2, 7, 4, 2, 7, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 3, 1, 6, 4, 1, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                }

                if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 0, 3, 3, 0, 3, 4, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, 0, 3, 3, 1, 3, 4, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 0, 2, 3, 0, 2, 4, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 0, 1, 3, 1, 1, 4, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                }

                if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 7, 3, 3, 7, 3, 4, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                } else {
                    this.fillWithBlocks(world, structureboundingbox, 6, 3, 3, 7, 3, 4, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 7, 2, 3, 7, 2, 4, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 6, 1, 3, 7, 1, 4, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                }
            } else if (this.mainDesign == 1) {
                this.fillWithBlocks(world, structureboundingbox, 2, 1, 2, 2, 3, 2, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 2, 1, 5, 2, 3, 5, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 1, 5, 5, 3, 5, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 1, 2, 5, 3, 2, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.SEA_LANTERN, 2, 2, 2, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.SEA_LANTERN, 2, 2, 5, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.SEA_LANTERN, 5, 2, 5, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.SEA_LANTERN, 5, 2, 2, structureboundingbox);
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 1, 3, 0, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 3, 1, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 7, 1, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 6, 0, 3, 6, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 6, 1, 7, 7, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 1, 6, 7, 3, 6, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 6, 1, 0, 7, 3, 0, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 1, 1, 7, 3, 1, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, 1, 2, 0, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, 0, 2, 1, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, 1, 2, 7, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, 0, 2, 6, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, 6, 2, 7, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, 7, 2, 6, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, 6, 2, 0, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, 7, 2, 1, structureboundingbox);
                if (!this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 1, 3, 0, 6, 3, 0, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 6, 2, 0, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 6, 1, 0, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                }

                if (!this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 1, 3, 7, 6, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 1, 2, 7, 6, 2, 7, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 1, 1, 7, 6, 1, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                }

                if (!this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 0, 3, 1, 0, 3, 6, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 0, 2, 1, 0, 2, 6, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 0, 1, 1, 0, 1, 6, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                }

                if (!this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                    this.fillWithBlocks(world, structureboundingbox, 7, 3, 1, 7, 3, 6, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 7, 2, 1, 7, 2, 6, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 7, 1, 1, 7, 1, 6, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                }
            } else if (this.mainDesign == 2) {
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 1, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 1, 0, 7, 1, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 6, 1, 0, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 1, 7, 6, 1, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 0, 2, 7, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 2, 0, 7, 2, 7, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 2, 0, 6, 2, 0, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 2, 7, 6, 2, 7, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 0, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 3, 0, 7, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 3, 0, 6, 3, 0, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 1, 3, 7, 6, 3, 7, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 0, 1, 3, 0, 2, 4, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 7, 1, 3, 7, 2, 4, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 3, 1, 0, 4, 2, 0, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 3, 1, 7, 4, 2, 7, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.DARK_PRISMARINE, false);
                if (this.roomDefinition.hasOpening[EnumFacing.SOUTH.getIndex()]) {
                    this.generateWaterBox(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
                }

                if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                    this.generateWaterBox(world, structureboundingbox, 3, 1, 7, 4, 2, 7, false);
                }

                if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                    this.generateWaterBox(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
                }

                if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                    this.generateWaterBox(world, structureboundingbox, 7, 1, 3, 7, 2, 4, false);
                }
            }

            if (flag) {
                this.fillWithBlocks(world, structureboundingbox, 3, 1, 3, 4, 1, 4, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 3, 2, 3, 4, 2, 4, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 3, 3, 3, 4, 3, 4, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.SimpleRoom.BRICKS_PRISMARINE, false);
            }

            return true;
        }
    }

    public static class EntryRoom extends StructureOceanMonumentPieces.Piece {

        public EntryRoom() {}

        public EntryRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 1, 1);
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithBlocks(world, structureboundingbox, 0, 3, 0, 2, 3, 7, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 3, 0, 7, 3, 7, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 2, 0, 1, 2, 7, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 6, 2, 0, 7, 2, 7, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 0, 0, 1, 7, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 7, 1, 0, 7, 1, 7, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 0, 1, 7, 7, 3, 7, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 1, 1, 0, 2, 3, 0, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            this.fillWithBlocks(world, structureboundingbox, 5, 1, 0, 6, 3, 0, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, StructureOceanMonumentPieces.EntryRoom.BRICKS_PRISMARINE, false);
            if (this.roomDefinition.hasOpening[EnumFacing.NORTH.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 3, 1, 7, 4, 2, 7, false);
            }

            if (this.roomDefinition.hasOpening[EnumFacing.WEST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 0, 1, 3, 1, 2, 4, false);
            }

            if (this.roomDefinition.hasOpening[EnumFacing.EAST.getIndex()]) {
                this.generateWaterBox(world, structureboundingbox, 6, 1, 3, 7, 2, 4, false);
            }

            return true;
        }
    }

    public static class MonumentBuilding extends StructureOceanMonumentPieces.Piece {

        private StructureOceanMonumentPieces.RoomDefinition sourceRoom;
        private StructureOceanMonumentPieces.RoomDefinition coreRoom;
        private final List<StructureOceanMonumentPieces.Piece> childPieces = Lists.newArrayList();

        public MonumentBuilding() {}

        public MonumentBuilding(Random random, int i, int j, EnumFacing enumdirection) {
            super(0);
            this.setCoordBaseMode(enumdirection);
            EnumFacing enumdirection1 = this.getCoordBaseMode();

            if (enumdirection1.getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(i, 39, j, i + 58 - 1, 61, j + 58 - 1);
            } else {
                this.boundingBox = new StructureBoundingBox(i, 39, j, i + 58 - 1, 61, j + 58 - 1);
            }

            List list = this.generateRoomGraph(random);

            this.sourceRoom.claimed = true;
            this.childPieces.add(new StructureOceanMonumentPieces.EntryRoom(enumdirection1, this.sourceRoom));
            this.childPieces.add(new StructureOceanMonumentPieces.MonumentCoreRoom(enumdirection1, this.coreRoom, random));
            ArrayList arraylist = Lists.newArrayList();

            arraylist.add(new StructureOceanMonumentPieces.XYDoubleRoomFitHelper(null));
            arraylist.add(new StructureOceanMonumentPieces.YZDoubleRoomFitHelper(null));
            arraylist.add(new StructureOceanMonumentPieces.ZDoubleRoomFitHelper(null));
            arraylist.add(new StructureOceanMonumentPieces.XDoubleRoomFitHelper(null));
            arraylist.add(new StructureOceanMonumentPieces.YDoubleRoomFitHelper(null));
            arraylist.add(new StructureOceanMonumentPieces.FitSimpleRoomTopHelper(null));
            arraylist.add(new StructureOceanMonumentPieces.FitSimpleRoomHelper(null));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = (StructureOceanMonumentPieces.RoomDefinition) iterator.next();

                if (!worldgenmonumentpieces_worldgenmonumentstatetracker.claimed && !worldgenmonumentpieces_worldgenmonumentstatetracker.isSpecial()) {
                    Iterator iterator1 = arraylist.iterator();

                    while (iterator1.hasNext()) {
                        StructureOceanMonumentPieces.MonumentRoomFitHelper worldgenmonumentpieces_iworldgenmonumentpieceselector = (StructureOceanMonumentPieces.MonumentRoomFitHelper) iterator1.next();

                        if (worldgenmonumentpieces_iworldgenmonumentpieceselector.fits(worldgenmonumentpieces_worldgenmonumentstatetracker)) {
                            this.childPieces.add(worldgenmonumentpieces_iworldgenmonumentpieceselector.create(enumdirection1, worldgenmonumentpieces_worldgenmonumentstatetracker, random));
                            break;
                        }
                    }
                }
            }

            int k = this.boundingBox.minY;
            int l = this.getXWithOffset(9, 22);
            int i1 = this.getZWithOffset(9, 22);
            Iterator iterator2 = this.childPieces.iterator();

            while (iterator2.hasNext()) {
                StructureOceanMonumentPieces.Piece worldgenmonumentpieces_worldgenmonumentpiece = (StructureOceanMonumentPieces.Piece) iterator2.next();

                worldgenmonumentpieces_worldgenmonumentpiece.getBoundingBox().offset(l, k, i1);
            }

            StructureBoundingBox structureboundingbox = StructureBoundingBox.createProper(this.getXWithOffset(1, 1), this.getYWithOffset(1), this.getZWithOffset(1, 1), this.getXWithOffset(23, 21), this.getYWithOffset(8), this.getZWithOffset(23, 21));
            StructureBoundingBox structureboundingbox1 = StructureBoundingBox.createProper(this.getXWithOffset(34, 1), this.getYWithOffset(1), this.getZWithOffset(34, 1), this.getXWithOffset(56, 21), this.getYWithOffset(8), this.getZWithOffset(56, 21));
            StructureBoundingBox structureboundingbox2 = StructureBoundingBox.createProper(this.getXWithOffset(22, 22), this.getYWithOffset(13), this.getZWithOffset(22, 22), this.getXWithOffset(35, 35), this.getYWithOffset(17), this.getZWithOffset(35, 35));
            int j1 = random.nextInt();

            this.childPieces.add(new StructureOceanMonumentPieces.WingRoom(enumdirection1, structureboundingbox, j1++));
            this.childPieces.add(new StructureOceanMonumentPieces.WingRoom(enumdirection1, structureboundingbox1, j1++));
            this.childPieces.add(new StructureOceanMonumentPieces.Penthouse(enumdirection1, structureboundingbox2));
        }

        private List<StructureOceanMonumentPieces.RoomDefinition> generateRoomGraph(Random random) {
            StructureOceanMonumentPieces.RoomDefinition[] aworldgenmonumentpieces_worldgenmonumentstatetracker = new StructureOceanMonumentPieces.RoomDefinition[75];

            int i;
            int j;
            boolean flag;
            int k;

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 4; ++j) {
                    flag = false;
                    k = getRoomIndex(i, 0, j);
                    aworldgenmonumentpieces_worldgenmonumentstatetracker[k] = new StructureOceanMonumentPieces.RoomDefinition(k);
                }
            }

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 4; ++j) {
                    flag = true;
                    k = getRoomIndex(i, 1, j);
                    aworldgenmonumentpieces_worldgenmonumentstatetracker[k] = new StructureOceanMonumentPieces.RoomDefinition(k);
                }
            }

            for (i = 1; i < 4; ++i) {
                for (j = 0; j < 2; ++j) {
                    flag = true;
                    k = getRoomIndex(i, 2, j);
                    aworldgenmonumentpieces_worldgenmonumentstatetracker[k] = new StructureOceanMonumentPieces.RoomDefinition(k);
                }
            }

            this.sourceRoom = aworldgenmonumentpieces_worldgenmonumentstatetracker[StructureOceanMonumentPieces.MonumentBuilding.GRIDROOM_SOURCE_INDEX];

            int l;
            int i1;
            int j1;
            int k1;
            int l1;

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 5; ++j) {
                    for (int i2 = 0; i2 < 3; ++i2) {
                        k = getRoomIndex(i, i2, j);
                        if (aworldgenmonumentpieces_worldgenmonumentstatetracker[k] != null) {
                            EnumFacing[] aenumdirection = EnumFacing.values();

                            l = aenumdirection.length;

                            for (i1 = 0; i1 < l; ++i1) {
                                EnumFacing enumdirection = aenumdirection[i1];

                                j1 = i + enumdirection.getFrontOffsetX();
                                k1 = i2 + enumdirection.getFrontOffsetY();
                                l1 = j + enumdirection.getFrontOffsetZ();
                                if (j1 >= 0 && j1 < 5 && l1 >= 0 && l1 < 5 && k1 >= 0 && k1 < 3) {
                                    int j2 = getRoomIndex(j1, k1, l1);

                                    if (aworldgenmonumentpieces_worldgenmonumentstatetracker[j2] != null) {
                                        if (l1 == j) {
                                            aworldgenmonumentpieces_worldgenmonumentstatetracker[k].setConnection(enumdirection, aworldgenmonumentpieces_worldgenmonumentstatetracker[j2]);
                                        } else {
                                            aworldgenmonumentpieces_worldgenmonumentstatetracker[k].setConnection(enumdirection.getOpposite(), aworldgenmonumentpieces_worldgenmonumentstatetracker[j2]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = new StructureOceanMonumentPieces.RoomDefinition(1003);
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = new StructureOceanMonumentPieces.RoomDefinition(1001);
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker2 = new StructureOceanMonumentPieces.RoomDefinition(1002);

            aworldgenmonumentpieces_worldgenmonumentstatetracker[StructureOceanMonumentPieces.MonumentBuilding.GRIDROOM_TOP_CONNECT_INDEX].setConnection(EnumFacing.UP, worldgenmonumentpieces_worldgenmonumentstatetracker);
            aworldgenmonumentpieces_worldgenmonumentstatetracker[StructureOceanMonumentPieces.MonumentBuilding.GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(EnumFacing.SOUTH, worldgenmonumentpieces_worldgenmonumentstatetracker1);
            aworldgenmonumentpieces_worldgenmonumentstatetracker[StructureOceanMonumentPieces.MonumentBuilding.GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(EnumFacing.SOUTH, worldgenmonumentpieces_worldgenmonumentstatetracker2);
            worldgenmonumentpieces_worldgenmonumentstatetracker.claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker1.claimed = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker2.claimed = true;
            this.sourceRoom.isSource = true;
            this.coreRoom = aworldgenmonumentpieces_worldgenmonumentstatetracker[getRoomIndex(random.nextInt(4), 0, 2)];
            this.coreRoom.claimed = true;
            this.coreRoom.connections[EnumFacing.EAST.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.NORTH.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.NORTH.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.UP.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            this.coreRoom.connections[EnumFacing.EAST.getIndex()].connections[EnumFacing.NORTH.getIndex()].connections[EnumFacing.UP.getIndex()].claimed = true;
            ArrayList arraylist = Lists.newArrayList();
            StructureOceanMonumentPieces.RoomDefinition[] aworldgenmonumentpieces_worldgenmonumentstatetracker1 = aworldgenmonumentpieces_worldgenmonumentstatetracker;

            l = aworldgenmonumentpieces_worldgenmonumentstatetracker.length;

            for (i1 = 0; i1 < l; ++i1) {
                StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker3 = aworldgenmonumentpieces_worldgenmonumentstatetracker1[i1];

                if (worldgenmonumentpieces_worldgenmonumentstatetracker3 != null) {
                    worldgenmonumentpieces_worldgenmonumentstatetracker3.updateOpenings();
                    arraylist.add(worldgenmonumentpieces_worldgenmonumentstatetracker3);
                }
            }

            worldgenmonumentpieces_worldgenmonumentstatetracker.updateOpenings();
            Collections.shuffle(arraylist, random);
            int k2 = 1;
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker4 = (StructureOceanMonumentPieces.RoomDefinition) iterator.next();
                int l2 = 0;

                j1 = 0;

                while (l2 < 2 && j1 < 5) {
                    ++j1;
                    k1 = random.nextInt(6);
                    if (worldgenmonumentpieces_worldgenmonumentstatetracker4.hasOpening[k1]) {
                        l1 = EnumFacing.getFront(k1).getOpposite().getIndex();
                        worldgenmonumentpieces_worldgenmonumentstatetracker4.hasOpening[k1] = false;
                        worldgenmonumentpieces_worldgenmonumentstatetracker4.connections[k1].hasOpening[l1] = false;
                        if (worldgenmonumentpieces_worldgenmonumentstatetracker4.findSource(k2++) && worldgenmonumentpieces_worldgenmonumentstatetracker4.connections[k1].findSource(k2++)) {
                            ++l2;
                        } else {
                            worldgenmonumentpieces_worldgenmonumentstatetracker4.hasOpening[k1] = true;
                            worldgenmonumentpieces_worldgenmonumentstatetracker4.connections[k1].hasOpening[l1] = true;
                        }
                    }
                }
            }

            arraylist.add(worldgenmonumentpieces_worldgenmonumentstatetracker);
            arraylist.add(worldgenmonumentpieces_worldgenmonumentstatetracker1);
            arraylist.add(worldgenmonumentpieces_worldgenmonumentstatetracker2);
            return arraylist;
        }

        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            int i = Math.max(world.getSeaLevel(), 64) - this.boundingBox.minY;

            this.generateWaterBox(world, structureboundingbox, 0, 0, 0, 58, i, 58, false);
            this.generateWing(false, 0, world, random, structureboundingbox);
            this.generateWing(true, 33, world, random, structureboundingbox);
            this.generateEntranceArchs(world, random, structureboundingbox);
            this.generateEntranceWall(world, random, structureboundingbox);
            this.generateRoofPiece(world, random, structureboundingbox);
            this.generateLowerWall(world, random, structureboundingbox);
            this.generateMiddleWall(world, random, structureboundingbox);
            this.generateUpperWall(world, random, structureboundingbox);

            int j;

            for (j = 0; j < 7; ++j) {
                int k = 0;

                while (k < 7) {
                    if (k == 0 && j == 3) {
                        k = 6;
                    }

                    int l = j * 9;
                    int i1 = k * 9;

                    for (int j1 = 0; j1 < 4; ++j1) {
                        for (int k1 = 0; k1 < 4; ++k1) {
                            this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, l + j1, 0, i1 + k1, structureboundingbox);
                            this.replaceAirAndLiquidDownwards(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, l + j1, -1, i1 + k1, structureboundingbox);
                        }
                    }

                    if (j != 0 && j != 6) {
                        k += 6;
                    } else {
                        ++k;
                    }
                }
            }

            for (j = 0; j < 5; ++j) {
                this.generateWaterBox(world, structureboundingbox, -1 - j, 0 + j * 2, -1 - j, -1 - j, 23, 58 + j, false);
                this.generateWaterBox(world, structureboundingbox, 58 + j, 0 + j * 2, -1 - j, 58 + j, 23, 58 + j, false);
                this.generateWaterBox(world, structureboundingbox, 0 - j, 0 + j * 2, -1 - j, 57 + j, 23, -1 - j, false);
                this.generateWaterBox(world, structureboundingbox, 0 - j, 0 + j * 2, 58 + j, 57 + j, 23, 58 + j, false);
            }

            Iterator iterator = this.childPieces.iterator();

            while (iterator.hasNext()) {
                StructureOceanMonumentPieces.Piece worldgenmonumentpieces_worldgenmonumentpiece = (StructureOceanMonumentPieces.Piece) iterator.next();

                if (worldgenmonumentpieces_worldgenmonumentpiece.getBoundingBox().intersectsWith(structureboundingbox)) {
                    worldgenmonumentpieces_worldgenmonumentpiece.addComponentParts(world, random, structureboundingbox);
                }
            }

            return true;
        }

        private void generateWing(boolean flag, int i, World world, Random random, StructureBoundingBox structureboundingbox) {
            boolean flag1 = true;

            if (this.doesChunkIntersect(structureboundingbox, i, 0, i + 23, 20)) {
                this.fillWithBlocks(world, structureboundingbox, i + 0, 0, 0, i + 24, 0, 20, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, i + 0, 1, 0, i + 24, 10, 20, false);

                int j;

                for (j = 0; j < 4; ++j) {
                    this.fillWithBlocks(world, structureboundingbox, i + j, j + 1, j, i + j, j + 1, 20, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, i + j + 7, j + 5, j + 7, i + j + 7, j + 5, 20, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, i + 17 - j, j + 5, j + 7, i + 17 - j, j + 5, 20, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, i + 24 - j, j + 1, j, i + 24 - j, j + 1, 20, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, i + j + 1, j + 1, j, i + 23 - j, j + 1, j, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, i + j + 8, j + 5, j + 7, i + 16 - j, j + 5, j + 7, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                this.fillWithBlocks(world, structureboundingbox, i + 4, 4, 4, i + 6, 4, 20, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 7, 4, 4, i + 17, 4, 6, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 18, 4, 4, i + 20, 4, 20, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 11, 8, 11, i + 13, 8, 20, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i + 12, 9, 12, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i + 12, 9, 15, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i + 12, 9, 18, structureboundingbox);
                j = i + (flag ? 19 : 5);
                int k = i + (flag ? 5 : 19);

                int l;

                for (l = 20; l >= 5; l -= 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, j, 5, l, structureboundingbox);
                }

                for (l = 19; l >= 7; l -= 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, k, 5, l, structureboundingbox);
                }

                for (l = 0; l < 4; ++l) {
                    int i1 = flag ? i + (24 - (17 - l * 3)) : i + 17 - l * 3;

                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i1, 5, 5, structureboundingbox);
                }

                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, k, 5, 5, structureboundingbox);
                this.fillWithBlocks(world, structureboundingbox, i + 11, 1, 12, i + 13, 7, 12, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 12, 1, 11, i + 12, 7, 13, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
            }

        }

        private void generateEntranceArchs(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.doesChunkIntersect(structureboundingbox, 22, 5, 35, 17)) {
                this.generateWaterBox(world, structureboundingbox, 25, 0, 0, 32, 8, 20, false);

                for (int i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 24, 2, 5 + i * 4, 24, 4, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 22, 4, 5 + i * 4, 23, 4, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 25, 5, 5 + i * 4, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 26, 6, 5 + i * 4, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.SEA_LANTERN, 26, 5, 5 + i * 4, structureboundingbox);
                    this.fillWithBlocks(world, structureboundingbox, 33, 2, 5 + i * 4, 33, 4, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 34, 4, 5 + i * 4, 35, 4, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 32, 5, 5 + i * 4, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 31, 6, 5 + i * 4, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.SEA_LANTERN, 31, 5, 5 + i * 4, structureboundingbox);
                    this.fillWithBlocks(world, structureboundingbox, 27, 6, 5 + i * 4, 30, 6, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                }
            }

        }

        private void generateEntranceWall(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.doesChunkIntersect(structureboundingbox, 15, 20, 42, 21)) {
                this.fillWithBlocks(world, structureboundingbox, 15, 0, 21, 42, 0, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 26, 1, 21, 31, 3, 21, false);
                this.fillWithBlocks(world, structureboundingbox, 21, 12, 21, 36, 12, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 17, 11, 21, 40, 11, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 16, 10, 21, 41, 10, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 15, 7, 21, 42, 9, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 16, 6, 21, 41, 6, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 17, 5, 21, 40, 5, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 21, 4, 21, 36, 4, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 22, 3, 21, 26, 3, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 31, 3, 21, 35, 3, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 23, 2, 21, 25, 2, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 32, 2, 21, 34, 2, 21, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 28, 4, 20, 29, 4, 21, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 27, 3, 21, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 30, 3, 21, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 26, 2, 21, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 31, 2, 21, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 25, 1, 21, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 32, 1, 21, structureboundingbox);

                int i;

                for (i = 0; i < 7; ++i) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 28 - i, 6 + i, 21, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 29 + i, 6 + i, 21, structureboundingbox);
                }

                for (i = 0; i < 4; ++i) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 28 - i, 9 + i, 21, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 29 + i, 9 + i, 21, structureboundingbox);
                }

                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 28, 12, 21, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 29, 12, 21, structureboundingbox);

                for (i = 0; i < 3; ++i) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 22 - i * 2, 8, 21, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 22 - i * 2, 9, 21, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 35 + i * 2, 8, 21, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DARK_PRISMARINE, 35 + i * 2, 9, 21, structureboundingbox);
                }

                this.generateWaterBox(world, structureboundingbox, 15, 13, 21, 42, 15, 21, false);
                this.generateWaterBox(world, structureboundingbox, 15, 1, 21, 15, 6, 21, false);
                this.generateWaterBox(world, structureboundingbox, 16, 1, 21, 16, 5, 21, false);
                this.generateWaterBox(world, structureboundingbox, 17, 1, 21, 20, 4, 21, false);
                this.generateWaterBox(world, structureboundingbox, 21, 1, 21, 21, 3, 21, false);
                this.generateWaterBox(world, structureboundingbox, 22, 1, 21, 22, 2, 21, false);
                this.generateWaterBox(world, structureboundingbox, 23, 1, 21, 24, 1, 21, false);
                this.generateWaterBox(world, structureboundingbox, 42, 1, 21, 42, 6, 21, false);
                this.generateWaterBox(world, structureboundingbox, 41, 1, 21, 41, 5, 21, false);
                this.generateWaterBox(world, structureboundingbox, 37, 1, 21, 40, 4, 21, false);
                this.generateWaterBox(world, structureboundingbox, 36, 1, 21, 36, 3, 21, false);
                this.generateWaterBox(world, structureboundingbox, 33, 1, 21, 34, 1, 21, false);
                this.generateWaterBox(world, structureboundingbox, 35, 1, 21, 35, 2, 21, false);
            }

        }

        private void generateRoofPiece(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.doesChunkIntersect(structureboundingbox, 21, 21, 36, 36)) {
                this.fillWithBlocks(world, structureboundingbox, 21, 0, 22, 36, 0, 36, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 21, 1, 22, 36, 23, 36, false);

                for (int i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 21 + i, 13 + i, 21 + i, 36 - i, 13 + i, 21 + i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 21 + i, 13 + i, 36 - i, 36 - i, 13 + i, 36 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 21 + i, 13 + i, 22 + i, 21 + i, 13 + i, 35 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                    this.fillWithBlocks(world, structureboundingbox, 36 - i, 13 + i, 22 + i, 36 - i, 13 + i, 35 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                this.fillWithBlocks(world, structureboundingbox, 25, 16, 25, 32, 16, 32, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 25, 17, 25, 25, 19, 25, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 32, 17, 25, 32, 19, 25, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 25, 17, 32, 25, 19, 32, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 32, 17, 32, 32, 19, 32, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 26, 20, 26, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 27, 21, 27, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.SEA_LANTERN, 27, 20, 27, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 26, 20, 31, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 27, 21, 30, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.SEA_LANTERN, 27, 20, 30, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 31, 20, 31, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 30, 21, 30, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.SEA_LANTERN, 30, 20, 30, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 31, 20, 26, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, 30, 21, 27, structureboundingbox);
                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.SEA_LANTERN, 30, 20, 27, structureboundingbox);
                this.fillWithBlocks(world, structureboundingbox, 28, 21, 27, 29, 21, 27, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 27, 21, 28, 27, 21, 29, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 28, 21, 30, 29, 21, 30, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 30, 21, 28, 30, 21, 29, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
            }

        }

        private void generateLowerWall(World world, Random random, StructureBoundingBox structureboundingbox) {
            int i;

            if (this.doesChunkIntersect(structureboundingbox, 0, 21, 6, 58)) {
                this.fillWithBlocks(world, structureboundingbox, 0, 0, 21, 6, 0, 57, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 0, 1, 21, 6, 7, 57, false);
                this.fillWithBlocks(world, structureboundingbox, 4, 4, 21, 6, 4, 53, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, i, i + 1, 21, i, i + 1, 57 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                for (i = 23; i < 53; i += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, 5, 5, i, structureboundingbox);
                }

                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, 5, 5, 52, structureboundingbox);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, i, i + 1, 21, i, i + 1, 57 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                this.fillWithBlocks(world, structureboundingbox, 4, 1, 52, 6, 3, 52, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 5, 1, 51, 5, 3, 53, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
            }

            if (this.doesChunkIntersect(structureboundingbox, 51, 21, 58, 58)) {
                this.fillWithBlocks(world, structureboundingbox, 51, 0, 21, 57, 0, 57, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 51, 1, 21, 57, 7, 57, false);
                this.fillWithBlocks(world, structureboundingbox, 51, 4, 21, 53, 4, 53, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 57 - i, i + 1, 21, 57 - i, i + 1, 57 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                for (i = 23; i < 53; i += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, 52, 5, i, structureboundingbox);
                }

                this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, 52, 5, 52, structureboundingbox);
                this.fillWithBlocks(world, structureboundingbox, 51, 1, 52, 53, 3, 52, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 52, 1, 51, 52, 3, 53, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
            }

            if (this.doesChunkIntersect(structureboundingbox, 0, 51, 57, 57)) {
                this.fillWithBlocks(world, structureboundingbox, 7, 0, 51, 50, 0, 57, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 7, 1, 51, 50, 10, 57, false);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, i + 1, i + 1, 57 - i, 56 - i, i + 1, 57 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }
            }

        }

        private void generateMiddleWall(World world, Random random, StructureBoundingBox structureboundingbox) {
            int i;

            if (this.doesChunkIntersect(structureboundingbox, 7, 21, 13, 50)) {
                this.fillWithBlocks(world, structureboundingbox, 7, 0, 21, 13, 0, 50, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 7, 1, 21, 13, 10, 50, false);
                this.fillWithBlocks(world, structureboundingbox, 11, 8, 21, 13, 8, 53, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, i + 7, i + 5, 21, i + 7, i + 5, 54, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                for (i = 21; i <= 45; i += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, 12, 9, i, structureboundingbox);
                }
            }

            if (this.doesChunkIntersect(structureboundingbox, 44, 21, 50, 54)) {
                this.fillWithBlocks(world, structureboundingbox, 44, 0, 21, 50, 0, 50, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 44, 1, 21, 50, 10, 50, false);
                this.fillWithBlocks(world, structureboundingbox, 44, 8, 21, 46, 8, 53, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 50 - i, i + 5, 21, 50 - i, i + 5, 54, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                for (i = 21; i <= 45; i += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, 45, 9, i, structureboundingbox);
                }
            }

            if (this.doesChunkIntersect(structureboundingbox, 8, 44, 49, 54)) {
                this.fillWithBlocks(world, structureboundingbox, 14, 0, 44, 43, 0, 50, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 14, 1, 44, 43, 10, 50, false);

                for (i = 12; i <= 45; i += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 9, 45, structureboundingbox);
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 9, 52, structureboundingbox);
                    if (i == 12 || i == 18 || i == 24 || i == 33 || i == 39 || i == 45) {
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 9, 47, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 9, 50, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 10, 45, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 10, 46, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 10, 51, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 10, 52, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 11, 47, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 11, 50, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 12, 48, structureboundingbox);
                        this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 12, 49, structureboundingbox);
                    }
                }

                for (i = 0; i < 3; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 8 + i, 5 + i, 54, 49 - i, 5 + i, 54, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                }

                this.fillWithBlocks(world, structureboundingbox, 11, 8, 54, 46, 8, 54, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 14, 8, 44, 43, 8, 53, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
            }

        }

        private void generateUpperWall(World world, Random random, StructureBoundingBox structureboundingbox) {
            int i;

            if (this.doesChunkIntersect(structureboundingbox, 14, 21, 20, 43)) {
                this.fillWithBlocks(world, structureboundingbox, 14, 0, 21, 20, 0, 43, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 14, 1, 22, 20, 14, 43, false);
                this.fillWithBlocks(world, structureboundingbox, 18, 12, 22, 20, 12, 39, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 18, 12, 21, 20, 12, 21, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, i + 14, i + 9, 21, i + 14, i + 9, 43 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                for (i = 23; i <= 39; i += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, 19, 13, i, structureboundingbox);
                }
            }

            if (this.doesChunkIntersect(structureboundingbox, 37, 21, 43, 43)) {
                this.fillWithBlocks(world, structureboundingbox, 37, 0, 21, 43, 0, 43, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 37, 1, 22, 43, 14, 43, false);
                this.fillWithBlocks(world, structureboundingbox, 37, 12, 22, 39, 12, 39, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, 37, 12, 21, 39, 12, 21, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 43 - i, i + 9, 21, 43 - i, i + 9, 43 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                for (i = 23; i <= 39; i += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, 38, 13, i, structureboundingbox);
                }
            }

            if (this.doesChunkIntersect(structureboundingbox, 15, 37, 42, 43)) {
                this.fillWithBlocks(world, structureboundingbox, 21, 0, 37, 36, 0, 43, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);
                this.generateWaterBox(world, structureboundingbox, 21, 1, 37, 36, 14, 43, false);
                this.fillWithBlocks(world, structureboundingbox, 21, 12, 37, 36, 12, 39, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.ROUGH_PRISMARINE, false);

                for (i = 0; i < 4; ++i) {
                    this.fillWithBlocks(world, structureboundingbox, 15 + i, i + 9, 43 - i, 42 - i, i + 9, 43 - i, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, StructureOceanMonumentPieces.MonumentBuilding.BRICKS_PRISMARINE, false);
                }

                for (i = 21; i <= 36; i += 3) {
                    this.setBlockState(world, StructureOceanMonumentPieces.MonumentBuilding.DOT_DECO_DATA, i, 13, 38, structureboundingbox);
                }
            }

        }
    }

    public abstract static class Piece extends StructureComponent {

        protected static final IBlockState ROUGH_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.ROUGH_META);
        protected static final IBlockState BRICKS_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.BRICKS_META);
        protected static final IBlockState DARK_PRISMARINE = Blocks.PRISMARINE.getStateFromMeta(BlockPrismarine.DARK_META);
        protected static final IBlockState DOT_DECO_DATA = StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE;
        protected static final IBlockState SEA_LANTERN = Blocks.SEA_LANTERN.getDefaultState();
        protected static final IBlockState WATER = Blocks.WATER.getDefaultState();
        protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
        protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
        protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
        protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
        protected StructureOceanMonumentPieces.RoomDefinition roomDefinition;

        protected static final int getRoomIndex(int i, int j, int k) {
            return j * 25 + k * 5 + i;
        }

        public Piece() {
            super(0);
        }

        public Piece(int i) {
            super(i);
        }

        public Piece(EnumFacing enumdirection, StructureBoundingBox structureboundingbox) {
            super(1);
            this.setCoordBaseMode(enumdirection);
            this.boundingBox = structureboundingbox;
        }

        protected Piece(int i, EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, int j, int k, int l) {
            super(i);
            this.setCoordBaseMode(enumdirection);
            this.roomDefinition = worldgenmonumentpieces_worldgenmonumentstatetracker;
            int i1 = worldgenmonumentpieces_worldgenmonumentstatetracker.index;
            int j1 = i1 % 5;
            int k1 = i1 / 5 % 5;
            int l1 = i1 / 25;

            if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.SOUTH) {
                this.boundingBox = new StructureBoundingBox(0, 0, 0, l * 8 - 1, k * 4 - 1, j * 8 - 1);
            } else {
                this.boundingBox = new StructureBoundingBox(0, 0, 0, j * 8 - 1, k * 4 - 1, l * 8 - 1);
            }

            switch (enumdirection) {
            case NORTH:
                this.boundingBox.offset(j1 * 8, l1 * 4, -(k1 + l) * 8 + 1);
                break;

            case SOUTH:
                this.boundingBox.offset(j1 * 8, l1 * 4, k1 * 8);
                break;

            case WEST:
                this.boundingBox.offset(-(k1 + l) * 8 + 1, l1 * 4, j1 * 8);
                break;

            default:
                this.boundingBox.offset(k1 * 8, l1 * 4, j1 * 8);
            }

        }

        protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {}

        protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {}

        protected void generateWaterBox(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, boolean flag) {
            for (int k1 = j; k1 <= i1; ++k1) {
                for (int l1 = i; l1 <= l; ++l1) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        if (!flag || this.getBlockStateFromPos(world, l1, k1, i2, structureboundingbox).getMaterial() != Material.AIR) {
                            if (this.getYWithOffset(k1) >= world.getSeaLevel()) {
                                this.setBlockState(world, Blocks.AIR.getDefaultState(), l1, k1, i2, structureboundingbox);
                            } else {
                                this.setBlockState(world, StructureOceanMonumentPieces.Piece.WATER, l1, k1, i2, structureboundingbox);
                            }
                        }
                    }
                }
            }

        }

        protected void generateDefaultFloor(World world, StructureBoundingBox structureboundingbox, int i, int j, boolean flag) {
            if (flag) {
                this.fillWithBlocks(world, structureboundingbox, i + 0, 0, j + 0, i + 2, 0, j + 8 - 1, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 5, 0, j + 0, i + 8 - 1, 0, j + 8 - 1, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 3, 0, j + 0, i + 4, 0, j + 2, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 3, 0, j + 5, i + 4, 0, j + 8 - 1, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 3, 0, j + 2, i + 4, 0, j + 2, StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 3, 0, j + 5, i + 4, 0, j + 5, StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 2, 0, j + 3, i + 2, 0, j + 4, StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE, false);
                this.fillWithBlocks(world, structureboundingbox, i + 5, 0, j + 3, i + 5, 0, j + 4, StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE, StructureOceanMonumentPieces.Piece.BRICKS_PRISMARINE, false);
            } else {
                this.fillWithBlocks(world, structureboundingbox, i + 0, 0, j + 0, i + 8 - 1, 0, j + 8 - 1, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, StructureOceanMonumentPieces.Piece.ROUGH_PRISMARINE, false);
            }

        }

        protected void generateBoxOnFillOnly(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, IBlockState iblockdata) {
            for (int k1 = j; k1 <= i1; ++k1) {
                for (int l1 = i; l1 <= l; ++l1) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        if (this.getBlockStateFromPos(world, l1, k1, i2, structureboundingbox) == StructureOceanMonumentPieces.Piece.WATER) {
                            this.setBlockState(world, iblockdata, l1, k1, i2, structureboundingbox);
                        }
                    }
                }
            }

        }

        protected boolean doesChunkIntersect(StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
            int i1 = this.getXWithOffset(i, j);
            int j1 = this.getZWithOffset(i, j);
            int k1 = this.getXWithOffset(k, l);
            int l1 = this.getZWithOffset(k, l);

            return structureboundingbox.intersectsWith(Math.min(i1, k1), Math.min(j1, l1), Math.max(i1, k1), Math.max(j1, l1));
        }

        protected boolean spawnElder(World world, StructureBoundingBox structureboundingbox, int i, int j, int k) {
            int l = this.getXWithOffset(i, k);
            int i1 = this.getYWithOffset(j);
            int j1 = this.getZWithOffset(i, k);

            if (structureboundingbox.isVecInside((Vec3i) (new BlockPos(l, i1, j1)))) {
                EntityElderGuardian entityguardianelder = new EntityElderGuardian(world);

                entityguardianelder.heal(entityguardianelder.getMaxHealth());
                entityguardianelder.setLocationAndAngles((double) l + 0.5D, (double) i1, (double) j1 + 0.5D, 0.0F, 0.0F);
                entityguardianelder.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityguardianelder)), (IEntityLivingData) null);
                world.spawnEntity(entityguardianelder);
                return true;
            } else {
                return false;
            }
        }
    }
}
