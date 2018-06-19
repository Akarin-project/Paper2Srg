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

    public static void func_175970_a() {
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.MonumentBuilding.class, "OMB");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.MonumentCoreRoom.class, "OMCR");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.DoubleXRoom.class, "OMDXR");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.DoubleXYRoom.class, "OMDXYR");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.DoubleYRoom.class, "OMDYR");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.DoubleYZRoom.class, "OMDYZR");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.DoubleZRoom.class, "OMDZR");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.EntryRoom.class, "OMEntry");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.Penthouse.class, "OMPenthouse");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.SimpleRoom.class, "OMSimple");
        MapGenStructureIO.func_143031_a(StructureOceanMonumentPieces.SimpleTopRoom.class, "OMSimpleT");
    }

    static class YZDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private YZDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.NORTH.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175963_d && worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.UP.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d) {
                StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.NORTH.func_176745_a()];

                return worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.UP.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d;
            } else {
                return false;
            }
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleYZRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        YZDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class XYDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private XYDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.EAST.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175963_d && worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.UP.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d) {
                StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.EAST.func_176745_a()];

                return worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.UP.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d;
            } else {
                return false;
            }
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleXYRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        XYDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class ZDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private ZDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.NORTH.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175963_d;
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker;

            if (!worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.NORTH.func_176745_a()] || worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175963_d) {
                worldgenmonumentpieces_worldgenmonumentstatetracker1 = worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.SOUTH.func_176745_a()];
            }

            worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleZRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker1, random);
        }

        ZDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class XDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private XDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.EAST.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175963_d;
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleXRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        XDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class YDoubleRoomFitHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private YDoubleRoomFitHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.UP.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d;
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            return new StructureOceanMonumentPieces.DoubleYRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        YDoubleRoomFitHelper(Object object) {
            this();
        }
    }

    static class FitSimpleRoomTopHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private FitSimpleRoomTopHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.WEST.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.EAST.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.NORTH.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.SOUTH.func_176745_a()] && !worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.UP.func_176745_a()];
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175963_d = true;
            return new StructureOceanMonumentPieces.SimpleTopRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        FitSimpleRoomTopHelper(Object object) {
            this();
        }
    }

    static class FitSimpleRoomHelper implements StructureOceanMonumentPieces.MonumentRoomFitHelper {

        private FitSimpleRoomHelper() {}

        public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            return true;
        }

        public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175963_d = true;
            return new StructureOceanMonumentPieces.SimpleRoom(enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, random);
        }

        FitSimpleRoomHelper(Object object) {
            this();
        }
    }

    interface MonumentRoomFitHelper {

        boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker);

        StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random);
    }

    static class RoomDefinition {

        int field_175967_a;
        StructureOceanMonumentPieces.RoomDefinition[] field_175965_b = new StructureOceanMonumentPieces.RoomDefinition[6];
        boolean[] field_175966_c = new boolean[6];
        boolean field_175963_d;
        boolean field_175964_e;
        int field_175962_f;

        public RoomDefinition(int i) {
            this.field_175967_a = i;
        }

        public void func_175957_a(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            this.field_175965_b[enumdirection.func_176745_a()] = worldgenmonumentpieces_worldgenmonumentstatetracker;
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[enumdirection.func_176734_d().func_176745_a()] = this;
        }

        public void func_175958_a() {
            for (int i = 0; i < 6; ++i) {
                this.field_175966_c[i] = this.field_175965_b[i] != null;
            }

        }

        public boolean func_175959_a(int i) {
            if (this.field_175964_e) {
                return true;
            } else {
                this.field_175962_f = i;

                for (int j = 0; j < 6; ++j) {
                    if (this.field_175965_b[j] != null && this.field_175966_c[j] && this.field_175965_b[j].field_175962_f != i && this.field_175965_b[j].func_175959_a(i)) {
                        return true;
                    }
                }

                return false;
            }
        }

        public boolean func_175961_b() {
            return this.field_175967_a >= 75;
        }

        public int func_175960_c() {
            int i = 0;

            for (int j = 0; j < 6; ++j) {
                if (this.field_175966_c[j]) {
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

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 2, -1, 2, 11, -1, 11, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 0, -1, 0, 1, -1, 11, StructureOceanMonumentPieces.Penthouse.field_175828_a, StructureOceanMonumentPieces.Penthouse.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 12, -1, 0, 13, -1, 11, StructureOceanMonumentPieces.Penthouse.field_175828_a, StructureOceanMonumentPieces.Penthouse.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 2, -1, 0, 11, -1, 1, StructureOceanMonumentPieces.Penthouse.field_175828_a, StructureOceanMonumentPieces.Penthouse.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 2, -1, 12, 11, -1, 13, StructureOceanMonumentPieces.Penthouse.field_175828_a, StructureOceanMonumentPieces.Penthouse.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 0, 0, 13, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 13, 0, 0, 13, 0, 13, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 0, 12, 0, 0, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 13, 12, 0, 13, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);

            for (int i = 2; i <= 11; i += 3) {
                this.func_175811_a(world, StructureOceanMonumentPieces.Penthouse.field_175825_e, 0, 0, i, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.Penthouse.field_175825_e, 13, 0, i, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.Penthouse.field_175825_e, i, 0, 0, structureboundingbox);
            }

            this.func_175804_a(world, structureboundingbox, 2, 0, 3, 4, 0, 9, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 9, 0, 3, 11, 0, 9, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 4, 0, 9, 9, 0, 11, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175811_a(world, StructureOceanMonumentPieces.Penthouse.field_175826_b, 5, 0, 8, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.Penthouse.field_175826_b, 8, 0, 8, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.Penthouse.field_175826_b, 10, 0, 10, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.Penthouse.field_175826_b, 3, 0, 10, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 3, 0, 3, 3, 0, 7, StructureOceanMonumentPieces.Penthouse.field_175827_c, StructureOceanMonumentPieces.Penthouse.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 10, 0, 3, 10, 0, 7, StructureOceanMonumentPieces.Penthouse.field_175827_c, StructureOceanMonumentPieces.Penthouse.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 6, 0, 10, 7, 0, 10, StructureOceanMonumentPieces.Penthouse.field_175827_c, StructureOceanMonumentPieces.Penthouse.field_175827_c, false);
            byte b0 = 3;

            for (int j = 0; j < 2; ++j) {
                for (int k = 2; k <= 8; k += 3) {
                    this.func_175804_a(world, structureboundingbox, b0, 0, k, b0, 2, k, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
                }

                b0 = 10;
            }

            this.func_175804_a(world, structureboundingbox, 5, 0, 10, 5, 2, 10, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 8, 0, 10, 8, 2, 10, StructureOceanMonumentPieces.Penthouse.field_175826_b, StructureOceanMonumentPieces.Penthouse.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, -1, 7, 7, -1, 8, StructureOceanMonumentPieces.Penthouse.field_175827_c, StructureOceanMonumentPieces.Penthouse.field_175827_c, false);
            this.func_181655_a(world, structureboundingbox, 6, -1, 3, 7, -1, 4, false);
            this.func_175817_a(world, structureboundingbox, 6, 1, 6);
            return true;
        }
    }

    public static class WingRoom extends StructureOceanMonumentPieces.Piece {

        private int field_175834_o;

        public WingRoom() {}

        public WingRoom(EnumFacing enumdirection, StructureBoundingBox structureboundingbox, int i) {
            super(enumdirection, structureboundingbox);
            this.field_175834_o = i & 1;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_175834_o == 0) {
                int i;

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, 10 - i, 3 - i, 20 - i, 12 + i, 3 - i, 20, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                }

                this.func_175804_a(world, structureboundingbox, 7, 0, 6, 15, 0, 16, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 6, 0, 6, 6, 3, 20, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 16, 0, 6, 16, 3, 20, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 7, 1, 7, 7, 1, 20, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 15, 1, 7, 15, 1, 20, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 7, 1, 6, 9, 3, 6, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 13, 1, 6, 15, 3, 6, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 8, 1, 7, 9, 1, 7, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 13, 1, 7, 14, 1, 7, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 9, 0, 5, 13, 0, 5, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 10, 0, 7, 12, 0, 7, StructureOceanMonumentPieces.WingRoom.field_175827_c, StructureOceanMonumentPieces.WingRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 8, 0, 10, 8, 0, 12, StructureOceanMonumentPieces.WingRoom.field_175827_c, StructureOceanMonumentPieces.WingRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 14, 0, 10, 14, 0, 12, StructureOceanMonumentPieces.WingRoom.field_175827_c, StructureOceanMonumentPieces.WingRoom.field_175827_c, false);

                for (i = 18; i >= 7; i -= 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 6, 3, i, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 16, 3, i, structureboundingbox);
                }

                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 10, 0, 10, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 12, 0, 10, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 10, 0, 12, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 12, 0, 12, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 8, 3, 6, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 14, 3, 6, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 4, 2, 4, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 4, 1, 4, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 4, 0, 4, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 18, 2, 4, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 18, 1, 4, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 18, 0, 4, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 4, 2, 18, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 4, 1, 18, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 4, 0, 18, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 18, 2, 18, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, 18, 1, 18, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 18, 0, 18, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 9, 7, 20, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, 13, 7, 20, structureboundingbox);
                this.func_175804_a(world, structureboundingbox, 6, 0, 21, 7, 4, 21, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 15, 0, 21, 16, 4, 21, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175817_a(world, structureboundingbox, 11, 2, 16);
            } else if (this.field_175834_o == 1) {
                this.func_175804_a(world, structureboundingbox, 9, 3, 18, 13, 3, 20, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 9, 0, 18, 9, 2, 18, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 13, 0, 18, 13, 2, 18, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                byte b0 = 9;
                boolean flag = true;
                boolean flag1 = true;

                int j;

                for (j = 0; j < 2; ++j) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, b0, 6, 20, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, b0, 5, 20, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175826_b, b0, 4, 20, structureboundingbox);
                    b0 = 13;
                }

                this.func_175804_a(world, structureboundingbox, 7, 3, 7, 15, 3, 14, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                b0 = 10;

                for (j = 0; j < 2; ++j) {
                    this.func_175804_a(world, structureboundingbox, b0, 0, 10, b0, 6, 10, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, b0, 0, 12, b0, 6, 12, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, b0, 0, 10, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, b0, 0, 12, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, b0, 4, 10, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.WingRoom.field_175825_e, b0, 4, 12, structureboundingbox);
                    b0 = 12;
                }

                b0 = 8;

                for (j = 0; j < 2; ++j) {
                    this.func_175804_a(world, structureboundingbox, b0, 0, 7, b0, 2, 7, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, b0, 0, 14, b0, 2, 14, StructureOceanMonumentPieces.WingRoom.field_175826_b, StructureOceanMonumentPieces.WingRoom.field_175826_b, false);
                    b0 = 14;
                }

                this.func_175804_a(world, structureboundingbox, 8, 3, 8, 8, 3, 13, StructureOceanMonumentPieces.WingRoom.field_175827_c, StructureOceanMonumentPieces.WingRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 14, 3, 8, 14, 3, 13, StructureOceanMonumentPieces.WingRoom.field_175827_c, StructureOceanMonumentPieces.WingRoom.field_175827_c, false);
                this.func_175817_a(world, structureboundingbox, 11, 5, 13);
            }

            return true;
        }
    }

    public static class MonumentCoreRoom extends StructureOceanMonumentPieces.Piece {

        public MonumentCoreRoom() {}

        public MonumentCoreRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 2, 2, 2);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175819_a(world, structureboundingbox, 1, 8, 0, 14, 8, 14, StructureOceanMonumentPieces.MonumentCoreRoom.field_175828_a);
            boolean flag = true;
            IBlockState iblockdata = StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b;

            this.func_175804_a(world, structureboundingbox, 0, 7, 0, 0, 7, 15, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 15, 7, 0, 15, 7, 15, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 7, 0, 15, 7, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 7, 15, 14, 7, 15, iblockdata, iblockdata, false);

            int i;

            for (i = 1; i <= 6; ++i) {
                iblockdata = StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b;
                if (i == 2 || i == 6) {
                    iblockdata = StructureOceanMonumentPieces.MonumentCoreRoom.field_175828_a;
                }

                for (int j = 0; j <= 15; j += 15) {
                    this.func_175804_a(world, structureboundingbox, j, i, 0, j, i, 1, iblockdata, iblockdata, false);
                    this.func_175804_a(world, structureboundingbox, j, i, 6, j, i, 9, iblockdata, iblockdata, false);
                    this.func_175804_a(world, structureboundingbox, j, i, 14, j, i, 15, iblockdata, iblockdata, false);
                }

                this.func_175804_a(world, structureboundingbox, 1, i, 0, 1, i, 0, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 6, i, 0, 9, i, 0, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 14, i, 0, 14, i, 0, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 1, i, 15, 14, i, 15, iblockdata, iblockdata, false);
            }

            this.func_175804_a(world, structureboundingbox, 6, 3, 6, 9, 6, 9, StructureOceanMonumentPieces.MonumentCoreRoom.field_175827_c, StructureOceanMonumentPieces.MonumentCoreRoom.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 7, 4, 7, 8, 5, 8, Blocks.field_150340_R.func_176223_P(), Blocks.field_150340_R.func_176223_P(), false);

            for (i = 3; i <= 6; i += 3) {
                for (int k = 6; k <= 9; k += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentCoreRoom.field_175825_e, k, i, 6, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentCoreRoom.field_175825_e, k, i, 9, structureboundingbox);
                }
            }

            this.func_175804_a(world, structureboundingbox, 5, 1, 6, 5, 2, 6, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 1, 9, 5, 2, 9, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 1, 6, 10, 2, 6, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 1, 9, 10, 2, 9, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 1, 5, 6, 2, 5, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 9, 1, 5, 9, 2, 5, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 1, 10, 6, 2, 10, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 9, 1, 10, 9, 2, 10, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 2, 5, 5, 6, 5, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 2, 10, 5, 6, 10, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 2, 5, 10, 6, 5, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 2, 10, 10, 6, 10, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 7, 1, 5, 7, 6, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 7, 1, 10, 7, 6, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 7, 9, 5, 7, 14, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 7, 9, 10, 7, 14, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 7, 5, 6, 7, 5, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 7, 10, 6, 7, 10, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 9, 7, 5, 14, 7, 5, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 9, 7, 10, 14, 7, 10, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 2, 1, 2, 2, 1, 3, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 2, 3, 1, 2, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 13, 1, 2, 13, 1, 3, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 12, 1, 2, 12, 1, 2, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 2, 1, 12, 2, 1, 13, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 13, 3, 1, 13, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 13, 1, 12, 13, 1, 13, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 12, 1, 13, 12, 1, 13, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, StructureOceanMonumentPieces.MonumentCoreRoom.field_175826_b, false);
            return true;
        }
    }

    public static class DoubleYZRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleYZRoom() {}

        public DoubleYZRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 2, 2);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.field_175830_k.field_175965_b[EnumFacing.NORTH.func_176745_a()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.field_175830_k;
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker2 = worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker3 = worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175965_b[EnumFacing.UP.func_176745_a()];

            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureboundingbox, 0, 8, worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
                this.func_175821_a(world, structureboundingbox, 0, 0, worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 8, 1, 6, 8, 7, StructureOceanMonumentPieces.DoubleYZRoom.field_175828_a);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 8, 8, 6, 8, 14, StructureOceanMonumentPieces.DoubleYZRoom.field_175828_a);
            }

            int i;
            IBlockState iblockdata;

            for (i = 1; i <= 7; ++i) {
                iblockdata = StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b;
                if (i == 2 || i == 6) {
                    iblockdata = StructureOceanMonumentPieces.DoubleYZRoom.field_175828_a;
                }

                this.func_175804_a(world, structureboundingbox, 0, i, 0, 0, i, 15, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 7, i, 0, 7, i, 15, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 1, i, 0, 6, i, 0, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 1, i, 15, 6, i, 15, iblockdata, iblockdata, false);
            }

            for (i = 1; i <= 7; ++i) {
                iblockdata = StructureOceanMonumentPieces.DoubleYZRoom.field_175827_c;
                if (i == 2 || i == 6) {
                    iblockdata = StructureOceanMonumentPieces.DoubleYZRoom.field_175825_e;
                }

                this.func_175804_a(world, structureboundingbox, 3, i, 7, 4, i, 8, iblockdata, iblockdata, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 7, 1, 3, 7, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 15, 4, 2, 15, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 1, 11, 0, 2, 12, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 7, 1, 11, 7, 2, 12, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 5, 0, 4, 6, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 7, 5, 3, 7, 6, 4, false);
                this.func_175804_a(world, structureboundingbox, 5, 4, 2, 6, 4, 5, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 6, 1, 2, 6, 3, 2, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 6, 1, 5, 6, 3, 5, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 5, 3, 0, 6, 4, false);
                this.func_175804_a(world, structureboundingbox, 1, 4, 2, 2, 4, 5, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 1, 1, 2, 1, 3, 2, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 1, 1, 5, 1, 3, 5, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 5, 15, 4, 6, 15, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 5, 11, 0, 6, 12, false);
                this.func_175804_a(world, structureboundingbox, 1, 4, 10, 2, 4, 13, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 1, 1, 10, 1, 3, 10, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 1, 1, 13, 1, 3, 13, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 7, 5, 11, 7, 6, 12, false);
                this.func_175804_a(world, structureboundingbox, 5, 4, 10, 6, 4, 13, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 6, 1, 10, 6, 3, 10, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 6, 1, 13, 6, 3, 13, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYZRoom.field_175826_b, false);
            }

            return true;
        }
    }

    public static class DoubleXYRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleXYRoom() {}

        public DoubleXYRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 2, 2, 1);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.field_175830_k.field_175965_b[EnumFacing.EAST.func_176745_a()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.field_175830_k;
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker2 = worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175965_b[EnumFacing.UP.func_176745_a()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker3 = worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()];

            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureboundingbox, 8, 0, worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
                this.func_175821_a(world, structureboundingbox, 0, 0, worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 8, 1, 7, 8, 6, StructureOceanMonumentPieces.DoubleXYRoom.field_175828_a);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 8, 8, 1, 14, 8, 6, StructureOceanMonumentPieces.DoubleXYRoom.field_175828_a);
            }

            for (int i = 1; i <= 7; ++i) {
                IBlockState iblockdata = StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b;

                if (i == 2 || i == 6) {
                    iblockdata = StructureOceanMonumentPieces.DoubleXYRoom.field_175828_a;
                }

                this.func_175804_a(world, structureboundingbox, 0, i, 0, 0, i, 7, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 15, i, 0, 15, i, 7, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 1, i, 0, 15, i, 0, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 1, i, 7, 14, i, 7, iblockdata, iblockdata, false);
            }

            this.func_175804_a(world, structureboundingbox, 2, 1, 3, 2, 7, 4, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 2, 4, 7, 2, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 5, 4, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 13, 1, 3, 13, 7, 4, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 11, 1, 2, 12, 7, 2, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 11, 1, 5, 12, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 1, 3, 5, 3, 4, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 1, 3, 10, 3, 4, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 7, 2, 10, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 5, 2, 5, 7, 2, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 5, 2, 10, 7, 2, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 5, 5, 5, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 10, 5, 5, 10, 7, 5, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, 6, 6, 2, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, 9, 6, 2, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, 6, 6, 5, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, 9, 6, 5, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 5, 4, 3, 6, 4, 4, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 9, 4, 3, 10, 4, 4, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXYRoom.field_175826_b, false);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXYRoom.field_175825_e, 5, 4, 2, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXYRoom.field_175825_e, 5, 4, 5, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXYRoom.field_175825_e, 10, 4, 2, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXYRoom.field_175825_e, 10, 4, 5, structureboundingbox);
            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 7, 4, 2, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 11, 1, 0, 12, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 11, 1, 7, 12, 2, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 15, 1, 3, 15, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 5, 0, 4, 6, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 5, 7, 4, 6, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 5, 3, 0, 6, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 11, 5, 0, 12, 6, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 11, 5, 7, 12, 6, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker3.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 15, 5, 3, 15, 6, 4, false);
            }

            return true;
        }
    }

    public static class DoubleZRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleZRoom() {}

        public DoubleZRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 1, 2);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.field_175830_k.field_175965_b[EnumFacing.NORTH.func_176745_a()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.field_175830_k;

            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureboundingbox, 0, 8, worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
                this.func_175821_a(world, structureboundingbox, 0, 0, worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 4, 1, 6, 4, 7, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 4, 8, 6, 4, 14, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a);
            }

            this.func_175804_a(world, structureboundingbox, 0, 3, 0, 0, 3, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 7, 3, 0, 7, 3, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 0, 7, 3, 0, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 15, 6, 3, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 2, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 7, 2, 0, 7, 2, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 0, 7, 2, 0, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 15, 6, 2, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a, StructureOceanMonumentPieces.DoubleZRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 0, 1, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 7, 1, 0, 7, 1, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 0, 7, 1, 0, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 15, 6, 1, 15, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 1, 1, 1, 2, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 1, 1, 6, 1, 2, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 1, 1, 3, 2, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 3, 1, 6, 3, 2, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 13, 1, 1, 14, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 1, 13, 6, 1, 14, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 13, 1, 3, 14, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 3, 13, 6, 3, 14, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 2, 1, 6, 2, 3, 6, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 1, 6, 5, 3, 6, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 2, 1, 9, 2, 3, 9, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 1, 9, 5, 3, 9, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 3, 2, 6, 4, 2, 6, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 3, 2, 9, 4, 2, 9, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 2, 2, 7, 2, 2, 8, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 2, 7, 5, 2, 8, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, false);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleZRoom.field_175825_e, 2, 2, 5, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleZRoom.field_175825_e, 5, 2, 5, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleZRoom.field_175825_e, 2, 2, 10, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleZRoom.field_175825_e, 5, 2, 10, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, 2, 3, 5, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, 5, 3, 5, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, 2, 3, 10, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleZRoom.field_175826_b, 5, 3, 10, structureboundingbox);
            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 7, 1, 3, 7, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 15, 4, 2, 15, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 1, 11, 0, 2, 12, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 7, 1, 11, 7, 2, 12, false);
            }

            return true;
        }
    }

    public static class DoubleXRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleXRoom() {}

        public DoubleXRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 2, 1, 1);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.field_175830_k.field_175965_b[EnumFacing.EAST.func_176745_a()];
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.field_175830_k;

            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureboundingbox, 8, 0, worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
                this.func_175821_a(world, structureboundingbox, 0, 0, worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 4, 1, 7, 4, 6, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 8, 4, 1, 14, 4, 6, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a);
            }

            this.func_175804_a(world, structureboundingbox, 0, 3, 0, 0, 3, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 15, 3, 0, 15, 3, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 0, 15, 3, 0, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 7, 14, 3, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 2, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 15, 2, 0, 15, 2, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 0, 15, 2, 0, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 7, 14, 2, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 0, 1, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 15, 1, 0, 15, 1, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 0, 15, 1, 0, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 7, 14, 1, 7, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 1, 0, 10, 1, 4, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 2, 0, 9, 2, 3, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, StructureOceanMonumentPieces.DoubleXRoom.field_175828_a, false);
            this.func_175804_a(world, structureboundingbox, 5, 3, 0, 10, 3, 4, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, StructureOceanMonumentPieces.DoubleXRoom.field_175826_b, false);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXRoom.field_175825_e, 6, 2, 3, structureboundingbox);
            this.func_175811_a(world, StructureOceanMonumentPieces.DoubleXRoom.field_175825_e, 9, 2, 3, structureboundingbox);
            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 7, 4, 2, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 11, 1, 0, 12, 2, 0, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 11, 1, 7, 12, 2, 7, false);
            }

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 15, 1, 3, 15, 2, 4, false);
            }

            return true;
        }
    }

    public static class DoubleYRoom extends StructureOceanMonumentPieces.Piece {

        public DoubleYRoom() {}

        public DoubleYRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 2, 1);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureboundingbox, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
            }

            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker = this.field_175830_k.field_175965_b[EnumFacing.UP.func_176745_a()];

            if (worldgenmonumentpieces_worldgenmonumentstatetracker.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 8, 1, 6, 8, 6, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a);
            }

            this.func_175804_a(world, structureboundingbox, 0, 4, 0, 0, 4, 7, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 7, 4, 0, 7, 4, 7, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 4, 0, 6, 4, 0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 4, 7, 6, 4, 7, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 2, 4, 1, 2, 4, 2, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 4, 2, 1, 4, 2, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 4, 1, 5, 4, 2, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 4, 2, 6, 4, 2, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 2, 4, 5, 2, 4, 6, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 4, 5, 1, 4, 5, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 4, 5, 5, 4, 6, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 4, 5, 6, 4, 5, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
            StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker1 = this.field_175830_k;

            for (int i = 1; i <= 5; i += 4) {
                byte b0 = 0;

                if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 2, i, b0, 2, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 5, i, b0, 5, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 3, i + 2, b0, 4, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, 0, i, b0, 7, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 0, i + 1, b0, 7, i + 1, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a, false);
                }

                b0 = 7;
                if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 2, i, b0, 2, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 5, i, b0, 5, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 3, i + 2, b0, 4, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, 0, i, b0, 7, i + 2, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 0, i + 1, b0, 7, i + 1, b0, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a, false);
                }

                byte b1 = 0;

                if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, b1, i, 2, b1, i + 2, 2, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, b1, i, 5, b1, i + 2, 5, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, b1, i + 2, 3, b1, i + 2, 4, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, b1, i, 0, b1, i + 2, 7, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, b1, i + 1, 0, b1, i + 1, 7, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a, false);
                }

                b1 = 7;
                if (worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, b1, i, 2, b1, i + 2, 2, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, b1, i, 5, b1, i + 2, 5, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, b1, i + 2, 3, b1, i + 2, 4, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, b1, i, 0, b1, i + 2, 7, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, StructureOceanMonumentPieces.DoubleYRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, b1, i + 1, 0, b1, i + 1, 7, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a, StructureOceanMonumentPieces.DoubleYRoom.field_175828_a, false);
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

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureboundingbox, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
            }

            if (this.field_175830_k.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 4, 1, 6, 4, 6, StructureOceanMonumentPieces.SimpleTopRoom.field_175828_a);
            }

            for (int i = 1; i <= 6; ++i) {
                for (int j = 1; j <= 6; ++j) {
                    if (random.nextInt(3) != 0) {
                        int k = 2 + (random.nextInt(4) == 0 ? 0 : 1);

                        this.func_175804_a(world, structureboundingbox, i, k, j, i, 3, j, Blocks.field_150360_v.func_176203_a(1), Blocks.field_150360_v.func_176203_a(1), false);
                    }
                }
            }

            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 0, 1, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 7, 1, 0, 7, 1, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 0, 6, 1, 0, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 7, 6, 1, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 2, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 7, 2, 0, 7, 2, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 0, 6, 2, 0, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 7, 6, 2, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 0, 0, 3, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 7, 3, 0, 7, 3, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 0, 6, 3, 0, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 7, 6, 3, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, StructureOceanMonumentPieces.SimpleTopRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 3, 0, 2, 4, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 7, 1, 3, 7, 2, 4, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 0, 4, 2, 0, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 7, 4, 2, 7, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, StructureOceanMonumentPieces.SimpleTopRoom.field_175827_c, false);
            if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
            }

            return true;
        }
    }

    public static class SimpleRoom extends StructureOceanMonumentPieces.Piece {

        private int field_175833_o;

        public SimpleRoom() {}

        public SimpleRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, Random random) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 1, 1);
            this.field_175833_o = random.nextInt(3);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_175830_k.field_175967_a / 25 > 0) {
                this.func_175821_a(world, structureboundingbox, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.func_176745_a()]);
            }

            if (this.field_175830_k.field_175965_b[EnumFacing.UP.func_176745_a()] == null) {
                this.func_175819_a(world, structureboundingbox, 1, 4, 1, 6, 4, 6, StructureOceanMonumentPieces.SimpleRoom.field_175828_a);
            }

            boolean flag = this.field_175833_o != 0 && random.nextBoolean() && !this.field_175830_k.field_175966_c[EnumFacing.DOWN.func_176745_a()] && !this.field_175830_k.field_175966_c[EnumFacing.UP.func_176745_a()] && this.field_175830_k.func_175960_c() > 1;

            if (this.field_175833_o == 0) {
                this.func_175804_a(world, structureboundingbox, 0, 1, 0, 2, 1, 2, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 3, 0, 2, 3, 2, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 2, 2, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 1, 2, 0, 2, 2, 0, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175825_e, 1, 2, 1, structureboundingbox);
                this.func_175804_a(world, structureboundingbox, 5, 1, 0, 7, 1, 2, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 5, 3, 0, 7, 3, 2, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 7, 2, 0, 7, 2, 2, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 5, 2, 0, 6, 2, 0, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175825_e, 6, 2, 1, structureboundingbox);
                this.func_175804_a(world, structureboundingbox, 0, 1, 5, 2, 1, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 3, 5, 2, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 2, 5, 0, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 1, 2, 7, 2, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175825_e, 1, 2, 6, structureboundingbox);
                this.func_175804_a(world, structureboundingbox, 5, 1, 5, 7, 1, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 5, 3, 5, 7, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 7, 2, 5, 7, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 5, 2, 7, 6, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175825_e, 6, 2, 6, structureboundingbox);
                if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 3, 3, 0, 4, 3, 0, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, 3, 3, 0, 4, 3, 1, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 3, 2, 0, 4, 2, 0, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                    this.func_175804_a(world, structureboundingbox, 3, 1, 0, 4, 1, 1, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 3, 3, 7, 4, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, 3, 3, 6, 4, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 3, 2, 7, 4, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                    this.func_175804_a(world, structureboundingbox, 3, 1, 6, 4, 1, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 0, 3, 3, 0, 3, 4, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, 0, 3, 3, 1, 3, 4, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 0, 2, 3, 0, 2, 4, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                    this.func_175804_a(world, structureboundingbox, 0, 1, 3, 1, 1, 4, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 7, 3, 3, 7, 3, 4, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, 6, 3, 3, 7, 3, 4, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 7, 2, 3, 7, 2, 4, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                    this.func_175804_a(world, structureboundingbox, 6, 1, 3, 7, 1, 4, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                }
            } else if (this.field_175833_o == 1) {
                this.func_175804_a(world, structureboundingbox, 2, 1, 2, 2, 3, 2, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 2, 1, 5, 2, 3, 5, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 5, 1, 5, 5, 3, 5, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 5, 1, 2, 5, 3, 2, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175825_e, 2, 2, 2, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175825_e, 2, 2, 5, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175825_e, 5, 2, 5, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175825_e, 5, 2, 2, structureboundingbox);
                this.func_175804_a(world, structureboundingbox, 0, 1, 0, 1, 3, 0, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 3, 1, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 1, 7, 1, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 1, 6, 0, 3, 6, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 6, 1, 7, 7, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 7, 1, 6, 7, 3, 6, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 6, 1, 0, 7, 3, 0, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 7, 1, 1, 7, 3, 1, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, 1, 2, 0, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, 0, 2, 1, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, 1, 2, 7, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, 0, 2, 6, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, 6, 2, 7, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, 7, 2, 6, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, 6, 2, 0, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, 7, 2, 1, structureboundingbox);
                if (!this.field_175830_k.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 1, 3, 0, 6, 3, 0, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 1, 2, 0, 6, 2, 0, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                    this.func_175804_a(world, structureboundingbox, 1, 1, 0, 6, 1, 0, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                }

                if (!this.field_175830_k.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 1, 3, 7, 6, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 1, 2, 7, 6, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                    this.func_175804_a(world, structureboundingbox, 1, 1, 7, 6, 1, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                }

                if (!this.field_175830_k.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 0, 3, 1, 0, 3, 6, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 0, 2, 1, 0, 2, 6, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                    this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 1, 6, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                }

                if (!this.field_175830_k.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                    this.func_175804_a(world, structureboundingbox, 7, 3, 1, 7, 3, 6, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 7, 2, 1, 7, 2, 6, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                    this.func_175804_a(world, structureboundingbox, 7, 1, 1, 7, 1, 6, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                }
            } else if (this.field_175833_o == 2) {
                this.func_175804_a(world, structureboundingbox, 0, 1, 0, 0, 1, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 7, 1, 0, 7, 1, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 1, 1, 0, 6, 1, 0, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 1, 1, 7, 6, 1, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 7, 2, 0, 7, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 1, 2, 0, 6, 2, 0, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 1, 2, 7, 6, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 0, 3, 0, 0, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 7, 3, 0, 7, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 1, 3, 0, 6, 3, 0, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 1, 3, 7, 6, 3, 7, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 0, 1, 3, 0, 2, 4, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 7, 1, 3, 7, 2, 4, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 3, 1, 0, 4, 2, 0, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, false);
                this.func_175804_a(world, structureboundingbox, 3, 1, 7, 4, 2, 7, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, StructureOceanMonumentPieces.SimpleRoom.field_175827_c, false);
                if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.func_176745_a()]) {
                    this.func_181655_a(world, structureboundingbox, 3, 1, 0, 4, 2, 0, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                    this.func_181655_a(world, structureboundingbox, 3, 1, 7, 4, 2, 7, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                    this.func_181655_a(world, structureboundingbox, 0, 1, 3, 0, 2, 4, false);
                }

                if (this.field_175830_k.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                    this.func_181655_a(world, structureboundingbox, 7, 1, 3, 7, 2, 4, false);
                }
            }

            if (flag) {
                this.func_175804_a(world, structureboundingbox, 3, 1, 3, 4, 1, 4, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 3, 2, 3, 4, 2, 4, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, StructureOceanMonumentPieces.SimpleRoom.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 3, 3, 3, 4, 3, 4, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, StructureOceanMonumentPieces.SimpleRoom.field_175826_b, false);
            }

            return true;
        }
    }

    public static class EntryRoom extends StructureOceanMonumentPieces.Piece {

        public EntryRoom() {}

        public EntryRoom(EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker) {
            super(1, enumdirection, worldgenmonumentpieces_worldgenmonumentstatetracker, 1, 1, 1);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 3, 0, 2, 3, 7, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 3, 0, 7, 3, 7, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 1, 2, 7, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 6, 2, 0, 7, 2, 7, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 0, 1, 7, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 7, 1, 0, 7, 1, 7, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 7, 7, 3, 7, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 0, 2, 3, 0, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            this.func_175804_a(world, structureboundingbox, 5, 1, 0, 6, 3, 0, StructureOceanMonumentPieces.EntryRoom.field_175826_b, StructureOceanMonumentPieces.EntryRoom.field_175826_b, false);
            if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 3, 1, 7, 4, 2, 7, false);
            }

            if (this.field_175830_k.field_175966_c[EnumFacing.WEST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 0, 1, 3, 1, 2, 4, false);
            }

            if (this.field_175830_k.field_175966_c[EnumFacing.EAST.func_176745_a()]) {
                this.func_181655_a(world, structureboundingbox, 6, 1, 3, 7, 2, 4, false);
            }

            return true;
        }
    }

    public static class MonumentBuilding extends StructureOceanMonumentPieces.Piece {

        private StructureOceanMonumentPieces.RoomDefinition field_175845_o;
        private StructureOceanMonumentPieces.RoomDefinition field_175844_p;
        private final List<StructureOceanMonumentPieces.Piece> field_175843_q = Lists.newArrayList();

        public MonumentBuilding() {}

        public MonumentBuilding(Random random, int i, int j, EnumFacing enumdirection) {
            super(0);
            this.func_186164_a(enumdirection);
            EnumFacing enumdirection1 = this.func_186165_e();

            if (enumdirection1.func_176740_k() == EnumFacing.Axis.Z) {
                this.field_74887_e = new StructureBoundingBox(i, 39, j, i + 58 - 1, 61, j + 58 - 1);
            } else {
                this.field_74887_e = new StructureBoundingBox(i, 39, j, i + 58 - 1, 61, j + 58 - 1);
            }

            List list = this.func_175836_a(random);

            this.field_175845_o.field_175963_d = true;
            this.field_175843_q.add(new StructureOceanMonumentPieces.EntryRoom(enumdirection1, this.field_175845_o));
            this.field_175843_q.add(new StructureOceanMonumentPieces.MonumentCoreRoom(enumdirection1, this.field_175844_p, random));
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

                if (!worldgenmonumentpieces_worldgenmonumentstatetracker.field_175963_d && !worldgenmonumentpieces_worldgenmonumentstatetracker.func_175961_b()) {
                    Iterator iterator1 = arraylist.iterator();

                    while (iterator1.hasNext()) {
                        StructureOceanMonumentPieces.MonumentRoomFitHelper worldgenmonumentpieces_iworldgenmonumentpieceselector = (StructureOceanMonumentPieces.MonumentRoomFitHelper) iterator1.next();

                        if (worldgenmonumentpieces_iworldgenmonumentpieceselector.func_175969_a(worldgenmonumentpieces_worldgenmonumentstatetracker)) {
                            this.field_175843_q.add(worldgenmonumentpieces_iworldgenmonumentpieceselector.func_175968_a(enumdirection1, worldgenmonumentpieces_worldgenmonumentstatetracker, random));
                            break;
                        }
                    }
                }
            }

            int k = this.field_74887_e.field_78895_b;
            int l = this.func_74865_a(9, 22);
            int i1 = this.func_74873_b(9, 22);
            Iterator iterator2 = this.field_175843_q.iterator();

            while (iterator2.hasNext()) {
                StructureOceanMonumentPieces.Piece worldgenmonumentpieces_worldgenmonumentpiece = (StructureOceanMonumentPieces.Piece) iterator2.next();

                worldgenmonumentpieces_worldgenmonumentpiece.func_74874_b().func_78886_a(l, k, i1);
            }

            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175899_a(this.func_74865_a(1, 1), this.func_74862_a(1), this.func_74873_b(1, 1), this.func_74865_a(23, 21), this.func_74862_a(8), this.func_74873_b(23, 21));
            StructureBoundingBox structureboundingbox1 = StructureBoundingBox.func_175899_a(this.func_74865_a(34, 1), this.func_74862_a(1), this.func_74873_b(34, 1), this.func_74865_a(56, 21), this.func_74862_a(8), this.func_74873_b(56, 21));
            StructureBoundingBox structureboundingbox2 = StructureBoundingBox.func_175899_a(this.func_74865_a(22, 22), this.func_74862_a(13), this.func_74873_b(22, 22), this.func_74865_a(35, 35), this.func_74862_a(17), this.func_74873_b(35, 35));
            int j1 = random.nextInt();

            this.field_175843_q.add(new StructureOceanMonumentPieces.WingRoom(enumdirection1, structureboundingbox, j1++));
            this.field_175843_q.add(new StructureOceanMonumentPieces.WingRoom(enumdirection1, structureboundingbox1, j1++));
            this.field_175843_q.add(new StructureOceanMonumentPieces.Penthouse(enumdirection1, structureboundingbox2));
        }

        private List<StructureOceanMonumentPieces.RoomDefinition> func_175836_a(Random random) {
            StructureOceanMonumentPieces.RoomDefinition[] aworldgenmonumentpieces_worldgenmonumentstatetracker = new StructureOceanMonumentPieces.RoomDefinition[75];

            int i;
            int j;
            boolean flag;
            int k;

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 4; ++j) {
                    flag = false;
                    k = func_175820_a(i, 0, j);
                    aworldgenmonumentpieces_worldgenmonumentstatetracker[k] = new StructureOceanMonumentPieces.RoomDefinition(k);
                }
            }

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 4; ++j) {
                    flag = true;
                    k = func_175820_a(i, 1, j);
                    aworldgenmonumentpieces_worldgenmonumentstatetracker[k] = new StructureOceanMonumentPieces.RoomDefinition(k);
                }
            }

            for (i = 1; i < 4; ++i) {
                for (j = 0; j < 2; ++j) {
                    flag = true;
                    k = func_175820_a(i, 2, j);
                    aworldgenmonumentpieces_worldgenmonumentstatetracker[k] = new StructureOceanMonumentPieces.RoomDefinition(k);
                }
            }

            this.field_175845_o = aworldgenmonumentpieces_worldgenmonumentstatetracker[StructureOceanMonumentPieces.MonumentBuilding.field_175823_g];

            int l;
            int i1;
            int j1;
            int k1;
            int l1;

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 5; ++j) {
                    for (int i2 = 0; i2 < 3; ++i2) {
                        k = func_175820_a(i, i2, j);
                        if (aworldgenmonumentpieces_worldgenmonumentstatetracker[k] != null) {
                            EnumFacing[] aenumdirection = EnumFacing.values();

                            l = aenumdirection.length;

                            for (i1 = 0; i1 < l; ++i1) {
                                EnumFacing enumdirection = aenumdirection[i1];

                                j1 = i + enumdirection.func_82601_c();
                                k1 = i2 + enumdirection.func_96559_d();
                                l1 = j + enumdirection.func_82599_e();
                                if (j1 >= 0 && j1 < 5 && l1 >= 0 && l1 < 5 && k1 >= 0 && k1 < 3) {
                                    int j2 = func_175820_a(j1, k1, l1);

                                    if (aworldgenmonumentpieces_worldgenmonumentstatetracker[j2] != null) {
                                        if (l1 == j) {
                                            aworldgenmonumentpieces_worldgenmonumentstatetracker[k].func_175957_a(enumdirection, aworldgenmonumentpieces_worldgenmonumentstatetracker[j2]);
                                        } else {
                                            aworldgenmonumentpieces_worldgenmonumentstatetracker[k].func_175957_a(enumdirection.func_176734_d(), aworldgenmonumentpieces_worldgenmonumentstatetracker[j2]);
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

            aworldgenmonumentpieces_worldgenmonumentstatetracker[StructureOceanMonumentPieces.MonumentBuilding.field_175831_h].func_175957_a(EnumFacing.UP, worldgenmonumentpieces_worldgenmonumentstatetracker);
            aworldgenmonumentpieces_worldgenmonumentstatetracker[StructureOceanMonumentPieces.MonumentBuilding.field_175832_i].func_175957_a(EnumFacing.SOUTH, worldgenmonumentpieces_worldgenmonumentstatetracker1);
            aworldgenmonumentpieces_worldgenmonumentstatetracker[StructureOceanMonumentPieces.MonumentBuilding.field_175829_j].func_175957_a(EnumFacing.SOUTH, worldgenmonumentpieces_worldgenmonumentstatetracker2);
            worldgenmonumentpieces_worldgenmonumentstatetracker.field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker1.field_175963_d = true;
            worldgenmonumentpieces_worldgenmonumentstatetracker2.field_175963_d = true;
            this.field_175845_o.field_175964_e = true;
            this.field_175844_p = aworldgenmonumentpieces_worldgenmonumentstatetracker[func_175820_a(random.nextInt(4), 0, 2)];
            this.field_175844_p.field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            this.field_175844_p.field_175965_b[EnumFacing.EAST.func_176745_a()].field_175965_b[EnumFacing.NORTH.func_176745_a()].field_175965_b[EnumFacing.UP.func_176745_a()].field_175963_d = true;
            ArrayList arraylist = Lists.newArrayList();
            StructureOceanMonumentPieces.RoomDefinition[] aworldgenmonumentpieces_worldgenmonumentstatetracker1 = aworldgenmonumentpieces_worldgenmonumentstatetracker;

            l = aworldgenmonumentpieces_worldgenmonumentstatetracker.length;

            for (i1 = 0; i1 < l; ++i1) {
                StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker3 = aworldgenmonumentpieces_worldgenmonumentstatetracker1[i1];

                if (worldgenmonumentpieces_worldgenmonumentstatetracker3 != null) {
                    worldgenmonumentpieces_worldgenmonumentstatetracker3.func_175958_a();
                    arraylist.add(worldgenmonumentpieces_worldgenmonumentstatetracker3);
                }
            }

            worldgenmonumentpieces_worldgenmonumentstatetracker.func_175958_a();
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
                    if (worldgenmonumentpieces_worldgenmonumentstatetracker4.field_175966_c[k1]) {
                        l1 = EnumFacing.func_82600_a(k1).func_176734_d().func_176745_a();
                        worldgenmonumentpieces_worldgenmonumentstatetracker4.field_175966_c[k1] = false;
                        worldgenmonumentpieces_worldgenmonumentstatetracker4.field_175965_b[k1].field_175966_c[l1] = false;
                        if (worldgenmonumentpieces_worldgenmonumentstatetracker4.func_175959_a(k2++) && worldgenmonumentpieces_worldgenmonumentstatetracker4.field_175965_b[k1].func_175959_a(k2++)) {
                            ++l2;
                        } else {
                            worldgenmonumentpieces_worldgenmonumentstatetracker4.field_175966_c[k1] = true;
                            worldgenmonumentpieces_worldgenmonumentstatetracker4.field_175965_b[k1].field_175966_c[l1] = true;
                        }
                    }
                }
            }

            arraylist.add(worldgenmonumentpieces_worldgenmonumentstatetracker);
            arraylist.add(worldgenmonumentpieces_worldgenmonumentstatetracker1);
            arraylist.add(worldgenmonumentpieces_worldgenmonumentstatetracker2);
            return arraylist;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            int i = Math.max(world.func_181545_F(), 64) - this.field_74887_e.field_78895_b;

            this.func_181655_a(world, structureboundingbox, 0, 0, 0, 58, i, 58, false);
            this.func_175840_a(false, 0, world, random, structureboundingbox);
            this.func_175840_a(true, 33, world, random, structureboundingbox);
            this.func_175839_b(world, random, structureboundingbox);
            this.func_175837_c(world, random, structureboundingbox);
            this.func_175841_d(world, random, structureboundingbox);
            this.func_175835_e(world, random, structureboundingbox);
            this.func_175842_f(world, random, structureboundingbox);
            this.func_175838_g(world, random, structureboundingbox);

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
                            this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, l + j1, 0, i1 + k1, structureboundingbox);
                            this.func_175808_b(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, l + j1, -1, i1 + k1, structureboundingbox);
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
                this.func_181655_a(world, structureboundingbox, -1 - j, 0 + j * 2, -1 - j, -1 - j, 23, 58 + j, false);
                this.func_181655_a(world, structureboundingbox, 58 + j, 0 + j * 2, -1 - j, 58 + j, 23, 58 + j, false);
                this.func_181655_a(world, structureboundingbox, 0 - j, 0 + j * 2, -1 - j, 57 + j, 23, -1 - j, false);
                this.func_181655_a(world, structureboundingbox, 0 - j, 0 + j * 2, 58 + j, 57 + j, 23, 58 + j, false);
            }

            Iterator iterator = this.field_175843_q.iterator();

            while (iterator.hasNext()) {
                StructureOceanMonumentPieces.Piece worldgenmonumentpieces_worldgenmonumentpiece = (StructureOceanMonumentPieces.Piece) iterator.next();

                if (worldgenmonumentpieces_worldgenmonumentpiece.func_74874_b().func_78884_a(structureboundingbox)) {
                    worldgenmonumentpieces_worldgenmonumentpiece.func_74875_a(world, random, structureboundingbox);
                }
            }

            return true;
        }

        private void func_175840_a(boolean flag, int i, World world, Random random, StructureBoundingBox structureboundingbox) {
            boolean flag1 = true;

            if (this.func_175818_a(structureboundingbox, i, 0, i + 23, 20)) {
                this.func_175804_a(world, structureboundingbox, i + 0, 0, 0, i + 24, 0, 20, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, i + 0, 1, 0, i + 24, 10, 20, false);

                int j;

                for (j = 0; j < 4; ++j) {
                    this.func_175804_a(world, structureboundingbox, i + j, j + 1, j, i + j, j + 1, 20, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, i + j + 7, j + 5, j + 7, i + j + 7, j + 5, 20, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, i + 17 - j, j + 5, j + 7, i + 17 - j, j + 5, 20, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, i + 24 - j, j + 1, j, i + 24 - j, j + 1, 20, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, i + j + 1, j + 1, j, i + 23 - j, j + 1, j, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, i + j + 8, j + 5, j + 7, i + 16 - j, j + 5, j + 7, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                this.func_175804_a(world, structureboundingbox, i + 4, 4, 4, i + 6, 4, 20, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, i + 7, 4, 4, i + 17, 4, 6, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, i + 18, 4, 4, i + 20, 4, 20, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, i + 11, 8, 11, i + 13, 8, 20, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i + 12, 9, 12, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i + 12, 9, 15, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i + 12, 9, 18, structureboundingbox);
                j = i + (flag ? 19 : 5);
                int k = i + (flag ? 5 : 19);

                int l;

                for (l = 20; l >= 5; l -= 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, j, 5, l, structureboundingbox);
                }

                for (l = 19; l >= 7; l -= 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, k, 5, l, structureboundingbox);
                }

                for (l = 0; l < 4; ++l) {
                    int i1 = flag ? i + (24 - (17 - l * 3)) : i + 17 - l * 3;

                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i1, 5, 5, structureboundingbox);
                }

                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, k, 5, 5, structureboundingbox);
                this.func_175804_a(world, structureboundingbox, i + 11, 1, 12, i + 13, 7, 12, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, i + 12, 1, 11, i + 12, 7, 13, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
            }

        }

        private void func_175839_b(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_175818_a(structureboundingbox, 22, 5, 35, 17)) {
                this.func_181655_a(world, structureboundingbox, 25, 0, 0, 32, 8, 20, false);

                for (int i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, 24, 2, 5 + i * 4, 24, 4, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 22, 4, 5 + i * 4, 23, 4, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 25, 5, 5 + i * 4, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 26, 6, 5 + i * 4, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175825_e, 26, 5, 5 + i * 4, structureboundingbox);
                    this.func_175804_a(world, structureboundingbox, 33, 2, 5 + i * 4, 33, 4, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 34, 4, 5 + i * 4, 35, 4, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 32, 5, 5 + i * 4, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 31, 6, 5 + i * 4, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175825_e, 31, 5, 5 + i * 4, structureboundingbox);
                    this.func_175804_a(world, structureboundingbox, 27, 6, 5 + i * 4, 30, 6, 5 + i * 4, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                }
            }

        }

        private void func_175837_c(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_175818_a(structureboundingbox, 15, 20, 42, 21)) {
                this.func_175804_a(world, structureboundingbox, 15, 0, 21, 42, 0, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 26, 1, 21, 31, 3, 21, false);
                this.func_175804_a(world, structureboundingbox, 21, 12, 21, 36, 12, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 17, 11, 21, 40, 11, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 16, 10, 21, 41, 10, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 15, 7, 21, 42, 9, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 16, 6, 21, 41, 6, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 17, 5, 21, 40, 5, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 21, 4, 21, 36, 4, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 22, 3, 21, 26, 3, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 31, 3, 21, 35, 3, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 23, 2, 21, 25, 2, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 32, 2, 21, 34, 2, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 28, 4, 20, 29, 4, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 27, 3, 21, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 30, 3, 21, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 26, 2, 21, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 31, 2, 21, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 25, 1, 21, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 32, 1, 21, structureboundingbox);

                int i;

                for (i = 0; i < 7; ++i) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 28 - i, 6 + i, 21, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 29 + i, 6 + i, 21, structureboundingbox);
                }

                for (i = 0; i < 4; ++i) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 28 - i, 9 + i, 21, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 29 + i, 9 + i, 21, structureboundingbox);
                }

                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 28, 12, 21, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 29, 12, 21, structureboundingbox);

                for (i = 0; i < 3; ++i) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 22 - i * 2, 8, 21, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 22 - i * 2, 9, 21, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 35 + i * 2, 8, 21, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175827_c, 35 + i * 2, 9, 21, structureboundingbox);
                }

                this.func_181655_a(world, structureboundingbox, 15, 13, 21, 42, 15, 21, false);
                this.func_181655_a(world, structureboundingbox, 15, 1, 21, 15, 6, 21, false);
                this.func_181655_a(world, structureboundingbox, 16, 1, 21, 16, 5, 21, false);
                this.func_181655_a(world, structureboundingbox, 17, 1, 21, 20, 4, 21, false);
                this.func_181655_a(world, structureboundingbox, 21, 1, 21, 21, 3, 21, false);
                this.func_181655_a(world, structureboundingbox, 22, 1, 21, 22, 2, 21, false);
                this.func_181655_a(world, structureboundingbox, 23, 1, 21, 24, 1, 21, false);
                this.func_181655_a(world, structureboundingbox, 42, 1, 21, 42, 6, 21, false);
                this.func_181655_a(world, structureboundingbox, 41, 1, 21, 41, 5, 21, false);
                this.func_181655_a(world, structureboundingbox, 37, 1, 21, 40, 4, 21, false);
                this.func_181655_a(world, structureboundingbox, 36, 1, 21, 36, 3, 21, false);
                this.func_181655_a(world, structureboundingbox, 33, 1, 21, 34, 1, 21, false);
                this.func_181655_a(world, structureboundingbox, 35, 1, 21, 35, 2, 21, false);
            }

        }

        private void func_175841_d(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_175818_a(structureboundingbox, 21, 21, 36, 36)) {
                this.func_175804_a(world, structureboundingbox, 21, 0, 22, 36, 0, 36, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 21, 1, 22, 36, 23, 36, false);

                for (int i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, 21 + i, 13 + i, 21 + i, 36 - i, 13 + i, 21 + i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 21 + i, 13 + i, 36 - i, 36 - i, 13 + i, 36 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 21 + i, 13 + i, 22 + i, 21 + i, 13 + i, 35 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                    this.func_175804_a(world, structureboundingbox, 36 - i, 13 + i, 22 + i, 36 - i, 13 + i, 35 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                this.func_175804_a(world, structureboundingbox, 25, 16, 25, 32, 16, 32, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 25, 17, 25, 25, 19, 25, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 32, 17, 25, 32, 19, 25, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 25, 17, 32, 25, 19, 32, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 32, 17, 32, 32, 19, 32, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 26, 20, 26, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 27, 21, 27, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175825_e, 27, 20, 27, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 26, 20, 31, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 27, 21, 30, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175825_e, 27, 20, 30, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 31, 20, 31, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 30, 21, 30, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175825_e, 30, 20, 30, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 31, 20, 26, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, 30, 21, 27, structureboundingbox);
                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175825_e, 30, 20, 27, structureboundingbox);
                this.func_175804_a(world, structureboundingbox, 28, 21, 27, 29, 21, 27, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 27, 21, 28, 27, 21, 29, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 28, 21, 30, 29, 21, 30, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 30, 21, 28, 30, 21, 29, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
            }

        }

        private void func_175835_e(World world, Random random, StructureBoundingBox structureboundingbox) {
            int i;

            if (this.func_175818_a(structureboundingbox, 0, 21, 6, 58)) {
                this.func_175804_a(world, structureboundingbox, 0, 0, 21, 6, 0, 57, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 0, 1, 21, 6, 7, 57, false);
                this.func_175804_a(world, structureboundingbox, 4, 4, 21, 6, 4, 53, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, i, i + 1, 21, i, i + 1, 57 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                for (i = 23; i < 53; i += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, 5, 5, i, structureboundingbox);
                }

                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, 5, 5, 52, structureboundingbox);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, i, i + 1, 21, i, i + 1, 57 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                this.func_175804_a(world, structureboundingbox, 4, 1, 52, 6, 3, 52, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 5, 1, 51, 5, 3, 53, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
            }

            if (this.func_175818_a(structureboundingbox, 51, 21, 58, 58)) {
                this.func_175804_a(world, structureboundingbox, 51, 0, 21, 57, 0, 57, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 51, 1, 21, 57, 7, 57, false);
                this.func_175804_a(world, structureboundingbox, 51, 4, 21, 53, 4, 53, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, 57 - i, i + 1, 21, 57 - i, i + 1, 57 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                for (i = 23; i < 53; i += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, 52, 5, i, structureboundingbox);
                }

                this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, 52, 5, 52, structureboundingbox);
                this.func_175804_a(world, structureboundingbox, 51, 1, 52, 53, 3, 52, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 52, 1, 51, 52, 3, 53, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
            }

            if (this.func_175818_a(structureboundingbox, 0, 51, 57, 57)) {
                this.func_175804_a(world, structureboundingbox, 7, 0, 51, 50, 0, 57, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 7, 1, 51, 50, 10, 57, false);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, i + 1, i + 1, 57 - i, 56 - i, i + 1, 57 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }
            }

        }

        private void func_175842_f(World world, Random random, StructureBoundingBox structureboundingbox) {
            int i;

            if (this.func_175818_a(structureboundingbox, 7, 21, 13, 50)) {
                this.func_175804_a(world, structureboundingbox, 7, 0, 21, 13, 0, 50, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 7, 1, 21, 13, 10, 50, false);
                this.func_175804_a(world, structureboundingbox, 11, 8, 21, 13, 8, 53, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, i + 7, i + 5, 21, i + 7, i + 5, 54, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                for (i = 21; i <= 45; i += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, 12, 9, i, structureboundingbox);
                }
            }

            if (this.func_175818_a(structureboundingbox, 44, 21, 50, 54)) {
                this.func_175804_a(world, structureboundingbox, 44, 0, 21, 50, 0, 50, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 44, 1, 21, 50, 10, 50, false);
                this.func_175804_a(world, structureboundingbox, 44, 8, 21, 46, 8, 53, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, 50 - i, i + 5, 21, 50 - i, i + 5, 54, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                for (i = 21; i <= 45; i += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, 45, 9, i, structureboundingbox);
                }
            }

            if (this.func_175818_a(structureboundingbox, 8, 44, 49, 54)) {
                this.func_175804_a(world, structureboundingbox, 14, 0, 44, 43, 0, 50, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 14, 1, 44, 43, 10, 50, false);

                for (i = 12; i <= 45; i += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 9, 45, structureboundingbox);
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 9, 52, structureboundingbox);
                    if (i == 12 || i == 18 || i == 24 || i == 33 || i == 39 || i == 45) {
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 9, 47, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 9, 50, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 10, 45, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 10, 46, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 10, 51, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 10, 52, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 11, 47, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 11, 50, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 12, 48, structureboundingbox);
                        this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 12, 49, structureboundingbox);
                    }
                }

                for (i = 0; i < 3; ++i) {
                    this.func_175804_a(world, structureboundingbox, 8 + i, 5 + i, 54, 49 - i, 5 + i, 54, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                }

                this.func_175804_a(world, structureboundingbox, 11, 8, 54, 46, 8, 54, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, 14, 8, 44, 43, 8, 53, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
            }

        }

        private void func_175838_g(World world, Random random, StructureBoundingBox structureboundingbox) {
            int i;

            if (this.func_175818_a(structureboundingbox, 14, 21, 20, 43)) {
                this.func_175804_a(world, structureboundingbox, 14, 0, 21, 20, 0, 43, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 14, 1, 22, 20, 14, 43, false);
                this.func_175804_a(world, structureboundingbox, 18, 12, 22, 20, 12, 39, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 18, 12, 21, 20, 12, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, i + 14, i + 9, 21, i + 14, i + 9, 43 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                for (i = 23; i <= 39; i += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, 19, 13, i, structureboundingbox);
                }
            }

            if (this.func_175818_a(structureboundingbox, 37, 21, 43, 43)) {
                this.func_175804_a(world, structureboundingbox, 37, 0, 21, 43, 0, 43, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 37, 1, 22, 43, 14, 43, false);
                this.func_175804_a(world, structureboundingbox, 37, 12, 22, 39, 12, 39, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, 37, 12, 21, 39, 12, 21, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, 43 - i, i + 9, 21, 43 - i, i + 9, 43 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                for (i = 23; i <= 39; i += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, 38, 13, i, structureboundingbox);
                }
            }

            if (this.func_175818_a(structureboundingbox, 15, 37, 42, 43)) {
                this.func_175804_a(world, structureboundingbox, 21, 0, 37, 36, 0, 43, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);
                this.func_181655_a(world, structureboundingbox, 21, 1, 37, 36, 14, 43, false);
                this.func_175804_a(world, structureboundingbox, 21, 12, 37, 36, 12, 39, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, StructureOceanMonumentPieces.MonumentBuilding.field_175828_a, false);

                for (i = 0; i < 4; ++i) {
                    this.func_175804_a(world, structureboundingbox, 15 + i, i + 9, 43 - i, 42 - i, i + 9, 43 - i, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, StructureOceanMonumentPieces.MonumentBuilding.field_175826_b, false);
                }

                for (i = 21; i <= 36; i += 3) {
                    this.func_175811_a(world, StructureOceanMonumentPieces.MonumentBuilding.field_175824_d, i, 13, 38, structureboundingbox);
                }
            }

        }
    }

    public abstract static class Piece extends StructureComponent {

        protected static final IBlockState field_175828_a = Blocks.field_180397_cI.func_176203_a(BlockPrismarine.field_176331_b);
        protected static final IBlockState field_175826_b = Blocks.field_180397_cI.func_176203_a(BlockPrismarine.field_176333_M);
        protected static final IBlockState field_175827_c = Blocks.field_180397_cI.func_176203_a(BlockPrismarine.field_176334_N);
        protected static final IBlockState field_175824_d = StructureOceanMonumentPieces.Piece.field_175826_b;
        protected static final IBlockState field_175825_e = Blocks.field_180398_cJ.func_176223_P();
        protected static final IBlockState field_175822_f = Blocks.field_150355_j.func_176223_P();
        protected static final int field_175823_g = func_175820_a(2, 0, 0);
        protected static final int field_175831_h = func_175820_a(2, 2, 0);
        protected static final int field_175832_i = func_175820_a(0, 1, 0);
        protected static final int field_175829_j = func_175820_a(4, 1, 0);
        protected StructureOceanMonumentPieces.RoomDefinition field_175830_k;

        protected static final int func_175820_a(int i, int j, int k) {
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
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        protected Piece(int i, EnumFacing enumdirection, StructureOceanMonumentPieces.RoomDefinition worldgenmonumentpieces_worldgenmonumentstatetracker, int j, int k, int l) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_175830_k = worldgenmonumentpieces_worldgenmonumentstatetracker;
            int i1 = worldgenmonumentpieces_worldgenmonumentstatetracker.field_175967_a;
            int j1 = i1 % 5;
            int k1 = i1 / 5 % 5;
            int l1 = i1 / 25;

            if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.SOUTH) {
                this.field_74887_e = new StructureBoundingBox(0, 0, 0, l * 8 - 1, k * 4 - 1, j * 8 - 1);
            } else {
                this.field_74887_e = new StructureBoundingBox(0, 0, 0, j * 8 - 1, k * 4 - 1, l * 8 - 1);
            }

            switch (enumdirection) {
            case NORTH:
                this.field_74887_e.func_78886_a(j1 * 8, l1 * 4, -(k1 + l) * 8 + 1);
                break;

            case SOUTH:
                this.field_74887_e.func_78886_a(j1 * 8, l1 * 4, k1 * 8);
                break;

            case WEST:
                this.field_74887_e.func_78886_a(-(k1 + l) * 8 + 1, l1 * 4, j1 * 8);
                break;

            default:
                this.field_74887_e.func_78886_a(k1 * 8, l1 * 4, j1 * 8);
            }

        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {}

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {}

        protected void func_181655_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, boolean flag) {
            for (int k1 = j; k1 <= i1; ++k1) {
                for (int l1 = i; l1 <= l; ++l1) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        if (!flag || this.func_175807_a(world, l1, k1, i2, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                            if (this.func_74862_a(k1) >= world.func_181545_F()) {
                                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), l1, k1, i2, structureboundingbox);
                            } else {
                                this.func_175811_a(world, StructureOceanMonumentPieces.Piece.field_175822_f, l1, k1, i2, structureboundingbox);
                            }
                        }
                    }
                }
            }

        }

        protected void func_175821_a(World world, StructureBoundingBox structureboundingbox, int i, int j, boolean flag) {
            if (flag) {
                this.func_175804_a(world, structureboundingbox, i + 0, 0, j + 0, i + 2, 0, j + 8 - 1, StructureOceanMonumentPieces.Piece.field_175828_a, StructureOceanMonumentPieces.Piece.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, i + 5, 0, j + 0, i + 8 - 1, 0, j + 8 - 1, StructureOceanMonumentPieces.Piece.field_175828_a, StructureOceanMonumentPieces.Piece.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, i + 3, 0, j + 0, i + 4, 0, j + 2, StructureOceanMonumentPieces.Piece.field_175828_a, StructureOceanMonumentPieces.Piece.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, i + 3, 0, j + 5, i + 4, 0, j + 8 - 1, StructureOceanMonumentPieces.Piece.field_175828_a, StructureOceanMonumentPieces.Piece.field_175828_a, false);
                this.func_175804_a(world, structureboundingbox, i + 3, 0, j + 2, i + 4, 0, j + 2, StructureOceanMonumentPieces.Piece.field_175826_b, StructureOceanMonumentPieces.Piece.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, i + 3, 0, j + 5, i + 4, 0, j + 5, StructureOceanMonumentPieces.Piece.field_175826_b, StructureOceanMonumentPieces.Piece.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, i + 2, 0, j + 3, i + 2, 0, j + 4, StructureOceanMonumentPieces.Piece.field_175826_b, StructureOceanMonumentPieces.Piece.field_175826_b, false);
                this.func_175804_a(world, structureboundingbox, i + 5, 0, j + 3, i + 5, 0, j + 4, StructureOceanMonumentPieces.Piece.field_175826_b, StructureOceanMonumentPieces.Piece.field_175826_b, false);
            } else {
                this.func_175804_a(world, structureboundingbox, i + 0, 0, j + 0, i + 8 - 1, 0, j + 8 - 1, StructureOceanMonumentPieces.Piece.field_175828_a, StructureOceanMonumentPieces.Piece.field_175828_a, false);
            }

        }

        protected void func_175819_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, IBlockState iblockdata) {
            for (int k1 = j; k1 <= i1; ++k1) {
                for (int l1 = i; l1 <= l; ++l1) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        if (this.func_175807_a(world, l1, k1, i2, structureboundingbox) == StructureOceanMonumentPieces.Piece.field_175822_f) {
                            this.func_175811_a(world, iblockdata, l1, k1, i2, structureboundingbox);
                        }
                    }
                }
            }

        }

        protected boolean func_175818_a(StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
            int i1 = this.func_74865_a(i, j);
            int j1 = this.func_74873_b(i, j);
            int k1 = this.func_74865_a(k, l);
            int l1 = this.func_74873_b(k, l);

            return structureboundingbox.func_78885_a(Math.min(i1, k1), Math.min(j1, l1), Math.max(i1, k1), Math.max(j1, l1));
        }

        protected boolean func_175817_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k) {
            int l = this.func_74865_a(i, k);
            int i1 = this.func_74862_a(j);
            int j1 = this.func_74873_b(i, k);

            if (structureboundingbox.func_175898_b((Vec3i) (new BlockPos(l, i1, j1)))) {
                EntityElderGuardian entityguardianelder = new EntityElderGuardian(world);

                entityguardianelder.func_70691_i(entityguardianelder.func_110138_aP());
                entityguardianelder.func_70012_b((double) l + 0.5D, (double) i1, (double) j1 + 0.5D, 0.0F, 0.0F);
                entityguardianelder.func_180482_a(world.func_175649_E(new BlockPos(entityguardianelder)), (IEntityLivingData) null);
                world.func_72838_d(entityguardianelder);
                return true;
            } else {
                return false;
            }
        }
    }
}
