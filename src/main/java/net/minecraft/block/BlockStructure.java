package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStructure extends BlockContainer {

    public static final PropertyEnum<TileEntityStructure.Mode> field_185587_a = PropertyEnum.func_177709_a("mode", TileEntityStructure.Mode.class);

    public BlockStructure() {
        super(Material.field_151573_f, MapColor.field_151680_x);
        this.func_180632_j(this.field_176227_L.func_177621_b());
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityStructure();
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        return tileentity instanceof TileEntityStructure ? ((TileEntityStructure) tileentity).func_189701_a(entityhuman) : false;
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        if (!world.field_72995_K) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityStructure) {
                TileEntityStructure tileentitystructure = (TileEntityStructure) tileentity;

                tileentitystructure.func_189720_a(entityliving);
            }
        }
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockStructure.field_185587_a, TileEntityStructure.Mode.DATA);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockStructure.field_185587_a, TileEntityStructure.Mode.func_185108_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((TileEntityStructure.Mode) iblockdata.func_177229_b(BlockStructure.field_185587_a)).func_185110_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockStructure.field_185587_a});
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityStructure) {
                TileEntityStructure tileentitystructure = (TileEntityStructure) tileentity;
                boolean flag = world.func_175640_z(blockposition);
                boolean flag1 = tileentitystructure.func_189722_G();

                if (flag && !flag1) {
                    tileentitystructure.func_189723_d(true);
                    this.func_189874_a(tileentitystructure);
                } else if (!flag && flag1) {
                    tileentitystructure.func_189723_d(false);
                }

            }
        }
    }

    private void func_189874_a(TileEntityStructure tileentitystructure) {
        switch (tileentitystructure.func_189700_k()) {
        case SAVE:
            tileentitystructure.func_189712_b(false);
            break;

        case LOAD:
            tileentitystructure.func_189714_c(false);
            break;

        case CORNER:
            tileentitystructure.func_189706_E();

        case DATA:
        }

    }
}
