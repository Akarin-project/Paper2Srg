package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
// CraftBukkit end

public class EntityEnderPearl extends EntityThrowable {

    private EntityLivingBase field_181555_c;

    public EntityEnderPearl(World world) {
        super(world);
    }

    public EntityEnderPearl(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
        this.field_181555_c = entityliving;
    }

    public static void func_189663_a(DataFixer dataconvertermanager) {
        EntityThrowable.func_189661_a(dataconvertermanager, "ThrownEnderpearl");
    }

    protected void func_70184_a(RayTraceResult movingobjectposition) {
        EntityLivingBase entityliving = this.func_85052_h();

        if (movingobjectposition.field_72308_g != null) {
            if (movingobjectposition.field_72308_g == this.field_181555_c) {
                return;
            }

            movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_76356_a(this, entityliving), 0.0F);
        }

        if (movingobjectposition.field_72313_a == RayTraceResult.Type.BLOCK) {
            BlockPos blockposition = movingobjectposition.func_178782_a();
            TileEntity tileentity = this.field_70170_p.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityEndGateway) {
                TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway) tileentity;

                if (entityliving != null) {
                    if (entityliving instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_192124_d.func_192193_a((EntityPlayerMP) entityliving, this.field_70170_p.func_180495_p(blockposition));
                    }

                    tileentityendgateway.func_184306_a((Entity) entityliving);
                    this.func_70106_y();
                    return;
                }

                tileentityendgateway.func_184306_a((Entity) this);
                return;
            }
        }

        for (int i = 0; i < 32; ++i) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.PORTAL, this.field_70165_t, this.field_70163_u + this.field_70146_Z.nextDouble() * 2.0D, this.field_70161_v, this.field_70146_Z.nextGaussian(), 0.0D, this.field_70146_Z.nextGaussian(), new int[0]);
        }

        if (!this.field_70170_p.field_72995_K) {
            if (entityliving instanceof EntityPlayerMP) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) entityliving;

                if (entityplayer.field_71135_a.func_147362_b().func_150724_d() && entityplayer.field_70170_p == this.field_70170_p && !entityplayer.func_70608_bn()) {
                    // CraftBukkit start - Fire PlayerTeleportEvent
                    org.bukkit.craftbukkit.entity.CraftPlayer player = entityplayer.getBukkitEntity();
                    org.bukkit.Location location = getBukkitEntity().getLocation();
                    location.setPitch(player.getLocation().getPitch());
                    location.setYaw(player.getLocation().getYaw());

                    PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                    Bukkit.getPluginManager().callEvent(teleEvent);

                    if (!teleEvent.isCancelled() && !entityplayer.field_71135_a.isDisconnected()) {
                        if (this.field_70146_Z.nextFloat() < 0.05F && this.field_70170_p.func_82736_K().func_82766_b("doMobSpawning")) {
                            EntityEndermite entityendermite = new EntityEndermite(this.field_70170_p);

                            entityendermite.func_175496_a(true);
                            entityendermite.func_70012_b(entityliving.field_70165_t, entityliving.field_70163_u, entityliving.field_70161_v, entityliving.field_70177_z, entityliving.field_70125_A);
                            this.field_70170_p.addEntity(entityendermite, CreatureSpawnEvent.SpawnReason.ENDER_PEARL);
                        }

                        if (entityliving.func_184218_aH()) {
                            entityliving.func_184210_p();
                        }

                        entityplayer.field_71135_a.teleport(teleEvent.getTo());
                        entityliving.field_70143_R = 0.0F;
                        CraftEventFactory.entityDamage = this;
                        entityliving.func_70097_a(DamageSource.field_76379_h, 5.0F);
                        CraftEventFactory.entityDamage = null;
                    }
                    // CraftBukkit end
                }
            } else if (entityliving != null) {
                entityliving.func_70634_a(this.field_70165_t, this.field_70163_u, this.field_70161_v);
                entityliving.field_70143_R = 0.0F;
            }

            this.func_70106_y();
        }

    }

    public void func_70071_h_() {
        EntityLivingBase entityliving = this.func_85052_h();

        if (entityliving != null && entityliving instanceof EntityPlayer && !entityliving.func_70089_S()) {
            this.func_70106_y();
        } else {
            super.func_70071_h_();
        }

    }

    @Nullable
    public Entity func_184204_a(int i) {
        if (this.field_70192_c.field_71093_bK != i) {
            this.field_70192_c = null;
        }

        return super.func_184204_a(i);
    }
}
