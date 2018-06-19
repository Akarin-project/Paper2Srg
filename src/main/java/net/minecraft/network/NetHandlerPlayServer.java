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

    private static final Logger LOGGER = LogManager.getLogger();
    public final NetworkManager netManager;
    private final MinecraftServer serverController;
    public EntityPlayerMP player;
    private int networkTickCount;
    private long field_194402_f = getCurrentMillis(); private void setLastPing(long lastPing) { this.field_194402_f = lastPing;}; private long getLastPing() { return this.field_194402_f;}; // Paper - OBFHELPER - set ping to delay initial
    private boolean field_194403_g; private void setPendingPing(boolean isPending) { this.field_194403_g = isPending;}; private boolean isPendingPing() { return this.field_194403_g;}; // Paper - OBFHELPER
    private long field_194404_h; private void setKeepAliveID(long keepAliveID) { this.field_194404_h = keepAliveID;}; private long getKeepAliveID() {return this.field_194404_h; };  // Paper - OBFHELPER
    // CraftBukkit start - multithreaded fields
    private volatile int chatSpamThresholdCount;
    private static final AtomicIntegerFieldUpdater chatSpamField = AtomicIntegerFieldUpdater.newUpdater(NetHandlerPlayServer.class, "chatThrottle");
    // CraftBukkit end
    private int itemDropThreshold;
    private final IntHashMap<Short> pendingTransactions = new IntHashMap();
    private double firstGoodX;
    private double firstGoodY;
    private double firstGoodZ;
    private double lastGoodX;
    private double lastGoodY;
    private double lastGoodZ;
    private Entity lowestRiddenEnt;
    private double lowestRiddenX;
    private double lowestRiddenY;
    private double lowestRiddenZ;
    private double lowestRiddenX1;
    private double lowestRiddenY1;
    private double lowestRiddenZ1;
    private Vec3d targetPos;
    private int teleportId;
    private int lastPositionUpdate;
    private boolean floating;
    private int floatingTickCount;
    private boolean vehicleFloating;
    private int vehicleFloatingTickCount;
    private int movePacketCounter;
    private int lastMovePacketCounter;
    private ServerRecipeBookHelper field_194309_H = new ServerRecipeBookHelper();
    private static final long KEEPALIVE_LIMIT = Long.getLong("paper.playerconnection.keepalive", 30) * 1000; // Paper - provide property to set keepalive limit

    public NetHandlerPlayServer(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayerMP entityplayer) {
        this.serverController = minecraftserver;
        this.netManager = networkmanager;
        networkmanager.setNetHandler(this);
        this.player = entityplayer;
        entityplayer.connection = this;

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
        return (this.player == null) ? null : (CraftPlayer) this.player.getBukkitEntity();
    }
    private final static HashSet<Integer> invalidItems = new HashSet<Integer>(java.util.Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 55, 59, 62, 63, 64, 68, 71, 74, 75, 83, 90, 92, 93, 94, 104, 105, 115, 117, 118, 119, 125, 127, 132, 140, 141, 142, 144)); // TODO: Check after every update.
    // CraftBukkit end

    @Override
    public void update() {
        this.captureCurrentPosition();
        this.player.onUpdateEntity();
        this.player.setPositionAndRotation(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.rotationYaw, this.player.rotationPitch);
        ++this.networkTickCount;
        this.lastMovePacketCounter = this.movePacketCounter;
        if (this.floating) {
            if (++this.floatingTickCount > 80) {
                NetHandlerPlayServer.LOGGER.warn("{} was kicked for floating too long!", this.player.getName());
                this.disconnect(com.destroystokyo.paper.PaperConfig.flyingKickPlayerMessage); // Paper - use configurable kick message
                return;
            }
        } else {
            this.floating = false;
            this.floatingTickCount = 0;
        }

        this.lowestRiddenEnt = this.player.getLowestRidingEntity();
        if (this.lowestRiddenEnt != this.player && this.lowestRiddenEnt.getControllingPassenger() == this.player) {
            this.lowestRiddenX = this.lowestRiddenEnt.posX;
            this.lowestRiddenY = this.lowestRiddenEnt.posY;
            this.lowestRiddenZ = this.lowestRiddenEnt.posZ;
            this.lowestRiddenX1 = this.lowestRiddenEnt.posX;
            this.lowestRiddenY1 = this.lowestRiddenEnt.posY;
            this.lowestRiddenZ1 = this.lowestRiddenEnt.posZ;
            if (this.vehicleFloating && this.player.getLowestRidingEntity().getControllingPassenger() == this.player) {
                if (++this.vehicleFloatingTickCount > 80) {
                    NetHandlerPlayServer.LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getName());
                    this.disconnect(com.destroystokyo.paper.PaperConfig.flyingKickVehicleMessage); // Paper - use configurable kick message
                    return;
                }
            } else {
                this.vehicleFloating = false;
                this.vehicleFloatingTickCount = 0;
            }
        } else {
            this.lowestRiddenEnt = null;
            this.vehicleFloating = false;
            this.vehicleFloatingTickCount = 0;
        }

        this.serverController.profiler.startSection("keepAlive");
        // Paper Start - give clients a longer time to respond to pings as per pre 1.12.2 timings
        // This should effectively place the keepalive handling back to "as it was" before 1.12.2
        long currentTime = this.getCurrentMillis();
        long elapsedTime = currentTime - this.getLastPing();
        if (this.isPendingPing()) {
            // We're pending a ping from the client
            if (!this.processedDisconnect && elapsedTime >= KEEPALIVE_LIMIT) { // check keepalive limit, don't fire if already disconnected
                NetHandlerPlayServer.LOGGER.warn("{} was kicked due to keepalive timeout!", this.player.getName()); // more info
                this.disconnect(new TextComponentTranslation("disconnect.timeout"));
            }
        } else {
            if (elapsedTime >= 15000L) { // 15 seconds
                this.setPendingPing(true);
                this.setLastPing(currentTime);
                this.setKeepAliveID(currentTime);
                this.sendPacket(new SPacketKeepAlive(this.getKeepAliveID()));
            }
        }
        // Paper end

        this.serverController.profiler.endSection();
        // CraftBukkit start
        for (int spam; (spam = this.chatSpamThresholdCount) > 0 && !chatSpamField.compareAndSet(this, spam, spam - 1); ) ;
        /* Use thread-safe field access instead
        if (this.chatThrottle > 0) {
            --this.chatThrottle;
        }
        */
        // CraftBukkit end

        if (this.itemDropThreshold > 0) {
            --this.itemDropThreshold;
        }

        if (this.player.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.player.getLastActiveTime() > this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60) {
            this.player.markPlayerActive(); // CraftBukkit - SPIGOT-854
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.idling", new Object[0]));
        }

    }

    public void captureCurrentPosition() {
        this.firstGoodX = this.player.posX;
        this.firstGoodY = this.player.posY;
        this.firstGoodZ = this.player.posZ;
        this.lastGoodX = this.player.posX;
        this.lastGoodY = this.player.posY;
        this.lastGoodZ = this.player.posZ;
    }

    public NetworkManager getNetworkManager() {
        return this.netManager;
    }

    // CraftBukkit start
    @Deprecated
    public void disconnect(ITextComponent ichatbasecomponent) {
        disconnect(CraftChatMessage.fromComponent(ichatbasecomponent, TextFormatting.WHITE));
    }
    // CraftBukkit end

    public void disconnect(String s) {
        // CraftBukkit start - fire PlayerKickEvent
        if (this.processedDisconnect) {
            return;
        }
        String leaveMessage = TextFormatting.YELLOW + this.player.getName() + " left the game.";

        PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.player), s, leaveMessage);

        if (this.server.getServer().isServerRunning()) {
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

        this.netManager.sendPacket(new SPacketDisconnect(chatcomponenttext), new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception { // CraftBukkit - decompile error
                NetHandlerPlayServer.this.netManager.closeChannel(chatcomponenttext);
            }
        }, new GenericFutureListener[0]);
        this.onDisconnect(chatcomponenttext); // CraftBukkit - fire quit instantly
        this.netManager.disableAutoRead();
        // CraftBukkit - Don't wait
        this.serverController.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                NetHandlerPlayServer.this.netManager.checkDisconnected();
            }
        });
    }

    @Override
    public void processInput(CPacketInput packetplayinsteervehicle) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinsteervehicle, this, this.player.getServerWorld());
        this.player.setEntityActionState(packetplayinsteervehicle.getStrafeSpeed(), packetplayinsteervehicle.getForwardSpeed(), packetplayinsteervehicle.isJumping(), packetplayinsteervehicle.isSneaking());
    }

    private static boolean isMovePlayerPacketInvalid(CPacketPlayer packetplayinflying) {
        return Doubles.isFinite(packetplayinflying.getX(0.0D)) && Doubles.isFinite(packetplayinflying.getY(0.0D)) && Doubles.isFinite(packetplayinflying.getZ(0.0D)) && Floats.isFinite(packetplayinflying.getPitch(0.0F)) && Floats.isFinite(packetplayinflying.getYaw(0.0F)) ? Math.abs(packetplayinflying.getX(0.0D)) > 3.0E7D || Math.abs(packetplayinflying.getY(0.0D)) > 3.0E7D || Math.abs(packetplayinflying.getZ(0.0D)) > 3.0E7D : true;
    }

    private static boolean isMoveVehiclePacketInvalid(CPacketVehicleMove packetplayinvehiclemove) {
        return !Doubles.isFinite(packetplayinvehiclemove.getX()) || !Doubles.isFinite(packetplayinvehiclemove.getY()) || !Doubles.isFinite(packetplayinvehiclemove.getZ()) || !Floats.isFinite(packetplayinvehiclemove.getPitch()) || !Floats.isFinite(packetplayinvehiclemove.getYaw());
    }

    @Override
    public void processVehicleMove(CPacketVehicleMove packetplayinvehiclemove) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinvehiclemove, this, this.player.getServerWorld());
        if (isMoveVehiclePacketInvalid(packetplayinvehiclemove)) {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_vehicle_movement", new Object[0]));
        } else {
            Entity entity = this.player.getLowestRidingEntity();

            if (entity != this.player && entity.getControllingPassenger() == this.player && entity == this.lowestRiddenEnt) {
                WorldServer worldserver = this.player.getServerWorld();
                double d0 = entity.posX;
                double d1 = entity.posY;
                double d2 = entity.posZ;
                double d3 = packetplayinvehiclemove.getX();
                double d4 = packetplayinvehiclemove.getY();
                double d5 = packetplayinvehiclemove.getZ();
                float f = packetplayinvehiclemove.getYaw();
                float f1 = packetplayinvehiclemove.getPitch();
                double d6 = d3 - this.lowestRiddenX;
                double d7 = d4 - this.lowestRiddenY;
                double d8 = d5 - this.lowestRiddenZ;
                double d9 = entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ;
                double d10 = d6 * d6 + d7 * d7 + d8 * d8;


                // CraftBukkit start - handle custom speeds and skipped ticks
                this.allowedPlayerTicks += (System.currentTimeMillis() / 50) - this.lastTick;
                this.allowedPlayerTicks = Math.max(this.allowedPlayerTicks, 1);
                this.lastTick = (int) (System.currentTimeMillis() / 50);

                ++this.movePacketCounter;
                int i = this.movePacketCounter - this.lastMovePacketCounter;
                if (i > Math.max(this.allowedPlayerTicks, 5)) {
                    NetHandlerPlayServer.LOGGER.debug(this.player.getName() + " is sending move packets too frequently (" + i + " packets since last tick)");
                    i = 1;
                }

                if (d10 > 0) {
                    allowedPlayerTicks -= 1;
                } else {
                    allowedPlayerTicks = 20;
                }
                float speed;
                if (player.capabilities.isFlying) {
                    speed = player.capabilities.flySpeed * 20f;
                } else {
                    speed = player.capabilities.walkSpeed * 10f;
                }
                speed *= 2f; // TODO: Get the speed of the vehicle instead of the player

                if (d10 - d9 > Math.max(100.0D, Math.pow(org.spigotmc.SpigotConfig.movedTooQuicklyMultiplier * i * speed, 2)) && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(entity.getName()))) { // Spigot
                // CraftBukkit end
                    NetHandlerPlayServer.LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", entity.getName(), this.player.getName(), Double.valueOf(d6), Double.valueOf(d7), Double.valueOf(d8));
                    this.netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }

                boolean flag = worldserver.getCollisionBoxes(entity, entity.getEntityBoundingBox().shrink(0.0625D)).isEmpty();

                d6 = d3 - this.lowestRiddenX1;
                d7 = d4 - this.lowestRiddenY1 - 1.0E-6D;
                d8 = d5 - this.lowestRiddenZ1;
                entity.move(MoverType.PLAYER, d6, d7, d8);
                double d11 = d7;

                d6 = d3 - entity.posX;
                d7 = d4 - entity.posY;
                if (d7 > -0.5D || d7 < 0.5D) {
                    d7 = 0.0D;
                }

                d8 = d5 - entity.posZ;
                d10 = d6 * d6 + d7 * d7 + d8 * d8;
                boolean flag1 = false;

                if (d10 > org.spigotmc.SpigotConfig.movedWronglyThreshold) { // Spigot
                    flag1 = true;
                    NetHandlerPlayServer.LOGGER.warn(entity.getName() + " (vehicle of " + this.player.getName() + ") moved wrongly!"); // Paper - More informative
                }

                entity.setPositionAndRotation(d3, d4, d5, f, f1);
                boolean flag2 = worldserver.getCollisionBoxes(entity, entity.getEntityBoundingBox().shrink(0.0625D)).isEmpty();

                if (flag && (flag1 || !flag2)) {
                    entity.setPositionAndRotation(d0, d1, d2, f, f1);
                    this.netManager.sendPacket(new SPacketMoveVehicle(entity));
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
                to.setX(packetplayinvehiclemove.getX());
                to.setY(packetplayinvehiclemove.getY());
                to.setZ(packetplayinvehiclemove.getZ());


                // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
                to.setYaw(packetplayinvehiclemove.getYaw());
                to.setPitch(packetplayinvehiclemove.getPitch());

                // Prevent 40 event-calls for less than a single pixel of movement >.>
                double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
                float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

                if ((delta > 1f / 256 || deltaAngle > 10f) && !this.player.isMovementBlocked()) {
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
                            this.player.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.PLUGIN);
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

                this.serverController.getPlayerList().serverUpdateMovingPlayer(this.player);
                this.player.addMovementStat(this.player.posX - d0, this.player.posY - d1, this.player.posZ - d2);
                this.vehicleFloating = d11 >= -0.03125D && !this.serverController.isFlightAllowed() && !worldserver.checkBlockCollision(entity.getEntityBoundingBox().grow(0.0625D).expand(0.0D, -0.55D, 0.0D));
                this.lowestRiddenX1 = entity.posX;
                this.lowestRiddenY1 = entity.posY;
                this.lowestRiddenZ1 = entity.posZ;
            }

        }
    }

    @Override
    public void processConfirmTeleport(CPacketConfirmTeleport packetplayinteleportaccept) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinteleportaccept, this, this.player.getServerWorld());
        if (packetplayinteleportaccept.getTeleportId() == this.teleportId && this.targetPos != null) { // CraftBukkit
            this.player.setPositionAndRotation(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.player.rotationYaw, this.player.rotationPitch);
            if (this.player.isInvulnerableDimensionChange()) {
                this.lastGoodX = this.targetPos.x;
                this.lastGoodY = this.targetPos.y;
                this.lastGoodZ = this.targetPos.z;
                this.player.clearInvulnerableDimensionChange();
            }

            this.targetPos = null;
        }

    }

    @Override
    public void handleRecipeBookUpdate(CPacketRecipeInfo packetplayinrecipedisplayed) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinrecipedisplayed, this, this.player.getServerWorld());
        if (packetplayinrecipedisplayed.getPurpose() == CPacketRecipeInfo.Purpose.SHOWN) {
            this.player.getRecipeBook().markSeen(packetplayinrecipedisplayed.getRecipe());
        } else if (packetplayinrecipedisplayed.getPurpose() == CPacketRecipeInfo.Purpose.SETTINGS) {
            this.player.getRecipeBook().setGuiOpen(packetplayinrecipedisplayed.isGuiOpen());
            this.player.getRecipeBook().setFilteringCraftable(packetplayinrecipedisplayed.isFilteringCraftable());
        }

    }

    @Override
    public void handleSeenAdvancements(CPacketSeenAdvancements packetplayinadvancements) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinadvancements, this, this.player.getServerWorld());
        if (packetplayinadvancements.getAction() == CPacketSeenAdvancements.Action.OPENED_TAB) {
            ResourceLocation minecraftkey = packetplayinadvancements.getTab();
            Advancement advancement = this.serverController.getAdvancementManager().getAdvancement(minecraftkey);

            if (advancement != null) {
                this.player.getAdvancements().setSelectedTab(advancement);
            }
        }

    }

    @Override
    public void processPlayer(CPacketPlayer packetplayinflying) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinflying, this, this.player.getServerWorld());
        if (isMovePlayerPacketInvalid(packetplayinflying)) {
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_player_movement", new Object[0]));
        } else {
            WorldServer worldserver = this.serverController.getWorld(this.player.dimension);

            if (!this.player.queuedEndExit && !this.player.isMovementBlocked()) { // CraftBukkit
                if (this.networkTickCount == 0) {
                    this.captureCurrentPosition();
                }

                if (this.targetPos != null) {
                    if (this.networkTickCount - this.lastPositionUpdate > 20) {
                        this.lastPositionUpdate = this.networkTickCount;
                        this.setPlayerLocation(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.player.rotationYaw, this.player.rotationPitch);
                    }
                    this.allowedPlayerTicks = 20; // CraftBukkit
                } else {
                    this.lastPositionUpdate = this.networkTickCount;
                    if (this.player.isRiding()) {
                        this.player.setPositionAndRotation(this.player.posX, this.player.posY, this.player.posZ, packetplayinflying.getYaw(this.player.rotationYaw), packetplayinflying.getPitch(this.player.rotationPitch));
                        this.serverController.getPlayerList().serverUpdateMovingPlayer(this.player);
                        this.allowedPlayerTicks = 20; // CraftBukkit
                    } else {
                        // CraftBukkit - Make sure the move is valid but then reset it for plugins to modify
                        double prevX = player.posX;
                        double prevY = player.posY;
                        double prevZ = player.posZ;
                        float prevYaw = player.rotationYaw;
                        float prevPitch = player.rotationPitch;
                        // CraftBukkit end
                        double d0 = this.player.posX;
                        double d1 = this.player.posY;
                        double d2 = this.player.posZ;
                        double d3 = this.player.posY;
                        double d4 = packetplayinflying.getX(this.player.posX);
                        double d5 = packetplayinflying.getY(this.player.posY);
                        double d6 = packetplayinflying.getZ(this.player.posZ);
                        float f = packetplayinflying.getYaw(this.player.rotationYaw);
                        float f1 = packetplayinflying.getPitch(this.player.rotationPitch);
                        double d7 = d4 - this.firstGoodX;
                        double d8 = d5 - this.firstGoodY;
                        double d9 = d6 - this.firstGoodZ;
                        double d10 = this.player.motionX * this.player.motionX + this.player.motionY * this.player.motionY + this.player.motionZ * this.player.motionZ;
                        double d11 = d7 * d7 + d8 * d8 + d9 * d9;

                        if (this.player.isPlayerSleeping()) {
                            if (d11 > 1.0D) {
                                this.setPlayerLocation(this.player.posX, this.player.posY, this.player.posZ, packetplayinflying.getYaw(this.player.rotationYaw), packetplayinflying.getPitch(this.player.rotationPitch));
                            }

                        } else {
                            ++this.movePacketCounter;
                            int i = this.movePacketCounter - this.lastMovePacketCounter;

                            // CraftBukkit start - handle custom speeds and skipped ticks
                            this.allowedPlayerTicks += (System.currentTimeMillis() / 50) - this.lastTick;
                            this.allowedPlayerTicks = Math.max(this.allowedPlayerTicks, 1);
                            this.lastTick = (int) (System.currentTimeMillis() / 50);

                            if (i > Math.max(this.allowedPlayerTicks, 5)) {
                                NetHandlerPlayServer.LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getName(), Integer.valueOf(i));
                                i = 1;
                            }

                            if (packetplayinflying.rotating || d11 > 0) {
                                allowedPlayerTicks -= 1;
                            } else {
                                allowedPlayerTicks = 20;
                            }
                            float speed;
                            if (player.capabilities.isFlying) {
                                speed = player.capabilities.flySpeed * 20f;
                            } else {
                                speed = player.capabilities.walkSpeed * 10f;
                            }

                            if (!this.player.isInvulnerableDimensionChange() && (!this.player.getServerWorld().getGameRules().getBoolean("disableElytraMovementCheck") || !this.player.isElytraFlying())) {
                                float f2 = this.player.isElytraFlying() ? 300.0F : 100.0F;

                                if (d11 - d10 > Math.max(f2, Math.pow(org.spigotmc.SpigotConfig.movedTooQuicklyMultiplier * i * speed, 2)) && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(this.player.getName()))) { // Spigot
                                // CraftBukkit end
                                    NetHandlerPlayServer.LOGGER.warn("{} moved too quickly! {},{},{}", this.player.getName(), Double.valueOf(d7), Double.valueOf(d8), Double.valueOf(d9));
                                    this.setPlayerLocation(this.player.posX, this.player.posY, this.player.posZ, this.player.rotationYaw, this.player.rotationPitch);
                                    return;
                                }
                            }

                            boolean flag = worldserver.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().shrink(0.0625D)).isEmpty();

                            d7 = d4 - this.lastGoodX;
                            d8 = d5 - this.lastGoodY;
                            d9 = d6 - this.lastGoodZ;
                            if (this.player.onGround && !packetplayinflying.isOnGround() && d8 > 0.0D) {
                                // Paper start - Add player jump event
                                Player player = this.getPlayer();
                                Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
                                Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

                                // If the packet contains movement information then we update the To location with the correct XYZ.
                                if (packetplayinflying.moving) {
                                    to.setX(packetplayinflying.x);
                                    to.setY(packetplayinflying.y);
                                    to.setZ(packetplayinflying.z);
                                }

                                // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
                                if (packetplayinflying.rotating) {
                                    to.setYaw(packetplayinflying.yaw);
                                    to.setPitch(packetplayinflying.pitch);
                                }

                                PlayerJumpEvent event = new PlayerJumpEvent(player, from, to);

                                if (event.callEvent()) {
                                    this.player.jump();
                                } else {
                                    from = event.getFrom();
                                    this.internalTeleport(from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch(), Collections.emptySet());
                                    return;
                                }
                                // Paper end
                            }

                            this.player.move(MoverType.PLAYER, d7, d8, d9);
                            this.player.onGround = packetplayinflying.isOnGround();
                            double d12 = d8;

                            d7 = d4 - this.player.posX;
                            d8 = d5 - this.player.posY;
                            if (d8 > -0.5D || d8 < 0.5D) {
                                d8 = 0.0D;
                            }

                            d9 = d6 - this.player.posZ;
                            d11 = d7 * d7 + d8 * d8 + d9 * d9;
                            boolean flag1 = false;

                            if (!this.player.isInvulnerableDimensionChange() && d11 > org.spigotmc.SpigotConfig.movedWronglyThreshold && !this.player.isPlayerSleeping() && !this.player.interactionManager.isCreative() && this.player.interactionManager.getGameType() != GameType.SPECTATOR) { // Spigot
                                flag1 = true;
                                NetHandlerPlayServer.LOGGER.warn("{} moved wrongly!", this.player.getName());
                            }

                            this.player.setPositionAndRotation(d4, d5, d6, f, f1);
                            this.player.addMovementStat(this.player.posX - d0, this.player.posY - d1, this.player.posZ - d2);
                            if (!this.player.noClip && !this.player.isPlayerSleeping()) {
                                boolean flag2 = worldserver.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().shrink(0.0625D)).isEmpty();

                                if (flag && (flag1 || !flag2)) {
                                    this.setPlayerLocation(d0, d1, d2, f, f1);
                                    return;
                                }
                            }

                            // CraftBukkit start - fire PlayerMoveEvent
                            // Rest to old location first
                            this.player.setPositionAndRotation(prevX, prevY, prevZ, prevYaw, prevPitch);

                            Player player = this.getPlayer();
                            Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
                            Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

                            // If the packet contains movement information then we update the To location with the correct XYZ.
                            if (packetplayinflying.moving) {
                                to.setX(packetplayinflying.x);
                                to.setY(packetplayinflying.y);
                                to.setZ(packetplayinflying.z);
                            }

                            // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
                            if (packetplayinflying.rotating) {
                                to.setYaw(packetplayinflying.yaw);
                                to.setPitch(packetplayinflying.pitch);
                            }

                            // Prevent 40 event-calls for less than a single pixel of movement >.>
                            double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
                            float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

                            if ((delta > 1f / 256 || deltaAngle > 10f) && !this.player.isMovementBlocked()) {
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
                                        this.player.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.PLUGIN);
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
                            this.player.setPositionAndRotation(d4, d5, d6, f, f1); // Copied from above
                            // CraftBukkit end

                            this.floating = d12 >= -0.03125D;
                            this.floating &= !this.serverController.isFlightAllowed() && !this.player.capabilities.allowFlying;
                            this.floating &= !this.player.isPotionActive(MobEffects.LEVITATION) && !this.player.isElytraFlying() && !worldserver.checkBlockCollision(this.player.getEntityBoundingBox().grow(0.0625D).expand(0.0D, -0.55D, 0.0D));
                            this.player.onGround = packetplayinflying.isOnGround();
                            this.serverController.getPlayerList().serverUpdateMovingPlayer(this.player);
                            this.player.handleFalling(this.player.posY - d3, packetplayinflying.isOnGround());
                            this.lastGoodX = this.player.posX;
                            this.lastGoodY = this.player.posY;
                            this.lastGoodZ = this.player.posZ;
                        }
                    }
                }
            }
        }
    }

    public void setPlayerLocation(double d0, double d1, double d2, float f, float f1) {
        this.setPlayerLocation(d0, d1, d2, f, f1, Collections.<SPacketPlayerPosLook.EnumFlags>emptySet());
    }

    // CraftBukkit start - Delegate to teleport(Location)
    public void a(double d0, double d1, double d2, float f, float f1, PlayerTeleportEvent.TeleportCause cause) {
        this.a(d0, d1, d2, f, f1, Collections.<SPacketPlayerPosLook.EnumFlags>emptySet(), cause);
    }

    public void setPlayerLocation(double d0, double d1, double d2, float f, float f1, Set<SPacketPlayerPosLook.EnumFlags> set) {
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
        double d3 = set.contains(SPacketPlayerPosLook.EnumFlags.X) ? this.player.posX : 0.0D;
        double d4 = set.contains(SPacketPlayerPosLook.EnumFlags.Y) ? this.player.posY : 0.0D;
        double d5 = set.contains(SPacketPlayerPosLook.EnumFlags.Z) ? this.player.posZ : 0.0D;

        this.targetPos = new Vec3d(d0 + d3, d1 + d4, d2 + d5);
        float f2 = f;
        float f3 = f1;

        if (set.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f2 = f + this.player.rotationYaw;
        }

        if (set.contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
            f3 = f1 + this.player.rotationPitch;
        }

        // CraftBukkit start - update last location
        this.lastPosX = this.targetPos.x;
        this.lastPosY = this.targetPos.y;
        this.lastPosZ = this.targetPos.z;
        this.lastYaw = f2;
        this.lastPitch = f3;
        // CraftBukkit end

        if (++this.teleportId == Integer.MAX_VALUE) {
            this.teleportId = 0;
        }

        this.lastPositionUpdate = this.networkTickCount;
        this.player.setPositionAndRotation(this.targetPos.x, this.targetPos.y, this.targetPos.z, f2, f3);
        this.player.connection.sendPacket(new SPacketPlayerPosLook(d0, d1, d2, f, f1, set, this.teleportId));
    }

    @Override
    public void processPlayerDigging(CPacketPlayerDigging packetplayinblockdig) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinblockdig, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        BlockPos blockposition = packetplayinblockdig.getPosition();

        this.player.markPlayerActive();
        switch (packetplayinblockdig.getAction()) {
        case SWAP_HELD_ITEMS:
            if (!this.player.isSpectator()) {
                ItemStack itemstack = this.player.getHeldItem(EnumHand.OFF_HAND);

                // CraftBukkit start
                PlayerSwapHandItemsEvent swapItemsEvent = new PlayerSwapHandItemsEvent(getPlayer(), CraftItemStack.asBukkitCopy(itemstack), CraftItemStack.asBukkitCopy(this.player.getHeldItem(EnumHand.MAIN_HAND)));
                this.server.getPluginManager().callEvent(swapItemsEvent);
                if (swapItemsEvent.isCancelled()) {
                    return;
                }
                itemstack = CraftItemStack.asNMSCopy(swapItemsEvent.getMainHandItem());
                this.player.setHeldItem(EnumHand.OFF_HAND, CraftItemStack.asNMSCopy(swapItemsEvent.getOffHandItem()));
                // CraftBukkit end
                this.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
            }

            return;

        case DROP_ITEM:
            if (!this.player.isSpectator()) {
                // limit how quickly items can be dropped
                // If the ticks aren't the same then the count starts from 0 and we update the lastDropTick.
                if (this.lastDropTick != MinecraftServer.currentTick) {
                    this.dropCount = 0;
                    this.lastDropTick = MinecraftServer.currentTick;
                } else {
                    // Else we increment the drop count and check the amount.
                    this.dropCount++;
                    if (this.dropCount >= 20) {
                        LOGGER.warn(this.player.getName() + " dropped their items too quickly!");
                        this.disconnect("You dropped your items too quickly (Hacking?)");
                        return;
                    }
                }
                // CraftBukkit end
                this.player.dropItem(false);
            }

            return;

        case DROP_ALL_ITEMS:
            if (!this.player.isSpectator()) {
                this.player.dropItem(true);
            }

            return;

        case RELEASE_USE_ITEM:
            this.player.stopActiveHand();
            return;

        case START_DESTROY_BLOCK:
        case ABORT_DESTROY_BLOCK:
        case STOP_DESTROY_BLOCK:
            double d0 = this.player.posX - (blockposition.getX() + 0.5D);
            double d1 = this.player.posY - (blockposition.getY() + 0.5D) + 1.5D;
            double d2 = this.player.posZ - (blockposition.getZ() + 0.5D);
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 > 36.0D) {
                this.sendPacket(new SPacketBlockChange(worldserver, blockposition)); // Paper - Fix block break desync
                return;
            } else if (blockposition.getY() >= this.serverController.getBuildLimit()) {
                return;
            } else {
                if (packetplayinblockdig.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    if (!this.serverController.isBlockProtected(worldserver, blockposition, this.player) && worldserver.getWorldBorder().contains(blockposition)) {
                        this.player.interactionManager.onBlockClicked(blockposition, packetplayinblockdig.getFacing());
                    } else {
                        // CraftBukkit start - fire PlayerInteractEvent
                        CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, blockposition, packetplayinblockdig.getFacing(), this.player.inventory.getCurrentItem(), EnumHand.MAIN_HAND);
                        this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockposition));
                        // Update any tile entity data for this block
                        TileEntity tileentity = worldserver.getTileEntity(blockposition);
                        if (tileentity != null) {
                            this.player.connection.sendPacket(tileentity.getUpdatePacket());
                        }
                        // CraftBukkit end
                    }
                } else {
                    if (packetplayinblockdig.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.player.interactionManager.blockRemoving(blockposition);
                    } else if (packetplayinblockdig.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                        this.player.interactionManager.cancelDestroyingBlock();
                    }

                    if (worldserver.getBlockState(blockposition).getMaterial() != Material.AIR) {
                        this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockposition));
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

    @Override
    public void processTryUseItemOnBlock(CPacketPlayerTryUseItemOnBlock packetplayinuseitem) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinuseitem, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        if (!checkLimit(packetplayinuseitem.timestamp)) return; // Spigot - check limit
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        EnumHand enumhand = packetplayinuseitem.getHand();
        ItemStack itemstack = this.player.getHeldItem(enumhand);
        BlockPos blockposition = packetplayinuseitem.getPos();
        EnumFacing enumdirection = packetplayinuseitem.getDirection();

        this.player.markPlayerActive();
        if (blockposition.getY() >= this.serverController.getBuildLimit() - 1 && (enumdirection == EnumFacing.UP || blockposition.getY() >= this.serverController.getBuildLimit())) {
            TextComponentTranslation chatmessage = new TextComponentTranslation("build.tooHigh", new Object[] { Integer.valueOf(this.serverController.getBuildLimit())});

            chatmessage.getStyle().setColor(TextFormatting.RED);
            this.player.connection.sendPacket(new SPacketChat(chatmessage, ChatType.GAME_INFO));
        } else if (this.targetPos == null && this.player.getDistanceSq(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D, blockposition.getZ() + 0.5D) < 64.0D && !this.serverController.isBlockProtected(worldserver, blockposition, this.player) && worldserver.getWorldBorder().contains(blockposition)) {
            // CraftBukkit start - Check if we can actually do something over this large a distance
            Location eyeLoc = this.getPlayer().getEyeLocation();
            double reachDistance = NumberConversions.square(eyeLoc.getX() - blockposition.getX()) + NumberConversions.square(eyeLoc.getY() - blockposition.getY()) + NumberConversions.square(eyeLoc.getZ() - blockposition.getZ());
            if (reachDistance > (this.getPlayer().getGameMode() == org.bukkit.GameMode.CREATIVE ? CREATIVE_PLACE_DISTANCE_SQUARED : SURVIVAL_PLACE_DISTANCE_SQUARED)) {
                return;
            }
            // CraftBukkit end
            this.player.interactionManager.processRightClickBlock(this.player, worldserver, itemstack, enumhand, blockposition, enumdirection, packetplayinuseitem.getFacingX(), packetplayinuseitem.getFacingY(), packetplayinuseitem.getFacingZ());
        }

        this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockposition));
        this.player.connection.sendPacket(new SPacketBlockChange(worldserver, blockposition.offset(enumdirection)));
    }

    @Override
    public void processTryUseItem(CPacketPlayerTryUseItem packetplayinblockplace) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinblockplace, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        if (!checkLimit(packetplayinblockplace.timestamp)) return; // Spigot - check limit
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        EnumHand enumhand = packetplayinblockplace.getHand();
        ItemStack itemstack = this.player.getHeldItem(enumhand);

        this.player.markPlayerActive();
        if (!itemstack.isEmpty()) {
            // CraftBukkit start
            // Raytrace to look for 'rogue armswings'
            float f1 = this.player.rotationPitch;
            float f2 = this.player.rotationYaw;
            double d0 = this.player.posX;
            double d1 = this.player.posY + this.player.getEyeHeight();
            double d2 = this.player.posZ;
            Vec3d vec3d = new Vec3d(d0, d1, d2);

            float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
            float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            double d3 = player.interactionManager.getGameType()== GameType.CREATIVE ? 5.0D : 4.5D;
            Vec3d vec3d1 = vec3d.addVector(f7 * d3, f6 * d3, f8 * d3);
            RayTraceResult movingobjectposition = this.player.world.rayTraceBlocks(vec3d, vec3d1, false);

            boolean cancelled;
            if (movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK) {
                org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack, enumhand);
                cancelled = event.useItemInHand() == Event.Result.DENY;
            } else {
                if (player.interactionManager.firedInteract) {
                    player.interactionManager.firedInteract = false;
                    cancelled = player.interactionManager.interactResult;
                } else {
                    org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, movingobjectposition.getBlockPos(), movingobjectposition.sideHit, itemstack, true, enumhand);
                    cancelled = event.useItemInHand() == Event.Result.DENY;
                }
            }

            if (cancelled) {
                this.player.getBukkitEntity().updateInventory(); // SPIGOT-2524
            } else {
                this.player.interactionManager.processRightClick(this.player, worldserver, itemstack, enumhand);
            }
            // CraftBukkit end
        }
    }

    @Override
    public void handleSpectate(CPacketSpectate packetplayinspectate) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinspectate, this, this.player.getServerWorld());
        if (this.player.isSpectator()) {
            Entity entity = null;
            WorldServer[] aworldserver = this.serverController.worlds;
            int i = aworldserver.length;

            // CraftBukkit - use the worlds array list
            for (WorldServer worldserver : serverController.worlds) {

                if (worldserver != null) {
                    entity = packetplayinspectate.getEntity(worldserver);
                    if (entity != null) {
                        break;
                    }
                }
            }

            if (entity != null) {
                this.player.setSpectatingEntity(this.player);
                this.player.dismountRidingEntity();

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
                this.player.getBukkitEntity().teleport(entity.getBukkitEntity(), PlayerTeleportEvent.TeleportCause.SPECTATE);
                // CraftBukkit end
            }
        }

    }

    // CraftBukkit start
    @Override
    public void handleResourcePackStatus(CPacketResourcePackStatus packetplayinresourcepackstatus) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinresourcepackstatus, this, this.player.getServerWorld());
        // Paper start
        //this.server.getPluginManager().callEvent(new PlayerResourcePackStatusEvent(getPlayer(), PlayerResourcePackStatusEvent.Status.values()[packetplayinresourcepackstatus.status.ordinal()]));
        final PlayerResourcePackStatusEvent.Status status = PlayerResourcePackStatusEvent.Status.values()[packetplayinresourcepackstatus.action.ordinal()];
        this.getPlayer().setResourcePackStatus(status);
        this.server.getPluginManager().callEvent(new PlayerResourcePackStatusEvent(getPlayer(), status));
        // Paper end
    }
    // CraftBukkit end

    @Override
    public void processSteerBoat(CPacketSteerBoat packetplayinboatmove) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinboatmove, this, this.player.getServerWorld());
        Entity entity = this.player.getRidingEntity();

        if (entity instanceof EntityBoat) {
            ((EntityBoat) entity).setPaddleState(packetplayinboatmove.getLeft(), packetplayinboatmove.getRight());
        }

    }

    @Override
    public void onDisconnect(ITextComponent ichatbasecomponent) {
        // CraftBukkit start - Rarely it would send a disconnect line twice
        if (this.processedDisconnect) {
            return;
        } else {
            this.processedDisconnect = true;
        }
        // CraftBukkit end
        NetHandlerPlayServer.LOGGER.info("{} lost connection: {}", this.player.getName(), ichatbasecomponent.getUnformattedText());
        // CraftBukkit start - Replace vanilla quit message handling with our own.
        /*
        this.minecraftServer.aD();
        ChatMessage chatmessage = new ChatMessage("multiplayer.player.left", new Object[] { this.player.getScoreboardDisplayName()});

        chatmessage.getChatModifier().setColor(EnumChatFormat.YELLOW);
        this.minecraftServer.getPlayerList().sendMessage(chatmessage);
        */

        this.player.mountEntityAndWakeUp();
        String quitMessage = this.serverController.getPlayerList().disconnect(this.player);
        if ((quitMessage != null) && (quitMessage.length() > 0)) {
            this.serverController.getPlayerList().sendMessage(CraftChatMessage.fromString(quitMessage));
        }
        // CraftBukkit end
        if (this.serverController.isSinglePlayer() && this.player.getName().equals(this.serverController.getServerOwner())) {
            NetHandlerPlayServer.LOGGER.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }

    }

    public void sendPacket(final Packet<?> packet) {
        if (packet instanceof SPacketChat) {
            SPacketChat packetplayoutchat = (SPacketChat) packet;
            EntityPlayer.EnumChatVisibility entityhuman_enumchatvisibility = this.player.getChatVisibility();

            if (entityhuman_enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN && packetplayoutchat.getType() != ChatType.GAME_INFO) {
                return;
            }

            if (entityhuman_enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !packetplayoutchat.isSystem()) {
                return;
            }
        }

        // CraftBukkit start
        if (packet == null || this.processedDisconnect) { // Spigot
            return;
        } else if (packet instanceof SPacketSpawnPosition) {
            SPacketSpawnPosition packet6 = (SPacketSpawnPosition) packet;
            this.player.compassTarget = new Location(this.getPlayer().getWorld(), packet6.spawnBlockPos.getX(), packet6.spawnBlockPos.getY(), packet6.spawnBlockPos.getZ());
        }
        // CraftBukkit end

        try {
            this.netManager.sendPacket(packet);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Packet being sent");

            crashreportsystemdetails.addDetail("Packet class", new ICrashReportDetail() {
                public String a() throws Exception {
                    return packet.getClass().getCanonicalName();
                }

                @Override
                public Object call() throws Exception {
                    return this.a();
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public void processHeldItemChange(CPacketHeldItemChange packetplayinhelditemslot) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinhelditemslot, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        if (packetplayinhelditemslot.getSlotId() >= 0 && packetplayinhelditemslot.getSlotId() < InventoryPlayer.getHotbarSize()) {
            PlayerItemHeldEvent event = new PlayerItemHeldEvent(this.getPlayer(), this.player.inventory.currentItem, packetplayinhelditemslot.getSlotId());
            this.server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                this.sendPacket(new SPacketHeldItemChange(this.player.inventory.currentItem));
                this.player.markPlayerActive();
                return;
            }
            // CraftBukkit end
            this.player.inventory.currentItem = packetplayinhelditemslot.getSlotId();
            this.player.markPlayerActive();
        } else {
            NetHandlerPlayServer.LOGGER.warn("{} tried to set an invalid carried item", this.player.getName());
            this.disconnect("Invalid hotbar selection (Hacking?)"); // CraftBukkit //Spigot "Nope" -> Descriptive reason
        }
    }

    @Override
    public void processChatMessage(CPacketChatMessage packetplayinchat) {
        // CraftBukkit start - async chat
        // SPIGOT-3638
        if (this.serverController.isServerStopped()) {
            return;
        }

        boolean isSync = packetplayinchat.getMessage().startsWith("/");
        if (packetplayinchat.getMessage().startsWith("/")) {
            PacketThreadUtil.checkThreadAndEnqueue(packetplayinchat, this, this.player.getServerWorld());
        }
        // CraftBukkit end
        if (this.player.isDead || this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) { // CraftBukkit - dead men tell no tales
            TextComponentTranslation chatmessage = new TextComponentTranslation("chat.cannotSend", new Object[0]);

            chatmessage.getStyle().setColor(TextFormatting.RED);
            this.sendPacket(new SPacketChat(chatmessage));
        } else {
            this.player.markPlayerActive();
            String s = packetplayinchat.getMessage();

            s = StringUtils.normalizeSpace(s);

            for (int i = 0; i < s.length(); ++i) {
                if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i))) {
                    // CraftBukkit start - threadsafety
                    if (!isSync) {
                        Waitable waitable = new Waitable() {
                            @Override
                            protected Object evaluate() {
                                NetHandlerPlayServer.this.disconnect(new TextComponentTranslation("multiplayer.disconnect.illegal_characters", new Object[0]));
                                return null;
                            }
                        };

                        this.serverController.processQueue.add(waitable);

                        try {
                            waitable.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        this.disconnect(new TextComponentTranslation("multiplayer.disconnect.illegal_characters", new Object[0]));
                    }
                    // CraftBukkit end
                    return;
                }
            }

            // CraftBukkit start
            if (isSync) {
                try {
                    this.serverController.server.playerCommandState = true;
                    this.handleSlashCommand(s);
                } finally {
                    this.serverController.server.playerCommandState = false;
                }
            } else if (s.isEmpty()) {
                LOGGER.warn(this.player.getName() + " tried to send an empty message");
            } else if (getPlayer().isConversing()) {
                // Spigot start
                final String message = s;
                this.serverController.processQueue.add( new Waitable()
                {
                    @Override
                    protected Object evaluate()
                    {
                        getPlayer().acceptConversationInput( message );
                        return null;
                    }
                } );
                // Spigot end
            } else if (this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.SYSTEM) { // Re-add "Command Only" flag check
                TextComponentTranslation chatmessage = new TextComponentTranslation("chat.cannotSend", new Object[0]);

                chatmessage.getStyle().setColor(TextFormatting.RED);
                this.sendPacket(new SPacketChat(chatmessage));
            } else if (true) {
                this.chat(s, true);
                // CraftBukkit end - the below is for reference. :)
            } else {
                TextComponentTranslation chatmessage1 = new TextComponentTranslation("chat.type.text", new Object[] { this.player.getDisplayName(), s});

                this.serverController.getPlayerList().sendMessage(chatmessage1, false);
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
            if (counted && chatSpamField.addAndGet(this, 20) > 200 && !this.serverController.getPlayerList().canSendCommands(this.player.getGameProfile())) { // Spigot
                if (!isSync) {
                    Waitable waitable = new Waitable() {
                        @Override
                        protected Object evaluate() {
                            NetHandlerPlayServer.this.disconnect(new TextComponentTranslation("disconnect.spam", new Object[0]));
                            return null;
                        }
                    };

                    this.serverController.processQueue.add(waitable);

                    try {
                        waitable.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.disconnect(new TextComponentTranslation("disconnect.spam", new Object[0]));
                }
                // CraftBukkit end
            }

        }
    }

    // CraftBukkit start - add method
    public void chat(String s, boolean async) {
        if (s.isEmpty() || this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            return;
        }

        if (!async && s.startsWith("/")) {
            // Paper Start
            if (!org.spigotmc.AsyncCatcher.shuttingDown && !org.bukkit.Bukkit.isPrimaryThread()) {
                final String fCommandLine = s;
                MinecraftServer.LOGGER.log(org.apache.logging.log4j.Level.ERROR, "Command Dispatched Async: " + fCommandLine);
                MinecraftServer.LOGGER.log(org.apache.logging.log4j.Level.ERROR, "Please notify author of plugin causing this execution to fix this bug! see: http://bit.ly/1oSiM6C", new Throwable());
                Waitable wait = new Waitable() {
                    @Override
                    protected Object evaluate() {
                        chat(fCommandLine, false);
                        return null;
                    }
                };
                serverController.processQueue.add(wait);
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
            this.handleSlashCommand(s);
        } else if (this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.SYSTEM) {
            // Do nothing, this is coming from a plugin
        } else {
            Player player = this.getPlayer();
            AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet(serverController));
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
                        NetHandlerPlayServer.this.serverController.console.sendMessage(message);
                        if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                            for (Object player : NetHandlerPlayServer.this.serverController.getPlayerList().playerEntityList) {
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
                    serverController.processQueue.add(waitable);
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
                if (this.player.getEntityWorld().paperConfig.useVanillaScoreboardColoring) {
                    displayName = ScorePlayerTeam.formatPlayerName(this.player.getTeam(), player.getDisplayName());
                }

                s = String.format(event.getFormat(), displayName, event.getMessage());
                // Paper end
                serverController.console.sendMessage(s);
                if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
                    for (Object recipient : serverController.getPlayerList().playerEntityList) {
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

    private void handleSlashCommand(String s) {
        MinecraftTimings.playerCommandTimer.startTiming(); // Paper
       // CraftBukkit start - whole method
        if ( org.spigotmc.SpigotConfig.logCommands ) // Spigot
        this.LOGGER.info(this.player.getName() + " issued server command: " + s);

        CraftPlayer player = this.getPlayer();

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet(serverController));
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

    @Override
    public void handleAnimation(CPacketAnimation packetplayinarmanimation) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinarmanimation, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        this.player.markPlayerActive();
        // CraftBukkit start - Raytrace to look for 'rogue armswings'
        float f1 = this.player.rotationPitch;
        float f2 = this.player.rotationYaw;
        double d0 = this.player.posX;
        double d1 = this.player.posY + this.player.getEyeHeight();
        double d2 = this.player.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);

        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = player.interactionManager.getGameType()== GameType.CREATIVE ? 5.0D : 4.5D;
        Vec3d vec3d1 = vec3d.addVector(f7 * d3, f6 * d3, f8 * d3);
        RayTraceResult movingobjectposition = this.player.world.rayTraceBlocks(vec3d, vec3d1, false);

        if (movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK) {
            CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR, this.player.inventory.getCurrentItem(), EnumHand.MAIN_HAND);
        }

        // Arm swing animation
        PlayerAnimationEvent event = new PlayerAnimationEvent(this.getPlayer());
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        // CraftBukkit end
        this.player.swingArm(packetplayinarmanimation.getHand());
    }

    @Override
    public void processEntityAction(CPacketEntityAction packetplayinentityaction) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinentityaction, this, this.player.getServerWorld());
        // CraftBukkit start
        if (this.player.isDead) return;
        switch (packetplayinentityaction.getAction()) {
            case START_SNEAKING:
            case STOP_SNEAKING:
                PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(this.getPlayer(), packetplayinentityaction.getAction() == CPacketEntityAction.Action.START_SNEAKING);
                this.server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                break;
            case START_SPRINTING:
            case STOP_SPRINTING:
                PlayerToggleSprintEvent e2 = new PlayerToggleSprintEvent(this.getPlayer(), packetplayinentityaction.getAction() == CPacketEntityAction.Action.START_SPRINTING);
                this.server.getPluginManager().callEvent(e2);

                if (e2.isCancelled()) {
                    return;
                }
                break;
        }
        // CraftBukkit end
        this.player.markPlayerActive();
        IJumpingMount ijumpable;

        switch (packetplayinentityaction.getAction()) {
        case START_SNEAKING:
            this.player.setSneaking(true);

            // Paper start - Hang on!
            if (this.player.world.paperConfig.parrotsHangOnBetter) {
                this.player.spawnShoulderEntities();
            }
            // Paper end

            break;

        case STOP_SNEAKING:
            this.player.setSneaking(false);
            break;

        case START_SPRINTING:
            this.player.setSprinting(true);
            break;

        case STOP_SPRINTING:
            this.player.setSprinting(false);
            break;

        case STOP_SLEEPING:
            if (this.player.isPlayerSleeping()) {
                this.player.wakeUpPlayer(false, true, true);
                this.targetPos = new Vec3d(this.player.posX, this.player.posY, this.player.posZ);
            }
            break;

        case START_RIDING_JUMP:
            if (this.player.getRidingEntity() instanceof IJumpingMount) {
                ijumpable = (IJumpingMount) this.player.getRidingEntity();
                int i = packetplayinentityaction.getAuxData();

                if (ijumpable.canJump() && i > 0) {
                    ijumpable.handleStartJump(i);
                }
            }
            break;

        case STOP_RIDING_JUMP:
            if (this.player.getRidingEntity() instanceof IJumpingMount) {
                ijumpable = (IJumpingMount) this.player.getRidingEntity();
                ijumpable.handleStopJump();
            }
            break;

        case OPEN_INVENTORY:
            if (this.player.getRidingEntity() instanceof AbstractHorse) {
                ((AbstractHorse) this.player.getRidingEntity()).openGUI(this.player);
            }
            break;

        case START_FALL_FLYING:
            if (!this.player.onGround && this.player.motionY < 0.0D && !this.player.isElytraFlying() && !this.player.isInWater()) {
                ItemStack itemstack = this.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack)) {
                    this.player.setElytraFlying();
                }
            } else {
                this.player.clearElytraFlying();
            }
            break;

        default:
            throw new IllegalArgumentException("Invalid client command!");
        }

    }

    @Override
    public void processUseEntity(CPacketUseEntity packetplayinuseentity) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinuseentity, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        Entity entity = packetplayinuseentity.getEntityFromWorld(worldserver);
        // Spigot Start
        if ( entity == player && !player.isSpectator() )
        {
            disconnect( "Cannot interact with self!" );
            return;
        }
        // Spigot End

        this.player.markPlayerActive();
        if (entity != null) {
            boolean flag = this.player.canEntityBeSeen(entity);
            double d0 = 36.0D;

            if (!flag) {
                d0 = 9.0D;
            }

            if (this.player.getDistanceSq(entity) < d0) {
                EnumHand enumhand;

                ItemStack itemInHand = this.player.getHeldItem(packetplayinuseentity.getHand() == null ? EnumHand.MAIN_HAND : packetplayinuseentity.getHand()); // CraftBukkit

                if (packetplayinuseentity.getAction() == CPacketUseEntity.Action.INTERACT
                        || packetplayinuseentity.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                    // CraftBukkit start
                    boolean triggerLeashUpdate = itemInHand != null && itemInHand.getItem() == Items.LEAD && entity instanceof EntityLiving;
                    Item origItem = this.player.inventory.getCurrentItem() == null ? null : this.player.inventory.getCurrentItem().getItem();
                    PlayerInteractEntityEvent event;
                    if (packetplayinuseentity.getAction() == CPacketUseEntity.Action.INTERACT) {
                        event = new PlayerInteractEntityEvent(this.getPlayer(), entity.getBukkitEntity(), (packetplayinuseentity.getHand() == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
                    } else {
                        Vec3d target = packetplayinuseentity.getHitVec();
                        event = new PlayerInteractAtEntityEvent(this.getPlayer(), entity.getBukkitEntity(), new org.bukkit.util.Vector(target.x, target.y, target.z), (packetplayinuseentity.getHand() == EnumHand.OFF_HAND) ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
                    }
                    this.server.getPluginManager().callEvent(event);

                    if (triggerLeashUpdate && (event.isCancelled() || this.player.inventory.getCurrentItem() == null || this.player.inventory.getCurrentItem().getItem() != Items.LEAD)) {
                        // Refresh the current leash state
                        this.sendPacket(new SPacketEntityAttach(entity, ((EntityLiving) entity).getLeashHolder()));
                    }

                    if (event.isCancelled() || this.player.inventory.getCurrentItem() == null || this.player.inventory.getCurrentItem().getItem() != origItem) {
                        // Refresh the current entity metadata
                        this.sendPacket(new SPacketEntityMetadata(entity.getEntityId(), entity.dataManager, true));
                    }

                    if (event.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                }

                if (packetplayinuseentity.getAction() == CPacketUseEntity.Action.INTERACT) {
                    enumhand = packetplayinuseentity.getHand();
                    this.player.interactOn(entity, enumhand);
                    // CraftBukkit start
                    if (!itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
                        this.player.sendContainerToPlayer(this.player.openContainer);
                    }
                    // CraftBukkit end
                } else if (packetplayinuseentity.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                    enumhand = packetplayinuseentity.getHand();
                    entity.applyPlayerInteraction(this.player, packetplayinuseentity.getHitVec(), enumhand);
                    // CraftBukkit start
                    if (!itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
                        this.player.sendContainerToPlayer(this.player.openContainer);
                    }
                    // CraftBukkit end
                } else if (packetplayinuseentity.getAction() == CPacketUseEntity.Action.ATTACK) {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || (entity == this.player && !player.isSpectator())) { // CraftBukkit
                        this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_entity_attacked", new Object[0]));
                        this.serverController.logWarning("Player " + this.player.getName() + " tried to attack an invalid entity");
                        return;
                    }

                    this.player.attackTargetEntityWithCurrentItem(entity);

                    // CraftBukkit start
                    if (!itemInHand.isEmpty() && itemInHand.getCount() <= -1) {
                        this.player.sendContainerToPlayer(this.player.openContainer);
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
                packetplayinuseentity.getAction() == CPacketUseEntity.Action.ATTACK,
                packetplayinuseentity.getHand() == EnumHand.MAIN_HAND ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND
            ));
        }
        // Paper end

    }

    @Override
    public void processClientStatus(CPacketClientStatus packetplayinclientcommand) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinclientcommand, this, this.player.getServerWorld());
        this.player.markPlayerActive();
        CPacketClientStatus.State packetplayinclientcommand_enumclientcommand = packetplayinclientcommand.getStatus();

        switch (packetplayinclientcommand_enumclientcommand) {
        case PERFORM_RESPAWN:
            if (this.player.queuedEndExit) {
                this.player.queuedEndExit = false;
                // this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, 0, true);
                this.serverController.getPlayerList().changeDimension(this.player, 0, PlayerTeleportEvent.TeleportCause.END_PORTAL); // CraftBukkit - reroute logic through custom portal management
                CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, DimensionType.THE_END, DimensionType.OVERWORLD);
            } else {
                if (this.player.getHealth() > 0.0F) {
                    return;
                }

                this.player = this.serverController.getPlayerList().recreatePlayerEntity(this.player, 0, false);
                if (this.serverController.isHardcore()) {
                    this.player.setGameType(GameType.SPECTATOR);
                    this.player.getServerWorld().getGameRules().setOrCreateGameRule("spectatorsGenerateChunks", "false");
                }
            }
            break;

        case REQUEST_STATS:
            this.player.getStatFile().sendStats(this.player);
        }

    }

    @Override
    public void processCloseWindow(CPacketCloseWindow packetplayinclosewindow) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinclosewindow, this, this.player.getServerWorld());

        if (this.player.isMovementBlocked()) return; // CraftBukkit
        CraftEventFactory.handleInventoryCloseEvent(this.player); // CraftBukkit

        this.player.closeContainer();
    }

    @Override
    public void processClickWindow(CPacketClickWindow packetplayinwindowclick) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinwindowclick, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        this.player.markPlayerActive();
        if (this.player.openContainer.windowId == packetplayinwindowclick.getWindowId() && this.player.openContainer.getCanCraft(this.player) && this.player.openContainer.canInteractWith(this.player)) { // CraftBukkit
            boolean cancelled = this.player.isSpectator(); // CraftBukkit - see below if
            if (false/*this.player.isSpectator()*/) { // CraftBukkit
                NonNullList nonnulllist = NonNullList.create();

                for (int i = 0; i < this.player.openContainer.inventorySlots.size(); ++i) {
                    nonnulllist.add(this.player.openContainer.inventorySlots.get(i).getStack());
                }

                this.player.sendAllContents(this.player.openContainer, nonnulllist);
            } else {
                // CraftBukkit start - Call InventoryClickEvent
                if (packetplayinwindowclick.getSlotId() < -1 && packetplayinwindowclick.getSlotId() != -999) {
                    return;
                }

                InventoryView inventory = this.player.openContainer.getBukkitView();
                SlotType type = CraftInventoryView.getSlotType(inventory, packetplayinwindowclick.getSlotId());

                InventoryClickEvent event;
                org.bukkit.event.inventory.ClickType click = org.bukkit.event.inventory.ClickType.UNKNOWN;
                InventoryAction action = InventoryAction.UNKNOWN;

                ItemStack itemstack = ItemStack.EMPTY;

                switch (packetplayinwindowclick.getClickType()) {
                    case PICKUP:
                        if (packetplayinwindowclick.getUsedButton() == 0) {
                            click = org.bukkit.event.inventory.ClickType.LEFT;
                        } else if (packetplayinwindowclick.getUsedButton() == 1) {
                            click = org.bukkit.event.inventory.ClickType.RIGHT;
                        }
                        if (packetplayinwindowclick.getUsedButton() == 0 || packetplayinwindowclick.getUsedButton() == 1) {
                            action = InventoryAction.NOTHING; // Don't want to repeat ourselves
                            if (packetplayinwindowclick.getSlotId() == -999) {
                                if (!player.inventory.getItemStack().isEmpty()) {
                                    action = packetplayinwindowclick.getUsedButton() == 0 ? InventoryAction.DROP_ALL_CURSOR : InventoryAction.DROP_ONE_CURSOR;
                                }
                            } else if (packetplayinwindowclick.getSlotId() < 0)  {
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.player.openContainer.getSlot(packetplayinwindowclick.getSlotId());
                                if (slot != null) {
                                    ItemStack clickedItem = slot.getStack();
                                    ItemStack cursor = player.inventory.getItemStack();
                                    if (clickedItem.isEmpty()) {
                                        if (!cursor.isEmpty()) {
                                            action = packetplayinwindowclick.getUsedButton() == 0 ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_ONE;
                                        }
                                    } else if (slot.canTakeStack(player)) {
                                        if (cursor.isEmpty()) {
                                            action = packetplayinwindowclick.getUsedButton() == 0 ? InventoryAction.PICKUP_ALL : InventoryAction.PICKUP_HALF;
                                        } else if (slot.isItemValid(cursor)) {
                                            if (clickedItem.isItemEqual(cursor) && ItemStack.areItemStackTagsEqual(clickedItem, cursor)) {
                                                int toPlace = packetplayinwindowclick.getUsedButton() == 0 ? cursor.getCount() : 1;
                                                toPlace = Math.min(toPlace, clickedItem.getMaxStackSize() - clickedItem.getCount());
                                                toPlace = Math.min(toPlace, slot.inventory.getInventoryStackLimit() - clickedItem.getCount());
                                                if (toPlace == 1) {
                                                    action = InventoryAction.PLACE_ONE;
                                                } else if (toPlace == cursor.getCount()) {
                                                    action = InventoryAction.PLACE_ALL;
                                                } else if (toPlace < 0) {
                                                    action = toPlace != -1 ? InventoryAction.PICKUP_SOME : InventoryAction.PICKUP_ONE; // this happens with oversized stacks
                                                } else if (toPlace != 0) {
                                                    action = InventoryAction.PLACE_SOME;
                                                }
                                            } else if (cursor.getCount() <= slot.getSlotStackLimit()) {
                                                action = InventoryAction.SWAP_WITH_CURSOR;
                                            }
                                        } else if (cursor.getItem() == clickedItem.getItem() && (!cursor.getHasSubtypes() || cursor.getMetadata() == clickedItem.getMetadata()) && ItemStack.areItemStackTagsEqual(cursor, clickedItem)) {
                                            if (clickedItem.getCount() >= 0) {
                                                if (clickedItem.getCount() + cursor.getCount() <= cursor.getMaxStackSize()) {
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
                        if (packetplayinwindowclick.getUsedButton() == 0) {
                            click = org.bukkit.event.inventory.ClickType.SHIFT_LEFT;
                        } else if (packetplayinwindowclick.getUsedButton() == 1) {
                            click = org.bukkit.event.inventory.ClickType.SHIFT_RIGHT;
                        }
                        if (packetplayinwindowclick.getUsedButton() == 0 || packetplayinwindowclick.getUsedButton() == 1) {
                            if (packetplayinwindowclick.getSlotId() < 0) {
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.player.openContainer.getSlot(packetplayinwindowclick.getSlotId());
                                if (slot != null && slot.canTakeStack(this.player) && slot.getHasStack()) {
                                    action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        }
                        break;
                    case SWAP:
                        if (packetplayinwindowclick.getUsedButton() >= 0 && packetplayinwindowclick.getUsedButton() < 9) {
                            click = org.bukkit.event.inventory.ClickType.NUMBER_KEY;
                            Slot clickedSlot = this.player.openContainer.getSlot(packetplayinwindowclick.getSlotId());
                            if (clickedSlot.canTakeStack(player)) {
                                ItemStack hotbar = this.player.inventory.getStackInSlot(packetplayinwindowclick.getUsedButton());
                                boolean canCleanSwap = hotbar.isEmpty() || (clickedSlot.inventory == player.inventory && clickedSlot.isItemValid(hotbar)); // the slot will accept the hotbar item
                                if (clickedSlot.getHasStack()) {
                                    if (canCleanSwap) {
                                        action = InventoryAction.HOTBAR_SWAP;
                                    } else {
                                        action = InventoryAction.HOTBAR_MOVE_AND_READD;
                                    }
                                } else if (!clickedSlot.getHasStack() && !hotbar.isEmpty() && clickedSlot.isItemValid(hotbar)) {
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
                        if (packetplayinwindowclick.getUsedButton() == 2) {
                            click = org.bukkit.event.inventory.ClickType.MIDDLE;
                            if (packetplayinwindowclick.getSlotId() < 0) { // Paper - GH-404
                                action = InventoryAction.NOTHING;
                            } else {
                                Slot slot = this.player.openContainer.getSlot(packetplayinwindowclick.getSlotId());
                                if (slot != null && slot.getHasStack() && player.capabilities.isCreativeMode && player.inventory.getItemStack().isEmpty()) {
                                    action = InventoryAction.CLONE_STACK;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        } else {
                            click = org.bukkit.event.inventory.ClickType.UNKNOWN;
                            action = InventoryAction.UNKNOWN;
                        }
                        break;
                    case THROW:
                        if (packetplayinwindowclick.getSlotId() >= 0) {
                            if (packetplayinwindowclick.getUsedButton() == 0) {
                                click = org.bukkit.event.inventory.ClickType.DROP;
                                Slot slot = this.player.openContainer.getSlot(packetplayinwindowclick.getSlotId());
                                if (slot != null && slot.getHasStack() && slot.canTakeStack(player) && !slot.getStack().isEmpty() && slot.getStack().getItem() != Item.getItemFromBlock(Blocks.AIR)) {
                                    action = InventoryAction.DROP_ONE_SLOT;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            } else if (packetplayinwindowclick.getUsedButton() == 1) {
                                click = org.bukkit.event.inventory.ClickType.CONTROL_DROP;
                                Slot slot = this.player.openContainer.getSlot(packetplayinwindowclick.getSlotId());
                                if (slot != null && slot.getHasStack() && slot.canTakeStack(player) && !slot.getStack().isEmpty() && slot.getStack().getItem() != Item.getItemFromBlock(Blocks.AIR)) {
                                    action = InventoryAction.DROP_ALL_SLOT;
                                } else {
                                    action = InventoryAction.NOTHING;
                                }
                            }
                        } else {
                            // Sane default (because this happens when they are holding nothing. Don't ask why.)
                            click = org.bukkit.event.inventory.ClickType.LEFT;
                            if (packetplayinwindowclick.getUsedButton() == 1) {
                                click = org.bukkit.event.inventory.ClickType.RIGHT;
                            }
                            action = InventoryAction.NOTHING;
                        }
                        break;
                    case QUICK_CRAFT:
                        itemstack = this.player.openContainer.slotClick(packetplayinwindowclick.getSlotId(), packetplayinwindowclick.getUsedButton(), packetplayinwindowclick.getClickType(), this.player);
                        break;
                    case PICKUP_ALL:
                        click = org.bukkit.event.inventory.ClickType.DOUBLE_CLICK;
                        action = InventoryAction.NOTHING;
                        if (packetplayinwindowclick.getSlotId() >= 0 && !this.player.inventory.getItemStack().isEmpty()) {
                            ItemStack cursor = this.player.inventory.getItemStack();
                            action = InventoryAction.NOTHING;
                            // Quick check for if we have any of the item
                            if (inventory.getTopInventory().contains(org.bukkit.Material.getMaterial(Item.getIdFromItem(cursor.getItem()))) || inventory.getBottomInventory().contains(org.bukkit.Material.getMaterial(Item.getIdFromItem(cursor.getItem())))) {
                                action = InventoryAction.COLLECT_TO_CURSOR;
                            }
                        }
                        break;
                    default:
                        break;
                }

                if (packetplayinwindowclick.getClickType() != ClickType.QUICK_CRAFT) {
                    if (click == org.bukkit.event.inventory.ClickType.NUMBER_KEY) {
                        event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.getSlotId(), click, action, packetplayinwindowclick.getUsedButton());
                    } else {
                        event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.getSlotId(), click, action);
                    }

                    org.bukkit.inventory.Inventory top = inventory.getTopInventory();
                    if (packetplayinwindowclick.getSlotId() == 0 && top instanceof CraftingInventory) {
                        org.bukkit.inventory.Recipe recipe = ((CraftingInventory) top).getRecipe();
                        if (recipe != null) {
                            if (click == org.bukkit.event.inventory.ClickType.NUMBER_KEY) {
                                event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.getSlotId(), click, action, packetplayinwindowclick.getUsedButton());
                            } else {
                                event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.getSlotId(), click, action);
                            }
                        }
                    }

                    event.setCancelled(cancelled);
                    Container oldContainer = this.player.openContainer; // SPIGOT-1224
                    server.getPluginManager().callEvent(event);
                    if (this.player.openContainer != oldContainer) {
                        return;
                    }

                    switch (event.getResult()) {
                        case ALLOW:
                        case DEFAULT:
                            itemstack = this.player.openContainer.slotClick(packetplayinwindowclick.getSlotId(), packetplayinwindowclick.getUsedButton(), packetplayinwindowclick.getClickType(), this.player);
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
                                    this.player.sendContainerToPlayer(this.player.openContainer);
                                    break;
                                // Modified cursor and clicked
                                case PICKUP_SOME:
                                case PICKUP_HALF:
                                case PICKUP_ONE:
                                case PLACE_ALL:
                                case PLACE_SOME:
                                case PLACE_ONE:
                                case SWAP_WITH_CURSOR:
                                    this.player.connection.sendPacket(new SPacketSetSlot(-1, -1, this.player.inventory.getItemStack()));
                                    this.player.connection.sendPacket(new SPacketSetSlot(this.player.openContainer.windowId, packetplayinwindowclick.getSlotId(), this.player.openContainer.getSlot(packetplayinwindowclick.getSlotId()).getStack()));
                                    break;
                                // Modified clicked only
                                case DROP_ALL_SLOT:
                                case DROP_ONE_SLOT:
                                    this.player.connection.sendPacket(new SPacketSetSlot(this.player.openContainer.windowId, packetplayinwindowclick.getSlotId(), this.player.openContainer.getSlot(packetplayinwindowclick.getSlotId()).getStack()));
                                    break;
                                // Modified cursor only
                                case DROP_ALL_CURSOR:
                                case DROP_ONE_CURSOR:
                                case CLONE_STACK:
                                    this.player.connection.sendPacket(new SPacketSetSlot(-1, -1, this.player.inventory.getItemStack()));
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
                        player.sendContainerToPlayer(player.openContainer);
                    }
                }
                // CraftBukkit end
                if (ItemStack.areItemStacksEqual(packetplayinwindowclick.getClickedItem(), itemstack)) {
                    this.player.connection.sendPacket(new SPacketConfirmTransaction(packetplayinwindowclick.getWindowId(), packetplayinwindowclick.getActionNumber(), true));
                    this.player.isChangingQuantityOnly = true;
                    this.player.openContainer.detectAndSendChanges();
                    this.player.updateHeldItem();
                    this.player.isChangingQuantityOnly = false;
                } else {
                    this.pendingTransactions.addKey(this.player.openContainer.windowId, Short.valueOf(packetplayinwindowclick.getActionNumber()));
                    this.player.connection.sendPacket(new SPacketConfirmTransaction(packetplayinwindowclick.getWindowId(), packetplayinwindowclick.getActionNumber(), false));
                    this.player.openContainer.setCanCraft(this.player, false);
                    NonNullList nonnulllist1 = NonNullList.create();

                    for (int j = 0; j < this.player.openContainer.inventorySlots.size(); ++j) {
                        ItemStack itemstack1 = this.player.openContainer.inventorySlots.get(j).getStack();
                        ItemStack itemstack2 = itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1;

                        nonnulllist1.add(itemstack2);
                    }

                    this.player.sendAllContents(this.player.openContainer, nonnulllist1);
                }
            }
        }

    }

    @Override
    public void func_194308_a(CPacketPlaceRecipe packetplayinautorecipe) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinautorecipe, this, this.player.getServerWorld());
        this.player.markPlayerActive();
        if (!this.player.isSpectator() && this.player.openContainer.windowId == packetplayinautorecipe.func_194318_a() && this.player.openContainer.getCanCraft(this.player)) {
            this.field_194309_H.func_194327_a(this.player, packetplayinautorecipe.func_194317_b(), packetplayinautorecipe.func_194319_c());
        }
    }

    @Override
    public void processEnchantItem(CPacketEnchantItem packetplayinenchantitem) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinenchantitem, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        this.player.markPlayerActive();
        if (this.player.openContainer.windowId == packetplayinenchantitem.getWindowId() && this.player.openContainer.getCanCraft(this.player) && !this.player.isSpectator()) {
            this.player.openContainer.enchantItem(this.player, packetplayinenchantitem.getButton());
            this.player.openContainer.detectAndSendChanges();
        }

    }

    @Override
    public void processCreativeInventoryAction(CPacketCreativeInventoryAction packetplayinsetcreativeslot) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinsetcreativeslot, this, this.player.getServerWorld());
        if (this.player.interactionManager.isCreative()) {
            boolean flag = packetplayinsetcreativeslot.getSlotId() < 0;
            ItemStack itemstack = packetplayinsetcreativeslot.getStack();

            if (!itemstack.isEmpty() && itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("BlockEntityTag", 10)) {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("BlockEntityTag");

                if (nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z")) {
                    BlockPos blockposition = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
                    TileEntity tileentity = this.player.world.getTileEntity(blockposition);

                    if (tileentity != null) {
                        NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());

                        nbttagcompound1.removeTag("x");
                        nbttagcompound1.removeTag("y");
                        nbttagcompound1.removeTag("z");
                        itemstack.setTagInfo("BlockEntityTag", nbttagcompound1);
                    }
                }
            }

            boolean flag1 = packetplayinsetcreativeslot.getSlotId() >= 1 && packetplayinsetcreativeslot.getSlotId() <= 45;
            // CraftBukkit - Add invalidItems check
            boolean flag2 = itemstack.isEmpty() || itemstack.getMetadata() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty() && (!invalidItems.contains(Item.getIdFromItem(itemstack.getItem())) || !org.spigotmc.SpigotConfig.filterCreativeItems); // Spigot
            if (flag || (flag1 && !ItemStack.areItemStacksEqual(this.player.inventoryContainer.getSlot(packetplayinsetcreativeslot.getSlotId()).getStack(), packetplayinsetcreativeslot.getStack()))) { // Insist on valid slot
                // CraftBukkit start - Call click event
                InventoryView inventory = this.player.inventoryContainer.getBukkitView();
                org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(packetplayinsetcreativeslot.getStack());

                SlotType type = SlotType.QUICKBAR;
                if (flag) {
                    type = SlotType.OUTSIDE;
                } else if (packetplayinsetcreativeslot.getSlotId() < 36) {
                    if (packetplayinsetcreativeslot.getSlotId() >= 5 && packetplayinsetcreativeslot.getSlotId() < 9) {
                        type = SlotType.ARMOR;
                    } else {
                        type = SlotType.CONTAINER;
                    }
                }
                InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type, flag ? -999 : packetplayinsetcreativeslot.getSlotId(), item);
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
                    if (packetplayinsetcreativeslot.getSlotId() >= 0) {
                        this.player.connection.sendPacket(new SPacketSetSlot(this.player.inventoryContainer.windowId, packetplayinsetcreativeslot.getSlotId(), this.player.inventoryContainer.getSlot(packetplayinsetcreativeslot.getSlotId()).getStack()));
                        this.player.connection.sendPacket(new SPacketSetSlot(-1, -1, ItemStack.EMPTY));
                    }
                    return;
                }
            }
            // CraftBukkit end

            if (flag1 && flag2) {
                if (itemstack.isEmpty()) {
                    this.player.inventoryContainer.putStackInSlot(packetplayinsetcreativeslot.getSlotId(), ItemStack.EMPTY);
                } else {
                    this.player.inventoryContainer.putStackInSlot(packetplayinsetcreativeslot.getSlotId(), itemstack);
                }

                this.player.inventoryContainer.setCanCraft(this.player, true);
            } else if (flag && flag2 && this.itemDropThreshold < 200) {
                this.itemDropThreshold += 20;
                EntityItem entityitem = this.player.dropItem(itemstack, true);

                if (entityitem != null) {
                    entityitem.setAgeToCreativeDespawnTime();
                }
            }
        }

    }

    @Override
    public void processConfirmTransaction(CPacketConfirmTransaction packetplayintransaction) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayintransaction, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        Short oshort = this.pendingTransactions.lookup(this.player.openContainer.windowId);

        if (oshort != null && packetplayintransaction.getUid() == oshort.shortValue() && this.player.openContainer.windowId == packetplayintransaction.getWindowId() && !this.player.openContainer.getCanCraft(this.player) && !this.player.isSpectator()) {
            this.player.openContainer.setCanCraft(this.player, true);
        }

    }

    @Override
    public void processUpdateSign(CPacketUpdateSign packetplayinupdatesign) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinupdatesign, this, this.player.getServerWorld());
        if (this.player.isMovementBlocked()) return; // CraftBukkit
        this.player.markPlayerActive();
        WorldServer worldserver = this.serverController.getWorld(this.player.dimension);
        BlockPos blockposition = packetplayinupdatesign.getPosition();

        if (worldserver.isBlockLoaded(blockposition)) {
            IBlockState iblockdata = worldserver.getBlockState(blockposition);
            TileEntity tileentity = worldserver.getTileEntity(blockposition);

            if (!(tileentity instanceof TileEntitySign)) {
                return;
            }

            TileEntitySign tileentitysign = (TileEntitySign) tileentity;

            if (!tileentitysign.getIsEditable() || tileentitysign.getPlayer() != this.player) {
                this.serverController.logWarning("Player " + this.player.getName() + " just tried to change non-editable sign");
                this.sendPacket(tileentity.getUpdatePacket()); // CraftBukkit
                return;
            }

            String[] astring = packetplayinupdatesign.getLines();

            // CraftBukkit start
            Player player = this.server.getPlayer(this.player);
            int x = packetplayinupdatesign.getPosition().getX();
            int y = packetplayinupdatesign.getPosition().getY();
            int z = packetplayinupdatesign.getPosition().getZ();
            String[] lines = new String[4];

            for (int i = 0; i < astring.length; ++i) {
                lines[i] = ChatAllowedCharacters.filterAllowedCharacters(astring[i]); //Paper - Replaced with anvil color stripping method to stop exploits that allow colored signs to be created.
            }
            SignChangeEvent event = new SignChangeEvent(player.getWorld().getBlockAt(x, y, z), this.server.getPlayer(this.player), lines);
            this.server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                System.arraycopy(org.bukkit.craftbukkit.block.CraftSign.sanitizeLines(event.getLines()), 0, tileentitysign.signText, 0, 4);
                tileentitysign.isEditable = false;
             }
            // CraftBukkit end

            tileentitysign.markDirty();
            worldserver.notifyBlockUpdate(blockposition, iblockdata, iblockdata, 3);
        }

    }

    @Override
    public void processKeepAlive(CPacketKeepAlive packetplayinkeepalive) {
        //PlayerConnectionUtils.ensureMainThread(packetplayinkeepalive, this, this.player.x()); // CraftBukkit // Paper - This shouldn't be on the main thread
        if (this.field_194403_g && packetplayinkeepalive.getKey() == this.field_194404_h) {
            int i = (int) (this.currentTimeMillis() - this.field_194402_f);

            this.player.ping = (this.player.ping * 3 + i) / 4;
            this.field_194403_g = false;
        } else if (!this.player.getName().equals(this.serverController.getServerOwner())) {
            // Paper start - This needs to be handled on the main thread for plugins
            NetHandlerPlayServer.LOGGER.warn("{} sent an invalid keepalive! pending keepalive: {} got id: {} expected id: {}",
                    this.player.getName(), this.isPendingPing(), packetplayinkeepalive.getKey(), this.getKeepAliveID());
            serverController.addScheduledTask(() -> {
                    this.disconnect(new TextComponentTranslation("disconnect.timeout", new Object[0]));
            });
            // Paper end
        }

    }

    private long getCurrentMillis() { return currentTimeMillis(); } // Paper - OBFHELPER
    private long currentTimeMillis() {
        return System.nanoTime() / 1000000L;
    }

    @Override
    public void processPlayerAbilities(CPacketPlayerAbilities packetplayinabilities) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinabilities, this, this.player.getServerWorld());
        // CraftBukkit start
        if (this.player.capabilities.allowFlying && this.player.capabilities.isFlying != packetplayinabilities.isFlying()) {
            PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(this.server.getPlayer(this.player), packetplayinabilities.isFlying());
            this.server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.player.capabilities.isFlying = packetplayinabilities.isFlying(); // Actually set the player's flying status
            } else {
                this.player.sendPlayerAbilities(); // Tell the player their ability was reverted
            }
        }
        // CraftBukkit end
    }

    // Paper start - async tab completion
    @Override
    public void processTabComplete(CPacketTabComplete packet) {
        // CraftBukkit start
        if (chatSpamField.addAndGet(this, 10) > 500 && !this.serverController.getPlayerList().canSendCommands(this.player.getGameProfile())) {
            serverController.addScheduledTask(() -> this.disconnect(new TextComponentTranslation("disconnect.spam", new Object[0])));
            return;
        }
        // CraftBukkit end

        com.destroystokyo.paper.event.server.AsyncTabCompleteEvent event;
        java.util.List<String> completions = new ArrayList<>();
        BlockPos blockpos = packet.getTargetBlock();
        String buffer = packet.getMessage();
        boolean isCommand = buffer.startsWith("/") || packet.hasTargetBlock();
        event = new com.destroystokyo.paper.event.server.AsyncTabCompleteEvent(this.getPlayer(), completions,
            buffer, isCommand, blockpos != null ? MCUtil.toLocation(player.world, blockpos) : null);
        event.callEvent();
        completions = event.isCancelled() ? com.google.common.collect.ImmutableList.of() : event.getCompletions();
        if (event.isCancelled() || event.isHandled()) {
            // Still fire sync event with the provided completions, if someone is listening
            if (!event.isCancelled() && org.bukkit.event.server.TabCompleteEvent.getHandlerList().getRegisteredListeners().length > 0) {
                java.util.List<String> finalCompletions = completions;
                Waitable<java.util.List<String>> syncCompletions = new Waitable<java.util.List<String>>() {
                    @Override
                    protected java.util.List<String> evaluate() {
                        org.bukkit.event.server.TabCompleteEvent syncEvent = new org.bukkit.event.server.TabCompleteEvent(NetHandlerPlayServer.this.getPlayer(), buffer, finalCompletions, isCommand, blockpos != null ? MCUtil.toLocation(player.world, blockpos) : null);
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

            this.player.connection.sendPacket(new SPacketTabComplete(completions.toArray(new String[completions.size()])));
            return;
        }
        serverController.addScheduledTask(() -> {
            java.util.List<String> syncCompletions = this.serverController.getTabCompletions(this.player, buffer, blockpos, isCommand);
            this.player.connection.sendPacket(new SPacketTabComplete(syncCompletions.toArray(new String[syncCompletions.size()])));
        });
        // Paper end
    }

    @Override
    public void processClientSettings(CPacketClientSettings packetplayinsettings) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayinsettings, this, this.player.getServerWorld());
        this.player.handleClientSettings(packetplayinsettings);
    }

    @Override
    public void processCustomPayload(CPacketCustomPayload packetplayincustompayload) {
        PacketThreadUtil.checkThreadAndEnqueue(packetplayincustompayload, this, this.player.getServerWorld());
        String s = packetplayincustompayload.getChannelName();
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
            packetdataserializer = packetplayincustompayload.getBufferData();

            try {
                itemstack = packetdataserializer.readItemStack();
                if (itemstack.isEmpty()) {
                    return;
                }

                if (!ItemWritableBook.isNBTValid(itemstack.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }

                itemstack1 = this.player.getHeldItemMainhand();
                if (itemstack1.isEmpty()) {
                    return;
                }

                if (itemstack.getItem() == Items.WRITABLE_BOOK && itemstack.getItem() == itemstack1.getItem()) {
                    itemstack1 = new ItemStack(Items.WRITABLE_BOOK); // CraftBukkit
                    itemstack1.setTagInfo("pages", itemstack.getTagCompound().getTagList("pages", 8));
                    CraftEventFactory.handleEditBookEvent(player, itemstack1); // CraftBukkit
                }
            } catch (Exception exception) {
                IllegalPacketEvent.process(player.getBukkitEntity(), "InvalidBookEdit", "Invalid book data!", exception); // Paper
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
                packetdataserializer = packetplayincustompayload.getBufferData();

                try {
                    itemstack = packetdataserializer.readItemStack();
                    if (itemstack.isEmpty()) {
                        return;
                    }

                    if (!ItemWrittenBook.validBookTagContents(itemstack.getTagCompound())) {
                        throw new IOException("Invalid book tag!");
                    }

                    itemstack1 = this.player.getHeldItemMainhand();
                    if (itemstack1.isEmpty()) {
                        return;
                    }

                    if (itemstack.getItem() == Items.WRITABLE_BOOK && itemstack1.getItem() == Items.WRITABLE_BOOK) {
                        ItemStack itemstack2 = new ItemStack(Items.WRITTEN_BOOK);

                        itemstack2.setTagInfo("author", (new NBTTagString(this.player.getName())));
                        itemstack2.setTagInfo("title", (new NBTTagString(itemstack.getTagCompound().getString("title"))));
                        NBTTagList nbttaglist = itemstack.getTagCompound().getTagList("pages", 8);

                        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                            s1 = nbttaglist.getStringTagAt(i);
                            TextComponentString chatcomponenttext = new TextComponentString(s1);

                            s1 = ITextComponent.Serializer.componentToJson(chatcomponenttext);
                            nbttaglist.set(i, new NBTTagString(s1));
                        }

                        itemstack2.setTagInfo("pages", nbttaglist);
                        CraftEventFactory.handleEditBookEvent(player, itemstack2); // CraftBukkit
                    }
                } catch (Exception exception1) {
                    IllegalPacketEvent.process(player.getBukkitEntity(), "InvalidBookSign", "Invalid book data!", exception1); // Paper
                }
            } else if ("MC|TrSel".equals(s)) {
                try {
                    int j = packetplayincustompayload.getBufferData().readInt();
                    Container container = this.player.openContainer;

                    if (container instanceof ContainerMerchant) {
                        ((ContainerMerchant) container).setCurrentRecipeIndex(j);
                    }
                } catch (Exception exception2) {
                    IllegalPacketEvent.process(player.getBukkitEntity(), "InvalidTrade", "Invalid trade data!", exception2); // Paper
                }
            } else {
                TileEntity tileentity;

                if ("MC|AdvCmd".equals(s)) {
                    if (!this.serverController.isCommandBlockEnabled()) {
                        this.player.sendMessage(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                        return;
                    }

                    if (!this.player.canUseCommandBlock()) {
                        this.player.sendMessage(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                        return;
                    }

                    packetdataserializer = packetplayincustompayload.getBufferData();

                    try {
                        byte b0 = packetdataserializer.readByte();
                        CommandBlockBaseLogic commandblocklistenerabstract = null;

                        if (b0 == 0) {
                            tileentity = this.player.world.getTileEntity(new BlockPos(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt()));
                            if (tileentity instanceof TileEntityCommandBlock) {
                                commandblocklistenerabstract = ((TileEntityCommandBlock) tileentity).getCommandBlockLogic();
                            }
                        } else if (b0 == 1) {
                            Entity entity = this.player.world.getEntityByID(packetdataserializer.readInt());

                            if (entity instanceof EntityMinecartCommandBlock) {
                                commandblocklistenerabstract = ((EntityMinecartCommandBlock) entity).getCommandBlockLogic();
                            }
                        }

                        String s2 = packetdataserializer.readString(packetdataserializer.readableBytes());
                        boolean flag = packetdataserializer.readBoolean();

                        if (commandblocklistenerabstract != null) {
                            commandblocklistenerabstract.setCommand(s2);
                            commandblocklistenerabstract.setTrackOutput(flag);
                            if (!flag) {
                                commandblocklistenerabstract.setLastOutput((ITextComponent) null);
                            }

                            commandblocklistenerabstract.updateCommand();
                            this.player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", new Object[] { s2}));
                        }
                    } catch (Exception exception3) {
                        NetHandlerPlayServer.LOGGER.error("Couldn\'t set command block", exception3);
                        this.disconnect("Invalid command data!"); // CraftBukkit
                    }
                } else if ("MC|AutoCmd".equals(s)) {
                    if (!this.serverController.isCommandBlockEnabled()) {
                        this.player.sendMessage(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                        return;
                    }

                    if (!this.player.canUseCommandBlock()) {
                        this.player.sendMessage(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                        return;
                    }

                    packetdataserializer = packetplayincustompayload.getBufferData();

                    try {
                        CommandBlockBaseLogic commandblocklistenerabstract1 = null;
                        TileEntityCommandBlock tileentitycommand = null;
                        BlockPos blockposition = new BlockPos(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt());
                        TileEntity tileentity1 = this.player.world.getTileEntity(blockposition);

                        if (tileentity1 instanceof TileEntityCommandBlock) {
                            tileentitycommand = (TileEntityCommandBlock) tileentity1;
                            commandblocklistenerabstract1 = tileentitycommand.getCommandBlockLogic();
                        }

                        String s3 = packetdataserializer.readString(packetdataserializer.readableBytes());
                        boolean flag1 = packetdataserializer.readBoolean();
                        TileEntityCommandBlock.Mode tileentitycommand_type = TileEntityCommandBlock.Mode.valueOf(packetdataserializer.readString(16));
                        boolean flag2 = packetdataserializer.readBoolean();
                        boolean flag3 = packetdataserializer.readBoolean();

                        if (commandblocklistenerabstract1 != null) {
                            EnumFacing enumdirection = this.player.world.getBlockState(blockposition).getValue(BlockCommandBlock.FACING);
                            IBlockState iblockdata;

                            switch (tileentitycommand_type) {
                            case SEQUENCE:
                                iblockdata = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
                                this.player.world.setBlockState(blockposition, iblockdata.withProperty(BlockCommandBlock.FACING, enumdirection).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag2)), 2);
                                break;

                            case AUTO:
                                iblockdata = Blocks.REPEATING_COMMAND_BLOCK.getDefaultState();
                                this.player.world.setBlockState(blockposition, iblockdata.withProperty(BlockCommandBlock.FACING, enumdirection).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag2)), 2);
                                break;

                            case REDSTONE:
                                iblockdata = Blocks.COMMAND_BLOCK.getDefaultState();
                                this.player.world.setBlockState(blockposition, iblockdata.withProperty(BlockCommandBlock.FACING, enumdirection).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag2)), 2);
                            }

                            tileentity1.validate();
                            this.player.world.setTileEntity(blockposition, tileentity1);
                            commandblocklistenerabstract1.setCommand(s3);
                            commandblocklistenerabstract1.setTrackOutput(flag1);
                            if (!flag1) {
                                commandblocklistenerabstract1.setLastOutput((ITextComponent) null);
                            }

                            tileentitycommand.setAuto(flag3);
                            commandblocklistenerabstract1.updateCommand();
                            if (!StringUtils.isEmpty(s3)) {
                                this.player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", new Object[] { s3}));
                            }
                        }
                    } catch (Exception exception4) {
                        NetHandlerPlayServer.LOGGER.error("Couldn\'t set command block", exception4);
                        this.disconnect("Invalid command data!"); // CraftBukkit
                    }
                } else {
                    int k;

                    if ("MC|Beacon".equals(s)) {
                        if (this.player.openContainer instanceof ContainerBeacon) {
                            try {
                                packetdataserializer = packetplayincustompayload.getBufferData();
                                k = packetdataserializer.readInt();
                                int l = packetdataserializer.readInt();
                                ContainerBeacon containerbeacon = (ContainerBeacon) this.player.openContainer;
                                Slot slot = containerbeacon.getSlot(0);

                                if (slot.getHasStack()) {
                                    slot.decrStackSize(1);
                                    IInventory iinventory = containerbeacon.getTileEntity();

                                    iinventory.setField(1, k);
                                    iinventory.setField(2, l);
                                    iinventory.markDirty();
                                }
                            } catch (Exception exception5) {
                                IllegalPacketEvent.process(player.getBukkitEntity(), "InvalidBeacon", "Invalid beacon data!", exception5); // Paper
                            }
                        }
                    } else if ("MC|ItemName".equals(s)) {
                        if (this.player.openContainer instanceof ContainerRepair) {
                            ContainerRepair containeranvil = (ContainerRepair) this.player.openContainer;

                            if (packetplayincustompayload.getBufferData() != null && packetplayincustompayload.getBufferData().readableBytes() >= 1) {
                                String s4 = ChatAllowedCharacters.filterAllowedCharacters(packetplayincustompayload.getBufferData().readString(32767));

                                if (s4.length() <= 35) {
                                    containeranvil.updateItemName(s4);
                                }
                            } else {
                                containeranvil.updateItemName("");
                            }
                        }
                    } else if ("MC|Struct".equals(s)) {
                        if (!this.player.canUseCommandBlock()) {
                            return;
                        }

                        packetdataserializer = packetplayincustompayload.getBufferData();

                        try {
                            BlockPos blockposition1 = new BlockPos(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt());
                            IBlockState iblockdata1 = this.player.world.getBlockState(blockposition1);

                            tileentity = this.player.world.getTileEntity(blockposition1);
                            if (tileentity instanceof TileEntityStructure) {
                                TileEntityStructure tileentitystructure = (TileEntityStructure) tileentity;
                                byte b1 = packetdataserializer.readByte();

                                s1 = packetdataserializer.readString(32);
                                tileentitystructure.setMode(TileEntityStructure.Mode.valueOf(s1));
                                tileentitystructure.setName(packetdataserializer.readString(64));
                                int i1 = MathHelper.clamp(packetdataserializer.readInt(), -32, 32);
                                int j1 = MathHelper.clamp(packetdataserializer.readInt(), -32, 32);
                                int k1 = MathHelper.clamp(packetdataserializer.readInt(), -32, 32);

                                tileentitystructure.setPosition(new BlockPos(i1, j1, k1));
                                int l1 = MathHelper.clamp(packetdataserializer.readInt(), 0, 32);
                                int i2 = MathHelper.clamp(packetdataserializer.readInt(), 0, 32);
                                int j2 = MathHelper.clamp(packetdataserializer.readInt(), 0, 32);

                                tileentitystructure.setSize(new BlockPos(l1, i2, j2));
                                String s5 = packetdataserializer.readString(32);

                                tileentitystructure.setMirror(Mirror.valueOf(s5));
                                String s6 = packetdataserializer.readString(32);

                                tileentitystructure.setRotation(Rotation.valueOf(s6));
                                tileentitystructure.setMetadata(packetdataserializer.readString(128));
                                tileentitystructure.setIgnoresEntities(packetdataserializer.readBoolean());
                                tileentitystructure.setShowAir(packetdataserializer.readBoolean());
                                tileentitystructure.setShowBoundingBox(packetdataserializer.readBoolean());
                                tileentitystructure.setIntegrity(MathHelper.clamp(packetdataserializer.readFloat(), 0.0F, 1.0F));
                                tileentitystructure.setSeed(packetdataserializer.readVarLong());
                                String s7 = tileentitystructure.getName();

                                if (b1 == 2) {
                                    if (tileentitystructure.save()) {
                                        this.player.sendStatusMessage((new TextComponentTranslation("structure_block.save_success", new Object[] { s7})), false);
                                    } else {
                                        this.player.sendStatusMessage((new TextComponentTranslation("structure_block.save_failure", new Object[] { s7})), false);
                                    }
                                } else if (b1 == 3) {
                                    if (!tileentitystructure.isStructureLoadable()) {
                                        this.player.sendStatusMessage((new TextComponentTranslation("structure_block.load_not_found", new Object[] { s7})), false);
                                    } else if (tileentitystructure.load()) {
                                        this.player.sendStatusMessage((new TextComponentTranslation("structure_block.load_success", new Object[] { s7})), false);
                                    } else {
                                        this.player.sendStatusMessage((new TextComponentTranslation("structure_block.load_prepare", new Object[] { s7})), false);
                                    }
                                } else if (b1 == 4) {
                                    if (tileentitystructure.detectSize()) {
                                        this.player.sendStatusMessage((new TextComponentTranslation("structure_block.size_success", new Object[] { s7})), false);
                                    } else {
                                        this.player.sendStatusMessage((new TextComponentTranslation("structure_block.size_failure", new Object[0])), false);
                                    }
                                }

                                tileentitystructure.markDirty();
                                this.player.world.notifyBlockUpdate(blockposition1, iblockdata1, iblockdata1, 3);
                            }
                        } catch (Exception exception6) {
                            NetHandlerPlayServer.LOGGER.error("Couldn\'t set structure block", exception6);
                            this.disconnect("Invalid structure data!"); // CraftBukkit
                        }
                    } else if ("MC|PickItem".equals(s)) {
                        packetdataserializer = packetplayincustompayload.getBufferData();

                        try {
                            k = packetdataserializer.readVarInt();
                            this.player.inventory.pickItem(k);
                            this.player.connection.sendPacket(new SPacketSetSlot(-2, this.player.inventory.currentItem, this.player.inventory.getStackInSlot(this.player.inventory.currentItem)));
                            this.player.connection.sendPacket(new SPacketSetSlot(-2, k, this.player.inventory.getStackInSlot(k)));
                            this.player.connection.sendPacket(new SPacketHeldItemChange(this.player.inventory.currentItem));
                        } catch (Exception exception7) {
                            IllegalPacketEvent.process(player.getBukkitEntity(), "InvalidPickItem", "Invalid PickItem", exception7); // Paper
                        }
                    }
                    // CraftBukkit start
                    else if (packetplayincustompayload.getChannelName().equals("REGISTER")) {
                        try {
                            String channels = packetplayincustompayload.getBufferData().toString(com.google.common.base.Charsets.UTF_8);
                            for (String channel : channels.split("\0")) {
                                getPlayer().addChannel(channel);
                            }
                        } catch (Exception ex) {
                            NetHandlerPlayServer.LOGGER.error("Couldn\'t register custom payload", ex);
                            this.disconnect("Invalid payload REGISTER!");
                        }
                    } else if (packetplayincustompayload.getChannelName().equals("UNREGISTER")) {
                        try {
                            String channels = packetplayincustompayload.getBufferData().toString(com.google.common.base.Charsets.UTF_8);
                            for (String channel : channels.split("\0")) {
                                getPlayer().removeChannel(channel);
                            }
                        } catch (Exception ex) {
                            NetHandlerPlayServer.LOGGER.error("Couldn\'t unregister custom payload", ex);
                            this.disconnect("Invalid payload UNREGISTER!");
                        }
                    } else {
                        try {
                            byte[] data = new byte[packetplayincustompayload.getBufferData().readableBytes()];
                            packetplayincustompayload.getBufferData().readBytes(data);
                            server.getMessenger().dispatchIncomingMessage(player.getBukkitEntity(), packetplayincustompayload.getChannelName(), data);
                        } catch (Exception ex) {
                            NetHandlerPlayServer.LOGGER.error("Couldn\'t dispatch custom payload", ex);
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
        return (!this.player.joining && !this.netManager.isChannelOpen()) || this.processedDisconnect; // Paper
    }
}
