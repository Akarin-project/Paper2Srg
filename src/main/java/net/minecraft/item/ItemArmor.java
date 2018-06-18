package net.minecraft.item;

import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public class ItemArmor extends Item {

    private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11};
    private static final UUID[] ARMOR_MODIFIERS = new UUID[] { UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public static final String[] EMPTY_SLOT_NAMES = new String[] { "minecraft:items/empty_armor_slot_boots", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_helmet"};
    public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
        protected ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
            ItemStack itemstack1 = ItemArmor.dispenseArmor(isourceblock, itemstack);

            return itemstack1.isEmpty() ? super.dispenseStack(isourceblock, itemstack) : itemstack1;
        }
    };
    public final EntityEquipmentSlot armorType;
    public final int damageReduceAmount;
    public final float toughness;
    public final int renderIndex;
    private final ItemArmor.ArmorMaterial material;

    public static ItemStack dispenseArmor(IBlockSource isourceblock, ItemStack itemstack) {
        BlockPos blockposition = isourceblock.getBlockPos().offset((EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING));
        List list = isourceblock.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(blockposition), Predicates.and(EntitySelectors.NOT_SPECTATING, new EntitySelectors.ArmoredMob(itemstack)));

        if (list.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            EntityLivingBase entityliving = (EntityLivingBase) list.get(0);
            EntityEquipmentSlot enumitemslot = EntityLiving.getSlotForItemStack(itemstack);
            ItemStack itemstack1 = itemstack.splitStack(1);
            // CraftBukkit start
            World world = isourceblock.getWorld();
            org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

            BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
            if (!BlockDispenser.eventFired) {
                world.getServer().getPluginManager().callEvent(event);
            }

            if (event.isCancelled()) {
                itemstack.grow(1);
                return itemstack;
            }

            if (!event.getItem().equals(craftItem)) {
                itemstack.grow(1);
                // Chain to handler for new item
                ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != ItemArmor.DISPENSER_BEHAVIOR) {
                    idispensebehavior.dispense(isourceblock, eventStack);
                    return itemstack;
                }
            }
            // CraftBukkit end

            entityliving.setItemStackToSlot(enumitemslot, itemstack1);
            if (entityliving instanceof EntityLiving) {
                ((EntityLiving) entityliving).setDropChance(enumitemslot, 2.0F);
            }

            return itemstack;
        }
    }

    public ItemArmor(ItemArmor.ArmorMaterial itemarmor_enumarmormaterial, int i, EntityEquipmentSlot enumitemslot) {
        this.material = itemarmor_enumarmormaterial;
        this.armorType = enumitemslot;
        this.renderIndex = i;
        this.damageReduceAmount = itemarmor_enumarmormaterial.getDamageReductionAmount(enumitemslot);
        this.setMaxDamage(itemarmor_enumarmormaterial.getDurability(enumitemslot));
        this.toughness = itemarmor_enumarmormaterial.getToughness();
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.COMBAT);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }

    public ItemArmor.ArmorMaterial getArmorMaterial() {
        return this.material;
    }

    public boolean hasColor(ItemStack itemstack) {
        if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
            return false;
        } else {
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();

            return nbttagcompound != null && nbttagcompound.hasKey("display", 10) ? nbttagcompound.getCompoundTag("display").hasKey("color", 3) : false;
        }
    }

    public int getColor(ItemStack itemstack) {
        if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
            return 16777215;
        } else {
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();

            if (nbttagcompound != null) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

                if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3)) {
                    return nbttagcompound1.getInteger("color");
                }
            }

            return 10511680;
        }
    }

    public void removeColor(ItemStack itemstack) {
        if (this.material == ItemArmor.ArmorMaterial.LEATHER) {
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();

            if (nbttagcompound != null) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

                if (nbttagcompound1.hasKey("color")) {
                    nbttagcompound1.removeTag("color");
                }

            }
        }
    }

    public void setColor(ItemStack itemstack, int i) {
        if (this.material != ItemArmor.ArmorMaterial.LEATHER) {
            throw new UnsupportedOperationException("Can\'t dye non-leather!");
        } else {
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();

            if (nbttagcompound == null) {
                nbttagcompound = new NBTTagCompound();
                itemstack.setTagCompound(nbttagcompound);
            }

            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (!nbttagcompound.hasKey("display", 10)) {
                nbttagcompound.setTag("display", nbttagcompound1);
            }

            nbttagcompound1.setInteger("color", i);
        }
    }

    public boolean getIsRepairable(ItemStack itemstack, ItemStack itemstack1) {
        return this.material.getRepairItem() == itemstack1.getItem() ? true : super.getIsRepairable(itemstack, itemstack1);
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        EntityEquipmentSlot enumitemslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = entityhuman.getItemStackFromSlot(enumitemslot);

        if (itemstack1.isEmpty()) {
            entityhuman.setItemStackToSlot(enumitemslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot enumitemslot) {
        Multimap multimap = super.getItemAttributeModifiers(enumitemslot);

        if (enumitemslot == this.armorType) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ItemArmor.ARMOR_MODIFIERS[enumitemslot.getIndex()], "Armor modifier", (double) this.damageReduceAmount, 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ItemArmor.ARMOR_MODIFIERS[enumitemslot.getIndex()], "Armor toughness", (double) this.toughness, 0));
        }

        return multimap;
    }

    public static enum ArmorMaterial {

        LEATHER("leather", 5, new int[] { 1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F), CHAIN("chainmail", 15, new int[] { 1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0.0F), IRON("iron", 15, new int[] { 2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F), GOLD("gold", 7, new int[] { 1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F), DIAMOND("diamond", 33, new int[] { 3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);

        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        private final SoundEvent soundEvent;
        private final float toughness;

        private ArmorMaterial(String s, int i, int[] aint, int j, SoundEvent soundeffect, float f) {
            this.name = s;
            this.maxDamageFactor = i;
            this.damageReductionAmountArray = aint;
            this.enchantability = j;
            this.soundEvent = soundeffect;
            this.toughness = f;
        }

        public int getDurability(EntityEquipmentSlot enumitemslot) {
            return ItemArmor.MAX_DAMAGE_ARRAY[enumitemslot.getIndex()] * this.maxDamageFactor;
        }

        public int getDamageReductionAmount(EntityEquipmentSlot enumitemslot) {
            return this.damageReductionAmountArray[enumitemslot.getIndex()];
        }

        public int getEnchantability() {
            return this.enchantability;
        }

        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }

        public Item getRepairItem() {
            return this == ItemArmor.ArmorMaterial.LEATHER ? Items.LEATHER : (this == ItemArmor.ArmorMaterial.CHAIN ? Items.IRON_INGOT : (this == ItemArmor.ArmorMaterial.GOLD ? Items.GOLD_INGOT : (this == ItemArmor.ArmorMaterial.IRON ? Items.IRON_INGOT : (this == ItemArmor.ArmorMaterial.DIAMOND ? Items.DIAMOND : null))));
        }

        public float getToughness() {
            return this.toughness;
        }
    }
}
