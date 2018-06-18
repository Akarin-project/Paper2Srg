package net.minecraft.entity.item;

import java.util.Iterator;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.destroystokyo.paper.HopperPusher;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

// CraftBukkit start
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
// CraftBukkit end
import org.bukkit.event.player.PlayerAttemptPickupItemEvent; // Paper
import com.destroystokyo.paper.HopperPusher; // Paper

// Paper start - implement HopperPusher
public class EntityItem extends Entity implements HopperPusher {
    @Override
    public boolean acceptItem(TileEntityHopper hopper) {
        return TileEntityHopper.putDropInInventory(null, hopper, this);
    }
// Paper end

    private static final Logger LOGGER = LogManager.getLogger();
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItem.class, DataSerializers.ITEM_STACK);
    private int age;
    public int pickupDelay;
    public boolean canMobPickup = true; // Paper
    private int health;
    private String thrower;
    private String owner;
    public float hoverStart;
    private int lastTick = MinecraftServer.currentTick - 1; // CraftBukkit

    public EntityItem(World world, double d0, double d1, double d2) {
        super(world);
        this.health = 5;
        this.hoverStart = (float) (Math.random() * 3.141592653589793D * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.setPosition(d0, d1, d2);
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
    }

    public EntityItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
        this(world, d0, d1, d2);
        this.setItem(itemstack);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public EntityItem(World world) {
        super(world);
        this.health = 5;
        this.hoverStart = (float) (Math.random() * 3.141592653589793D * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.setItem(ItemStack.EMPTY);
    }

    protected void entityInit() {
        this.getDataManager().register(EntityItem.ITEM, ItemStack.EMPTY);
    }

    public void onUpdate() {
        if (this.getItem().isEmpty()) {
            this.setDead();
        } else {
            super.onUpdate();
            if (tryPutInHopper()) return; // Paper
            // CraftBukkit start - Use wall time for pickup and despawn timers
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            if (this.pickupDelay != 32767) this.pickupDelay -= elapsedTicks;
            if (this.age != -32768) this.age += elapsedTicks;
            this.lastTick = MinecraftServer.currentTick;
            // CraftBukkit end

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            double d0 = this.motionX;
            double d1 = this.motionY;
            double d2 = this.motionZ;

            if (!this.hasNoGravity()) {
                this.motionY -= 0.03999999910593033D;
            }

            if (this.world.isRemote) {
                this.noClip = false;
            } else {
                this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY || (int) this.prevPosZ != (int) this.posZ;

            if (flag || this.ticksExisted % 25 == 0) {
                if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
                    this.motionY = 0.20000000298023224D;
                    this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
                }

                if (!this.world.isRemote) {
                    this.searchForOtherItemsNearby();
                }
            }

            float f = 0.98F;

            if (this.onGround) {
                f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
            }

            this.motionX *= (double) f;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= (double) f;
            if (this.onGround) {
                this.motionY *= -0.5D;
            }

            /* Craftbukkit start - moved up
            if (this.age != -32768) {
                ++this.age;
            }
            // Craftbukkit end */

            this.handleWaterMovement();
            if (!this.world.isRemote) {
                double d3 = this.motionX - d0;
                double d4 = this.motionY - d1;
                double d5 = this.motionZ - d2;
                double d6 = d3 * d3 + d4 * d4 + d5 * d5;

                if (d6 > 0.01D) {
                    this.isAirBorne = true;
                }
            }

            if (!this.world.isRemote && this.age >= world.spigotConfig.itemDespawnRate) { // Spigot
                // CraftBukkit start - fire ItemDespawnEvent
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                    this.age = 0;
                    return;
                }
                // CraftBukkit end
                this.setDead();
            }

        }
    }

    // Spigot start - copied from above
    @Override
    public void inactiveTick() {
        if (tryPutInHopper()) return; // Paper
        // CraftBukkit start - Use wall time for pickup and despawn timers
        int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
        if (this.pickupDelay != 32767) this.pickupDelay -= elapsedTicks;
        if (this.age != -32768) this.age += elapsedTicks;
        this.lastTick = MinecraftServer.currentTick;
        // CraftBukkit end

        if (!this.world.isRemote && this.age >= world.spigotConfig.itemDespawnRate) { // Spigot
            // CraftBukkit start - fire ItemDespawnEvent
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                this.age = 0;
                return;
            }
            // CraftBukkit end
            this.setDead();
        }
    }
    // Spigot end

    private void searchForOtherItemsNearby() {
        // Spigot start
        double radius = world.spigotConfig.itemMerge;
        Iterator iterator = this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(radius, radius, radius)).iterator();
        // Spigot end

        while (iterator.hasNext()) {
            EntityItem entityitem = (EntityItem) iterator.next();

            this.combineItems(entityitem);
        }

    }

    private boolean combineItems(EntityItem entityitem) {
        if (entityitem == this) {
            return false;
        } else if (entityitem.isEntityAlive() && this.isEntityAlive()) {
            ItemStack itemstack = this.getItem();
            ItemStack itemstack1 = entityitem.getItem();

            if (this.pickupDelay != 32767 && entityitem.pickupDelay != 32767) {
                if (this.age != -32768 && entityitem.age != -32768) {
                    if (itemstack1.getItem() != itemstack.getItem()) {
                        return false;
                    } else if (itemstack1.hasTagCompound() ^ itemstack.hasTagCompound()) {
                        return false;
                    } else if (itemstack1.hasTagCompound() && !itemstack1.getTagCompound().equals(itemstack.getTagCompound())) {
                        return false;
                    } else if (itemstack1.getItem() == null) {
                        return false;
                    } else if (itemstack1.getItem().getHasSubtypes() && itemstack1.getMetadata() != itemstack.getMetadata()) {
                        return false;
                    } else if (itemstack1.getCount() < itemstack.getCount()) {
                        return entityitem.combineItems(this);
                    } else if (itemstack1.getCount() + itemstack.getCount() > itemstack1.getMaxStackSize()) {
                        return false;
                    } else {
                        // Spigot start
                        if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemMergeEvent(entityitem, this).isCancelled()) return false; // CraftBukkit
                        itemstack.grow(itemstack1.getCount());
                        this.pickupDelay = Math.max(entityitem.pickupDelay, this.pickupDelay);
                        this.age = Math.min(entityitem.age, this.age);
                        this.setItem(itemstack);
                        entityitem.setDead();
                        // Spigot end
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setAgeToCreativeDespawnTime() {
        this.age = 4800;
    }

    public boolean handleWaterMovement() {
        if (this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, (Entity) this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.doWaterSplashEffect();
            }

            this.inWater = true;
        } else {
            this.inWater = false;
        }

        return this.inWater;
    }

    protected void burn(int i) {
        this.attackEntityFrom(DamageSource.IN_FIRE, (float) i);
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (!this.getItem().isEmpty() && this.getItem().getItem() == Items.NETHER_STAR && damagesource.isExplosion()) {
            return false;
        } else {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                return false;
            }
            // CraftBukkit end
            this.markVelocityChanged();
            this.health = (int) ((float) this.health - f);
            if (this.health <= 0) {
                this.setDead();
            }

            return false;
        }
    }

    public static void registerFixesItem(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityItem.class, new String[] { "Item"})));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) this.health);
        nbttagcompound.setShort("Age", (short) this.age);
        nbttagcompound.setShort("PickupDelay", (short) this.pickupDelay);
        if (this.getThrower() != null) {
            nbttagcompound.setString("Thrower", this.thrower);
        }

        if (this.getOwner() != null) {
            nbttagcompound.setString("Owner", this.owner);
        }

        if (!this.getItem().isEmpty()) {
            nbttagcompound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.getShort("Health");
        this.age = nbttagcompound.getShort("Age");
        if (nbttagcompound.hasKey("PickupDelay")) {
            this.pickupDelay = nbttagcompound.getShort("PickupDelay");
        }

        if (nbttagcompound.hasKey("Owner")) {
            this.owner = nbttagcompound.getString("Owner");
        }

        if (nbttagcompound.hasKey("Thrower")) {
            this.thrower = nbttagcompound.getString("Thrower");
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");

        this.setItem(new ItemStack(nbttagcompound1));
        if (this.getItem().isEmpty()) {
            this.setDead();
        }

    }

    public void onCollideWithPlayer(EntityPlayer entityhuman) {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            // CraftBukkit start - fire PlayerPickupItemEvent
            int canHold = entityhuman.inventory.canHold(itemstack);
            int remaining = i - canHold;
            boolean flyAtPlayer = false; // Paper

            // Paper start
            if (this.pickupDelay <= 0) {
                PlayerAttemptPickupItemEvent attemptEvent = new PlayerAttemptPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                this.world.getServer().getPluginManager().callEvent(attemptEvent);

                flyAtPlayer = attemptEvent.getFlyAtPlayer();
                if (attemptEvent.isCancelled()) {
                    if (flyAtPlayer) {
                        entityhuman.onItemPickup(this, i);
                    }

                    return;
                }
            }
            // Paper end

            if (this.pickupDelay <= 0 && canHold > 0) {
                itemstack.setCount(canHold);
                // Call legacy event
                PlayerPickupItemEvent playerEvent = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                playerEvent.setCancelled(!entityhuman.canPickUpLoot);
                this.world.getServer().getPluginManager().callEvent(playerEvent);
                flyAtPlayer = playerEvent.getFlyAtPlayer(); // Paper
                if (playerEvent.isCancelled()) {
                    // Paper Start
                    if (flyAtPlayer) {
                        entityhuman.onItemPickup(this, i);
                    }
                    // Paper End
                    return;
                }

                // Call newer event afterwards
                EntityPickupItemEvent entityEvent = new EntityPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                entityEvent.setCancelled(!entityhuman.canPickUpLoot);
                this.world.getServer().getPluginManager().callEvent(entityEvent);
                if (entityEvent.isCancelled()) {
                    return;
                }

                itemstack.setCount(canHold + remaining);

                // Possibly < 0; fix here so we do not have to modify code below
                this.pickupDelay = 0;
            }
            // CraftBukkit end

            if (this.pickupDelay == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityhuman.getName())) && entityhuman.inventory.addItemStackToInventory(itemstack)) {
                // Paper Start
                if (flyAtPlayer) {
                    entityhuman.onItemPickup(this, i);
                }
                // Paper End
                if (itemstack.isEmpty()) {
                    this.setDead();
                    itemstack.setCount(i);
                }

                entityhuman.addStat(StatList.getObjectsPickedUpStats(item), i);
            }

        }
    }

    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : I18n.translateToLocal("item." + this.getItem().getUnlocalizedName());
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Nullable
    public Entity changeDimension(int i) {
        Entity entity = super.changeDimension(i);

        if (!this.world.isRemote && entity instanceof EntityItem) {
            ((EntityItem) entity).searchForOtherItemsNearby();
        }

        return entity;
    }

    public ItemStack getItem() {
        return (ItemStack) this.getDataManager().get(EntityItem.ITEM);
    }

    public void setItem(ItemStack itemstack) {
        this.getDataManager().set(EntityItem.ITEM, itemstack);
        this.getDataManager().setDirty(EntityItem.ITEM);
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String s) {
        this.owner = s;
    }

    public String getThrower() {
        return this.thrower;
    }

    public void setThrower(String s) {
        this.thrower = s;
    }

    public void setDefaultPickupDelay() {
        this.pickupDelay = 10;
    }

    public void setNoPickupDelay() {
        this.pickupDelay = 0;
    }

    public void setInfinitePickupDelay() {
        this.pickupDelay = 32767;
    }

    public void setPickupDelay(int i) {
        this.pickupDelay = i;
    }

    public boolean cannotPickup() {
        return this.pickupDelay > 0;
    }

    public void setNoDespawn() {
        this.age = -6000;
    }

    public void makeFakeItem() {
        this.setInfinitePickupDelay();
        this.age = world.spigotConfig.itemDespawnRate - 1; // Spigot
    }
}
