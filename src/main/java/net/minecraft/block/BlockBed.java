package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBed extends BlockHorizontal implements ITileEntityProvider {

    public static final PropertyEnum<BlockBed.EnumPartType> field_176472_a = PropertyEnum.func_177709_a("part", BlockBed.EnumPartType.class);
    public static final PropertyBool field_176471_b = PropertyBool.func_177716_a("occupied");
    protected static final AxisAlignedBB field_185513_c = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);

    public BlockBed() {
        super(Material.field_151580_n);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockBed.field_176472_a, BlockBed.EnumPartType.FOOT).func_177226_a(BlockBed.field_176471_b, Boolean.valueOf(false)));
        this.field_149758_A = true;
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.FOOT) {
            TileEntity tileentity = iblockaccess.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityBed) {
                EnumDyeColor enumcolor = ((TileEntityBed) tileentity).func_193048_a();

                return MapColor.func_193558_a(enumcolor);
            }
        }

        return MapColor.field_151659_e;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            if (iblockdata.func_177229_b(BlockBed.field_176472_a) != BlockBed.EnumPartType.HEAD) {
                blockposition = blockposition.func_177972_a((EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D));
                iblockdata = world.func_180495_p(blockposition);
                if (iblockdata.func_177230_c() != this) {
                    return true;
                }
            }

            if (world.field_73011_w.func_76567_e() && world.func_180494_b(blockposition) != Biomes.field_76778_j) {
                if (((Boolean) iblockdata.func_177229_b(BlockBed.field_176471_b)).booleanValue()) {
                    EntityPlayer entityhuman1 = this.func_176470_e(world, blockposition);

                    if (entityhuman1 != null) {
                        entityhuman.func_146105_b((ITextComponent) (new TextComponentTranslation("tile.bed.occupied", new Object[0])), true);
                        return true;
                    }

                    iblockdata = iblockdata.func_177226_a(BlockBed.field_176471_b, Boolean.valueOf(false));
                    world.func_180501_a(blockposition, iblockdata, 4);
                }

                EntityPlayer.SleepResult entityhuman_enumbedresult = entityhuman.func_180469_a(blockposition);

                if (entityhuman_enumbedresult == EntityPlayer.SleepResult.OK) {
                    iblockdata = iblockdata.func_177226_a(BlockBed.field_176471_b, Boolean.valueOf(true));
                    world.func_180501_a(blockposition, iblockdata, 4);
                    return true;
                } else {
                    if (entityhuman_enumbedresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
                        entityhuman.func_146105_b((ITextComponent) (new TextComponentTranslation("tile.bed.noSleep", new Object[0])), true);
                    } else if (entityhuman_enumbedresult == EntityPlayer.SleepResult.NOT_SAFE) {
                        entityhuman.func_146105_b((ITextComponent) (new TextComponentTranslation("tile.bed.notSafe", new Object[0])), true);
                    } else if (entityhuman_enumbedresult == EntityPlayer.SleepResult.TOO_FAR_AWAY) {
                        entityhuman.func_146105_b((ITextComponent) (new TextComponentTranslation("tile.bed.tooFarAway", new Object[0])), true);
                    }

                    return true;
                }
            } else {
                world.func_175698_g(blockposition);
                BlockPos blockposition1 = blockposition.func_177972_a(((EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D)).func_176734_d());

                if (world.func_180495_p(blockposition1).func_177230_c() == this) {
                    world.func_175698_g(blockposition1);
                }

                world.func_72885_a((Entity) null, (double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }

    @Nullable
    private EntityPlayer func_176470_e(World world, BlockPos blockposition) {
        Iterator iterator = world.field_73010_i.iterator();

        EntityPlayer entityhuman;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entityhuman = (EntityPlayer) iterator.next();
        } while (!entityhuman.func_70608_bn() || !entityhuman.field_71081_bT.equals(blockposition));

        return entityhuman;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public void func_180658_a(World world, BlockPos blockposition, Entity entity, float f) {
        super.func_180658_a(world, blockposition, entity, f * 0.5F);
    }

    public void func_176216_a(World world, Entity entity) {
        if (entity.func_70093_af()) {
            super.func_176216_a(world, entity);
        } else if (entity.field_70181_x < 0.0D) {
            entity.field_70181_x = -entity.field_70181_x * 0.6600000262260437D;
            if (!(entity instanceof EntityLivingBase)) {
                entity.field_70181_x *= 0.8D;
            }
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D);

        if (iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.FOOT) {
            if (world.func_180495_p(blockposition.func_177972_a(enumdirection)).func_177230_c() != this) {
                world.func_175698_g(blockposition);
            }
        } else if (world.func_180495_p(blockposition.func_177972_a(enumdirection.func_176734_d())).func_177230_c() != this) {
            if (!world.field_72995_K) {
                this.func_176226_b(world, blockposition, iblockdata, 0);
            }

            world.func_175698_g(blockposition);
        }

    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.FOOT ? Items.field_190931_a : Items.field_151104_aV;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockBed.field_185513_c;
    }

    @Nullable
    public static BlockPos func_176468_a(World world, BlockPos blockposition, int i) {
        EnumFacing enumdirection = (EnumFacing) world.func_180495_p(blockposition).func_177229_b(BlockBed.field_185512_D);
        int j = blockposition.func_177958_n();
        int k = blockposition.func_177956_o();
        int l = blockposition.func_177952_p();

        for (int i1 = 0; i1 <= 1; ++i1) {
            int j1 = j - enumdirection.func_82601_c() * i1 - 1;
            int k1 = l - enumdirection.func_82599_e() * i1 - 1;
            int l1 = j1 + 2;
            int i2 = k1 + 2;

            for (int j2 = j1; j2 <= l1; ++j2) {
                for (int k2 = k1; k2 <= i2; ++k2) {
                    BlockPos blockposition1 = new BlockPos(j2, k, k2);

                    if (func_176469_d(world, blockposition1)) {
                        if (i <= 0) {
                            return blockposition1;
                        }

                        --i;
                    }
                }
            }
        }

        return null;
    }

    protected static boolean func_176469_d(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177977_b()).func_185896_q() && !world.func_180495_p(blockposition).func_185904_a().func_76220_a() && !world.func_180495_p(blockposition.func_177984_a()).func_185904_a().func_76220_a();
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.HEAD) {
            TileEntity tileentity = world.func_175625_s(blockposition);
            EnumDyeColor enumcolor = tileentity instanceof TileEntityBed ? ((TileEntityBed) tileentity).func_193048_a() : EnumDyeColor.RED;

            func_180635_a(world, blockposition, new ItemStack(Items.field_151104_aV, 1, enumcolor.func_176765_a()));
        }

    }

    public EnumPushReaction func_149656_h(IBlockState iblockdata) {
        return EnumPushReaction.DESTROY;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        BlockPos blockposition1 = blockposition;

        if (iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.FOOT) {
            blockposition1 = blockposition.func_177972_a((EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D));
        }

        TileEntity tileentity = world.func_175625_s(blockposition1);
        EnumDyeColor enumcolor = tileentity instanceof TileEntityBed ? ((TileEntityBed) tileentity).func_193048_a() : EnumDyeColor.RED;

        return new ItemStack(Items.field_151104_aV, 1, enumcolor.func_176765_a());
    }

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (entityhuman.field_71075_bZ.field_75098_d && iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.FOOT) {
            BlockPos blockposition1 = blockposition.func_177972_a((EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D));

            if (world.func_180495_p(blockposition1).func_177230_c() == this) {
                world.func_175698_g(blockposition1);
            }
        }

    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, TileEntity tileentity, ItemStack itemstack) {
        if (iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.HEAD && tileentity instanceof TileEntityBed) {
            TileEntityBed tileentitybed = (TileEntityBed) tileentity;
            ItemStack itemstack1 = tileentitybed.func_193049_f();

            func_180635_a(world, blockposition, itemstack1);
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, (TileEntity) null, itemstack);
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_180663_b(world, blockposition, iblockdata);
        world.func_175713_t(blockposition);
    }

    public IBlockState func_176203_a(int i) {
        EnumFacing enumdirection = EnumFacing.func_176731_b(i);

        return (i & 8) > 0 ? this.func_176223_P().func_177226_a(BlockBed.field_176472_a, BlockBed.EnumPartType.HEAD).func_177226_a(BlockBed.field_185512_D, enumdirection).func_177226_a(BlockBed.field_176471_b, Boolean.valueOf((i & 4) > 0)) : this.func_176223_P().func_177226_a(BlockBed.field_176472_a, BlockBed.EnumPartType.FOOT).func_177226_a(BlockBed.field_185512_D, enumdirection);
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.FOOT) {
            IBlockState iblockdata1 = iblockaccess.func_180495_p(blockposition.func_177972_a((EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D)));

            if (iblockdata1.func_177230_c() == this) {
                iblockdata = iblockdata.func_177226_a(BlockBed.field_176471_b, iblockdata1.func_177229_b(BlockBed.field_176471_b));
            }
        }

        return iblockdata;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockBed.field_185512_D, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D)));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockBed.field_185512_D)).func_176736_b();

        if (iblockdata.func_177229_b(BlockBed.field_176472_a) == BlockBed.EnumPartType.HEAD) {
            i |= 8;
            if (((Boolean) iblockdata.func_177229_b(BlockBed.field_176471_b)).booleanValue()) {
                i |= 4;
            }
        }

        return i;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockBed.field_185512_D, BlockBed.field_176472_a, BlockBed.field_176471_b});
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityBed();
    }

    public static enum EnumPartType implements IStringSerializable {

        HEAD("head"), FOOT("foot");

        private final String field_177036_c;

        private EnumPartType(String s) {
            this.field_177036_c = s;
        }

        public String toString() {
            return this.field_177036_c;
        }

        public String func_176610_l() {
            return this.field_177036_c;
        }
    }
}
