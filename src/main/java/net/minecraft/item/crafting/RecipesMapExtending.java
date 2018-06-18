package net.minecraft.item.crafting;

import java.util.Iterator;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class RecipesMapExtending extends ShapedRecipes {

    public RecipesMapExtending() {
        super("", 3, 3, NonNullList.from(Ingredient.EMPTY, new Ingredient[] { Ingredient.fromItems(new Item[] { Items.PAPER}), Ingredient.fromItems(new Item[] { Items.PAPER}), Ingredient.fromItems(new Item[] { Items.PAPER}), Ingredient.fromItems(new Item[] { Items.PAPER}), Ingredient.fromItem((Item) Items.FILLED_MAP), Ingredient.fromItems(new Item[] { Items.PAPER}), Ingredient.fromItems(new Item[] { Items.PAPER}), Ingredient.fromItems(new Item[] { Items.PAPER}), Ingredient.fromItems(new Item[] { Items.PAPER})}), new ItemStack(Items.MAP));
    }

    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        if (!super.matches(inventorycrafting, world)) {
            return false;
        } else {
            ItemStack itemstack = ItemStack.EMPTY;

            for (int i = 0; i < inventorycrafting.getSizeInventory() && itemstack.isEmpty(); ++i) {
                ItemStack itemstack1 = inventorycrafting.getStackInSlot(i);

                if (itemstack1.getItem() == Items.FILLED_MAP) {
                    itemstack = itemstack1;
                }
            }

            if (itemstack.isEmpty()) {
                return false;
            } else {
                MapData worldmap = Items.FILLED_MAP.getMapData(itemstack, world);

                return worldmap == null ? false : (this.isExplorationMap(worldmap) ? false : worldmap.scale < 4);
            }
        }
    }

    private boolean isExplorationMap(MapData worldmap) {
        if (worldmap.mapDecorations != null) {
            Iterator iterator = worldmap.mapDecorations.values().iterator();

            while (iterator.hasNext()) {
                MapDecoration mapicon = (MapDecoration) iterator.next();

                if (mapicon.getType() == MapDecoration.Type.MANSION || mapicon.getType() == MapDecoration.Type.MONUMENT) {
                    return true;
                }
            }
        }

        return false;
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inventorycrafting.getSizeInventory() && itemstack.isEmpty(); ++i) {
            ItemStack itemstack1 = inventorycrafting.getStackInSlot(i);

            if (itemstack1.getItem() == Items.FILLED_MAP) {
                itemstack = itemstack1;
            }
        }

        itemstack = itemstack.copy();
        itemstack.setCount(1);
        if (itemstack.getTagCompound() == null) {
            itemstack.setTagCompound(new NBTTagCompound());
        }

        itemstack.getTagCompound().setInteger("map_scale_direction", 1);
        return itemstack;
    }

    public boolean isDynamic() {
        return true;
    }
}
