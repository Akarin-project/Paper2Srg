package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemFishFood extends ItemFood {

    private final boolean field_150907_b;

    public ItemFishFood(boolean flag) {
        super(0, 0.0F, false);
        this.field_150907_b = flag;
    }

    public int func_150905_g(ItemStack itemstack) {
        ItemFishFood.FishType itemfish_enumfish = ItemFishFood.FishType.func_150978_a(itemstack);

        return this.field_150907_b && itemfish_enumfish.func_150973_i() ? itemfish_enumfish.func_150970_e() : itemfish_enumfish.func_150975_c();
    }

    public float func_150906_h(ItemStack itemstack) {
        ItemFishFood.FishType itemfish_enumfish = ItemFishFood.FishType.func_150978_a(itemstack);

        return this.field_150907_b && itemfish_enumfish.func_150973_i() ? itemfish_enumfish.func_150977_f() : itemfish_enumfish.func_150967_d();
    }

    protected void func_77849_c(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        ItemFishFood.FishType itemfish_enumfish = ItemFishFood.FishType.func_150978_a(itemstack);

        if (itemfish_enumfish == ItemFishFood.FishType.PUFFERFISH) {
            entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76436_u, 1200, 3));
            entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76438_s, 300, 2));
            entityhuman.func_70690_d(new PotionEffect(MobEffects.field_76431_k, 300, 1));
        }

        super.func_77849_c(itemstack, world, entityhuman);
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            ItemFishFood.FishType[] aitemfish_enumfish = ItemFishFood.FishType.values();
            int i = aitemfish_enumfish.length;

            for (int j = 0; j < i; ++j) {
                ItemFishFood.FishType itemfish_enumfish = aitemfish_enumfish[j];

                if (!this.field_150907_b || itemfish_enumfish.func_150973_i()) {
                    nonnulllist.add(new ItemStack(this, 1, itemfish_enumfish.func_150976_a()));
                }
            }
        }

    }

    public String func_77667_c(ItemStack itemstack) {
        ItemFishFood.FishType itemfish_enumfish = ItemFishFood.FishType.func_150978_a(itemstack);

        return this.func_77658_a() + "." + itemfish_enumfish.func_150972_b() + "." + (this.field_150907_b && itemfish_enumfish.func_150973_i() ? "cooked" : "raw");
    }

    public static enum FishType {

        COD(0, "cod", 2, 0.1F, 5, 0.6F), SALMON(1, "salmon", 2, 0.1F, 6, 0.8F), CLOWNFISH(2, "clownfish", 1, 0.1F), PUFFERFISH(3, "pufferfish", 1, 0.1F);

        private static final Map<Integer, ItemFishFood.FishType> field_150983_e = Maps.newHashMap();
        private final int field_150980_f;
        private final String field_150981_g;
        private final int field_150991_j;
        private final float field_150992_k;
        private final int field_150989_l;
        private final float field_150990_m;
        private final boolean field_150987_n;

        private FishType(int i, String s, int j, float f, int k, float f1) {
            this.field_150980_f = i;
            this.field_150981_g = s;
            this.field_150991_j = j;
            this.field_150992_k = f;
            this.field_150989_l = k;
            this.field_150990_m = f1;
            this.field_150987_n = true;
        }

        private FishType(int i, String s, int j, float f) {
            this.field_150980_f = i;
            this.field_150981_g = s;
            this.field_150991_j = j;
            this.field_150992_k = f;
            this.field_150989_l = 0;
            this.field_150990_m = 0.0F;
            this.field_150987_n = false;
        }

        public int func_150976_a() {
            return this.field_150980_f;
        }

        public String func_150972_b() {
            return this.field_150981_g;
        }

        public int func_150975_c() {
            return this.field_150991_j;
        }

        public float func_150967_d() {
            return this.field_150992_k;
        }

        public int func_150970_e() {
            return this.field_150989_l;
        }

        public float func_150977_f() {
            return this.field_150990_m;
        }

        public boolean func_150973_i() {
            return this.field_150987_n;
        }

        public static ItemFishFood.FishType func_150974_a(int i) {
            ItemFishFood.FishType itemfish_enumfish = (ItemFishFood.FishType) ItemFishFood.FishType.field_150983_e.get(Integer.valueOf(i));

            return itemfish_enumfish == null ? ItemFishFood.FishType.COD : itemfish_enumfish;
        }

        public static ItemFishFood.FishType func_150978_a(ItemStack itemstack) {
            return itemstack.func_77973_b() instanceof ItemFishFood ? func_150974_a(itemstack.func_77960_j()) : ItemFishFood.FishType.COD;
        }

        static {
            ItemFishFood.FishType[] aitemfish_enumfish = values();
            int i = aitemfish_enumfish.length;

            for (int j = 0; j < i; ++j) {
                ItemFishFood.FishType itemfish_enumfish = aitemfish_enumfish[j];

                ItemFishFood.FishType.field_150983_e.put(Integer.valueOf(itemfish_enumfish.func_150976_a()), itemfish_enumfish);
            }

        }
    }
}
