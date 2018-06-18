package net.minecraft.entity.passive;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public enum HorseArmorType {

    NONE(0), IRON(5, "iron", "meo"), GOLD(7, "gold", "goo"), DIAMOND(11, "diamond", "dio");

    private final String textureName;
    private final String hash;
    private final int protection;

    private HorseArmorType(int i) {
        this.protection = i;
        this.textureName = null;
        this.hash = "";
    }

    private HorseArmorType(int i, String s, String s1) {
        this.protection = i;
        this.textureName = "textures/entity/horse/armor/horse_armor_" + s + ".png";
        this.hash = s1;
    }

    public int getOrdinal() {
        return this.ordinal();
    }

    public int getProtection() {
        return this.protection;
    }

    public static HorseArmorType getByOrdinal(int i) {
        return values()[i];
    }

    public static HorseArmorType getByItemStack(ItemStack itemstack) {
        return itemstack.isEmpty() ? HorseArmorType.NONE : getByItem(itemstack.getItem());
    }

    public static HorseArmorType getByItem(Item item) {
        return item == Items.IRON_HORSE_ARMOR ? HorseArmorType.IRON : (item == Items.GOLDEN_HORSE_ARMOR ? HorseArmorType.GOLD : (item == Items.DIAMOND_HORSE_ARMOR ? HorseArmorType.DIAMOND : HorseArmorType.NONE));
    }

    public static boolean isHorseArmor(Item item) {
        return getByItem(item) != HorseArmorType.NONE;
    }
}
