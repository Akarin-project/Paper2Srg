package net.minecraft.item;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSword extends Item {

    private final float attackDamage;
    private final Item.ToolMaterial material;

    public ItemSword(Item.ToolMaterial item_enumtoolmaterial) {
        this.material = item_enumtoolmaterial;
        this.maxStackSize = 1;
        this.setMaxDamage(item_enumtoolmaterial.getMaxUses());
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.attackDamage = 3.0F + item_enumtoolmaterial.getAttackDamage();
    }

    public float getAttackDamage() {
        return this.material.getAttackDamage();
    }

    public float getDestroySpeed(ItemStack itemstack, IBlockState iblockdata) {
        Block block = iblockdata.getBlock();

        if (block == Blocks.WEB) {
            return 15.0F;
        } else {
            Material material = iblockdata.getMaterial();

            return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
        }
    }

    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        itemstack.damageItem(1, entityliving1);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState iblockdata, BlockPos blockposition, EntityLivingBase entityliving) {
        if ((double) iblockdata.getBlockHardness(world, blockposition) != 0.0D) {
            itemstack.damageItem(2, entityliving);
        }

        return true;
    }

    public boolean canHarvestBlock(IBlockState iblockdata) {
        return iblockdata.getBlock() == Blocks.WEB;
    }

    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }

    public String getToolMaterialName() {
        return this.material.toString();
    }

    public boolean getIsRepairable(ItemStack itemstack, ItemStack itemstack1) {
        return this.material.getRepairItem() == itemstack1.getItem() ? true : super.getIsRepairable(itemstack, itemstack1);
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot enumitemslot) {
        Multimap multimap = super.getItemAttributeModifiers(enumitemslot);

        if (enumitemslot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ItemSword.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ItemSword.ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }
}
