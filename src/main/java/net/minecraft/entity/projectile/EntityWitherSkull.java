package net.minecraft.entity.projectile;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityWitherSkull extends EntityFireball {

    private static final DataParameter<Boolean> field_184565_e = EntityDataManager.func_187226_a(EntityWitherSkull.class, DataSerializers.field_187198_h);

    public EntityWitherSkull(World world) {
        super(world);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public EntityWitherSkull(World world, EntityLivingBase entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public static void func_189746_a(DataFixer dataconvertermanager) {
        EntityFireball.func_189743_a(dataconvertermanager, "WitherSkull");
    }

    protected float func_82341_c() {
        return this.func_82342_d() ? 0.73F : super.func_82341_c();
    }

    public boolean func_70027_ad() {
        return false;
    }

    public float func_180428_a(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata) {
        float f = super.func_180428_a(explosion, world, blockposition, iblockdata);
        Block block = iblockdata.func_177230_c();

        if (this.func_82342_d() && EntityWither.func_181033_a(block)) {
            f = Math.min(0.8F, f);
        }

        return f;
    }

    protected void func_70227_a(RayTraceResult movingobjectposition) {
        if (!this.field_70170_p.field_72995_K) {
            if (movingobjectposition.field_72308_g != null) {
                // Spigot start
                boolean didDamage = false;
                if (this.field_70235_a != null) {
                    didDamage = movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_76356_a(this, field_70235_a), 8.0F);
                    if (didDamage) { // CraftBukkit
                        if (movingobjectposition.field_72308_g.func_70089_S()) {
                            this.func_174815_a(this.field_70235_a, movingobjectposition.field_72308_g);
                        } else {
                            this.field_70235_a.heal(5.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.WITHER); // CraftBukkit
                        }
                    }
                } else {
                    didDamage = movingobjectposition.field_72308_g.func_70097_a(DamageSource.field_76376_m, 5.0F);
                }

                if (didDamage && movingobjectposition.field_72308_g instanceof EntityLivingBase) {
                // Spigot end
                    byte b0 = 0;

                    if (this.field_70170_p.func_175659_aa() == EnumDifficulty.NORMAL) {
                        b0 = 10;
                    } else if (this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD) {
                        b0 = 40;
                    }

                    if (b0 > 0) {
                        ((EntityLivingBase) movingobjectposition.field_72308_g).func_70690_d(new PotionEffect(MobEffects.field_82731_v, 20 * b0, 1));
                    }
                }
            }

            // CraftBukkit start
            // this.world.createExplosion(this, this.locX, this.locY, this.locZ, 1.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 1.0F, false);
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.field_70170_p.func_72885_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, event.getRadius(), event.getFire(), this.field_70170_p.func_82736_K().func_82766_b("mobGriefing"));
            }
            // CraftBukkit end
            this.func_70106_y();
        }

    }

    public boolean func_70067_L() {
        return false;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        return false;
    }

    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(EntityWitherSkull.field_184565_e, Boolean.valueOf(false));
    }

    public boolean func_82342_d() {
        return ((Boolean) this.field_70180_af.func_187225_a(EntityWitherSkull.field_184565_e)).booleanValue();
    }

    public void func_82343_e(boolean flag) {
        this.field_70180_af.func_187227_b(EntityWitherSkull.field_184565_e, Boolean.valueOf(flag));
    }

    protected boolean func_184564_k() {
        return false;
    }
}
