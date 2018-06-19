package net.minecraft.entity;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.bukkit.entity.Hanging;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

// CraftBukkit start
import org.bukkit.entity.Hanging;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
// CraftBukkit end

public abstract class EntityHanging extends Entity {

    private static final Predicate<Entity> field_184524_c = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof EntityHanging;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    private int field_70520_f;
    public BlockPos field_174861_a;
    @Nullable
    public EnumFacing field_174860_b;

    public EntityHanging(World world) {
        super(world);
        this.func_70105_a(0.5F, 0.5F);
    }

    public EntityHanging(World world, BlockPos blockposition) {
        this(world);
        this.field_174861_a = blockposition;
    }

    protected void func_70088_a() {}

    public void func_174859_a(EnumFacing enumdirection) {
        Validate.notNull(enumdirection);
        Validate.isTrue(enumdirection.func_176740_k().func_176722_c());
        this.field_174860_b = enumdirection;
        this.field_70177_z = (float) (this.field_174860_b.func_176736_b() * 90);
        this.field_70126_B = this.field_70177_z;
        this.func_174856_o();
    }

    // CraftBukkit start - break out BB calc into own method
    public static AxisAlignedBB calculateBoundingBox(Entity entity, BlockPos blockPosition, EnumFacing direction, int width, int height) {
        double d0 = (double) blockPosition.func_177958_n() + 0.5D;
        double d1 = (double) blockPosition.func_177956_o() + 0.5D;
        double d2 = (double) blockPosition.func_177952_p() + 0.5D;
        double d3 = 0.46875D;
        double d4 = func_190202_a(width);
        double d5 = func_190202_a(height);

        d0 -= (double) direction.func_82601_c() * 0.46875D;
        d2 -= (double) direction.func_82599_e() * 0.46875D;
        d1 += d5;
        EnumFacing enumdirection = direction.func_176735_f();

        d0 += d4 * (double) enumdirection.func_82601_c();
        d2 += d4 * (double) enumdirection.func_82599_e();
        if (entity != null) {
            entity.field_70165_t = d0;
            entity.field_70163_u = d1;
            entity.field_70161_v = d2;
        }
        double d6 = (double) width;
        double d7 = (double) height;
        double d8 = (double) width;

        if (direction.func_176740_k() == EnumFacing.Axis.Z) {
            d8 = 1.0D;
        } else {
            d6 = 1.0D;
        }

        d6 /= 32.0D;
        d7 /= 32.0D;
        d8 /= 32.0D;
        return new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8);
    }

    protected void func_174856_o() {
        if (this.field_174860_b != null) {
            // CraftBukkit start code moved in to calculateBoundingBox
            this.func_174826_a(calculateBoundingBox(this, this.field_174861_a, this.field_174860_b, this.func_82329_d(), this.func_82330_g()));
            // CraftBukkit end
        }
    }

    private static double func_190202_a(int i) { // CraftBukkit - static
        return i % 32 == 0 ? 0.5D : 0.0D;
    }

    public void func_70071_h_() {
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        if (this.field_70520_f++ == this.field_70170_p.spigotConfig.hangingTickFrequency && !this.field_70170_p.field_72995_K) { // Spigot
            this.field_70520_f = 0;
            if (!this.field_70128_L && !this.func_70518_d()) {
                // CraftBukkit start - fire break events
                Material material = this.field_70170_p.func_180495_p(new BlockPos(this)).func_185904_a();
                HangingBreakEvent.RemoveCause cause;

                if (!material.equals(Material.field_151579_a)) {
                    // TODO: This feels insufficient to catch 100% of suffocation cases
                    cause = HangingBreakEvent.RemoveCause.OBSTRUCTION;
                } else {
                    cause = HangingBreakEvent.RemoveCause.PHYSICS;
                }

                HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), cause);
                this.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (field_70128_L || event.isCancelled()) {
                    return;
                }
                // CraftBukkit end
                this.func_70106_y();
                this.func_110128_b((Entity) null);
            }
        }

    }

    public boolean func_70518_d() {
        if (!this.field_70170_p.func_184144_a(this, this.func_174813_aQ()).isEmpty()) {
            return false;
        } else {
            int i = Math.max(1, this.func_82329_d() / 16);
            int j = Math.max(1, this.func_82330_g() / 16);
            BlockPos blockposition = this.field_174861_a.func_177972_a(this.field_174860_b.func_176734_d());
            EnumFacing enumdirection = this.field_174860_b.func_176735_f();
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < j; ++l) {
                    int i1 = (i - 1) / -2;
                    int j1 = (j - 1) / -2;

                    blockposition_mutableblockposition.func_189533_g(blockposition).func_189534_c(enumdirection, k + i1).func_189534_c(EnumFacing.UP, l + j1);
                    IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition_mutableblockposition);

                    if (!iblockdata.func_185904_a().func_76220_a() && !BlockRedstoneDiode.func_185546_B(iblockdata)) {
                        return false;
                    }
                }
            }

            return this.field_70170_p.func_175674_a(this, this.func_174813_aQ(), EntityHanging.field_184524_c).isEmpty();
        }
    }

    public boolean func_70067_L() {
        return true;
    }

    public boolean func_85031_j(Entity entity) {
        return entity instanceof EntityPlayer ? this.func_70097_a(DamageSource.func_76365_a((EntityPlayer) entity), 0.0F) : false;
    }

    public EnumFacing func_174811_aO() {
        return this.field_174860_b;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            if (!this.field_70128_L && !this.field_70170_p.field_72995_K) {
                // CraftBukkit start - fire break events
                HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.DEFAULT);
                if (damagesource.func_76346_g() != null) {
                    event = new HangingBreakByEntityEvent((Hanging) this.getBukkitEntity(), damagesource.func_76346_g() == null ? null : damagesource.func_76346_g().getBukkitEntity(), damagesource.func_94541_c() ? HangingBreakEvent.RemoveCause.EXPLOSION : HangingBreakEvent.RemoveCause.ENTITY);
                } else if (damagesource.func_94541_c()) {
                    event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.EXPLOSION);
                }

                this.field_70170_p.getServer().getPluginManager().callEvent(event);

                if (this.field_70128_L || event.isCancelled()) {
                    return true;
                }
                // CraftBukkit end

                this.func_70106_y();
                this.func_70018_K();
                this.func_110128_b(damagesource.func_76346_g());
            }

            return true;
        }
    }

    public void func_70091_d(MoverType enummovetype, double d0, double d1, double d2) {
        if (!this.field_70170_p.field_72995_K && !this.field_70128_L && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) {
            if (this.field_70128_L) return; // CraftBukkit

            // CraftBukkit start - fire break events
            // TODO - Does this need its own cause? Seems to only be triggered by pistons
            HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.PHYSICS);
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (this.field_70128_L || event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            this.func_70106_y();
            this.func_110128_b((Entity) null);
        }

    }

    public void func_70024_g(double d0, double d1, double d2) {
        if (false && !this.field_70170_p.field_72995_K && !this.field_70128_L && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) { // CraftBukkit - not needed
            this.func_70106_y();
            this.func_110128_b((Entity) null);
        }

    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74774_a("Facing", (byte) this.field_174860_b.func_176736_b());
        BlockPos blockposition = this.func_174857_n();

        nbttagcompound.func_74768_a("TileX", blockposition.func_177958_n());
        nbttagcompound.func_74768_a("TileY", blockposition.func_177956_o());
        nbttagcompound.func_74768_a("TileZ", blockposition.func_177952_p());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_174861_a = new BlockPos(nbttagcompound.func_74762_e("TileX"), nbttagcompound.func_74762_e("TileY"), nbttagcompound.func_74762_e("TileZ"));
        this.func_174859_a(EnumFacing.func_176731_b(nbttagcompound.func_74771_c("Facing")));
    }

    public abstract int func_82329_d();

    public abstract int func_82330_g();

    public abstract void func_110128_b(@Nullable Entity entity);

    public abstract void func_184523_o();

    public EntityItem func_70099_a(ItemStack itemstack, float f) {
        EntityItem entityitem = new EntityItem(this.field_70170_p, this.field_70165_t + (double) ((float) this.field_174860_b.func_82601_c() * 0.15F), this.field_70163_u + (double) f, this.field_70161_v + (double) ((float) this.field_174860_b.func_82599_e() * 0.15F), itemstack);

        entityitem.func_174869_p();
        this.field_70170_p.func_72838_d(entityitem);
        return entityitem;
    }

    protected boolean func_142008_O() {
        return false;
    }

    public void func_70107_b(double d0, double d1, double d2) {
        this.field_174861_a = new BlockPos(d0, d1, d2);
        this.func_174856_o();
        this.field_70160_al = true;
    }

    public BlockPos func_174857_n() {
        return this.field_174861_a;
    }

    public float func_184229_a(Rotation enumblockrotation) {
        if (this.field_174860_b != null && this.field_174860_b.func_176740_k() != EnumFacing.Axis.Y) {
            switch (enumblockrotation) {
            case CLOCKWISE_180:
                this.field_174860_b = this.field_174860_b.func_176734_d();
                break;

            case COUNTERCLOCKWISE_90:
                this.field_174860_b = this.field_174860_b.func_176735_f();
                break;

            case CLOCKWISE_90:
                this.field_174860_b = this.field_174860_b.func_176746_e();
            }
        }

        float f = MathHelper.func_76142_g(this.field_70177_z);

        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return f + 180.0F;

        case COUNTERCLOCKWISE_90:
            return f + 90.0F;

        case CLOCKWISE_90:
            return f + 270.0F;

        default:
            return f;
        }
    }

    public float func_184217_a(Mirror enumblockmirror) {
        return this.func_184229_a(enumblockmirror.func_185800_a(this.field_174860_b));
    }

    public void func_70077_a(EntityLightningBolt entitylightning) {}
}
