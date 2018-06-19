package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityChest extends TileEntityLockableLoot { // Paper - Remove ITickable

    private NonNullList<ItemStack> field_145985_p;
    public boolean field_145984_a;
    public TileEntityChest field_145992_i; // Paper - Adjacent Chest Z Neg
    public TileEntityChest field_145990_j; // Paper - Adjacent Chest X Pos
    public TileEntityChest field_145991_k; // Paper - Adjacent Chest X Neg
    public TileEntityChest field_145988_l; // Paper - Adjacent Chest Z Pos
    public float field_145989_m; // Paper - lid angle
    public float field_145986_n;
    public int field_145987_o; // Paper - Number of viewers
    private int field_145983_q;
    private BlockChest.Type field_145982_r;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.field_145985_p;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityChest() {
        this.field_145985_p = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
    }

    public TileEntityChest(BlockChest.Type blockchest_type) {
        this.field_145985_p = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
        this.field_145982_r = blockchest_type;
    }

    public int func_70302_i_() {
        return 27;
    }

    public boolean func_191420_l() {
        Iterator iterator = this.field_145985_p.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.func_190926_b());

        return false;
    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.field_190577_o : "container.chest";
    }

    public static void func_189677_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityChest.class, new String[] { "Items"})));
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_145985_p = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
        if (!this.func_184283_b(nbttagcompound)) {
            ItemStackHelper.func_191283_b(nbttagcompound, this.field_145985_p);
        }

        if (nbttagcompound.func_150297_b("CustomName", 8)) {
            this.field_190577_o = nbttagcompound.func_74779_i("CustomName");
        }

    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        if (!this.func_184282_c(nbttagcompound)) {
            ItemStackHelper.func_191282_a(nbttagcompound, this.field_145985_p);
        }

        if (this.func_145818_k_()) {
            nbttagcompound.func_74778_a("CustomName", this.field_190577_o);
        }

        return nbttagcompound;
    }

    public int func_70297_j_() {
        return maxStack; // CraftBukkit
    }

    public void func_145836_u() {
        super.func_145836_u();
        this.field_145984_a = false;
    }

    private void func_174910_a(TileEntityChest tileentitychest, EnumFacing enumdirection) {
        if (tileentitychest.func_145837_r()) {
            this.field_145984_a = false;
        } else if (this.field_145984_a) {
            switch (enumdirection) {
            case NORTH:
                if (this.field_145992_i != tileentitychest) {
                    this.field_145984_a = false;
                }
                break;

            case SOUTH:
                if (this.field_145988_l != tileentitychest) {
                    this.field_145984_a = false;
                }
                break;

            case EAST:
                if (this.field_145990_j != tileentitychest) {
                    this.field_145984_a = false;
                }
                break;

            case WEST:
                if (this.field_145991_k != tileentitychest) {
                    this.field_145984_a = false;
                }
            }
        }

    }

    public void func_145979_i() {
        if (!this.field_145984_a) {
            this.field_145984_a = true;
            this.field_145991_k = this.func_174911_a(EnumFacing.WEST);
            this.field_145990_j = this.func_174911_a(EnumFacing.EAST);
            this.field_145992_i = this.func_174911_a(EnumFacing.NORTH);
            this.field_145988_l = this.func_174911_a(EnumFacing.SOUTH);
        }
    }

    @Nullable
    protected TileEntityChest func_174911_a(EnumFacing enumdirection) {
        BlockPos blockposition = this.field_174879_c.func_177972_a(enumdirection);

        if (this.func_174912_b(blockposition)) {
            TileEntity tileentity = this.field_145850_b.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityChest) {
                TileEntityChest tileentitychest = (TileEntityChest) tileentity;

                tileentitychest.func_174910_a(this, enumdirection.func_176734_d());
                return tileentitychest;
            }
        }

        return null;
    }

    private boolean func_174912_b(BlockPos blockposition) {
        if (this.field_145850_b == null) {
            return false;
        } else {
            Block block = this.field_145850_b.func_180495_p(blockposition).func_177230_c();

            return block instanceof BlockChest && ((BlockChest) block).field_149956_a == this.func_145980_j();
        }
    }

    public void func_73660_a() {
        // Paper - Disable all of this, just in case this does get ticked
        /*
        this.o();
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();

        ++this.q;
        float f;

        if (!this.world.isClientSide && this.l != 0 && (this.q + i + j + k) % 200 == 0) {
            this.l = 0;
            f = 5.0F;
            List list = this.world.a(EntityHuman.class, new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F), (double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F), (double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityHuman entityhuman = (EntityHuman) iterator.next();

                if (entityhuman.activeContainer instanceof ContainerChest) {
                    IInventory iinventory = ((ContainerChest) entityhuman.activeContainer).e();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest) iinventory).a((IInventory) this)) {
                        ++this.l;
                    }
                }
            }
        }

        this.k = this.j;
        f = 0.1F;
        double d0;

        if (this.l > 0 && this.j == 0.0F && this.f == null && this.h == null) {
            double d1 = (double) i + 0.5D;

            d0 = (double) k + 0.5D;
            if (this.i != null) {
                d0 += 0.5D;
            }

            if (this.g != null) {
                d1 += 0.5D;
            }

            this.world.a((EntityHuman) null, d1, (double) j + 0.5D, d0, SoundEffects.ac, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.l == 0 && this.j > 0.0F || this.l > 0 && this.j < 1.0F) {
            float f1 = this.j;

            if (this.l > 0) {
                this.j += 0.1F;
            } else {
                this.j -= 0.1F;
            }

            if (this.j > 1.0F) {
                this.j = 1.0F;
            }

            float f2 = 0.5F;

            if (this.j < 0.5F && f1 >= 0.5F && this.f == null && this.h == null) {
                d0 = (double) i + 0.5D;
                double d2 = (double) k + 0.5D;

                if (this.i != null) {
                    d2 += 0.5D;
                }

                if (this.g != null) {
                    d0 += 0.5D;
                }

                this.world.a((EntityHuman) null, d0, (double) j + 0.5D, d2, SoundEffects.aa, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.j < 0.0F) {
                this.j = 0.0F;
            }
        }
        */
        // Paper end
    }

    public boolean func_145842_c(int i, int j) {
        if (i == 1) {
            this.field_145987_o = j;
            return true;
        } else {
            return super.func_145842_c(i, j);
        }
    }

    public void func_174889_b(EntityPlayer entityhuman) {
        if (!entityhuman.func_175149_v()) {
            if (this.field_145987_o < 0) {
                this.field_145987_o = 0;
            }
            int oldPower = Math.max(0, Math.min(15, this.field_145987_o)); // CraftBukkit - Get power before new viewer is added

            ++this.field_145987_o;

            // Paper start - Move chest open sound out of the tick loop
            this.func_145979_i();

            if (this.field_145987_o > 0 && this.field_145989_m == 0.0F && this.field_145992_i == null && this.field_145991_k == null) {
                this.field_145989_m = 0.7F;

                double d0 = (double) this.field_174879_c.func_177952_p() + 0.5D;
                double d1 = (double) this.field_174879_c.func_177958_n() + 0.5D;

                if (this.field_145988_l != null) {
                    d0 += 0.5D;
                }

                if (this.field_145990_j != null) {
                    d1 += 0.5D;
                }

                this.field_145850_b.func_184148_a((EntityPlayer) null, d1, (double) this.field_174879_c.func_177956_o() + 0.5D, d0, SoundEvents.field_187657_V, SoundCategory.BLOCKS, 0.5F, this.field_145850_b.field_73012_v.nextFloat() * 0.1F + 0.9F);
            }
            // Paper end

            if (this.field_145850_b == null) return; // CraftBukkit
            this.field_145850_b.func_175641_c(this.field_174879_c, this.func_145838_q(), 1, this.field_145987_o);

            // CraftBukkit start - Call redstone event
            if (this.func_145838_q() == Blocks.field_150447_bR) {
                int newPower = Math.max(0, Math.min(15, this.field_145987_o));

                if (oldPower != newPower) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(field_145850_b, field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p(), oldPower, newPower);
                }
            }
            // CraftBukkit end
            this.field_145850_b.func_175685_c(this.field_174879_c, this.func_145838_q(), false);
            if (this.func_145980_j() == BlockChest.Type.TRAP) {
                this.field_145850_b.func_175685_c(this.field_174879_c.func_177977_b(), this.func_145838_q(), false);
            }
        }

    }

    public void func_174886_c(EntityPlayer entityhuman) {
        if (!entityhuman.func_175149_v() && this.func_145838_q() instanceof BlockChest) {
            int oldPower = Math.max(0, Math.min(15, this.field_145987_o)); // CraftBukkit - Get power before new viewer is added
            --this.field_145987_o;

            // Paper start - Move chest close sound out of the tick loop
            if (this.field_145987_o == 0 && this.field_145989_m > 0.0F || this.field_145987_o > 0 && this.field_145989_m < 1.0F) {
                float f = 0.1F;

                if (this.field_145987_o > 0) {
                    this.field_145989_m += f;
                } else {
                    this.field_145989_m -= f;
                }

                double d0 = (double) this.func_174877_v().func_177958_n() + 0.5D;
                double d2 = (double) this.func_174877_v().func_177952_p() + 0.5D;
                int yLoc = this.field_174879_c.func_177956_o();

                if (this.field_145988_l != null) {
                    d2 += 0.5D;
                }

                if (this.field_145990_j != null) {
                    d0 += 0.5D;
                }

                this.field_145850_b.func_184148_a((EntityPlayer) null, d0, (double) yLoc + 0.5D, d2, SoundEvents.field_187651_T, SoundCategory.BLOCKS, 0.5F, this.field_145850_b.field_73012_v.nextFloat() * 0.1F + 0.9F);
                this.field_145989_m = 0.0F;
            }
            // Paper end

            this.field_145850_b.func_175641_c(this.field_174879_c, this.func_145838_q(), 1, this.field_145987_o);
            this.field_145850_b.func_175685_c(this.field_174879_c, this.func_145838_q(), false);

            // CraftBukkit start - Call redstone event
            if (this.func_145980_j() == BlockChest.Type.TRAP) {
                int newPower = Math.max(0, Math.min(15, this.field_145987_o));

                if (oldPower != newPower) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(field_145850_b, field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p(), oldPower, newPower);
                }
                this.field_145850_b.func_175685_c(this.field_174879_c.func_177977_b(), this.func_145838_q(), false);
            }
            // CraftBukkit end
        }

    }

    public void func_145843_s() {
        super.func_145843_s();
        this.func_145836_u();
        this.func_145979_i();
    }

    public BlockChest.Type func_145980_j() {
        if (this.field_145982_r == null) {
            if (this.field_145850_b == null || !(this.func_145838_q() instanceof BlockChest)) {
                return BlockChest.Type.BASIC;
            }

            this.field_145982_r = ((BlockChest) this.func_145838_q()).field_149956_a;
        }

        return this.field_145982_r;
    }

    public String func_174875_k() {
        return "minecraft:chest";
    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        this.func_184281_d(entityhuman);
        return new ContainerChest(playerinventory, this, entityhuman);
    }

    protected NonNullList<ItemStack> func_190576_q() {
        return this.field_145985_p;
    }

    // CraftBukkit start
    @Override
    public boolean func_183000_F() {
        return true;
    }
    // CraftBukkit end
}
