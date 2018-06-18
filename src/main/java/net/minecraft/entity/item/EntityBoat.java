package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
// CraftBukkit end

public class EntityBoat extends Entity {

    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(EntityBoat.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> BOAT_TYPE = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean>[] DATA_ID_PADDLE = new DataParameter[] { EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN), EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN)};
    private final float[] paddlePositions;
    private float momentum;
    private float outOfControlTicks;
    private float deltaRotation;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;
    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;
    private double waterLevel;
    private float boatGlide;
    private EntityBoat.Status status;
    private EntityBoat.Status previousStatus;
    private double lastYd;

    // CraftBukkit start
    // PAIL: Some of these haven't worked since a few updates, and since 1.9 they are less and less applicable.
    public double maxSpeed = 0.4D;
    public double occupiedDeceleration = 0.2D;
    public double unoccupiedDeceleration = -1;
    public boolean landBoats = false;
    // CraftBukkit end

    public EntityBoat(World world) {
        super(world);
        this.paddlePositions = new float[2];
        this.preventEntitySpawning = true;
        this.setSize(1.375F, 0.5625F);
    }

    public EntityBoat(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1, d2);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = d0;
        this.prevPosY = d1;
        this.prevPosZ = d2;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
        this.dataManager.register(EntityBoat.TIME_SINCE_HIT, Integer.valueOf(0));
        this.dataManager.register(EntityBoat.FORWARD_DIRECTION, Integer.valueOf(1));
        this.dataManager.register(EntityBoat.DAMAGE_TAKEN, Float.valueOf(0.0F));
        this.dataManager.register(EntityBoat.BOAT_TYPE, Integer.valueOf(EntityBoat.Type.OAK.ordinal()));
        DataParameter[] adatawatcherobject = EntityBoat.DATA_ID_PADDLE;
        int i = adatawatcherobject.length;

        for (int j = 0; j < i; ++j) {
            DataParameter datawatcherobject = adatawatcherobject[j];

            this.dataManager.register(datawatcherobject, Boolean.valueOf(false));
        }

    }

    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.canBePushed() ? entity.getEntityBoundingBox() : null;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    public boolean canBePushed() {
        return true;
    }

    public double getMountedYOffset() {
        return -0.1D;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (!this.world.isRemote && !this.isDead) {
            if (damagesource instanceof EntityDamageSourceIndirect && damagesource.getTrueSource() != null && this.isPassenger(damagesource.getTrueSource())) {
                return false;
            } else {
                // CraftBukkit start
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity attacker = (damagesource.getTrueSource() == null) ? null : damagesource.getTrueSource().getBukkitEntity();

                VehicleDamageEvent event = new VehicleDamageEvent(vehicle, attacker, (double) f);
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }
                // f = event.getDamage(); // TODO Why don't we do this?
                // CraftBukkit end

                this.setForwardDirection(-this.getForwardDirection());
                this.setTimeSinceHit(10);
                this.setDamageTaken(this.getDamageTaken() + f * 10.0F);
                this.markVelocityChanged();
                boolean flag = damagesource.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) damagesource.getTrueSource()).capabilities.isCreativeMode;

                if (flag || this.getDamageTaken() > 40.0F) {
                    // CraftBukkit start
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
                    this.world.getServer().getPluginManager().callEvent(destroyEvent);

                    if (destroyEvent.isCancelled()) {
                        this.setDamageTaken(40F); // Maximize damage so this doesn't get triggered again right away
                        return true;
                    }
                    // CraftBukkit end
                    if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
                        this.dropItemWithOffset(this.getItemBoat(), 1, 0.0F);
                    }

                    this.setDead();
                }

                return true;
            }
        } else {
            return true;
        }
    }

    public void applyEntityCollision(Entity entity) {
        if (entity instanceof EntityBoat) {
            if (entity.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY) {
                // CraftBukkit start
                VehicleEntityCollisionEvent event = new VehicleEntityCollisionEvent((Vehicle) this.getBukkitEntity(), entity.getBukkitEntity());
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end
                super.applyEntityCollision(entity);
            }
        } else if (entity.getEntityBoundingBox().minY <= this.getEntityBoundingBox().minY) {
            // CraftBukkit start
            VehicleEntityCollisionEvent event = new VehicleEntityCollisionEvent((Vehicle) this.getBukkitEntity(), entity.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end
            super.applyEntityCollision(entity);
        }

    }

    public Item getItemBoat() {
        switch (this.getBoatType()) {
        case OAK:
        default:
            return Items.BOAT;

        case SPRUCE:
            return Items.SPRUCE_BOAT;

        case BIRCH:
            return Items.BIRCH_BOAT;

        case JUNGLE:
            return Items.JUNGLE_BOAT;

        case ACACIA:
            return Items.ACACIA_BOAT;

        case DARK_OAK:
            return Items.DARK_OAK_BOAT;
        }
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public EnumFacing getAdjustedHorizontalFacing() {
        return this.getHorizontalFacing().rotateY();
    }

    private Location lastLocation; // CraftBukkit
    public void onUpdate() {
        this.previousStatus = this.status;
        this.status = this.getBoatStatus();
        if (this.status != EntityBoat.Status.UNDER_WATER && this.status != EntityBoat.Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0F;
        } else {
            ++this.outOfControlTicks;
        }

        if (!this.world.isRemote && this.outOfControlTicks >= 60.0F) {
            this.removePassengers();
        }

        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F) {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        super.onUpdate();
        this.tickLerp();
        if (this.canPassengerSteer()) {
            if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityPlayer)) {
                this.setPaddleState(false, false);
            }

            this.updateMotion();
            if (this.world.isRemote) {
                this.controlBoat();
                this.world.sendPacketToServer((Packet) (new CPacketSteerBoat(this.getPaddleState(0), this.getPaddleState(1))));
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }

        // CraftBukkit start
        org.bukkit.Server server = this.world.getServer();
        org.bukkit.World bworld = this.world.getWorld();

        Location to = new Location(bworld, this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();

        server.getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));

        if (lastLocation != null && !lastLocation.equals(to)) {
            VehicleMoveEvent event = new VehicleMoveEvent(vehicle, lastLocation, to);
            server.getPluginManager().callEvent(event);
        }
        lastLocation = vehicle.getLocation();
        // CraftBukkit end

        for (int i = 0; i <= 1; ++i) {
            if (this.getPaddleState(i)) {
                if (!this.isSilent() && (double) (this.paddlePositions[i] % 6.2831855F) <= 0.7853981852531433D && ((double) this.paddlePositions[i] + 0.39269909262657166D) % 6.2831854820251465D >= 0.7853981852531433D) {
                    SoundEvent soundeffect = this.getPaddleSound();

                    if (soundeffect != null) {
                        Vec3d vec3d = this.getLook(1.0F);
                        double d0 = i == 1 ? -vec3d.z : vec3d.z;
                        double d1 = i == 1 ? vec3d.x : -vec3d.x;

                        this.world.playSound((EntityPlayer) null, this.posX + d0, this.posY, this.posZ + d1, soundeffect, this.getSoundCategory(), 1.0F, 0.8F + 0.4F * this.rand.nextFloat());
                    }
                }

                this.paddlePositions[i] = (float) ((double) this.paddlePositions[i] + 0.39269909262657166D);
            } else {
                this.paddlePositions[i] = 0.0F;
            }
        }

        this.doBlockCollisions();
        List list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelectors.getTeamCollisionPredicate(this));

        if (!list.isEmpty()) {
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof EntityPlayer);

            for (int j = 0; j < list.size(); ++j) {
                Entity entity = (Entity) list.get(j);

                if (!entity.isPassenger(this)) {
                    if (flag && this.getPassengers().size() < 2 && !entity.isRiding() && entity.width < this.width && entity instanceof EntityLivingBase && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityPlayer)) {
                        entity.startRiding(this);
                    } else {
                        this.applyEntityCollision(entity);
                    }
                }
            }
        }

    }

    @Nullable
    protected SoundEvent getPaddleSound() {
        switch (this.getBoatStatus()) {
        case IN_WATER:
        case UNDER_WATER:
        case UNDER_FLOWING_WATER:
            return SoundEvents.ENTITY_BOAT_PADDLE_WATER;

        case ON_LAND:
            return SoundEvents.ENTITY_BOAT_PADDLE_LAND;

        case IN_AIR:
        default:
            return null;
        }
    }

    private void tickLerp() {
        if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
            double d0 = this.posX + (this.lerpX - this.posX) / (double) this.lerpSteps;
            double d1 = this.posY + (this.lerpY - this.posY) / (double) this.lerpSteps;
            double d2 = this.posZ + (this.lerpZ - this.posZ) / (double) this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.lerpYaw - (double) this.rotationYaw);

            this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.lerpSteps);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.lerpPitch - (double) this.rotationPitch) / (double) this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }

    public void setPaddleState(boolean flag, boolean flag1) {
        this.dataManager.set(EntityBoat.DATA_ID_PADDLE[0], Boolean.valueOf(flag));
        this.dataManager.set(EntityBoat.DATA_ID_PADDLE[1], Boolean.valueOf(flag1));
    }

    private EntityBoat.Status getBoatStatus() {
        EntityBoat.Status entityboat_enumstatus = this.getUnderwaterStatus();

        if (entityboat_enumstatus != null) {
            this.waterLevel = this.getEntityBoundingBox().maxY;
            return entityboat_enumstatus;
        } else if (this.checkInWater()) {
            return EntityBoat.Status.IN_WATER;
        } else {
            float f = this.getBoatGlide();

            if (f > 0.0F) {
                this.boatGlide = f;
                return EntityBoat.Status.ON_LAND;
            } else {
                return EntityBoat.Status.IN_AIR;
            }
        }
    }

    public float getWaterLevelAbove() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(axisalignedbb.maxY - this.lastYd);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        try {
            label108:
            for (int k1 = k; k1 < l; ++k1) {
                float f = 0.0F;

                for (int l1 = i; l1 < j; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.setPos(l1, k1, i2);
                        IBlockState iblockdata = this.world.getBlockState(blockposition_pooledblockposition);

                        if (iblockdata.getMaterial() == Material.WATER) {
                            f = Math.max(f, BlockLiquid.getBlockLiquidHeight(iblockdata, this.world, blockposition_pooledblockposition));
                        }

                        if (f >= 1.0F) {
                            continue label108;
                        }
                    }
                }

                if (f < 1.0F) {
                    float f1 = (float) blockposition_pooledblockposition.getY() + f;

                    return f1;
                }
            }

            float f2 = (float) (l + 1);

            return f2;
        } finally {
            blockposition_pooledblockposition.release();
        }
    }

    public float getBoatGlide() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY - 0.001D, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        int i = MathHelper.floor(axisalignedbb1.minX) - 1;
        int j = MathHelper.ceil(axisalignedbb1.maxX) + 1;
        int k = MathHelper.floor(axisalignedbb1.minY) - 1;
        int l = MathHelper.ceil(axisalignedbb1.maxY) + 1;
        int i1 = MathHelper.floor(axisalignedbb1.minZ) - 1;
        int j1 = MathHelper.ceil(axisalignedbb1.maxZ) + 1;
        ArrayList arraylist = Lists.newArrayList();
        float f = 0.0F;
        int k1 = 0;
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int l1 = i; l1 < j; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);

                    if (j2 != 2) {
                        for (int k2 = k; k2 < l; ++k2) {
                            if (j2 <= 0 || k2 != k && k2 != l - 1) {
                                blockposition_pooledblockposition.setPos(l1, k2, i2);
                                IBlockState iblockdata = this.world.getBlockState(blockposition_pooledblockposition);

                                iblockdata.addCollisionBoxToList(this.world, blockposition_pooledblockposition, axisalignedbb1, arraylist, this, false);
                                if (!arraylist.isEmpty()) {
                                    f += iblockdata.getBlock().slipperiness;
                                    ++k1;
                                }

                                arraylist.clear();
                            }
                        }
                    }
                }
            }
        } finally {
            blockposition_pooledblockposition.release();
        }

        return f / (float) k1;
    }

    private boolean checkInWater() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.minY + 0.001D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;

        this.waterLevel = Double.MIN_VALUE;
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.setPos(k1, l1, i2);
                        IBlockState iblockdata = this.world.getBlockState(blockposition_pooledblockposition);

                        if (iblockdata.getMaterial() == Material.WATER) {
                            float f = BlockLiquid.getLiquidHeight(iblockdata, this.world, blockposition_pooledblockposition);

                            this.waterLevel = Math.max((double) f, this.waterLevel);
                            flag |= axisalignedbb.minY < (double) f;
                        }
                    }
                }
            }
        } finally {
            blockposition_pooledblockposition.release();
        }

        return flag;
    }

    @Nullable
    private EntityBoat.Status getUnderwaterStatus() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        double d0 = axisalignedbb.maxY + 0.001D;
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(d0);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.setPos(k1, l1, i2);
                        IBlockState iblockdata = this.world.getBlockState(blockposition_pooledblockposition);

                        if (iblockdata.getMaterial() == Material.WATER && d0 < (double) BlockLiquid.getLiquidHeight(iblockdata, this.world, blockposition_pooledblockposition)) {
                            if (((Integer) iblockdata.getValue(BlockLiquid.LEVEL)).intValue() != 0) {
                                EntityBoat.Status entityboat_enumstatus = EntityBoat.Status.UNDER_FLOWING_WATER;

                                return entityboat_enumstatus;
                            }

                            flag = true;
                        }
                    }
                }
            }

            return flag ? EntityBoat.Status.UNDER_WATER : null;
        } finally {
            blockposition_pooledblockposition.release();
        }
    }

    private void updateMotion() {
        double d0 = -0.03999999910593033D;
        double d1 = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
        double d2 = 0.0D;

        this.momentum = 0.05F;
        if (this.previousStatus == EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.ON_LAND) {
            this.waterLevel = this.getEntityBoundingBox().minY + (double) this.height;
            this.setPosition(this.posX, (double) (this.getWaterLevelAbove() - this.height) + 0.101D, this.posZ);
            this.motionY = 0.0D;
            this.lastYd = 0.0D;
            this.status = EntityBoat.Status.IN_WATER;
        } else {
            if (this.status == EntityBoat.Status.IN_WATER) {
                d2 = (this.waterLevel - this.getEntityBoundingBox().minY) / (double) this.height;
                this.momentum = 0.9F;
            } else if (this.status == EntityBoat.Status.UNDER_FLOWING_WATER) {
                d1 = -7.0E-4D;
                this.momentum = 0.9F;
            } else if (this.status == EntityBoat.Status.UNDER_WATER) {
                d2 = 0.009999999776482582D;
                this.momentum = 0.45F;
            } else if (this.status == EntityBoat.Status.IN_AIR) {
                this.momentum = 0.9F;
            } else if (this.status == EntityBoat.Status.ON_LAND) {
                this.momentum = this.boatGlide;
                if (this.getControllingPassenger() instanceof EntityPlayer) {
                    this.boatGlide /= 2.0F;
                }
            }

            this.motionX *= (double) this.momentum;
            this.motionZ *= (double) this.momentum;
            this.deltaRotation *= this.momentum;
            this.motionY += d1;
            if (d2 > 0.0D) {
                double d3 = 0.65D;

                this.motionY += d2 * 0.06153846016296973D;
                double d4 = 0.75D;

                this.motionY *= 0.75D;
            }
        }

    }

    private void controlBoat() {
        if (this.isBeingRidden()) {
            float f = 0.0F;

            if (this.leftInputDown) {
                this.deltaRotation += -1.0F;
            }

            if (this.rightInputDown) {
                ++this.deltaRotation;
            }

            if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
                f += 0.005F;
            }

            this.rotationYaw += this.deltaRotation;
            if (this.forwardInputDown) {
                f += 0.04F;
            }

            if (this.backInputDown) {
                f -= 0.005F;
            }

            this.motionX += (double) (MathHelper.sin(-this.rotationYaw * 0.017453292F) * f);
            this.motionZ += (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * f);
            this.setPaddleState(this.rightInputDown && !this.leftInputDown || this.forwardInputDown, this.leftInputDown && !this.rightInputDown || this.forwardInputDown);
        }
    }

    public void updatePassenger(Entity entity) {
        if (this.isPassenger(entity)) {
            float f = 0.0F;
            float f1 = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + entity.getYOffset());

            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(entity);

                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }

                if (entity instanceof EntityAnimal) {
                    f = (float) ((double) f + 0.2D);
                }
            }

            Vec3d vec3d = (new Vec3d((double) f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * 0.017453292F - 1.5707964F);

            entity.setPosition(this.posX + vec3d.x, this.posY + (double) f1, this.posZ + vec3d.z);
            entity.rotationYaw += this.deltaRotation;
            entity.setRotationYawHead(entity.getRotationYawHead() + this.deltaRotation);
            this.applyYawToEntity(entity);
            if (entity instanceof EntityAnimal && this.getPassengers().size() > 1) {
                int j = entity.getEntityId() % 2 == 0 ? 90 : 270;

                entity.setRenderYawOffset(((EntityAnimal) entity).renderYawOffset + (float) j);
                entity.setRotationYawHead(entity.getRotationYawHead() + (float) j);
            }

        }
    }

    protected void applyYawToEntity(Entity entity) {
        entity.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entity.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);

        entity.prevRotationYaw += f1 - f;
        entity.rotationYaw += f1 - f;
        entity.setRotationYawHead(entity.rotationYaw);
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Type", this.getBoatType().getName());
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("Type", 8)) {
            this.setBoatType(EntityBoat.Type.getTypeFromString(nbttagcompound.getString("Type")));
        }

    }

    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        if (entityhuman.isSneaking()) {
            return false;
        } else {
            if (!this.world.isRemote && this.outOfControlTicks < 60.0F) {
                entityhuman.startRiding(this);
            }

            return true;
        }
    }

    protected void updateFallState(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {
        this.lastYd = this.motionY;
        if (!this.isRiding()) {
            if (flag) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != EntityBoat.Status.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.fall(this.fallDistance, 1.0F);
                    if (!this.world.isRemote && !this.isDead) {
                    // CraftBukkit start
                    Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, null);
                    this.world.getServer().getPluginManager().callEvent(destroyEvent);
                    if (!destroyEvent.isCancelled()) {
                        this.setDead();
                        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
                            int i;

                            for (i = 0; i < 3; ++i) {
                                this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, this.getBoatType().getMetadata()), 0.0F);
                            }

                            for (i = 0; i < 2; ++i) {
                                this.dropItemWithOffset(Items.STICK, 1, 0.0F);
                            }
                        }
                    }
                    } // CraftBukkit end
                }

                this.fallDistance = 0.0F;
            } else if (this.world.getBlockState((new BlockPos(this)).down()).getMaterial() != Material.WATER && d0 < 0.0D) {
                this.fallDistance = (float) ((double) this.fallDistance - d0);
            }

        }
    }

    public boolean getPaddleState(int i) {
        return ((Boolean) this.dataManager.get(EntityBoat.DATA_ID_PADDLE[i])).booleanValue() && this.getControllingPassenger() != null;
    }

    public void setDamageTaken(float f) {
        this.dataManager.set(EntityBoat.DAMAGE_TAKEN, Float.valueOf(f));
    }

    public float getDamageTaken() {
        return ((Float) this.dataManager.get(EntityBoat.DAMAGE_TAKEN)).floatValue();
    }

    public void setTimeSinceHit(int i) {
        this.dataManager.set(EntityBoat.TIME_SINCE_HIT, Integer.valueOf(i));
    }

    public int getTimeSinceHit() {
        return ((Integer) this.dataManager.get(EntityBoat.TIME_SINCE_HIT)).intValue();
    }

    public void setForwardDirection(int i) {
        this.dataManager.set(EntityBoat.FORWARD_DIRECTION, Integer.valueOf(i));
    }

    public int getForwardDirection() {
        return ((Integer) this.dataManager.get(EntityBoat.FORWARD_DIRECTION)).intValue();
    }

    public void setBoatType(EntityBoat.Type entityboat_enumboattype) {
        this.dataManager.set(EntityBoat.BOAT_TYPE, Integer.valueOf(entityboat_enumboattype.ordinal()));
    }

    public EntityBoat.Type getBoatType() {
        return EntityBoat.Type.byId(((Integer) this.dataManager.get(EntityBoat.BOAT_TYPE)).intValue());
    }

    protected boolean canFitPassenger(Entity entity) {
        return this.getPassengers().size() < 2;
    }

    @Nullable
    public Entity getControllingPassenger() {
        List list = this.getPassengers();

        return list.isEmpty() ? null : (Entity) list.get(0);
    }

    public static enum Type {

        OAK(BlockPlanks.EnumType.OAK.getMetadata(), "oak"), SPRUCE(BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce"), BIRCH(BlockPlanks.EnumType.BIRCH.getMetadata(), "birch"), JUNGLE(BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle"), ACACIA(BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia"), DARK_OAK(BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak");

        private final String name;
        private final int metadata;

        private Type(int i, String s) {
            this.name = s;
            this.metadata = i;
        }

        public String getName() {
            return this.name;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String toString() {
            return this.name;
        }

        public static EntityBoat.Type byId(int i) {
            if (i < 0 || i >= values().length) {
                i = 0;
            }

            return values()[i];
        }

        public static EntityBoat.Type getTypeFromString(String s) {
            for (int i = 0; i < values().length; ++i) {
                if (values()[i].getName().equals(s)) {
                    return values()[i];
                }
            }

            return values()[0];
        }
    }

    public static enum Status {

        IN_WATER, UNDER_WATER, UNDER_FLOWING_WATER, ON_LAND, IN_AIR;

        private Status() {}
    }
}
