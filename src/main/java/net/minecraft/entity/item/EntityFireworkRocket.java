package net.minecraft.entity.item;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityFireworkRocket extends Entity {

    public static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Integer> BOOSTED_ENTITY_ID = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.VARINT);
    private int fireworkAge;
    public int lifetime;
    public UUID spawningEntity; // Paper
    private EntityLivingBase boostedEntity;public EntityLivingBase getBoostedEntity() { return boostedEntity; } // Paper - OBFHELPER

    public EntityFireworkRocket(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    // Spigot Start
    @Override
    public void inactiveTick() {
        this.fireworkAge += 1;
        super.inactiveTick();
    }
    // Spigot End

    protected void entityInit() {
        this.dataManager.register(EntityFireworkRocket.FIREWORK_ITEM, ItemStack.EMPTY);
        this.dataManager.register(EntityFireworkRocket.BOOSTED_ENTITY_ID, Integer.valueOf(0));
    }

    public EntityFireworkRocket(World world, double d0, double d1, double d2, ItemStack itemstack) {
        super(world);
        this.fireworkAge = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(d0, d1, d2);
        int i = 1;

        if (!itemstack.isEmpty() && itemstack.hasTagCompound()) {
            this.dataManager.set(EntityFireworkRocket.FIREWORK_ITEM, itemstack.copy());
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");

            i += nbttagcompound1.getByte("Flight");
        }

        this.motionX = this.rand.nextGaussian() * 0.001D;
        this.motionZ = this.rand.nextGaussian() * 0.001D;
        this.motionY = 0.05D;
        this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
    }

    public EntityFireworkRocket(World world, ItemStack itemstack, EntityLivingBase entityliving) {
        this(world, entityliving.posX, entityliving.posY, entityliving.posZ, itemstack);
        this.dataManager.set(EntityFireworkRocket.BOOSTED_ENTITY_ID, Integer.valueOf(entityliving.getEntityId()));
        this.boostedEntity = entityliving;
    }

    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.isAttachedToEntity()) {
            if (this.boostedEntity == null) {
                Entity entity = this.world.getEntityByID(((Integer) this.dataManager.get(EntityFireworkRocket.BOOSTED_ENTITY_ID)).intValue());

                if (entity instanceof EntityLivingBase) {
                    this.boostedEntity = (EntityLivingBase) entity;
                }
            }

            if (this.boostedEntity != null) {
                if (this.boostedEntity.isElytraFlying()) {
                    Vec3d vec3d = this.boostedEntity.getLookVec();
                    double d0 = 1.5D;
                    double d1 = 0.1D;

                    this.boostedEntity.motionX += vec3d.x * 0.1D + (vec3d.x * 1.5D - this.boostedEntity.motionX) * 0.5D;
                    this.boostedEntity.motionY += vec3d.y * 0.1D + (vec3d.y * 1.5D - this.boostedEntity.motionY) * 0.5D;
                    this.boostedEntity.motionZ += vec3d.z * 0.1D + (vec3d.z * 1.5D - this.boostedEntity.motionZ) * 0.5D;
                }

                this.setPosition(this.boostedEntity.posX, this.boostedEntity.posY, this.boostedEntity.posZ);
                this.motionX = this.boostedEntity.motionX;
                this.motionY = this.boostedEntity.motionY;
                this.motionZ = this.boostedEntity.motionZ;
            }
        } else {
            this.motionX *= 1.15D;
            this.motionZ *= 1.15D;
            this.motionY += 0.04D;
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }

        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.2957763671875D);

        for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * 57.2957763671875D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
        if (this.fireworkAge == 0 && !this.isSilent()) {
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++this.fireworkAge;
        if (this.world.isRemote && this.fireworkAge % 2 < 2) {
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D, new int[0]);
        }

        if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
            // CraftBukkit start
            if (!org.bukkit.craftbukkit.event.CraftEventFactory.callFireworkExplodeEvent(this).isCancelled()) {
                this.world.setEntityState(this, (byte) 17);
                this.dealExplosionDamage();
            }
            // CraftBukkit end
            this.setDead();
        }

    }

    private void dealExplosionDamage() {
        float f = 0.0F;
        ItemStack itemstack = (ItemStack) this.dataManager.get(EntityFireworkRocket.FIREWORK_ITEM);
        NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
        NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.getTagList("Explosions", 10) : null;

        if (nbttaglist != null && !nbttaglist.hasNoTags()) {
            f = (float) (5 + nbttaglist.tagCount() * 2);
        }

        if (f > 0.0F) {
            if (this.boostedEntity != null) {
                CraftEventFactory.entityDamage = this; // CraftBukkit
                this.boostedEntity.attackEntityFrom(DamageSource.FIREWORKS, (float) (5 + nbttaglist.tagCount() * 2));
                CraftEventFactory.entityDamage = null; // CraftBukkit
            }

            double d0 = 5.0D;
            Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            List list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(5.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                if (entityliving != this.boostedEntity && this.getDistanceSq(entityliving) <= 25.0D) {
                    boolean flag = false;

                    for (int i = 0; i < 2; ++i) {
                        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3d, new Vec3d(entityliving.posX, entityliving.posY + (double) entityliving.height * 0.5D * (double) i, entityliving.posZ), false, true, false);

                        if (movingobjectposition == null || movingobjectposition.typeOfHit == RayTraceResult.Type.MISS) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        float f1 = f * (float) Math.sqrt((5.0D - (double) this.getDistance(entityliving)) / 5.0D);

                        CraftEventFactory.entityDamage = this; // CraftBukkit
                        entityliving.attackEntityFrom(DamageSource.FIREWORKS, f1);
                        CraftEventFactory.entityDamage = null; // CraftBukkit
                    }
                }
            }
        }

    }

    public boolean isAttachedToEntity() {
        return ((Integer) this.dataManager.get(EntityFireworkRocket.BOOSTED_ENTITY_ID)).intValue() > 0;
    }

    public static void registerFixesFireworkRocket(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityFireworkRocket.class, new String[] { "FireworksItem"})));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("Life", this.fireworkAge);
        nbttagcompound.setInteger("LifeTime", this.lifetime);
        ItemStack itemstack = (ItemStack) this.dataManager.get(EntityFireworkRocket.FIREWORK_ITEM);

        if (!itemstack.isEmpty()) {
            nbttagcompound.setTag("FireworksItem", itemstack.writeToNBT(new NBTTagCompound()));
        }
        // Paper start
        if (spawningEntity != null) {
            nbttagcompound.setUUID("SpawningEntity", spawningEntity);
        }
        // Paper end

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.fireworkAge = nbttagcompound.getInteger("Life");
        this.lifetime = nbttagcompound.getInteger("LifeTime");
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("FireworksItem");

        if (nbttagcompound1 != null) {
            ItemStack itemstack = new ItemStack(nbttagcompound1);

            if (!itemstack.isEmpty()) {
                this.dataManager.set(EntityFireworkRocket.FIREWORK_ITEM, itemstack);
            }
        }
        // Paper start
        if (nbttagcompound.hasUUID("SpawningEntity")) {
            spawningEntity = nbttagcompound.getUUID("SpawningEntity");
        }
        // Paper end
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }
}
