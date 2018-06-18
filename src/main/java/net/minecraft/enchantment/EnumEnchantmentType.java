package net.minecraft.enchantment;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;


public enum EnumEnchantmentType {

    ALL {;
        public boolean canEnchantItem(Item item) {
            EnumEnchantmentType[] aenchantmentslottype = EnumEnchantmentType.values();
            int i = aenchantmentslottype.length;

            for (int j = 0; j < i; ++j) {
                EnumEnchantmentType enchantmentslottype = aenchantmentslottype[j];

                if (enchantmentslottype != EnumEnchantmentType.ALL && enchantmentslottype.canEnchantItem(item)) {
                    return true;
                }
            }

            return false;
        }
    }, ARMOR {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemArmor;
    }
}, ARMOR_FEET {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.FEET;
    }
}, ARMOR_LEGS {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.LEGS;
    }
}, ARMOR_CHEST {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.CHEST;
    }
}, ARMOR_HEAD {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemArmor && ((ItemArmor) item).armorType == EntityEquipmentSlot.HEAD;
    }
}, WEAPON {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemSword;
    }
}, DIGGER {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemTool;
    }
}, FISHING_ROD {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemFishingRod;
    }
}, BREAKABLE {;
    public boolean canEnchantItem(Item item) {
        return item.isDamageable();
    }
}, BOW {;
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemBow;
    }
}, WEARABLE {;
    public boolean canEnchantItem(Item item) {
        boolean flag = item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockPumpkin;

        return item instanceof ItemArmor || item instanceof ItemElytra || item instanceof ItemSkull || flag;
    }
};

    private EnumEnchantmentType() {}

    public abstract boolean canEnchantItem(Item item);

    EnumEnchantmentType(Object object) {
        this();
    }
}
