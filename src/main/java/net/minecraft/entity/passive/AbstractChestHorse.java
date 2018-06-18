package net.minecraft.entity.passive;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.World;


public abstract class AbstractChestHorse extends AbstractHorse {

    private static final DataParameter<Boolean> DATA_ID_CHEST = EntityDataManager.createKey(AbstractChestHorse.class, DataSerializers.BOOLEAN);

    public AbstractChestHorse(World world) {
        super(world);
        this.canGallop = false;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractChestHorse.DATA_ID_CHEST, Boolean.valueOf(false));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) this.getModifiedMaxHealth());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.17499999701976776D);
        this.getEntityAttribute(AbstractChestHorse.JUMP_STRENGTH).setBaseValue(0.5D);
    }

    public boolean hasChest() {
        return ((Boolean) this.dataManager.get(AbstractChestHorse.DATA_ID_CHEST)).booleanValue();
    }

    public void setChested(boolean flag) {
        this.dataManager.set(AbstractChestHorse.DATA_ID_CHEST, Boolean.valueOf(flag));
    }

    protected int getInventorySize() {
        return this.hasChest() ? 17 : super.getInventorySize();
    }

    public double getMountedYOffset() {
        return super.getMountedYOffset() - 0.25D;
    }

    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.ENTITY_DONKEY_ANGRY;
    }

    public void onDeath(DamageSource damagesource) {
        // super.die(damagesource); // CraftBukkit - moved down
        if (this.hasChest()) {
            if (!this.world.isRemote) {
                this.dropItem(Item.getItemFromBlock(Blocks.CHEST), 1);
            }

            // this.setCarryingChest(false); // CraftBukkit - moved down
        }
        // CraftBukkit start
        super.onDeath(damagesource);
        this.setChested(false);
        // CraftBukkit end

    }

    public static void registerFixesAbstractChestHorse(DataFixer dataconvertermanager, Class<?> oclass) {
        AbstractHorse.registerFixesAbstractHorse(dataconvertermanager, oclass);
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackDataLists(oclass, new String[] { "Items"})));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("ChestedHorse", this.hasChest());
        if (this.hasChest()) {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 2; i < this.horseChest.getSizeInventory(); ++i) {
                ItemStack itemstack = this.horseChest.getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.setByte("Slot", (byte) i);
                    itemstack.writeToNBT(nbttagcompound1);
                    nbttaglist.appendTag(nbttagcompound1);
                }
            }

            nbttagcompound.setTag("Items", nbttaglist);
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setChested(nbttagcompound.getBoolean("ChestedHorse"));
        if (this.hasChest()) {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);

            this.initHorseChest();

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound1.getByte("Slot") & 255;

                if (j >= 2 && j < this.horseChest.getSizeInventory()) {
                    this.horseChest.setInventorySlotContents(j, new ItemStack(nbttagcompound1));
                }
            }
        }

        this.updateHorseSlots();
    }

    public boolean replaceItemInInventory(int i, ItemStack itemstack) {
        if (i == 499) {
            if (this.hasChest() && itemstack.isEmpty()) {
                this.setChested(false);
                this.initHorseChest();
                return true;
            }

            if (!this.hasChest() && itemstack.getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
                this.setChested(true);
                this.initHorseChest();
                return true;
            }
        }

        return super.replaceItemInInventory(i, itemstack);
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (itemstack.getItem() == Items.SPAWN_EGG) {
            return super.processInteract(entityhuman, enumhand);
        } else {
            if (!this.isChild()) {
                if (this.isTame() && entityhuman.isSneaking()) {
                    this.openGUI(entityhuman);
                    return true;
                }

                if (this.isBeingRidden()) {
                    return super.processInteract(entityhuman, enumhand);
                }
            }

            if (!itemstack.isEmpty()) {
                boolean flag = this.handleEating(entityhuman, itemstack);

                if (!flag && !this.isTame()) {
                    if (itemstack.interactWithEntity(entityhuman, (EntityLivingBase) this, enumhand)) {
                        return true;
                    }

                    this.makeMad();
                    return true;
                }

                if (!flag && !this.hasChest() && itemstack.getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
                    this.setChested(true);
                    this.playChestEquipSound();
                    flag = true;
                    this.initHorseChest();
                }

                if (!flag && !this.isChild() && !this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE) {
                    this.openGUI(entityhuman);
                    return true;
                }

                if (flag) {
                    if (!entityhuman.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }

                    return true;
                }
            }

            if (this.isChild()) {
                return super.processInteract(entityhuman, enumhand);
            } else if (itemstack.interactWithEntity(entityhuman, (EntityLivingBase) this, enumhand)) {
                return true;
            } else {
                this.mountTo(entityhuman);
                return true;
            }
        }
    }

    protected void playChestEquipSound() {
        this.playSound(SoundEvents.ENTITY_DONKEY_CHEST, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    public int getInventoryColumns() {
        return 5;
    }
}
