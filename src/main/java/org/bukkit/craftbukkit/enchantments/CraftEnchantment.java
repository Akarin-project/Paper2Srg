package org.bukkit.craftbukkit.enchantments;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;



public class CraftEnchantment extends Enchantment {
    private final net.minecraft.enchantment.Enchantment target;

    public CraftEnchantment(net.minecraft.enchantment.Enchantment target) {
        super(net.minecraft.enchantment.Enchantment.func_185258_b(target));
        this.target = target;
    }

    @Override
    public int getMaxLevel() {
        return target.func_77325_b();
    }

    @Override
    public int getStartLevel() {
        return target.func_77319_d();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        switch (target.field_77351_y) {
        case ALL:
            return EnchantmentTarget.ALL;
        case ARMOR:
            return EnchantmentTarget.ARMOR;
        case ARMOR_FEET:
            return EnchantmentTarget.ARMOR_FEET;
        case ARMOR_HEAD:
            return EnchantmentTarget.ARMOR_HEAD;
        case ARMOR_LEGS:
            return EnchantmentTarget.ARMOR_LEGS;
        case ARMOR_CHEST:
            return EnchantmentTarget.ARMOR_TORSO;
        case DIGGER:
            return EnchantmentTarget.TOOL;
        case WEAPON:
            return EnchantmentTarget.WEAPON;
        case BOW:
            return EnchantmentTarget.BOW;
        case FISHING_ROD:
            return EnchantmentTarget.FISHING_ROD;
        case BREAKABLE:
            return EnchantmentTarget.BREAKABLE;
        case WEARABLE:
            return EnchantmentTarget.WEARABLE;
        default:
            return null;
        }
    }

    @Override
    public boolean isTreasure() {
        return target.func_185261_e();
    }

    @Override
    public boolean isCursed() {
        return target.func_190936_d();
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return target.func_92089_a(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public String getName() {
        switch (getId()) {
        case 0:
            return "PROTECTION_ENVIRONMENTAL";
        case 1:
            return "PROTECTION_FIRE";
        case 2:
            return "PROTECTION_FALL";
        case 3:
            return "PROTECTION_EXPLOSIONS";
        case 4:
            return "PROTECTION_PROJECTILE";
        case 5:
            return "OXYGEN";
        case 6:
            return "WATER_WORKER";
        case 7:
            return "THORNS";
        case 8:
            return "DEPTH_STRIDER";
        case 9:
            return "FROST_WALKER";
        case 10:
            return "BINDING_CURSE";
        case 16:
            return "DAMAGE_ALL";
        case 17:
            return "DAMAGE_UNDEAD";
        case 18:
            return "DAMAGE_ARTHROPODS";
        case 19:
            return "KNOCKBACK";
        case 20:
            return "FIRE_ASPECT";
        case 21:
            return "LOOT_BONUS_MOBS";
        case 22:
            return "SWEEPING_EDGE";
        case 32:
            return "DIG_SPEED";
        case 33:
            return "SILK_TOUCH";
        case 34:
            return "DURABILITY";
        case 35:
            return "LOOT_BONUS_BLOCKS";
        case 48:
            return "ARROW_DAMAGE";
        case 49:
            return "ARROW_KNOCKBACK";
        case 50:
            return "ARROW_FIRE";
        case 51:
            return "ARROW_INFINITE";
        case 61:
            return "LUCK";
        case 62:
            return "LURE";
        case 70:
            return "MENDING";
        case 71:
            return "VANISHING_CURSE";
        default:
            return "UNKNOWN_ENCHANT_" + getId();
        }
    }

    public static net.minecraft.enchantment.Enchantment getRaw(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentWrapper) {
            enchantment = ((EnchantmentWrapper) enchantment).getEnchantment();
        }

        if (enchantment instanceof CraftEnchantment) {
            return ((CraftEnchantment) enchantment).target;
        }

        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        if (other instanceof EnchantmentWrapper) {
            other = ((EnchantmentWrapper) other).getEnchantment();
        }
        if (!(other instanceof CraftEnchantment)) {
            return false;
        }
        CraftEnchantment ench = (CraftEnchantment) other;
        return !target.func_191560_c(ench.target);
    }

    public net.minecraft.enchantment.Enchantment getHandle() {
        return target;
    }
}
