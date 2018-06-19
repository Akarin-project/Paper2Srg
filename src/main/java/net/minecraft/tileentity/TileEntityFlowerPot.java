package net.minecraft.tileentity;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;

public class TileEntityFlowerPot extends TileEntity {

    private Item field_145967_a;
    private int field_145968_i;

    public TileEntityFlowerPot() {}

    public TileEntityFlowerPot(Item item, int i) {
        this.field_145967_a = item;
        this.field_145968_i = i;
    }

    public static void func_189699_a(DataFixer dataconvertermanager) {}

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        ResourceLocation minecraftkey = (ResourceLocation) Item.field_150901_e.func_177774_c(this.field_145967_a);

        nbttagcompound.func_74778_a("Item", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound.func_74768_a("Data", this.field_145968_i);
        return nbttagcompound;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        if (nbttagcompound.func_150297_b("Item", 8)) {
            this.field_145967_a = Item.func_111206_d(nbttagcompound.func_74779_i("Item"));
        } else {
            this.field_145967_a = Item.func_150899_d(nbttagcompound.func_74762_e("Item"));
        }

        this.field_145968_i = nbttagcompound.func_74762_e("Data");
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 5, this.func_189517_E_());
    }

    public NBTTagCompound func_189517_E_() {
        return this.func_189515_b(new NBTTagCompound());
    }

    public void func_190614_a(ItemStack itemstack) {
        this.field_145967_a = itemstack.func_77973_b();
        this.field_145968_i = itemstack.func_77960_j();
    }

    public ItemStack func_184403_b() {
        return this.field_145967_a == null ? ItemStack.field_190927_a : new ItemStack(this.field_145967_a, 1, this.field_145968_i);
    }

    @Nullable
    public Item func_145965_a() {
        return this.field_145967_a;
    }

    public int func_145966_b() {
        return this.field_145968_i;
    }
}
