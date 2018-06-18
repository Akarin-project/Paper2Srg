package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;


public class BlockBeacon extends BlockContainer {

    public BlockBeacon() {
        super(Material.GLASS, MapColor.DIAMOND);
        this.setHardness(3.0F);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityBeacon();
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityBeacon) {
                entityhuman.displayGUIChest((TileEntityBeacon) tileentity);
                entityhuman.addStat(StatList.BEACON_INTERACTION);
            }

            return true;
        }
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        super.onBlockPlacedBy(world, blockposition, iblockdata, entityliving, itemstack);
        if (itemstack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityBeacon) {
                ((TileEntityBeacon) tileentity).setName(itemstack.getDisplayName());
            }
        }

    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityBeacon) {
            ((TileEntityBeacon) tileentity).updateBeacon();
            world.addBlockEvent(blockposition, this, 1, 0);
        }

    }

    public static void updateColorAsync(final World world, final BlockPos blockposition) {
        /*HttpUtilities.a.submit(new Runnable() {
            public void run() {*/ // Paper
                Chunk chunk = world.getChunkFromBlockCoords(blockposition);

                for (int i = blockposition.getY() - 1; i >= 0; --i) {
                    final BlockPos blockposition1 = new BlockPos(blockposition.getX(), i, blockposition.getZ());

                    if (!chunk.canSeeSky(blockposition1)) {
                        break;
                    }

                    IBlockState iblockdata = world.getBlockState(blockposition1);

                    if (iblockdata.getBlock() == Blocks.BEACON) {
                        /*((WorldServer) world).postToMainThread(new Runnable() {
                            public void run() {*/ // Paper
                                TileEntity tileentity = world.getTileEntity(blockposition);

                                if (tileentity instanceof TileEntityBeacon) {
                                    ((TileEntityBeacon) tileentity).updateBeacon();
                                    world.addBlockEvent(blockposition, Blocks.BEACON, 1, 0);
                                }

                            /*}
                        });*/ // Paper
                    }
                }

            /*}
        });*/ // Paper
    }
}
