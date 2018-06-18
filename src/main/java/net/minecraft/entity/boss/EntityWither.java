package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.EntityWither.a;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityWither extends EntityMob implements IRangedAttackMob {

    private static final DataParameter<Integer> FIRST_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SECOND_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> THIRD_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer>[] HEAD_TARGETS = new DataParameter[] { EntityWither.FIRST_HEAD_TARGET, EntityWither.SECOND_HEAD_TARGET, EntityWither.THIRD_HEAD_TARGET};
    private static final DataParameter<Integer> INVULNERABILITY_TIME = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private final float[] xRotationHeads = new float[2];
    private final float[] yRotationHeads = new float[2];
    private final float[] xRotOHeads = new float[2];
    private final float[] yRotOHeads = new float[2];
    private final int[] nextHeadUpdate = new int[2];
    private final int[] idleHeadUpdates = new int[2];
    private int blockBreakCounter;
    private final BossInfoServer bossInfo;
    private static final Predicate<Entity> NOT_UNDEAD = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD && ((EntityLivingBase) entity).attackable();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };

    public EntityWither(World world) {
        super(world);
        this.bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
        this.setHealth(this.getMaxHealth());
        this.setSize(0.9F, 3.5F);
        this.isImmuneToFire = true;
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.experienceValue = 50;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityWither.a());
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 40, 20.0F));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, EntityWither.NOT_UNDEAD));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityWither.FIRST_HEAD_TARGET, Integer.valueOf(0));
        this.dataManager.register(EntityWither.SECOND_HEAD_TARGET, Integer.valueOf(0));
        this.dataManager.register(EntityWither.THIRD_HEAD_TARGET, Integer.valueOf(0));
        this.dataManager.register(EntityWither.INVULNERABILITY_TIME, Integer.valueOf(0));
    }

    public static void registerFixesWither(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityWither.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Invul", this.getInvulTime());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setInvulTime(nbttagcompound.getInteger("Invul"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }

    }

    public void setCustomNameTag(String s) {
        super.setCustomNameTag(s);
        this.bossInfo.setName(this.getDisplayName());
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_DEATH;
    }

    public void onLivingUpdate() {
        this.motionY *= 0.6000000238418579D;
        double d0;
        double d1;
        double d2;

        if (!this.world.isRemote && this.getWatchedTargetId(0) > 0) {
            Entity entity = this.world.getEntityByID(this.getWatchedTargetId(0));

            if (entity != null) {
                if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0D) {
                    if (this.motionY < 0.0D) {
                        this.motionY = 0.0D;
                    }

                    this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
                }

                double d3 = entity.posX - this.posX;

                d0 = entity.posZ - this.posZ;
                d1 = d3 * d3 + d0 * d0;
                if (d1 > 9.0D) {
                    d2 = (double) MathHelper.sqrt(d1);
                    this.motionX += (d3 / d2 * 0.5D - this.motionX) * 0.6000000238418579D;
                    this.motionZ += (d0 / d2 * 0.5D - this.motionZ) * 0.6000000238418579D;
                }
            }
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D) {
            this.rotationYaw = (float) MathHelper.atan2(this.motionZ, this.motionX) * 57.295776F - 90.0F;
        }

        super.onLivingUpdate();

        int i;

        for (i = 0; i < 2; ++i) {
            this.yRotOHeads[i] = this.yRotationHeads[i];
            this.xRotOHeads[i] = this.xRotationHeads[i];
        }

        int j;

        for (i = 0; i < 2; ++i) {
            j = this.getWatchedTargetId(i + 1);
            Entity entity1 = null;

            if (j > 0) {
                entity1 = this.world.getEntityByID(j);
            }

            if (entity1 != null) {
                d0 = this.getHeadX(i + 1);
                d1 = this.getHeadY(i + 1);
                d2 = this.getHeadZ(i + 1);
                double d4 = entity1.posX - d0;
                double d5 = entity1.posY + (double) entity1.getEyeHeight() - d1;
                double d6 = entity1.posZ - d2;
                double d7 = (double) MathHelper.sqrt(d4 * d4 + d6 * d6);
                float f = (float) (MathHelper.atan2(d6, d4) * 57.2957763671875D) - 90.0F;
                float f1 = (float) (-(MathHelper.atan2(d5, d7) * 57.2957763671875D));

                this.xRotationHeads[i] = this.rotlerp(this.xRotationHeads[i], f1, 40.0F);
                this.yRotationHeads[i] = this.rotlerp(this.yRotationHeads[i], f, 10.0F);
            } else {
                this.yRotationHeads[i] = this.rotlerp(this.yRotationHeads[i], this.renderYawOffset, 10.0F);
            }
        }

        boolean flag = this.isArmored();

        for (j = 0; j < 3; ++j) {
            double d8 = this.getHeadX(j);
            double d9 = this.getHeadY(j);
            double d10 = this.getHeadZ(j);

            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d8 + this.rand.nextGaussian() * 0.30000001192092896D, d9 + this.rand.nextGaussian() * 0.30000001192092896D, d10 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
            if (flag && this.world.rand.nextInt(4) == 0) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, d8 + this.rand.nextGaussian() * 0.30000001192092896D, d9 + this.rand.nextGaussian() * 0.30000001192092896D, d10 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
            }
        }

        if (this.getInvulTime() > 0) {
            for (j = 0; j < 3; ++j) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian(), this.posY + (double) (this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian(), 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
            }
        }

    }

    protected void updateAITasks() {
        int i;

        if (this.getInvulTime() > 0) {
            i = this.getInvulTime() - 1;
            if (i <= 0) {
                // CraftBukkit start
                // this.world.createExplosion(this, this.locX, this.locY + (double) this.getHeadHeight(), this.locZ, 7.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
                ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 7.0F, false);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.world.newExplosion(this, this.posX, this.posY + (double) this.getEyeHeight(), this.posZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
                }
                // CraftBukkit end

                // CraftBukkit start - Use relative location for far away sounds
                // this.world.a(1023, new BlockPosition(this), 0);
                // Paper start
                //int viewDistance = ((WorldServer) this.world).spigotConfig.viewDistance * 16; // Paper - updated to use worlds actual view distance incase we have to uncomment this due to removal of player view distance API
                for (EntityPlayer human : world.playerEntities) {
                    EntityPlayerMP player = (EntityPlayerMP) human;
                    int viewDistance = player.getViewDistance();
                    // Paper end
                    double deltaX = this.posX - player.posX;
                    double deltaZ = this.posZ - player.posZ;
                    double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    if ( world.spigotConfig.witherSpawnSoundRadius > 0 && distanceSquared > world.spigotConfig.witherSpawnSoundRadius * world.spigotConfig.witherSpawnSoundRadius ) continue; // Spigot
                    if (distanceSquared > viewDistance * viewDistance) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.posX + (deltaX / deltaLength) * viewDistance;
                        double relativeZ = player.posZ + (deltaZ / deltaLength) * viewDistance;
                        player.connection.sendPacket(new SPacketEffect(1023, new BlockPos((int) relativeX, (int) this.posY, (int) relativeZ), 0, true));
                    } else {
                        player.connection.sendPacket(new SPacketEffect(1023, new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ), 0, true));
                    }
                }
                // CraftBukkit end
            }

            this.setInvulTime(i);
            if (this.ticksExisted % 10 == 0) {
                this.heal(10.0F, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN); // CraftBukkit
            }

        } else {
            super.updateAITasks();

            int j;

            for (i = 1; i < 3; ++i) {
                if (this.ticksExisted >= this.nextHeadUpdate[i - 1]) {
                    this.nextHeadUpdate[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) {
                        int k = i - 1;
                        int l = this.idleHeadUpdates[i - 1];

                        this.idleHeadUpdates[k] = this.idleHeadUpdates[i - 1] + 1;
                        if (l > 15) {
                            float f = 10.0F;
                            float f1 = 5.0F;
                            double d0 = MathHelper.nextDouble(this.rand, this.posX - 10.0D, this.posX + 10.0D);
                            double d1 = MathHelper.nextDouble(this.rand, this.posY - 5.0D, this.posY + 5.0D);
                            double d2 = MathHelper.nextDouble(this.rand, this.posZ - 10.0D, this.posZ + 10.0D);

                            this.launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
                            this.idleHeadUpdates[i - 1] = 0;
                        }
                    }

                    j = this.getWatchedTargetId(i);
                    if (j > 0) {
                        Entity entity = this.world.getEntityByID(j);

                        if (entity != null && entity.isEntityAlive() && this.getDistanceSq(entity) <= 900.0D && this.canEntityBeSeen(entity)) {
                            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.disableDamage) {
                                this.updateWatchedTargetId(i, 0);
                            } else {
                                this.launchWitherSkullToEntity(i + 1, (EntityLivingBase) entity);
                                this.nextHeadUpdate[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                                this.idleHeadUpdates[i - 1] = 0;
                            }
                        } else {
                            this.updateWatchedTargetId(i, 0);
                        }
                    } else {
                        List list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(20.0D, 8.0D, 20.0D), Predicates.and(EntityWither.NOT_UNDEAD, EntitySelectors.NOT_SPECTATING));

                        for (int i1 = 0; i1 < 10 && !list.isEmpty(); ++i1) {
                            EntityLivingBase entityliving = (EntityLivingBase) list.get(this.rand.nextInt(list.size()));

                            if (entityliving != this && entityliving.isEntityAlive() && this.canEntityBeSeen(entityliving)) {
                                if (entityliving instanceof EntityPlayer) {
                                    if (!((EntityPlayer) entityliving).capabilities.disableDamage) {
                                        this.updateWatchedTargetId(i, entityliving.getEntityId());
                                    }
                                } else {
                                    this.updateWatchedTargetId(i, entityliving.getEntityId());
                                }
                                break;
                            }

                            list.remove(entityliving);
                        }
                    }
                }
            }

            if (this.getAttackTarget() != null) {
                this.updateWatchedTargetId(0, this.getAttackTarget().getEntityId());
            } else {
                this.updateWatchedTargetId(0, 0);
            }

            if (this.blockBreakCounter > 0) {
                --this.blockBreakCounter;
                if (this.blockBreakCounter == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
                    i = MathHelper.floor(this.posY);
                    j = MathHelper.floor(this.posX);
                    int j1 = MathHelper.floor(this.posZ);
                    boolean flag = false;

                    for (int k1 = -1; k1 <= 1; ++k1) {
                        for (int l1 = -1; l1 <= 1; ++l1) {
                            for (int i2 = 0; i2 <= 3; ++i2) {
                                int j2 = j + k1;
                                int k2 = i + i2;
                                int l2 = j1 + l1;
                                BlockPos blockposition = new BlockPos(j2, k2, l2);
                                IBlockState iblockdata = this.world.getBlockState(blockposition);
                                Block block = iblockdata.getBlock();

                                if (iblockdata.getMaterial() != Material.AIR && canDestroyBlock(block)) {
                                    // CraftBukkit start
                                    if (CraftEventFactory.callEntityChangeBlockEvent(this, blockposition, Blocks.AIR, 0).isCancelled()) {
                                        continue;
                                    }
                                    // CraftBukkit end
                                    flag = this.world.destroyBlock(blockposition, true) || flag;
                                }
                            }
                        }
                    }

                    if (flag) {
                        this.world.playEvent((EntityPlayer) null, 1022, new BlockPos(this), 0);
                    }
                }
            }

            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN); // CraftBukkit
            }

            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }

    public static boolean canDestroyBlock(Block block) {
        return block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME && block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.BARRIER && block != Blocks.STRUCTURE_BLOCK && block != Blocks.STRUCTURE_VOID && block != Blocks.PISTON_EXTENSION && block != Blocks.END_GATEWAY;
    }

    public void ignite() {
        this.setInvulTime(220);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    public void setInWeb() {}

    public void addTrackingPlayer(EntityPlayerMP entityplayer) {
        super.addTrackingPlayer(entityplayer);
        this.bossInfo.addPlayer(entityplayer);
    }

    public void removeTrackingPlayer(EntityPlayerMP entityplayer) {
        super.removeTrackingPlayer(entityplayer);
        this.bossInfo.removePlayer(entityplayer);
    }

    private double getHeadX(int i) {
        if (i <= 0) {
            return this.posX;
        } else {
            float f = (this.renderYawOffset + (float) (180 * (i - 1))) * 0.017453292F;
            float f1 = MathHelper.cos(f);

            return this.posX + (double) f1 * 1.3D;
        }
    }

    private double getHeadY(int i) {
        return i <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
    }

    private double getHeadZ(int i) {
        if (i <= 0) {
            return this.posZ;
        } else {
            float f = (this.renderYawOffset + (float) (180 * (i - 1))) * 0.017453292F;
            float f1 = MathHelper.sin(f);

            return this.posZ + (double) f1 * 1.3D;
        }
    }

    private float rotlerp(float f, float f1, float f2) {
        float f3 = MathHelper.wrapDegrees(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    private void launchWitherSkullToEntity(int i, EntityLivingBase entityliving) {
        this.launchWitherSkullToCoords(i, entityliving.posX, entityliving.posY + (double) entityliving.getEyeHeight() * 0.5D, entityliving.posZ, i == 0 && this.rand.nextFloat() < 0.001F);
    }

    private void launchWitherSkullToCoords(int i, double d0, double d1, double d2, boolean flag) {
        this.world.playEvent((EntityPlayer) null, 1024, new BlockPos(this), 0);
        double d3 = this.getHeadX(i);
        double d4 = this.getHeadY(i);
        double d5 = this.getHeadZ(i);
        double d6 = d0 - d3;
        double d7 = d1 - d4;
        double d8 = d2 - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8);

        if (flag) {
            entitywitherskull.setInvulnerable(true);
        }

        entitywitherskull.posY = d4;
        entitywitherskull.posX = d3;
        entitywitherskull.posZ = d5;
        this.world.spawnEntity(entitywitherskull);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase entityliving, float f) {
        this.launchWitherSkullToEntity(0, entityliving);
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (damagesource != DamageSource.DROWN && !(damagesource.getTrueSource() instanceof EntityWither)) {
            if (this.getInvulTime() > 0 && damagesource != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                Entity entity;

                if (this.isArmored()) {
                    entity = damagesource.getImmediateSource();
                    if (entity instanceof EntityArrow) {
                        return false;
                    }
                }

                entity = damagesource.getTrueSource();
                if (entity != null && !(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getCreatureAttribute() == this.getCreatureAttribute()) {
                    return false;
                } else {
                    if (this.blockBreakCounter <= 0) {
                        this.blockBreakCounter = 20;
                    }

                    for (int i = 0; i < this.idleHeadUpdates.length; ++i) {
                        this.idleHeadUpdates[i] += 3;
                    }

                    return super.attackEntityFrom(damagesource, f);
                }
            }
        } else {
            return false;
        }
    }

    protected void dropFewItems(boolean flag, int i) {
        EntityItem entityitem = this.dropItem(Items.NETHER_STAR, 1);

        if (entityitem != null) {
            entityitem.setNoDespawn();
        }

    }

    protected void despawnEntity() {
        this.idleTime = 0;
    }

    public void fall(float f, float f1) {}

    public void addPotionEffect(PotionEffect mobeffect) {}

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    public int getInvulTime() {
        return ((Integer) this.dataManager.get(EntityWither.INVULNERABILITY_TIME)).intValue();
    }

    public void setInvulTime(int i) {
        this.dataManager.set(EntityWither.INVULNERABILITY_TIME, Integer.valueOf(i));
    }

    public int getWatchedTargetId(int i) {
        return ((Integer) this.dataManager.get(EntityWither.HEAD_TARGETS[i])).intValue();
    }

    public void updateWatchedTargetId(int i, int j) {
        this.dataManager.set(EntityWither.HEAD_TARGETS[i], Integer.valueOf(j));
    }

    public boolean isArmored() {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected boolean canBeRidden(Entity entity) {
        return false;
    }

    public boolean isNonBoss() {
        return false;
    }

    public void setSwingingArms(boolean flag) {}

    class a extends EntityAIBase {

        public a() {
            this.setMutexBits(7);
        }

        public boolean shouldExecute() {
            return EntityWither.this.getInvulTime() > 0;
        }
    }
}
