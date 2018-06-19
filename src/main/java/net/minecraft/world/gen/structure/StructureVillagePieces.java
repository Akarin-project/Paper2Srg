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

    public static void func_143016_a() {
        MapGenStructureIO.func_143031_a(StructureVillagePieces.House1.class, "ViBH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Field1.class, "ViDF");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Field2.class, "ViF");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Torch.class, "ViL");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Hall.class, "ViPH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.House4Garden.class, "ViSH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.WoodHut.class, "ViSmH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Church.class, "ViST");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.House2.class, "ViS");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Start.class, "ViStart");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Path.class, "ViSR");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.House3.class, "ViTRH");
        MapGenStructureIO.func_143031_a(StructureVillagePieces.Well.class, "ViW");
    }

    public static List<StructureVillagePieces.PieceWeight> func_75084_a(Random random, int i) {
        ArrayList arraylist = Lists.newArrayList();

        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, 4, MathHelper.func_76136_a(random, 2 + i, 4 + i * 2)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20, MathHelper.func_76136_a(random, 0 + i, 1 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, 20, MathHelper.func_76136_a(random, 0 + i, 2 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, 3, MathHelper.func_76136_a(random, 2 + i, 5 + i * 3)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, 15, MathHelper.func_76136_a(random, 0 + i, 2 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, 3, MathHelper.func_76136_a(random, 1 + i, 4 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, 3, MathHelper.func_76136_a(random, 2 + i, 4 + i * 2)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, 15, MathHelper.func_76136_a(random, 0, 1 + i)));
        arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, 8, MathHelper.func_76136_a(random, 0 + i, 3 + i * 2)));
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            if (((StructureVillagePieces.PieceWeight) iterator.next()).field_75087_d == 0) {
                iterator.remove();
            }
        }

        return arraylist;
    }

    private static int func_75079_a(List<StructureVillagePieces.PieceWeight> list) {
        boolean flag = false;
        int i = 0;

        StructureVillagePieces.PieceWeight worldgenvillagepieces_worldgenvillagepieceweight;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); i += worldgenvillagepieces_worldgenvillagepieceweight.field_75088_b) {
            worldgenvillagepieces_worldgenvillagepieceweight = (StructureVillagePieces.PieceWeight) iterator.next();
            if (worldgenvillagepieces_worldgenvillagepieceweight.field_75087_d > 0 && worldgenvillagepieces_worldgenvillagepieceweight.field_75089_c < worldgenvillagepieces_worldgenvillagepieceweight.field_75087_d) {
                flag = true;
            }
        }

        return flag ? i : -1;
    }

    private static StructureVillagePieces.Village func_176065_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, StructureVillagePieces.PieceWeight worldgenvillagepieces_worldgenvillagepieceweight, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        Class oclass = worldgenvillagepieces_worldgenvillagepieceweight.field_75090_a;
        Object object = null;

        if (oclass == StructureVillagePieces.House4Garden.class) {
            object = StructureVillagePieces.House4Garden.func_175858_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.Church.class) {
            object = StructureVillagePieces.Church.func_175854_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.House1.class) {
            object = StructureVillagePieces.House1.func_175850_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.WoodHut.class) {
            object = StructureVillagePieces.WoodHut.func_175853_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.Hall.class) {
            object = StructureVillagePieces.Hall.func_175857_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.Field1.class) {
            object = StructureVillagePieces.Field1.func_175851_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.Field2.class) {
            object = StructureVillagePieces.Field2.func_175852_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.House2.class) {
            object = StructureVillagePieces.House2.func_175855_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        } else if (oclass == StructureVillagePieces.House3.class) {
            object = StructureVillagePieces.House3.func_175849_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l);
        }

        return (StructureVillagePieces.Village) object;
    }

    private static StructureVillagePieces.Village func_176067_c(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        int i1 = func_75079_a(worldgenvillagepieces_worldgenvillagestartpiece.field_74931_h);

        if (i1 <= 0) {
            return null;
        } else {
            int j1 = 0;

            while (j1 < 5) {
                ++j1;
                int k1 = random.nextInt(i1);
                Iterator iterator = worldgenvillagepieces_worldgenvillagestartpiece.field_74931_h.iterator();

                while (iterator.hasNext()) {
                    StructureVillagePieces.PieceWeight worldgenvillagepieces_worldgenvillagepieceweight = (StructureVillagePieces.PieceWeight) iterator.next();

                    k1 -= worldgenvillagepieces_worldgenvillagepieceweight.field_75088_b;
                    if (k1 < 0) {
                        if (!worldgenvillagepieces_worldgenvillagepieceweight.func_75085_a(l) || worldgenvillagepieces_worldgenvillagepieceweight == worldgenvillagepieces_worldgenvillagestartpiece.field_74926_d && worldgenvillagepieces_worldgenvillagestartpiece.field_74931_h.size() > 1) {
                            break;
                        }

                        StructureVillagePieces.Village worldgenvillagepieces_worldgenvillagepiece = func_176065_a(worldgenvillagepieces_worldgenvillagestartpiece, worldgenvillagepieces_worldgenvillagepieceweight, list, random, i, j, k, enumdirection, l);

                        if (worldgenvillagepieces_worldgenvillagepiece != null) {
                            ++worldgenvillagepieces_worldgenvillagepieceweight.field_75089_c;
                            worldgenvillagepieces_worldgenvillagestartpiece.field_74926_d = worldgenvillagepieces_worldgenvillagepieceweight;
                            if (!worldgenvillagepieces_worldgenvillagepieceweight.func_75086_a()) {
                                worldgenvillagepieces_worldgenvillagestartpiece.field_74931_h.remove(worldgenvillagepieces_worldgenvillagepieceweight);
                            }

                            return worldgenvillagepieces_worldgenvillagepiece;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = StructureVillagePieces.Torch.func_175856_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection);

            if (structureboundingbox != null) {
                return new StructureVillagePieces.Torch(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection);
            } else {
                return null;
            }
        }
    }

    private static StructureComponent func_176066_d(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        if (l > 50) {
            return null;
        } else if (Math.abs(i - worldgenvillagepieces_worldgenvillagestartpiece.func_74874_b().field_78897_a) <= 112 && Math.abs(k - worldgenvillagepieces_worldgenvillagestartpiece.func_74874_b().field_78896_c) <= 112) {
            StructureVillagePieces.Village worldgenvillagepieces_worldgenvillagepiece = func_176067_c(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection, l + 1);

            if (worldgenvillagepieces_worldgenvillagepiece != null) {
                list.add(worldgenvillagepieces_worldgenvillagepiece);
                worldgenvillagepieces_worldgenvillagestartpiece.field_74932_i.add(worldgenvillagepieces_worldgenvillagepiece);
                return worldgenvillagepieces_worldgenvillagepiece;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static StructureComponent func_176069_e(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
        if (l > 3 + worldgenvillagepieces_worldgenvillagestartpiece.field_74928_c) {
            return null;
        } else if (Math.abs(i - worldgenvillagepieces_worldgenvillagestartpiece.func_74874_b().field_78897_a) <= 112 && Math.abs(k - worldgenvillagepieces_worldgenvillagestartpiece.func_74874_b().field_78896_c) <= 112) {
            StructureBoundingBox structureboundingbox = StructureVillagePieces.Path.func_175848_a(worldgenvillagepieces_worldgenvillagestartpiece, list, random, i, j, k, enumdirection);

            if (structureboundingbox != null && structureboundingbox.field_78895_b > 10) {
                StructureVillagePieces.Path worldgenvillagepieces_worldgenvillageroad = new StructureVillagePieces.Path(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection);

                list.add(worldgenvillagepieces_worldgenvillageroad);
                worldgenvillagepieces_worldgenvillagestartpiece.field_74930_j.add(worldgenvillagepieces_worldgenvillageroad);
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
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public static StructureBoundingBox func_175856_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 3, 4, 2, enumdirection);

            return StructureComponent.func_74883_a(list, structureboundingbox) != null ? null : structureboundingbox;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 4 - 1, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_180407_aO.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 2, 3, 1, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175811_a(world, iblockdata, 1, 0, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 1, 1, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 1, 2, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150325_L.func_176203_a(EnumDyeColor.WHITE.func_176767_b()), 1, 3, 0, structureboundingbox);
            this.func_189926_a(world, EnumFacing.EAST, 2, 3, 0, structureboundingbox);
            this.func_189926_a(world, EnumFacing.NORTH, 1, 3, 1, structureboundingbox);
            this.func_189926_a(world, EnumFacing.WEST, 0, 3, 0, structureboundingbox);
            this.func_189926_a(world, EnumFacing.SOUTH, 1, 3, -1, structureboundingbox);
            return true;
        }
    }

    public static class Field1 extends StructureVillagePieces.Village {

        private Block field_82679_b;
        private Block field_82680_c;
        private Block field_82678_d;
        private Block field_82681_h;

        public Field1() {}

        public Field1(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_82679_b = this.func_151559_a(random);
            this.field_82680_c = this.func_151559_a(random);
            this.field_82678_d = this.func_151559_a(random);
            this.field_82681_h = this.func_151559_a(random);
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74768_a("CA", Block.field_149771_c.func_148757_b(this.field_82679_b));
            nbttagcompound.func_74768_a("CB", Block.field_149771_c.func_148757_b(this.field_82680_c));
            nbttagcompound.func_74768_a("CC", Block.field_149771_c.func_148757_b(this.field_82678_d));
            nbttagcompound.func_74768_a("CD", Block.field_149771_c.func_148757_b(this.field_82681_h));
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_82679_b = Block.func_149729_e(nbttagcompound.func_74762_e("CA"));
            this.field_82680_c = Block.func_149729_e(nbttagcompound.func_74762_e("CB"));
            this.field_82678_d = Block.func_149729_e(nbttagcompound.func_74762_e("CC"));
            this.field_82681_h = Block.func_149729_e(nbttagcompound.func_74762_e("CD"));
            if (!(this.field_82679_b instanceof BlockCrops)) {
                this.field_82679_b = Blocks.field_150464_aj;
            }

            if (!(this.field_82680_c instanceof BlockCrops)) {
                this.field_82680_c = Blocks.field_150459_bM;
            }

            if (!(this.field_82678_d instanceof BlockCrops)) {
                this.field_82678_d = Blocks.field_150469_bN;
            }

            if (!(this.field_82681_h instanceof BlockCrops)) {
                this.field_82681_h = Blocks.field_185773_cZ;
            }

        }

        private Block func_151559_a(Random random) {
            switch (random.nextInt(10)) {
            case 0:
            case 1:
                return Blocks.field_150459_bM;

            case 2:
            case 3:
                return Blocks.field_150469_bN;

            case 4:
                return Blocks.field_185773_cZ;

            default:
                return Blocks.field_150464_aj;
            }
        }

        public static StructureVillagePieces.Field1 func_175851_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 13, 4, 9, enumdirection);

            return func_74895_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureVillagePieces.Field1(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 4 - 1, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_150364_r.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 12, 4, 8, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 1, 2, 0, 7, Blocks.field_150458_ak.func_176223_P(), Blocks.field_150458_ak.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 0, 1, 5, 0, 7, Blocks.field_150458_ak.func_176223_P(), Blocks.field_150458_ak.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 7, 0, 1, 8, 0, 7, Blocks.field_150458_ak.func_176223_P(), Blocks.field_150458_ak.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 10, 0, 1, 11, 0, 7, Blocks.field_150458_ak.func_176223_P(), Blocks.field_150458_ak.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 0, 0, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 6, 0, 0, 6, 0, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 12, 0, 0, 12, 0, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 0, 11, 0, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 8, 11, 0, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 3, 0, 1, 3, 0, 7, Blocks.field_150355_j.func_176223_P(), Blocks.field_150355_j.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, 0, 1, 9, 0, 7, Blocks.field_150355_j.func_176223_P(), Blocks.field_150355_j.func_176223_P(), false);

            int i;
            int j;

            for (i = 1; i <= 7; ++i) {
                j = ((BlockCrops) this.field_82679_b).func_185526_g();
                int k = j / 3;

                this.func_175811_a(world, this.field_82679_b.func_176203_a(MathHelper.func_76136_a(random, k, j)), 1, 1, i, structureboundingbox);
                this.func_175811_a(world, this.field_82679_b.func_176203_a(MathHelper.func_76136_a(random, k, j)), 2, 1, i, structureboundingbox);
                int l = ((BlockCrops) this.field_82680_c).func_185526_g();
                int i1 = l / 3;

                this.func_175811_a(world, this.field_82680_c.func_176203_a(MathHelper.func_76136_a(random, i1, l)), 4, 1, i, structureboundingbox);
                this.func_175811_a(world, this.field_82680_c.func_176203_a(MathHelper.func_76136_a(random, i1, l)), 5, 1, i, structureboundingbox);
                int j1 = ((BlockCrops) this.field_82678_d).func_185526_g();
                int k1 = j1 / 3;

                this.func_175811_a(world, this.field_82678_d.func_176203_a(MathHelper.func_76136_a(random, k1, j1)), 7, 1, i, structureboundingbox);
                this.func_175811_a(world, this.field_82678_d.func_176203_a(MathHelper.func_76136_a(random, k1, j1)), 8, 1, i, structureboundingbox);
                int l1 = ((BlockCrops) this.field_82681_h).func_185526_g();
                int i2 = l1 / 3;

                this.func_175811_a(world, this.field_82681_h.func_176203_a(MathHelper.func_76136_a(random, i2, l1)), 10, 1, i, structureboundingbox);
                this.func_175811_a(world, this.field_82681_h.func_176203_a(MathHelper.func_76136_a(random, i2, l1)), 11, 1, i, structureboundingbox);
            }

            for (i = 0; i < 9; ++i) {
                for (j = 0; j < 13; ++j) {
                    this.func_74871_b(world, j, 4, i, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150346_d.func_176223_P(), j, -1, i, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class Field2 extends StructureVillagePieces.Village {

        private Block field_82675_b;
        private Block field_82676_c;

        public Field2() {}

        public Field2(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_82675_b = this.func_151560_a(random);
            this.field_82676_c = this.func_151560_a(random);
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74768_a("CA", Block.field_149771_c.func_148757_b(this.field_82675_b));
            nbttagcompound.func_74768_a("CB", Block.field_149771_c.func_148757_b(this.field_82676_c));
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_82675_b = Block.func_149729_e(nbttagcompound.func_74762_e("CA"));
            this.field_82676_c = Block.func_149729_e(nbttagcompound.func_74762_e("CB"));
        }

        private Block func_151560_a(Random random) {
            switch (random.nextInt(10)) {
            case 0:
            case 1:
                return Blocks.field_150459_bM;

            case 2:
            case 3:
                return Blocks.field_150469_bN;

            case 4:
                return Blocks.field_185773_cZ;

            default:
                return Blocks.field_150464_aj;
            }
        }

        public static StructureVillagePieces.Field2 func_175852_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 7, 4, 9, enumdirection);

            return func_74895_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureVillagePieces.Field2(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 4 - 1, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_150364_r.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 6, 4, 8, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 1, 2, 0, 7, Blocks.field_150458_ak.func_176223_P(), Blocks.field_150458_ak.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 0, 1, 5, 0, 7, Blocks.field_150458_ak.func_176223_P(), Blocks.field_150458_ak.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 0, 0, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 6, 0, 0, 6, 0, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 0, 5, 0, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 8, 5, 0, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 3, 0, 1, 3, 0, 7, Blocks.field_150355_j.func_176223_P(), Blocks.field_150355_j.func_176223_P(), false);

            int i;
            int j;

            for (i = 1; i <= 7; ++i) {
                j = ((BlockCrops) this.field_82675_b).func_185526_g();
                int k = j / 3;

                this.func_175811_a(world, this.field_82675_b.func_176203_a(MathHelper.func_76136_a(random, k, j)), 1, 1, i, structureboundingbox);
                this.func_175811_a(world, this.field_82675_b.func_176203_a(MathHelper.func_76136_a(random, k, j)), 2, 1, i, structureboundingbox);
                int l = ((BlockCrops) this.field_82676_c).func_185526_g();
                int i1 = l / 3;

                this.func_175811_a(world, this.field_82676_c.func_176203_a(MathHelper.func_76136_a(random, i1, l)), 4, 1, i, structureboundingbox);
                this.func_175811_a(world, this.field_82676_c.func_176203_a(MathHelper.func_76136_a(random, i1, l)), 5, 1, i, structureboundingbox);
            }

            for (i = 0; i < 9; ++i) {
                for (j = 0; j < 7; ++j) {
                    this.func_74871_b(world, j, 4, i, structureboundingbox);
                    this.func_175808_b(world, Blocks.field_150346_d.func_176223_P(), j, -1, i, structureboundingbox);
                }
            }

            return true;
        }
    }

    public static class House2 extends StructureVillagePieces.Village {

        private boolean field_74917_c;

        public House2() {}

        public House2(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public static StructureVillagePieces.House2 func_175855_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 10, 6, 7, enumdirection);

            return func_74895_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureVillagePieces.House2(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Chest", this.field_74917_c);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74917_c = nbttagcompound.func_74767_n("Chest");
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 6 - 1, 0);
            }

            IBlockState iblockdata = Blocks.field_150347_e.func_176223_P();
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.WEST));
            IBlockState iblockdata3 = this.func_175847_a(Blocks.field_150344_f.func_176223_P());
            IBlockState iblockdata4 = this.func_175847_a(Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata5 = this.func_175847_a(Blocks.field_150364_r.func_176223_P());
            IBlockState iblockdata6 = this.func_175847_a(Blocks.field_180407_aO.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 9, 4, 6, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 9, 0, 6, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 4, 0, 9, 4, 6, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 0, 9, 5, 6, Blocks.field_150333_U.func_176223_P(), Blocks.field_150333_U.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 5, 1, 8, 5, 5, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 0, 2, 3, 0, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 0, 4, 0, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 0, 3, 4, 0, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 6, 0, 4, 6, iblockdata5, iblockdata5, false);
            this.func_175811_a(world, iblockdata3, 3, 3, 1, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 3, 1, 2, 3, 3, 2, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 4, 1, 3, 5, 3, 3, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 3, 5, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 6, 5, 3, 6, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 5, 1, 0, 5, 3, 0, iblockdata6, iblockdata6, false);
            this.func_175804_a(world, structureboundingbox, 9, 1, 0, 9, 3, 0, iblockdata6, iblockdata6, false);
            this.func_175804_a(world, structureboundingbox, 6, 1, 4, 9, 4, 6, iblockdata, iblockdata, false);
            this.func_175811_a(world, Blocks.field_150356_k.func_176223_P(), 7, 1, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150356_k.func_176223_P(), 8, 1, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), 9, 2, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150411_aY.func_176223_P(), 9, 2, 4, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 7, 2, 4, 8, 2, 5, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175811_a(world, iblockdata, 6, 1, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150460_al.func_176223_P(), 6, 2, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150460_al.func_176223_P(), 6, 3, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150334_T.func_176223_P(), 8, 1, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 4, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 2, 6, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 2, 6, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 2, 1, 4, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150452_aw.func_176223_P(), 2, 2, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 1, 1, 5, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 2, 1, 5, structureboundingbox);
            this.func_175811_a(world, iblockdata2, 1, 1, 4, structureboundingbox);
            if (!this.field_74917_c && structureboundingbox.func_175898_b((Vec3i) (new BlockPos(this.func_74865_a(5, 5), this.func_74862_a(1), this.func_74873_b(5, 5))))) {
                this.field_74917_c = true;
                this.func_186167_a(world, structureboundingbox, random, 5, 1, 5, LootTableList.field_186423_e);
            }

            int i;

            for (i = 6; i <= 8; ++i) {
                if (this.func_175807_a(world, i, 0, -1, structureboundingbox).func_185904_a() == Material.field_151579_a && this.func_175807_a(world, i, -1, -1, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                    this.func_175811_a(world, iblockdata4, i, 0, -1, structureboundingbox);
                    if (this.func_175807_a(world, i, -1, -1, structureboundingbox).func_177230_c() == Blocks.field_185774_da) {
                        this.func_175811_a(world, Blocks.field_150349_c.func_176223_P(), i, -1, -1, structureboundingbox);
                    }
                }
            }

            for (i = 0; i < 7; ++i) {
                for (int j = 0; j < 10; ++j) {
                    this.func_74871_b(world, j, 6, i, structureboundingbox);
                    this.func_175808_b(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.func_74893_a(world, structureboundingbox, 7, 1, 1, 1);
            return true;
        }

        protected int func_180779_c(int i, int j) {
            return 3;
        }
    }

    public static class House3 extends StructureVillagePieces.Village {

        public House3() {}

        public House3(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public static StructureVillagePieces.House3 func_175849_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 9, 7, 12, enumdirection);

            return func_74895_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureVillagePieces.House3(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 7 - 1, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_150347_e.func_176223_P());
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.SOUTH));
            IBlockState iblockdata3 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.EAST));
            IBlockState iblockdata4 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.WEST));
            IBlockState iblockdata5 = this.func_175847_a(Blocks.field_150344_f.func_176223_P());
            IBlockState iblockdata6 = this.func_175847_a(Blocks.field_150364_r.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 1, 1, 1, 7, 4, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 1, 6, 8, 4, 10, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 0, 5, 8, 0, 10, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 1, 7, 0, 4, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 0, 3, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 8, 0, 0, 8, 3, 10, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 0, 7, 2, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 5, 2, 1, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 2, 0, 6, 2, 3, 10, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 3, 0, 10, 7, 3, 10, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 0, 7, 3, 0, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 5, 2, 3, 5, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 0, 4, 1, 8, 4, 1, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 0, 4, 4, 3, 4, 4, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 2, 8, 5, 3, iblockdata5, iblockdata5, false);
            this.func_175811_a(world, iblockdata5, 0, 4, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 0, 4, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 8, 4, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 8, 4, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 8, 4, 4, structureboundingbox);
            IBlockState iblockdata7 = iblockdata1;
            IBlockState iblockdata8 = iblockdata2;
            IBlockState iblockdata9 = iblockdata4;
            IBlockState iblockdata10 = iblockdata3;

            int i;
            int j;

            for (i = -1; i <= 2; ++i) {
                for (j = 0; j <= 8; ++j) {
                    this.func_175811_a(world, iblockdata7, j, 4 + i, i, structureboundingbox);
                    if ((i > -1 || j <= 1) && (i > 0 || j <= 3) && (i > 1 || j <= 4 || j >= 6)) {
                        this.func_175811_a(world, iblockdata8, j, 4 + i, 5 - i, structureboundingbox);
                    }
                }
            }

            this.func_175804_a(world, structureboundingbox, 3, 4, 5, 3, 4, 10, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 7, 4, 2, 7, 4, 10, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 4, 5, 4, 4, 5, 10, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 6, 5, 4, 6, 5, 10, iblockdata5, iblockdata5, false);
            this.func_175804_a(world, structureboundingbox, 5, 6, 3, 5, 6, 10, iblockdata5, iblockdata5, false);

            for (i = 4; i >= 1; --i) {
                this.func_175811_a(world, iblockdata5, i, 2 + i, 7 - i, structureboundingbox);

                for (j = 8 - i; j <= 10; ++j) {
                    this.func_175811_a(world, iblockdata10, i, 2 + i, j, structureboundingbox);
                }
            }

            this.func_175811_a(world, iblockdata5, 6, 6, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 7, 5, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata4, 6, 6, 4, structureboundingbox);

            for (i = 6; i <= 8; ++i) {
                for (j = 5; j <= 10; ++j) {
                    this.func_175811_a(world, iblockdata9, i, 12 - i, j, structureboundingbox);
                }
            }

            this.func_175811_a(world, iblockdata6, 0, 2, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 0, 2, 4, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 4, 2, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 5, 2, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 6, 2, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 8, 2, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 2, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 8, 2, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 8, 2, 5, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 8, 2, 6, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 2, 7, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 2, 8, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 8, 2, 9, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 2, 2, 6, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 2, 7, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 2, 8, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 2, 2, 9, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 4, 4, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 5, 4, 10, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 6, 4, 10, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 5, 5, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, 1, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, 2, 0, structureboundingbox);
            this.func_189926_a(world, EnumFacing.NORTH, 2, 3, 1, structureboundingbox);
            this.func_189927_a(world, structureboundingbox, random, 2, 1, 0, EnumFacing.NORTH);
            this.func_175804_a(world, structureboundingbox, 1, 0, -1, 3, 2, -1, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            if (this.func_175807_a(world, 2, 0, -1, structureboundingbox).func_185904_a() == Material.field_151579_a && this.func_175807_a(world, 2, -1, -1, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                this.func_175811_a(world, iblockdata7, 2, 0, -1, structureboundingbox);
                if (this.func_175807_a(world, 2, -1, -1, structureboundingbox).func_177230_c() == Blocks.field_185774_da) {
                    this.func_175811_a(world, Blocks.field_150349_c.func_176223_P(), 2, -1, -1, structureboundingbox);
                }
            }

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 9; ++j) {
                    this.func_74871_b(world, j, 7, i, structureboundingbox);
                    this.func_175808_b(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            for (i = 5; i < 11; ++i) {
                for (j = 2; j < 9; ++j) {
                    this.func_74871_b(world, j, 7, i, structureboundingbox);
                    this.func_175808_b(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.func_74893_a(world, structureboundingbox, 4, 1, 2, 2);
            return true;
        }
    }

    public static class Hall extends StructureVillagePieces.Village {

        public Hall() {}

        public Hall(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public static StructureVillagePieces.Hall func_175857_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 9, 7, 11, enumdirection);

            return func_74895_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureVillagePieces.Hall(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 7 - 1, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_150347_e.func_176223_P());
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.SOUTH));
            IBlockState iblockdata3 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.WEST));
            IBlockState iblockdata4 = this.func_175847_a(Blocks.field_150344_f.func_176223_P());
            IBlockState iblockdata5 = this.func_175847_a(Blocks.field_150364_r.func_176223_P());
            IBlockState iblockdata6 = this.func_175847_a(Blocks.field_180407_aO.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 1, 1, 1, 7, 4, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 1, 6, 8, 4, 10, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 2, 0, 6, 8, 0, 10, Blocks.field_150346_d.func_176223_P(), Blocks.field_150346_d.func_176223_P(), false);
            this.func_175811_a(world, iblockdata, 6, 0, 6, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 2, 1, 6, 2, 1, 10, iblockdata6, iblockdata6, false);
            this.func_175804_a(world, structureboundingbox, 8, 1, 6, 8, 1, 10, iblockdata6, iblockdata6, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 10, 7, 1, 10, iblockdata6, iblockdata6, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 1, 7, 0, 4, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 0, 3, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 8, 0, 0, 8, 3, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 0, 7, 1, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 5, 7, 1, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 0, 7, 3, 0, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 5, 7, 3, 5, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 0, 4, 1, 8, 4, 1, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 0, 4, 4, 8, 4, 4, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 2, 8, 5, 3, iblockdata4, iblockdata4, false);
            this.func_175811_a(world, iblockdata4, 0, 4, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata4, 0, 4, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata4, 8, 4, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata4, 8, 4, 3, structureboundingbox);
            IBlockState iblockdata7 = iblockdata1;
            IBlockState iblockdata8 = iblockdata2;

            int i;
            int j;

            for (i = -1; i <= 2; ++i) {
                for (j = 0; j <= 8; ++j) {
                    this.func_175811_a(world, iblockdata7, j, 4 + i, i, structureboundingbox);
                    this.func_175811_a(world, iblockdata8, j, 4 + i, 5 - i, structureboundingbox);
                }
            }

            this.func_175811_a(world, iblockdata5, 0, 2, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 0, 2, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 8, 2, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata5, 8, 2, 4, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 2, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 2, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 3, 2, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 5, 2, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 6, 2, 5, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 2, 1, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150452_aw.func_176223_P(), 2, 2, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata4, 1, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata7, 2, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 1, 1, 3, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 5, 0, 1, 7, 0, 3, Blocks.field_150334_T.func_176223_P(), Blocks.field_150334_T.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150334_T.func_176223_P(), 6, 1, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150334_T.func_176223_P(), 6, 1, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, 1, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, 2, 0, structureboundingbox);
            this.func_189926_a(world, EnumFacing.NORTH, 2, 3, 1, structureboundingbox);
            this.func_189927_a(world, structureboundingbox, random, 2, 1, 0, EnumFacing.NORTH);
            if (this.func_175807_a(world, 2, 0, -1, structureboundingbox).func_185904_a() == Material.field_151579_a && this.func_175807_a(world, 2, -1, -1, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                this.func_175811_a(world, iblockdata7, 2, 0, -1, structureboundingbox);
                if (this.func_175807_a(world, 2, -1, -1, structureboundingbox).func_177230_c() == Blocks.field_185774_da) {
                    this.func_175811_a(world, Blocks.field_150349_c.func_176223_P(), 2, -1, -1, structureboundingbox);
                }
            }

            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 6, 1, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 6, 2, 5, structureboundingbox);
            this.func_189926_a(world, EnumFacing.SOUTH, 6, 3, 4, structureboundingbox);
            this.func_189927_a(world, structureboundingbox, random, 6, 1, 5, EnumFacing.SOUTH);

            for (i = 0; i < 5; ++i) {
                for (j = 0; j < 9; ++j) {
                    this.func_74871_b(world, j, 7, i, structureboundingbox);
                    this.func_175808_b(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.func_74893_a(world, structureboundingbox, 4, 1, 2, 2);
            return true;
        }

        protected int func_180779_c(int i, int j) {
            return i == 0 ? 4 : super.func_180779_c(i, j);
        }
    }

    public static class WoodHut extends StructureVillagePieces.Village {

        private boolean field_74909_b;
        private int field_74910_c;

        public WoodHut() {}

        public WoodHut(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_74909_b = random.nextBoolean();
            this.field_74910_c = random.nextInt(3);
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74768_a("T", this.field_74910_c);
            nbttagcompound.func_74757_a("C", this.field_74909_b);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74910_c = nbttagcompound.func_74762_e("T");
            this.field_74909_b = nbttagcompound.func_74767_n("C");
        }

        public static StructureVillagePieces.WoodHut func_175853_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 4, 6, 5, enumdirection);

            return func_74895_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureVillagePieces.WoodHut(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 6 - 1, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_150347_e.func_176223_P());
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_150344_f.func_176223_P());
            IBlockState iblockdata2 = this.func_175847_a(Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata3 = this.func_175847_a(Blocks.field_150364_r.func_176223_P());
            IBlockState iblockdata4 = this.func_175847_a(Blocks.field_180407_aO.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 1, 1, 1, 3, 5, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 3, 0, 4, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 1, 2, 0, 3, Blocks.field_150346_d.func_176223_P(), Blocks.field_150346_d.func_176223_P(), false);
            if (this.field_74909_b) {
                this.func_175804_a(world, structureboundingbox, 1, 4, 1, 2, 4, 3, iblockdata3, iblockdata3, false);
            } else {
                this.func_175804_a(world, structureboundingbox, 1, 5, 1, 2, 5, 3, iblockdata3, iblockdata3, false);
            }

            this.func_175811_a(world, iblockdata3, 1, 4, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 2, 4, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 1, 4, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 2, 4, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 0, 4, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 0, 4, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 0, 4, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 3, 4, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 3, 4, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 3, 4, 3, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 0, 3, 0, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 0, 3, 3, 0, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 4, 0, 3, 4, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 4, 3, 3, 4, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 3, 3, iblockdata1, iblockdata1, false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 1, 3, 3, 3, iblockdata1, iblockdata1, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 0, 2, 3, 0, iblockdata1, iblockdata1, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 4, 2, 3, 4, iblockdata1, iblockdata1, false);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 3, 2, 2, structureboundingbox);
            if (this.field_74910_c > 0) {
                this.func_175811_a(world, iblockdata4, this.field_74910_c, 1, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150452_aw.func_176223_P(), this.field_74910_c, 2, 3, structureboundingbox);
            }

            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 1, 1, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 1, 2, 0, structureboundingbox);
            this.func_189927_a(world, structureboundingbox, random, 1, 1, 0, EnumFacing.NORTH);
            if (this.func_175807_a(world, 1, 0, -1, structureboundingbox).func_185904_a() == Material.field_151579_a && this.func_175807_a(world, 1, -1, -1, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                this.func_175811_a(world, iblockdata2, 1, 0, -1, structureboundingbox);
                if (this.func_175807_a(world, 1, -1, -1, structureboundingbox).func_177230_c() == Blocks.field_185774_da) {
                    this.func_175811_a(world, Blocks.field_150349_c.func_176223_P(), 1, -1, -1, structureboundingbox);
                }
            }

            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 4; ++j) {
                    this.func_74871_b(world, j, 6, i, structureboundingbox);
                    this.func_175808_b(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.func_74893_a(world, structureboundingbox, 1, 1, 2, 1);
            return true;
        }
    }

    public static class House1 extends StructureVillagePieces.Village {

        public House1() {}

        public House1(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public static StructureVillagePieces.House1 func_175850_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 9, 9, 6, enumdirection);

            return func_74895_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureVillagePieces.House1(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 9 - 1, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_150347_e.func_176223_P());
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.SOUTH));
            IBlockState iblockdata3 = this.func_175847_a(Blocks.field_150476_ad.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.EAST));
            IBlockState iblockdata4 = this.func_175847_a(Blocks.field_150344_f.func_176223_P());
            IBlockState iblockdata5 = this.func_175847_a(Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata6 = this.func_175847_a(Blocks.field_180407_aO.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 1, 1, 1, 7, 5, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 8, 0, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 5, 0, 8, 5, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 6, 1, 8, 6, 4, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 7, 2, 8, 7, 3, iblockdata, iblockdata, false);

            int i;

            for (int j = -1; j <= 2; ++j) {
                for (i = 0; i <= 8; ++i) {
                    this.func_175811_a(world, iblockdata1, i, 6 + j, j, structureboundingbox);
                    this.func_175811_a(world, iblockdata2, i, 6 + j, 5 - j, structureboundingbox);
                }
            }

            this.func_175804_a(world, structureboundingbox, 0, 1, 0, 0, 1, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 5, 8, 1, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 8, 1, 0, 8, 1, 4, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 2, 1, 0, 7, 1, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 0, 0, 4, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 5, 0, 4, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 8, 2, 5, 8, 4, 5, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 8, 2, 0, 8, 4, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 2, 1, 0, 4, 4, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 5, 7, 4, 5, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 8, 2, 1, 8, 4, 4, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 1, 2, 0, 7, 4, 0, iblockdata4, iblockdata4, false);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 2, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 5, 2, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 6, 2, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 3, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 5, 3, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 6, 3, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 3, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 3, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 2, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 3, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 8, 3, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 2, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 3, 2, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 5, 2, 5, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 6, 2, 5, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 1, 4, 1, 7, 4, 1, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 1, 4, 4, 7, 4, 4, iblockdata4, iblockdata4, false);
            this.func_175804_a(world, structureboundingbox, 1, 3, 4, 7, 3, 4, Blocks.field_150342_X.func_176223_P(), Blocks.field_150342_X.func_176223_P(), false);
            this.func_175811_a(world, iblockdata4, 7, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 7, 1, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 6, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 5, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 4, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 3, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 6, 1, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150452_aw.func_176223_P(), 6, 2, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata6, 4, 1, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150452_aw.func_176223_P(), 4, 2, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150462_ai.func_176223_P(), 7, 1, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 1, 1, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 1, 2, 0, structureboundingbox);
            this.func_189927_a(world, structureboundingbox, random, 1, 1, 0, EnumFacing.NORTH);
            if (this.func_175807_a(world, 1, 0, -1, structureboundingbox).func_185904_a() == Material.field_151579_a && this.func_175807_a(world, 1, -1, -1, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                this.func_175811_a(world, iblockdata5, 1, 0, -1, structureboundingbox);
                if (this.func_175807_a(world, 1, -1, -1, structureboundingbox).func_177230_c() == Blocks.field_185774_da) {
                    this.func_175811_a(world, Blocks.field_150349_c.func_176223_P(), 1, -1, -1, structureboundingbox);
                }
            }

            for (i = 0; i < 6; ++i) {
                for (int k = 0; k < 9; ++k) {
                    this.func_74871_b(world, k, 9, i, structureboundingbox);
                    this.func_175808_b(world, iblockdata, k, -1, i, structureboundingbox);
                }
            }

            this.func_74893_a(world, structureboundingbox, 2, 1, 2, 1);
            return true;
        }

        protected int func_180779_c(int i, int j) {
            return 1;
        }
    }

    public static class Church extends StructureVillagePieces.Village {

        public Church() {}

        public Church(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
        }

        public static StructureVillagePieces.Church func_175854_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 5, 12, 9, enumdirection);

            return func_74895_a(structureboundingbox) && StructureComponent.func_74883_a(list, structureboundingbox) == null ? new StructureVillagePieces.Church(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection) : null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 12 - 1, 0);
            }

            IBlockState iblockdata = Blocks.field_150347_e.func_176223_P();
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata2 = this.func_175847_a(Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.WEST));
            IBlockState iblockdata3 = this.func_175847_a(Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.EAST));

            this.func_175804_a(world, structureboundingbox, 1, 1, 1, 3, 3, 7, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 5, 1, 3, 9, 3, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 0, 0, 3, 0, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 0, 3, 10, 0, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 10, 3, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 4, 1, 1, 4, 10, 3, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 0, 4, 0, 4, 7, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 4, 0, 4, 4, 4, 7, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 8, 3, 4, 8, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 5, 4, 3, 10, 4, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 1, 5, 5, 3, 5, 7, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 9, 0, 4, 9, 4, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 4, 0, 4, 4, 4, iblockdata, iblockdata, false);
            this.func_175811_a(world, iblockdata, 0, 11, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata, 4, 11, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata, 2, 11, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 2, 11, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata, 1, 1, 6, structureboundingbox);
            this.func_175811_a(world, iblockdata, 1, 1, 7, structureboundingbox);
            this.func_175811_a(world, iblockdata, 2, 1, 7, structureboundingbox);
            this.func_175811_a(world, iblockdata, 3, 1, 6, structureboundingbox);
            this.func_175811_a(world, iblockdata, 3, 1, 7, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 1, 1, 5, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 2, 1, 6, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 3, 1, 5, structureboundingbox);
            this.func_175811_a(world, iblockdata2, 1, 2, 7, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 3, 2, 7, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 3, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 3, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 6, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 7, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 6, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 7, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 6, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 7, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 6, 4, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 7, 4, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 3, 6, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 3, 6, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 3, 8, structureboundingbox);
            this.func_189926_a(world, EnumFacing.SOUTH, 2, 4, 7, structureboundingbox);
            this.func_189926_a(world, EnumFacing.EAST, 1, 4, 6, structureboundingbox);
            this.func_189926_a(world, EnumFacing.WEST, 3, 4, 6, structureboundingbox);
            this.func_189926_a(world, EnumFacing.NORTH, 2, 4, 5, structureboundingbox);
            IBlockState iblockdata4 = Blocks.field_150468_ap.func_176223_P().func_177226_a(BlockLadder.field_176382_a, EnumFacing.WEST);

            int i;

            for (i = 1; i <= 9; ++i) {
                this.func_175811_a(world, iblockdata4, 3, i, 3, structureboundingbox);
            }

            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, 1, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, 2, 0, structureboundingbox);
            this.func_189927_a(world, structureboundingbox, random, 2, 1, 0, EnumFacing.NORTH);
            if (this.func_175807_a(world, 2, 0, -1, structureboundingbox).func_185904_a() == Material.field_151579_a && this.func_175807_a(world, 2, -1, -1, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                this.func_175811_a(world, iblockdata1, 2, 0, -1, structureboundingbox);
                if (this.func_175807_a(world, 2, -1, -1, structureboundingbox).func_177230_c() == Blocks.field_185774_da) {
                    this.func_175811_a(world, Blocks.field_150349_c.func_176223_P(), 2, -1, -1, structureboundingbox);
                }
            }

            for (i = 0; i < 9; ++i) {
                for (int j = 0; j < 5; ++j) {
                    this.func_74871_b(world, j, 12, i, structureboundingbox);
                    this.func_175808_b(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.func_74893_a(world, structureboundingbox, 2, 1, 2, 1);
            return true;
        }

        protected int func_180779_c(int i, int j) {
            return 2;
        }
    }

    public static class House4Garden extends StructureVillagePieces.Village {

        private boolean field_74913_b;

        public House4Garden() {}

        public House4Garden(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_74913_b = random.nextBoolean();
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Terrace", this.field_74913_b);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74913_b = nbttagcompound.func_74767_n("Terrace");
        }

        public static StructureVillagePieces.House4Garden func_175858_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection, int l) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 5, 6, 5, enumdirection);

            return StructureComponent.func_74883_a(list, structureboundingbox) != null ? null : new StructureVillagePieces.House4Garden(worldgenvillagepieces_worldgenvillagestartpiece, l, random, structureboundingbox, enumdirection);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 6 - 1, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_150347_e.func_176223_P());
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_150344_f.func_176223_P());
            IBlockState iblockdata2 = this.func_175847_a(Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH));
            IBlockState iblockdata3 = this.func_175847_a(Blocks.field_150364_r.func_176223_P());
            IBlockState iblockdata4 = this.func_175847_a(Blocks.field_180407_aO.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 4, 0, 4, iblockdata, iblockdata, false);
            this.func_175804_a(world, structureboundingbox, 0, 4, 0, 4, 4, 4, iblockdata3, iblockdata3, false);
            this.func_175804_a(world, structureboundingbox, 1, 4, 1, 3, 4, 3, iblockdata1, iblockdata1, false);
            this.func_175811_a(world, iblockdata, 0, 1, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 0, 2, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 0, 3, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 4, 1, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 4, 2, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 4, 3, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata, 0, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata, 0, 2, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata, 0, 3, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata, 4, 1, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata, 4, 2, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata, 4, 3, 4, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 0, 1, 1, 0, 3, 3, iblockdata1, iblockdata1, false);
            this.func_175804_a(world, structureboundingbox, 4, 1, 1, 4, 3, 3, iblockdata1, iblockdata1, false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 4, 3, 3, 4, iblockdata1, iblockdata1, false);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 0, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 2, 2, 4, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150410_aZ.func_176223_P(), 4, 2, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 1, 1, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 1, 2, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 1, 3, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 2, 3, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 3, 3, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 3, 2, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 3, 1, 0, structureboundingbox);
            if (this.func_175807_a(world, 2, 0, -1, structureboundingbox).func_185904_a() == Material.field_151579_a && this.func_175807_a(world, 2, -1, -1, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                this.func_175811_a(world, iblockdata2, 2, 0, -1, structureboundingbox);
                if (this.func_175807_a(world, 2, -1, -1, structureboundingbox).func_177230_c() == Blocks.field_185774_da) {
                    this.func_175811_a(world, Blocks.field_150349_c.func_176223_P(), 2, -1, -1, structureboundingbox);
                }
            }

            this.func_175804_a(world, structureboundingbox, 1, 1, 1, 3, 3, 3, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            if (this.field_74913_b) {
                this.func_175811_a(world, iblockdata4, 0, 5, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 1, 5, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 2, 5, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 3, 5, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 4, 5, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 0, 5, 4, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 1, 5, 4, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 2, 5, 4, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 3, 5, 4, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 4, 5, 4, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 4, 5, 1, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 4, 5, 2, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 4, 5, 3, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 0, 5, 1, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 0, 5, 2, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 0, 5, 3, structureboundingbox);
            }

            if (this.field_74913_b) {
                IBlockState iblockdata5 = Blocks.field_150468_ap.func_176223_P().func_177226_a(BlockLadder.field_176382_a, EnumFacing.SOUTH);

                this.func_175811_a(world, iblockdata5, 3, 1, 3, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 3, 2, 3, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 3, 3, 3, structureboundingbox);
                this.func_175811_a(world, iblockdata5, 3, 4, 3, structureboundingbox);
            }

            this.func_189926_a(world, EnumFacing.NORTH, 2, 3, 1, structureboundingbox);

            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    this.func_74871_b(world, j, 6, i, structureboundingbox);
                    this.func_175808_b(world, iblockdata, j, -1, i, structureboundingbox);
                }
            }

            this.func_74893_a(world, structureboundingbox, 1, 1, 2, 1);
            return true;
        }
    }

    public static class Path extends StructureVillagePieces.Road {

        private int field_74934_a;

        public Path() {}

        public Path(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, StructureBoundingBox structureboundingbox, EnumFacing enumdirection) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(enumdirection);
            this.field_74887_e = structureboundingbox;
            this.field_74934_a = Math.max(structureboundingbox.func_78883_b(), structureboundingbox.func_78880_d());
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74768_a("Length", this.field_74934_a);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74934_a = nbttagcompound.func_74762_e("Length");
        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            boolean flag = false;

            int i;
            StructureComponent structurepiece1;

            for (i = random.nextInt(5); i < this.field_74934_a - 8; i += 2 + random.nextInt(5)) {
                structurepiece1 = this.func_74891_a((StructureVillagePieces.Start) structurepiece, list, random, 0, i);
                if (structurepiece1 != null) {
                    i += Math.max(structurepiece1.field_74887_e.func_78883_b(), structurepiece1.field_74887_e.func_78880_d());
                    flag = true;
                }
            }

            for (i = random.nextInt(5); i < this.field_74934_a - 8; i += 2 + random.nextInt(5)) {
                structurepiece1 = this.func_74894_b((StructureVillagePieces.Start) structurepiece, list, random, 0, i);
                if (structurepiece1 != null) {
                    i += Math.max(structurepiece1.field_74887_e.func_78883_b(), structurepiece1.field_74887_e.func_78880_d());
                    flag = true;
                }
            }

            EnumFacing enumdirection = this.func_186165_e();

            if (flag && random.nextInt(3) > 0 && enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c, EnumFacing.WEST, this.func_74877_c());
                    break;

                case SOUTH:
                    StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f - 2, EnumFacing.WEST, this.func_74877_c());
                    break;

                case WEST:
                    StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c());
                    break;

                case EAST:
                    StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78893_d - 2, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c());
                }
            }

            if (flag && random.nextInt(3) > 0 && enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78896_c, EnumFacing.EAST, this.func_74877_c());
                    break;

                case SOUTH:
                    StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f - 2, EnumFacing.EAST, this.func_74877_c());
                    break;

                case WEST:
                    StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78897_a, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c());
                    break;

                case EAST:
                    StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78893_d - 2, this.field_74887_e.field_78895_b, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c());
                }
            }

        }

        public static StructureBoundingBox func_175848_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j, int k, EnumFacing enumdirection) {
            for (int l = 7 * MathHelper.func_76136_a(random, 3, 5); l >= 7; l -= 7) {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175897_a(i, j, k, 0, 0, 0, 3, 3, l, enumdirection);

                if (StructureComponent.func_74883_a(list, structureboundingbox) == null) {
                    return structureboundingbox;
                }
            }

            return null;
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            IBlockState iblockdata = this.func_175847_a(Blocks.field_185774_da.func_176223_P());
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_150344_f.func_176223_P());
            IBlockState iblockdata2 = this.func_175847_a(Blocks.field_150351_n.func_176223_P());
            IBlockState iblockdata3 = this.func_175847_a(Blocks.field_150347_e.func_176223_P());

            for (int i = this.field_74887_e.field_78897_a; i <= this.field_74887_e.field_78893_d; ++i) {
                for (int j = this.field_74887_e.field_78896_c; j <= this.field_74887_e.field_78892_f; ++j) {
                    BlockPos blockposition = new BlockPos(i, 64, j);

                    if (structureboundingbox.func_175898_b((Vec3i) blockposition)) {
                        blockposition = world.func_175672_r(blockposition).func_177977_b();
                        if (blockposition.func_177956_o() < world.func_181545_F()) {
                            blockposition = new BlockPos(blockposition.func_177958_n(), world.func_181545_F() - 1, blockposition.func_177952_p());
                        }

                        while (blockposition.func_177956_o() >= world.func_181545_F() - 1) {
                            IBlockState iblockdata4 = world.func_180495_p(blockposition);

                            if (iblockdata4.func_177230_c() == Blocks.field_150349_c && world.func_175623_d(blockposition.func_177984_a())) {
                                world.func_180501_a(blockposition, iblockdata, 2);
                                break;
                            }

                            if (iblockdata4.func_185904_a().func_76224_d()) {
                                world.func_180501_a(blockposition, iblockdata1, 2);
                                break;
                            }

                            if (iblockdata4.func_177230_c() == Blocks.field_150354_m || iblockdata4.func_177230_c() == Blocks.field_150322_A || iblockdata4.func_177230_c() == Blocks.field_180395_cM) {
                                world.func_180501_a(blockposition, iblockdata2, 2);
                                world.func_180501_a(blockposition.func_177977_b(), iblockdata3, 2);
                                break;
                            }

                            blockposition = blockposition.func_177977_b();
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

        public BiomeProvider field_74929_a;
        public int field_74928_c;
        public StructureVillagePieces.PieceWeight field_74926_d;
        public List<StructureVillagePieces.PieceWeight> field_74931_h;
        public List<StructureComponent> field_74932_i = Lists.newArrayList();
        public List<StructureComponent> field_74930_j = Lists.newArrayList();

        public Start() {}

        public Start(BiomeProvider worldchunkmanager, int i, Random random, int j, int k, List<StructureVillagePieces.PieceWeight> list, int l) {
            super((StructureVillagePieces.Start) null, 0, random, j, k);
            this.field_74929_a = worldchunkmanager;
            this.field_74931_h = list;
            this.field_74928_c = l;
            Biome biomebase = worldchunkmanager.func_180300_a(new BlockPos(j, 0, k), Biomes.field_180279_ad);

            if (biomebase instanceof BiomeDesert) {
                this.field_189928_h = 1;
            } else if (biomebase instanceof BiomeSavanna) {
                this.field_189928_h = 2;
            } else if (biomebase instanceof BiomeTaiga) {
                this.field_189928_h = 3;
            }

            this.func_189924_a(this.field_189928_h);
            this.field_189929_i = random.nextInt(50) == 0;
        }
    }

    public static class Well extends StructureVillagePieces.Village {

        public Well() {}

        public Well(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i, Random random, int j, int k) {
            super(worldgenvillagepieces_worldgenvillagestartpiece, i);
            this.func_186164_a(EnumFacing.Plane.HORIZONTAL.func_179518_a(random));
            if (this.func_186165_e().func_176740_k() == EnumFacing.Axis.Z) {
                this.field_74887_e = new StructureBoundingBox(j, 64, k, j + 6 - 1, 78, k + 6 - 1);
            } else {
                this.field_74887_e = new StructureBoundingBox(j, 64, k, j + 6 - 1, 78, k + 6 - 1);
            }

        }

        public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {
            StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78894_e - 4, this.field_74887_e.field_78896_c + 1, EnumFacing.WEST, this.func_74877_c());
            StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78894_e - 4, this.field_74887_e.field_78896_c + 1, EnumFacing.EAST, this.func_74877_c());
            StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78894_e - 4, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c());
            StructureVillagePieces.func_176069_e((StructureVillagePieces.Start) structurepiece, list, random, this.field_74887_e.field_78897_a + 1, this.field_74887_e.field_78894_e - 4, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c());
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.func_74889_b(world, structureboundingbox);
                if (this.field_143015_k < 0) {
                    return true;
                }

                this.field_74887_e.func_78886_a(0, this.field_143015_k - this.field_74887_e.field_78894_e + 3, 0);
            }

            IBlockState iblockdata = this.func_175847_a(Blocks.field_150347_e.func_176223_P());
            IBlockState iblockdata1 = this.func_175847_a(Blocks.field_180407_aO.func_176223_P());

            this.func_175804_a(world, structureboundingbox, 1, 0, 1, 4, 12, 4, iblockdata, Blocks.field_150358_i.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, 12, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 3, 12, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 2, 12, 3, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 3, 12, 3, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 1, 13, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 1, 14, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 4, 13, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 4, 14, 1, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 1, 13, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 1, 14, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 4, 13, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 4, 14, 4, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 1, 15, 1, 4, 15, 4, iblockdata, iblockdata, false);

            for (int i = 0; i <= 5; ++i) {
                for (int j = 0; j <= 5; ++j) {
                    if (j == 0 || j == 5 || i == 0 || i == 5) {
                        this.func_175811_a(world, iblockdata, j, 11, i, structureboundingbox);
                        this.func_74871_b(world, j, 12, i, structureboundingbox);
                    }
                }
            }

            return true;
        }
    }

    abstract static class Village extends StructureComponent {

        protected int field_143015_k = -1;
        private int field_74896_a;
        protected int field_189928_h;
        protected boolean field_189929_i;

        public Village() {}

        protected Village(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, int i) {
            super(i);
            if (worldgenvillagepieces_worldgenvillagestartpiece != null) {
                this.field_189928_h = worldgenvillagepieces_worldgenvillagestartpiece.field_189928_h;
                this.field_189929_i = worldgenvillagepieces_worldgenvillagestartpiece.field_189929_i;
            }

        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            nbttagcompound.func_74768_a("HPos", this.field_143015_k);
            nbttagcompound.func_74768_a("VCount", this.field_74896_a);
            nbttagcompound.func_74774_a("Type", (byte) this.field_189928_h);
            nbttagcompound.func_74757_a("Zombie", this.field_189929_i);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            this.field_143015_k = nbttagcompound.func_74762_e("HPos");
            this.field_74896_a = nbttagcompound.func_74762_e("VCount");
            this.field_189928_h = nbttagcompound.func_74771_c("Type");
            if (nbttagcompound.func_74767_n("Desert")) {
                this.field_189928_h = 1;
            }

            this.field_189929_i = nbttagcompound.func_74767_n("Zombie");
        }

        @Nullable
        protected StructureComponent func_74891_a(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    return StructureVillagePieces.func_176066_d(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.WEST, this.func_74877_c());

                case SOUTH:
                    return StructureVillagePieces.func_176066_d(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.field_74887_e.field_78897_a - 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.WEST, this.func_74877_c());

                case WEST:
                    return StructureVillagePieces.func_176066_d(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c());

                case EAST:
                    return StructureVillagePieces.func_176066_d(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c - 1, EnumFacing.NORTH, this.func_74877_c());
                }
            } else {
                return null;
            }
        }

        @Nullable
        protected StructureComponent func_74894_b(StructureVillagePieces.Start worldgenvillagepieces_worldgenvillagestartpiece, List<StructureComponent> list, Random random, int i, int j) {
            EnumFacing enumdirection = this.func_186165_e();

            if (enumdirection != null) {
                switch (enumdirection) {
                case NORTH:
                default:
                    return StructureVillagePieces.func_176066_d(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.EAST, this.func_74877_c());

                case SOUTH:
                    return StructureVillagePieces.func_176066_d(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.field_74887_e.field_78893_d + 1, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78896_c + j, EnumFacing.EAST, this.func_74877_c());

                case WEST:
                    return StructureVillagePieces.func_176066_d(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c());

                case EAST:
                    return StructureVillagePieces.func_176066_d(worldgenvillagepieces_worldgenvillagestartpiece, list, random, this.field_74887_e.field_78897_a + j, this.field_74887_e.field_78895_b + i, this.field_74887_e.field_78892_f + 1, EnumFacing.SOUTH, this.func_74877_c());
                }
            } else {
                return null;
            }
        }

        protected int func_74889_b(World world, StructureBoundingBox structureboundingbox) {
            int i = 0;
            int j = 0;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

            for (int k = this.field_74887_e.field_78896_c; k <= this.field_74887_e.field_78892_f; ++k) {
                for (int l = this.field_74887_e.field_78897_a; l <= this.field_74887_e.field_78893_d; ++l) {
                    blockposition_mutableblockposition.func_181079_c(l, 64, k);
                    if (structureboundingbox.func_175898_b((Vec3i) blockposition_mutableblockposition)) {
                        i += Math.max(world.func_175672_r(blockposition_mutableblockposition).func_177956_o(), world.field_73011_w.func_76557_i() - 1);
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

        protected static boolean func_74895_a(StructureBoundingBox structureboundingbox) {
            return structureboundingbox != null && structureboundingbox.field_78895_b > 10;
        }

        protected void func_74893_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l) {
            if (this.field_74896_a < l) {
                for (int i1 = this.field_74896_a; i1 < l; ++i1) {
                    int j1 = this.func_74865_a(i + i1, k);
                    int k1 = this.func_74862_a(j);
                    int l1 = this.func_74873_b(i + i1, k);

                    if (!structureboundingbox.func_175898_b((Vec3i) (new BlockPos(j1, k1, l1)))) {
                        break;
                    }

                    ++this.field_74896_a;
                    if (this.field_189929_i) {
                        EntityZombieVillager entityzombievillager = new EntityZombieVillager(world);

                        entityzombievillager.func_70012_b((double) j1 + 0.5D, (double) k1, (double) l1 + 0.5D, 0.0F, 0.0F);
                        entityzombievillager.func_180482_a(world.func_175649_E(new BlockPos(entityzombievillager)), (IEntityLivingData) null);
                        entityzombievillager.func_190733_a(this.func_180779_c(i1, 0));
                        entityzombievillager.func_110163_bv();
                        world.addEntity(entityzombievillager, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
                    } else {
                        EntityVillager entityvillager = new EntityVillager(world);

                        entityvillager.func_70012_b((double) j1 + 0.5D, (double) k1, (double) l1 + 0.5D, 0.0F, 0.0F);
                        entityvillager.func_70938_b(this.func_180779_c(i1, world.field_73012_v.nextInt(6)));
                        entityvillager.func_190672_a(world.func_175649_E(new BlockPos(entityvillager)), (IEntityLivingData) null, false);
                        world.addEntity(entityvillager, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
                    }
                }

            }
        }

        protected int func_180779_c(int i, int j) {
            return j;
        }

        protected IBlockState func_175847_a(IBlockState iblockdata) {
            if (this.field_189928_h == 1) {
                if (iblockdata.func_177230_c() == Blocks.field_150364_r || iblockdata.func_177230_c() == Blocks.field_150363_s) {
                    return Blocks.field_150322_A.func_176223_P();
                }

                if (iblockdata.func_177230_c() == Blocks.field_150347_e) {
                    return Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.DEFAULT.func_176675_a());
                }

                if (iblockdata.func_177230_c() == Blocks.field_150344_f) {
                    return Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a());
                }

                if (iblockdata.func_177230_c() == Blocks.field_150476_ad) {
                    return Blocks.field_150372_bz.func_176223_P().func_177226_a(BlockStairs.field_176309_a, iblockdata.func_177229_b(BlockStairs.field_176309_a));
                }

                if (iblockdata.func_177230_c() == Blocks.field_150446_ar) {
                    return Blocks.field_150372_bz.func_176223_P().func_177226_a(BlockStairs.field_176309_a, iblockdata.func_177229_b(BlockStairs.field_176309_a));
                }

                if (iblockdata.func_177230_c() == Blocks.field_150351_n) {
                    return Blocks.field_150322_A.func_176223_P();
                }
            } else if (this.field_189928_h == 3) {
                if (iblockdata.func_177230_c() == Blocks.field_150364_r || iblockdata.func_177230_c() == Blocks.field_150363_s) {
                    return Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.SPRUCE).func_177226_a(BlockLog.field_176299_a, iblockdata.func_177229_b(BlockLog.field_176299_a));
                }

                if (iblockdata.func_177230_c() == Blocks.field_150344_f) {
                    return Blocks.field_150344_f.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.SPRUCE);
                }

                if (iblockdata.func_177230_c() == Blocks.field_150476_ad) {
                    return Blocks.field_150485_bF.func_176223_P().func_177226_a(BlockStairs.field_176309_a, iblockdata.func_177229_b(BlockStairs.field_176309_a));
                }

                if (iblockdata.func_177230_c() == Blocks.field_180407_aO) {
                    return Blocks.field_180408_aP.func_176223_P();
                }
            } else if (this.field_189928_h == 2) {
                if (iblockdata.func_177230_c() == Blocks.field_150364_r || iblockdata.func_177230_c() == Blocks.field_150363_s) {
                    return Blocks.field_150363_s.func_176223_P().func_177226_a(BlockNewLog.field_176300_b, BlockPlanks.EnumType.ACACIA).func_177226_a(BlockLog.field_176299_a, iblockdata.func_177229_b(BlockLog.field_176299_a));
                }

                if (iblockdata.func_177230_c() == Blocks.field_150344_f) {
                    return Blocks.field_150344_f.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.ACACIA);
                }

                if (iblockdata.func_177230_c() == Blocks.field_150476_ad) {
                    return Blocks.field_150400_ck.func_176223_P().func_177226_a(BlockStairs.field_176309_a, iblockdata.func_177229_b(BlockStairs.field_176309_a));
                }

                if (iblockdata.func_177230_c() == Blocks.field_150347_e) {
                    return Blocks.field_150363_s.func_176223_P().func_177226_a(BlockNewLog.field_176300_b, BlockPlanks.EnumType.ACACIA).func_177226_a(BlockLog.field_176299_a, BlockLog.EnumAxis.Y);
                }

                if (iblockdata.func_177230_c() == Blocks.field_180407_aO) {
                    return Blocks.field_180405_aT.func_176223_P();
                }
            }

            return iblockdata;
        }

        protected BlockDoor func_189925_i() {
            switch (this.field_189928_h) {
            case 2:
                return Blocks.field_180410_as;

            case 3:
                return Blocks.field_180414_ap;

            default:
                return Blocks.field_180413_ao;
            }
        }

        protected void func_189927_a(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, EnumFacing enumdirection) {
            if (!this.field_189929_i) {
                this.func_189915_a(world, structureboundingbox, random, i, j, k, EnumFacing.NORTH, this.func_189925_i());
            }

        }

        protected void func_189926_a(World world, EnumFacing enumdirection, int i, int j, int k, StructureBoundingBox structureboundingbox) {
            if (!this.field_189929_i) {
                this.func_175811_a(world, Blocks.field_150478_aa.func_176223_P().func_177226_a(BlockTorch.field_176596_a, enumdirection), i, j, k, structureboundingbox);
            }

        }

        protected void func_175808_b(World world, IBlockState iblockdata, int i, int j, int k, StructureBoundingBox structureboundingbox) {
            IBlockState iblockdata1 = this.func_175847_a(iblockdata);

            super.func_175808_b(world, iblockdata1, i, j, k, structureboundingbox);
        }

        protected void func_189924_a(int i) {
            this.field_189928_h = i;
        }
    }

    public static class PieceWeight {

        public Class<? extends StructureVillagePieces.Village> field_75090_a;
        public final int field_75088_b;
        public int field_75089_c;
        public int field_75087_d;

        public PieceWeight(Class<? extends StructureVillagePieces.Village> oclass, int i, int j) {
            this.field_75090_a = oclass;
            this.field_75088_b = i;
            this.field_75087_d = j;
        }

        public boolean func_75085_a(int i) {
            return this.field_75087_d == 0 || this.field_75089_c < this.field_75087_d;
        }

        public boolean func_75086_a() {
            return this.field_75087_d == 0 || this.field_75089_c < this.field_75087_d;
        }
    }
}
