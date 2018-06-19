package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenRegistration.b;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

public class ComponentScatteredFeaturePieces {

    public static void func_143045_a() {
        MapGenStructureIO.func_143031_a(ComponentScatteredFeaturePieces.DesertPyramid.class, "TeDP");
        MapGenStructureIO.func_143031_a(ComponentScatteredFeaturePieces.JunglePyramid.class, "TeJP");
        MapGenStructureIO.func_143031_a(ComponentScatteredFeaturePieces.SwampHut.class, "TeSH");
        MapGenStructureIO.func_143031_a(WorldGenRegistration.b.class, "Iglu");
    }

    public static class b extends ComponentScatteredFeaturePieces.Feature {

        private static final ResourceLocation e = new ResourceLocation("igloo/igloo_top");
        private static final ResourceLocation f = new ResourceLocation("igloo/igloo_middle");
        private static final ResourceLocation g = new ResourceLocation("igloo/igloo_bottom");

        public b() {}

        public b(Random random, int i, int j) {
            super(random, i, 64, j, 7, 5, 8);
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (!this.func_74935_a(world, structureboundingbox, -1)) {
                return false;
            } else {
                StructureBoundingBox structureboundingbox1 = this.func_74874_b();
                BlockPos blockposition = new BlockPos(structureboundingbox1.field_78897_a, structureboundingbox1.field_78895_b, structureboundingbox1.field_78896_c);
                Rotation[] aenumblockrotation = Rotation.values();
                MinecraftServer minecraftserver = world.func_73046_m();
                TemplateManager definedstructuremanager = world.func_72860_G().func_186340_h();
                PlacementSettings definedstructureinfo = (new PlacementSettings()).func_186220_a(aenumblockrotation[random.nextInt(aenumblockrotation.length)]).func_186225_a(Blocks.field_189881_dj).func_186223_a(structureboundingbox1);
                Template definedstructure = definedstructuremanager.func_186237_a(minecraftserver, WorldGenRegistration.b.e);

                definedstructure.func_186260_a(world, blockposition, definedstructureinfo);
                if (random.nextDouble() < 0.5D) {
                    Template definedstructure1 = definedstructuremanager.func_186237_a(minecraftserver, WorldGenRegistration.b.f);
                    Template definedstructure2 = definedstructuremanager.func_186237_a(minecraftserver, WorldGenRegistration.b.g);
                    int i = random.nextInt(8) + 4;

                    for (int j = 0; j < i; ++j) {
                        BlockPos blockposition1 = definedstructure.func_186262_a(definedstructureinfo, new BlockPos(3, -1 - j * 3, 5), definedstructureinfo, new BlockPos(1, 2, 1));

                        definedstructure1.func_186260_a(world, blockposition.func_177971_a((Vec3i) blockposition1), definedstructureinfo);
                    }

                    BlockPos blockposition2 = blockposition.func_177971_a((Vec3i) definedstructure.func_186262_a(definedstructureinfo, new BlockPos(3, -1 - i * 3, 5), definedstructureinfo, new BlockPos(3, 5, 7)));

                    definedstructure2.func_186260_a(world, blockposition2, definedstructureinfo);
                    Map map = definedstructure2.func_186258_a(blockposition2, definedstructureinfo);
                    Iterator iterator = map.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Entry entry = (Entry) iterator.next();

                        if ("chest".equals(entry.getValue())) {
                            BlockPos blockposition3 = (BlockPos) entry.getKey();

                            world.func_180501_a(blockposition3, Blocks.field_150350_a.func_176223_P(), 3);
                            TileEntity tileentity = world.func_175625_s(blockposition3.func_177977_b());

                            if (tileentity instanceof TileEntityChest) {
                                ((TileEntityChest) tileentity).func_189404_a(LootTableList.field_186431_m, random.nextLong());
                            }
                        }
                    }
                } else {
                    BlockPos blockposition4 = Template.func_186266_a(definedstructureinfo, new BlockPos(3, 0, 5));

                    world.func_180501_a(blockposition.func_177971_a((Vec3i) blockposition4), Blocks.field_150433_aE.func_176223_P(), 3);
                }

                return true;
            }
        }
    }

    public static class SwampHut extends ComponentScatteredFeaturePieces.Feature {

        private boolean field_82682_h;

        public SwampHut() {}

        public SwampHut(Random random, int i, int j) {
            super(random, i, 64, j, 7, 7, 9);
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("Witch", this.field_82682_h);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_82682_h = nbttagcompound.func_74767_n("Witch");
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (!this.func_74935_a(world, structureboundingbox, 0)) {
                return false;
            } else {
                this.func_175804_a(world, structureboundingbox, 1, 1, 1, 5, 1, 7, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.func_175804_a(world, structureboundingbox, 1, 4, 2, 5, 4, 7, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.func_175804_a(world, structureboundingbox, 2, 1, 0, 4, 1, 0, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.func_175804_a(world, structureboundingbox, 2, 2, 2, 3, 3, 2, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.func_175804_a(world, structureboundingbox, 1, 2, 3, 1, 3, 6, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.func_175804_a(world, structureboundingbox, 5, 2, 3, 5, 3, 6, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.func_175804_a(world, structureboundingbox, 2, 2, 7, 4, 3, 7, Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), Blocks.field_150344_f.func_176203_a(BlockPlanks.EnumType.SPRUCE.func_176839_a()), false);
                this.func_175804_a(world, structureboundingbox, 1, 0, 2, 1, 3, 2, Blocks.field_150364_r.func_176223_P(), Blocks.field_150364_r.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 5, 0, 2, 5, 3, 2, Blocks.field_150364_r.func_176223_P(), Blocks.field_150364_r.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 1, 0, 7, 1, 3, 7, Blocks.field_150364_r.func_176223_P(), Blocks.field_150364_r.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, 5, 0, 7, 5, 3, 7, Blocks.field_150364_r.func_176223_P(), Blocks.field_150364_r.func_176223_P(), false);
                this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 2, 3, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 3, 3, 7, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 1, 3, 4, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 5, 3, 4, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 5, 3, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150457_bL.func_176223_P().func_177226_a(BlockFlowerPot.field_176443_b, BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 1, 3, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150462_ai.func_176223_P(), 3, 2, 6, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150383_bp.func_176223_P(), 4, 2, 6, structureboundingbox);
                this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 1, 2, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_180407_aO.func_176223_P(), 5, 2, 1, structureboundingbox);
                IBlockState iblockdata = Blocks.field_150485_bF.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH);
                IBlockState iblockdata1 = Blocks.field_150485_bF.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.EAST);
                IBlockState iblockdata2 = Blocks.field_150485_bF.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.WEST);
                IBlockState iblockdata3 = Blocks.field_150485_bF.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.SOUTH);

                this.func_175804_a(world, structureboundingbox, 0, 4, 1, 6, 4, 1, iblockdata, iblockdata, false);
                this.func_175804_a(world, structureboundingbox, 0, 4, 2, 0, 4, 7, iblockdata1, iblockdata1, false);
                this.func_175804_a(world, structureboundingbox, 6, 4, 2, 6, 4, 7, iblockdata2, iblockdata2, false);
                this.func_175804_a(world, structureboundingbox, 0, 4, 8, 6, 4, 8, iblockdata3, iblockdata3, false);

                int i;
                int j;

                for (i = 2; i <= 7; i += 5) {
                    for (j = 1; j <= 5; j += 4) {
                        this.func_175808_b(world, Blocks.field_150364_r.func_176223_P(), j, -1, i, structureboundingbox);
                    }
                }

                if (!this.field_82682_h) {
                    i = this.func_74865_a(2, 5);
                    j = this.func_74862_a(2);
                    int k = this.func_74873_b(2, 5);

                    if (structureboundingbox.func_175898_b((Vec3i) (new BlockPos(i, j, k)))) {
                        this.field_82682_h = true;
                        EntityWitch entitywitch = new EntityWitch(world);

                        entitywitch.func_110163_bv();
                        entitywitch.func_70012_b((double) i + 0.5D, (double) j, (double) k + 0.5D, 0.0F, 0.0F);
                        entitywitch.func_180482_a(world.func_175649_E(new BlockPos(i, j, k)), (IEntityLivingData) null);
                        world.addEntity(entitywitch, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CHUNK_GEN); // CraftBukkit - add SpawnReason
                    }
                }

                return true;
            }
        }
    }

    public static class JunglePyramid extends ComponentScatteredFeaturePieces.Feature {

        private boolean field_74947_h;
        private boolean field_74948_i;
        private boolean field_74945_j;
        private boolean field_74946_k;
        private static final ComponentScatteredFeaturePieces.JunglePyramid.Stones field_74942_n = new ComponentScatteredFeaturePieces.JunglePyramid.Stones(null);

        public JunglePyramid() {}

        public JunglePyramid(Random random, int i, int j) {
            super(random, i, 64, j, 12, 10, 15);
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("placedMainChest", this.field_74947_h);
            nbttagcompound.func_74757_a("placedHiddenChest", this.field_74948_i);
            nbttagcompound.func_74757_a("placedTrap1", this.field_74945_j);
            nbttagcompound.func_74757_a("placedTrap2", this.field_74946_k);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74947_h = nbttagcompound.func_74767_n("placedMainChest");
            this.field_74948_i = nbttagcompound.func_74767_n("placedHiddenChest");
            this.field_74945_j = nbttagcompound.func_74767_n("placedTrap1");
            this.field_74946_k = nbttagcompound.func_74767_n("placedTrap2");
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            if (!this.func_74935_a(world, structureboundingbox, 0)) {
                return false;
            } else {
                this.func_74882_a(world, structureboundingbox, 0, -4, 0, this.field_74939_a - 1, 0, this.field_74938_c - 1, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 2, 1, 2, 9, 2, 2, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 2, 1, 12, 9, 2, 12, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 2, 1, 3, 2, 2, 11, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 9, 1, 3, 9, 2, 11, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 1, 3, 1, 10, 6, 1, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 1, 3, 13, 10, 6, 13, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 1, 3, 2, 1, 6, 12, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 10, 3, 2, 10, 6, 12, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 2, 3, 2, 9, 3, 12, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 2, 6, 2, 9, 6, 12, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 3, 7, 3, 8, 7, 11, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 4, 8, 4, 7, 8, 10, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74878_a(world, structureboundingbox, 3, 1, 3, 8, 2, 11);
                this.func_74878_a(world, structureboundingbox, 4, 3, 6, 7, 3, 9);
                this.func_74878_a(world, structureboundingbox, 2, 4, 2, 9, 5, 12);
                this.func_74878_a(world, structureboundingbox, 4, 6, 5, 7, 6, 9);
                this.func_74878_a(world, structureboundingbox, 5, 7, 6, 6, 7, 8);
                this.func_74878_a(world, structureboundingbox, 5, 1, 2, 6, 2, 2);
                this.func_74878_a(world, structureboundingbox, 5, 2, 12, 6, 2, 12);
                this.func_74878_a(world, structureboundingbox, 5, 5, 1, 6, 5, 1);
                this.func_74878_a(world, structureboundingbox, 5, 5, 13, 6, 5, 13);
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 1, 5, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 10, 5, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 1, 5, 9, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 10, 5, 9, structureboundingbox);

                int i;

                for (i = 0; i <= 14; i += 14) {
                    this.func_74882_a(world, structureboundingbox, 2, 4, i, 2, 5, i, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                    this.func_74882_a(world, structureboundingbox, 4, 4, i, 4, 5, i, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                    this.func_74882_a(world, structureboundingbox, 7, 4, i, 7, 5, i, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                    this.func_74882_a(world, structureboundingbox, 9, 4, i, 9, 5, i, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                }

                this.func_74882_a(world, structureboundingbox, 5, 6, 0, 6, 6, 0, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);

                for (i = 0; i <= 11; i += 11) {
                    for (int j = 2; j <= 12; j += 2) {
                        this.func_74882_a(world, structureboundingbox, i, 4, j, i, 5, j, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                    }

                    this.func_74882_a(world, structureboundingbox, i, 6, 5, i, 6, 5, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                    this.func_74882_a(world, structureboundingbox, i, 6, 9, i, 6, 9, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                }

                this.func_74882_a(world, structureboundingbox, 2, 7, 2, 2, 9, 2, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 9, 7, 2, 9, 9, 2, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 2, 7, 12, 2, 9, 12, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 9, 7, 12, 9, 9, 12, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 4, 9, 4, 4, 9, 4, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 7, 9, 4, 7, 9, 4, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 4, 9, 10, 4, 9, 10, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 7, 9, 10, 7, 9, 10, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 5, 9, 7, 6, 9, 7, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                IBlockState iblockdata = Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.EAST);
                IBlockState iblockdata1 = Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.WEST);
                IBlockState iblockdata2 = Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.SOUTH);
                IBlockState iblockdata3 = Blocks.field_150446_ar.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH);

                this.func_175811_a(world, iblockdata3, 5, 9, 6, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 6, 9, 6, structureboundingbox);
                this.func_175811_a(world, iblockdata2, 5, 9, 8, structureboundingbox);
                this.func_175811_a(world, iblockdata2, 6, 9, 8, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 4, 0, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 5, 0, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 6, 0, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 7, 0, 0, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 4, 1, 8, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 4, 2, 9, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 4, 3, 10, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 7, 1, 8, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 7, 2, 9, structureboundingbox);
                this.func_175811_a(world, iblockdata3, 7, 3, 10, structureboundingbox);
                this.func_74882_a(world, structureboundingbox, 4, 1, 9, 4, 1, 9, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 7, 1, 9, 7, 1, 9, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 4, 1, 10, 7, 2, 10, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 5, 4, 5, 6, 4, 5, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_175811_a(world, iblockdata, 4, 4, 5, structureboundingbox);
                this.func_175811_a(world, iblockdata1, 7, 4, 5, structureboundingbox);

                int k;

                for (k = 0; k < 4; ++k) {
                    this.func_175811_a(world, iblockdata2, 5, 0 - k, 6 + k, structureboundingbox);
                    this.func_175811_a(world, iblockdata2, 6, 0 - k, 6 + k, structureboundingbox);
                    this.func_74878_a(world, structureboundingbox, 5, 0 - k, 7 + k, 6, 0 - k, 9 + k);
                }

                this.func_74878_a(world, structureboundingbox, 1, -3, 12, 10, -1, 13);
                this.func_74878_a(world, structureboundingbox, 1, -3, 1, 3, -1, 13);
                this.func_74878_a(world, structureboundingbox, 1, -3, 1, 9, -1, 5);

                for (k = 1; k <= 13; k += 2) {
                    this.func_74882_a(world, structureboundingbox, 1, -3, k, 1, -2, k, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                }

                for (k = 2; k <= 12; k += 2) {
                    this.func_74882_a(world, structureboundingbox, 1, -1, k, 3, -1, k, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                }

                this.func_74882_a(world, structureboundingbox, 2, -2, 1, 5, -2, 1, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 7, -2, 1, 9, -2, 1, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 6, -3, 1, 6, -3, 1, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 6, -1, 1, 6, -1, 1, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_175811_a(world, Blocks.field_150479_bC.func_176223_P().func_177226_a(BlockTripWireHook.field_176264_a, EnumFacing.EAST).func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf(true)), 1, -3, 8, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150479_bC.func_176223_P().func_177226_a(BlockTripWireHook.field_176264_a, EnumFacing.WEST).func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf(true)), 4, -3, 8, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150473_bD.func_176223_P().func_177226_a(BlockTripWire.field_176294_M, Boolean.valueOf(true)), 2, -3, 8, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150473_bD.func_176223_P().func_177226_a(BlockTripWire.field_176294_M, Boolean.valueOf(true)), 3, -3, 8, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 5, -3, 7, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 5, -3, 6, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 5, -3, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 5, -3, 4, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 5, -3, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 5, -3, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 5, -3, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 4, -3, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 3, -3, 1, structureboundingbox);
                if (!this.field_74945_j) {
                    this.field_74945_j = this.func_189419_a(world, structureboundingbox, random, 3, -2, 1, EnumFacing.NORTH, LootTableList.field_189420_m);
                }

                this.func_175811_a(world, Blocks.field_150395_bd.func_176223_P().func_177226_a(BlockVine.field_176279_N, Boolean.valueOf(true)), 3, -2, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150479_bC.func_176223_P().func_177226_a(BlockTripWireHook.field_176264_a, EnumFacing.NORTH).func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf(true)), 7, -3, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150479_bC.func_176223_P().func_177226_a(BlockTripWireHook.field_176264_a, EnumFacing.SOUTH).func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf(true)), 7, -3, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150473_bD.func_176223_P().func_177226_a(BlockTripWire.field_176294_M, Boolean.valueOf(true)), 7, -3, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150473_bD.func_176223_P().func_177226_a(BlockTripWire.field_176294_M, Boolean.valueOf(true)), 7, -3, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150473_bD.func_176223_P().func_177226_a(BlockTripWire.field_176294_M, Boolean.valueOf(true)), 7, -3, 4, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 8, -3, 6, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 9, -3, 6, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 9, -3, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 9, -3, 4, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 9, -2, 4, structureboundingbox);
                if (!this.field_74946_k) {
                    this.field_74946_k = this.func_189419_a(world, structureboundingbox, random, 9, -2, 3, EnumFacing.WEST, LootTableList.field_189420_m);
                }

                this.func_175811_a(world, Blocks.field_150395_bd.func_176223_P().func_177226_a(BlockVine.field_176278_M, Boolean.valueOf(true)), 8, -1, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150395_bd.func_176223_P().func_177226_a(BlockVine.field_176278_M, Boolean.valueOf(true)), 8, -2, 3, structureboundingbox);
                if (!this.field_74947_h) {
                    this.field_74947_h = this.func_186167_a(world, structureboundingbox, random, 8, -3, 3, LootTableList.field_186430_l);
                }

                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 9, -3, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 8, -3, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 4, -3, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 5, -2, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 5, -1, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 6, -3, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 7, -2, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 7, -1, 5, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 8, -3, 5, structureboundingbox);
                this.func_74882_a(world, structureboundingbox, 9, -1, 1, 9, -1, 5, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74878_a(world, structureboundingbox, 8, -3, 8, 10, -1, 10);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176203_a(BlockStoneBrick.field_176252_O), 8, -2, 11, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176203_a(BlockStoneBrick.field_176252_O), 9, -2, 11, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150417_aV.func_176203_a(BlockStoneBrick.field_176252_O), 10, -2, 11, structureboundingbox);
                IBlockState iblockdata4 = Blocks.field_150442_at.func_176223_P().func_177226_a(BlockLever.field_176360_a, BlockLever.EnumOrientation.NORTH);

                this.func_175811_a(world, iblockdata4, 8, -2, 12, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 9, -2, 12, structureboundingbox);
                this.func_175811_a(world, iblockdata4, 10, -2, 12, structureboundingbox);
                this.func_74882_a(world, structureboundingbox, 8, -3, 8, 8, -3, 10, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_74882_a(world, structureboundingbox, 10, -3, 8, 10, -3, 10, false, random, ComponentScatteredFeaturePieces.JunglePyramid.field_74942_n);
                this.func_175811_a(world, Blocks.field_150341_Y.func_176223_P(), 10, -2, 9, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 8, -2, 9, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 8, -2, 10, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150488_af.func_176223_P(), 10, -1, 9, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150320_F.func_176223_P().func_177226_a(BlockPistonBase.field_176387_N, EnumFacing.UP), 9, -2, 8, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150320_F.func_176223_P().func_177226_a(BlockPistonBase.field_176387_N, EnumFacing.WEST), 10, -2, 8, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150320_F.func_176223_P().func_177226_a(BlockPistonBase.field_176387_N, EnumFacing.WEST), 10, -1, 8, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150413_aR.func_176223_P().func_177226_a(BlockRedstoneRepeater.field_185512_D, EnumFacing.NORTH), 10, -2, 10, structureboundingbox);
                if (!this.field_74948_i) {
                    this.field_74948_i = this.func_186167_a(world, structureboundingbox, random, 9, -3, 10, LootTableList.field_186430_l);
                }

                return true;
            }
        }

        static class Stones extends StructureComponent.BlockSelector {

            private Stones() {}

            public void func_75062_a(Random random, int i, int j, int k, boolean flag) {
                if (random.nextFloat() < 0.4F) {
                    this.field_151562_a = Blocks.field_150347_e.func_176223_P();
                } else {
                    this.field_151562_a = Blocks.field_150341_Y.func_176223_P();
                }

            }

            Stones(Object object) {
                this();
            }
        }
    }

    public static class DesertPyramid extends ComponentScatteredFeaturePieces.Feature {

        private final boolean[] field_74940_h = new boolean[4];

        public DesertPyramid() {}

        public DesertPyramid(Random random, int i, int j) {
            super(random, i, 64, j, 21, 15, 21);
        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            super.func_143012_a(nbttagcompound);
            nbttagcompound.func_74757_a("hasPlacedChest0", this.field_74940_h[0]);
            nbttagcompound.func_74757_a("hasPlacedChest1", this.field_74940_h[1]);
            nbttagcompound.func_74757_a("hasPlacedChest2", this.field_74940_h[2]);
            nbttagcompound.func_74757_a("hasPlacedChest3", this.field_74940_h[3]);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            super.func_143011_b(nbttagcompound, definedstructuremanager);
            this.field_74940_h[0] = nbttagcompound.func_74767_n("hasPlacedChest0");
            this.field_74940_h[1] = nbttagcompound.func_74767_n("hasPlacedChest1");
            this.field_74940_h[2] = nbttagcompound.func_74767_n("hasPlacedChest2");
            this.field_74940_h[3] = nbttagcompound.func_74767_n("hasPlacedChest3");
        }

        public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.func_175804_a(world, structureboundingbox, 0, -4, 0, this.field_74939_a - 1, 0, this.field_74938_c - 1, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);

            int i;

            for (i = 1; i <= 9; ++i) {
                this.func_175804_a(world, structureboundingbox, i, i, i, this.field_74939_a - 1 - i, i, this.field_74938_c - 1 - i, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
                this.func_175804_a(world, structureboundingbox, i + 1, i, i + 1, this.field_74939_a - 2 - i, i, this.field_74938_c - 2 - i, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            }

            for (i = 0; i < this.field_74939_a; ++i) {
                for (int j = 0; j < this.field_74938_c; ++j) {
                    boolean flag = true;

                    this.func_175808_b(world, Blocks.field_150322_A.func_176223_P(), i, -5, j, structureboundingbox);
                }
            }

            IBlockState iblockdata = Blocks.field_150372_bz.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.NORTH);
            IBlockState iblockdata1 = Blocks.field_150372_bz.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.SOUTH);
            IBlockState iblockdata2 = Blocks.field_150372_bz.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.EAST);
            IBlockState iblockdata3 = Blocks.field_150372_bz.func_176223_P().func_177226_a(BlockStairs.field_176309_a, EnumFacing.WEST);
            int k = ~EnumDyeColor.ORANGE.func_176767_b() & 15;
            int l = ~EnumDyeColor.BLUE.func_176767_b() & 15;

            this.func_175804_a(world, structureboundingbox, 0, 0, 0, 4, 9, 4, Blocks.field_150322_A.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 1, 10, 1, 3, 10, 3, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175811_a(world, iblockdata, 2, 10, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata1, 2, 10, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata2, 0, 10, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 4, 10, 2, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 5, 0, 0, this.field_74939_a - 1, 9, 4, Blocks.field_150322_A.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 4, 10, 1, this.field_74939_a - 2, 10, 3, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175811_a(world, iblockdata, this.field_74939_a - 3, 10, 0, structureboundingbox);
            this.func_175811_a(world, iblockdata1, this.field_74939_a - 3, 10, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata2, this.field_74939_a - 5, 10, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata3, this.field_74939_a - 1, 10, 2, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 8, 0, 0, 12, 4, 4, Blocks.field_150322_A.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, 1, 0, 11, 3, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 9, 1, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 9, 2, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 9, 3, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 10, 3, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 11, 3, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 11, 2, 1, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 11, 1, 1, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 4, 1, 1, 8, 3, 3, Blocks.field_150322_A.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 4, 1, 2, 8, 2, 2, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 12, 1, 1, 16, 3, 3, Blocks.field_150322_A.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 12, 1, 2, 16, 2, 2, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 4, 5, this.field_74939_a - 6, 4, this.field_74938_c - 6, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, 4, 9, 11, 4, 11, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 8, 1, 8, 8, 3, 8, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175804_a(world, structureboundingbox, 12, 1, 8, 12, 3, 8, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175804_a(world, structureboundingbox, 8, 1, 12, 8, 3, 12, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175804_a(world, structureboundingbox, 12, 1, 12, 12, 3, 12, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175804_a(world, structureboundingbox, 1, 1, 5, 4, 4, 11, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 5, 1, 5, this.field_74939_a - 2, 4, 11, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 6, 7, 9, 6, 7, 11, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 7, 7, 9, this.field_74939_a - 7, 7, 11, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 5, 5, 9, 5, 7, 11, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 6, 5, 9, this.field_74939_a - 6, 7, 11, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 5, 5, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 5, 6, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 6, 6, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), this.field_74939_a - 6, 5, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), this.field_74939_a - 6, 6, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), this.field_74939_a - 7, 6, 10, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 2, 4, 4, 2, 6, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 3, 4, 4, this.field_74939_a - 3, 6, 4, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175811_a(world, iblockdata, 2, 4, 5, structureboundingbox);
            this.func_175811_a(world, iblockdata, 2, 3, 4, structureboundingbox);
            this.func_175811_a(world, iblockdata, this.field_74939_a - 3, 4, 5, structureboundingbox);
            this.func_175811_a(world, iblockdata, this.field_74939_a - 3, 3, 4, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 1, 1, 3, 2, 2, 3, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 3, 1, 3, this.field_74939_a - 2, 2, 3, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150322_A.func_176223_P(), 1, 1, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176223_P(), this.field_74939_a - 2, 1, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.SAND.func_176624_a()), 1, 2, 2, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150333_U.func_176203_a(BlockStoneSlab.EnumType.SAND.func_176624_a()), this.field_74939_a - 2, 2, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata3, 2, 1, 2, structureboundingbox);
            this.func_175811_a(world, iblockdata2, this.field_74939_a - 3, 1, 2, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 4, 3, 5, 4, 3, 18, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 5, 3, 5, this.field_74939_a - 5, 3, 17, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 3, 1, 5, 4, 2, 16, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, this.field_74939_a - 6, 1, 5, this.field_74939_a - 5, 2, 16, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);

            int i1;

            for (i1 = 5; i1 <= 17; i1 += 2) {
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 4, 1, i1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 4, 2, i1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), this.field_74939_a - 5, 1, i1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), this.field_74939_a - 5, 2, i1, structureboundingbox);
            }

            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 10, 0, 7, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 10, 0, 8, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 9, 0, 9, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 11, 0, 9, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 8, 0, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 12, 0, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 7, 0, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 13, 0, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 9, 0, 11, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 11, 0, 11, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 10, 0, 12, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 10, 0, 13, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(l), 10, 0, 10, structureboundingbox);

            for (i1 = 0; i1 <= this.field_74939_a - 1; i1 += this.field_74939_a - 1) {
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 2, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 2, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 2, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 3, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 3, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 3, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 4, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), i1, 4, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 4, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 5, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 5, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 5, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 6, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), i1, 6, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 6, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 7, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 7, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 7, 3, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 8, 1, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 8, 2, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 8, 3, structureboundingbox);
            }

            for (i1 = 2; i1 <= this.field_74939_a - 3; i1 += this.field_74939_a - 3 - 2) {
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1 - 1, 2, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 2, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1 + 1, 2, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1 - 1, 3, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 3, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1 + 1, 3, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1 - 1, 4, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), i1, 4, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1 + 1, 4, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1 - 1, 5, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 5, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1 + 1, 5, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1 - 1, 6, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), i1, 6, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1 + 1, 6, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1 - 1, 7, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1, 7, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), i1 + 1, 7, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1 - 1, 8, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1, 8, 0, structureboundingbox);
                this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), i1 + 1, 8, 0, structureboundingbox);
            }

            this.func_175804_a(world, structureboundingbox, 8, 4, 0, 12, 6, 0, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 8, 6, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 12, 6, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 9, 5, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 10, 5, 0, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150406_ce.func_176203_a(k), 11, 5, 0, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 8, -14, 8, 12, -11, 12, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175804_a(world, structureboundingbox, 8, -10, 8, 12, -10, 12, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), false);
            this.func_175804_a(world, structureboundingbox, 8, -9, 8, 12, -9, 12, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), false);
            this.func_175804_a(world, structureboundingbox, 8, -8, 8, 12, -1, 12, Blocks.field_150322_A.func_176223_P(), Blocks.field_150322_A.func_176223_P(), false);
            this.func_175804_a(world, structureboundingbox, 9, -11, 9, 11, -1, 11, Blocks.field_150350_a.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150456_au.func_176223_P(), 10, -11, 10, structureboundingbox);
            this.func_175804_a(world, structureboundingbox, 9, -13, 9, 11, -13, 11, Blocks.field_150335_W.func_176223_P(), Blocks.field_150350_a.func_176223_P(), false);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 8, -11, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 8, -10, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 7, -10, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 7, -11, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 12, -11, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 12, -10, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 13, -10, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 13, -11, 10, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 10, -11, 8, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 10, -10, 8, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 10, -10, 7, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 10, -11, 7, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 10, -11, 12, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), 10, -10, 12, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.CHISELED.func_176675_a()), 10, -10, 13, structureboundingbox);
            this.func_175811_a(world, Blocks.field_150322_A.func_176203_a(BlockSandStone.EnumType.SMOOTH.func_176675_a()), 10, -11, 13, structureboundingbox);
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumFacing enumdirection = (EnumFacing) iterator.next();

                if (!this.field_74940_h[enumdirection.func_176736_b()]) {
                    int j1 = enumdirection.func_82601_c() * 2;
                    int k1 = enumdirection.func_82599_e() * 2;

                    this.field_74940_h[enumdirection.func_176736_b()] = this.func_186167_a(world, structureboundingbox, random, 10 + j1, -11, 10 + k1, LootTableList.field_186429_k);
                }
            }

            return true;
        }
    }

    abstract static class Feature extends StructureComponent {

        protected int field_74939_a;
        protected int field_74937_b;
        protected int field_74938_c;
        protected int field_74936_d = -1;

        public Feature() {}

        protected Feature(Random random, int i, int j, int k, int l, int i1, int j1) {
            super(0);
            this.field_74939_a = l;
            this.field_74937_b = i1;
            this.field_74938_c = j1;
            this.func_186164_a(EnumFacing.Plane.HORIZONTAL.func_179518_a(random));
            if (this.func_186165_e().func_176740_k() == EnumFacing.Axis.Z) {
                this.field_74887_e = new StructureBoundingBox(i, j, k, i + l - 1, j + i1 - 1, k + j1 - 1);
            } else {
                this.field_74887_e = new StructureBoundingBox(i, j, k, i + j1 - 1, j + i1 - 1, k + l - 1);
            }

        }

        protected void func_143012_a(NBTTagCompound nbttagcompound) {
            nbttagcompound.func_74768_a("Width", this.field_74939_a);
            nbttagcompound.func_74768_a("Height", this.field_74937_b);
            nbttagcompound.func_74768_a("Depth", this.field_74938_c);
            nbttagcompound.func_74768_a("HPos", this.field_74936_d);
        }

        protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
            this.field_74939_a = nbttagcompound.func_74762_e("Width");
            this.field_74937_b = nbttagcompound.func_74762_e("Height");
            this.field_74938_c = nbttagcompound.func_74762_e("Depth");
            this.field_74936_d = nbttagcompound.func_74762_e("HPos");
        }

        protected boolean func_74935_a(World world, StructureBoundingBox structureboundingbox, int i) {
            if (this.field_74936_d >= 0) {
                return true;
            } else {
                int j = 0;
                int k = 0;
                BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                for (int l = this.field_74887_e.field_78896_c; l <= this.field_74887_e.field_78892_f; ++l) {
                    for (int i1 = this.field_74887_e.field_78897_a; i1 <= this.field_74887_e.field_78893_d; ++i1) {
                        blockposition_mutableblockposition.func_181079_c(i1, 64, l);
                        if (structureboundingbox.func_175898_b((Vec3i) blockposition_mutableblockposition)) {
                            j += Math.max(world.func_175672_r(blockposition_mutableblockposition).func_177956_o(), world.field_73011_w.func_76557_i());
                            ++k;
                        }
                    }
                }

                if (k == 0) {
                    return false;
                } else {
                    this.field_74936_d = j / k;
                    this.field_74887_e.func_78886_a(0, this.field_74936_d - this.field_74887_e.field_78895_b + i, 0);
                    return true;
                }
            }
        }
    }
}
