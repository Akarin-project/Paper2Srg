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
        this.func_77625_d(1);
        this.func_77656_e(238);
        this.func_77637_a(CreativeTabs.field_78040_i);
    }

    public boolean func_179218_a(ItemStack itemstack, World world, IBlockState iblockdata, BlockPos blockposition, EntityLivingBase entityliving) {
        if (!world.field_72995_K) {
            itemstack.func_77972_a(1, entityliving);
        }

        Block block = iblockdata.func_177230_c();

        return iblockdata.func_185904_a() != Material.field_151584_j && block != Blocks.field_150321_G && block != Blocks.field_150329_H && block != Blocks.field_150395_bd && block != Blocks.field_150473_bD && block != Blocks.field_150325_L ? super.func_179218_a(itemstack, world, iblockdata, blockposition, entityliving) : true;
    }

    public boolean func_150897_b(IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();

        return block == Blocks.field_150321_G || block == Blocks.field_150488_af || block == Blocks.field_150473_bD;
    }

    public float func_150893_a(ItemStack itemstack, IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();

        return block != Blocks.field_150321_G && iblockdata.func_185904_a() != Material.field_151584_j ? (block == Blocks.field_150325_L ? 5.0F : super.func_150893_a(itemstack, iblockdata)) : 15.0F;
    }
}
