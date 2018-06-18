package net.minecraft.entity.player;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.EntityHuman.c;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapData;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
// CraftBukkit end

public abstract class EntityPlayer extends EntityLivingBase {

    private static final DataParameter<Float> ABSORPTION = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> PLAYER_SCORE = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    protected static final DataParameter<Byte> PLAYER_MODEL_FLAG = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> MAIN_HAND = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
    protected static final DataParameter<NBTTagCompound> LEFT_SHOULDER_ENTITY = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.COMPOUND_TAG);
    protected static final DataParameter<NBTTagCompound> RIGHT_SHOULDER_ENTITY = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.COMPOUND_TAG);
    public InventoryPlayer inventory = new InventoryPlayer(this);
    protected InventoryEnderChest enderChest = new InventoryEnderChest(this); // CraftBukkit - add "this" to constructor
    public Container inventoryContainer;
    public Container openContainer;
    protected FoodStats foodStats = new FoodStats(this); // CraftBukkit - add "this" to constructor
    protected int flyToggleTimer;
    public float prevCameraYaw;
    public float cameraYaw;
    public int xpCooldown;
    public double prevChasingPosX;
    public double prevChasingPosY;
    public double prevChasingPosZ;
    public double chasingPosX;
    public double chasingPosY;
    public double chasingPosZ;
    public boolean sleeping;
    public BlockPos bedLocation;
    public int sleepTimer;
    public float renderOffsetX;
    public float renderOffsetZ;
    private BlockPos spawnPos;
    private boolean spawnForced;
    public PlayerCapabilities capabilities = new PlayerCapabilities();
    public int experienceLevel;
    public int experienceTotal;
    public float experience;
    protected int xpSeed;
    protected float speedInAir = 0.02F;
    private int lastXPSound;
    private GameProfile gameProfile; public void setProfile(GameProfile profile) { this.gameProfile = profile; } // Paper - OBFHELPER
    private ItemStack itemStackMainHand;
    private final CooldownTracker cooldownTracker;
    @Nullable
    public EntityFishHook fishEntity;
    public boolean affectsSpawning = true;

    // CraftBukkit start
    public boolean fauxSleeping;
    public String spawnWorld = "";
    public int oldLevel = -1;

    @Override
    public CraftHumanEntity getBukkitEntity() {
        return (CraftHumanEntity) super.getBukkitEntity();
    }
    // CraftBukkit end

    protected CooldownTracker createCooldownTracker() {
        return new CooldownTracker();
    }

    public EntityPlayer(World world, GameProfile gameprofile) {
        super(world);
        this.itemStackMainHand = ItemStack.EMPTY;
        this.cooldownTracker = this.createCooldownTracker();
        this.setUniqueId(getUUID(gameprofile));
        this.gameProfile = gameprofile;
        this.inventoryContainer = new ContainerPlayer(this.inventory, !world.isRemote, this);
        this.openContainer = this.inventoryContainer;
        BlockPos blockposition = world.getSpawnPoint();

        this.setLocationAndAngles((double) blockposition.getX() + 0.5D, (double) (blockposition.getY() + 1), (double) blockposition.getZ() + 0.5D, 0.0F, 0.0F);
        this.unused180 = 180.0F;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityPlayer.ABSORPTION, Float.valueOf(0.0F));
        this.dataManager.register(EntityPlayer.PLAYER_SCORE, Integer.valueOf(0));
        this.dataManager.register(EntityPlayer.PLAYER_MODEL_FLAG, Byte.valueOf((byte) 0));
        this.dataManager.register(EntityPlayer.MAIN_HAND, Byte.valueOf((byte) 1));
        this.dataManager.register(EntityPlayer.LEFT_SHOULDER_ENTITY, new NBTTagCompound());
        this.dataManager.register(EntityPlayer.RIGHT_SHOULDER_ENTITY, new NBTTagCompound());
    }

    public void onUpdate() {
        this.noClip = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }

        if (this.xpCooldown > 0) {
            --this.xpCooldown;
        }

        if (this.isPlayerSleeping()) {
            ++this.sleepTimer;
            if (this.sleepTimer > 100) {
                this.sleepTimer = 100;
            }

            if (!this.world.isRemote) {
                if (!this.isInBed()) {
                    this.wakeUpPlayer(true, true, false);
                } else if (this.world.isDaytime()) {
                    this.wakeUpPlayer(false, true, true);
                }
            }
        } else if (this.sleepTimer > 0) {
            ++this.sleepTimer;
            if (this.sleepTimer >= 110) {
                this.sleepTimer = 0;
            }
        }

        super.onUpdate();
        if (!this.world.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }

        if (this.isBurning() && this.capabilities.disableDamage) {
            this.extinguish();
        }

        this.updateCape();
        if (!this.world.isRemote) {
            this.foodStats.onUpdate(this);
            this.addStat(StatList.PLAY_ONE_MINUTE);
            if (this.isEntityAlive()) {
                this.addStat(StatList.TIME_SINCE_DEATH);
            }

            if (this.isSneaking()) {
                this.addStat(StatList.SNEAK_TIME);
            }
        }

        int i = 29999999;
        double d0 = MathHelper.clamp(this.posX, -2.9999999E7D, 2.9999999E7D);
        double d1 = MathHelper.clamp(this.posZ, -2.9999999E7D, 2.9999999E7D);

        if (d0 != this.posX || d1 != this.posZ) {
            this.setPosition(d0, this.posY, d1);
        }

        ++this.ticksSinceLastSwing;
        ItemStack itemstack = this.getHeldItemMainhand();

        if (!ItemStack.areItemStacksEqual(this.itemStackMainHand, itemstack)) {
            if (!ItemStack.areItemsEqualIgnoreDurability(this.itemStackMainHand, itemstack)) {
                this.resetCooldown();
            }

            this.itemStackMainHand = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
        }

        this.cooldownTracker.tick();
        this.updateSize();
    }

    private void updateCape() {
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        double d0 = this.posX - this.chasingPosX;
        double d1 = this.posY - this.chasingPosY;
        double d2 = this.posZ - this.chasingPosZ;
        double d3 = 10.0D;

        if (d0 > 10.0D) {
            this.chasingPosX = this.posX;
            this.prevChasingPosX = this.chasingPosX;
        }

        if (d2 > 10.0D) {
            this.chasingPosZ = this.posZ;
            this.prevChasingPosZ = this.chasingPosZ;
        }

        if (d1 > 10.0D) {
            this.chasingPosY = this.posY;
            this.prevChasingPosY = this.chasingPosY;
        }

        if (d0 < -10.0D) {
            this.chasingPosX = this.posX;
            this.prevChasingPosX = this.chasingPosX;
        }

        if (d2 < -10.0D) {
            this.chasingPosZ = this.posZ;
            this.prevChasingPosZ = this.chasingPosZ;
        }

        if (d1 < -10.0D) {
            this.chasingPosY = this.posY;
            this.prevChasingPosY = this.chasingPosY;
        }

        this.chasingPosX += d0 * 0.25D;
        this.chasingPosZ += d2 * 0.25D;
        this.chasingPosY += d1 * 0.25D;
    }

    protected void updateSize() {
        float f;
        float f1;

        if (this.isElytraFlying()) {
            f = 0.6F;
            f1 = 0.6F;
        } else if (this.isPlayerSleeping()) {
            f = 0.2F;
            f1 = 0.2F;
        } else if (this.isSneaking()) {
            f = 0.6F;
            f1 = 1.65F;
        } else {
            f = 0.6F;
            f1 = 1.8F;
        }

        if (f != this.width || f1 != this.height) {
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

            axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double) f, axisalignedbb.minY + (double) f1, axisalignedbb.minZ + (double) f);
            if (!this.world.collidesWithAnyBlock(axisalignedbb)) {
                this.setSize(f, f1);
            }
        }

    }

    public int getMaxInPortalTime() {
        return this.capabilities.disableDamage ? 1 : 80;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_PLAYER_SWIM;
    }

    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_PLAYER_SPLASH;
    }

    public int getPortalCooldown() {
        return 10;
    }

    public void playSound(SoundEvent soundeffect, float f, float f1) {
        this.world.playSound(this, this.posX, this.posY, this.posZ, soundeffect, this.getSoundCategory(), f, f1);
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }

    public int getFireImmuneTicks() {
        return 20;
    }

    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0F || this.isPlayerSleeping();
    }

    public void closeScreen() {
        this.openContainer = this.inventoryContainer;
    }

    public void updateRidden() {
        if (!this.world.isRemote && this.isSneaking() && this.isRiding()) {
            this.dismountRidingEntity();
            this.setSneaking(false);
        } else {
            double d0 = this.posX;
            double d1 = this.posY;
            double d2 = this.posZ;
            float f = this.rotationYaw;
            float f1 = this.rotationPitch;

            super.updateRidden();
            this.prevCameraYaw = this.cameraYaw;
            this.cameraYaw = 0.0F;
            this.addMountedMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
            if (this.getRidingEntity() instanceof EntityPig) {
                this.rotationPitch = f1;
                this.rotationYaw = f;
                this.renderYawOffset = ((EntityPig) this.getRidingEntity()).renderYawOffset;
            }

        }
    }

    protected void updateEntityActionState() {
        super.updateEntityActionState();
        this.updateArmSwingProgress();
        this.rotationYawHead = this.rotationYaw;
    }

    public void onLivingUpdate() {
        if (this.flyToggleTimer > 0) {
            --this.flyToggleTimer;
        }

        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean("naturalRegeneration")) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
                // CraftBukkit - added regain reason of "REGEN" for filtering purposes.
                this.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.REGEN);
            }

            if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
                this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
            }
        }

        this.inventory.decrementAnimations();
        this.prevCameraYaw = this.cameraYaw;
        super.onLivingUpdate();
        IAttributeInstance attributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (!this.world.isRemote) {
            attributeinstance.setBaseValue((double) this.capabilities.getWalkSpeed());
        }

        this.jumpMovementFactor = this.speedInAir;
        if (this.isSprinting()) {
            this.jumpMovementFactor = (float) ((double) this.jumpMovementFactor + (double) this.speedInAir * 0.3D);
        }

        this.setAIMoveSpeed((float) attributeinstance.getAttributeValue());
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f1 = (float) ( org.bukkit.craftbukkit.TrigMath.atan(-this.motionY * 0.20000000298023224D) * 15.0D); // CraftBukkit

        if (f > 0.1F) {
            f = 0.1F;
        }

        if (!this.onGround || this.getHealth() <= 0.0F) {
            f = 0.0F;
        }

        if (this.onGround || this.getHealth() <= 0.0F) {
            f1 = 0.0F;
        }

        this.cameraYaw += (f - this.cameraYaw) * 0.4F;
        this.cameraPitch += (f1 - this.cameraPitch) * 0.8F;
        if (this.getHealth() > 0.0F && !this.isSpectator()) {
            AxisAlignedBB axisalignedbb;

            if (this.isRiding() && !this.getRidingEntity().isDead) {
                axisalignedbb = this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox()).grow(1.0D, 0.0D, 1.0D);
            } else {
                axisalignedbb = this.getEntityBoundingBox().grow(1.0D, 0.5D, 1.0D);
            }

            List list = this.world.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);

            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (!entity.isDead) {
                    this.collideWithPlayer(entity);
                }
            }
        }

        this.playShoulderEntityAmbientSound(this.getLeftShoulderEntity());
        this.playShoulderEntityAmbientSound(this.getRightShoulderEntity());
        if (!this.world.isRemote && (this.fallDistance > 0.5F || this.isInWater() || this.isRiding()) || this.capabilities.isFlying) {
            if (!this.world.paperConfig.parrotsHangOnBetter) this.spawnShoulderEntities(); // Paper - Hang on!
        }

    }

    private void playShoulderEntityAmbientSound(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound != null && !nbttagcompound.hasKey("Silent") || !nbttagcompound.getBoolean("Silent")) {
            String s = nbttagcompound.getString("id");

            if (s.equals(EntityList.getKey(EntityParrot.class).toString())) {
                EntityParrot.playAmbientSound(this.world, (Entity) this);
            }
        }

    }

    private void collideWithPlayer(Entity entity) {
        entity.onCollideWithPlayer(this);
    }

    public int getScore() {
        return ((Integer) this.dataManager.get(EntityPlayer.PLAYER_SCORE)).intValue();
    }

    public void setScore(int i) {
        this.dataManager.set(EntityPlayer.PLAYER_SCORE, Integer.valueOf(i));
    }

    public void addScore(int i) {
        int j = this.getScore();

        this.dataManager.set(EntityPlayer.PLAYER_SCORE, Integer.valueOf(j + i));
    }

    public void onDeath(DamageSource damagesource) {
        super.onDeath(damagesource);
        this.setSize(0.2F, 0.2F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.10000000149011612D;
        if ("Notch".equals(this.getName())) {
            this.dropItem(new ItemStack(Items.APPLE, 1), true, false);
        }

        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            this.destroyVanishingCursedItems();
            this.inventory.dropAllItems();
        }

        if (damagesource != null) {
            this.motionX = (double) (-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 0.017453292F) * 0.1F);
            this.motionZ = (double) (-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 0.017453292F) * 0.1F);
        } else {
            this.motionX = 0.0D;
            this.motionZ = 0.0D;
        }

        this.addStat(StatList.DEATHS);
        this.takeStat(StatList.TIME_SINCE_DEATH);
        this.extinguish();
        this.setFlag(0, false);
    }

    protected void destroyVanishingCursedItems() {
        for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);

            if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.inventory.removeStackFromSlot(i);
            }
        }

    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return damagesource == DamageSource.ON_FIRE ? SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE : (damagesource == DamageSource.DROWN ? SoundEvents.ENTITY_PLAYER_HURT_DROWN : SoundEvents.ENTITY_PLAYER_HURT);
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PLAYER_DEATH;
    }

    @Nullable
    public EntityItem dropItem(boolean flag) {
        // Called only when dropped by Q or CTRL-Q
        return this.dropItem(this.inventory.decrStackSize(this.inventory.currentItem, flag && !this.inventory.getCurrentItem().isEmpty() ? this.inventory.getCurrentItem().getCount() : 1), false, true);
    }

    @Nullable
    public EntityItem dropItem(ItemStack itemstack, boolean flag) {
        return this.dropItem(itemstack, false, flag);
    }

    @Nullable
    public EntityItem dropItem(ItemStack itemstack, boolean flag, boolean flag1) {
        if (itemstack.isEmpty()) {
            return null;
        } else {
            double d0 = this.posY - 0.30000001192092896D + (double) this.getEyeHeight();
            EntityItem entityitem = new EntityItem(this.world, this.posX, d0, this.posZ, itemstack);

            entityitem.setPickupDelay(40);
            if (flag1) {
                entityitem.setThrower(this.getName());
            }

            float f;
            float f1;

            if (flag) {
                f = this.rand.nextFloat() * 0.5F;
                f1 = this.rand.nextFloat() * 6.2831855F;
                entityitem.motionX = (double) (-MathHelper.sin(f1) * f);
                entityitem.motionZ = (double) (MathHelper.cos(f1) * f);
                entityitem.motionY = 0.20000000298023224D;
            } else {
                f = 0.3F;
                entityitem.motionX = (double) (-MathHelper.sin(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F) * f);
                entityitem.motionZ = (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F) * f);
                entityitem.motionY = (double) (-MathHelper.sin(this.rotationPitch * 0.017453292F) * f + 0.1F);
                f1 = this.rand.nextFloat() * 6.2831855F;
                f = 0.02F * this.rand.nextFloat();
                entityitem.motionX += Math.cos((double) f1) * (double) f;
                entityitem.motionY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                entityitem.motionZ += Math.sin((double) f1) * (double) f;
            }

            // CraftBukkit start - fire PlayerDropItemEvent
            Player player = (Player) this.getBukkitEntity();
            CraftItem drop = new CraftItem(this.world.getServer(), entityitem);

            PlayerDropItemEvent event = new PlayerDropItemEvent(player, drop);
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                org.bukkit.inventory.ItemStack cur = player.getInventory().getItemInHand();
                if (flag1 && (cur == null || cur.getAmount() == 0)) {
                    // The complete stack was dropped
                    player.getInventory().setItemInHand(drop.getItemStack());
                } else if (flag1 && cur.isSimilar(drop.getItemStack()) && cur.getAmount() < cur.getMaxStackSize() && drop.getItemStack().getAmount() == 1) {
                    // Only one item is dropped
                    cur.setAmount(cur.getAmount() + 1);
                    player.getInventory().setItemInHand(cur);
                } else {
                    // Fallback
                    player.getInventory().addItem(drop.getItemStack());
                }
                return null;
            }
            // CraftBukkit end
            // Paper start - remove player from map on drop
            if (itemstack.getItem() == Items.FILLED_MAP) {
                MapData worldmap = Items.FILLED_MAP.getMapData(itemstack, this.world);
                worldmap.updateSeenPlayers(this, itemstack);
            }
            // Paper stop

            ItemStack itemstack1 = this.dropItemAndGetStack(entityitem);

            if (flag1) {
                if (!itemstack1.isEmpty()) {
                    this.addStat(StatList.getDroppedObjectStats(itemstack1.getItem()), itemstack.getCount());
                }

                this.addStat(StatList.DROP);
            }

            return entityitem;
        }
    }

    protected ItemStack dropItemAndGetStack(EntityItem entityitem) {
        this.world.spawnEntity(entityitem);
        return entityitem.getItem();
    }

    public float getDigSpeed(IBlockState iblockdata) {
        float f = this.inventory.getDestroySpeed(iblockdata);

        if (f > 1.0F) {
            int i = EnchantmentHelper.getEfficiencyModifier(this);
            ItemStack itemstack = this.getHeldItemMainhand();

            if (i > 0 && !itemstack.isEmpty()) {
                f += (float) (i * i + 1);
            }
        }

        if (this.isPotionActive(MobEffects.HASTE)) {
            f *= 1.0F + (float) (this.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
        }

        if (this.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float f1;

            switch (this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
            case 0:
                f1 = 0.3F;
                break;

            case 1:
                f1 = 0.09F;
                break;

            case 2:
                f1 = 0.0027F;
                break;

            case 3:
            default:
                f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (this.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    public boolean canHarvestBlock(IBlockState iblockdata) {
        return this.inventory.canHarvestBlock(iblockdata);
    }

    public static void registerFixesPlayer(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.PLAYER, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                DataFixesManager.processInventory(dataconverter, nbttagcompound, i, "Inventory");
                DataFixesManager.processInventory(dataconverter, nbttagcompound, i, "EnderItems");
                if (nbttagcompound.hasKey("ShoulderEntityLeft", 10)) {
                    nbttagcompound.setTag("ShoulderEntityLeft", dataconverter.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("ShoulderEntityLeft"), i));
                }

                if (nbttagcompound.hasKey("ShoulderEntityRight", 10)) {
                    nbttagcompound.setTag("ShoulderEntityRight", dataconverter.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("ShoulderEntityRight"), i));
                }

                return nbttagcompound;
            }
        });
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setUniqueId(getUUID(this.gameProfile));
        NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory", 10);

        this.inventory.readFromNBT(nbttaglist);
        this.inventory.currentItem = nbttagcompound.getInteger("SelectedItemSlot");
        this.sleeping = nbttagcompound.getBoolean("Sleeping");
        this.sleepTimer = nbttagcompound.getShort("SleepTimer");
        this.experience = nbttagcompound.getFloat("XpP");
        this.experienceLevel = nbttagcompound.getInteger("XpLevel");
        this.experienceTotal = nbttagcompound.getInteger("XpTotal");
        this.xpSeed = nbttagcompound.getInteger("XpSeed");
        if (this.xpSeed == 0) {
            this.xpSeed = this.rand.nextInt();
        }

        this.setScore(nbttagcompound.getInteger("Score"));
        if (this.sleeping) {
            this.bedLocation = new BlockPos(this);
            this.wakeUpPlayer(true, true, false);
        }

        // CraftBukkit start
        this.spawnWorld = nbttagcompound.getString("SpawnWorld");
        if ("".equals(spawnWorld)) {
            this.spawnWorld = this.world.getServer().getWorlds().get(0).getName();
        }
        // CraftBukkit end

        if (nbttagcompound.hasKey("SpawnX", 99) && nbttagcompound.hasKey("SpawnY", 99) && nbttagcompound.hasKey("SpawnZ", 99)) {
            this.spawnPos = new BlockPos(nbttagcompound.getInteger("SpawnX"), nbttagcompound.getInteger("SpawnY"), nbttagcompound.getInteger("SpawnZ"));
            this.spawnForced = nbttagcompound.getBoolean("SpawnForced");
        }

        this.foodStats.readNBT(nbttagcompound);
        this.capabilities.readCapabilitiesFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("EnderItems", 9)) {
            NBTTagList nbttaglist1 = nbttagcompound.getTagList("EnderItems", 10);

            this.enderChest.loadInventoryFromNBT(nbttaglist1);
        }

        if (nbttagcompound.hasKey("ShoulderEntityLeft", 10)) {
            this.setLeftShoulderEntity(nbttagcompound.getCompoundTag("ShoulderEntityLeft"));
        }

        if (nbttagcompound.hasKey("ShoulderEntityRight", 10)) {
            this.setRightShoulderEntity(nbttagcompound.getCompoundTag("ShoulderEntityRight"));
        }

    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("DataVersion", 1343);
        nbttagcompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        nbttagcompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
        nbttagcompound.setBoolean("Sleeping", this.sleeping);
        nbttagcompound.setShort("SleepTimer", (short) this.sleepTimer);
        nbttagcompound.setFloat("XpP", this.experience);
        nbttagcompound.setInteger("XpLevel", this.experienceLevel);
        nbttagcompound.setInteger("XpTotal", this.experienceTotal);
        nbttagcompound.setInteger("XpSeed", this.xpSeed);
        nbttagcompound.setInteger("Score", this.getScore());
        if (this.spawnPos != null) {
            nbttagcompound.setInteger("SpawnX", this.spawnPos.getX());
            nbttagcompound.setInteger("SpawnY", this.spawnPos.getY());
            nbttagcompound.setInteger("SpawnZ", this.spawnPos.getZ());
            nbttagcompound.setBoolean("SpawnForced", this.spawnForced);
        }

        this.foodStats.writeNBT(nbttagcompound);
        this.capabilities.writeCapabilitiesToNBT(nbttagcompound);
        nbttagcompound.setTag("EnderItems", this.enderChest.saveInventoryToNBT());
        if (!this.getLeftShoulderEntity().hasNoTags()) {
            nbttagcompound.setTag("ShoulderEntityLeft", this.getLeftShoulderEntity());
        }

        if (!this.getRightShoulderEntity().hasNoTags()) {
            nbttagcompound.setTag("ShoulderEntityRight", this.getRightShoulderEntity());
        }
        nbttagcompound.setString("SpawnWorld", spawnWorld); // CraftBukkit - fixes bed spawns for multiworld worlds

    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (this.capabilities.disableDamage && !damagesource.canHarmInCreative()) {
            return false;
        } else {
            this.idleTime = 0;
            if (this.getHealth() <= 0.0F) {
                return false;
            } else {
                if (this.isPlayerSleeping() && !this.world.isRemote) {
                    this.wakeUpPlayer(true, true, false);
                }

                // this.releaseShoulderEntities(); // CraftBukkit - moved down
                if (damagesource.isDifficultyScaled()) {
                    if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
                        return false; // CraftBukkit - f = 0.0f -> return false
                    }

                    if (this.world.getDifficulty() == EnumDifficulty.EASY) {
                        f = Math.min(f / 2.0F + 1.0F, f);
                    }

                    if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                        f = f * 3.0F / 2.0F;
                    }
                }

                // CraftBukkit start - Don't filter out 0 damage
                boolean damaged = super.attackEntityFrom(damagesource, f);
                if (damaged) {
                    this.spawnShoulderEntities();
                }
                return damaged;
                // CraftBukkit end
            }
        }
    }

    protected void blockUsingShield(EntityLivingBase entityliving) {
        super.blockUsingShield(entityliving);
        if (entityliving.getHeldItemMainhand().getItem() instanceof ItemAxe) {
            this.disableShield(true);
        }

    }

    public boolean canAttackPlayer(EntityPlayer entityhuman) {
        // CraftBukkit start - Change to check OTHER player's scoreboard team according to API
        // To summarize this method's logic, it's "Can parameter hurt this"
        org.bukkit.scoreboard.Team team;
        if (entityhuman instanceof EntityPlayerMP) {
            EntityPlayerMP thatPlayer = (EntityPlayerMP) entityhuman;
            team = thatPlayer.getBukkitEntity().getScoreboard().getPlayerTeam(thatPlayer.getBukkitEntity());
            if (team == null || team.allowFriendlyFire()) {
                return true;
            }
        } else {
            // This should never be called, but is implemented anyway
            org.bukkit.OfflinePlayer thisPlayer = entityhuman.world.getServer().getOfflinePlayer(entityhuman.getName());
            team = entityhuman.world.getServer().getScoreboardManager().getMainScoreboard().getPlayerTeam(thisPlayer);
            if (team == null || team.allowFriendlyFire()) {
                return true;
            }
        }

        if (this instanceof EntityPlayerMP) {
            return !team.hasPlayer(((EntityPlayerMP) this).getBukkitEntity());
        }
        return !team.hasPlayer(this.world.getServer().getOfflinePlayer(this.getName()));
        // CraftBukkit end
    }

    protected void damageArmor(float f) {
        this.inventory.damageArmor(f);
    }

    protected void damageShield(float f) {
        if (f >= 3.0F && this.activeItemStack.getItem() == Items.SHIELD) {
            int i = 1 + MathHelper.floor(f);

            this.activeItemStack.damageItem(i, this);
            if (this.activeItemStack.isEmpty()) {
                EnumHand enumhand = this.getActiveHand();

                if (enumhand == EnumHand.MAIN_HAND) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }

                this.activeItemStack = ItemStack.EMPTY;
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
        }

    }

    public float getArmorVisibility() {
        int i = 0;
        Iterator iterator = this.inventory.armorInventory.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            if (!itemstack.isEmpty()) {
                ++i;
            }
        }

        return (float) i / (float) this.inventory.armorInventory.size();
    }

    // CraftBukkit start
    protected boolean damageEntity0(DamageSource damagesource, float f) { // void -> boolean
        if (true) {
            return super.damageEntity0(damagesource, f);
        }
        // CraftBukkit end
        if (!this.isEntityInvulnerable(damagesource)) {
            f = this.applyArmorCalculations(damagesource, f);
            f = this.applyPotionDamageCalculations(damagesource, f);
            float f1 = f;

            f = Math.max(f - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f1 - f));
            if (f != 0.0F) {
                this.addExhaustion(damagesource.getHungerDamage());
                float f2 = this.getHealth();

                this.setHealth(this.getHealth() - f);
                this.getCombatTracker().trackDamage(damagesource, f2, f);
                if (f < 3.4028235E37F) {
                    this.addStat(StatList.DAMAGE_TAKEN, Math.round(f * 10.0F));
                }

            }
        }
        return false; // CraftBukkit
    }

    public void openEditSign(TileEntitySign tileentitysign) {}

    public void displayGuiEditCommandCart(CommandBlockBaseLogic commandblocklistenerabstract) {}

    public void displayGuiCommandBlock(TileEntityCommandBlock tileentitycommand) {}

    public void openEditStructure(TileEntityStructure tileentitystructure) {}

    public void displayVillagerTradeGui(IMerchant imerchant) {}

    public void displayGUIChest(IInventory iinventory) {}

    public void openGuiHorseInventory(AbstractHorse entityhorseabstract, IInventory iinventory) {}

    public void displayGui(IInteractionObject itileentitycontainer) {}

    public void openBook(ItemStack itemstack, EnumHand enumhand) {}

    public EnumActionResult interactOn(Entity entity, EnumHand enumhand) {
        if (this.isSpectator()) {
            if (entity instanceof IInventory) {
                this.displayGUIChest((IInventory) entity);
            }

            return EnumActionResult.PASS;
        } else {
            ItemStack itemstack = this.getHeldItem(enumhand);
            ItemStack itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();

            if (entity.processInitialInteract(this, enumhand)) {
                if (this.capabilities.isCreativeMode && itemstack == this.getHeldItem(enumhand) && itemstack.getCount() < itemstack1.getCount()) {
                    itemstack.setCount(itemstack1.getCount());
                }

                return EnumActionResult.SUCCESS;
            } else {
                if (!itemstack.isEmpty() && entity instanceof EntityLivingBase) {
                    if (this.capabilities.isCreativeMode) {
                        itemstack = itemstack1;
                    }

                    if (itemstack.interactWithEntity(this, (EntityLivingBase) entity, enumhand)) {
                        if (itemstack.isEmpty() && !this.capabilities.isCreativeMode) {
                            this.setHeldItem(enumhand, ItemStack.EMPTY);
                        }

                        return EnumActionResult.SUCCESS;
                    }
                }

                return EnumActionResult.PASS;
            }
        }
    }

    public double getYOffset() {
        return -0.35D;
    }

    public void dismountRidingEntity() {
        super.dismountRidingEntity();
        this.rideCooldown = 0;
    }

    // Paper start - send SoundEffect to everyone who can see fromEntity
    private static void sendSoundEffect(EntityPlayer fromEntity, double x, double y, double z, SoundEvent soundEffect, SoundCategory soundCategory, float volume, float pitch) {
        fromEntity.world.sendSoundEffect(fromEntity, x, y, z, soundEffect, soundCategory, volume, pitch); // This will not send the effect to the entity himself
        if (fromEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP) fromEntity).connection.sendPacket(new SPacketSoundEffect(soundEffect, soundCategory, x, y, z, volume, pitch));
        }
    }
    // Paper end

    public void attackTargetEntityWithCurrentItem(Entity entity) {
        if (entity.canBeAttackedWithItem()) {
            if (!entity.hitByEntity(this)) {
                float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                float f1;

                if (entity instanceof EntityLivingBase) {
                    f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entity).getCreatureAttribute());
                } else {
                    f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
                }

                float f2 = this.getCooledAttackStrength(0.5F);

                f *= 0.2F + f2 * f2 * 0.8F;
                f1 *= f2;
                this.resetCooldown();
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    byte b0 = 0;
                    int i = b0 + EnchantmentHelper.getKnockbackModifier((EntityLivingBase) this);

                    if (this.isSprinting() && flag) {
                        sendSoundEffect(this, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F); // Paper - send while respecting visibility
                        ++i;
                        flag1 = true;
                    }

                    boolean flag2 = flag && this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(MobEffects.BLINDNESS) && !this.isRiding() && entity instanceof EntityLivingBase;
                    flag2 = flag2 && !world.paperConfig.disablePlayerCrits; // Paper
                    flag2 = flag2 && !this.isSprinting();
                    if (flag2) {
                        f *= 1.5F;
                    }

                    f += f1;
                    boolean flag3 = false;
                    double d0 = (double) (this.distanceWalkedModified - this.prevDistanceWalkedModified);

                    if (flag && !flag2 && !flag1 && this.onGround && d0 < (double) this.getAIMoveSpeed()) {
                        ItemStack itemstack = this.getHeldItem(EnumHand.MAIN_HAND);

                        if (itemstack.getItem() instanceof ItemSword) {
                            flag3 = true;
                        }
                    }

                    float f3 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(this);

                    if (entity instanceof EntityLivingBase) {
                        f3 = ((EntityLivingBase) entity).getHealth();
                        if (j > 0 && !entity.isBurning()) {
                            // CraftBukkit start - Call a combust event when somebody hits with a fire enchanted item
                            EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 1);
                            org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                            if (!combustEvent.isCancelled()) {
                                flag4 = true;
                                entity.setFire(combustEvent.getDuration());
                            }
                            // CraftBukkit end
                        }
                    }

                    double d1 = entity.motionX;
                    double d2 = entity.motionY;
                    double d3 = entity.motionZ;
                    boolean flag5 = entity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);

                    if (flag5) {
                        if (i > 0) {
                            if (entity instanceof EntityLivingBase) {
                                ((EntityLivingBase) entity).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                            } else {
                                entity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * 0.017453292F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * (float) i * 0.5F));
                            }

                            this.motionX *= 0.6D;
                            this.motionZ *= 0.6D;
                            // Paper start - Configuration option to disable automatic sprint interruption
                            if (!world.paperConfig.disableSprintInterruptionOnAttack) {
                                this.setSprinting(false);
                            }
                            // Paper end
                        }

                        if (flag3) {
                            float f4 = 1.0F + EnchantmentHelper.getSweepingDamageRatio((EntityLivingBase) this) * f;
                            List list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D));
                            Iterator iterator = list.iterator();

                            while (iterator.hasNext()) {
                                EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                                if (entityliving != this && entityliving != entity && !this.isOnSameTeam(entityliving) && this.getDistanceSq(entityliving) < 9.0D) {
                                    // CraftBukkit start - Only apply knockback if the damage hits
                                    if (entityliving.attackEntityFrom(DamageSource.causePlayerDamage(this).sweep(), f4)) {
                                    entityliving.knockBack(this, 0.4F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                                    }
                                    // CraftBukkit end
                                }
                            }

                            sendSoundEffect(this, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F); // Paper - send while respecting visibility
                            this.spawnSweepParticles();
                        }

                        if (entity instanceof EntityPlayerMP && entity.velocityChanged) {
                            // CraftBukkit start - Add Velocity Event
                            boolean cancelled = false;
                            Player player = (Player) entity.getBukkitEntity();
                            org.bukkit.util.Vector velocity = new Vector( d1, d2, d3 );

                            PlayerVelocityEvent event = new PlayerVelocityEvent(player, velocity.clone());
                            world.getServer().getPluginManager().callEvent(event);

                            if (event.isCancelled()) {
                                cancelled = true;
                            } else if (!velocity.equals(event.getVelocity())) {
                                player.setVelocity(event.getVelocity());
                            }

                            if (!cancelled) {
                            ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityVelocity(entity));
                            entity.velocityChanged = false;
                            entity.motionX = d1;
                            entity.motionY = d2;
                            entity.motionZ = d3;
                            }
                            // CraftBukkit end
                        }

                        if (flag2) {
                            sendSoundEffect(this, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F); // Paper - send while respecting visibility
                            this.onCriticalHit(entity);
                        }

                        if (!flag2 && !flag3) {
                            if (flag) {
                                sendSoundEffect(this, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F); // Paper - send while respecting visibility
                            } else {
                                sendSoundEffect(this, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F); // Paper - send while respecting visibility
                            }
                        }

                        if (f1 > 0.0F) {
                            this.onEnchantmentCritical(entity);
                        }

                        this.setLastAttackedEntity(entity);
                        if (entity instanceof EntityLivingBase) {
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entity, (Entity) this);
                        }

                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this, entity);
                        ItemStack itemstack1 = this.getHeldItemMainhand();
                        Object object = entity;

                        if (entity instanceof MultiPartEntityPart) {
                            IEntityMultiPart icomplex = ((MultiPartEntityPart) entity).parent;

                            if (icomplex instanceof EntityLivingBase) {
                                object = (EntityLivingBase) icomplex;
                            }
                        }

                        if (!itemstack1.isEmpty() && object instanceof EntityLivingBase) {
                            itemstack1.hitEntity((EntityLivingBase) object, this);
                            if (itemstack1.isEmpty()) {
                                this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (entity instanceof EntityLivingBase) {
                            float f5 = f3 - ((EntityLivingBase) entity).getHealth();

                            this.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                // CraftBukkit start - Call a combust event when somebody hits with a fire enchanted item
                                EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), j * 4);
                                org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                                if (!combustEvent.isCancelled()) {
                                    entity.setFire(combustEvent.getDuration());
                                }
                                // CraftBukkit end
                            }

                            if (this.world instanceof WorldServer && f5 > 2.0F) {
                                int k = (int) ((double) f5 * 0.5D);

                                ((WorldServer) this.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, entity.posX, entity.posY + (double) (entity.height * 0.5F), entity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D, new int[0]);
                            }
                        }

                        this.addExhaustion(world.spigotConfig.combatExhaustion); // Spigot - Change to use configurable value
                    } else {
                        sendSoundEffect(this, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F); // Paper - send while respecting visibility
                        if (flag4) {
                            entity.extinguish();
                        }
                        // CraftBukkit start - resync on cancelled event
                        if (this instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) this).getBukkitEntity().updateInventory();
                        }
                        // CraftBukkit end
                    }
                }

            }
        }
    }

    public void disableShield(boolean flag) {
        float f = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

        if (flag) {
            f += 0.75F;
        }

        if (this.rand.nextFloat() < f) {
            this.getCooldownTracker().setCooldown(Items.SHIELD, 100);
            this.resetActiveHand();
            this.world.setEntityState(this, (byte) 30);
        }

    }

    public void onCriticalHit(Entity entity) {}

    public void onEnchantmentCritical(Entity entity) {}

    public void spawnSweepParticles() {
        double d0 = (double) (-MathHelper.sin(this.rotationYaw * 0.017453292F));
        double d1 = (double) MathHelper.cos(this.rotationYaw * 0.017453292F);

        if (this.world instanceof WorldServer) {
            ((WorldServer) this.world).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.posX + d0, this.posY + (double) this.height * 0.5D, this.posZ + d1, 0, d0, 0.0D, d1, 0.0D, new int[0]);
        }

    }

    public void setDead() {
        super.setDead();
        this.inventoryContainer.onContainerClosed(this);
        if (this.openContainer != null) {
            this.openContainer.onContainerClosed(this);
        }

    }

    public boolean isEntityInsideOpaqueBlock() {
        return !this.sleeping && super.isEntityInsideOpaqueBlock();
    }

    public boolean isUser() {
        return false;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public EntityPlayer.SleepResult trySleep(BlockPos blockposition) {
        EnumFacing enumdirection = (EnumFacing) this.world.getBlockState(blockposition).getValue(BlockHorizontal.FACING);

        if (!this.world.isRemote) {
            if (this.isPlayerSleeping() || !this.isEntityAlive()) {
                return EntityPlayer.SleepResult.OTHER_PROBLEM;
            }

            if (!this.world.provider.isSurfaceWorld()) {
                return EntityPlayer.SleepResult.NOT_POSSIBLE_HERE;
            }

            if (this.world.isDaytime()) {
                return EntityPlayer.SleepResult.NOT_POSSIBLE_NOW;
            }

            if (!this.bedInRange(blockposition, enumdirection)) {
                return EntityPlayer.SleepResult.TOO_FAR_AWAY;
            }

            double d0 = 8.0D;
            double d1 = 5.0D;
            List list = this.world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB((double) blockposition.getX() - 8.0D, (double) blockposition.getY() - 5.0D, (double) blockposition.getZ() - 8.0D, (double) blockposition.getX() + 8.0D, (double) blockposition.getY() + 5.0D, (double) blockposition.getZ() + 8.0D), (Predicate) (new EntityHuman.c(this, null)));

            if (!list.isEmpty()) {
                return EntityPlayer.SleepResult.NOT_SAFE;
            }
        }

        if (this.isRiding()) {
            this.dismountRidingEntity();
        }

        // CraftBukkit start - fire PlayerBedEnterEvent
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();
            org.bukkit.block.Block bed = this.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, bed);
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return SleepResult.OTHER_PROBLEM;
            }
        }
        // CraftBukkit end

        this.spawnShoulderEntities();
        this.setSize(0.2F, 0.2F);
        if (this.world.isBlockLoaded(blockposition)) {
            float f = 0.5F + (float) enumdirection.getFrontOffsetX() * 0.4F;
            float f1 = 0.5F + (float) enumdirection.getFrontOffsetZ() * 0.4F;

            this.setRenderOffsetForSleep(enumdirection);
            this.setPosition((double) ((float) blockposition.getX() + f), (double) ((float) blockposition.getY() + 0.6875F), (double) ((float) blockposition.getZ() + f1));
        } else {
            this.setPosition((double) ((float) blockposition.getX() + 0.5F), (double) ((float) blockposition.getY() + 0.6875F), (double) ((float) blockposition.getZ() + 0.5F));
        }

        this.sleeping = true;
        this.sleepTimer = 0;
        this.bedLocation = blockposition;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        if (!this.world.isRemote) {
            this.world.updateAllPlayersSleepingFlag();
        }

        return EntityPlayer.SleepResult.OK;
    }

    private boolean bedInRange(BlockPos blockposition, EnumFacing enumdirection) {
        if (Math.abs(this.posX - (double) blockposition.getX()) <= 3.0D && Math.abs(this.posY - (double) blockposition.getY()) <= 2.0D && Math.abs(this.posZ - (double) blockposition.getZ()) <= 3.0D) {
            return true;
        } else {
            BlockPos blockposition1 = blockposition.offset(enumdirection.getOpposite());

            return Math.abs(this.posX - (double) blockposition1.getX()) <= 3.0D && Math.abs(this.posY - (double) blockposition1.getY()) <= 2.0D && Math.abs(this.posZ - (double) blockposition1.getZ()) <= 3.0D;
        }
    }

    private void setRenderOffsetForSleep(EnumFacing enumdirection) {
        this.renderOffsetX = -1.8F * (float) enumdirection.getFrontOffsetX();
        this.renderOffsetZ = -1.8F * (float) enumdirection.getFrontOffsetZ();
    }

    public void wakeUpPlayer(boolean flag, boolean flag1, boolean flag2) {
        this.setSize(0.6F, 1.8F);
        IBlockState iblockdata = this.world.getBlockState(this.bedLocation);

        if (this.bedLocation != null && iblockdata.getBlock() == Blocks.BED) {
            this.world.setBlockState(this.bedLocation, iblockdata.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
            BlockPos blockposition = BlockBed.getSafeExitLocation(this.world, this.bedLocation, 0);

            if (blockposition == null) {
                blockposition = this.bedLocation.up();
            }

            this.setPosition((double) ((float) blockposition.getX() + 0.5F), (double) ((float) blockposition.getY() + 0.1F), (double) ((float) blockposition.getZ() + 0.5F));
        }

        this.sleeping = false;
        if (!this.world.isRemote && flag1) {
            this.world.updateAllPlayersSleepingFlag();
        }

        // CraftBukkit start - fire PlayerBedLeaveEvent
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();

            org.bukkit.block.Block bed;
            BlockPos blockposition = this.bedLocation;
            if (blockposition != null) {
                bed = this.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            } else {
                bed = this.world.getWorld().getBlockAt(player.getLocation());
            }

            PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, bed);
            this.world.getServer().getPluginManager().callEvent(event);
        }
        // CraftBukkit end

        this.sleepTimer = flag ? 0 : 100;
        if (flag2) {
            this.setSpawnPoint(this.bedLocation, false);
        }

    }

    private boolean isInBed() {
        return this.world.getBlockState(this.bedLocation).getBlock() == Blocks.BED;
    }

    @Nullable
    public static BlockPos getBedSpawnLocation(World world, BlockPos blockposition, boolean flag) {
        Block block = world.getBlockState(blockposition).getBlock();

        if (block != Blocks.BED) {
            if (!flag) {
                return null;
            } else {
                boolean flag1 = block.canSpawnInBlock();
                boolean flag2 = world.getBlockState(blockposition.up()).getBlock().canSpawnInBlock();

                return flag1 && flag2 ? blockposition : null;
            }
        } else {
            return BlockBed.getSafeExitLocation(world, blockposition, 0);
        }
    }

    public boolean isPlayerSleeping() {
        return this.sleeping;
    }

    public boolean isPlayerFullyAsleep() {
        return this.sleeping && this.sleepTimer >= 100;
    }

    public void sendStatusMessage(ITextComponent ichatbasecomponent, boolean flag) {}

    public BlockPos getBedLocation() {
        return this.spawnPos;
    }

    public boolean isSpawnForced() {
        return this.spawnForced;
    }

    public void setSpawnPoint(BlockPos blockposition, boolean flag) {
        if (blockposition != null) {
            this.spawnPos = blockposition;
            this.spawnForced = flag;
            this.spawnWorld = this.world.worldInfo.getWorldName(); // CraftBukkit
        } else {
            this.spawnPos = null;
            this.spawnForced = false;
            this.spawnWorld = ""; // CraftBukkit
        }

    }

    public void addStat(StatBase statistic) {
        this.addStat(statistic, 1);
    }

    public void addStat(StatBase statistic, int i) {}

    public void takeStat(StatBase statistic) {}

    public void unlockRecipes(List<IRecipe> list) {}

    public void unlockRecipes(ResourceLocation[] aminecraftkey) {}

    public void resetRecipes(List<IRecipe> list) {}

    public void jump() { this.jump(); } // Paper - OBFHELPER
    public void jump() {
        super.jump();
        this.addStat(StatList.JUMP);
        if (this.isSprinting()) {
            this.addExhaustion(world.spigotConfig.jumpSprintExhaustion); // Spigot - Change to use configurable value
        } else {
            this.addExhaustion(world.spigotConfig.jumpWalkExhaustion); // Spigot - Change to use configurable value
        }

    }

    public void travel(float f, float f1, float f2) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;

        if (this.capabilities.isFlying && !this.isRiding()) {
            double d3 = this.motionY;
            float f3 = this.jumpMovementFactor;

            this.jumpMovementFactor = this.capabilities.getFlySpeed() * (float) (this.isSprinting() ? 2 : 1);
            super.travel(f, f1, f2);
            this.motionY = d3 * 0.6D;
            this.jumpMovementFactor = f3;
            this.fallDistance = 0.0F;
            // CraftBukkit start
            if (getFlag(7) && !org.bukkit.craftbukkit.event.CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) {
                this.setFlag(7, false);
            }
            // CraftBukkit end
        } else {
            super.travel(f, f1, f2);
        }

        this.addMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
    }

    public float getAIMoveSpeed() {
        return (float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
    }

    public void addMovementStat(double d0, double d1, double d2) {
        if (!this.isRiding()) {
            int i;

            if (this.isInsideOfMaterial(Material.WATER)) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.addStat(StatList.DIVE_ONE_CM, i);
                    this.addExhaustion(world.spigotConfig.swimMultiplier * (float) i * 0.01F); // Spigot
                }
            } else if (this.isInWater()) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.addStat(StatList.SWIM_ONE_CM, i);
                    this.addExhaustion(world.spigotConfig.swimMultiplier * (float) i * 0.01F); // Spigot
                }
            } else if (this.isOnLadder()) {
                if (d1 > 0.0D) {
                    this.addStat(StatList.CLIMB_ONE_CM, (int) Math.round(d1 * 100.0D));
                }
            } else if (this.onGround) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    if (this.isSprinting()) {
                        this.addStat(StatList.SPRINT_ONE_CM, i);
                        this.addExhaustion(world.spigotConfig.sprintMultiplier * (float) i * 0.01F); // Spigot
                    } else if (this.isSneaking()) {
                        this.addStat(StatList.CROUCH_ONE_CM, i);
                        this.addExhaustion(world.spigotConfig.otherMultiplier * (float) i * 0.01F); // Spigot
                    } else {
                        this.addStat(StatList.WALK_ONE_CM, i);
                        this.addExhaustion(world.spigotConfig.otherMultiplier * (float) i * 0.01F); // Spigot
                    }
                }
            } else if (this.isElytraFlying()) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                this.addStat(StatList.AVIATE_ONE_CM, i);
            } else {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.addStat(StatList.FLY_ONE_CM, i);
                }
            }

        }
    }

    private void addMountedMovementStat(double d0, double d1, double d2) {
        if (this.isRiding()) {
            int i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);

            if (i > 0) {
                if (this.getRidingEntity() instanceof EntityMinecart) {
                    this.addStat(StatList.MINECART_ONE_CM, i);
                } else if (this.getRidingEntity() instanceof EntityBoat) {
                    this.addStat(StatList.BOAT_ONE_CM, i);
                } else if (this.getRidingEntity() instanceof EntityPig) {
                    this.addStat(StatList.PIG_ONE_CM, i);
                } else if (this.getRidingEntity() instanceof AbstractHorse) {
                    this.addStat(StatList.HORSE_ONE_CM, i);
                }
            }
        }

    }

    public void fall(float f, float f1) {
        if (!this.capabilities.allowFlying) {
            if (f >= 2.0F) {
                this.addStat(StatList.FALL_ONE_CM, (int) Math.round((double) f * 100.0D));
            }

            super.fall(f, f1);
        }
    }

    protected void doWaterSplashEffect() {
        if (!this.isSpectator()) {
            super.doWaterSplashEffect();
        }

    }

    protected SoundEvent getFallSound(int i) {
        return i > 4 ? SoundEvents.ENTITY_PLAYER_BIG_FALL : SoundEvents.ENTITY_PLAYER_SMALL_FALL;
    }

    public void onKillEntity(EntityLivingBase entityliving) {
        EntityList.EntityEggInfo entitytypes_monsteregginfo = (EntityList.EntityEggInfo) EntityList.ENTITY_EGGS.get(EntityList.getKey((Entity) entityliving));

        if (entitytypes_monsteregginfo != null) {
            this.addStat(entitytypes_monsteregginfo.killEntityStat);
        }

    }

    public void setInWeb() {
        if (!this.capabilities.isFlying) {
            super.setInWeb();
        }

    }

    public void addExperience(int i) {
        this.addScore(i);
        int j = Integer.MAX_VALUE - this.experienceTotal;

        if (i > j) {
            i = j;
        }

        this.experience += (float) i / (float) this.xpBarCap();

        for (this.experienceTotal += i; this.experience >= 1.0F; this.experience /= (float) this.xpBarCap()) {
            this.experience = (this.experience - 1.0F) * (float) this.xpBarCap();
            this.addExperienceLevel(1);
        }

    }

    public int getXPSeed() {
        return this.xpSeed;
    }

    public void onEnchant(ItemStack itemstack, int i) {
        this.experienceLevel -= i;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0F;
            this.experienceTotal = 0;
        }

        this.xpSeed = this.rand.nextInt();
    }

    public void addExperienceLevel(int i) {
        this.experienceLevel += i;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0F;
            this.experienceTotal = 0;
        }

        if (i > 0 && this.experienceLevel % 5 == 0 && (float) this.lastXPSound < (float) this.ticksExisted - 100.0F) {
            float f = this.experienceLevel > 30 ? 1.0F : (float) this.experienceLevel / 30.0F;

            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, this.getSoundCategory(), f * 0.75F, 1.0F);
            this.lastXPSound = this.ticksExisted;
        }

    }

    public int xpBarCap() {
        return this.experienceLevel >= 30 ? 112 + (this.experienceLevel - 30) * 9 : (this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : 7 + this.experienceLevel * 2);
    }

    public void addExhaustion(float f) {
        if (!this.capabilities.disableDamage) {
            if (!this.world.isRemote) {
                this.foodStats.addExhaustion(f);
            }

        }
    }

    public FoodStats getFoodStats() {
        return this.foodStats;
    }

    public boolean canEat(boolean flag) {
        return (flag || this.foodStats.needFood()) && !this.capabilities.disableDamage;
    }

    public boolean shouldHeal() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public boolean isAllowEdit() {
        return this.capabilities.allowEdit;
    }

    public boolean canPlayerEdit(BlockPos blockposition, EnumFacing enumdirection, ItemStack itemstack) {
        if (this.capabilities.allowEdit) {
            return true;
        } else if (itemstack.isEmpty()) {
            return false;
        } else {
            BlockPos blockposition1 = blockposition.offset(enumdirection.getOpposite());
            Block block = this.world.getBlockState(blockposition1).getBlock();

            return itemstack.canPlaceOn(block) || itemstack.canEditBlocks();
        }
    }

    protected int getExperiencePoints(EntityPlayer entityhuman) {
        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            int i = this.experienceLevel * 7;

            return i > 100 ? 100 : i;
        } else {
            return 0;
        }
    }

    protected boolean isPlayer() {
        return true;
    }

    protected boolean canTriggerWalking() {
        return !this.capabilities.isFlying;
    }

    public void sendPlayerAbilities() {}

    public void setGameType(GameType enumgamemode) {}

    public String getName() {
        return this.gameProfile.getName();
    }

    public InventoryEnderChest getInventoryEnderChest() {
        return this.enderChest;
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot enumitemslot) {
        return enumitemslot == EntityEquipmentSlot.MAINHAND ? this.inventory.getCurrentItem() : (enumitemslot == EntityEquipmentSlot.OFFHAND ? (ItemStack) this.inventory.offHandInventory.get(0) : (enumitemslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR ? (ItemStack) this.inventory.armorInventory.get(enumitemslot.getIndex()) : ItemStack.EMPTY));
    }

    public void setItemStackToSlot(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        if (enumitemslot == EntityEquipmentSlot.MAINHAND) {
            this.playEquipSound(itemstack);
            this.inventory.mainInventory.set(this.inventory.currentItem, itemstack);
        } else if (enumitemslot == EntityEquipmentSlot.OFFHAND) {
            this.playEquipSound(itemstack);
            this.inventory.offHandInventory.set(0, itemstack);
        } else if (enumitemslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
            this.playEquipSound(itemstack);
            this.inventory.armorInventory.set(enumitemslot.getIndex(), itemstack);
        }

    }

    public boolean addItemStackToInventory(ItemStack itemstack) {
        this.playEquipSound(itemstack);
        return this.inventory.addItemStackToInventory(itemstack);
    }

    public Iterable<ItemStack> getHeldEquipment() {
        return Lists.newArrayList(new ItemStack[] { this.getHeldItemMainhand(), this.getHeldItemOffhand()});
    }

    public Iterable<ItemStack> getArmorInventoryList() {
        return this.inventory.armorInventory;
    }

    public boolean addShoulderEntity(NBTTagCompound nbttagcompound) {
        if (!this.isRiding() && this.onGround && !this.isInWater()) {
            if (this.getLeftShoulderEntity().hasNoTags()) {
                this.setLeftShoulderEntity(nbttagcompound);
                return true;
            } else if (this.getRightShoulderEntity().hasNoTags()) {
                this.setRightShoulderEntity(nbttagcompound);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected void spawnShoulderEntities() {
        // CraftBukkit start
        if (this.spawnEntityFromShoulder(this.getLeftShoulderEntity())) {
            this.setLeftShoulderEntity(new NBTTagCompound());
        }
        if (this.spawnEntityFromShoulder(this.getRightShoulderEntity())) {
            this.setRightShoulderEntity(new NBTTagCompound());
        }
        // CraftBukkit end
    }
    // Paper start
    public Entity releaseLeftShoulderEntity() {
        Entity entity = this.spawnEntityFromShoulder0(this.getLeftShoulderEntity());
        if (entity != null) {
            this.setLeftShoulderEntity(new NBTTagCompound());
        }
        return entity;
    }

    public Entity releaseRightShoulderEntity() {
        Entity entity = this.spawnEntityFromShoulder0(this.getRightShoulderEntity());
        if (entity != null) {
            this.setRightShoulderEntity(new NBTTagCompound());
        }
        return entity;
    }

    // Paper - incase any plugins used NMS to call this... old method signature to avoid other diff
    private boolean spawnEntityFromShoulder(@Nullable NBTTagCompound nbttagcompound) {
        return spawnEntityFromShoulder0(nbttagcompound) != null;
    }
    // Paper - Moved to new method that now returns entity, and properly null checks
    private Entity spawnEntityFromShoulder0(@Nullable NBTTagCompound nbttagcompound) { // CraftBukkit void->boolean - Paper - return Entity
        if (!this.world.isRemote && nbttagcompound != null && !nbttagcompound.hasNoTags()) { // Paper - null check
            Entity entity = EntityList.createEntityFromNBT(nbttagcompound, this.world);
            if (entity == null) { // Paper - null check
                return null;
            }

            if (entity instanceof EntityTameable) {
                ((EntityTameable) entity).setOwnerId(this.entityUniqueID);
            }

            entity.setPosition(this.posX, this.posY + 0.699999988079071D, this.posZ);
            if (this.world.addEntity(entity, CreatureSpawnEvent.SpawnReason.SHOULDER_ENTITY)) { // CraftBukkit
                return entity;
            }
        }

        return null;
    }
    // Paper end

    public abstract boolean isSpectator();

    public abstract boolean isCreative();

    public boolean isPushedByWater() {
        return !this.capabilities.isFlying;
    }

    public Scoreboard getWorldScoreboard() {
        return this.world.getScoreboard();
    }

    public Team getTeam() {
        return this.getWorldScoreboard().getPlayersTeam(this.getName());
    }

    public ITextComponent getDisplayName() {
        TextComponentString chatcomponenttext = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));

        chatcomponenttext.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
        chatcomponenttext.getStyle().setHoverEvent(this.getHoverEvent());
        chatcomponenttext.getStyle().setInsertion(this.getName());
        return chatcomponenttext;
    }

    public float getEyeHeight() {
        float f = 1.62F;

        if (this.isPlayerSleeping()) {
            f = 0.2F;
        } else if (!this.isSneaking() && this.height != 1.65F) {
            if (this.isElytraFlying() || this.height == 0.6F) {
                f = 0.4F;
            }
        } else {
            f -= 0.08F;
        }

        return f;
    }

    public void setAbsorptionAmount(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.getDataManager().set(EntityPlayer.ABSORPTION, Float.valueOf(f));
    }

    public float getAbsorptionAmount() {
        return ((Float) this.getDataManager().get(EntityPlayer.ABSORPTION)).floatValue();
    }

    public static UUID getUUID(GameProfile gameprofile) {
        UUID uuid = gameprofile.getId();

        if (uuid == null) {
            uuid = getOfflineUUID(gameprofile.getName());
        }

        return uuid;
    }

    public static UUID getOfflineUUID(String s) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + s).getBytes(StandardCharsets.UTF_8));
    }

    public boolean canOpen(LockCode chestlock) {
        if (chestlock.isEmpty()) {
            return true;
        } else {
            ItemStack itemstack = this.getHeldItemMainhand();

            return !itemstack.isEmpty() && itemstack.hasDisplayName() ? itemstack.getDisplayName().equals(chestlock.getLock()) : false;
        }
    }

    public boolean sendCommandFeedback() {
        return this.getServer().worlds[0].getGameRules().getBoolean("sendCommandFeedback");
    }

    public boolean replaceItemInInventory(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.inventory.mainInventory.size()) {
            this.inventory.setInventorySlotContents(i, itemstack);
            return true;
        } else {
            EntityEquipmentSlot enumitemslot;

            if (i == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
                enumitemslot = EntityEquipmentSlot.HEAD;
            } else if (i == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
                enumitemslot = EntityEquipmentSlot.CHEST;
            } else if (i == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
                enumitemslot = EntityEquipmentSlot.LEGS;
            } else if (i == 100 + EntityEquipmentSlot.FEET.getIndex()) {
                enumitemslot = EntityEquipmentSlot.FEET;
            } else {
                enumitemslot = null;
            }

            if (i == 98) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack);
                return true;
            } else if (i == 99) {
                this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemstack);
                return true;
            } else if (enumitemslot == null) {
                int j = i - 200;

                if (j >= 0 && j < this.enderChest.getSizeInventory()) {
                    this.enderChest.setInventorySlotContents(j, itemstack);
                    return true;
                } else {
                    return false;
                }
            } else {
                if (!itemstack.isEmpty()) {
                    if (!(itemstack.getItem() instanceof ItemArmor) && !(itemstack.getItem() instanceof ItemElytra)) {
                        if (enumitemslot != EntityEquipmentSlot.HEAD) {
                            return false;
                        }
                    } else if (EntityLiving.getSlotForItemStack(itemstack) != enumitemslot) {
                        return false;
                    }
                }

                this.inventory.setInventorySlotContents(enumitemslot.getIndex() + this.inventory.mainInventory.size(), itemstack);
                return true;
            }
        }
    }

    public EnumHandSide getPrimaryHand() {
        return ((Byte) this.dataManager.get(EntityPlayer.MAIN_HAND)).byteValue() == 0 ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

    public void setPrimaryHand(EnumHandSide enummainhand) {
        this.dataManager.set(EntityPlayer.MAIN_HAND, Byte.valueOf((byte) (enummainhand == EnumHandSide.LEFT ? 0 : 1)));
    }

    public NBTTagCompound getLeftShoulderEntity() {
        return (NBTTagCompound) this.dataManager.get(EntityPlayer.LEFT_SHOULDER_ENTITY);
    }

    public void setLeftShoulderEntity(NBTTagCompound nbttagcompound) {
        this.dataManager.set(EntityPlayer.LEFT_SHOULDER_ENTITY, nbttagcompound);
    }

    public NBTTagCompound getRightShoulderEntity() {
        return (NBTTagCompound) this.dataManager.get(EntityPlayer.RIGHT_SHOULDER_ENTITY);
    }

    public void setRightShoulderEntity(NBTTagCompound nbttagcompound) {
        this.dataManager.set(EntityPlayer.RIGHT_SHOULDER_ENTITY, nbttagcompound);
    }

    public float getCooldownPeriod() {
        return (float) (1.0D / this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0D);
    }

    public float getCooledAttackStrength(float f) {
        return MathHelper.clamp(((float) this.ticksSinceLastSwing + f) / this.getCooldownPeriod(), 0.0F, 1.0F);
    }

    public void resetCooldown() {
        this.ticksSinceLastSwing = 0;
    }

    public CooldownTracker getCooldownTracker() {
        return this.cooldownTracker;
    }

    public void applyEntityCollision(Entity entity) {
        if (!this.isPlayerSleeping()) {
            super.applyEntityCollision(entity);
        }

    }

    public float getLuck() {
        return (float) this.getEntityAttribute(SharedMonsterAttributes.LUCK).getAttributeValue();
    }

    public boolean canUseCommandBlock() {
        return this.capabilities.isCreativeMode && this.canUseCommand(2, "");
    }

    static class c implements Predicate<EntityMob> {

        private final EntityPlayer a;

        private c(EntityPlayer entityhuman) {
            this.a = entityhuman;
        }

        public boolean a(@Nullable EntityMob entitymonster) {
            return entitymonster.isPreventingPlayerRest(this.a);
        }

        public boolean apply(@Nullable EntityMob object) { // CraftBukkit - decompile error
            return this.a((EntityMob) object);
        }

        c(EntityPlayer entityhuman, Object object) {
            this(entityhuman);
        }
    }

    public static enum SleepResult {

        OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM, NOT_SAFE;

        private SleepResult() {}
    }

    public static enum EnumChatVisibility {

        FULL(0, "options.chat.visibility.full"), SYSTEM(1, "options.chat.visibility.system"), HIDDEN(2, "options.chat.visibility.hidden");

        private static final EntityPlayer.EnumChatVisibility[] ID_LOOKUP = new EntityPlayer.EnumChatVisibility[values().length];
        private final int chatVisibility;
        private final String resourceKey;

        private EnumChatVisibility(int i, String s) {
            this.chatVisibility = i;
            this.resourceKey = s;
        }

        static {
            EntityPlayer.EnumChatVisibility[] aentityhuman_enumchatvisibility = values();
            int i = aentityhuman_enumchatvisibility.length;

            for (int j = 0; j < i; ++j) {
                EntityPlayer.EnumChatVisibility entityhuman_enumchatvisibility = aentityhuman_enumchatvisibility[j];

                EntityPlayer.EnumChatVisibility.ID_LOOKUP[entityhuman_enumchatvisibility.chatVisibility] = entityhuman_enumchatvisibility;
            }

        }
    }
}
