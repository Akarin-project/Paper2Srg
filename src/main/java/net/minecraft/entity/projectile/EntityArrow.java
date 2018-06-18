package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;

// CraftBukkit start
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
// CraftBukkit end

public abstract class EntityArrow extends Entity implements IProjectile {

    private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity.canBeCollidedWith();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    }});
    private static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(EntityArrow.class, DataSerializers.BYTE);
    public int xTile; // PAIL: private->public
    public int yTile; // PAIL: private->public
    public int zTile; // PAIL: private->public
    private Block inTile;
    private int inData;
    public boolean inGround;
    protected int timeInGround;
    public EntityArrow.PickupStatus pickupStatus;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage;
    public int knockbackStrength;

    // Spigot Start
    @Override
    public void inactiveTick()
    {
        if ( this.inGround )
        {
            this.ticksInGround += 1; // Despawn counter. First int after shooter
        }
        super.inactiveTick();
    }
    // Spigot End

    public EntityArrow(World world) {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        this.damage = 2.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntityArrow(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1, d2);
    }

    public EntityArrow(World world, EntityLivingBase entityliving) {
        this(world, entityliving.posX, entityliving.posY + (double) entityliving.getEyeHeight() - 0.10000000149011612D, entityliving.posZ);
        this.shootingEntity = entityliving;
        this.projectileSource = (LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
        if (entityliving instanceof EntityPlayer) {
            this.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
        }

    }

    protected void entityInit() {
        this.dataManager.register(EntityArrow.CRITICAL, Byte.valueOf((byte) 0));
    }

    public void shoot(Entity entity, float f, float f1, float f2, float f3, float f4) {
        float f5 = -MathHelper.sin(f1 * 0.017453292F) * MathHelper.cos(f * 0.017453292F);
        float f6 = -MathHelper.sin(f * 0.017453292F);
        float f7 = MathHelper.cos(f1 * 0.017453292F) * MathHelper.cos(f * 0.017453292F);

        this.shoot((double) f5, (double) f6, (double) f7, f3, f4);
        this.motionX += entity.motionX;
        this.motionZ += entity.motionZ;
        if (!entity.onGround) {
            this.motionY += entity.motionY;
        }

    }

    public void shoot(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= (double) f2;
        d1 /= (double) f2;
        d2 /= (double) f2;
        d0 += this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
        d1 += this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
        d2 += this.rand.nextGaussian() * 0.007499999832361937D * (double) f1;
        d0 *= (double) f;
        d1 *= (double) f;
        d2 *= (double) f;
        this.motionX = d0;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        this.rotationYaw = (float) (MathHelper.atan2(d0, d2) * 57.2957763671875D);
        this.rotationPitch = (float) (MathHelper.atan2(d1, (double) f3) * 57.2957763671875D);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.ticksInGround = 0;
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.2957763671875D);
            this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * 57.2957763671875D);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        BlockPos blockposition = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockdata = this.world.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        if (iblockdata.getMaterial() != Material.AIR) {
            AxisAlignedBB axisalignedbb = iblockdata.getCollisionBoundingBox(this.world, blockposition);

            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockposition).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int i = block.getMetaFromState(iblockdata);

            if ((block != this.inTile || i != this.inData) && !this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(0.05D))) {
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            } else {
                ++this.ticksInGround;
                if (this.ticksInGround >= (pickupStatus != PickupStatus.DISALLOWED ? world.spigotConfig.arrowDespawnRate : world.paperConfig.nonPlayerArrowDespawnRate)) { // Spigot - First int after shooter // Paper
                    this.setDead();
                }
            }

            ++this.timeInGround;
        } else {
            this.timeInGround = 0;
            ++this.ticksInAir;
            Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3d, vec3d1, false, true, false);

            vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec3d1 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
            }

            Entity entity = this.findEntityOnPath(vec3d, vec3d1);

            if (entity != null) {
                movingobjectposition = new RayTraceResult(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) movingobjectposition.entityHit;

                if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityhuman)) {
                    movingobjectposition = null;
                }
            }

            // Paper start - Call ProjectileCollideEvent
            if (movingobjectposition != null && movingobjectposition.entityHit != null) {
                com.destroystokyo.paper.event.entity.ProjectileCollideEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileCollideEvent(this, movingobjectposition);
                if (event.isCancelled()) {
                    movingobjectposition = null;
                }
            }
            // Paper end

            if (movingobjectposition != null) {
                this.onHit(movingobjectposition);
            }

            if (this.getIsCritical()) {
                for (int j = 0; j < 4; ++j) {
                    this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double) j / 4.0D, this.posY + this.motionY * (double) j / 4.0D, this.posZ + this.motionZ * (double) j / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.2957763671875D);

            for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f1) * 57.2957763671875D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f2 = 0.99F;
            float f3 = 0.05F;

            if (this.isInWater()) {
                for (int k = 0; k < 4; ++k) {
                    float f4 = 0.25F;

                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
                }

                f2 = 0.6F;
            }

            if (this.isWet()) {
                this.extinguish();
            }

            this.motionX *= (double) f2;
            this.motionY *= (double) f2;
            this.motionZ *= (double) f2;
            if (!this.hasNoGravity()) {
                this.motionY -= 0.05000000074505806D;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    protected void onHit(RayTraceResult movingobjectposition) {
        Entity entity = movingobjectposition.entityHit;
        org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition); // CraftBukkit - Call event
        if (entity != null) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int i = MathHelper.ceil((double) f * this.damage);

            if (this.getIsCritical()) {
                i += this.rand.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (this.shootingEntity == null) {
                damagesource = DamageSource.causeArrowDamage(this, this);
            } else {
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }

            if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                // CraftBukkit start
                EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 5);
                org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);
                if (!combustEvent.isCancelled()) {
                    entity.setFire(combustEvent.getDuration());
                }
                // CraftBukkit end
            }

            if (entity.attackEntityFrom(damagesource, (float) i)) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase entityliving = (EntityLivingBase) entity;

                    if (!this.world.isRemote) {
                        entityliving.setArrowCountInEntity(entityliving.getArrowCountInEntity() + 1);
                    }

                    if (this.knockbackStrength > 0) {
                        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                        if (f1 > 0.0F) {
                            entityliving.addVelocity(this.motionX * (double) this.knockbackStrength * 0.6000000238418579D / (double) f1, 0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D / (double) f1);
                        }
                    }

                    if (this.shootingEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(entityliving, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, (Entity) entityliving);
                    }

                    this.arrowHit(entityliving);
                    if (this.shootingEntity != null && entityliving != this.shootingEntity && entityliving instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }
                }

                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                if (!(entity instanceof EntityEnderman)) {
                    this.setDead();
                }
            } else {
                this.motionX *= -0.10000000149011612D;
                this.motionY *= -0.10000000149011612D;
                this.motionZ *= -0.10000000149011612D;
                this.rotationYaw += 180.0F;
                this.prevRotationYaw += 180.0F;
                this.ticksInAir = 0;
                if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D) {
                    if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED) {
                        this.entityDropItem(this.getArrowStack(), 0.1F);
                    }

                    this.setDead();
                }
            }
        } else {
            BlockPos blockposition = movingobjectposition.getBlockPos();

            this.xTile = blockposition.getX();
            this.yTile = blockposition.getY();
            this.zTile = blockposition.getZ();
            IBlockState iblockdata = this.world.getBlockState(blockposition);

            this.inTile = iblockdata.getBlock();
            this.inData = this.inTile.getMetaFromState(iblockdata);
            this.motionX = (double) ((float) (movingobjectposition.hitVec.x - this.posX));
            this.motionY = (double) ((float) (movingobjectposition.hitVec.y - this.posY));
            this.motionZ = (double) ((float) (movingobjectposition.hitVec.z - this.posZ));
            float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);

            this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
            this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
            this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
            this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.inGround = true;
            this.arrowShake = 7;
            this.setIsCritical(false);
            if (iblockdata.getMaterial() != Material.AIR) {
                this.inTile.onEntityCollidedWithBlock(this.world, blockposition, iblockdata, (Entity) this);
            }
        }

    }

    public void move(MoverType enummovetype, double d0, double d1, double d2) {
        super.move(enummovetype, d0, d1, d2);
        if (this.inGround) {
            this.xTile = MathHelper.floor(this.posX);
            this.yTile = MathHelper.floor(this.posY);
            this.zTile = MathHelper.floor(this.posZ);
        }

    }

    protected void arrowHit(EntityLivingBase entityliving) {}

    @Nullable
    protected Entity findEntityOnPath(Vec3d vec3d, Vec3d vec3d1) {
        Entity entity = null;
        List list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), EntityArrow.ARROW_TARGETS);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1 != this.shootingEntity || this.ticksInAir >= 5) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d1);

                if (movingobjectposition != null) {
                    double d1 = vec3d.squareDistanceTo(movingobjectposition.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    public static void registerFixesArrow(DataFixer dataconvertermanager, String s) {}

    public static void registerFixesArrow(DataFixer dataconvertermanager) {
        registerFixesArrow(dataconvertermanager, "Arrow");
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("xTile", this.xTile);
        nbttagcompound.setInteger("yTile", this.yTile);
        nbttagcompound.setInteger("zTile", this.zTile);
        nbttagcompound.setShort("life", (short) this.ticksInGround);
        ResourceLocation minecraftkey = (ResourceLocation) Block.REGISTRY.getNameForObject(this.inTile);

        nbttagcompound.setString("inTile", minecraftkey == null ? "" : minecraftkey.toString());
        nbttagcompound.setByte("inData", (byte) this.inData);
        nbttagcompound.setByte("shake", (byte) this.arrowShake);
        nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        nbttagcompound.setByte("pickup", (byte) this.pickupStatus.ordinal());
        nbttagcompound.setDouble("damage", this.damage);
        nbttagcompound.setBoolean("crit", this.getIsCritical());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.xTile = nbttagcompound.getInteger("xTile");
        this.yTile = nbttagcompound.getInteger("yTile");
        this.zTile = nbttagcompound.getInteger("zTile");
        this.ticksInGround = nbttagcompound.getShort("life");
        if (nbttagcompound.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(nbttagcompound.getString("inTile"));
        } else {
            this.inTile = Block.getBlockById(nbttagcompound.getByte("inTile") & 255);
        }

        this.inData = nbttagcompound.getByte("inData") & 255;
        this.arrowShake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
        if (nbttagcompound.hasKey("damage", 99)) {
            this.damage = nbttagcompound.getDouble("damage");
        }

        if (nbttagcompound.hasKey("pickup", 99)) {
            this.pickupStatus = EntityArrow.PickupStatus.getByOrdinal(nbttagcompound.getByte("pickup"));
        } else if (nbttagcompound.hasKey("player", 99)) {
            this.pickupStatus = nbttagcompound.getBoolean("player") ? EntityArrow.PickupStatus.ALLOWED : EntityArrow.PickupStatus.DISALLOWED;
        }

        this.setIsCritical(nbttagcompound.getBoolean("crit"));
    }

    public void onCollideWithPlayer(EntityPlayer entityhuman) {
        if (!this.world.isRemote && this.inGround && this.arrowShake <= 0) {
            // CraftBukkit start
            ItemStack itemstack = this.getArrowStack(); // PAIL: rename
            EntityItem item = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemstack);
            if (this.pickupStatus == PickupStatus.ALLOWED && entityhuman.inventory.canHold(itemstack) > 0) {
                PlayerPickupArrowEvent event = new PlayerPickupArrowEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), new org.bukkit.craftbukkit.entity.CraftItem(this.world.getServer(), this, item), (org.bukkit.entity.Arrow) this.getBukkitEntity());
                // event.setCancelled(!entityhuman.canPickUpLoot); TODO
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
            }
            boolean flag = this.pickupStatus == EntityArrow.PickupStatus.ALLOWED || this.pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && entityhuman.capabilities.isCreativeMode;

            if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED && !entityhuman.inventory.addItemStackToInventory(item.getItem())) {
                // CraftBukkit end
                flag = false;
            }

            if (flag) {
                entityhuman.onItemPickup(this, 1);
                this.setDead();
            }

        }
    }

    protected abstract ItemStack getArrowStack();

    protected boolean canTriggerWalking() {
        return false;
    }

    public void setDamage(double d0) {
        this.damage = d0;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int i) {
        this.knockbackStrength = i;
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }

    public float getEyeHeight() {
        return 0.0F;
    }

    public void setIsCritical(boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntityArrow.CRITICAL)).byteValue();

        if (flag) {
            this.dataManager.set(EntityArrow.CRITICAL, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.dataManager.set(EntityArrow.CRITICAL, Byte.valueOf((byte) (b0 & -2)));
        }

    }

    public boolean getIsCritical() {
        byte b0 = ((Byte) this.dataManager.get(EntityArrow.CRITICAL)).byteValue();

        return (b0 & 1) != 0;
    }

    public void setEnchantmentEffectsFromEntity(EntityLivingBase entityliving, float f) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, entityliving);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, entityliving);

        this.setDamage((double) (f * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.world.getDifficulty().getDifficultyId() * 0.11F));
        if (i > 0) {
            this.setDamage(this.getDamage() + (double) i * 0.5D + 0.5D);
        }

        if (j > 0) {
            this.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, entityliving) > 0) {
            // CraftBukkit start - call EntityCombustEvent
            EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 100);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.setFire(event.getDuration());
            }
            // CraftBukkit end
        }

    }

    public static enum PickupStatus {

        DISALLOWED, ALLOWED, CREATIVE_ONLY;

        private PickupStatus() {}

        public static EntityArrow.PickupStatus getByOrdinal(int i) {
            if (i < 0 || i > values().length) {
                i = 0;
            }

            return values()[i];
        }
    }
}
