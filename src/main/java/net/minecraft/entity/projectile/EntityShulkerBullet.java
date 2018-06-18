package net.minecraft.entity.projectile;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityShulkerBullet extends Entity {

    private EntityLivingBase owner;
    private Entity target;
    @Nullable
    private EnumFacing direction;
    private int steps;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    @Nullable
    private UUID ownerUniqueId;
    private BlockPos ownerBlockPos;
    @Nullable
    private UUID targetUniqueId;
    private BlockPos targetBlockPos;

    public EntityShulkerBullet(World world) {
        super(world);
        this.setSize(0.3125F, 0.3125F);
        this.noClip = true;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    public EntityShulkerBullet(World world, EntityLivingBase entityliving, Entity entity, EnumFacing.Axis enumdirection_enumaxis) {
        this(world);
        this.owner = entityliving;
        BlockPos blockposition = new BlockPos(entityliving);
        double d0 = (double) blockposition.getX() + 0.5D;
        double d1 = (double) blockposition.getY() + 0.5D;
        double d2 = (double) blockposition.getZ() + 0.5D;

        this.setLocationAndAngles(d0, d1, d2, this.rotationYaw, this.rotationPitch);
        this.target = entity;
        this.direction = EnumFacing.UP;
        this.selectNextMoveDirection(enumdirection_enumaxis);
        projectileSource = (org.bukkit.entity.LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
    }

    // CraftBukkit start
    public EntityLivingBase getShooter() {
        return this.owner;
    }

    public void setShooter(EntityLivingBase e) {
        this.owner = e;
    }

    public Entity getTarget() {
        return this.target;
    }

    public void setTarget(Entity e) {
        this.target = e;
        this.direction = EnumFacing.UP;
        this.selectNextMoveDirection(EnumFacing.Axis.X);
    }
    // CraftBukkit end

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        BlockPos blockposition;
        NBTTagCompound nbttagcompound1;

        if (this.owner != null) {
            blockposition = new BlockPos(this.owner);
            nbttagcompound1 = NBTUtil.createUUIDTag(this.owner.getUniqueID());
            nbttagcompound1.setInteger("X", blockposition.getX());
            nbttagcompound1.setInteger("Y", blockposition.getY());
            nbttagcompound1.setInteger("Z", blockposition.getZ());
            nbttagcompound.setTag("Owner", nbttagcompound1);
        }

        if (this.target != null) {
            blockposition = new BlockPos(this.target);
            nbttagcompound1 = NBTUtil.createUUIDTag(this.target.getUniqueID());
            nbttagcompound1.setInteger("X", blockposition.getX());
            nbttagcompound1.setInteger("Y", blockposition.getY());
            nbttagcompound1.setInteger("Z", blockposition.getZ());
            nbttagcompound.setTag("Target", nbttagcompound1);
        }

        if (this.direction != null) {
            nbttagcompound.setInteger("Dir", this.direction.getIndex());
        }

        nbttagcompound.setInteger("Steps", this.steps);
        nbttagcompound.setDouble("TXD", this.targetDeltaX);
        nbttagcompound.setDouble("TYD", this.targetDeltaY);
        nbttagcompound.setDouble("TZD", this.targetDeltaZ);
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.steps = nbttagcompound.getInteger("Steps");
        this.targetDeltaX = nbttagcompound.getDouble("TXD");
        this.targetDeltaY = nbttagcompound.getDouble("TYD");
        this.targetDeltaZ = nbttagcompound.getDouble("TZD");
        if (nbttagcompound.hasKey("Dir", 99)) {
            this.direction = EnumFacing.getFront(nbttagcompound.getInteger("Dir"));
        }

        NBTTagCompound nbttagcompound1;

        if (nbttagcompound.hasKey("Owner", 10)) {
            nbttagcompound1 = nbttagcompound.getCompoundTag("Owner");
            this.ownerUniqueId = NBTUtil.getUUIDFromTag(nbttagcompound1);
            this.ownerBlockPos = new BlockPos(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Y"), nbttagcompound1.getInteger("Z"));
        }

        if (nbttagcompound.hasKey("Target", 10)) {
            nbttagcompound1 = nbttagcompound.getCompoundTag("Target");
            this.targetUniqueId = NBTUtil.getUUIDFromTag(nbttagcompound1);
            this.targetBlockPos = new BlockPos(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Y"), nbttagcompound1.getInteger("Z"));
        }

    }

    protected void entityInit() {}

    private void setDirection(@Nullable EnumFacing enumdirection) {
        this.direction = enumdirection;
    }

    private void selectNextMoveDirection(@Nullable EnumFacing.Axis enumdirection_enumaxis) {
        double d0 = 0.5D;
        BlockPos blockposition;

        if (this.target == null) {
            blockposition = (new BlockPos(this)).down();
        } else {
            d0 = (double) this.target.height * 0.5D;
            blockposition = new BlockPos(this.target.posX, this.target.posY + d0, this.target.posZ);
        }

        double d1 = (double) blockposition.getX() + 0.5D;
        double d2 = (double) blockposition.getY() + d0;
        double d3 = (double) blockposition.getZ() + 0.5D;
        EnumFacing enumdirection = null;

        if (blockposition.distanceSqToCenter(this.posX, this.posY, this.posZ) >= 4.0D) {
            BlockPos blockposition1 = new BlockPos(this);
            ArrayList arraylist = Lists.newArrayList();

            if (enumdirection_enumaxis != EnumFacing.Axis.X) {
                if (blockposition1.getX() < blockposition.getX() && this.world.isAirBlock(blockposition1.east())) {
                    arraylist.add(EnumFacing.EAST);
                } else if (blockposition1.getX() > blockposition.getX() && this.world.isAirBlock(blockposition1.west())) {
                    arraylist.add(EnumFacing.WEST);
                }
            }

            if (enumdirection_enumaxis != EnumFacing.Axis.Y) {
                if (blockposition1.getY() < blockposition.getY() && this.world.isAirBlock(blockposition1.up())) {
                    arraylist.add(EnumFacing.UP);
                } else if (blockposition1.getY() > blockposition.getY() && this.world.isAirBlock(blockposition1.down())) {
                    arraylist.add(EnumFacing.DOWN);
                }
            }

            if (enumdirection_enumaxis != EnumFacing.Axis.Z) {
                if (blockposition1.getZ() < blockposition.getZ() && this.world.isAirBlock(blockposition1.south())) {
                    arraylist.add(EnumFacing.SOUTH);
                } else if (blockposition1.getZ() > blockposition.getZ() && this.world.isAirBlock(blockposition1.north())) {
                    arraylist.add(EnumFacing.NORTH);
                }
            }

            enumdirection = EnumFacing.random(this.rand);
            if (arraylist.isEmpty()) {
                for (int i = 5; !this.world.isAirBlock(blockposition1.offset(enumdirection)) && i > 0; --i) {
                    enumdirection = EnumFacing.random(this.rand);
                }
            } else {
                enumdirection = (EnumFacing) arraylist.get(this.rand.nextInt(arraylist.size()));
            }

            d1 = this.posX + (double) enumdirection.getFrontOffsetX();
            d2 = this.posY + (double) enumdirection.getFrontOffsetY();
            d3 = this.posZ + (double) enumdirection.getFrontOffsetZ();
        }

        this.setDirection(enumdirection);
        double d4 = d1 - this.posX;
        double d5 = d2 - this.posY;
        double d6 = d3 - this.posZ;
        double d7 = (double) MathHelper.sqrt(d4 * d4 + d5 * d5 + d6 * d6);

        if (d7 == 0.0D) {
            this.targetDeltaX = 0.0D;
            this.targetDeltaY = 0.0D;
            this.targetDeltaZ = 0.0D;
        } else {
            this.targetDeltaX = d4 / d7 * 0.15D;
            this.targetDeltaY = d5 / d7 * 0.15D;
            this.targetDeltaZ = d6 / d7 * 0.15D;
        }

        this.isAirBorne = true;
        this.steps = 10 + this.rand.nextInt(5) * 10;
    }

    public void onUpdate() {
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        } else {
            super.onUpdate();
            if (!this.world.isRemote) {
                List list;
                Iterator iterator;
                EntityLivingBase entityliving;

                if (this.target == null && this.targetUniqueId != null) {
                    list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.targetBlockPos.add(-2, -2, -2), this.targetBlockPos.add(2, 2, 2)));
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityliving = (EntityLivingBase) iterator.next();
                        if (entityliving.getUniqueID().equals(this.targetUniqueId)) {
                            this.target = entityliving;
                            break;
                        }
                    }

                    this.targetUniqueId = null;
                }

                if (this.owner == null && this.ownerUniqueId != null) {
                    list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.ownerBlockPos.add(-2, -2, -2), this.ownerBlockPos.add(2, 2, 2)));
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityliving = (EntityLivingBase) iterator.next();
                        if (entityliving.getUniqueID().equals(this.ownerUniqueId)) {
                            this.owner = entityliving;
                            break;
                        }
                    }

                    this.ownerUniqueId = null;
                }

                if (this.target != null && this.target.isEntityAlive() && (!(this.target instanceof EntityPlayer) || !((EntityPlayer) this.target).isSpectator())) {
                    this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
                    this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
                    this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
                    this.motionX += (this.targetDeltaX - this.motionX) * 0.2D;
                    this.motionY += (this.targetDeltaY - this.motionY) * 0.2D;
                    this.motionZ += (this.targetDeltaZ - this.motionZ) * 0.2D;
                } else if (!this.hasNoGravity()) {
                    this.motionY -= 0.04D;
                }

                RayTraceResult movingobjectposition = ProjectileHelper.forwardsRaycast(this, true, false, this.owner);

                if (movingobjectposition != null) {
                    this.bulletHit(movingobjectposition);
                }
            }

            this.setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            ProjectileHelper.rotateTowardsMovement(this, 0.5F);
            if (this.world.isRemote) {
                this.world.spawnParticle(EnumParticleTypes.END_ROD, this.posX - this.motionX, this.posY - this.motionY + 0.15D, this.posZ - this.motionZ, 0.0D, 0.0D, 0.0D, new int[0]);
            } else if (this.target != null && !this.target.isDead) {
                if (this.steps > 0) {
                    --this.steps;
                    if (this.steps == 0) {
                        this.selectNextMoveDirection(this.direction == null ? null : this.direction.getAxis());
                    }
                }

                if (this.direction != null) {
                    BlockPos blockposition = new BlockPos(this);
                    EnumFacing.Axis enumdirection_enumaxis = this.direction.getAxis();

                    if (this.world.isBlockNormalCube(blockposition.offset(this.direction), false)) {
                        this.selectNextMoveDirection(enumdirection_enumaxis);
                    } else {
                        BlockPos blockposition1 = new BlockPos(this.target);

                        if (enumdirection_enumaxis == EnumFacing.Axis.X && blockposition.getX() == blockposition1.getX() || enumdirection_enumaxis == EnumFacing.Axis.Z && blockposition.getZ() == blockposition1.getZ() || enumdirection_enumaxis == EnumFacing.Axis.Y && blockposition.getY() == blockposition1.getY()) {
                            this.selectNextMoveDirection(enumdirection_enumaxis);
                        }
                    }
                }
            }

        }
    }

    public boolean isBurning() {
        return false;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void bulletHit(RayTraceResult movingobjectposition) {
        org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition); // Craftbukkit - Call event
        if (movingobjectposition.entityHit == null) {
            ((WorldServer) this.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 2, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
        } else {
            boolean flag = movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 4.0F);

            if (flag) {
                this.applyEnchantments(this.owner, movingobjectposition.entityHit);
                if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                    ((EntityLivingBase) movingobjectposition.entityHit).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 200));
                }
            }
        }

        this.setDead();
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (!this.world.isRemote) {
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HURT, 1.0F, 1.0F);
            ((WorldServer) this.world).spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 15, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
            this.setDead();
        }

        return true;
    }
}
