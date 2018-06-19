package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.util.Vector;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.util.Vector;
// CraftBukkit end

public abstract class EntityMinecart extends Entity implements IWorldNameable {

    private static final DataParameter<Integer> field_184265_a = EntityDataManager.func_187226_a(EntityMinecart.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer> field_184266_b = EntityDataManager.func_187226_a(EntityMinecart.class, DataSerializers.field_187192_b);
    private static final DataParameter<Float> field_184267_c = EntityDataManager.func_187226_a(EntityMinecart.class, DataSerializers.field_187193_c);
    private static final DataParameter<Integer> field_184268_d = EntityDataManager.func_187226_a(EntityMinecart.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer> field_184269_e = EntityDataManager.func_187226_a(EntityMinecart.class, DataSerializers.field_187192_b);
    private static final DataParameter<Boolean> field_184270_f = EntityDataManager.func_187226_a(EntityMinecart.class, DataSerializers.field_187198_h);
    private boolean field_70499_f;
    private static final int[][][] field_70500_g = new int[][][] { { { 0, 0, -1}, { 0, 0, 1}}, { { -1, 0, 0}, { 1, 0, 0}}, { { -1, -1, 0}, { 1, 0, 0}}, { { -1, 0, 0}, { 1, -1, 0}}, { { 0, 0, -1}, { 0, -1, 1}}, { { 0, -1, -1}, { 0, 0, 1}}, { { 0, 0, 1}, { 1, 0, 0}}, { { 0, 0, 1}, { -1, 0, 0}}, { { 0, 0, -1}, { -1, 0, 0}}, { { 0, 0, -1}, { 1, 0, 0}}};
    private int field_70510_h;
    private double field_70511_i;
    private double field_70509_j;
    private double field_70514_an;
    private double field_70512_ao;
    private double field_70513_ap;

    // CraftBukkit start
    public boolean slowWhenEmpty = true;
    private double derailedX = 0.5;
    private double derailedY = 0.5;
    private double derailedZ = 0.5;
    private double flyingX = 0.95;
    private double flyingY = 0.95;
    private double flyingZ = 0.95;
    public double maxSpeed = 0.4D;
    // CraftBukkit end

    public EntityMinecart(World world) {
        super(world);
        this.field_70156_m = true;
        this.func_70105_a(0.98F, 0.7F);
    }

    public static EntityMinecart func_184263_a(World world, double d0, double d1, double d2, EntityMinecart.Type entityminecartabstract_enumminecarttype) {
        switch (entityminecartabstract_enumminecarttype) {
        case CHEST:
            return new EntityMinecartChest(world, d0, d1, d2);

        case FURNACE:
            return new EntityMinecartFurnace(world, d0, d1, d2);

        case TNT:
            return new EntityMinecartTNT(world, d0, d1, d2);

        case SPAWNER:
            return new EntityMinecartMobSpawner(world, d0, d1, d2);

        case HOPPER:
            return new EntityMinecartHopper(world, d0, d1, d2);

        case COMMAND_BLOCK:
            return new EntityMinecartCommandBlock(world, d0, d1, d2);

        default:
            return new EntityMinecartEmpty(world, d0, d1, d2);
        }
    }

    protected boolean func_70041_e_() {
        return false;
    }

    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(EntityMinecart.field_184265_a, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityMinecart.field_184266_b, Integer.valueOf(1));
        this.field_70180_af.func_187214_a(EntityMinecart.field_184267_c, Float.valueOf(0.0F));
        this.field_70180_af.func_187214_a(EntityMinecart.field_184268_d, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityMinecart.field_184269_e, Integer.valueOf(6));
        this.field_70180_af.func_187214_a(EntityMinecart.field_184270_f, Boolean.valueOf(false));
    }

    @Nullable
    public AxisAlignedBB func_70114_g(Entity entity) {
        return entity.func_70104_M() ? entity.func_174813_aQ() : null;
    }

    @Nullable
    public AxisAlignedBB func_70046_E() {
        return null;
    }

    public boolean func_70104_M() {
        return true;
    }

    public EntityMinecart(World world, double d0, double d1, double d2) {
        this(world);
        this.func_70107_b(d0, d1, d2);
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        this.field_70169_q = d0;
        this.field_70167_r = d1;
        this.field_70166_s = d2;
    }

    public double func_70042_X() {
        return 0.0D;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (!this.field_70170_p.field_72995_K && !this.field_70128_L) {
            if (this.func_180431_b(damagesource)) {
                return false;
            } else {
                // CraftBukkit start - fire VehicleDamageEvent
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity passenger = (damagesource.func_76346_g() == null) ? null : damagesource.func_76346_g().getBukkitEntity();

                VehicleDamageEvent event = new VehicleDamageEvent(vehicle, passenger, f);
                this.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }

                f = (float) event.getDamage();
                // CraftBukkit end
                this.func_70494_i(-this.func_70493_k());
                this.func_70497_h(10);
                this.func_70018_K();
                this.func_70492_c(this.func_70491_i() + f * 10.0F);
                boolean flag = damagesource.func_76346_g() instanceof EntityPlayer && ((EntityPlayer) damagesource.func_76346_g()).field_71075_bZ.field_75098_d;

                if (flag || this.func_70491_i() > 40.0F) {
                    // CraftBukkit start
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, passenger);
                    this.field_70170_p.getServer().getPluginManager().callEvent(destroyEvent);

                    if (destroyEvent.isCancelled()) {
                        this.func_70492_c(40); // Maximize damage so this doesn't get triggered again right away
                        return true;
                    }
                    // CraftBukkit end
                    this.func_184226_ay();
                    if (flag && !this.func_145818_k_()) {
                        this.func_70106_y();
                    } else {
                        this.func_94095_a(damagesource);
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    public void func_94095_a(DamageSource damagesource) {
        this.func_70106_y();
        if (this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
            ItemStack itemstack = new ItemStack(Items.field_151143_au, 1);

            if (this.func_145818_k_()) {
                itemstack.func_151001_c(this.func_95999_t());
            }

            this.func_70099_a(itemstack, 0.0F);
        }

    }

    public boolean func_70067_L() {
        return !this.field_70128_L;
    }

    public EnumFacing func_184172_bi() {
        return this.field_70499_f ? this.func_174811_aO().func_176734_d().func_176746_e() : this.func_174811_aO().func_176746_e();
    }

    public void func_70071_h_() {
        // CraftBukkit start
        double prevX = this.field_70165_t;
        double prevY = this.field_70163_u;
        double prevZ = this.field_70161_v;
        float prevYaw = this.field_70177_z;
        float prevPitch = this.field_70125_A;
        // CraftBukkit end

        if (this.func_70496_j() > 0) {
            this.func_70497_h(this.func_70496_j() - 1);
        }

        if (this.func_70491_i() > 0.0F) {
            this.func_70492_c(this.func_70491_i() - 1.0F);
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

        int i;

        // CraftBukkit - handled in postTick
        /*
        if (!this.world.isClientSide && this.world instanceof WorldServer) {
            this.world.methodProfiler.a("portal");
            MinecraftServer minecraftserver = this.world.getMinecraftServer();

            i = this.Z();
            if (this.ak) {
                if (minecraftserver.getAllowNether()) {
                    if (!this.isPassenger() && this.al++ >= i) {
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

            if (this.portalCooldown > 0) {
                --this.portalCooldown;
            }

            this.world.methodProfiler.b();
        }
        */

        if (this.field_70170_p.field_72995_K) {
            if (this.field_70510_h > 0) {
                double d0 = this.field_70165_t + (this.field_70511_i - this.field_70165_t) / (double) this.field_70510_h;
                double d1 = this.field_70163_u + (this.field_70509_j - this.field_70163_u) / (double) this.field_70510_h;
                double d2 = this.field_70161_v + (this.field_70514_an - this.field_70161_v) / (double) this.field_70510_h;
                double d3 = MathHelper.func_76138_g(this.field_70512_ao - (double) this.field_70177_z);

                this.field_70177_z = (float) ((double) this.field_70177_z + d3 / (double) this.field_70510_h);
                this.field_70125_A = (float) ((double) this.field_70125_A + (this.field_70513_ap - (double) this.field_70125_A) / (double) this.field_70510_h);
                --this.field_70510_h;
                this.func_70107_b(d0, d1, d2);
                this.func_70101_b(this.field_70177_z, this.field_70125_A);
            } else {
                this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
                this.func_70101_b(this.field_70177_z, this.field_70125_A);
            }

        } else {
            this.field_70169_q = this.field_70165_t;
            this.field_70167_r = this.field_70163_u;
            this.field_70166_s = this.field_70161_v;
            if (!this.func_189652_ae()) {
                this.field_70181_x -= 0.03999999910593033D;
            }

            int j = MathHelper.func_76128_c(this.field_70165_t);

            i = MathHelper.func_76128_c(this.field_70163_u);
            int k = MathHelper.func_76128_c(this.field_70161_v);

            if (BlockRailBase.func_176562_d(this.field_70170_p, new BlockPos(j, i - 1, k))) {
                --i;
            }

            BlockPos blockposition = new BlockPos(j, i, k);
            IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);

            if (BlockRailBase.func_176563_d(iblockdata)) {
                this.func_180460_a(blockposition, iblockdata);
                if (iblockdata.func_177230_c() == Blocks.field_150408_cc) {
                    this.func_96095_a(j, i, k, ((Boolean) iblockdata.func_177229_b(BlockRailPowered.field_176569_M)).booleanValue());
                }
            } else {
                this.func_180459_n();
            }

            this.func_145775_I();
            this.field_70125_A = 0.0F;
            double d4 = this.field_70169_q - this.field_70165_t;
            double d5 = this.field_70166_s - this.field_70161_v;

            if (d4 * d4 + d5 * d5 > 0.001D) {
                this.field_70177_z = (float) (MathHelper.func_181159_b(d5, d4) * 180.0D / 3.141592653589793D);
                if (this.field_70499_f) {
                    this.field_70177_z += 180.0F;
                }
            }

            double d6 = (double) MathHelper.func_76142_g(this.field_70177_z - this.field_70126_B);

            if (d6 < -170.0D || d6 >= 170.0D) {
                this.field_70177_z += 180.0F;
                this.field_70499_f = !this.field_70499_f;
            }

            this.func_70101_b(this.field_70177_z, this.field_70125_A);
            // CraftBukkit start
            org.bukkit.World bworld = this.field_70170_p.getWorld();
            Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
            Location to = new Location(bworld, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, this.field_70125_A);
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();

            this.field_70170_p.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));

            if (!from.equals(to)) {
                this.field_70170_p.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleMoveEvent(vehicle, from, to));
            }
            // CraftBukkit end
            if (this.func_184264_v() == EntityMinecart.Type.RIDEABLE && this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 0.01D) {
                List list = this.field_70170_p.func_175674_a(this, this.func_174813_aQ().func_72314_b(0.20000000298023224D, 0.0D, 0.20000000298023224D), EntitySelectors.func_188442_a(this));

                if (!list.isEmpty()) {
                    for (int l = 0; l < list.size(); ++l) {
                        Entity entity = (Entity) list.get(l);

                        if (!(entity instanceof EntityPlayer) && !(entity instanceof EntityIronGolem) && !(entity instanceof EntityMinecart) && !this.func_184207_aI() && !entity.func_184218_aH()) {
                            // CraftBukkit start
                            VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent(vehicle, entity.getBukkitEntity());
                            this.field_70170_p.getServer().getPluginManager().callEvent(collisionEvent);

                            if (collisionEvent.isCancelled()) {
                                continue;
                            }
                            // CraftBukkit end
                            entity.func_184220_m(this);
                        } else {
                            // CraftBukkit start
                            VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent(vehicle, entity.getBukkitEntity());
                            this.field_70170_p.getServer().getPluginManager().callEvent(collisionEvent);

                            if (collisionEvent.isCancelled()) {
                                continue;
                            }
                            // CraftBukkit end
                            entity.func_70108_f(this);
                        }
                    }
                }
            } else {
                Iterator iterator = this.field_70170_p.func_72839_b(this, this.func_174813_aQ().func_72314_b(0.20000000298023224D, 0.0D, 0.20000000298023224D)).iterator();

                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    if (!this.func_184196_w(entity1) && entity1.func_70104_M() && entity1 instanceof EntityMinecart) {
                        // CraftBukkit start
                        VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent(vehicle, entity1.getBukkitEntity());
                        this.field_70170_p.getServer().getPluginManager().callEvent(collisionEvent);

                        if (collisionEvent.isCancelled()) {
                            continue;
                        }
                        // CraftBukkit end
                        entity1.func_70108_f(this);
                    }
                }
            }

            this.func_70072_I();
        }
    }

    protected double func_174898_m() {
        return this.maxSpeed; // CraftBukkit
    }

    public void func_96095_a(int i, int j, int k, boolean flag) {}

    protected void func_180459_n() {
        double d0 = this.func_174898_m();

        this.field_70159_w = MathHelper.func_151237_a(this.field_70159_w, -d0, d0);
        this.field_70179_y = MathHelper.func_151237_a(this.field_70179_y, -d0, d0);
        if (this.field_70122_E) {
            // CraftBukkit start - replace magic numbers with our variables
            this.field_70159_w *= this.derailedX;
            this.field_70181_x *= this.derailedY;
            this.field_70179_y *= this.derailedZ;
            // CraftBukkit end
        }

        this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        if (!this.field_70122_E) {
            // CraftBukkit start - replace magic numbers with our variables
            this.field_70159_w *= this.flyingX;
            this.field_70181_x *= this.flyingY;
            this.field_70179_y *= this.flyingZ;
            // CraftBukkit end
        }

    }

    protected void func_180460_a(BlockPos blockposition, IBlockState iblockdata) {
        this.field_70143_R = 0.0F;
        Vec3d vec3d = this.func_70489_a(this.field_70165_t, this.field_70163_u, this.field_70161_v);

        this.field_70163_u = (double) blockposition.func_177956_o();
        boolean flag = false;
        boolean flag1 = false;
        BlockRailBase blockminecarttrackabstract = (BlockRailBase) iblockdata.func_177230_c();

        if (blockminecarttrackabstract == Blocks.field_150318_D) {
            flag = ((Boolean) iblockdata.func_177229_b(BlockRailPowered.field_176569_M)).booleanValue();
            flag1 = !flag;
        }

        double d0 = 0.0078125D;
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(blockminecarttrackabstract.func_176560_l());

        switch (blockminecarttrackabstract_enumtrackposition) {
        case ASCENDING_EAST:
            this.field_70159_w -= 0.0078125D;
            ++this.field_70163_u;
            break;

        case ASCENDING_WEST:
            this.field_70159_w += 0.0078125D;
            ++this.field_70163_u;
            break;

        case ASCENDING_NORTH:
            this.field_70179_y += 0.0078125D;
            ++this.field_70163_u;
            break;

        case ASCENDING_SOUTH:
            this.field_70179_y -= 0.0078125D;
            ++this.field_70163_u;
        }

        int[][] aint = EntityMinecart.field_70500_g[blockminecarttrackabstract_enumtrackposition.func_177015_a()];
        double d1 = (double) (aint[1][0] - aint[0][0]);
        double d2 = (double) (aint[1][2] - aint[0][2]);
        double d3 = Math.sqrt(d1 * d1 + d2 * d2);
        double d4 = this.field_70159_w * d1 + this.field_70179_y * d2;

        if (d4 < 0.0D) {
            d1 = -d1;
            d2 = -d2;
        }

        double d5 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

        if (d5 > 2.0D) {
            d5 = 2.0D;
        }

        this.field_70159_w = d5 * d1 / d3;
        this.field_70179_y = d5 * d2 / d3;
        Entity entity = this.func_184188_bt().isEmpty() ? null : (Entity) this.func_184188_bt().get(0);
        double d6;
        double d7;
        double d8;
        double d9;

        if (entity instanceof EntityLivingBase) {
            d6 = (double) ((EntityLivingBase) entity).field_191988_bg;
            if (d6 > 0.0D) {
                d7 = -Math.sin((double) (entity.field_70177_z * 0.017453292F));
                d8 = Math.cos((double) (entity.field_70177_z * 0.017453292F));
                d9 = this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y;
                if (d9 < 0.01D) {
                    this.field_70159_w += d7 * 0.1D;
                    this.field_70179_y += d8 * 0.1D;
                    flag1 = false;
                }
            }
        }

        if (flag1) {
            d6 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            if (d6 < 0.03D) {
                this.field_70159_w *= 0.0D;
                this.field_70181_x *= 0.0D;
                this.field_70179_y *= 0.0D;
            } else {
                this.field_70159_w *= 0.5D;
                this.field_70181_x *= 0.0D;
                this.field_70179_y *= 0.5D;
            }
        }

        d6 = (double) blockposition.func_177958_n() + 0.5D + (double) aint[0][0] * 0.5D;
        d7 = (double) blockposition.func_177952_p() + 0.5D + (double) aint[0][2] * 0.5D;
        d8 = (double) blockposition.func_177958_n() + 0.5D + (double) aint[1][0] * 0.5D;
        d9 = (double) blockposition.func_177952_p() + 0.5D + (double) aint[1][2] * 0.5D;
        d1 = d8 - d6;
        d2 = d9 - d7;
        double d10;
        double d11;
        double d12;

        if (d1 == 0.0D) {
            this.field_70165_t = (double) blockposition.func_177958_n() + 0.5D;
            d10 = this.field_70161_v - (double) blockposition.func_177952_p();
        } else if (d2 == 0.0D) {
            this.field_70161_v = (double) blockposition.func_177952_p() + 0.5D;
            d10 = this.field_70165_t - (double) blockposition.func_177958_n();
        } else {
            d11 = this.field_70165_t - d6;
            d12 = this.field_70161_v - d7;
            d10 = (d11 * d1 + d12 * d2) * 2.0D;
        }

        this.field_70165_t = d6 + d1 * d10;
        this.field_70161_v = d7 + d2 * d10;
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        d11 = this.field_70159_w;
        d12 = this.field_70179_y;
        if (this.func_184207_aI()) {
            d11 *= 0.75D;
            d12 *= 0.75D;
        }

        double d13 = this.func_174898_m();

        d11 = MathHelper.func_151237_a(d11, -d13, d13);
        d12 = MathHelper.func_151237_a(d12, -d13, d13);
        this.func_70091_d(MoverType.SELF, d11, 0.0D, d12);
        if (aint[0][1] != 0 && MathHelper.func_76128_c(this.field_70165_t) - blockposition.func_177958_n() == aint[0][0] && MathHelper.func_76128_c(this.field_70161_v) - blockposition.func_177952_p() == aint[0][2]) {
            this.func_70107_b(this.field_70165_t, this.field_70163_u + (double) aint[0][1], this.field_70161_v);
        } else if (aint[1][1] != 0 && MathHelper.func_76128_c(this.field_70165_t) - blockposition.func_177958_n() == aint[1][0] && MathHelper.func_76128_c(this.field_70161_v) - blockposition.func_177952_p() == aint[1][2]) {
            this.func_70107_b(this.field_70165_t, this.field_70163_u + (double) aint[1][1], this.field_70161_v);
        }

        this.func_94101_h();
        Vec3d vec3d1 = this.func_70489_a(this.field_70165_t, this.field_70163_u, this.field_70161_v);

        if (vec3d1 != null && vec3d != null) {
            double d14 = (vec3d.field_72448_b - vec3d1.field_72448_b) * 0.05D;

            d5 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            if (d5 > 0.0D) {
                this.field_70159_w = this.field_70159_w / d5 * (d5 + d14);
                this.field_70179_y = this.field_70179_y / d5 * (d5 + d14);
            }

            this.func_70107_b(this.field_70165_t, vec3d1.field_72448_b, this.field_70161_v);
        }

        int i = MathHelper.func_76128_c(this.field_70165_t);
        int j = MathHelper.func_76128_c(this.field_70161_v);

        if (i != blockposition.func_177958_n() || j != blockposition.func_177952_p()) {
            d5 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            this.field_70159_w = d5 * (double) (i - blockposition.func_177958_n());
            this.field_70179_y = d5 * (double) (j - blockposition.func_177952_p());
        }

        if (flag) {
            double d15 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

            if (d15 > 0.01D) {
                double d16 = 0.06D;

                this.field_70159_w += this.field_70159_w / d15 * 0.06D;
                this.field_70179_y += this.field_70179_y / d15 * 0.06D;
            } else if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (this.field_70170_p.func_180495_p(blockposition.func_177976_e()).func_185915_l()) {
                    this.field_70159_w = 0.02D;
                } else if (this.field_70170_p.func_180495_p(blockposition.func_177974_f()).func_185915_l()) {
                    this.field_70159_w = -0.02D;
                }
            } else if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (this.field_70170_p.func_180495_p(blockposition.func_177978_c()).func_185915_l()) {
                    this.field_70179_y = 0.02D;
                } else if (this.field_70170_p.func_180495_p(blockposition.func_177968_d()).func_185915_l()) {
                    this.field_70179_y = -0.02D;
                }
            }
        }

    }

    protected void func_94101_h() {
        if (this.func_184207_aI() || !this.slowWhenEmpty) { // CraftBukkit - add !this.slowWhenEmpty
            this.field_70159_w *= 0.996999979019165D;
            this.field_70181_x *= 0.0D;
            this.field_70179_y *= 0.996999979019165D;
        } else {
            this.field_70159_w *= 0.9599999785423279D;
            this.field_70181_x *= 0.0D;
            this.field_70179_y *= 0.9599999785423279D;
        }

    }

    public void func_70107_b(double d0, double d1, double d2) {
        this.field_70165_t = d0;
        this.field_70163_u = d1;
        this.field_70161_v = d2;
        float f = this.field_70130_N / 2.0F;
        float f1 = this.field_70131_O;

        this.func_174826_a(new AxisAlignedBB(d0 - (double) f, d1, d2 - (double) f, d0 + (double) f, d1 + (double) f1, d2 + (double) f));
    }

    @Nullable
    public Vec3d func_70489_a(double d0, double d1, double d2) {
        int i = MathHelper.func_76128_c(d0);
        int j = MathHelper.func_76128_c(d1);
        int k = MathHelper.func_76128_c(d2);

        if (BlockRailBase.func_176562_d(this.field_70170_p, new BlockPos(i, j - 1, k))) {
            --j;
        }

        IBlockState iblockdata = this.field_70170_p.func_180495_p(new BlockPos(i, j, k));

        if (BlockRailBase.func_176563_d(iblockdata)) {
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(((BlockRailBase) iblockdata.func_177230_c()).func_176560_l());
            int[][] aint = EntityMinecart.field_70500_g[blockminecarttrackabstract_enumtrackposition.func_177015_a()];
            double d3 = (double) i + 0.5D + (double) aint[0][0] * 0.5D;
            double d4 = (double) j + 0.0625D + (double) aint[0][1] * 0.5D;
            double d5 = (double) k + 0.5D + (double) aint[0][2] * 0.5D;
            double d6 = (double) i + 0.5D + (double) aint[1][0] * 0.5D;
            double d7 = (double) j + 0.0625D + (double) aint[1][1] * 0.5D;
            double d8 = (double) k + 0.5D + (double) aint[1][2] * 0.5D;
            double d9 = d6 - d3;
            double d10 = (d7 - d4) * 2.0D;
            double d11 = d8 - d5;
            double d12;

            if (d9 == 0.0D) {
                d12 = d2 - (double) k;
            } else if (d11 == 0.0D) {
                d12 = d0 - (double) i;
            } else {
                double d13 = d0 - d3;
                double d14 = d2 - d5;

                d12 = (d13 * d9 + d14 * d11) * 2.0D;
            }

            d0 = d3 + d9 * d12;
            d1 = d4 + d10 * d12;
            d2 = d5 + d11 * d12;
            if (d10 < 0.0D) {
                ++d1;
            }

            if (d10 > 0.0D) {
                d1 += 0.5D;
            }

            return new Vec3d(d0, d1, d2);
        } else {
            return null;
        }
    }

    public static void func_189669_a(DataFixer dataconvertermanager, Class<?> oclass) {}

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_74767_n("CustomDisplayTile")) {
            Block block;

            if (nbttagcompound.func_150297_b("DisplayTile", 8)) {
                block = Block.func_149684_b(nbttagcompound.func_74779_i("DisplayTile"));
            } else {
                block = Block.func_149729_e(nbttagcompound.func_74762_e("DisplayTile"));
            }

            int i = nbttagcompound.func_74762_e("DisplayData");

            this.func_174899_a(block == null ? Blocks.field_150350_a.func_176223_P() : block.func_176203_a(i));
            this.func_94086_l(nbttagcompound.func_74762_e("DisplayOffset"));
        }

    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        if (this.func_94100_s()) {
            nbttagcompound.func_74757_a("CustomDisplayTile", true);
            IBlockState iblockdata = this.func_174897_t();
            ResourceLocation minecraftkey = (ResourceLocation) Block.field_149771_c.func_177774_c(iblockdata.func_177230_c());

            nbttagcompound.func_74778_a("DisplayTile", minecraftkey == null ? "" : minecraftkey.toString());
            nbttagcompound.func_74768_a("DisplayData", iblockdata.func_177230_c().func_176201_c(iblockdata));
            nbttagcompound.func_74768_a("DisplayOffset", this.func_94099_q());
        }

    }

    public void func_70108_f(Entity entity) {
        if (!this.field_70170_p.field_72995_K) {
            if (!entity.field_70145_X && !this.field_70145_X) {
                if (!this.func_184196_w(entity)) {
                    // CraftBukkit start
                    VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent((Vehicle) this.getBukkitEntity(), entity.getBukkitEntity());
                    this.field_70170_p.getServer().getPluginManager().callEvent(collisionEvent);

                    if (collisionEvent.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                    double d0 = entity.field_70165_t - this.field_70165_t;
                    double d1 = entity.field_70161_v - this.field_70161_v;
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 >= 9.999999747378752E-5D) {
                        d2 = (double) MathHelper.func_76133_a(d2);
                        d0 /= d2;
                        d1 /= d2;
                        double d3 = 1.0D / d2;

                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 *= d3;
                        d1 *= d3;
                        d0 *= 0.10000000149011612D;
                        d1 *= 0.10000000149011612D;
                        d0 *= (double) (1.0F - this.field_70144_Y);
                        d1 *= (double) (1.0F - this.field_70144_Y);
                        d0 *= 0.5D;
                        d1 *= 0.5D;
                        if (entity instanceof EntityMinecart) {
                            double d4 = entity.field_70165_t - this.field_70165_t;
                            double d5 = entity.field_70161_v - this.field_70161_v;
                            Vec3d vec3d = (new Vec3d(d4, 0.0D, d5)).func_72432_b();
                            Vec3d vec3d1 = (new Vec3d((double) MathHelper.func_76134_b(this.field_70177_z * 0.017453292F), 0.0D, (double) MathHelper.func_76126_a(this.field_70177_z * 0.017453292F))).func_72432_b();
                            double d6 = Math.abs(vec3d.func_72430_b(vec3d1));

                            if (d6 < 0.800000011920929D) {
                                return;
                            }

                            double d7 = entity.field_70159_w + this.field_70159_w;
                            double d8 = entity.field_70179_y + this.field_70179_y;

                            if (((EntityMinecart) entity).func_184264_v() == EntityMinecart.Type.FURNACE && this.func_184264_v() != EntityMinecart.Type.FURNACE) {
                                this.field_70159_w *= 0.20000000298023224D;
                                this.field_70179_y *= 0.20000000298023224D;
                                this.func_70024_g(entity.field_70159_w - d0, 0.0D, entity.field_70179_y - d1);
                                entity.field_70159_w *= 0.949999988079071D;
                                entity.field_70179_y *= 0.949999988079071D;
                            } else if (((EntityMinecart) entity).func_184264_v() != EntityMinecart.Type.FURNACE && this.func_184264_v() == EntityMinecart.Type.FURNACE) {
                                entity.field_70159_w *= 0.20000000298023224D;
                                entity.field_70179_y *= 0.20000000298023224D;
                                entity.func_70024_g(this.field_70159_w + d0, 0.0D, this.field_70179_y + d1);
                                this.field_70159_w *= 0.949999988079071D;
                                this.field_70179_y *= 0.949999988079071D;
                            } else {
                                d7 /= 2.0D;
                                d8 /= 2.0D;
                                this.field_70159_w *= 0.20000000298023224D;
                                this.field_70179_y *= 0.20000000298023224D;
                                this.func_70024_g(d7 - d0, 0.0D, d8 - d1);
                                entity.field_70159_w *= 0.20000000298023224D;
                                entity.field_70179_y *= 0.20000000298023224D;
                                entity.func_70024_g(d7 + d0, 0.0D, d8 + d1);
                            }
                        } else {
                            this.func_70024_g(-d0, 0.0D, -d1);
                            entity.func_70024_g(d0 / 4.0D, 0.0D, d1 / 4.0D);
                        }
                    }

                }
            }
        }
    }

    public void func_70492_c(float f) {
        this.field_70180_af.func_187227_b(EntityMinecart.field_184267_c, Float.valueOf(f));
    }

    public float func_70491_i() {
        return ((Float) this.field_70180_af.func_187225_a(EntityMinecart.field_184267_c)).floatValue();
    }

    public void func_70497_h(int i) {
        this.field_70180_af.func_187227_b(EntityMinecart.field_184265_a, Integer.valueOf(i));
    }

    public int func_70496_j() {
        return ((Integer) this.field_70180_af.func_187225_a(EntityMinecart.field_184265_a)).intValue();
    }

    public void func_70494_i(int i) {
        this.field_70180_af.func_187227_b(EntityMinecart.field_184266_b, Integer.valueOf(i));
    }

    public int func_70493_k() {
        return ((Integer) this.field_70180_af.func_187225_a(EntityMinecart.field_184266_b)).intValue();
    }

    public abstract EntityMinecart.Type func_184264_v();

    public IBlockState func_174897_t() {
        return !this.func_94100_s() ? this.func_180457_u() : Block.func_176220_d(((Integer) this.func_184212_Q().func_187225_a(EntityMinecart.field_184268_d)).intValue());
    }

    public IBlockState func_180457_u() {
        return Blocks.field_150350_a.func_176223_P();
    }

    public int func_94099_q() {
        return !this.func_94100_s() ? this.func_94085_r() : ((Integer) this.func_184212_Q().func_187225_a(EntityMinecart.field_184269_e)).intValue();
    }

    public int func_94085_r() {
        return 6;
    }

    public void func_174899_a(IBlockState iblockdata) {
        this.func_184212_Q().func_187227_b(EntityMinecart.field_184268_d, Integer.valueOf(Block.func_176210_f(iblockdata)));
        this.func_94096_e(true);
    }

    public void func_94086_l(int i) {
        this.func_184212_Q().func_187227_b(EntityMinecart.field_184269_e, Integer.valueOf(i));
        this.func_94096_e(true);
    }

    public boolean func_94100_s() {
        return ((Boolean) this.func_184212_Q().func_187225_a(EntityMinecart.field_184270_f)).booleanValue();
    }

    public void func_94096_e(boolean flag) {
        this.func_184212_Q().func_187227_b(EntityMinecart.field_184270_f, Boolean.valueOf(flag));
    }

    public static enum Type {

        RIDEABLE(0, "MinecartRideable"), CHEST(1, "MinecartChest"), FURNACE(2, "MinecartFurnace"), TNT(3, "MinecartTNT"), SPAWNER(4, "MinecartSpawner"), HOPPER(5, "MinecartHopper"), COMMAND_BLOCK(6, "MinecartCommandBlock");

        private static final Map<Integer, EntityMinecart.Type> field_184965_h = Maps.newHashMap();
        private final int field_184966_i;
        private final String field_184967_j;

        private Type(int i, String s) {
            this.field_184966_i = i;
            this.field_184967_j = s;
        }

        public int func_184956_a() {
            return this.field_184966_i;
        }

        public String func_184954_b() {
            return this.field_184967_j;
        }

        static {
            EntityMinecart.Type[] aentityminecartabstract_enumminecarttype = values();
            int i = aentityminecartabstract_enumminecarttype.length;

            for (int j = 0; j < i; ++j) {
                EntityMinecart.Type entityminecartabstract_enumminecarttype = aentityminecartabstract_enumminecarttype[j];

                EntityMinecart.Type.field_184965_h.put(Integer.valueOf(entityminecartabstract_enumminecarttype.func_184956_a()), entityminecartabstract_enumminecarttype);
            }

        }
    }

    // CraftBukkit start - Methods for getting and setting flying and derailed velocity modifiers
    public Vector getFlyingVelocityMod() {
        return new Vector(flyingX, flyingY, flyingZ);
    }

    public void setFlyingVelocityMod(Vector flying) {
        flyingX = flying.getX();
        flyingY = flying.getY();
        flyingZ = flying.getZ();
    }

    public Vector getDerailedVelocityMod() {
        return new Vector(derailedX, derailedY, derailedZ);
    }

    public void setDerailedVelocityMod(Vector derailed) {
        derailedX = derailed.getX();
        derailedY = derailed.getY();
        derailedZ = derailed.getZ();
    }
    // CraftBukkit end
}
