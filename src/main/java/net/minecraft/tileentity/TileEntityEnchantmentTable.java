package net.minecraft.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject {

    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float flipT;
    public float flipA;
    public float bookSpread;
    public float bookSpreadPrev;
    public float bookRotation;
    public float bookRotationPrev;
    public float tRot;
    private static final Random rand = new Random();
    private String customName;

    public TileEntityEnchantmentTable() {}

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.customName);
        }

        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("CustomName", 8)) {
            this.customName = nbttagcompound.getString("CustomName");
        }

    }

    public void update() {
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        EntityPlayer entityhuman = this.world.getClosestPlayer((double) ((float) this.pos.getX() + 0.5F), (double) ((float) this.pos.getY() + 0.5F), (double) ((float) this.pos.getZ() + 0.5F), 3.0D, false);

        if (entityhuman != null) {
            double d0 = entityhuman.posX - (double) ((float) this.pos.getX() + 0.5F);
            double d1 = entityhuman.posZ - (double) ((float) this.pos.getZ() + 0.5F);

            this.tRot = (float) MathHelper.atan2(d1, d0);
            this.bookSpread += 0.1F;
            if (this.bookSpread < 0.5F || TileEntityEnchantmentTable.rand.nextInt(40) == 0) {
                float f = this.flipT;

                do {
                    this.flipT += (float) (TileEntityEnchantmentTable.rand.nextInt(4) - TileEntityEnchantmentTable.rand.nextInt(4));
                } while (f == this.flipT);
            }
        } else {
            this.tRot += 0.02F;
            this.bookSpread -= 0.1F;
        }

        while (this.bookRotation >= 3.1415927F) {
            this.bookRotation -= 6.2831855F;
        }

        while (this.bookRotation < -3.1415927F) {
            this.bookRotation += 6.2831855F;
        }

        while (this.tRot >= 3.1415927F) {
            this.tRot -= 6.2831855F;
        }

        while (this.tRot < -3.1415927F) {
            this.tRot += 6.2831855F;
        }

        float f1;

        for (f1 = this.tRot - this.bookRotation; f1 >= 3.1415927F; f1 -= 6.2831855F) {
            ;
        }

        while (f1 < -3.1415927F) {
            f1 += 6.2831855F;
        }

        this.bookRotation += f1 * 0.4F;
        this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f2 = (this.flipT - this.pageFlip) * 0.4F;
        float f3 = 0.2F;

        f2 = MathHelper.clamp(f2, -0.2F, 0.2F);
        this.flipA += (f2 - this.flipA) * 0.9F;
        this.pageFlip += this.flipA;
    }

    public String getName() {
        return this.hasCustomName() ? this.customName : "container.enchant";
    }

    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String s) {
        this.customName = s;
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerEnchantment(playerinventory, this.world, this.pos);
    }

    public String getGuiID() {
        return "minecraft:enchanting_table";
    }
}
