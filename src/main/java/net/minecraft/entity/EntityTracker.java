package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class EntityTracker {

    private static final Logger LOGGER = LogManager.getLogger();
    private final WorldServer world;
    private final Set<EntityTrackerEntry> entries = Sets.newHashSet();
    public final IntHashMap<EntityTrackerEntry> trackedEntityHashTable = new IntHashMap();
    private int maxTrackingDistanceThreshold;

    public EntityTracker(WorldServer worldserver) {
        this.world = worldserver;
        this.maxTrackingDistanceThreshold = PlayerChunkMap.getFurthestViewableBlock(worldserver.spigotConfig.viewDistance); // Spigot
    }

    public static long getPositionLong(double d0) {
        return MathHelper.lfloor(d0 * 4096.0D);
    }

    public void track(Entity entity) {
        if (entity instanceof EntityPlayerMP) {
            this.track(entity, 512, 2);
            EntityPlayerMP entityplayer = (EntityPlayerMP) entity;
            Iterator iterator = this.entries.iterator();

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                if (entitytrackerentry.getTrackedEntity() != entityplayer) {
                    entitytrackerentry.updatePlayerEntity(entityplayer);
                }
            }
        } else if (entity instanceof EntityFishHook) {
            this.track(entity, 64, 5, true);
        } else if (entity instanceof EntityArrow) {
            this.track(entity, 64, 20, false);
        } else if (entity instanceof EntitySmallFireball) {
            this.track(entity, 64, 10, false);
        } else if (entity instanceof EntityFireball) {
            this.track(entity, 64, 10, true);
        } else if (entity instanceof EntitySnowball) {
            this.track(entity, 64, 10, true);
        } else if (entity instanceof EntityLlamaSpit) {
            this.track(entity, 64, 10, false);
        } else if (entity instanceof EntityEnderPearl) {
            this.track(entity, 64, 10, true);
        } else if (entity instanceof EntityEnderEye) {
            this.track(entity, 64, 4, true);
        } else if (entity instanceof EntityEgg) {
            this.track(entity, 64, 10, true);
        } else if (entity instanceof EntityPotion) {
            this.track(entity, 64, 10, true);
        } else if (entity instanceof EntityExpBottle) {
            this.track(entity, 64, 10, true);
        } else if (entity instanceof EntityFireworkRocket) {
            this.track(entity, 64, 10, true);
        } else if (entity instanceof EntityItem) {
            this.track(entity, 64, 20, true);
        } else if (entity instanceof EntityMinecart) {
            this.track(entity, 80, 3, true);
        } else if (entity instanceof EntityBoat) {
            this.track(entity, 80, 3, true);
        } else if (entity instanceof EntitySquid) {
            this.track(entity, 64, 3, true);
        } else if (entity instanceof EntityWither) {
            this.track(entity, 80, 3, false);
        } else if (entity instanceof EntityShulkerBullet) {
            this.track(entity, 80, 3, true);
        } else if (entity instanceof EntityBat) {
            this.track(entity, 80, 3, false);
        } else if (entity instanceof EntityDragon) {
            this.track(entity, 160, 3, true);
        } else if (entity instanceof IAnimals) {
            this.track(entity, 80, 3, true);
        } else if (entity instanceof EntityTNTPrimed) {
            this.track(entity, 160, 10, true);
        } else if (entity instanceof EntityFallingBlock) {
            this.track(entity, 160, 20, true);
        } else if (entity instanceof EntityHanging) {
            this.track(entity, 160, Integer.MAX_VALUE, false);
        } else if (entity instanceof EntityArmorStand) {
            this.track(entity, 160, 3, true);
        } else if (entity instanceof EntityXPOrb) {
            this.track(entity, 160, 20, true);
        } else if (entity instanceof EntityAreaEffectCloud) {
            this.track(entity, 160, 10, true); // CraftBukkit
        } else if (entity instanceof EntityEnderCrystal) {
            this.track(entity, 256, Integer.MAX_VALUE, false);
        } else if (entity instanceof EntityEvokerFangs) {
            this.track(entity, 160, 2, false);
        }

    }

    public void track(Entity entity, int i, int j) {
        this.track(entity, i, j, false);
    }

    public void track(Entity entity, int i, final int j, boolean flag) {
        org.spigotmc.AsyncCatcher.catchOp( "entity track"); // Spigot
        i = org.spigotmc.TrackingRange.getEntityTrackingRange(entity, i); // Spigot
        try {
            if (this.trackedEntityHashTable.containsItem(entity.getEntityId())) {
                throw new IllegalStateException("Entity is already tracked!");
            }

            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entity, i, this.maxTrackingDistanceThreshold, j, flag);

            this.entries.add(entitytrackerentry);
            this.trackedEntityHashTable.addKey(entity.getEntityId(), entitytrackerentry);
            entitytrackerentry.updatePlayerEntities(this.world.playerEntities);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Entity To Track");

            crashreportsystemdetails.addCrashSection("Tracking range", (Object) (i + " blocks"));
            final int finalI = i; // CraftBukkit - fix decompile error
            crashreportsystemdetails.addDetail("Update interval", new ICrashReportDetail() {
                public String a() throws Exception {
                    String s = "Once per " + finalI + " ticks"; // CraftBukkit

                    if (finalI == Integer.MAX_VALUE) { // CraftBukkit
                        s = "Maximum (" + s + ")";
                    }

                    return s;
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
            entity.addEntityCrashInfo(crashreportsystemdetails);
            ((EntityTrackerEntry) this.trackedEntityHashTable.lookup(entity.getEntityId())).getTrackedEntity().addEntityCrashInfo(crashreport.makeCategory("Entity That Is Already Tracked"));

            try {
                throw new ReportedException(crashreport);
            } catch (ReportedException reportedexception) {
                EntityTracker.LOGGER.error("\"Silently\" catching entity tracking error.", reportedexception);
            }
        }

    }

    public void untrack(Entity entity) {
        org.spigotmc.AsyncCatcher.catchOp( "entity untrack"); // Spigot
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) entity;
            Iterator iterator = this.entries.iterator();

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                entitytrackerentry.removeFromTrackedPlayers(entityplayer);
            }
        }

        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) this.trackedEntityHashTable.removeObject(entity.getEntityId());

        if (entitytrackerentry1 != null) {
            this.entries.remove(entitytrackerentry1);
            entitytrackerentry1.sendDestroyEntityPacketToTrackedPlayers();
        }

    }

    public void tick() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.entries.iterator();
        world.timings.tracker1.startTiming(); // Spigot
        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.updatePlayerList(this.world.playerEntities);
            if (entitytrackerentry.playerEntitiesUpdated) {
                Entity entity = entitytrackerentry.getTrackedEntity();

                if (entity instanceof EntityPlayerMP) {
                    arraylist.add((EntityPlayerMP) entity);
                }
            }
        }
        world.timings.tracker1.stopTiming(); // Spigot

        world.timings.tracker2.startTiming(); // Spigot
        for (int i = 0; i < arraylist.size(); ++i) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) arraylist.get(i);
            Iterator iterator1 = this.entries.iterator();

            while (iterator1.hasNext()) {
                EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) iterator1.next();

                if (entitytrackerentry1.getTrackedEntity() != entityplayer) {
                    entitytrackerentry1.updatePlayerEntity(entityplayer);
                }
            }
        }
        world.timings.tracker2.stopTiming(); // Spigot

    }

    public void updateVisibility(EntityPlayerMP entityplayer) {
        Iterator iterator = this.entries.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            if (entitytrackerentry.getTrackedEntity() == entityplayer) {
                entitytrackerentry.updatePlayerEntities(this.world.playerEntities);
            } else {
                entitytrackerentry.updatePlayerEntity(entityplayer);
            }
        }

    }

    public void sendToTracking(Entity entity, Packet<?> packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.trackedEntityHashTable.lookup(entity.getEntityId());

        if (entitytrackerentry != null) {
            entitytrackerentry.sendPacketToTrackedPlayers(packet);
        }

    }

    public void sendToTrackingAndSelf(Entity entity, Packet<?> packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.trackedEntityHashTable.lookup(entity.getEntityId());

        if (entitytrackerentry != null) {
            entitytrackerentry.sendToTrackingAndSelf(packet);
        }

    }

    public void removePlayerFromTrackers(EntityPlayerMP entityplayer) {
        Iterator iterator = this.entries.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.removeTrackedPlayerSymmetric(entityplayer);
        }

    }

    public void sendLeashedEntitiesInChunk(EntityPlayerMP entityplayer, Chunk chunk) {
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList();
        Iterator iterator = this.entries.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();
            Entity entity = entitytrackerentry.getTrackedEntity();

            if (entity != entityplayer && entity.chunkCoordX == chunk.x && entity.chunkCoordZ == chunk.z) {
                entitytrackerentry.updatePlayerEntity(entityplayer);
                if (entity instanceof EntityLiving && ((EntityLiving) entity).getLeashHolder() != null) {
                    arraylist.add(entity);
                }

                if (!entity.getPassengers().isEmpty()) {
                    arraylist1.add(entity);
                }
            }
        }

        Entity entity1;

        if (!arraylist.isEmpty()) {
            iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                entity1 = (Entity) iterator.next();
                entityplayer.connection.sendPacket(new SPacketEntityAttach(entity1, ((EntityLiving) entity1).getLeashHolder()));
            }
        }

        if (!arraylist1.isEmpty()) {
            iterator = arraylist1.iterator();

            while (iterator.hasNext()) {
                entity1 = (Entity) iterator.next();
                entityplayer.connection.sendPacket(new SPacketSetPassengers(entity1));
            }
        }

    }

    public void setViewDistance(int i) {
        this.maxTrackingDistanceThreshold = (i - 1) * 16;
        Iterator iterator = this.entries.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.setMaxRange(this.maxTrackingDistanceThreshold);
        }

    }
}
