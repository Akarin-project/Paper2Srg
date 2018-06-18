package net.minecraft.item;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemShears extends Item {

    public ItemShears() {
        this.setMaxStackSize(1);
        this.setMaxDamage(238);
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState iblockdata, BlockPos blockposition, EntityLivingBase entityliving) {
        if (!world.isRemote) {
            itemstack.damageItem(1, entityliving);
        }

        Block block = iblockdata.getBlock();

        return iblockdata.getMaterial() != Material.LEAVES && block != Blocks.WEB && block != Blocks.TALLGRASS && block != Blocks.VINE && block != Blocks.TRIPWIRE && block != Blocks.WOOL ? super.onBlockDestroyed(itemstack, world, iblockdata, blockposition, entityliving) : true;
    }

    public boolean canHarvestBlock(IBlockState iblockdata) {
        Block block = iblockdata.getBlock();

        return block == Blocks.WEB || block == Blocks.REDSTONE_WIRE || block == Blocks.TRIPWIRE;
    }

    public float getDestroySpeed(ItemStack itemstack, IBlockState iblockdata) {
        Block block = iblockdata.getBlock();

        return block != Blocks.WEB && iblockdata.getMaterial() != Material.LEAVES ? (block == Blocks.WOOL ? 5.0F : super.getDestroySpeed(itemstack, iblockdata)) : 15.0F;
    }
}
