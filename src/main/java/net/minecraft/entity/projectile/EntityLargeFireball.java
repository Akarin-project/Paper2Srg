package net.minecraft.entity.projectile;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityLargeFireball extends EntityFireball {

    public int field_92057_e = 1;

    public EntityLargeFireball(World world) {
        super(world);
        isIncendiary = this.field_70170_p.func_82736_K().func_82766_b("mobGriefing"); // CraftBukkit
    }

    public EntityLargeFireball(World world, EntityLivingBase entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        isIncendiary = this.field_70170_p.func_82736_K().func_82766_b("mobGriefing"); // CraftBukkit
    }

    protected void func_70227_a(RayTraceResult movingobjectposition) {
        if (!this.field_70170_p.field_72995_K) {
            if (movingobjectposition.field_72308_g != null) {
                movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_76362_a(this, this.field_70235_a), 6.0F);
                this.func_174815_a(this.field_70235_a, movingobjectposition.field_72308_g);
            }

            boolean flag = this.field_70170_p.func_82736_K().func_82766_b("mobGriefing");

            // CraftBukkit start - fire ExplosionPrimeEvent
            ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(this.field_70170_p.getServer(), this));
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                // give 'this' instead of (Entity) null so we know what causes the damage
                this.field_70170_p.func_72885_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, event.getRadius(), event.getFire(), isIncendiary);
            }
            // CraftBukkit end
            this.func_70106_y();
        }

    }

    public static void func_189744_a(DataFixer dataconvertermanager) {
        EntityFireball.func_189743_a(dataconvertermanager, "Fireball");
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("ExplosionPower", this.field_92057_e);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        if (nbttagcompound.func_150297_b("ExplosionPower", 99)) {
            // CraftBukkit - set bukkitYield when setting explosionpower
            bukkitYield = this.field_92057_e = nbttagcompound.func_74762_e("ExplosionPower");
        }

    }
}
