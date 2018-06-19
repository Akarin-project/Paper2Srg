package net.minecraft.entity;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.CombatRules;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

// CraftBukkit start
import java.util.ArrayList;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.craftbukkit.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
// CraftBukkit end

import co.aikar.timings.MinecraftTimings; // Paper

public abstract class EntityLivingBase extends Entity {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final UUID SPRINTING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier SPRINTING_SPEED_BOOST = (new AttributeModifier(EntityLivingBase.SPRINTING_SPEED_BOOST_ID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
    protected static final DataParameter<Byte> HAND_STATES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BYTE);
    public static final DataParameter<Float> HEALTH = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> POTION_EFFECTS = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HIDE_PARTICLES = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ARROW_COUNT_IN_ENTITY = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
    private AbstractAttributeMap attributeMap;
    public CombatTracker _combatTracker = new CombatTracker(this);
    public final Map<Potion, PotionEffect> activePotionsMap = Maps.newHashMap();
    private final NonNullList<ItemStack> handInventory;
    private final NonNullList<ItemStack> armorArray;
    public boolean isSwingInProgress;
    public EnumHand swingingHand;
    public int swingProgressInt;
    public int arrowHitTimer;
    public int hurtTime;
    public int maxHurtTime;
    public float attackedAtYaw;
    public int deathTime;
    public float prevSwingProgress;
    public float swingProgress;
    protected int ticksSinceLastSwing;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;
    public float limbSwing;
    public int maxHurtResistantTime;
    public float prevCameraPitch;
    public float cameraPitch;
    public float randomUnused2;
    public float randomUnused1;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float rotationYawHead;
    public float prevRotationYawHead;
    public float jumpMovementFactor;
    public EntityPlayer attackingPlayer;
    public int recentlyHit; // Paper - public
    protected boolean dead;
    protected int idleTime;
    protected float prevOnGroundSpeedFactor;
    protected float onGroundSpeedFactor;
    protected float movedDistance;
    protected float prevMovedDistance;
    protected float unused180;
    protected int scoreValue;
    public float lastDamage;
    protected boolean isJumping;
    public float moveStrafing;
    public float moveVertical;
    public float moveForward;
    public float randomYawVelocity;
    protected int newPosRotationIncrements;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    public boolean potionsNeedUpdate;
    public EntityLivingBase revengeTarget;
    public int revengeTimer;
    private EntityLivingBase lastAttackedEntity;
    private int lastAttackedEntityTime;
    private float landMovementFactor;
    private int jumpTicks;
    private float absorptionAmount;
    protected ItemStack activeItemStack;
    protected int activeItemStackUseCount;
    protected int ticksElytraFlying;
    private BlockPos prevBlockpos;
    private DamageSource lastDamageSource;
    private long lastDamageStamp;
    // CraftBukkit start
    public int expToDrop;
    public int maxAirTicks = 300;
    boolean forceDrops;
    protected ArrayList<org.bukkit.inventory.ItemStack> drops = new ArrayList<org.bukkit.inventory.ItemStack>();
    public org.bukkit.craftbukkit.attribute.CraftAttributeMap craftAttributes;
    public boolean collides = true;
    public boolean canPickUpLoot;

    @Override
    public float getBukkitYaw() {
        return getRotationYawHead();
    }
    // CraftBukkit end
    // Spigot start
    @Override
    public void inactiveTick()
    {
        super.inactiveTick();
        ++this.idleTime; // Above all the floats
    }
    // Spigot end

    @Override
    public void onKillCommand() {
        this.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }

    public EntityLivingBase(World world) {
        super(world);
        this.handInventory = NonNullList.withSize(2, ItemStack.EMPTY);
        this.armorArray = NonNullList.withSize(4, ItemStack.EMPTY);
        this.maxHurtResistantTime = 20;
        this.jumpMovementFactor = 0.02F;
        this.potionsNeedUpdate = true;
        this.activeItemStack = ItemStack.EMPTY;
        this.applyEntityAttributes();
        // CraftBukkit - setHealth(getMaxHealth()) inlined and simplified to skip the instanceof check for EntityPlayer, as getBukkitEntity() is not initialized in constructor
        this.dataManager.set(EntityLivingBase.HEALTH, (float) this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
        this.preventEntitySpawning = true;
        this.randomUnused1 = (float) ((Math.random() + 1.0D) * 0.009999999776482582D);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.randomUnused2 = (float) Math.random() * 12398.0F;
        this.rotationYaw = (float) (Math.random() * 6.2831854820251465D);
        this.rotationYawHead = this.rotationYaw;
        this.stepHeight = 0.6F;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(EntityLivingBase.HAND_STATES, Byte.valueOf((byte) 0));
        this.dataManager.register(EntityLivingBase.POTION_EFFECTS, Integer.valueOf(0));
        this.dataManager.register(EntityLivingBase.HIDE_PARTICLES, Boolean.valueOf(false));
        this.dataManager.register(EntityLivingBase.ARROW_COUNT_IN_ENTITY, Integer.valueOf(0));
        this.dataManager.register(EntityLivingBase.HEALTH, Float.valueOf(1.0F));
    }

    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
    }

    @Override
    protected void updateFallState(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }

        if (!this.world.isRemote && this.fallDistance > 3.0F && flag) {
            float f = MathHelper.ceil(this.fallDistance - 3.0F);

            if (iblockdata.getMaterial() != Material.AIR) {
                double d1 = Math.min(0.2F + f / 15.0F, 2.5D);
                int i = (int) (150.0D * d1);

                // CraftBukkit start - visiblity api
                if (this instanceof EntityPlayerMP) {
                    ((WorldServer) this.world).sendParticles((EntityPlayerMP) this, EnumParticleTypes.BLOCK_DUST, false, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(iblockdata)});
                } else {
                    ((WorldServer) this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(iblockdata)});
                }
                // CraftBukkit end
            }
        }

        super.updateFallState(d0, flag, iblockdata, blockposition);
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    @Override
    public void onEntityUpdate() {
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        this.world.profiler.startSection("livingEntityBaseTick");
        boolean flag = this instanceof EntityPlayer;

        if (this.isEntityAlive()) {
            if (this.isEntityInsideOpaqueBlock()) {
                this.attackEntityFrom(DamageSource.IN_WALL, 1.0F);
            } else if (flag && !this.world.getWorldBorder().contains(this.getEntityBoundingBox())) {
                double d0 = this.world.getWorldBorder().getClosestDistance(this) + this.world.getWorldBorder().getDamageBuffer();

                if (d0 < 0.0D) {
                    double d1 = this.world.getWorldBorder().getDamageAmount();

                    if (d1 > 0.0D) {
                        this.attackEntityFrom(DamageSource.IN_WALL, Math.max(1, MathHelper.floor(-d0 * d1)));
                    }
                }
            }
        }

        if (this.isImmuneToFire() || this.world.isRemote) {
            this.extinguish();
        }

        boolean flag1 = flag && ((EntityPlayer) this).capabilities.disableDamage;

        if (this.isEntityAlive()) {
            if (this.isInsideOfMaterial(Material.WATER)) {
                if (!this.canBreatheUnderwater() && !this.isPotionActive(MobEffects.WATER_BREATHING) && !flag1) {
                    this.setAir(this.decreaseAirSupply(this.getAir()));
                    if (this.getAir() == -20) {
                        this.setAir(0);

                        for (int i = 0; i < 8; ++i) {
                            float f = this.rand.nextFloat() - this.rand.nextFloat();
                            float f1 = this.rand.nextFloat() - this.rand.nextFloat();
                            float f2 = this.rand.nextFloat() - this.rand.nextFloat();

                            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f, this.posY + f1, this.posZ + f2, this.motionX, this.motionY, this.motionZ, new int[0]);
                        }

                        this.attackEntityFrom(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!this.world.isRemote && this.isRiding() && this.getRidingEntity() instanceof EntityLivingBase) {
                    this.dismountRidingEntity();
                }
            } else {
                // CraftBukkit start - Only set if needed to work around a DataWatcher inefficiency
                if (this.getAir() != 300) {
                    this.setAir(maxAirTicks);
                }
                // CraftBukkit end
            }

            if (!this.world.isRemote) {
                BlockPos blockposition = new BlockPos(this);

                if (!Objects.equal(this.prevBlockpos, blockposition)) {
                    this.prevBlockpos = blockposition;
                    this.frostWalk(blockposition);
                }
            }
        }

        if (this.isEntityAlive() && this.isWet()) {
            this.extinguish();
        }

        this.prevCameraPitch = this.cameraPitch;
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.hurtResistantTime > 0 && !(this instanceof EntityPlayerMP)) {
            --this.hurtResistantTime;
        }

        if (this.getHealth() <= 0.0F) {
            this.onDeathUpdate();
        }

        if (this.recentlyHit > 0) {
            --this.recentlyHit;
        } else {
            this.attackingPlayer = null;
        }

        if (this.lastAttackedEntity != null && !this.lastAttackedEntity.isEntityAlive()) {
            this.lastAttackedEntity = null;
        }

        if (this.revengeTarget != null) {
            if (!this.revengeTarget.isEntityAlive()) {
                this.setRevengeTarget((EntityLivingBase) null);
            } else if (this.ticksExisted - this.revengeTimer > 100) {
                this.setRevengeTarget((EntityLivingBase) null);
            }
        }

        this.updatePotionEffects();
        this.prevMovedDistance = this.movedDistance;
        this.prevRenderYawOffset = this.renderYawOffset;
        this.prevRotationYawHead = this.rotationYawHead;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.world.profiler.endSection();
    }

    // CraftBukkit start
    public int getExpReward() {
        int exp = this.getExperiencePoints(this.attackingPlayer);

        if (!this.world.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")) {
            return exp;
        } else {
            return 0;
        }
    }
    // CraftBukkit end

    protected void frostWalk(BlockPos blockposition) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, this);

        if (i > 0) {
            EnchantmentFrostWalker.freezeNearby(this, this.world, blockposition, i);
        }

    }

    public boolean isChild() {
        return false;
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        if (this.deathTime >= 20 && !this.isDead) { // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead)
            int i;

            // CraftBukkit start - Update getExpReward() above if the removed if() changes!
            i = this.expToDrop;
            while (i > 0) {
                int j = EntityXPOrb.getXPSplit(i);

                i -= j;
                EntityLivingBase attacker = attackingPlayer != null ? attackingPlayer : revengeTarget; // Paper
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j, this instanceof EntityPlayerMP ? org.bukkit.entity.ExperienceOrb.SpawnReason.PLAYER_DEATH : org.bukkit.entity.ExperienceOrb.SpawnReason.ENTITY_DEATH, attacker, this)); // Paper
            }
            this.expToDrop = 0;
            // CraftBukkit end

            this.setDead();

            for (i = 0; i < 20; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;

                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
            }
        }

    }

    protected boolean canDropLoot() {
        return !this.isChild();
    }

    protected int decreaseAirSupply(int i) {
        int j = EnchantmentHelper.getRespirationModifier(this);

        return j > 0 && this.rand.nextInt(j + 1) > 0 ? i : i - 1;
    }

    protected int getExperiencePoints(EntityPlayer entityhuman) {
        return 0;
    }

    protected boolean isPlayer() {
        return false;
    }

    public Random getRNG() {
        return this.rand;
    }

    @Nullable
    public EntityLivingBase getRevengeTarget() {
        return this.revengeTarget;
    }

    public int getRevengeTimer() {
        return this.revengeTimer;
    }

    public void setRevengeTarget(@Nullable EntityLivingBase entityliving) {
        this.revengeTarget = entityliving;
        this.revengeTimer = this.ticksExisted;
    }

    public EntityLivingBase getLastAttackedEntity() {
        return this.lastAttackedEntity;
    }

    public int getLastAttackedEntityTime() {
        return this.lastAttackedEntityTime;
    }

    public void setLastAttackedEntity(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            this.lastAttackedEntity = (EntityLivingBase) entity;
        } else {
            this.lastAttackedEntity = null;
        }

        this.lastAttackedEntityTime = this.ticksExisted;
    }

    public int getIdleTime() {
        return this.idleTime;
    }

    protected void playEquipSound(ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            SoundEvent soundeffect = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor) {
                soundeffect = ((ItemArmor) item).getArmorMaterial().getSoundEvent();
            } else if (item == Items.ELYTRA) {
                soundeffect = SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA;
            }

            this.playSound(soundeffect, 1.0F, 1.0F);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setFloat("Health", this.getHealth());
        nbttagcompound.setShort("HurtTime", (short) this.hurtTime);
        nbttagcompound.setInteger("HurtByTimestamp", this.revengeTimer);
        nbttagcompound.setShort("DeathTime", (short) this.deathTime);
        nbttagcompound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
        EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
        int i = aenumitemslot.length;

        int j;
        EntityEquipmentSlot enumitemslot;
        ItemStack itemstack;

        for (j = 0; j < i; ++j) {
            enumitemslot = aenumitemslot[j];
            itemstack = this.getItemStackFromSlot(enumitemslot);
            if (!itemstack.isEmpty()) {
                this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(enumitemslot));
            }
        }

        nbttagcompound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));
        aenumitemslot = EntityEquipmentSlot.values();
        i = aenumitemslot.length;

        for (j = 0; j < i; ++j) {
            enumitemslot = aenumitemslot[j];
            itemstack = this.getItemStackFromSlot(enumitemslot);
            if (!itemstack.isEmpty()) {
                this.getAttributeMap().applyAttributeModifiers(itemstack.getAttributeModifiers(enumitemslot));
            }
        }

        if (!this.activePotionsMap.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.activePotionsMap.values().iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                nbttaglist.appendTag(mobeffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            nbttagcompound.setTag("ActiveEffects", nbttaglist);
        }

        nbttagcompound.setBoolean("FallFlying", this.isElytraFlying());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        // Paper start - jvm keeps optimizing the setter
        float absorptionAmount = nbttagcompound.getFloat("AbsorptionAmount");
        if (Float.isNaN(absorptionAmount)) {
            absorptionAmount = 0;
        }
        this.setAbsorptionAmount(absorptionAmount);
        // Paper end
        if (nbttagcompound.hasKey("Attributes", 9) && this.world != null && !this.world.isRemote) {
            SharedMonsterAttributes.setAttributeModifiers(this.getAttributeMap(), nbttagcompound.getTagList("Attributes", 10));
        }

        if (nbttagcompound.hasKey("ActiveEffects", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getTagList("ActiveEffects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                PotionEffect mobeffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound1);

                if (mobeffect != null) {
                    this.activePotionsMap.put(mobeffect.getPotion(), mobeffect);
                }
            }
        }

        // CraftBukkit start
        if (nbttagcompound.hasKey("Bukkit.MaxHealth")) {
            NBTBase nbtbase = nbttagcompound.getTag("Bukkit.MaxHealth");
            if (nbtbase.getId() == 5) {
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(((NBTTagFloat) nbtbase).getDouble());
            } else if (nbtbase.getId() == 3) {
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(((NBTTagInt) nbtbase).getDouble());
            }
        }
        // CraftBukkit end

        if (nbttagcompound.hasKey("Health", 99)) {
            this.setHealth(nbttagcompound.getFloat("Health"));
        }

        this.hurtTime = nbttagcompound.getShort("HurtTime");
        this.deathTime = nbttagcompound.getShort("DeathTime");
        this.revengeTimer = nbttagcompound.getInteger("HurtByTimestamp");
        if (nbttagcompound.hasKey("Team", 8)) {
            String s = nbttagcompound.getString("Team");
            boolean flag = this.world.getScoreboard().addPlayerToTeam(this.getCachedUniqueIdString(), s);

            if (!flag) {
                EntityLivingBase.LOGGER.warn("Unable to add mob to team \"" + s + "\" (that team probably doesn\'t exist)");
            }
        }

        if (nbttagcompound.getBoolean("FallFlying")) {
            this.setFlag(7, true);
        }

    }

    // CraftBukkit start
    private boolean isTickingEffects = false;
    private List<Object> effectsToProcess = Lists.newArrayList();
    // CraftBukkit end

    protected void updatePotionEffects() {
        Iterator iterator = this.activePotionsMap.keySet().iterator();

        isTickingEffects = true; // CraftBukkit
        try {
            while (iterator.hasNext()) {
                Potion mobeffectlist = (Potion) iterator.next();
                PotionEffect mobeffect = this.activePotionsMap.get(mobeffectlist);

                if (!mobeffect.onUpdate(this)) {
                    if (!this.world.isRemote) {
                        iterator.remove();
                        this.onFinishedPotionEffect(mobeffect);
                    }
                } else if (mobeffect.getDuration() % 600 == 0) {
                    this.onChangedPotionEffect(mobeffect, false);
                }
            }
        } catch (ConcurrentModificationException concurrentmodificationexception) {
            ;
        }
        // CraftBukkit start
        isTickingEffects = false;
        for (Object e : effectsToProcess) {
            if (e instanceof PotionEffect) {
                addPotionEffect((PotionEffect) e);
            } else {
                removePotionEffect((Potion) e);
            }
        }
        effectsToProcess.clear();
        // CraftBukkit end

        if (this.potionsNeedUpdate) {
            if (!this.world.isRemote) {
                this.updatePotionMetadata();
            }

            this.potionsNeedUpdate = false;
        }

        int i = this.dataManager.get(EntityLivingBase.POTION_EFFECTS).intValue();
        boolean flag = this.dataManager.get(EntityLivingBase.HIDE_PARTICLES).booleanValue();

        if (i > 0) {
            boolean flag1;

            if (this.isInvisible()) {
                flag1 = this.rand.nextInt(15) == 0;
            } else {
                flag1 = this.rand.nextBoolean();
            }

            if (flag) {
                flag1 &= this.rand.nextInt(5) == 0;
            }

            if (flag1 && i > 0) {
                double d0 = (i >> 16 & 255) / 255.0D;
                double d1 = (i >> 8 & 255) / 255.0D;
                double d2 = (i >> 0 & 255) / 255.0D;

                this.world.spawnParticle(flag ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, d0, d1, d2, new int[0]);
            }
        }

    }

    protected void updatePotionMetadata() {
        if (this.activePotionsMap.isEmpty()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(false);
        } else {
            Collection collection = this.activePotionsMap.values();

            this.dataManager.set(EntityLivingBase.HIDE_PARTICLES, Boolean.valueOf(areAllPotionsAmbient(collection)));
            this.dataManager.set(EntityLivingBase.POTION_EFFECTS, Integer.valueOf(PotionUtils.getPotionColorFromEffectList(collection)));
            this.setInvisible(this.isPotionActive(MobEffects.INVISIBILITY));
        }

    }

    public static boolean areAllPotionsAmbient(Collection<PotionEffect> collection) {
        Iterator iterator = collection.iterator();

        PotionEffect mobeffect;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            mobeffect = (PotionEffect) iterator.next();
        } while (mobeffect.getIsAmbient());

        return false;
    }

    protected void resetPotionEffectMetadata() {
        this.dataManager.set(EntityLivingBase.HIDE_PARTICLES, Boolean.valueOf(false));
        this.dataManager.set(EntityLivingBase.POTION_EFFECTS, Integer.valueOf(0));
    }

    public void clearActivePotions() {
        if (!this.world.isRemote) {
            Iterator iterator = this.activePotionsMap.values().iterator();

            while (iterator.hasNext()) {
                this.onFinishedPotionEffect((PotionEffect) iterator.next());
                iterator.remove();
            }

        }
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        return this.activePotionsMap.values();
    }

    public Map<Potion, PotionEffect> getActivePotionMap() {
        return this.activePotionsMap;
    }

    public boolean isPotionActive(Potion mobeffectlist) {
        return this.activePotionsMap.containsKey(mobeffectlist);
    }

    @Nullable
    public PotionEffect getActivePotionEffect(Potion mobeffectlist) {
        return this.activePotionsMap.get(mobeffectlist);
    }

    public void addPotionEffect(PotionEffect mobeffect) {
        org.spigotmc.AsyncCatcher.catchOp( "effect add"); // Spigot
        // CraftBukkit start
        if (isTickingEffects) {
            effectsToProcess.add(mobeffect);
            return;
        }
        // CraftBukkit end
        if (this.isPotionApplicable(mobeffect)) {
            PotionEffect mobeffect1 = this.activePotionsMap.get(mobeffect.getPotion());

            if (mobeffect1 == null) {
                this.activePotionsMap.put(mobeffect.getPotion(), mobeffect);
                this.onNewPotionEffect(mobeffect);
            } else {
                mobeffect1.combine(mobeffect);
                this.onChangedPotionEffect(mobeffect1, true);
            }

        }
    }

    public boolean isPotionApplicable(PotionEffect mobeffect) {
        if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
            Potion mobeffectlist = mobeffect.getPotion();

            if (mobeffectlist == MobEffects.REGENERATION || mobeffectlist == MobEffects.POISON) {
                return false;
            }
        }

        return true;
    }

    public boolean isEntityUndead() {
        return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    @Nullable
    public PotionEffect removeActivePotionEffect(@Nullable Potion mobeffectlist) {
        // CraftBukkit start
        if (isTickingEffects) {
            effectsToProcess.add(mobeffectlist);
            return null;
        }
        // CraftBukkit end
        return this.activePotionsMap.remove(mobeffectlist);
    }

    public void removePotionEffect(Potion mobeffectlist) {
        PotionEffect mobeffect = this.removeActivePotionEffect(mobeffectlist);

        if (mobeffect != null) {
            this.onFinishedPotionEffect(mobeffect);
        }

    }

    protected void onNewPotionEffect(PotionEffect mobeffect) {
        this.potionsNeedUpdate = true;
        if (!this.world.isRemote) {
            mobeffect.getPotion().applyAttributesModifiersToEntity(this, this.getAttributeMap(), mobeffect.getAmplifier());
        }

    }

    protected void onChangedPotionEffect(PotionEffect mobeffect, boolean flag) {
        this.potionsNeedUpdate = true;
        if (flag && !this.world.isRemote) {
            Potion mobeffectlist = mobeffect.getPotion();

            mobeffectlist.removeAttributesModifiersFromEntity(this, this.getAttributeMap(), mobeffect.getAmplifier());
            mobeffectlist.applyAttributesModifiersToEntity(this, this.getAttributeMap(), mobeffect.getAmplifier());
        }

    }

    protected void onFinishedPotionEffect(PotionEffect mobeffect) {
        this.potionsNeedUpdate = true;
        if (!this.world.isRemote) {
            mobeffect.getPotion().removeAttributesModifiersFromEntity(this, this.getAttributeMap(), mobeffect.getAmplifier());
        }

    }

    // CraftBukkit start - Delegate so we can handle providing a reason for health being regained
    public void heal(float f) {
        heal(f, EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    public void heal(float f, EntityRegainHealthEvent.RegainReason regainReason) {
        // Paper start - Forward
        heal(f, regainReason, false);
    }

    public void heal(float f, EntityRegainHealthEvent.RegainReason regainReason, boolean isFastRegen) {
        // Paper end
        float f1 = this.getHealth();

        if (f1 > 0.0F) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), f, regainReason, isFastRegen); // Paper - Add isFastRegen
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.setHealth((float) (this.getHealth() + event.getAmount()));
            }
            // CraftBukkit end
        }

    }

    public final float getHealth() {
        // CraftBukkit start - Use unscaled health
        if (this instanceof EntityPlayerMP) {
            return (float) ((EntityPlayerMP) this).getBukkitEntity().getHealth();
        }
        // CraftBukkit end
        return this.dataManager.get(EntityLivingBase.HEALTH).floatValue();
    }

    public void setHealth(float f) {
        // Paper start
        if (Float.isNaN(f)) { f = getMaxHealth(); if (this.valid) {
            System.err.println("[NAN-HEALTH] " + getName() + " had NaN health set");
        } } // Paper end
        // CraftBukkit start - Handle scaled health
        if (this instanceof EntityPlayerMP) {
            org.bukkit.craftbukkit.entity.CraftPlayer player = ((EntityPlayerMP) this).getBukkitEntity();
            // Squeeze
            if (f < 0.0F) {
                player.setRealHealth(0.0D);
            } else if (f > player.getMaxHealth()) {
                player.setRealHealth(player.getMaxHealth());
            } else {
                player.setRealHealth(f);
            }

            player.updateScaledHealth();
            return;
        }
        // CraftBukkit end
        this.dataManager.set(EntityLivingBase.HEALTH, Float.valueOf(MathHelper.clamp(f, 0.0F, this.getMaxHealth())));
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (this.world.isRemote) {
            return false;
        } else {
            this.idleTime = 0;
            if (this.getHealth() <= 0.0F) {
                return false;
            } else if (damagesource.isFireDamage() && this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                return false;
            } else {
                float f1 = f;

                // CraftBukkit - Moved into damageEntity0(DamageSource, float)
                if (false && (damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && !this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
                    this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int) (f * 4.0F + this.rand.nextFloat() * f * 2.0F), this);
                    f *= 0.75F;
                }

                boolean flag = f > 0.0F && this.canBlockDamageSource(damagesource); // Copied from below

                // CraftBukkit - Moved into damageEntity0(DamageSource, float)
                if (false && f > 0.0F && this.canBlockDamageSource(damagesource)) {
                    this.damageShield(f);
                    f = 0.0F;
                    if (!damagesource.isProjectile()) {
                        Entity entity = damagesource.getImmediateSource();

                        if (entity instanceof EntityLivingBase) {
                            this.blockUsingShield((EntityLivingBase) entity);
                        }
                    }

                    flag = true;
                }

                this.limbSwingAmount = 1.5F;
                boolean flag1 = true;

                if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F) {
                    if (f <= this.lastDamage) {
                        this.forceExplosionKnockback = true; // CraftBukkit - SPIGOT-949 - for vanilla consistency, cooldown does not prevent explosion knockback
                        return false;
                    }

                    // CraftBukkit start
                    if (!this.damageEntity0(damagesource, f - this.lastDamage)) {
                        return false;
                    }
                    // CraftBukkit end
                    this.lastDamage = f;
                    flag1 = false;
                } else {
                    // CraftBukkit start
                    if (!this.damageEntity0(damagesource, f)) {
                        return false;
                    }
                    this.lastDamage = f;
                    this.hurtResistantTime = this.maxHurtResistantTime;
                    // this.damageEntity0(damagesource, f);
                    // CraftBukkit end
                    this.maxHurtTime = 10;
                    this.hurtTime = this.maxHurtTime;
                }

                // CraftBukkit start
                if (this instanceof EntityAnimal) {
                    ((EntityAnimal) this).resetInLove();
                    if (this instanceof EntityTameable) {
                        ((EntityTameable) this).getAISit().setSitting(false);
                    }
                }
                // CraftBukkit end

                this.attackedAtYaw = 0.0F;
                Entity entity1 = damagesource.getTrueSource();

                if (entity1 != null) {
                    if (entity1 instanceof EntityLivingBase) {
                        this.setRevengeTarget((EntityLivingBase) entity1);
                    }

                    if (entity1 instanceof EntityPlayer) {
                        this.recentlyHit = 100;
                        this.attackingPlayer = (EntityPlayer) entity1;
                    } else if (entity1 instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf) entity1;

                        if (entitywolf.isTamed()) {
                            this.recentlyHit = 100;
                            this.attackingPlayer = null;
                        }
                    }
                }

                boolean knockbackCancelled = world.paperConfig.disableExplosionKnockback && damagesource.isExplosion() && this instanceof EntityPlayer; // Paper - Disable explosion knockback
                if (flag1) {
                    if (flag) {
                        this.world.setEntityState(this, (byte) 29);
                    } else if (damagesource instanceof EntityDamageSource && ((EntityDamageSource) damagesource).getIsThornsDamage()) {
                        this.world.setEntityState(this, (byte) 33);
                    } else {
                        byte b0;

                        if (damagesource == DamageSource.DROWN) {
                            b0 = 36;
                        } else if (damagesource.isFireDamage()) {
                            b0 = 37;
                        } else {
                            b0 = 2;
                        }

                        if (!knockbackCancelled) // Paper - Disable explosion knockback
                        this.world.setEntityState(this, b0);
                    }

                    if (damagesource != DamageSource.DROWN && (!flag || f > 0.0F)) {
                        this.markVelocityChanged();
                    }

                    if (entity1 != null) {
                        double d0 = entity1.posX - this.posX;

                        double d1;

                        for (d1 = entity1.posZ - this.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.attackedAtYaw = (float) (MathHelper.atan2(d1, d0) * 57.2957763671875D - this.rotationYaw);
                        this.knockBack(entity1, 0.4F, d0, d1);
                    } else {
                        this.attackedAtYaw = (int) (Math.random() * 2.0D) * 180;
                    }
                }

                if (knockbackCancelled) this.world.setEntityState(this, (byte) 2); // Paper - Disable explosion knockback

                if (this.getHealth() <= 0.0F) {
                    if (!this.checkTotemDeathProtection(damagesource)) {
                        SoundEvent soundeffect = this.getDeathSound();

                        if (flag1 && soundeffect != null) {
                            this.playSound(soundeffect, this.getSoundVolume(), this.getSoundPitch());
                        }

                        this.onDeath(damagesource);
                    }
                } else if (flag1) {
                    this.playHurtSound(damagesource);
                }

                boolean flag2 = !flag || f > 0.0F;

                if (flag2) {
                    this.lastDamageSource = damagesource;
                    this.lastDamageStamp = this.world.getTotalWorldTime();
                }

                if (this instanceof EntityPlayerMP) {
                    CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((EntityPlayerMP) this, damagesource, f1, f, flag);
                }

                if (entity1 instanceof EntityPlayerMP) {
                    CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((EntityPlayerMP) entity1, this, damagesource, f1, f, flag);
                }

                return flag2;
            }
        }
    }

    protected void blockUsingShield(EntityLivingBase entityliving) {
        entityliving.knockBack(this, 0.5F, this.posX - entityliving.posX, this.posZ - entityliving.posZ);
    }

    private boolean checkTotemDeathProtection(DamageSource damagesource) {
        if (damagesource.canHarmInCreative()) {
            return false;
        } else {
            ItemStack itemstack = null;
            EnumHand[] aenumhand = EnumHand.values();
            int i = aenumhand.length;

            // CraftBukkit start
            ItemStack itemstack1 = ItemStack.EMPTY;
            for (int j = 0; j < i; ++j) {
                EnumHand enumhand = aenumhand[j];
                itemstack1 = this.getHeldItem(enumhand);

                if (itemstack1.getItem() == Items.TOTEM_OF_UNDYING) {
                    itemstack = itemstack1.copy();
                    // itemstack1.subtract(1); // CraftBukkit
                    break;
                }
            }

            EntityResurrectEvent event = new EntityResurrectEvent((LivingEntity) this.getBukkitEntity());
            event.setCancelled(itemstack == null);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (!itemstack1.isEmpty()) {
                    itemstack1.shrink(1);
                }
                if (itemstack != null && this instanceof EntityPlayerMP) {
                    // CraftBukkit end
                    EntityPlayerMP entityplayer = (EntityPlayerMP) this;

                    entityplayer.addStat(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING));
                    CriteriaTriggers.USED_TOTEM.trigger(entityplayer, itemstack);
                }

                this.setHealth(1.0F);
                this.clearActivePotions();
                this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
                this.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
                this.world.setEntityState(this, (byte) 35);
            }

            return !event.isCancelled();
        }
    }

    @Nullable
    public DamageSource getLastDamageSource() {
        if (this.world.getTotalWorldTime() - this.lastDamageStamp > 40L) {
            this.lastDamageSource = null;
        }

        return this.lastDamageSource;
    }

    protected void playHurtSound(DamageSource damagesource) {
        SoundEvent soundeffect = this.getHurtSound(damagesource);

        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.getSoundPitch());
        }

    }

    private boolean canBlockDamageSource(DamageSource damagesource) {
        if (!damagesource.isUnblockable() && this.isActiveItemStackBlocking()) {
            Vec3d vec3d = damagesource.getDamageLocation();

            if (vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();

                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
                if (vec3d2.dotProduct(vec3d1) < 0.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    public void renderBrokenItemStack(ItemStack itemstack) {
        this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);

        for (int i = 0; i < 5; ++i) {
            Vec3d vec3d = new Vec3d((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

            vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
            vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
            double d0 = (-this.rand.nextFloat()) * 0.6D - 0.3D;
            Vec3d vec3d1 = new Vec3d((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);

            vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
            vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
            vec3d1 = vec3d1.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, new int[] { Item.getIdFromItem(itemstack.getItem())});
        }

    }

    public void onDeath(DamageSource damagesource) {
        if (!this.dead) {
            Entity entity = damagesource.getTrueSource();
            EntityLivingBase entityliving = this.getAttackingEntity();

            if (this.scoreValue >= 0 && entityliving != null) {
                entityliving.awardKillScore(this, this.scoreValue, damagesource);
            }

            if (entity != null) {
                entity.onKillEntity(this);
            }

            this.dead = true;
            this.getCombatTracker().reset();
            if (!this.world.isRemote) {
                int i = 0;

                if (entity instanceof EntityPlayer) {
                    i = EnchantmentHelper.getLootingModifier((EntityLivingBase) entity);
                }

                if (this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")) {
                    boolean flag = this.recentlyHit > 0;

                    this.dropLoot(flag, i, damagesource);
                    // CraftBukkit start - Call death event
                    CraftEventFactory.callEntityDeathEvent(this, this.drops);
                    this.drops = new ArrayList<org.bukkit.inventory.ItemStack>();
                } else {
                    CraftEventFactory.callEntityDeathEvent(this);
                    // CraftBukkit end
                }
            }

            this.world.setEntityState(this, (byte) 3);
        }
    }

    protected void dropLoot(boolean flag, int i, DamageSource damagesource) {
        this.dropFewItems(flag, i);
        this.dropEquipment(flag, i);
    }

    protected void dropEquipment(boolean flag, int i) {}

    public void knockBack(Entity entity, float f, double d0, double d1) {
        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue()) {
            this.isAirBorne = true;
            float f1 = MathHelper.sqrt(d0 * d0 + d1 * d1);

            this.motionX /= 2.0D;
            this.motionZ /= 2.0D;
            this.motionX -= d0 / f1 * f;
            this.motionZ -= d1 / f1 * f;
            if (this.onGround) {
                this.motionY /= 2.0D;
                this.motionY += f;
                if (this.motionY > 0.4000000059604645D) {
                    this.motionY = 0.4000000059604645D;
                }
            }

        }
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_GENERIC_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GENERIC_DEATH;
    }

    protected SoundEvent getFallSound(int i) {
        return i > 4 ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
    }

    protected void dropFewItems(boolean flag, int i) {}

    public boolean isOnLadder() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);

        if (this instanceof EntityPlayer && ((EntityPlayer) this).isSpectator()) {
            return false;
        } else {
            BlockPos blockposition = new BlockPos(i, j, k);
            IBlockState iblockdata = this.world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();

            return block != Blocks.LADDER && block != Blocks.VINE ? block instanceof BlockTrapDoor && this.canGoThroughtTrapDoorOnLadder(blockposition, iblockdata) : true;
        }
    }

    private boolean canGoThroughtTrapDoorOnLadder(BlockPos blockposition, IBlockState iblockdata) {
        if (iblockdata.getValue(BlockTrapDoor.OPEN).booleanValue()) {
            IBlockState iblockdata1 = this.world.getBlockState(blockposition.down());

            if (iblockdata1.getBlock() == Blocks.LADDER && iblockdata1.getValue(BlockLadder.FACING) == iblockdata.getValue(BlockTrapDoor.FACING)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isEntityAlive() {
        return !this.isDead && this.getHealth() > 0.0F;
    }

    @Override
    public void fall(float f, float f1) {
        super.fall(f, f1);
        PotionEffect mobeffect = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f2 = mobeffect == null ? 0.0F : (float) (mobeffect.getAmplifier() + 1);
        int i = MathHelper.ceil((f - 3.0F - f2) * f1);

        if (i > 0) {
            // CraftBukkit start
            if (!this.attackEntityFrom(DamageSource.FALL, i)) {
                return;
            }
            // CraftBukkit end
            this.playSound(this.getFallSound(i), 1.0F, 1.0F);
            // this.damageEntity(DamageSource.FALL, (float) i); // CraftBukkit - moved up
            int j = MathHelper.floor(this.posX);
            int k = MathHelper.floor(this.posY - 0.20000000298023224D);
            int l = MathHelper.floor(this.posZ);
            IBlockState iblockdata = this.world.getBlockState(new BlockPos(j, k, l));

            if (iblockdata.getMaterial() != Material.AIR) {
                SoundType soundeffecttype = iblockdata.getBlock().getSoundType();

                this.playSound(soundeffecttype.getFallSound(), soundeffecttype.getVolume() * 0.5F, soundeffecttype.getPitch() * 0.75F);
            }
        }

    }

    public int getTotalArmorValue() {
        IAttributeInstance attributeinstance = this.getEntityAttribute(SharedMonsterAttributes.ARMOR);

        return MathHelper.floor(attributeinstance.getAttributeValue());
    }

    protected void damageArmor(float f) {}

    protected void damageShield(float f) {}

    protected float applyArmorCalculations(DamageSource damagesource, float f) {
        if (!damagesource.isUnblockable()) {
            // this.damageArmor(f); // CraftBukkit - Moved into damageEntity0(DamageSource, float)
            f = CombatRules.getDamageAfterAbsorb(f, this.getTotalArmorValue(), (float) this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        }

        return f;
    }

    protected float applyPotionDamageCalculations(DamageSource damagesource, float f) {
        if (damagesource.isDamageAbsolute()) {
            return f;
        } else {
            int i;

            // CraftBukkit - Moved to damageEntity0(DamageSource, float)
            if (false && this.isPotionActive(MobEffects.RESISTANCE) && damagesource != DamageSource.OUT_OF_WORLD) {
                i = (this.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f1 = f * j;

                f = f1 / 25.0F;
            }

            if (f <= 0.0F) {
                return 0.0F;
            } else {
                i = EnchantmentHelper.getEnchantmentModifierDamage(this.getArmorInventoryList(), damagesource);
                if (i > 0) {
                    f = CombatRules.getDamageAfterMagicAbsorb(f, i);
                }

                return f;
            }
        }
    }

    // CraftBukkit start
    protected boolean damageEntity0(final DamageSource damagesource, float f) { // void -> boolean, add final
       if (!this.isEntityInvulnerable(damagesource)) {
            final boolean human = this instanceof EntityPlayer;
            float originalDamage = f;
            Function<Double, Double> hardHat = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && !EntityLivingBase.this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
                        return -(f - (f * 0.75F));

                    }
                    return -0.0;
                }
            };
            float hardHatModifier = hardHat.apply((double) f).floatValue();
            f += hardHatModifier;

            Function<Double, Double> blocking = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -((EntityLivingBase.this.canBlockDamageSource(damagesource)) ? f : 0.0);
                }
            };
            float blockingModifier = blocking.apply((double) f).floatValue();
            f += blockingModifier;

            Function<Double, Double> armor = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(f - EntityLivingBase.this.applyArmorCalculations(damagesource, f.floatValue()));
                }
            };
            float armorModifier = armor.apply((double) f).floatValue();
            f += armorModifier;

            Function<Double, Double> resistance = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    if (!damagesource.isDamageAbsolute() && EntityLivingBase.this.isPotionActive(MobEffects.RESISTANCE) && damagesource != DamageSource.OUT_OF_WORLD) {
                        int i = (EntityLivingBase.this.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                        int j = 25 - i;
                        float f1 = f.floatValue() * j;
                        return -(f - (f1 / 25.0F));
                    }
                    return -0.0;
                }
            };
            float resistanceModifier = resistance.apply((double) f).floatValue();
            f += resistanceModifier;

            Function<Double, Double> magic = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(f - EntityLivingBase.this.applyPotionDamageCalculations(damagesource, f.floatValue()));
                }
            };
            float magicModifier = magic.apply((double) f).floatValue();
            f += magicModifier;

            Function<Double, Double> absorption = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(Math.max(f - Math.max(f - EntityLivingBase.this.getAbsorptionAmount(), 0.0F), 0.0F));
                }
            };
            float absorptionModifier = absorption.apply((double) f).floatValue();

            EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent(this, damagesource, originalDamage, hardHatModifier, blockingModifier, armorModifier, resistanceModifier, magicModifier, absorptionModifier, hardHat, blocking, armor, resistance, magic, absorption);
            if (event.isCancelled()) {
                return false;
            }

            f = (float) event.getFinalDamage();

            // Apply damage to helmet
            if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null) {
                this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int) (event.getDamage() * 4.0F + this.rand.nextFloat() * event.getDamage() * 2.0F), this);
            }

            // Apply damage to armor
            if (!damagesource.isUnblockable()) {
                float armorDamage = (float) (event.getDamage() + event.getDamage(DamageModifier.BLOCKING) + event.getDamage(DamageModifier.HARD_HAT));
                this.damageArmor(armorDamage);
            }

            // Apply blocking code // PAIL: steal from above
            if (event.getDamage(DamageModifier.BLOCKING) < 0) {
                this.damageShield((float) -event.getDamage(DamageModifier.BLOCKING));
                Entity entity = damagesource.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }

            absorptionModifier = (float) -event.getDamage(DamageModifier.ABSORPTION);
            this.setAbsorptionAmount(Math.max(this.getAbsorptionAmount() - absorptionModifier, 0.0F));
            if (f > 0 || !human) {
                if (human) {
                    // PAIL: Be sure to drag all this code from the EntityHuman subclass each update.
                    ((EntityPlayer) this).addExhaustion(damagesource.getHungerDamage());
                    if (f < 3.4028235E37F) {
                        ((EntityPlayer) this).addStat(StatList.DAMAGE_TAKEN, Math.round(f * 10.0F));
                    }
                }
                // CraftBukkit end
                float f2 = this.getHealth();

                this.setHealth(f2 - f);
                this.getCombatTracker().trackDamage(damagesource, f2, f);
                // CraftBukkit start
                if (!human) {
                    this.setAbsorptionAmount(this.getAbsorptionAmount() - f);
                }

                return true;
            } else {
                // Duplicate triggers if blocking
                if (event.getDamage(DamageModifier.BLOCKING) < 0) {
                    if (this instanceof EntityPlayerMP) {
                        CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((EntityPlayerMP) this, damagesource, f, originalDamage, true);
                    }

                    if (damagesource.getTrueSource() instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((EntityPlayerMP) damagesource.getTrueSource(), this, damagesource, f, originalDamage, true);
                    }

                    return false;
                } else {
                    return originalDamage > 0;
                }
                // CraftBukkit end
            }
        }
        return false; // CraftBukkit
    }

    public CombatTracker getCombatTracker() {
        return this._combatTracker;
    }

    @Nullable
    public EntityLivingBase getAttackingEntity() {
        return this._combatTracker.getBestAttacker() != null ? this._combatTracker.getBestAttacker() : (this.attackingPlayer != null ? this.attackingPlayer : (this.revengeTarget != null ? this.revengeTarget : null));
    }

    public final float getMaxHealth() {
        return (float) this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }

    public final int getArrowCountInEntity() {
        return this.dataManager.get(EntityLivingBase.ARROW_COUNT_IN_ENTITY).intValue();
    }

    public final void setArrowCountInEntity(int i) {
        this.dataManager.set(EntityLivingBase.ARROW_COUNT_IN_ENTITY, Integer.valueOf(i));
    }

    private int getArmSwingAnimationEnd() {
        return this.isPotionActive(MobEffects.HASTE) ? 6 - (1 + this.getActivePotionEffect(MobEffects.HASTE).getAmplifier()) : (this.isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6);
    }

    public void swingArm(EnumHand enumhand) {
        if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
            this.swingProgressInt = -1;
            this.isSwingInProgress = true;
            this.swingingHand = enumhand;
            if (this.world instanceof WorldServer) {
                ((WorldServer) this.world).getEntityTracker().sendToTracking(this, (new SPacketAnimation(this, enumhand == EnumHand.MAIN_HAND ? 0 : 3)));
            }
        }

    }

    @Override
    protected void outOfWorld() {
        this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0F);
    }

    protected void updateArmSwingProgress() {
        int i = this.getArmSwingAnimationEnd();

        if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= i) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        } else {
            this.swingProgressInt = 0;
        }

        this.swingProgress = (float) this.swingProgressInt / (float) i;
    }

    public IAttributeInstance getEntityAttribute(IAttribute iattribute) {
        return this.getAttributeMap().getAttributeInstance(iattribute);
    }

    public AbstractAttributeMap getAttributeMap() {
        if (this.attributeMap == null) {
            this.attributeMap = new AttributeMap();
            this.craftAttributes = new CraftAttributeMap(attributeMap); // CraftBukkit
        }

        return this.attributeMap;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public ItemStack getHeldItemMainhand() {
        return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
    }

    public ItemStack getHeldItemOffhand() {
        return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
    }

    public ItemStack getHeldItem(EnumHand enumhand) {
        if (enumhand == EnumHand.MAIN_HAND) {
            return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        } else if (enumhand == EnumHand.OFF_HAND) {
            return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        } else {
            throw new IllegalArgumentException("Invalid hand " + enumhand);
        }
    }

    public void setHeldItem(EnumHand enumhand, ItemStack itemstack) {
        if (enumhand == EnumHand.MAIN_HAND) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack);
        } else {
            if (enumhand != EnumHand.OFF_HAND) {
                throw new IllegalArgumentException("Invalid hand " + enumhand);
            }

            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemstack);
        }

    }

    public boolean hasItemInSlot(EntityEquipmentSlot enumitemslot) {
        return !this.getItemStackFromSlot(enumitemslot).isEmpty();
    }

    @Override
    public abstract Iterable<ItemStack> getArmorInventoryList();

    public abstract ItemStack getItemStackFromSlot(EntityEquipmentSlot enumitemslot);

    @Override
    public abstract void setItemStackToSlot(EntityEquipmentSlot enumitemslot, ItemStack itemstack);

    @Override
    public void setSprinting(boolean flag) {
        super.setSprinting(flag);
        IAttributeInstance attributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (attributeinstance.getModifier(EntityLivingBase.SPRINTING_SPEED_BOOST_ID) != null) {
            attributeinstance.removeModifier(EntityLivingBase.SPRINTING_SPEED_BOOST);
        }

        if (flag) {
            attributeinstance.applyModifier(EntityLivingBase.SPRINTING_SPEED_BOOST);
        }

    }

    protected float getSoundVolume() {
        return 1.0F;
    }

    protected float getSoundPitch() {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
    }

    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0F;
    }

    public void dismountEntity(Entity entity) {
        double d0;

        if (!(entity instanceof EntityBoat) && !(entity instanceof AbstractHorse)) {
            double d1 = entity.posX;
            double d2 = entity.getEntityBoundingBox().minY + entity.height;

            d0 = entity.posZ;
            EnumFacing enumdirection = entity.getAdjustedHorizontalFacing();

            if (enumdirection != null) {
                EnumFacing enumdirection1 = enumdirection.rotateY();
                int[][] aint = new int[][] { { 0, 1}, { 0, -1}, { -1, 1}, { -1, -1}, { 1, 1}, { 1, -1}, { -1, 0}, { 1, 0}, { 0, 1}};
                double d3 = Math.floor(this.posX) + 0.5D;
                double d4 = Math.floor(this.posZ) + 0.5D;
                double d5 = this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX;
                double d6 = this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ;
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(d3 - d5 / 2.0D, entity.getEntityBoundingBox().minY, d4 - d6 / 2.0D, d3 + d5 / 2.0D, Math.floor(entity.getEntityBoundingBox().minY) + this.height, d4 + d6 / 2.0D);
                int[][] aint1 = aint;
                int i = aint.length;

                for (int j = 0; j < i; ++j) {
                    int[] aint2 = aint1[j];
                    double d7 = enumdirection.getFrontOffsetX() * aint2[0] + enumdirection1.getFrontOffsetX() * aint2[1];
                    double d8 = enumdirection.getFrontOffsetZ() * aint2[0] + enumdirection1.getFrontOffsetZ() * aint2[1];
                    double d9 = d3 + d7;
                    double d10 = d4 + d8;
                    AxisAlignedBB axisalignedbb1 = axisalignedbb.offset(d7, 0.0D, d8);

                    if (!this.world.collidesWithAnyBlock(axisalignedbb1)) {
                        if (this.world.getBlockState(new BlockPos(d9, this.posY, d10)).isTopSolid()) {
                            this.setPositionAndUpdate(d9, this.posY + 1.0D, d10);
                            return;
                        }

                        BlockPos blockposition = new BlockPos(d9, this.posY - 1.0D, d10);

                        if (this.world.getBlockState(blockposition).isTopSolid() || this.world.getBlockState(blockposition).getMaterial() == Material.WATER) {
                            d1 = d9;
                            d2 = this.posY + 1.0D;
                            d0 = d10;
                        }
                    } else if (!this.world.collidesWithAnyBlock(axisalignedbb1.offset(0.0D, 1.0D, 0.0D)) && this.world.getBlockState(new BlockPos(d9, this.posY + 1.0D, d10)).isTopSolid()) {
                        d1 = d9;
                        d2 = this.posY + 2.0D;
                        d0 = d10;
                    }
                }
            }

            this.setPositionAndUpdate(d1, d2, d0);
        } else {
            double d11 = this.width / 2.0F + entity.width / 2.0F + 0.4D;
            float f;

            if (entity instanceof EntityBoat) {
                f = 0.0F;
            } else {
                f = 1.5707964F * (this.getPrimaryHand() == EnumHandSide.RIGHT ? -1 : 1);
            }

            float f1 = -MathHelper.sin(-this.rotationYaw * 0.017453292F - 3.1415927F + f);
            float f2 = -MathHelper.cos(-this.rotationYaw * 0.017453292F - 3.1415927F + f);

            d0 = Math.abs(f1) > Math.abs(f2) ? d11 / Math.abs(f1) : d11 / Math.abs(f2);
            double d12 = this.posX + f1 * d0;
            double d13 = this.posZ + f2 * d0;

            this.setPosition(d12, entity.posY + entity.height + 0.001D, d13);
            if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
                this.setPosition(d12, entity.posY + entity.height + 1.001D, d13);
                if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox())) {
                    this.setPosition(entity.posX, entity.posY + this.height + 0.001D, entity.posZ);
                }
            }
        }
    }

    protected float getJumpUpwardsMotion() {
        return 0.42F;
    }

    protected void jump() {
        this.motionY = this.getJumpUpwardsMotion();
        if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
            this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting()) {
            float f = this.rotationYaw * 0.017453292F;

            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
    }

    protected void handleJumpWater() {
        this.motionY += 0.03999999910593033D;
    }

    protected void handleJumpLava() {
        this.motionY += 0.03999999910593033D;
    }

    protected float getWaterSlowDown() {
        return 0.8F;
    }

    public void travel(float f, float f1, float f2) {
        double d0;
        double d1;
        double d2;

        if (this.isServerWorld() || this.canPassengerSteer()) {
            float f3;
            float f4;
            float f5;

            if (this.isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying)) {
                d2 = this.posY;
                f4 = this.getWaterSlowDown();
                f3 = 0.02F;
                f5 = EnchantmentHelper.getDepthStriderModifier(this);
                if (f5 > 3.0F) {
                    f5 = 3.0F;
                }

                if (!this.onGround) {
                    f5 *= 0.5F;
                }

                if (f5 > 0.0F) {
                    f4 += (0.54600006F - f4) * f5 / 3.0F;
                    f3 += (this.getAIMoveSpeed() - f3) * f5 / 3.0F;
                }

                this.moveRelative(f, f1, f2, f3);
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= f4;
                this.motionY *= 0.800000011920929D;
                this.motionZ *= f4;
                if (!this.hasNoGravity()) {
                    this.motionY -= 0.02D;
                }

                if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d2, this.motionZ)) {
                    this.motionY = 0.30000001192092896D;
                }
            } else if (this.isInLava() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying)) {
                d2 = this.posY;
                this.moveRelative(f, f1, f2, 0.02F);
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
                if (!this.hasNoGravity()) {
                    this.motionY -= 0.02D;
                }

                if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d2, this.motionZ)) {
                    this.motionY = 0.30000001192092896D;
                }
            } else if (this.isElytraFlying()) {
                if (world.paperConfig.elytraHitWallDamage) { // Paper start - Toggleable Elytra Wall Damage
                if (this.motionY > -0.5D) {
                    this.fallDistance = 1.0F;
                }

                Vec3d vec3d = this.getLookVec();
                float f6 = this.rotationPitch * 0.017453292F;

                d0 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                d1 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                double d3 = vec3d.lengthVector();
                float f7 = MathHelper.cos(f6);

                f7 = (float) ((double) f7 * (double) f7 * Math.min(1.0D, d3 / 0.4D));
                this.motionY += -0.08D + f7 * 0.06D;
                double d4;

                if (this.motionY < 0.0D && d0 > 0.0D) {
                    d4 = this.motionY * -0.1D * f7;
                    this.motionY += d4;
                    this.motionX += vec3d.x * d4 / d0;
                    this.motionZ += vec3d.z * d4 / d0;
                }

                if (f6 < 0.0F) {
                    d4 = d1 * (-MathHelper.sin(f6)) * 0.04D;
                    this.motionY += d4 * 3.2D;
                    this.motionX -= vec3d.x * d4 / d0;
                    this.motionZ -= vec3d.z * d4 / d0;
                }

                if (d0 > 0.0D) {
                    this.motionX += (vec3d.x / d0 * d1 - this.motionX) * 0.1D;
                    this.motionZ += (vec3d.z / d0 * d1 - this.motionZ) * 0.1D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.9800000190734863D;
                this.motionZ *= 0.9900000095367432D;
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                if (this.collidedHorizontally && !this.world.isRemote) {
                    d4 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    double d5 = d1 - d4;
                    float f8 = (float) (d5 * 10.0D - 3.0D);

                    if (f8 > 0.0F) {
                        this.playSound(this.getFallSound((int) f8), 1.0F, 1.0F);
                        this.attackEntityFrom(DamageSource.FLY_INTO_WALL, f8);
                    }
                }
                } // Paper end - Elyta Wall Damage if statement

                if (this.onGround && !this.world.isRemote) {
                    if (getFlag(7) && !CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) // CraftBukkit
                    this.setFlag(7, false);
                }
            } else {
                float f9 = 0.91F;
                BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ);

                if (this.onGround) {
                    f9 = this.world.getBlockState(blockposition_pooledblockposition).getBlock().slipperiness * 0.91F;
                }

                f4 = 0.16277136F / (f9 * f9 * f9);
                if (this.onGround) {
                    f3 = this.getAIMoveSpeed() * f4;
                } else {
                    f3 = this.jumpMovementFactor;
                }

                this.moveRelative(f, f1, f2, f3);
                f9 = 0.91F;
                if (this.onGround) {
                    f9 = this.world.getBlockState(blockposition_pooledblockposition.setPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ)).getBlock().slipperiness * 0.91F;
                }

                if (this.isOnLadder()) {
                    f5 = 0.15F;
                    this.motionX = MathHelper.clamp(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
                    this.motionZ = MathHelper.clamp(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
                    this.fallDistance = 0.0F;
                    if (this.motionY < -0.15D) {
                        this.motionY = -0.15D;
                    }

                    boolean flag = this.isSneaking() && this instanceof EntityPlayer;

                    if (flag && this.motionY < 0.0D) {
                        this.motionY = 0.0D;
                    }
                }

                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                if (this.collidedHorizontally && this.isOnLadder()) {
                    this.motionY = 0.2D;
                }

                if (this.isPotionActive(MobEffects.LEVITATION)) {
                    this.motionY += (0.05D * (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2D;
                } else {
                    blockposition_pooledblockposition.setPos(this.posX, 0.0D, this.posZ);
                    if (this.world.isRemote && (!this.world.isBlockLoaded(blockposition_pooledblockposition) || !this.world.getChunkFromBlockCoords(blockposition_pooledblockposition).isLoaded())) {
                        if (this.posY > 0.0D) {
                            this.motionY = -0.1D;
                        } else {
                            this.motionY = 0.0D;
                        }
                    } else if (!this.hasNoGravity()) {
                        this.motionY -= 0.08D;
                    }
                }

                this.motionY *= 0.9800000190734863D;
                this.motionX *= f9;
                this.motionZ *= f9;
                blockposition_pooledblockposition.release();
            }
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        d2 = this.posX - this.prevPosX;
        d0 = this.posZ - this.prevPosZ;
        d1 = this instanceof EntityFlying ? this.posY - this.prevPosY : 0.0D;
        float f10 = MathHelper.sqrt(d2 * d2 + d1 * d1 + d0 * d0) * 4.0F;

        if (f10 > 1.0F) {
            f10 = 1.0F;
        }

        this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    public float getAIMoveSpeed() {
        return this.landMovementFactor;
    }

    public void setAIMoveSpeed(float f) {
        this.landMovementFactor = f;
    }

    public boolean attackEntityAsMob(Entity entity) {
        this.setLastAttackedEntity(entity);
        return false;
    }

    public boolean isPlayerSleeping() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.updateActiveHand();
        if (!this.world.isRemote) {
            int i = this.getArrowCountInEntity();

            if (i > 0) {
                if (this.arrowHitTimer <= 0) {
                    this.arrowHitTimer = 20 * (30 - i);
                }

                --this.arrowHitTimer;
                if (this.arrowHitTimer <= 0) {
                    this.setArrowCountInEntity(i - 1);
                }
            }

            EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
            int j = aenumitemslot.length;

            for (int k = 0; k < j; ++k) {
                EntityEquipmentSlot enumitemslot = aenumitemslot[k];
                ItemStack itemstack;

                switch (enumitemslot.getSlotType()) {
                case HAND:
                    itemstack = this.handInventory.get(enumitemslot.getIndex());
                    break;

                case ARMOR:
                    itemstack = this.armorArray.get(enumitemslot.getIndex());
                    break;

                default:
                    continue;
                }

                ItemStack itemstack1 = this.getItemStackFromSlot(enumitemslot);

                if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                    // Paper start - PlayerArmorChangeEvent
                    if (this instanceof EntityPlayerMP && enumitemslot.getType() == EntityEquipmentSlot.Type.ARMOR && !itemstack.getItem().equals(itemstack1.getItem())) {
                        final org.bukkit.inventory.ItemStack oldItem = CraftItemStack.asBukkitCopy(itemstack);
                        final org.bukkit.inventory.ItemStack newItem = CraftItemStack.asBukkitCopy(itemstack1);
                        new PlayerArmorChangeEvent((Player) this.getBukkitEntity(), PlayerArmorChangeEvent.SlotType.valueOf(enumitemslot.name()), oldItem, newItem).callEvent();
                    }
                    // Paper end
                    ((WorldServer) this.world).getEntityTracker().sendToTracking(this, (new SPacketEntityEquipment(this.getEntityId(), enumitemslot, itemstack1)));
                    if (!itemstack.isEmpty()) {
                        this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(enumitemslot));
                    }

                    if (!itemstack1.isEmpty()) {
                        this.getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(enumitemslot));
                    }

                    switch (enumitemslot.getSlotType()) {
                    case HAND:
                        this.handInventory.set(enumitemslot.getIndex(), itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy());
                        break;

                    case ARMOR:
                        this.armorArray.set(enumitemslot.getIndex(), itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy());
                    }
                }
            }

            if (this.ticksExisted % 20 == 0) {
                this.getCombatTracker().reset();
            }

            if (!this.glowing) {
                boolean flag = this.isPotionActive(MobEffects.GLOWING);

                if (this.getFlag(6) != flag) {
                    this.setFlag(6, flag);
                }
            }
        }

        this.onLivingUpdate();
        double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.renderYawOffset;
        float f2 = 0.0F;

        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        float f3 = 0.0F;

        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt(f) * 3.0F;
            float f4 = (float) MathHelper.atan2(d1, d0) * 57.295776F - 90.0F;
            float f5 = MathHelper.abs(MathHelper.wrapDegrees(this.rotationYaw) - f4);

            if (95.0F < f5 && f5 < 265.0F) {
                f1 = f4 - 180.0F;
            } else {
                f1 = f4;
            }
        }

        if (this.swingProgress > 0.0F) {
            f1 = this.rotationYaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.onGroundSpeedFactor += (f3 - this.onGroundSpeedFactor) * 0.3F;
        this.world.profiler.startSection("headTurn");
        f2 = this.updateDistance(f1, f2);
        this.world.profiler.endSection();
        this.world.profiler.startSection("rangeChecks");

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
            this.prevRenderYawOffset -= 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
            this.prevRenderYawOffset += 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
            this.prevRotationYawHead -= 360.0F;
        }

        while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
            this.prevRotationYawHead += 360.0F;
        }

        this.world.profiler.endSection();
        this.movedDistance += f2;
        if (this.isElytraFlying()) {
            ++this.ticksElytraFlying;
        } else {
            this.ticksElytraFlying = 0;
        }
    }

    protected float updateDistance(float f, float f1) {
        float f2 = MathHelper.wrapDegrees(f - this.renderYawOffset);

        this.renderYawOffset += f2 * 0.3F;
        float f3 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
        boolean flag = f3 < -90.0F || f3 >= 90.0F;

        if (f3 < -75.0F) {
            f3 = -75.0F;
        }

        if (f3 >= 75.0F) {
            f3 = 75.0F;
        }

        this.renderYawOffset = this.rotationYaw - f3;
        if (f3 * f3 > 2500.0F) {
            this.renderYawOffset += f3 * 0.2F;
        }

        if (flag) {
            f1 *= -1.0F;
        }

        return f1;
    }

    public void onLivingUpdate() {
        if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }

        if (this.newPosRotationIncrements > 0 && !this.canPassengerSteer()) {
            double d0 = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
            double d1 = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
            double d2 = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
            double d3 = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);

            this.rotationYaw = (float) (this.rotationYaw + d3 / this.newPosRotationIncrements);
            this.rotationPitch = (float) (this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        } else if (!this.isServerWorld()) {
            this.motionX *= 0.98D;
            this.motionY *= 0.98D;
            this.motionZ *= 0.98D;
        }

        if (Math.abs(this.motionX) < 0.003D) {
            this.motionX = 0.0D;
        }

        if (Math.abs(this.motionY) < 0.003D) {
            this.motionY = 0.0D;
        }

        if (Math.abs(this.motionZ) < 0.003D) {
            this.motionZ = 0.0D;
        }

        this.world.profiler.startSection("ai");
        if (this.isMovementBlocked()) {
            this.isJumping = false;
            this.moveStrafing = 0.0F;
            this.moveForward = 0.0F;
            this.randomYawVelocity = 0.0F;
        } else if (this.isServerWorld()) {
            this.world.profiler.startSection("newAi");
            this.updateEntityActionState();
            this.world.profiler.endSection();
        }

        this.world.profiler.endSection();
        this.world.profiler.startSection("jump");
        if (this.isJumping) {
            if (this.isInWater()) {
                this.handleJumpWater();
            } else if (this.isInLava()) {
                this.handleJumpLava();
            } else if (this.onGround && this.jumpTicks == 0) {
                this.jump();
                this.jumpTicks = 10;
            }
        } else {
            this.jumpTicks = 0;
        }

        this.world.profiler.endSection();
        this.world.profiler.startSection("travel");
        this.moveStrafing *= 0.98F;
        this.moveForward *= 0.98F;
        this.randomYawVelocity *= 0.9F;
        this.updateElytra();
        this.travel(this.moveStrafing, this.moveVertical, this.moveForward);
        this.world.profiler.endSection();
        this.world.profiler.startSection("push");
        this.collideWithNearbyEntities();
        this.world.profiler.endSection();
    }

    private void updateElytra() {
        boolean flag = this.getFlag(7);

        if (flag && !this.onGround && !this.isRiding()) {
            ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack)) {
                flag = true;
                if (!this.world.isRemote && (this.ticksElytraFlying + 1) % 20 == 0) {
                    itemstack.damageItem(1, this);
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }

        if (!this.world.isRemote) {
            if (flag != this.getFlag(7) && !CraftEventFactory.callToggleGlideEvent(this, flag).isCancelled()) // CraftBukkit
            this.setFlag(7, flag);
        }

    }

    protected void updateEntityActionState() {}

    protected void collideWithNearbyEntities() {
        List list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntitySelectors.getTeamCollisionPredicate(this));

        if (!list.isEmpty()) {
            int i = this.world.getGameRules().getInt("maxEntityCramming");
            int j;

            if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0) {
                j = 0;

                for (int k = 0; k < list.size(); ++k) {
                    if (!((Entity) list.get(k)).isRiding()) {
                        ++j;
                    }
                }

                if (j > i - 1) {
                    this.attackEntityFrom(DamageSource.CRAMMING, 6.0F);
                }
            }

            numCollisions = Math.max(0, numCollisions - world.paperConfig.maxCollisionsPerEntity); // Paper
            for (j = 0; j < list.size() && numCollisions < world.paperConfig.maxCollisionsPerEntity; ++j) { // Paper
                Entity entity = (Entity) list.get(j);
                entity.numCollisions++; // Paper
                numCollisions++; // Paper

                this.collideWithEntity(entity);
            }
        }

    }

    protected void collideWithEntity(Entity entity) {
        entity.applyEntityCollision(this);
    }

    @Override
    public void dismountRidingEntity() {
        Entity entity = this.getRidingEntity();

        super.dismountRidingEntity();
        if (entity != null && entity != this.getRidingEntity() && !this.world.isRemote) {
            this.dismountEntity(entity);
        }

    }

    @Override
    public void updateRidden() {
        super.updateRidden();
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        this.onGroundSpeedFactor = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void setJumping(boolean flag) {
        this.isJumping = flag;
    }

    public void onItemPickup(Entity entity, int i) {
        if (!entity.isDead && !this.world.isRemote) {
            EntityTracker entitytracker = ((WorldServer) this.world).getEntityTracker();

            if (entity instanceof EntityItem || entity instanceof EntityArrow || entity instanceof EntityXPOrb) {
                entitytracker.sendToTracking(entity, (new SPacketCollectItem(entity.getEntityId(), this.getEntityId(), i)));
            }
        }

    }

    public boolean canEntityBeSeen(Entity entity) {
        return this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + this.getEyeHeight(), this.posZ), new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ), false, true, false) == null;
    }

    @Override
    public Vec3d getLook(float f) {
        if (f == 1.0F) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
        } else {
            float f1 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * f;
            float f2 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * f;

            return this.getVectorForRotation(f1, f2);
        }
    }

    public boolean isServerWorld() {
        return !this.world.isRemote;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead && this.collides; // CraftBukkit
    }

    @Override
    public boolean canBePushed() {
        return this.isEntityAlive() && !this.isOnLadder() && this.collides; // CraftBukkit
    }

    @Override
    protected void markVelocityChanged() {
        this.velocityChanged = this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
    }

    @Override
    public float getRotationYawHead() {
        return this.rotationYawHead;
    }

    @Override
    public void setRotationYawHead(float f) {
        this.rotationYawHead = f;
    }

    @Override
    public void setRenderYawOffset(float f) {
        this.renderYawOffset = f;
    }

    public float getAbsorptionAmount() {
        return this.absorptionAmount;
    }

    public void setAbsorptionAmount(float f) {
        if (f < 0.0F || Float.isNaN(f)) { // Paper
            f = 0.0F;
        }

        this.absorptionAmount = f;
    }

    public void sendEnterCombat() {}

    public void sendEndCombat() {}

    protected void markPotionsDirty() {
        this.potionsNeedUpdate = true;
    }

    public abstract EnumHandSide getPrimaryHand();

    public boolean isHandActive() {
        return (this.dataManager.get(EntityLivingBase.HAND_STATES).byteValue() & 1) > 0;
    }

    public EnumHand getActiveHand() {
        return (this.dataManager.get(EntityLivingBase.HAND_STATES).byteValue() & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    protected void updateActiveHand() {
        if (this.isHandActive()) {
            ItemStack itemstack = this.getHeldItem(this.getActiveHand());

            if (itemstack == this.activeItemStack) {
                if (this.getItemInUseCount() <= 25 && this.getItemInUseCount() % 4 == 0) {
                    this.updateItemUse(this.activeItemStack, 5);
                }

                if (--this.activeItemStackUseCount == 0 && !this.world.isRemote) {
                    this.onItemUseFinish();
                }
            } else {
                this.resetActiveHand();
            }
        }

    }

    public void setActiveHand(EnumHand enumhand) {
        ItemStack itemstack = this.getHeldItem(enumhand);

        if (!itemstack.isEmpty() && !this.isHandActive()) {
            this.activeItemStack = itemstack;
            this.activeItemStackUseCount = itemstack.getMaxItemUseDuration();
            if (!this.world.isRemote) {
                int i = 1;

                if (enumhand == EnumHand.OFF_HAND) {
                    i |= 2;
                }

                this.dataManager.set(EntityLivingBase.HAND_STATES, Byte.valueOf((byte) i));
            }

        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        super.notifyDataManagerChange(datawatcherobject);
        if (EntityLivingBase.HAND_STATES.equals(datawatcherobject) && this.world.isRemote) {
            if (this.isHandActive() && this.activeItemStack.isEmpty()) {
                this.activeItemStack = this.getHeldItem(this.getActiveHand());
                if (!this.activeItemStack.isEmpty()) {
                    this.activeItemStackUseCount = this.activeItemStack.getMaxItemUseDuration();
                }
            } else if (!this.isHandActive() && !this.activeItemStack.isEmpty()) {
                this.activeItemStack = ItemStack.EMPTY;
                this.activeItemStackUseCount = 0;
            }
        }

    }

    protected void updateItemUse(ItemStack itemstack, int i) {
        if (!itemstack.isEmpty() && this.isHandActive()) {
            if (itemstack.getItemUseAction() == EnumAction.DRINK) {
                this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (itemstack.getItemUseAction() == EnumAction.EAT) {
                for (int j = 0; j < i; ++j) {
                    Vec3d vec3d = new Vec3d((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

                    vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
                    vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
                    double d0 = (-this.rand.nextFloat()) * 0.6D - 0.3D;
                    Vec3d vec3d1 = new Vec3d((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);

                    vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
                    vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
                    vec3d1 = vec3d1.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
                    if (itemstack.getHasSubtypes()) {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, new int[] { Item.getIdFromItem(itemstack.getItem()), itemstack.getMetadata()});
                    } else {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, new int[] { Item.getIdFromItem(itemstack.getItem())});
                    }
                }

                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

        }
    }

    protected void onItemUseFinish() {
        if (!this.activeItemStack.isEmpty() && this.isHandActive()) {
            PlayerItemConsumeEvent event = null; // Paper
            this.updateItemUse(this.activeItemStack, 16);
            // CraftBukkit start - fire PlayerItemConsumeEvent
            ItemStack itemstack;
            if (this instanceof EntityPlayerMP) {
                org.bukkit.inventory.ItemStack craftItem = CraftItemStack.asBukkitCopy(this.activeItemStack);
                event = new PlayerItemConsumeEvent((Player) this.getBukkitEntity(), craftItem); // Paper
                world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    // Update client
                    ((EntityPlayerMP) this).getBukkitEntity().updateInventory();
                    ((EntityPlayerMP) this).getBukkitEntity().updateScaledHealth();
                    return;
                }

                itemstack = (craftItem.equals(event.getItem())) ? this.activeItemStack.onItemUseFinish(this.world, this) : CraftItemStack.asNMSCopy(event.getItem()).onItemUseFinish(world, this);
            } else {
                itemstack = this.activeItemStack.onItemUseFinish(this.world, this);
            }

            // Paper start - save the default replacement item and change it if necessary
            final ItemStack defaultReplacement = itemstack;
            if (event != null && event.getReplacement() != null) {
                itemstack = CraftItemStack.asNMSCopy(event.getReplacement());
            }
            // Paper end

            this.setHeldItem(this.getActiveHand(), itemstack);
            // CraftBukkit end
            this.resetActiveHand();

            // Paper start - if the replacement is anything but the default, update the client inventory
            if (this instanceof EntityPlayerMP && !com.google.common.base.Objects.equal(defaultReplacement, itemstack)) {
                ((EntityPlayerMP) this).getBukkitEntity().updateInventory();
            }
        }

    }

    public ItemStack getActiveItemStack() {
        return this.activeItemStack;
    }

    public int getItemInUseCount() {
        return this.activeItemStackUseCount;
    }

    public int getItemInUseMaxCount() {
        return this.isHandActive() ? this.activeItemStack.getMaxItemUseDuration() - this.getItemInUseCount() : 0;
    }

    public void stopActiveHand() {
        if (!this.activeItemStack.isEmpty()) {
            this.activeItemStack.onPlayerStoppedUsing(this.world, this, this.getItemInUseCount());
        }

        this.resetActiveHand();
    }

    public void resetActiveHand() {
        if (!this.world.isRemote) {
            this.dataManager.set(EntityLivingBase.HAND_STATES, Byte.valueOf((byte) 0));
        }

        this.activeItemStack = ItemStack.EMPTY;
        this.activeItemStackUseCount = 0;
    }

    public boolean isActiveItemStackBlocking() {
        if (this.isHandActive() && !this.activeItemStack.isEmpty()) {
            Item item = this.activeItemStack.getItem();

            return item.getItemUseAction(this.activeItemStack) != EnumAction.BLOCK ? false : item.getMaxItemUseDuration(this.activeItemStack) - this.activeItemStackUseCount >= getShieldBlockingDelay(); // Paper - shieldBlockingDelay
        } else {
            return false;
        }
    }

    public boolean isElytraFlying() {
        return this.getFlag(7);
    }

    public boolean attemptTeleport(double d0, double d1, double d2) {
        double d3 = this.posX;
        double d4 = this.posY;
        double d5 = this.posZ;

        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        boolean flag = false;
        BlockPos blockposition = new BlockPos(this);
        World world = this.world;
        Random random = this.getRNG();
        boolean flag1;

        if (world.isBlockLoaded(blockposition)) {
            flag1 = false;

            while (!flag1 && blockposition.getY() > 0) {
                BlockPos blockposition1 = blockposition.down();
                IBlockState iblockdata = world.getBlockState(blockposition1);

                if (iblockdata.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --this.posY;
                    blockposition = blockposition1;
                }
            }

            if (flag1) {
                // CraftBukkit start - Teleport event
                // this.enderTeleportTo(this.locX, this.locY, this.locZ);
                EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), new Location(this.world.getWorld(), d3, d4, d5), new Location(this.world.getWorld(), this.posX, this.posY, this.posZ));
                this.world.getServer().getPluginManager().callEvent(teleport);
                if (!teleport.isCancelled()) {
                    Location to = teleport.getTo();
                    this.setPositionAndUpdate(to.getX(), to.getY(), to.getZ());
                    if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(this.getEntityBoundingBox())) {
                        flag = true;
                    }
                }
                // CraftBukkit end
            }
        }

        if (!flag) {
            this.setPositionAndUpdate(d3, d4, d5);
            return false;
        } else {
            flag1 = true;

            for (int i = 0; i < 128; ++i) {
                double d6 = i / 127.0D;
                float f = (random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (this.posX - d3) * d6 + (random.nextDouble() - 0.5D) * this.width * 2.0D;
                double d8 = d4 + (this.posY - d4) * d6 + random.nextDouble() * this.height;
                double d9 = d5 + (this.posZ - d5) * d6 + (random.nextDouble() - 0.5D) * this.width * 2.0D;

                world.spawnParticle(EnumParticleTypes.PORTAL, d7, d8, d9, f, f1, f2, new int[0]);
            }

            if (this instanceof EntityCreature) {
                ((EntityCreature) this).getNavigator().clearPath();
            }

            return true;
        }
    }

    public boolean canBeHitWithPotion() {
        return true;
    }

    public boolean attackable() {
        return true;
    }

    // Paper start
    public int shieldBlockingDelay = world.paperConfig.shieldBlockingDelay;

    public int getShieldBlockingDelay() {
        return shieldBlockingDelay;
    }

    public void setShieldBlockingDelay(int shieldBlockingDelay) {
        this.shieldBlockingDelay = shieldBlockingDelay;
    }
    // Paper end
}
