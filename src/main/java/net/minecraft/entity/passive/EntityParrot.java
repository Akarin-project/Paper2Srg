package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAILandOnOwnersShoulder;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityParrot extends EntityShoulderRiding implements EntityFlying {

    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityParrot.class, DataSerializers.VARINT);
    private static final Predicate<EntityLiving> CAN_MIMIC = new Predicate() {
        public boolean a(@Nullable EntityLiving entityinsentient) {
            return entityinsentient != null && EntityParrot.IMITATION_SOUND_EVENTS.containsKey(EntityList.REGISTRY.getIDForObject(entityinsentient.getClass())); // CraftBukkit - decompile error
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EntityLiving) object);
        }
    };
    private static final Item DEADLY_ITEM = Items.COOKIE;
    private static final Set<Item> TAME_ITEMS = Sets.newHashSet(new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS});
    private static final Int2ObjectMap<SoundEvent> IMITATION_SOUND_EVENTS = new Int2ObjectOpenHashMap(32);
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private boolean partyParrot;
    private BlockPos jukeboxPosition;

    public EntityParrot(World world) {
        super(world);
        this.setSize(0.5F, 0.9F);
        this.moveHelper = new EntityFlyHelper(this);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.setVariant(this.rand.nextInt(5));
        return super.onInitialSpawn(difficultydamagescaler, groupdataentity);
    }

    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(0, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0D, 5.0F, 1.0F));
        this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
        this.tasks.addTask(3, new EntityAILandOnOwnersShoulder(this));
        this.tasks.addTask(3, new EntityAIFollow(this, 1.0D, 3.0F, 7.0F));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4000000059604645D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }

    protected PathNavigate createNavigator(World world) {
        PathNavigateFlying navigationflying = new PathNavigateFlying(this, world);

        navigationflying.setCanOpenDoors(false);
        navigationflying.setCanFloat(true);
        navigationflying.setCanEnterDoors(true);
        return navigationflying;
    }

    public float getEyeHeight() {
        return this.height * 0.6F;
    }

    public void onLivingUpdate() {
        playMimicSound(this.world, (Entity) this);
        if (this.jukeboxPosition == null || this.jukeboxPosition.distanceSq(this.posX, this.posY, this.posZ) > 12.0D || this.world.getBlockState(this.jukeboxPosition).getBlock() != Blocks.JUKEBOX) {
            this.partyParrot = false;
            this.jukeboxPosition = null;
        }

        super.onLivingUpdate();
        this.calculateFlapping();
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float) ((double) this.flapSpeed + (double) (this.onGround ? -1 : 4) * 0.3D);
        this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping = (float) ((double) this.flapping * 0.9D);
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        this.flap += this.flapping * 2.0F;
    }

    private static boolean playMimicSound(World world, Entity entity) {
        if (!entity.isSilent() && world.rand.nextInt(50) == 0) {
            List list = world.getEntitiesWithinAABB(EntityLiving.class, entity.getEntityBoundingBox().grow(20.0D), EntityParrot.CAN_MIMIC);

            if (!list.isEmpty()) {
                EntityLiving entityinsentient = (EntityLiving) list.get(world.rand.nextInt(list.size()));

                if (!entityinsentient.isSilent()) {
                    SoundEvent soundeffect = getImitatedSound(EntityList.REGISTRY.getIDForObject(entityinsentient.getClass())); // CraftBukkit - decompile error

                    world.playSound((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, soundeffect, entity.getSoundCategory(), 0.7F, getPitch(world.rand));
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!this.isTamed() && EntityParrot.TAME_ITEMS.contains(itemstack.getItem())) {
            if (!entityhuman.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            if (!this.isSilent()) {
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PARROT_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            }

            if (!this.world.isRemote) {
                if (this.rand.nextInt(10) == 0 && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) { // CraftBukkit
                    this.setTamedBy(entityhuman);
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte) 7);
                } else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte) 6);
                }
            }

            return true;
        } else if (itemstack.getItem() == EntityParrot.DEADLY_ITEM) {
            if (!entityhuman.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            this.addPotionEffect(new PotionEffect(MobEffects.POISON, 900));
            if (entityhuman.isCreative() || !this.getIsInvulnerable()) {
                this.attackEntityFrom(DamageSource.causePlayerDamage(entityhuman), Float.MAX_VALUE);
            }

            return true;
        } else {
            if (!this.world.isRemote && !this.isFlying() && this.isTamed() && this.isOwner((EntityLivingBase) entityhuman)) {
                this.aiSit.setSitting(!this.isSitting());
            }

            return super.processInteract(entityhuman, enumhand);
        }
    }

    public boolean isBreedingItem(ItemStack itemstack) {
        return false;
    }

    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockposition = new BlockPos(i, j, k);
        Block block = this.world.getBlockState(blockposition.down()).getBlock();

        return block instanceof BlockLeaves || block == Blocks.GRASS || block instanceof BlockLog || block == Blocks.AIR && this.world.getLight(blockposition) > 8 && super.getCanSpawnHere();
    }

    public void fall(float f, float f1) {}

    protected void updateFallState(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {}

    public boolean canMateWith(EntityAnimal entityanimal) {
        return false;
    }

    @Nullable
    public EntityAgeable createChild(EntityAgeable entityageable) {
        return null;
    }

    public static void playAmbientSound(World world, Entity entity) {
        if (!entity.isSilent() && !playMimicSound(world, entity) && world.rand.nextInt(200) == 0) {
            world.playSound((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, getAmbientSound(world.rand), entity.getSoundCategory(), 1.0F, getPitch(world.rand));
        }

    }

    public boolean attackEntityAsMob(Entity entity) {
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return getAmbientSound(this.rand);
    }

    private static SoundEvent getAmbientSound(Random random) {
        if (random.nextInt(1000) == 0) {
            ArrayList arraylist = new ArrayList(EntityParrot.IMITATION_SOUND_EVENTS.keySet());

            return getImitatedSound(((Integer) arraylist.get(random.nextInt(arraylist.size()))).intValue());
        } else {
            return SoundEvents.ENTITY_PARROT_AMBIENT;
        }
    }

    public static SoundEvent getImitatedSound(int i) {
        return EntityParrot.IMITATION_SOUND_EVENTS.containsKey(i) ? (SoundEvent) EntityParrot.IMITATION_SOUND_EVENTS.get(i) : SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        this.playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

    protected float playFlySound(float f) {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15F, 1.0F);
        return f + this.flapSpeed / 2.0F;
    }

    protected boolean makeFlySound() {
        return true;
    }

    protected float getSoundPitch() {
        return getPitch(this.rand);
    }

    private static float getPitch(Random random) {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    public boolean canBePushed() {
        return true;
    }

    protected void collideWithEntity(Entity entity) {
        if (!(entity instanceof EntityPlayer)) {
            super.collideWithEntity(entity);
        }
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else {
            if (this.aiSit != null) {
                // CraftBukkit - moved into EntityLiving.d(DamageSource, float)
                // this.goalSit.setSitting(false);
            }

            return super.attackEntityFrom(damagesource, f);
        }
    }

    public int getVariant() {
        return MathHelper.clamp(((Integer) this.dataManager.get(EntityParrot.VARIANT)).intValue(), 0, 4);
    }

    public void setVariant(int i) {
        this.dataManager.set(EntityParrot.VARIANT, Integer.valueOf(i));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityParrot.VARIANT, Integer.valueOf(0));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Variant", this.getVariant());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setVariant(nbttagcompound.getInteger("Variant"));
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_PARROT;
    }

    public boolean isFlying() {
        return !this.onGround;
    }

    static {
        // CraftBukkit start
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityBlaze.class), SoundEvents.E_PARROT_IM_BLAZE);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityCaveSpider.class), SoundEvents.E_PARROT_IM_SPIDER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityCreeper.class), SoundEvents.E_PARROT_IM_CREEPER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityElderGuardian.class), SoundEvents.E_PARROT_IM_ELDER_GUARDIAN);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityDragon.class), SoundEvents.E_PARROT_IM_ENDERDRAGON);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEnderman.class), SoundEvents.E_PARROT_IM_ENDERMAN);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEndermite.class), SoundEvents.E_PARROT_IM_ENDERMITE);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityEvoker.class), SoundEvents.E_PARROT_IM_EVOCATION_ILLAGER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityGhast.class), SoundEvents.E_PARROT_IM_GHAST);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityHusk.class), SoundEvents.E_PARROT_IM_HUSK);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityIllusionIllager.class), SoundEvents.E_PARROT_IM_ILLUSION_ILLAGER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityMagmaCube.class), SoundEvents.E_PARROT_IM_MAGMACUBE);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityPigZombie.class), SoundEvents.E_PARROT_IM_ZOMBIE_PIGMAN);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityPolarBear.class), SoundEvents.E_PARROT_IM_POLAR_BEAR);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityShulker.class), SoundEvents.E_PARROT_IM_SHULKER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySilverfish.class), SoundEvents.E_PARROT_IM_SILVERFISH);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySkeleton.class), SoundEvents.E_PARROT_IM_SKELETON);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySlime.class), SoundEvents.E_PARROT_IM_SLIME);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntitySpider.class), SoundEvents.E_PARROT_IM_SPIDER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityStray.class), SoundEvents.E_PARROT_IM_STRAY);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityVex.class), SoundEvents.E_PARROT_IM_VEX);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityVindicator.class), SoundEvents.E_PARROT_IM_VINDICATION_ILLAGER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWitch.class), SoundEvents.E_PARROT_IM_WITCH);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWither.class), SoundEvents.E_PARROT_IM_WITHER);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWitherSkeleton.class), SoundEvents.E_PARROT_IM_WITHER_SKELETON);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityWolf.class), SoundEvents.E_PARROT_IM_WOLF);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityZombie.class), SoundEvents.E_PARROT_IM_ZOMBIE);
        EntityParrot.IMITATION_SOUND_EVENTS.put(EntityList.REGISTRY.getIDForObject(EntityZombieVillager.class), SoundEvents.E_PARROT_IM_ZOMBIE_VILLAGER);
        // CraftBukkit end
    }
}
