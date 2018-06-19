package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

// CraftBukkit start
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public class BlockSkull extends BlockContainer {

    public static final PropertyDirection field_176418_a = BlockDirectional.field_176387_N;
    public static final PropertyBool field_176417_b = PropertyBool.func_177716_a("nodrop");
    private static final Predicate<BlockWorldState> field_176419_M = new Predicate() {
        public boolean a(@Nullable BlockWorldState shapedetectorblock) {
            return shapedetectorblock.func_177509_a() != null && shapedetectorblock.func_177509_a().func_177230_c() == Blocks.field_150465_bP && shapedetectorblock.func_177507_b() instanceof TileEntitySkull && ((TileEntitySkull) shapedetectorblock.func_177507_b()).func_145904_a() == 1;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((BlockWorldState) object);
        }
    };
    protected static final AxisAlignedBB field_185582_c = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
    protected static final AxisAlignedBB field_185583_d = new AxisAlignedBB(0.25D, 0.25D, 0.5D, 0.75D, 0.75D, 1.0D);
    protected static final AxisAlignedBB field_185584_e = new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.5D);
    protected static final AxisAlignedBB field_185585_f = new AxisAlignedBB(0.5D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
    protected static final AxisAlignedBB field_185586_g = new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.5D, 0.75D, 0.75D);
    private BlockPattern field_176420_N;
    private BlockPattern field_176421_O;

    protected BlockSkull() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockSkull.field_176418_a, EnumFacing.NORTH).func_177226_a(BlockSkull.field_176417_b, Boolean.valueOf(false)));
    }

    public String func_149732_F() {
        return I18n.func_74838_a("tile.skull.skeleton.name");
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.func_177229_b(BlockSkull.field_176418_a)) {
        case UP:
        default:
            return BlockSkull.field_185582_c;

        case NORTH:
            return BlockSkull.field_185583_d;

        case SOUTH:
            return BlockSkull.field_185584_e;

        case WEST:
            return BlockSkull.field_185585_f;

        case EAST:
            return BlockSkull.field_185586_g;
        }
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockSkull.field_176418_a, entityliving.func_174811_aO()).func_177226_a(BlockSkull.field_176417_b, Boolean.valueOf(false));
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntitySkull();
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = 0;
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntitySkull) {
            i = ((TileEntitySkull) tileentity).func_145904_a();
        }

        return new ItemStack(Items.field_151144_bL, 1, i);
    }

    // CraftBukkit start - Special case dropping so we can get info from the tile entity
    @Override
    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (world.field_73012_v.nextFloat() < f) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntitySkull) {
                TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;
                ItemStack itemstack = this.func_185473_a(world, blockposition, iblockdata);

                if (tileentityskull.func_145904_a() == 3 && tileentityskull.func_152108_a() != null) {
                    itemstack.func_77982_d(new NBTTagCompound());
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    NBTUtil.func_180708_a(nbttagcompound, tileentityskull.func_152108_a());
                    itemstack.func_77978_p().func_74782_a("SkullOwner", nbttagcompound);
                }

                func_180635_a(world, blockposition, itemstack);
            }
        }
    }
    // CraftBukkit end

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (entityhuman.field_71075_bZ.field_75098_d) {
            iblockdata = iblockdata.func_177226_a(BlockSkull.field_176417_b, Boolean.valueOf(true));
            world.func_180501_a(blockposition, iblockdata, 4);
        }

        super.func_176208_a(world, blockposition, iblockdata, entityhuman);
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K) {
            // CraftBukkit start - Drop item in code above, not here
            // if (!((Boolean) iblockdata.get(BlockSkull.NODROP)).booleanValue()) {
            if (false) {
                // CraftBukkit end
                TileEntity tileentity = world.func_175625_s(blockposition);

                if (tileentity instanceof TileEntitySkull) {
                    TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;
                    ItemStack itemstack = this.func_185473_a(world, blockposition, iblockdata);

                    if (tileentityskull.func_145904_a() == 3 && tileentityskull.func_152108_a() != null) {
                        itemstack.func_77982_d(new NBTTagCompound());
                        NBTTagCompound nbttagcompound = new NBTTagCompound();

                        NBTUtil.func_180708_a(nbttagcompound, tileentityskull.func_152108_a());
                        itemstack.func_77978_p().func_74782_a("SkullOwner", nbttagcompound);
                    }

                    func_180635_a(world, blockposition, itemstack);
                }
            }

            super.func_180663_b(world, blockposition, iblockdata);
        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151144_bL;
    }

    public boolean func_176415_b(World world, BlockPos blockposition, ItemStack itemstack) {
        return itemstack.func_77960_j() == 1 && blockposition.func_177956_o() >= 2 && world.func_175659_aa() != EnumDifficulty.PEACEFUL && !world.field_72995_K ? this.func_176414_j().func_177681_a(world, blockposition) != null : false;
    }

    public void func_180679_a(World world, BlockPos blockposition, TileEntitySkull tileentityskull) {
        if (world.captureBlockStates) return; // CraftBukkit
        if (tileentityskull.func_145904_a() == 1 && blockposition.func_177956_o() >= 2 && world.func_175659_aa() != EnumDifficulty.PEACEFUL && !world.field_72995_K) {
            BlockPattern shapedetector = this.func_176416_l();
            BlockPattern.PatternHelper shapedetector_shapedetectorcollection = shapedetector.func_177681_a(world, blockposition);

            if (shapedetector_shapedetectorcollection != null) {
                // CraftBukkit start - Use BlockStateListPopulator
                BlockStateListPopulator blockList = new BlockStateListPopulator(world.getWorld());
                int i;

                for (i = 0; i < 3; ++i) {
                    BlockWorldState shapedetectorblock = shapedetector_shapedetectorcollection.func_177670_a(i, 0, 0);

                    // CraftBukkit start
                    // world.setTypeAndData(shapedetectorblock.getPosition(), shapedetectorblock.a().set(BlockSkull.NODROP, Boolean.valueOf(true)), 2);
                    BlockPos pos = shapedetectorblock.func_177508_d();
                    IBlockState data = shapedetectorblock.func_177509_a().func_177226_a(BlockSkull.field_176417_b, Boolean.valueOf(true));
                    blockList.setTypeAndData(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), data.func_177230_c(), data.func_177230_c().func_176201_c(data), 2);
                    // CraftBukkit end
                }

                for (i = 0; i < shapedetector.func_177684_c(); ++i) {
                    for (int j = 0; j < shapedetector.func_177685_b(); ++j) {
                        BlockWorldState shapedetectorblock1 = shapedetector_shapedetectorcollection.func_177670_a(i, j, 0);

                        // CraftBukkit start
                        // world.setTypeAndData(shapedetectorblock1.getPosition(), Blocks.AIR.getBlockData(), 2);
                        BlockPos pos = shapedetectorblock1.func_177508_d();
                        blockList.setTypeAndData(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), Blocks.field_150350_a, 0, 2);
                        // CraftBukkit end
                    }
                }

                BlockPos blockposition1 = shapedetector_shapedetectorcollection.func_177670_a(1, 0, 0).func_177508_d();
                EntityWither entitywither = new EntityWither(world);
                BlockPos blockposition2 = shapedetector_shapedetectorcollection.func_177670_a(1, 2, 0).func_177508_d();

                entitywither.func_70012_b((double) blockposition2.func_177958_n() + 0.5D, (double) blockposition2.func_177956_o() + 0.55D, (double) blockposition2.func_177952_p() + 0.5D, shapedetector_shapedetectorcollection.func_177669_b().func_176740_k() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
                entitywither.field_70761_aq = shapedetector_shapedetectorcollection.func_177669_b().func_176740_k() == EnumFacing.Axis.X ? 0.0F : 90.0F;
                entitywither.func_82206_m();
                Iterator iterator = world.func_72872_a(EntityPlayerMP.class, entitywither.func_174813_aQ().func_186662_g(50.0D)).iterator();

                // CraftBukkit start
                if (world.addEntity(entitywither, SpawnReason.BUILD_WITHER)) {
                    blockList.updateList();

                while (iterator.hasNext()) {
                    EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                    CriteriaTriggers.field_192133_m.func_192229_a(entityplayer, (Entity) entitywither);
                }

                int k;

                for (k = 0; k < 120; ++k) {
                    world.func_175688_a(EnumParticleTypes.SNOWBALL, (double) blockposition1.func_177958_n() + world.field_73012_v.nextDouble(), (double) (blockposition1.func_177956_o() - 2) + world.field_73012_v.nextDouble() * 3.9D, (double) blockposition1.func_177952_p() + world.field_73012_v.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
                }

                for (k = 0; k < shapedetector.func_177684_c(); ++k) {
                    for (int l = 0; l < shapedetector.func_177685_b(); ++l) {
                        BlockWorldState shapedetectorblock2 = shapedetector_shapedetectorcollection.func_177670_a(k, l, 0);

                        world.func_175722_b(shapedetectorblock2.func_177508_d(), Blocks.field_150350_a, false);
                    }
                }
                } // CraftBukkit end

            }
        }
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockSkull.field_176418_a, EnumFacing.func_82600_a(i & 7)).func_177226_a(BlockSkull.field_176417_b, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockSkull.field_176418_a)).func_176745_a();

        if (((Boolean) iblockdata.func_177229_b(BlockSkull.field_176417_b)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockSkull.field_176418_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockSkull.field_176418_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockSkull.field_176418_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockSkull.field_176418_a, BlockSkull.field_176417_b});
    }

    protected BlockPattern func_176414_j() {
        if (this.field_176420_N == null) {
            this.field_176420_N = FactoryBlockPattern.func_177660_a().func_177659_a(new String[] { "   ", "###", "~#~"}).func_177662_a('#', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150425_aM))).func_177662_a('~', BlockWorldState.func_177510_a(BlockMaterialMatcher.func_189886_a(Material.field_151579_a))).func_177661_b();
        }

        return this.field_176420_N;
    }

    protected BlockPattern func_176416_l() {
        if (this.field_176421_O == null) {
            this.field_176421_O = FactoryBlockPattern.func_177660_a().func_177659_a(new String[] { "^^^", "###", "~#~"}).func_177662_a('#', BlockWorldState.func_177510_a(BlockStateMatcher.func_177638_a(Blocks.field_150425_aM))).func_177662_a('^', BlockSkull.field_176419_M).func_177662_a('~', BlockWorldState.func_177510_a(BlockMaterialMatcher.func_189886_a(Material.field_151579_a))).func_177661_b();
        }

        return this.field_176421_O;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
