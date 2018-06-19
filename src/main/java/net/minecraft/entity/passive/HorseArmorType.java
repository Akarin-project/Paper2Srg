package net.minecraft.entity.passive;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public enum HorseArmorType {

    NONE(0), IRON(5, "iron", "meo"), GOLD(7, "gold", "goo"), DIAMOND(11, "diamond", "dio");

    private final String field_188586_e;
    private final String field_188587_f;
    private final int field_188588_g;

    private HorseArmorType(int i) {
        this.field_188588_g = i;
        this.field_188586_e = null;
        this.field_188587_f = "";
    }

    private HorseArmorType(int i, String s, String s1) {
        this.field_188588_g = i;
        this.field_188586_e = "textures/entity/horse/armor/horse_armor_" + s + ".png";
        this.field_188587_f = s1;
    }

    public int func_188579_a() {
        return this.ordinal();
    }

    public int func_188578_c() {
        return this.field_188588_g;
    }

    public static HorseArmorType func_188575_a(int i) {
        return values()[i];
    }

    public static HorseArmorType func_188580_a(ItemStack itemstack) {
        return itemstack.func_190926_b() ? HorseArmorType.NONE : func_188576_a(itemstack.func_77973_b());
    }

    public static HorseArmorType func_188576_a(Item item) {
        return item == Items.field_151138_bX ? HorseArmorType.IRON : (item == Items.field_151136_bY ? HorseArmorType.GOLD : (item == Items.field_151125_bZ ? HorseArmorType.DIAMOND : HorseArmorType.NONE));
    }

    public static boolean func_188577_b(Item item) {
        return func_188576_a(item) != HorseArmorType.NONE;
    }
}
