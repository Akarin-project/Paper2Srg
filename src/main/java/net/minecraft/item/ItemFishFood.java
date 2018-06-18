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

    private final boolean cooked;

    public ItemFishFood(boolean flag) {
        super(0, 0.0F, false);
        this.cooked = flag;
    }

    public int getHealAmount(ItemStack itemstack) {
        ItemFishFood.FishType itemfish_enumfish = ItemFishFood.FishType.byItemStack(itemstack);

        return this.cooked && itemfish_enumfish.canCook() ? itemfish_enumfish.getCookedHealAmount() : itemfish_enumfish.getUncookedHealAmount();
    }

    public float getSaturationModifier(ItemStack itemstack) {
        ItemFishFood.FishType itemfish_enumfish = ItemFishFood.FishType.byItemStack(itemstack);

        return this.cooked && itemfish_enumfish.canCook() ? itemfish_enumfish.getCookedSaturationModifier() : itemfish_enumfish.getUncookedSaturationModifier();
    }

    protected void onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        ItemFishFood.FishType itemfish_enumfish = ItemFishFood.FishType.byItemStack(itemstack);

        if (itemfish_enumfish == ItemFishFood.FishType.PUFFERFISH) {
            entityhuman.addPotionEffect(new PotionEffect(MobEffects.POISON, 1200, 3));
            entityhuman.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 300, 2));
            entityhuman.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300, 1));
        }

        super.onFoodEaten(itemstack, world, entityhuman);
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            ItemFishFood.FishType[] aitemfish_enumfish = ItemFishFood.FishType.values();
            int i = aitemfish_enumfish.length;

            for (int j = 0; j < i; ++j) {
                ItemFishFood.FishType itemfish_enumfish = aitemfish_enumfish[j];

                if (!this.cooked || itemfish_enumfish.canCook()) {
                    nonnulllist.add(new ItemStack(this, 1, itemfish_enumfish.getMetadata()));
                }
            }
        }

    }

    public String getUnlocalizedName(ItemStack itemstack) {
        ItemFishFood.FishType itemfish_enumfish = ItemFishFood.FishType.byItemStack(itemstack);

        return this.getUnlocalizedName() + "." + itemfish_enumfish.getUnlocalizedName() + "." + (this.cooked && itemfish_enumfish.canCook() ? "cooked" : "raw");
    }

    public static enum FishType {

        COD(0, "cod", 2, 0.1F, 5, 0.6F), SALMON(1, "salmon", 2, 0.1F, 6, 0.8F), CLOWNFISH(2, "clownfish", 1, 0.1F), PUFFERFISH(3, "pufferfish", 1, 0.1F);

        private static final Map<Integer, ItemFishFood.FishType> META_LOOKUP = Maps.newHashMap();
        private final int meta;
        private final String unlocalizedName;
        private final int uncookedHealAmount;
        private final float uncookedSaturationModifier;
        private final int cookedHealAmount;
        private final float cookedSaturationModifier;
        private final boolean cookable;

        private FishType(int i, String s, int j, float f, int k, float f1) {
            this.meta = i;
            this.unlocalizedName = s;
            this.uncookedHealAmount = j;
            this.uncookedSaturationModifier = f;
            this.cookedHealAmount = k;
            this.cookedSaturationModifier = f1;
            this.cookable = true;
        }

        private FishType(int i, String s, int j, float f) {
            this.meta = i;
            this.unlocalizedName = s;
            this.uncookedHealAmount = j;
            this.uncookedSaturationModifier = f;
            this.cookedHealAmount = 0;
            this.cookedSaturationModifier = 0.0F;
            this.cookable = false;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public int getUncookedHealAmount() {
            return this.uncookedHealAmount;
        }

        public float getUncookedSaturationModifier() {
            return this.uncookedSaturationModifier;
        }

        public int getCookedHealAmount() {
            return this.cookedHealAmount;
        }

        public float getCookedSaturationModifier() {
            return this.cookedSaturationModifier;
        }

        public boolean canCook() {
            return this.cookable;
        }

        public static ItemFishFood.FishType byMetadata(int i) {
            ItemFishFood.FishType itemfish_enumfish = (ItemFishFood.FishType) ItemFishFood.FishType.META_LOOKUP.get(Integer.valueOf(i));

            return itemfish_enumfish == null ? ItemFishFood.FishType.COD : itemfish_enumfish;
        }

        public static ItemFishFood.FishType byItemStack(ItemStack itemstack) {
            return itemstack.getItem() instanceof ItemFishFood ? byMetadata(itemstack.getMetadata()) : ItemFishFood.FishType.COD;
        }

        static {
            ItemFishFood.FishType[] aitemfish_enumfish = values();
            int i = aitemfish_enumfish.length;

            for (int j = 0; j < i; ++j) {
                ItemFishFood.FishType itemfish_enumfish = aitemfish_enumfish[j];

                ItemFishFood.FishType.META_LOOKUP.put(Integer.valueOf(itemfish_enumfish.getMetadata()), itemfish_enumfish);
            }

        }
    }
}
