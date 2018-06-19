package net.minecraft.entity.item;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityTNTPrimed extends Entity {

    private static final DataParameter<Integer> field_184537_a = EntityDataManager.func_187226_a(EntityTNTPrimed.class, DataSerializers.field_187192_b);
    @Nullable
    private EntityLivingBase field_94084_b;
    private int field_70516_a;
    public float yield = 4; // CraftBukkit - add field
    public boolean isIncendiary = false; // CraftBukkit - add field

    public EntityTNTPrimed(World world) {
        super(world);
        this.field_70516_a = 80;
        this.field_70156_m = true;
        this.field_70178_ae = true;
        this.func_70105_a(0.98F, 0.98F);
    }

    public EntityTNTPrimed(World world, double d0, double d1, double d2, EntityLivingBase entityliving) {
        this(world);
        this.func_70107_b(d0, d1, d2);
        float f = (float) (Math.random() * 6.2831854820251465D);

        this.field_70159_w = (double) (-((float) Math.sin((double) f)) * 0.02F);
        this.field_70181_x = 0.20000000298023224D;
        this.field_70179_y = (double) (-((float) Math.cos((double) f)) * 0.02F);
        this.func_184534_a(80);
        this.field_70169_q = d0;
        this.field_70167_r = d1;
        this.field_70166_s = d2;
        this.field_94084_b = entityliving;
    }

    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(EntityTNTPrimed.field_184537_a, Integer.valueOf(80));
    }

    protected boolean func_70041_e_() {
        return false;
    }

    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    public void func_70071_h_() {
        if (field_70170_p.spigotConfig.currentPrimedTnt++ > field_70170_p.spigotConfig.maxTntTicksPerTick) { return; } // Spigot
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        if (!this.func_189652_ae()) {
            this.field_70181_x -= 0.03999999910593033D;
        }

        this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);

        // Paper start - Configurable TNT entity height nerf
        if (this.field_70170_p.paperConfig.entityTNTHeightNerf != 0 && this.field_70163_u > this.field_70170_p.paperConfig.entityTNTHeightNerf) {
            this.func_70106_y();
        }
        // Paper end

        this.field_70159_w *= 0.9800000190734863D;
        this.field_70181_x *= 0.9800000190734863D;
        this.field_70179_y *= 0.9800000190734863D;
        if (this.field_70122_E) {
            this.field_70159_w *= 0.699999988079071D;
            this.field_70179_y *= 0.699999988079071D;
            this.field_70181_x *= -0.5D;
        }

        --this.field_70516_a;
        if (this.field_70516_a <= 0) {
            // CraftBukkit start - Need to reverse the order of the explosion and the entity death so we have a location for the event
            // this.die();
            if (!this.field_70170_p.field_72995_K) {
                this.func_70515_d();
            }
            this.func_70106_y();
            // CraftBukkit end
        } else {
            this.func_70072_I();
            this.field_70170_p.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, this.field_70165_t, this.field_70163_u + 0.5D, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    private void func_70515_d() {
        // CraftBukkit start
        // float f = 4.0F;

        org.bukkit.craftbukkit.CraftServer server = this.field_70170_p.getServer();
        ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(server, this));
        server.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            this.field_70170_p.func_72885_a(this, this.field_70165_t, this.field_70163_u + (double) (this.field_70131_O / 16.0F), this.field_70161_v, event.getRadius(), event.getFire(), true);
        }
        // CraftBukkit end
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74777_a("Fuse", (short) this.func_184536_l());
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        this.func_184534_a(nbttagcompound.func_74765_d("Fuse"));
        // Paper start - Try and load origin location from the old NBT tags for backwards compatibility
        if (nbttagcompound.func_74764_b("SourceLoc_x")) {
            int srcX = nbttagcompound.func_74762_e("SourceLoc_x");
            int srcY = nbttagcompound.func_74762_e("SourceLoc_y");
            int srcZ = nbttagcompound.func_74762_e("SourceLoc_z");
            origin = new org.bukkit.Location(field_70170_p.getWorld(), srcX, srcY, srcZ);
        }
        // Paper end
    }

    @Nullable
    public EntityLivingBase func_94083_c() {
        return this.field_94084_b;
    }

    public float func_70047_e() {
        return 0.0F;
    }

    public void func_184534_a(int i) {
        this.field_70180_af.func_187227_b(EntityTNTPrimed.field_184537_a, Integer.valueOf(i));
        this.field_70516_a = i;
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityTNTPrimed.field_184537_a.equals(datawatcherobject)) {
            this.field_70516_a = this.func_184535_k();
        }

    }

    public int func_184535_k() {
        return ((Integer) this.field_70180_af.func_187225_a(EntityTNTPrimed.field_184537_a)).intValue();
    }

    public int func_184536_l() {
        return this.field_70516_a;
    }

    // Paper start - Optional prevent TNT from moving in water
    @Override
    public boolean pushedByWater() {
        return !field_70170_p.paperConfig.preventTntFromMovingInWater && super.pushedByWater();
    }

    /**
     * Author: Jedediah Smith <jedediah@silencegreys.com>
     */
    @Override
    public boolean doWaterMovement() {
        if (!field_70170_p.paperConfig.preventTntFromMovingInWater) return super.doWaterMovement();

        // Preserve velocity while calling the super method
        double oldMotX = this.field_70159_w;
        double oldMotY = this.field_70181_x;
        double oldMotZ = this.field_70179_y;

        super.doWaterMovement();

        this.field_70159_w = oldMotX;
        this.field_70181_x = oldMotY;
        this.field_70179_y = oldMotZ;

        if (this.field_70171_ac) {
            // Send position and velocity updates to nearby players on every tick while the TNT is in water.
            // This does pretty well at keeping their clients in sync with the server.
            EntityTrackerEntry ete = ((WorldServer) this.func_130014_f_()).func_73039_n().field_72794_c.func_76041_a(this.func_145782_y());
            if (ete != null) {
                SPacketEntityVelocity velocityPacket = new SPacketEntityVelocity(this);
                SPacketEntityTeleport positionPacket = new SPacketEntityTeleport(this);

                ete.field_73134_o.stream()
                        .filter(viewer -> (viewer.field_70165_t - this.field_70165_t) * (viewer.field_70163_u - this.field_70163_u) * (viewer.field_70161_v - this.field_70161_v) < 16 * 16)
                        .forEach(viewer -> {
                    viewer.field_71135_a.func_147359_a(velocityPacket);
                    viewer.field_71135_a.func_147359_a(positionPacket);
                });
            }
        }

        return this.field_70171_ac;
    }
    // Paper end
}
