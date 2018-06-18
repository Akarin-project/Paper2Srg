package net.minecraft.entity.item;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;

// CraftBukkit start
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
// CraftBukkit end

public class EntityArmorStand extends EntityLivingBase {

    private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
    private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
    private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
    private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
    public static final DataParameter<Byte> STATUS = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.BYTE);
    public static final DataParameter<Rotations> HEAD_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> BODY_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> LEFT_ARM_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> RIGHT_ARM_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> LEFT_LEG_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    public static final DataParameter<Rotations> RIGHT_LEG_ROTATION = EntityDataManager.createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
    private static final Predicate<Entity> IS_RIDEABLE_MINECART = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof EntityMinecart && ((EntityMinecart) entity).getType() == EntityMinecart.Type.RIDEABLE;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    private final NonNullList<ItemStack> handItems;
    private final NonNullList<ItemStack> armorItems;
    private boolean canInteract;
    public long punchCooldown;
    private int disabledSlots;
    private boolean wasMarker;
    public Rotations headRotation;
    public Rotations bodyRotation;
    public Rotations leftArmRotation;
    public Rotations rightArmRotation;
    public Rotations leftLegRotation;
    public Rotations rightLegRotation;
    public boolean canMove = true; // Paper

    public EntityArmorStand(World world) {
        super(world);
        this.handItems = NonNullList.withSize(2, ItemStack.EMPTY);
        this.armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
        this.headRotation = EntityArmorStand.DEFAULT_HEAD_ROTATION;
        this.bodyRotation = EntityArmorStand.DEFAULT_BODY_ROTATION;
        this.leftArmRotation = EntityArmorStand.DEFAULT_LEFTARM_ROTATION;
        this.rightArmRotation = EntityArmorStand.DEFAULT_RIGHTARM_ROTATION;
        this.leftLegRotation = EntityArmorStand.DEFAULT_LEFTLEG_ROTATION;
        this.rightLegRotation = EntityArmorStand.DEFAULT_RIGHTLEG_ROTATION;
        this.noClip = this.hasNoGravity();
        this.setSize(0.5F, 1.975F);
    }

    public EntityArmorStand(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1, d2);
    }

    // CraftBukkit start - SPIGOT-3607, SPIGOT-3637
    @Override
    public float getBukkitYaw() {
        return this.rotationYaw;
    }
    // CraftBukkit end

    public final void setSize(float f, float f1) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        float f2 = this.hasMarker() ? 0.0F : (this.isChild() ? 0.5F : 1.0F);

        super.setSize(f * f2, f1 * f2);
        this.setPosition(d0, d1, d2);
    }

    public boolean isServerWorld() {
        return super.isServerWorld() && !this.hasNoGravity();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityArmorStand.STATUS, Byte.valueOf((byte) 0));
        this.dataManager.register(EntityArmorStand.HEAD_ROTATION, EntityArmorStand.DEFAULT_HEAD_ROTATION);
        this.dataManager.register(EntityArmorStand.BODY_ROTATION, EntityArmorStand.DEFAULT_BODY_ROTATION);
        this.dataManager.register(EntityArmorStand.LEFT_ARM_ROTATION, EntityArmorStand.DEFAULT_LEFTARM_ROTATION);
        this.dataManager.register(EntityArmorStand.RIGHT_ARM_ROTATION, EntityArmorStand.DEFAULT_RIGHTARM_ROTATION);
        this.dataManager.register(EntityArmorStand.LEFT_LEG_ROTATION, EntityArmorStand.DEFAULT_LEFTLEG_ROTATION);
        this.dataManager.register(EntityArmorStand.RIGHT_LEG_ROTATION, EntityArmorStand.DEFAULT_RIGHTLEG_ROTATION);
    }

    public Iterable<ItemStack> getHeldEquipment() {
        return this.handItems;
    }

    public Iterable<ItemStack> getArmorInventoryList() {
        return this.armorItems;
    }

    public ItemStack getItemStackFromSlot(EntityEquipmentSlot enumitemslot) {
        switch (enumitemslot.getSlotType()) {
        case HAND:
            return (ItemStack) this.handItems.get(enumitemslot.getIndex());

        case ARMOR:
            return (ItemStack) this.armorItems.get(enumitemslot.getIndex());

        default:
            return ItemStack.EMPTY;
        }
    }

    public void setItemStackToSlot(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        switch (enumitemslot.getSlotType()) {
        case HAND:
            this.playEquipSound(itemstack);
            this.handItems.set(enumitemslot.getIndex(), itemstack);
            break;

        case ARMOR:
            this.playEquipSound(itemstack);
            this.armorItems.set(enumitemslot.getIndex(), itemstack);
        }

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

        if (!itemstack.isEmpty() && !EntityLiving.isItemStackInSlot(enumitemslot, itemstack) && enumitemslot != EntityEquipmentSlot.HEAD) {
            return false;
        } else {
            this.setItemStackToSlot(enumitemslot, itemstack);
            return true;
        }
    }

    public static void registerFixesArmorStand(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackDataLists(EntityArmorStand.class, new String[] { "ArmorItems", "HandItems"})));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.armorItems.iterator(); iterator.hasNext(); nbttaglist.appendTag(nbttagcompound1)) {
            ItemStack itemstack = (ItemStack) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            if (!itemstack.isEmpty()) {
                itemstack.writeToNBT(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.handItems.iterator(); iterator1.hasNext(); nbttaglist1.appendTag(nbttagcompound2)) {
            ItemStack itemstack1 = (ItemStack) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            if (!itemstack1.isEmpty()) {
                itemstack1.writeToNBT(nbttagcompound2);
            }
        }

        nbttagcompound.setTag("HandItems", nbttaglist1);
        nbttagcompound.setBoolean("Invisible", this.isInvisible());
        nbttagcompound.setBoolean("Small", this.isSmall());
        nbttagcompound.setBoolean("ShowArms", this.getShowArms());
        nbttagcompound.setInteger("DisabledSlots", this.disabledSlots);
        nbttagcompound.setBoolean("NoBasePlate", this.hasNoBasePlate());
        if (this.hasMarker()) {
            nbttagcompound.setBoolean("Marker", this.hasMarker());
        }

        nbttagcompound.setTag("Pose", this.readPoseFromNBT());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.hasKey("ArmorItems", 9)) {
            nbttaglist = nbttagcompound.getTagList("ArmorItems", 10);

            for (i = 0; i < this.armorItems.size(); ++i) {
                this.armorItems.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (nbttagcompound.hasKey("HandItems", 9)) {
            nbttaglist = nbttagcompound.getTagList("HandItems", 10);

            for (i = 0; i < this.handItems.size(); ++i) {
                this.handItems.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }

        this.setInvisible(nbttagcompound.getBoolean("Invisible"));
        this.setSmall(nbttagcompound.getBoolean("Small"));
        this.setShowArms(nbttagcompound.getBoolean("ShowArms"));
        this.disabledSlots = nbttagcompound.getInteger("DisabledSlots");
        this.setNoBasePlate(nbttagcompound.getBoolean("NoBasePlate"));
        this.setMarker(nbttagcompound.getBoolean("Marker"));
        this.wasMarker = !this.hasMarker();
        this.noClip = this.hasNoGravity();
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Pose");

        this.writePoseToNBT(nbttagcompound1);
    }

    private void writePoseToNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getTagList("Head", 5);

        this.setHeadRotation(nbttaglist.hasNoTags() ? EntityArmorStand.DEFAULT_HEAD_ROTATION : new Rotations(nbttaglist));
        NBTTagList nbttaglist1 = nbttagcompound.getTagList("Body", 5);

        this.setBodyRotation(nbttaglist1.hasNoTags() ? EntityArmorStand.DEFAULT_BODY_ROTATION : new Rotations(nbttaglist1));
        NBTTagList nbttaglist2 = nbttagcompound.getTagList("LeftArm", 5);

        this.setLeftArmRotation(nbttaglist2.hasNoTags() ? EntityArmorStand.DEFAULT_LEFTARM_ROTATION : new Rotations(nbttaglist2));
        NBTTagList nbttaglist3 = nbttagcompound.getTagList("RightArm", 5);

        this.setRightArmRotation(nbttaglist3.hasNoTags() ? EntityArmorStand.DEFAULT_RIGHTARM_ROTATION : new Rotations(nbttaglist3));
        NBTTagList nbttaglist4 = nbttagcompound.getTagList("LeftLeg", 5);

        this.setLeftLegRotation(nbttaglist4.hasNoTags() ? EntityArmorStand.DEFAULT_LEFTLEG_ROTATION : new Rotations(nbttaglist4));
        NBTTagList nbttaglist5 = nbttagcompound.getTagList("RightLeg", 5);

        this.setRightLegRotation(nbttaglist5.hasNoTags() ? EntityArmorStand.DEFAULT_RIGHTLEG_ROTATION : new Rotations(nbttaglist5));
    }

    private NBTTagCompound readPoseFromNBT() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!EntityArmorStand.DEFAULT_HEAD_ROTATION.equals(this.headRotation)) {
            nbttagcompound.setTag("Head", this.headRotation.writeToNBT());
        }

        if (!EntityArmorStand.DEFAULT_BODY_ROTATION.equals(this.bodyRotation)) {
            nbttagcompound.setTag("Body", this.bodyRotation.writeToNBT());
        }

        if (!EntityArmorStand.DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation)) {
            nbttagcompound.setTag("LeftArm", this.leftArmRotation.writeToNBT());
        }

        if (!EntityArmorStand.DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation)) {
            nbttagcompound.setTag("RightArm", this.rightArmRotation.writeToNBT());
        }

        if (!EntityArmorStand.DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation)) {
            nbttagcompound.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
        }

        if (!EntityArmorStand.DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation)) {
            nbttagcompound.setTag("RightLeg", this.rightLegRotation.writeToNBT());
        }

        return nbttagcompound;
    }

    public boolean canBePushed() {
        return false;
    }

    protected void collideWithEntity(Entity entity) {}

    protected void collideWithNearbyEntities() {
        List list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntityArmorStand.IS_RIDEABLE_MINECART);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (this.getDistanceSq(entity) <= 0.2D) {
                entity.applyEntityCollision(this);
            }
        }

    }

    public EnumActionResult applyPlayerInteraction(EntityPlayer entityhuman, Vec3d vec3d, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!this.hasMarker() && itemstack.getItem() != Items.NAME_TAG) {
            if (!this.world.isRemote && !entityhuman.isSpectator()) {
                EntityEquipmentSlot enumitemslot = EntityLiving.getSlotForItemStack(itemstack);

                if (itemstack.isEmpty()) {
                    EntityEquipmentSlot enumitemslot1 = this.getClickedSlot(vec3d);
                    EntityEquipmentSlot enumitemslot2 = this.isDisabled(enumitemslot1) ? enumitemslot : enumitemslot1;

                    if (this.hasItemInSlot(enumitemslot2)) {
                        this.swapItem(entityhuman, enumitemslot2, itemstack, enumhand);
                    }
                } else {
                    if (this.isDisabled(enumitemslot)) {
                        return EnumActionResult.FAIL;
                    }

                    if (enumitemslot.getSlotType() == EntityEquipmentSlot.Type.HAND && !this.getShowArms()) {
                        return EnumActionResult.FAIL;
                    }

                    this.swapItem(entityhuman, enumitemslot, itemstack, enumhand);
                }

                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.SUCCESS;
            }
        } else {
            return EnumActionResult.PASS;
        }
    }

    protected EntityEquipmentSlot getClickedSlot(Vec3d vec3d) {
        EntityEquipmentSlot enumitemslot = EntityEquipmentSlot.MAINHAND;
        boolean flag = this.isSmall();
        double d0 = flag ? vec3d.y * 2.0D : vec3d.y;
        EntityEquipmentSlot enumitemslot1 = EntityEquipmentSlot.FEET;

        if (d0 >= 0.1D && d0 < 0.1D + (flag ? 0.8D : 0.45D) && this.hasItemInSlot(enumitemslot1)) {
            enumitemslot = EntityEquipmentSlot.FEET;
        } else if (d0 >= 0.9D + (flag ? 0.3D : 0.0D) && d0 < 0.9D + (flag ? 1.0D : 0.7D) && this.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            enumitemslot = EntityEquipmentSlot.CHEST;
        } else if (d0 >= 0.4D && d0 < 0.4D + (flag ? 1.0D : 0.8D) && this.hasItemInSlot(EntityEquipmentSlot.LEGS)) {
            enumitemslot = EntityEquipmentSlot.LEGS;
        } else if (d0 >= 1.6D && this.hasItemInSlot(EntityEquipmentSlot.HEAD)) {
            enumitemslot = EntityEquipmentSlot.HEAD;
        }

        return enumitemslot;
    }

    private boolean isDisabled(EntityEquipmentSlot enumitemslot) {
        return (this.disabledSlots & 1 << enumitemslot.getSlotIndex()) != 0;
    }

    private void swapItem(EntityPlayer entityhuman, EntityEquipmentSlot enumitemslot, ItemStack itemstack, EnumHand enumhand) {
        ItemStack itemstack1 = this.getItemStackFromSlot(enumitemslot);

        if (itemstack1.isEmpty() || (this.disabledSlots & 1 << enumitemslot.getSlotIndex() + 8) == 0) {
            if (!itemstack1.isEmpty() || (this.disabledSlots & 1 << enumitemslot.getSlotIndex() + 16) == 0) {
                ItemStack itemstack2;
                // CraftBukkit start
                org.bukkit.inventory.ItemStack armorStandItem = CraftItemStack.asCraftMirror(itemstack1);
                org.bukkit.inventory.ItemStack playerHeldItem = CraftItemStack.asCraftMirror(itemstack);

                Player player = (Player) entityhuman.getBukkitEntity();
                ArmorStand self = (ArmorStand) this.getBukkitEntity();

                EquipmentSlot slot = CraftEquipmentSlot.getSlot(enumitemslot);
                PlayerArmorStandManipulateEvent armorStandManipulateEvent = new PlayerArmorStandManipulateEvent(player,self,playerHeldItem,armorStandItem,slot);
                this.world.getServer().getPluginManager().callEvent(armorStandManipulateEvent);

                if (armorStandManipulateEvent.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                if (entityhuman.capabilities.isCreativeMode && itemstack1.isEmpty() && !itemstack.isEmpty()) {
                    itemstack2 = itemstack.copy();
                    itemstack2.setCount(1);
                    this.setItemStackToSlot(enumitemslot, itemstack2);
                } else if (!itemstack.isEmpty() && itemstack.getCount() > 1) {
                    if (itemstack1.isEmpty()) {
                        itemstack2 = itemstack.copy();
                        itemstack2.setCount(1);
                        this.setItemStackToSlot(enumitemslot, itemstack2);
                        itemstack.shrink(1);
                    }
                } else {
                    this.setItemStackToSlot(enumitemslot, itemstack);
                    entityhuman.setHeldItem(enumhand, itemstack1);
                }
            }
        }
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        // CraftBukkit start
        if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
            return false;
        }
        // CraftBukkit end
        if (!this.world.isRemote && !this.isDead) {
            if (DamageSource.OUT_OF_WORLD.equals(damagesource)) {
                this.onKillCommand(); // CraftBukkit - this.die() -> this.killEntity()
                return false;
            } else if (!this.isEntityInvulnerable(damagesource) && !this.canInteract && !this.hasMarker()) {
                if (damagesource.isExplosion()) {
                    this.dropContents();
                    this.onKillCommand(); // CraftBukkit - this.die() -> this.killEntity()
                    return false;
                } else if (DamageSource.IN_FIRE.equals(damagesource)) {
                    if (this.isBurning()) {
                        this.damageArmorStand(0.15F);
                    } else {
                        this.setFire(5);
                    }

                    return false;
                } else if (DamageSource.ON_FIRE.equals(damagesource) && this.getHealth() > 0.5F) {
                    this.damageArmorStand(4.0F);
                    return false;
                } else {
                    boolean flag = "arrow".equals(damagesource.getDamageType());
                    boolean flag1 = "player".equals(damagesource.getDamageType());

                    if (!flag1 && !flag) {
                        return false;
                    } else {
                        if (damagesource.getImmediateSource() instanceof EntityArrow) {
                            damagesource.getImmediateSource().setDead();
                        }

                        if (damagesource.getTrueSource() instanceof EntityPlayer && !((EntityPlayer) damagesource.getTrueSource()).capabilities.allowEdit) {
                            return false;
                        } else if (damagesource.isCreativePlayer()) {
                            this.playBrokenSound();
                            this.playParticles();
                            this.onKillCommand(); // CraftBukkit - this.die() -> this.killEntity()
                            return false;
                        } else {
                            long i = this.world.getTotalWorldTime();

                            if (i - this.punchCooldown > 5L && !flag) {
                                this.world.setEntityState(this, (byte) 32);
                                this.punchCooldown = i;
                            } else {
                                this.dropBlock();
                                this.playParticles();
                                this.onKillCommand(); // CraftBukkit - this.die() -> this.killEntity()
                            }

                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void playParticles() {
        if (this.world instanceof WorldServer) {
            ((WorldServer) this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double) this.height / 1.5D, this.posZ, 10, (double) (this.width / 4.0F), (double) (this.height / 4.0F), (double) (this.width / 4.0F), 0.05D, new int[] { Block.getStateId(Blocks.PLANKS.getDefaultState())});
        }

    }

    private void damageArmorStand(float f) {
        float f1 = this.getHealth();

        f1 -= f;
        if (f1 <= 0.5F) {
            this.dropContents();
            this.onKillCommand(); // CraftBukkit - this.die() -> this.killEntity()
        } else {
            this.setHealth(f1);
        }

    }

    private void dropBlock() {
        drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(new ItemStack(Items.ARMOR_STAND))); // CraftBukkit - add to drops
        this.dropContents();
    }

    private void dropContents() {
        this.playBrokenSound();

        int i;
        ItemStack itemstack;

        for (i = 0; i < this.handItems.size(); ++i) {
            itemstack = (ItemStack) this.handItems.get(i);
            if (!itemstack.isEmpty()) {
                drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(itemstack)); // CraftBukkit - add to drops
                this.handItems.set(i, ItemStack.EMPTY);
            }
        }

        for (i = 0; i < this.armorItems.size(); ++i) {
            itemstack = (ItemStack) this.armorItems.get(i);
            if (!itemstack.isEmpty()) {
                drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(itemstack)); // CraftBukkit - add to drops
                this.armorItems.set(i, ItemStack.EMPTY);
            }
        }

    }

    private void playBrokenSound() {
        this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ARMORSTAND_BREAK, this.getSoundCategory(), 1.0F, 1.0F);
    }

    protected float updateDistance(float f, float f1) {
        this.prevRenderYawOffset = this.prevRotationYaw;
        this.renderYawOffset = this.rotationYaw;
        return 0.0F;
    }

    public float getEyeHeight() {
        return this.isChild() ? this.height * 0.5F : this.height * 0.9F;
    }

    public double getYOffset() {
        return this.hasMarker() ? 0.0D : 0.10000000149011612D;
    }

    public void travel(float f, float f1, float f2) {
        if (!this.hasNoGravity()) {
            super.travel(f, f1, f2);
        }
    }

    public void setRenderYawOffset(float f) {
        this.prevRenderYawOffset = this.prevRotationYaw = f;
        this.prevRotationYawHead = this.rotationYawHead = f;
    }

    public void setRotationYawHead(float f) {
        this.prevRenderYawOffset = this.prevRotationYaw = f;
        this.prevRotationYawHead = this.rotationYawHead = f;
    }

    public void onUpdate() {
        super.onUpdate();
        Rotations vector3f = (Rotations) this.dataManager.get(EntityArmorStand.HEAD_ROTATION);

        if (!this.headRotation.equals(vector3f)) {
            this.setHeadRotation(vector3f);
        }

        Rotations vector3f1 = (Rotations) this.dataManager.get(EntityArmorStand.BODY_ROTATION);

        if (!this.bodyRotation.equals(vector3f1)) {
            this.setBodyRotation(vector3f1);
        }

        Rotations vector3f2 = (Rotations) this.dataManager.get(EntityArmorStand.LEFT_ARM_ROTATION);

        if (!this.leftArmRotation.equals(vector3f2)) {
            this.setLeftArmRotation(vector3f2);
        }

        Rotations vector3f3 = (Rotations) this.dataManager.get(EntityArmorStand.RIGHT_ARM_ROTATION);

        if (!this.rightArmRotation.equals(vector3f3)) {
            this.setRightArmRotation(vector3f3);
        }

        Rotations vector3f4 = (Rotations) this.dataManager.get(EntityArmorStand.LEFT_LEG_ROTATION);

        if (!this.leftLegRotation.equals(vector3f4)) {
            this.setLeftLegRotation(vector3f4);
        }

        Rotations vector3f5 = (Rotations) this.dataManager.get(EntityArmorStand.RIGHT_LEG_ROTATION);

        if (!this.rightLegRotation.equals(vector3f5)) {
            this.setRightLegRotation(vector3f5);
        }

        boolean flag = this.hasMarker();

        if (this.wasMarker != flag) {
            this.updateBoundingBox(flag);
            this.preventEntitySpawning = !flag;
            this.wasMarker = flag;
        }

    }

    private void updateBoundingBox(boolean flag) {
        if (flag) {
            this.setSize(0.0F, 0.0F);
        } else {
            this.setSize(0.5F, 1.975F);
        }

    }

    protected void updatePotionMetadata() {
        this.setInvisible(this.canInteract);
    }

    public void setInvisible(boolean flag) {
        this.canInteract = flag;
        super.setInvisible(flag);
    }

    public boolean isChild() {
        return this.isSmall();
    }

    public void onKillCommand() {
        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, drops); // CraftBukkit - call event
        this.setDead();
    }

    public boolean isImmuneToExplosions() {
        return this.isInvisible();
    }

    public EnumPushReaction getPushReaction() {
        return this.hasMarker() ? EnumPushReaction.IGNORE : super.getPushReaction();
    }

    public void setSmall(boolean flag) {
        this.dataManager.set(EntityArmorStand.STATUS, Byte.valueOf(this.setBit(((Byte) this.dataManager.get(EntityArmorStand.STATUS)).byteValue(), 1, flag)));
        this.setSize(0.5F, 1.975F);
    }

    public boolean isSmall() {
        return (((Byte) this.dataManager.get(EntityArmorStand.STATUS)).byteValue() & 1) != 0;
    }

    public void setShowArms(boolean flag) {
        this.dataManager.set(EntityArmorStand.STATUS, Byte.valueOf(this.setBit(((Byte) this.dataManager.get(EntityArmorStand.STATUS)).byteValue(), 4, flag)));
    }

    public boolean getShowArms() {
        return (((Byte) this.dataManager.get(EntityArmorStand.STATUS)).byteValue() & 4) != 0;
    }

    public void setNoBasePlate(boolean flag) {
        this.dataManager.set(EntityArmorStand.STATUS, Byte.valueOf(this.setBit(((Byte) this.dataManager.get(EntityArmorStand.STATUS)).byteValue(), 8, flag)));
    }

    public boolean hasNoBasePlate() {
        return (((Byte) this.dataManager.get(EntityArmorStand.STATUS)).byteValue() & 8) != 0;
    }

    public void setMarker(boolean flag) {
        this.dataManager.set(EntityArmorStand.STATUS, Byte.valueOf(this.setBit(((Byte) this.dataManager.get(EntityArmorStand.STATUS)).byteValue(), 16, flag)));
        this.setSize(0.5F, 1.975F);
    }

    public boolean hasMarker() {
        return (((Byte) this.dataManager.get(EntityArmorStand.STATUS)).byteValue() & 16) != 0;
    }

    private byte setBit(byte b0, int i, boolean flag) {
        if (flag) {
            b0 = (byte) (b0 | i);
        } else {
            b0 = (byte) (b0 & ~i);
        }

        return b0;
    }

    public void setHeadRotation(Rotations vector3f) {
        this.headRotation = vector3f;
        this.dataManager.set(EntityArmorStand.HEAD_ROTATION, vector3f);
    }

    public void setBodyRotation(Rotations vector3f) {
        this.bodyRotation = vector3f;
        this.dataManager.set(EntityArmorStand.BODY_ROTATION, vector3f);
    }

    public void setLeftArmRotation(Rotations vector3f) {
        this.leftArmRotation = vector3f;
        this.dataManager.set(EntityArmorStand.LEFT_ARM_ROTATION, vector3f);
    }

    public void setRightArmRotation(Rotations vector3f) {
        this.rightArmRotation = vector3f;
        this.dataManager.set(EntityArmorStand.RIGHT_ARM_ROTATION, vector3f);
    }

    public void setLeftLegRotation(Rotations vector3f) {
        this.leftLegRotation = vector3f;
        this.dataManager.set(EntityArmorStand.LEFT_LEG_ROTATION, vector3f);
    }

    public void setRightLegRotation(Rotations vector3f) {
        this.rightLegRotation = vector3f;
        this.dataManager.set(EntityArmorStand.RIGHT_LEG_ROTATION, vector3f);
    }

    public Rotations getHeadRotation() {
        return this.headRotation;
    }

    public Rotations getBodyRotation() {
        return this.bodyRotation;
    }

    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith() && !this.hasMarker();
    }

    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    protected SoundEvent getFallSound(int i) {
        return SoundEvents.ENTITY_ARMORSTAND_FALL;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_ARMORSTAND_HIT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ARMORSTAND_BREAK;
    }

    public void onStruckByLightning(EntityLightningBolt entitylightning) {}

    public boolean canBeHitWithPotion() {
        return false;
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (EntityArmorStand.STATUS.equals(datawatcherobject)) {
            this.setSize(0.5F, 1.975F);
        }

        super.notifyDataManagerChange(datawatcherobject);
    }

    public boolean attackable() {
        return false;
    }

    // Paper start
    @Override
    public void move(MoverType moveType, double x, double y, double z) {
        if (this.canMove) {
            super.move(moveType, x, y, z);
        }
    }

    @Override
    public boolean canBreatheUnderwater() { // Skips a bit of damage handling code, probably a micro-optimization
        return true;
    }
    // Paper end
}
