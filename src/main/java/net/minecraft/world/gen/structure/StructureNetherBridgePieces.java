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

    private static final StructureNetherBridgePieces.PieceWeight[] field_78742_a = new StructureNetherBridgePieces.PieceWeight[] { new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Straight.class, 30, 0, true), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing3.class, 10, 4), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing.class, 10, 4), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Stairs.class, 10, 3), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Throne.class, 5, 2), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Entrance.class, 5, 1)};
    private static final StructureNetherBridgePieces.PieceWeight[] field_78741_b = new StructureNetherBridgePieces.PieceWeight[] { new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor5.class, 25, 0, true), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Crossing2.class, 15, 5), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor2.class, 5, 10), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor.class, 5, 10), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor3.class, 10, 3, true), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.Corridor4.class, 7, 2), new StructureNetherBridgePieces.PieceWeight(StructureNetherBridgePieces.NetherStalkRoom.class, 5, 2)};

    public static void func_143049_a() {
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Crossing3.class, "NeBCr");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.End.class, "NeBEF");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Straight.class, "NeBS");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Corridor3.class, "NeCCS");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Corridor4.class, "NeCTB");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Entrance.class, "NeCE");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Crossing2.class, "NeSCSC");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Corridor.class, "NeSCLT");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Corridor5.class, "NeSC");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Corridor2.class, "NeSCRT");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.NetherStalkRoom.class, "NeCSR");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Throne.class, "NeMT");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Crossing.class, "NeRC");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Stairs.class, "NeSR");
        MapGenStructureIO.func_143031_a(StructureNetherBridgePieces.Start.class, "NeStart");
    }

    private static StructureNetherBridgePieces.Piece func_175887_b(StructureNetherBridgePieces.PieceWeight worldgennetherpieces_worldgennetherpieceweight, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        Class oclass = worldgennetherpieces_worldgennetherpieceweight.field_78828_a;
        Object object = null;

        if (oclass == StructureNetherBridgePieces.Straight.class) {
            object = StructureNetherBridgePieces.Straight.func_175882_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Crossing3.class) {
            object = StructureNetherBridgePieces.Crossing3.func_175885_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Crossing.class) {
            object = StructureNetherBridgePieces.Crossing.func_175873_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Stairs.class) {
            object = StructureNetherBridgePieces.Stairs.func_175872_a(list, random, i, j, k, l, enumdirection);
        } else if (oclass == StructureNetherBridgePieces.Throne.class) {
            object = StructureNetherBridgePieces.Throne.func_175874_a(list, random, i, j, k, l, enumdirection);
        } else if (oclass == StructureNetherBridgePieces.Entrance.class) {
            object = StructureNetherBridgePieces.Entrance.func_175881_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor5.class) {
            object = StructureNetherBridgePieces.Corridor5.func_175877_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor2.class) {
            object = StructureNetherBridgePieces.Corridor2.func_175876_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor.class) {
            object = StructureNetherBridgePieces.Corridor.func_175879_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor3.class) {
            object = StructureNetherBridgePieces.Corridor3.func_175883_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Corridor4.class) {
            object = StructureNetherBridgePieces.Corridor4.func_175880_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.Crossing2.class) {
            object = StructureNetherBridgePieces.Crossing2.func_175878_a(list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureNetherBridgePieces.NetherStalkRoom.class) {
            object = StructureNetherBridgePieces.NetherStalkRoom.func_175875_a(list, random, i, j, k, enumdirection, l);
        }

        return (StructureNetherBridgePieces.Piece) object;
    }

    public static class Corridor4 extends StructureNetherBridgePieces.Piece {

        public Corridor4() {}

        public Corridor4(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            byte b0 = 1;
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection == EnumFacing.WEST || enumdirection == EnumFacing.NORTH) {
                b0 = 5;
            }

            this.func_74961_b((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, b0, random.nextInt(8) > 0);
            this.func_74965_c((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, b0, random.nextInt(8) > 0);
        }

        public static StructureNetherBridgePieces.Corridor4 func_175880_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -3, 0, 0, 9, 7, 9, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor4(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 8, 1, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 8, 5, 8, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 6, 0, 8, 6, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 2, 5, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 2, 0, 8, 5, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 0, 1, 4, 0, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 3, 0, 7, 4, 0, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 4, 8, 2, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 4, 2, 2, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 1, 4, 7, 2, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 8, 8, 3, 8, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 6, 0, 3, 7, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 3, 6, 8, 3, 7, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 4, 0, 5, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 3, 4, 8, 5, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 5, 2, 5, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 3, 5, 7, 5, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 4, 5, 1, 5, 5, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 4, 5, 7, 5, 5, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);

            for (int i = 0; i <= 5; ++i) {
                for (int j = 0; j <= 8; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), j, -1, i, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Corridor3 extends StructureNetherBridgePieces.Piece {

        public Corridor3() {}

        public Corridor3(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 1, 0, true);
        }

        public static StructureNetherBridgePieces.Corridor3 func_175883_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -7, 0, 5, 14, 10, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor3(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            IBlockState iblockdata = Blocks.field_150387_bl.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.SOUTH);

            for (int i = 0; i <= 9; ++i) {
                int j = Math.max(1, 7 - i);
                int k = Math.min(Math.max(j + 5, 14 - i), 13);
                int l = i;

                this.func_175804_a(world, structureboundingbox, 0, 0, i, 4, j, i, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 1, j + 1, i, 3, k - 1, i, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                if (i <= 6) {
                    this.func_175811_a(world, iblockdata, 1, j + 1, i, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 2, j + 1, i, structureboundingbox);
                    this.func_175811_a(world, iblockdata, 3, j + 1, i, structureboundingbox);
                }

                this.func_175804_a(world, structureboundingbox, 0, k, i, 4, k, i, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 0, j + 1, i, 0, k - 1, i, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 4, j + 1, i, 4, k - 1, i, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
                if ((i & 1) == 0) {
                    this.func_175804_a(world, structureboundingbox, 0, j + 2, i, 0, j + 3, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, 4, j + 2, i, 4, j + 3, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                }

                for (int i1 = 0; i1 <= 4; ++i1) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i1, -1, l, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Corridor extends StructureNetherBridgePieces.Piece {

        private boolean field_111021_b;

        public Corridor() {}

        public Corridor(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_111021_b = random.nextInt(3) == 0;
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_111021_b = nbttagcompound.func_74767_n("Chest");
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Chest", this.field_111021_b);
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74961_b((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 1, true);
        }

        public static StructureNetherBridgePieces.Corridor func_175879_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, 0, 0, 5, 7, 5, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 2, 0, 4, 5, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 3, 1, 4, 4, 1, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 3, 3, 4, 4, 3, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 5, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 4, 3, 5, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 4, 1, 4, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 3, 3, 4, 3, 4, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            if (this.field_111021_b && structureboundingbox.func_175898_b((Vec3i) (new BlockPos(this.func_74865_a(3, 3), this.func_74862_a(2), this.func_74873_b(3, 3))))) {
                this.field_111021_b = false;
                this.func_186167_a(world, structureboundingbox, random, 3, 2, 3, LootTableList.field_186425_g);
            }

            this.func_175804_a(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Corridor2 extends StructureNetherBridgePieces.Piece {

        private boolean field_111020_b;

        public Corridor2() {}

        public Corridor2(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_111020_b = random.nextInt(3) == 0;
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_111020_b = nbttagcompound.func_74767_n("Chest");
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Chest", this.field_111020_b);
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74965_c((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 1, true);
        }

        public static StructureNetherBridgePieces.Corridor2 func_175876_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, 0, 0, 5, 7, 5, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor2(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 5, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 1, 0, 4, 1, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 3, 0, 4, 3, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 2, 0, 4, 5, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 4, 4, 5, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 4, 1, 4, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 3, 3, 4, 3, 4, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            if (this.field_111020_b && structureboundingbox.func_175898_b((Vec3i) (new BlockPos(this.func_74865_a(1, 3), this.func_74862_a(2), this.func_74873_b(1, 3))))) {
                this.field_111020_b = false;
                this.func_186167_a(world, structureboundingbox, random, 1, 2, 3, LootTableList.field_186425_g);
            }

            this.func_175804_a(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Crossing2 extends StructureNetherBridgePieces.Piece {

        public Crossing2() {}

        public Crossing2(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 1, 0, true);
            this.func_74961_b((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 1, true);
            this.func_74965_c((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 1, true);
        }

        public static StructureNetherBridgePieces.Crossing2 func_175878_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, 0, 0, 5, 7, 5, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Crossing2(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 5, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 2, 0, 4, 5, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 4, 0, 5, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 2, 4, 4, 5, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Corridor5 extends StructureNetherBridgePieces.Piece {

        public Corridor5() {}

        public Corridor5(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 1, 0, true);
        }

        public static StructureNetherBridgePieces.Corridor5 func_175877_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, 0, 0, 5, 7, 5, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor5(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 4, 1, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 4, 5, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 5, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 2, 0, 4, 5, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 1, 0, 4, 1, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 3, 0, 4, 3, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 3, 1, 4, 4, 1, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 3, 3, 4, 4, 3, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 6, 0, 4, 6, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class NetherStalkRoom extends StructureNetherBridgePieces.Piece {

        public NetherStalkRoom() {}

        public NetherStalkRoom(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 5, 3, true);
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 5, 11, true);
        }

        public static StructureNetherBridgePieces.NetherStalkRoom func_175875_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -5, -3, 0, 13, 14, 13, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.NetherStalkRoom(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 3, 0, 12, 4, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 0, 12, 13, 12, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 0, 1, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 11, 5, 0, 12, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 11, 4, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 5, 11, 10, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 9, 11, 7, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 0, 4, 12, 1, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 5, 0, 10, 12, 1, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 9, 0, 7, 12, 1, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 11, 2, 10, 12, 10, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            int i;

            for (i = 1; i <= 11; i += 2) {
                this.func_175804_a(world, structureboundingbox, i, 10, 0, i, 11, 0, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, i, 10, 12, i, 11, 12, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 0, 10, i, 0, 11, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 12, 10, i, 12, 11, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), i, 13, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), i, 13, 12, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), 0, 13, i, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), 12, 13, i, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), i + 1, 13, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), i + 1, 13, 12, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 0, 13, i + 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 12, 13, i + 1, structureboundingbox);
            }

            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 0, 13, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 0, 13, 12, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 0, 13, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 12, 13, 0, structureboundingbox);

            for (i = 3; i <= 9; i += 2) {
                this.func_175804_a(world, structureboundingbox, 1, 7, i, 1, 8, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 11, 7, i, 11, 8, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            }

            IBlockState iblockdata = Blocks.field_150387_bl.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH);

            int j;
            int k;

            for (j = 0; j <= 6; ++j) {
                int l = j + 4;

                for (k = 5; k <= 7; ++k) {
                    this.func_175811_a(world, iblockdata, k, 5 + j, l, structureboundingbox);
                }

                if (l >= 5 && l <= 8) {
                    this.func_175804_a(world, structureboundingbox, 5, 5, l, 7, j + 4, l, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
                } else if (l >= 9 && l <= 10) {
                    this.func_175804_a(world, structureboundingbox, 5, 8, l, 7, j + 4, l, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
                }

                if (j >= 1) {
                    this.func_175804_a(world, structureboundingbox, 5, 6 + j, l, 7, 9 + j, l, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }
            }

            for (j = 5; j <= 7; ++j) {
                this.func_175811_a(world, iblockdata, j, 12, 11, structureboundingbox);
            }

            this.func_175804_a(world, structureboundingbox, 5, 6, 7, 5, 7, 7, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 6, 7, 7, 7, 7, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 13, 12, 7, 13, 12, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 2, 3, 5, 3, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 9, 3, 5, 10, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 4, 2, 5, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, 5, 2, 10, 5, 3, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, 5, 9, 10, 5, 10, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 10, 5, 4, 10, 5, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            IBlockState iblockdata1 = iblockdata.func_177226_a(BlockStairs.field_176309_a, EnumFacing.EAST);
            IBlockState iblockdata2 = iblockdata.func_177226_a(BlockStairs.field_176309_a, EnumFacing.WEST);

            this.func_175811_a(world, iblockdata2, 4, 5, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata2, 4, 5, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata2, 4, 5, 9, structureboundingbox);
            this.func_175811_a(world, iblockdata2, 4, 5, 10, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 8, 5, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 8, 5, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 8, 5, 9, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 8, 5, 10, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 3, 4, 4, 4, 4, 8, Blocks.field_150425_aM.func_176223_P(), Blocks.field_150425_aM.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 4, 4, 9, 4, 8, Blocks.field_150425_aM.func_176223_P(), Blocks.field_150425_aM.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 3, 5, 4, 4, 5, 8, Blocks.field_150388_bm.func_176223_P(), Blocks.field_150388_bm.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 5, 4, 9, 5, 8, Blocks.field_150388_bm.func_176223_P(), Blocks.field_150388_bm.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 2, 0, 8, 2, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 4, 12, 2, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 0, 0, 8, 1, 3, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 0, 9, 8, 1, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 4, 3, 1, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, 0, 4, 12, 1, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            int i1;

            for (k = 4; k <= 8; ++k) {
                for (i1 = 0; i1 <= 2; ++i1) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), k, -1, i1, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), k, -1, 12 - i1, structureboundingbox);
                }
            }

            for (k = 0; k <= 2; ++k) {
                for (i1 = 4; i1 <= 8; ++i1) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), k, -1, i1, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), 12 - k, -1, i1, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Entrance extends StructureNetherBridgePieces.Piece {

        public Entrance() {}

        public Entrance(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 5, 3, true);
        }

        public static StructureNetherBridgePieces.Entrance func_175881_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -5, -3, 0, 13, 14, 13, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Entrance(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 3, 0, 12, 4, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 0, 12, 13, 12, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 0, 1, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 11, 5, 0, 12, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 11, 4, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 5, 11, 10, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 9, 11, 7, 12, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 0, 4, 12, 1, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 5, 0, 10, 12, 1, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 9, 0, 7, 12, 1, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 11, 2, 10, 12, 10, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 8, 0, 7, 8, 0, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);

            int i;

            for (i = 1; i <= 11; i += 2) {
                this.func_175804_a(world, structureboundingbox, i, 10, 0, i, 11, 0, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, i, 10, 12, i, 11, 12, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 0, 10, i, 0, 11, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 12, 10, i, 12, 11, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), i, 13, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), i, 13, 12, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), 0, 13, i, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), 12, 13, i, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), i + 1, 13, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), i + 1, 13, 12, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 0, 13, i + 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 12, 13, i + 1, structureboundingbox);
            }

            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 0, 13, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 0, 13, 12, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 0, 13, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 12, 13, 0, structureboundingbox);

            for (i = 3; i <= 9; i += 2) {
                this.func_175804_a(world, structureboundingbox, 1, 7, i, 1, 8, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 11, 7, i, 11, 8, i, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            }

            this.func_175804_a(world, structureboundingbox, 4, 2, 0, 8, 2, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 4, 12, 2, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 0, 0, 8, 1, 3, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 0, 9, 8, 1, 12, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 4, 3, 1, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, 0, 4, 12, 1, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            int j;

            for (i = 4; i <= 8; ++i) {
                for (j = 0; j <= 2; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, 12 - j, structureboundingbox);
                }
            }

            for (i = 0; i <= 2; ++i) {
                for (j = 4; j <= 8; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), 12 - i, -1, j, structureboundingbox);
                }
            }

            this.func_175804_a(world, structureboundingbox, 5, 5, 5, 7, 5, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 1, 6, 6, 4, 6, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), 6, 0, 6, structureboundingbox);
            IBlockState iblockdata = Blocks.field_150356_k.func_176223_P();

            this.func_175811_a(world, iblockdata, 6, 5, 6, structureboundingbox);
            BlockPos blockposition = new BlockPos(this.func_74865_a(6, 6), this.func_74862_a(5), this.func_74873_b(6, 6));

            if (structureboundingbox.func_175898_b((Vec3i) blockposition)) {
                world.func_189507_a(blockposition, iblockdata, random);
            }

            return true;
        }
    }

    public static class Throne extends StructureNetherBridgePieces.Piece {

        private boolean field_74976_a;

        public Throne() {}

        public Throne(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74976_a = nbttagcompound.func_74767_n("Mob");
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Mob", this.field_74976_a);
        }

        public static StructureNetherBridgePieces.Throne func_175874_a(List<StructureComponent> list, Random random, int i, int j, int k, int l, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -2, 0, 0, 7, 8, 9, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Throne(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 6, 7, 7, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 0, 5, 1, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 1, 5, 2, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 2, 5, 3, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 4, 3, 5, 4, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 0, 1, 4, 2, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 2, 0, 5, 4, 2, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 5, 2, 1, 5, 3, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 5, 2, 5, 5, 3, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 3, 0, 5, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 5, 3, 6, 5, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 5, 8, 5, 5, 8, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 1, 6, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150386_bk.func_176223_P(), 5, 6, 3, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 0, 6, 3, 0, 6, 8, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 6, 3, 6, 6, 8, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 6, 8, 5, 7, 8, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 8, 8, 4, 8, 8, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            if (!this.field_74976_a) {
                BlockPos blockposition = new BlockPos(this.func_74865_a(3, 5), this.func_74862_a(5), this.func_74873_b(3, 5));

                if (structureboundingbox.func_175898_b((Vec3i) blockposition)) {
                    this.field_74976_a = true;
                    world.func_180501_a(blockposition, Blocks.field_150474_ac.func_176223_P(), 2);
                    TileEntity tileentity = world.func_175625_s(blockposition);

                    if (tileentity instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner) tileentity).func_145881_a().func_190894_a(EntityList.func_191306_a(EntityBlaze.class));
                    }
                }
            }

            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Stairs extends StructureNetherBridgePieces.Piece {

        public Stairs() {}

        public Stairs(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74965_c((StructureNetherBridgePieces.Start) structurepiece, list, random, 6, 2, false);
        }

        public static StructureNetherBridgePieces.Stairs func_175872_a(List<StructureComponent> list, Random random, int i, int j, int k, int l, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -2, 0, 0, 7, 11, 7, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Stairs(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 6, 1, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 6, 10, 6, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 1, 8, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 2, 0, 6, 8, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 1, 0, 8, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 2, 1, 6, 8, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 6, 5, 8, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 2, 0, 5, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 3, 2, 6, 5, 2, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 3, 4, 6, 5, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150385_bj.func_176223_P(), 5, 2, 5, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 4, 2, 5, 4, 3, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 3, 2, 5, 3, 4, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 2, 5, 2, 5, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 5, 1, 6, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 7, 1, 5, 7, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 8, 2, 6, 8, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 6, 0, 4, 8, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 0, 4, 5, 0, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);

            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Crossing extends StructureNetherBridgePieces.Piece {

        public Crossing() {}

        public Crossing(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 2, 0, false);
            this.func_74961_b((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 2, false);
            this.func_74965_c((StructureNetherBridgePieces.Start) structurepiece, list, random, 0, 2, false);
        }

        public static StructureNetherBridgePieces.Crossing func_175873_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -2, 0, 0, 7, 9, 7, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Crossing(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 6, 1, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 6, 7, 6, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 1, 6, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 6, 1, 6, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 2, 0, 6, 6, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 2, 6, 6, 6, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 6, 1, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 5, 0, 6, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 2, 0, 6, 6, 1, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 2, 5, 6, 6, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 6, 0, 4, 6, 0, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 0, 4, 5, 0, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 6, 6, 4, 6, 6, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 5, 6, 4, 5, 6, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 6, 2, 0, 6, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 2, 0, 5, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 6, 2, 6, 6, 4, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 5, 2, 6, 5, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);

            for (int i = 0; i <= 6; ++i) {
                for (int j = 0; j <= 6; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Crossing3 extends StructureNetherBridgePieces.Piece {

        public Crossing3() {}

        public Crossing3(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        protected Crossing3(Random random, int i, int j) {
            super(0);
            this.func_186164_a(EnumFacing.Plane.HORIZONTAL.func_179518_a(random));
            if (this.func_186165_e().func_176740_k() == EnumFacing.Axis.Z) {
                this.field_74887_e = new StructureBoundingBox(i, 64, j, i + 19 - 1, 73, j + 19 - 1);
            } else {
                this.field_74887_e = new StructureBoundingBox(i, 64, j, i + 19 - 1, 73, j + 19 - 1);
            }

        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 8, 3, false);
            this.func_74961_b((StructureNetherBridgePieces.Start) structurepiece, list, random, 3, 8, false);
            this.func_74965_c((StructureNetherBridgePieces.Start) structurepiece, list, random, 3, 8, false);
        }

        public static StructureNetherBridgePieces.Crossing3 func_175885_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -8, -3, 0, 19, 10, 19, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Crossing3(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 7, 3, 0, 11, 4, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 7, 18, 4, 11, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 5, 0, 10, 7, 18, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 8, 18, 7, 10, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 5, 0, 7, 5, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 5, 11, 7, 5, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 11, 5, 0, 11, 5, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 11, 5, 11, 11, 5, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 7, 7, 5, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 11, 5, 7, 18, 5, 7, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 11, 7, 5, 11, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 11, 5, 11, 18, 5, 11, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 2, 0, 11, 2, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 2, 13, 11, 2, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 0, 0, 11, 1, 3, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 0, 15, 11, 1, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            int i;
            int j;

            for (i = 7; i <= 11; ++i) {
                for (j = 0; j <= 2; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, 18 - j, structureboundingbox);
                }
            }

            this.func_175804_a(world, structureboundingbox, 0, 2, 7, 5, 2, 11, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 13, 2, 7, 18, 2, 11, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 7, 3, 1, 11, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 15, 0, 7, 18, 1, 11, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            for (i = 0; i <= 2; ++i) {
                for (j = 7; j <= 11; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), 18 - i, -1, j, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class End extends StructureNetherBridgePieces.Piece {

        private int field_74972_a;

        public End() {}

        public End(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_74972_a = random.nextInt();
        }

        public static StructureNetherBridgePieces.End func_175884_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -3, 0, 5, 10, 8, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.End(l, random, structureboundingbox, enumdirection) : null;
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74972_a = nbttagcompound.func_74762_e("Seed");
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74768_a("Seed", this.field_74972_a);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            Random random1 = new Random((long) this.field_74972_a);

            int i;
            int j;
            int k;

            for (i = 0; i <= 4; ++i) {
                for (j = 3; j <= 4; ++j) {
                    k = random1.nextInt(8);
                    this.func_175804_a(world, structureboundingbox, i, j, 0, i, j, k, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
                }
            }

            i = random1.nextInt(8);
            this.func_175804_a(world, structureboundingbox, 0, 5, 0, 0, 5, i, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            i = random1.nextInt(8);
            this.func_175804_a(world, structureboundingbox, 4, 5, 0, 4, 5, i, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            for (i = 0; i <= 4; ++i) {
                j = random1.nextInt(5);
                this.func_175804_a(world, structureboundingbox, i, 2, 0, i, 2, j, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            }

            for (i = 0; i <= 4; ++i) {
                for (j = 0; j <= 1; ++j) {
                    k = random1.nextInt(3);
                    this.func_175804_a(world, structureboundingbox, i, j, 0, i, j, k, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
                }
            }

            return true;
        }
    }

    public static class Straight extends StructureNetherBridgePieces.Piece {

        public Straight() {}

        public Straight(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            this.func_74963_a((StructureNetherBridgePieces.Start) structurepiece, list, random, 1, 3, false);
        }

        public static StructureNetherBridgePieces.Straight func_175882_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, -1, -3, 0, 5, 10, 19, enumdirection);

            return func_74964_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureNetherBridgePieces.Straight(l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, 3, 0, 4, 4, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 5, 0, 3, 7, 18, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 0, 0, 5, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 5, 0, 4, 5, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 4, 2, 5, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 13, 4, 2, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 4, 1, 3, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 15, 4, 1, 18, Blocks.field_150385_bj.func_176223_P(), Blocks.field_150385_bj.func_176223_P(), false);

            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 2; ++j) {
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, j, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150385_bj.func_176223_P(), i, -1, 18 - j, structureboundingbox);
                }
            }

            this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 4, 1, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 4, 0, 4, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 3, 14, 0, 4, 14, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 17, 0, 4, 17, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 1, 1, 4, 4, 1, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 3, 4, 4, 4, 4, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 3, 14, 4, 4, 14, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 1, 17, 4, 4, 17, Blocks.field_150386_bk.func_176223_P(), Blocks.field_150386_bk.func_176223_P(), false);
            return true;
        }
    }

    public static class Start extends StructureNetherBridgePieces.Crossing3 {

        public StructureNetherBridgePieces.PieceWeight field_74970_a;
        public List<StructureNetherBridgePieces.PieceWeight> field_74968_b;
        public List<StructureNetherBridgePieces.PieceWeight> field_74969_c;
        public List<StructureComponent> field_74967_d = Lists.newArrayList();

        public Start() {}

        public Start(Random random, int i, int j) {
            super(random, i, j);
            this.field_74968_b = Lists.newArrayList();
            StructureNetherBridgePieces.PieceWeight[] aworldgennetherpieces_worldgennetherpieceweight = StructureNetherBridgePieces.field_78742_a;
            int k = aworldgennetherpieces_worldgennetherpieceweight.length;

            int l;
            StructureNetherBridgePieces.PieceWeight worldgennetherpieces_worldgennetherpieceweight;

            for (l = 0; l < k; ++l) {
                worldgennetherpieces_worldgennetherpieceweight = aworldgennetherpieces_worldgennetherpieceweight[l];
                worldgennetherpieces_worldgennetherpieceweight.field_78827_c = 0;
                this.field_74968_b.add(worldgennetherpieces_worldgennetherpieceweight);
            }

            this.field_74969_c = Lists.newArrayList();
            aworldgennetherpieces_worldgennetherpieceweight = StructureNetherBridgePieces.field_78741_b;
            k = aworldgennetherpieces_worldgennetherpieceweight.length;

            for (l = 0; l < k; ++l) {
                worldgennetherpieces_worldgennetherpieceweight = aworldgennetherpieces_worldgennetherpieceweight[l];
                worldgennetherpieces_worldgennetherpieceweight.field_78827_c = 0;
                this.field_74969_c.add(worldgennetherpieces_worldgennetherpieceweight);
            }

        }
    }

    abstract static class Piece extends StructureComponent {

        public Piece() {}

        protected Piece(int i) {
            super(i);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {}

        protected void func_143012_a(NBTTagCompound nbttagcompound) {}

        private int func_74960_a(List<StructureNetherBridgePieces.PieceWeight> list) {
            boolean flag = false;
            int i = 0;

            StructureNetherBridgePieces.PieceWeight worldgennetherpieces_worldgennetherpieceweight;

            for (Iterator iterator = list.iterator(); iterator.hasNext(); i += worldgennetherpieces_worldgennetherpieceweight.field_78826_b) {
                worldgennetherpieces_worldgennetherpieceweight = (StructureNetherBridgePieces.PieceWeight) iterator.next();
                if (worldgennetherpieces_worldgennetherpieceweight.field_78824_d > 0 && worldgennetherpieces_worldgennetherpieceweight.field_78827_c < worldgennetherpieces_worldgennetherpieceweight.field_78824_d) {
                    flag = true;
                }
            }

            return flag ? i : -1;
        }

        private StructureNetherBridgePieces.Piece func_175871_a(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureNetherBridgePieces.PieceWeight> list, List<StructureComponent> list1, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            int i1 = this.func_74960_a(list);
            boolean flag = i1 > 0 && l <= 30;
            int j1 = 0;

            while (j1 < 5 && flag) {
                ++j1;
                int k1 = random.nextInt(i1);
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    StructureNetherBridgePieces.PieceWeight worldgennetherpieces_worldgennetherpieceweight = (StructureNetherBridgePieces.PieceWeight) iterator.next();

                    k1 -= worldgennetherpieces_worldgennetherpieceweight.field_78826_b;
                    if (k1 < 0) {
                        if (!worldgennetherpieces_worldgennetherpieceweight.func_78822_a(l) || worldgennetherpieces_worldgennetherpieceweight == worldgennetherpieces_worldgennetherpiece15.field_74970_a && !worldgennetherpieces_worldgennetherpieceweight.field_78825_e) {
                            break;
                        }

                        StructureNetherBridgePieces.Piece worldgennetherpieces_worldgennetherpiece = StructureNetherBridgePieces.func_175887_b(worldgennetherpieces_worldgennetherpieceweight, list1, random, i, j, k, enumdirection, l);

                        if (worldgennetherpieces_worldgennetherpiece != null) {
                            ++worldgennetherpieces_worldgennetherpieceweight.field_78827_c;
                            worldgennetherpieces_worldgennetherpiece15.field_74970_a = worldgennetherpieces_worldgennetherpieceweight;
                            if (!worldgennetherpieces_worldgennetherpieceweight.func_78823_a()) {
                                list.remove(worldgennetherpieces_worldgennetherpieceweight);
                            }

                            return worldgennetherpieces_worldgennetherpiece;
                        }
                    }
                }
            }

            return StructureNetherBridgePieces.End.func_175884_a(list1, random, i, j, k, enumdirection, l);
        }

        private StructureComponent func_175870_a(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureComponent> list, Random random, int i, int j, int k, @Nullable EnumFacing enumdirection, int l, boolean flag) {
            if (Math.abs(i - worldgennetherpieces_worldgennetherpiece15.func_74874_b().field_78897_a) <= 112 && Math.abs(k - worldgennetherpieces_worldgennetherpiece15.func_74874_b().field_78896_c) <= 112) {
                List list1 = worldgennetherpieces_worldgennetherpiece15.field_74968_b;

                if (flag) {
                    list1 = worldgennetherpieces_worldgennetherpiece15.field_74969_c;
                }

                StructureNetherBridgePieces.Piece worldgennetherpieces_worldgennetherpiece = this.func_175871_a(worldgennetherpieces_worldgennetherpiece15, list1, list, random, i, j, k, enumdirection, l + 1);

                if (worldgennetherpieces_worldgennetherpiece != null) {
                    list.add(worldgennetherpieces_worldgennetherpiece);
                    worldgennetherpieces_worldgennetherpiece15.field_74967_d.add(worldgennetherpieces_worldgennetherpiece);
                }

                return worldgennetherpieces_worldgennetherpiece;
            } else {
                return StructureNetherBridgePieces.End.func_175884_a(list, random, i, j, k, enumdirection, l);
            }
        }

        @Nullable
        protected StructureComponent func_74963_a(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureComponent> list, Random random, int i, int j, boolean flag) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a + i, this.field_74887_e.field_78895_b + j, this.field_74887_e.field_78896_c - 1, enumdirection, this.func_74877_c(), flag);

                case SOUTH:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a + i, this.field_74887_e.field_78895_b + j, this.field_74887_e.field_78892_f + 1, enumdirection, this.func_74877_c(), flag);

                case WEST:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + j, this.field_74887_e.field_78896_c + i, enumdirection, this.func_74877_c(), flag);

                case EAST:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + j, this.field_74887_e.field_78896_c + i, enumdirection, this.func_74877_c(), flag);
                }
            }

            return null;
        }

        @Nullable
        protected StructureComponent func_74961_b(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureComponent> list, Random random, int i, int j, boolean flag) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.WEST, this.func_74877_c(), flag);

                case SOUTH:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.WEST, this.func_74877_c(), flag);

                case WEST:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c(), flag);

                case EAST:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c(), flag);
                }
            }

            return null;
        }

        @Nullable
        protected StructureComponent func_74965_c(StructureNetherBridgePieces.Start worldgennetherpieces_worldgennetherpiece15, List<StructureComponent> list, Random random, int i, int j, boolean flag) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.EAST, this.func_74877_c(), flag);

                case SOUTH:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.EAST, this.func_74877_c(), flag);

                case WEST:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c(), flag);

                case EAST:
                    return this.func_175870_a(worldgennetherpieces_worldgennetherpiece15, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c(), flag);
                }
            }

            return null;
        }

        protected static boolean func_74964_a(StructureBoundingBox structureboundingbox) {
            return structureboundingbox != null && structureboundingbox.field_78895_b > 10;
        }
    }

    static class PieceWeight {

        public Class<? extends StructureNetherBridgePieces.Piece> field_78828_a;
        public final int field_78826_b;
        public int field_78827_c;
        public int field_78824_d;
        public boolean field_78825_e;

        public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> oclass, int i, int j, boolean flag) {
            this.field_78828_a = oclass;
            this.field_78826_b = i;
            this.field_78824_d = j;
            this.field_78825_e = flag;
        }

        public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> oclass, int i, int j) {
            this(oclass, i, j, false);
        }

        public boolean func_78822_a(int i) {
            return this.field_78824_d == 0 || this.field_78827_c < this.field_78824_d;
        }

        public boolean func_78823_a() {
            return this.field_78824_d == 0 || this.field_78827_c < this.field_78824_d;
        }
    }
}
