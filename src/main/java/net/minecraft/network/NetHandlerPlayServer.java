package net.minecraft.network;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.aikar.timings.MinecraftTimings;
import com.destroystokyo.paper.event.player.IllegalPacketEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.MCUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.ServerRecipeBookHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.util.NumberConversions;

// CraftBukkit start
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.util.NumberConversions;
import com.destroystokyo.paper.event.player.IllegalPacketEvent; // Paper
import com.destroystokyo.paper.event.player.PlayerJumpEvent; // Paper
import co.aikar.timings.MinecraftTimings; // Paper
// CraftBukkit end

public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable {

    private static final Logger field_147370_c = LogManager.getLogger();
    public final NetworkManager field_147371_a;
    private final MinecraftServer field_147367_d;
    public EntityPlayerMP field_147369_b;
    private int field_147368_e;
    private long field_194402_f = getCurrentMillis(); private void setLastPing(long lastPing) { this.field_194402_f = lastPing;}; private long getLastPing() { return this.field_194402_f;}; // Paper - OBFHELPER - set ping to delay initial
    private boolean field_194403_g; private void setPendingPing(boolean isPending) { this.field_194403_g = isPending;}; private boolean isPendingPing() { return this.field_194403_g;}; // Paper - OBFHELPER
    private long field_194404_h; private void setKeepAliveID(long keepAliveID) { this.field_194404_h = keepAliveID;}; private long getKeepAliveID() {return this.field_194404_h; };  // Paper - OBFHELPER
    // CraftBukkit start - multithreaded fields
    private volatile int field_147374_l;
    private static final AtomicIntegerFieldUpdater chatSpamField = AtomicIntegerFieldUpdater.newUpdater(NetHandlerPlayServer.class, "chatThrottle");
    // CraftBukkit end
    private int field_147375_m;
    private final IntHashMap<Short> field_147372_n = new IntHashMap();
    private double field_184349_l;
    private double field_184350_m;
    private double field_184351_n;
    private double field_184352_o;
    private double field_184353_p;
    private double field_184354_q;
    private Entity field_184355_r;
    private double field_184356_s;
    private double field_184357_t;
    private double field_184358_u;
    private double field_184359_v;
    private double field_184360_w;
    private double field_184361_x;
    private Vec3d field_184362_y;
    private int field_184363_z;
    private int field_184343_A;
    private boolean field_184344_B;
    private int field_147365_f;
    private boolean field_184345_D;
    private int field_184346_E;
    private int field_184347_F;
    private int field_184348_G;
    private ServerRecipeBookHelper field_194309_H = new ServerRecipeBookHelper();
    private static final long KEEPALIVE_LIMIT = Long.getLong("paper.playerconnection.keepalive", 30) * 1000; // Paper - provide property to set keepalive limit

    public NetHandlerPlayServer(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayerMP entityplayer) {
        this.field_147367_d = minecraftserver;
        this.field_147371_a = networkmanager;
        networkmanager.func_150719_a(this);
        this.field_147369_b = entityplayer;
        entityplayer.field_71135_a = this;

        // CraftBukkit start - add fields and methods
        this.server = minecraftserver.server;
    }

    private final org.bukkit.craftbukkit.CraftServer server;
    private boolean processedDisconnect;
    private int lastTick = MinecraftServer.currentTick;
    private int allowedPlayerTicks = 1;
    private int lastDropTick = MinecraftServer.currentTick;
    private int lastBookTick  = MinecraftServer.currentTick;
    private int dropCount = 0;
    private static final int SURVIVAL_PLACE_DISTANCE_SQUARED = 6 * 6;
    private static final int CREATIVE_PLACE_DISTANCE_SQUARED = 7 * 7;

    // Get position of last block hit for BlockDamageLevel.STOPPED
    private double lastPosX = Double.MAX_VALUE;
    private double lastPosY = Double.MAX_VALUE;
    private double lastPosZ = Double.MAX_VALUE;
    private float lastPitch = Float.MAX_VALUE;
    private float lastYaw = Float.MAX_VALUE;
    private boolean justTeleported = false;
    private boolean hasMoved; // Spigot

    public CraftPlayer getPlayer() {
        return (this.field_147369_b == null) ? null : (CraftPlayer) this.field_147369_b.getBukkitEntity();
    }
    private final static HashSet<Integer> invalidItems = new HashSet<Integer>(java.util.Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 55, 59, 62, 63, 64, 68, 71, 74, 75, 83, 90, 92, 93, 94, 104, 105, 115, 117, 118, 119, 125, 127, 132, 140, 141, 142, 144)); // TODO: Check after every update.
    // CraftBukkit end

    public void func_73660_a() {
        this.func_184342_d();
        this.field_147369_b.func_71127_g();
        this.field_147369_b.func_70080_a(this.field_184349_l, this.field_184350_m, this.field_184351_n, this.field_147369_b.field_70177_z, this.field_147369_b.field_70125_A);
        ++this.field_147368_e;
        this.field_184348_G = this.field_184347_F;
        if (this.field_184344_B) {
            if (++this.field_147365_f > 80) {
                NetHandlerPlayServer.field_147370_c.warn("{} was kicked for floating too long!", this.field_147369_b.func_70005_c_());
                this.disconnect(com.destroystokyo.paper.PaperConfig.flyingKickPlayerMessage); // Paper - use configurable kick message
                return;
            }
        } else {
            this.field_184344_B = false;
            this.field_147365_f = 0;
        }

        this.field_184355_r = this.field_147369_b.func_184208_bv();
        if (this.field_184355_r != this.field_147369_b && this.field_184355_r.func_184179_bs() == this.field_147369_b) {
            this.field_184356_s = this.field_184355_r.field_70165_t;
            this.field_184357_t = this.field_184355_r.field_70163_u;
            this.field_184358_u = this.field_184355_r.field_70161_v;
            this.field_184359_v = this.field_184355_r.field_70165_t;
            this.field_184360_w = this.field_184355_r.field_70163_u;
            this.field_184361_x = this.field_184355_r.field_70161_v;
            if (this.field_184345_D && this.field_147369_b.func_184208_bv().func_184179_bs() == this.field_147369_b) {
                if (++this.field_184346_E > 80) {
                    NetHandlerPlayServer.field_147370_c.warn("{} was kicked for floating a vehicle too long!", this.field_147369_b.func_70005_c_());
                    this.disconnect(com.destroystokyo.paper.PaperConfig.flyingKickVehicleMessage); // Paper - use configurable kick message
                    return;
                }
            } else {
                this.field_184345_D = false;
                this.field_184346_E = 0;
            }
        } else {
            this.field_184355_r = null;
            this.field_184345_D = false;
            this.field_184346_E = 0;
        }

        this.field_147367_d.field_71304_b.func_76320_a("keepAlive");
        // Paper Start - give clients a longer time to respond to pings as per pre 1.12.2 timings
        // This should effectively place the keepalive handling back to "as it was" before 1.12.2
        long currentTime = this.getCurrentMillis();
        long elapsedTime = currentTime - this.getLastPing();
        if (this.isPendingPing()) {
            // We're pending a ping from the client
            if (!this.processedDisconnect && elapsedTime >= KEEPALIVE_LIMIT) { // check keepalive limit, don't fire if already disconnected
                NetHandlerPlayServer.field_147370_c.warn("{} was kicked due to keepalive timeout!", this.field_147369_b.func_70005_c_()); // more info
                this.func_194028_b(new TextComponentTranslation("disconnect.timeout"));
            }
        } else {
            if (elapsedTime >= 15000L) { // 15 seconds
                this.setPendingPing(true);
                this.setLastPing(currentTime);
                this.setKeepAliveID(currentTime);
                this.func_147359_a(new SPacketKeepAlive(this.getKeepAliveID()));
            }
        }
        // Paper end

        this.field_147367_d.field_71304_b.func_76319_b();
        // CraftBukkit start
        for (int spam; (spam = this.field_147374_l) > 0 && !chatSpamField.compareAndSet(this, spam, spam - 1); ) ;
        /* Use thread-safe field access instead
        if (this.chatThrottle > 0) {
            --this.chatThrottle;
        }
        */
        // CraftBukkit end

        if (this.field_147375_m > 0) {
            --this.field_147375_m;
        }

        if (this.field_147369_b.func_154331_x() > 0L && this.field_147367_d.func_143007_ar() > 0 && MinecraftServer.func_130071_aq() - this.field_147369_b.func_154331_x() > (long) (this.field_147367_d.func_143007_ar() * 1000 * 60)) {
            this.field_147369_b.func_143004_u(); // CraftBukkit - SPIGOT-854
            this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.idling", new Object[0]));
        }

    }

    public void func_184342_d() {
        this.field_184349_l = this.field_147369_b.field_70165_t;
        this.field_184350_m = this.field_147369_b.field_70163_u;
        this.field_184351_n = this.field_147369_b.field_70161_v;
        this.field_184352_o = this.field_147369_b.field_70165_t;
        this.field_184353_p = this.field_147369_b.field_70163_u;
        this.field_184354_q = this.field_147369_b.field_70161_v;
    }

    public NetworkManager func_147362_b() {
        return this.field_147371_a;
    }

    // CraftBukkit start
    @Deprecated
    public void func_194028_b(ITextComponent ichatbasecomponent) {
        disconnect(CraftChatMessage.fromComponent(ichatbasecomponent, TextFormatting.WHITE));
    }
    // CraftBukkit end

    public void disconnect(String s) {
        // CraftBukkit start - fire PlayerKickEvent
        if (this.processedDisconnect) {
            return;
        }
        String leaveMessage = TextFormatting.YELLOW + this.field_147369_b.func_70005_c_() + " left the game.";

        PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.field_147369_b), s, leaveMessage);

        if (this.server.getServer().func_71278_l()) {
            this.server.getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            // Do not kick the player
            return;
        }
        // Send the possibly modified leave message
        s = event.getReason();
        // CraftBukkit end
        final TextComponentString chatcomponenttext = new TextComponentString(s);

        this.field_147371_a.func_179288_a(new SPacketDisconnect(chatcomponenttext), new GenericFutureListener() {
            public void operationComplete(Future future) throws Exception { // CraftBukkit - decompile error
                NetHandlerPlayServer.this.field_147371_a.func_150718_a(chatcomponenttext);
            }
        }, new GenericFutureListener[0]);
        this.func_147231_a(chatcomponenttext); // CraftBukkit - fire quit instantly
        this.field_147371_a.func_150721_g();
        // CraftBukkit - Don't wait
        this.field_147367_d.func_152344_a(new Runnable() {
            public void run() {
                NetHandlerPlayServer.this.field_147371_a.func_179293_l();
            }
        });
    }

    public void func_147358_a(CPacketInput packetplayinsteervehicle) {
        PacketThreadUtil.func_180031_a(packetplayinsteervehicle, this, this.field_147369_b.func_71121_q());
        this.field_147369_b.func_110430_a(packetplayinsteervehicle.func_149620_c(), packetplayinsteervehicle.func_192620_b(), packetplayinsteervehicle.func_149618_e(), packetplayinsteervehicle.func_149617_f());
    }

    private static boolean func_183006_b(CPacketPlayer packetplayinflying) {
        return Doubles.isFinite(packetplayinflying.func_186997_a(0.0D)) && Doubles.isFinite(packetplayinflying.func_186996_b(0.0D)) && Doubles.isFinite(packetplayinflying.func_187000_c(0.0D)) && Floats.isFinite(packetplayinflying.func_186998_b(0.0F)) && Floats.isFinite(packetplayinflying.func_186999_a(0.0F)) ? Math.abs(packetplayinflying.func_186997_a(0.0D)) > 3.0E7D || Math.abs(packetplayinflying.func_186996_b(0.0D)) > 3.0E7D || Math.abs(packetplayinflying.func_187000_c(0.0D)) > 3.0E7D : true;
    }

    private static boolean func_184341_b(CPacketVehicleMove packetplayinvehiclemove) {
        return !Doubles.isFinite(packetplayinvehiclemove.func_187004_a()) || !Doubles.isFinite(packetplayinvehiclemove.func_187002_b()) || !Doubles.isFinite(packetplayinvehiclemove.func_187003_c()) || !Floats.isFinite(packetplayinvehiclemove.func_187005_e()) || !Floats.isFinite(packetplayinvehiclemove.func_187006_d());
    }

    public void func_184338_a(CPacketVehicleMove packetplayinvehiclemove) {
        PacketThreadUtil.func_180031_a(packetplayinvehiclemove, this, this.field_147369_b.func_71121_q());
        if (func_184341_b(packetplayinvehiclemove)) {
            this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.invalid_vehicle_movement", new Object[0]));
        } else {
            Entity entity = this.field_147369_b.func_184208_bv();

            if (entity != this.field_147369_b && entity.func_184179_bs() == this.field_147369_b && entity == this.field_184355_r) {
                WorldServer worldserver = this.field_147369_b.func_71121_q();
                double d0 = entity.field_70165_t;
                double d1 = entity.field_70163_u;
                double d2 = entity.field_70161_v;
                double d3 = packetplayinvehiclemove.func_187004_a();
                double d4 = packetplayinvehiclemove.func_187002_b();
                double d5 = packetplayinvehiclemove.func_187003_c();
                float f = packetplayinvehiclemove.func_187006_d();
                float f1 = packetplayinvehiclemove.func_187005_e();
                double d6 = d3 - this.field_184356_s;
                double d7 = d4 - this.field_184357_t;
                double d8 = d5 - this.field_184358_u;
                double d9 = entity.field_70159_w * entity.field_70159_w + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y;
                double d10 = d6 * d6 + d7 * d7 + d8 * d8;


                // CraftBukkit start - handle custom speeds and skipped ticks
                this.allowedPlayerTicks += (System.currentTimeMillis() / 50) - this.lastTick;
                this.allowedPlayerTicks = Math.max(this.allowedPlayerTicks, 1);
                this.lastTick = (int) (System.currentTimeMillis() / 50);

                ++this.field_184347_F;
                int i = this.field_184347_F - this.field_184348_G;
                if (i > Math.max(this.allowedPlayerTicks, 5)) {
                    NetHandlerPlayServer.field_147370_c.debug(this.field_147369_b.func_70005_c_() + " is sending move packets too frequently (" + i + " packets since last tick)");
                    i = 1;
                }

                if (d10 > 0) {
                    allowedPlayerTicks -= 1;
                } else {
                    allowedPlayerTicks = 20;
                }
                float speed;
                if (field_147369_b.field_71075_bZ.field_75100_b) {
                    speed = field_147369_b.field_71075_bZ.field_75096_f * 20f;
                } else {
                    speed = field_147369_b.field_71075_bZ.field_75097_g * 10f;
                }
                speed *= 2f; // TODO: Get the speed of the vehicle instead of the player

                if (d10 - d9 > Math.max(100.0D, Math.pow((double) (org.spigotmc.SpigotConfig.movedTooQuicklyMultiplier * (float) i * speed), 2)) && (!this.field_147367_d.func_71264_H() || !this.field_147367_d.func_71214_G().equals(entity.func_70005_c_()))) { // Spigot
                // CraftBukkit end
                    NetHandlerPlayServer.field_147370_c.warn("{} (vehicle of {}) moved too quickly! {},{},{}", entity.func_70005_c_(), this.field_147369_b.func_70005_c_(), Double.valueOf(d6), Double.valueOf(d7), Double.valueOf(d8));
                    this.field_147371_a.func_179290_a(new SPacketMoveVehicle(entity));
                    return;
                }

                boolean flag = worldserver.func_184144_a(entity, entity.func_174813_aQ().func_186664_h(0.0625D)).isEmpty();

                d6 = d3 - this.field_184359_v;
                d7 = d4 - this.field_184360_w - 1.0E-6D;
                d8 = d5 - this.field_184361_x;
                entity.func_70091_d(MoverType.PLAYER, d6, d7, d8);
                double d11 = d7;

                d6 = d3 - entity.field_70165_t;
                d7 = d4 - entity.field_70163_u;
                if (d7 > -0.5D || d7 < 0.5D) {
                    d7 = 0.0D;
                }

                d8 = d5 - entity.field_70161_v;
                d10 = d6 * d6 + d7 * d7 + d8 * d8;
                boolean flag1 = false;

                if (d10 > org.spigotmc.SpigotConfig.movedWronglyThreshold) { // Spigot
                    flag1 = true;
                    NetHandlerPlayServer.field_147370_c.warn(entity.func_70005_c_() + " (vehicle of " + this.field_147369_b.func_70005_c_() + ") moved wrongly!"); // Paper - More informative
                }

                entity.func_70080_a(d3, d4, d5, f, f1);
                boolean flag2 = worldserver.func_184144_a(entity, entity.func_174813_aQ().func_186664_h(0.0625D)).isEmpty();

                if (flag && (flag1 || !flag2)) {
                    entity.func_70080_a(d0, d1, d2, f, f1);
                    this.field_147371_a.func_179290_a(new SPacketMoveVehicle(entity));
                    return;
                }

                // CraftBukkit start - fire PlayerMoveEvent
                Player player = this.getPlayer();
                // Spigot Start
                if ( !hasMoved )
                {
                    Location curPos = player.getLocation();
                    lastPosX = curPos.getX();
                    lastPosY = curPos.getY();
                    lastPosZ = curPos.getZ();
                    lastYaw = curPos.getYaw();
                    lastPitch = curPos.getPitch();
                    hasMoved = true;
                }
                // Spigot End
                Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
                Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

                // If the packet contains movement information then we update the To location with the correct XYZ.
                to.setX(packetplayinvehiclemove.func_187004_a());
                to.setY(packetplayinvehiclemove.func_187002_b());
                to.setZ(packetplayinvehiclemove.func_187003_c());


                // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
                to.setYaw(packetplayinvehiclemove.func_187006_d());
                to.setPitch(packetplayinvehiclemove.func_187005_e());

                // Prevent 40 event-calls for less than a single pixel of movement >.>
                double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
                float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

                if ((delta > 1f / 256 || deltaAngle > 10f) && !this.field_147369_b.func_70610_aX()) {
                    this.lastPosX = to.getX();
                    this.lastPosY = to.getY();
                    this.lastPosZ = to.getZ();
                    this.lastYaw = to.getYaw();
                    this.lastPitch = to.getPitch();

                    // Skip the first time we do this
                    if (true) { // Spigot - don't skip any move events
                        Location oldTo = to.clone();
                        PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
                        this.server.getPluginManager().callEvent(event);

                        // If the event is cancelled we move the player back to their old location.
                        if (event.isCancelled()) {
                            teleport(from);
                            return;
                        }

                        // If a Plugin has changed the To destination then we teleport the Player
                        // there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
                        // We only do this if the Event was not cancelled.
                        if (!oldTo.equals(event.getTo()) && !event.isCancelled()) {
                            this.field_147369_b.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                            return;
                        }

                        // Check to see if the Players Location has some how changed during the call of the event.
                        // This can happen due to a plugin teleporting the player instead of using .setTo()
                        if (!from.equals(this.getPlayer().getLocation()) && this.justTeleported) {
                            this.justTeleported = false;
                            return;
                        }
                    }
                }
                // CraftBukkit end

                this.field_147367_d.func_184103_al().func_72358_d(this.field_147369_b);
                this.field_147369_b.func_71000_j(this.field_147369_b.field_70165_t - d0, this.field_147369_b.field_70163_u - d1, this.field_147369_b.field_70161_v - d2);
                this.field_184345_D = d11 >= -0.03125D && !this.field_147367_d.func_71231_X() && !worldserver.func_72829_c(entity.func_174813_aQ().func_186662_g(0.0625D).func_72321_a(0.0D, -0.55D, 0.0D));
                this.field_184359_v = entity.field_70165_t;
                this.field_184360_w = entity.field_70163_u;
                this.field_184361_x = entity.field_70161_v;
            }

        }
    }

    public void func_184339_a(CPacketConfirmTeleport packetplayinteleportaccept) {
        PacketThreadUtil.func_180031_a(packetplayinteleportaccept, this, this.field_147369_b.func_71121_q());
        if (packetplayinteleportaccept.func_186987_a() == this.field_184363_z && this.field_184362_y != null) { // CraftBukkit
            this.field_147369_b.func_70080_a(this.field_184362_y.field_72450_a, this.field_184362_y.field_72448_b, this.field_184362_y.field_72449_c, this.field_147369_b.field_70177_z, this.field_147369_b.field_70125_A);
            if (this.field_147369_b.func_184850_K()) {
                this.field_184352_o = this.field_184362_y.field_72450_a;
                this.field_184353_p = this.field_184362_y.field_72448_b;
                this.field_184354_q = this.field_184362_y.field_72449_c;
                this.field_147369_b.func_184846_L();
            }

            this.field_184362_y = null;
        }

    }

    public void func_191984_a(CPacketRecipeInfo packetplayinrecipedisplayed) {
        PacketThreadUtil.func_180031_a(packetplayinrecipedisplayed, this, this.field_147369_b.func_71121_q());
        if (packetplayinrecipedisplayed.func_194156_a() == CPacketRecipeInfo.Purpose.SHOWN) {
            this.field_147369_b.func_192037_E().func_194074_f(packetplayinrecipedisplayed.func_193648_b());
        } else if (packetplayinrecipedisplayed.func_194156_a() == CPacketRecipeInfo.Purpose.SETTINGS) {
            this.field_147369_b.func_192037_E().func_192813_a(packetplayinrecipedisplayed.func_192624_c());
            this.field_147369_b.func_192037_E().func_192810_b(packetplayinrecipedisplayed.func_192625_d());
        }

    }

    public void func_194027_a(CPacketSeenAdvancements packetplayinadvancements) {
        PacketThreadUtil.func_180031_a(packetplayinadvancements, this, this.field_147369_b.func_71121_q());
        if (packetplayinadvancements.func_194162_b() == CPacketSeenAdvancements.Action.OPENED_TAB) {
            ResourceLocation minecraftkey = packetplayinadvancements.func_194165_c();
            Advancement advancement = this.field_147367_d.func_191949_aK().func_192778_a(minecraftkey);

            if (advancement != null) {
                this.field_147369_b.func_192039_O().func_194220_a(advancement);
            }
        }

    }

    public void func_147347_a(CPacketPlayer packetplayinflying) {
        PacketThreadUtil.func_180031_a(packetplayinflying, this, this.field_147369_b.func_71121_q());
        if (func_183006_b(packetplayinflying)) {
            this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.invalid_player_movement", new Object[0]));
        } else {
            WorldServer worldserver = this.field_147367_d.func_71218_a(this.field_147369_b.field_71093_bK);

            if (!this.field_147369_b.field_71136_j && !this.field_147369_b.func_70610_aX()) { // CraftBukkit
                if (this.field_147368_e == 0) {
                    this.func_184342_d();
                }

                if (this.field_184362_y != null) {
                    if (this.field_147368_e - this.field_184343_A > 20) {
                        this.field_184343_A = this.field_147368_e;
                        this.func_147364_a(this.field_184362_y.field_72450_a, this.field_184362_y.field_72448_b, this.field_184362_y.field_72449_c, this.field_147369_b.field_70177_z, this.field_147369_b.field_70125_A);
                    }
                    this.allowedPlayerTicks = 20; // CraftBukkit
                } else {
                    this.field_184343_A = this.field_147368_e;
                    if (this.field_147369_b.func_184218_aH()) {
                        this.field_147369_b.func_70080_a(this.field_147369_b.field_70165_t, this.field_147369_b.field_70163_u, this.field_147369_b.field_70161_v, packetplayinflying.func_186999_a(this.field_147369_b.field_70177_z), packetplayinflying.func_186998_b(this.field_147369_b.field_70125_A));
                        this.field_147367_d.func_184103_al().func_72358_d(this.field_147369_b);
                        this.allowedPlayerTicks = 20; // CraftBukkit
                    } else {
                        // CraftBukkit - Make sure the move is valid but then reset it for plugins to modify
                        double prevX = field_147369_b.field_70165_t;
                        double prevY = field_147369_b.field_70163_u;
                        double prevZ = field_147369_b.field_70161_v;
                        float prevYaw = field_147369_b.field_70177_z;
                        float prevPitch = field_147369_b.field_70125_A;
                        // CraftBukkit end
                        double d0 = this.field_147369_b.field_70165_t;
                        double d1 = this.field_147369_b.field_70163_u;
                        double d2 = this.field_147369_b.field_70161_v;
                        double d3 = this.field_147369_b.field_70163_u;
                        double d4 = packetplayinflying.func_186997_a(this.field_147369_b.field_70165_t);
                        double d5 = packetplayinflying.func_186996_b(this.field_147369_b.field_70163_u);
                        double d6 = packetplayinflying.func_187000_c(this.field_147369_b.field_70161_v);
                        float f = packetplayinflying.func_186999_a(this.field_147369_b.field_70177_z);
                        float f1 = packetplayinflying.func_186998_b(this.field_147369_b.field_70125_A);
                        double d7 = d4 - this.field_184349_l;
                        double d8 = d5 - this.field_184350_m;
                        double d9 = d6 - this.field_184351_n;
                        double d10 = this.field_147369_b.field_70159_w * this.field_147369_b.field_70159_w + this.field_147369_b.field_70181_x * this.field_147369_b.field_70181_x + this.field_147369_b.field_70179_y * this.field_147369_b.field_70179_y;
                        double d11 = d7 * d7 + d8 * d8 + d9 * d9;

                        if (this.field_147369_b.func_70608_bn()) {
                            if (d11 > 1.0D) {
                                this.func_147364_a(this.field_147369_b.field_70165_t, this.field_147369_b.field_70163_u, this.field_147369_b.field_70161_v, packetplayinflying.func_186999_a(this.field_147369_b.field_70177_z), packetplayinflying.func_186998_b(this.field_147369_b.field_70125_A));
                            }

                        } else {
                            ++this.field_184347_F;
                            int i = this.field_184347_F - this.field_184348_G;

                            // CraftBukkit start - handle custom speeds and skipped ticks
                            this.allowedPlayerTicks += (System.currentTimeMillis() / 50) - this.lastTick;
                            this.allowedPlayerTicks = Math.max(this.allowedPlayerTicks, 1);
                            this.lastTick = (int) (System.currentTimeMillis() / 50);

                            if (i > Math.max(this.allowedPlayerTicks, 5)) {
                                NetHandlerPlayServer.field_147370_c.debug("{} is sending move packets too frequently ({} packets since last tick)", this.field_147369_b.func_70005_c_(), Integer.valueOf(i));
                                i = 1;
                            }

                            if (packetplayinflying.field_149481_i || d11 > 0) {
                                allowedPlayerTicks -= 1;
                            } else {
                                allowedPlayerTicks = 20;
                            }
                            float speed;
                            if (field_147369_b.field_71075_bZ.field_75100_b) {
                                speed = field_147369_b.field_71075_bZ.field_75096_f * 20f;
                            } else {
                                speed = field_147369_b.field_71075_bZ.field_75097_g * 10f;
                            }

                            if (!this.field_147369_b.func_184850_K() && (!this.field_147369_b.func_71121_q().func_82736_K().func_82766_b("disableElytraMovementCheck") || !this.field_147369_b.func_184613_cA())) {
                                float f2 = this.field_147369_b.func_184613_cA() ? 300.0F : 100.0F;

                                if (d11 - d10 > Math.max(f2, Math.pow((double) (org.spigotmc.SpigotConfig.movedTooQuicklyMultiplier * (float) i * speed), 2)) && (!this.field_147367_d.func_71264_H() || !this.field_147367_d.func_71214_G().equals(this.field_147369_b.func_70005_c_()))) { // Spigot
                                // CraftBukkit end
                                    NetHandlerPlayServer.field_147370_c.warn("{} moved too quickly! {},{},{}", this.field_147369_b.func_70005_c_(), Double.valueOf(d7), Double.valueOf(d8), Double.valueOf(d9));
                                    this.func_147364_a(this.field_147369_b.field_70165_t, this.field_147369_b.field_70163_u, this.field_147369_b.field_70161_v, this.field_147369_b.field_70177_z, this.field_147369_b.field_70125_A);
                                    return;
                                }
                            }

                            boolean flag = worldserver.func_184144_a(this.field_147369_b, this.field_147369_b.func_174813_aQ().func_186664_h(0.0625D)).isEmpty();

                            d7 = d4 - this.field_184352_o;
                            d8 = d5 - this.field_184353_p;
                            d9 = d6 - this.field_184354_q;
                            if (this.field_147369_b.field_70122_E && !packetplayinflying.func_149465_i() && d8 > 0.0D) {
                                // Paper start - Add player jump event
                                Player player = this.getPlayer();
                                Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
                                Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

                                // If the packet contains movement information then we update the To location with the correct XYZ.
                                if (packetplayinflying.field_149480_h) {
                                    to.setX(packetplayinflying.field_149479_a);
                                    to.setY(packetplayinflying.field_149477_b);
                                    to.setZ(packetplayinflying.field_149478_c);
                                }

                                // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
                                if (packetplayinflying.field_149481_i) {
                                    to.setYaw(packetplayinflying.field_149476_e);
                                    to.setPitch(packetplayinflying.field_149473_f);
                                }

                                PlayerJumpEvent event = new PlayerJumpEvent(player, from, to);

                                if (event.callEvent()) {
                                    this.field_147369_b.jump();
                                } else {
                                    from = event.getFrom();
                                    this.internalTeleport(from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch(), Collections.emptySet());
                                    return;
                                }
                                // Paper end
                            }

                            this.field_147369_b.func_70091_d(MoverType.PLAYER, d7, d8, d9);
                            this.field_147369_b.field_70122_E = packetplayinflying.func_149465_i();
                            double d12 = d8;

                            d7 = d4 - this.field_147369_b.field_70165_t;
                            d8 = d5 - this.field_147369_b.field_70163_u;
                            if (d8 > -0.5D || d8 < 0.5D) {
                                d8 = 0.0D;
                            }

                            d9 = d6 - this.field_147369_b.field_70161_v;
                            d11 = d7 * d7 + d8 * d8 + d9 * d9;
                            boolean flag1 = false;

                            if (!this.field_147369_b.func_184850_K() && d11 > org.spigotmc.SpigotConfig.movedWronglyThreshold && !this.field_147369_b.func_70608_bn() && !this.field_147369_b.field_71134_c.func_73083_d() && this.field_147369_b.field_71134_c.func_73081_b() != GameType.SPECTATOR) { // Spigot
                                flag1 = true;
                                NetHandlerPlayServer.field_147370_c.warn("{} moved wrongly!", this.field_147369_b.func_70005_c_());
                            }

                            this.field_147369_b.func_70080_a(d4, d5, d6, f, f1);
                            this.field_147369_b.func_71000_j(this.field_147369_b.field_70165_t - d0, this.field_147369_b.field_70163_u - d1, this.field_147369_b.field_70161_v - d2);
                            if (!this.field_147369_b.field_70145_X && !this.field_147369_b.func_70608_bn()) {
                                boolean flag2 = worldserver.func_184144_a(this.field_147369_b, this.field_147369_b.func_174813_aQ().func_186664_h(0.0625D)).isEmpty();

                                if (flag && (flag1 || !flag2)) {
                                    this.func_147364_a(d0, d1, d2, f, f1);
                                    return;
                                }
                            }

                            // CraftBukkit start - fire PlayerMoveEvent
                            // Rest to old location first
                            this.field_147369_b.func_70080_a(prevX, prevY, prevZ, prevYaw, prevPitch);

                            Player player = this.getPlayer();
                            Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
                            Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

                            // If the packet contains movement information then we update the To location with the correct XYZ.
                            if (packetplayinflying.field_149480_h) {
                                to.setX(packetplayinflying.field_149479_a);
                                to.setY(packetplayinflying.field_149477_b);
                                to.setZ(packetplayinflying.field_149478_c);
                            }

                            // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
                            if (packetplayinflying.field_149481_i) {
                                to.setYaw(packetplayinflying.field_149476_e);
                                to.setPitch(packetplayinflying.field_149473_f);
                            }

                            // Prevent 40 event-calls for less than a single pixel of movement >.>
                            double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
                            float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

                            if ((delta > 1f / 256 || deltaAngle > 10f) && !this.field_147369_b.func_70610_aX()) {
                                this.lastPosX = to.getX();
                                this.lastPosY = to.getY();
                                this.lastPosZ = to.getZ();
                                this.lastYaw = to.getYaw();
                                this.lastPitch = to.getPitch();

                                // Skip the first time we do this
                                if (from.getX() != Double.MAX_VALUE) {
                                    Location oldTo = to.clone();
                                    PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
                                    this.server.getPluginManager().callEvent(event);

                                    // If the event is cancelled we move the player back to their old location.
                                    if (event.isCancelled()) {
                                        teleport(from);
                                        return;
                                    }

                                    // If a Plugin has changed the To destination then we teleport the Player
                                    // there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
                                    // We only do this if the Event was not cancelled.
                                    if (!oldTo.equals(event.getTo()) && !event.isCancelled()) {
                                        this.field_147369_b.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                                        return;
                                    }

                                    // Check to see if the Players Location has some how changed during the call of the event.
                                    // This can happen due to a plugin teleporting the player instead of using .setTo()
                                    if (!from.equals(this.getPlayer().getLocation()) && this.justTeleported) {
                                        this.justTeleported = false;
                                        return;
                                    }
                                }
                            }
                            this.field_147369_b.func_70080_a(d4, d5, d6, f, f1); // Copied from above
                            // CraftBukkit end

                            this.field_184344_B = d12 >= -0.03125D;
                            this.field_184344_B &= !this.field_147367_d.func_71231_X() && !this.field_147369_b.field_71075_bZ.field_75101_c;
                            this.field_184344_B &= !this.field_147369_b.func_70644_a(MobEffects.field_188424_y) && !this.field_147369_b.func_184613_cA() && !worldserver.func_72829_c(this.field_147369_b.func_174813_aQ().func_186662_g(0.0625D).func_72321_a(0.0D, -0.55D, 0.0D));
                            this.field_147369_b.field_70122_E = packetplayinflying.func_149465_i();
                            this.field_147367_d.func_184103_al().func_72358_d(this.field_147369_b);
                            this.field_147369_b.func_71122_b(this.field_147369_b.field_70163_u - d3, packetplayinflying.func_149465_i());
                            this.field_184352_o = this.field_147369_b.field_70165_t;
                            this.field_184353_p = this.field_147369_b.field_70163_u;
                            this.field_184354_q = this.field_147369_b.field_70161_v;
                        }
                    }
                }
            }
        }
    }

    public void func_147364_a(double d0, double d1, double d2, float f, float f1) {
        this.func_175089_a(d0, d1, d2, f, f1, Collections.<SPacketPlayerPosLook.EnumFlags>emptySet());
    }

    // CraftBukkit start - Delegate to teleport(Location)
    public void a(double d0, double d1, double d2, float f, float f1, PlayerTeleportEvent.TeleportCause cause) {
        this.a(d0, d1, d2, f, f1, Collections.<SPacketPlayerPosLook.EnumFlags>emptySet(), cause);
    }

    public void func_175089_a(double d0, double d1, double d2, float f, float f1, Set<SPacketPlayerPosLook.EnumFlags> set) {
        this.a(d0, d1, d2, f, f1, set, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    public void a(double d0, double d1, double d2, float f, float f1, Set<SPacketPlayerPosLook.EnumFlags> set, PlayerTeleportEvent.TeleportCause cause) {
        Player player = this.getPlayer();
        Location from = player.getLocation();

        double x = d0;
        double y = d1;
        double z = d2;
        float yaw = f;
        float pitch = f1;
        if (set.contains(SPacketPlayerPosLook.EnumFlags.X)) {
            x += from.getX();
        }
        if (set.contains(SPacketPlayerPosLook.EnumFlags.Y)) {
            y += from.getY();
        }
        if (set.contains(SPacketPlayerPosLook.EnumFlags.Z)) {
            z += from.getZ();
        }
        if (set.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
            yaw += from.getYaw();
        }
        if (set.contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
            pitch += from.getPitch();
        }


        Location to = new Location(this.getPlayer().getWorld(), x, y, z, yaw, pitch);
        PlayerTeleportEvent event = new PlayerTeleportEvent(player, from.clone(), to.clone(), cause);
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled() || !to.equals(event.getTo())) {
            set.clear(); // Can't relative teleport
            to = event.isCancelled() ? event.getFrom() : event.getTo();
            d0 = to.getX();
            d1 = to.getY();
            d2 = to.getZ();
            f = to.getYaw();
            f1 = to.getPitch();
        }

        this.internalTeleport(d0, d1, d2, f, f1, set);
    }

    public void teleport(Location dest) {
        internalTeleport(dest.getX(), dest.getY(), dest.getZ(), dest.getYaw(), dest.getPitch(), Collections.<SPacketPlayerPosLook.EnumFlags>emptySet());
    }

    private void internalTeleport(double d0, double d1, double d2, float f, float f1, Set<SPacketPlayerPosLook.EnumFlags> set) {
        // CraftBukkit start
        if (Float.isNaN(f)) {
            f = 0;
        }
        if (Float.isNaN(f1)) {
            f1 = 0;
        }

        this.justTeleported = true;
        // CraftBukkit end
        double d3 = set.contains(SPacketPlayerPosLook.EnumFlags.X) ? this.field_147369_b.field_70165_t : 0.0D;
        double d4 = set.contains(SPacketPlayerPosLook.EnumFlags.Y) ? this.field_147369_b.field_70163_u : 0.0D;
        double d5 = set.contains(SPacketPlayerPosLook.EnumFlags.Z) ? this.field_147369_b.field_70161_v : 0.0D;

        this.field_184362_y = new Vec3d(d0 + d3, d1 + d4, d2 + d5);
        float f2 = f;
        float f3 = f1;

        if (set.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f2 = f + this.field_147369_b.field_70177_z;
        }

        if (set.contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
            f3 = f1 + this.field_147369_b.field_70125_A;
        }

        // CraftBukkit start - update last location
        this.lastPosX = this.field_184362_y.field_72450_a;
        this.lastPosY = this.field_184362_y.field_72448_b;
        this.lastPosZ = this.field_184362_y.field_72449_c;
        this.lastYaw = f2;
        this.lastPitch = f3;
        // CraftBukkit end

        if (++this.field_184363_z == Integer.MAX_VALUE) {
            this.field_184363_z = 0;
        }

        this.field_184343_A = this.field_147368_e;
        this.field_147369_b.func_70080_a(this.field_184362_y.field_72450_a, this.field_184362_y.field_72448_b, this.field_184362_y.field_72449_c, f2, f3);
        this.field_147369_b.field_71135_a.func_147359_a(new SPacketPlayerPosLook(d0, d1, d2, f, f1, set, this.field_184363_z));
    }

    public void func_147345_a(CPacketPlayerDigging packetplayinblockdig) {
        PacketThreadUtil.func_180031_a(packetplayinblockdig, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        WorldServer worldserver = this.field_147367_d.func_71218_a(this.field_147369_b.field_71093_bK);
        BlockPos blockposition = packetplayinblockdig.func_179715_a();

        this.field_147369_b.func_143004_u();
        switch (packetplayinblockdig.func_180762_c()) {
        case SWAP_HELD_ITEMS:
            if (!this.field_147369_b.func_175149_v()) {
                ItemStack itemstack = this.field_147369_b.func_184586_b(EnumHand.OFF_HAND);

                // CraftBukkit start
                PlayerSwapHandItemsEvent swapItemsEvent = new PlayerSwapHandItemsEvent(getPlayer(), CraftItemStack.asBukkitCopy(itemstack), CraftItemStack.asBukkitCopy(this.field_147369_b.func_184586_b(EnumHand.MAIN_HAND)));
                this.server.getPluginManager().callEvent(swapItemsEvent);
                if (swapItemsEvent.isCancelled()) {
                    return;
                }
                itemstack = CraftItemStack.asNMSCopy(swapItemsEvent.getMainHandItem());
                this.field_147369_b.func_184611_a(EnumHand.OFF_HAND, CraftItemStack.asNMSCopy(swapItemsEvent.getOffHandItem()));
                // CraftBukkit end
                this.field_147369_b.func_184611_a(EnumHand.MAIN_HAND, itemstack);
            }

            return;

        case DROP_ITEM:
            if (!this.field_147369_b.func_175149_v()) {
                // limit how quickly items can be dropped
                // If the ticks aren't the same then the count starts from 0 and we update the lastDropTick.
                if (this.lastDropTick != MinecraftServer.currentTick) {
                    this.dropCount = 0;
                    this.lastDropTick = MinecraftServer.currentTick;
                } else {
                    // Else we increment the drop count and check the amount.
                    this.dropCount++;
                    if (this.dropCount >= 20) {
                        field_147370_c.warn(this.field_147369_b.func_70005_c_() + " dropped their items too quickly!");
                        this.disconnect("You dropped your items too quickly (Hacking?)");
                        return;
                    }
                }
                // CraftBukkit end
                this.field_147369_b.func_71040_bB(false);
            }

            return;

        case DROP_ALL_ITEMS:
            if (!this.field_147369_b.func_175149_v()) {
                this.field_147369_b.func_71040_bB(true);
            }

            return;

        case RELEASE_USE_ITEM:
            this.field_147369_b.func_184597_cx();
            return;

        case START_DESTROY_BLOCK:
        case ABORT_DESTROY_BLOCK:
        case STOP_DESTROY_BLOCK:
            double d0 = this.field_147369_b.field_70165_t - ((double) blockposition.func_177958_n() + 0.5D);
            double d1 = this.field_147369_b.field_70163_u - ((double) blockposition.func_177956_o() + 0.5D) + 1.5D;
            double d2 = this.field_147369_b.field_70161_v - ((double) blockposition.func_177952_p() + 0.5D);
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > 36.0D) {
                this.func_147359_a(new SPacketBlockChange(worldserver, blockposition)); // Paper - Fix block break desync
                return;
            } else if (blockposition.func_177956_o() >= this.field_147367_d.func_71207_Z()) {
                return;
            } else {
                if (packetplayinblockdig.func_180762_c() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    if (!this.field_147367_d.func_175579_a(worldserver, blockposition, this.field_147369_b) && worldserver.func_175723_af().func_177746_a(blockposition)) {
                        this.field_147369_b.field_71134_c.func_180784_a(blockposition, packetplayinblockdig.func_179714_b());
                    } else {
                        // CraftBukkit start - fire PlayerInteractEvent
                        CraftEventFactory.callPlayerInteractEvent(this.field_147369_b, Action.LEFT_CLICK_BLOCK, blockposition, packetplayinblockdig.func_179714_b(), this.field_147369_b.field_71071_by.func_70448_g(), EnumHand.MAIN_HAND);
                        this.field_147369_b.field_71135_a.func_147359_a(new SPacketBlockChange(worldserver, blockposition));
                        // Update any tile entity data for this block
                        TileEntity tileentity = worldserver.func_175625_s(blockposition);
                        if (tileentity != null) {
                            this.field_147369_b.field_71135_a.func_147359_a(tileentity.func_189518_D_());
                        }
                        // CraftBukkit end
                    }
                } else {
                    if (packetplayinblockdig.func_180762_c() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.field_147369_b.field_71134_c.func_180785_a(blockposition);
                    } else if (packetplayinblockdig.func_180762_c() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                        this.field_147369_b.field_71134_c.func_180238_e();
                    }

                    if (worldserver.func_180495_p(blockposition).func_185904_a() != Material.field_151579_a) {
                        this.field_147369_b.field_71135_a.func_147359_a(new SPacketBlockChange(worldserver, blockposition));
                    }
                }

                return;
            }

        default:
            throw new IllegalArgumentException("Invalid player action");
        }
        // CraftBukkit end
    }

    // Spigot start - limit place/interactions
    private int limitedPackets;
    private long lastLimitedPacket = -1;
    private static final int THRESHOLD = com.destroystokyo.paper.PaperConfig.packetInSpamThreshold; // Paper - Configurable threshold

    private boolean checkLimit(long timestamp) {
        if (lastLimitedPacket != -1 && timestamp - lastLimitedPacket < THRESHOLD && limitedPackets++ >= 8) { // Paper - Use threshold, raise packet limit to 8
            return false;
        }

        if (lastLimitedPacket == -1 || timestamp - lastLimitedPacket >= THRESHOLD) { // Paper
            lastLimitedPacket = timestamp;
            limitedPackets = 0;
            return true;
        }

        return true;
    }
    // Spigot end

    public void func_184337_a(CPacketPlayerTryUseItemOnBlock packetplayinuseitem) {
        PacketThreadUtil.func_180031_a(packetplayinuseitem, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        if (!checkLimit(packetplayinuseitem.timestamp)) return; // Spigot - check limit
        WorldServer worldserver = this.field_147367_d.func_71218_a(this.field_147369_b.field_71093_bK);
        EnumHand enumhand = packetplayinuseitem.func_187022_c();
        ItemStack itemstack = this.field_147369_b.func_184586_b(enumhand);
        BlockPos blockposition = packetplayinuseitem.func_187023_a();
        EnumFacing enumdirection = packetplayinuseitem.func_187024_b();

        this.field_147369_b.func_143004_u();
        if (blockposition.func_177956_o() >= this.field_147367_d.func_71207_Z() - 1 && (enumdirection == EnumFacing.UP || blockposition.func_177956_o() >= this.field_147367_d.func_71207_Z())) {
            TextComponentTranslation chatmessage = new TextComponentTranslation("build.tooHigh", new Object[] { Integer.valueOf(this.field_147367_d.func_71207_Z())});

            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
            this.field_147369_b.field_71135_a.func_147359_a(new SPacketChat(chatmessage, ChatType.GAME_INFO));
        } else if (this.field_184362_y == null && this.field_147369_b.func_70092_e((double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D) < 64.0D && !this.field_147367_d.func_175579_a(worldserver, blockposition, this.field_147369_b) && worldserver.func_175723_af().func_177746_a(blockposition)) {
            // CraftBukkit start - Check if we can actually do something over this large a distance
            Location eyeLoc = this.getPlayer().getEyeLocation();
            double reachDistance = NumberConversions.square(eyeLoc.getX() - blockposition.func_177958_n()) + NumberConversions.square(eyeLoc.getY() - blockposition.func_177956_o()) + NumberConversions.square(eyeLoc.getZ() - blockposition.func_177952_p());
            if (reachDistance > (this.getPlayer().getGameMode() == org.bukkit.GameMode.CREATIVE ? CREATIVE_PLACE_DISTANCE_SQUARED : SURVIVAL_PLACE_DISTANCE_SQUARED)) {
                return;
            }
            // CraftBukkit end
            this.field_147369_b.field_71134_c.func_187251_a(this.field_147369_b, worldserver, itemstack, enumhand, blockposition, enumdirection, packetplayinuseitem.func_187026_d(), packetplayinuseitem.func_187025_e(), packetplayinuseitem.func_187020_f());
        }

        this.field_147369_b.field_71135_a.func_147359_a(new SPacketBlockChange(worldserver, blockposition));
        this.field_147369_b.field_71135_a.func_147359_a(new SPacketBlockChange(worldserver, blockposition.func_177972_a(enumdirection)));
    }

    public void func_147346_a(CPacketPlayerTryUseItem packetplayinblockplace) {
        PacketThreadUtil.func_180031_a(packetplayinblockplace, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        if (!checkLimit(packetplayinblockplace.timestamp)) return; // Spigot - check limit
        WorldServer worldserver = this.field_147367_d.func_71218_a(this.field_147369_b.field_71093_bK);
        EnumHand enumhand = packetplayinblockplace.func_187028_a();
        ItemStack itemstack = this.field_147369_b.func_184586_b(enumhand);

        this.field_147369_b.func_143004_u();
        if (!itemstack.func_190926_b()) {
            // CraftBukkit start
            // Raytrace to look for 'rogue armswings'
            float f1 = this.field_147369_b.field_70125_A;
            float f2 = this.field_147369_b.field_70177_z;
            double d0 = this.field_147369_b.field_70165_t;
            double d1 = this.field_147369_b.field_70163_u + (double) this.field_147369_b.func_70047_e();
            double d2 = this.field_147369_b.field_70161_v;
            Vec3d vec3d = new Vec3d(d0, d1, d2);

            float f3 = MathHelper.func_76134_b(-f2 * 0.017453292F - 3.1415927F);
            float f4 = MathHelper.func_76126_a(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.func_76134_b(-f1 * 0.017453292F);
            float f6 = MathHelper.func_76126_a(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            double d3 = field_147369_b.field_71134_c.func_73081_b()== GameType.CREATIVE ? 5.0D : 4.5D;
            Vec3d vec3d1 = vec3d.func_72441_c((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
            RayTraceResult movingobjectposition = this.field_147369_b.field_70170_p.func_72901_a(vec3d, vec3d1, false);

            boolean cancelled;
            if (movingobjectposition == null || movingobjectposition.field_72313_a != RayTraceResult.Type.BLOCK) {
                org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.field_147369_b, Action.RIGHT_CLICK_AIR, itemstack, enumhand);
                cancelled = event.useItemInHand() == Event.Result.DENY;
            } else {
                if (field_147369_b.field_71134_c.firedInteract) {
                    field_147369_b.field_71134_c.firedInteract = false;
                    cancelled = field_147369_b.field_71134_c.interactResult;
                } else {
                    org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(field_147369_b, Action.RIGHT_CLICK_BLOCK, movingobjectposition.func_178782_a(), movingobjectposition.field_178784_b, itemstack, true, enumhand);
                    cancelled = event.useItemInHand() == Event.Result.DENY;
                }
            }

            if (cancelled) {
                this.field_147369_b.getBukkitEntity().updateInventory(); // SPIGOT-2524
            } else {
                this.field_147369_b.field_71134_c.func_187250_a(this.field_147369_b, worldserver, itemstack, enumhand);
            }
            // CraftBukkit end
        }
    }

    public void func_175088_a(CPacketSpectate packetplayinspectate) {
        PacketThreadUtil.func_180031_a(packetplayinspectate, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_175149_v()) {
            Entity entity = null;
            WorldServer[] aworldserver = this.field_147367_d.field_71305_c;
            int i = aworldserver.length;

            // CraftBukkit - use the worlds array list
            for (WorldServer worldserver : field_147367_d.worlds) {

                if (worldserver != null) {
                    entity = packetplayinspectate.func_179727_a(worldserver);
                    if (entity != null) {
                        break;
                    }
                }
            }

            if (entity != null) {
                this.field_147369_b.func_175399_e(this.field_147369_b);
                this.field_147369_b.func_184210_p();

                /* CraftBukkit start - replace with bukkit handling for multi-world
                if (entity.world == this.player.world) {
                    this.player.enderTeleportTo(entity.locX, entity.locY, entity.locZ);
                } else {
                    WorldServer worldserver1 = this.player.x();
                    WorldServer worldserver2 = (WorldServer) entity.world;

                    this.player.dimension = entity.dimension;
                    this.sendPacket(new PacketPlayOutRespawn(this.player.dimension, worldserver1.getDifficulty(), worldserver1.getWorldData().getType(), this.player.playerInteractManager.getGameMode()));
                    this.minecraftServer.getPlayerList().f(this.player);
                    worldserver1.removeEntity(this.player);
                    this.player.dead = false;
                    this.player.setPositionRotation(entity.locX, entity.locY, entity.locZ, entity.yaw, entity.pitch);
                    if (this.player.isAlive()) {
                        worldserver1.entityJoinedWorld(this.player, false);
                        worldserver2.addEntity(this.player);
                        worldserver2.entityJoinedWorld(this.player, false);
                    }

                    this.player.spawnIn(worldserver2);
                    this.minecraftServer.getPlayerList().a(this.player, worldserver1);
                    this.player.enderTeleportTo(entity.locX, entity.locY, entity.locZ);
                    this.player.playerInteractManager.a(worldserver2);
                    this.minecraftServer.getPlayerList().b(this.player, worldserver2);
                    this.minecraftServer.getPlayerList().updateClient(this.player);
                }
                */
                this.field_147369_b.getBukkitEntity().teleport(entity.getBukkitEntity(), PlayerTeleportEvent.TeleportCause.SPECTATE);
                // CraftBukkit end
            }
        }

    }

    // CraftBukkit start
    public void func_175086_a(CPacketResourcePackStatus packetplayinresourcepackstatus) {
        PacketThreadUtil.func_180031_a(packetplayinresourcepackstatus, this, this.field_147369_b.func_71121_q());
        // Paper start
        //this.server.getPluginManager().callEvent(new PlayerResourcePackStatusEvent(getPlayer(), PlayerResourcePackStatusEvent.Status.values()[packetplayinresourcepackstatus.status.ordinal()]));
        final PlayerResourcePackStatusEvent.Status status = PlayerResourcePackStatusEvent.Status.values()[packetplayinresourcepackstatus.field_179719_b.ordinal()];
        this.getPlayer().setResourcePackStatus(status);
        this.server.getPluginManager().callEvent(new PlayerResourcePackStatusEvent(getPlayer(), status));
        // Paper end
    }
    // CraftBukkit end

    public void func_184340_a(CPacketSteerBoat packetplayinboatmove) {
        PacketThreadUtil.func_180031_a(packetplayinboatmove, this, this.field_147369_b.func_71121_q());
        Entity entity = this.field_147369_b.func_184187_bx();

        if (entity instanceof EntityBoat) {
            ((EntityBoat) entity).func_184445_a(packetplayinboatmove.func_187012_a(), packetplayinboatmove.func_187014_b());
        }

    }

    public void func_147231_a(ITextComponent ichatbasecomponent) {
        // CraftBukkit start - Rarely it would send a disconnect line twice
        if (this.processedDisconnect) {
            return;
        } else {
            this.processedDisconnect = true;
        }
        // CraftBukkit end
        NetHandlerPlayServer.field_147370_c.info("{} lost connection: {}", this.field_147369_b.func_70005_c_(), ichatbasecomponent.func_150260_c());
        // CraftBukkit start - Replace vanilla quit message handling with our own.
        /*
        this.minecraftServer.aD();
        ChatMessage chatmessage = new ChatMessage("multiplayer.player.left", new Object[] { this.player.getScoreboardDisplayName()});

        chatmessage.getChatModifier().setColor(EnumChatFormat.YELLOW);
        this.minecraftServer.getPlayerList().sendMessage(chatmessage);
        */

        this.field_147369_b.func_71123_m();
        String quitMessage = this.field_147367_d.func_184103_al().disconnect(this.field_147369_b);
        if ((quitMessage != null) && (quitMessage.length() > 0)) {
            this.field_147367_d.func_184103_al().sendMessage(CraftChatMessage.fromString(quitMessage));
        }
        // CraftBukkit end
        if (this.field_147367_d.func_71264_H() && this.field_147369_b.func_70005_c_().equals(this.field_147367_d.func_71214_G())) {
            NetHandlerPlayServer.field_147370_c.info("Stopping singleplayer server as player logged out");
            this.field_147367_d.func_71263_m();
        }

    }

    public void func_147359_a(final Packet<?> packet) {
        if (packet instanceof SPacketChat) {
            SPacketChat packetplayoutchat = (SPacketChat) packet;
            EntityPlayer.EnumChatVisibility entityhuman_enumchatvisibility = this.field_147369_b.func_147096_v();

            if (entityhuman_enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN && packetplayoutchat.func_192590_c() != ChatType.GAME_INFO) {
                return;
            }

            if (entityhuman_enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !packetplayoutchat.func_148916_d()) {
                return;
            }
        }

        // CraftBukkit start
        if (packet == null || this.processedDisconnect) { // Spigot
            return;
        } else if (packet instanceof SPacketSpawnPosition) {
            SPacketSpawnPosition packet6 = (SPacketSpawnPosition) packet;
            this.field_147369_b.compassTarget = new Location(this.getPlayer().getWorld(), packet6.field_179801_a.func_177958_n(), packet6.field_179801_a.func_177956_o(), packet6.field_179801_a.func_177952_p());
        }
        // CraftBukkit end

        try {
            this.field_147371_a.func_179290_a(packet);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Sending packet");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Packet being sent");

            crashreportsystemdetails.func_189529_a("Packet class", new ICrashReportDetail() {
                public String a() throws Exception {
                    return packet.getClass().getCanonicalName();
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    public void func_147355_a(CPacketHeldItemChange packetplayinhelditemslot) {
        PacketThreadUtil.func_180031_a(packetplayinhelditemslot, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        if (packetplayinhelditemslot.func_149614_c() >= 0 && packetplayinhelditemslot.func_149614_c() < InventoryPlayer.func_70451_h()) {
            PlayerItemHeldEvent event = new PlayerItemHeldEvent(this.getPlayer(), this.field_147369_b.field_71071_by.field_70461_c, packetplayinhelditemslot.func_149614_c());
            this.server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                this.func_147359_a(new SPacketHeldItemChange(this.field_147369_b.field_71071_by.field_70461_c));
                this.field_147369_b.func_143004_u();
                return;
            }
            // CraftBukkit end
            this.field_147369_b.field_71071_by.field_70461_c = packetplayinhelditemslot.func_149614_c();
            this.field_147369_b.func_143004_u();
        } else {
            NetHandlerPlayServer.field_147370_c.warn("{} tried to set an invalid carried item", this.field_147369_b.func_70005_c_());
            this.disconnect("Invalid hotbar selection (Hacking?)"); // CraftBukkit //Spigot "Nope" -> Descriptive reason
        }
    }

    public void func_147354_a(CPacketChatMessage packetplayinchat) {
        // CraftBukkit start - async chat
        // SPIGOT-3638
        if (this.field_147367_d.func_71241_aa()) {
            return;
        }

        boolean isSync = packetplayinchat.func_149439_c().startsWith("/");
        if (packetplayinchat.func_149439_c().startsWith("/")) {
            PacketThreadUtil.func_180031_a(packetplayinchat, this, this.field_147369_b.func_71121_q());
        }
        // CraftBukkit end
        if (this.field_147369_b.field_70128_L || this.field_147369_b.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN) { // CraftBukkit - dead men tell no tales
            TextComponentTranslation chatmessage = new TextComponentTranslation("chat.cannotSend", new Object[0]);

            chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
            this.func_147359_a(new SPacketChat(chatmessage));
        } else {
            this.field_147369_b.func_143004_u();
            String s = packetplayinchat.func_149439_c();

            s = StringUtils.normalizeSpace(s);

            for (int i = 0; i < s.length(); ++i) {
                if (!ChatAllowedCharacters.func_71566_a(s.charAt(i))) {
                    // CraftBukkit start - threadsafety
                    if (!isSync) {
                        Waitable waitable = new Waitable() {
                            @Override
                            protected Object evaluate() {
                                NetHandlerPlayServer.this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.illegal_characters", new Object[0]));
                                return null;
                            }
                        };

                        this.field_147367_d.processQueue.add(waitable);

                        try {
                            waitable.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.illegal_characters", new Object[0]));
                    }
                    // CraftBukkit end
                    return;
                }
            }

            // CraftBukkit start
            if (isSync) {
                try {
                    this.field_147367_d.server.playerCommandState = true;
                    this.func_147361_d(s);
                } finally {
                    this.field_147367_d.server.playerCommandState = false;
                }
            } else if (s.isEmpty()) {
                field_147370_c.warn(this.field_147369_b.func_70005_c_() + " tried to send an empty message");
            } else if (getPlayer().isConversing()) {
                // Spigot start
                final String message = s;
                this.field_147367_d.processQueue.add( new Waitable()
                {
                    @Override
                    protected Object evaluate()
                    {
                        getPlayer().acceptConversationInput( message );
                        return null;
                    }
                } );
                // Spigot end
            } else if (this.field_147369_b.func_147096_v() == EntityPlayer.EnumChatVisibility.SYSTEM) { // Re-add "Command Only" flag check
                TextComponentTranslation chatmessage = new TextComponentTranslation("chat.cannotSend", new Object[0]);

                chatmessage.func_150256_b().func_150238_a(TextFormatting.RED);
                this.func_147359_a(new SPacketChat(chatmessage));
            } else if (true) {
                this.chat(s, true);
                // CraftBukkit end - the below is for reference. :)
            } else {
                TextComponentTranslation chatmessage1 = new TextComponentTranslation("chat.type.text", new Object[] { this.field_147369_b.func_145748_c_(), s});

                this.field_147367_d.func_184103_al().func_148544_a(chatmessage1, false);
            }

            // Spigot start - spam exclusions
            boolean counted = true;
            for ( String exclude : org.spigotmc.SpigotConfig.spamExclusions )
            {
                if ( exclude != null && s.startsWith( exclude ) )
                {
                    counted = false;
                    break;
                }
            }
            // Spigot end
            // CraftBukkit start - replaced with thread safe throttle
            // this.chatThrottle += 20;
            if (counted && chatSpamField.addAndGet(this, 20) > 200 && !this.field_147367_d.func_184103_al().func_152596_g(this.field_147369_b.func_146103_bH())) { // Spigot
                if (!isSync) {
                    Waitable waitable = new Waitable() {
                        @Override
                        protected Object evaluate() {
                            NetHandlerPlayServer.this.func_194028_b(new TextComponentTranslation("disconnect.spam", new Object[0]));
                            return null;
                        }
                    };

                    this.field_147367_d.processQueue.add(waitable);

                    try {
                        waitable.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.func_194028_b(new TextComponentTranslation("disconnect.spam", new Object[0]));
                }
                // CraftBukkit end
            }

        }
    }

    // CraftBukkit start - add method
    public void chat(String s, boolean async) {
        if (s.isEmpty() || this.field_147369_b.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            return;
        }

        if (!async && s.startsWith("/")) {
            // Paper Start
            if (!org.spigotmc.AsyncCatcher.shuttingDown && !org.bukkit.Bukkit.isPrimaryThread()) {
                final String fCommandLine = s;
                MinecraftServer.field_147145_h.log(org.apache.logging.log4j.Level.ERROR, "Command Dispatched Async: " + fCommandLine);
                MinecraftServer.field_147145_h.log(org.apache.logging.log4j.Level.ERROR, "Please notify author of plugin causing this execution to fix this bug! see: http://bit.ly/1oSiM6C", new Throwable());
                Waitable wait = new Waitable() {
                    @Override
                    protected Object evaluate() {
                        chat(fCommandLine, false);
                        return null;
                    }
                };
                field_147367_d.processQueue.add(wait);
                try {
                    wait.get();
                    return;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // This is proper habit for java. If we aren't handling it, pass it on!
                } catch (Exception e) {
                    throw new RuntimeException("Exception processing chat command", e.getCause());
                }
            }
            // Paper End
            this.func_147361_d(s);
        } else if (this.field_147369_b.func_147096_v() == EntityPlayer.EnumChatVisibility.SYSTEM) {
            // Do nothing, this is coming from a plugin
        } else {
            Player player = this.getPlayer();
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet(field_147367_d));
            this.server.getPluginManager().callEvent(event);

            if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
                // Evil plugins still listening to deprecated event
                final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
                queueEvent.setCancelled(event.isCancelled());
                Waitable waitable = new Waitable() {
                    @Override
                    protected Object evaluate() {
                        org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

                        if (queueEvent.isCancelled()) {
                            return null;
                        }

                        String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
                        NetHandlerPlayServer.this.field_147367_d.console.sendMessage(message);
                        if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                            for (Object player : NetHandlerPlayServer.this.field_147367_d.func_184103_al().field_72404_b) {
                                ((EntityPlayerMP) player).sendMessage(CraftChatMessage.fromString(message));
                            }
                        } else {
                            for (Player player : queueEvent.getRecipients()) {
                                player.sendMessage(message);
                            }
                        }
                        return null;
                    }};
                if (async) {
                    field_147367_d.processQueue.add(waitable);
                } else {
                    waitable.run();
                }
                try {
                    waitable.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // This is proper habit for java. If we aren't handling it, pass it on!
                } catch (ExecutionException e) {
                    throw new RuntimeException("Exception processing chat event", e.getCause());
                }
            } else {
                if (event.isCancelled()) {
                    return;
                }

                // Paper Start - (Meh) Support for vanilla world scoreboard name coloring
                String displayName = event.getPlayer().getDisplayName();
                if (this.field_147369_b.func_130014_f_().paperConfig.useVanillaScoreboardColoring) {
                    displayName = ScorePlayerTeam.func_96667_a(this.field_147369_b.getTeam(), player.getDisplayName());
                }

                s = String.format(event.getFormat(), displayName, event.getMessage());
                // Paper end
                field_147367_d.console.sendMessage(s);
                if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
                    for (Object recipient : field_147367_d.func_184103_al().field_72404_b) {
                        ((EntityPlayerMP) recipient).sendMessage(CraftChatMessage.fromString(s));
                    }
                } else {
                    for (Player recipient : event.getRecipients()) {
                        recipient.sendMessage(s);
                    }
                }
            }
        }
    }
    // CraftBukkit end

    private void func_147361_d(String s) {
        MinecraftTimings.playerCommandTimer.startTiming(); // Paper
       // CraftBukkit start - whole method
        if ( org.spigotmc.SpigotConfig.logCommands ) // Spigot
        this.field_147370_c.info(this.field_147369_b.func_70005_c_() + " issued server command: " + s);

        CraftPlayer player = this.getPlayer();

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet(field_147367_d));
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            MinecraftTimings.playerCommandTimer.stopTiming(); // Paper
            return;
        }

        try {
            if (this.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
                MinecraftTimings.playerCommandTimer.stopTiming(); // Paper
                return;
            }
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(NetHandlerPlayServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            MinecraftTimings.playerCommandTimer.stopTiming(); // Paper
            return;
        }
        MinecraftTimings.playerCommandTimer.stopTiming(); // Paper
        // this.minecraftServer.getCommandHandler().a(this.player, s);
        // CraftBukkit end
    }

    public void func_175087_a(CPacketAnimation packetplayinarmanimation) {
        PacketThreadUtil.func_180031_a(packetplayinarmanimation, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        this.field_147369_b.func_143004_u();
        // CraftBukkit start - Raytrace to look for 'rogue armswings'
        float f1 = this.field_147369_b.field_70125_A;
        float f2 = this.field_147369_b.field_70177_z;
        double d0 = this.field_147369_b.field_70165_t;
        double d1 = this.field_147369_b.field_70163_u + (double) this.field_147369_b.func_70047_e();
        double d2 = this.field_147369_b.field_70161_v;
        Vec3d vec3d = new Vec3d(d0, d1, d2);

        float f3 = MathHelper.func_76134_b(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.func_76126_a(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.func_76134_b(-f1 * 0.017453292F);
        float f6 = MathHelper.func_76126_a(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = field_147369_b.field_71134_c.func_73081_b()== GameType.CREATIVE ? 5.0D : 4.5D;
        Vec3d vec3d1 = vec3d.func_72441_c((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        RayTraceResult movingobjectposition = this.field_147369_b.field_70170_p.func_72901_a(vec3d, vec3d1, false);

        if (movingobjectposition == null || movingobjectposition.field_72313_a != RayTraceResult.Type.BLOCK) {
            CraftEventFactory.callPlayerInteractEvent(this.field_147369_b, Action.LEFT_CLICK_AIR, this.field_147369_b.field_71071_by.func_70448_g(), EnumHand.MAIN_HAND);
        }

        // Arm swing animation
        PlayerAnimationEvent event = new PlayerAnimationEvent(this.getPlayer());
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        // CraftBukkit end
        this.field_147369_b.func_184609_a(packetplayinarmanimation.func_187018_a());
    }

    public void func_147357_a(CPacketEntityAction packetplayinentityaction) {
        PacketThreadUtil.func_180031_a(packetplayinentityaction, this, this.field_147369_b.func_71121_q());
        // CraftBukkit start
        if (this.field_147369_b.field_70128_L) return;
        switch (packetplayinentityaction.func_180764_b()) {
            case START_SNEAKING:
            case STOP_SNEAKING:
                PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(this.getPlayer(), packetplayinentityaction.func_180764_b() == CPacketEntityAction.Action.START_SNEAKING);
                this.server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                break;
            case START_SPRINTING:
            case STOP_SPRINTING:
                PlayerToggleSprintEvent e2 = new PlayerToggleSprintEvent(this.getPlayer(), packetplayinentityaction.func_180764_b() == CPacketEntityAction.Action.START_SPRINTING);
                this.server.getPluginManager().callEvent(e2);

                if (e2.isCancelled()) {
                    return;
                }
                break;
        }
        // CraftBukkit end
        this.field_147369_b.func_143004_u();
        IJumpingMount ijumpable;

        switch (packetplayinentityaction.func_180764_b()) {
        case START_SNEAKING:
            this.field_147369_b.func_70095_a(true);

            // Paper start - Hang on!
            if (this.field_147369_b.field_70170_p.paperConfig.parrotsHangOnBetter) {
                this.field_147369_b.func_192030_dh();
            }
            // Paper end

            break;

        case STOP_SNEAKING:
            this.field_147369_b.func_70095_a(false);
            break;

        case START_SPRINTING:
            this.field_147369_b.func_70031_b(true);
            break;

        case STOP_SPRINTING:
            this.field_147369_b.func_70031_b(false);
            break;

        case STOP_SLEEPING:
            if (this.field_147369_b.func_70608_bn()) {
                this.field_147369_b.func_70999_a(false, true, true);
                this.field_184362_y = new Vec3d(this.field_147369_b.field_70165_t, this.field_147369_b.field_70163_u, this.field_147369_b.field_70161_v);
            }
            break;

        case START_RIDING_JUMP:
            if (this.field_147369_b.func_184187_bx() instanceof IJumpingMount) {
                ijumpable = (IJumpingMount) this.field_147369_b.func_184187_bx();
                int i = packetplayinentityaction.func_149512_e();

                if (ijumpable.func_184776_b() && i > 0) {
                    ijumpable.func_184775_b(i);
                }
            }
            break;

        case STOP_RIDING_JUMP:
            if (this.field_147369_b.func_184187_bx() instanceof IJumpingMount) {
                ijumpable = (IJumpingMount) this.field_147369_b.func_184187_bx();
                ijumpable.func_184777_r_();
            }
            break;

        case OPEN_INVENTORY:
            if (this.field_147369_b.func_184187_bx() instanceof AbstractHorse) {
                ((AbstractHorse) this.field_147369_b.func_184187_bx()).func_110199_f((EntityPlayer) this.field_147369_b);
            }
            break;

        case START_FALL_FLYING:
            if (!this.field_147369_b.field_70122_E && this.field_147369_b.field_70181_x < 0.0D && !this.field_147369_b.func_184613_cA() && !this.field_147369_b.func_70090_H()) {
                ItemStack itemstack = this.field_147369_b.func_184582_a(EntityEquipmentSlot.CHEST);

                if (itemstack.func_77973_b() == Items.field_185160_cR && ItemElytra.func_185069_d(itemstack)) {
                    this.field_147369_b.func_184847_M();
                }
            } else {
                this.field_147369_b.func_189103_N();
            }
            break;

        default:
            throw new IllegalArgumentException("Invalid client command!");
        }

    }

    public void func_147340_a(CPacketUseEntity packetplayinuseentity) {
        PacketThreadUtil.func_180031_a(packetplayinuseentity, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        WorldServer worldserver = this.field_147367_d.func_71218_a(this.field_147369_b.field_71093_bK);
        Entity entity = packetplayinuseentity.func_149564_a((World) worldserver);
        // Spigot Start
        if ( entity == field_147369_b && !field_147369_b.func_175149_v() )
        {
            disconnect( "Cannot interact with self!" );
            return;
        }
        // Spigot End

        this.field_147369_b.func_143004_u();
        if (entity != null) {
            boolean flag = this.field_147369_b.func_70685_l(entity);
            double d0 = 36.0D;

            if (!flag) {
                d0 = 9.0D;
            }

            if (this.field_147369_b.func_70068_e(entity) < d0) {
                EnumHand enumhand;

                ItemStack itemInHand = this.field_147369_b.func_184586_b(packetplayinuseentity.func_186994_b() == null ? EnumHand.MAIN_HAND : packetplayinuseentity.func_186994_b()); // CraftBukkit

                if (packetplayinuseentity.func_149565_c() == CPacketUseEntity.Action.INTERACT
                        || packetplayinuseentity.func_149565_c() == CPacketUseEntity.Action.INTERACT_AT) {
                    // CraftBukkit start
                    boolean triggerLeashUpdate = itemInHand != null && itemInHand.func_77973_b() == Items.field_151058_ca && entity instanceof EntityLiving;
                    Item origItem = this.field_147369_b.field_71071_by.func_70448_g() == null ? null : this.field_147369_b.field_71071_by.func_70448_g().func_77973_b();
                    PlayerInteractEntityEvent event;
                    if (packetplayinuseentity.func_149565_c() == CPacketUseEntity.Action.INTERACT) {
                        event = new PlayerInteractEntityEvent((Player) this.getPlayer(), entity.getBukkitEntity(), (packetplayinuseentity.func_186994_b() == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
                    } else {
                        Vec3d target = packetplayinuseentity.func_179712_b();
                        event = new PlayerInteractAtEntityEvent((Player) this.getPlayer(), entity.getBukkitEntity(), new org.bukkit.util.Vector(target.field_72450_a, target.field_72448_b, target.field_72449_c), (packetplayinuseentity.func_186994_b() == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
                    }
                    this.server.getPluginManager().callEvent(event);

                    if (triggerLeashUpdate && (event.isCancelled() || this.field_147369_b.field_71071_by.func_70448_g() == null || this.field_147369_b.field_71071_by.func_70448_g().func_77973_b() != Items.field_151058_ca)) {
                        // Refresh the current leash state
                        this.func_147359_a(new SPacketEntityAttach(entity, ((EntityLiving) entity).func_110166_bE()));
                    }

                    if (event.isCancelled() || this.field_147369_b.field_71071_by.func_70448_g() == null || this.field_147369_b.field_71071_by.func_70448_g().func_77973_b() != origItem) {
                        // Refresh the current entity metadata
                        this.func_147359_a(new SPacketEntityMetadata(entity.func_145782_y(), entity.field_70180_af, true));
                    }

                    if (event.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                }

                if (packetplayinuseentity.func_149565_c() == CPacketUseEntity.Action.INTERACT) {
                    enumhand = packetplayinuseentity.func_186994_b();
                    this.field_147369_b.func_190775_a(entity, enumhand);
                    // CraftBukkit start
                    if (!itemInHand.func_190926_b() && itemInHand.func_190916_E() <= -1) {
                        this.field_147369_b.func_71120_a(this.field_147369_b.field_71070_bA);
                    }
                    // CraftBukkit end
                } else if (packetplayinuseentity.func_149565_c() == CPacketUseEntity.Action.INTERACT_AT) {
                    enumhand = packetplayinuseentity.func_186994_b();
                    entity.func_184199_a(this.field_147369_b, packetplayinuseentity.func_179712_b(), enumhand);
                    // CraftBukkit start
                    if (!itemInHand.func_190926_b() && itemInHand.func_190916_E() <= -1) {
                        this.field_147369_b.func_71120_a(this.field_147369_b.field_71070_bA);
                    }
                    // CraftBukkit end
                } else if (packetplayinuseentity.func_149565_c() == CPacketUseEntity.Action.ATTACK) {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || (entity == this.field_147369_b && !field_147369_b.func_175149_v())) { // CraftBukkit
                        this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.invalid_entity_attacked", new Object[0]));
                        this.field_147367_d.func_71236_h("Player " + this.field_147369_b.func_70005_c_() + " tried to attack an invalid entity");
                        return;
                    }

                    this.field_147369_b.func_71059_n(entity);

                    // CraftBukkit start
                    if (!itemInHand.func_190926_b() && itemInHand.func_190916_E() <= -1) {
                        this.field_147369_b.func_71120_a(this.field_147369_b.field_71070_bA);
                    }
                    // CraftBukkit end
                }
            }
        }
        // Paper start - fire event
        else {
            this.server.getPluginManager().callEvent(new com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent(
                this.getPlayer(),
                packetplayinuseentity.getEntityId(),
                packetplayinuseentity.func_149565_c() == CPacketUseEntity.Action.ATTACK,
                packetplayinuseentity.func_186994_b() == EnumHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND
            ));
        }
        // Paper end

    }

    public void func_147342_a(CPacketClientStatus packetplayinclientcommand) {
        PacketThreadUtil.func_180031_a(packetplayinclientcommand, this, this.field_147369_b.func_71121_q());
        this.field_147369_b.func_143004_u();
        CPacketClientStatus.State packetplayinclientcommand_enumclientcommand = packetplayinclientcommand.func_149435_c();

        switch (packetplayinclientcommand_enumclientcommand) {
        case PERFORM_RESPAWN:
            if (this.field_147369_b.field_71136_j) {
                this.field_147369_b.field_71136_j = false;
                // this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, 0, true);
                this.field_147367_d.func_184103_al().changeDimension(this.field_147369_b, 0, PlayerTeleportEvent.TeleportCause.END_PORTAL); // CraftBukkit - reroute logic through custom portal management
                CriteriaTriggers.field_193134_u.func_193143_a(this.field_147369_b, DimensionType.THE_END, DimensionType.OVERWORLD);
            } else {
                if (this.field_147369_b.func_110143_aJ() > 0.0F) {
                    return;
                }

                this.field_147369_b = this.field_147367_d.func_184103_al().func_72368_a(this.field_147369_b, 0, false);
                if (this.field_147367_d.func_71199_h()) {
                    this.field_147369_b.func_71033_a(GameType.SPECTATOR);
                    this.field_147369_b.func_71121_q().func_82736_K().func_82764_b("spectatorsGenerateChunks", "false");
                }
            }
            break;

        case REQUEST_STATS:
            this.field_147369_b.func_147099_x().func_150876_a(this.field_147369_b);
        }

    }

    public void func_147356_a(CPacketCloseWindow packetplayinclosewindow) {
        PacketThreadUtil.func_180031_a(packetplayinclosewindow, this, this.field_147369_b.func_71121_q());

        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        CraftEventFactory.handleInventoryCloseEvent(this.field_147369_b); // CraftBukkit

        this.field_147369_b.func_71128_l();
    }

    public void func_147351_a(CPacketClickWindow packetplayinwindowclick) {
        PacketThreadUtil.func_180031_a(packetplayinwindowclick, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        this.field_147369_b.func_143004_u();
        if (this.field_147369_b.field_71070_bA.field_75152_c == packetplayinwindowclick.func_149548_c() && this.field_147369_b.field_71070_bA.func_75129_b(this.field_147369_b) && this.field_147369_b.field_71070_bA.func_75145_c(this.field_147369_b)) { // CraftBukkit
            boolean cancelled = this.field_147369_b.func_175149_v(); // CraftBukkit - see below if
            if (false/*this.player.isSpectator()*/) { // CraftBukkit
                NonNullList nonnulllist = NonNullList.func_191196_a();

                for (int i = 0; i < this.field_147369_b.field_71070_bA.field_75151_b.size(); ++i) {
                    nonnulllist.add(((Slot) this.field_147369_b.field_71070_bA.field_75151_b.get(i)).func_75211_c());
                }

                this.field_147369_b.func_71110_a(this.field_147369_b.field_71070_bA, nonnulllist);
            } else {
                // CraftBukkit start - Call InventoryClickEvent
                if (packetplayinwindowclick.func_149544_d() < -1 && packetplayinwindowclick.func_149544_d() != -999) {
                    return;
                }

                InventoryView inventory = this.field_147369_b.field_71070_bA.getBukkitView();
                SlotType type = CraftInventoryView.getSlotType(inventory, packetplayinwindowclick.func_149544_d());

                InventoryClickEvent event;
                ClickType click = ClickType.UNKNOWN;
                InventoryAction action = InventoryAction.UNKNOWN;

                ItemStack itemstack = ItemStack.field_190927_a;

                switch (packetplayinwindowclick.func_186993_f()) {
                    case PICKUP:
                        if (packetplayinwindowclick.func_149543_e() == 0) {
                            click = ClickType.LEFT;
                        } else if (packetplayinwindowclick.func_149543_e() == 1) {
                            click = ClickType.RIGHT;
                        }
                        if (packetplayinwindowclick.func_149543_e() == 0 || packetplayinwindowclick.func_149543_e() == 1) {
                            action = InventoryAction.NOTHING; // Don't want to repeat ourselves
                            if (packetplayinwindowclick.func_149544_d() == -999) {
                                if (!field_147369_b.field_71071_by.func_70445_o().func_190926_b()) {
                                    action = packetplayinwindowclick.func_149543_e() == 0 ? InventoryAction.DROP_ALL_CURSOR : InventoryAction.DROP_ONE_CURSOR;
                                }
                            } else if (packetplayinwindowclick.func_149544_d() < 0)  {
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.field_147369_b.field_71070_bA.func_75139_a(packetplayinwindowclick.func_149544_d());
                                if (slot != null) {
                                    ItemStack clickedItem = slot.func_75211_c();
                                    ItemStack cursor = field_147369_b.field_71071_by.func_70445_o();
                                    if (clickedItem.func_190926_b()) {
                                        if (!cursor.func_190926_b()) {
                                            action = packetplayinwindowclick.func_149543_e() == 0 ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_ONE;
                                        }
                                    } else if (slot.func_82869_a(field_147369_b)) {
                                        if (cursor.func_190926_b()) {
                                            action = packetplayinwindowclick.func_149543_e() == 0 ? InventoryAction.PICKUP_ALL : InventoryAction.PICKUP_HALF;
                                        } else if (slot.func_75214_a(cursor)) {
                                            if (clickedItem.func_77969_a(cursor) && ItemStack.func_77970_a(clickedItem, cursor)) {
                                                int toPlace = packetplayinwindowclick.func_149543_e() == 0 ? cursor.func_190916_E() : 1;
                                                toPlace = Math.min(toPlace, clickedItem.func_77976_d() - clickedItem.func_190916_E());
                                                toPlace = Math.min(toPlace, slot.field_75224_c.func_70297_j_() - clickedItem.func_190916_E());
                                                if (toPlace == 1) {
                                                    action = InventoryAction.PLACE_ONE;
                                                } else if (toPlace == cursor.func_190916_E()) {
                                                    action = InventoryAction.PLACE_ALL;
                                                } else if (toPlace < 0) {
                                                    action = toPlace != -1 ? InventoryAction.PICKUP_SOME : InventoryAction.PICKUP_ONE; // this happens with oversized stacks
                                                } else if (toPlace != 0) {
                                                    action = InventoryAction.PLACE_SOME;
                                                }
                                            } else if (cursor.func_190916_E() <= slot.func_75219_a()) {
                                                action = InventoryAction.SWAP_WITH_CURSOR;
                                            }
                                        } else if (cursor.func_77973_b() == clickedItem.func_77973_b() && (!cursor.func_77981_g() || cursor.func_77960_j() == clickedItem.func_77960_j()) && ItemStack.func_77970_a(cursor, clickedItem)) {
                                            if (clickedItem.func_190916_E() >= 0) {
                                                if (clickedItem.func_190916_E() + cursor.func_190916_E() <= cursor.func_77976_d()) {
                                                    // As of 1.5, this is result slots only
                                                    action = InventoryAction.PICKUP_ALL;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    // TODO check on updates
                    case QUICK_MOVE:
                        if (packetplayinwindowclick.func_149543_e() == 0) {
                            click = ClickType.SHIFT_LEFT;
                        } else if (packetplayinwindowclick.func_149543_e() == 1) {
                            click = ClickType.SHIFT_RIGHT;
                        }
                        if (packetplayinwindowclick.func_149543_e() == 0 || packetplayinwindowclick.func_149543_e() == 1) {
                            if (packetplayinwindowclick.func_149544_d() < 0) {
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.field_147369_b.field_71070_bA.func_75139_a(packetplayinwindowclick.func_149544_d());
                                if (slot != null && slot.func_82869_a(this.field_147369_b) && slot.func_75216_d()) {
                                    action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        }
                        break;
                    case SWAP:
                        if (packetplayinwindowclick.func_149543_e() >= 0 && packetplayinwindowclick.func_149543_e() < 9) {
                            click = ClickType.NUMBER_KEY;
                            Slot clickedSlot = this.field_147369_b.field_71070_bA.func_75139_a(packetplayinwindowclick.func_149544_d());
                            if (clickedSlot.func_82869_a(field_147369_b)) {
                                ItemStack hotbar = this.field_147369_b.field_71071_by.func_70301_a(packetplayinwindowclick.func_149543_e());
                                boolean canCleanSwap = hotbar.func_190926_b() || (clickedSlot.field_75224_c == field_147369_b.field_71071_by && clickedSlot.func_75214_a(hotbar)); // the slot will accept the hotbar item
                                if (clickedSlot.func_75216_d()) {
                                    if (canCleanSwap) {
                                        action = InventoryAction.HOTBAR_SWAP;
                                    } else {
                                        action = InventoryAction.HOTBAR_MOVE_AND_READD;
                                    }
                                } else if (!clickedSlot.func_75216_d() && !hotbar.func_190926_b() && clickedSlot.func_75214_a(hotbar)) {
                                    action = InventoryAction.HOTBAR_SWAP;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            } else {
                                action = InventoryAction.NOTHING;
                            }
                        }
                        break;
                    case CLONE:
                        if (packetplayinwindowclick.func_149543_e() == 2) {
                            click = ClickType.MIDDLE;
                            if (packetplayinwindowclick.func_149544_d() < 0) { // Paper - GH-404
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.field_147369_b.field_71070_bA.func_75139_a(packetplayinwindowclick.func_149544_d());
                                if (slot != null && slot.func_75216_d() && field_147369_b.field_71075_bZ.field_75098_d && field_147369_b.field_71071_by.func_70445_o().func_190926_b()) {
                                    action = InventoryAction.CLONE_STACK;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        } else {
                            click = ClickType.UNKNOWN;
                            action = InventoryAction.UNKNOWN;
                        }
                        break;
                    case THROW:
                        if (packetplayinwindowclick.func_149544_d() >= 0) {
                            if (packetplayinwindowclick.func_149543_e() == 0) {
                                click = ClickType.DROP;
                                Slot slot = this.field_147369_b.field_71070_bA.func_75139_a(packetplayinwindowclick.func_149544_d());
                                if (slot != null && slot.func_75216_d() && slot.func_82869_a(field_147369_b) && !slot.func_75211_c().func_190926_b() && slot.func_75211_c().func_77973_b() != Item.func_150898_a(Blocks.field_150350_a)) {
                                    action = InventoryAction.DROP_ONE_SLOT;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            } else if (packetplayinwindowclick.func_149543_e() == 1) {
                                click = ClickType.CONTROL_DROP;
                                Slot slot = this.field_147369_b.field_71070_bA.func_75139_a(packetplayinwindowclick.func_149544_d());
                                if (slot != null && slot.func_75216_d() && slot.func_82869_a(field_147369_b) && !slot.func_75211_c().func_190926_b() && slot.func_75211_c().func_77973_b() != Item.func_150898_a(Blocks.field_150350_a)) {
                                    action = InventoryAction.DROP_ALL_SLOT;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        } else {
                            // Sane default (because this happens when they are holding nothing. Don't ask why.)
                            click = ClickType.LEFT;
                            if (packetplayinwindowclick.func_149543_e() == 1) {
                                click = ClickType.RIGHT;
                            }
                            action = InventoryAction.NOTHING;
                        }
                        break;
                    case QUICK_CRAFT:
                        itemstack = this.field_147369_b.field_71070_bA.func_184996_a(packetplayinwindowclick.func_149544_d(), packetplayinwindowclick.func_149543_e(), packetplayinwindowclick.func_186993_f(), this.field_147369_b);
                        break;
                    case PICKUP_ALL:
                        click = ClickType.DOUBLE_CLICK;
                        action = InventoryAction.NOTHING;
                        if (packetplayinwindowclick.func_149544_d() >= 0 && !this.field_147369_b.field_71071_by.func_70445_o().func_190926_b()) {
                            ItemStack cursor = this.field_147369_b.field_71071_by.func_70445_o();
                            action = InventoryAction.NOTHING;
                            // Quick check for if we have any of the item
                            if (inventory.getTopInventory().contains(org.bukkit.Material.getMaterial(Item.func_150891_b(cursor.func_77973_b()))) || inventory.getBottomInventory().contains(org.bukkit.Material.getMaterial(Item.func_150891_b(cursor.func_77973_b())))) {
                                action = InventoryAction.COLLECT_TO_CURSOR;
                            }
                        }
                        break;
                    default:
                        break;
                }

                if (packetplayinwindowclick.func_186993_f() != ClickType.QUICK_CRAFT) {
                    if (click == ClickType.NUMBER_KEY) {
                        event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.func_149544_d(), click, action, packetplayinwindowclick.func_149543_e());
                    } else {
                        event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.func_149544_d(), click, action);
                    }

                    org.bukkit.inventory.Inventory top = inventory.getTopInventory();
                    if (packetplayinwindowclick.func_149544_d() == 0 && top instanceof CraftingInventory) {
                        org.bukkit.inventory.Recipe recipe = ((CraftingInventory) top).getRecipe();
                        if (recipe != null) {
                            if (click == ClickType.NUMBER_KEY) {
                                event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.func_149544_d(), click, action, packetplayinwindowclick.func_149543_e());
                            } else {
                                event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.func_149544_d(), click, action);
                            }
                        }
                    }

                    event.setCancelled(cancelled);
                    Container oldContainer = this.field_147369_b.field_71070_bA; // SPIGOT-1224
                    server.getPluginManager().callEvent(event);
                    if (this.field_147369_b.field_71070_bA != oldContainer) {
                        return;
                    }

                    switch (event.getResult()) {
                        case ALLOW:
                        case DEFAULT:
                            itemstack = this.field_147369_b.field_71070_bA.func_184996_a(packetplayinwindowclick.func_149544_d(), packetplayinwindowclick.func_149543_e(), packetplayinwindowclick.func_186993_f(), this.field_147369_b);
                            break;
                        case DENY:
                            /* Needs enum constructor in InventoryAction
                            if (action.modifiesOtherSlots()) {

                            } else {
                                if (action.modifiesCursor()) {
                                    this.player.playerConnection.sendPacket(new Packet103SetSlot(-1, -1, this.player.inventory.getCarried()));
                                }
                                if (action.modifiesClicked()) {
                                    this.player.playerConnection.sendPacket(new Packet103SetSlot(this.player.activeContainer.windowId, packet102windowclick.slot, this.player.activeContainer.getSlot(packet102windowclick.slot).getItem()));
                                }
                            }*/
                            switch (action) {
                                // Modified other slots
                                case PICKUP_ALL:
                                case MOVE_TO_OTHER_INVENTORY:
                                case HOTBAR_MOVE_AND_READD:
                                case HOTBAR_SWAP:
                                case COLLECT_TO_CURSOR:
                                case UNKNOWN:
                                    this.field_147369_b.func_71120_a(this.field_147369_b.field_71070_bA);
                                    break;
                                // Modified cursor and clicked
                                case PICKUP_SOME:
                                case PICKUP_HALF:
                                case PICKUP_ONE:
                                case PLACE_ALL:
                                case PLACE_SOME:
                                case PLACE_ONE:
                                case SWAP_WITH_CURSOR:
                                    this.field_147369_b.field_71135_a.func_147359_a(new SPacketSetSlot(-1, -1, this.field_147369_b.field_71071_by.func_70445_o()));
                                    this.field_147369_b.field_71135_a.func_147359_a(new SPacketSetSlot(this.field_147369_b.field_71070_bA.field_75152_c, packetplayinwindowclick.func_149544_d(), this.field_147369_b.field_71070_bA.func_75139_a(packetplayinwindowclick.func_149544_d()).func_75211_c()));
                                    break;
                                // Modified clicked only
                                case DROP_ALL_SLOT:
                                case DROP_ONE_SLOT:
                                    this.field_147369_b.field_71135_a.func_147359_a(new SPacketSetSlot(this.field_147369_b.field_71070_bA.field_75152_c, packetplayinwindowclick.func_149544_d(), this.field_147369_b.field_71070_bA.func_75139_a(packetplayinwindowclick.func_149544_d()).func_75211_c()));
                                    break;
                                // Modified cursor only
                                case DROP_ALL_CURSOR:
                                case DROP_ONE_CURSOR:
                                case CLONE_STACK:
                                    this.field_147369_b.field_71135_a.func_147359_a(new SPacketSetSlot(-1, -1, this.field_147369_b.field_71071_by.func_70445_o()));
                                    break;
                                // Nothing
                                case NOTHING:
                                    break;
                            }
                            return;
                    }

                    if (event instanceof CraftItemEvent) {
                        // Need to update the inventory on crafting to
                        // correctly support custom recipes
                        field_147369_b.func_71120_a(field_147369_b.field_71070_bA);
                    }
                }
                // CraftBukkit end
                if (ItemStack.func_77989_b(packetplayinwindowclick.func_149546_g(), itemstack)) {
                    this.field_147369_b.field_71135_a.func_147359_a(new SPacketConfirmTransaction(packetplayinwindowclick.func_149548_c(), packetplayinwindowclick.func_149547_f(), true));
                    this.field_147369_b.field_71137_h = true;
                    this.field_147369_b.field_71070_bA.func_75142_b();
                    this.field_147369_b.func_71113_k();
                    this.field_147369_b.field_71137_h = false;
                } else {
                    this.field_147372_n.func_76038_a(this.field_147369_b.field_71070_bA.field_75152_c, Short.valueOf(packetplayinwindowclick.func_149547_f()));
                    this.field_147369_b.field_71135_a.func_147359_a(new SPacketConfirmTransaction(packetplayinwindowclick.func_149548_c(), packetplayinwindowclick.func_149547_f(), false));
                    this.field_147369_b.field_71070_bA.func_75128_a(this.field_147369_b, false);
                    NonNullList nonnulllist1 = NonNullList.func_191196_a();

                    for (int j = 0; j < this.field_147369_b.field_71070_bA.field_75151_b.size(); ++j) {
                        ItemStack itemstack1 = ((Slot) this.field_147369_b.field_71070_bA.field_75151_b.get(j)).func_75211_c();
                        ItemStack itemstack2 = itemstack1.func_190926_b() ? ItemStack.field_190927_a : itemstack1;

                        nonnulllist1.add(itemstack2);
                    }

                    this.field_147369_b.func_71110_a(this.field_147369_b.field_71070_bA, nonnulllist1);
                }
            }
        }

    }

    public void func_194308_a(CPacketPlaceRecipe packetplayinautorecipe) {
        PacketThreadUtil.func_180031_a(packetplayinautorecipe, this, this.field_147369_b.func_71121_q());
        this.field_147369_b.func_143004_u();
        if (!this.field_147369_b.func_175149_v() && this.field_147369_b.field_71070_bA.field_75152_c == packetplayinautorecipe.func_194318_a() && this.field_147369_b.field_71070_bA.func_75129_b(this.field_147369_b)) {
            this.field_194309_H.func_194327_a(this.field_147369_b, packetplayinautorecipe.func_194317_b(), packetplayinautorecipe.func_194319_c());
        }
    }

    public void func_147338_a(CPacketEnchantItem packetplayinenchantitem) {
        PacketThreadUtil.func_180031_a(packetplayinenchantitem, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        this.field_147369_b.func_143004_u();
        if (this.field_147369_b.field_71070_bA.field_75152_c == packetplayinenchantitem.func_149539_c() && this.field_147369_b.field_71070_bA.func_75129_b(this.field_147369_b) && !this.field_147369_b.func_175149_v()) {
            this.field_147369_b.field_71070_bA.func_75140_a(this.field_147369_b, packetplayinenchantitem.func_149537_d());
            this.field_147369_b.field_71070_bA.func_75142_b();
        }

    }

    public void func_147344_a(CPacketCreativeInventoryAction packetplayinsetcreativeslot) {
        PacketThreadUtil.func_180031_a(packetplayinsetcreativeslot, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.field_71134_c.func_73083_d()) {
            boolean flag = packetplayinsetcreativeslot.func_149627_c() < 0;
            ItemStack itemstack = packetplayinsetcreativeslot.func_149625_d();

            if (!itemstack.func_190926_b() && itemstack.func_77942_o() && itemstack.func_77978_p().func_150297_b("BlockEntityTag", 10)) {
                NBTTagCompound nbttagcompound = itemstack.func_77978_p().func_74775_l("BlockEntityTag");

                if (nbttagcompound.func_74764_b("x") && nbttagcompound.func_74764_b("y") && nbttagcompound.func_74764_b("z")) {
                    BlockPos blockposition = new BlockPos(nbttagcompound.func_74762_e("x"), nbttagcompound.func_74762_e("y"), nbttagcompound.func_74762_e("z"));
                    TileEntity tileentity = this.field_147369_b.field_70170_p.func_175625_s(blockposition);

                    if (tileentity != null) {
                        NBTTagCompound nbttagcompound1 = tileentity.func_189515_b(new NBTTagCompound());

                        nbttagcompound1.func_82580_o("x");
                        nbttagcompound1.func_82580_o("y");
                        nbttagcompound1.func_82580_o("z");
                        itemstack.func_77983_a("BlockEntityTag", (NBTBase) nbttagcompound1);
                    }
                }
            }

            boolean flag1 = packetplayinsetcreativeslot.func_149627_c() >= 1 && packetplayinsetcreativeslot.func_149627_c() <= 45;
            // CraftBukkit - Add invalidItems check
            boolean flag2 = itemstack.func_190926_b() || itemstack.func_77960_j() >= 0 && itemstack.func_190916_E() <= 64 && !itemstack.func_190926_b() && (!invalidItems.contains(Item.func_150891_b(itemstack.func_77973_b())) || !org.spigotmc.SpigotConfig.filterCreativeItems); // Spigot
            if (flag || (flag1 && !ItemStack.func_77989_b(this.field_147369_b.field_71069_bz.func_75139_a(packetplayinsetcreativeslot.func_149627_c()).func_75211_c(), packetplayinsetcreativeslot.func_149625_d()))) { // Insist on valid slot
                // CraftBukkit start - Call click event
                InventoryView inventory = this.field_147369_b.field_71069_bz.getBukkitView();
                org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(packetplayinsetcreativeslot.func_149625_d());

                SlotType type = SlotType.QUICKBAR;
                if (flag) {
                    type = SlotType.OUTSIDE;
                } else if (packetplayinsetcreativeslot.func_149627_c() < 36) {
                    if (packetplayinsetcreativeslot.func_149627_c() >= 5 && packetplayinsetcreativeslot.func_149627_c() < 9) {
                        type = SlotType.ARMOR;
                    } else {
                        type = SlotType.CONTAINER;
                    }
                }
                InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type, flag ? -999 : packetplayinsetcreativeslot.func_149627_c(), item);
                server.getPluginManager().callEvent(event);

                itemstack = CraftItemStack.asNMSCopy(event.getCursor());

                switch (event.getResult()) {
                case ALLOW:
                    // Plugin cleared the id / stacksize checks
                    flag2 = true;
                    break;
                case DEFAULT:
                    break;
                case DENY:
                    // Reset the slot
                    if (packetplayinsetcreativeslot.func_149627_c() >= 0) {
                        this.field_147369_b.field_71135_a.func_147359_a(new SPacketSetSlot(this.field_147369_b.field_71069_bz.field_75152_c, packetplayinsetcreativeslot.func_149627_c(), this.field_147369_b.field_71069_bz.func_75139_a(packetplayinsetcreativeslot.func_149627_c()).func_75211_c()));
                        this.field_147369_b.field_71135_a.func_147359_a(new SPacketSetSlot(-1, -1, ItemStack.field_190927_a));
                    }
                    return;
                }
            }
            // CraftBukkit end

            if (flag1 && flag2) {
                if (itemstack.func_190926_b()) {
                    this.field_147369_b.field_71069_bz.func_75141_a(packetplayinsetcreativeslot.func_149627_c(), ItemStack.field_190927_a);
                } else {
                    this.field_147369_b.field_71069_bz.func_75141_a(packetplayinsetcreativeslot.func_149627_c(), itemstack);
                }

                this.field_147369_b.field_71069_bz.func_75128_a(this.field_147369_b, true);
            } else if (flag && flag2 && this.field_147375_m < 200) {
                this.field_147375_m += 20;
                EntityItem entityitem = this.field_147369_b.func_71019_a(itemstack, true);

                if (entityitem != null) {
                    entityitem.func_70288_d();
                }
            }
        }

    }

    public void func_147339_a(CPacketConfirmTransaction packetplayintransaction) {
        PacketThreadUtil.func_180031_a(packetplayintransaction, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        Short oshort = (Short) this.field_147372_n.func_76041_a(this.field_147369_b.field_71070_bA.field_75152_c);

        if (oshort != null && packetplayintransaction.func_149533_d() == oshort.shortValue() && this.field_147369_b.field_71070_bA.field_75152_c == packetplayintransaction.func_149532_c() && !this.field_147369_b.field_71070_bA.func_75129_b(this.field_147369_b) && !this.field_147369_b.func_175149_v()) {
            this.field_147369_b.field_71070_bA.func_75128_a(this.field_147369_b, true);
        }

    }

    public void func_147343_a(CPacketUpdateSign packetplayinupdatesign) {
        PacketThreadUtil.func_180031_a(packetplayinupdatesign, this, this.field_147369_b.func_71121_q());
        if (this.field_147369_b.func_70610_aX()) return; // CraftBukkit
        this.field_147369_b.func_143004_u();
        WorldServer worldserver = this.field_147367_d.func_71218_a(this.field_147369_b.field_71093_bK);
        BlockPos blockposition = packetplayinupdatesign.func_179722_a();

        if (worldserver.func_175667_e(blockposition)) {
            IBlockState iblockdata = worldserver.func_180495_p(blockposition);
            TileEntity tileentity = worldserver.func_175625_s(blockposition);

            if (!(tileentity instanceof TileEntitySign)) {
                return;
            }

            TileEntitySign tileentitysign = (TileEntitySign) tileentity;

            if (!tileentitysign.func_145914_a() || tileentitysign.func_145911_b() != this.field_147369_b) {
                this.field_147367_d.func_71236_h("Player " + this.field_147369_b.func_70005_c_() + " just tried to change non-editable sign");
                this.func_147359_a(tileentity.func_189518_D_()); // CraftBukkit
                return;
            }

            String[] astring = packetplayinupdatesign.func_187017_b();

            // CraftBukkit start
            Player player = this.server.getPlayer(this.field_147369_b);
            int x = packetplayinupdatesign.func_179722_a().func_177958_n();
            int y = packetplayinupdatesign.func_179722_a().func_177956_o();
            int z = packetplayinupdatesign.func_179722_a().func_177952_p();
            String[] lines = new String[4];

            for (int i = 0; i < astring.length; ++i) {
                lines[i] = ChatAllowedCharacters.func_71565_a(astring[i]); //Paper - Replaced with anvil color stripping method to stop exploits that allow colored signs to be created.
            }
            SignChangeEvent event = new SignChangeEvent((org.bukkit.craftbukkit.block.CraftBlock) player.getWorld().getBlockAt(x, y, z), this.server.getPlayer(this.field_147369_b), lines);
            this.server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                System.arraycopy(org.bukkit.craftbukkit.block.CraftSign.sanitizeLines(event.getLines()), 0, tileentitysign.field_145915_a, 0, 4);
                tileentitysign.field_145916_j = false;
             }
            // CraftBukkit end

            tileentitysign.func_70296_d();
            worldserver.func_184138_a(blockposition, iblockdata, iblockdata, 3);
        }

    }

    public void func_147353_a(CPacketKeepAlive packetplayinkeepalive) {
        //PlayerConnectionUtils.ensureMainThread(packetplayinkeepalive, this, this.player.x()); // CraftBukkit // Paper - This shouldn't be on the main thread
        if (this.field_194403_g && packetplayinkeepalive.func_149460_c() == this.field_194404_h) {
            int i = (int) (this.func_147363_d() - this.field_194402_f);

            this.field_147369_b.field_71138_i = (this.field_147369_b.field_71138_i * 3 + i) / 4;
            this.field_194403_g = false;
        } else if (!this.field_147369_b.func_70005_c_().equals(this.field_147367_d.func_71214_G())) {
            // Paper start - This needs to be handled on the main thread for plugins
            NetHandlerPlayServer.field_147370_c.warn("{} sent an invalid keepalive! pending keepalive: {} got id: {} expected id: {}",
                    this.field_147369_b.func_70005_c_(), this.isPendingPing(), packetplayinkeepalive.func_149460_c(), this.getKeepAliveID());
            field_147367_d.func_152344_a(() -> {
                    this.func_194028_b(new TextComponentTranslation("disconnect.timeout", new Object[0]));
            });
            // Paper end
        }

    }

    private long getCurrentMillis() { return func_147363_d(); } // Paper - OBFHELPER
    private long func_147363_d() {
        return System.nanoTime() / 1000000L;
    }

    public void func_147348_a(CPacketPlayerAbilities packetplayinabilities) {
        PacketThreadUtil.func_180031_a(packetplayinabilities, this, this.field_147369_b.func_71121_q());
        // CraftBukkit start
        if (this.field_147369_b.field_71075_bZ.field_75101_c && this.field_147369_b.field_71075_bZ.field_75100_b != packetplayinabilities.func_149488_d()) {
            PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(this.server.getPlayer(this.field_147369_b), packetplayinabilities.func_149488_d());
            this.server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.field_147369_b.field_71075_bZ.field_75100_b = packetplayinabilities.func_149488_d(); // Actually set the player's flying status
            } else {
                this.field_147369_b.func_71016_p(); // Tell the player their ability was reverted
            }
        }
        // CraftBukkit end
    }

    // Paper start - async tab completion
    public void func_147341_a(CPacketTabComplete packet) {
        // CraftBukkit start
        if (chatSpamField.addAndGet(this, 10) > 500 && !this.field_147367_d.func_184103_al().func_152596_g(this.field_147369_b.func_146103_bH())) {
            field_147367_d.func_152344_a(() -> this.func_194028_b(new TextComponentTranslation("disconnect.spam", new Object[0])));
            return;
        }
        // CraftBukkit end

        com.destroystokyo.paper.event.server.AsyncTabCompleteEvent event;
        java.util.List<String> completions = new ArrayList<>();
        BlockPos blockpos = packet.func_179709_b();
        String buffer = packet.func_149419_c();
        boolean isCommand = buffer.startsWith("/") || packet.func_186989_c();
        event = new com.destroystokyo.paper.event.server.AsyncTabCompleteEvent(this.getPlayer(), completions,
            buffer, isCommand, blockpos != null ? MCUtil.toLocation(field_147369_b.field_70170_p, blockpos) : null);
        event.callEvent();
        completions = event.isCancelled() ? com.google.common.collect.ImmutableList.of() : event.getCompletions();
        if (event.isCancelled() || event.isHandled()) {
            // Still fire sync event with the provided completions, if someone is listening
            if (!event.isCancelled() && org.bukkit.event.server.TabCompleteEvent.getHandlerList().getRegisteredListeners().length > 0) {
                java.util.List<String> finalCompletions = completions;
                Waitable<java.util.List<String>> syncCompletions = new Waitable<java.util.List<String>>() {
                    @Override
                    protected java.util.List<String> evaluate() {
                        org.bukkit.event.server.TabCompleteEvent syncEvent = new org.bukkit.event.server.TabCompleteEvent(NetHandlerPlayServer.this.getPlayer(), buffer, finalCompletions, isCommand, blockpos != null ? MCUtil.toLocation(field_147369_b.field_70170_p, blockpos) : null);
                        return syncEvent.callEvent() ? syncEvent.getCompletions() : com.google.common.collect.ImmutableList.of();
                    }
                };
                server.getServer().processQueue.add(syncCompletions);
                try {
                    completions = syncCompletions.get();
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            }

            this.field_147369_b.field_71135_a.func_147359_a(new SPacketTabComplete(completions.toArray(new String[completions.size()])));
            return;
        }
        field_147367_d.func_152344_a(() -> {
            java.util.List<String> syncCompletions = this.field_147367_d.func_184104_a(this.field_147369_b, buffer, blockpos, isCommand);
            this.field_147369_b.field_71135_a.func_147359_a(new SPacketTabComplete(syncCompletions.toArray(new String[syncCompletions.size()])));
        });
        // Paper end
    }

    public void func_147352_a(CPacketClientSettings packetplayinsettings) {
        PacketThreadUtil.func_180031_a(packetplayinsettings, this, this.field_147369_b.func_71121_q());
        this.field_147369_b.func_147100_a(packetplayinsettings);
    }

    public void func_147349_a(CPacketCustomPayload packetplayincustompayload) {
        PacketThreadUtil.func_180031_a(packetplayincustompayload, this, this.field_147369_b.func_71121_q());
        String s = packetplayincustompayload.func_149559_c();
        PacketBuffer packetdataserializer;
        ItemStack itemstack;
        ItemStack itemstack1;

        if ("MC|BEdit".equals(s)) {
            // CraftBukkit start
            if (this.lastBookTick + 20 > MinecraftServer.currentTick) {
                this.disconnect("Book edited too quickly!");
                return;
            }
            this.lastBookTick = MinecraftServer.currentTick;
            // CraftBukkit end
            packetdataserializer = packetplayincustompayload.func_180760_b();

            try {
                itemstack = packetdataserializer.func_150791_c();
                if (itemstack.func_190926_b()) {
                    return;
                }

                if (!ItemWritableBook.func_150930_a(itemstack.func_77978_p())) {
                    throw new IOException("Invalid book tag!");
                }

                itemstack1 = this.field_147369_b.func_184614_ca();
                if (itemstack1.func_190926_b()) {
                    return;
                }

                if (itemstack.func_77973_b() == Items.field_151099_bA && itemstack.func_77973_b() == itemstack1.func_77973_b()) {
                    itemstack1 = new ItemStack(Items.field_151099_bA); // CraftBukkit
                    itemstack1.func_77983_a("pages", (NBTBase) itemstack.func_77978_p().func_150295_c("pages", 8));
                    CraftEventFactory.handleEditBookEvent(field_147369_b, itemstack1); // CraftBukkit
                }
            } catch (Exception exception) {
                IllegalPacketEvent.process(field_147369_b.getBukkitEntity(), "InvalidBookEdit", "Invalid book data!", exception); // Paper
            }
        } else {
            String s1;

            if ("MC|BSign".equals(s)) {
                // CraftBukkit start
                if (this.lastBookTick + 20 > MinecraftServer.currentTick) {
                    this.disconnect("Book edited too quickly!");
                    return;
                }
                this.lastBookTick = MinecraftServer.currentTick;
                // CraftBukkit end
                packetdataserializer = packetplayincustompayload.func_180760_b();

                try {
                    itemstack = packetdataserializer.func_150791_c();
                    if (itemstack.func_190926_b()) {
                        return;
                    }

                    if (!ItemWrittenBook.func_77828_a(itemstack.func_77978_p())) {
                        throw new IOException("Invalid book tag!");
                    }

                    itemstack1 = this.field_147369_b.func_184614_ca();
                    if (itemstack1.func_190926_b()) {
                        return;
                    }

                    if (itemstack.func_77973_b() == Items.field_151099_bA && itemstack1.func_77973_b() == Items.field_151099_bA) {
                        ItemStack itemstack2 = new ItemStack(Items.field_151164_bB);

                        itemstack2.func_77983_a("author", (NBTBase) (new NBTTagString(this.field_147369_b.func_70005_c_())));
                        itemstack2.func_77983_a("title", (NBTBase) (new NBTTagString(itemstack.func_77978_p().func_74779_i("title"))));
                        NBTTagList nbttaglist = itemstack.func_77978_p().func_150295_c("pages", 8);

                        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                            s1 = nbttaglist.func_150307_f(i);
                            TextComponentString chatcomponenttext = new TextComponentString(s1);

                            s1 = ITextComponent.Serializer.func_150696_a((ITextComponent) chatcomponenttext);
                            nbttaglist.func_150304_a(i, new NBTTagString(s1));
                        }

                        itemstack2.func_77983_a("pages", (NBTBase) nbttaglist);
                        CraftEventFactory.handleEditBookEvent(field_147369_b, itemstack2); // CraftBukkit
                    }
                } catch (Exception exception1) {
                    IllegalPacketEvent.process(field_147369_b.getBukkitEntity(), "InvalidBookSign", "Invalid book data!", exception1); // Paper
                }
            } else if ("MC|TrSel".equals(s)) {
                try {
                    int j = packetplayincustompayload.func_180760_b().readInt();
                    Container container = this.field_147369_b.field_71070_bA;

                    if (container instanceof ContainerMerchant) {
                        ((ContainerMerchant) container).func_75175_c(j);
                    }
                } catch (Exception exception2) {
                    IllegalPacketEvent.process(field_147369_b.getBukkitEntity(), "InvalidTrade", "Invalid trade data!", exception2); // Paper
                }
            } else {
                TileEntity tileentity;

                if ("MC|AdvCmd".equals(s)) {
                    if (!this.field_147367_d.func_82356_Z()) {
                        this.field_147369_b.func_145747_a(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                        return;
                    }

                    if (!this.field_147369_b.func_189808_dh()) {
                        this.field_147369_b.func_145747_a(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                        return;
                    }

                    packetdataserializer = packetplayincustompayload.func_180760_b();

                    try {
                        byte b0 = packetdataserializer.readByte();
                        CommandBlockBaseLogic commandblocklistenerabstract = null;

                        if (b0 == 0) {
                            tileentity = this.field_147369_b.field_70170_p.func_175625_s(new BlockPos(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt()));
                            if (tileentity instanceof TileEntityCommandBlock) {
                                commandblocklistenerabstract = ((TileEntityCommandBlock) tileentity).func_145993_a();
                            }
                        } else if (b0 == 1) {
                            Entity entity = this.field_147369_b.field_70170_p.func_73045_a(packetdataserializer.readInt());

                            if (entity instanceof EntityMinecartCommandBlock) {
                                commandblocklistenerabstract = ((EntityMinecartCommandBlock) entity).func_145822_e();
                            }
                        }

                        String s2 = packetdataserializer.func_150789_c(packetdataserializer.readableBytes());
                        boolean flag = packetdataserializer.readBoolean();

                        if (commandblocklistenerabstract != null) {
                            commandblocklistenerabstract.func_145752_a(s2);
                            commandblocklistenerabstract.func_175573_a(flag);
                            if (!flag) {
                                commandblocklistenerabstract.func_145750_b((ITextComponent) null);
                            }

                            commandblocklistenerabstract.func_145756_e();
                            this.field_147369_b.func_145747_a(new TextComponentTranslation("advMode.setCommand.success", new Object[] { s2}));
                        }
                    } catch (Exception exception3) {
                        NetHandlerPlayServer.field_147370_c.error("Couldn\'t set command block", exception3);
                        this.disconnect("Invalid command data!"); // CraftBukkit
                    }
                } else if ("MC|AutoCmd".equals(s)) {
                    if (!this.field_147367_d.func_82356_Z()) {
                        this.field_147369_b.func_145747_a(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                        return;
                    }

                    if (!this.field_147369_b.func_189808_dh()) {
                        this.field_147369_b.func_145747_a(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                        return;
                    }

                    packetdataserializer = packetplayincustompayload.func_180760_b();

                    try {
                        CommandBlockBaseLogic commandblocklistenerabstract1 = null;
                        TileEntityCommandBlock tileentitycommand = null;
                        BlockPos blockposition = new BlockPos(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt());
                        TileEntity tileentity1 = this.field_147369_b.field_70170_p.func_175625_s(blockposition);

                        if (tileentity1 instanceof TileEntityCommandBlock) {
                            tileentitycommand = (TileEntityCommandBlock) tileentity1;
                            commandblocklistenerabstract1 = tileentitycommand.func_145993_a();
                        }

                        String s3 = packetdataserializer.func_150789_c(packetdataserializer.readableBytes());
                        boolean flag1 = packetdataserializer.readBoolean();
                        TileEntityCommandBlock.Mode tileentitycommand_type = TileEntityCommandBlock.Mode.valueOf(packetdataserializer.func_150789_c(16));
                        boolean flag2 = packetdataserializer.readBoolean();
                        boolean flag3 = packetdataserializer.readBoolean();

                        if (commandblocklistenerabstract1 != null) {
                            EnumFacing enumdirection = (EnumFacing) this.field_147369_b.field_70170_p.func_180495_p(blockposition).func_177229_b(BlockCommandBlock.field_185564_a);
                            IBlockState iblockdata;

                            switch (tileentitycommand_type) {
                            case SEQUENCE:
                                iblockdata = Blocks.field_185777_dd.func_176223_P();
                                this.field_147369_b.field_70170_p.func_180501_a(blockposition, iblockdata.func_177226_a(BlockCommandBlock.field_185564_a, enumdirection).func_177226_a(BlockCommandBlock.field_185565_b, Boolean.valueOf(flag2)), 2);
                                break;

                            case AUTO:
                                iblockdata = Blocks.field_185776_dc.func_176223_P();
                                this.field_147369_b.field_70170_p.func_180501_a(blockposition, iblockdata.func_177226_a(BlockCommandBlock.field_185564_a, enumdirection).func_177226_a(BlockCommandBlock.field_185565_b, Boolean.valueOf(flag2)), 2);
                                break;

                            case REDSTONE:
                                iblockdata = Blocks.field_150483_bI.func_176223_P();
                                this.field_147369_b.field_70170_p.func_180501_a(blockposition, iblockdata.func_177226_a(BlockCommandBlock.field_185564_a, enumdirection).func_177226_a(BlockCommandBlock.field_185565_b, Boolean.valueOf(flag2)), 2);
                            }

                            tileentity1.func_145829_t();
                            this.field_147369_b.field_70170_p.func_175690_a(blockposition, tileentity1);
                            commandblocklistenerabstract1.func_145752_a(s3);
                            commandblocklistenerabstract1.func_175573_a(flag1);
                            if (!flag1) {
                                commandblocklistenerabstract1.func_145750_b((ITextComponent) null);
                            }

                            tileentitycommand.func_184253_b(flag3);
                            commandblocklistenerabstract1.func_145756_e();
                            if (!StringUtils.func_151246_b(s3)) {
                                this.field_147369_b.func_145747_a(new TextComponentTranslation("advMode.setCommand.success", new Object[] { s3}));
                            }
                        }
                    } catch (Exception exception4) {
                        NetHandlerPlayServer.field_147370_c.error("Couldn\'t set command block", exception4);
                        this.disconnect("Invalid command data!"); // CraftBukkit
                    }
                } else {
                    int k;

                    if ("MC|Beacon".equals(s)) {
                        if (this.field_147369_b.field_71070_bA instanceof ContainerBeacon) {
                            try {
                                packetdataserializer = packetplayincustompayload.func_180760_b();
                                k = packetdataserializer.readInt();
                                int l = packetdataserializer.readInt();
                                ContainerBeacon containerbeacon = (ContainerBeacon) this.field_147369_b.field_71070_bA;
                                Slot slot = containerbeacon.func_75139_a(0);

                                if (slot.func_75216_d()) {
                                    slot.func_75209_a(1);
                                    IInventory iinventory = containerbeacon.func_180611_e();

                                    iinventory.func_174885_b(1, k);
                                    iinventory.func_174885_b(2, l);
                                    iinventory.func_70296_d();
                                }
                            } catch (Exception exception5) {
                                IllegalPacketEvent.process(field_147369_b.getBukkitEntity(), "InvalidBeacon", "Invalid beacon data!", exception5); // Paper
                            }
                        }
                    } else if ("MC|ItemName".equals(s)) {
                        if (this.field_147369_b.field_71070_bA instanceof ContainerRepair) {
                            ContainerRepair containeranvil = (ContainerRepair) this.field_147369_b.field_71070_bA;

                            if (packetplayincustompayload.func_180760_b() != null && packetplayincustompayload.func_180760_b().readableBytes() >= 1) {
                                String s4 = ChatAllowedCharacters.func_71565_a(packetplayincustompayload.func_180760_b().func_150789_c(32767));

                                if (s4.length() <= 35) {
                                    containeranvil.func_82850_a(s4);
                                }
                            } else {
                                containeranvil.func_82850_a("");
                            }
                        }
                    } else if ("MC|Struct".equals(s)) {
                        if (!this.field_147369_b.func_189808_dh()) {
                            return;
                        }

                        packetdataserializer = packetplayincustompayload.func_180760_b();

                        try {
                            BlockPos blockposition1 = new BlockPos(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt());
                            IBlockState iblockdata1 = this.field_147369_b.field_70170_p.func_180495_p(blockposition1);

                            tileentity = this.field_147369_b.field_70170_p.func_175625_s(blockposition1);
                            if (tileentity instanceof TileEntityStructure) {
                                TileEntityStructure tileentitystructure = (TileEntityStructure) tileentity;
                                byte b1 = packetdataserializer.readByte();

                                s1 = packetdataserializer.func_150789_c(32);
                                tileentitystructure.func_184405_a(TileEntityStructure.Mode.valueOf(s1));
                                tileentitystructure.func_184404_a(packetdataserializer.func_150789_c(64));
                                int i1 = MathHelper.func_76125_a(packetdataserializer.readInt(), -32, 32);
                                int j1 = MathHelper.func_76125_a(packetdataserializer.readInt(), -32, 32);
                                int k1 = MathHelper.func_76125_a(packetdataserializer.readInt(), -32, 32);

                                tileentitystructure.func_184414_b(new BlockPos(i1, j1, k1));
                                int l1 = MathHelper.func_76125_a(packetdataserializer.readInt(), 0, 32);
                                int i2 = MathHelper.func_76125_a(packetdataserializer.readInt(), 0, 32);
                                int j2 = MathHelper.func_76125_a(packetdataserializer.readInt(), 0, 32);

                                tileentitystructure.func_184409_c(new BlockPos(l1, i2, j2));
                                String s5 = packetdataserializer.func_150789_c(32);

                                tileentitystructure.func_184411_a(Mirror.valueOf(s5));
                                String s6 = packetdataserializer.func_150789_c(32);

                                tileentitystructure.func_184408_a(Rotation.valueOf(s6));
                                tileentitystructure.func_184410_b(packetdataserializer.func_150789_c(128));
                                tileentitystructure.func_184406_a(packetdataserializer.readBoolean());
                                tileentitystructure.func_189703_e(packetdataserializer.readBoolean());
                                tileentitystructure.func_189710_f(packetdataserializer.readBoolean());
                                tileentitystructure.func_189718_a(MathHelper.func_76131_a(packetdataserializer.readFloat(), 0.0F, 1.0F));
                                tileentitystructure.func_189725_a(packetdataserializer.func_179260_f());
                                String s7 = tileentitystructure.func_189715_d();

                                if (b1 == 2) {
                                    if (tileentitystructure.func_184419_m()) {
                                        this.field_147369_b.func_146105_b((ITextComponent) (new TextComponentTranslation("structure_block.save_success", new Object[] { s7})), false);
                                    } else {
                                        this.field_147369_b.func_146105_b((ITextComponent) (new TextComponentTranslation("structure_block.save_failure", new Object[] { s7})), false);
                                    }
                                } else if (b1 == 3) {
                                    if (!tileentitystructure.func_189709_F()) {
                                        this.field_147369_b.func_146105_b((ITextComponent) (new TextComponentTranslation("structure_block.load_not_found", new Object[] { s7})), false);
                                    } else if (tileentitystructure.func_184412_n()) {
                                        this.field_147369_b.func_146105_b((ITextComponent) (new TextComponentTranslation("structure_block.load_success", new Object[] { s7})), false);
                                    } else {
                                        this.field_147369_b.func_146105_b((ITextComponent) (new TextComponentTranslation("structure_block.load_prepare", new Object[] { s7})), false);
                                    }
                                } else if (b1 == 4) {
                                    if (tileentitystructure.func_184417_l()) {
                                        this.field_147369_b.func_146105_b((ITextComponent) (new TextComponentTranslation("structure_block.size_success", new Object[] { s7})), false);
                                    } else {
                                        this.field_147369_b.func_146105_b((ITextComponent) (new TextComponentTranslation("structure_block.size_failure", new Object[0])), false);
                                    }
                                }

                                tileentitystructure.func_70296_d();
                                this.field_147369_b.field_70170_p.func_184138_a(blockposition1, iblockdata1, iblockdata1, 3);
                            }
                        } catch (Exception exception6) {
                            NetHandlerPlayServer.field_147370_c.error("Couldn\'t set structure block", exception6);
                            this.disconnect("Invalid structure data!"); // CraftBukkit
                        }
                    } else if ("MC|PickItem".equals(s)) {
                        packetdataserializer = packetplayincustompayload.func_180760_b();

                        try {
                            k = packetdataserializer.func_150792_a();
                            this.field_147369_b.field_71071_by.func_184430_d(k);
                            this.field_147369_b.field_71135_a.func_147359_a(new SPacketSetSlot(-2, this.field_147369_b.field_71071_by.field_70461_c, this.field_147369_b.field_71071_by.func_70301_a(this.field_147369_b.field_71071_by.field_70461_c)));
                            this.field_147369_b.field_71135_a.func_147359_a(new SPacketSetSlot(-2, k, this.field_147369_b.field_71071_by.func_70301_a(k)));
                            this.field_147369_b.field_71135_a.func_147359_a(new SPacketHeldItemChange(this.field_147369_b.field_71071_by.field_70461_c));
                        } catch (Exception exception7) {
                            IllegalPacketEvent.process(field_147369_b.getBukkitEntity(), "InvalidPickItem", "Invalid PickItem", exception7); // Paper
                        }
                    }
                    // CraftBukkit start
                    else if (packetplayincustompayload.func_149559_c().equals("REGISTER")) {
                        try {
                            String channels = packetplayincustompayload.func_180760_b().toString(com.google.common.base.Charsets.UTF_8);
                            for (String channel : channels.split("\0")) {
                                getPlayer().addChannel(channel);
                            }
                        } catch (Exception ex) {
                            NetHandlerPlayServer.field_147370_c.error("Couldn\'t register custom payload", ex);
                            this.disconnect("Invalid payload REGISTER!");
                        }
                    } else if (packetplayincustompayload.func_149559_c().equals("UNREGISTER")) {
                        try {
                            String channels = packetplayincustompayload.func_180760_b().toString(com.google.common.base.Charsets.UTF_8);
                            for (String channel : channels.split("\0")) {
                                getPlayer().removeChannel(channel);
                            }
                        } catch (Exception ex) {
                            NetHandlerPlayServer.field_147370_c.error("Couldn\'t unregister custom payload", ex);
                            this.disconnect("Invalid payload UNREGISTER!");
                        }
                    } else {
                        try {
                            byte[] data = new byte[packetplayincustompayload.func_180760_b().readableBytes()];
                            packetplayincustompayload.func_180760_b().readBytes(data);
                            server.getMessenger().dispatchIncomingMessage(field_147369_b.getBukkitEntity(), packetplayincustompayload.func_149559_c(), data);
                        } catch (Exception ex) {
                            NetHandlerPlayServer.field_147370_c.error("Couldn\'t dispatch custom payload", ex);
                            this.disconnect("Invalid custom payload!");
                        }
                    }
                    // CraftBukkit end
                }
            }
        }

    }

    // CraftBukkit start - Add "isDisconnected" method
    public final boolean isDisconnected() {
        return (!this.field_147369_b.joining && !this.field_147371_a.func_150724_d()) || this.processedDisconnect; // Paper
    }
}
