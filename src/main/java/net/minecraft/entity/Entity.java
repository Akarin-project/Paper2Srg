package net.minecraft.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.aikar.timings.MinecraftTimings;
import co.aikar.timings.Timing;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.TravelAgent;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.PluginManager;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.TravelAgent;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import co.aikar.timings.MinecraftTimings; // Paper
import co.aikar.timings.Timing; // Paper
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.plugin.PluginManager;
// CraftBukkit end

public abstract class Entity implements ICommandSender {

    // CraftBukkit start
    private static final int CURRENT_LEVEL = 2;
    public static Random SHARED_RANDOM = new Random(); // Paper
    static boolean isLevelAtLeast(NBTTagCompound tag, int level) {
        return tag.hasKey("Bukkit.updateLevel") && tag.getInteger("Bukkit.updateLevel") >= level;
    }

    protected CraftEntity bukkitEntity;

    EntityTrackerEntry tracker; // Paper
    public CraftEntity getBukkitEntity() {
        if (bukkitEntity == null) {
            bukkitEntity = CraftEntity.getEntity(world.getServer(), this);
        }
        return bukkitEntity;
    }
    // CraftBukikt end

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<ItemStack> EMPTY_EQUIPMENT = Collections.emptyList();
    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static double renderDistanceWeight = 1.0D;
    private static int nextEntityID;
    private int entityId;
    public boolean preventEntitySpawning; public boolean blocksEntitySpawning() { return preventEntitySpawning; } // Paper - OBFHELPER
    public final List<Entity> riddenByEntities;
    protected int rideCooldown;
    private Entity ridingEntity;public void setVehicle(Entity entity) { this.ridingEntity = entity; } // Paper // OBFHELPER
    public boolean forceSpawn;
    public World world;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double posX;
    public double posY;
    public double posZ;
    // Paper start - getters to implement HopperPusher
    public double getX() {
        return posX;
    }

    public double getY() {
        return posY;
    }

    public double getZ() {
        return posZ;
    }
    // Paper end
    public double motionX;
    public double motionY;
    public double motionZ;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    private AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean collidedHorizontally;
    public boolean collidedVertically;
    public boolean collided;
    public boolean velocityChanged;
    protected boolean isInWeb;
    private boolean isOutsideBorder;
    public boolean isDead;
    public float width;
    public float height;
    public float prevDistanceWalkedModified;
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;
    private int nextStepDistance;
    private float nextFlap;
    public double lastTickPosX;
    public double lastTickPosY;
    public double lastTickPosZ;
    public float stepHeight;
    public boolean noClip;
    public float entityCollisionReduction;
    protected Random rand;
    public int ticksExisted;
    public int fire;
    public boolean inWater; // Spigot - protected -> public // PAIL
    public int hurtResistantTime;
    protected boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected EntityDataManager dataManager;
    protected static final DataParameter<Byte> FLAGS = EntityDataManager.createKey(Entity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> AIR = EntityDataManager.createKey(Entity.class, DataSerializers.VARINT);
    private static final DataParameter<String> CUSTOM_NAME = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> CUSTOM_NAME_VISIBLE = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SILENT = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> NO_GRAVITY = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
    public boolean addedToChunk; public boolean isAddedToChunk() { return addedToChunk; } // Paper - OBFHELPER
    public int chunkCoordX; public int getChunkX() { return chunkCoordX; } // Paper - OBFHELPER
    public int chunkCoordY; public int getChunkY() { return chunkCoordY; } // Paper - OBFHELPER
    public int chunkCoordZ; public int getChunkZ() { return chunkCoordZ; } // Paper - OBFHELPER
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;
    protected boolean inPortal; public boolean inPortal() { return inPortal; } // Paper - OBFHELPER
    protected int portalCounter;
    public int dimension;
    protected BlockPos lastPortalPos;
    protected Vec3d lastPortalVec;
    protected EnumFacing teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    protected String cachedUniqueIdString;
    private final CommandResultStats cmdResultStats;
    public boolean glowing;
    private final Set<String> tags;
    private boolean isPositionDirty;
    private final double[] pistonDeltas;
    private long pistonDeltasGameTime;
    // CraftBukkit start
    public boolean valid;
    public org.bukkit.projectiles.ProjectileSource projectileSource; // For projectiles only
    public boolean forceExplosionKnockback; // SPIGOT-949
    public Timing tickTimer = MinecraftTimings.getEntityTimings(this); // Paper
    public Location origin; // Paper
    // Spigot start
    public final byte activationType = org.spigotmc.ActivationRange.initializeEntityActivationType(this);
    public final boolean defaultActivationState;
    public long activatedTick = Integer.MIN_VALUE;
    public boolean fromMobSpawner;
    public boolean spawnedViaMobSpawner; // Paper - Yes this name is similar to above, upstream took the better one
    protected int numCollisions = 0; // Paper
    public void inactiveTick() { }
    // Spigot end

    public float getBukkitYaw() {
        return this.rotationYaw;
    }
    // CraftBukkit end

    public Entity(World world) {
        this.entityId = Entity.nextEntityID++;
        this.riddenByEntities = Lists.newArrayList();
        this.boundingBox = Entity.ZERO_AABB;
        this.width = 0.6F;
        this.height = 1.8F;
        this.nextStepDistance = 1;
        this.nextFlap = 1.0F;
        this.rand = SHARED_RANDOM; // Paper
        this.fire = -this.getFireImmuneTicks();
        this.firstUpdate = true;
        this.entityUniqueID = MathHelper.getRandomUUID(this.rand);
        this.cachedUniqueIdString = this.entityUniqueID.toString();
        this.cmdResultStats = new CommandResultStats();
        this.tags = Sets.newHashSet();
        this.pistonDeltas = new double[] { 0.0D, 0.0D, 0.0D};
        this.world = world;
        this.setPosition(0.0D, 0.0D, 0.0D);
        if (world != null) {
            this.dimension = world.provider.getDimensionType().getId();
            // Spigot start
            this.defaultActivationState = org.spigotmc.ActivationRange.initializeEntityActivationState(this, world.spigotConfig);
        } else {
            this.defaultActivationState = false;
        }
        // Spigot end

        this.dataManager = new EntityDataManager(this);
        this.dataManager.register(Entity.FLAGS, Byte.valueOf((byte) 0));
        this.dataManager.register(Entity.AIR, Integer.valueOf(300));
        this.dataManager.register(Entity.CUSTOM_NAME_VISIBLE, Boolean.valueOf(false));
        this.dataManager.register(Entity.CUSTOM_NAME, "");
        this.dataManager.register(Entity.SILENT, Boolean.valueOf(false));
        this.dataManager.register(Entity.NO_GRAVITY, Boolean.valueOf(false));
        this.entityInit();
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setEntityId(int i) {
        this.entityId = i;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    public boolean addTag(String s) {
        if (this.tags.size() >= 1024) {
            return false;
        } else {
            this.tags.add(s);
            return true;
        }
    }

    public boolean removeTag(String s) {
        return this.tags.remove(s);
    }

    public void onKillCommand() {
        this.setDead();
    }

    protected abstract void entityInit();

    public EntityDataManager getDataManager() {
        return this.dataManager;
    }

    public boolean equals(Object object) {
        return object instanceof Entity ? ((Entity) object).entityId == this.entityId : false;
    }

    public int hashCode() {
        return this.entityId;
    }

    public void setDead() {
        this.isDead = true;
    }

    public void setDropItemsWhenDead(boolean flag) {}

    public void setSize(float f, float f1) {
        if (f != this.width || f1 != this.height) {
            float f2 = this.width;

            this.width = f;
            this.height = f1;
            if (this.width < f2) {
                double d0 = (double) f / 2.0D;

                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + (double) this.height, this.posZ + d0));
                return;
            }

            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

            this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) this.width, axisalignedbb.minY + (double) this.height, axisalignedbb.minZ + (double) this.width));
            if (this.width > f2 && !this.firstUpdate && !this.world.isRemote) {
                this.move(MoverType.SELF, (double) (f2 - this.width), 0.0D, (double) (f2 - this.width));
            }
        }

    }

    protected void setRotation(float f, float f1) {
        // CraftBukkit start - yaw was sometimes set to NaN, so we need to set it back to 0
        if (Float.isNaN(f)) {
            f = 0;
        }

        if (f == Float.POSITIVE_INFINITY || f == Float.NEGATIVE_INFINITY) {
            if (this instanceof EntityPlayerMP) {
                this.world.getServer().getLogger().warning(this.getName() + " was caught trying to crash the server with an invalid yaw");
                ((CraftPlayer) this.getBukkitEntity()).kickPlayer("Infinite yaw (Hacking?)"); //Spigot "Nope" -> Descriptive reason
            }
            f = 0;
        }

        // pitch was sometimes set to NaN, so we need to set it back to 0
        if (Float.isNaN(f1)) {
            f1 = 0;
        }

        if (f1 == Float.POSITIVE_INFINITY || f1 == Float.NEGATIVE_INFINITY) {
            if (this instanceof EntityPlayerMP) {
                this.world.getServer().getLogger().warning(this.getName() + " was caught trying to crash the server with an invalid pitch");
                ((CraftPlayer) this.getBukkitEntity()).kickPlayer("Infinite pitch (Hacking?)"); //Spigot "Nope" -> Descriptive reason
            }
            f1 = 0;
        }
        // CraftBukkit end

        this.rotationYaw = f % 360.0F;
        this.rotationPitch = f1 % 360.0F;
    }

    public void setPosition(double d0, double d1, double d2) {
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        float f = this.width / 2.0F;
        float f1 = this.height;

        this.setEntityBoundingBox(new AxisAlignedBB(d0 - (double) f, d1, d2 - (double) f, d0 + (double) f, d1 + (double) f1, d2 + (double) f));
    }

    public void onUpdate() {
        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }

        this.onEntityUpdate();
    }

    // CraftBukkit start
    public void postTick() {
        // No clean way to break out of ticking once the entity has been copied to a new world, so instead we move the portalling later in the tick cycle
        if (!this.world.isRemote && this.world instanceof WorldServer) {
            this.world.profiler.startSection("portal");
            if (this.inPortal) {
                MinecraftServer minecraftserver = this.world.getMinecraftServer();

                if (true || minecraftserver.getAllowNether()) { // CraftBukkit
                    if (!this.isRiding()) {
                        int i = this.getMaxInPortalTime();

                        if (this.portalCounter++ >= i) {
                            this.portalCounter = i;
                            this.timeUntilPortal = this.getPortalCooldown();
                            byte b0;

                            if (this.world.provider.getDimensionType().getId() == -1) {
                                b0 = 0;
                            } else {
                                b0 = -1;
                            }

                            this.changeDimension(b0);
                        }
                    }

                    this.inPortal = false;
                }
            } else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }

            this.decrementTimeUntilPortal();
            this.world.profiler.endSection();
        }
    }
    // CraftBukkit end

    public void onEntityUpdate() {
        this.world.profiler.startSection("entityBaseTick");
        if (this.isRiding() && this.getRidingEntity().isDead) {
            this.dismountRidingEntity();
        }

        if (this.rideCooldown > 0) {
            --this.rideCooldown;
        }

        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        // Moved up to postTick
        /*
        if (!this.world.isClientSide && this.world instanceof WorldServer) {
            this.world.methodProfiler.a("portal");
            if (this.ak) {
                MinecraftServer minecraftserver = this.world.getMinecraftServer();

                if (minecraftserver.getAllowNether()) {
                    if (!this.isPassenger()) {
                        int i = this.Z();

                        if (this.al++ >= i) {
                            this.al = i;
                            this.portalCooldown = this.aM();
                            byte b0;

                            if (this.world.worldProvider.getDimensionManager().getDimensionID() == -1) {
                                b0 = 0;
                            } else {
                                b0 = -1;
                            }

                            this.b(b0);
                        }
                    }

                    this.ak = false;
                }
            } else {
                if (this.al > 0) {
                    this.al -= 4;
                }

                if (this.al < 0) {
                    this.al = 0;
                }
            }

            this.I();
            this.world.methodProfiler.b();
        }
        */

        this.spawnRunningParticles();
        this.handleWaterMovement();
        if (this.world.isRemote) {
            this.extinguish();
        } else if (this.fire > 0) {
            if (this.isImmuneToFire) {
                this.fire -= 4;
                if (this.fire < 0) {
                    this.extinguish();
                }
            } else {
                if (this.fire % 20 == 0) {
                    this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
                }

                --this.fire;
            }
        }

        if (this.isInLava()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }

        // Paper start - Configurable nether ceiling damage
        // Extracted to own function
        /*
        if (this.locY < -64.0D) {
            this.ac();
        }
        */
        this.checkAndDoHeightDamage();
        // Paper end

        if (!this.world.isRemote) {
            this.setFlag(0, this.fire > 0);
        }

        this.firstUpdate = false;
        this.world.profiler.endSection();
    }

    // Paper start - Configurable top of nether void damage
    private boolean paperNetherCheck() {
        return this.world.paperConfig.netherVoidTopDamage && this.world.getWorld().getEnvironment() == org.bukkit.World.Environment.NETHER && this.posY >= 128.0D;
    }

    protected void checkAndDoHeightDamage() {
        if (this.posY < -64.0D || paperNetherCheck()) {
            this.kill();
        }
    }
    // Paper end

    protected void decrementTimeUntilPortal() {
        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }

    }

    public int getMaxInPortalTime() {
        return 1;
    }

    protected void setOnFireFromLava() {
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.LAVA, 4.0F);

            // CraftBukkit start - Fallen in lava TODO: this event spams!
            if (this instanceof EntityLivingBase) {
                if (fire <= 0) {
                    // not on fire yet
                    // TODO: shouldn't be sending null for the block
                    org.bukkit.block.Block damager = null; // ((WorldServer) this.l).getWorld().getBlockAt(i, j, k);
                    org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                    EntityCombustEvent combustEvent = new org.bukkit.event.entity.EntityCombustByBlockEvent(damager, damagee, 15);
                    this.world.getServer().getPluginManager().callEvent(combustEvent);

                    if (!combustEvent.isCancelled()) {
                        this.setFire(combustEvent.getDuration());
                    }
                } else {
                    // This will be called every single tick the entity is in lava, so don't throw an event
                    this.setFire(15);
                }
                return;
            }
            // CraftBukkit end - we also don't throw an event unless the object in lava is living, to save on some event calls
            this.setFire(15);
        }
    }

    public void setFire(int i) {
        int j = i * 20;

        if (this instanceof EntityLivingBase) {
            j = EnchantmentProtection.getFireTimeForEntity((EntityLivingBase) this, j);
        }

        if (this.fire < j) {
            this.fire = j;
        }

    }

    public void extinguish() {
        this.fire = 0;
    }

    protected final void kill() { this.outOfWorld(); } // Paper - OBFHELPER
    protected void outOfWorld() {
        this.setDead();
    }

    public boolean isOffsetPositionInLiquid(double d0, double d1, double d2) {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().offset(d0, d1, d2);

        return this.isLiquidPresentInAABB(axisalignedbb);
    }

    private boolean isLiquidPresentInAABB(AxisAlignedBB axisalignedbb) {
        return this.world.getCollisionBoxes(this, axisalignedbb).isEmpty() && !this.world.containsAnyLiquid(axisalignedbb);
    }

    public void move(MoverType enummovetype, double d0, double d1, double d2) {
        if (this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(d0, d1, d2));
            this.resetPositionToBB();
        } else {
            // CraftBukkit start - Don't do anything if we aren't moving
            // We need to do this regardless of whether or not we are moving thanks to portals
            try {
                this.doBlockCollisions();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Entity being checked for collision");

                this.addEntityCrashInfo(crashreportsystemdetails);
                throw new ReportedException(crashreport);
            }
            // Check if we're moving
            if (d0 == 0 && d1 == 0 && d2 == 0 && this.isBeingRidden() && this.isRiding()) {
                return;
            }
            // CraftBukkit end
            if (enummovetype == MoverType.PISTON) {
                long i = this.world.getTotalWorldTime();

                if (i != this.pistonDeltasGameTime) {
                    Arrays.fill(this.pistonDeltas, 0.0D);
                    this.pistonDeltasGameTime = i;
                }

                int j;
                double d3;

                if (d0 != 0.0D) {
                    j = EnumFacing.Axis.X.ordinal();
                    d3 = MathHelper.clamp(d0 + this.pistonDeltas[j], -0.51D, 0.51D);
                    d0 = d3 - this.pistonDeltas[j];
                    this.pistonDeltas[j] = d3;
                    if (Math.abs(d0) <= 9.999999747378752E-6D) {
                        return;
                    }
                } else if (d1 != 0.0D) {
                    j = EnumFacing.Axis.Y.ordinal();
                    d3 = MathHelper.clamp(d1 + this.pistonDeltas[j], -0.51D, 0.51D);
                    d1 = d3 - this.pistonDeltas[j];
                    this.pistonDeltas[j] = d3;
                    if (Math.abs(d1) <= 9.999999747378752E-6D) {
                        return;
                    }
                } else {
                    if (d2 == 0.0D) {
                        return;
                    }

                    j = EnumFacing.Axis.Z.ordinal();
                    d3 = MathHelper.clamp(d2 + this.pistonDeltas[j], -0.51D, 0.51D);
                    d2 = d3 - this.pistonDeltas[j];
                    this.pistonDeltas[j] = d3;
                    if (Math.abs(d2) <= 9.999999747378752E-6D) {
                        return;
                    }
                }
            }

            this.world.profiler.startSection("move");
            double d4 = this.posX;
            double d5 = this.posY;
            double d6 = this.posZ;

            if (this.isInWeb) {
                this.isInWeb = false;
                d0 *= 0.25D;
                d1 *= 0.05000000074505806D;
                d2 *= 0.25D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            double d7 = d0;
            double d8 = d1;
            double d9 = d2;

            if ((enummovetype == MoverType.SELF || enummovetype == MoverType.PLAYER) && this.onGround && this.isSneaking() && this instanceof EntityPlayer) {
                for (double d10 = 0.05D; d0 != 0.0D && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(d0, (double) (-this.stepHeight), 0.0D)).isEmpty(); d7 = d0) {
                    if (d0 < 0.05D && d0 >= -0.05D) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= 0.05D;
                    } else {
                        d0 += 0.05D;
                    }
                }

                for (; d2 != 0.0D && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(0.0D, (double) (-this.stepHeight), d2)).isEmpty(); d9 = d2) {
                    if (d2 < 0.05D && d2 >= -0.05D) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= 0.05D;
                    } else {
                        d2 += 0.05D;
                    }
                }

                for (; d0 != 0.0D && d2 != 0.0D && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(d0, (double) (-this.stepHeight), d2)).isEmpty(); d9 = d2) {
                    if (d0 < 0.05D && d0 >= -0.05D) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= 0.05D;
                    } else {
                        d0 += 0.05D;
                    }

                    d7 = d0;
                    if (d2 < 0.05D && d2 >= -0.05D) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= 0.05D;
                    } else {
                        d2 += 0.05D;
                    }
                }
            }

            List list = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().expand(d0, d1, d2));
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            int k;
            int l;

            if (d1 != 0.0D) {
                k = 0;

                for (l = list.size(); k < l; ++k) {
                    d1 = ((AxisAlignedBB) list.get(k)).calculateYOffset(this.getEntityBoundingBox(), d1);
                }

                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, d1, 0.0D));
            }

            if (d0 != 0.0D) {
                k = 0;

                for (l = list.size(); k < l; ++k) {
                    d0 = ((AxisAlignedBB) list.get(k)).calculateXOffset(this.getEntityBoundingBox(), d0);
                }

                if (d0 != 0.0D) {
                    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(d0, 0.0D, 0.0D));
                }
            }

            if (d2 != 0.0D) {
                k = 0;

                for (l = list.size(); k < l; ++k) {
                    d2 = ((AxisAlignedBB) list.get(k)).calculateZOffset(this.getEntityBoundingBox(), d2);
                }

                if (d2 != 0.0D) {
                    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, d2));
                }
            }

            boolean flag = this.onGround || d1 != d8 && d1 < 0.0D; // CraftBukkit - decompile error
            double d11;

            if (this.stepHeight > 0.0F && flag && (d7 != d0 || d9 != d2)) {
                double d12 = d0;
                double d13 = d1;
                double d14 = d2;
                AxisAlignedBB axisalignedbb1 = this.getEntityBoundingBox();

                this.setEntityBoundingBox(axisalignedbb);
                d1 = (double) this.stepHeight;
                List list1 = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().expand(d7, d1, d9));
                AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.expand(d7, 0.0D, d9);

                d11 = d1;
                int i1 = 0;

                for (int j1 = list1.size(); i1 < j1; ++i1) {
                    d11 = ((AxisAlignedBB) list1.get(i1)).calculateYOffset(axisalignedbb3, d11);
                }

                axisalignedbb2 = axisalignedbb2.offset(0.0D, d11, 0.0D);
                double d15 = d7;
                int k1 = 0;

                for (int l1 = list1.size(); k1 < l1; ++k1) {
                    d15 = ((AxisAlignedBB) list1.get(k1)).calculateXOffset(axisalignedbb2, d15);
                }

                axisalignedbb2 = axisalignedbb2.offset(d15, 0.0D, 0.0D);
                double d16 = d9;
                int i2 = 0;

                for (int j2 = list1.size(); i2 < j2; ++i2) {
                    d16 = ((AxisAlignedBB) list1.get(i2)).calculateZOffset(axisalignedbb2, d16);
                }

                axisalignedbb2 = axisalignedbb2.offset(0.0D, 0.0D, d16);
                AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
                double d17 = d1;
                int k2 = 0;

                for (int l2 = list1.size(); k2 < l2; ++k2) {
                    d17 = ((AxisAlignedBB) list1.get(k2)).calculateYOffset(axisalignedbb4, d17);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, d17, 0.0D);
                double d18 = d7;
                int i3 = 0;

                for (int j3 = list1.size(); i3 < j3; ++i3) {
                    d18 = ((AxisAlignedBB) list1.get(i3)).calculateXOffset(axisalignedbb4, d18);
                }

                axisalignedbb4 = axisalignedbb4.offset(d18, 0.0D, 0.0D);
                double d19 = d9;
                int k3 = 0;

                for (int l3 = list1.size(); k3 < l3; ++k3) {
                    d19 = ((AxisAlignedBB) list1.get(k3)).calculateZOffset(axisalignedbb4, d19);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d19);
                double d20 = d15 * d15 + d16 * d16;
                double d21 = d18 * d18 + d19 * d19;

                if (d20 > d21) {
                    d0 = d15;
                    d2 = d16;
                    d1 = -d11;
                    this.setEntityBoundingBox(axisalignedbb2);
                } else {
                    d0 = d18;
                    d2 = d19;
                    d1 = -d17;
                    this.setEntityBoundingBox(axisalignedbb4);
                }

                int i4 = 0;

                for (int j4 = list1.size(); i4 < j4; ++i4) {
                    d1 = ((AxisAlignedBB) list1.get(i4)).calculateYOffset(this.getEntityBoundingBox(), d1);
                }

                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, d1, 0.0D));
                if (d12 * d12 + d14 * d14 >= d0 * d0 + d2 * d2) {
                    d0 = d12;
                    d1 = d13;
                    d2 = d14;
                    this.setEntityBoundingBox(axisalignedbb1);
                }
            }

            this.world.profiler.endSection();
            this.world.profiler.startSection("rest");
            this.resetPositionToBB();
            this.collidedHorizontally = d7 != d0 || d9 != d2;
            this.collidedVertically = d1 != d8; // CraftBukkit - decompile error
            this.onGround = this.collidedVertically && d8 < 0.0D;
            this.collided = this.collidedHorizontally || this.collidedVertically;
            l = MathHelper.floor(this.posX);
            int k4 = MathHelper.floor(this.posY - 0.20000000298023224D);
            int l4 = MathHelper.floor(this.posZ);
            BlockPos blockposition = new BlockPos(l, k4, l4);
            IBlockState iblockdata = this.world.getBlockState(blockposition);

            if (iblockdata.getMaterial() == Material.AIR) {
                BlockPos blockposition1 = blockposition.down();
                IBlockState iblockdata1 = this.world.getBlockState(blockposition1);
                Block block = iblockdata1.getBlock();

                if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                    iblockdata = iblockdata1;
                    blockposition = blockposition1;
                }
            }

            this.updateFallState(d1, this.onGround, iblockdata, blockposition);
            if (d7 != d0) {
                this.motionX = 0.0D;
            }

            if (d9 != d2) {
                this.motionZ = 0.0D;
            }

            Block block1 = iblockdata.getBlock();

            if (d8 != d1) {
                block1.onLanded(this.world, this);
            }

            // CraftBukkit start
            if (collidedHorizontally && getBukkitEntity() instanceof Vehicle) {
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.block.Block bl = this.world.getWorld().getBlockAt(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));

                if (d7 > d0) {
                    bl = bl.getRelative(BlockFace.EAST);
                } else if (d7 < d0) {
                    bl = bl.getRelative(BlockFace.WEST);
                } else if (d9 > d2) {
                    bl = bl.getRelative(BlockFace.SOUTH);
                } else if (d9 < d2) {
                    bl = bl.getRelative(BlockFace.NORTH);
                }

                if (bl.getType() != org.bukkit.Material.AIR) {
                    VehicleBlockCollisionEvent event = new VehicleBlockCollisionEvent(vehicle, bl);
                    world.getServer().getPluginManager().callEvent(event);
                }
            }
            // CraftBukkit end

            if (this.canTriggerWalking() && (!this.onGround || !this.isSneaking() || !(this instanceof EntityPlayer)) && !this.isRiding()) {
                double d22 = this.posX - d4;
                double d23 = this.posY - d5;

                d11 = this.posZ - d6;
                if (block1 != Blocks.LADDER) {
                    d23 = 0.0D;
                }

                if (block1 != null && this.onGround) {
                    block1.onEntityWalk(this.world, blockposition, this);
                }

                this.distanceWalkedModified = (float) ((double) this.distanceWalkedModified + (double) MathHelper.sqrt(d22 * d22 + d11 * d11) * 0.6D);
                this.distanceWalkedOnStepModified = (float) ((double) this.distanceWalkedOnStepModified + (double) MathHelper.sqrt(d22 * d22 + d23 * d23 + d11 * d11) * 0.6D);
                if (this.distanceWalkedOnStepModified > (float) this.nextStepDistance && iblockdata.getMaterial() != Material.AIR) {
                    this.nextStepDistance = (int) this.distanceWalkedOnStepModified + 1;
                    if (this.isInWater()) {
                        Entity entity = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
                        float f = entity == this ? 0.35F : 0.4F;
                        float f1 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224D + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224D) * f;

                        if (f1 > 1.0F) {
                            f1 = 1.0F;
                        }

                        this.playSound(this.getSwimSound(), f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    } else {
                        this.playStepSound(blockposition, block1);
                    }
                } else if (this.distanceWalkedOnStepModified > this.nextFlap && this.makeFlySound() && iblockdata.getMaterial() == Material.AIR) {
                    this.nextFlap = this.playFlySound(this.distanceWalkedOnStepModified);
                }
            }

            // CraftBukkit start - Move to the top of the method
            /*
            try {
                this.checkBlockCollisions();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Checking entity block collision");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity being checked for collision");

                this.appendEntityCrashDetails(crashreportsystemdetails);
                throw new ReportedException(crashreport);
            }
            */
            // CraftBukkit end

            boolean flag1 = this.isWet();

            if (this.world.isFlammableWithin(this.getEntityBoundingBox().shrink(0.001D))) {
                this.burn(1);
                if (!flag1) {
                    ++this.fire;
                    if (this.fire == 0) {
                        // CraftBukkit start
                        EntityCombustEvent event = new org.bukkit.event.entity.EntityCombustByBlockEvent(null, getBukkitEntity(), 8);
                        world.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            this.setFire(event.getDuration());
                        }
                        // CraftBukkit end
                    }
                }
            } else if (this.fire <= 0) {
                this.fire = -this.getFireImmuneTicks();
            }

            if (flag1 && this.isBurning()) {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.fire = -this.getFireImmuneTicks();
            }

            this.world.profiler.endSection();
        }
    }

    public void resetPositionToBB() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_GENERIC_SWIM;
    }

    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_GENERIC_SPLASH;
    }

    protected void doBlockCollisions() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition1 = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition2 = BlockPos.PooledMutableBlockPos.retain();

        if (this.world.isAreaLoaded(blockposition_pooledblockposition, blockposition_pooledblockposition1)) {
            for (int i = blockposition_pooledblockposition.getX(); i <= blockposition_pooledblockposition1.getX(); ++i) {
                for (int j = blockposition_pooledblockposition.getY(); j <= blockposition_pooledblockposition1.getY(); ++j) {
                    for (int k = blockposition_pooledblockposition.getZ(); k <= blockposition_pooledblockposition1.getZ(); ++k) {
                        blockposition_pooledblockposition2.setPos(i, j, k);
                        IBlockState iblockdata = this.world.getBlockState(blockposition_pooledblockposition2);

                        try {
                            iblockdata.getBlock().onEntityCollidedWithBlock(this.world, (BlockPos) blockposition_pooledblockposition2, iblockdata, this);
                            this.onInsideBlock(iblockdata);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Block being collided with");

                            CrashReportCategory.addBlockInfo(crashreportsystemdetails, blockposition_pooledblockposition2, iblockdata);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }

        blockposition_pooledblockposition.release();
        blockposition_pooledblockposition1.release();
        blockposition_pooledblockposition2.release();
    }

    protected void onInsideBlock(IBlockState iblockdata) {}

    protected void playStepSound(BlockPos blockposition, Block block) {
        SoundType soundeffecttype = block.getSoundType();

        if (this.world.getBlockState(blockposition.up()).getBlock() == Blocks.SNOW_LAYER) {
            soundeffecttype = Blocks.SNOW_LAYER.getSoundType();
            this.playSound(soundeffecttype.getStepSound(), soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
        } else if (!block.getDefaultState().getMaterial().isLiquid()) {
            this.playSound(soundeffecttype.getStepSound(), soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
        }

    }

    protected float playFlySound(float f) {
        return 0.0F;
    }

    protected boolean makeFlySound() {
        return false;
    }

    public void playSound(SoundEvent soundeffect, float f, float f1) {
        if (!this.isSilent()) {
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, soundeffect, this.getSoundCategory(), f, f1);
        }

    }

    public boolean isSilent() {
        return ((Boolean) this.dataManager.get(Entity.SILENT)).booleanValue();
    }

    public void setSilent(boolean flag) {
        this.dataManager.set(Entity.SILENT, Boolean.valueOf(flag));
    }

    public boolean hasNoGravity() {
        return ((Boolean) this.dataManager.get(Entity.NO_GRAVITY)).booleanValue();
    }

    public void setNoGravity(boolean flag) {
        this.dataManager.set(Entity.NO_GRAVITY, Boolean.valueOf(flag));
    }

    protected boolean canTriggerWalking() {
        return true;
    }

    protected void updateFallState(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {
        if (flag) {
            if (this.fallDistance > 0.0F) {
                iblockdata.getBlock().onFallenUpon(this.world, blockposition, this, this.fallDistance);
            }

            this.fallDistance = 0.0F;
        } else if (d0 < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - d0);
        }

    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    protected void burn(float i) { // CraftBukkit - int -> float
        if (!this.isImmuneToFire) {
            this.attackEntityFrom(DamageSource.IN_FIRE, (float) i);
        }

    }

    public final boolean isImmuneToFire() {
        return this.isImmuneToFire;
    }

    public void fall(float f, float f1) {
        if (this.isBeingRidden()) {
            Iterator iterator = this.getPassengers().iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                entity.fall(f, f1);
            }
        }

    }

    public boolean isWet() {
        if (this.inWater) {
            return true;
        } else {
            BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain(this.posX, this.posY, this.posZ);

            if (!this.world.isRainingAt(blockposition_pooledblockposition) && !this.world.isRainingAt(blockposition_pooledblockposition.setPos(this.posX, this.posY + (double) this.height, this.posZ))) {
                blockposition_pooledblockposition.release();
                return false;
            } else {
                blockposition_pooledblockposition.release();
                return true;
            }
        }
    }

    public boolean isInWater() {
        return this.inWater;
    }

    public boolean isOverWater() {
        return this.world.handleMaterialAcceleration(this.getEntityBoundingBox().grow(0.0D, -20.0D, 0.0D).shrink(0.001D), Material.WATER, this);
    }

    public boolean handleWaterMovement() {
        return this.doWaterMovement();
    }

    public boolean doWaterMovement() {
        // Paper end
        if (this.getRidingEntity() instanceof EntityBoat) {
            this.inWater = false;
        } else if (this.world.handleMaterialAcceleration(this.getEntityBoundingBox().grow(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D), Material.WATER, this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.doWaterSplashEffect();
            }

            this.fallDistance = 0.0F;
            this.inWater = true;
            this.extinguish();
        } else {
            this.inWater = false;
        }

        return this.inWater;
    }

    protected void doWaterSplashEffect() {
        Entity entity = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
        float f = entity == this ? 0.2F : 0.9F;
        float f1 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224D + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224D) * f;

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        this.playSound(this.getSplashSound(), f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
        float f2 = (float) MathHelper.floor(this.getEntityBoundingBox().minY);

        int i;
        float f3;
        float f4;

        for (i = 0; (float) i < 1.0F + this.width * 20.0F; ++i) {
            f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (double) f3, (double) (f2 + 1.0F), this.posZ + (double) f4, this.motionX, this.motionY - (double) (this.rand.nextFloat() * 0.2F), this.motionZ, new int[0]);
        }

        for (i = 0; (float) i < 1.0F + this.width * 20.0F; ++i) {
            f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
            this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double) f3, (double) (f2 + 1.0F), this.posZ + (double) f4, this.motionX, this.motionY, this.motionZ, new int[0]);
        }

    }

    public void spawnRunningParticles() {
        if (this.isSprinting() && !this.isInWater()) {
            this.createRunningParticles();
        }

    }

    protected void createRunningParticles() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY - 0.20000000298023224D);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockposition = new BlockPos(i, j, k);
        IBlockState iblockdata = this.world.getBlockState(blockposition);

        if (iblockdata.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double) this.rand.nextFloat() - 0.5D) * (double) this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, new int[] { Block.getStateId(iblockdata)});
        }

    }

    public boolean isInsideOfMaterial(Material material) {
        if (this.getRidingEntity() instanceof EntityBoat) {
            return false;
        } else {
            double d0 = this.posY + (double) this.getEyeHeight();
            BlockPos blockposition = new BlockPos(this.posX, d0, this.posZ);
            IBlockState iblockdata = this.world.getBlockState(blockposition);

            if (iblockdata.getMaterial() == material) {
                float f = BlockLiquid.getLiquidHeightPercent(iblockdata.getBlock().getMetaFromState(iblockdata)) - 0.11111111F;
                float f1 = (float) (blockposition.getY() + 1) - f;
                boolean flag = d0 < (double) f1;

                return !flag && this instanceof EntityPlayer ? false : flag;
            } else {
                return false;
            }
        }
    }

    public boolean isInLava() {
        return this.world.isMaterialInBB(this.getEntityBoundingBox().grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
    }

    public void moveRelative(float f, float f1, float f2, float f3) {
        float f4 = f * f + f1 * f1 + f2 * f2;

        if (f4 >= 1.0E-4F) {
            f4 = MathHelper.sqrt(f4);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f3 / f4;
            f *= f4;
            f1 *= f4;
            f2 *= f4;
            float f5 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f6 = MathHelper.cos(this.rotationYaw * 0.017453292F);

            this.motionX += (double) (f * f6 - f2 * f5);
            this.motionY += (double) f1;
            this.motionZ += (double) (f2 * f6 + f * f5);
        }
    }

    public float getBrightness() {
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));

        if (this.world.isBlockLoaded(blockposition_mutableblockposition)) {
            blockposition_mutableblockposition.setY(MathHelper.floor(this.posY + (double) this.getEyeHeight()));
            return this.world.getLightBrightness(blockposition_mutableblockposition);
        } else {
            return 0.0F;
        }
    }

    public void setWorld(World world) {
        // CraftBukkit start
        if (world == null) {
            setDead();
            this.world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
            return;
        }
        // CraftBukkit end
        this.world = world;
    }

    public void setPositionAndRotation(double d0, double d1, double d2, float f, float f1) {
        this.posX = MathHelper.clamp(d0, -3.0E7D, 3.0E7D);
        this.posY = d1;
        this.posZ = MathHelper.clamp(d2, -3.0E7D, 3.0E7D);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        f1 = MathHelper.clamp(f1, -90.0F, 90.0F);
        this.rotationYaw = f;
        this.rotationPitch = f1;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        double d3 = (double) (this.prevRotationYaw - f);

        if (d3 < -180.0D) {
            this.prevRotationYaw += 360.0F;
        }

        if (d3 >= 180.0D) {
            this.prevRotationYaw -= 360.0F;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(f, f1);
    }

    public void moveToBlockPosAndAngles(BlockPos blockposition, float f, float f1) {
        this.setLocationAndAngles((double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, f, f1);
    }

    public void setLocationAndAngles(double d0, double d1, double d2, float f, float f1) {
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        this.rotationYaw = f;
        this.rotationPitch = f1;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public float getDistance(Entity entity) {
        float f = (float) (this.posX - entity.posX);
        float f1 = (float) (this.posY - entity.posY);
        float f2 = (float) (this.posZ - entity.posZ);

        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public double getDistanceSq(double d0, double d1, double d2) {
        double d3 = this.posX - d0;
        double d4 = this.posY - d1;
        double d5 = this.posZ - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double getDistanceSq(BlockPos blockposition) {
        return blockposition.distanceSq(this.posX, this.posY, this.posZ);
    }

    public double getDistanceSqToCenter(BlockPos blockposition) {
        return blockposition.distanceSqToCenter(this.posX, this.posY, this.posZ);
    }

    public double getDistance(double d0, double d1, double d2) {
        double d3 = this.posX - d0;
        double d4 = this.posY - d1;
        double d5 = this.posZ - d2;

        return (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
    }

    public double getDistanceSq(Entity entity) {
        double d0 = this.posX - entity.posX;
        double d1 = this.posY - entity.posY;
        double d2 = this.posZ - entity.posZ;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void onCollideWithPlayer(EntityPlayer entityhuman) {}

    public void applyEntityCollision(Entity entity) {
        if (!this.isRidingSameEntity(entity)) {
            if (!entity.noClip && !this.noClip) {
                double d0 = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                double d2 = MathHelper.absMax(d0, d1);

                if (d2 >= 0.009999999776482582D) {
                    d2 = (double) MathHelper.sqrt(d2);
                    d0 /= d2;
                    d1 /= d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }

                    d0 *= d3;
                    d1 *= d3;
                    d0 *= 0.05000000074505806D;
                    d1 *= 0.05000000074505806D;
                    d0 *= (double) (1.0F - this.entityCollisionReduction);
                    d1 *= (double) (1.0F - this.entityCollisionReduction);
                    if (!this.isBeingRidden()) {
                        this.addVelocity(-d0, 0.0D, -d1);
                    }

                    if (!entity.isBeingRidden()) {
                        entity.addVelocity(d0, 0.0D, d1);
                    }
                }

            }
        }
    }

    public void addVelocity(double d0, double d1, double d2) {
        this.motionX += d0;
        this.motionY += d1;
        this.motionZ += d2;
        this.isAirBorne = true;
    }

    protected void markVelocityChanged() {
        this.velocityChanged = true;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else {
            this.markVelocityChanged();
            return false;
        }
    }

    public Vec3d getLook(float f) {
        if (f == 1.0F) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
        } else {
            float f1 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * f;
            float f2 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * f;

            return this.getVectorForRotation(f1, f2);
        }
    }

    protected final Vec3d getVectorForRotation(float f, float f1) {
        float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);

        return new Vec3d((double) (f3 * f4), (double) f5, (double) (f2 * f4));
    }

    public Vec3d getPositionEyes(float f) {
        if (f == 1.0F) {
            return new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ);
        } else {
            double d0 = this.prevPosX + (this.posX - this.prevPosX) * (double) f;
            double d1 = this.prevPosY + (this.posY - this.prevPosY) * (double) f + (double) this.getEyeHeight();
            double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double) f;

            return new Vec3d(d0, d1, d2);
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean canBePushed() {
        return false;
    }

    public void awardKillScore(Entity entity, int i, DamageSource damagesource) {
        if (entity instanceof EntityPlayerMP) {
            CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger((EntityPlayerMP) entity, this, damagesource);
        }

    }

    public boolean writeToNBTAtomically(NBTTagCompound nbttagcompound) {
        String s = this.getEntityString();

        if (!this.isDead && s != null) {
            nbttagcompound.setString("id", s);
            this.writeToNBT(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public boolean writeToNBTOptional(NBTTagCompound nbttagcompound) {
        String s = this.getEntityString();

        if (!this.isDead && s != null && !this.isRiding()) {
            nbttagcompound.setString("id", s);
            this.writeToNBT(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public static void registerFixes(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.ENTITY, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (nbttagcompound.hasKey("Passengers", 9)) {
                    NBTTagList nbttaglist = nbttagcompound.getTagList("Passengers", 10);

                    for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                        nbttaglist.set(j, dataconverter.process(FixTypes.ENTITY, nbttaglist.getCompoundTagAt(j), i));
                    }
                }

                return nbttagcompound;
            }
        });
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        try {
            nbttagcompound.setTag("Pos", this.newDoubleNBTList(new double[] { this.posX, this.posY, this.posZ}));
            nbttagcompound.setTag("Motion", this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ}));

            // CraftBukkit start - Checking for NaN pitch/yaw and resetting to zero
            // TODO: make sure this is the best way to address this.
            if (Float.isNaN(this.rotationYaw)) {
                this.rotationYaw = 0;
            }

            if (Float.isNaN(this.rotationPitch)) {
                this.rotationPitch = 0;
            }
            // CraftBukkit end

            nbttagcompound.setTag("Rotation", this.newFloatNBTList(new float[] { this.rotationYaw, this.rotationPitch}));
            nbttagcompound.setFloat("FallDistance", this.fallDistance);
            nbttagcompound.setShort("Fire", (short) this.fire);
            nbttagcompound.setShort("Air", (short) this.getAir());
            nbttagcompound.setBoolean("OnGround", this.onGround);
            nbttagcompound.setInteger("Dimension", this.dimension);
            nbttagcompound.setBoolean("Invulnerable", this.invulnerable);
            nbttagcompound.setInteger("PortalCooldown", this.timeUntilPortal);
            nbttagcompound.setUniqueId("UUID", this.getUniqueID());
            // CraftBukkit start
            // PAIL: Check above UUID reads 1.8 properly, ie: UUIDMost / UUIDLeast
            nbttagcompound.setLong("WorldUUIDLeast", this.world.getSaveHandler().getUUID().getLeastSignificantBits());
            nbttagcompound.setLong("WorldUUIDMost", this.world.getSaveHandler().getUUID().getMostSignificantBits());
            nbttagcompound.setInteger("Bukkit.updateLevel", CURRENT_LEVEL);
            nbttagcompound.setInteger("Spigot.ticksLived", this.ticksExisted);
            // CraftBukkit end
            if (this.hasCustomName()) {
                nbttagcompound.setString("CustomName", this.getCustomNameTag());
            }

            if (this.getAlwaysRenderNameTag()) {
                nbttagcompound.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
            }

            this.cmdResultStats.writeStatsToNBT(nbttagcompound);
            if (this.isSilent()) {
                nbttagcompound.setBoolean("Silent", this.isSilent());
            }

            if (this.hasNoGravity()) {
                nbttagcompound.setBoolean("NoGravity", this.hasNoGravity());
            }

            if (this.glowing) {
                nbttagcompound.setBoolean("Glowing", this.glowing);
            }

            NBTTagList nbttaglist;
            Iterator iterator;

            if (!this.tags.isEmpty()) {
                nbttaglist = new NBTTagList();
                iterator = this.tags.iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();

                    nbttaglist.appendTag(new NBTTagString(s));
                }

                nbttagcompound.setTag("Tags", nbttaglist);
            }

            this.writeEntityToNBT(nbttagcompound);
            if (this.isBeingRidden()) {
                nbttaglist = new NBTTagList();
                iterator = this.getPassengers().iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    if (entity.writeToNBTAtomically(nbttagcompound1)) {
                        nbttaglist.appendTag(nbttagcompound1);
                    }
                }

                if (!nbttaglist.hasNoTags()) {
                    nbttagcompound.setTag("Passengers", nbttaglist);
                }
            }

            // Paper start - Save the entity's origin location
            if (origin != null) {
                nbttagcompound.setTag("Paper.Origin", this.createList(origin.getX(), origin.getY(), origin.getZ()));
            }
            // Save entity's from mob spawner status
            if (spawnedViaMobSpawner) {
                nbttagcompound.setBoolean("Paper.FromMobSpawner", true);
            }
            // Paper end
            return nbttagcompound;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Entity being saved");

            this.addEntityCrashInfo(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        try {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
            NBTTagList nbttaglist1 = nbttagcompound.getTagList("Motion", 6);
            NBTTagList nbttaglist2 = nbttagcompound.getTagList("Rotation", 5);

            this.motionX = nbttaglist1.getDoubleAt(0);
            this.motionY = nbttaglist1.getDoubleAt(1);
            this.motionZ = nbttaglist1.getDoubleAt(2);

            /* CraftBukkit start - Moved section down
            if (Math.abs(this.motX) > 10.0D) {
                this.motX = 0.0D;
            }

            if (Math.abs(this.motY) > 10.0D) {
                this.motY = 0.0D;
            }

            if (Math.abs(this.motZ) > 10.0D) {
                this.motZ = 0.0D;
            }
            // CraftBukkit end */

            this.posX = nbttaglist.getDoubleAt(0);
            this.posY = nbttaglist.getDoubleAt(1);
            this.posZ = nbttaglist.getDoubleAt(2);
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.rotationYaw = nbttaglist2.getFloatAt(0);
            this.rotationPitch = nbttaglist2.getFloatAt(1);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
            this.setRotationYawHead(this.rotationYaw);
            this.setRenderYawOffset(this.rotationYaw);
            this.fallDistance = nbttagcompound.getFloat("FallDistance");
            this.fire = nbttagcompound.getShort("Fire");
            this.setAir(nbttagcompound.getShort("Air"));
            this.onGround = nbttagcompound.getBoolean("OnGround");
            if (nbttagcompound.hasKey("Dimension")) {
                this.dimension = nbttagcompound.getInteger("Dimension");
            }

            this.invulnerable = nbttagcompound.getBoolean("Invulnerable");
            this.timeUntilPortal = nbttagcompound.getInteger("PortalCooldown");
            if (nbttagcompound.hasUniqueId("UUID")) {
                this.entityUniqueID = nbttagcompound.getUniqueId("UUID");
                this.cachedUniqueIdString = this.entityUniqueID.toString();
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if (nbttagcompound.hasKey("CustomName", 8)) {
                this.setCustomNameTag(nbttagcompound.getString("CustomName"));
            }

            this.setAlwaysRenderNameTag(nbttagcompound.getBoolean("CustomNameVisible"));
            this.cmdResultStats.readStatsFromNBT(nbttagcompound);
            this.setSilent(nbttagcompound.getBoolean("Silent"));
            this.setNoGravity(nbttagcompound.getBoolean("NoGravity"));
            this.setGlowing(nbttagcompound.getBoolean("Glowing"));
            if (nbttagcompound.hasKey("Tags", 9)) {
                this.tags.clear();
                NBTTagList nbttaglist3 = nbttagcompound.getTagList("Tags", 8);
                int i = Math.min(nbttaglist3.tagCount(), 1024);

                for (int j = 0; j < i; ++j) {
                    this.tags.add(nbttaglist3.getStringTagAt(j));
                }
            }

            this.readEntityFromNBT(nbttagcompound);
            if (this.shouldSetPosAfterLoading()) {
                this.setPosition(this.posX, this.posY, this.posZ);
            }

            // CraftBukkit start
            if (this instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) this;

                this.ticksExisted = nbttagcompound.getInteger("Spigot.ticksLived");

                // Reset the persistence for tamed animals
                if (entity instanceof EntityTameable && !isLevelAtLeast(nbttagcompound, 2) && !nbttagcompound.getBoolean("PersistenceRequired")) {
                    EntityLiving entityinsentient = (EntityLiving) entity;
                    entityinsentient.persistenceRequired = !entityinsentient.canDespawn();
                }
            }
            // CraftBukkit end

            // CraftBukkit start
            double limit = getBukkitEntity() instanceof Vehicle ? 100.0D : 10.0D;
            if (Math.abs(this.motionX) > limit) {
                this.motionX = 0.0D;
            }

            if (Math.abs(this.motionY) > limit) {
                this.motionY = 0.0D;
            }

            if (Math.abs(this.motionZ) > limit) {
                this.motionZ = 0.0D;
            }
            // CraftBukkit end

            // CraftBukkit start - Reset world
            if (this instanceof EntityPlayerMP) {
                Server server = Bukkit.getServer();
                org.bukkit.World bworld = null;

                // TODO: Remove World related checks, replaced with WorldUID
                String worldName = nbttagcompound.getString("world");

                if (nbttagcompound.hasKey("WorldUUIDMost") && nbttagcompound.hasKey("WorldUUIDLeast")) {
                    UUID uid = new UUID(nbttagcompound.getLong("WorldUUIDMost"), nbttagcompound.getLong("WorldUUIDLeast"));
                    bworld = server.getWorld(uid);
                } else {
                    bworld = server.getWorld(worldName);
                }

                if (bworld == null) {
                    EntityPlayerMP entityPlayer = (EntityPlayerMP) this;
                    bworld = ((org.bukkit.craftbukkit.CraftServer) server).getServer().getWorld(entityPlayer.dimension).getWorld();
                }

                setWorld(bworld == null? null : ((CraftWorld) bworld).getHandle());
            }
            // CraftBukkit end

            // Paper start - Restore the entity's origin location
            NBTTagList originTag = nbttagcompound.getTagList("Paper.Origin", 6);
            if (!originTag.hasNoTags()) {
                origin = new Location(world.getWorld(), originTag.getDoubleAt(0), originTag.getDoubleAt(1), originTag.getDoubleAt(2));
            }

            spawnedViaMobSpawner = nbttagcompound.getBoolean("Paper.FromMobSpawner"); // Restore entity's from mob spawner status
            // Paper end

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("Entity being loaded");

            this.addEntityCrashInfo(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    protected boolean shouldSetPosAfterLoading() {
        return true;
    }

    @Nullable
    public final String getEntityString() {
        ResourceLocation minecraftkey = EntityList.getKey(this);

        return minecraftkey == null ? null : minecraftkey.toString();
    }

    protected abstract void readEntityFromNBT(NBTTagCompound nbttagcompound);

    protected abstract void writeEntityToNBT(NBTTagCompound nbttagcompound);

    protected NBTTagList createList(double... adouble) { return newDoubleNBTList(adouble); } // Paper - OBFHELPER
    protected NBTTagList newDoubleNBTList(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.appendTag(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    protected NBTTagList newFloatNBTList(float... afloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat1 = afloat;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float f = afloat1[j];

            nbttaglist.appendTag(new NBTTagFloat(f));
        }

        return nbttaglist;
    }

    @Nullable
    public EntityItem dropItem(Item item, int i) {
        return this.dropItemWithOffset(item, i, 0.0F);
    }

    @Nullable
    public EntityItem dropItemWithOffset(Item item, int i, float f) {
        return this.entityDropItem(new ItemStack(item, i, 0), f);
    }

    @Nullable public final EntityItem dropItem(ItemStack itemstack, float offset) { return this.entityDropItem(itemstack, offset); } // Paper - OBFHELPER
    @Nullable
    public EntityItem entityDropItem(ItemStack itemstack, float f) {
        if (itemstack.isEmpty()) {
            return null;
        } else {
            // CraftBukkit start - Capture drops for death event
            if (this instanceof EntityLivingBase && !((EntityLivingBase) this).forceDrops) {
                ((EntityLivingBase) this).drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(itemstack));
                return null;
            }
            // CraftBukkit end
            EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + (double) f, this.posZ, itemstack);

            entityitem.setDefaultPickupDelay();
            this.world.spawnEntity(entityitem);
            return entityitem;
        }
    }

    public boolean isEntityAlive() {
        return !this.isDead;
    }

    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        } else {
            BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

            for (int i = 0; i < 8; ++i) {
                int j = MathHelper.floor(this.posY + (double) (((float) ((i >> 0) % 2) - 0.5F) * 0.1F) + (double) this.getEyeHeight());
                int k = MathHelper.floor(this.posX + (double) (((float) ((i >> 1) % 2) - 0.5F) * this.width * 0.8F));
                int l = MathHelper.floor(this.posZ + (double) (((float) ((i >> 2) % 2) - 0.5F) * this.width * 0.8F));

                if (blockposition_pooledblockposition.getX() != k || blockposition_pooledblockposition.getY() != j || blockposition_pooledblockposition.getZ() != l) {
                    blockposition_pooledblockposition.setPos(k, j, l);
                    if (this.world.getBlockState(blockposition_pooledblockposition).causesSuffocation()) {
                        blockposition_pooledblockposition.release();
                        return true;
                    }
                }
            }

            blockposition_pooledblockposition.release();
            return false;
        }
    }

    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        return false;
    }

    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return null;
    }

    public void updateRidden() {
        Entity entity = this.getRidingEntity();

        if (this.isRiding() && entity.isDead) {
            this.dismountRidingEntity();
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.onUpdate();
            if (this.isRiding()) {
                entity.updatePassenger(this);
            }
        }
    }

    public void updatePassenger(Entity entity) {
        if (this.isPassenger(entity)) {
            entity.setPosition(this.posX, this.posY + this.getMountedYOffset() + entity.getYOffset(), this.posZ);
        }
    }

    public double getYOffset() {
        return 0.0D;
    }

    public double getMountedYOffset() {
        return (double) this.height * 0.75D;
    }

    public boolean startRiding(Entity entity) {
        return this.startRiding(entity, false);
    }

    public boolean startRiding(Entity entity, boolean flag) {
        for (Entity entity1 = entity; entity1.ridingEntity != null; entity1 = entity1.ridingEntity) {
            if (entity1.ridingEntity == this) {
                return false;
            }
        }

        if (!flag && (!this.canBeRidden(entity) || !entity.canFitPassenger(this))) {
            return false;
        } else {
            if (this.isRiding()) {
                this.dismountRidingEntity();
            }

            this.ridingEntity = entity;
            this.ridingEntity.addPassenger(this);
            return true;
        }
    }

    protected boolean canBeRidden(Entity entity) {
        return this.rideCooldown <= 0;
    }

    public void removePassengers() {
        for (int i = this.riddenByEntities.size() - 1; i >= 0; --i) {
            ((Entity) this.riddenByEntities.get(i)).dismountRidingEntity();
        }

    }

    public void dismountRidingEntity() {
        if (this.ridingEntity != null) {
            Entity entity = this.ridingEntity;

            this.ridingEntity = null;
            entity.removePassenger(this);
        }

    }

    protected void addPassenger(Entity entity) {
        if (entity == this) throw new IllegalArgumentException("Entities cannot become a passenger of themselves"); // Paper - issue 572
        if (entity.getRidingEntity() != this) {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        } else {
            // CraftBukkit start
            com.google.common.base.Preconditions.checkState(!entity.riddenByEntities.contains(this), "Circular entity riding! %s %s", this, entity);

            CraftEntity craft = (CraftEntity) entity.getBukkitEntity().getVehicle();
            Entity orig = craft == null ? null : craft.getHandle();
            if (getBukkitEntity() instanceof Vehicle && entity.getBukkitEntity() instanceof LivingEntity && entity.world.isChunkLoaded((int) entity.posX >> 4, (int) entity.posZ >> 4, false)) { // Boolean not used
                VehicleEnterEvent event = new VehicleEnterEvent(
                        (Vehicle) getBukkitEntity(),
                         entity.getBukkitEntity()
                );
                Bukkit.getPluginManager().callEvent(event);
                CraftEntity craftn = (CraftEntity) entity.getBukkitEntity().getVehicle();
                Entity n = craftn == null ? null : craftn.getHandle();
                if (event.isCancelled() || n != orig) {
                    return;
                }
            }
            // CraftBukkit end
            // Spigot start
            org.spigotmc.event.entity.EntityMountEvent event = new org.spigotmc.event.entity.EntityMountEvent(entity.getBukkitEntity(), this.getBukkitEntity());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            // Spigot end
            if (!this.world.isRemote && entity instanceof EntityPlayer && !(this.getControllingPassenger() instanceof EntityPlayer)) {
                this.riddenByEntities.add(0, entity);
            } else {
                this.riddenByEntities.add(entity);
            }

        }
    }

    protected void removePassenger(Entity entity) {
        if (entity.getRidingEntity() == this) {
            throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
        } else {
            // CraftBukkit start
            entity.setVehicle(this); // Paper - Set the vehicle back for the event
            CraftEntity craft = (CraftEntity) entity.getBukkitEntity().getVehicle();
            Entity orig = craft == null ? null : craft.getHandle();
            if (getBukkitEntity() instanceof Vehicle && entity.getBukkitEntity() instanceof LivingEntity) {
                VehicleExitEvent event = new VehicleExitEvent(
                        (Vehicle) getBukkitEntity(),
                        (LivingEntity) entity.getBukkitEntity()
                );
                Bukkit.getPluginManager().callEvent(event);
                CraftEntity craftn = (CraftEntity) entity.getBukkitEntity().getVehicle();
                Entity n = craftn == null ? null : craftn.getHandle();
                if (event.isCancelled() || n != orig) {
                    return;
                }
            }
            // CraftBukkit end
            // Paper start - make EntityDismountEvent cancellable
            if (!new org.spigotmc.event.entity.EntityDismountEvent(entity.getBukkitEntity(), this.getBukkitEntity()).callEvent()) {
                return;
            }
            entity.setVehicle(null);
            // Paper end

            this.riddenByEntities.remove(entity);
            entity.rideCooldown = 60;
        }
    }

    protected boolean canFitPassenger(Entity entity) {
        return this.getPassengers().size() < 1;
    }

    public float getCollisionBorderSize() {
        return 0.0F;
    }

    public Vec3d getLookVec() {
        return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
    }

    public void setPortal(BlockPos blockposition) {
        if (this.timeUntilPortal > 0) {
            this.timeUntilPortal = this.getPortalCooldown();
        } else {
            if (!this.world.isRemote && !blockposition.equals(this.lastPortalPos)) {
                this.lastPortalPos = new BlockPos(blockposition);
                BlockPattern.PatternHelper shapedetector_shapedetectorcollection = Blocks.PORTAL.createPatternHelper(this.world, this.lastPortalPos);
                double d0 = shapedetector_shapedetectorcollection.getForwards().getAxis() == EnumFacing.Axis.X ? (double) shapedetector_shapedetectorcollection.getFrontTopLeft().getZ() : (double) shapedetector_shapedetectorcollection.getFrontTopLeft().getX();
                double d1 = shapedetector_shapedetectorcollection.getForwards().getAxis() == EnumFacing.Axis.X ? this.posZ : this.posX;

                d1 = Math.abs(MathHelper.pct(d1 - (double) (shapedetector_shapedetectorcollection.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - (double) shapedetector_shapedetectorcollection.getWidth()));
                double d2 = MathHelper.pct(this.posY - 1.0D, (double) shapedetector_shapedetectorcollection.getFrontTopLeft().getY(), (double) (shapedetector_shapedetectorcollection.getFrontTopLeft().getY() - shapedetector_shapedetectorcollection.getHeight()));

                this.lastPortalVec = new Vec3d(d1, d2, 0.0D);
                this.teleportDirection = shapedetector_shapedetectorcollection.getForwards();
            }

            this.inPortal = true;
        }
    }

    public int getPortalCooldown() {
        return 300;
    }

    public Iterable<ItemStack> getHeldEquipment() {
        return Entity.EMPTY_EQUIPMENT;
    }

    public Iterable<ItemStack> getArmorInventoryList() {
        return Entity.EMPTY_EQUIPMENT;
    }

    public Iterable<ItemStack> getEquipmentAndArmor() {
        return Iterables.concat(this.getHeldEquipment(), this.getArmorInventoryList());
    }

    public void setItemStackToSlot(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {}

    public boolean isBurning() {
        boolean flag = this.world != null && this.world.isRemote;

        return !this.isImmuneToFire && (this.fire > 0 || flag && this.getFlag(0));
    }

    public boolean isRiding() {
        return this.getRidingEntity() != null;
    }

    public boolean isBeingRidden() {
        return !this.getPassengers().isEmpty();
    }

    public boolean isSneaking() {
        return this.getFlag(1);
    }

    public void setSneaking(boolean flag) {
        this.setFlag(1, flag);
    }

    public boolean isSprinting() {
        return this.getFlag(3);
    }

    public void setSprinting(boolean flag) {
        this.setFlag(3, flag);
    }

    public boolean isGlowing() {
        return this.glowing || this.world.isRemote && this.getFlag(6);
    }

    public void setGlowing(boolean flag) {
        this.glowing = flag;
        if (!this.world.isRemote) {
            this.setFlag(6, this.glowing);
        }

    }

    public boolean isInvisible() {
        return this.getFlag(5);
    }

    @Nullable public Team getTeam() { return this.getTeam(); } // Paper - OBFHELPER
    @Nullable
    public Team getTeam() {
        if (!this.world.paperConfig.nonPlayerEntitiesOnScoreboards && !(this instanceof EntityPlayer)) { return null; } // Paper
        return this.world.getScoreboard().getPlayersTeam(this.getCachedUniqueIdString());
    }

    public boolean isOnSameTeam(Entity entity) {
        return this.isOnScoreboardTeam(entity.getTeam());
    }

    public boolean isOnScoreboardTeam(Team scoreboardteambase) {
        return this.getTeam() != null ? this.getTeam().isSameTeam(scoreboardteambase) : false;
    }

    public void setInvisible(boolean flag) {
        this.setFlag(5, flag);
    }

    public boolean getFlag(int i) {
        return (((Byte) this.dataManager.get(Entity.FLAGS)).byteValue() & 1 << i) != 0;
    }

    public void setFlag(int i, boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(Entity.FLAGS)).byteValue();

        if (flag) {
            this.dataManager.set(Entity.FLAGS, Byte.valueOf((byte) (b0 | 1 << i)));
        } else {
            this.dataManager.set(Entity.FLAGS, Byte.valueOf((byte) (b0 & ~(1 << i))));
        }

    }

    public int getAir() {
        return ((Integer) this.dataManager.get(Entity.AIR)).intValue();
    }

    public void setAir(int i) {
        // CraftBukkit start
        EntityAirChangeEvent event = new EntityAirChangeEvent(this.getBukkitEntity(), i);
        event.getEntity().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        this.dataManager.set(Entity.AIR, Integer.valueOf(event.getAmount()));
        // CraftBukkit end
    }

    public void onStruckByLightning(EntityLightningBolt entitylightning) {
        // CraftBukkit start
        final org.bukkit.entity.Entity thisBukkitEntity = this.getBukkitEntity();
        final org.bukkit.entity.Entity stormBukkitEntity = entitylightning.getBukkitEntity();
        final PluginManager pluginManager = Bukkit.getPluginManager();

        if (thisBukkitEntity instanceof Hanging) {
            HangingBreakByEntityEvent hangingEvent = new HangingBreakByEntityEvent((Hanging) thisBukkitEntity, stormBukkitEntity);
            pluginManager.callEvent(hangingEvent);

            if (hangingEvent.isCancelled()) {
                return;
            }
        }

        if (this.isImmuneToFire) {
            return;
        }
        CraftEventFactory.entityDamage = entitylightning;
        if (!this.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 5.0F)) {
            CraftEventFactory.entityDamage = null;
            return;
        }
        // CraftBukkit end
        ++this.fire;
        if (this.fire == 0) {
            // CraftBukkit start - Call a combust event when lightning strikes
            EntityCombustByEntityEvent entityCombustEvent = new EntityCombustByEntityEvent(stormBukkitEntity, thisBukkitEntity, 8);
            pluginManager.callEvent(entityCombustEvent);
            if (!entityCombustEvent.isCancelled()) {
                this.setFire(entityCombustEvent.getDuration());
            }
            // CraftBukkit end
        }

    }

    public void onKillEntity(EntityLivingBase entityliving) {}

    protected boolean pushOutOfBlocks(double d0, double d1, double d2) {
        BlockPos blockposition = new BlockPos(d0, d1, d2);
        double d3 = d0 - (double) blockposition.getX();
        double d4 = d1 - (double) blockposition.getY();
        double d5 = d2 - (double) blockposition.getZ();

        if (!this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
            return false;
        } else {
            EnumFacing enumdirection = EnumFacing.UP;
            double d6 = Double.MAX_VALUE;

            if (!this.world.isBlockFullCube(blockposition.west()) && d3 < d6) {
                d6 = d3;
                enumdirection = EnumFacing.WEST;
            }

            if (!this.world.isBlockFullCube(blockposition.east()) && 1.0D - d3 < d6) {
                d6 = 1.0D - d3;
                enumdirection = EnumFacing.EAST;
            }

            if (!this.world.isBlockFullCube(blockposition.north()) && d5 < d6) {
                d6 = d5;
                enumdirection = EnumFacing.NORTH;
            }

            if (!this.world.isBlockFullCube(blockposition.south()) && 1.0D - d5 < d6) {
                d6 = 1.0D - d5;
                enumdirection = EnumFacing.SOUTH;
            }

            if (!this.world.isBlockFullCube(blockposition.up()) && 1.0D - d4 < d6) {
                d6 = 1.0D - d4;
                enumdirection = EnumFacing.UP;
            }

            float f = this.rand.nextFloat() * 0.2F + 0.1F;
            float f1 = (float) enumdirection.getAxisDirection().getOffset();

            if (enumdirection.getAxis() == EnumFacing.Axis.X) {
                this.motionX = (double) (f1 * f);
                this.motionY *= 0.75D;
                this.motionZ *= 0.75D;
            } else if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
                this.motionX *= 0.75D;
                this.motionY = (double) (f1 * f);
                this.motionZ *= 0.75D;
            } else if (enumdirection.getAxis() == EnumFacing.Axis.Z) {
                this.motionX *= 0.75D;
                this.motionY *= 0.75D;
                this.motionZ = (double) (f1 * f);
            }

            return true;
        }
    }

    public void setInWeb() {
        this.isInWeb = true;
        this.fallDistance = 0.0F;
    }

    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        } else {
            String s = EntityList.getEntityString(this);

            if (s == null) {
                s = "generic";
            }

            return I18n.translateToLocal("entity." + s + ".name");
        }
    }

    @Nullable
    public Entity[] getParts() {
        return null;
    }

    public boolean isEntityEqual(Entity entity) {
        return this == entity;
    }

    public float getRotationYawHead() {
        return 0.0F;
    }

    public void setRotationYawHead(float f) {}

    public void setRenderYawOffset(float f) {}

    public boolean canBeAttackedWithItem() {
        return true;
    }

    public boolean hitByEntity(Entity entity) {
        return false;
    }

    public String toString() {
        return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[] { this.getClass().getSimpleName(), this.getName(), Integer.valueOf(this.entityId), this.world == null ? "~NULL~" : this.world.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ)});
    }

    public boolean isEntityInvulnerable(DamageSource damagesource) {
        return this.invulnerable && damagesource != DamageSource.OUT_OF_WORLD && !damagesource.isCreativePlayer();
    }

    public boolean getIsInvulnerable() {
        return this.invulnerable;
    }

    public void setEntityInvulnerable(boolean flag) {
        this.invulnerable = flag;
    }

    public void copyLocationAndAnglesFrom(Entity entity) {
        this.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
    }

    private void copyDataFromOld(Entity entity) {
        NBTTagCompound nbttagcompound = entity.writeToNBT(new NBTTagCompound());

        nbttagcompound.removeTag("Dimension");
        this.readFromNBT(nbttagcompound);
        this.timeUntilPortal = entity.timeUntilPortal;
        this.lastPortalPos = entity.lastPortalPos;
        this.lastPortalVec = entity.lastPortalVec;
        this.teleportDirection = entity.teleportDirection;
    }

    @Nullable
    public Entity changeDimension(int i) {
        if (!this.world.isRemote && !this.isDead) {
            this.world.profiler.startSection("changeDimension");
            MinecraftServer minecraftserver = this.getServer();
            // CraftBukkit start - Move logic into new function "teleportTo(Location,boolean)"
            // int j = this.dimension;
            // WorldServer worldserver = minecraftserver.getWorldServer(j);
            // WorldServer worldserver1 = minecraftserver.getWorldServer(i);
            WorldServer exitWorld = null;
            if (this.dimension < CraftWorld.CUSTOM_DIMENSION_OFFSET) { // Plugins must specify exit from custom Bukkit worlds
                // Only target existing worlds (compensate for allow-nether/allow-end as false)
                for (WorldServer world : minecraftserver.worlds) {
                    if (world.dimension == i) {
                        exitWorld = world;
                    }
                }
            }

            BlockPos blockposition = null; // PAIL: CHECK
            Location enter = this.getBukkitEntity().getLocation();
            Location exit;
            if (exitWorld != null) {
                if (blockposition != null) {
                    exit = new Location(exitWorld.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
                } else {
                    exit = minecraftserver.getPlayerList().calculateTarget(enter, minecraftserver.getWorld(i));
                }
            }
            else {
                exit = null;
            }
            boolean useTravelAgent = exitWorld != null && !(this.dimension == 1 && exitWorld.dimension == 1); // don't use agent for custom worlds or return from THE_END

            TravelAgent agent = exit != null ? (TravelAgent) ((CraftWorld) exit.getWorld()).getHandle().getDefaultTeleporter() : org.bukkit.craftbukkit.CraftTravelAgent.DEFAULT; // return arbitrary TA to compensate for implementation dependent plugins
            boolean oldCanCreate = agent.getCanCreatePortal();
            agent.setCanCreatePortal(false); // General entities cannot create portals

            EntityPortalEvent event = new EntityPortalEvent(this.getBukkitEntity(), enter, exit, agent);
            event.useTravelAgent(useTravelAgent);
            event.getEntity().getServer().getPluginManager().callEvent(event);
            if (event.isCancelled() || event.getTo() == null || event.getTo().getWorld() == null || !this.isEntityAlive()) {
                agent.setCanCreatePortal(oldCanCreate);
                return null;
            }
            exit = event.useTravelAgent() ? event.getPortalTravelAgent().findOrCreate(event.getTo()) : event.getTo();
            agent.setCanCreatePortal(oldCanCreate);

            // Need to make sure the profiler state is reset afterwards (but we still want to time the call)
            Entity entity = this.teleportTo(exit, true);
            this.world.profiler.endSection();
            return entity;
        }
        return null;
    }

    public Entity teleportTo(Location exit, boolean portal) {
        if (!this.isDead) { // Paper
            WorldServer worldserver = ((CraftWorld) getBukkitEntity().getLocation().getWorld()).getHandle();
            WorldServer worldserver1 = ((CraftWorld) exit.getWorld()).getHandle();
            int i = worldserver1.dimension;
            // CraftBukkit end

            this.dimension = i;
            /* CraftBukkit start - TODO: Check if we need this
            if (j == 1 && i == 1) {
                worldserver1 = minecraftserver.getWorldServer(0);
                this.dimension = 0;
            }
            // CraftBukkit end */

            this.world.removeEntityDangerously(this); // Paper - Fully remove entity, can't have dupes in the UUID map
            this.isDead = false;
            this.world.profiler.startSection("reposition");
            /* CraftBukkit start - Handled in calculateTarget
            BlockPosition blockposition;

            if (i == 1) {
                blockposition = worldserver1.getDimensionSpawn();
            } else {
                double d0 = this.locX;
                double d1 = this.locZ;
                double d2 = 8.0D;

                if (i == -1) {
                    d0 = MathHelper.a(d0 / 8.0D, worldserver1.getWorldBorder().b() + 16.0D, worldserver1.getWorldBorder().d() - 16.0D);
                    d1 = MathHelper.a(d1 / 8.0D, worldserver1.getWorldBorder().c() + 16.0D, worldserver1.getWorldBorder().e() - 16.0D);
                } else if (i == 0) {
                    d0 = MathHelper.a(d0 * 8.0D, worldserver1.getWorldBorder().b() + 16.0D, worldserver1.getWorldBorder().d() - 16.0D);
                    d1 = MathHelper.a(d1 * 8.0D, worldserver1.getWorldBorder().c() + 16.0D, worldserver1.getWorldBorder().e() - 16.0D);
                }

                d0 = (double) MathHelper.clamp((int) d0, -29999872, 29999872);
                d1 = (double) MathHelper.clamp((int) d1, -29999872, 29999872);
                float f = this.yaw;

                this.setPositionRotation(d0, this.locY, d1, 90.0F, 0.0F);
                PortalTravelAgent portaltravelagent = worldserver1.getTravelAgent();

                portaltravelagent.b(this, f);
                blockposition = new BlockPosition(this);
            }

            // CraftBukkit end */
            // CraftBukkit start - Ensure chunks are loaded in case TravelAgent is not used which would initially cause chunks to load during find/create
            // minecraftserver.getPlayerList().changeWorld(this, j, worldserver, worldserver1);
            worldserver1.getMinecraftServer().getPlayerList().repositionEntity(this, exit, portal);
            // worldserver.entityJoinedWorld(this, false); // Handled in repositionEntity
            // CraftBukkit end
            this.world.profiler.endStartSection("reloading");
            Entity entity = EntityList.newEntity(this.getClass(), (World) worldserver1);

            if (entity != null) {
                entity.copyDataFromOld(this);
                /* CraftBukkit start - We need to do this...
                if (j == 1 && i == 1) {
                    BlockPosition blockposition1 = worldserver1.q(worldserver1.getSpawn());

                    entity.setPositionRotation(blockposition1, entity.yaw, entity.pitch);
                } else {
                    entity.setPositionRotation(blockposition, entity.yaw, entity.pitch);
                }
                // CraftBukkit end */

                boolean flag = entity.forceSpawn;

                entity.forceSpawn = true;
                worldserver1.spawnEntity(entity);
                entity.forceSpawn = flag;
                worldserver1.updateEntityWithOptionalForce(entity, false);
                // CraftBukkit start - Forward the CraftEntity to the new entity
                this.getBukkitEntity().setHandle(entity);
                entity.bukkitEntity = this.getBukkitEntity();

                if (this instanceof EntityLiving) {
                    ((EntityLiving)this).clearLeashed(true, false); // Unleash to prevent duping of leads.
                }
                // CraftBukkit end
            }

            this.isDead = true;
            this.world.profiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            // this.world.methodProfiler.b(); // CraftBukkit: Moved up to keep balanced
            return entity;
        } else {
            return null;
        }
    }

    public boolean isNonBoss() {
        return true;
    }

    public float getExplosionResistance(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata) {
        return iblockdata.getBlock().getExplosionResistance(this);
    }

    public boolean canExplosionDestroyBlock(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata, float f) {
        return true;
    }

    public int getMaxFallHeight() {
        return 3;
    }

    public Vec3d getLastPortalVec() {
        return this.lastPortalVec;
    }

    public EnumFacing getTeleportDirection() {
        return this.teleportDirection;
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory crashreportsystemdetails) {
        crashreportsystemdetails.addDetail("Entity Type", new ICrashReportDetail() {
            public String a() throws Exception {
                return EntityList.getKey(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addCrashSection("Entity ID", (Object) Integer.valueOf(this.entityId));
        crashreportsystemdetails.addDetail("Entity Name", new ICrashReportDetail() {
            public String a() throws Exception {
                return Entity.this.getName();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addCrashSection("Entity\'s Exact location", (Object) String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ)}));
        crashreportsystemdetails.addCrashSection("Entity\'s Block location", (Object) CrashReportCategory.getCoordinateInfo(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)));
        crashreportsystemdetails.addCrashSection("Entity\'s Momentum", (Object) String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.motionX), Double.valueOf(this.motionY), Double.valueOf(this.motionZ)}));
        crashreportsystemdetails.addDetail("Entity\'s Passengers", new ICrashReportDetail() {
            public String a() throws Exception {
                return Entity.this.getPassengers().toString();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Entity\'s Vehicle", new ICrashReportDetail() {
            public String a() throws Exception {
                return Entity.this.getRidingEntity().toString();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
    }

    public void setUniqueId(UUID uuid) {
        this.entityUniqueID = uuid;
        this.cachedUniqueIdString = this.entityUniqueID.toString();
    }

    public UUID getUniqueID() {
        return this.entityUniqueID;
    }

    public String getCachedUniqueIdString() {
        return this.cachedUniqueIdString;
    }

    public boolean isPushedByWater() {
        return this.pushedByWater();
    }

    public boolean pushedByWater() {
        // Paper end
        return true;
    }

    public ITextComponent getDisplayName() {
        TextComponentString chatcomponenttext = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));

        chatcomponenttext.getStyle().setHoverEvent(this.getHoverEvent());
        chatcomponenttext.getStyle().setInsertion(this.getCachedUniqueIdString());
        return chatcomponenttext;
    }

    public void setCustomNameTag(String s) {
        // CraftBukkit start - Add a sane limit for name length
        if (s.length() > 256) {
            s = s.substring(0, 256);
        }
        // CraftBukkit end
        this.dataManager.set(Entity.CUSTOM_NAME, s);
    }

    public String getCustomNameTag() {
        return (String) this.dataManager.get(Entity.CUSTOM_NAME);
    }

    public boolean hasCustomName() {
        return !((String) this.dataManager.get(Entity.CUSTOM_NAME)).isEmpty();
    }

    public void setAlwaysRenderNameTag(boolean flag) {
        this.dataManager.set(Entity.CUSTOM_NAME_VISIBLE, Boolean.valueOf(flag));
    }

    public boolean getAlwaysRenderNameTag() {
        return ((Boolean) this.dataManager.get(Entity.CUSTOM_NAME_VISIBLE)).booleanValue();
    }

    public void setPositionAndUpdate(double d0, double d1, double d2) {
        this.isPositionDirty = true;
        this.setLocationAndAngles(d0, d1, d2, this.rotationYaw, this.rotationPitch);
        this.world.updateEntityWithOptionalForce(this, false);
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {}

    public EnumFacing getHorizontalFacing() {
        return EnumFacing.getHorizontal(MathHelper.floor((double) (this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
    }

    public EnumFacing getAdjustedHorizontalFacing() {
        return this.getHorizontalFacing();
    }

    protected HoverEvent getHoverEvent() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        ResourceLocation minecraftkey = EntityList.getKey(this);

        nbttagcompound.setString("id", this.getCachedUniqueIdString());
        if (minecraftkey != null) {
            nbttagcompound.setString("type", minecraftkey.toString());
        }

        nbttagcompound.setString("name", this.getName());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new TextComponentString(nbttagcompound.toString()));
    }

    public boolean isSpectatedByPlayer(EntityPlayerMP entityplayer) {
        return true;
    }

    public AxisAlignedBB getEntityBoundingBox() {
        return this.boundingBox;
    }

    public void setEntityBoundingBox(AxisAlignedBB axisalignedbb) {
        // CraftBukkit start - block invalid bounding boxes
        double a = axisalignedbb.minX,
                b = axisalignedbb.minY,
                c = axisalignedbb.minZ,
                d = axisalignedbb.maxX,
                e = axisalignedbb.maxY,
                f = axisalignedbb.maxZ;
        double len = axisalignedbb.maxX - axisalignedbb.minX;
        if (len < 0) d = a;
        if (len > 64) d = a + 64.0;

        len = axisalignedbb.maxY - axisalignedbb.minY;
        if (len < 0) e = b;
        if (len > 64) e = b + 64.0;

        len = axisalignedbb.maxZ - axisalignedbb.minZ;
        if (len < 0) f = c;
        if (len > 64) f = c + 64.0;
        this.boundingBox = new AxisAlignedBB(a, b, c, d, e, f);
        // CraftBukkit end
    }

    public float getEyeHeight() {
        return this.height * 0.85F;
    }

    public boolean isOutsideBorder() {
        return this.isOutsideBorder;
    }

    public void setOutsideBorder(boolean flag) {
        this.isOutsideBorder = flag;
    }

    public boolean replaceItemInInventory(int i, ItemStack itemstack) {
        return false;
    }

    public void sendMessage(ITextComponent ichatbasecomponent) {}

    public boolean canUseCommand(int i, String s) {
        return true;
    }

    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
    }

    public Vec3d getPositionVector() {
        return new Vec3d(this.posX, this.posY, this.posZ);
    }

    public World getEntityWorld() {
        return this.world;
    }

    public Entity getCommandSenderEntity() {
        return this;
    }

    public boolean sendCommandFeedback() {
        return false;
    }

    public void setCommandStat(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
        if (this.world != null && !this.world.isRemote) {
            this.cmdResultStats.setCommandStatForSender(this.world.getMinecraftServer(), this, commandobjectiveexecutor_enumcommandresult, i);
        }

    }

    @Nullable
    public MinecraftServer getServer() {
        return this.world.getMinecraftServer();
    }

    public CommandResultStats getCommandStats() {
        return this.cmdResultStats;
    }

    public void setCommandStats(Entity entity) {
        this.cmdResultStats.addAllStats(entity.getCommandStats());
    }

    public EnumActionResult applyPlayerInteraction(EntityPlayer entityhuman, Vec3d vec3d, EnumHand enumhand) {
        return EnumActionResult.PASS;
    }

    public boolean isImmuneToExplosions() {
        return false;
    }

    protected void applyEnchantments(EntityLivingBase entityliving, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entity, (Entity) entityliving);
        }

        EnchantmentHelper.applyArthropodEnchantments(entityliving, entity);
    }

    public void addTrackingPlayer(EntityPlayerMP entityplayer) {}

    public void removeTrackingPlayer(EntityPlayerMP entityplayer) {}

    public float getRotatedYaw(Rotation enumblockrotation) {
        float f = MathHelper.wrapDegrees(this.rotationYaw);

        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return f + 180.0F;

        case COUNTERCLOCKWISE_90:
            return f + 270.0F;

        case CLOCKWISE_90:
            return f + 90.0F;

        default:
            return f;
        }
    }

    public float getMirroredYaw(Mirror enumblockmirror) {
        float f = MathHelper.wrapDegrees(this.rotationYaw);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return -f;

        case FRONT_BACK:
            return 180.0F - f;

        default:
            return f;
        }
    }

    public boolean ignoreItemEntityData() {
        return false;
    }

    public boolean setPositionNonDirty() {
        boolean flag = this.isPositionDirty;

        this.isPositionDirty = false;
        return flag;
    }

    @Nullable
    public Entity getControllingPassenger() {
        return null;
    }

    public List<Entity> getPassengers() {
        return (List) (this.riddenByEntities.isEmpty() ? Collections.emptyList() : Lists.newArrayList(this.riddenByEntities));
    }

    public boolean isPassenger(Entity entity) {
        Iterator iterator = this.getPassengers().iterator();

        Entity entity1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            entity1 = (Entity) iterator.next();
        } while (!entity1.equals(entity));

        return true;
    }

    public Collection<Entity> getRecursivePassengers() {
        HashSet hashset = Sets.newHashSet();

        this.getRecursivePassengersByType(Entity.class, (Set) hashset);
        return hashset;
    }

    public <T extends Entity> Collection<T> getRecursivePassengersByType(Class<T> oclass) {
        HashSet hashset = Sets.newHashSet();

        this.getRecursivePassengersByType(oclass, (Set) hashset);
        return hashset;
    }

    private <T extends Entity> void getRecursivePassengersByType(Class<T> oclass, Set<T> set) {
        Entity entity;

        for (Iterator iterator = this.getPassengers().iterator(); iterator.hasNext(); entity.getRecursivePassengersByType(oclass, set)) {
            entity = (Entity) iterator.next();
            if (oclass.isAssignableFrom(entity.getClass())) {
                set.add((T) entity); // CraftBukkit - decompile error
            }
        }

    }

    public Entity getLowestRidingEntity() {
        Entity entity;

        for (entity = this; entity.isRiding(); entity = entity.getRidingEntity()) {
            ;
        }

        return entity;
    }

    public boolean isRidingSameEntity(Entity entity) {
        return this.getLowestRidingEntity() == entity.getLowestRidingEntity();
    }

    public boolean isRidingOrBeingRiddenBy(Entity entity) {
        Iterator iterator = this.getPassengers().iterator();

        Entity entity1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            entity1 = (Entity) iterator.next();
            if (entity1.equals(entity)) {
                return true;
            }
        } while (!entity1.isRidingOrBeingRiddenBy(entity));

        return true;
    }

    public boolean canPassengerSteer() {
        Entity entity = this.getControllingPassenger();

        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).isUser() : !this.world.isRemote;
    }

    @Nullable
    public Entity getRidingEntity() {
        return this.ridingEntity;
    }

    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.NORMAL;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    public int getFireImmuneTicks() {
        return 1;
    }
}
