package net.minecraft.entity.monster;

import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEnderman extends EntityMob {

    private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier ATTACKING_SPEED_BOOST = (new AttributeModifier(EntityEnderman.ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
    private static final Set<Block> CARRIABLE_BLOCKS = Sets.newIdentityHashSet();
    private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.BOOLEAN);
    private int lastCreepySound;
    private int targetChangeTime;

    public EntityEnderman(World world) {
        super(world);
        this.setSize(0.6F, 2.9F);
        this.stepHeight = 1.0F;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(10, new EntityEnderman.AIPlaceBlock(this));
        this.tasks.addTask(11, new EntityEnderman.AITakeBlock(this));
        this.targetTasks.addTask(1, new EntityEnderman.AIFindPlayer(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate() {
            public boolean a(@Nullable EntityEndermite entityendermite) {
                return entityendermite.isSpawnedByPlayer();
            }

            public boolean apply(@Nullable Object object) {
                return this.a((EntityEndermite) object);
            }
        }));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    public void setAttackTarget(@Nullable EntityLivingBase entityliving) {
        // CraftBukkit start - fire event
        setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    // Paper start
    private boolean tryEscape(EndermanEscapeEvent.Reason reason) {
        return new EndermanEscapeEvent((org.bukkit.craftbukkit.entity.CraftEnderman) this.getBukkitEntity(), reason).callEvent();
    }
    // Paper end

    @Override
    public boolean setGoalTarget(EntityLivingBase entityliving, org.bukkit.event.entity.EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        if (!super.setGoalTarget(entityliving, reason, fireEvent)) {
            return false;
        }
        entityliving = getAttackTarget();
        // CraftBukkit end
        IAttributeInstance attributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (entityliving == null) {
            this.targetChangeTime = 0;
            this.dataManager.set(EntityEnderman.SCREAMING, Boolean.valueOf(false));
            attributeinstance.removeModifier(EntityEnderman.ATTACKING_SPEED_BOOST);
        } else {
            this.targetChangeTime = this.ticksExisted;
            this.dataManager.set(EntityEnderman.SCREAMING, Boolean.valueOf(true));
            if (!attributeinstance.hasModifier(EntityEnderman.ATTACKING_SPEED_BOOST)) {
                attributeinstance.applyModifier(EntityEnderman.ATTACKING_SPEED_BOOST);
            }
        }
        return true;

    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityEnderman.CARRIED_BLOCK, Optional.absent());
        this.dataManager.register(EntityEnderman.SCREAMING, Boolean.valueOf(false));
    }

    public void playEndermanSound() {
        if (this.ticksExisted >= this.lastCreepySound + 400) {
            this.lastCreepySound = this.ticksExisted;
            if (!this.isSilent()) {
                this.world.playSound(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ, SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5F, 1.0F, false);
            }
        }

    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (EntityEnderman.SCREAMING.equals(datawatcherobject) && this.isScreaming() && this.world.isRemote) {
            this.playEndermanSound();
        }

        super.notifyDataManagerChange(datawatcherobject);
    }

    public static void registerFixesEnderman(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityEnderman.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        IBlockState iblockdata = this.getHeldBlockState();

        if (iblockdata != null) {
            nbttagcompound.setShort("carried", (short) Block.getIdFromBlock(iblockdata.getBlock()));
            nbttagcompound.setShort("carriedData", (short) iblockdata.getBlock().getMetaFromState(iblockdata));
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        IBlockState iblockdata;

        if (nbttagcompound.hasKey("carried", 8)) {
            iblockdata = Block.getBlockFromName(nbttagcompound.getString("carried")).getStateFromMeta(nbttagcompound.getShort("carriedData") & '\uffff');
        } else {
            iblockdata = Block.getBlockById(nbttagcompound.getShort("carried")).getStateFromMeta(nbttagcompound.getShort("carriedData") & '\uffff');
        }

        if (iblockdata == null || iblockdata.getBlock() == null || iblockdata.getMaterial() == Material.AIR) {
            iblockdata = null;
        }

        this.setHeldBlockState(iblockdata);
    }

    // Paper start - OBFHELPER - ok not really, but verify this on updates
    private boolean shouldAttackPlayer(EntityPlayer entityhuman) {
        boolean shouldAttack = f_real(entityhuman);
        com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent event = new com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent((org.bukkit.entity.Enderman) getBukkitEntity(), (org.bukkit.entity.Player) entityhuman.getBukkitEntity());
        event.setCancelled(!shouldAttack);
        return event.callEvent();
    }
    private boolean f_real(EntityPlayer entityhuman) {
        // Paper end
        ItemStack itemstack = (ItemStack) entityhuman.inventory.armorInventory.get(3);

        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            return false;
        } else {
            Vec3d vec3d = entityhuman.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.posX - entityhuman.posX, this.getEntityBoundingBox().minY + (double) this.getEyeHeight() - (entityhuman.posY + (double) entityhuman.getEyeHeight()), this.posZ - entityhuman.posZ);
            double d0 = vec3d1.lengthVector();

            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);

            return d1 > 1.0D - 0.025D / d0 ? entityhuman.canEntityBeSeen(this) : false;
        }
    }

    public float getEyeHeight() {
        return 2.55F;
    }

    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
            }
        }

        this.isJumping = false;
        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600) {
            float f = this.getBrightness();

            if (f > 0.5F && this.world.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && tryEscape(EndermanEscapeEvent.Reason.RUNAWAY)) { // Paper
                this.setAttackTarget((EntityLivingBase) null);
                this.teleportRandomly();
            }
        }

        super.updateAITasks();
    }

    public boolean teleportRandomly() { return teleportRandomly(); } // Paper - OBFHELPER
    protected boolean teleportRandomly() {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double) (this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;

        return this.teleportTo(d0, d1, d2);
    }

    protected boolean teleportToEntity(Entity entity) {
        Vec3d vec3d = new Vec3d(this.posX - entity.posX, this.getEntityBoundingBox().minY + (double) (this.height / 2.0F) - entity.posY + (double) entity.getEyeHeight(), this.posZ - entity.posZ);

        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.posY + (double) (this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;

        return this.teleportTo(d1, d2, d3);
    }

    private boolean teleportTo(double d0, double d1, double d2) {
        boolean flag = this.attemptTeleport(d0, d1, d2);

        if (flag) {
            this.world.playSound((EntityPlayer) null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }

    protected SoundEvent getAmbientSound() {
        return this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMEN_DEATH;
    }

    protected void dropEquipment(boolean flag, int i) {
        super.dropEquipment(flag, i);
        IBlockState iblockdata = this.getHeldBlockState();

        if (iblockdata != null) {
            Item item = Item.getItemFromBlock(iblockdata.getBlock());
            int j = item.getHasSubtypes() ? iblockdata.getBlock().getMetaFromState(iblockdata) : 0;

            this.entityDropItem(new ItemStack(item, 1, j), 0.0F);
        }

    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ENDERMAN;
    }

    public void setHeldBlockState(@Nullable IBlockState iblockdata) {
        this.dataManager.set(EntityEnderman.CARRIED_BLOCK, Optional.fromNullable(iblockdata));
    }

    @Nullable
    public IBlockState getHeldBlockState() {
        return (IBlockState) ((Optional) this.dataManager.get(EntityEnderman.CARRIED_BLOCK)).orNull();
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (damagesource instanceof EntityDamageSourceIndirect && tryEscape(EndermanEscapeEvent.Reason.INDIRECT)) { // Paper
            for (int i = 0; i < 64; ++i) {
                if (this.teleportRandomly()) {
                    return true;
                }
            }

            return false;
        } else {
            boolean flag = super.attackEntityFrom(damagesource, f);

            if (damagesource.isUnblockable() && this.rand.nextInt(10) != 0 && tryEscape(damagesource == DamageSource.DROWN ? EndermanEscapeEvent.Reason.DROWN : EndermanEscapeEvent.Reason.CRITICAL_HIT)) { // Paper
                this.teleportRandomly();
            }

            return flag;
        }
    }

    public boolean isScreaming() {
        return ((Boolean) this.dataManager.get(EntityEnderman.SCREAMING)).booleanValue();
    }

    static {
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.GRASS);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.DIRT);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.SAND);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.GRAVEL);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.YELLOW_FLOWER);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.RED_FLOWER);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.BROWN_MUSHROOM);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.RED_MUSHROOM);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.TNT);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.CACTUS);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.CLAY);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.PUMPKIN);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.MELON_BLOCK);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.MYCELIUM);
        EntityEnderman.CARRIABLE_BLOCKS.add(Blocks.NETHERRACK);
    }

    static class AITakeBlock extends EntityAIBase {

        private final EntityEnderman enderman;

        public AITakeBlock(EntityEnderman entityenderman) {
            this.enderman = entityenderman;
        }

        public boolean shouldExecute() {
            return this.enderman.getHeldBlockState() != null ? false : (!this.enderman.world.getGameRules().getBoolean("mobGriefing") ? false : this.enderman.getRNG().nextInt(20) == 0);
        }

        public void updateTask() {
            Random random = this.enderman.getRNG();
            World world = this.enderman.world;
            int i = MathHelper.floor(this.enderman.posX - 2.0D + random.nextDouble() * 4.0D);
            int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 3.0D);
            int k = MathHelper.floor(this.enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
            BlockPos blockposition = new BlockPos(i, j, k);
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();
            RayTraceResult movingobjectposition = world.rayTraceBlocks(new Vec3d((double) ((float) MathHelper.floor(this.enderman.posX) + 0.5F), (double) ((float) j + 0.5F), (double) ((float) MathHelper.floor(this.enderman.posZ) + 0.5F)), new Vec3d((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F)), false, true, false);
            boolean flag = movingobjectposition != null && movingobjectposition.getBlockPos().equals(blockposition);

            if (EntityEnderman.CARRIABLE_BLOCKS.contains(block) && flag) {
                // CraftBukkit start - Pickup event
                if (!org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.enderman, this.enderman.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), org.bukkit.Material.AIR).isCancelled()) {
                    this.enderman.setHeldBlockState(iblockdata);
                    world.setBlockToAir(blockposition);
                }
                // CraftBukkit end
            }

        }
    }

    static class AIPlaceBlock extends EntityAIBase {

        private final EntityEnderman enderman;

        public AIPlaceBlock(EntityEnderman entityenderman) {
            this.enderman = entityenderman;
        }

        public boolean shouldExecute() {
            return this.enderman.getHeldBlockState() == null ? false : (!this.enderman.world.getGameRules().getBoolean("mobGriefing") ? false : this.enderman.getRNG().nextInt(2000) == 0);
        }

        public void updateTask() {
            Random random = this.enderman.getRNG();
            World world = this.enderman.world;
            int i = MathHelper.floor(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
            int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 2.0D);
            int k = MathHelper.floor(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
            BlockPos blockposition = new BlockPos(i, j, k);
            IBlockState iblockdata = world.getBlockState(blockposition);
            IBlockState iblockdata1 = world.getBlockState(blockposition.down());
            IBlockState iblockdata2 = this.enderman.getHeldBlockState();

            if (iblockdata2 != null && this.canPlaceBlock(world, blockposition, iblockdata2.getBlock(), iblockdata, iblockdata1)) {
                // CraftBukkit start - Place event
                if (!org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.enderman, blockposition, this.enderman.getHeldBlockState().getBlock(), this.enderman.getHeldBlockState().getBlock().getMetaFromState(this.enderman.getHeldBlockState())).isCancelled()) {
                world.setBlockState(blockposition, iblockdata2, 3);
                this.enderman.setHeldBlockState((IBlockState) null);
                }
                // CraftBukkit end
            }

        }

        private boolean canPlaceBlock(World world, BlockPos blockposition, Block block, IBlockState iblockdata, IBlockState iblockdata1) {
            return !block.canPlaceBlockAt(world, blockposition) ? false : (iblockdata.getMaterial() != Material.AIR ? false : (iblockdata1.getMaterial() == Material.AIR ? false : iblockdata1.isFullCube()));
        }
    }

    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {

        private final EntityEnderman enderman; public EntityEnderman getEnderman() { return enderman; } // Paper - OBFHELPER
        private EntityPlayer player;
        private int aggroTime;
        private int teleportTime;

        public AIFindPlayer(EntityEnderman entityenderman) {
            super(entityenderman, EntityPlayer.class, false);
            this.enderman = entityenderman;
        }

        public boolean shouldExecute() {
            double d0 = this.getTargetDistance();

            this.player = this.enderman.world.getNearestAttackablePlayer(this.enderman.posX, this.enderman.posY, this.enderman.posZ, d0, d0, (Function) null, new Predicate() {
                public boolean a(@Nullable EntityPlayer entityhuman) {
                    return entityhuman != null && AIFindPlayer.this.enderman.shouldAttackPlayer(entityhuman);
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((EntityPlayer) object);
                }
            });
            return this.player != null;
        }

        public void startExecuting() {
            this.aggroTime = 5;
            this.teleportTime = 0;
        }

        public void resetTask() {
            this.player = null;
            super.resetTask();
        }

        public boolean shouldContinueExecuting() {
            if (this.player != null) {
                if (!this.enderman.shouldAttackPlayer(this.player)) {
                    return false;
                } else {
                    this.enderman.faceEntity((Entity) this.player, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.targetEntity != null && ((EntityPlayer) this.targetEntity).isEntityAlive() ? true : super.shouldContinueExecuting();
            }
        }

        public void updateTask() {
            if (this.player != null) {
                if (--this.aggroTime <= 0) {
                    this.targetEntity = this.player;
                    this.player = null;
                    super.startExecuting();
                }
            } else {
                if (this.targetEntity != null) {
                    if (this.enderman.shouldAttackPlayer((EntityPlayer) this.targetEntity)) {
                        if (((EntityPlayer) this.targetEntity).getDistanceSq(this.enderman) < 16.0D && this.getEnderman().tryEscape(EndermanEscapeEvent.Reason.STARE)) { // Paper
                            this.enderman.teleportRandomly();
                        }

                        this.teleportTime = 0;
                    } else if (((EntityPlayer) this.targetEntity).getDistanceSq(this.enderman) > 256.0D && this.teleportTime++ >= 30 && this.enderman.teleportToEntity((Entity) this.targetEntity)) {
                        this.teleportTime = 0;
                    }
                }

                super.updateTask();
            }

        }
    }
}
