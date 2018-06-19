package net.minecraft.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;
// CraftBukkit end

public class EntityTrackerEntry {

    private static final Logger field_151262_p = LogManager.getLogger();
    private final Entity field_73132_a;
    private final int field_73130_b;
    private int field_187262_f;
    private final int field_73131_c;
    private long field_73128_d;
    private long field_73129_e;
    private long field_73126_f;
    private int field_73127_g;
    private int field_73139_h;
    private int field_73140_i;
    private double field_73137_j;
    private double field_73138_k;
    private double field_73135_l;
    public int field_73136_m;
    private double field_73147_p;
    private double field_73146_q;
    private double field_73145_r;
    private boolean field_73144_s;
    private final boolean field_73143_t;
    private int field_73142_u;
    private List<Entity> field_187263_w = Collections.emptyList();
    private boolean field_73141_v;
    private boolean field_180234_y;
    public boolean field_73133_n;
    // Paper start
    // Replace trackedPlayers Set with a Map. The value is true until the player receives
    // their first update (which is forced to have absolute coordinates), false afterward.
    public java.util.Map<EntityPlayerMP, Boolean> trackedPlayerMap = new java.util.HashMap<EntityPlayerMP, Boolean>();
    public Set<EntityPlayerMP> field_73134_o = trackedPlayerMap.keySet();
    // Paper end

    public EntityTrackerEntry(Entity entity, int i, int j, int k, boolean flag) {
        entity.tracker = this; // Paper
        this.field_73132_a = entity;
        this.field_73130_b = i;
        this.field_187262_f = j;
        this.field_73131_c = k;
        this.field_73143_t = flag;
        this.field_73128_d = EntityTracker.func_187253_a(entity.field_70165_t);
        this.field_73129_e = EntityTracker.func_187253_a(entity.field_70163_u);
        this.field_73126_f = EntityTracker.func_187253_a(entity.field_70161_v);
        this.field_73127_g = MathHelper.func_76141_d(entity.field_70177_z * 256.0F / 360.0F);
        this.field_73139_h = MathHelper.func_76141_d(entity.field_70125_A * 256.0F / 360.0F);
        this.field_73140_i = MathHelper.func_76141_d(entity.func_70079_am() * 256.0F / 360.0F);
        this.field_180234_y = entity.field_70122_E;
    }

    public boolean equals(Object object) {
        return object instanceof EntityTrackerEntry ? ((EntityTrackerEntry) object).field_73132_a.func_145782_y() == this.field_73132_a.func_145782_y() : false;
    }

    public int hashCode() {
        return this.field_73132_a.func_145782_y();
    }

    public void func_73122_a(List<EntityPlayer> list) {
        this.field_73133_n = false;
        if (!this.field_73144_s || this.field_73132_a.func_70092_e(this.field_73147_p, this.field_73146_q, this.field_73145_r) > 16.0D) {
            this.field_73147_p = this.field_73132_a.field_70165_t;
            this.field_73146_q = this.field_73132_a.field_70163_u;
            this.field_73145_r = this.field_73132_a.field_70161_v;
            this.field_73144_s = true;
            this.field_73133_n = true;
            this.func_73125_b(list);
        }

        List list1 = this.field_73132_a.func_184188_bt();

        if (!list1.equals(this.field_187263_w)) {
            this.field_187263_w = list1;
            this.func_151261_b(new SPacketSetPassengers(this.field_73132_a)); // CraftBukkit
        }

        // PAIL : rename
        if (this.field_73132_a instanceof EntityItemFrame && this.field_73136_m % 20 == 0) { // Paper
            EntityItemFrame entityitemframe = (EntityItemFrame) this.field_73132_a;
            ItemStack itemstack = entityitemframe.func_82335_i();

            if (itemstack != null && itemstack.func_77973_b() instanceof ItemMap) { // Paper - moved back up
                MapData worldmap = Items.field_151098_aY.func_77873_a(itemstack, this.field_73132_a.field_70170_p);
                Iterator iterator = this.field_73134_o.iterator(); // CraftBukkit

                while (iterator.hasNext()) {
                    EntityPlayer entityhuman = (EntityPlayer) iterator.next();
                    EntityPlayerMP entityplayer = (EntityPlayerMP) entityhuman;

                    worldmap.func_76191_a(entityplayer, itemstack);
                    Packet packet = Items.field_151098_aY.func_150911_c(itemstack, this.field_73132_a.field_70170_p, (EntityPlayer) entityplayer);

                    if (packet != null) {
                        entityplayer.field_71135_a.func_147359_a(packet);
                    }
                }
            }

            this.func_111190_b();
        }

        if (this.field_73136_m % this.field_73131_c == 0 || this.field_73132_a.field_70160_al || this.field_73132_a.func_184212_Q().func_187223_a()) {
            int i;

            if (this.field_73132_a.func_184218_aH()) {
                i = MathHelper.func_76141_d(this.field_73132_a.field_70177_z * 256.0F / 360.0F);
                int j = MathHelper.func_76141_d(this.field_73132_a.field_70125_A * 256.0F / 360.0F);
                boolean flag = Math.abs(i - this.field_73127_g) >= 1 || Math.abs(j - this.field_73139_h) >= 1;

                if (flag) {
                    this.func_151259_a(new SPacketEntity.S16PacketEntityLook(this.field_73132_a.func_145782_y(), (byte) i, (byte) j, this.field_73132_a.field_70122_E));
                    this.field_73127_g = i;
                    this.field_73139_h = j;
                }

                this.field_73128_d = EntityTracker.func_187253_a(this.field_73132_a.field_70165_t);
                this.field_73129_e = EntityTracker.func_187253_a(this.field_73132_a.field_70163_u);
                this.field_73126_f = EntityTracker.func_187253_a(this.field_73132_a.field_70161_v);
                this.func_111190_b();
                this.field_73141_v = true;
            } else {
                ++this.field_73142_u;
                long k = EntityTracker.func_187253_a(this.field_73132_a.field_70165_t);
                long l = EntityTracker.func_187253_a(this.field_73132_a.field_70163_u);
                long i1 = EntityTracker.func_187253_a(this.field_73132_a.field_70161_v);
                int j1 = MathHelper.func_76141_d(this.field_73132_a.field_70177_z * 256.0F / 360.0F);
                int k1 = MathHelper.func_76141_d(this.field_73132_a.field_70125_A * 256.0F / 360.0F);
                long l1 = k - this.field_73128_d;
                long i2 = l - this.field_73129_e;
                long j2 = i1 - this.field_73126_f;
                Object object = null;
                boolean flag1 = l1 * l1 + i2 * i2 + j2 * j2 >= 128L || this.field_73136_m % 60 == 0;
                boolean flag2 = Math.abs(j1 - this.field_73127_g) >= 1 || Math.abs(k1 - this.field_73139_h) >= 1;

                if (this.field_73136_m > 0 || this.field_73132_a instanceof EntityArrow) { // Paper - Moved up
                // CraftBukkit start - Code moved from below
                if (flag1) {
                    this.field_73128_d = k;
                    this.field_73129_e = l;
                    this.field_73126_f = i1;
                }

                if (flag2) {
                    this.field_73127_g = j1;
                    this.field_73139_h = k1;
                }
                // CraftBukkit end

                    if (l1 >= -32768L && l1 < 32768L && i2 >= -32768L && i2 < 32768L && j2 >= -32768L && j2 < 32768L && this.field_73142_u <= 400 && !this.field_73141_v && this.field_180234_y == this.field_73132_a.field_70122_E) {
                        if ((!flag1 || !flag2) && !(this.field_73132_a instanceof EntityArrow)) {
                            if (flag1) {
                                object = new SPacketEntity.S15PacketEntityRelMove(this.field_73132_a.func_145782_y(), l1, i2, j2, this.field_73132_a.field_70122_E);
                            } else if (flag2) {
                                object = new SPacketEntity.S16PacketEntityLook(this.field_73132_a.func_145782_y(), (byte) j1, (byte) k1, this.field_73132_a.field_70122_E);
                            }
                        } else {
                            object = new SPacketEntity.S17PacketEntityLookMove(this.field_73132_a.func_145782_y(), l1, i2, j2, (byte) j1, (byte) k1, this.field_73132_a.field_70122_E);
                        }
                    } else {
                        this.field_180234_y = this.field_73132_a.field_70122_E;
                        this.field_73142_u = 0;
                        // CraftBukkit start - Refresh list of who can see a player before sending teleport packet
                        if (this.field_73132_a instanceof EntityPlayerMP) {
                            this.func_73125_b(new java.util.ArrayList(this.field_73134_o));
                        }
                        // CraftBukkit end
                        this.func_187261_c();
                        object = new SPacketEntityTeleport(this.field_73132_a);
                    }
                }

                boolean flag3 = this.field_73143_t;

                if (this.field_73132_a instanceof EntityLivingBase && ((EntityLivingBase) this.field_73132_a).func_184613_cA()) {
                    flag3 = true;
                }

                if (flag3 && this.field_73136_m > 0) {
                    double d0 = this.field_73132_a.field_70159_w - this.field_73137_j;
                    double d1 = this.field_73132_a.field_70181_x - this.field_73138_k;
                    double d2 = this.field_73132_a.field_70179_y - this.field_73135_l;
                    double d3 = 0.02D;
                    double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d4 > 4.0E-4D || d4 > 0.0D && this.field_73132_a.field_70159_w == 0.0D && this.field_73132_a.field_70181_x == 0.0D && this.field_73132_a.field_70179_y == 0.0D) {
                        this.field_73137_j = this.field_73132_a.field_70159_w;
                        this.field_73138_k = this.field_73132_a.field_70181_x;
                        this.field_73135_l = this.field_73132_a.field_70179_y;
                        this.func_151259_a(new SPacketEntityVelocity(this.field_73132_a.func_145782_y(), this.field_73137_j, this.field_73138_k, this.field_73135_l));
                    }
                }

                if (object != null) {
                    // Paper start - ensure fresh viewers get an absolute position on their first update,
                    // since we can't be certain what position they received in the spawn packet.
                    if (object instanceof SPacketEntityTeleport) {
                        this.func_151259_a((Packet) object);
                    } else {
                        SPacketEntityTeleport teleportPacket = null;

                        for (java.util.Map.Entry<EntityPlayerMP, Boolean> viewer : trackedPlayerMap.entrySet()) {
                            if (viewer.getValue()) {
                                viewer.setValue(false);
                                if (teleportPacket == null) {
                                    teleportPacket = new SPacketEntityTeleport(this.field_73132_a);
                                }
                                viewer.getKey().field_71135_a.func_147359_a(teleportPacket);
                            } else {
                                viewer.getKey().field_71135_a.func_147359_a((Packet) object);
                            }
                        }
                    }
                    // Paper end
                }

                this.func_111190_b();
                /* CraftBukkit start - Code moved up
                if (flag1) {
                    this.xLoc = k;
                    this.yLoc = l;
                    this.zLoc = i1;
                }

                if (flag2) {
                    this.yRot = j1;
                    this.xRot = k1;
                }
                // CraftBukkit end */

                this.field_73141_v = false;
            }

            i = MathHelper.func_76141_d(this.field_73132_a.func_70079_am() * 256.0F / 360.0F);
            if (Math.abs(i - this.field_73140_i) >= 1) {
                this.func_151259_a(new SPacketEntityHeadLook(this.field_73132_a, (byte) i));
                this.field_73140_i = i;
            }

            this.field_73132_a.field_70160_al = false;
        }

        ++this.field_73136_m;
        if (this.field_73132_a.field_70133_I) {
            // CraftBukkit start - Create PlayerVelocity event
            boolean cancelled = false;

            if (this.field_73132_a instanceof EntityPlayerMP) {
                Player player = (Player) this.field_73132_a.getBukkitEntity();
                org.bukkit.util.Vector velocity = player.getVelocity();

                PlayerVelocityEvent event = new PlayerVelocityEvent(player, velocity.clone());
                this.field_73132_a.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    cancelled = true;
                } else if (!velocity.equals(event.getVelocity())) {
                    player.setVelocity(event.getVelocity());
                }
            }

            if (!cancelled) {
                this.func_151261_b(new SPacketEntityVelocity(this.field_73132_a));
            }
            // CraftBukkit end
            this.field_73132_a.field_70133_I = false;
        }

    }

    private void func_111190_b() {
        EntityDataManager datawatcher = this.field_73132_a.func_184212_Q();

        if (datawatcher.func_187223_a()) {
            this.func_151261_b(new SPacketEntityMetadata(this.field_73132_a.func_145782_y(), datawatcher, false));
        }

        if (this.field_73132_a instanceof EntityLivingBase) {
            AttributeMap attributemapserver = (AttributeMap) ((EntityLivingBase) this.field_73132_a).func_110140_aT();
            Set set = attributemapserver.func_111161_b();

            if (!set.isEmpty()) {
                // CraftBukkit start - Send scaled max health
                if (this.field_73132_a instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) this.field_73132_a).getBukkitEntity().injectScaledMaxHealth(set, false);
                }
                // CraftBukkit end
                this.func_151261_b(new SPacketEntityProperties(this.field_73132_a.func_145782_y(), set));
            }

            set.clear();
        }

    }

    public void func_151259_a(Packet<?> packet) {
        Iterator iterator = this.field_73134_o.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            entityplayer.field_71135_a.func_147359_a(packet);
        }

    }

    public void func_151261_b(Packet<?> packet) {
        this.func_151259_a(packet);
        if (this.field_73132_a instanceof EntityPlayerMP) {
            ((EntityPlayerMP) this.field_73132_a).field_71135_a.func_147359_a(packet);
        }

    }

    public void func_73119_a() {
        Iterator iterator = this.field_73134_o.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            this.field_73132_a.func_184203_c(entityplayer);
            entityplayer.func_152339_d(this.field_73132_a);
        }

    }

    public void func_73118_a(EntityPlayerMP entityplayer) {
        if (this.field_73134_o.contains(entityplayer)) {
            this.field_73132_a.func_184203_c(entityplayer);
            entityplayer.func_152339_d(this.field_73132_a);
            this.field_73134_o.remove(entityplayer);
        }

    }

    public void func_73117_b(EntityPlayerMP entityplayer) {
        org.spigotmc.AsyncCatcher.catchOp( "player tracker update"); // Spigot
        if (entityplayer != this.field_73132_a) {
            if (this.func_180233_c(entityplayer)) {
                if (!this.field_73134_o.contains(entityplayer) && (this.func_73121_d(entityplayer) || this.field_73132_a.field_98038_p)) {
                    // CraftBukkit start - respect vanish API
                    if (this.field_73132_a instanceof EntityPlayerMP) {
                        Player player = ((EntityPlayerMP) this.field_73132_a).getBukkitEntity();
                        if (!entityplayer.getBukkitEntity().canSee(player)) {
                            return;
                        }
                    }

                    entityplayer.field_71130_g.remove(Integer.valueOf(this.field_73132_a.func_145782_y()));
                    // CraftBukkit end
                    this.trackedPlayerMap.put(entityplayer, true); // Paper
                    Packet packet = this.func_151260_c();

                    entityplayer.field_71135_a.func_147359_a(packet);
                    if (!this.field_73132_a.func_184212_Q().func_187228_d()) {
                        entityplayer.field_71135_a.func_147359_a(new SPacketEntityMetadata(this.field_73132_a.func_145782_y(), this.field_73132_a.func_184212_Q(), true));
                    }

                    boolean flag = this.field_73143_t;

                    if (this.field_73132_a instanceof EntityLivingBase) {
                        AttributeMap attributemapserver = (AttributeMap) ((EntityLivingBase) this.field_73132_a).func_110140_aT();
                        Collection collection = attributemapserver.func_111160_c();

                        // CraftBukkit start - If sending own attributes send scaled health instead of current maximum health
                        if (this.field_73132_a.func_145782_y() == entityplayer.func_145782_y()) {
                            ((EntityPlayerMP) this.field_73132_a).getBukkitEntity().injectScaledMaxHealth(collection, false);
                        }
                        // CraftBukkit end

                        if (!collection.isEmpty()) {
                            entityplayer.field_71135_a.func_147359_a(new SPacketEntityProperties(this.field_73132_a.func_145782_y(), collection));
                        }

                        if (((EntityLivingBase) this.field_73132_a).func_184613_cA()) {
                            flag = true;
                        }
                    }

                    this.field_73137_j = this.field_73132_a.field_70159_w;
                    this.field_73138_k = this.field_73132_a.field_70181_x;
                    this.field_73135_l = this.field_73132_a.field_70179_y;
                    if (flag && !(packet instanceof SPacketSpawnMob)) {
                        entityplayer.field_71135_a.func_147359_a(new SPacketEntityVelocity(this.field_73132_a.func_145782_y(), this.field_73132_a.field_70159_w, this.field_73132_a.field_70181_x, this.field_73132_a.field_70179_y));
                    }

                    if (this.field_73132_a instanceof EntityLivingBase) {
                        EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
                        int i = aenumitemslot.length;

                        for (int j = 0; j < i; ++j) {
                            EntityEquipmentSlot enumitemslot = aenumitemslot[j];
                            ItemStack itemstack = ((EntityLivingBase) this.field_73132_a).func_184582_a(enumitemslot);

                            if (!itemstack.func_190926_b()) {
                                entityplayer.field_71135_a.func_147359_a(new SPacketEntityEquipment(this.field_73132_a.func_145782_y(), enumitemslot, itemstack));
                            }
                        }
                    }

                    if (this.field_73132_a instanceof EntityPlayer) {
                        EntityPlayer entityhuman = (EntityPlayer) this.field_73132_a;

                        if (entityhuman.func_70608_bn()) {
                            entityplayer.field_71135_a.func_147359_a(new SPacketUseBed(entityhuman, new BlockPos(this.field_73132_a)));
                        }
                    }

                    // CraftBukkit start - Fix for nonsensical head yaw
                    this.field_73140_i = MathHelper.func_76141_d(this.field_73132_a.func_70079_am() * 256.0F / 360.0F);
                    this.func_151259_a(new SPacketEntityHeadLook(this.field_73132_a, (byte) field_73140_i));
                    // CraftBukkit end

                    if (this.field_73132_a instanceof EntityLivingBase) {
                        EntityLivingBase entityliving = (EntityLivingBase) this.field_73132_a;
                        Iterator iterator = entityliving.func_70651_bq().iterator();

                        while (iterator.hasNext()) {
                            PotionEffect mobeffect = (PotionEffect) iterator.next();

                            entityplayer.field_71135_a.func_147359_a(new SPacketEntityEffect(this.field_73132_a.func_145782_y(), mobeffect));
                        }
                    }

                    if (!this.field_73132_a.func_184188_bt().isEmpty()) {
                        entityplayer.field_71135_a.func_147359_a(new SPacketSetPassengers(this.field_73132_a));
                    }

                    if (this.field_73132_a.func_184218_aH()) {
                        entityplayer.field_71135_a.func_147359_a(new SPacketSetPassengers(this.field_73132_a.func_184187_bx()));
                    }

                    this.field_73132_a.func_184178_b(entityplayer);
                    entityplayer.func_184848_d(this.field_73132_a);
                    updatePassengers(entityplayer); // Paper
                }
            } else if (this.field_73134_o.contains(entityplayer)) {
                this.field_73134_o.remove(entityplayer);
                this.field_73132_a.func_184203_c(entityplayer);
                entityplayer.func_152339_d(this.field_73132_a);
                updatePassengers(entityplayer); // Paper
            }

        }
    }

    public boolean func_180233_c(EntityPlayerMP entityplayer) {
        // Paper start
        if (field_73132_a.func_184218_aH()) {
            return isTrackedBy(field_73132_a.func_184208_bv(), entityplayer);
        } else if (hasPassengerInRange(field_73132_a, entityplayer)) {
            return true;
        }

        return isInRangeOfPlayer(entityplayer);
    }
    private static boolean hasPassengerInRange(Entity entity, EntityPlayerMP entityplayer) {
        if (!entity.func_184207_aI()) {
            return false;
        }
        for (Entity passenger : entity.field_184244_h) {
            if (passenger.tracker != null && passenger.tracker.isInRangeOfPlayer(entityplayer)) {
                return true;
            }
            if (passenger.func_184207_aI()) {
                if (hasPassengerInRange(passenger, entityplayer)) {
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean isTrackedBy(Entity entity, EntityPlayerMP entityplayer) {
        return entity == entityplayer || entity.tracker != null && entity.tracker.field_73134_o.contains(entityplayer);
    }
    private void updatePassengers(EntityPlayerMP player) {
        if (field_73132_a.func_184207_aI()) {
            field_73132_a.field_184244_h.forEach((e) -> {
                if (e.tracker != null) {
                    e.tracker.func_73117_b(player);
                }
            });
            player.field_71135_a.func_147359_a(new SPacketSetPassengers(this.field_73132_a));
        }
    }
    private boolean isInRangeOfPlayer(EntityPlayerMP entityplayer) {
        // Paper end
        double d0 = entityplayer.field_70165_t - (double) this.field_73128_d / 4096.0D;
        double d1 = entityplayer.field_70161_v - (double) this.field_73126_f / 4096.0D;
        int i = Math.min(this.field_73130_b, this.field_187262_f);

        return d0 >= (double) (-i) && d0 <= (double) i && d1 >= (double) (-i) && d1 <= (double) i && this.field_73132_a.func_174827_a(entityplayer);
    }

    private boolean func_73121_d(EntityPlayerMP entityplayer) {
        return entityplayer.func_71121_q().func_184164_w().func_72694_a(entityplayer, this.field_73132_a.field_70176_ah, this.field_73132_a.field_70164_aj);
    }

    public void func_73125_b(List<EntityPlayer> list) {
        for (int i = 0; i < list.size(); ++i) {
            this.func_73117_b((EntityPlayerMP) list.get(i));
        }

    }

    private Packet<?> func_151260_c() {
        if (this.field_73132_a.field_70128_L) {
            // CraftBukkit start - Remove useless error spam, just return
            // EntityTrackerEntry.d.warn("Fetching addPacket for removed entity");
            return null;
            // CraftBukkit end
        }

        if (this.field_73132_a instanceof EntityPlayerMP) {
            return new SPacketSpawnPlayer((EntityPlayer) this.field_73132_a);
        } else if (this.field_73132_a instanceof IAnimals) {
            this.field_73140_i = MathHelper.func_76141_d(this.field_73132_a.func_70079_am() * 256.0F / 360.0F);
            return new SPacketSpawnMob((EntityLivingBase) this.field_73132_a);
        } else if (this.field_73132_a instanceof EntityPainting) {
            return new SPacketSpawnPainting((EntityPainting) this.field_73132_a);
        } else if (this.field_73132_a instanceof EntityItem) {
            return new SPacketSpawnObject(this.field_73132_a, 2, 1);
        } else if (this.field_73132_a instanceof EntityMinecart) {
            EntityMinecart entityminecartabstract = (EntityMinecart) this.field_73132_a;

            return new SPacketSpawnObject(this.field_73132_a, 10, entityminecartabstract.func_184264_v().func_184956_a());
        } else if (this.field_73132_a instanceof EntityBoat) {
            return new SPacketSpawnObject(this.field_73132_a, 1);
        } else if (this.field_73132_a instanceof EntityXPOrb) {
            return new SPacketSpawnExperienceOrb((EntityXPOrb) this.field_73132_a);
        } else if (this.field_73132_a instanceof EntityFishHook) {
            EntityPlayer entityhuman = ((EntityFishHook) this.field_73132_a).func_190619_l();

            return new SPacketSpawnObject(this.field_73132_a, 90, entityhuman == null ? this.field_73132_a.func_145782_y() : entityhuman.func_145782_y());
        } else {
            Entity entity;

            if (this.field_73132_a instanceof EntitySpectralArrow) {
                entity = ((EntitySpectralArrow) this.field_73132_a).field_70250_c;
                return new SPacketSpawnObject(this.field_73132_a, 91, 1 + (entity == null ? this.field_73132_a.func_145782_y() : entity.func_145782_y()));
            } else if (this.field_73132_a instanceof EntityTippedArrow) {
                entity = ((EntityArrow) this.field_73132_a).field_70250_c;
                return new SPacketSpawnObject(this.field_73132_a, 60, 1 + (entity == null ? this.field_73132_a.func_145782_y() : entity.func_145782_y()));
            } else if (this.field_73132_a instanceof EntitySnowball) {
                return new SPacketSpawnObject(this.field_73132_a, 61);
            } else if (this.field_73132_a instanceof EntityLlamaSpit) {
                return new SPacketSpawnObject(this.field_73132_a, 68);
            } else if (this.field_73132_a instanceof EntityPotion) {
                return new SPacketSpawnObject(this.field_73132_a, 73);
            } else if (this.field_73132_a instanceof EntityExpBottle) {
                return new SPacketSpawnObject(this.field_73132_a, 75);
            } else if (this.field_73132_a instanceof EntityEnderPearl) {
                return new SPacketSpawnObject(this.field_73132_a, 65);
            } else if (this.field_73132_a instanceof EntityEnderEye) {
                return new SPacketSpawnObject(this.field_73132_a, 72);
            } else if (this.field_73132_a instanceof EntityFireworkRocket) {
                return new SPacketSpawnObject(this.field_73132_a, 76);
            } else if (this.field_73132_a instanceof EntityFireball) {
                EntityFireball entityfireball = (EntityFireball) this.field_73132_a;
                SPacketSpawnObject packetplayoutspawnentity = null;
                byte b0 = 63;

                if (this.field_73132_a instanceof EntitySmallFireball) {
                    b0 = 64;
                } else if (this.field_73132_a instanceof EntityDragonFireball) {
                    b0 = 93;
                } else if (this.field_73132_a instanceof EntityWitherSkull) {
                    b0 = 66;
                }

                if (entityfireball.field_70235_a != null) {
                    packetplayoutspawnentity = new SPacketSpawnObject(this.field_73132_a, b0, ((EntityFireball) this.field_73132_a).field_70235_a.func_145782_y());
                } else {
                    packetplayoutspawnentity = new SPacketSpawnObject(this.field_73132_a, b0, 0);
                }

                packetplayoutspawnentity.func_149003_d((int) (entityfireball.field_70232_b * 8000.0D));
                packetplayoutspawnentity.func_149000_e((int) (entityfireball.field_70233_c * 8000.0D));
                packetplayoutspawnentity.func_149007_f((int) (entityfireball.field_70230_d * 8000.0D));
                return packetplayoutspawnentity;
            } else if (this.field_73132_a instanceof EntityShulkerBullet) {
                SPacketSpawnObject packetplayoutspawnentity1 = new SPacketSpawnObject(this.field_73132_a, 67, 0);

                packetplayoutspawnentity1.func_149003_d((int) (this.field_73132_a.field_70159_w * 8000.0D));
                packetplayoutspawnentity1.func_149000_e((int) (this.field_73132_a.field_70181_x * 8000.0D));
                packetplayoutspawnentity1.func_149007_f((int) (this.field_73132_a.field_70179_y * 8000.0D));
                return packetplayoutspawnentity1;
            } else if (this.field_73132_a instanceof EntityEgg) {
                return new SPacketSpawnObject(this.field_73132_a, 62);
            } else if (this.field_73132_a instanceof EntityEvokerFangs) {
                return new SPacketSpawnObject(this.field_73132_a, 79);
            } else if (this.field_73132_a instanceof EntityTNTPrimed) {
                return new SPacketSpawnObject(this.field_73132_a, 50);
            } else if (this.field_73132_a instanceof EntityEnderCrystal) {
                return new SPacketSpawnObject(this.field_73132_a, 51);
            } else if (this.field_73132_a instanceof EntityFallingBlock) {
                EntityFallingBlock entityfallingblock = (EntityFallingBlock) this.field_73132_a;

                return new SPacketSpawnObject(this.field_73132_a, 70, Block.func_176210_f(entityfallingblock.func_175131_l()));
            } else if (this.field_73132_a instanceof EntityArmorStand) {
                return new SPacketSpawnObject(this.field_73132_a, 78);
            } else if (this.field_73132_a instanceof EntityItemFrame) {
                EntityItemFrame entityitemframe = (EntityItemFrame) this.field_73132_a;

                return new SPacketSpawnObject(this.field_73132_a, 71, entityitemframe.field_174860_b.func_176736_b(), entityitemframe.func_174857_n());
            } else if (this.field_73132_a instanceof EntityLeashKnot) {
                EntityLeashKnot entityleash = (EntityLeashKnot) this.field_73132_a;

                return new SPacketSpawnObject(this.field_73132_a, 77, 0, entityleash.func_174857_n());
            } else if (this.field_73132_a instanceof EntityAreaEffectCloud) {
                return new SPacketSpawnObject(this.field_73132_a, 3);
            } else {
                throw new IllegalArgumentException("Don\'t know how to add " + this.field_73132_a.getClass() + "!");
            }
        }
    }

    public void func_73123_c(EntityPlayerMP entityplayer) {
        org.spigotmc.AsyncCatcher.catchOp( "player tracker clear"); // Spigot
        if (this.field_73134_o.contains(entityplayer)) {
            this.field_73134_o.remove(entityplayer);
            this.field_73132_a.func_184203_c(entityplayer);
            entityplayer.func_152339_d(this.field_73132_a);
            updatePassengers(entityplayer); // Paper
        }

    }

    public Entity func_187260_b() {
        return this.field_73132_a;
    }

    public void func_187259_a(int i) {
        this.field_187262_f = i;
    }

    public void func_187261_c() {
        this.field_73144_s = false;
    }
}
