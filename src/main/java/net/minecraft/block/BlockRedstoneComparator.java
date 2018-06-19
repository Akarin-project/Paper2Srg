package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider {

    public static final PropertyBool field_176464_a = PropertyBool.func_177716_a("powered");
    public static final PropertyEnum<BlockRedstoneComparator.Mode> field_176463_b = PropertyEnum.func_177709_a("mode", BlockRedstoneComparator.Mode.class);

    public BlockRedstoneComparator(boolean flag) {
        super(flag);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockRedstoneComparator.field_185512_D, EnumFacing.NORTH).func_177226_a(BlockRedstoneComparator.field_176464_a, Boolean.valueOf(false)).func_177226_a(BlockRedstoneComparator.field_176463_b, BlockRedstoneComparator.Mode.COMPARE));
        this.field_149758_A = true;
    }

    public String func_149732_F() {
        return I18n.func_74838_a("item.comparator.name");
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151132_bS;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151132_bS);
    }

    protected int func_176403_d(IBlockState iblockdata) {
        return 2;
    }

    protected IBlockState func_180674_e(IBlockState iblockdata) {
        Boolean obool = (Boolean) iblockdata.func_177229_b(BlockRedstoneComparator.field_176464_a);
        BlockRedstoneComparator.Mode blockredstonecomparator_enumcomparatormode = (BlockRedstoneComparator.Mode) iblockdata.func_177229_b(BlockRedstoneComparator.field_176463_b);
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneComparator.field_185512_D);

        return Blocks.field_150455_bV.func_176223_P().func_177226_a(BlockRedstoneComparator.field_185512_D, enumdirection).func_177226_a(BlockRedstoneComparator.field_176464_a, obool).func_177226_a(BlockRedstoneComparator.field_176463_b, blockredstonecomparator_enumcomparatormode);
    }

    protected IBlockState func_180675_k(IBlockState iblockdata) {
        Boolean obool = (Boolean) iblockdata.func_177229_b(BlockRedstoneComparator.field_176464_a);
        BlockRedstoneComparator.Mode blockredstonecomparator_enumcomparatormode = (BlockRedstoneComparator.Mode) iblockdata.func_177229_b(BlockRedstoneComparator.field_176463_b);
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneComparator.field_185512_D);

        return Blocks.field_150441_bU.func_176223_P().func_177226_a(BlockRedstoneComparator.field_185512_D, enumdirection).func_177226_a(BlockRedstoneComparator.field_176464_a, obool).func_177226_a(BlockRedstoneComparator.field_176463_b, blockredstonecomparator_enumcomparatormode);
    }

    protected boolean func_176406_l(IBlockState iblockdata) {
        return this.field_149914_a || ((Boolean) iblockdata.func_177229_b(BlockRedstoneComparator.field_176464_a)).booleanValue();
    }

    protected int func_176408_a(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = iblockaccess.func_175625_s(blockposition);

        return tileentity instanceof TileEntityComparator ? ((TileEntityComparator) tileentity).func_145996_a() : 0;
    }

    private int func_176460_j(World world, BlockPos blockposition, IBlockState iblockdata) {
        return iblockdata.func_177229_b(BlockRedstoneComparator.field_176463_b) == BlockRedstoneComparator.Mode.SUBTRACT ? Math.max(this.func_176397_f(world, blockposition, iblockdata) - this.func_176407_c((IBlockAccess) world, blockposition, iblockdata), 0) : this.func_176397_f(world, blockposition, iblockdata);
    }

    protected boolean func_176404_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = this.func_176397_f(world, blockposition, iblockdata);

        if (i >= 15) {
            return true;
        } else if (i == 0) {
            return false;
        } else {
            int j = this.func_176407_c((IBlockAccess) world, blockposition, iblockdata);

            return j == 0 ? true : i >= j;
        }
    }

    protected int func_176397_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = super.func_176397_f(world, blockposition, iblockdata);
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneComparator.field_185512_D);
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
        IBlockState iblockdata1 = world.func_180495_p(blockposition1);

        if (iblockdata1.func_185912_n()) {
            i = iblockdata1.func_185888_a(world, blockposition1);
        } else if (i < 15 && iblockdata1.func_185915_l()) {
            blockposition1 = blockposition1.func_177972_a(enumdirection);
            iblockdata1 = world.func_180495_p(blockposition1);
            if (iblockdata1.func_185912_n()) {
                i = iblockdata1.func_185888_a(world, blockposition1);
            } else if (iblockdata1.func_185904_a() == Material.field_151579_a) {
                EntityItemFrame entityitemframe = this.func_176461_a(world, enumdirection, blockposition1);

                if (entityitemframe != null) {
                    i = entityitemframe.func_174866_q();
                }
            }
        }

        return i;
    }

    @Nullable
    private EntityItemFrame func_176461_a(World world, final EnumFacing enumdirection, BlockPos blockposition) {
        List list = world.func_175647_a(EntityItemFrame.class, new AxisAlignedBB((double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p(), (double) (blockposition.func_177958_n() + 1), (double) (blockposition.func_177956_o() + 1), (double) (blockposition.func_177952_p() + 1)), new Predicate() {
            public boolean a(@Nullable Entity entity) {
                return entity != null && entity.func_174811_aO() == enumdirection;
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        });

        return list.size() == 1 ? (EntityItemFrame) list.get(0) : null;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!entityhuman.field_71075_bZ.field_75099_e) {
            return false;
        } else {
            iblockdata = iblockdata.func_177231_a((IProperty) BlockRedstoneComparator.field_176463_b);
            float f3 = iblockdata.func_177229_b(BlockRedstoneComparator.field_176463_b) == BlockRedstoneComparator.Mode.SUBTRACT ? 0.55F : 0.5F;

            world.func_184133_a(entityhuman, blockposition, SoundEvents.field_187556_aj, SoundCategory.BLOCKS, 0.3F, f3);
            world.func_180501_a(blockposition, iblockdata, 2);
            this.func_176462_k(world, blockposition, iblockdata);
            return true;
        }
    }

    protected void func_176398_g(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.func_175691_a(blockposition, (Block) this)) {
            int i = this.func_176460_j(world, blockposition, iblockdata);
            TileEntity tileentity = world.func_175625_s(blockposition);
            int j = tileentity instanceof TileEntityComparator ? ((TileEntityComparator) tileentity).func_145996_a() : 0;

            if (i != j || this.func_176406_l(iblockdata) != this.func_176404_e(world, blockposition, iblockdata)) {
                if (this.func_176402_i(world, blockposition, iblockdata)) {
                    world.func_175654_a(blockposition, this, 2, -1);
                } else {
                    world.func_175654_a(blockposition, this, 2, 0);
                }
            }

        }
    }

    private void func_176462_k(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = this.func_176460_j(world, blockposition, iblockdata);
        TileEntity tileentity = world.func_175625_s(blockposition);
        int j = 0;

        if (tileentity instanceof TileEntityComparator) {
            TileEntityComparator tileentitycomparator = (TileEntityComparator) tileentity;

            j = tileentitycomparator.func_145996_a();
            tileentitycomparator.func_145995_a(i);
        }

        if (j != i || iblockdata.func_177229_b(BlockRedstoneComparator.field_176463_b) == BlockRedstoneComparator.Mode.COMPARE) {
            boolean flag = this.func_176404_e(world, blockposition, iblockdata);
            boolean flag1 = this.func_176406_l(iblockdata);

            if (flag1 && !flag) {
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockRedstoneComparator.field_176464_a, Boolean.valueOf(false)), 2);
            } else if (!flag1 && flag) {
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockRedstoneComparator.field_176464_a, Boolean.valueOf(true)), 2);
            }

            this.func_176400_h(world, blockposition, iblockdata);
        }

    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (this.field_149914_a) {
            world.func_180501_a(blockposition, this.func_180675_k(iblockdata).func_177226_a(BlockRedstoneComparator.field_176464_a, Boolean.valueOf(true)), 4);
        }

        this.func_176462_k(world, blockposition, iblockdata);
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_176213_c(world, blockposition, iblockdata);
        world.func_175690_a(blockposition, this.func_149915_a(world, 0));
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_180663_b(world, blockposition, iblockdata);
        world.func_175713_t(blockposition);
        this.func_176400_h(world, blockposition, iblockdata);
    }

    public boolean func_189539_a(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        super.func_189539_a(iblockdata, world, blockposition, i, j);
        TileEntity tileentity = world.func_175625_s(blockposition);

        return tileentity == null ? false : tileentity.func_145842_c(i, j);
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityComparator();
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockRedstoneComparator.field_185512_D, EnumFacing.func_176731_b(i)).func_177226_a(BlockRedstoneComparator.field_176464_a, Boolean.valueOf((i & 8) > 0)).func_177226_a(BlockRedstoneComparator.field_176463_b, (i & 4) > 0 ? BlockRedstoneComparator.Mode.SUBTRACT : BlockRedstoneComparator.Mode.COMPARE);
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockRedstoneComparator.field_185512_D)).func_176736_b();

        if (((Boolean) iblockdata.func_177229_b(BlockRedstoneComparator.field_176464_a)).booleanValue()) {
            i |= 8;
        }

        if (iblockdata.func_177229_b(BlockRedstoneComparator.field_176463_b) == BlockRedstoneComparator.Mode.SUBTRACT) {
            i |= 4;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockRedstoneComparator.field_185512_D, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockRedstoneComparator.field_185512_D)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockRedstoneComparator.field_185512_D)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockRedstoneComparator.field_185512_D, BlockRedstoneComparator.field_176463_b, BlockRedstoneComparator.field_176464_a});
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockRedstoneComparator.field_185512_D, entityliving.func_174811_aO().func_176734_d()).func_177226_a(BlockRedstoneComparator.field_176464_a, Boolean.valueOf(false)).func_177226_a(BlockRedstoneComparator.field_176463_b, BlockRedstoneComparator.Mode.COMPARE);
    }

    public static enum Mode implements IStringSerializable {

        COMPARE("compare"), SUBTRACT("subtract");

        private final String field_177041_c;

        private Mode(String s) {
            this.field_177041_c = s;
        }

        public String toString() {
            return this.field_177041_c;
        }

        public String func_176610_l() {
            return this.field_177041_c;
        }
    }
}
