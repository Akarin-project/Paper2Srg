package net.minecraft.tileentity;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;

public class TileEntityFlowerPot extends TileEntity {

    private Item flowerPotItem;
    private int flowerPotData;

    public TileEntityFlowerPot() {}

    public TileEntityFlowerPot(Item item, int i) {
        this.flowerPotItem = item;
        this.flowerPotData = i;
    }

    public static void registerFixesFlowerPot(DataFixer dataconvertermanager) {}

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        ResourceLocation minecraftkey = (ResourceLocation) Item.REGISTRY.getNameForObject(this.flowerPotItem);

        nbttagcompound.setString("Item", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound.setInteger("Data", this.flowerPotData);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("Item", 8)) {
            this.flowerPotItem = Item.getByNameOrId(nbttagcompound.getString("Item"));
        } else {
            this.flowerPotItem = Item.getItemById(nbttagcompound.getInteger("Item"));
        }

        this.flowerPotData = nbttagcompound.getInteger("Data");
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public void setItemStack(ItemStack itemstack) {
        this.flowerPotItem = itemstack.getItem();
        this.flowerPotData = itemstack.getMetadata();
    }

    public ItemStack getFlowerItemStack() {
        return this.flowerPotItem == null ? ItemStack.EMPTY : new ItemStack(this.flowerPotItem, 1, this.flowerPotData);
    }

    @Nullable
    public Item getFlowerPotItem() {
        return this.flowerPotItem;
    }

    public int getFlowerPotData() {
        return this.flowerPotData;
    }
}
