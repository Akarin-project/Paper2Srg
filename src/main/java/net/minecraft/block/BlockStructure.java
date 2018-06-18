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

    public static final PropertyEnum<TileEntityStructure.Mode> MODE = PropertyEnum.create("mode", TileEntityStructure.Mode.class);

    public BlockStructure() {
        super(Material.IRON, MapColor.SILVER);
        this.setDefaultState(this.blockState.getBaseState());
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityStructure();
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity instanceof TileEntityStructure ? ((TileEntityStructure) tileentity).usedBy(entityhuman) : false;
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        if (!world.isRemote) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityStructure) {
                TileEntityStructure tileentitystructure = (TileEntityStructure) tileentity;

                tileentitystructure.createdBy(entityliving);
            }
        }
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockStructure.MODE, TileEntityStructure.Mode.DATA);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockStructure.MODE, TileEntityStructure.Mode.getById(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((TileEntityStructure.Mode) iblockdata.getValue(BlockStructure.MODE)).getModeId();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockStructure.MODE});
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityStructure) {
                TileEntityStructure tileentitystructure = (TileEntityStructure) tileentity;
                boolean flag = world.isBlockPowered(blockposition);
                boolean flag1 = tileentitystructure.isPowered();

                if (flag && !flag1) {
                    tileentitystructure.setPowered(true);
                    this.trigger(tileentitystructure);
                } else if (!flag && flag1) {
                    tileentitystructure.setPowered(false);
                }

            }
        }
    }

    private void trigger(TileEntityStructure tileentitystructure) {
        switch (tileentitystructure.getMode()) {
        case SAVE:
            tileentitystructure.save(false);
            break;

        case LOAD:
            tileentitystructure.load(false);
            break;

        case CORNER:
            tileentitystructure.unloadStructure();

        case DATA:
        }

    }
}
