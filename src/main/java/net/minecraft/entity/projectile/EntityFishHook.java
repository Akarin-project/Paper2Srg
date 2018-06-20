package net.minecraft.entity.projectile;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.entity.Fish;
import org.bukkit.event.player.PlayerFishEvent;
// CraftBukkit end

public class EntityFishHook extends Entity {

    private static final DataParameter<Integer> field_184528_c = EntityDataManager.func_187226_a(EntityFishHook.class, DataSerializers.field_187192_b);
    private boolean field_146051_au;
    private int field_146049_av;
    public EntityPlayer field_146042_b;
    private int field_146047_aw;
    private int field_146045_ax;
    private int field_146040_ay;
    private int field_146038_az;
    private float field_146054_aA;
    public Entity field_146043_c;
    private EntityFishHook.State field_190627_av;
    private int field_191518_aw;
    private int field_191519_ax;

    public EntityFishHook(World world, EntityPlayer entityhuman) {
        super(world);
        this.field_190627_av = EntityFishHook.State.FLYING;
        this.func_190626_a(entityhuman);
        this.func_190620_n();
    }

    private void func_190626_a(EntityPlayer entityhuman) {
        this.func_70105_a(0.25F, 0.25F);
        this.field_70158_ak = true;
        this.field_146042_b = entityhuman;
        this.field_146042_b.field_71104_cf = this;
    }

    public void func_191516_a(int i) {
        this.field_191519_ax = i;
    }

    public void func_191517_b(int i) {
        this.field_191518_aw = i;
    }

    private void func_190620_n() {
        float f = this.field_146042_b.field_70127_C + (this.field_146042_b.field_70125_A - this.field_146042_b.field_70127_C);
        float f1 = this.field_146042_b.field_70126_B + (this.field_146042_b.field_70177_z - this.field_146042_b.field_70126_B);
        float f2 = MathHelper.func_76134_b(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.func_76126_a(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.func_76134_b(-f * 0.017453292F);
        float f5 = MathHelper.func_76126_a(-f * 0.017453292F);
        double d0 = this.field_146042_b.field_70169_q + (this.field_146042_b.field_70165_t - this.field_146042_b.field_70169_q) - (double) f3 * 0.3D;
        double d1 = this.field_146042_b.field_70167_r + (this.field_146042_b.field_70163_u - this.field_146042_b.field_70167_r) + (double) this.field_146042_b.func_70047_e();
        double d2 = this.field_146042_b.field_70166_s + (this.field_146042_b.field_70161_v - this.field_146042_b.field_70166_s) - (double) f2 * 0.3D;

        this.func_70012_b(d0, d1, d2, f1, f);
        this.field_70159_w = (double) (-f3);
        this.field_70181_x = (double) MathHelper.func_76131_a(-(f5 / f4), -5.0F, 5.0F);
        this.field_70179_y = (double) (-f2);
        float f6 = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);

        this.field_70159_w *= 0.6D / (double) f6 + 0.5D + this.field_70146_Z.nextGaussian() * 0.0045D;
        this.field_70181_x *= 0.6D / (double) f6 + 0.5D + this.field_70146_Z.nextGaussian() * 0.0045D;
        this.field_70179_y *= 0.6D / (double) f6 + 0.5D + this.field_70146_Z.nextGaussian() * 0.0045D;
        float f7 = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

        this.field_70177_z = (float) (MathHelper.func_181159_b(this.field_70159_w, this.field_70179_y) * 57.2957763671875D);
        this.field_70125_A = (float) (MathHelper.func_181159_b(this.field_70181_x, (double) f7) * 57.2957763671875D);
        this.field_70126_B = this.field_70177_z;
        this.field_70127_C = this.field_70125_A;
    }

    protected void func_70088_a() {
        this.func_184212_Q().func_187214_a(EntityFishHook.field_184528_c, Integer.valueOf(0));
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityFishHook.field_184528_c.equals(datawatcherobject)) {
            int i = ((Integer) this.func_184212_Q().func_187225_a(EntityFishHook.field_184528_c)).intValue();

            this.field_146043_c = i > 0 ? this.field_70170_p.func_73045_a(i - 1) : null;
        }

        super.func_184206_a(datawatcherobject);
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_146042_b == null) {
            this.func_70106_y();
        } else if (this.field_70170_p.field_72995_K || !this.func_190625_o()) {
            if (this.field_146051_au) {
                ++this.field_146049_av;
                if (this.field_146049_av >= 1200) {
                    this.func_70106_y();
                    return;
                }
            }

            float f = 0.0F;
            BlockPos blockposition = new BlockPos(this);
            IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);

            if (iblockdata.func_185904_a() == Material.field_151586_h) {
                f = BlockLiquid.func_190973_f(iblockdata, this.field_70170_p, blockposition);
            }

            double d0;

            if (this.field_190627_av == EntityFishHook.State.FLYING) {
                if (this.field_146043_c != null) {
                    this.field_70159_w = 0.0D;
                    this.field_70181_x = 0.0D;
                    this.field_70179_y = 0.0D;
                    this.field_190627_av = EntityFishHook.State.HOOKED_IN_ENTITY;
                    return;
                }

                if (f > 0.0F) {
                    this.field_70159_w *= 0.3D;
                    this.field_70181_x *= 0.2D;
                    this.field_70179_y *= 0.3D;
                    this.field_190627_av = EntityFishHook.State.BOBBING;
                    return;
                }

                if (!this.field_70170_p.field_72995_K) {
                    this.func_190624_r();
                }

                if (!this.field_146051_au && !this.field_70122_E && !this.field_70123_F) {
                    ++this.field_146047_aw;
                } else {
                    this.field_146047_aw = 0;
                    this.field_70159_w = 0.0D;
                    this.field_70181_x = 0.0D;
                    this.field_70179_y = 0.0D;
                }
            } else {
                if (this.field_190627_av == EntityFishHook.State.HOOKED_IN_ENTITY) {
                    if (this.field_146043_c != null) {
                        if (this.field_146043_c.field_70128_L) {
                            this.field_146043_c = null;
                            this.field_190627_av = EntityFishHook.State.FLYING;
                        } else {
                            this.field_70165_t = this.field_146043_c.field_70165_t;
                            double d1 = (double) this.field_146043_c.field_70131_O;

                            this.field_70163_u = this.field_146043_c.func_174813_aQ().field_72338_b + d1 * 0.8D;
                            this.field_70161_v = this.field_146043_c.field_70161_v;
                            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
                        }
                    }

                    return;
                }

                if (this.field_190627_av == EntityFishHook.State.BOBBING) {
                    this.field_70159_w *= 0.9D;
                    this.field_70179_y *= 0.9D;
                    d0 = this.field_70163_u + this.field_70181_x - (double) blockposition.func_177956_o() - (double) f;
                    if (Math.abs(d0) < 0.01D) {
                        d0 += Math.signum(d0) * 0.1D;
                    }

                    this.field_70181_x -= d0 * (double) this.field_70146_Z.nextFloat() * 0.2D;
                    if (!this.field_70170_p.field_72995_K && f > 0.0F) {
                        this.func_190621_a(blockposition);
                    }
                }
            }

            if (iblockdata.func_185904_a() != Material.field_151586_h) {
                this.field_70181_x -= 0.03D;
            }

            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.func_190623_q();
            d0 = 0.92D;
            this.field_70159_w *= 0.92D;
            this.field_70181_x *= 0.92D;
            this.field_70179_y *= 0.92D;
            this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);

            // Paper start - These shouldn't be going through portals
            if (this.inPortal()) {
                this.func_70106_y();
            }
            // Paper end
        }
    }

    private boolean func_190625_o() {
        ItemStack itemstack = this.field_146042_b.func_184614_ca();
        ItemStack itemstack1 = this.field_146042_b.func_184592_cb();
        boolean flag = itemstack.func_77973_b() == Items.field_151112_aM;
        boolean flag1 = itemstack1.func_77973_b() == Items.field_151112_aM;

        if (!this.field_146042_b.field_70128_L && this.field_146042_b.func_70089_S() && (flag || flag1) && this.func_70068_e(this.field_146042_b) <= 1024.0D) {
            return false;
        } else {
            this.func_70106_y();
            return true;
        }
    }

    private void func_190623_q() {
        float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

        this.field_70177_z = (float) (MathHelper.func_181159_b(this.field_70159_w, this.field_70179_y) * 57.2957763671875D);

        for (this.field_70125_A = (float) (MathHelper.func_181159_b(this.field_70181_x, (double) f) * 57.2957763671875D); this.field_70125_A - this.field_70127_C < -180.0F; this.field_70127_C -= 360.0F) {
            ;
        }

        while (this.field_70125_A - this.field_70127_C >= 180.0F) {
            this.field_70127_C += 360.0F;
        }

        while (this.field_70177_z - this.field_70126_B < -180.0F) {
            this.field_70126_B -= 360.0F;
        }

        while (this.field_70177_z - this.field_70126_B >= 180.0F) {
            this.field_70126_B += 360.0F;
        }

        this.field_70125_A = this.field_70127_C + (this.field_70125_A - this.field_70127_C) * 0.2F;
        this.field_70177_z = this.field_70126_B + (this.field_70177_z - this.field_70126_B) * 0.2F;
    }

    private void func_190624_r() {
        Vec3d vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        Vec3d vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
        RayTraceResult movingobjectposition = this.field_70170_p.func_147447_a(vec3d, vec3d1, false, true, false);

        vec3d = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        vec3d1 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);

        // Paper start - Call ProjectileCollideEvent
        if (movingobjectposition != null && movingobjectposition.field_72308_g != null) {
            com.destroystokyo.paper.event.entity.ProjectileCollideEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileCollideEvent(this, movingobjectposition);
            if (event.isCancelled()) {
                movingobjectposition = null;
            }
        }
        // Paper end

        if (movingobjectposition != null) {
            vec3d1 = new Vec3d(movingobjectposition.field_72307_f.field_72450_a, movingobjectposition.field_72307_f.field_72448_b, movingobjectposition.field_72307_f.field_72449_c);
        }

        Entity entity = null;
        List list = this.field_70170_p.func_72839_b(this, this.func_174813_aQ().func_72321_a(this.field_70159_w, this.field_70181_x, this.field_70179_y).func_186662_g(1.0D));
        double d0 = 0.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity1 = (Entity) iterator.next();

            if (this.func_189739_a(entity1) && (entity1 != this.field_146042_b || this.field_146047_aw >= 5)) {
                AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_186662_g(0.30000001192092896D);
                RayTraceResult movingobjectposition1 = axisalignedbb.func_72327_a(vec3d, vec3d1);

                if (movingobjectposition1 != null) {
                    double d1 = vec3d.func_72436_e(movingobjectposition1.field_72307_f);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null) {
            movingobjectposition = new RayTraceResult(entity);
        }

        if (movingobjectposition != null && movingobjectposition.field_72313_a != RayTraceResult.Type.MISS) {
            org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition); // Craftbukkit - Call event
            if (movingobjectposition.field_72313_a == RayTraceResult.Type.ENTITY) {
                this.field_146043_c = movingobjectposition.field_72308_g;
                this.func_190622_s();
            } else {
                this.field_146051_au = true;
            }
        }

    }

    private void func_190622_s() {
        this.func_184212_Q().func_187227_b(EntityFishHook.field_184528_c, Integer.valueOf(this.field_146043_c.func_145782_y() + 1));
    }

    private void func_190621_a(BlockPos blockposition) {
        WorldServer worldserver = (WorldServer) this.field_70170_p;
        int i = 1;
        BlockPos blockposition1 = blockposition.func_177984_a();

        if (this.field_70146_Z.nextFloat() < 0.25F && this.field_70170_p.func_175727_C(blockposition1)) {
            ++i;
        }

        if (this.field_70146_Z.nextFloat() < 0.5F && !this.field_70170_p.func_175678_i(blockposition1)) {
            --i;
        }

        if (this.field_146045_ax > 0) {
            --this.field_146045_ax;
            if (this.field_146045_ax <= 0) {
                this.field_146040_ay = 0;
                this.field_146038_az = 0;
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.field_146042_b.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.FAILED_ATTEMPT);
                this.field_70170_p.getServer().getPluginManager().callEvent(playerFishEvent);
                // CraftBukkit end
            } else {
                this.field_70181_x -= 0.2D * (double) this.field_70146_Z.nextFloat() * (double) this.field_70146_Z.nextFloat();
            }
        } else {
            float f;
            float f1;
            float f2;
            double d0;
            double d1;
            double d2;
            Block block;

            if (this.field_146038_az > 0) {
                this.field_146038_az -= i;
                if (this.field_146038_az > 0) {
                    this.field_146054_aA = (float) ((double) this.field_146054_aA + this.field_70146_Z.nextGaussian() * 4.0D);
                    f = this.field_146054_aA * 0.017453292F;
                    f1 = MathHelper.func_76126_a(f);
                    f2 = MathHelper.func_76134_b(f);
                    d0 = this.field_70165_t + (double) (f1 * (float) this.field_146038_az * 0.1F);
                    d1 = (double) ((float) MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) + 1.0F);
                    d2 = this.field_70161_v + (double) (f2 * (float) this.field_146038_az * 0.1F);
                    block = worldserver.func_180495_p(new BlockPos(d0, d1 - 1.0D, d2)).func_177230_c();
                    if (block == Blocks.field_150355_j || block == Blocks.field_150358_i) {
                        if (this.field_70146_Z.nextFloat() < 0.15F) {
                            worldserver.func_175739_a(EnumParticleTypes.WATER_BUBBLE, d0, d1 - 0.10000000149011612D, d2, 1, (double) f1, 0.1D, (double) f2, 0.0D, new int[0]);
                        }

                        float f3 = f1 * 0.04F;
                        float f4 = f2 * 0.04F;

                        worldserver.func_175739_a(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double) f4, 0.01D, (double) (-f3), 1.0D, new int[0]);
                        worldserver.func_175739_a(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double) (-f4), 0.01D, (double) f3, 1.0D, new int[0]);
                    }
                } else {
                    // CraftBukkit start
                    PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.field_146042_b.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.BITE);
                    this.field_70170_p.getServer().getPluginManager().callEvent(playerFishEvent);
                    if (playerFishEvent.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                    this.field_70181_x = (double) (-0.4F * MathHelper.func_151240_a(this.field_70146_Z, 0.6F, 1.0F));
                    this.func_184185_a(SoundEvents.field_187609_F, 0.25F, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4F);
                    double d3 = this.func_174813_aQ().field_72338_b + 0.5D;

                    worldserver.func_175739_a(EnumParticleTypes.WATER_BUBBLE, this.field_70165_t, d3, this.field_70161_v, (int) (1.0F + this.field_70130_N * 20.0F), (double) this.field_70130_N, 0.0D, (double) this.field_70130_N, 0.20000000298023224D, new int[0]);
                    worldserver.func_175739_a(EnumParticleTypes.WATER_WAKE, this.field_70165_t, d3, this.field_70161_v, (int) (1.0F + this.field_70130_N * 20.0F), (double) this.field_70130_N, 0.0D, (double) this.field_70130_N, 0.20000000298023224D, new int[0]);
                    this.field_146045_ax = MathHelper.func_76136_a(this.field_70146_Z, 20, 40);
                }
            } else if (this.field_146040_ay > 0) {
                this.field_146040_ay -= i;
                f = 0.15F;
                if (this.field_146040_ay < 20) {
                    f = (float) ((double) f + (double) (20 - this.field_146040_ay) * 0.05D);
                } else if (this.field_146040_ay < 40) {
                    f = (float) ((double) f + (double) (40 - this.field_146040_ay) * 0.02D);
                } else if (this.field_146040_ay < 60) {
                    f = (float) ((double) f + (double) (60 - this.field_146040_ay) * 0.01D);
                }

                if (this.field_70146_Z.nextFloat() < f) {
                    f1 = MathHelper.func_151240_a(this.field_70146_Z, 0.0F, 360.0F) * 0.017453292F;
                    f2 = MathHelper.func_151240_a(this.field_70146_Z, 25.0F, 60.0F);
                    d0 = this.field_70165_t + (double) (MathHelper.func_76126_a(f1) * f2 * 0.1F);
                    d1 = (double) ((float) MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) + 1.0F);
                    d2 = this.field_70161_v + (double) (MathHelper.func_76134_b(f1) * f2 * 0.1F);
                    block = worldserver.func_180495_p(new BlockPos((int) d0, (int) d1 - 1, (int) d2)).func_177230_c();
                    if (block == Blocks.field_150355_j || block == Blocks.field_150358_i) {
                        worldserver.func_175739_a(EnumParticleTypes.WATER_SPLASH, d0, d1, d2, 2 + this.field_70146_Z.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
                    }
                }

                if (this.field_146040_ay <= 0) {
                    this.field_146054_aA = MathHelper.func_151240_a(this.field_70146_Z, 0.0F, 360.0F);
                    this.field_146038_az = MathHelper.func_76136_a(this.field_70146_Z, 20, 80);
                }
            } else {
                this.field_146040_ay = MathHelper.func_76136_a(this.field_70146_Z, field_70170_p.paperConfig.fishingMinTicks, field_70170_p.paperConfig.fishingMaxTicks); // Paper
                this.field_146040_ay -= this.field_191519_ax * 20 * 5;
            }
        }

    }

    protected boolean func_189739_a(Entity entity) {
        return entity.func_70067_L() || entity instanceof EntityItem;
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {}

    public void func_70037_a(NBTTagCompound nbttagcompound) {}

    public int func_146034_e() {
        if (!this.field_70170_p.field_72995_K && this.field_146042_b != null) {
            int i = 0;

            if (this.field_146043_c != null) {
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.field_146042_b.getBukkitEntity(), this.field_146043_c.getBukkitEntity(), (Fish) this.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_ENTITY);
                this.field_70170_p.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    return 0;
                }
                // CraftBukkit end
                this.func_184527_k();
                this.field_70170_p.func_72960_a(this, (byte) 31);
                i = this.field_146043_c instanceof EntityItem ? 3 : 5;
            } else if (this.field_146045_ax > 0) {
                LootContext.a loottableinfo_a = new LootContext.a((WorldServer) this.field_70170_p);

                loottableinfo_a.a((float) this.field_191518_aw + this.field_146042_b.func_184817_da());
                Iterator iterator = this.field_70170_p.func_184146_ak().func_186521_a(LootTableList.field_186387_al).func_186462_a(this.field_70146_Z, loottableinfo_a.a()).iterator();

                while (iterator.hasNext()) {
                    ItemStack itemstack = (ItemStack) iterator.next();
                    EntityItem entityitem = new EntityItem(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, itemstack);
                    // CraftBukkit start
                    PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.field_146042_b.getBukkitEntity(), entityitem.getBukkitEntity(), (Fish) this.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_FISH);
                    playerFishEvent.setExpToDrop(this.field_70146_Z.nextInt(6) + 1);
                    this.field_70170_p.getServer().getPluginManager().callEvent(playerFishEvent);

                    if (playerFishEvent.isCancelled()) {
                        return 0;
                    }
                    // CraftBukkit end
                    double d0 = this.field_146042_b.field_70165_t - this.field_70165_t;
                    double d1 = this.field_146042_b.field_70163_u - this.field_70163_u;
                    double d2 = this.field_146042_b.field_70161_v - this.field_70161_v;
                    double d3 = (double) MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = 0.1D;

                    entityitem.field_70159_w = d0 * 0.1D;
                    entityitem.field_70181_x = d1 * 0.1D + (double) MathHelper.func_76133_a(d3) * 0.08D;
                    entityitem.field_70179_y = d2 * 0.1D;
                    this.field_70170_p.func_72838_d(entityitem);
                    // CraftBukkit start - this.random.nextInt(6) + 1 -> playerFishEvent.getExpToDrop()
                    if (playerFishEvent.getExpToDrop() > 0) {
                        this.field_146042_b.field_70170_p.func_72838_d(new EntityXPOrb(this.field_146042_b.field_70170_p, this.field_146042_b.field_70165_t, this.field_146042_b.field_70163_u + 0.5D, this.field_146042_b.field_70161_v + 0.5D, playerFishEvent.getExpToDrop(), org.bukkit.entity.ExperienceOrb.SpawnReason.FISHING, this.field_146042_b, this)); // Paper
                    }
                    // CraftBukkit end
                    Item item = itemstack.func_77973_b();

                    if (item == Items.field_151115_aP || item == Items.field_179566_aV) {
                        this.field_146042_b.func_71064_a(StatList.field_188071_E, 1);
                    }
                }

                i = 1;
            }

            if (this.field_146051_au) {
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.field_146042_b.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.IN_GROUND);
                this.field_70170_p.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    return 0;
                }
                // CraftBukkit end
                i = 2;
            }
            // CraftBukkit start
            if (i == 0) {
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.field_146042_b.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.FAILED_ATTEMPT);
                this.field_70170_p.getServer().getPluginManager().callEvent(playerFishEvent);
                if (playerFishEvent.isCancelled()) {
                    return 0;
                }
            }
            // CraftBukkit end

            this.func_70106_y();
            return i;
        } else {
            return 0;
        }
    }

    protected void func_184527_k() {
        if (this.field_146042_b != null) {
            double d0 = this.field_146042_b.field_70165_t - this.field_70165_t;
            double d1 = this.field_146042_b.field_70163_u - this.field_70163_u;
            double d2 = this.field_146042_b.field_70161_v - this.field_70161_v;
            double d3 = 0.1D;

            this.field_146043_c.field_70159_w += d0 * 0.1D;
            this.field_146043_c.field_70181_x += d1 * 0.1D;
            this.field_146043_c.field_70179_y += d2 * 0.1D;
        }
    }

    protected boolean func_70041_e_() {
        return false;
    }

    public void func_70106_y() {
        super.func_70106_y();
        if (this.field_146042_b != null) {
            this.field_146042_b.field_71104_cf = null;
        }

    }

    public EntityPlayer func_190619_l() {
        return this.field_146042_b;
    }

    static enum State {

        FLYING, HOOKED_IN_ENTITY, BOBBING;

        private State() {}
    }
}
