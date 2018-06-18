package net.minecraft.tileentity;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;


public class TileEntityBed extends TileEntity {

    private EnumDyeColor color;

    public TileEntityBed() {
        this.color = EnumDyeColor.RED;
    }

    public void setItemValues(ItemStack itemstack) {
        this.setColor(EnumDyeColor.byMetadata(itemstack.getMetadata()));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("color")) {
            this.color = EnumDyeColor.byMetadata(nbttagcompound.getInteger("color"));
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("color", this.color.getMetadata());
        return nbttagcompound;
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 11, this.getUpdateTag());
    }

    public EnumDyeColor getColor() {
        return this.color;
    }

    public void setColor(EnumDyeColor enumcolor) {
        this.color = enumcolor;
        this.markDirty();
    }

    public ItemStack getItemStack() {
        return new ItemStack(Items.BED, 1, this.color.getMetadata());
    }
}
