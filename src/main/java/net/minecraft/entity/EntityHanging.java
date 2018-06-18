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

    private static final Predicate<Entity> IS_HANGING_ENTITY = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof EntityHanging;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    private int tickCounter1;
    public BlockPos hangingPosition;
    @Nullable
    public EnumFacing facingDirection;

    public EntityHanging(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
    }

    public EntityHanging(World world, BlockPos blockposition) {
        this(world);
        this.hangingPosition = blockposition;
    }

    protected void entityInit() {}

    public void updateFacingWithBoundingBox(EnumFacing enumdirection) {
        Validate.notNull(enumdirection);
        Validate.isTrue(enumdirection.getAxis().isHorizontal());
        this.facingDirection = enumdirection;
        this.rotationYaw = (float) (this.facingDirection.getHorizontalIndex() * 90);
        this.prevRotationYaw = this.rotationYaw;
        this.updateBoundingBox();
    }

    // CraftBukkit start - break out BB calc into own method
    public static AxisAlignedBB calculateBoundingBox(Entity entity, BlockPos blockPosition, EnumFacing direction, int width, int height) {
        double d0 = (double) blockPosition.getX() + 0.5D;
        double d1 = (double) blockPosition.getY() + 0.5D;
        double d2 = (double) blockPosition.getZ() + 0.5D;
        double d3 = 0.46875D;
        double d4 = offs(width);
        double d5 = offs(height);

        d0 -= (double) direction.getFrontOffsetX() * 0.46875D;
        d2 -= (double) direction.getFrontOffsetZ() * 0.46875D;
        d1 += d5;
        EnumFacing enumdirection = direction.rotateYCCW();

        d0 += d4 * (double) enumdirection.getFrontOffsetX();
        d2 += d4 * (double) enumdirection.getFrontOffsetZ();
        if (entity != null) {
            entity.posX = d0;
            entity.posY = d1;
            entity.posZ = d2;
        }
        double d6 = (double) width;
        double d7 = (double) height;
        double d8 = (double) width;

        if (direction.getAxis() == EnumFacing.Axis.Z) {
            d8 = 1.0D;
        } else {
            d6 = 1.0D;
        }

        d6 /= 32.0D;
        d7 /= 32.0D;
        d8 /= 32.0D;
        return new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8);
    }

    protected void updateBoundingBox() {
        if (this.facingDirection != null) {
            // CraftBukkit start code moved in to calculateBoundingBox
            this.setEntityBoundingBox(calculateBoundingBox(this, this.hangingPosition, this.facingDirection, this.getWidthPixels(), this.getHeightPixels()));
            // CraftBukkit end
        }
    }

    private static double offs(int i) { // CraftBukkit - static
        return i % 32 == 0 ? 0.5D : 0.0D;
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.tickCounter1++ == this.world.spigotConfig.hangingTickFrequency && !this.world.isRemote) { // Spigot
            this.tickCounter1 = 0;
            if (!this.isDead && !this.onValidSurface()) {
                // CraftBukkit start - fire break events
                Material material = this.world.getBlockState(new BlockPos(this)).getMaterial();
                HangingBreakEvent.RemoveCause cause;

                if (!material.equals(Material.AIR)) {
                    // TODO: This feels insufficient to catch 100% of suffocation cases
                    cause = HangingBreakEvent.RemoveCause.OBSTRUCTION;
                } else {
                    cause = HangingBreakEvent.RemoveCause.PHYSICS;
                }

                HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), cause);
                this.world.getServer().getPluginManager().callEvent(event);

                if (isDead || event.isCancelled()) {
                    return;
                }
                // CraftBukkit end
                this.setDead();
                this.onBroken((Entity) null);
            }
        }

    }

    public boolean onValidSurface() {
        if (!this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) {
            return false;
        } else {
            int i = Math.max(1, this.getWidthPixels() / 16);
            int j = Math.max(1, this.getHeightPixels() / 16);
            BlockPos blockposition = this.hangingPosition.offset(this.facingDirection.getOpposite());
            EnumFacing enumdirection = this.facingDirection.rotateYCCW();
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < j; ++l) {
                    int i1 = (i - 1) / -2;
                    int j1 = (j - 1) / -2;

                    blockposition_mutableblockposition.setPos(blockposition).move(enumdirection, k + i1).move(EnumFacing.UP, l + j1);
                    IBlockState iblockdata = this.world.getBlockState(blockposition_mutableblockposition);

                    if (!iblockdata.getMaterial().isSolid() && !BlockRedstoneDiode.isDiode(iblockdata)) {
                        return false;
                    }
                }
            }

            return this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntityHanging.IS_HANGING_ENTITY).isEmpty();
        }
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean hitByEntity(Entity entity) {
        return entity instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entity), 0.0F) : false;
    }

    public EnumFacing getHorizontalFacing() {
        return this.facingDirection;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else {
            if (!this.isDead && !this.world.isRemote) {
                // CraftBukkit start - fire break events
                HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.DEFAULT);
                if (damagesource.getTrueSource() != null) {
                    event = new HangingBreakByEntityEvent((Hanging) this.getBukkitEntity(), damagesource.getTrueSource() == null ? null : damagesource.getTrueSource().getBukkitEntity(), damagesource.isExplosion() ? HangingBreakEvent.RemoveCause.EXPLOSION : HangingBreakEvent.RemoveCause.ENTITY);
                } else if (damagesource.isExplosion()) {
                    event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.EXPLOSION);
                }

                this.world.getServer().getPluginManager().callEvent(event);

                if (this.isDead || event.isCancelled()) {
                    return true;
                }
                // CraftBukkit end

                this.setDead();
                this.markVelocityChanged();
                this.onBroken(damagesource.getTrueSource());
            }

            return true;
        }
    }

    public void move(MoverType enummovetype, double d0, double d1, double d2) {
        if (!this.world.isRemote && !this.isDead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) {
            if (this.isDead) return; // CraftBukkit

            // CraftBukkit start - fire break events
            // TODO - Does this need its own cause? Seems to only be triggered by pistons
            HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.PHYSICS);
            this.world.getServer().getPluginManager().callEvent(event);

            if (this.isDead || event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            this.setDead();
            this.onBroken((Entity) null);
        }

    }

    public void addVelocity(double d0, double d1, double d2) {
        if (false && !this.world.isRemote && !this.isDead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) { // CraftBukkit - not needed
            this.setDead();
            this.onBroken((Entity) null);
        }

    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Facing", (byte) this.facingDirection.getHorizontalIndex());
        BlockPos blockposition = this.getHangingPosition();

        nbttagcompound.setInteger("TileX", blockposition.getX());
        nbttagcompound.setInteger("TileY", blockposition.getY());
        nbttagcompound.setInteger("TileZ", blockposition.getZ());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.hangingPosition = new BlockPos(nbttagcompound.getInteger("TileX"), nbttagcompound.getInteger("TileY"), nbttagcompound.getInteger("TileZ"));
        this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(nbttagcompound.getByte("Facing")));
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    public abstract void onBroken(@Nullable Entity entity);

    public abstract void playPlaceSound();

    public EntityItem entityDropItem(ItemStack itemstack, float f) {
        EntityItem entityitem = new EntityItem(this.world, this.posX + (double) ((float) this.facingDirection.getFrontOffsetX() * 0.15F), this.posY + (double) f, this.posZ + (double) ((float) this.facingDirection.getFrontOffsetZ() * 0.15F), itemstack);

        entityitem.setDefaultPickupDelay();
        this.world.spawnEntity(entityitem);
        return entityitem;
    }

    protected boolean shouldSetPosAfterLoading() {
        return false;
    }

    public void setPosition(double d0, double d1, double d2) {
        this.hangingPosition = new BlockPos(d0, d1, d2);
        this.updateBoundingBox();
        this.isAirBorne = true;
    }

    public BlockPos getHangingPosition() {
        return this.hangingPosition;
    }

    public float getRotatedYaw(Rotation enumblockrotation) {
        if (this.facingDirection != null && this.facingDirection.getAxis() != EnumFacing.Axis.Y) {
            switch (enumblockrotation) {
            case CLOCKWISE_180:
                this.facingDirection = this.facingDirection.getOpposite();
                break;

            case COUNTERCLOCKWISE_90:
                this.facingDirection = this.facingDirection.rotateYCCW();
                break;

            case CLOCKWISE_90:
                this.facingDirection = this.facingDirection.rotateY();
            }
        }

        float f = MathHelper.wrapDegrees(this.rotationYaw);

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

    public float getMirroredYaw(Mirror enumblockmirror) {
        return this.getRotatedYaw(enumblockmirror.toRotation(this.facingDirection));
    }

    public void onStruckByLightning(EntityLightningBolt entitylightning) {}
}
