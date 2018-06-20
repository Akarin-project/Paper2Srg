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
        return tag.func_74764_b("Bukkit.updateLevel") && tag.func_74762_e("Bukkit.updateLevel") >= level;
    }

    protected CraftEntity bukkitEntity;

    EntityTrackerEntry tracker; // Paper
    public CraftEntity getBukkitEntity() {
        if (bukkitEntity == null) {
            bukkitEntity = CraftEntity.getEntity(field_70170_p.getServer(), this);
        }
        return bukkitEntity;
    }
    // CraftBukikt end

    private static final Logger field_184243_a = LogManager.getLogger();
    private static final List<ItemStack> field_190535_b = Collections.emptyList();
    private static final AxisAlignedBB field_174836_a = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static double field_70155_l = 1.0D;
    private static int field_70152_a;
    private int field_145783_c;
    public boolean field_70156_m; public boolean blocksEntitySpawning() { return field_70156_m; } // Paper - OBFHELPER
    public final List<Entity> field_184244_h;
    protected int field_184245_j;
    private Entity field_184239_as;public void setVehicle(Entity entity) { this.field_184239_as = entity; } // Paper // OBFHELPER
    public boolean field_98038_p;
    public World field_70170_p;
    public double field_70169_q;
    public double field_70167_r;
    public double field_70166_s;
    public double field_70165_t;
    public double field_70163_u;
    public double field_70161_v;
    // Paper start - getters to implement HopperPusher
    public double getX() {
        return field_70165_t;
    }

    public double getY() {
        return field_70163_u;
    }

    public double getZ() {
        return field_70161_v;
    }
    // Paper end
    public double field_70159_w;
    public double field_70181_x;
    public double field_70179_y;
    public float field_70177_z;
    public float field_70125_A;
    public float field_70126_B;
    public float field_70127_C;
    private AxisAlignedBB field_70121_D;
    public boolean field_70122_E;
    public boolean field_70123_F;
    public boolean field_70124_G;
    public boolean field_70132_H;
    public boolean field_70133_I;
    protected boolean field_70134_J;
    private boolean field_174835_g;
    public boolean field_70128_L;
    public float field_70130_N;
    public float field_70131_O;
    public float field_70141_P;
    public float field_70140_Q;
    public float field_82151_R;
    public float field_70143_R;
    private int field_70150_b;
    private float field_191959_ay;
    public double field_70142_S;
    public double field_70137_T;
    public double field_70136_U;
    public float field_70138_W;
    public boolean field_70145_X;
    public float field_70144_Y;
    protected Random field_70146_Z;
    public int field_70173_aa;
    public int field_190534_ay;
    public boolean field_70171_ac; // Spigot - protected -> public // PAIL
    public int field_70172_ad;
    protected boolean field_70148_d;
    protected boolean field_70178_ae;
    public EntityDataManager field_70180_af;
    protected static final DataParameter<Byte> field_184240_ax = EntityDataManager.func_187226_a(Entity.class, DataSerializers.field_187191_a);
    private static final DataParameter<Integer> field_184241_ay = EntityDataManager.func_187226_a(Entity.class, DataSerializers.field_187192_b);
    private static final DataParameter<String> field_184242_az = EntityDataManager.func_187226_a(Entity.class, DataSerializers.field_187194_d);
    private static final DataParameter<Boolean> field_184233_aA = EntityDataManager.func_187226_a(Entity.class, DataSerializers.field_187198_h);
    private static final DataParameter<Boolean> field_184234_aB = EntityDataManager.func_187226_a(Entity.class, DataSerializers.field_187198_h);
    private static final DataParameter<Boolean> field_189655_aD = EntityDataManager.func_187226_a(Entity.class, DataSerializers.field_187198_h);
    public boolean field_70175_ag; public boolean isAddedToChunk() { return field_70175_ag; } // Paper - OBFHELPER
    public int field_70176_ah; public int getChunkX() { return field_70176_ah; } // Paper - OBFHELPER
    public int field_70162_ai; public int getChunkY() { return field_70162_ai; } // Paper - OBFHELPER
    public int field_70164_aj; public int getChunkZ() { return field_70164_aj; } // Paper - OBFHELPER
    public boolean field_70158_ak;
    public boolean field_70160_al;
    public int field_71088_bW;
    protected boolean field_71087_bX; public boolean inPortal() { return field_71087_bX; } // Paper - OBFHELPER
    protected int field_82153_h;
    public int field_71093_bK;
    protected BlockPos field_181016_an;
    protected Vec3d field_181017_ao;
    protected EnumFacing field_181018_ap;
    private boolean field_83001_bt;
    protected UUID field_96093_i;
    protected String field_189513_ar;
    private final CommandResultStats field_174837_as;
    public boolean field_184238_ar;
    private final Set<String> field_184236_aF;
    private boolean field_184237_aG;
    private final double[] field_191505_aI;
    private long field_191506_aJ;
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
        return this.field_70177_z;
    }
    // CraftBukkit end

    public Entity(World world) {
        this.field_145783_c = Entity.field_70152_a++;
        this.field_184244_h = Lists.newArrayList();
        this.field_70121_D = Entity.field_174836_a;
        this.field_70130_N = 0.6F;
        this.field_70131_O = 1.8F;
        this.field_70150_b = 1;
        this.field_191959_ay = 1.0F;
        this.field_70146_Z = SHARED_RANDOM; // Paper
        this.field_190534_ay = -this.func_190531_bD();
        this.field_70148_d = true;
        this.field_96093_i = MathHelper.func_180182_a(this.field_70146_Z);
        this.field_189513_ar = this.field_96093_i.toString();
        this.field_174837_as = new CommandResultStats();
        this.field_184236_aF = Sets.newHashSet();
        this.field_191505_aI = new double[] { 0.0D, 0.0D, 0.0D};
        this.field_70170_p = world;
        this.func_70107_b(0.0D, 0.0D, 0.0D);
        if (world != null) {
            this.field_71093_bK = world.field_73011_w.func_186058_p().func_186068_a();
            // Spigot start
            this.defaultActivationState = org.spigotmc.ActivationRange.initializeEntityActivationState(this, world.spigotConfig);
        } else {
            this.defaultActivationState = false;
        }
        // Spigot end

        this.field_70180_af = new EntityDataManager(this);
        this.field_70180_af.func_187214_a(Entity.field_184240_ax, Byte.valueOf((byte) 0));
        this.field_70180_af.func_187214_a(Entity.field_184241_ay, Integer.valueOf(300));
        this.field_70180_af.func_187214_a(Entity.field_184233_aA, Boolean.valueOf(false));
        this.field_70180_af.func_187214_a(Entity.field_184242_az, "");
        this.field_70180_af.func_187214_a(Entity.field_184234_aB, Boolean.valueOf(false));
        this.field_70180_af.func_187214_a(Entity.field_189655_aD, Boolean.valueOf(false));
        this.func_70088_a();
    }

    public int func_145782_y() {
        return this.field_145783_c;
    }

    public void func_145769_d(int i) {
        this.field_145783_c = i;
    }

    public Set<String> func_184216_O() {
        return this.field_184236_aF;
    }

    public boolean func_184211_a(String s) {
        if (this.field_184236_aF.size() >= 1024) {
            return false;
        } else {
            this.field_184236_aF.add(s);
            return true;
        }
    }

    public boolean func_184197_b(String s) {
        return this.field_184236_aF.remove(s);
    }

    public void func_174812_G() {
        this.func_70106_y();
    }

    protected abstract void func_70088_a();

    public EntityDataManager func_184212_Q() {
        return this.field_70180_af;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Entity ? ((Entity) object).field_145783_c == this.field_145783_c : false;
    }

    @Override
    public int hashCode() {
        return this.field_145783_c;
    }

    public void func_70106_y() {
        this.field_70128_L = true;
    }

    public void func_184174_b(boolean flag) {}

    public void func_70105_a(float f, float f1) {
        if (f != this.field_70130_N || f1 != this.field_70131_O) {
            float f2 = this.field_70130_N;

            this.field_70130_N = f;
            this.field_70131_O = f1;
            if (this.field_70130_N < f2) {
                double d0 = f / 2.0D;

                this.func_174826_a(new AxisAlignedBB(this.field_70165_t - d0, this.field_70163_u, this.field_70161_v - d0, this.field_70165_t + d0, this.field_70163_u + this.field_70131_O, this.field_70161_v + d0));
                return;
            }

            AxisAlignedBB axisalignedbb = this.func_174813_aQ();

            this.func_174826_a(new AxisAlignedBB(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c, axisalignedbb.field_72340_a + this.field_70130_N, axisalignedbb.field_72338_b + this.field_70131_O, axisalignedbb.field_72339_c + this.field_70130_N));
            if (this.field_70130_N > f2 && !this.field_70148_d && !this.field_70170_p.field_72995_K) {
                this.func_70091_d(MoverType.SELF, f2 - this.field_70130_N, 0.0D, f2 - this.field_70130_N);
            }
        }

    }

    public void func_70101_b(float f, float f1) {
        // CraftBukkit start - yaw was sometimes set to NaN, so we need to set it back to 0
        if (Float.isNaN(f)) {
            f = 0;
        }

        if (f == Float.POSITIVE_INFINITY || f == Float.NEGATIVE_INFINITY) {
            if (this instanceof EntityPlayerMP) {
                this.field_70170_p.getServer().getLogger().warning(this.func_70005_c_() + " was caught trying to crash the server with an invalid yaw");
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
                this.field_70170_p.getServer().getLogger().warning(this.func_70005_c_() + " was caught trying to crash the server with an invalid pitch");
                ((CraftPlayer) this.getBukkitEntity()).kickPlayer("Infinite pitch (Hacking?)"); //Spigot "Nope" -> Descriptive reason
            }
            f1 = 0;
        }
        // CraftBukkit end

        this.field_70177_z = f % 360.0F;
        this.field_70125_A = f1 % 360.0F;
    }

    public void func_70107_b(double d0, double d1, double d2) {
        this.field_70165_t = d0;
        this.field_70163_u = d1;
        this.field_70161_v = d2;
        float f = this.field_70130_N / 2.0F;
        float f1 = this.field_70131_O;

        this.func_174826_a(new AxisAlignedBB(d0 - f, d1, d2 - f, d0 + f, d1 + f1, d2 + f));
    }

    public void func_70071_h_() {
        if (!this.field_70170_p.field_72995_K) {
            this.func_70052_a(6, this.func_184202_aL());
        }

        this.func_70030_z();
    }

    // CraftBukkit start
    public void postTick() {
        // No clean way to break out of ticking once the entity has been copied to a new world, so instead we move the portalling later in the tick cycle
        if (!this.field_70170_p.field_72995_K && this.field_70170_p instanceof WorldServer) {
            this.field_70170_p.field_72984_F.func_76320_a("portal");
            if (this.field_71087_bX) {
                MinecraftServer minecraftserver = this.field_70170_p.func_73046_m();

                if (true || minecraftserver.func_71255_r()) { // CraftBukkit
                    if (!this.func_184218_aH()) {
                        int i = this.func_82145_z();

                        if (this.field_82153_h++ >= i) {
                            this.field_82153_h = i;
                            this.field_71088_bW = this.func_82147_ab();
                            byte b0;

                            if (this.field_70170_p.field_73011_w.func_186058_p().func_186068_a() == -1) {
                                b0 = 0;
                            } else {
                                b0 = -1;
                            }

                            this.func_184204_a(b0);
                        }
                    }

                    this.field_71087_bX = false;
                }
            } else {
                if (this.field_82153_h > 0) {
                    this.field_82153_h -= 4;
                }

                if (this.field_82153_h < 0) {
                    this.field_82153_h = 0;
                }
            }

            this.func_184173_H();
            this.field_70170_p.field_72984_F.func_76319_b();
        }
    }
    // CraftBukkit end

    public void func_70030_z() {
        this.field_70170_p.field_72984_F.func_76320_a("entityBaseTick");
        if (this.func_184218_aH() && this.func_184187_bx().field_70128_L) {
            this.func_184210_p();
        }

        if (this.field_184245_j > 0) {
            --this.field_184245_j;
        }

        this.field_70141_P = this.field_70140_Q;
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        this.field_70127_C = this.field_70125_A;
        this.field_70126_B = this.field_70177_z;
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

        this.func_174830_Y();
        this.func_70072_I();
        if (this.field_70170_p.field_72995_K) {
            this.func_70066_B();
        } else if (this.field_190534_ay > 0) {
            if (this.field_70178_ae) {
                this.field_190534_ay -= 4;
                if (this.field_190534_ay < 0) {
                    this.func_70066_B();
                }
            } else {
                if (this.field_190534_ay % 20 == 0) {
                    this.func_70097_a(DamageSource.field_76370_b, 1.0F);
                }

                --this.field_190534_ay;
            }
        }

        if (this.func_180799_ab()) {
            this.func_70044_A();
            this.field_70143_R *= 0.5F;
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

        if (!this.field_70170_p.field_72995_K) {
            this.func_70052_a(0, this.field_190534_ay > 0);
        }

        this.field_70148_d = false;
        this.field_70170_p.field_72984_F.func_76319_b();
    }

    // Paper start - Configurable top of nether void damage
    private boolean paperNetherCheck() {
        return this.field_70170_p.paperConfig.netherVoidTopDamage && this.field_70170_p.getWorld().getEnvironment() == org.bukkit.World.Environment.NETHER && this.field_70163_u >= 128.0D;
    }

    protected void checkAndDoHeightDamage() {
        if (this.field_70163_u < -64.0D || paperNetherCheck()) {
            this.kill();
        }
    }
    // Paper end

    protected void func_184173_H() {
        if (this.field_71088_bW > 0) {
            --this.field_71088_bW;
        }

    }

    public int func_82145_z() {
        return 1;
    }

    protected void func_70044_A() {
        if (!this.field_70178_ae) {
            this.func_70097_a(DamageSource.field_76371_c, 4.0F);

            // CraftBukkit start - Fallen in lava TODO: this event spams!
            if (this instanceof EntityLivingBase) {
                if (field_190534_ay <= 0) {
                    // not on fire yet
                    // TODO: shouldn't be sending null for the block
                    org.bukkit.block.Block damager = null; // ((WorldServer) this.l).getWorld().getBlockAt(i, j, k);
                    org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                    EntityCombustEvent combustEvent = new org.bukkit.event.entity.EntityCombustByBlockEvent(damager, damagee, 15);
                    this.field_70170_p.getServer().getPluginManager().callEvent(combustEvent);

                    if (!combustEvent.isCancelled()) {
                        this.func_70015_d(combustEvent.getDuration());
                    }
                } else {
                    // This will be called every single tick the entity is in lava, so don't throw an event
                    this.func_70015_d(15);
                }
                return;
            }
            // CraftBukkit end - we also don't throw an event unless the object in lava is living, to save on some event calls
            this.func_70015_d(15);
        }
    }

    public void func_70015_d(int i) {
        int j = i * 20;

        if (this instanceof EntityLivingBase) {
            j = EnchantmentProtection.func_92093_a((EntityLivingBase) this, j);
        }

        if (this.field_190534_ay < j) {
            this.field_190534_ay = j;
        }

    }

    public void func_70066_B() {
        this.field_190534_ay = 0;
    }

    protected final void kill() { this.func_70076_C(); } // Paper - OBFHELPER
    protected void func_70076_C() {
        this.func_70106_y();
    }

    public boolean func_70038_c(double d0, double d1, double d2) {
        AxisAlignedBB axisalignedbb = this.func_174813_aQ().func_72317_d(d0, d1, d2);

        return this.func_174809_b(axisalignedbb);
    }

    private boolean func_174809_b(AxisAlignedBB axisalignedbb) {
        return this.field_70170_p.func_184144_a(this, axisalignedbb).isEmpty() && !this.field_70170_p.func_72953_d(axisalignedbb);
    }

    public void func_70091_d(MoverType enummovetype, double d0, double d1, double d2) {
        if (this.field_70145_X) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(d0, d1, d2));
            this.func_174829_m();
        } else {
            // CraftBukkit start - Don't do anything if we aren't moving
            // We need to do this regardless of whether or not we are moving thanks to portals
            try {
                this.func_145775_I();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a(throwable, "Checking entity block collision");
                CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Entity being checked for collision");

                this.func_85029_a(crashreportsystemdetails);
                throw new ReportedException(crashreport);
            }
            // Check if we're moving
            if (d0 == 0 && d1 == 0 && d2 == 0 && this.func_184207_aI() && this.func_184218_aH()) {
                return;
            }
            // CraftBukkit end
            if (enummovetype == MoverType.PISTON) {
                long i = this.field_70170_p.func_82737_E();

                if (i != this.field_191506_aJ) {
                    Arrays.fill(this.field_191505_aI, 0.0D);
                    this.field_191506_aJ = i;
                }

                int j;
                double d3;

                if (d0 != 0.0D) {
                    j = EnumFacing.Axis.X.ordinal();
                    d3 = MathHelper.func_151237_a(d0 + this.field_191505_aI[j], -0.51D, 0.51D);
                    d0 = d3 - this.field_191505_aI[j];
                    this.field_191505_aI[j] = d3;
                    if (Math.abs(d0) <= 9.999999747378752E-6D) {
                        return;
                    }
                } else if (d1 != 0.0D) {
                    j = EnumFacing.Axis.Y.ordinal();
                    d3 = MathHelper.func_151237_a(d1 + this.field_191505_aI[j], -0.51D, 0.51D);
                    d1 = d3 - this.field_191505_aI[j];
                    this.field_191505_aI[j] = d3;
                    if (Math.abs(d1) <= 9.999999747378752E-6D) {
                        return;
                    }
                } else {
                    if (d2 == 0.0D) {
                        return;
                    }

                    j = EnumFacing.Axis.Z.ordinal();
                    d3 = MathHelper.func_151237_a(d2 + this.field_191505_aI[j], -0.51D, 0.51D);
                    d2 = d3 - this.field_191505_aI[j];
                    this.field_191505_aI[j] = d3;
                    if (Math.abs(d2) <= 9.999999747378752E-6D) {
                        return;
                    }
                }
            }

            this.field_70170_p.field_72984_F.func_76320_a("move");
            double d4 = this.field_70165_t;
            double d5 = this.field_70163_u;
            double d6 = this.field_70161_v;

            if (this.field_70134_J) {
                this.field_70134_J = false;
                d0 *= 0.25D;
                d1 *= 0.05000000074505806D;
                d2 *= 0.25D;
                this.field_70159_w = 0.0D;
                this.field_70181_x = 0.0D;
                this.field_70179_y = 0.0D;
            }

            double d7 = d0;
            double d8 = d1;
            double d9 = d2;

            if ((enummovetype == MoverType.SELF || enummovetype == MoverType.PLAYER) && this.field_70122_E && this.func_70093_af() && this instanceof EntityPlayer) {
                for (double d10 = 0.05D; d0 != 0.0D && this.field_70170_p.func_184144_a(this, this.func_174813_aQ().func_72317_d(d0, (-this.field_70138_W), 0.0D)).isEmpty(); d7 = d0) {
                    if (d0 < 0.05D && d0 >= -0.05D) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= 0.05D;
                    } else {
                        d0 += 0.05D;
                    }
                }

                for (; d2 != 0.0D && this.field_70170_p.func_184144_a(this, this.func_174813_aQ().func_72317_d(0.0D, (-this.field_70138_W), d2)).isEmpty(); d9 = d2) {
                    if (d2 < 0.05D && d2 >= -0.05D) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= 0.05D;
                    } else {
                        d2 += 0.05D;
                    }
                }

                for (; d0 != 0.0D && d2 != 0.0D && this.field_70170_p.func_184144_a(this, this.func_174813_aQ().func_72317_d(d0, (-this.field_70138_W), d2)).isEmpty(); d9 = d2) {
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

            List list = this.field_70170_p.func_184144_a(this, this.func_174813_aQ().func_72321_a(d0, d1, d2));
            AxisAlignedBB axisalignedbb = this.func_174813_aQ();
            int k;
            int l;

            if (d1 != 0.0D) {
                k = 0;

                for (l = list.size(); k < l; ++k) {
                    d1 = ((AxisAlignedBB) list.get(k)).func_72323_b(this.func_174813_aQ(), d1);
                }

                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0D, d1, 0.0D));
            }

            if (d0 != 0.0D) {
                k = 0;

                for (l = list.size(); k < l; ++k) {
                    d0 = ((AxisAlignedBB) list.get(k)).func_72316_a(this.func_174813_aQ(), d0);
                }

                if (d0 != 0.0D) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(d0, 0.0D, 0.0D));
                }
            }

            if (d2 != 0.0D) {
                k = 0;

                for (l = list.size(); k < l; ++k) {
                    d2 = ((AxisAlignedBB) list.get(k)).func_72322_c(this.func_174813_aQ(), d2);
                }

                if (d2 != 0.0D) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0D, 0.0D, d2));
                }
            }

            boolean flag = this.field_70122_E || d1 != d8 && d1 < 0.0D; // CraftBukkit - decompile error
            double d11;

            if (this.field_70138_W > 0.0F && flag && (d7 != d0 || d9 != d2)) {
                double d12 = d0;
                double d13 = d1;
                double d14 = d2;
                AxisAlignedBB axisalignedbb1 = this.func_174813_aQ();

                this.func_174826_a(axisalignedbb);
                d1 = this.field_70138_W;
                List list1 = this.field_70170_p.func_184144_a(this, this.func_174813_aQ().func_72321_a(d7, d1, d9));
                AxisAlignedBB axisalignedbb2 = this.func_174813_aQ();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.func_72321_a(d7, 0.0D, d9);

                d11 = d1;
                int i1 = 0;

                for (int j1 = list1.size(); i1 < j1; ++i1) {
                    d11 = ((AxisAlignedBB) list1.get(i1)).func_72323_b(axisalignedbb3, d11);
                }

                axisalignedbb2 = axisalignedbb2.func_72317_d(0.0D, d11, 0.0D);
                double d15 = d7;
                int k1 = 0;

                for (int l1 = list1.size(); k1 < l1; ++k1) {
                    d15 = ((AxisAlignedBB) list1.get(k1)).func_72316_a(axisalignedbb2, d15);
                }

                axisalignedbb2 = axisalignedbb2.func_72317_d(d15, 0.0D, 0.0D);
                double d16 = d9;
                int i2 = 0;

                for (int j2 = list1.size(); i2 < j2; ++i2) {
                    d16 = ((AxisAlignedBB) list1.get(i2)).func_72322_c(axisalignedbb2, d16);
                }

                axisalignedbb2 = axisalignedbb2.func_72317_d(0.0D, 0.0D, d16);
                AxisAlignedBB axisalignedbb4 = this.func_174813_aQ();
                double d17 = d1;
                int k2 = 0;

                for (int l2 = list1.size(); k2 < l2; ++k2) {
                    d17 = ((AxisAlignedBB) list1.get(k2)).func_72323_b(axisalignedbb4, d17);
                }

                axisalignedbb4 = axisalignedbb4.func_72317_d(0.0D, d17, 0.0D);
                double d18 = d7;
                int i3 = 0;

                for (int j3 = list1.size(); i3 < j3; ++i3) {
                    d18 = ((AxisAlignedBB) list1.get(i3)).func_72316_a(axisalignedbb4, d18);
                }

                axisalignedbb4 = axisalignedbb4.func_72317_d(d18, 0.0D, 0.0D);
                double d19 = d9;
                int k3 = 0;

                for (int l3 = list1.size(); k3 < l3; ++k3) {
                    d19 = ((AxisAlignedBB) list1.get(k3)).func_72322_c(axisalignedbb4, d19);
                }

                axisalignedbb4 = axisalignedbb4.func_72317_d(0.0D, 0.0D, d19);
                double d20 = d15 * d15 + d16 * d16;
                double d21 = d18 * d18 + d19 * d19;

                if (d20 > d21) {
                    d0 = d15;
                    d2 = d16;
                    d1 = -d11;
                    this.func_174826_a(axisalignedbb2);
                } else {
                    d0 = d18;
                    d2 = d19;
                    d1 = -d17;
                    this.func_174826_a(axisalignedbb4);
                }

                int i4 = 0;

                for (int j4 = list1.size(); i4 < j4; ++i4) {
                    d1 = ((AxisAlignedBB) list1.get(i4)).func_72323_b(this.func_174813_aQ(), d1);
                }

                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0D, d1, 0.0D));
                if (d12 * d12 + d14 * d14 >= d0 * d0 + d2 * d2) {
                    d0 = d12;
                    d1 = d13;
                    d2 = d14;
                    this.func_174826_a(axisalignedbb1);
                }
            }

            this.field_70170_p.field_72984_F.func_76319_b();
            this.field_70170_p.field_72984_F.func_76320_a("rest");
            this.func_174829_m();
            this.field_70123_F = d7 != d0 || d9 != d2;
            this.field_70124_G = d1 != d8; // CraftBukkit - decompile error
            this.field_70122_E = this.field_70124_G && d8 < 0.0D;
            this.field_70132_H = this.field_70123_F || this.field_70124_G;
            l = MathHelper.func_76128_c(this.field_70165_t);
            int k4 = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224D);
            int l4 = MathHelper.func_76128_c(this.field_70161_v);
            BlockPos blockposition = new BlockPos(l, k4, l4);
            IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);

            if (iblockdata.func_185904_a() == Material.field_151579_a) {
                BlockPos blockposition1 = blockposition.func_177977_b();
                IBlockState iblockdata1 = this.field_70170_p.func_180495_p(blockposition1);
                Block block = iblockdata1.func_177230_c();

                if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                    iblockdata = iblockdata1;
                    blockposition = blockposition1;
                }
            }

            this.func_184231_a(d1, this.field_70122_E, iblockdata, blockposition);
            if (d7 != d0) {
                this.field_70159_w = 0.0D;
            }

            if (d9 != d2) {
                this.field_70179_y = 0.0D;
            }

            Block block1 = iblockdata.func_177230_c();

            if (d8 != d1) {
                block1.func_176216_a(this.field_70170_p, this);
            }

            // CraftBukkit start
            if (field_70123_F && getBukkitEntity() instanceof Vehicle) {
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.block.Block bl = this.field_70170_p.getWorld().getBlockAt(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.field_70163_u), MathHelper.func_76128_c(this.field_70161_v));

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
                    field_70170_p.getServer().getPluginManager().callEvent(event);
                }
            }
            // CraftBukkit end

            if (this.func_70041_e_() && (!this.field_70122_E || !this.func_70093_af() || !(this instanceof EntityPlayer)) && !this.func_184218_aH()) {
                double d22 = this.field_70165_t - d4;
                double d23 = this.field_70163_u - d5;

                d11 = this.field_70161_v - d6;
                if (block1 != Blocks.field_150468_ap) {
                    d23 = 0.0D;
                }

                if (block1 != null && this.field_70122_E) {
                    block1.func_176199_a(this.field_70170_p, blockposition, this);
                }

                this.field_70140_Q = (float) (this.field_70140_Q + MathHelper.func_76133_a(d22 * d22 + d11 * d11) * 0.6D);
                this.field_82151_R = (float) (this.field_82151_R + MathHelper.func_76133_a(d22 * d22 + d23 * d23 + d11 * d11) * 0.6D);
                if (this.field_82151_R > this.field_70150_b && iblockdata.func_185904_a() != Material.field_151579_a) {
                    this.field_70150_b = (int) this.field_82151_R + 1;
                    if (this.func_70090_H()) {
                        Entity entity = this.func_184207_aI() && this.func_184179_bs() != null ? this.func_184179_bs() : this;
                        float f = entity == this ? 0.35F : 0.4F;
                        float f1 = MathHelper.func_76133_a(entity.field_70159_w * entity.field_70159_w * 0.20000000298023224D + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y * 0.20000000298023224D) * f;

                        if (f1 > 1.0F) {
                            f1 = 1.0F;
                        }

                        this.func_184185_a(this.func_184184_Z(), f1, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4F);
                    } else {
                        this.func_180429_a(blockposition, block1);
                    }
                } else if (this.field_82151_R > this.field_191959_ay && this.func_191957_ae() && iblockdata.func_185904_a() == Material.field_151579_a) {
                    this.field_191959_ay = this.func_191954_d(this.field_82151_R);
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

            boolean flag1 = this.func_70026_G();

            if (this.field_70170_p.func_147470_e(this.func_174813_aQ().func_186664_h(0.001D))) {
                this.burn(1);
                if (!flag1) {
                    ++this.field_190534_ay;
                    if (this.field_190534_ay == 0) {
                        // CraftBukkit start
                        EntityCombustEvent event = new org.bukkit.event.entity.EntityCombustByBlockEvent(null, getBukkitEntity(), 8);
                        field_70170_p.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            this.func_70015_d(event.getDuration());
                        }
                        // CraftBukkit end
                    }
                }
            } else if (this.field_190534_ay <= 0) {
                this.field_190534_ay = -this.func_190531_bD();
            }

            if (flag1 && this.func_70027_ad()) {
                this.func_184185_a(SoundEvents.field_187541_bC, 0.7F, 1.6F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4F);
                this.field_190534_ay = -this.func_190531_bD();
            }

            this.field_70170_p.field_72984_F.func_76319_b();
        }
    }

    public void func_174829_m() {
        AxisAlignedBB axisalignedbb = this.func_174813_aQ();

        this.field_70165_t = (axisalignedbb.field_72340_a + axisalignedbb.field_72336_d) / 2.0D;
        this.field_70163_u = axisalignedbb.field_72338_b;
        this.field_70161_v = (axisalignedbb.field_72339_c + axisalignedbb.field_72334_f) / 2.0D;
    }

    protected SoundEvent func_184184_Z() {
        return SoundEvents.field_187549_bG;
    }

    protected SoundEvent func_184181_aa() {
        return SoundEvents.field_187547_bF;
    }

    protected void func_145775_I() {
        AxisAlignedBB axisalignedbb = this.func_174813_aQ();
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185345_c(axisalignedbb.field_72340_a + 0.001D, axisalignedbb.field_72338_b + 0.001D, axisalignedbb.field_72339_c + 0.001D);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition1 = BlockPos.PooledMutableBlockPos.func_185345_c(axisalignedbb.field_72336_d - 0.001D, axisalignedbb.field_72337_e - 0.001D, axisalignedbb.field_72334_f - 0.001D);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition2 = BlockPos.PooledMutableBlockPos.func_185346_s();

        if (this.field_70170_p.func_175707_a(blockposition_pooledblockposition, blockposition_pooledblockposition1)) {
            for (int i = blockposition_pooledblockposition.func_177958_n(); i <= blockposition_pooledblockposition1.func_177958_n(); ++i) {
                for (int j = blockposition_pooledblockposition.func_177956_o(); j <= blockposition_pooledblockposition1.func_177956_o(); ++j) {
                    for (int k = blockposition_pooledblockposition.func_177952_p(); k <= blockposition_pooledblockposition1.func_177952_p(); ++k) {
                        blockposition_pooledblockposition2.func_181079_c(i, j, k);
                        IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition_pooledblockposition2);

                        try {
                            iblockdata.func_177230_c().func_180634_a(this.field_70170_p, blockposition_pooledblockposition2, iblockdata, this);
                            this.func_191955_a(iblockdata);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Block being collided with");

                            CrashReportCategory.func_175750_a(crashreportsystemdetails, blockposition_pooledblockposition2, iblockdata);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }

        blockposition_pooledblockposition.func_185344_t();
        blockposition_pooledblockposition1.func_185344_t();
        blockposition_pooledblockposition2.func_185344_t();
    }

    protected void func_191955_a(IBlockState iblockdata) {}

    protected void func_180429_a(BlockPos blockposition, Block block) {
        SoundType soundeffecttype = block.func_185467_w();

        if (this.field_70170_p.func_180495_p(blockposition.func_177984_a()).func_177230_c() == Blocks.field_150431_aC) {
            soundeffecttype = Blocks.field_150431_aC.func_185467_w();
            this.func_184185_a(soundeffecttype.func_185844_d(), soundeffecttype.func_185843_a() * 0.15F, soundeffecttype.func_185847_b());
        } else if (!block.func_176223_P().func_185904_a().func_76224_d()) {
            this.func_184185_a(soundeffecttype.func_185844_d(), soundeffecttype.func_185843_a() * 0.15F, soundeffecttype.func_185847_b());
        }

    }

    protected float func_191954_d(float f) {
        return 0.0F;
    }

    protected boolean func_191957_ae() {
        return false;
    }

    public void func_184185_a(SoundEvent soundeffect, float f, float f1) {
        if (!this.func_174814_R()) {
            this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, soundeffect, this.func_184176_by(), f, f1);
        }

    }

    public boolean func_174814_R() {
        return this.field_70180_af.func_187225_a(Entity.field_184234_aB).booleanValue();
    }

    public void func_174810_b(boolean flag) {
        this.field_70180_af.func_187227_b(Entity.field_184234_aB, Boolean.valueOf(flag));
    }

    public boolean func_189652_ae() {
        return this.field_70180_af.func_187225_a(Entity.field_189655_aD).booleanValue();
    }

    public void func_189654_d(boolean flag) {
        this.field_70180_af.func_187227_b(Entity.field_189655_aD, Boolean.valueOf(flag));
    }

    protected boolean func_70041_e_() {
        return true;
    }

    protected void func_184231_a(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {
        if (flag) {
            if (this.field_70143_R > 0.0F) {
                iblockdata.func_177230_c().func_180658_a(this.field_70170_p, blockposition, this, this.field_70143_R);
            }

            this.field_70143_R = 0.0F;
        } else if (d0 < 0.0D) {
            this.field_70143_R = (float) (this.field_70143_R - d0);
        }

    }

    @Nullable
    public AxisAlignedBB func_70046_E() {
        return null;
    }

    protected void burn(float i) { // CraftBukkit - int -> float
        if (!this.field_70178_ae) {
            this.func_70097_a(DamageSource.field_76372_a, i);
        }

    }

    public final boolean func_70045_F() {
        return this.field_70178_ae;
    }

    public void func_180430_e(float f, float f1) {
        if (this.func_184207_aI()) {
            Iterator iterator = this.func_184188_bt().iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                entity.func_180430_e(f, f1);
            }
        }

    }

    public boolean func_70026_G() {
        if (this.field_70171_ac) {
            return true;
        } else {
            BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185345_c(this.field_70165_t, this.field_70163_u, this.field_70161_v);

            if (!this.field_70170_p.func_175727_C(blockposition_pooledblockposition) && !this.field_70170_p.func_175727_C(blockposition_pooledblockposition.func_189532_c(this.field_70165_t, this.field_70163_u + this.field_70131_O, this.field_70161_v))) {
                blockposition_pooledblockposition.func_185344_t();
                return false;
            } else {
                blockposition_pooledblockposition.func_185344_t();
                return true;
            }
        }
    }

    public boolean func_70090_H() {
        return this.field_70171_ac;
    }

    public boolean func_191953_am() {
        return this.field_70170_p.func_72918_a(this.func_174813_aQ().func_72314_b(0.0D, -20.0D, 0.0D).func_186664_h(0.001D), Material.field_151586_h, this);
    }

    public boolean func_70072_I() {
        return this.doWaterMovement();
    }

    public boolean doWaterMovement() {
        // Paper end
        if (this.func_184187_bx() instanceof EntityBoat) {
            this.field_70171_ac = false;
        } else if (this.field_70170_p.func_72918_a(this.func_174813_aQ().func_72314_b(0.0D, -0.4000000059604645D, 0.0D).func_186664_h(0.001D), Material.field_151586_h, this)) {
            if (!this.field_70171_ac && !this.field_70148_d) {
                this.func_71061_d_();
            }

            this.field_70143_R = 0.0F;
            this.field_70171_ac = true;
            this.func_70066_B();
        } else {
            this.field_70171_ac = false;
        }

        return this.field_70171_ac;
    }

    protected void func_71061_d_() {
        Entity entity = this.func_184207_aI() && this.func_184179_bs() != null ? this.func_184179_bs() : this;
        float f = entity == this ? 0.2F : 0.9F;
        float f1 = MathHelper.func_76133_a(entity.field_70159_w * entity.field_70159_w * 0.20000000298023224D + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y * 0.20000000298023224D) * f;

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        this.func_184185_a(this.func_184181_aa(), f1, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4F);
        float f2 = MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b);

        int i;
        float f3;
        float f4;

        for (i = 0; i < 1.0F + this.field_70130_N * 20.0F; ++i) {
            f3 = (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * this.field_70130_N;
            f4 = (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * this.field_70130_N;
            this.field_70170_p.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.field_70165_t + f3, f2 + 1.0F, this.field_70161_v + f4, this.field_70159_w, this.field_70181_x - this.field_70146_Z.nextFloat() * 0.2F, this.field_70179_y, new int[0]);
        }

        for (i = 0; i < 1.0F + this.field_70130_N * 20.0F; ++i) {
            f3 = (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * this.field_70130_N;
            f4 = (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * this.field_70130_N;
            this.field_70170_p.func_175688_a(EnumParticleTypes.WATER_SPLASH, this.field_70165_t + f3, f2 + 1.0F, this.field_70161_v + f4, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
        }

    }

    public void func_174830_Y() {
        if (this.func_70051_ag() && !this.func_70090_H()) {
            this.func_174808_Z();
        }

    }

    protected void func_174808_Z() {
        int i = MathHelper.func_76128_c(this.field_70165_t);
        int j = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224D);
        int k = MathHelper.func_76128_c(this.field_70161_v);
        BlockPos blockposition = new BlockPos(i, j, k);
        IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);

        if (iblockdata.func_185901_i() != EnumBlockRenderType.INVISIBLE) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.BLOCK_CRACK, this.field_70165_t + (this.field_70146_Z.nextFloat() - 0.5D) * this.field_70130_N, this.func_174813_aQ().field_72338_b + 0.1D, this.field_70161_v + (this.field_70146_Z.nextFloat() - 0.5D) * this.field_70130_N, -this.field_70159_w * 4.0D, 1.5D, -this.field_70179_y * 4.0D, new int[] { Block.func_176210_f(iblockdata)});
        }

    }

    public boolean func_70055_a(Material material) {
        if (this.func_184187_bx() instanceof EntityBoat) {
            return false;
        } else {
            double d0 = this.field_70163_u + this.func_70047_e();
            BlockPos blockposition = new BlockPos(this.field_70165_t, d0, this.field_70161_v);
            IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);

            if (iblockdata.func_185904_a() == material) {
                float f = BlockLiquid.func_149801_b(iblockdata.func_177230_c().func_176201_c(iblockdata)) - 0.11111111F;
                float f1 = blockposition.func_177956_o() + 1 - f;
                boolean flag = d0 < f1;

                return !flag && this instanceof EntityPlayer ? false : flag;
            } else {
                return false;
            }
        }
    }

    public boolean func_180799_ab() {
        return this.field_70170_p.func_72875_a(this.func_174813_aQ().func_72314_b(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.field_151587_i);
    }

    public void func_191958_b(float f, float f1, float f2, float f3) {
        float f4 = f * f + f1 * f1 + f2 * f2;

        if (f4 >= 1.0E-4F) {
            f4 = MathHelper.func_76129_c(f4);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f3 / f4;
            f *= f4;
            f1 *= f4;
            f2 *= f4;
            float f5 = MathHelper.func_76126_a(this.field_70177_z * 0.017453292F);
            float f6 = MathHelper.func_76134_b(this.field_70177_z * 0.017453292F);

            this.field_70159_w += f * f6 - f2 * f5;
            this.field_70181_x += f1;
            this.field_70179_y += f2 * f6 + f * f5;
        }
    }

    public float func_70013_c() {
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(MathHelper.func_76128_c(this.field_70165_t), 0, MathHelper.func_76128_c(this.field_70161_v));

        if (this.field_70170_p.func_175667_e(blockposition_mutableblockposition)) {
            blockposition_mutableblockposition.func_185336_p(MathHelper.func_76128_c(this.field_70163_u + this.func_70047_e()));
            return this.field_70170_p.func_175724_o(blockposition_mutableblockposition);
        } else {
            return 0.0F;
        }
    }

    public void func_70029_a(World world) {
        // CraftBukkit start
        if (world == null) {
            func_70106_y();
            this.field_70170_p = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
            return;
        }
        // CraftBukkit end
        this.field_70170_p = world;
    }

    public void func_70080_a(double d0, double d1, double d2, float f, float f1) {
        this.field_70165_t = MathHelper.func_151237_a(d0, -3.0E7D, 3.0E7D);
        this.field_70163_u = d1;
        this.field_70161_v = MathHelper.func_151237_a(d2, -3.0E7D, 3.0E7D);
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        f1 = MathHelper.func_76131_a(f1, -90.0F, 90.0F);
        this.field_70177_z = f;
        this.field_70125_A = f1;
        this.field_70126_B = this.field_70177_z;
        this.field_70127_C = this.field_70125_A;
        double d3 = this.field_70126_B - f;

        if (d3 < -180.0D) {
            this.field_70126_B += 360.0F;
        }

        if (d3 >= 180.0D) {
            this.field_70126_B -= 360.0F;
        }

        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        this.func_70101_b(f, f1);
    }

    public void func_174828_a(BlockPos blockposition, float f, float f1) {
        this.func_70012_b(blockposition.func_177958_n() + 0.5D, blockposition.func_177956_o(), blockposition.func_177952_p() + 0.5D, f, f1);
    }

    public void func_70012_b(double d0, double d1, double d2, float f, float f1) {
        this.field_70165_t = d0;
        this.field_70163_u = d1;
        this.field_70161_v = d2;
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        this.field_70142_S = this.field_70165_t;
        this.field_70137_T = this.field_70163_u;
        this.field_70136_U = this.field_70161_v;
        this.field_70177_z = f;
        this.field_70125_A = f1;
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public float func_70032_d(Entity entity) {
        float f = (float) (this.field_70165_t - entity.field_70165_t);
        float f1 = (float) (this.field_70163_u - entity.field_70163_u);
        float f2 = (float) (this.field_70161_v - entity.field_70161_v);

        return MathHelper.func_76129_c(f * f + f1 * f1 + f2 * f2);
    }

    public double func_70092_e(double d0, double d1, double d2) {
        double d3 = this.field_70165_t - d0;
        double d4 = this.field_70163_u - d1;
        double d5 = this.field_70161_v - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double func_174818_b(BlockPos blockposition) {
        return blockposition.func_177954_c(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public double func_174831_c(BlockPos blockposition) {
        return blockposition.func_177957_d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public double func_70011_f(double d0, double d1, double d2) {
        double d3 = this.field_70165_t - d0;
        double d4 = this.field_70163_u - d1;
        double d5 = this.field_70161_v - d2;

        return MathHelper.func_76133_a(d3 * d3 + d4 * d4 + d5 * d5);
    }

    public double func_70068_e(Entity entity) {
        double d0 = this.field_70165_t - entity.field_70165_t;
        double d1 = this.field_70163_u - entity.field_70163_u;
        double d2 = this.field_70161_v - entity.field_70161_v;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void func_70100_b_(EntityPlayer entityhuman) {}

    public void func_70108_f(Entity entity) {
        if (!this.func_184223_x(entity)) {
            if (!entity.field_70145_X && !this.field_70145_X) {
                double d0 = entity.field_70165_t - this.field_70165_t;
                double d1 = entity.field_70161_v - this.field_70161_v;
                double d2 = MathHelper.func_76132_a(d0, d1);

                if (d2 >= 0.009999999776482582D) {
                    d2 = MathHelper.func_76133_a(d2);
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
                    d0 *= 1.0F - this.field_70144_Y;
                    d1 *= 1.0F - this.field_70144_Y;
                    if (!this.func_184207_aI()) {
                        this.func_70024_g(-d0, 0.0D, -d1);
                    }

                    if (!entity.func_184207_aI()) {
                        entity.func_70024_g(d0, 0.0D, d1);
                    }
                }

            }
        }
    }

    public void func_70024_g(double d0, double d1, double d2) {
        this.field_70159_w += d0;
        this.field_70181_x += d1;
        this.field_70179_y += d2;
        this.field_70160_al = true;
    }

    protected void func_70018_K() {
        this.field_70133_I = true;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            this.func_70018_K();
            return false;
        }
    }

    public Vec3d func_70676_i(float f) {
        if (f == 1.0F) {
            return this.func_174806_f(this.field_70125_A, this.field_70177_z);
        } else {
            float f1 = this.field_70127_C + (this.field_70125_A - this.field_70127_C) * f;
            float f2 = this.field_70126_B + (this.field_70177_z - this.field_70126_B) * f;

            return this.func_174806_f(f1, f2);
        }
    }

    protected final Vec3d func_174806_f(float f, float f1) {
        float f2 = MathHelper.func_76134_b(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.func_76126_a(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.func_76134_b(-f * 0.017453292F);
        float f5 = MathHelper.func_76126_a(-f * 0.017453292F);

        return new Vec3d(f3 * f4, f5, f2 * f4);
    }

    public Vec3d func_174824_e(float f) {
        if (f == 1.0F) {
            return new Vec3d(this.field_70165_t, this.field_70163_u + this.func_70047_e(), this.field_70161_v);
        } else {
            double d0 = this.field_70169_q + (this.field_70165_t - this.field_70169_q) * f;
            double d1 = this.field_70167_r + (this.field_70163_u - this.field_70167_r) * f + this.func_70047_e();
            double d2 = this.field_70166_s + (this.field_70161_v - this.field_70166_s) * f;

            return new Vec3d(d0, d1, d2);
        }
    }

    public boolean func_70067_L() {
        return false;
    }

    public boolean func_70104_M() {
        return false;
    }

    public void func_191956_a(Entity entity, int i, DamageSource damagesource) {
        if (entity instanceof EntityPlayerMP) {
            CriteriaTriggers.field_192123_c.func_192211_a((EntityPlayerMP) entity, this, damagesource);
        }

    }

    public boolean func_184198_c(NBTTagCompound nbttagcompound) {
        String s = this.func_70022_Q();

        if (!this.field_70128_L && s != null) {
            nbttagcompound.func_74778_a("id", s);
            this.func_189511_e(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public boolean func_70039_c(NBTTagCompound nbttagcompound) {
        String s = this.func_70022_Q();

        if (!this.field_70128_L && s != null && !this.func_184218_aH()) {
            nbttagcompound.func_74778_a("id", s);
            this.func_189511_e(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public static void func_190533_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, new IDataWalker() {
            @Override
            public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (nbttagcompound.func_150297_b("Passengers", 9)) {
                    NBTTagList nbttaglist = nbttagcompound.func_150295_c("Passengers", 10);

                    for (int j = 0; j < nbttaglist.func_74745_c(); ++j) {
                        nbttaglist.func_150304_a(j, dataconverter.func_188251_a(FixTypes.ENTITY, nbttaglist.func_150305_b(j), i));
                    }
                }

                return nbttagcompound;
            }
        });
    }

    public NBTTagCompound func_189511_e(NBTTagCompound nbttagcompound) {
        try {
            nbttagcompound.func_74782_a("Pos", this.func_70087_a(new double[] { this.field_70165_t, this.field_70163_u, this.field_70161_v}));
            nbttagcompound.func_74782_a("Motion", this.func_70087_a(new double[] { this.field_70159_w, this.field_70181_x, this.field_70179_y}));

            // CraftBukkit start - Checking for NaN pitch/yaw and resetting to zero
            // TODO: make sure this is the best way to address this.
            if (Float.isNaN(this.field_70177_z)) {
                this.field_70177_z = 0;
            }

            if (Float.isNaN(this.field_70125_A)) {
                this.field_70125_A = 0;
            }
            // CraftBukkit end

            nbttagcompound.func_74782_a("Rotation", this.func_70049_a(new float[] { this.field_70177_z, this.field_70125_A}));
            nbttagcompound.func_74776_a("FallDistance", this.field_70143_R);
            nbttagcompound.func_74777_a("Fire", (short) this.field_190534_ay);
            nbttagcompound.func_74777_a("Air", (short) this.func_70086_ai());
            nbttagcompound.func_74757_a("OnGround", this.field_70122_E);
            nbttagcompound.func_74768_a("Dimension", this.field_71093_bK);
            nbttagcompound.func_74757_a("Invulnerable", this.field_83001_bt);
            nbttagcompound.func_74768_a("PortalCooldown", this.field_71088_bW);
            nbttagcompound.func_186854_a("UUID", this.func_110124_au());
            // CraftBukkit start
            // PAIL: Check above UUID reads 1.8 properly, ie: UUIDMost / UUIDLeast
            nbttagcompound.func_74772_a("WorldUUIDLeast", this.field_70170_p.func_72860_G().getUUID().getLeastSignificantBits());
            nbttagcompound.func_74772_a("WorldUUIDMost", this.field_70170_p.func_72860_G().getUUID().getMostSignificantBits());
            nbttagcompound.func_74768_a("Bukkit.updateLevel", CURRENT_LEVEL);
            nbttagcompound.func_74768_a("Spigot.ticksLived", this.field_70173_aa);
            // CraftBukkit end
            if (this.func_145818_k_()) {
                nbttagcompound.func_74778_a("CustomName", this.func_95999_t());
            }

            if (this.func_174833_aM()) {
                nbttagcompound.func_74757_a("CustomNameVisible", this.func_174833_aM());
            }

            this.field_174837_as.func_179670_b(nbttagcompound);
            if (this.func_174814_R()) {
                nbttagcompound.func_74757_a("Silent", this.func_174814_R());
            }

            if (this.func_189652_ae()) {
                nbttagcompound.func_74757_a("NoGravity", this.func_189652_ae());
            }

            if (this.field_184238_ar) {
                nbttagcompound.func_74757_a("Glowing", this.field_184238_ar);
            }

            NBTTagList nbttaglist;
            Iterator iterator;

            if (!this.field_184236_aF.isEmpty()) {
                nbttaglist = new NBTTagList();
                iterator = this.field_184236_aF.iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();

                    nbttaglist.func_74742_a(new NBTTagString(s));
                }

                nbttagcompound.func_74782_a("Tags", nbttaglist);
            }

            this.func_70014_b(nbttagcompound);
            if (this.func_184207_aI()) {
                nbttaglist = new NBTTagList();
                iterator = this.func_184188_bt().iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    if (entity.func_184198_c(nbttagcompound1)) {
                        nbttaglist.func_74742_a(nbttagcompound1);
                    }
                }

                if (!nbttaglist.func_82582_d()) {
                    nbttagcompound.func_74782_a("Passengers", nbttaglist);
                }
            }

            // Paper start - Save the entity's origin location
            if (origin != null) {
                nbttagcompound.func_74782_a("Paper.Origin", this.createList(origin.getX(), origin.getY(), origin.getZ()));
            }
            // Save entity's from mob spawner status
            if (spawnedViaMobSpawner) {
                nbttagcompound.func_74757_a("Paper.FromMobSpawner", true);
            }
            // Paper end
            return nbttagcompound;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Saving entity NBT");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Entity being saved");

            this.func_85029_a(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    public void func_70020_e(NBTTagCompound nbttagcompound) {
        try {
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("Pos", 6);
            NBTTagList nbttaglist1 = nbttagcompound.func_150295_c("Motion", 6);
            NBTTagList nbttaglist2 = nbttagcompound.func_150295_c("Rotation", 5);

            this.field_70159_w = nbttaglist1.func_150309_d(0);
            this.field_70181_x = nbttaglist1.func_150309_d(1);
            this.field_70179_y = nbttaglist1.func_150309_d(2);

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

            this.field_70165_t = nbttaglist.func_150309_d(0);
            this.field_70163_u = nbttaglist.func_150309_d(1);
            this.field_70161_v = nbttaglist.func_150309_d(2);
            this.field_70142_S = this.field_70165_t;
            this.field_70137_T = this.field_70163_u;
            this.field_70136_U = this.field_70161_v;
            this.field_70169_q = this.field_70165_t;
            this.field_70167_r = this.field_70163_u;
            this.field_70166_s = this.field_70161_v;
            this.field_70177_z = nbttaglist2.func_150308_e(0);
            this.field_70125_A = nbttaglist2.func_150308_e(1);
            this.field_70126_B = this.field_70177_z;
            this.field_70127_C = this.field_70125_A;
            this.func_70034_d(this.field_70177_z);
            this.func_181013_g(this.field_70177_z);
            this.field_70143_R = nbttagcompound.func_74760_g("FallDistance");
            this.field_190534_ay = nbttagcompound.func_74765_d("Fire");
            this.func_70050_g(nbttagcompound.func_74765_d("Air"));
            this.field_70122_E = nbttagcompound.func_74767_n("OnGround");
            if (nbttagcompound.func_74764_b("Dimension")) {
                this.field_71093_bK = nbttagcompound.func_74762_e("Dimension");
            }

            this.field_83001_bt = nbttagcompound.func_74767_n("Invulnerable");
            this.field_71088_bW = nbttagcompound.func_74762_e("PortalCooldown");
            if (nbttagcompound.func_186855_b("UUID")) {
                this.field_96093_i = nbttagcompound.func_186857_a("UUID");
                this.field_189513_ar = this.field_96093_i.toString();
            }

            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
            if (nbttagcompound.func_150297_b("CustomName", 8)) {
                this.func_96094_a(nbttagcompound.func_74779_i("CustomName"));
            }

            this.func_174805_g(nbttagcompound.func_74767_n("CustomNameVisible"));
            this.field_174837_as.func_179668_a(nbttagcompound);
            this.func_174810_b(nbttagcompound.func_74767_n("Silent"));
            this.func_189654_d(nbttagcompound.func_74767_n("NoGravity"));
            this.func_184195_f(nbttagcompound.func_74767_n("Glowing"));
            if (nbttagcompound.func_150297_b("Tags", 9)) {
                this.field_184236_aF.clear();
                NBTTagList nbttaglist3 = nbttagcompound.func_150295_c("Tags", 8);
                int i = Math.min(nbttaglist3.func_74745_c(), 1024);

                for (int j = 0; j < i; ++j) {
                    this.field_184236_aF.add(nbttaglist3.func_150307_f(j));
                }
            }

            this.func_70037_a(nbttagcompound);
            if (this.func_142008_O()) {
                this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
            }

            // CraftBukkit start
            if (this instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) this;

                this.field_70173_aa = nbttagcompound.func_74762_e("Spigot.ticksLived");

                // Reset the persistence for tamed animals
                if (entity instanceof EntityTameable && !isLevelAtLeast(nbttagcompound, 2) && !nbttagcompound.func_74767_n("PersistenceRequired")) {
                    EntityLiving entityinsentient = (EntityLiving) entity;
                    entityinsentient.field_82179_bU = !entityinsentient.func_70692_ba();
                }
            }
            // CraftBukkit end

            // CraftBukkit start
            double limit = getBukkitEntity() instanceof Vehicle ? 100.0D : 10.0D;
            if (Math.abs(this.field_70159_w) > limit) {
                this.field_70159_w = 0.0D;
            }

            if (Math.abs(this.field_70181_x) > limit) {
                this.field_70181_x = 0.0D;
            }

            if (Math.abs(this.field_70179_y) > limit) {
                this.field_70179_y = 0.0D;
            }
            // CraftBukkit end

            // CraftBukkit start - Reset world
            if (this instanceof EntityPlayerMP) {
                Server server = Bukkit.getServer();
                org.bukkit.World bworld = null;

                // TODO: Remove World related checks, replaced with WorldUID
                String worldName = nbttagcompound.func_74779_i("world");

                if (nbttagcompound.func_74764_b("WorldUUIDMost") && nbttagcompound.func_74764_b("WorldUUIDLeast")) {
                    UUID uid = new UUID(nbttagcompound.func_74763_f("WorldUUIDMost"), nbttagcompound.func_74763_f("WorldUUIDLeast"));
                    bworld = server.getWorld(uid);
                } else {
                    bworld = server.getWorld(worldName);
                }

                if (bworld == null) {
                    EntityPlayerMP entityPlayer = (EntityPlayerMP) this;
                    bworld = ((org.bukkit.craftbukkit.CraftServer) server).getServer().func_71218_a(entityPlayer.field_71093_bK).getWorld();
                }

                func_70029_a(bworld == null? null : ((CraftWorld) bworld).getHandle());
            }
            // CraftBukkit end

            // Paper start - Restore the entity's origin location
            NBTTagList originTag = nbttagcompound.func_150295_c("Paper.Origin", 6);
            if (!originTag.func_82582_d()) {
                origin = new Location(field_70170_p.getWorld(), originTag.getDoubleAt(0), originTag.getDoubleAt(1), originTag.getDoubleAt(2));
            }

            spawnedViaMobSpawner = nbttagcompound.func_74767_n("Paper.FromMobSpawner"); // Restore entity's from mob spawner status
            // Paper end

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Loading entity NBT");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Entity being loaded");

            this.func_85029_a(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    protected boolean func_142008_O() {
        return true;
    }

    @Nullable
    public final String func_70022_Q() {
        ResourceLocation minecraftkey = EntityList.func_191301_a(this);

        return minecraftkey == null ? null : minecraftkey.toString();
    }

    protected abstract void func_70037_a(NBTTagCompound nbttagcompound);

    protected abstract void func_70014_b(NBTTagCompound nbttagcompound);

    protected NBTTagList createList(double... adouble) { return func_70087_a(adouble); } // Paper - OBFHELPER
    protected NBTTagList func_70087_a(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.func_74742_a(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    protected NBTTagList func_70049_a(float... afloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat1 = afloat;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float f = afloat1[j];

            nbttaglist.func_74742_a(new NBTTagFloat(f));
        }

        return nbttaglist;
    }

    @Nullable
    public EntityItem func_145779_a(Item item, int i) {
        return this.func_145778_a(item, i, 0.0F);
    }

    @Nullable
    public EntityItem func_145778_a(Item item, int i, float f) {
        return this.func_70099_a(new ItemStack(item, i, 0), f);
    }

    @Nullable public final EntityItem dropItem(ItemStack itemstack, float offset) { return this.func_70099_a(itemstack, offset); } // Paper - OBFHELPER
    @Nullable
    public EntityItem func_70099_a(ItemStack itemstack, float f) {
        if (itemstack.func_190926_b()) {
            return null;
        } else {
            // CraftBukkit start - Capture drops for death event
            if (this instanceof EntityLivingBase && !((EntityLivingBase) this).forceDrops) {
                ((EntityLivingBase) this).drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(itemstack));
                return null;
            }
            // CraftBukkit end
            EntityItem entityitem = new EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u + f, this.field_70161_v, itemstack);

            entityitem.func_174869_p();
            this.field_70170_p.func_72838_d(entityitem);
            return entityitem;
        }
    }

    public boolean func_70089_S() {
        return !this.field_70128_L;
    }

    public boolean func_70094_T() {
        if (this.field_70145_X) {
            return false;
        } else {
            BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();

            for (int i = 0; i < 8; ++i) {
                int j = MathHelper.func_76128_c(this.field_70163_u + ((i >> 0) % 2 - 0.5F) * 0.1F + this.func_70047_e());
                int k = MathHelper.func_76128_c(this.field_70165_t + ((i >> 1) % 2 - 0.5F) * this.field_70130_N * 0.8F);
                int l = MathHelper.func_76128_c(this.field_70161_v + ((i >> 2) % 2 - 0.5F) * this.field_70130_N * 0.8F);

                if (blockposition_pooledblockposition.func_177958_n() != k || blockposition_pooledblockposition.func_177956_o() != j || blockposition_pooledblockposition.func_177952_p() != l) {
                    blockposition_pooledblockposition.func_181079_c(k, j, l);
                    if (this.field_70170_p.func_180495_p(blockposition_pooledblockposition).func_191058_s()) {
                        blockposition_pooledblockposition.func_185344_t();
                        return true;
                    }
                }
            }

            blockposition_pooledblockposition.func_185344_t();
            return false;
        }
    }

    public boolean func_184230_a(EntityPlayer entityhuman, EnumHand enumhand) {
        return false;
    }

    @Nullable
    public AxisAlignedBB func_70114_g(Entity entity) {
        return null;
    }

    public void func_70098_U() {
        Entity entity = this.func_184187_bx();

        if (this.func_184218_aH() && entity.field_70128_L) {
            this.func_184210_p();
        } else {
            this.field_70159_w = 0.0D;
            this.field_70181_x = 0.0D;
            this.field_70179_y = 0.0D;
            this.func_70071_h_();
            if (this.func_184218_aH()) {
                entity.func_184232_k(this);
            }
        }
    }

    public void func_184232_k(Entity entity) {
        if (this.func_184196_w(entity)) {
            entity.func_70107_b(this.field_70165_t, this.field_70163_u + this.func_70042_X() + entity.func_70033_W(), this.field_70161_v);
        }
    }

    public double func_70033_W() {
        return 0.0D;
    }

    public double func_70042_X() {
        return this.field_70131_O * 0.75D;
    }

    public boolean func_184220_m(Entity entity) {
        return this.func_184205_a(entity, false);
    }

    public boolean func_184205_a(Entity entity, boolean flag) {
        for (Entity entity1 = entity; entity1.field_184239_as != null; entity1 = entity1.field_184239_as) {
            if (entity1.field_184239_as == this) {
                return false;
            }
        }

        if (!flag && (!this.func_184228_n(entity) || !entity.func_184219_q(this))) {
            return false;
        } else {
            if (this.func_184218_aH()) {
                this.func_184210_p();
            }

            this.field_184239_as = entity;
            this.field_184239_as.func_184200_o(this);
            return true;
        }
    }

    protected boolean func_184228_n(Entity entity) {
        return this.field_184245_j <= 0;
    }

    public void func_184226_ay() {
        for (int i = this.field_184244_h.size() - 1; i >= 0; --i) {
            this.field_184244_h.get(i).func_184210_p();
        }

    }

    public void func_184210_p() {
        if (this.field_184239_as != null) {
            Entity entity = this.field_184239_as;

            this.field_184239_as = null;
            entity.func_184225_p(this);
        }

    }

    protected void func_184200_o(Entity entity) {
        if (entity == this) throw new IllegalArgumentException("Entities cannot become a passenger of themselves"); // Paper - issue 572
        if (entity.func_184187_bx() != this) {
            throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
        } else {
            // CraftBukkit start
            com.google.common.base.Preconditions.checkState(!entity.field_184244_h.contains(this), "Circular entity riding! %s %s", this, entity);

            CraftEntity craft = (CraftEntity) entity.getBukkitEntity().getVehicle();
            Entity orig = craft == null ? null : craft.getHandle();
            if (getBukkitEntity() instanceof Vehicle && entity.getBukkitEntity() instanceof LivingEntity && entity.field_70170_p.func_175680_a((int) entity.field_70165_t >> 4, (int) entity.field_70161_v >> 4, false)) { // Boolean not used
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
            if (!this.field_70170_p.field_72995_K && entity instanceof EntityPlayer && !(this.func_184179_bs() instanceof EntityPlayer)) {
                this.field_184244_h.add(0, entity);
            } else {
                this.field_184244_h.add(entity);
            }

        }
    }

    protected void func_184225_p(Entity entity) {
        if (entity.func_184187_bx() == this) {
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

            this.field_184244_h.remove(entity);
            entity.field_184245_j = 60;
        }
    }

    protected boolean func_184219_q(Entity entity) {
        return this.func_184188_bt().size() < 1;
    }

    public float func_70111_Y() {
        return 0.0F;
    }

    public Vec3d func_70040_Z() {
        return this.func_174806_f(this.field_70125_A, this.field_70177_z);
    }

    public void func_181015_d(BlockPos blockposition) {
        if (this.field_71088_bW > 0) {
            this.field_71088_bW = this.func_82147_ab();
        } else {
            if (!this.field_70170_p.field_72995_K && !blockposition.equals(this.field_181016_an)) {
                this.field_181016_an = new BlockPos(blockposition);
                BlockPattern.PatternHelper shapedetector_shapedetectorcollection = Blocks.field_150427_aO.func_181089_f(this.field_70170_p, this.field_181016_an);
                double d0 = shapedetector_shapedetectorcollection.func_177669_b().func_176740_k() == EnumFacing.Axis.X ? (double) shapedetector_shapedetectorcollection.func_181117_a().func_177952_p() : (double) shapedetector_shapedetectorcollection.func_181117_a().func_177958_n();
                double d1 = shapedetector_shapedetectorcollection.func_177669_b().func_176740_k() == EnumFacing.Axis.X ? this.field_70161_v : this.field_70165_t;

                d1 = Math.abs(MathHelper.func_181160_c(d1 - (shapedetector_shapedetectorcollection.func_177669_b().func_176746_e().func_176743_c() == EnumFacing.AxisDirection.NEGATIVE ? 1 : 0), d0, d0 - shapedetector_shapedetectorcollection.func_181118_d()));
                double d2 = MathHelper.func_181160_c(this.field_70163_u - 1.0D, shapedetector_shapedetectorcollection.func_181117_a().func_177956_o(), shapedetector_shapedetectorcollection.func_181117_a().func_177956_o() - shapedetector_shapedetectorcollection.func_181119_e());

                this.field_181017_ao = new Vec3d(d1, d2, 0.0D);
                this.field_181018_ap = shapedetector_shapedetectorcollection.func_177669_b();
            }

            this.field_71087_bX = true;
        }
    }

    public int func_82147_ab() {
        return 300;
    }

    public Iterable<ItemStack> func_184214_aD() {
        return Entity.field_190535_b;
    }

    public Iterable<ItemStack> func_184193_aE() {
        return Entity.field_190535_b;
    }

    public Iterable<ItemStack> func_184209_aF() {
        return Iterables.concat(this.func_184214_aD(), this.func_184193_aE());
    }

    public void func_184201_a(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {}

    public boolean func_70027_ad() {
        boolean flag = this.field_70170_p != null && this.field_70170_p.field_72995_K;

        return !this.field_70178_ae && (this.field_190534_ay > 0 || flag && this.func_70083_f(0));
    }

    public boolean func_184218_aH() {
        return this.func_184187_bx() != null;
    }

    public boolean func_184207_aI() {
        return !this.func_184188_bt().isEmpty();
    }

    public boolean func_70093_af() {
        return this.func_70083_f(1);
    }

    public void func_70095_a(boolean flag) {
        this.func_70052_a(1, flag);
    }

    public boolean func_70051_ag() {
        return this.func_70083_f(3);
    }

    public void func_70031_b(boolean flag) {
        this.func_70052_a(3, flag);
    }

    public boolean func_184202_aL() {
        return this.field_184238_ar || this.field_70170_p.field_72995_K && this.func_70083_f(6);
    }

    public void func_184195_f(boolean flag) {
        this.field_184238_ar = flag;
        if (!this.field_70170_p.field_72995_K) {
            this.func_70052_a(6, this.field_184238_ar);
        }

    }

    public boolean func_82150_aj() {
        return this.func_70083_f(5);
    }

    @Nullable public Team getTeam() { return this.func_96124_cp(); } // Paper - OBFHELPER
    @Nullable
    public Team func_96124_cp() {
        if (!this.field_70170_p.paperConfig.nonPlayerEntitiesOnScoreboards && !(this instanceof EntityPlayer)) { return null; } // Paper
        return this.field_70170_p.func_96441_U().func_96509_i(this.func_189512_bd());
    }

    public boolean func_184191_r(Entity entity) {
        return this.func_184194_a(entity.func_96124_cp());
    }

    public boolean func_184194_a(Team scoreboardteambase) {
        return this.func_96124_cp() != null ? this.func_96124_cp().func_142054_a(scoreboardteambase) : false;
    }

    public void func_82142_c(boolean flag) {
        this.func_70052_a(5, flag);
    }

    public boolean func_70083_f(int i) {
        return (this.field_70180_af.func_187225_a(Entity.field_184240_ax).byteValue() & 1 << i) != 0;
    }

    public void func_70052_a(int i, boolean flag) {
        byte b0 = this.field_70180_af.func_187225_a(Entity.field_184240_ax).byteValue();

        if (flag) {
            this.field_70180_af.func_187227_b(Entity.field_184240_ax, Byte.valueOf((byte) (b0 | 1 << i)));
        } else {
            this.field_70180_af.func_187227_b(Entity.field_184240_ax, Byte.valueOf((byte) (b0 & ~(1 << i))));
        }

    }

    public int func_70086_ai() {
        return this.field_70180_af.func_187225_a(Entity.field_184241_ay).intValue();
    }

    public void func_70050_g(int i) {
        // CraftBukkit start
        EntityAirChangeEvent event = new EntityAirChangeEvent(this.getBukkitEntity(), i);
        event.getEntity().getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        this.field_70180_af.func_187227_b(Entity.field_184241_ay, Integer.valueOf(event.getAmount()));
        // CraftBukkit end
    }

    public void func_70077_a(EntityLightningBolt entitylightning) {
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

        if (this.field_70178_ae) {
            return;
        }
        CraftEventFactory.entityDamage = entitylightning;
        if (!this.func_70097_a(DamageSource.field_180137_b, 5.0F)) {
            CraftEventFactory.entityDamage = null;
            return;
        }
        // CraftBukkit end
        ++this.field_190534_ay;
        if (this.field_190534_ay == 0) {
            // CraftBukkit start - Call a combust event when lightning strikes
            EntityCombustByEntityEvent entityCombustEvent = new EntityCombustByEntityEvent(stormBukkitEntity, thisBukkitEntity, 8);
            pluginManager.callEvent(entityCombustEvent);
            if (!entityCombustEvent.isCancelled()) {
                this.func_70015_d(entityCombustEvent.getDuration());
            }
            // CraftBukkit end
        }

    }

    public void func_70074_a(EntityLivingBase entityliving) {}

    protected boolean func_145771_j(double d0, double d1, double d2) {
        BlockPos blockposition = new BlockPos(d0, d1, d2);
        double d3 = d0 - blockposition.func_177958_n();
        double d4 = d1 - blockposition.func_177956_o();
        double d5 = d2 - blockposition.func_177952_p();

        if (!this.field_70170_p.func_184143_b(this.func_174813_aQ())) {
            return false;
        } else {
            EnumFacing enumdirection = EnumFacing.UP;
            double d6 = Double.MAX_VALUE;

            if (!this.field_70170_p.func_175665_u(blockposition.func_177976_e()) && d3 < d6) {
                d6 = d3;
                enumdirection = EnumFacing.WEST;
            }

            if (!this.field_70170_p.func_175665_u(blockposition.func_177974_f()) && 1.0D - d3 < d6) {
                d6 = 1.0D - d3;
                enumdirection = EnumFacing.EAST;
            }

            if (!this.field_70170_p.func_175665_u(blockposition.func_177978_c()) && d5 < d6) {
                d6 = d5;
                enumdirection = EnumFacing.NORTH;
            }

            if (!this.field_70170_p.func_175665_u(blockposition.func_177968_d()) && 1.0D - d5 < d6) {
                d6 = 1.0D - d5;
                enumdirection = EnumFacing.SOUTH;
            }

            if (!this.field_70170_p.func_175665_u(blockposition.func_177984_a()) && 1.0D - d4 < d6) {
                d6 = 1.0D - d4;
                enumdirection = EnumFacing.UP;
            }

            float f = this.field_70146_Z.nextFloat() * 0.2F + 0.1F;
            float f1 = enumdirection.func_176743_c().func_179524_a();

            if (enumdirection.func_176740_k() == EnumFacing.Axis.X) {
                this.field_70159_w = f1 * f;
                this.field_70181_x *= 0.75D;
                this.field_70179_y *= 0.75D;
            } else if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
                this.field_70159_w *= 0.75D;
                this.field_70181_x = f1 * f;
                this.field_70179_y *= 0.75D;
            } else if (enumdirection.func_176740_k() == EnumFacing.Axis.Z) {
                this.field_70159_w *= 0.75D;
                this.field_70181_x *= 0.75D;
                this.field_70179_y = f1 * f;
            }

            return true;
        }
    }

    public void func_70110_aj() {
        this.field_70134_J = true;
        this.field_70143_R = 0.0F;
    }

    @Override
    public String func_70005_c_() {
        if (this.func_145818_k_()) {
            return this.func_95999_t();
        } else {
            String s = EntityList.func_75621_b(this);

            if (s == null) {
                s = "generic";
            }

            return I18n.func_74838_a("entity." + s + ".name");
        }
    }

    @Nullable
    public Entity[] func_70021_al() {
        return null;
    }

    public boolean func_70028_i(Entity entity) {
        return this == entity;
    }

    public float func_70079_am() {
        return 0.0F;
    }

    public void func_70034_d(float f) {}

    public void func_181013_g(float f) {}

    public boolean func_70075_an() {
        return true;
    }

    public boolean func_85031_j(Entity entity) {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[] { this.getClass().getSimpleName(), this.func_70005_c_(), Integer.valueOf(this.field_145783_c), this.field_70170_p == null ? "~NULL~" : this.field_70170_p.func_72912_H().func_76065_j(), Double.valueOf(this.field_70165_t), Double.valueOf(this.field_70163_u), Double.valueOf(this.field_70161_v)});
    }

    public boolean func_180431_b(DamageSource damagesource) {
        return this.field_83001_bt && damagesource != DamageSource.field_76380_i && !damagesource.func_180136_u();
    }

    public boolean func_190530_aW() {
        return this.field_83001_bt;
    }

    public void func_184224_h(boolean flag) {
        this.field_83001_bt = flag;
    }

    public void func_82149_j(Entity entity) {
        this.func_70012_b(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, entity.field_70177_z, entity.field_70125_A);
    }

    private void func_180432_n(Entity entity) {
        NBTTagCompound nbttagcompound = entity.func_189511_e(new NBTTagCompound());

        nbttagcompound.func_82580_o("Dimension");
        this.func_70020_e(nbttagcompound);
        this.field_71088_bW = entity.field_71088_bW;
        this.field_181016_an = entity.field_181016_an;
        this.field_181017_ao = entity.field_181017_ao;
        this.field_181018_ap = entity.field_181018_ap;
    }

    @Nullable
    public Entity func_184204_a(int i) {
        if (!this.field_70170_p.field_72995_K && !this.field_70128_L) {
            this.field_70170_p.field_72984_F.func_76320_a("changeDimension");
            MinecraftServer minecraftserver = this.func_184102_h();
            // CraftBukkit start - Move logic into new function "teleportTo(Location,boolean)"
            // int j = this.dimension;
            // WorldServer worldserver = minecraftserver.getWorldServer(j);
            // WorldServer worldserver1 = minecraftserver.getWorldServer(i);
            WorldServer exitWorld = null;
            if (this.field_71093_bK < CraftWorld.CUSTOM_DIMENSION_OFFSET) { // Plugins must specify exit from custom Bukkit worlds
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
                    exit = new Location(exitWorld.getWorld(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                } else {
                    exit = minecraftserver.func_184103_al().calculateTarget(enter, minecraftserver.func_71218_a(i));
                }
            }
            else {
                exit = null;
            }
            boolean useTravelAgent = exitWorld != null && !(this.field_71093_bK == 1 && exitWorld.dimension == 1); // don't use agent for custom worlds or return from THE_END

            TravelAgent agent = exit != null ? (TravelAgent) ((CraftWorld) exit.getWorld()).getHandle().func_85176_s() : org.bukkit.craftbukkit.CraftTravelAgent.DEFAULT; // return arbitrary TA to compensate for implementation dependent plugins
            boolean oldCanCreate = agent.getCanCreatePortal();
            agent.setCanCreatePortal(false); // General entities cannot create portals

            EntityPortalEvent event = new EntityPortalEvent(this.getBukkitEntity(), enter, exit, agent);
            event.useTravelAgent(useTravelAgent);
            event.getEntity().getServer().getPluginManager().callEvent(event);
            if (event.isCancelled() || event.getTo() == null || event.getTo().getWorld() == null || !this.func_70089_S()) {
                agent.setCanCreatePortal(oldCanCreate);
                return null;
            }
            exit = event.useTravelAgent() ? event.getPortalTravelAgent().findOrCreate(event.getTo()) : event.getTo();
            agent.setCanCreatePortal(oldCanCreate);

            // Need to make sure the profiler state is reset afterwards (but we still want to time the call)
            Entity entity = this.teleportTo(exit, true);
            this.field_70170_p.field_72984_F.func_76319_b();
            return entity;
        }
        return null;
    }

    public Entity teleportTo(Location exit, boolean portal) {
        if (!this.field_70128_L) { // Paper
            WorldServer worldserver = ((CraftWorld) getBukkitEntity().getLocation().getWorld()).getHandle();
            WorldServer worldserver1 = ((CraftWorld) exit.getWorld()).getHandle();
            int i = worldserver1.dimension;
            // CraftBukkit end

            this.field_71093_bK = i;
            /* CraftBukkit start - TODO: Check if we need this
            if (j == 1 && i == 1) {
                worldserver1 = minecraftserver.getWorldServer(0);
                this.dimension = 0;
            }
            // CraftBukkit end */

            this.field_70170_p.func_72973_f(this); // Paper - Fully remove entity, can't have dupes in the UUID map
            this.field_70128_L = false;
            this.field_70170_p.field_72984_F.func_76320_a("reposition");
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
            worldserver1.func_73046_m().func_184103_al().repositionEntity(this, exit, portal);
            // worldserver.entityJoinedWorld(this, false); // Handled in repositionEntity
            // CraftBukkit end
            this.field_70170_p.field_72984_F.func_76318_c("reloading");
            Entity entity = EntityList.func_191304_a(this.getClass(), worldserver1);

            if (entity != null) {
                entity.func_180432_n(this);
                /* CraftBukkit start - We need to do this...
                if (j == 1 && i == 1) {
                    BlockPosition blockposition1 = worldserver1.q(worldserver1.getSpawn());

                    entity.setPositionRotation(blockposition1, entity.yaw, entity.pitch);
                } else {
                    entity.setPositionRotation(blockposition, entity.yaw, entity.pitch);
                }
                // CraftBukkit end */

                boolean flag = entity.field_98038_p;

                entity.field_98038_p = true;
                worldserver1.func_72838_d(entity);
                entity.field_98038_p = flag;
                worldserver1.func_72866_a(entity, false);
                // CraftBukkit start - Forward the CraftEntity to the new entity
                this.getBukkitEntity().setHandle(entity);
                entity.bukkitEntity = this.getBukkitEntity();

                if (this instanceof EntityLiving) {
                    ((EntityLiving)this).func_110160_i(true, false); // Unleash to prevent duping of leads.
                }
                // CraftBukkit end
            }

            this.field_70128_L = true;
            this.field_70170_p.field_72984_F.func_76319_b();
            worldserver.func_82742_i();
            worldserver1.func_82742_i();
            // this.world.methodProfiler.b(); // CraftBukkit: Moved up to keep balanced
            return entity;
        } else {
            return null;
        }
    }

    public boolean func_184222_aU() {
        return true;
    }

    public float func_180428_a(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata) {
        return iblockdata.func_177230_c().func_149638_a(this);
    }

    public boolean func_174816_a(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata, float f) {
        return true;
    }

    public int func_82143_as() {
        return 3;
    }

    public Vec3d func_181014_aG() {
        return this.field_181017_ao;
    }

    public EnumFacing func_181012_aH() {
        return this.field_181018_ap;
    }

    public boolean func_145773_az() {
        return false;
    }

    public void func_85029_a(CrashReportCategory crashreportsystemdetails) {
        crashreportsystemdetails.func_189529_a("Entity Type", new ICrashReportDetail() {
            public String a() throws Exception {
                return EntityList.func_191301_a(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_71507_a("Entity ID", Integer.valueOf(this.field_145783_c));
        crashreportsystemdetails.func_189529_a("Entity Name", new ICrashReportDetail() {
            public String a() throws Exception {
                return Entity.this.func_70005_c_();
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_71507_a("Entity\'s Exact location", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.field_70165_t), Double.valueOf(this.field_70163_u), Double.valueOf(this.field_70161_v)}));
        crashreportsystemdetails.func_71507_a("Entity\'s Block location", CrashReportCategory.func_184876_a(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.field_70163_u), MathHelper.func_76128_c(this.field_70161_v)));
        crashreportsystemdetails.func_71507_a("Entity\'s Momentum", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.field_70159_w), Double.valueOf(this.field_70181_x), Double.valueOf(this.field_70179_y)}));
        crashreportsystemdetails.func_189529_a("Entity\'s Passengers", new ICrashReportDetail() {
            public String a() throws Exception {
                return Entity.this.func_184188_bt().toString();
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Entity\'s Vehicle", new ICrashReportDetail() {
            public String a() throws Exception {
                return Entity.this.func_184187_bx().toString();
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
    }

    public void func_184221_a(UUID uuid) {
        this.field_96093_i = uuid;
        this.field_189513_ar = this.field_96093_i.toString();
    }

    public UUID func_110124_au() {
        return this.field_96093_i;
    }

    public String func_189512_bd() {
        return this.field_189513_ar;
    }

    public boolean func_96092_aw() {
        return this.pushedByWater();
    }

    public boolean pushedByWater() {
        // Paper end
        return true;
    }

    @Override
    public ITextComponent func_145748_c_() {
        TextComponentString chatcomponenttext = new TextComponentString(ScorePlayerTeam.func_96667_a(this.func_96124_cp(), this.func_70005_c_()));

        chatcomponenttext.func_150256_b().func_150209_a(this.func_174823_aP());
        chatcomponenttext.func_150256_b().func_179989_a(this.func_189512_bd());
        return chatcomponenttext;
    }

    public void func_96094_a(String s) {
        // CraftBukkit start - Add a sane limit for name length
        if (s.length() > 256) {
            s = s.substring(0, 256);
        }
        // CraftBukkit end
        this.field_70180_af.func_187227_b(Entity.field_184242_az, s);
    }

    public String func_95999_t() {
        return this.field_70180_af.func_187225_a(Entity.field_184242_az);
    }

    public boolean func_145818_k_() {
        return !this.field_70180_af.func_187225_a(Entity.field_184242_az).isEmpty();
    }

    public void func_174805_g(boolean flag) {
        this.field_70180_af.func_187227_b(Entity.field_184233_aA, Boolean.valueOf(flag));
    }

    public boolean func_174833_aM() {
        return this.field_70180_af.func_187225_a(Entity.field_184233_aA).booleanValue();
    }

    public void func_70634_a(double d0, double d1, double d2) {
        this.field_184237_aG = true;
        this.func_70012_b(d0, d1, d2, this.field_70177_z, this.field_70125_A);
        this.field_70170_p.func_72866_a(this, false);
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {}

    public EnumFacing func_174811_aO() {
        return EnumFacing.func_176731_b(MathHelper.func_76128_c(this.field_70177_z * 4.0F / 360.0F + 0.5D) & 3);
    }

    public EnumFacing func_184172_bi() {
        return this.func_174811_aO();
    }

    protected HoverEvent func_174823_aP() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        ResourceLocation minecraftkey = EntityList.func_191301_a(this);

        nbttagcompound.func_74778_a("id", this.func_189512_bd());
        if (minecraftkey != null) {
            nbttagcompound.func_74778_a("type", minecraftkey.toString());
        }

        nbttagcompound.func_74778_a("name", this.func_70005_c_());
        return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new TextComponentString(nbttagcompound.toString()));
    }

    public boolean func_174827_a(EntityPlayerMP entityplayer) {
        return true;
    }

    public AxisAlignedBB getBoundingBox() { return func_174813_aQ(); }
    public AxisAlignedBB func_174813_aQ() {
        return this.field_70121_D;
    }

    public void func_174826_a(AxisAlignedBB axisalignedbb) {
        // CraftBukkit start - block invalid bounding boxes
        double a = axisalignedbb.field_72340_a,
                b = axisalignedbb.field_72338_b,
                c = axisalignedbb.field_72339_c,
                d = axisalignedbb.field_72336_d,
                e = axisalignedbb.field_72337_e,
                f = axisalignedbb.field_72334_f;
        double len = axisalignedbb.field_72336_d - axisalignedbb.field_72340_a;
        if (len < 0) d = a;
        if (len > 64) d = a + 64.0;

        len = axisalignedbb.field_72337_e - axisalignedbb.field_72338_b;
        if (len < 0) e = b;
        if (len > 64) e = b + 64.0;

        len = axisalignedbb.field_72334_f - axisalignedbb.field_72339_c;
        if (len < 0) f = c;
        if (len > 64) f = c + 64.0;
        this.field_70121_D = new AxisAlignedBB(a, b, c, d, e, f);
        // CraftBukkit end
    }

    public float func_70047_e() {
        return this.field_70131_O * 0.85F;
    }

    public boolean func_174832_aS() {
        return this.field_174835_g;
    }

    public void func_174821_h(boolean flag) {
        this.field_174835_g = flag;
    }

    public boolean func_174820_d(int i, ItemStack itemstack) {
        return false;
    }

    @Override
    public void func_145747_a(ITextComponent ichatbasecomponent) {}

    @Override
    public boolean func_70003_b(int i, String s) {
        return true;
    }

    @Override
    public BlockPos func_180425_c() {
        return new BlockPos(this.field_70165_t, this.field_70163_u + 0.5D, this.field_70161_v);
    }

    @Override
    public Vec3d func_174791_d() {
        return new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public World getWorld() { return func_130014_f_(); }
    @Override
    public World func_130014_f_() {
        return this.field_70170_p;
    }

    @Override
    public Entity func_174793_f() {
        return this;
    }

    @Override
    public boolean func_174792_t_() {
        return false;
    }

    @Override
    public void func_174794_a(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
        if (this.field_70170_p != null && !this.field_70170_p.field_72995_K) {
            this.field_174837_as.func_184932_a(this.field_70170_p.func_73046_m(), this, commandobjectiveexecutor_enumcommandresult, i);
        }

    }

    @Override
    @Nullable
    public MinecraftServer func_184102_h() {
        return this.field_70170_p.func_73046_m();
    }

    public CommandResultStats func_174807_aT() {
        return this.field_174837_as;
    }

    public void func_174817_o(Entity entity) {
        this.field_174837_as.func_179671_a(entity.func_174807_aT());
    }

    public EnumActionResult func_184199_a(EntityPlayer entityhuman, Vec3d vec3d, EnumHand enumhand) {
        return EnumActionResult.PASS;
    }

    public boolean func_180427_aV() {
        return false;
    }

    protected void func_174815_a(EntityLivingBase entityliving, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EnchantmentHelper.func_151384_a((EntityLivingBase) entity, (Entity) entityliving);
        }

        EnchantmentHelper.func_151385_b(entityliving, entity);
    }

    public void func_184178_b(EntityPlayerMP entityplayer) {}

    public void func_184203_c(EntityPlayerMP entityplayer) {}

    public float func_184229_a(Rotation enumblockrotation) {
        float f = MathHelper.func_76142_g(this.field_70177_z);

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

    public float func_184217_a(Mirror enumblockmirror) {
        float f = MathHelper.func_76142_g(this.field_70177_z);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return -f;

        case FRONT_BACK:
            return 180.0F - f;

        default:
            return f;
        }
    }

    public boolean func_184213_bq() {
        return false;
    }

    public boolean func_184189_br() {
        boolean flag = this.field_184237_aG;

        this.field_184237_aG = false;
        return flag;
    }

    @Nullable
    public Entity func_184179_bs() {
        return null;
    }

    public List<Entity> func_184188_bt() {
        return (List) (this.field_184244_h.isEmpty() ? Collections.emptyList() : Lists.newArrayList(this.field_184244_h));
    }

    public boolean func_184196_w(Entity entity) {
        Iterator iterator = this.func_184188_bt().iterator();

        Entity entity1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            entity1 = (Entity) iterator.next();
        } while (!entity1.equals(entity));

        return true;
    }

    public Collection<Entity> func_184182_bu() {
        HashSet hashset = Sets.newHashSet();

        this.func_184175_a(Entity.class, (Set) hashset);
        return hashset;
    }

    public <T extends Entity> Collection<T> func_184180_b(Class<T> oclass) {
        HashSet hashset = Sets.newHashSet();

        this.func_184175_a(oclass, (Set) hashset);
        return hashset;
    }

    private <T extends Entity> void func_184175_a(Class<T> oclass, Set<T> set) {
        Entity entity;

        for (Iterator iterator = this.func_184188_bt().iterator(); iterator.hasNext(); entity.func_184175_a(oclass, set)) {
            entity = (Entity) iterator.next();
            if (oclass.isAssignableFrom(entity.getClass())) {
                set.add((T) entity); // CraftBukkit - decompile error
            }
        }

    }

    public Entity func_184208_bv() {
        Entity entity;

        for (entity = this; entity.func_184218_aH(); entity = entity.func_184187_bx()) {
            ;
        }

        return entity;
    }

    public boolean func_184223_x(Entity entity) {
        return this.func_184208_bv() == entity.func_184208_bv();
    }

    public boolean func_184215_y(Entity entity) {
        Iterator iterator = this.func_184188_bt().iterator();

        Entity entity1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            entity1 = (Entity) iterator.next();
            if (entity1.equals(entity)) {
                return true;
            }
        } while (!entity1.func_184215_y(entity));

        return true;
    }

    public boolean func_184186_bw() {
        Entity entity = this.func_184179_bs();

        return entity instanceof EntityPlayer ? ((EntityPlayer) entity).func_175144_cb() : !this.field_70170_p.field_72995_K;
    }

    @Nullable
    public Entity func_184187_bx() {
        return this.field_184239_as;
    }

    public EnumPushReaction func_184192_z() {
        return EnumPushReaction.NORMAL;
    }

    public SoundCategory func_184176_by() {
        return SoundCategory.NEUTRAL;
    }

    public int func_190531_bD() {
        return 1;
    }
}
