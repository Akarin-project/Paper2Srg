package net.minecraft.entity.projectile;


import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityCombustByEntityEvent;

public class EntitySmallFireball extends EntityFireball {

    public EntitySmallFireball(World world) {
        super(world);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World world, EntityLivingBase entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.func_70105_a(0.3125F, 0.3125F);
        // CraftBukkit start
        if (this.field_70235_a != null && this.field_70235_a instanceof EntityLiving) {
            isIncendiary = this.field_70170_p.func_82736_K().func_82766_b("mobGriefing");
        }
        // CraftBukkit end
    }

    public EntitySmallFireball(World world, double d0, double d1, double d2, double d3, double d4, double d5) {
        super(world, d0, d1, d2, d3, d4, d5);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public static void func_189745_a(DataFixer dataconvertermanager) {
        EntityFireball.func_189743_a(dataconvertermanager, "SmallFireball");
    }

    protected void func_70227_a(RayTraceResult movingobjectposition) {
        if (!this.field_70170_p.field_72995_K) {
            boolean flag;

            if (movingobjectposition.field_72308_g != null) {
                if (!movingobjectposition.field_72308_g.func_70045_F()) {
                    // CraftBukkit start - Entity damage by entity event + combust event
                    isIncendiary = movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_76362_a(this, this.field_70235_a), 5.0F);
                    if (isIncendiary) {
                        this.func_174815_a(this.field_70235_a, movingobjectposition.field_72308_g);
                        EntityCombustByEntityEvent event = new EntityCombustByEntityEvent((org.bukkit.entity.Projectile) this.getBukkitEntity(), movingobjectposition.field_72308_g.getBukkitEntity(), 5);
                        movingobjectposition.field_72308_g.field_70170_p.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            movingobjectposition.field_72308_g.func_70015_d(event.getDuration());
                        }
                        // CraftBukkit end
                    }
                }
            } else {
                flag = true;
                if (this.field_70235_a != null && this.field_70235_a instanceof EntityLiving) {
                    flag = this.field_70170_p.func_82736_K().func_82766_b("mobGriefing");
                }

                // CraftBukkit start
                if (isIncendiary) {
                    BlockPos blockposition = movingobjectposition.func_178782_a().func_177972_a(movingobjectposition.field_178784_b);

                    if (this.field_70170_p.func_175623_d(blockposition)) {
                        if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(field_70170_p, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this).isCancelled()) {
                            this.field_70170_p.func_175656_a(blockposition, Blocks.field_150480_ab.func_176223_P());
                        }
                        // CraftBukkit end
                    }
                }
            }

            this.func_70106_y();
        }

    }

    public boolean func_70067_L() {
        return false;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        return false;
    }
}
