package net.minecraft.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature {

    private static final DataParameter<Boolean> field_184751_bv = EntityDataManager.func_187226_a(EntityAgeable.class, DataSerializers.field_187198_h);
    protected int field_175504_a;
    protected int field_175502_b;
    protected int field_175503_c;
    private float field_98056_d = -1.0F;
    private float field_98057_e;
    public boolean ageLocked; // CraftBukkit

    // Spigot start
    @Override
    public void inactiveTick()
    {
        super.inactiveTick();
        if ( this.field_70170_p.field_72995_K || this.ageLocked )
        { // CraftBukkit
            this.func_98054_a( this.func_70631_g_() );
        } else
        {
            int i = this.func_70874_b();

            if ( i < 0 )
            {
                ++i;
                this.func_70873_a( i );
            } else if ( i > 0 )
            {
                --i;
                this.func_70873_a( i );
            }
        }
    }
    // Spigot end

    public EntityAgeable(World world) {
        super(world);
    }

    @Nullable
    public abstract EntityAgeable func_90011_a(EntityAgeable entityageable);

    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (itemstack.func_77973_b() == Items.field_151063_bx) {
            if (!this.field_70170_p.field_72995_K) {
                Class oclass = (Class) EntityList.field_191308_b.func_82594_a(ItemMonsterPlacer.func_190908_h(itemstack));

                if (oclass != null && this.getClass() == oclass) {
                    EntityAgeable entityageable = this.func_90011_a(this);

                    if (entityageable != null) {
                        entityageable.func_70873_a(-24000);
                        entityageable.func_70012_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.0F, 0.0F);
                        this.field_70170_p.addEntity(entityageable, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG); // CraftBukkit
                        if (itemstack.func_82837_s()) {
                            entityageable.func_96094_a(itemstack.func_82833_r());
                        }

                        if (!entityhuman.field_71075_bZ.field_75098_d) {
                            itemstack.func_190918_g(1);
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean func_190669_a(ItemStack itemstack, Class<? extends Entity> oclass) {
        if (itemstack.func_77973_b() != Items.field_151063_bx) {
            return false;
        } else {
            Class oclass1 = (Class) EntityList.field_191308_b.func_82594_a(ItemMonsterPlacer.func_190908_h(itemstack));

            return oclass1 != null && oclass == oclass1;
        }
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityAgeable.field_184751_bv, Boolean.valueOf(false));
    }

    public int func_70874_b() {
        return this.field_70170_p.field_72995_K ? (((Boolean) this.field_70180_af.func_187225_a(EntityAgeable.field_184751_bv)).booleanValue() ? -1 : 1) : this.field_175504_a;
    }

    public void func_175501_a(int i, boolean flag) {
        int j = this.func_70874_b();
        int k = j;

        j += i * 20;
        if (j > 0) {
            j = 0;
            if (k < 0) {
                this.func_175500_n();
            }
        }

        int l = j - k;

        this.func_70873_a(j);
        if (flag) {
            this.field_175502_b += l;
            if (this.field_175503_c == 0) {
                this.field_175503_c = 40;
            }
        }

        if (this.func_70874_b() == 0) {
            this.func_70873_a(this.field_175502_b);
        }

    }

    public void func_110195_a(int i) {
        this.func_175501_a(i, false);
    }

    public void func_70873_a(int i) {
        this.field_70180_af.func_187227_b(EntityAgeable.field_184751_bv, Boolean.valueOf(i < 0));
        this.field_175504_a = i;
        this.func_98054_a(this.func_70631_g_());
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("Age", this.func_70874_b());
        nbttagcompound.func_74768_a("ForcedAge", this.field_175502_b);
        nbttagcompound.func_74757_a("AgeLocked", this.ageLocked); // CraftBukkit
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_70873_a(nbttagcompound.func_74762_e("Age"));
        this.field_175502_b = nbttagcompound.func_74762_e("ForcedAge");
        this.ageLocked = nbttagcompound.func_74767_n("AgeLocked"); // CraftBukkit
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityAgeable.field_184751_bv.equals(datawatcherobject)) {
            this.func_98054_a(this.func_70631_g_());
        }

        super.func_184206_a(datawatcherobject);
    }

    public void func_70636_d() {
        super.func_70636_d();
        if (this.field_70170_p.field_72995_K || ageLocked) { // CraftBukkit
            if (this.field_175503_c > 0) {
                if (this.field_175503_c % 4 == 0) {
                    this.field_70170_p.func_175688_a(EnumParticleTypes.VILLAGER_HAPPY, this.field_70165_t + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, this.field_70163_u + 0.5D + (double) (this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                --this.field_175503_c;
            }
        } else {
            int i = this.func_70874_b();

            if (i < 0) {
                ++i;
                this.func_70873_a(i);
                if (i == 0) {
                    this.func_175500_n();
                }
            } else if (i > 0) {
                --i;
                this.func_70873_a(i);
            }
        }

    }

    protected void func_175500_n() {}

    public boolean func_70631_g_() {
        return this.func_70874_b() < 0;
    }

    public void func_98054_a(boolean flag) {
        this.func_98055_j(flag ? 0.5F : 1.0F);
    }

    public final void func_70105_a(float f, float f1) {
        boolean flag = this.field_98056_d > 0.0F;

        this.field_98056_d = f;
        this.field_98057_e = f1;
        if (!flag) {
            this.func_98055_j(1.0F);
        }

    }

    protected final void func_98055_j(float f) {
        super.func_70105_a(this.field_98056_d * f, this.field_98057_e * f);
    }
}
