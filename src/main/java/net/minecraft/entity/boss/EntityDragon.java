package net.minecraft.entity.boss;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

// CraftBukkit start
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
// CraftBukkit end

// PAIL: Fixme
public class EntityDragon extends EntityLiving implements IEntityMultiPart, IMob {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityDragon.class, DataSerializers.VARINT);
    public double[][] ringBuffer = new double[64][3];
    public int ringBufferIndex = -1;
    public MultiPartEntityPart[] dragonPartArray;
    public MultiPartEntityPart dragonPartHead = new MultiPartEntityPart(this, "head", 6.0F, 6.0F);
    public MultiPartEntityPart dragonPartNeck = new MultiPartEntityPart(this, "neck", 6.0F, 6.0F);
    public MultiPartEntityPart dragonPartBody = new MultiPartEntityPart(this, "body", 8.0F, 8.0F);
    public MultiPartEntityPart dragonPartTail1 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart dragonPartTail2 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart dragonPartTail3 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
    public MultiPartEntityPart dragonPartWing1 = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
    public MultiPartEntityPart dragonPartWing2 = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
    public float prevAnimTime;
    public float animTime;
    public boolean slowed;
    public int deathTicks;
    public EntityEnderCrystal healingEnderCrystal;
    private final DragonFightManager fightManager;
    private final PhaseManager phaseManager;
    private int growlTime = 200;
    private int sittingDamageReceived;
    private final PathPoint[] pathPoints = new PathPoint[24];
    private final int[] neighbors = new int[24];
    private final PathHeap pathFindQueue = new PathHeap();
    private Explosion explosionSource = new Explosion(null, this, Double.NaN, Double.NaN, Double.NaN, Float.NaN, true, true); // CraftBukkit - reusable source for CraftTNTPrimed.getSource()

    public EntityDragon(World world) {
        super(world);
        this.dragonPartArray = new MultiPartEntityPart[] { this.dragonPartHead, this.dragonPartNeck, this.dragonPartBody, this.dragonPartTail1, this.dragonPartTail2, this.dragonPartTail3, this.dragonPartWing1, this.dragonPartWing2};
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0F, 8.0F);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.growlTime = 100;
        this.ignoreFrustumCheck = true;
        if (!world.isRemote && world.provider instanceof WorldProviderEnd) {
            this.fightManager = ((WorldProviderEnd) world.provider).getDragonFightManager();
        } else {
            this.fightManager = null;
        }

        this.phaseManager = new PhaseManager(this);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
    }

    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EntityDragon.PHASE, Integer.valueOf(PhaseList.HOVER.getId()));
    }

    public double[] getMovementOffsets(int i, float f) {
        if (this.getHealth() <= 0.0F) {
            f = 0.0F;
        }

        f = 1.0F - f;
        int j = this.ringBufferIndex - i & 63;
        int k = this.ringBufferIndex - i - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[j][0];
        double d1 = MathHelper.wrapDegrees(this.ringBuffer[k][0] - d0);

        adouble[0] = d0 + d1 * (double) f;
        d0 = this.ringBuffer[j][1];
        d1 = this.ringBuffer[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.ringBuffer[j][2] + (this.ringBuffer[k][2] - this.ringBuffer[j][2]) * (double) f;
        return adouble;
    }

    public void onLivingUpdate() {
        float f;
        float f1;

        if (this.world.isRemote) {
            this.setHealth(this.getHealth());
            if (!this.isSilent()) {
                f = MathHelper.cos(this.animTime * 6.2831855F);
                f1 = MathHelper.cos(this.prevAnimTime * 6.2831855F);
                if (f1 <= -0.3F && f >= -0.3F) {
                    this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, this.getSoundCategory(), 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
                }

                if (!this.phaseManager.getCurrentPhase().getIsStationary() && --this.growlTime < 0) {
                    this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_GROWL, this.getSoundCategory(), 2.5F, 0.8F + this.rand.nextFloat() * 0.3F, false);
                    this.growlTime = 200 + this.rand.nextInt(200);
                }
            }
        }

        this.prevAnimTime = this.animTime;
        float f2;

        if (this.getHealth() <= 0.0F) {
            f = (this.rand.nextFloat() - 0.5F) * 8.0F;
            f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double) f, this.posY + 2.0D + (double) f1, this.posZ + (double) f2, 0.0D, 0.0D, 0.0D, new int[0]);
        } else {
            this.updateDragonEnderCrystal();
            f = 0.2F / (MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
            f *= (float) Math.pow(2.0D, this.motionY);
            if (this.phaseManager.getCurrentPhase().getIsStationary()) {
                this.animTime += 0.1F;
            } else if (this.slowed) {
                this.animTime += f * 0.5F;
            } else {
                this.animTime += f;
            }

            this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
            if (this.isAIDisabled()) {
                this.animTime = 0.5F;
            } else {
                if (this.ringBufferIndex < 0) {
                    for (int i = 0; i < this.ringBuffer.length; ++i) {
                        this.ringBuffer[i][0] = (double) this.rotationYaw;
                        this.ringBuffer[i][1] = this.posY;
                    }
                }

                if (++this.ringBufferIndex == this.ringBuffer.length) {
                    this.ringBufferIndex = 0;
                }

                this.ringBuffer[this.ringBufferIndex][0] = (double) this.rotationYaw;
                this.ringBuffer[this.ringBufferIndex][1] = this.posY;
                double d0;
                double d1;
                double d2;
                float f3;
                float f4;

                if (this.world.isRemote) {
                    if (this.newPosRotationIncrements > 0) {
                        double d3 = this.posX + (this.interpTargetX - this.posX) / (double) this.newPosRotationIncrements;

                        d0 = this.posY + (this.interpTargetY - this.posY) / (double) this.newPosRotationIncrements;
                        d1 = this.posZ + (this.interpTargetZ - this.posZ) / (double) this.newPosRotationIncrements;
                        d2 = MathHelper.wrapDegrees(this.interpTargetYaw - (double) this.rotationYaw);
                        this.rotationYaw = (float) ((double) this.rotationYaw + d2 / (double) this.newPosRotationIncrements);
                        this.rotationPitch = (float) ((double) this.rotationPitch + (this.interpTargetPitch - (double) this.rotationPitch) / (double) this.newPosRotationIncrements);
                        --this.newPosRotationIncrements;
                        this.setPosition(d3, d0, d1);
                        this.setRotation(this.rotationYaw, this.rotationPitch);
                    }

                    this.phaseManager.getCurrentPhase().doClientRenderEffects();
                } else {
                    IPhase idragoncontroller = this.phaseManager.getCurrentPhase();

                    idragoncontroller.doLocalUpdate();
                    if (this.phaseManager.getCurrentPhase() != idragoncontroller) {
                        idragoncontroller = this.phaseManager.getCurrentPhase();
                        idragoncontroller.doLocalUpdate();
                    }

                    Vec3d vec3d = idragoncontroller.getTargetLocation();

                    if (vec3d != null && idragoncontroller.getType() != PhaseList.HOVER) { // CraftBukkit - Don't move when hovering // PAIL: rename
                        d0 = vec3d.x - this.posX;
                        d1 = vec3d.y - this.posY;
                        d2 = vec3d.z - this.posZ;
                        double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                        f3 = idragoncontroller.getMaxRiseOrFall();
                        d1 = MathHelper.clamp(d1 / (double) MathHelper.sqrt(d0 * d0 + d2 * d2), (double) (-f3), (double) f3);
                        this.motionY += d1 * 0.10000000149011612D;
                        this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
                        double d5 = MathHelper.clamp(MathHelper.wrapDegrees(180.0D - MathHelper.atan2(d0, d2) * 57.2957763671875D - (double) this.rotationYaw), -50.0D, 50.0D);
                        Vec3d vec3d1 = (new Vec3d(vec3d.x - this.posX, vec3d.y - this.posY, vec3d.z - this.posZ)).normalize();
                        Vec3d vec3d2 = (new Vec3d((double) MathHelper.sin(this.rotationYaw * 0.017453292F), this.motionY, (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)))).normalize();

                        f4 = Math.max(((float) vec3d2.dotProduct(vec3d1) + 0.5F) / 1.5F, 0.0F);
                        this.randomYawVelocity *= 0.8F;
                        this.randomYawVelocity = (float) ((double) this.randomYawVelocity + d5 * (double) idragoncontroller.getYawFactor());
                        this.rotationYaw += this.randomYawVelocity * 0.1F;
                        float f5 = (float) (2.0D / (d4 + 1.0D));
                        float f6 = 0.06F;

                        this.moveRelative(0.0F, 0.0F, -1.0F, 0.06F * (f4 * f5 + (1.0F - f5)));
                        if (this.slowed) {
                            this.move(MoverType.SELF, this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
                        } else {
                            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                        }

                        Vec3d vec3d3 = (new Vec3d(this.motionX, this.motionY, this.motionZ)).normalize();
                        float f7 = ((float) vec3d3.dotProduct(vec3d2) + 1.0F) / 2.0F;

                        f7 = 0.8F + 0.15F * f7;
                        this.motionX *= (double) f7;
                        this.motionZ *= (double) f7;
                        this.motionY *= 0.9100000262260437D;
                    }
                }

                this.renderYawOffset = this.rotationYaw;
                this.dragonPartHead.width = 1.0F;
                this.dragonPartHead.height = 1.0F;
                this.dragonPartNeck.width = 3.0F;
                this.dragonPartNeck.height = 3.0F;
                this.dragonPartTail1.width = 2.0F;
                this.dragonPartTail1.height = 2.0F;
                this.dragonPartTail2.width = 2.0F;
                this.dragonPartTail2.height = 2.0F;
                this.dragonPartTail3.width = 2.0F;
                this.dragonPartTail3.height = 2.0F;
                this.dragonPartBody.height = 3.0F;
                this.dragonPartBody.width = 5.0F;
                this.dragonPartWing1.height = 2.0F;
                this.dragonPartWing1.width = 4.0F;
                this.dragonPartWing2.height = 3.0F;
                this.dragonPartWing2.width = 4.0F;
                Vec3d[] avec3d = new Vec3d[this.dragonPartArray.length];

                for (int j = 0; j < this.dragonPartArray.length; ++j) {
                    avec3d[j] = new Vec3d(this.dragonPartArray[j].posX, this.dragonPartArray[j].posY, this.dragonPartArray[j].posZ);
                }

                f2 = (float) (this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F * 0.017453292F;
                float f8 = MathHelper.cos(f2);
                float f9 = MathHelper.sin(f2);
                float f10 = this.rotationYaw * 0.017453292F;
                float f11 = MathHelper.sin(f10);
                float f12 = MathHelper.cos(f10);

                this.dragonPartBody.onUpdate();
                this.dragonPartBody.setLocationAndAngles(this.posX + (double) (f11 * 0.5F), this.posY, this.posZ - (double) (f12 * 0.5F), 0.0F, 0.0F);
                this.dragonPartWing1.onUpdate();
                this.dragonPartWing1.setLocationAndAngles(this.posX + (double) (f12 * 4.5F), this.posY + 2.0D, this.posZ + (double) (f11 * 4.5F), 0.0F, 0.0F);
                this.dragonPartWing2.onUpdate();
                this.dragonPartWing2.setLocationAndAngles(this.posX - (double) (f12 * 4.5F), this.posY + 2.0D, this.posZ - (double) (f11 * 4.5F), 0.0F, 0.0F);
                if (!this.world.isRemote && this.hurtTime == 0) {
                    this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                    this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                    this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().grow(1.0D)));
                    this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartNeck.getEntityBoundingBox().grow(1.0D)));
                }

                double[] adouble = this.getMovementOffsets(5, 1.0F);
                float f13 = MathHelper.sin(this.rotationYaw * 0.017453292F - this.randomYawVelocity * 0.01F);
                float f14 = MathHelper.cos(this.rotationYaw * 0.017453292F - this.randomYawVelocity * 0.01F);

                this.dragonPartHead.onUpdate();
                this.dragonPartNeck.onUpdate();
                f3 = this.getHeadYOffset(1.0F);
                this.dragonPartHead.setLocationAndAngles(this.posX + (double) (f13 * 6.5F * f8), this.posY + (double) f3 + (double) (f9 * 6.5F), this.posZ - (double) (f14 * 6.5F * f8), 0.0F, 0.0F);
                this.dragonPartNeck.setLocationAndAngles(this.posX + (double) (f13 * 5.5F * f8), this.posY + (double) f3 + (double) (f9 * 5.5F), this.posZ - (double) (f14 * 5.5F * f8), 0.0F, 0.0F);

                int k;

                for (k = 0; k < 3; ++k) {
                    MultiPartEntityPart entitycomplexpart = null;

                    if (k == 0) {
                        entitycomplexpart = this.dragonPartTail1;
                    }

                    if (k == 1) {
                        entitycomplexpart = this.dragonPartTail2;
                    }

                    if (k == 2) {
                        entitycomplexpart = this.dragonPartTail3;
                    }

                    double[] adouble1 = this.getMovementOffsets(12 + k * 2, 1.0F);
                    float f15 = this.rotationYaw * 0.017453292F + this.simplifyAngle(adouble1[0] - adouble[0]) * 0.017453292F;
                    float f16 = MathHelper.sin(f15);
                    float f17 = MathHelper.cos(f15);
                    float f18 = 1.5F;

                    f4 = (float) (k + 1) * 2.0F;
                    entitycomplexpart.onUpdate();
                    entitycomplexpart.setLocationAndAngles(this.posX - (double) ((f11 * 1.5F + f16 * f4) * f8), this.posY + (adouble1[1] - adouble[1]) - (double) ((f4 + 1.5F) * f9) + 1.5D, this.posZ + (double) ((f12 * 1.5F + f17 * f4) * f8), 0.0F, 0.0F);
                }

                if (!this.world.isRemote) {
                    this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartNeck.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
                    if (this.fightManager != null) {
                        this.fightManager.dragonUpdate(this);
                    }
                }

                for (k = 0; k < this.dragonPartArray.length; ++k) {
                    this.dragonPartArray[k].prevPosX = avec3d[k].x;
                    this.dragonPartArray[k].prevPosY = avec3d[k].y;
                    this.dragonPartArray[k].prevPosZ = avec3d[k].z;
                }

            }
        }
    }

    private float getHeadYOffset(float f) {
        double d0;

        if (this.phaseManager.getCurrentPhase().getIsStationary()) {
            d0 = -1.0D;
        } else {
            double[] adouble = this.getMovementOffsets(5, 1.0F);
            double[] adouble1 = this.getMovementOffsets(0, 1.0F);

            d0 = adouble[1] - adouble1[1];
        }

        return (float) d0;
    }

    private void updateDragonEnderCrystal() {
        if (this.healingEnderCrystal != null) {
            if (this.healingEnderCrystal.isDead) {
                this.healingEnderCrystal = null;
            } else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                // CraftBukkit start
                EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), 1.0F, EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.setHealth((float) (this.getHealth() + event.getAmount()));
                }
                // CraftBukkit end
            }
        }

        if (this.rand.nextInt(10) == 0) {
            List list = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().grow(32.0D));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal) iterator.next();
                double d1 = entityendercrystal1.getDistanceSq(this);

                if (d1 < d0) {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.healingEnderCrystal = entityendercrystal;
        }

    }

    private void collideWithEntities(List<Entity> list) {
        double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0D;
        double d1 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLivingBase) {
                double d2 = entity.posX - d0;
                double d3 = entity.posZ - d1;
                double d4 = d2 * d2 + d3 * d3;

                entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
                if (!this.phaseManager.getCurrentPhase().getIsStationary() && ((EntityLivingBase) entity).getRevengeTimer() < entity.ticksExisted - 2) {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F);
                    this.applyEnchantments((EntityLivingBase) this, entity);
                }
            }
        }

    }

    private void attackEntitiesInList(List<Entity> list) {
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (entity instanceof EntityLivingBase) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
                this.applyEnchantments((EntityLivingBase) this, entity);
            }
        }

    }

    private float simplifyAngle(double d0) {
        return (float) MathHelper.wrapDegrees(d0);
    }

    private boolean destroyBlocksInAABB(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.floor(axisalignedbb.minY);
        int k = MathHelper.floor(axisalignedbb.minZ);
        int l = MathHelper.floor(axisalignedbb.maxX);
        int i1 = MathHelper.floor(axisalignedbb.maxY);
        int j1 = MathHelper.floor(axisalignedbb.maxZ);
        boolean flag = false;
        boolean flag1 = false;
        // CraftBukkit start - Create a list to hold all the destroyed blocks
        List<org.bukkit.block.Block> destroyedBlocks = new java.util.ArrayList<org.bukkit.block.Block>();
        org.bukkit.craftbukkit.CraftWorld craftWorld = this.world.getWorld();
        // CraftBukkit end

        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    BlockPos blockposition = new BlockPos(k1, l1, i2);
                    IBlockState iblockdata = this.world.getBlockState(blockposition);
                    Block block = iblockdata.getBlock();

                    if (iblockdata.getMaterial() != Material.AIR && iblockdata.getMaterial() != Material.FIRE) {
                        if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                            flag = true;
                        } else if (block != Blocks.BARRIER && block != Blocks.OBSIDIAN && block != Blocks.END_STONE && block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME) {
                            if (block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY) {
                                // CraftBukkit start - Add blocks to list rather than destroying them
                                // flag1 = this.world.setAir(blockposition) || flag1;
                                flag1 = true;
                                destroyedBlocks.add(craftWorld.getBlockAt(k1, l1, i2));
                                // CraftBukkit end
                            } else {
                                flag = true;
                            }
                        } else {
                            flag = true;
                        }
                    }
                }
            }
        }

        // CraftBukkit start - Set off an EntityExplodeEvent for the dragon exploding all these blocks
        org.bukkit.entity.Entity bukkitEntity = this.getBukkitEntity();
        EntityExplodeEvent event = new EntityExplodeEvent(bukkitEntity, bukkitEntity.getLocation(), destroyedBlocks, 0F);
        bukkitEntity.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            // This flag literally means 'Dragon hit something hard' (Obsidian, White Stone or Bedrock) and will cause the dragon to slow down.
            // We should consider adding an event extension for it, or perhaps returning true if the event is cancelled.
            return flag;
        } else if (event.getYield() == 0F) {
            // Yield zero ==> no drops
            for (org.bukkit.block.Block block : event.blockList()) {
                this.world.setBlockToAir(new BlockPos(block.getX(), block.getY(), block.getZ()));
            }
        } else {
            for (org.bukkit.block.Block block : event.blockList()) {
                org.bukkit.Material blockId = block.getType();
                if (blockId == org.bukkit.Material.AIR) {
                    continue;
                }

                int blockX = block.getX();
                int blockY = block.getY();
                int blockZ = block.getZ();

                Block nmsBlock = org.bukkit.craftbukkit.util.CraftMagicNumbers.getBlock(blockId);
                if (nmsBlock.canDropFromExplosion(explosionSource)) {
                    nmsBlock.dropBlockAsItemWithChance(this.world, new BlockPos(blockX, blockY, blockZ), nmsBlock.getStateFromMeta(block.getData()), event.getYield(), 0);
                }
                nmsBlock.onBlockDestroyedByExplosion(world, new BlockPos(blockX, blockY, blockZ), explosionSource);

                this.world.setBlockToAir(new BlockPos(blockX, blockY, blockZ));
            }
        }
        // CraftBukkit end

        if (flag1) {
            double d0 = axisalignedbb.minX + (axisalignedbb.maxX - axisalignedbb.minX) * (double) this.rand.nextFloat();
            double d1 = axisalignedbb.minY + (axisalignedbb.maxY - axisalignedbb.minY) * (double) this.rand.nextFloat();
            double d2 = axisalignedbb.minZ + (axisalignedbb.maxZ - axisalignedbb.minZ) * (double) this.rand.nextFloat();

            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        return flag;
    }

    public boolean attackEntityFromPart(MultiPartEntityPart entitycomplexpart, DamageSource damagesource, float f) {
        f = this.phaseManager.getCurrentPhase().getAdjustedDamage(entitycomplexpart, damagesource, f);
        if (entitycomplexpart != this.dragonPartHead) {
            f = f / 4.0F + Math.min(f, 1.0F);
        }

        if (f < 0.01F) {
            return false;
        } else {
            if (damagesource.getTrueSource() instanceof EntityPlayer || damagesource.isExplosion()) {
                float f1 = this.getHealth();

                this.attackDragonFrom(damagesource, f);
                if (this.getHealth() <= 0.0F && !this.phaseManager.getCurrentPhase().getIsStationary()) {
                    this.setHealth(1.0F);
                    this.phaseManager.setPhase(PhaseList.DYING);
                }

                if (this.phaseManager.getCurrentPhase().getIsStationary()) {
                    this.sittingDamageReceived = (int) ((float) this.sittingDamageReceived + (f1 - this.getHealth()));
                    if ((float) this.sittingDamageReceived > 0.25F * this.getMaxHealth()) {
                        this.sittingDamageReceived = 0;
                        this.phaseManager.setPhase(PhaseList.TAKEOFF);
                    }
                }
            }

            return true;
        }
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (damagesource instanceof EntityDamageSource && ((EntityDamageSource) damagesource).getIsThornsDamage()) {
            this.attackEntityFromPart(this.dragonPartBody, damagesource, f);
        }

        return false;
    }

    protected boolean attackDragonFrom(DamageSource damagesource, float f) {
        return super.attackEntityFrom(damagesource, f);
    }

    public void onKillCommand() {
        this.setDead();
        if (this.fightManager != null) {
            this.fightManager.dragonUpdate(this);
            this.fightManager.processDragonDeath(this);
        }

    }

    protected void onDeathUpdate() {
        if (this.fightManager != null) {
            this.fightManager.dragonUpdate(this);
        }

        ++this.deathTicks;
        if (this.deathTicks >= 180 && this.deathTicks <= 200) {
            float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;

            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double) f, this.posY + 2.0D + (double) f1, this.posZ + (double) f2, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        boolean flag = this.world.getGameRules().getBoolean("doMobLoot");
        short short0 = 500;

        if (this.fightManager != null && !this.fightManager.hasPreviouslyKilledDragon()) {
            short0 = 12000;
        }

        if (!this.world.isRemote) {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && flag) {
                this.dropExperience(MathHelper.floor((float) short0 * 0.08F));
            }

            if (this.deathTicks == 1) {
                // CraftBukkit start - Use relative location for far away sounds
                // this.world.a(1028, new BlockPosition(this), 0);
                // Paper start
                //int viewDistance = ((WorldServer) this.world).spigotConfig.viewDistance * 16; // Paper - updated to use worlds actual view distance incase we have to uncomment this due to removal of player view distance API
                for (EntityPlayer human : world.playerEntities) {
                    EntityPlayerMP player = (EntityPlayerMP) human;
                    int viewDistance = player.getViewDistance();
                    // Paper end
                    double deltaX = this.posX - player.posX;
                    double deltaZ = this.posZ - player.posZ;
                    double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    if ( world.spigotConfig.dragonDeathSoundRadius > 0 && distanceSquared > world.spigotConfig.dragonDeathSoundRadius * world.spigotConfig.dragonDeathSoundRadius ) continue; // Spigot
                    if (distanceSquared > viewDistance * viewDistance) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.posX + (deltaX / deltaLength) * viewDistance;
                        double relativeZ = player.posZ + (deltaZ / deltaLength) * viewDistance;
                        player.connection.sendPacket(new SPacketEffect(1028, new BlockPos((int) relativeX, (int) this.posY, (int) relativeZ), 0, true));
                    } else {
                        player.connection.sendPacket(new SPacketEffect(1028, new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ), 0, true));
                    }
                }
                // CraftBukkit end
            }
        }

        this.move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
        this.rotationYaw += 20.0F;
        this.renderYawOffset = this.rotationYaw;
        if (this.deathTicks == 200 && !this.world.isRemote) {
            if (flag) {
                this.dropExperience(MathHelper.floor((float) short0 * 0.2F));
            }

            if (this.fightManager != null) {
                this.fightManager.processDragonDeath(this);
            }

            this.setDead();
        }

    }

    private void dropExperience(int i) {
        while (i > 0) {
            int j = EntityXPOrb.getXPSplit(i);

            i -= j;
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j, org.bukkit.entity.ExperienceOrb.SpawnReason.ENTITY_DEATH, this.attackingPlayer, this)); // Paper
        }

    }

    public int initPathPoints() {
        if (this.pathPoints[0] == null) {
            for (int i = 0; i < 24; ++i) {
                int j = 5;
                int k;
                int l;

                if (i < 12) {
                    k = (int) (60.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.2617994F * (float) i)));
                    l = (int) (60.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.2617994F * (float) i)));
                } else {
                    int i1;

                    if (i < 20) {
                        i1 = i - 12;
                        k = (int) (40.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.3926991F * (float) i1)));
                        l = (int) (40.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.3926991F * (float) i1)));
                        j += 10;
                    } else {
                        i1 = i - 20;
                        k = (int) (20.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.7853982F * (float) i1)));
                        l = (int) (20.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.7853982F * (float) i1)));
                    }
                }

                int j1 = Math.max(this.world.getSeaLevel() + 10, this.world.getTopSolidOrLiquidBlock(new BlockPos(k, 0, l)).getY() + j);

                this.pathPoints[i] = new PathPoint(k, j1, l);
            }

            this.neighbors[0] = 6146;
            this.neighbors[1] = 8197;
            this.neighbors[2] = 8202;
            this.neighbors[3] = 16404;
            this.neighbors[4] = '\u8028';
            this.neighbors[5] = '\u8050';
            this.neighbors[6] = 65696;
            this.neighbors[7] = 131392;
            this.neighbors[8] = 131712;
            this.neighbors[9] = 263424;
            this.neighbors[10] = 526848;
            this.neighbors[11] = 525313;
            this.neighbors[12] = 1581057;
            this.neighbors[13] = 3166214;
            this.neighbors[14] = 2138120;
            this.neighbors[15] = 6373424;
            this.neighbors[16] = 4358208;
            this.neighbors[17] = 12910976;
            this.neighbors[18] = 9044480;
            this.neighbors[19] = 9706496;
            this.neighbors[20] = 15216640;
            this.neighbors[21] = 13688832;
            this.neighbors[22] = 11763712;
            this.neighbors[23] = 8257536;
        }

        return this.getNearestPpIdx(this.posX, this.posY, this.posZ);
    }

    public int getNearestPpIdx(double d0, double d1, double d2) {
        float f = 10000.0F;
        int i = 0;
        PathPoint pathpoint = new PathPoint(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
        byte b0 = 0;

        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
            b0 = 12;
        }

        for (int j = b0; j < 24; ++j) {
            if (this.pathPoints[j] != null) {
                float f1 = this.pathPoints[j].distanceToSquared(pathpoint);

                if (f1 < f) {
                    f = f1;
                    i = j;
                }
            }
        }

        return i;
    }

    @Nullable
    public Path findPath(int i, int j, @Nullable PathPoint pathpoint) {
        PathPoint pathpoint1;

        for (int k = 0; k < 24; ++k) {
            pathpoint1 = this.pathPoints[k];
            pathpoint1.visited = false;
            pathpoint1.distanceToTarget = 0.0F;
            pathpoint1.totalPathDistance = 0.0F;
            pathpoint1.distanceToNext = 0.0F;
            pathpoint1.previous = null;
            pathpoint1.index = -1;
        }

        PathPoint pathpoint2 = this.pathPoints[i];

        pathpoint1 = this.pathPoints[j];
        pathpoint2.totalPathDistance = 0.0F;
        pathpoint2.distanceToNext = pathpoint2.distanceTo(pathpoint1);
        pathpoint2.distanceToTarget = pathpoint2.distanceToNext;
        this.pathFindQueue.clearPath();
        this.pathFindQueue.addPoint(pathpoint2);
        PathPoint pathpoint3 = pathpoint2;
        byte b0 = 0;

        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
            b0 = 12;
        }

        label70:
        while (!this.pathFindQueue.isPathEmpty()) {
            PathPoint pathpoint4 = this.pathFindQueue.dequeue();

            if (pathpoint4.equals(pathpoint1)) {
                if (pathpoint != null) {
                    pathpoint.previous = pathpoint1;
                    pathpoint1 = pathpoint;
                }

                return this.makePath(pathpoint2, pathpoint1);
            }

            if (pathpoint4.distanceTo(pathpoint1) < pathpoint3.distanceTo(pathpoint1)) {
                pathpoint3 = pathpoint4;
            }

            pathpoint4.visited = true;
            int l = 0;
            int i1 = 0;

            while (true) {
                if (i1 < 24) {
                    if (this.pathPoints[i1] != pathpoint4) {
                        ++i1;
                        continue;
                    }

                    l = i1;
                }

                i1 = b0;

                while (true) {
                    if (i1 >= 24) {
                        continue label70;
                    }

                    if ((this.neighbors[l] & 1 << i1) > 0) {
                        PathPoint pathpoint5 = this.pathPoints[i1];

                        if (!pathpoint5.visited) {
                            float f = pathpoint4.totalPathDistance + pathpoint4.distanceTo(pathpoint5);

                            if (!pathpoint5.isAssigned() || f < pathpoint5.totalPathDistance) {
                                pathpoint5.previous = pathpoint4;
                                pathpoint5.totalPathDistance = f;
                                pathpoint5.distanceToNext = pathpoint5.distanceTo(pathpoint1);
                                if (pathpoint5.isAssigned()) {
                                    this.pathFindQueue.changeDistance(pathpoint5, pathpoint5.totalPathDistance + pathpoint5.distanceToNext);
                                } else {
                                    pathpoint5.distanceToTarget = pathpoint5.totalPathDistance + pathpoint5.distanceToNext;
                                    this.pathFindQueue.addPoint(pathpoint5);
                                }
                            }
                        }
                    }

                    ++i1;
                }
            }
        }

        if (pathpoint3 == pathpoint2) {
            return null;
        } else {
            EntityDragon.LOGGER.debug("Failed to find path from {} to {}", Integer.valueOf(i), Integer.valueOf(j));
            if (pathpoint != null) {
                pathpoint.previous = pathpoint3;
                pathpoint3 = pathpoint;
            }

            return this.makePath(pathpoint2, pathpoint3);
        }
    }

    private Path makePath(PathPoint pathpoint, PathPoint pathpoint1) {
        int i = 1;

        PathPoint pathpoint2;

        for (pathpoint2 = pathpoint1; pathpoint2.previous != null; pathpoint2 = pathpoint2.previous) {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];

        pathpoint2 = pathpoint1;
        --i;

        for (apathpoint[i] = pathpoint1; pathpoint2.previous != null; apathpoint[i] = pathpoint2) {
            pathpoint2 = pathpoint2.previous;
            --i;
        }

        return new Path(apathpoint);
    }

    public static void registerFixesDragon(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityDragon.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("DragonPhase", this.phaseManager.getCurrentPhase().getType().getId());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("DragonPhase")) {
            this.phaseManager.setPhase(PhaseList.getById(nbttagcompound.getInteger("DragonPhase")));
        }

    }

    protected void despawnEntity() {}

    public Entity[] getParts() {
        return this.dragonPartArray;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public World getWorld() {
        return this.world;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERDRAGON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_ENDERDRAGON_HURT;
    }

    protected float getSoundVolume() {
        return 5.0F;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ENDER_DRAGON;
    }

    public Vec3d getHeadLookVec(float f) {
        IPhase idragoncontroller = this.phaseManager.getCurrentPhase();
        PhaseList dragoncontrollerphase = idragoncontroller.getType();
        Vec3d vec3d;
        float f1;

        if (dragoncontrollerphase != PhaseList.LANDING && dragoncontrollerphase != PhaseList.TAKEOFF) {
            if (idragoncontroller.getIsStationary()) {
                float f2 = this.rotationPitch;

                f1 = 1.5F;
                this.rotationPitch = -45.0F;
                vec3d = this.getLook(f);
                this.rotationPitch = f2;
            } else {
                vec3d = this.getLook(f);
            }
        } else {
            BlockPos blockposition = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);

            f1 = Math.max(MathHelper.sqrt(this.getDistanceSqToCenter(blockposition)) / 4.0F, 1.0F);
            float f3 = 6.0F / f1;
            float f4 = this.rotationPitch;
            float f5 = 1.5F;

            this.rotationPitch = -f3 * 1.5F * 5.0F;
            vec3d = this.getLook(f);
            this.rotationPitch = f4;
        }

        return vec3d;
    }

    public void onCrystalDestroyed(EntityEnderCrystal entityendercrystal, BlockPos blockposition, DamageSource damagesource) {
        EntityPlayer entityhuman;

        if (damagesource.getTrueSource() instanceof EntityPlayer) {
            entityhuman = (EntityPlayer) damagesource.getTrueSource();
        } else {
            entityhuman = this.world.getNearestAttackablePlayer(blockposition, 64.0D, 64.0D);
        }

        if (entityendercrystal == this.healingEnderCrystal) {
            this.attackEntityFromPart(this.dragonPartHead, DamageSource.causeExplosionDamage(entityhuman), 10.0F);
        }

        this.phaseManager.getCurrentPhase().onCrystalDestroyed(entityendercrystal, blockposition, damagesource, entityhuman);
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (EntityDragon.PHASE.equals(datawatcherobject) && this.world.isRemote) {
            this.phaseManager.setPhase(PhaseList.getById(((Integer) this.getDataManager().get(EntityDragon.PHASE)).intValue()));
        }

        super.notifyDataManagerChange(datawatcherobject);
    }

    public PhaseManager getPhaseManager() {
        return this.phaseManager;
    }

    @Nullable
    public DragonFightManager getFightManager() {
        return this.fightManager;
    }

    public void addPotionEffect(PotionEffect mobeffect) {}

    protected boolean canBeRidden(Entity entity) {
        return false;
    }

    public boolean isNonBoss() {
        return false;
    }
}
