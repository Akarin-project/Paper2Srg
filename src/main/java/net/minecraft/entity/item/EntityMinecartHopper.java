package net.minecraft.entity.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper {

    private boolean field_96113_a = true;
    private int field_98044_b = -1;
    private final BlockPos field_174900_c;

    public EntityMinecartHopper(World world) {
        super(world);
        this.field_174900_c = BlockPos.field_177992_a;
    }

    public EntityMinecartHopper(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.field_174900_c = BlockPos.field_177992_a;
    }

    @Override
    public EntityMinecart.Type func_184264_v() {
        return EntityMinecart.Type.HOPPER;
    }

    @Override
    public IBlockState func_180457_u() {
        return Blocks.field_150438_bZ.func_176223_P();
    }

    @Override
    public int func_94085_r() {
        return 1;
    }

    @Override
    public int func_70302_i_() {
        return 5;
    }

    @Override
    public boolean func_184230_a(EntityPlayer entityhuman, EnumHand enumhand) {
        if (!this.field_70170_p.field_72995_K) {
            entityhuman.func_71007_a(this);
        }

        return true;
    }

    @Override
    public void func_96095_a(int i, int j, int k, boolean flag) {
        boolean flag1 = !flag;

        if (flag1 != this.func_96111_ay()) {
            this.func_96110_f(flag1);
        }

    }

    public boolean func_96111_ay() {
        return this.field_96113_a;
    }

    public void func_96110_f(boolean flag) {
        this.field_96113_a = flag;
    }

    @Override
    public World func_130014_f_() {
        return this.field_70170_p;
    }

    @Override
    public double func_96107_aA() {
        return this.field_70165_t;
    }

    @Override
    public double func_96109_aB() {
        return this.field_70163_u + 0.5D;
    }

    @Override
    public double func_96108_aC() {
        return this.field_70161_v;
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (!this.field_70170_p.field_72995_K && this.func_70089_S() && this.func_96111_ay()) {
            BlockPos blockposition = new BlockPos(this);

            if (blockposition.equals(this.field_174900_c)) {
                --this.field_98044_b;
            } else {
                this.func_98042_n(0);
            }

            if (!this.func_98043_aE()) {
                this.func_98042_n(0);
                if (this.func_96112_aD()) {
                    this.func_98042_n(4);
                    this.func_70296_d();
                }
            }
        }

    }

    public boolean func_96112_aD() {
        if (TileEntityHopper.func_145891_a(this)) {
            return true;
        } else {
            List list = this.field_70170_p.func_175647_a(EntityItem.class, this.func_174813_aQ().func_72314_b(0.25D, 0.0D, 0.25D), EntitySelectors.field_94557_a);

            if (!list.isEmpty()) {
                TileEntityHopper.func_145898_a((IInventory) null, this, (EntityItem) list.get(0));
            }

            return false;
        }
    }

    @Override
    public void func_94095_a(DamageSource damagesource) {
        super.func_94095_a(damagesource);
        if (this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
            this.func_145778_a(Item.func_150898_a(Blocks.field_150438_bZ), 1, 0.0F);
        }

    }

    public static void func_189682_a(DataFixer dataconvertermanager) {
        EntityMinecartContainer.func_190574_b(dataconvertermanager, EntityMinecartHopper.class);
    }

    @Override
    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("TransferCooldown", this.field_98044_b);
        nbttagcompound.func_74757_a("Enabled", this.field_96113_a);
    }

    @Override
    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.field_98044_b = nbttagcompound.func_74762_e("TransferCooldown");
        this.field_96113_a = nbttagcompound.func_74764_b("Enabled") ? nbttagcompound.func_74767_n("Enabled") : true;
    }

    public void func_98042_n(int i) {
        this.field_98044_b = i;
    }

    public boolean func_98043_aE() {
        return this.field_98044_b > 0;
    }

    @Override
    public String func_174875_k() {
        return "minecraft:hopper";
    }

    @Override
    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerHopper(playerinventory, this, entityhuman);
    }

    @Override
    public World func_145831_w() {
        return getWorld();
    }
}
