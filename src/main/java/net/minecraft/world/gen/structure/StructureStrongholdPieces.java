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

    private static final StructureStrongholdPieces.PieceWeight[] field_75205_b = new StructureStrongholdPieces.PieceWeight[] { new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Straight.class, 40, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Prison.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.LeftTurn.class, 20, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RightTurn.class, 20, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RoomCrossing.class, 10, 6), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.StairsStraight.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Stairs.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Crossing.class, 5, 4), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.ChestCorridor.class, 5, 4), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Library.class, 10, 2) {
        public boolean func_75189_a(int i) {
            return super.func_75189_a(i) && i > 4;
        }
    }, new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.PortalRoom.class, 20, 1) {
        public boolean func_75189_a(int i) {
            return super.func_75189_a(i) && i > 5;
        }
    }};
    private static List<StructureStrongholdPieces.PieceWeight> field_75206_c;
    private static Class<? extends StructureStrongholdPieces.Stronghold> field_75203_d;
    static int field_75207_a;
    private static final StructureStrongholdPieces.Stones field_75204_e = new StructureStrongholdPieces.Stones(null);

    public static void func_143046_a() {
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.ChestCorridor.class, "SHCC");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Corridor.class, "SHFC");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Crossing.class, "SH5C");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.LeftTurn.class, "SHLT");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Library.class, "SHLi");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.PortalRoom.class, "SHPR");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Prison.class, "SHPH");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.RightTurn.class, "SHRT");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.RoomCrossing.class, "SHRC");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Stairs.class, "SHSD");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Stairs2.class, "SHStart");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.Straight.class, "SHS");
        MapGenStructureIO.func_143031_a(StructureStrongholdPieces.StairsStraight.class, "SHSSD");
    }

    public static void func_75198_a() {
        StructureStrongholdPieces.field_75206_c = Lists.newArrayList();
        StructureStrongholdPieces.PieceWeight[] aworldgenstrongholdpieces_worldgenstrongholdpieceweight = StructureStrongholdPieces.field_75205_b;
        int i = aworldgenstrongholdpieces_worldgenstrongholdpieceweight.length;

        for (int j = 0; j < i; ++j) {
            StructureStrongholdPieces.PieceWeight worldgenstrongholdpieces_worldgenstrongholdpieceweight = aworldgenstrongholdpieces_worldgenstrongholdpieceweight[j];

            worldgenstrongholdpieces_worldgenstrongholdpieceweight.field_75193_c = 0;
            StructureStrongholdPieces.field_75206_c.add(worldgenstrongholdpieces_worldgenstrongholdpieceweight);
        }

        StructureStrongholdPieces.field_75203_d = null;
    }

    private static boolean func_75202_c() {
        boolean flag = false;

        StructureStrongholdPieces.field_75207_a = 0;

        StructureStrongholdPieces.PieceWeight worldgenstrongholdpieces_worldgenstrongholdpieceweight;

        for (Iterator iterator = StructureStrongholdPieces.field_75206_c.iterator(); iterator.hasNext(); StructureStrongholdPieces.field_75207_a += worldgenstrongholdpieces_worldgenstrongholdpieceweight.field_75192_b) {
            worldgenstrongholdpieces_worldgenstrongholdpieceweight = (StructureStrongholdPieces.PieceWeight) iterator.next();
            if (worldgenstrongholdpieces_worldgenstrongholdpieceweight.field_75191_d > 0 && worldgenstrongholdpieces_worldgenstrongholdpieceweight.field_75193_c < worldgenstrongholdpieces_worldgenstrongholdpieceweight.field_75191_d) {
                flag = true;
            }
        }

        return flag;
    }

    private static StructureStrongholdPieces.Stronghold func_175954_a(Class<? extends StructureStrongholdPieces.Stronghold> oclass, List<StructureComponent> list, Random random, int i, int j, int k, @Nullable EnumFacing enumdirection, int l) {
        Object object = null;

        if (oclass == StructureStrongholdPieces.Straight.class) {
            object = StructureStrongholdPieces.Straight.func_175862_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.Prison.class) {
            object = StructureStrongholdPieces.Prison.func_175860_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.LeftTurn.class) {
            object = StructureStrongholdPieces.LeftTurn.func_175867_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.RightTurn.class) {
            object = StructureStrongholdPieces.RightTurn.func_175867_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.RoomCrossing.class) {
            object = StructureStrongholdPieces.RoomCrossing.func_175859_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.StairsStraight.class) {
            object = StructureStrongholdPieces.StairsStraight.func_175861_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.Stairs.class) {
            object = StructureStrongholdPieces.Stairs.func_175863_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.Crossing.class) {
            object = StructureStrongholdPieces.Crossing.func_175866_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.ChestCorridor.class) {
            object = StructureStrongholdPieces.ChestCorridor.func_175868_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.Library.class) {
            object = StructureStrongholdPieces.Library.func_175864_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureStrongholdPieces.PortalRoom.class) {
            object = StructureStrongholdPieces.PortalRoom.func_175865_a(list, random, i, j, k, enumdirection, l);
        }

        return (StructureStrongholdPieces.Stronghold) object;
    }

    private static StructureStrongholdPieces.Stronghold func_175955_b(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        if (!func_75202_c()) {
            return null;
        } else {
            if (StructureStrongholdPieces.field_75203_d != null) {
                StructureStrongholdPieces.Stronghold worldgenstrongholdpieces_worldgenstrongholdpiece = func_175954_a(StructureStrongholdPieces.field_75203_d, list, random, i, j, k, enumdirection, l);

                StructureStrongholdPieces.field_75203_d = null;
                if (worldgenstrongholdpieces_worldgenstrongholdpiece != null) {
                    return worldgenstrongholdpieces_worldgenstrongholdpiece;
                }
            }

            int i1 = 0;

            while (i1 < 5) {
                ++i1;
                int j1 = random.nextInt(StructureStrongholdPieces.field_75207_a);
                Iterator iterator = StructureStrongholdPieces.field_75206_c.iterator();

                while (iterator.hasNext()) {
                    StructureStrongholdPieces.PieceWeight worldgenstrongholdpieces_worldgenstrongholdpieceweight = (StructureStrongholdPieces.PieceWeight) iterator.next();

                    j1 -= worldgenstrongholdpieces_worldgenstrongholdpieceweight.field_75192_b;
                    if (j1 < 0) {
                        if (!worldgenstrongholdpieces_worldgenstrongholdpieceweight.func_75189_a(l) || worldgenstrongholdpieces_worldgenstrongholdpieceweight == worldgenstrongholdpieces_worldgenstrongholdstart.field_75027_a) {
                            break;
                        }

                        StructureStrongholdPieces.Stronghold worldgenstrongholdpieces_worldgenstrongholdpiece1 = func_175954_a(worldgenstrongholdpieces_worldgenstrongholdpieceweight.field_75194_a, list, random, i, j, k, enumdirection, l);

                        if (worldgenstrongholdpieces_worldgenstrongholdpiece1 != null) {
                            ++worldgenstrongholdpieces_worldgenstrongholdpieceweight.field_75193_c;
                            worldgenstrongholdpieces_worldgenstrongholdstart.field_75027_a = worldgenstrongholdpieces_worldgenstrongholdpieceweight;
                            if (!worldgenstrongholdpieces_worldgenstrongholdpieceweight.func_75190_a()) {
                                StructureStrongholdPieces.field_75206_c.remove(worldgenstrongholdpieces_worldgenstrongholdpieceweight);
                            }

                            return worldgenstrongholdpieces_worldgenstrongholdpiece1;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = StructureStrongholdPieces.Corridor.func_175869_a(list, random, i, j, k, enumdirection);

            if (structureboundingbox != null && structureboundingbox.field_78895_b > 1) {
                return new StructureStrongholdPieces.Corridor(l, random, structureboundingbox, enumdirection);
            } else {
                return null;
            }
        }
    }

    private static StructureComponent func_175953_c(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j, int k, @Nullable EnumFacing enumdirection, int l) {
        if (l > 50) {
            return null;
        } else if (Math.abs(i - worldgenstrongholdpieces_worldgenstrongholdstart.func_74874_b().field_78897_a) <= 112 && Math.abs(k - worldgenstrongholdpieces_worldgenstrongholdstart.func_74874_b().field_78896_c) <= 112) {
            StructureStrongholdPieces.Stronghold worldgenstrongholdpieces_worldgenstrongholdpiece = func_175955_b(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, i, j, k, enumdirection, l + 1);

            if (worldgenstrongholdpieces_worldgenstrongholdpiece != null) {
                list.add(worldgenstrongholdpieces_worldgenstrongholdpiece);
                worldgenstrongholdpieces_worldgenstrongholdstart.field_75026_c.add(worldgenstrongholdpieces_worldgenstrongholdpiece);
            }

            return worldgenstrongholdpieces_worldgenstrongholdpiece;
        } else {
            return null;
        }
    }

    static class Stones extends StructureComponent.BlockSelector {

        private Stones() {}

        public void func_75062_a(Random random, int i, int j, int k, boolean flag) {
            if (flag) {
                float f = random.nextFloat();

                if (f < 0.2F) {
                    this.field_151562_a = Blocks.field_150417_aV.func_176203_a(BlockStoneBrick.field_176251_N);
                } else if (f < 0.5F) {
                    this.field_151562_a = Blocks.field_150417_aV.func_176203_a(BlockStoneBrick.field_176250_M);
                } else if (f < 0.55F) {
                    this.field_151562_a = Blocks.field_150418_aU.func_176203_a(BlockSilverfish.EnumType.STONEBRICK.func_176881_a());
                } else {
                    this.field_151562_a = Blocks.field_150417_aV.func_176223_P();
                }
            } else {
                this.field_151562_a = Blocks.field_150350_a.func_176223_P();
            }

        }

        Stones(Object object) {
            this();
        }
    }

    public static class PortalRoom extends StructureStrongholdPieces.Stronghold {

        private boolean field_75005_a;

        public PortalRoom() {}

        public PortalRoom(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Mob", this.field_75005_a);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_75005_a = nbttagcompound.func_74767_n("Mob");
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            if (structurepiece != null) {
                ((StructureStrongholdPieces.Stairs2) structurepiece).field_75025_b = this;
            }

        }

        public static StructureStrongholdPieces.PortalRoom func_175865_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -4, -1, 0, 11, 8, 16, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.PortalRoom(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_74882_a(world, structureboundingbox, 0, 0, 0, 10, 7, 15, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_74990_a(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
            byte b0 = 6;

            this.func_74882_a(world, structureboundingbox, 1, b0, 1, 1, b0, 14, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_74882_a(world, structureboundingbox, 9, b0, 1, 9, b0, 14, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_74882_a(world, structureboundingbox, 2, b0, 1, 8, b0, 2, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_74882_a(world, structureboundingbox, 2, b0, 14, 8, b0, 14, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_74882_a(world, structureboundingbox, 1, 1, 1, 2, 1, 4, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_74882_a(world, structureboundingbox, 8, 1, 1, 9, 1, 4, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_175804_a(world, structureboundingbox, 1, 1, 1, 1, 1, 3, Blocks.field_150356_k.func_176223_P(), Blocks.field_150356_k.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, 1, 1, 9, 1, 3, Blocks.field_150356_k.func_176223_P(), Blocks.field_150356_k.func_176223_P(), false);
            this.func_74882_a(world, structureboundingbox, 3, 1, 8, 7, 1, 12, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_175804_a(world, structureboundingbox, 4, 1, 9, 6, 1, 11, Blocks.field_150356_k.func_176223_P(), Blocks.field_150356_k.func_176223_P(), false);

            int i;

            for (i = 3; i < 14; i += 2) {
                this.func_175804_a(world, structureboundingbox, 0, 3, i, 0, 4, i, Blocks.field_150411_aY.func_176223_P(), Blocks.field_150411_aY.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 10, 3, i, 10, 4, i, Blocks.field_150411_aY.func_176223_P(), Blocks.field_150411_aY.func_176223_P(), false);
            }

            for (i = 2; i < 9; i += 2) {
                this.func_175804_a(world, structureboundingbox, i, 3, 15, i, 4, 15, Blocks.field_150411_aY.func_176223_P(), Blocks.field_150411_aY.func_176223_P(), false);
            }

            IBlockState iblockdata = Blocks.field_150390_bg.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH);

            this.func_74882_a(world, structureboundingbox, 4, 1, 5, 6, 1, 7, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_74882_a(world, structureboundingbox, 4, 2, 6, 6, 2, 7, false, random, StructureStrongholdPieces.field_75204_e);
            this.func_74882_a(world, structureboundingbox, 4, 3, 7, 6, 3, 7, false, random, StructureStrongholdPieces.field_75204_e);

            for (int j = 4; j <= 6; ++j) {
                this.func_175811_a(world, iblockdata, j, 1, 4, structureboundingbox);
                this.func_175811_a(world, iblockdata, j, 2, 5, structureboundingbox);
                this.func_175811_a(world, iblockdata, j, 3, 6, structureboundingbox);
            }

            IBlockState iblockdata1 = Blocks.field_150378_br.func_176223_P().func_177226_a(BlockEndPortalFrame.field_176508_a, EnumFacing.NORTH);
            IBlockState iblockdata2 = Blocks.field_150378_br.func_176223_P().func_177226_a(BlockEndPortalFrame.field_176508_a, EnumFacing.SOUTH);
            IBlockState iblockdata3 = Blocks.field_150378_br.func_176223_P().func_177226_a(BlockEndPortalFrame.field_176508_a, EnumFacing.EAST);
            IBlockState iblockdata4 = Blocks.field_150378_br.func_176223_P().func_177226_a(BlockEndPortalFrame.field_176508_a, EnumFacing.WEST);
            boolean flag = true;
            boolean[] aboolean = new boolean[12];

            for (int k = 0; k < aboolean.length; ++k) {
                aboolean[k] = random.nextFloat() > 0.9F;
                flag &= aboolean[k];
            }

            this.func_175811_a(world, iblockdata1.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[0])), 4, 3, 8, structureboundingbox);
            this.func_175811_a(world, iblockdata1.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[1])), 5, 3, 8, structureboundingbox);
            this.func_175811_a(world, iblockdata1.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[2])), 6, 3, 8, structureboundingbox);
            this.func_175811_a(world, iblockdata2.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[3])), 4, 3, 12, structureboundingbox);
            this.func_175811_a(world, iblockdata2.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[4])), 5, 3, 12, structureboundingbox);
            this.func_175811_a(world, iblockdata2.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[5])), 6, 3, 12, structureboundingbox);
            this.func_175811_a(world, iblockdata3.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[6])), 3, 3, 9, structureboundingbox);
            this.func_175811_a(world, iblockdata3.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[7])), 3, 3, 10, structureboundingbox);
            this.func_175811_a(world, iblockdata3.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[8])), 3, 3, 11, structureboundingbox);
            this.func_175811_a(world, iblockdata4.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[9])), 7, 3, 9, structureboundingbox);
            this.func_175811_a(world, iblockdata4.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[10])), 7, 3, 10, structureboundingbox);
            this.func_175811_a(world, iblockdata4.func_177226_a(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(aboolean[11])), 7, 3, 11, structureboundingbox);
            if (flag) {
                IBlockState iblockdata5 = Blocks.field_150384_bq.func_176223_P();

                this.func_175811_a(world, iblockdata5, 4, 3, 9, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 5, 3, 9, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 6, 3, 9, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 4, 3, 10, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 5, 3, 10, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 6, 3, 10, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 4, 3, 11, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 5, 3, 11, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 6, 3, 11, structureboundingbox);
            }

            if (!this.field_75005_a) {
                int l = this.func_74862_a(3);
                BlockPos blockposition = new BlockPos(this.func_74865_a(5, 6), l, this.func_74873_b(5, 6));

                if (structureboundingbox.func_175898_b((Vec3i) blockposition)) {
                    this.field_75005_a = true;
                    world.func_180501_a(blockposition, Blocks.field_150474_ac.func_176223_P(), 2);
                    TileEntity tileentity = world.func_175625_s(blockposition);

                    if (tileentity instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner) tileentity).func_145881_a().func_190894_a(EntityList.func_191306_a(EntitySilverfish.class));
                    }
                }
            }

            return true;
        }
    }

    public static class Crossing extends StructureStrongholdPieces.Stronghold {

        private boolean field_74996_b;
        private boolean field_74997_c;
        private boolean field_74995_d;
        private boolean field_74999_h;

        public Crossing() {}

        public Crossing(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
            this.field_74996_b = random.nextBoolean();
            this.field_74997_c = random.nextBoolean();
            this.field_74995_d = random.nextBoolean();
            this.field_74999_h = random.nextInt(3) > 0;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("leftLow", this.field_74996_b);
            nbttagcompound.func_74757_a("leftHigh", this.field_74997_c);
            nbttagcompound.func_74757_a("rightLow", this.field_74995_d);
            nbttagcompound.func_74757_a("rightHigh", this.field_74999_h);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74996_b = nbttagcompound.func_74767_n("leftLow");
            this.field_74997_c = nbttagcompound.func_74767_n("leftHigh");
            this.field_74995_d = nbttagcompound.func_74767_n("rightLow");
            this.field_74999_h = nbttagcompound.func_74767_n("rightHigh");
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = 3;
            int j = 5;
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection == EnumFacing.WEST || enumdirection == EnumFacing.NORTH) {
                i = 8 - i;
                j = 8 - j;
            }

            this.func_74986_a((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 5, 1);
            if (this.field_74996_b) {
                this.func_74989_b((StructureStrongholdPieces.Stairs2) structurepiece, list, random, i, 1);
            }

            if (this.field_74997_c) {
                this.func_74989_b((StructureStrongholdPieces.Stairs2) structurepiece, list, random, j, 7);
            }

            if (this.field_74995_d) {
                this.func_74987_c((StructureStrongholdPieces.Stairs2) structurepiece, list, random, i, 1);
            }

            if (this.field_74999_h) {
                this.func_74987_c((StructureStrongholdPieces.Stairs2) structurepiece, list, random, j, 7);
            }

        }

        public static StructureStrongholdPieces.Crossing func_175866_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -4, -3, 0, 10, 9, 11, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.Crossing(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 9, 8, 10, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 4, 3, 0);
                if (this.field_74996_b) {
                    this.func_175804_a(world, structureboundingbox, 0, 3, 1, 0, 5, 3, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                if (this.field_74995_d) {
                    this.func_175804_a(world, structureboundingbox, 9, 3, 1, 9, 5, 3, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                if (this.field_74997_c) {
                    this.func_175804_a(world, structureboundingbox, 0, 5, 7, 0, 7, 9, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                if (this.field_74999_h) {
                    this.func_175804_a(world, structureboundingbox, 9, 5, 7, 9, 7, 9, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                this.func_175804_a(world, structureboundingbox, 5, 1, 10, 7, 3, 10, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                this.func_74882_a(world, structureboundingbox, 1, 2, 1, 8, 2, 6, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_74882_a(world, structureboundingbox, 4, 1, 5, 4, 4, 9, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_74882_a(world, structureboundingbox, 8, 1, 5, 8, 4, 9, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_74882_a(world, structureboundingbox, 1, 4, 7, 3, 4, 9, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_74882_a(world, structureboundingbox, 1, 3, 5, 3, 3, 6, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_175804_a(world, structureboundingbox, 1, 3, 4, 3, 3, 4, Blocks.field_150333_U.func_176223_P(), Blocks.field_150333_U.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 1, 4, 6, 3, 4, 6, Blocks.field_150333_U.func_176223_P(), Blocks.field_150333_U.func_176223_P(), false);
                this.func_74882_a(world, structureboundingbox, 5, 1, 7, 7, 1, 8, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_175804_a(world, structureboundingbox, 5, 1, 9, 7, 1, 9, Blocks.field_150333_U.func_176223_P(), Blocks.field_150333_U.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 5, 2, 7, 7, 2, 7, Blocks.field_150333_U.func_176223_P(), Blocks.field_150333_U.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 4, 5, 7, 4, 5, 9, Blocks.field_150333_U.func_176223_P(), Blocks.field_150333_U.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 8, 5, 7, 8, 5, 9, Blocks.field_150333_U.func_176223_P(), Blocks.field_150333_U.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 5, 5, 7, 7, 5, 9, Blocks.field_150334_T.func_176223_P(), Blocks.field_150334_T.func_176223_P(), false);
                this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.SOUTH), 6, 5, 6, structureboundingbox);
                return true;
            }
        }
    }

    public static class Library extends StructureStrongholdPieces.Stronghold {

        private boolean field_75008_c;

        public Library() {}

        public Library(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
            this.field_75008_c = structureboundingbox.func_78882_c() > 6;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Tall", this.field_75008_c);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_75008_c = nbttagcompound.func_74767_n("Tall");
        }

        public static StructureStrongholdPieces.Library func_175864_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -4, -1, 0, 14, 11, 15, enumdirection);

            if (!func_74991_a(structureboundingbox) || StructureComponent.func_74883_a(list, structureboundingbox) != null) {
                structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -4, -1, 0, 14, 6, 15, enumdirection);
                if (!func_74991_a(structureboundingbox) || StructureComponent.func_74883_a(list, structureboundingbox) != null) {
                    return null;
                }
            }

            return new StructureStrongholdPieces.Library(l, random, structureboundingbox, enumdirection);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                byte b0 = 11;

                if (!this.field_75008_c) {
                    b0 = 6;
                }

                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 13, b0 - 1, 14, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 4, 1, 0);
                this.func_189914_a(world, structureboundingbox, random, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.field_150321_G.func_176223_P(), Blocks.field_150321_G.func_176223_P(), false, 0);
                boolean flag = true;
                boolean flag1 = true;

                int i;

                for (i = 1; i <= 13; ++i) {
                    if ((i - 1) % 4 == 0) {
                        this.func_175804_a(world, structureboundingbox, 1, 1, i, 1, 4, i, Blocks.field_150344_f.func_176223_P(), Blocks.field_150344_f.func_176223_P(), false);
                        this.func_175804_a(world, structureboundingbox, 12, 1, i, 12, 4, i, Blocks.field_150344_f.func_176223_P(), Blocks.field_150344_f.func_176223_P(), false);
                        this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.EAST), 2, 3, i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.WEST), 11, 3, i, structureboundingbox);
                        if (this.field_75008_c) {
                            this.func_175804_a(world, structureboundingbox, 1, 6, i, 1, 9, i, Blocks.field_150344_f.func_176223_P(), Blocks.field_150344_f.func_176223_P(), false);
                            this.func_175804_a(world, structureboundingbox, 12, 6, i, 12, 9, i, Blocks.field_150344_f.func_176223_P(), Blocks.field_150344_f.func_176223_P(), false);
                        }
                    } else {
                        this.func_175804_a(world, structureboundingbox, 1, 1, i, 1, 4, i, Blocks.field_150342_X.func_176223_P(), Blocks.field_150342_X.func_176223_P(), false);
                        this.func_175804_a(world, structureboundingbox, 12, 1, i, 12, 4, i, Blocks.field_150342_X.func_176223_P(), Blocks.field_150342_X.func_176223_P(), false);
                        if (this.field_75008_c) {
                            this.func_175804_a(world, structureboundingbox, 1, 6, i, 1, 9, i, Blocks.field_150342_X.func_176223_P(), Blocks.field_150342_X.func_176223_P(), false);
                            this.func_175804_a(world, structureboundingbox, 12, 6, i, 12, 9, i, Blocks.field_150342_X.func_176223_P(), Blocks.field_150342_X.func_176223_P(), false);
                        }
                    }
                }

                for (i = 3; i < 12; i += 2) {
                    this.func_175804_a(world, structureboundingbox, 3, 1, i, 4, 3, i, Blocks.field_150342_X.func_176223_P(), Blocks.field_150342_X.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 6, 1, i, 7, 3, i, Blocks.field_150342_X.func_176223_P(), Blocks.field_150342_X.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 9, 1, i, 10, 3, i, Blocks.field_150342_X.func_176223_P(), Blocks.field_150342_X.func_176223_P(), false);
                }

                if (this.field_75008_c) {
                    this.func_175804_a(world, structureboundingbox, 1, 5, 1, 3, 5, 13, Blocks.field_150344_f.func_176223_P(), Blocks.field_150344_f.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 10, 5, 1, 12, 5, 13, Blocks.field_150344_f.func_176223_P(), Blocks.field_150344_f.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 4, 5, 1, 9, 5, 2, Blocks.field_150344_f.func_176223_P(), Blocks.field_150344_f.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 4, 5, 12, 9, 5, 13, Blocks.field_150344_f.func_176223_P(), Blocks.field_150344_f.func_176223_P(), false);
                    this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 9, 5, 11, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 8, 5, 11, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 9, 5, 10, structureboundingbox);
                    this.func_175804_a(world, structureboundingbox, 3, 6, 2, 3, 6, 12, Blocks.field_180407_aO.func_176223_P(), Blocks.field_180407_aO.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 10, 6, 2, 10, 6, 10, Blocks.field_180407_aO.func_176223_P(), Blocks.field_180407_aO.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 4, 6, 2, 9, 6, 2, Blocks.field_180407_aO.func_176223_P(), Blocks.field_180407_aO.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 4, 6, 12, 8, 6, 12, Blocks.field_180407_aO.func_176223_P(), Blocks.field_180407_aO.func_176223_P(), false);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 9, 6, 11, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 8, 6, 11, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 9, 6, 10, structureboundingbox);
                    IBlockState iblockdata = Blocks.field_150468_ap.func_176223_P().func_177226_a(BlockLadder.field_176382_a, EnumFacing.SOUTH);

                    this.func_175811_a(world, iblockdata, 10, 1, 13, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 10, 2, 13, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 10, 3, 13, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 10, 4, 13, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 10, 5, 13, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 10, 6, 13, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 10, 7, 13, structureboundingbox);
                    boolean flag2 = true;
                    boolean flag3 = true;

                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 6, 9, 7, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 7, 9, 7, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 6, 8, 7, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 7, 8, 7, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 6, 7, 7, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 7, 7, 7, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 5, 7, 7, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 8, 7, 7, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 6, 7, 6, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 6, 7, 8, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 7, 7, 6, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 7, 7, 8, structureboundingbox);
                    IBlockState iblockdata1 = Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.UP);

                    this.func_175811_a(world, iblockdata1, 5, 8, 7, structureboundingbox);
                    this.func_175811_a(world, iblockdata1, 8, 8, 7, structureboundingbox);
                    this.func_175811_a(world, iblockdata1, 6, 8, 6, structureboundingbox);
                    this.func_175811_a(world, iblockdata1, 6, 8, 8, structureboundingbox);
                    this.func_175811_a(world, iblockdata1, 7, 8, 6, structureboundingbox);
                    this.func_175811_a(world, iblockdata1, 7, 8, 8, structureboundingbox);
                }

                this.func_186167_a(world, structureboundingbox, random, 3, 3, 5, LootTableList.field_186426_h);
                if (this.field_75008_c) {
                    this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 12, 9, 1, structureboundingbox);
                    this.func_186167_a(world, structureboundingbox, random, 12, 8, 1, LootTableList.field_186426_h);
                }

                return true;
            }
        }
    }

    public static class Prison extends StructureStrongholdPieces.Stronghold {

        public Prison() {}

        public Prison(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74986_a((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
        }

        public static StructureStrongholdPieces.Prison func_175860_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -1, 0, 9, 5, 11, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.Prison(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 8, 4, 10, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
                this.func_175804_a(world, structureboundingbox, 1, 1, 10, 3, 3, 10, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                this.func_74882_a(world, structureboundingbox, 4, 1, 1, 4, 3, 1, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_74882_a(world, structureboundingbox, 4, 1, 3, 4, 3, 3, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_74882_a(world, structureboundingbox, 4, 1, 7, 4, 3, 7, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_74882_a(world, structureboundingbox, 4, 1, 9, 4, 3, 9, false, random, StructureStrongholdPieces.field_75204_e);
                this.func_175804_a(world, structureboundingbox, 4, 1, 4, 4, 3, 6, Blocks.field_150411_aY.func_176223_P(), Blocks.field_150411_aY.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 5, 1, 5, 7, 3, 5, Blocks.field_150411_aY.func_176223_P(), Blocks.field_150411_aY.func_176223_P(), false);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), 4, 3, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), 4, 3, 8, structureboundingbox);
                IBlockState iblockdata = Blocks.field_150454_av.func_176223_P().func_177226_a(BlockDoor.field_176520_a, EnumFacing.WEST);
                IBlockState iblockdata1 = Blocks.field_150454_av.func_176223_P().func_177226_a(BlockDoor.field_176520_a, EnumFacing.WEST).func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.UPPER);

                this.func_175811_a(world, iblockdata, 4, 1, 2, structureboundingbox);
                this.func_175811_a(world, iblockdata1, 4, 2, 2, structureboundingbox);
                this.func_175811_a(world, iblockdata, 4, 1, 8, structureboundingbox);
                this.func_175811_a(world, iblockdata1, 4, 2, 8, structureboundingbox);
                return true;
            }
        }
    }

    public static class RoomCrossing extends StructureStrongholdPieces.Stronghold {

        protected int field_75013_b;

        public RoomCrossing() {}

        public RoomCrossing(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
            this.field_75013_b = random.nextInt(5);
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74768_a("Type", this.field_75013_b);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_75013_b = nbttagcompound.func_74762_e("Type");
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74986_a((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 4, 1);
            this.func_74989_b((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 4);
            this.func_74987_c((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 4);
        }

        public static StructureStrongholdPieces.RoomCrossing func_175859_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -4, -1, 0, 11, 7, 11, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.RoomCrossing(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 10, 6, 10, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 4, 1, 0);
                this.func_175804_a(world, structureboundingbox, 4, 1, 10, 6, 3, 10, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 0, 1, 4, 0, 3, 6, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 10, 1, 4, 10, 3, 6, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                int i;

                switch (this.field_75013_b) {
                case 0:
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 5, 1, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 5, 2, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 5, 3, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.WEST), 4, 3, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.EAST), 6, 3, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.SOUTH), 5, 3, 4, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.NORTH), 5, 3, 6, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150333_U.func_176223_P(), 4, 1, 4, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150333_U.func_176223_P(), 4, 1, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150333_U.func_176223_P(), 4, 1, 6, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150333_U.func_176223_P(), 6, 1, 4, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150333_U.func_176223_P(), 6, 1, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150333_U.func_176223_P(), 6, 1, 6, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150333_U.func_176223_P(), 5, 1, 4, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150333_U.func_176223_P(), 5, 1, 6, structureboundingbox);
                    break;

                case 1:
                    for (i = 0; i < 5; ++i) {
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3, 1, 3 + i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 7, 1, 3 + i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3 + i, 1, 3, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3 + i, 1, 7, structureboundingbox);
                    }

                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 5, 1, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 5, 2, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 5, 3, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150358_i.func_176223_P(), 5, 4, 5, structureboundingbox);
                    break;

                case 2:
                    for (i = 1; i <= 9; ++i) {
                        this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 1, 3, i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 9, 3, i, structureboundingbox);
                    }

                    for (i = 1; i <= 9; ++i) {
                        this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), i, 3, 1, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), i, 3, 9, structureboundingbox);
                    }

                    this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 5, 1, 4, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 5, 1, 6, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 5, 3, 4, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 5, 3, 6, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 4, 1, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 6, 1, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 4, 3, 5, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 6, 3, 5, structureboundingbox);

                    for (i = 1; i <= 3; ++i) {
                        this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 4, i, 4, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 6, i, 4, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 4, i, 6, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150347_e.func_176223_P(), 6, i, 6, structureboundingbox);
                    }

                    this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P(), 5, 3, 5, structureboundingbox);

                    for (i = 2; i <= 8; ++i) {
                        this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 2, 3, i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 3, 3, i, structureboundingbox);
                        if (i <= 3 || i >= 7) {
                            this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 4, 3, i, structureboundingbox);
                            this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 5, 3, i, structureboundingbox);
                            this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 6, 3, i, structureboundingbox);
                        }

                        this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 7, 3, i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150344_f.func_176223_P(), 8, 3, i, structureboundingbox);
                    }

                    IBlockState iblockdata = Blocks.field_150468_ap.func_176223_P().func_177226_a(BlockLadder.field_176382_a, EnumFacing.WEST);

                    this.func_175811_a(world, iblockdata, 9, 1, 3, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 9, 2, 3, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 9, 3, 3, structureboundingbox);
                    this.func_186167_a(world, structureboundingbox, random, 3, 4, 8, LootTableList.field_186427_i);
                }

                return true;
            }
        }
    }

    public static class RightTurn extends StructureStrongholdPieces.LeftTurn {

        public RightTurn() {}

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.EAST) {
                this.func_74989_b((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            } else {
                this.func_74987_c((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            }

        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 4, 4, 4, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
                EnumFacing enumdirection = this.func_186165_e();

                if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.EAST) {
                    this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                } else {
                    this.func_175804_a(world, structureboundingbox, 4, 1, 1, 4, 3, 3, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                return true;
            }
        }
    }

    public static class LeftTurn extends StructureStrongholdPieces.Stronghold {

        public LeftTurn() {}

        public LeftTurn(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.EAST) {
                this.func_74987_c((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            } else {
                this.func_74989_b((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            }

        }

        public static StructureStrongholdPieces.LeftTurn func_175867_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -1, 0, 5, 5, 5, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.LeftTurn(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 4, 4, 4, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
                EnumFacing enumdirection = this.func_186165_e();

                if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.EAST) {
                    this.func_175804_a(world, structureboundingbox, 4, 1, 1, 4, 3, 3, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                } else {
                    this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 3, 3, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                return true;
            }
        }
    }

    public static class StairsStraight extends StructureStrongholdPieces.Stronghold {

        public StairsStraight() {}

        public StairsStraight(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74986_a((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
        }

        public static StructureStrongholdPieces.StairsStraight func_175861_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -7, 0, 5, 11, 8, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.StairsStraight(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 4, 10, 7, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 1, 7, 0);
                this.func_74990_a(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 7);
                IBlockState iblockdata = Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.SOUTH);

                for (int i = 0; i < 6; ++i) {
                    this.func_175811_a(world, iblockdata, 1, 6 - i, 1 + i, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 2, 6 - i, 1 + i, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 3, 6 - i, 1 + i, structureboundingbox);
                    if (i < 5) {
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 1, 5 - i, 1 + i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 2, 5 - i, 1 + i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3, 5 - i, 1 + i, structureboundingbox);
                    }
                }

                return true;
            }
        }
    }

    public static class ChestCorridor extends StructureStrongholdPieces.Stronghold {

        private boolean field_75002_c;

        public ChestCorridor() {}

        public ChestCorridor(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Chest", this.field_75002_c);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_75002_c = nbttagcompound.func_74767_n("Chest");
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74986_a((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
        }

        public static StructureStrongholdPieces.ChestCorridor func_175868_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -1, 0, 5, 5, 7, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.ChestCorridor(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 4, 4, 6, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
                this.func_74990_a(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
                this.func_175804_a(world, structureboundingbox, 3, 1, 2, 3, 1, 4, Blocks.field_150417_aV.func_176223_P(), Blocks.field_150417_aV.func_176223_P(), false);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a()), 3, 1, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a()), 3, 1, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a()), 3, 2, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a()), 3, 2, 4, structureboundingbox);

                for (int i = 2; i <= 4; ++i) {
                    this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.SMOOTHBRICK.func_176624_a()), 2, 1, i, structureboundingbox);
                }

                if (!this.field_75002_c && structureboundingbox.func_175898_b((Vec3i) (new BlockPos(this.func_74865_a(3, 3), this.func_74862_a(2), this.func_74873_b(3, 3))))) {
                    this.field_75002_c = true;
                    this.func_186167_a(world, structureboundingbox, random, 3, 2, 3, LootTableList.field_186428_j);
                }

                return true;
            }
        }
    }

    public static class Straight extends StructureStrongholdPieces.Stronghold {

        private boolean field_75019_b;
        private boolean field_75020_c;

        public Straight() {}

        public Straight(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
            this.field_75019_b = random.nextInt(2) == 0;
            this.field_75020_c = random.nextInt(2) == 0;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Left", this.field_75019_b);
            nbttagcompound.func_74757_a("Right", this.field_75020_c);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_75019_b = nbttagcompound.func_74767_n("Left");
            this.field_75020_c = nbttagcompound.func_74767_n("Right");
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74986_a((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
            if (this.field_75019_b) {
                this.func_74989_b((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 2);
            }

            if (this.field_75020_c) {
                this.func_74987_c((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 2);
            }

        }

        public static StructureStrongholdPieces.Straight func_175862_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -1, 0, 5, 5, 7, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.Straight(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 4, 4, 6, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 1, 1, 0);
                this.func_74990_a(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
                IBlockState iblockdata = Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.EAST);
                IBlockState iblockdata1 = Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.WEST);

                this.func_175809_a(world, structureboundingbox, random, 0.1F, 1, 2, 1, iblockdata);
                this.func_175809_a(world, structureboundingbox, random, 0.1F, 3, 2, 1, iblockdata1);
                this.func_175809_a(world, structureboundingbox, random, 0.1F, 1, 2, 5, iblockdata);
                this.func_175809_a(world, structureboundingbox, random, 0.1F, 3, 2, 5, iblockdata1);
                if (this.field_75019_b) {
                    this.func_175804_a(world, structureboundingbox, 0, 1, 2, 0, 3, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                if (this.field_75020_c) {
                    this.func_175804_a(world, structureboundingbox, 4, 1, 2, 4, 3, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                return true;
            }
        }
    }

    public static class Stairs2 extends StructureStrongholdPieces.Stairs {

        public StructureStrongholdPieces.PieceWeight field_75027_a;
        public StructureStrongholdPieces.PortalRoom field_75025_b;
        public List<StructureComponent> field_75026_c = Lists.newArrayList();

        public Stairs2() {}

        public Stairs2(int i, Random random, int j, int k) {
            super(0, random, j, k);
        }
    }

    public static class Stairs extends StructureStrongholdPieces.Stronghold {

        private boolean field_75024_a;

        public Stairs() {}

        public Stairs(int i, Random random, int j, int k) {
            super(i);
            this.field_75024_a = true;
            this.func_186164_a(EnumFacing.Plane.HORIZONTAL.func_179518_a(random));
            this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
            if (this.func_186165_e().func_176740_k() == EnumFacing.Axis.Z) {
                this.field_74887_e = new StructureBoundingBox(j, 64, k, j + 5 - 1, 74, k + 5 - 1);
            } else {
                this.field_74887_e = new StructureBoundingBox(j, 64, k, j + 5 - 1, 74, k + 5 - 1);
            }

        }

        public Stairs(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.field_75024_a = false;
            this.func_186164_a(enumdirection);
            this.field_143013_d = this.func_74988_a(random);
            this.field_74887_e = structureboundingbox;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Source", this.field_75024_a);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_75024_a = nbttagcompound.func_74767_n("Source");
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            if (this.field_75024_a) {
                StructureStrongholdPieces.field_75203_d = StructureStrongholdPieces.Crossing.class;
            }

            this.func_74986_a((StructureStrongholdPieces.Stairs2) structurepiece, list, random, 1, 1);
        }

        public static StructureStrongholdPieces.Stairs func_175863_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -7, 0, 5, 11, 5, enumdirection);

            return func_74991_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureStrongholdPieces.Stairs(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, 0, 0, 4, 10, 4, true, random, StructureStrongholdPieces.field_75204_e);
                this.func_74990_a(world, random, structureboundingbox, this.field_143013_d, 1, 7, 0);
                this.func_74990_a(world, random, structureboundingbox, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 4);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 2, 6, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 1, 5, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.STONE.func_176624_a()), 1, 6, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 1, 5, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 1, 4, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.STONE.func_176624_a()), 1, 5, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 2, 4, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3, 3, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.STONE.func_176624_a()), 3, 4, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3, 3, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3, 2, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.STONE.func_176624_a()), 3, 3, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 2, 2, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 1, 1, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.STONE.func_176624_a()), 1, 2, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 1, 1, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.STONE.func_176624_a()), 1, 1, 3, structureboundingbox);
                return true;
            }
        }
    }

    public static class Corridor extends StructureStrongholdPieces.Stronghold {

        private int field_74993_a;

        public Corridor() {}

        public Corridor(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_74993_a = enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.SOUTH ? structureboundingbox.func_78883_b() : structureboundingbox.func_78880_d();
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74768_a("Steps", this.field_74993_a);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74993_a = nbttagcompound.func_74762_e("Steps");
        }

        public static StructureBoundingBox func_175869_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            boolean flag = true;
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -1, 0, 5, 5, 4, enumdirection);
            StructureComponent structurepiece = StructureComponent.func_74883_a(list, structureboundingbox);

            if (structurepiece == null) {
                return null;
            } else {
                if (structurepiece.func_74874_b().field_78895_b == structureboundingbox.field_78895_b) {
                    for (int l = 3; l >= 1; --l) {
                        structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -1, 0, 5, 5, l - 1, enumdirection);
                        if (!structurepiece.func_74874_b().func_78884_a(structureboundingbox)) {
                            return StructureBoundingBox.func_175897_a(i, j, k, -1, -1, 0, 5, 5, l, enumdirection);
                        }
                    }
                }

                return null;
            }
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                for (int i = 0; i < this.field_74993_a; ++i) {
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 0, 0, i, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 1, 0, i, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 2, 0, i, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3, 0, i, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 4, 0, i, structureboundingbox);

                    for (int j = 1; j <= 3; ++j) {
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 0, j, i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 1, j, i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, j, i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 3, j, i, structureboundingbox);
                        this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 4, j, i, structureboundingbox);
                    }

                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 0, 4, i, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 1, 4, i, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 2, 4, i, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 3, 4, i, structureboundingbox);
                    this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), 4, 4, i, structureboundingbox);
                }

                return true;
            }
        }
    }

    abstract static class Stronghold extends StructureComponent {

        protected StructureStrongholdPieces.Stronghold.Door field_143013_d;

        public Stronghold() {
            this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
        }

        protected Stronghold(int i) {
            super(i);
            this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            nbttagcompound.func_74778_a("EntryDoor", this.field_143013_d.name());
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.valueOf(nbttagcompound.func_74779_i("EntryDoor"));
        }

        protected void func_74990_a(World world, Random random, StructureBoundingBox structureboundingbox, StructureStrongholdPieces.Stronghold.Door worldgenstrongholdpieces_worldgenstrongholdpiece_worldgenstrongholddoortype, int i, int j, int k) {
            switch (worldgenstrongholdpieces_worldgenstrongholdpiece_worldgenstrongholddoortype) {
            case OPENING:
                this.func_175804_a(world, structureboundingbox, i, j, k, i + 3 - 1, j + 3 - 1, k, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                break;

            case WOOD_DOOR:
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i, j, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i, j + 1, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i + 1, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i + 2, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i + 2, j + 1, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i + 2, j, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_180413_ao.func_176223_P(), i + 1, j, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_180413_ao.func_176223_P().func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.UPPER), i + 1, j + 1, k, structureboundingbox);
                break;

            case GRATES:
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), i + 1, j, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), i + 1, j + 1, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), i, j, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), i, j + 1, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), i, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), i + 1, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), i + 2, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), i + 2, j + 1, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), i + 2, j, k, structureboundingbox);
                break;

            case IRON_DOOR:
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i, j, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i, j + 1, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i + 1, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i + 2, j + 2, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i + 2, j + 1, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176223_P(), i + 2, j, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150454_av.func_176223_P(), i + 1, j, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150454_av.func_176223_P().func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.UPPER), i + 1, j + 1, k, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150430_aB.func_176223_P().func_177226_a(BlockButton.field_176387_N, EnumFacing.NORTH), i + 2, j + 1, k + 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150430_aB.func_176223_P().func_177226_a(BlockButton.field_176387_N, EnumFacing.SOUTH), i + 2, j + 1, k - 1, structureboundingbox);
            }

        }

        protected StructureStrongholdPieces.Stronghold.Door func_74988_a(Random random) {
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
        protected StructureComponent func_74986_a(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a + i, this.field_74887_e.field_78895_b + j, this.field_74887_e.field_78896_c - 1, enumdirection, this.func_74877_c());

                case SOUTH:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a + i, this.field_74887_e.field_78895_b + j, this.field_74887_e.field_78892_f + 1, enumdirection, this.func_74877_c());

                case WEST:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + j, this.field_74887_e.field_78896_c + i, enumdirection, this.func_74877_c());

                case EAST:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + j, this.field_74887_e.field_78896_c + i, enumdirection, this.func_74877_c());
                }
            }

            return null;
        }

        @Nullable
        protected StructureComponent func_74989_b(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.WEST, this.func_74877_c());

                case SOUTH:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.WEST, this.func_74877_c());

                case WEST:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c());

                case EAST:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c());
                }
            }

            return null;
        }

        @Nullable
        protected StructureComponent func_74987_c(StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.EAST, this.func_74877_c());

                case SOUTH:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.EAST, this.func_74877_c());

                case WEST:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c());

                case EAST:
                    return StructureStrongholdPieces.func_175953_c(worldgenstrongholdpieces_worldgenstrongholdstart, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c());
                }
            }

            return null;
        }

        protected static boolean func_74991_a(StructureBoundingBox structureboundingbox) {
            return structureboundingbox != null && structureboundingbox.field_78895_b > 10;
        }

        public static enum Door {

            OPENING, WOOD_DOOR, GRATES, IRON_DOOR;

            private Door() {}
        }
    }

    static class PieceWeight {

        public Class<? extends StructureStrongholdPieces.Stronghold> field_75194_a;
        public final int field_75192_b;
        public int field_75193_c;
        public int field_75191_d;

        public PieceWeight(Class<? extends StructureStrongholdPieces.Stronghold> oclass, int i, int j) {
            this.field_75194_a = oclass;
            this.field_75192_b = i;
            this.field_75191_d = j;
        }

        public boolean func_75189_a(int i) {
            return this.field_75191_d == 0 || this.field_75193_c < this.field_75191_d;
        }

        public boolean func_75190_a() {
            return this.field_75191_d == 0 || this.field_75193_c < this.field_75191_d;
        }
    }
}
