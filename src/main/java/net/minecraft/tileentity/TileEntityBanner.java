package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;

public class TileEntityBanner extends TileEntity implements IWorldNameable {

    private String name;
    public EnumDyeColor baseColor;
    public NBTTagList patterns;
    private boolean patternDataSet;
    private List<BannerPattern> patternList;
    private List<EnumDyeColor> colorList;
    private String patternResourceLocation;

    public TileEntityBanner() {
        this.baseColor = EnumDyeColor.BLACK;
    }

    public void setItemValues(ItemStack itemstack, boolean flag) {
        this.patterns = null;
        NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
            this.patterns = nbttagcompound.getTagList("Patterns", 10).copy();
            // CraftBukkit start
            while (this.patterns.tagCount() > 20) {
                this.patterns.removeTag(20);
            }
            // CraftBukkit end
        }

        this.baseColor = flag ? getColor(itemstack) : ItemBanner.getBaseColor(itemstack);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.patternDataSet = true;
        this.name = itemstack.hasDisplayName() ? itemstack.getDisplayName() : null;
    }

    public String getName() {
        return this.hasCustomName() ? this.name : "banner";
    }

    public boolean hasCustomName() {
        return this.name != null && !this.name.isEmpty();
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("Base", this.baseColor.getDyeDamage());
        if (this.patterns != null) {
            nbttagcompound.setTag("Patterns", this.patterns);
        }

        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.name);
        }

        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("CustomName", 8)) {
            this.name = nbttagcompound.getString("CustomName");
        }

        this.baseColor = EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base"));
        this.patterns = nbttagcompound.getTagList("Patterns", 10);
        // CraftBukkit start
        while (this.patterns.tagCount() > 20) {
            this.patterns.removeTag(20);
        }
        // CraftBukkit end
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.patternDataSet = true;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 6, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public static int getPatterns(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");

        return nbttagcompound != null && nbttagcompound.hasKey("Patterns") ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
    }

    public static void removeBannerData(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);

            if (!nbttaglist.hasNoTags()) {
                nbttaglist.removeTag(nbttaglist.tagCount() - 1);
                if (nbttaglist.hasNoTags()) {
                    itemstack.getTagCompound().removeTag("BlockEntityTag");
                    if (itemstack.getTagCompound().hasNoTags()) {
                        itemstack.setTagCompound((NBTTagCompound) null);
                    }
                }

            }
        }
    }

    public ItemStack getItem() {
        ItemStack itemstack = ItemBanner.makeBanner(this.baseColor, this.patterns);

        if (this.hasCustomName()) {
            itemstack.setStackDisplayName(this.getName());
        }

        return itemstack;
    }

    public static EnumDyeColor getColor(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");

        return nbttagcompound != null && nbttagcompound.hasKey("Base") ? EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base")) : EnumDyeColor.BLACK;
    }
}
