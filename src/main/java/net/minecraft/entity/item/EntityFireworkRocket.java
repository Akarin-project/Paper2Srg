package net.minecraft.entity.item;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityFireworkRocket extends Entity {

    public static final DataParameter<ItemStack> field_184566_a = EntityDataManager.func_187226_a(EntityFireworkRocket.class, DataSerializers.field_187196_f);
    private static final DataParameter<Integer> field_191512_b = EntityDataManager.func_187226_a(EntityFireworkRocket.class, DataSerializers.field_187192_b);
    private int field_92056_a;
    public int field_92055_b;
    public UUID spawningEntity; // Paper
    private EntityLivingBase field_191513_e;public EntityLivingBase getBoostedEntity() { return field_191513_e; } // Paper - OBFHELPER

    public EntityFireworkRocket(World world) {
        super(world);
        this.func_70105_a(0.25F, 0.25F);
    }

    // Spigot Start
    @Override
    public void inactiveTick() {
        this.field_92056_a += 1;
        super.inactiveTick();
    }
    // Spigot End

    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(EntityFireworkRocket.field_184566_a, ItemStack.field_190927_a);
        this.field_70180_af.func_187214_a(EntityFireworkRocket.field_191512_b, Integer.valueOf(0));
    }

    public EntityFireworkRocket(World world, double d0, double d1, double d2, ItemStack itemstack) {
        super(world);
        this.field_92056_a = 0;
        this.func_70105_a(0.25F, 0.25F);
        this.func_70107_b(d0, d1, d2);
        int i = 1;

        if (!itemstack.func_190926_b() && itemstack.func_77942_o()) {
            this.field_70180_af.func_187227_b(EntityFireworkRocket.field_184566_a, itemstack.func_77946_l());
            NBTTagCompound nbttagcompound = itemstack.func_77978_p();
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Fireworks");

            i += nbttagcompound1.func_74771_c("Flight");
        }

        this.field_70159_w = this.field_70146_Z.nextGaussian() * 0.001D;
        this.field_70179_y = this.field_70146_Z.nextGaussian() * 0.001D;
        this.field_70181_x = 0.05D;
        this.field_92055_b = 10 * i + this.field_70146_Z.nextInt(6) + this.field_70146_Z.nextInt(7);
    }

    public EntityFireworkRocket(World world, ItemStack itemstack, EntityLivingBase entityliving) {
        this(world, entityliving.field_70165_t, entityliving.field_70163_u, entityliving.field_70161_v, itemstack);
        this.field_70180_af.func_187227_b(EntityFireworkRocket.field_191512_b, Integer.valueOf(entityliving.func_145782_y()));
        this.field_191513_e = entityliving;
    }

    public void func_70071_h_() {
        this.field_70142_S = this.field_70165_t;
        this.field_70137_T = this.field_70163_u;
        this.field_70136_U = this.field_70161_v;
        super.func_70071_h_();
        if (this.func_191511_j()) {
            if (this.field_191513_e == null) {
                Entity entity = this.field_70170_p.func_73045_a(((Integer) this.field_70180_af.func_187225_a(EntityFireworkRocket.field_191512_b)).intValue());

                if (entity instanceof EntityLivingBase) {
                    this.field_191513_e = (EntityLivingBase) entity;
                }
            }

            if (this.field_191513_e != null) {
                if (this.field_191513_e.func_184613_cA()) {
                    Vec3d vec3d = this.field_191513_e.func_70040_Z();
                    double d0 = 1.5D;
                    double d1 = 0.1D;

                    this.field_191513_e.field_70159_w += vec3d.field_72450_a * 0.1D + (vec3d.field_72450_a * 1.5D - this.field_191513_e.field_70159_w) * 0.5D;
                    this.field_191513_e.field_70181_x += vec3d.field_72448_b * 0.1D + (vec3d.field_72448_b * 1.5D - this.field_191513_e.field_70181_x) * 0.5D;
                    this.field_191513_e.field_70179_y += vec3d.field_72449_c * 0.1D + (vec3d.field_72449_c * 1.5D - this.field_191513_e.field_70179_y) * 0.5D;
                }

                this.func_70107_b(this.field_191513_e.field_70165_t, this.field_191513_e.field_70163_u, this.field_191513_e.field_70161_v);
                this.field_70159_w = this.field_191513_e.field_70159_w;
                this.field_70181_x = this.field_191513_e.field_70181_x;
                this.field_70179_y = this.field_191513_e.field_70179_y;
            }
        } else {
            this.field_70159_w *= 1.15D;
            this.field_70179_y *= 1.15D;
            this.field_70181_x += 0.04D;
            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        }

        float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

        this.field_70177_z = (float) (MathHelper.func_181159_b(this.field_70159_w, this.field_70179_y) * 57.2957763671875D);

        for (this.field_70125_A = (float) (MathHelper.func_181159_b(this.field_70181_x, (double) f) * 57.2957763671875D); this.field_70125_A - this.field_70127_C < -180.0F; this.field_70127_C -= 360.0F) {
            ;
        }

        while (this.field_70125_A - this.field_70127_C >= 180.0F) {
            this.field_70127_C += 360.0F;
        }

        while (this.field_70177_z - this.field_70126_B < -180.0F) {
            this.field_70126_B -= 360.0F;
        }

        while (this.field_70177_z - this.field_70126_B >= 180.0F) {
            this.field_70126_B += 360.0F;
        }

        this.field_70125_A = this.field_70127_C + (this.field_70125_A - this.field_70127_C) * 0.2F;
        this.field_70177_z = this.field_70126_B + (this.field_70177_z - this.field_70126_B) * 0.2F;
        if (this.field_92056_a == 0 && !this.func_174814_R()) {
            this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187631_bo, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++this.field_92056_a;
        if (this.field_70170_p.field_72995_K && this.field_92056_a % 2 < 2) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.FIREWORKS_SPARK, this.field_70165_t, this.field_70163_u - 0.3D, this.field_70161_v, this.field_70146_Z.nextGaussian() * 0.05D, -this.field_70181_x * 0.5D, this.field_70146_Z.nextGaussian() * 0.05D, new int[0]);
        }

        if (!this.field_70170_p.field_72995_K && this.field_92056_a > this.field_92055_b) {
            // CraftBukkit start
            if (!org.bukkit.craftbukkit.event.CraftEventFactory.callFireworkExplodeEvent(this).isCancelled()) {
                this.field_70170_p.func_72960_a(this, (byte) 17);
                this.func_191510_k();
            }
            // CraftBukkit end
            this.func_70106_y();
        }

    }

    private void func_191510_k() {
        float f = 0.0F;
        ItemStack itemstack = (ItemStack) this.field_70180_af.func_187225_a(EntityFireworkRocket.field_184566_a);
        NBTTagCompound nbttagcompound = itemstack.func_190926_b() ? null : itemstack.func_179543_a("Fireworks");
        NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.func_150295_c("Explosions", 10) : null;

        if (nbttaglist != null && !nbttaglist.func_82582_d()) {
            f = (float) (5 + nbttaglist.func_74745_c() * 2);
        }

        if (f > 0.0F) {
            if (this.field_191513_e != null) {
                CraftEventFactory.entityDamage = this; // CraftBukkit
                this.field_191513_e.func_70097_a(DamageSource.field_191552_t, (float) (5 + nbttaglist.func_74745_c() * 2));
                CraftEventFactory.entityDamage = null; // CraftBukkit
            }

            double d0 = 5.0D;
            Vec3d vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            List list = this.field_70170_p.func_72872_a(EntityLivingBase.class, this.func_174813_aQ().func_186662_g(5.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                if (entityliving != this.field_191513_e && this.func_70068_e(entityliving) <= 25.0D) {
                    boolean flag = false;

                    for (int i = 0; i < 2; ++i) {
                        RayTraceResult movingobjectposition = this.field_70170_p.func_147447_a(vec3d, new Vec3d(entityliving.field_70165_t, entityliving.field_70163_u + (double) entityliving.field_70131_O * 0.5D * (double) i, entityliving.field_70161_v), false, true, false);

                        if (movingobjectposition == null || movingobjectposition.field_72313_a == RayTraceResult.Type.MISS) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        float f1 = f * (float) Math.sqrt((5.0D - (double) this.func_70032_d(entityliving)) / 5.0D);

                        CraftEventFactory.entityDamage = this; // CraftBukkit
                        entityliving.func_70097_a(DamageSource.field_191552_t, f1);
                        CraftEventFactory.entityDamage = null; // CraftBukkit
                    }
                }
            }
        }

    }

    public boolean func_191511_j() {
        return ((Integer) this.field_70180_af.func_187225_a(EntityFireworkRocket.field_191512_b)).intValue() > 0;
    }

    public static void func_189656_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityFireworkRocket.class, new String[] { "FireworksItem"})));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("Life", this.field_92056_a);
        nbttagcompound.func_74768_a("LifeTime", this.field_92055_b);
        ItemStack itemstack = (ItemStack) this.field_70180_af.func_187225_a(EntityFireworkRocket.field_184566_a);

        if (!itemstack.func_190926_b()) {
            nbttagcompound.func_74782_a("FireworksItem", itemstack.func_77955_b(new NBTTagCompound()));
        }
        // Paper start
        if (spawningEntity != null) {
            nbttagcompound.setUUID("SpawningEntity", spawningEntity);
        }
        // Paper end

    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_92056_a = nbttagcompound.func_74762_e("Life");
        this.field_92055_b = nbttagcompound.func_74762_e("LifeTime");
        NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("FireworksItem");

        if (nbttagcompound1 != null) {
            ItemStack itemstack = new ItemStack(nbttagcompound1);

            if (!itemstack.func_190926_b()) {
                this.field_70180_af.func_187227_b(EntityFireworkRocket.field_184566_a, itemstack);
            }
        }
        // Paper start
        if (nbttagcompound.hasUUID("SpawningEntity")) {
            spawningEntity = nbttagcompound.getUUID("SpawningEntity");
        }
        // Paper end
    }

    public boolean func_70075_an() {
        return false;
    }
}
