package net.minecraft.item;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoe extends Item {

    private final float speed;
    protected Item.ToolMaterial toolMaterial;

    public ItemHoe(Item.ToolMaterial item_enumtoolmaterial) {
        this.toolMaterial = item_enumtoolmaterial;
        this.maxStackSize = 1;
        this.setMaxDamage(item_enumtoolmaterial.getMaxUses());
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.speed = item_enumtoolmaterial.getAttackDamage() + 1.0F;
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!entityhuman.canPlayerEdit(blockposition.offset(enumdirection), enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();

            if (enumdirection != EnumFacing.DOWN && world.getBlockState(blockposition.up()).getMaterial() == Material.AIR) {
                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                    this.setBlock(itemstack, entityhuman, world, blockposition, Blocks.FARMLAND.getDefaultState());
                    return EnumActionResult.SUCCESS;
                }

                if (block == Blocks.DIRT) {
                    switch ((BlockDirt.DirtType) iblockdata.getValue(BlockDirt.VARIANT)) {
                    case DIRT:
                        this.setBlock(itemstack, entityhuman, world, blockposition, Blocks.FARMLAND.getDefaultState());
                        return EnumActionResult.SUCCESS;

                    case COARSE_DIRT:
                        this.setBlock(itemstack, entityhuman, world, blockposition, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                        return EnumActionResult.SUCCESS;
                    }
                }
            }

            return EnumActionResult.PASS;
        }
    }

    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        itemstack.damageItem(1, entityliving1);
        return true;
    }

    protected void setBlock(ItemStack itemstack, EntityPlayer entityhuman, World world, BlockPos blockposition, IBlockState iblockdata) {
        world.playSound(entityhuman, blockposition, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!world.isRemote) {
            world.setBlockState(blockposition, iblockdata, 11);
            itemstack.damageItem(1, entityhuman);
        }

    }

    public String getMaterialName() {
        return this.toolMaterial.toString();
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot enumitemslot) {
        Multimap multimap = super.getItemAttributeModifiers(enumitemslot);

        if (enumitemslot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ItemHoe.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ItemHoe.ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) (this.speed - 4.0F), 0));
        }

        return multimap;
    }
}
