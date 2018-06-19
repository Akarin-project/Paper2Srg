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

    private static final Logger field_151249_a = LogManager.getLogger();
    private final WorldServer field_72795_a;
    private final Set<EntityTrackerEntry> field_72793_b = Sets.newHashSet();
    public final IntHashMap<EntityTrackerEntry> field_72794_c = new IntHashMap();
    private int field_72792_d;

    public EntityTracker(WorldServer worldserver) {
        this.field_72795_a = worldserver;
        this.field_72792_d = PlayerChunkMap.func_72686_a(worldserver.spigotConfig.viewDistance); // Spigot
    }

    public static long func_187253_a(double d0) {
        return MathHelper.func_76124_d(d0 * 4096.0D);
    }

    public void func_72786_a(Entity entity) {
        if (entity instanceof EntityPlayerMP) {
            this.func_72791_a(entity, 512, 2);
            EntityPlayerMP entityplayer = (EntityPlayerMP) entity;
            Iterator iterator = this.field_72793_b.iterator();

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                if (entitytrackerentry.func_187260_b() != entityplayer) {
                    entitytrackerentry.func_73117_b(entityplayer);
                }
            }
        } else if (entity instanceof EntityFishHook) {
            this.func_72785_a(entity, 64, 5, true);
        } else if (entity instanceof EntityArrow) {
            this.func_72785_a(entity, 64, 20, false);
        } else if (entity instanceof EntitySmallFireball) {
            this.func_72785_a(entity, 64, 10, false);
        } else if (entity instanceof EntityFireball) {
            this.func_72785_a(entity, 64, 10, true);
        } else if (entity instanceof EntitySnowball) {
            this.func_72785_a(entity, 64, 10, true);
        } else if (entity instanceof EntityLlamaSpit) {
            this.func_72785_a(entity, 64, 10, false);
        } else if (entity instanceof EntityEnderPearl) {
            this.func_72785_a(entity, 64, 10, true);
        } else if (entity instanceof EntityEnderEye) {
            this.func_72785_a(entity, 64, 4, true);
        } else if (entity instanceof EntityEgg) {
            this.func_72785_a(entity, 64, 10, true);
        } else if (entity instanceof EntityPotion) {
            this.func_72785_a(entity, 64, 10, true);
        } else if (entity instanceof EntityExpBottle) {
            this.func_72785_a(entity, 64, 10, true);
        } else if (entity instanceof EntityFireworkRocket) {
            this.func_72785_a(entity, 64, 10, true);
        } else if (entity instanceof EntityItem) {
            this.func_72785_a(entity, 64, 20, true);
        } else if (entity instanceof EntityMinecart) {
            this.func_72785_a(entity, 80, 3, true);
        } else if (entity instanceof EntityBoat) {
            this.func_72785_a(entity, 80, 3, true);
        } else if (entity instanceof EntitySquid) {
            this.func_72785_a(entity, 64, 3, true);
        } else if (entity instanceof EntityWither) {
            this.func_72785_a(entity, 80, 3, false);
        } else if (entity instanceof EntityShulkerBullet) {
            this.func_72785_a(entity, 80, 3, true);
        } else if (entity instanceof EntityBat) {
            this.func_72785_a(entity, 80, 3, false);
        } else if (entity instanceof EntityDragon) {
            this.func_72785_a(entity, 160, 3, true);
        } else if (entity instanceof IAnimals) {
            this.func_72785_a(entity, 80, 3, true);
        } else if (entity instanceof EntityTNTPrimed) {
            this.func_72785_a(entity, 160, 10, true);
        } else if (entity instanceof EntityFallingBlock) {
            this.func_72785_a(entity, 160, 20, true);
        } else if (entity instanceof EntityHanging) {
            this.func_72785_a(entity, 160, Integer.MAX_VALUE, false);
        } else if (entity instanceof EntityArmorStand) {
            this.func_72785_a(entity, 160, 3, true);
        } else if (entity instanceof EntityXPOrb) {
            this.func_72785_a(entity, 160, 20, true);
        } else if (entity instanceof EntityAreaEffectCloud) {
            this.func_72785_a(entity, 160, 10, true); // CraftBukkit
        } else if (entity instanceof EntityEnderCrystal) {
            this.func_72785_a(entity, 256, Integer.MAX_VALUE, false);
        } else if (entity instanceof EntityEvokerFangs) {
            this.func_72785_a(entity, 160, 2, false);
        }

    }

    public void func_72791_a(Entity entity, int i, int j) {
        this.func_72785_a(entity, i, j, false);
    }

    public void func_72785_a(Entity entity, int i, final int j, boolean flag) {
        org.spigotmc.AsyncCatcher.catchOp( "entity track"); // Spigot
        i = org.spigotmc.TrackingRange.getEntityTrackingRange(entity, i); // Spigot
        try {
            if (this.field_72794_c.func_76037_b(entity.func_145782_y())) {
                throw new IllegalStateException("Entity is already tracked!");
            }

            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entity, i, this.field_72792_d, j, flag);

            this.field_72793_b.add(entitytrackerentry);
            this.field_72794_c.func_76038_a(entity.func_145782_y(), entitytrackerentry);
            entitytrackerentry.func_73125_b(this.field_72795_a.field_73010_i);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Adding entity to track");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Entity To Track");

            crashreportsystemdetails.func_71507_a("Tracking range", (Object) (i + " blocks"));
            final int finalI = i; // CraftBukkit - fix decompile error
            crashreportsystemdetails.func_189529_a("Update interval", new ICrashReportDetail() {
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
            entity.func_85029_a(crashreportsystemdetails);
            ((EntityTrackerEntry) this.field_72794_c.func_76041_a(entity.func_145782_y())).func_187260_b().func_85029_a(crashreport.func_85058_a("Entity That Is Already Tracked"));

            try {
                throw new ReportedException(crashreport);
            } catch (ReportedException reportedexception) {
                EntityTracker.field_151249_a.error("\"Silently\" catching entity tracking error.", reportedexception);
            }
        }

    }

    public void func_72790_b(Entity entity) {
        org.spigotmc.AsyncCatcher.catchOp( "entity untrack"); // Spigot
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) entity;
            Iterator iterator = this.field_72793_b.iterator();

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                entitytrackerentry.func_73118_a(entityplayer);
            }
        }

        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) this.field_72794_c.func_76049_d(entity.func_145782_y());

        if (entitytrackerentry1 != null) {
            this.field_72793_b.remove(entitytrackerentry1);
            entitytrackerentry1.func_73119_a();
        }

    }

    public void func_72788_a() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_72793_b.iterator();
        field_72795_a.timings.tracker1.startTiming(); // Spigot
        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.func_73122_a(this.field_72795_a.field_73010_i);
            if (entitytrackerentry.field_73133_n) {
                Entity entity = entitytrackerentry.func_187260_b();

                if (entity instanceof EntityPlayerMP) {
                    arraylist.add((EntityPlayerMP) entity);
                }
            }
        }
        field_72795_a.timings.tracker1.stopTiming(); // Spigot

        field_72795_a.timings.tracker2.startTiming(); // Spigot
        for (int i = 0; i < arraylist.size(); ++i) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) arraylist.get(i);
            Iterator iterator1 = this.field_72793_b.iterator();

            while (iterator1.hasNext()) {
                EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) iterator1.next();

                if (entitytrackerentry1.func_187260_b() != entityplayer) {
                    entitytrackerentry1.func_73117_b(entityplayer);
                }
            }
        }
        field_72795_a.timings.tracker2.stopTiming(); // Spigot

    }

    public void func_180245_a(EntityPlayerMP entityplayer) {
        Iterator iterator = this.field_72793_b.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            if (entitytrackerentry.func_187260_b() == entityplayer) {
                entitytrackerentry.func_73125_b(this.field_72795_a.field_73010_i);
            } else {
                entitytrackerentry.func_73117_b(entityplayer);
            }
        }

    }

    public void func_151247_a(Entity entity, Packet<?> packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.field_72794_c.func_76041_a(entity.func_145782_y());

        if (entitytrackerentry != null) {
            entitytrackerentry.func_151259_a(packet);
        }

    }

    public void func_151248_b(Entity entity, Packet<?> packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.field_72794_c.func_76041_a(entity.func_145782_y());

        if (entitytrackerentry != null) {
            entitytrackerentry.func_151261_b(packet);
        }

    }

    public void func_72787_a(EntityPlayerMP entityplayer) {
        Iterator iterator = this.field_72793_b.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.func_73123_c(entityplayer);
        }

    }

    public void func_85172_a(EntityPlayerMP entityplayer, Chunk chunk) {
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList();
        Iterator iterator = this.field_72793_b.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();
            Entity entity = entitytrackerentry.func_187260_b();

            if (entity != entityplayer && entity.field_70176_ah == chunk.field_76635_g && entity.field_70164_aj == chunk.field_76647_h) {
                entitytrackerentry.func_73117_b(entityplayer);
                if (entity instanceof EntityLiving && ((EntityLiving) entity).func_110166_bE() != null) {
                    arraylist.add(entity);
                }

                if (!entity.func_184188_bt().isEmpty()) {
                    arraylist1.add(entity);
                }
            }
        }

        Entity entity1;

        if (!arraylist.isEmpty()) {
            iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                entity1 = (Entity) iterator.next();
                entityplayer.field_71135_a.func_147359_a(new SPacketEntityAttach(entity1, ((EntityLiving) entity1).func_110166_bE()));
            }
        }

        if (!arraylist1.isEmpty()) {
            iterator = arraylist1.iterator();

            while (iterator.hasNext()) {
                entity1 = (Entity) iterator.next();
                entityplayer.field_71135_a.func_147359_a(new SPacketSetPassengers(entity1));
            }
        }

    }

    public void func_187252_a(int i) {
        this.field_72792_d = (i - 1) * 16;
        Iterator iterator = this.field_72793_b.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.func_187259_a(this.field_72792_d);
        }

    }
}
