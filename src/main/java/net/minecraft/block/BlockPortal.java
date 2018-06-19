package net.minecraft.block;

import com.google.common.cache.LoadingCache;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.world.PortalCreateEvent;

public class BlockPortal extends BlockBreakable {

    public static final PropertyEnum<EnumFacing.Axis> field_176550_a = PropertyEnum.func_177706_a("axis", EnumFacing.Axis.class, new EnumFacing.Axis[] { EnumFacing.Axis.X, EnumFacing.Axis.Z});
    protected static final AxisAlignedBB field_185683_b = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB field_185684_c = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185685_d = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

    public BlockPortal() {
        super(Material.field_151567_E, false);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPortal.field_176550_a, EnumFacing.Axis.X));
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing.Axis) iblockdata.func_177229_b(BlockPortal.field_176550_a)) {
        case X:
            return BlockPortal.field_185683_b;

        case Y:
        default:
            return BlockPortal.field_185685_d;

        case Z:
            return BlockPortal.field_185684_c;
        }
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        super.func_180650_b(world, blockposition, iblockdata, random);
        if (world.spigotConfig.enableZombiePigmenPortalSpawns && world.field_73011_w.func_76569_d() && world.func_82736_K().func_82766_b("doMobSpawning") && random.nextInt(2000) < world.func_175659_aa().func_151525_a()) { // Spigot
            int i = blockposition.func_177956_o();

            BlockPos blockposition1;

            for (blockposition1 = blockposition; !world.func_180495_p(blockposition1).func_185896_q() && blockposition1.func_177956_o() > 0; blockposition1 = blockposition1.func_177977_b()) {
                ;
            }

            if (i > 0 && !world.func_180495_p(blockposition1.func_177984_a()).func_185915_l()) {
                // CraftBukkit - set spawn reason to NETHER_PORTAL
                Entity entity = ItemMonsterPlacer.spawnCreature(world, EntityList.func_191306_a(EntityPigZombie.class), (double) blockposition1.func_177958_n() + 0.5D, (double) blockposition1.func_177956_o() + 1.1D, (double) blockposition1.func_177952_p() + 0.5D, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NETHER_PORTAL);

                if (entity != null) {
                    entity.field_71088_bW = entity.func_82147_ab();
                }
            }
        }

    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockPortal.field_185506_k;
    }

    public static int func_176549_a(EnumFacing.Axis enumdirection_enumaxis) {
        return enumdirection_enumaxis == EnumFacing.Axis.X ? 1 : (enumdirection_enumaxis == EnumFacing.Axis.Z ? 2 : 0);
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176548_d(World world, BlockPos blockposition) {
        BlockPortal.Size blockportal_shape = new BlockPortal.Size(world, blockposition, EnumFacing.Axis.X);

        if (blockportal_shape.func_150860_b() && blockportal_shape.field_150864_e == 0) {
            // CraftBukkit start - return portalcreator
            return blockportal_shape.createPortal();
            // return true;
        } else {
            BlockPortal.Size blockportal_shape1 = new BlockPortal.Size(world, blockposition, EnumFacing.Axis.Z);

            if (blockportal_shape1.func_150860_b() && blockportal_shape1.field_150864_e == 0) {
                return blockportal_shape1.createPortal();
                // return true;
                // CraftBukkit end
            } else {
                return false;
            }
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing.Axis enumdirection_enumaxis = (EnumFacing.Axis) iblockdata.func_177229_b(BlockPortal.field_176550_a);
        BlockPortal.Size blockportal_shape;

        if (enumdirection_enumaxis == EnumFacing.Axis.X) {
            blockportal_shape = new BlockPortal.Size(world, blockposition, EnumFacing.Axis.X);
            if (!blockportal_shape.func_150860_b() || blockportal_shape.field_150864_e < blockportal_shape.field_150868_h * blockportal_shape.field_150862_g) {
                world.func_175656_a(blockposition, Blocks.field_150350_a.func_176223_P());
            }
        } else if (enumdirection_enumaxis == EnumFacing.Axis.Z) {
            blockportal_shape = new BlockPortal.Size(world, blockposition, EnumFacing.Axis.Z);
            if (!blockportal_shape.func_150860_b() || blockportal_shape.field_150864_e < blockportal_shape.field_150868_h * blockportal_shape.field_150862_g) {
                world.func_175656_a(blockposition, Blocks.field_150350_a.func_176223_P());
            }
        }

    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!entity.func_184218_aH() && !entity.func_184207_aI() && entity.func_184222_aU()) {
            // CraftBukkit start - Entity in portal
            EntityPortalEnterEvent event = new EntityPortalEnterEvent(entity.getBukkitEntity(), new org.bukkit.Location(world.getWorld(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
            world.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
            entity.func_181015_d(blockposition);
        }

    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return ItemStack.field_190927_a;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPortal.field_176550_a, (i & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }

    public int func_176201_c(IBlockState iblockdata) {
        return func_176549_a((EnumFacing.Axis) iblockdata.func_177229_b(BlockPortal.field_176550_a));
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            switch ((EnumFacing.Axis) iblockdata.func_177229_b(BlockPortal.field_176550_a)) {
            case X:
                return iblockdata.func_177226_a(BlockPortal.field_176550_a, EnumFacing.Axis.Z);

            case Z:
                return iblockdata.func_177226_a(BlockPortal.field_176550_a, EnumFacing.Axis.X);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPortal.field_176550_a});
    }

    public BlockPattern.PatternHelper func_181089_f(World world, BlockPos blockposition) {
        EnumFacing.Axis enumdirection_enumaxis = EnumFacing.Axis.Z;
        BlockPortal.Size blockportal_shape = new BlockPortal.Size(world, blockposition, EnumFacing.Axis.X);
        LoadingCache loadingcache = BlockPattern.func_181627_a(world, true);

        if (!blockportal_shape.func_150860_b()) {
            enumdirection_enumaxis = EnumFacing.Axis.X;
            blockportal_shape = new BlockPortal.Size(world, blockposition, EnumFacing.Axis.Z);
        }

        if (!blockportal_shape.func_150860_b()) {
            return new BlockPattern.PatternHelper(blockposition, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        } else {
            int[] aint = new int[EnumFacing.AxisDirection.values().length];
            EnumFacing enumdirection = blockportal_shape.field_150866_c.func_176735_f();
            BlockPos blockposition1 = blockportal_shape.field_150861_f.func_177981_b(blockportal_shape.func_181100_a() - 1);
            EnumFacing.AxisDirection[] aenumdirection_enumaxisdirection = EnumFacing.AxisDirection.values();
            int i = aenumdirection_enumaxisdirection.length;

            int j;

            for (j = 0; j < i; ++j) {
                EnumFacing.AxisDirection enumdirection_enumaxisdirection = aenumdirection_enumaxisdirection[j];
                BlockPattern.PatternHelper shapedetector_shapedetectorcollection = new BlockPattern.PatternHelper(enumdirection.func_176743_c() == enumdirection_enumaxisdirection ? blockposition1 : blockposition1.func_177967_a(blockportal_shape.field_150866_c, blockportal_shape.func_181101_b() - 1), EnumFacing.func_181076_a(enumdirection_enumaxisdirection, enumdirection_enumaxis), EnumFacing.UP, loadingcache, blockportal_shape.func_181101_b(), blockportal_shape.func_181100_a(), 1);

                for (int k = 0; k < blockportal_shape.func_181101_b(); ++k) {
                    for (int l = 0; l < blockportal_shape.func_181100_a(); ++l) {
                        BlockWorldState shapedetectorblock = shapedetector_shapedetectorcollection.func_177670_a(k, l, 1);

                        if (shapedetectorblock.func_177509_a() != null && shapedetectorblock.func_177509_a().func_185904_a() != Material.field_151579_a) {
                            ++aint[enumdirection_enumaxisdirection.ordinal()];
                        }
                    }
                }
            }

            EnumFacing.AxisDirection enumdirection_enumaxisdirection1 = EnumFacing.AxisDirection.POSITIVE;
            EnumFacing.AxisDirection[] aenumdirection_enumaxisdirection1 = EnumFacing.AxisDirection.values();

            j = aenumdirection_enumaxisdirection1.length;

            for (int i1 = 0; i1 < j; ++i1) {
                EnumFacing.AxisDirection enumdirection_enumaxisdirection2 = aenumdirection_enumaxisdirection1[i1];

                if (aint[enumdirection_enumaxisdirection2.ordinal()] < aint[enumdirection_enumaxisdirection1.ordinal()]) {
                    enumdirection_enumaxisdirection1 = enumdirection_enumaxisdirection2;
                }
            }

            return new BlockPattern.PatternHelper(enumdirection.func_176743_c() == enumdirection_enumaxisdirection1 ? blockposition1 : blockposition1.func_177967_a(blockportal_shape.field_150866_c, blockportal_shape.func_181101_b() - 1), EnumFacing.func_181076_a(enumdirection_enumaxisdirection1, enumdirection_enumaxis), EnumFacing.UP, loadingcache, blockportal_shape.func_181101_b(), blockportal_shape.func_181100_a(), 1);
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static class Size {

        private final World field_150867_a;
        private final EnumFacing.Axis field_150865_b;
        private final EnumFacing field_150866_c;
        private final EnumFacing field_150863_d;
        private int field_150864_e;
        private BlockPos field_150861_f;
        private int field_150862_g;
        private int field_150868_h;
        java.util.Collection<org.bukkit.block.Block> blocks = new java.util.HashSet<org.bukkit.block.Block>(); // CraftBukkit - add field

        public Size(World world, BlockPos blockposition, EnumFacing.Axis enumdirection_enumaxis) {
            this.field_150867_a = world;
            this.field_150865_b = enumdirection_enumaxis;
            if (enumdirection_enumaxis == EnumFacing.Axis.X) {
                this.field_150863_d = EnumFacing.EAST;
                this.field_150866_c = EnumFacing.WEST;
            } else {
                this.field_150863_d = EnumFacing.NORTH;
                this.field_150866_c = EnumFacing.SOUTH;
            }

            for (BlockPos blockposition1 = blockposition; blockposition.func_177956_o() > blockposition1.func_177956_o() - 21 && blockposition.func_177956_o() > 0 && this.func_150857_a(world.func_180495_p(blockposition.func_177977_b()).func_177230_c()); blockposition = blockposition.func_177977_b()) {
                ;
            }

            int i = this.func_180120_a(blockposition, this.field_150863_d) - 1;

            if (i >= 0) {
                this.field_150861_f = blockposition.func_177967_a(this.field_150863_d, i);
                this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);
                if (this.field_150868_h < 2 || this.field_150868_h > 21) {
                    this.field_150861_f = null;
                    this.field_150868_h = 0;
                }
            }

            if (this.field_150861_f != null) {
                this.field_150862_g = this.func_150858_a();
            }

        }

        protected int func_180120_a(BlockPos blockposition, EnumFacing enumdirection) {
            int i;

            for (i = 0; i < 22; ++i) {
                BlockPos blockposition1 = blockposition.func_177967_a(enumdirection, i);

                if (!this.func_150857_a(this.field_150867_a.func_180495_p(blockposition1).func_177230_c()) || this.field_150867_a.func_180495_p(blockposition1.func_177977_b()).func_177230_c() != Blocks.field_150343_Z) {
                    break;
                }
            }

            Block block = this.field_150867_a.func_180495_p(blockposition.func_177967_a(enumdirection, i)).func_177230_c();

            return block == Blocks.field_150343_Z ? i : 0;
        }

        public int func_181100_a() {
            return this.field_150862_g;
        }

        public int func_181101_b() {
            return this.field_150868_h;
        }

        protected int func_150858_a() {
            // CraftBukkit start
            this.blocks.clear();
            org.bukkit.World bworld = this.field_150867_a.getWorld();
            // CraftBukkit end
            int i;

            label56:
            for (this.field_150862_g = 0; this.field_150862_g < 21; ++this.field_150862_g) {
                for (i = 0; i < this.field_150868_h; ++i) {
                    BlockPos blockposition = this.field_150861_f.func_177967_a(this.field_150866_c, i).func_177981_b(this.field_150862_g);
                    Block block = this.field_150867_a.func_180495_p(blockposition).func_177230_c();

                    if (!this.func_150857_a(block)) {
                        break label56;
                    }

                    if (block == Blocks.field_150427_aO) {
                        ++this.field_150864_e;
                    }

                    if (i == 0) {
                        block = this.field_150867_a.func_180495_p(blockposition.func_177972_a(this.field_150863_d)).func_177230_c();
                        if (block != Blocks.field_150343_Z) {
                            break label56;
                            // CraftBukkit start - add the block to our list
                        } else {
                            BlockPos pos = blockposition.func_177972_a(this.field_150863_d);
                            blocks.add(bworld.getBlockAt(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()));
                            // CraftBukkit end
                        }
                    } else if (i == this.field_150868_h - 1) {
                        block = this.field_150867_a.func_180495_p(blockposition.func_177972_a(this.field_150866_c)).func_177230_c();
                        if (block != Blocks.field_150343_Z) {
                            break label56;
                            // CraftBukkit start - add the block to our list
                        } else {
                            BlockPos pos = blockposition.func_177972_a(this.field_150866_c);
                            blocks.add(bworld.getBlockAt(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()));
                            // CraftBukkit end
                        }
                    }
                }
            }

            for (i = 0; i < this.field_150868_h; ++i) {
                if (this.field_150867_a.func_180495_p(this.field_150861_f.func_177967_a(this.field_150866_c, i).func_177981_b(this.field_150862_g)).func_177230_c() != Blocks.field_150343_Z) {
                    this.field_150862_g = 0;
                    break;
                    // CraftBukkit start - add the block to our list
                } else {
                    BlockPos pos = this.field_150861_f.func_177967_a(this.field_150866_c, i).func_177981_b(this.field_150862_g);
                    blocks.add(bworld.getBlockAt(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()));
                    // CraftBukkit end
                }
            }

            if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
                return this.field_150862_g;
            } else {
                this.field_150861_f = null;
                this.field_150868_h = 0;
                this.field_150862_g = 0;
                return 0;
            }
        }

        protected boolean func_150857_a(Block block) {
            return block.field_149764_J == Material.field_151579_a || block == Blocks.field_150480_ab || block == Blocks.field_150427_aO;
        }

        public boolean func_150860_b() {
            return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
        }

        // CraftBukkit start - return boolean
        public boolean createPortal() {
            org.bukkit.World bworld = this.field_150867_a.getWorld();

            // Copy below for loop
            for (int i = 0; i < this.field_150868_h; ++i) {
                BlockPos blockposition = this.field_150861_f.func_177967_a(this.field_150866_c, i);

                for (int j = 0; j < this.field_150862_g; ++j) {
                    BlockPos pos = blockposition.func_177981_b(j);
                    blocks.add(bworld.getBlockAt(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()));
                }
            }

            PortalCreateEvent event = new PortalCreateEvent(blocks, bworld, PortalCreateEvent.CreateReason.FIRE);
            this.field_150867_a.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
            // CraftBukkit end
            for (int i = 0; i < this.field_150868_h; ++i) {
                BlockPos blockposition = this.field_150861_f.func_177967_a(this.field_150866_c, i);

                for (int j = 0; j < this.field_150862_g; ++j) {
                    this.field_150867_a.func_180501_a(blockposition.func_177981_b(j), Blocks.field_150427_aO.func_176223_P().func_177226_a(BlockPortal.field_176550_a, this.field_150865_b), 2);
                }
            }

            return true; // CraftBukkit
        }
    }
}
