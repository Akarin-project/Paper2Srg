package net.minecraft.tileentity;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;


public class TileEntityBed extends TileEntity {

    private EnumDyeColor field_193053_a;

    public TileEntityBed() {
        this.field_193053_a = EnumDyeColor.RED;
    }

    public void func_193051_a(ItemStack itemstack) {
        this.func_193052_a(EnumDyeColor.func_176764_b(itemstack.func_77960_j()));
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        if (nbttagcompound.func_74764_b("color")) {
            this.field_193053_a = EnumDyeColor.func_176764_b(nbttagcompound.func_74762_e("color"));
        }

    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74768_a("color", this.field_193053_a.func_176765_a());
        return nbttagcompound;
    }

    public NBTTagCompound func_189517_E_() {
        return this.func_189515_b(new NBTTagCompound());
    }

    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 11, this.func_189517_E_());
    }

    public EnumDyeColor func_193048_a() {
        return this.field_193053_a;
    }

    public void func_193052_a(EnumDyeColor enumcolor) {
        this.field_193053_a = enumcolor;
        this.func_70296_d();
    }

    public ItemStack func_193049_f() {
        return new ItemStack(Items.field_151104_aV, 1, this.field_193053_a.func_176765_a());
    }
}
