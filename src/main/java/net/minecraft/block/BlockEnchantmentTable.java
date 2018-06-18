package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockEnchantmentTable extends BlockContainer {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    protected BlockEnchantmentTable() {
        super(Material.ROCK, MapColor.RED);
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockEnchantmentTable.AABB;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityEnchantmentTable();
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityEnchantmentTable) {
                entityhuman.displayGui((TileEntityEnchantmentTable) tileentity);
            }

            return true;
        }
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        super.onBlockPlacedBy(world, blockposition, iblockdata, entityliving, itemstack);
        if (itemstack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityEnchantmentTable) {
                ((TileEntityEnchantmentTable) tileentity).setCustomName(itemstack.getDisplayName());
            }
        }

    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
