package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;

// CraftBukkit start
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
// CraftBukkit end

public abstract class EntityArrow extends Entity implements IProjectile {

    private static final Predicate<Entity> field_184553_f = Predicates.and(new Predicate[] { EntitySelectors.field_180132_d, EntitySelectors.field_94557_a, new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity.func_70067_L();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    }});
    private static final DataParameter<Byte> field_184554_g = EntityDataManager.func_187226_a(EntityArrow.class, DataSerializers.field_187191_a);
    public int field_145791_d; // PAIL: private->public
    public int field_145792_e; // PAIL: private->public
    public int field_145789_f; // PAIL: private->public
    private Block field_145790_g;
    private int field_70253_h;
    public boolean field_70254_i;
    protected int field_184552_b;
    public EntityArrow.PickupStatus field_70251_a;
    public int field_70249_b;
    public Entity field_70250_c;
    private int field_70252_j;
    private int field_70257_an;
    private double field_70255_ao;
    public int field_70256_ap;

    // Spigot Start
    @Override
    public void inactiveTick()
    {
        if ( this.field_70254_i )
        {
            this.field_70252_j += 1; // Despawn counter. First int after shooter
        }
        super.inactiveTick();
    }
    // Spigot End

    public EntityArrow(World world) {
        super(world);
        this.field_145791_d = -1;
        this.field_145792_e = -1;
        this.field_145789_f = -1;
        this.field_70251_a = EntityArrow.PickupStatus.DISALLOWED;
        this.field_70255_ao = 2.0D;
        this.func_70105_a(0.5F, 0.5F);
    }

    public EntityArrow(World world, double d0, double d1, double d2) {
        this(world);
        this.func_70107_b(d0, d1, d2);
    }

    public EntityArrow(World world, EntityLivingBase entityliving) {
        this(world, entityliving.field_70165_t, entityliving.field_70163_u + (double) entityliving.func_70047_e() - 0.10000000149011612D, entityliving.field_70161_v);
        this.field_70250_c = entityliving;
        this.projectileSource = (LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
        if (entityliving instanceof EntityPlayer) {
            this.field_70251_a = EntityArrow.PickupStatus.ALLOWED;
        }

    }

    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(EntityArrow.field_184554_g, Byte.valueOf((byte) 0));
    }

    public void func_184547_a(Entity entity, float f, float f1, float f2, float f3, float f4) {
        float f5 = -MathHelper.func_76126_a(f1 * 0.017453292F) * MathHelper.func_76134_b(f * 0.017453292F);
        float f6 = -MathHelper.func_76126_a(f * 0.017453292F);
        float f7 = MathHelper.func_76134_b(f1 * 0.017453292F) * MathHelper.func_76134_b(f * 0.017453292F);

        this.func_70186_c((double) f5, (double) f6, (double) f7, f3, f4);
        this.field_70159_w += entity.field_70159_w;
        this.field_70179_y += entity.field_70179_y;
        if (!entity.field_70122_E) {
            this.field_70181_x += entity.field_70181_x;
        }

    }

    public void func_70186_c(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= (double) f2;
        d1 /= (double) f2;
        d2 /= (double) f2;
        d0 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double) f1;
        d1 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double) f1;
        d2 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double) f1;
        d0 *= (double) f;
        d1 *= (double) f;
        d2 *= (double) f;
        this.field_70159_w = d0;
        this.field_70181_x = d1;
        this.field_70179_y = d2;
        float f3 = MathHelper.func_76133_a(d0 * d0 + d2 * d2);

        this.field_70177_z = (float) (MathHelper.func_181159_b(d0, d2) * 57.2957763671875D);
        this.field_70125_A = (float) (MathHelper.func_181159_b(d1, (double) f3) * 57.2957763671875D);
        this.field_70126_B = this.field_70177_z;
        this.field_70127_C = this.field_70125_A;
        this.field_70252_j = 0;
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_70127_C == 0.0F && this.field_70126_B == 0.0F) {
            float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

            this.field_70177_z = (float) (MathHelper.func_181159_b(this.field_70159_w, this.field_70179_y) * 57.2957763671875D);
            this.field_70125_A = (float) (MathHelper.func_181159_b(this.field_70181_x, (double) f) * 57.2957763671875D);
            this.field_70126_B = this.field_70177_z;
            this.field_70127_C = this.field_70125_A;
        }

        BlockPos blockposition = new BlockPos(this.field_145791_d, this.field_145792_e, this.field_145789_f);
        IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();

        if (iblockdata.func_185904_a() != Material.field_151579_a) {
            AxisAlignedBB axisalignedbb = iblockdata.func_185890_d(this.field_70170_p, blockposition);

            if (axisalignedbb != Block.field_185506_k && axisalignedbb.func_186670_a(blockposition).func_72318_a(new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v))) {
                this.field_70254_i = true;
            }
        }

        if (this.field_70249_b > 0) {
            --this.field_70249_b;
        }

        if (this.field_70254_i) {
            int i = block.func_176201_c(iblockdata);

            if ((block != this.field_145790_g || i != this.field_70253_h) && !this.field_70170_p.func_184143_b(this.func_174813_aQ().func_186662_g(0.05D))) {
                this.field_70254_i = false;
                this.field_70159_w *= (double) (this.field_70146_Z.nextFloat() * 0.2F);
                this.field_70181_x *= (double) (this.field_70146_Z.nextFloat() * 0.2F);
                this.field_70179_y *= (double) (this.field_70146_Z.nextFloat() * 0.2F);
                this.field_70252_j = 0;
                this.field_70257_an = 0;
            } else {
                ++this.field_70252_j;
                if (this.field_70252_j >= (field_70251_a != PickupStatus.DISALLOWED ? field_70170_p.spigotConfig.arrowDespawnRate : field_70170_p.paperConfig.nonPlayerArrowDespawnRate)) { // Spigot - First int after shooter // Paper
                    this.func_70106_y();
                }
            }

            ++this.field_184552_b;
        } else {
            this.field_184552_b = 0;
            ++this.field_70257_an;
            Vec3d vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            Vec3d vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
            RayTraceResult movingobjectposition = this.field_70170_p.func_147447_a(vec3d, vec3d1, false, true, false);

            vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
            if (movingobjectposition != null) {
                vec3d1 = new Vec3d(movingobjectposition.field_72307_f.field_72450_a, movingobjectposition.field_72307_f.field_72448_b, movingobjectposition.field_72307_f.field_72449_c);
            }

            Entity entity = this.func_184551_a(vec3d, vec3d1);

            if (entity != null) {
                movingobjectposition = new RayTraceResult(entity);
            }

            if (movingobjectposition != null && movingobjectposition.field_72308_g instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) movingobjectposition.field_72308_g;

                if (this.field_70250_c instanceof EntityPlayer && !((EntityPlayer) this.field_70250_c).func_96122_a(entityhuman)) {
                    movingobjectposition = null;
                }
            }

            // Paper start - Call ProjectileCollideEvent
            if (movingobjectposition != null && movingobjectposition.field_72308_g != null) {
                com.destroystokyo.paper.event.entity.ProjectileCollideEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileCollideEvent(this, movingobjectposition);
                if (event.isCancelled()) {
                    movingobjectposition = null;
                }
            }
            // Paper end

            if (movingobjectposition != null) {
                this.func_184549_a(movingobjectposition);
            }

            if (this.func_70241_g()) {
                for (int j = 0; j < 4; ++j) {
                    this.field_70170_p.func_175688_a(EnumParticleTypes.CRIT, this.field_70165_t + this.field_70159_w * (double) j / 4.0D, this.field_70163_u + this.field_70181_x * (double) j / 4.0D, this.field_70161_v + this.field_70179_y * (double) j / 4.0D, -this.field_70159_w, -this.field_70181_x + 0.2D, -this.field_70179_y, new int[0]);
                }
            }

            this.field_70165_t += this.field_70159_w;
            this.field_70163_u += this.field_70181_x;
            this.field_70161_v += this.field_70179_y;
            float f1 = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

            this.field_70177_z = (float) (MathHelper.func_181159_b(this.field_70159_w, this.field_70179_y) * 57.2957763671875D);

            for (this.field_70125_A = (float) (MathHelper.func_181159_b(this.field_70181_x, (double) f1) * 57.2957763671875D); this.field_70125_A - this.field_70127_C < -180.0F; this.field_70127_C -= 360.0F) {
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
            float f2 = 0.99F;
            float f3 = 0.05F;

            if (this.func_70090_H()) {
                for (int k = 0; k < 4; ++k) {
                    float f4 = 0.25F;

                    this.field_70170_p.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.field_70165_t - this.field_70159_w * 0.25D, this.field_70163_u - this.field_70181_x * 0.25D, this.field_70161_v - this.field_70179_y * 0.25D, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
                }

                f2 = 0.6F;
            }

            if (this.func_70026_G()) {
                this.func_70066_B();
            }

            this.field_70159_w *= (double) f2;
            this.field_70181_x *= (double) f2;
            this.field_70179_y *= (double) f2;
            if (!this.func_189652_ae()) {
                this.field_70181_x -= 0.05000000074505806D;
            }

            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            this.func_145775_I();
        }
    }

    protected void func_184549_a(RayTraceResult movingobjectposition) {
        Entity entity = movingobjectposition.field_72308_g;
        org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition); // CraftBukkit - Call event
        if (entity != null) {
            float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
            int i = MathHelper.func_76143_f((double) f * this.field_70255_ao);

            if (this.func_70241_g()) {
                i += this.field_70146_Z.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (this.field_70250_c == null) {
                damagesource = DamageSource.func_76353_a(this, this);
            } else {
                damagesource = DamageSource.func_76353_a(this, this.field_70250_c);
            }

            if (this.func_70027_ad() && !(entity instanceof EntityEnderman)) {
                // CraftBukkit start
                EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 5);
                org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);
                if (!combustEvent.isCancelled()) {
                    entity.func_70015_d(combustEvent.getDuration());
                }
                // CraftBukkit end
            }

            if (entity.func_70097_a(damagesource, (float) i)) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase entityliving = (EntityLivingBase) entity;

                    if (!this.field_70170_p.field_72995_K) {
                        entityliving.func_85034_r(entityliving.func_85035_bI() + 1);
                    }

                    if (this.field_70256_ap > 0) {
                        float f1 = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

                        if (f1 > 0.0F) {
                            entityliving.func_70024_g(this.field_70159_w * (double) this.field_70256_ap * 0.6000000238418579D / (double) f1, 0.1D, this.field_70179_y * (double) this.field_70256_ap * 0.6000000238418579D / (double) f1);
                        }
                    }

                    if (this.field_70250_c instanceof EntityLivingBase) {
                        EnchantmentHelper.func_151384_a(entityliving, this.field_70250_c);
                        EnchantmentHelper.func_151385_b((EntityLivingBase) this.field_70250_c, (Entity) entityliving);
                    }

                    this.func_184548_a(entityliving);
                    if (this.field_70250_c != null && entityliving != this.field_70250_c && entityliving instanceof EntityPlayer && this.field_70250_c instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) this.field_70250_c).field_71135_a.func_147359_a(new SPacketChangeGameState(6, 0.0F));
                    }
                }

                this.func_184185_a(SoundEvents.field_187731_t, 1.0F, 1.2F / (this.field_70146_Z.nextFloat() * 0.2F + 0.9F));
                if (!(entity instanceof EntityEnderman)) {
                    this.func_70106_y();
                }
            } else {
                this.field_70159_w *= -0.10000000149011612D;
                this.field_70181_x *= -0.10000000149011612D;
                this.field_70179_y *= -0.10000000149011612D;
                this.field_70177_z += 180.0F;
                this.field_70126_B += 180.0F;
                this.field_70257_an = 0;
                if (!this.field_70170_p.field_72995_K && this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y < 0.0010000000474974513D) {
                    if (this.field_70251_a == EntityArrow.PickupStatus.ALLOWED) {
                        this.func_70099_a(this.func_184550_j(), 0.1F);
                    }

                    this.func_70106_y();
                }
            }
        } else {
            BlockPos blockposition = movingobjectposition.func_178782_a();

            this.field_145791_d = blockposition.func_177958_n();
            this.field_145792_e = blockposition.func_177956_o();
            this.field_145789_f = blockposition.func_177952_p();
            IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);

            this.field_145790_g = iblockdata.func_177230_c();
            this.field_70253_h = this.field_145790_g.func_176201_c(iblockdata);
            this.field_70159_w = (double) ((float) (movingobjectposition.field_72307_f.field_72450_a - this.field_70165_t));
            this.field_70181_x = (double) ((float) (movingobjectposition.field_72307_f.field_72448_b - this.field_70163_u));
            this.field_70179_y = (double) ((float) (movingobjectposition.field_72307_f.field_72449_c - this.field_70161_v));
            float f2 = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);

            this.field_70165_t -= this.field_70159_w / (double) f2 * 0.05000000074505806D;
            this.field_70163_u -= this.field_70181_x / (double) f2 * 0.05000000074505806D;
            this.field_70161_v -= this.field_70179_y / (double) f2 * 0.05000000074505806D;
            this.func_184185_a(SoundEvents.field_187731_t, 1.0F, 1.2F / (this.field_70146_Z.nextFloat() * 0.2F + 0.9F));
            this.field_70254_i = true;
            this.field_70249_b = 7;
            this.func_70243_d(false);
            if (iblockdata.func_185904_a() != Material.field_151579_a) {
                this.field_145790_g.func_180634_a(this.field_70170_p, blockposition, iblockdata, (Entity) this);
            }
        }

    }

    public void func_70091_d(MoverType enummovetype, double d0, double d1, double d2) {
        super.func_70091_d(enummovetype, d0, d1, d2);
        if (this.field_70254_i) {
            this.field_145791_d = MathHelper.func_76128_c(this.field_70165_t);
            this.field_145792_e = MathHelper.func_76128_c(this.field_70163_u);
            this.field_145789_f = MathHelper.func_76128_c(this.field_70161_v);
        }

    }

    protected void func_184548_a(EntityLivingBase entityliving) {}

    @Nullable
    protected Entity func_184551_a(Vec3d vec3d, Vec3d vec3d1) {
        Entity entity = null;
        List list = this.field_70170_p.func_175674_a(this, this.func_174813_aQ().func_72321_a(this.field_70159_w, this.field_70181_x, this.field_70179_y).func_186662_g(1.0D), EntityArrow.field_184553_f);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1 != this.field_70250_c || this.field_70257_an >= 5) {
                AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_186662_g(0.30000001192092896D);
                RayTraceResult movingobjectposition = axisalignedbb.func_72327_a(vec3d, vec3d1);

                if (movingobjectposition != null) {
                    double d1 = vec3d.func_72436_e(movingobjectposition.field_72307_f);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    public static void func_189657_a(DataFixer dataconvertermanager, String s) {}

    public static void func_189658_a(DataFixer dataconvertermanager) {
        func_189657_a(dataconvertermanager, "Arrow");
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("xTile", this.field_145791_d);
        nbttagcompound.func_74768_a("yTile", this.field_145792_e);
        nbttagcompound.func_74768_a("zTile", this.field_145789_f);
        nbttagcompound.func_74777_a("life", (short) this.field_70252_j);
        ResourceLocation minecraftkey = (ResourceLocation) Block.field_149771_c.func_177774_c(this.field_145790_g);

        nbttagcompound.func_74778_a("inTile", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound.func_74774_a("inData", (byte) this.field_70253_h);
        nbttagcompound.func_74774_a("shake", (byte) this.field_70249_b);
        nbttagcompound.func_74774_a("inGround", (byte) (this.field_70254_i ? 1 : 0));
        nbttagcompound.func_74774_a("pickup", (byte) this.field_70251_a.ordinal());
        nbttagcompound.func_74780_a("damage", this.field_70255_ao);
        nbttagcompound.func_74757_a("crit", this.func_70241_g());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_145791_d = nbttagcompound.func_74762_e("xTile");
        this.field_145792_e = nbttagcompound.func_74762_e("yTile");
        this.field_145789_f = nbttagcompound.func_74762_e("zTile");
        this.field_70252_j = nbttagcompound.func_74765_d("life");
        if (nbttagcompound.func_150297_b("inTile", 8)) {
            this.field_145790_g = Block.func_149684_b(nbttagcompound.func_74779_i("inTile"));
        } else {
            this.field_145790_g = Block.func_149729_e(nbttagcompound.func_74771_c("inTile") & 255);
        }

        this.field_70253_h = nbttagcompound.func_74771_c("inData") & 255;
        this.field_70249_b = nbttagcompound.func_74771_c("shake") & 255;
        this.field_70254_i = nbttagcompound.func_74771_c("inGround") == 1;
        if (nbttagcompound.func_150297_b("damage", 99)) {
            this.field_70255_ao = nbttagcompound.func_74769_h("damage");
        }

        if (nbttagcompound.func_150297_b("pickup", 99)) {
            this.field_70251_a = EntityArrow.PickupStatus.func_188795_a(nbttagcompound.func_74771_c("pickup"));
        } else if (nbttagcompound.func_150297_b("player", 99)) {
            this.field_70251_a = nbttagcompound.func_74767_n("player") ? EntityArrow.PickupStatus.ALLOWED : EntityArrow.PickupStatus.DISALLOWED;
        }

        this.func_70243_d(nbttagcompound.func_74767_n("crit"));
    }

    public void func_70100_b_(EntityPlayer entityhuman) {
        if (!this.field_70170_p.field_72995_K && this.field_70254_i && this.field_70249_b <= 0) {
            // CraftBukkit start
            ItemStack itemstack = this.func_184550_j(); // PAIL: rename
            EntityItem item = new EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, itemstack);
            if (this.field_70251_a == PickupStatus.ALLOWED && entityhuman.field_71071_by.canHold(itemstack) > 0) {
                PlayerPickupArrowEvent event = new PlayerPickupArrowEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), new org.bukkit.craftbukkit.entity.CraftItem(this.field_70170_p.getServer(), this, item), (org.bukkit.entity.Arrow) this.getBukkitEntity());
                // event.setCancelled(!entityhuman.canPickUpLoot); TODO
                this.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
            }
            boolean flag = this.field_70251_a == EntityArrow.PickupStatus.ALLOWED || this.field_70251_a == EntityArrow.PickupStatus.CREATIVE_ONLY && entityhuman.field_71075_bZ.field_75098_d;

            if (this.field_70251_a == EntityArrow.PickupStatus.ALLOWED && !entityhuman.field_71071_by.func_70441_a(item.func_92059_d())) {
                // CraftBukkit end
                flag = false;
            }

            if (flag) {
                entityhuman.func_71001_a(this, 1);
                this.func_70106_y();
            }

        }
    }

    protected abstract ItemStack func_184550_j();

    protected boolean func_70041_e_() {
        return false;
    }

    public void func_70239_b(double d0) {
        this.field_70255_ao = d0;
    }

    public double func_70242_d() {
        return this.field_70255_ao;
    }

    public void func_70240_a(int i) {
        this.field_70256_ap = i;
    }

    public boolean func_70075_an() {
        return false;
    }

    public float func_70047_e() {
        return 0.0F;
    }

    public void func_70243_d(boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityArrow.field_184554_g)).byteValue();

        if (flag) {
            this.field_70180_af.func_187227_b(EntityArrow.field_184554_g, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.field_70180_af.func_187227_b(EntityArrow.field_184554_g, Byte.valueOf((byte) (b0 & -2)));
        }

    }

    public boolean func_70241_g() {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityArrow.field_184554_g)).byteValue();

        return (b0 & 1) != 0;
    }

    public void func_190547_a(EntityLivingBase entityliving, float f) {
        int i = EnchantmentHelper.func_185284_a(Enchantments.field_185309_u, entityliving);
        int j = EnchantmentHelper.func_185284_a(Enchantments.field_185310_v, entityliving);

        this.func_70239_b((double) (f * 2.0F) + this.field_70146_Z.nextGaussian() * 0.25D + (double) ((float) this.field_70170_p.func_175659_aa().func_151525_a() * 0.11F));
        if (i > 0) {
            this.func_70239_b(this.func_70242_d() + (double) i * 0.5D + 0.5D);
        }

        if (j > 0) {
            this.func_70240_a(j);
        }

        if (EnchantmentHelper.func_185284_a(Enchantments.field_185311_w, entityliving) > 0) {
            // CraftBukkit start - call EntityCombustEvent
            EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 100);
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.func_70015_d(event.getDuration());
            }
            // CraftBukkit end
        }

    }

    public static enum PickupStatus {

        DISALLOWED, ALLOWED, CREATIVE_ONLY;

        private PickupStatus() {}

        public static EntityArrow.PickupStatus func_188795_a(int i) {
            if (i < 0 || i > values().length) {
                i = 0;
            }

            return values()[i];
        }
    }
}
