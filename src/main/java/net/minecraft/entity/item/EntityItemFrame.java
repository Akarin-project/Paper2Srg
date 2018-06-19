package net.minecraft.entity.item;

// Spigot start
import java.util.UUID;
import org.apache.commons.codec.Charsets;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

// Spigot end
import javax.annotation.Nullable;

public class EntityItemFrame extends EntityHanging {

    private static final DataParameter<ItemStack> field_184525_c = EntityDataManager.func_187226_a(EntityItemFrame.class, DataSerializers.field_187196_f);
    private static final DataParameter<Integer> field_184526_d = EntityDataManager.func_187226_a(EntityItemFrame.class, DataSerializers.field_187192_b);
    private float field_82337_e = 1.0F;

    public EntityItemFrame(World world) {
        super(world);
    }

    public EntityItemFrame(World world, BlockPos blockposition, EnumFacing enumdirection) {
        super(world, blockposition);
        this.func_174859_a(enumdirection);
    }

    protected void func_70088_a() {
        this.func_184212_Q().func_187214_a(EntityItemFrame.field_184525_c, ItemStack.field_190927_a);
        this.func_184212_Q().func_187214_a(EntityItemFrame.field_184526_d, Integer.valueOf(0));
    }

    public float func_70111_Y() {
        return 0.0F;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else if (!damagesource.func_94541_c() && !this.func_82335_i().func_190926_b()) {
            if (!this.field_70170_p.field_72995_K) {
                // CraftBukkit start - fire EntityDamageEvent
                if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f, false) || this.field_70128_L) {
                    return true;
                }
                // CraftBukkit end
                this.func_146065_b(damagesource.func_76346_g(), false);
                this.func_184185_a(SoundEvents.field_187629_cO, 1.0F, 1.0F);
                this.func_82334_a(ItemStack.field_190927_a);
            }

            return true;
        } else {
            return super.func_70097_a(damagesource, f);
        }
    }

    public int func_82329_d() {
        return 12;
    }

    public int func_82330_g() {
        return 12;
    }

    public void func_110128_b(@Nullable Entity entity) {
        this.func_184185_a(SoundEvents.field_187623_cM, 1.0F, 1.0F);
        this.func_146065_b(entity, true);
    }

    public void func_184523_o() {
        this.func_184185_a(SoundEvents.field_187626_cN, 1.0F, 1.0F);
    }

    public void func_146065_b(@Nullable Entity entity, boolean flag) {
        if (this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
            ItemStack itemstack = this.func_82335_i();

            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;

                if (entityhuman.field_71075_bZ.field_75098_d) {
                    this.func_110131_b(itemstack);
                    return;
                }
            }

            if (flag) {
                this.func_70099_a(new ItemStack(Items.field_151160_bD), 0.0F);
            }

            if (!itemstack.func_190926_b() && this.field_70146_Z.nextFloat() < this.field_82337_e) {
                itemstack = itemstack.func_77946_l();
                this.func_110131_b(itemstack);
                this.func_70099_a(itemstack, 0.0F);
            }

        }
    }

    private void func_110131_b(ItemStack itemstack) {
        if (!itemstack.func_190926_b()) {
            if (itemstack.func_77973_b() == Items.field_151098_aY) {
                MapData worldmap = ((ItemMap) itemstack.func_77973_b()).func_77873_a(itemstack, this.field_70170_p);

                worldmap.field_76203_h.remove(UUID.nameUUIDFromBytes(("frame-" + this.func_145782_y()).getBytes(Charsets.US_ASCII))); // Spigot
            }

            itemstack.func_82842_a((EntityItemFrame) null);

            // Paper - MC-124833 - conflicting reports of what server software this does and doesn't affect.
            // It's a one liner with near-zero impact so we'll patch it anyway just in case
            this.func_82334_a(ItemStack.field_190927_a); // OBFHELPER - ItemStack.EMPTY
        }
    }

    public ItemStack func_82335_i() {
        return (ItemStack) this.func_184212_Q().func_187225_a(EntityItemFrame.field_184525_c);
    }

    public void func_82334_a(ItemStack itemstack) {
        this.func_174864_a(itemstack, true);
    }

    private void func_174864_a(ItemStack itemstack, boolean flag) {
        if (!itemstack.func_190926_b()) {
            itemstack = itemstack.func_77946_l();
            itemstack.func_190920_e(1);
            itemstack.func_82842_a(this);
        }

        this.func_184212_Q().func_187227_b(EntityItemFrame.field_184525_c, itemstack);
        this.func_184212_Q().func_187217_b(EntityItemFrame.field_184525_c);
        if (!itemstack.func_190926_b()) {
            this.func_184185_a(SoundEvents.field_187620_cL, 1.0F, 1.0F);
        }

        if (flag && this.field_174861_a != null) {
            this.field_70170_p.func_175666_e(this.field_174861_a, Blocks.field_150350_a);
        }

    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (datawatcherobject.equals(EntityItemFrame.field_184525_c)) {
            ItemStack itemstack = this.func_82335_i();

            if (!itemstack.func_190926_b() && itemstack.func_82836_z() != this) {
                itemstack.func_82842_a(this);
            }
        }

    }

    public int func_82333_j() {
        return ((Integer) this.func_184212_Q().func_187225_a(EntityItemFrame.field_184526_d)).intValue();
    }

    public void func_82336_g(int i) {
        this.func_174865_a(i, true);
    }

    private void func_174865_a(int i, boolean flag) {
        this.func_184212_Q().func_187227_b(EntityItemFrame.field_184526_d, Integer.valueOf(i % 8));
        if (flag && this.field_174861_a != null) {
            this.field_70170_p.func_175666_e(this.field_174861_a, Blocks.field_150350_a);
        }

    }

    public static void func_189738_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityItemFrame.class, new String[] { "Item"})));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        if (!this.func_82335_i().func_190926_b()) {
            nbttagcompound.func_74782_a("Item", this.func_82335_i().func_77955_b(new NBTTagCompound()));
            nbttagcompound.func_74774_a("ItemRotation", (byte) this.func_82333_j());
            nbttagcompound.func_74776_a("ItemDropChance", this.field_82337_e);
        }

        super.func_70014_b(nbttagcompound);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Item");

        if (nbttagcompound1 != null && !nbttagcompound1.func_82582_d()) {
            this.func_174864_a(new ItemStack(nbttagcompound1), false);
            this.func_174865_a(nbttagcompound.func_74771_c("ItemRotation"), false);
            if (nbttagcompound.func_150297_b("ItemDropChance", 99)) {
                this.field_82337_e = nbttagcompound.func_74760_g("ItemDropChance");
            }
        }

        super.func_70037_a(nbttagcompound);
    }

    public boolean func_184230_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!this.field_70170_p.field_72995_K) {
            if (this.func_82335_i().func_190926_b()) {
                if (!itemstack.func_190926_b()) {
                    this.func_82334_a(itemstack);
                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        itemstack.func_190918_g(1);
                    }
                }
            } else {
                this.func_184185_a(SoundEvents.field_187632_cP, 1.0F, 1.0F);
                this.func_82336_g(this.func_82333_j() + 1);
            }
        }

        return true;
    }

    public int func_174866_q() {
        return this.func_82335_i().func_190926_b() ? 0 : this.func_82333_j() % 8 + 1;
    }
}
