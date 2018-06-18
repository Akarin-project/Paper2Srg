package net.minecraft.item;

import com.google.common.collect.Multimap;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTool extends Item {

    private final Set<Block> effectiveBlocks;
    protected float efficiency;
    protected float attackDamage;
    protected float attackSpeed;
    protected Item.ToolMaterial toolMaterial;

    protected ItemTool(float f, float f1, Item.ToolMaterial item_enumtoolmaterial, Set<Block> set) {
        this.efficiency = 4.0F;
        this.toolMaterial = item_enumtoolmaterial;
        this.effectiveBlocks = set;
        this.maxStackSize = 1;
        this.setMaxDamage(item_enumtoolmaterial.getMaxUses());
        this.efficiency = item_enumtoolmaterial.getEfficiency();
        this.attackDamage = f + item_enumtoolmaterial.getAttackDamage();
        this.attackSpeed = f1;
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    protected ItemTool(Item.ToolMaterial item_enumtoolmaterial, Set<Block> set) {
        this(0.0F, 0.0F, item_enumtoolmaterial, set);
    }

    public float getDestroySpeed(ItemStack itemstack, IBlockState iblockdata) {
        return this.effectiveBlocks.contains(iblockdata.getBlock()) ? this.efficiency : 1.0F;
    }

    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        itemstack.damageItem(2, entityliving1);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState iblockdata, BlockPos blockposition, EntityLivingBase entityliving) {
        if (!world.isRemote && (double) iblockdata.getBlockHardness(world, blockposition) != 0.0D) {
            itemstack.damageItem(1, entityliving);
        }

        return true;
    }

    public int getItemEnchantability() {
        return this.toolMaterial.getEnchantability();
    }

    public String getToolMaterialName() {
        return this.toolMaterial.toString();
    }

    public boolean getIsRepairable(ItemStack itemstack, ItemStack itemstack1) {
        return this.toolMaterial.getRepairItem() == itemstack1.getItem() ? true : super.getIsRepairable(itemstack, itemstack1);
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot enumitemslot) {
        Multimap multimap = super.getItemAttributeModifiers(enumitemslot);

        if (enumitemslot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ItemTool.ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double) this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ItemTool.ATTACK_SPEED_MODIFIER, "Tool modifier", (double) this.attackSpeed, 0));
        }

        return multimap;
    }
}
