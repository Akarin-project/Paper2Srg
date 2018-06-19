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
        public boolean func_77557_a(Item item) {
            EnumEnchantmentType[] aenchantmentslottype = EnumEnchantmentType.values();
            int i = aenchantmentslottype.length;

            for (int j = 0; j < i; ++j) {
                EnumEnchantmentType enchantmentslottype = aenchantmentslottype[j];

                if (enchantmentslottype != EnumEnchantmentType.ALL && enchantmentslottype.func_77557_a(item)) {
                    return true;
                }
            }

            return false;
        }
    }, ARMOR {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemArmor;
    }
}, ARMOR_FEET {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemArmor && ((ItemArmor) item).field_77881_a == EntityEquipmentSlot.FEET;
    }
}, ARMOR_LEGS {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemArmor && ((ItemArmor) item).field_77881_a == EntityEquipmentSlot.LEGS;
    }
}, ARMOR_CHEST {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemArmor && ((ItemArmor) item).field_77881_a == EntityEquipmentSlot.CHEST;
    }
}, ARMOR_HEAD {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemArmor && ((ItemArmor) item).field_77881_a == EntityEquipmentSlot.HEAD;
    }
}, WEAPON {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemSword;
    }
}, DIGGER {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemTool;
    }
}, FISHING_ROD {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemFishingRod;
    }
}, BREAKABLE {;
    public boolean func_77557_a(Item item) {
        return item.func_77645_m();
    }
}, BOW {;
    public boolean func_77557_a(Item item) {
        return item instanceof ItemBow;
    }
}, WEARABLE {;
    public boolean func_77557_a(Item item) {
        boolean flag = item instanceof ItemBlock && ((ItemBlock) item).func_179223_d() instanceof BlockPumpkin;

        return item instanceof ItemArmor || item instanceof ItemElytra || item instanceof ItemSkull || flag;
    }
};

    private EnumEnchantmentType() {}

    public abstract boolean func_77557_a(Item item);

    EnumEnchantmentType(Object object) {
        this();
    }
}
