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

    public static void registerFixesEgg(DataFixer dataconvertermanager) {
        EntityThrowable.registerFixesThrowable(dataconvertermanager, "ThrownEgg");
    }

    protected void onImpact(RayTraceResult movingobjectposition) {
        if (movingobjectposition.entityHit != null) {
            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.world.isRemote) {
            boolean hatching = this.rand.nextInt(8) == 0; // CraftBukkit
            if (true) {
                byte b0 = 1;

                if (this.rand.nextInt(32) == 0) {
                    b0 = 4;
                }

                // CraftBukkit start
                if (!hatching) {
                    b0 = 0;
                }
                EntityType hatchingType = EntityType.CHICKEN;

                Entity shooter = this.getThrower();
                if (shooter instanceof EntityPlayerMP) {
                    PlayerEggThrowEvent event = new PlayerEggThrowEvent((Player) shooter.getBukkitEntity(), (org.bukkit.entity.Egg) this.getBukkitEntity(), hatching, b0, hatchingType);
                    this.world.getServer().getPluginManager().callEvent(event);

                    b0 = event.getNumHatches();
                    hatching = event.isHatching();
                    hatchingType = event.getHatchingType();
                }

                if (hatching) {
                    for (int i = 0; i < b0; ++i) {
                        Entity entity = world.getWorld().createEntity(new org.bukkit.Location(world.getWorld(), this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F), hatchingType.getEntityClass());
                        if (entity.getBukkitEntity() instanceof Ageable) {
                            ((Ageable) entity.getBukkitEntity()).setBaby();
                        }
                        world.getWorld().addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.EGG);
                    }
                }
                // CraftBukkit end
            }

            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }

    }
}
