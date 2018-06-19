package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class StructureEndCityPieces {

    private static final PlacementSettings field_186202_b = (new PlacementSettings()).func_186222_a(true);
    private static final PlacementSettings field_186203_c = (new PlacementSettings()).func_186222_a(true).func_186225_a(Blocks.field_150350_a);
    private static final StructureEndCityPieces.IGenerator field_186204_d = new StructureEndCityPieces.IGenerator() {
        public void func_186184_a() {}

        public boolean func_191086_a(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
            if (i > 8) {
                return false;
            } else {
                Rotation enumblockrotation = worldgenendcitypieces_piece.field_186177_b.func_186215_c();
                StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece, blockposition, "base_floor", enumblockrotation, true));
                int j = random.nextInt(3);

                if (j == 0) {
                    StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 4, -1), "base_roof", enumblockrotation, true));
                } else if (j == 1) {
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 0, -1), "second_floor_2", enumblockrotation, false));
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 8, -1), "second_roof", enumblockrotation, false));
                    StructureEndCityPieces.func_191088_b(definedstructuremanager, StructureEndCityPieces.field_186206_f, i + 1, worldgenendcitypieces_piece1, (BlockPos) null, list, random);
                } else if (j == 2) {
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 0, -1), "second_floor_2", enumblockrotation, false));
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 4, -1), "third_floor_c", enumblockrotation, false));
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 8, -1), "third_roof", enumblockrotation, true));
                    StructureEndCityPieces.func_191088_b(definedstructuremanager, StructureEndCityPieces.field_186206_f, i + 1, worldgenendcitypieces_piece1, (BlockPos) null, list, random);
                }

                return true;
            }
        }
    };
    private static final List<Tuple<Rotation, BlockPos>> field_186205_e = Lists.newArrayList(new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(1, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6))});
    private static final StructureEndCityPieces.IGenerator field_186206_f = new StructureEndCityPieces.IGenerator() {
        public void func_186184_a() {}

        public boolean func_191086_a(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
            Rotation enumblockrotation = worldgenendcitypieces_piece.field_186177_b.func_186215_c();
            StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(3 + random.nextInt(2), -3, 3 + random.nextInt(2)), "tower_base", enumblockrotation, true));

            worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, 7, 0), "tower_piece", enumblockrotation, true));
            StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece2 = random.nextInt(3) == 0 ? worldgenendcitypieces_piece1 : null;
            int j = 1 + random.nextInt(3);

            for (int k = 0; k < j; ++k) {
                worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, 4, 0), "tower_piece", enumblockrotation, true));
                if (k < j - 1 && random.nextBoolean()) {
                    worldgenendcitypieces_piece2 = worldgenendcitypieces_piece1;
                }
            }

            if (worldgenendcitypieces_piece2 != null) {
                Iterator iterator = StructureEndCityPieces.field_186205_e.iterator();

                while (iterator.hasNext()) {
                    Tuple tuple = (Tuple) iterator.next();

                    if (random.nextBoolean()) {
                        StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece3 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece2, (BlockPos) tuple.func_76340_b(), "bridge_end", enumblockrotation.func_185830_a((Rotation) tuple.func_76341_a()), true));

                        StructureEndCityPieces.func_191088_b(definedstructuremanager, StructureEndCityPieces.field_186207_g, i + 1, worldgenendcitypieces_piece3, (BlockPos) null, list, random);
                    }
                }

                StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 4, -1), "tower_top", enumblockrotation, true));
            } else {
                if (i != 7) {
                    return StructureEndCityPieces.func_191088_b(definedstructuremanager, StructureEndCityPieces.field_186209_i, i + 1, worldgenendcitypieces_piece1, (BlockPos) null, list, random);
                }

                StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-1, 4, -1), "tower_top", enumblockrotation, true));
            }

            return true;
        }
    };
    private static final StructureEndCityPieces.IGenerator field_186207_g = new StructureEndCityPieces.IGenerator() {
        public boolean a;

        public void func_186184_a() {
            this.a = false;
        }

        public boolean func_191086_a(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
            Rotation enumblockrotation = worldgenendcitypieces_piece.field_186177_b.func_186215_c();
            int j = random.nextInt(4) + 1;
            StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(0, 0, -4), "bridge_piece", enumblockrotation, true));

            worldgenendcitypieces_piece1.field_74886_g = -1;
            byte b0 = 0;

            for (int k = 0; k < j; ++k) {
                if (random.nextBoolean()) {
                    worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, b0, -4), "bridge_piece", enumblockrotation, true));
                    b0 = 0;
                } else {
                    if (random.nextBoolean()) {
                        worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, b0, -4), "bridge_steep_stairs", enumblockrotation, true));
                    } else {
                        worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, b0, -8), "bridge_gentle_stairs", enumblockrotation, true));
                    }

                    b0 = 4;
                }
            }

            if (!this.a && random.nextInt(10 - i) == 0) {
                StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-8 + random.nextInt(8), b0, -70 + random.nextInt(10)), "ship", enumblockrotation, true));
                this.a = true;
            } else if (!StructureEndCityPieces.func_191088_b(definedstructuremanager, StructureEndCityPieces.field_186204_d, i + 1, worldgenendcitypieces_piece1, new BlockPos(-3, b0 + 1, -11), list, random)) {
                return false;
            }

            worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(4, b0, 0), "bridge_end", enumblockrotation.func_185830_a(Rotation.CLOCKWISE_180), true));
            worldgenendcitypieces_piece1.field_74886_g = -1;
            return true;
        }
    };
    private static final List<Tuple<Rotation, BlockPos>> field_186208_h = Lists.newArrayList(new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(4, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12))});
    private static final StructureEndCityPieces.IGenerator field_186209_i = new StructureEndCityPieces.IGenerator() {
        public void func_186184_a() {}

        public boolean func_191086_a(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
            Rotation enumblockrotation = worldgenendcitypieces_piece.field_186177_b.func_186215_c();
            StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(-3, 4, -3), "fat_tower_base", enumblockrotation, true));

            worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, 4, 0), "fat_tower_middle", enumblockrotation, true));

            for (int j = 0; j < 2 && random.nextInt(3) != 0; ++j) {
                worldgenendcitypieces_piece1 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(0, 8, 0), "fat_tower_middle", enumblockrotation, true));
                Iterator iterator = StructureEndCityPieces.field_186208_h.iterator();

                while (iterator.hasNext()) {
                    Tuple tuple = (Tuple) iterator.next();

                    if (random.nextBoolean()) {
                        StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece2 = StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, (BlockPos) tuple.func_76340_b(), "bridge_end", enumblockrotation.func_185830_a((Rotation) tuple.func_76341_a()), true));

                        StructureEndCityPieces.func_191088_b(definedstructuremanager, StructureEndCityPieces.field_186207_g, i + 1, worldgenendcitypieces_piece2, (BlockPos) null, list, random);
                    }
                }
            }

            StructureEndCityPieces.func_189935_b(list, StructureEndCityPieces.func_191090_b(definedstructuremanager, worldgenendcitypieces_piece1, new BlockPos(-2, 8, -2), "fat_tower_top", enumblockrotation, true));
            return true;
        }
    };

    public static void func_186200_a() {
        MapGenStructureIO.func_143031_a(StructureEndCityPieces.CityTemplate.class, "ECP");
    }

    private static StructureEndCityPieces.CityTemplate func_191090_b(TemplateManager definedstructuremanager, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, String s, Rotation enumblockrotation, boolean flag) {
        StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece1 = new StructureEndCityPieces.CityTemplate(definedstructuremanager, s, worldgenendcitypieces_piece.field_186178_c, enumblockrotation, flag);
        BlockPos blockposition1 = worldgenendcitypieces_piece.field_186176_a.func_186262_a(worldgenendcitypieces_piece.field_186177_b, blockposition, worldgenendcitypieces_piece1.field_186177_b, BlockPos.field_177992_a);

        worldgenendcitypieces_piece1.func_181138_a(blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p());
        return worldgenendcitypieces_piece1;
    }

    public static void func_191087_a(TemplateManager definedstructuremanager, BlockPos blockposition, Rotation enumblockrotation, List<StructureComponent> list, Random random) {
        StructureEndCityPieces.field_186209_i.func_186184_a();
        StructureEndCityPieces.field_186204_d.func_186184_a();
        StructureEndCityPieces.field_186207_g.func_186184_a();
        StructureEndCityPieces.field_186206_f.func_186184_a();
        StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece = func_189935_b(list, new StructureEndCityPieces.CityTemplate(definedstructuremanager, "base_floor", blockposition, enumblockrotation, true));

        worldgenendcitypieces_piece = func_189935_b(list, func_191090_b(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(-1, 0, -1), "second_floor", enumblockrotation, false));
        worldgenendcitypieces_piece = func_189935_b(list, func_191090_b(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(-1, 4, -1), "third_floor", enumblockrotation, false));
        worldgenendcitypieces_piece = func_189935_b(list, func_191090_b(definedstructuremanager, worldgenendcitypieces_piece, new BlockPos(-1, 8, -1), "third_roof", enumblockrotation, true));
        func_191088_b(definedstructuremanager, StructureEndCityPieces.field_186206_f, 1, worldgenendcitypieces_piece, (BlockPos) null, list, random);
    }

    private static StructureEndCityPieces.CityTemplate func_189935_b(List<StructureComponent> list, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece) {
        list.add(worldgenendcitypieces_piece);
        return worldgenendcitypieces_piece;
    }

    private static boolean func_191088_b(TemplateManager definedstructuremanager, StructureEndCityPieces.IGenerator worldgenendcitypieces_piecegenerator, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random) {
        if (i > 8) {
            return false;
        } else {
            ArrayList arraylist = Lists.newArrayList();

            if (worldgenendcitypieces_piecegenerator.func_191086_a(definedstructuremanager, i, worldgenendcitypieces_piece, blockposition, arraylist, random)) {
                boolean flag = false;
                int j = random.nextInt();
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    StructureComponent structurepiece = (StructureComponent) iterator.next();

                    structurepiece.field_74886_g = j;
                    StructureComponent structurepiece1 = StructureComponent.func_74883_a(list, structurepiece.func_74874_b());

                    if (structurepiece1 != null && structurepiece1.field_74886_g != worldgenendcitypieces_piece.field_74886_g) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    list.addAll(arraylist);
                    return true;
                }
            }

            return false;
        }
    }

    interface IGenerator {

        void func_186184_a();

        boolean func_191086_a(TemplateManager definedstructuremanager, int i, StructureEndCityPieces.CityTemplate worldgenendcitypieces_piece, BlockPos blockposition, List<StructureComponent> list, Random random);
    }

    public static class CityTemplate extends StructureComponentTemplate {

        private String field_186181_d;
        private Rotation field_186182_e;
        private boolean field_186183_f;

        public CityTemplate() {}

        public CityTemplate(TemplateManager definedstructuremanager, String s, BlockPos blockposition, Rotation enumblockrotation, boolean flag) {
            super(0);
            this.field_186181_d = s;
            this.field_186178_c = blockposition;
            this.field_186182_e = enumblockrotation;
            this.field_186183_f = flag;
            this.func_191085_a(definedstructuremanager);
        }

        private void func_191085_a(TemplateManager definedstructuremanager) {
            Template definedstructure = definedstructuremanager.func_186237_a((MinecraftServer) null, new ResourceLocation("endcity/" + this.field_186181_d));
            PlacementSettings definedstructureinfo = (this.field_186183_f ? StructureEndCityPieces.field_186202_b : StructureEndCityPieces.field_186203_c).func_186217_a().func_186220_a(this.field_186182_e);

            this.func_186173_a(definedstructure, this.field_186178_c, definedstructureinfo);
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74778_a("Template", this.field_186181_d);
            nbttagcompound.func_74778_a("Rot", this.field_186182_e.name());
            nbttagcompound.func_74757_a("OW", this.field_186183_f);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_186181_d = nbttagcompound.func_74779_i("Template");
            this.field_186182_e = Rotation.valueOf(nbttagcompound.func_74779_i("Rot"));
            this.field_186183_f = nbttagcompound.func_74767_n("OW");
            this.func_191085_a(definedstructuremanager);
        }

        protected void func_186175_a(String s, BlockPos blockposition, World world, Random random, StructureBoundingBox structureboundingbox) {
            if (s.startsWith("Chest")) {
                BlockPos blockposition1 = blockposition.func_177977_b();

                if (structureboundingbox.func_175898_b((Vec3i) blockposition1)) {
                    TileEntity tileentity = world.func_175625_s(blockposition1);

                    if (tileentity instanceof TileEntityChest) {
                        ((TileEntityChest) tileentity).func_189404_a(LootTableList.field_186421_c, random.nextLong());
                    }
                }
            } else if (s.startsWith("Sentry")) {
                EntityShulker entityshulker = new EntityShulker(world);

                entityshulker.func_70107_b((double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D);
                entityshulker.func_184694_g(blockposition);
                world.func_72838_d(entityshulker);
            } else if (s.startsWith("Elytra")) {
                EntityItemFrame entityitemframe = new EntityItemFrame(world, blockposition, this.field_186182_e.func_185831_a(EnumFacing.SOUTH));

                entityitemframe.func_82334_a(new ItemStack(Items.field_185160_cR));
                world.func_72838_d(entityitemframe);
            }

        }
    }
}
