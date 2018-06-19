package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.WorldGenMineshaftPieces.c;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureMineshaftPieces {

    public static void func_143048_a() {
        MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Corridor.class, "MSCorridor");
        MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Cross.class, "MSCrossing");
        MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Room.class, "MSRoom");
        MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Stairs.class, "MSStairs");
    }

    private static WorldGenMineshaftPieces.c a(List<StructureComponent> list, Random random, int i, int j, int k, @Nullable EnumFacing enumdirection, int l, MapGenMineshaft.Type worldgenmineshaft_type) {
        int i1 = random.nextInt(100);
        StructureBoundingBox structureboundingbox;

        if (i1 >= 80) {
            structureboundingbox = StructureMineshaftPieces.Cross.func_175813_a(list, random, i, j, k, enumdirection);
            if (structureboundingbox != null) {
                return new StructureMineshaftPieces.Cross(l, random, structureboundingbox, enumdirection, worldgenmineshaft_type);
            }
        } else if (i1 >= 70) {
            structureboundingbox = StructureMineshaftPieces.Stairs.func_175812_a(list, random, i, j, k, enumdirection);
            if (structureboundingbox != null) {
                return new StructureMineshaftPieces.Stairs(l, random, structureboundingbox, enumdirection, worldgenmineshaft_type);
            }
        } else {
            structureboundingbox = StructureMineshaftPieces.Corridor.func_175814_a(list, random, i, j, k, enumdirection);
            if (structureboundingbox != null) {
                return new StructureMineshaftPieces.Corridor(l, random, structureboundingbox, enumdirection, worldgenmineshaft_type);
            }
        }

        return null;
    }

    private static WorldGenMineshaftPieces.c b(StructureComponent structurepiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        if (l > 8) {
            return null;
        } else if (Math.abs(i - structurepiece.func_74874_b().field_78897_a) <= 80 && Math.abs(k - structurepiece.func_74874_b().field_78896_c) <= 80) {
            MapGenMineshaft.Type worldgenmineshaft_type = ((WorldGenMineshaftPieces.c) structurepiece).a;
            WorldGenMineshaftPieces.c worldgenmineshaftpieces_c = a(list, random, i, j, k, enumdirection, l + 1, worldgenmineshaft_type);

            if (worldgenmineshaftpieces_c != null) {
                list.add(worldgenmineshaftpieces_c);
                worldgenmineshaftpieces_c.func_74861_a(structurepiece, list, random);
            }

            return worldgenmineshaftpieces_c;
        } else {
            return null;
        }
    }

    public static class Stairs extends WorldGenMineshaftPieces.c {

        public Stairs() {}

        public Stairs(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i, worldgenmineshaft_type);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public static StructureBoundingBox func_175812_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j - 5, k, i, j + 2, k);

            switch (enumdirection) {
            case NORTH:
            default:
                structureboundingbox.field_78893_d = i + 2;
                structureboundingbox.field_78896_c = k - 8;
                break;

            case SOUTH:
                structureboundingbox.field_78893_d = i + 2;
                structureboundingbox.field_78892_f = k + 8;
                break;

            case WEST:
                structureboundingbox.field_78897_a = i - 8;
                structureboundingbox.field_78892_f = k + 2;
                break;

            case EAST:
                structureboundingbox.field_78893_d = i + 8;
                structureboundingbox.field_78892_f = k + 2;
            }

            return StructureComponent.func_74883_a(list, structureboundingbox) != null ? null : structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = this.func_74877_c();
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i);
                    break;

                case SOUTH:
                    StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i);
                    break;

                case WEST:
                    StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c, EnumFacing.WEST, i);
                    break;

                case EAST:
                    StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c, EnumFacing.EAST, i);
                }
            }

        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_175804_a(world, structureboundingbox, 0, 5, 0, 2, 7, 1, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 0, 0, 7, 2, 2, 8, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);

                for (int i = 0; i < 5; ++i) {
                    this.func_175804_a(world, structureboundingbox, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                return true;
            }
        }
    }

    public static class Cross extends WorldGenMineshaftPieces.c {

        private EnumFacing field_74953_a;
        private boolean field_74952_b;

        public Cross() {}

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("tf", this.field_74952_b);
            nbttagcompound.func_74768_a("D", this.field_74953_a.func_176736_b());
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74952_b = nbttagcompound.func_74767_n("tf");
            this.field_74953_a = EnumFacing.func_176731_b(nbttagcompound.func_74762_e("D"));
        }

        public Cross(int i, Random random, StructureBoundingBox structureboundingbox, @Nullable EnumFacing enumdirection, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i, worldgenmineshaft_type);
            this.field_74953_a = enumdirection;
            this.field_74887_e = structureboundingbox;
            this.field_74952_b = structureboundingbox.func_78882_c() > 3;
        }

        public static StructureBoundingBox func_175813_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j, k, i, j + 2, k);

            if (random.nextInt(4) == 0) {
                structureboundingbox.field_78894_e += 4;
            }

            switch (enumdirection) {
            case NORTH:
            default:
                structureboundingbox.field_78897_a = i - 1;
                structureboundingbox.field_78893_d = i + 3;
                structureboundingbox.field_78896_c = k - 4;
                break;

            case SOUTH:
                structureboundingbox.field_78897_a = i - 1;
                structureboundingbox.field_78893_d = i + 3;
                structureboundingbox.field_78892_f = k + 3 + 1;
                break;

            case WEST:
                structureboundingbox.field_78897_a = i - 4;
                structureboundingbox.field_78896_c = k - 1;
                structureboundingbox.field_78892_f = k + 3;
                break;

            case EAST:
                structureboundingbox.field_78893_d = i + 3 + 1;
                structureboundingbox.field_78896_c = k - 1;
                structureboundingbox.field_78892_f = k + 3;
            }

            return StructureComponent.func_74883_a(list, structureboundingbox) != null ? null : structureboundingbox;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = this.func_74877_c();

            switch (this.field_74953_a) {
            case NORTH:
            default:
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, EnumFacing.WEST, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, EnumFacing.EAST, i);
                break;

            case SOUTH:
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, EnumFacing.WEST, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, EnumFacing.EAST, i);
                break;

            case WEST:
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, EnumFacing.WEST, i);
                break;

            case EAST:
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i);
                StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, EnumFacing.EAST, i);
            }

            if (this.field_74952_b) {
                if (random.nextBoolean()) {
                    StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b + 3 + 1, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i);
                }

                if (random.nextBoolean()) {
                    StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + 3 + 1, this.field_74887_e.field_78896_c + 1, EnumFacing.WEST, i);
                }

                if (random.nextBoolean()) {
                    StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + 3 + 1, this.field_74887_e.field_78896_c + 1, EnumFacing.EAST, i);
                }

                if (random.nextBoolean()) {
                    StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b + 3 + 1, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i);
                }
            }

        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                IBlockState iblockdata = this.G_();

                if (this.field_74952_b) {
                    this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c, this.field_74887_e.field_78893_d - 1, this.field_74887_e.field_78895_b + 3 - 1, this.field_74887_e.field_78892_f, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, this.field_74887_e.field_78893_d, this.field_74887_e.field_78895_b + 3 - 1, this.field_74887_e.field_78892_f - 1, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78894_e - 2, this.field_74887_e.field_78896_c, this.field_74887_e.field_78893_d - 1, this.field_74887_e.field_78894_e, this.field_74887_e.field_78892_f, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a, this.field_74887_e.field_78894_e - 2, this.field_74887_e.field_78896_c + 1, this.field_74887_e.field_78893_d, this.field_74887_e.field_78894_e, this.field_74887_e.field_78892_f - 1, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b + 3, this.field_74887_e.field_78896_c + 1, this.field_74887_e.field_78893_d - 1, this.field_74887_e.field_78895_b + 3, this.field_74887_e.field_78892_f - 1, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                } else {
                    this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c, this.field_74887_e.field_78893_d - 1, this.field_74887_e.field_78894_e, this.field_74887_e.field_78892_f, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                    this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, this.field_74887_e.field_78893_d, this.field_74887_e.field_78894_e, this.field_74887_e.field_78892_f - 1, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                this.func_189923_b(world, structureboundingbox, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, this.field_74887_e.field_78894_e);
                this.func_189923_b(world, structureboundingbox, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f - 1, this.field_74887_e.field_78894_e);
                this.func_189923_b(world, structureboundingbox, this.field_74887_e.field_78893_d - 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c + 1, this.field_74887_e.field_78894_e);
                this.func_189923_b(world, structureboundingbox, this.field_74887_e.field_78893_d - 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f - 1, this.field_74887_e.field_78894_e);

                for (int i = this.field_74887_e.field_78897_a; i <= this.field_74887_e.field_78893_d; ++i) {
                    for (int j = this.field_74887_e.field_78896_c; j <= this.field_74887_e.field_78892_f; ++j) {
                        if (this.func_175807_a(world, i, this.field_74887_e.field_78895_b - 1, j, structureboundingbox).func_185904_a() == Material.field_151579_a && this.func_189916_b(world, i, this.field_74887_e.field_78895_b - 1, j, structureboundingbox) < 8) {
                            this.func_175811_a(world, iblockdata, i, this.field_74887_e.field_78895_b - 1, j, structureboundingbox);
                        }
                    }
                }

                return true;
            }
        }

        private void func_189923_b(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
            if (this.func_175807_a(world, i, l + 1, k, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                this.func_175804_a(world, structureboundingbox, i, j, k, i, l, k, this.G_(), Blocks.field_150350_a.func_176223_P(), false);
            }

        }
    }

    public static class Corridor extends WorldGenMineshaftPieces.c {

        private boolean field_74958_a;
        private boolean field_74956_b;
        private boolean field_74957_c;
        private int field_74955_d;

        public Corridor() {}

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("hr", this.field_74958_a);
            nbttagcompound.func_74757_a("sc", this.field_74956_b);
            nbttagcompound.func_74757_a("hps", this.field_74957_c);
            nbttagcompound.func_74768_a("Num", this.field_74955_d);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74958_a = nbttagcompound.func_74767_n("hr");
            this.field_74956_b = nbttagcompound.func_74767_n("sc");
            this.field_74957_c = nbttagcompound.func_74767_n("hps");
            this.field_74955_d = nbttagcompound.func_74762_e("Num");
        }

        public Corridor(int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i, worldgenmineshaft_type);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_74958_a = random.nextInt(3) == 0;
            this.field_74956_b = !this.field_74958_a && random.nextInt(23) == 0;
            if (this.func_186165_e().func_176740_k() == EnumFacing.Axis.Z) {
                this.field_74955_d = structureboundingbox.func_78880_d() / 5;
            } else {
                this.field_74955_d = structureboundingbox.func_78883_b() / 5;
            }

        }

        public static StructureBoundingBox func_175814_a(List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(i, j, k, i, j + 2, k);

            int l;

            for (l = random.nextInt(3) + 2; l > 0; --l) {
                int i1 = l * 5;

                switch (enumdirection) {
                case NORTH:
                default:
                    structureboundingbox.field_78893_d = i + 2;
                    structureboundingbox.field_78896_c = k - (i1 - 1);
                    break;

                case SOUTH:
                    structureboundingbox.field_78893_d = i + 2;
                    structureboundingbox.field_78892_f = k + (i1 - 1);
                    break;

                case WEST:
                    structureboundingbox.field_78897_a = i - (i1 - 1);
                    structureboundingbox.field_78892_f = k + 2;
                    break;

                case EAST:
                    structureboundingbox.field_78893_d = i + (i1 - 1);
                    structureboundingbox.field_78892_f = k + 2;
                }

                if (StructureComponent.func_74883_a(list, structureboundingbox) == null) {
                    break;
                }
            }

            return l > 0 ? structureboundingbox : null;
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = this.func_74877_c();
            int j = random.nextInt(4);
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    if (j <= 1) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78896_c - 1, enumdirection, i);
                    } else if (j == 2) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78896_c, EnumFacing.WEST, i);
                    } else {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78896_c, EnumFacing.EAST, i);
                    }
                    break;

                case SOUTH:
                    if (j <= 1) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78892_f + 1, enumdirection, i);
                    } else if (j == 2) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78892_f - 3, EnumFacing.WEST, i);
                    } else {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78892_f - 3, EnumFacing.EAST, i);
                    }
                    break;

                case WEST:
                    if (j <= 1) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78896_c, enumdirection, i);
                    } else if (j == 2) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i);
                    } else {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i);
                    }
                    break;

                case EAST:
                    if (j <= 1) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78896_c, enumdirection, i);
                    } else if (j == 2) {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d - 3, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i);
                    } else {
                        StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d - 3, this.field_74887_e.field_78895_b - 1 + random.nextInt(3), this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i);
                    }
                }
            }

            if (i < 8) {
                int k;
                int l;

                if (enumdirection != EnumFacing.NORTH && enumdirection != EnumFacing.SOUTH) {
                    for (k = this.field_74887_e.field_78897_a + 3; k + 3 <= this.field_74887_e.field_78893_d; k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            StructureMineshaftPieces.b(structurepiece, list, random, k, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i + 1);
                        } else if (l == 1) {
                            StructureMineshaftPieces.b(structurepiece, list, random, k, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i + 1);
                        }
                    }
                } else {
                    for (k = this.field_74887_e.field_78896_c + 3; k + 3 <= this.field_74887_e.field_78892_f; k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b, k, EnumFacing.WEST, i + 1);
                        } else if (l == 1) {
                            StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b, k, EnumFacing.EAST, i + 1);
                        }
                    }
                }
            }

        }

        protected boolean func_186167_a(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, ResourceLocation minecraftkey) {
            BlockPos blockposition = new BlockPos(this.func_74865_a(i, k), this.func_74862_a(j), this.func_74873_b(i, k));

            if (structureboundingbox.func_175898_b((Vec3i) blockposition) && world.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a && world.func_180495_p(blockposition.func_177977_b()).func_185904_a() != Material.field_151579_a) {
                IBlockState iblockdata = Blocks.field_150448_aq.func_176223_P().func_177226_a(BlockRail.field_176565_b, random.nextBoolean() ? BlockRailBase.EnumRailDirection.NORTH_SOUTH : BlockRailBase.EnumRailDirection.EAST_WEST);

                this.func_175811_a(world, iblockdata, i, j, k, structureboundingbox);
                EntityMinecartChest entityminecartchest = new EntityMinecartChest(world, (double) ((float) blockposition.func_177958_n() + 0.5F), (double) ((float) blockposition.func_177956_o() + 0.5F), (double) ((float) blockposition.func_177952_p() + 0.5F));

                entityminecartchest.func_184289_a(minecraftkey, random.nextLong());
                world.func_72838_d(entityminecartchest);
                return true;
            } else {
                return false;
            }
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                boolean flag = false;
                boolean flag1 = true;
                boolean flag2 = false;
                boolean flag3 = true;
                int i = this.field_74955_d * 5 - 1;
                IBlockState iblockdata = this.G_();

                this.func_175804_a(world, structureboundingbox, 0, 0, 0, 2, 1, i, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                this.func_189914_a(world, structureboundingbox, random, 0.8F, 0, 2, 0, 2, 2, i, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false, 0);
                if (this.field_74956_b) {
                    this.func_189914_a(world, structureboundingbox, random, 0.6F, 0, 0, 0, 2, 1, i, Blocks.field_150321_G.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false, 8);
                }

                int j;
                int k;

                for (j = 0; j < this.field_74955_d; ++j) {
                    k = 2 + j * 5;
                    this.func_189921_a(world, structureboundingbox, 0, 0, k, 2, 2, random);
                    this.func_189922_a(world, structureboundingbox, random, 0.1F, 0, 2, k - 1);
                    this.func_189922_a(world, structureboundingbox, random, 0.1F, 2, 2, k - 1);
                    this.func_189922_a(world, structureboundingbox, random, 0.1F, 0, 2, k + 1);
                    this.func_189922_a(world, structureboundingbox, random, 0.1F, 2, 2, k + 1);
                    this.func_189922_a(world, structureboundingbox, random, 0.05F, 0, 2, k - 2);
                    this.func_189922_a(world, structureboundingbox, random, 0.05F, 2, 2, k - 2);
                    this.func_189922_a(world, structureboundingbox, random, 0.05F, 0, 2, k + 2);
                    this.func_189922_a(world, structureboundingbox, random, 0.05F, 2, 2, k + 2);
                    if (random.nextInt(100) == 0) {
                        this.func_186167_a(world, structureboundingbox, random, 2, 0, k - 1, LootTableList.field_186424_f);
                    }

                    if (random.nextInt(100) == 0) {
                        this.func_186167_a(world, structureboundingbox, random, 0, 0, k + 1, LootTableList.field_186424_f);
                    }

                    if (this.field_74956_b && !this.field_74957_c) {
                        int l = this.func_74862_a(0);
                        int i1 = k - 1 + random.nextInt(3);
                        int j1 = this.func_74865_a(1, i1);
                        int k1 = this.func_74873_b(1, i1);
                        BlockPos blockposition = new BlockPos(j1, l, k1);

                        if (structureboundingbox.func_175898_b((Vec3i) blockposition) && this.func_189916_b(world, 1, 0, i1, structureboundingbox) < 8) {
                            this.field_74957_c = true;
                            world.func_180501_a(blockposition, Blocks.field_150474_ac.func_176223_P(), 2);
                            TileEntity tileentity = world.func_175625_s(blockposition);

                            if (tileentity instanceof TileEntityMobSpawner) {
                                ((TileEntityMobSpawner) tileentity).func_145881_a().func_190894_a(EntityList.func_191306_a(EntityCaveSpider.class));
                            }
                        }
                    }
                }

                for (j = 0; j <= 2; ++j) {
                    for (k = 0; k <= i; ++k) {
                        boolean flag4 = true;
                        IBlockState iblockdata1 = this.func_175807_a(world, j, -1, k, structureboundingbox);

                        if (iblockdata1.func_185904_a() == Material.field_151579_a && this.func_189916_b(world, j, -1, k, structureboundingbox) < 8) {
                            boolean flag5 = true;

                            this.func_175811_a(world, iblockdata, j, -1, k, structureboundingbox);
                        }
                    }
                }

                if (this.field_74958_a) {
                    IBlockState iblockdata2 = Blocks.field_150448_aq.func_176223_P().func_177226_a(BlockRail.field_176565_b, BlockRailBase.EnumRailDirection.NORTH_SOUTH);

                    for (k = 0; k <= i; ++k) {
                        IBlockState iblockdata3 = this.func_175807_a(world, 1, -1, k, structureboundingbox);

                        if (iblockdata3.func_185904_a() != Material.field_151579_a && iblockdata3.func_185913_b()) {
                            float f = this.func_189916_b(world, 1, 0, k, structureboundingbox) > 8 ? 0.9F : 0.7F;

                            this.func_175809_a(world, structureboundingbox, random, f, 1, 0, k, iblockdata2);
                        }
                    }
                }

                return true;
            }
        }

        private void func_189921_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, Random random) {
            if (this.a(world, structureboundingbox, i, i1, l, k)) {
                IBlockState iblockdata = this.G_();
                IBlockState iblockdata1 = this.b();
                IBlockState iblockdata2 = Blocks.field_150350_a.func_176223_P();

                this.func_175804_a(world, structureboundingbox, i, j, k, i, l - 1, k, iblockdata1, iblockdata2, false);
                this.func_175804_a(world, structureboundingbox, i1, j, k, i1, l - 1, k, iblockdata1, iblockdata2, false);
                if (random.nextInt(4) == 0) {
                    this.func_175804_a(world, structureboundingbox, i, l, k, i, l, k, iblockdata, iblockdata2, false);
                    this.func_175804_a(world, structureboundingbox, i1, l, k, i1, l, k, iblockdata, iblockdata2, false);
                } else {
                    this.func_175804_a(world, structureboundingbox, i, l, k, i1, l, k, iblockdata, iblockdata2, false);
                    this.func_175809_a(world, structureboundingbox, random, 0.05F, i + 1, l, k - 1, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.NORTH));
                    this.func_175809_a(world, structureboundingbox, random, 0.05F, i + 1, l, k + 1, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, EnumFacing.SOUTH));
                }

            }
        }

        private void func_189922_a(World world, StructureBoundingBox structureboundingbox, Random random, float f, int i, int j, int k) {
            if (this.func_189916_b(world, i, j, k, structureboundingbox) < 8) {
                this.func_175809_a(world, structureboundingbox, random, f, i, j, k, Blocks.field_150321_G.func_176223_P());
            }

        }
    }

    public static class Room extends WorldGenMineshaftPieces.c {

        private final List<StructureBoundingBox> field_74949_a = Lists.newLinkedList();

        public Room() {}

        public Room(int i, Random random, int j, int k, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i, worldgenmineshaft_type);
            this.a = worldgenmineshaft_type;
            this.field_74887_e = new StructureBoundingBox(j, 50, k, j + 7 + random.nextInt(6), 54 + random.nextInt(6), k + 7 + random.nextInt(6));
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            int i = this.func_74877_c();
            int j = this.field_74887_e.func_78882_c() - 3 - 1;

            if (j <= 0) {
                j = 1;
            }

            int k;
            WorldGenMineshaftPieces.c worldgenmineshaftpieces_c;
            StructureBoundingBox structureboundingbox;

            for (k = 0; k < this.field_74887_e.func_78883_b(); k += 4) {
                k += random.nextInt(this.field_74887_e.func_78883_b());
                if (k + 3 > this.field_74887_e.func_78883_b()) {
                    break;
                }

                worldgenmineshaftpieces_c = StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + k, this.field_74887_e.field_78895_b + random.nextInt(j) + 1, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, i);
                if (worldgenmineshaftpieces_c != null) {
                    structureboundingbox = worldgenmineshaftpieces_c.func_74874_b();
                    this.field_74949_a.add(new StructureBoundingBox(structureboundingbox.field_78897_a, structureboundingbox.field_78895_b, this.field_74887_e.field_78896_c, structureboundingbox.field_78893_d, structureboundingbox.field_78894_e, this.field_74887_e.field_78896_c + 1));
                }
            }

            for (k = 0; k < this.field_74887_e.func_78883_b(); k += 4) {
                k += random.nextInt(this.field_74887_e.func_78883_b());
                if (k + 3 > this.field_74887_e.func_78883_b()) {
                    break;
                }

                worldgenmineshaftpieces_c = StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a + k, this.field_74887_e.field_78895_b + random.nextInt(j) + 1, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, i);
                if (worldgenmineshaftpieces_c != null) {
                    structureboundingbox = worldgenmineshaftpieces_c.func_74874_b();
                    this.field_74949_a.add(new StructureBoundingBox(structureboundingbox.field_78897_a, structureboundingbox.field_78895_b, this.field_74887_e.field_78892_f - 1, structureboundingbox.field_78893_d, structureboundingbox.field_78894_e, this.field_74887_e.field_78892_f));
                }
            }

            for (k = 0; k < this.field_74887_e.func_78880_d(); k += 4) {
                k += random.nextInt(this.field_74887_e.func_78880_d());
                if (k + 3 > this.field_74887_e.func_78880_d()) {
                    break;
                }

                worldgenmineshaftpieces_c = StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + random.nextInt(j) + 1, this.field_74887_e.field_78896_c + k, EnumFacing.WEST, i);
                if (worldgenmineshaftpieces_c != null) {
                    structureboundingbox = worldgenmineshaftpieces_c.func_74874_b();
                    this.field_74949_a.add(new StructureBoundingBox(this.field_74887_e.field_78897_a, structureboundingbox.field_78895_b, structureboundingbox.field_78896_c, this.field_74887_e.field_78897_a + 1, structureboundingbox.field_78894_e, structureboundingbox.field_78892_f));
                }
            }

            for (k = 0; k < this.field_74887_e.func_78880_d(); k += 4) {
                k += random.nextInt(this.field_74887_e.func_78880_d());
                if (k + 3 > this.field_74887_e.func_78880_d()) {
                    break;
                }

                worldgenmineshaftpieces_c = StructureMineshaftPieces.b(structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + random.nextInt(j) + 1, this.field_74887_e.field_78896_c + k, EnumFacing.EAST, i);
                if (worldgenmineshaftpieces_c != null) {
                    structureboundingbox = worldgenmineshaftpieces_c.func_74874_b();
                    this.field_74949_a.add(new StructureBoundingBox(this.field_74887_e.field_78893_d - 1, structureboundingbox.field_78895_b, structureboundingbox.field_78896_c, this.field_74887_e.field_78893_d, structureboundingbox.field_78894_e, structureboundingbox.field_78892_f));
                }
            }

        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.func_74860_a(world, structureboundingbox)) {
                return false;
            } else {
                this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c, this.field_74887_e.field_78893_d, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f, Blocks.field_150346_d.func_176223_P(), Blocks.field_150350_a.func_176223_P(), true);
                this.func_175804_a(world, structureboundingbox, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b + 1, this.field_74887_e.field_78896_c, this.field_74887_e.field_78893_d, Math.min(this.field_74887_e.field_78895_b + 3, this.field_74887_e.field_78894_e), this.field_74887_e.field_78892_f, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                Iterator iterator = this.field_74949_a.iterator();

                while (iterator.hasNext()) {
                    StructureBoundingBox structureboundingbox1 = (StructureBoundingBox) iterator.next();

                    this.func_175804_a(world, structureboundingbox, structureboundingbox1.field_78897_a, structureboundingbox1.field_78894_e - 2, structureboundingbox1.field_78896_c, structureboundingbox1.field_78893_d, structureboundingbox1.field_78894_e, structureboundingbox1.field_78892_f, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
                }

                this.func_180777_a(world, structureboundingbox, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b + 4, this.field_74887_e.field_78896_c, this.field_74887_e.field_78893_d, this.field_74887_e.field_78894_e, this.field_74887_e.field_78892_f, Blocks.field_150350_a.func_176223_P(), false);
                return true;
            }
        }

        public void func_181138_a(int i, int j, int k) {
            super.func_181138_a(i, j, k);
            Iterator iterator = this.field_74949_a.iterator();

            while (iterator.hasNext()) {
                StructureBoundingBox structureboundingbox = (StructureBoundingBox) iterator.next();

                structureboundingbox.func_78886_a(i, j, k);
            }

        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.field_74949_a.iterator();

            while (iterator.hasNext()) {
                StructureBoundingBox structureboundingbox = (StructureBoundingBox) iterator.next();

                nbttaglist.func_74742_a(structureboundingbox.func_151535_h());
            }

            nbttagcompound.func_74782_a("Entrances", nbttaglist);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("Entrances", 11);

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                this.field_74949_a.add(new StructureBoundingBox(nbttaglist.func_150306_c(i)));
            }

        }
    }

    abstract static class c extends StructureComponent {

        protected MapGenMineshaft.Type a;

        public c() {}

        public c(int i, MapGenMineshaft.Type worldgenmineshaft_type) {
            super(i);
            this.a = worldgenmineshaft_type;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            nbttagcompound.func_74768_a("MST", this.a.ordinal());
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            this.a = MapGenMineshaft.Type.func_189910_a(nbttagcompound.func_74762_e("MST"));
        }

        protected IBlockState G_() {
            switch (this.a) {
            case NORMAL:
            default:
                return Blocks.field_150344_f.func_176223_P();

            case MESA:
                return Blocks.field_150344_f.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.DARK_OAK);
            }
        }

        protected IBlockState b() {
            switch (this.a) {
            case NORMAL:
            default:
                return Blocks.field_180407_aO.func_176223_P();

            case MESA:
                return Blocks.field_180406_aS.func_176223_P();
            }
        }

        protected boolean a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
            for (int i1 = i; i1 <= j; ++i1) {
                if (this.func_175807_a(world, i1, k + 1, l, structureboundingbox).func_185904_a() == Material.field_151579_a) {
                    return false;
                }
            }

            return true;
        }
    }
}
