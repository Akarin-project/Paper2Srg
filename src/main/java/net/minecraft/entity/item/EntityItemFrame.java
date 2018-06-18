package net.minecraft.entity.item;

// Spigot start
import java.util.UUID;
import org.apache.commons.codec.Charsets;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

// Spigot end
import javax.annotation.Nullable;

public class EntityItemFrame extends EntityHanging {

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Integer> ROTATION = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.VARINT);
    private float itemDropChance = 1.0F;

    public EntityItemFrame(World world) {
        super(world);
    }

    public EntityItemFrame(World world, BlockPos blockposition, EnumFacing enumdirection) {
        super(world, blockposition);
        this.updateFacingWithBoundingBox(enumdirection);
    }

    protected void entityInit() {
        this.getDataManager().register(EntityItemFrame.ITEM, ItemStack.EMPTY);
        this.getDataManager().register(EntityItemFrame.ROTATION, Integer.valueOf(0));
    }

    public float getCollisionBorderSize() {
        return 0.0F;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (!damagesource.isExplosion() && !this.getDisplayedItem().isEmpty()) {
            if (!this.world.isRemote) {
                // CraftBukkit start - fire EntityDamageEvent
                if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f, false) || this.isDead) {
                    return true;
                }
                // CraftBukkit end
                this.dropItemOrSelf(damagesource.getTrueSource(), false);
                this.playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0F, 1.0F);
                this.setDisplayedItem(ItemStack.EMPTY);
            }

            return true;
        } else {
            return super.attackEntityFrom(damagesource, f);
        }
    }

    public int getWidthPixels() {
        return 12;
    }

    public int getHeightPixels() {
        return 12;
    }

    public void onBroken(@Nullable Entity entity) {
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_BREAK, 1.0F, 1.0F);
        this.dropItemOrSelf(entity, true);
    }

    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_PLACE, 1.0F, 1.0F);
    }

    public void dropItemOrSelf(@Nullable Entity entity, boolean flag) {
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = this.getDisplayedItem();

            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;

                if (entityhuman.capabilities.isCreativeMode) {
                    this.removeFrameFromMap(itemstack);
                    return;
                }
            }

            if (flag) {
                this.entityDropItem(new ItemStack(Items.ITEM_FRAME), 0.0F);
            }

            if (!itemstack.isEmpty() && this.rand.nextFloat() < this.itemDropChance) {
                itemstack = itemstack.copy();
                this.removeFrameFromMap(itemstack);
                this.entityDropItem(itemstack, 0.0F);
            }

        }
    }

    private void removeFrameFromMap(ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            if (itemstack.getItem() == Items.FILLED_MAP) {
                MapData worldmap = ((ItemMap) itemstack.getItem()).getMapData(itemstack, this.world);

                worldmap.mapDecorations.remove(UUID.nameUUIDFromBytes(("frame-" + this.getEntityId()).getBytes(Charsets.US_ASCII))); // Spigot
            }

            itemstack.setItemFrame((EntityItemFrame) null);

            // Paper - MC-124833 - conflicting reports of what server software this does and doesn't affect.
            // It's a one liner with near-zero impact so we'll patch it anyway just in case
            this.setDisplayedItem(ItemStack.EMPTY); // OBFHELPER - ItemStack.EMPTY
        }
    }

    public ItemStack getDisplayedItem() {
        return (ItemStack) this.getDataManager().get(EntityItemFrame.ITEM);
    }

    public void setDisplayedItem(ItemStack itemstack) {
        this.setDisplayedItemWithUpdate(itemstack, true);
    }

    private void setDisplayedItemWithUpdate(ItemStack itemstack, boolean flag) {
        if (!itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(1);
            itemstack.setItemFrame(this);
        }

        this.getDataManager().set(EntityItemFrame.ITEM, itemstack);
        this.getDataManager().setDirty(EntityItemFrame.ITEM);
        if (!itemstack.isEmpty()) {
            this.playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0F, 1.0F);
        }

        if (flag && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }

    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (datawatcherobject.equals(EntityItemFrame.ITEM)) {
            ItemStack itemstack = this.getDisplayedItem();

            if (!itemstack.isEmpty() && itemstack.getItemFrame() != this) {
                itemstack.setItemFrame(this);
            }
        }

    }

    public int getRotation() {
        return ((Integer) this.getDataManager().get(EntityItemFrame.ROTATION)).intValue();
    }

    public void setItemRotation(int i) {
        this.setRotation(i, true);
    }

    private void setRotation(int i, boolean flag) {
        this.getDataManager().set(EntityItemFrame.ROTATION, Integer.valueOf(i % 8));
        if (flag && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }

    }

    public static void registerFixesItemFrame(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityItemFrame.class, new String[] { "Item"})));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        if (!this.getDisplayedItem().isEmpty()) {
            nbttagcompound.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
            nbttagcompound.setByte("ItemRotation", (byte) this.getRotation());
            nbttagcompound.setFloat("ItemDropChance", this.itemDropChance);
        }

        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");

        if (nbttagcompound1 != null && !nbttagcompound1.hasNoTags()) {
            this.setDisplayedItemWithUpdate(new ItemStack(nbttagcompound1), false);
            this.setRotation(nbttagcompound.getByte("ItemRotation"), false);
            if (nbttagcompound.hasKey("ItemDropChance", 99)) {
                this.itemDropChance = nbttagcompound.getFloat("ItemDropChance");
            }
        }

        super.readEntityFromNBT(nbttagcompound);
    }

    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!this.world.isRemote) {
            if (this.getDisplayedItem().isEmpty()) {
                if (!itemstack.isEmpty()) {
                    this.setDisplayedItem(itemstack);
                    if (!entityhuman.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                }
            } else {
                this.playSound(SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM, 1.0F, 1.0F);
                this.setItemRotation(this.getRotation() + 1);
            }
        }

        return true;
    }

    public int getAnalogOutput() {
        return this.getDisplayedItem().isEmpty() ? 0 : this.getRotation() % 8 + 1;
    }
}
