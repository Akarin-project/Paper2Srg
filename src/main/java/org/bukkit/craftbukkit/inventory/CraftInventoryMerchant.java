package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.InventoryMerchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory {

    public CraftInventoryMerchant(InventoryMerchant merchant) {
        super(merchant);
    }

    @Override
    public int getSelectedRecipeIndex() {
        return getInventory().field_70473_e;
    }

    @Override
    public MerchantRecipe getSelectedRecipe() {
        return getInventory().func_70468_h().asBukkit();
    }

    @Override
    public InventoryMerchant getInventory() {
        return (InventoryMerchant) inventory;
    }
}
