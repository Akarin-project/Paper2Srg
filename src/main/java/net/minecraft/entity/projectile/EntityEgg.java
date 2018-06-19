package net.minecraft.entity.projectile;

// CraftBukkit start
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEggThrowEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

// CraftBukkit end

public class EntityEgg extends EntityThrowable {

    public EntityEgg(World world) {
        super(world);
    }

    public EntityEgg(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
    }

    public EntityEgg(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void func_189664_a(DataFixer dataconvertermanager) {
        EntityThrowable.func_189661_a(dataconvertermanager, "ThrownEgg");
    }

    protected void func_70184_a(RayTraceResult movingobjectposition) {
        if (movingobjectposition.field_72308_g != null) {
            movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_76356_a(this, this.func_85052_h()), 0.0F);
        }

        if (!this.field_70170_p.field_72995_K) {
            boolean hatching = this.field_70146_Z.nextInt(8) == 0; // CraftBukkit
            if (true) {
                byte b0 = 1;

                if (this.field_70146_Z.nextInt(32) == 0) {
                    b0 = 4;
                }

                // CraftBukkit start
                if (!hatching) {
                    b0 = 0;
                }
                EntityType hatchingType = EntityType.CHICKEN;

                Entity shooter = this.func_85052_h();
                if (shooter instanceof EntityPlayerMP) {
                    PlayerEggThrowEvent event = new PlayerEggThrowEvent((Player) shooter.getBukkitEntity(), (org.bukkit.entity.Egg) this.getBukkitEntity(), hatching, b0, hatchingType);
                    this.field_70170_p.getServer().getPluginManager().callEvent(event);

                    b0 = event.getNumHatches();
                    hatching = event.isHatching();
                    hatchingType = event.getHatchingType();
                }

                if (hatching) {
                    for (int i = 0; i < b0; ++i) {
                        Entity entity = field_70170_p.getWorld().createEntity(new org.bukkit.Location(field_70170_p.getWorld(), this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, 0.0F), hatchingType.getEntityClass());
                        if (entity.getBukkitEntity() instanceof Ageable) {
                            ((Ageable) entity.getBukkitEntity()).setBaby();
                        }
                        field_70170_p.getWorld().addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.EGG);
                    }
                }
                // CraftBukkit end
            }

            this.field_70170_p.func_72960_a(this, (byte) 3);
            this.func_70106_y();
        }

    }
}
