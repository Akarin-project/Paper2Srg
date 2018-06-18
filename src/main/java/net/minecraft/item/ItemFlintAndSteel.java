package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ItemFlintAndSteel extends Item {

    public ItemFlintAndSteel() {
        this.maxStackSize = 1;
        this.setMaxDamage(64);
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        blockposition = blockposition.offset(enumdirection);
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            if (world.getBlockState(blockposition).getMaterial() == Material.AIR) {
                // CraftBukkit start - Store the clicked block
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), org.bukkit.event.block.BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, entityhuman).isCancelled()) {
                    itemstack.damageItem(1, entityhuman);
                    return EnumActionResult.PASS;
                }
                // CraftBukkit end
                world.playSound(entityhuman, blockposition, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, ItemFlintAndSteel.itemRand.nextFloat() * 0.4F + 0.8F);
                world.setBlockState(blockposition, Blocks.FIRE.getDefaultState(), 11);
            }

            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
            }

            itemstack.damageItem(1, entityhuman);
            return EnumActionResult.SUCCESS;
        }
    }
}
