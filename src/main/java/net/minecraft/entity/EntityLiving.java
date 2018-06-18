package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.EntityUnleashEvent.UnleashReason;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.EntityUnleashEvent.UnleashReason;
// CraftBukkit end

public abstract class EntityLiving extends EntityLivingBase {

    private static final DataParameter<Byte> AI_FLAGS = EntityDataManager.createKey(EntityLiving.class, DataSerializers.BYTE);
    public int livingSoundTime;
    protected int experienceValue;
    private final EntityLookHelper lookHelper;
    protected EntityMoveHelper moveHelper;
    protected EntityJumpHelper jumpHelper;
    private final EntityBodyHelper bodyHelper;
    protected PathNavigate navigator;
    public EntityAITasks tasks;
    public EntityAITasks targetTasks;
    private EntityLivingBase attackTarget;
    private final EntitySenses senses;
    private final NonNullList<ItemStack> inventoryHands;
    public float[] inventoryHandsDropChances;
    private final NonNullList<ItemStack> inventoryArmor;
    public float[] inventoryArmorDropChances;
    // public boolean canPickUpLoot; // CraftBukkit - moved up to EntityLiving
    public boolean persistenceRequired;
    private final Map<PathNodeType, Float> mapPathPriority;
    private ResourceLocation deathLootTable;
    private long deathLootTableSeed;
    private boolean isLeashed;
    private Entity leashHolder;
    private NBTTagCompound leashNBTTag;
    @Nullable public EntityAISwimming goalFloat; // Paper

    public EntityLiving(World world) {
        super(world);
        this.inventoryHands = NonNullList.withSize(2, ItemStack.EMPTY);
        this.inventoryHandsDropChances = new float[2];
        this.inventoryArmor = NonNullList.withSize(4, ItemStack.EMPTY);
        this.inventoryArmorDropChances = new float[4];
        this.mapPathPriority = Maps.newEnumMap(PathNodeType.class);
        this.tasks = new EntityAITasks(world != null && world.profiler != null ? world.profiler : null);
        this.targetTasks = new EntityAITasks(world != null && world.profiler != null ? world.profiler : null);
        this.lookHelper = new EntityLookHelper(this);
        this.moveHelper = new EntityMoveHelper(this);
        this.jumpHelper = new EntityJumpHelper(this);
        this.bodyHelper = this.createBodyHelper();
        this.navigator = this.createNavigator(world);
        this.senses = new EntitySenses(this);
        Arrays.fill(this.inventoryArmorDropChances, 0.085F);
        Arrays.fill(this.inventoryHandsDropChances, 0.085F);
        if (world != null && !world.isRemote) {
            this.initEntityAI();
        }

        // CraftBukkit start - default persistance to type's persistance value
        this.persistenceRequired = !canDespawn();
        // CraftBukkit end
    }

    protected void initEntityAI() {}

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
    }

    protected PathNavigate createNavigator(World world) {
        return new PathNavigateGround(this, world);
    }

    public float getPathPriority(PathNodeType pathtype) {
        Float ofloat = (Float) this.mapPathPriority.get(pathtype);

        return ofloat == null ? pathtype.getPriority() : ofloat.floatValue();
    }

    public void setPathPriority(PathNodeType pathtype, float f) {
        this.mapPathPriority.put(pathtype, Float.valueOf(f));
    }

    protected EntityBodyHelper createBodyHelper() {
        return new EntityBodyHelper(this);
    }

    public EntityLookHelper getLookHelper() {
        return this.lookHelper;
    }

    public EntityMoveHelper getMoveHelper() {
        return this.moveHelper;
    }

    public EntityJumpHelper getJumpHelper() {
        return this.jumpHelper;
    }

    public PathNavigate getNavigator() {
        return this.navigator;
    }

    public EntitySenses getEntitySenses() {
        return this.senses;
    }

    @Nullable
    public EntityLivingBase getAttackTarget() {
        return this.attackTarget;
    }

    public void setAttackTarget(@Nullable EntityLivingBase entityliving) {
        // CraftBukkit start - fire event
        setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    public boolean setGoalTarget(EntityLivingBase entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        if (getAttackTarget() == entityliving) return false;
        if (fireEvent) {
            if (reason == EntityTargetEvent.TargetReason.UNKNOWN && getAttackTarget() != null && entityliving == null) {
                reason = getAttackTarget().isEntityAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
            }
            if (reason == EntityTargetEvent.TargetReason.UNKNOWN) {
                world.getServer().getLogger().log(java.util.logging.Level.WARNING, "Unknown target reason, please report on the issue tracker", new Exception());
            }
            CraftLivingEntity ctarget = null;
            if (entityliving != null) {
                ctarget = (CraftLivingEntity) entityliving.getBukkitEntity();
            }
            EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(this.getBukkitEntity(), ctarget, reason);
            world.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }

            if (event.getTarget() != null) {
                entityliving = ((CraftLivingEntity) event.getTarget()).getHandle();
            } else {
                entityliving = null;
            }
        }
        this.attackTarget = entityliving;
        return true;
        // CraftBukkit end
    }

    public boolean canAttackClass(Class<? extends EntityLivingBase> oclass) {
        return oclass != EntityGhast.class;
    }

    public void eatGrassBonus() {}

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityLiving.AI_FLAGS, Byte.valueOf((byte) 0));
    }

    public int getTalkInterval() {
        return 80;
    }

    public void playLivingSound() {
        SoundEvent soundeffect = this.getAmbientSound();

        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.getSoundPitch());
        }

    }

    public void onEntityUpdate() {
        super.onEntityUpdate();
        this.world.profiler.startSection("mobBaseTick");
        if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
            this.applyEntityAI();
            this.playLivingSound();
        }

        this.world.profiler.endSection();
    }

    protected void playHurtSound(DamageSource damagesource) {
        this.applyEntityAI();
        super.playHurtSound(damagesource);
    }

    private void applyEntityAI() {
        this.livingSoundTime = -this.getTalkInterval();
    }

    protected int getExperiencePoints(EntityPlayer entityhuman) {
        if (this.experienceValue > 0) {
            int i = this.experienceValue;

            int j;

            for (j = 0; j < this.inventoryArmor.size(); ++j) {
                if (!((ItemStack) this.inventoryArmor.get(j)).isEmpty() && this.inventoryArmorDropChances[j] <= 1.0F) {
                    i += 1 + this.rand.nextInt(3);
                }
            }

            for (j = 0; j < this.inventoryHands.size(); ++j) {
                if (!((ItemStack) this.inventoryHands.get(j)).isEmpty() && this.inventoryHandsDropChances[j] <= 1.0F) {
                    i += 1 + this.rand.nextInt(3);
                }
            }

            return i;
        } else {
            return this.experienceValue;
        }
    }

    public void spawnExplosionParticle() {
        if (this.world.isRemote) {
            for (int i = 0; i < 20; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d3 = 10.0D;

                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * 10.0D, this.posY + (double) (this.rand.nextFloat() * this.height) - d1 * 10.0D, this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * 10.0D, d0, d1, d2, new int[0]);
            }
        } else {
            this.world.setEntityState(this, (byte) 20);
        }

    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.updateLeashedState();
            if (this.ticksExisted % 5 == 0) {
                boolean flag = !(this.getControllingPassenger() instanceof EntityLiving);
                boolean flag1 = !(this.getRidingEntity() instanceof EntityBoat);

                this.tasks.setControlFlag(1, flag);
                this.tasks.setControlFlag(4, flag && flag1);
                this.tasks.setControlFlag(2, flag);
            }
        }

    }

    protected float updateDistance(float f, float f1) {
        this.bodyHelper.updateRenderAngles();
        return f1;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    protected Item getDropItem() {
        return null;
    }

    protected void dropFewItems(boolean flag, int i) {
        Item item = this.getDropItem();

        if (item != null) {
            int j = this.rand.nextInt(3);

            if (i > 0) {
                j += this.rand.nextInt(i + 1);
            }

            for (int k = 0; k < j; ++k) {
                this.dropItem(item, 1);
            }
        }

    }

    public static void registerFixesMob(DataFixer dataconvertermanager, Class<?> oclass) {
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackDataLists(oclass, new String[] { "ArmorItems", "HandItems"})));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
        nbttagcompound.setBoolean("PersistenceRequired", this.persistenceRequired);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.inventoryArmor.iterator(); iterator.hasNext(); nbttaglist.appendTag(nbttagcompound1)) {
            ItemStack itemstack = (ItemStack) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            if (!itemstack.isEmpty()) {
                itemstack.writeToNBT(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.inventoryHands.iterator(); iterator1.hasNext(); nbttaglist1.appendTag(nbttagcompound2)) {
            ItemStack itemstack1 = (ItemStack) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            if (!itemstack1.isEmpty()) {
                itemstack1.writeToNBT(nbttagcompound2);
            }
        }

        nbttagcompound.setTag("HandItems", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();
        float[] afloat = this.inventoryArmorDropChances;
        int i = afloat.length;

        int j;

        for (j = 0; j < i; ++j) {
            float f = afloat[j];

            nbttaglist2.appendTag(new NBTTagFloat(f));
        }

        nbttagcompound.setTag("ArmorDropChances", nbttaglist2);
        NBTTagList nbttaglist3 = new NBTTagList();
        float[] afloat1 = this.inventoryHandsDropChances;

        j = afloat1.length;

        for (int k = 0; k < j; ++k) {
            float f1 = afloat1[k];

            nbttaglist3.appendTag(new NBTTagFloat(f1));
        }

        nbttagcompound.setTag("HandDropChances", nbttaglist3);
        nbttagcompound.setBoolean("Leashed", this.isLeashed);
        if (this.leashHolder != null) {
            nbttagcompound2 = new NBTTagCompound();
            if (this.leashHolder instanceof EntityLivingBase) {
                UUID uuid = this.leashHolder.getUniqueID();

                nbttagcompound2.setUniqueId("UUID", uuid);
            } else if (this.leashHolder instanceof EntityHanging) {
                BlockPos blockposition = ((EntityHanging) this.leashHolder).getHangingPosition();

                nbttagcompound2.setInteger("X", blockposition.getX());
                nbttagcompound2.setInteger("Y", blockposition.getY());
                nbttagcompound2.setInteger("Z", blockposition.getZ());
            }

            nbttagcompound.setTag("Leash", nbttagcompound2);
        }

        nbttagcompound.setBoolean("LeftHanded", this.isLeftHanded());
        if (this.deathLootTable != null) {
            nbttagcompound.setString("DeathLootTable", this.deathLootTable.toString());
            if (this.deathLootTableSeed != 0L) {
                nbttagcompound.setLong("DeathLootTableSeed", this.deathLootTableSeed);
            }
        }

        if (this.isAIDisabled()) {
            nbttagcompound.setBoolean("NoAI", this.isAIDisabled());
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);

        // CraftBukkit start - If looting or persistence is false only use it if it was set after we started using it
        if (nbttagcompound.hasKey("CanPickUpLoot", 1)) {
            boolean data = nbttagcompound.getBoolean("CanPickUpLoot");
            if (isLevelAtLeast(nbttagcompound, 1) || data) {
                this.setCanPickUpLoot(data);
            }
        }

        boolean data = nbttagcompound.getBoolean("PersistenceRequired");
        if (isLevelAtLeast(nbttagcompound, 1) || data) {
            this.persistenceRequired = data;
        }
        // CraftBukkit end
        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.hasKey("ArmorItems", 9)) {
            nbttaglist = nbttagcompound.getTagList("ArmorItems", 10);

            for (i = 0; i < this.inventoryArmor.size(); ++i) {
                this.inventoryArmor.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (nbttagcompound.hasKey("HandItems", 9)) {
            nbttaglist = nbttagcompound.getTagList("HandItems", 10);

            for (i = 0; i < this.inventoryHands.size(); ++i) {
                this.inventoryHands.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (nbttagcompound.hasKey("ArmorDropChances", 9)) {
            nbttaglist = nbttagcompound.getTagList("ArmorDropChances", 5);

            for (i = 0; i < nbttaglist.tagCount(); ++i) {
                this.inventoryArmorDropChances[i] = nbttaglist.getFloatAt(i);
            }
        }

        if (nbttagcompound.hasKey("HandDropChances", 9)) {
            nbttaglist = nbttagcompound.getTagList("HandDropChances", 5);

            for (i = 0; i < nbttaglist.tagCount(); ++i) {
                this.inventoryHandsDropChances[i] = nbttaglist.getFloatAt(i);
            }
        }

        this.isLeashed = nbttagcompound.getBoolean("Leashed");
        if (this.isLeashed && nbttagcompound.hasKey("Leash", 10)) {
            this.leashNBTTag = nbttagcompound.getCompoundTag("Leash");
        }

        this.setLeftHanded(nbttagcompound.getBoolean("LeftHanded"));
        if (nbttagcompound.hasKey("DeathLootTable", 8)) {
            this.deathLootTable = new ResourceLocation(nbttagcompound.getString("DeathLootTable"));
            this.deathLootTableSeed = nbttagcompound.getLong("DeathLootTableSeed");
        }

        this.setNoAI(nbttagcompound.getBoolean("NoAI"));
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return null;
    }

    protected void dropLoot(boolean flag, int i, DamageSource damagesource) {
        ResourceLocation minecraftkey = this.deathLootTable;

        if (minecraftkey == null) {
            minecraftkey = this.getLootTable();
        }

        if (minecraftkey != null) {
            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(minecraftkey);

            this.deathLootTable = null;
            LootTableInfo.a loottableinfo_a = (new LootTableInfo.a((WorldServer) this.world)).a((Entity) this).a(damagesource);

            if (flag && this.attackingPlayer != null) {
                loottableinfo_a = loottableinfo_a.a(this.attackingPlayer).a(this.attackingPlayer.getLuck());
            }

            List list = loottable.generateLootForPools(this.deathLootTableSeed == 0L ? this.rand : new Random(this.deathLootTableSeed), loottableinfo_a.a());
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();

                this.entityDropItem(itemstack, 0.0F);
            }

            this.dropEquipment(flag, i);
        } else {
            super.dropLoot(flag, i, damagesource);
        }

    }

    public void setMoveForward(float f) {
        this.moveForward = f;
    }

    public void setMoveVertical(float f) {
        this.moveVertical = f;
    }

    public void setMoveStrafing(float f) {
        this.moveStrafing = f;
    }

    public void setAIMoveSpeed(float f) {
        super.setAIMoveSpeed(f);
        this.setMoveForward(f);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.world.profiler.startSection("looting");
        if (!this.world.isRemote && this.canPickUpLoot() && !this.dead && this.world.getGameRules().getBoolean("mobGriefing")) {
            List list = this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                // Paper Start
                if (!entityitem.canMobPickup) {
                    continue;
                }
                // Paper End

                if (!entityitem.isDead && !entityitem.getItem().isEmpty() && !entityitem.cannotPickup()) {
                    this.updateEquipmentIfNeeded(entityitem);
                }
            }
        }

        this.world.profiler.endSection();
    }

    protected void updateEquipmentIfNeeded(EntityItem entityitem) {
        ItemStack itemstack = entityitem.getItem();
        EntityEquipmentSlot enumitemslot = getSlotForItemStack(itemstack);
        boolean flag = true;
        ItemStack itemstack1 = this.getItemStackFromSlot(enumitemslot);

        if (!itemstack1.isEmpty()) {
            if (enumitemslot.getSlotType() == EntityEquipmentSlot.Type.HAND) {
                if (itemstack.getItem() instanceof ItemSword && !(itemstack1.getItem() instanceof ItemSword)) {
                    flag = true;
                } else if (itemstack.getItem() instanceof ItemSword && itemstack1.getItem() instanceof ItemSword) {
                    ItemSword itemsword = (ItemSword) itemstack.getItem();
                    ItemSword itemsword1 = (ItemSword) itemstack1.getItem();

                    if (itemsword.getAttackDamage() == itemsword1.getAttackDamage()) {
                        flag = itemstack.getMetadata() > itemstack1.getMetadata() || itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
                    } else {
                        flag = itemsword.getAttackDamage() > itemsword1.getAttackDamage();
                    }
                } else if (itemstack.getItem() instanceof ItemBow && itemstack1.getItem() instanceof ItemBow) {
                    flag = itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
                } else {
                    flag = false;
                }
            } else if (itemstack.getItem() instanceof ItemArmor && !(itemstack1.getItem() instanceof ItemArmor)) {
                flag = true;
            } else if (itemstack.getItem() instanceof ItemArmor && itemstack1.getItem() instanceof ItemArmor && !EnchantmentHelper.hasBindingCurse(itemstack1)) {
                ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
                ItemArmor itemarmor1 = (ItemArmor) itemstack1.getItem();

                if (itemarmor.damageReduceAmount == itemarmor1.damageReduceAmount) {
                    flag = itemstack.getMetadata() > itemstack1.getMetadata() || itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
                } else {
                    flag = itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount;
                }
            } else {
                flag = false;
            }
        }

        // CraftBukkit start
        boolean canPickup = flag && this.canEquipItem(itemstack);

        EntityPickupItemEvent entityEvent = new EntityPickupItemEvent((LivingEntity) getBukkitEntity(), (org.bukkit.entity.Item) entityitem.getBukkitEntity(), 0);
        entityEvent.setCancelled(!canPickup);
        this.world.getServer().getPluginManager().callEvent(entityEvent);
        canPickup = !entityEvent.isCancelled();
        if (canPickup) {
            // CraftBukkit end
            double d0;

            switch (enumitemslot.getSlotType()) {
            case HAND:
                d0 = (double) this.inventoryHandsDropChances[enumitemslot.getIndex()];
                break;

            case ARMOR:
                d0 = (double) this.inventoryArmorDropChances[enumitemslot.getIndex()];
                break;

            default:
                d0 = 0.0D;
            }

            if (!itemstack1.isEmpty() && (double) (this.rand.nextFloat() - 0.1F) < d0) {
                this.forceDrops = true; // CraftBukkit
                this.entityDropItem(itemstack1, 0.0F);
                this.forceDrops = false; // CraftBukkit
            }

            this.setItemStackToSlot(enumitemslot, itemstack);
            switch (enumitemslot.getSlotType()) {
            case HAND:
                this.inventoryHandsDropChances[enumitemslot.getIndex()] = 2.0F;
                break;

            case ARMOR:
                this.inventoryArmorDropChances[enumitemslot.getIndex()] = 2.0F;
            }

            this.persistenceRequired = true;
            this.onItemPickup(entityitem, itemstack.getCount());
            entityitem.setDead();
        }

    }

    protected boolean canEquipItem(ItemStack itemstack) {
        return true;
    }

    protected boolean canDespawn() {
        return true;
    }

    protected void despawnEntity() {
        if (this.persistenceRequired) {
            this.idleTime = 0;
        } else {
            EntityPlayer entityhuman = this.world.getClosestPlayerToEntity(this, -1.0D);

            if (entityhuman != null && entityhuman.affectsSpawning) { // Paper - Affects Spawning API
                double d0 = entityhuman.posX - this.posX;
                double d1 = entityhuman.posY - this.posY;
                double d2 = entityhuman.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > world.paperConfig.hardDespawnDistance) { // CraftBukkit - remove isTypeNotPersistent() check // Paper - custom despawn distances
                    this.setDead();
                }

                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d3 > world.paperConfig.softDespawnDistance) { // CraftBukkit - remove isTypeNotPersistent() check // Paper - custom despawn distances
                    this.setDead();
                } else if (d3 < world.paperConfig.softDespawnDistance) { // Paper - custom despawn distances
                    this.idleTime = 0;
                }
            }

        }
    }

    protected final void updateEntityActionState() {
        ++this.idleTime;
        this.world.profiler.startSection("checkDespawn");
        this.despawnEntity();
        this.world.profiler.endSection();
        // Spigot Start
        if ( this.fromMobSpawner )
        {
            // Paper start - Allow nerfed mobs to jump and float
            if (goalFloat != null) {
                if (goalFloat.validConditions()) goalFloat.update();
                this.getJumpHelper().jumpIfSet();
            }
            // Paper end
            return;
        }
        // Spigot End
        this.world.profiler.startSection("sensing");
        this.senses.clearSensingCache();
        this.world.profiler.endSection();
        this.world.profiler.startSection("targetSelector");
        this.targetTasks.onUpdateTasks();
        this.world.profiler.endSection();
        this.world.profiler.startSection("goalSelector");
        this.tasks.onUpdateTasks();
        this.world.profiler.endSection();
        this.world.profiler.startSection("navigation");
        this.navigator.onUpdateNavigation();
        this.world.profiler.endSection();
        this.world.profiler.startSection("mob tick");
        this.updateAITasks();
        this.world.profiler.endSection();
        if (this.isRiding() && this.getRidingEntity() instanceof EntityLiving) {
            EntityLiving entityinsentient = (EntityLiving) this.getRidingEntity();

            entityinsentient.getNavigator().setPath(this.getNavigator().getPath(), 1.5D);
            entityinsentient.getMoveHelper().read(this.getMoveHelper());
        }

        this.world.profiler.startSection("controls");
        this.world.profiler.startSection("move");
        this.moveHelper.onUpdateMoveHelper();
        this.world.profiler.endStartSection("look");
        this.lookHelper.onUpdateLook();
        this.world.profiler.endStartSection("jump");
        this.jumpHelper.doJump();
        this.world.profiler.endSection();
        this.world.profiler.endSection();
    }

    protected void updateAITasks() {}

    public int getVerticalFaceSpeed() {
        return 40;
    }

    public int getHorizontalFaceSpeed() {
        return 10;
    }

    public void faceEntity(Entity entity, float f, float f1) {
        double d0 = entity.posX - this.posX;
        double d1 = entity.posZ - this.posZ;
        double d2;

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityliving = (EntityLivingBase) entity;

            d2 = entityliving.posY + (double) entityliving.getEyeHeight() - (this.posY + (double) this.getEyeHeight());
        } else {
            d2 = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0D - (this.posY + (double) this.getEyeHeight());
        }

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (MathHelper.atan2(d1, d0) * 57.2957763671875D) - 90.0F;
        float f3 = (float) (-(MathHelper.atan2(d2, d3) * 57.2957763671875D));

        this.rotationPitch = this.updateRotation(this.rotationPitch, f3, f1);
        this.rotationYaw = this.updateRotation(this.rotationYaw, f2, f);
    }

    private float updateRotation(float f, float f1, float f2) {
        float f3 = MathHelper.wrapDegrees(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public boolean getCanSpawnHere() {
        IBlockState iblockdata = this.world.getBlockState((new BlockPos(this)).down());

        return iblockdata.canEntitySpawn((Entity) this);
    }

    public boolean isNotColliding() {
        return !this.world.containsAnyLiquid(this.getEntityBoundingBox()) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.world.checkNoEntityCollision(this.getEntityBoundingBox(), (Entity) this);
    }

    public int getMaxSpawnedInChunk() {
        return 4;
    }

    public int getMaxFallHeight() {
        if (this.getAttackTarget() == null) {
            return 3;
        } else {
            int i = (int) (this.getHealth() - this.getMaxHealth() * 0.33F);

            i -= (3 - this.world.getDifficulty().getDifficultyId()) * 4;
            if (i < 0) {
                i = 0;
            }

            return i + 3;
        }
    }

    public Iterable<ItemStack> getHeldEquipment() {
        return this.inventoryHands;
    }

    public Iterable<ItemStack> getArmorInventoryList() {
        return this.inventoryArmor;
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot enumitemslot) {
        switch (enumitemslot.getSlotType()) {
        case HAND:
            return (ItemStack) this.inventoryHands.get(enumitemslot.getIndex());

        case ARMOR:
            return (ItemStack) this.inventoryArmor.get(enumitemslot.getIndex());

        default:
            return ItemStack.EMPTY;
        }
    }

    public void setItemStackToSlot(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        switch (enumitemslot.getSlotType()) {
        case HAND:
            this.inventoryHands.set(enumitemslot.getIndex(), itemstack);
            break;

        case ARMOR:
            this.inventoryArmor.set(enumitemslot.getIndex(), itemstack);
        }

    }

    protected void dropEquipment(boolean flag, int i) {
        EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
        int j = aenumitemslot.length;

        for (int k = 0; k < j; ++k) {
            EntityEquipmentSlot enumitemslot = aenumitemslot[k];
            ItemStack itemstack = this.getItemStackFromSlot(enumitemslot);
            double d0;

            switch (enumitemslot.getSlotType()) {
            case HAND:
                d0 = (double) this.inventoryHandsDropChances[enumitemslot.getIndex()];
                break;

            case ARMOR:
                d0 = (double) this.inventoryArmorDropChances[enumitemslot.getIndex()];
                break;

            default:
                d0 = 0.0D;
            }

            boolean flag1 = d0 > 1.0D;

            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && (flag || flag1) && (double) (this.rand.nextFloat() - (float) i * 0.01F) < d0) {
                if (!flag1 && itemstack.isItemStackDamageable()) {
                    itemstack.setItemDamage(itemstack.getMaxDamage() - this.rand.nextInt(1 + this.rand.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
                }

                this.entityDropItem(itemstack, 0.0F);
            }
        }

    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultydamagescaler) {
        if (this.rand.nextFloat() < 0.15F * difficultydamagescaler.getClampedAdditionalDifficulty()) {
            int i = this.rand.nextInt(2);
            float f = this.world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;

            if (this.rand.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F) {
                ++i;
            }

            boolean flag = true;
            EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
            int j = aenumitemslot.length;

            for (int k = 0; k < j; ++k) {
                EntityEquipmentSlot enumitemslot = aenumitemslot[k];

                if (enumitemslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                    ItemStack itemstack = this.getItemStackFromSlot(enumitemslot);

                    if (!flag && this.rand.nextFloat() < f) {
                        break;
                    }

                    flag = false;
                    if (itemstack.isEmpty()) {
                        Item item = getArmorByChance(enumitemslot, i);

                        if (item != null) {
                            this.setItemStackToSlot(enumitemslot, new ItemStack(item));
                        }
                    }
                }
            }
        }

    }

    public static EntityEquipmentSlot getSlotForItemStack(ItemStack itemstack) {
        return itemstack.getItem() != Item.getItemFromBlock(Blocks.PUMPKIN) && itemstack.getItem() != Items.SKULL ? (itemstack.getItem() instanceof ItemArmor ? ((ItemArmor) itemstack.getItem()).armorType : (itemstack.getItem() == Items.ELYTRA ? EntityEquipmentSlot.CHEST : (itemstack.getItem() == Items.SHIELD ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND))) : EntityEquipmentSlot.HEAD;
    }

    @Nullable
    public static Item getArmorByChance(EntityEquipmentSlot enumitemslot, int i) {
        switch (enumitemslot) {
        case HEAD:
            if (i == 0) {
                return Items.LEATHER_HELMET;
            } else if (i == 1) {
                return Items.GOLDEN_HELMET;
            } else if (i == 2) {
                return Items.CHAINMAIL_HELMET;
            } else if (i == 3) {
                return Items.IRON_HELMET;
            } else if (i == 4) {
                return Items.DIAMOND_HELMET;
            }

        case CHEST:
            if (i == 0) {
                return Items.LEATHER_CHESTPLATE;
            } else if (i == 1) {
                return Items.GOLDEN_CHESTPLATE;
            } else if (i == 2) {
                return Items.CHAINMAIL_CHESTPLATE;
            } else if (i == 3) {
                return Items.IRON_CHESTPLATE;
            } else if (i == 4) {
                return Items.DIAMOND_CHESTPLATE;
            }

        case LEGS:
            if (i == 0) {
                return Items.LEATHER_LEGGINGS;
            } else if (i == 1) {
                return Items.GOLDEN_LEGGINGS;
            } else if (i == 2) {
                return Items.CHAINMAIL_LEGGINGS;
            } else if (i == 3) {
                return Items.IRON_LEGGINGS;
            } else if (i == 4) {
                return Items.DIAMOND_LEGGINGS;
            }

        case FEET:
            if (i == 0) {
                return Items.LEATHER_BOOTS;
            } else if (i == 1) {
                return Items.GOLDEN_BOOTS;
            } else if (i == 2) {
                return Items.CHAINMAIL_BOOTS;
            } else if (i == 3) {
                return Items.IRON_BOOTS;
            } else if (i == 4) {
                return Items.DIAMOND_BOOTS;
            }

        default:
            return null;
        }
    }

    protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficultydamagescaler) {
        float f = difficultydamagescaler.getClampedAdditionalDifficulty();

        if (!this.getHeldItemMainhand().isEmpty() && this.rand.nextFloat() < 0.25F * f) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItemMainhand(), (int) (5.0F + f * (float) this.rand.nextInt(18)), false));
        }

        EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EntityEquipmentSlot enumitemslot = aenumitemslot[j];

            if (enumitemslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                ItemStack itemstack = this.getItemStackFromSlot(enumitemslot);

                if (!itemstack.isEmpty() && this.rand.nextFloat() < 0.5F * f) {
                    this.setItemStackToSlot(enumitemslot, EnchantmentHelper.addRandomEnchantment(this.rand, itemstack, (int) (5.0F + f * (float) this.rand.nextInt(18)), false));
                }
            }
        }

    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        if (this.rand.nextFloat() < 0.05F) {
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }

        return groupdataentity;
    }

    public boolean canBeSteered() {
        return false;
    }

    public void enablePersistence() {
        this.persistenceRequired = true;
    }

    public void setDropChance(EntityEquipmentSlot enumitemslot, float f) {
        switch (enumitemslot.getSlotType()) {
        case HAND:
            this.inventoryHandsDropChances[enumitemslot.getIndex()] = f;
            break;

        case ARMOR:
            this.inventoryArmorDropChances[enumitemslot.getIndex()] = f;
        }

    }

    public boolean canPickUpLoot() {
        return this.canPickUpLoot;
    }

    public void setCanPickUpLoot(boolean flag) {
        this.canPickUpLoot = flag;
    }

    public boolean isNoDespawnRequired() {
        return this.persistenceRequired;
    }

    public final boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        if (this.getLeashed() && this.getLeashHolder() == entityhuman) {
            // CraftBukkit start - fire PlayerUnleashEntityEvent
            if (CraftEventFactory.callPlayerUnleashEntityEvent(this, entityhuman).isCancelled()) {
                ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketEntityAttach(this, this.getLeashHolder()));
                return false;
            }
            // CraftBukkit end
            this.clearLeashed(true, !entityhuman.capabilities.isCreativeMode);
            return true;
        } else {
            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (itemstack.getItem() == Items.LEAD && this.canBeLeashedTo(entityhuman)) {
                // CraftBukkit start - fire PlayerLeashEntityEvent
                if (CraftEventFactory.callPlayerLeashEntityEvent(this, entityhuman, entityhuman).isCancelled()) {
                    ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketEntityAttach(this, this.getLeashHolder()));
                    return false;
                }
                // CraftBukkit end
                this.setLeashHolder(entityhuman, true);
                itemstack.shrink(1);
                return true;
            } else {
                return this.processInteract(entityhuman, enumhand) ? true : super.processInitialInteract(entityhuman, enumhand);
            }
        }
    }

    protected boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        return false;
    }

    protected void updateLeashedState() {
        if (this.leashNBTTag != null) {
            this.recreateLeash();
        }

        if (this.isLeashed) {
            if (!this.isEntityAlive()) {
                this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), UnleashReason.PLAYER_UNLEASH)); // CraftBukkit
                this.clearLeashed(true, true);
            }

            if (this.leashHolder == null || this.leashHolder.isDead) {
                this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), UnleashReason.HOLDER_GONE)); // CraftBukkit
                this.clearLeashed(true, true);
            }
        }
    }

    public void clearLeashed(boolean flag, boolean flag1) {
        if (this.isLeashed) {
            this.isLeashed = false;
            this.leashHolder = null;
            if (!this.world.isRemote && flag1) {
                this.forceDrops = true; // CraftBukkit
                this.dropItem(Items.LEAD, 1);
                this.forceDrops = false; // CraftBukkit
            }

            if (!this.world.isRemote && flag && this.world instanceof WorldServer) {
                ((WorldServer) this.world).getEntityTracker().sendToTracking((Entity) this, (Packet) (new SPacketEntityAttach(this, (Entity) null)));
            }
        }

    }

    public boolean canBeLeashedTo(EntityPlayer entityhuman) {
        return !this.getLeashed() && !(this instanceof IMob);
    }

    public boolean getLeashed() {
        return this.isLeashed;
    }

    public Entity getLeashHolder() {
        return this.leashHolder;
    }

    public void setLeashHolder(Entity entity, boolean flag) {
        this.isLeashed = true;
        this.leashHolder = entity;
        if (!this.world.isRemote && flag && this.world instanceof WorldServer) {
            ((WorldServer) this.world).getEntityTracker().sendToTracking((Entity) this, (Packet) (new SPacketEntityAttach(this, this.leashHolder)));
        }

        if (this.isRiding()) {
            this.dismountRidingEntity();
        }

    }

    public boolean startRiding(Entity entity, boolean flag) {
        boolean flag1 = super.startRiding(entity, flag);

        if (flag1 && this.getLeashed()) {
            this.clearLeashed(true, true);
        }

        return flag1;
    }

    private void recreateLeash() {
        if (this.isLeashed && this.leashNBTTag != null) {
            if (this.leashNBTTag.hasUniqueId("UUID")) {
                UUID uuid = this.leashNBTTag.getUniqueId("UUID");
                List list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10.0D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                    if (entityliving.getUniqueID().equals(uuid)) {
                        this.setLeashHolder(entityliving, true);
                        break;
                    }
                }
            } else if (this.leashNBTTag.hasKey("X", 99) && this.leashNBTTag.hasKey("Y", 99) && this.leashNBTTag.hasKey("Z", 99)) {
                BlockPos blockposition = new BlockPos(this.leashNBTTag.getInteger("X"), this.leashNBTTag.getInteger("Y"), this.leashNBTTag.getInteger("Z"));
                EntityLeashKnot entityleash = EntityLeashKnot.getKnotForPosition(this.world, blockposition);

                if (entityleash == null) {
                    entityleash = EntityLeashKnot.createKnot(this.world, blockposition);
                }

                this.setLeashHolder(entityleash, true);
            } else {
                this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), UnleashReason.UNKNOWN)); // CraftBukkit
                this.clearLeashed(false, true);
            }
        }

        this.leashNBTTag = null;
    }

    public boolean replaceItemInInventory(int i, ItemStack itemstack) {
        EntityEquipmentSlot enumitemslot;

        if (i == 98) {
            enumitemslot = EntityEquipmentSlot.MAINHAND;
        } else if (i == 99) {
            enumitemslot = EntityEquipmentSlot.OFFHAND;
        } else if (i == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
            enumitemslot = EntityEquipmentSlot.HEAD;
        } else if (i == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
            enumitemslot = EntityEquipmentSlot.CHEST;
        } else if (i == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
            enumitemslot = EntityEquipmentSlot.LEGS;
        } else {
            if (i != 100 + EntityEquipmentSlot.FEET.getIndex()) {
                return false;
            }

            enumitemslot = EntityEquipmentSlot.FEET;
        }

        if (!itemstack.isEmpty() && !isItemStackInSlot(enumitemslot, itemstack) && enumitemslot != EntityEquipmentSlot.HEAD) {
            return false;
        } else {
            this.setItemStackToSlot(enumitemslot, itemstack);
            return true;
        }
    }

    public boolean canPassengerSteer() {
        return this.canBeSteered() && super.canPassengerSteer();
    }

    public static boolean isItemStackInSlot(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        EntityEquipmentSlot enumitemslot1 = getSlotForItemStack(itemstack);

        return enumitemslot1 == enumitemslot || enumitemslot1 == EntityEquipmentSlot.MAINHAND && enumitemslot == EntityEquipmentSlot.OFFHAND || enumitemslot1 == EntityEquipmentSlot.OFFHAND && enumitemslot == EntityEquipmentSlot.MAINHAND;
    }

    public boolean isServerWorld() {
        return super.isServerWorld() && !this.isAIDisabled();
    }

    public void setNoAI(boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntityLiving.AI_FLAGS)).byteValue();

        this.dataManager.set(EntityLiving.AI_FLAGS, Byte.valueOf(flag ? (byte) (b0 | 1) : (byte) (b0 & -2)));
    }

    public void setLeftHanded(boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntityLiving.AI_FLAGS)).byteValue();

        this.dataManager.set(EntityLiving.AI_FLAGS, Byte.valueOf(flag ? (byte) (b0 | 2) : (byte) (b0 & -3)));
    }

    public boolean isAIDisabled() {
        return (((Byte) this.dataManager.get(EntityLiving.AI_FLAGS)).byteValue() & 1) != 0;
    }

    public boolean isLeftHanded() {
        return (((Byte) this.dataManager.get(EntityLiving.AI_FLAGS)).byteValue() & 2) != 0;
    }

    public EnumHandSide getPrimaryHand() {
        return this.isLeftHanded() ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

    public static enum SpawnPlacementType {

        ON_GROUND, IN_AIR, IN_WATER;

        private SpawnPlacementType() {}
    }
}
