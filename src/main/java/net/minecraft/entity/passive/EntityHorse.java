package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.EntityHorse.a;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityHorse extends AbstractHorse {

    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
    private static final DataParameter<Integer> HORSE_VARIANT = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> HORSE_ARMOR = EntityDataManager.createKey(EntityHorse.class, DataSerializers.VARINT);
    private static final String[] HORSE_TEXTURES = new String[] { "textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
    private static final String[] HORSE_TEXTURES_ABBR = new String[] { "hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
    private static final String[] HORSE_MARKING_TEXTURES = new String[] { null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
    private static final String[] HORSE_MARKING_TEXTURES_ABBR = new String[] { "", "wo_", "wmo", "wdo", "bdo"};
    private String texturePrefix;
    private final String[] horseTexturesArray = new String[3];

    public EntityHorse(World world) {
        super(world);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityHorse.HORSE_VARIANT, Integer.valueOf(0));
        this.dataManager.register(EntityHorse.HORSE_ARMOR, Integer.valueOf(HorseArmorType.NONE.getOrdinal()));
    }

    public static void registerFixesHorse(DataFixer dataconvertermanager) {
        AbstractHorse.registerFixesAbstractHorse(dataconvertermanager, EntityHorse.class);
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityHorse.class, new String[] { "ArmorItem"})));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Variant", this.getHorseVariant());
        if (!this.horseChest.getStackInSlot(1).isEmpty()) {
            nbttagcompound.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setHorseVariant(nbttagcompound.getInteger("Variant"));
        if (nbttagcompound.hasKey("ArmorItem", 10)) {
            ItemStack itemstack = new ItemStack(nbttagcompound.getCompoundTag("ArmorItem"));

            if (!itemstack.isEmpty() && HorseArmorType.isHorseArmor(itemstack.getItem())) {
                this.horseChest.setInventorySlotContents(1, itemstack);
            }
        }

        this.updateHorseSlots();
    }

    public void setHorseVariant(int i) {
        this.dataManager.set(EntityHorse.HORSE_VARIANT, Integer.valueOf(i));
        this.resetTexturePrefix();
    }

    public int getHorseVariant() {
        return ((Integer) this.dataManager.get(EntityHorse.HORSE_VARIANT)).intValue();
    }

    private void resetTexturePrefix() {
        this.texturePrefix = null;
    }

    protected void updateHorseSlots() {
        super.updateHorseSlots();
        this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
    }

    public void setHorseArmorStack(ItemStack itemstack) {
        HorseArmorType enumhorsearmor = HorseArmorType.getByItemStack(itemstack);

        this.dataManager.set(EntityHorse.HORSE_ARMOR, Integer.valueOf(enumhorsearmor.getOrdinal()));
        this.resetTexturePrefix();
        if (!this.world.isRemote) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(EntityHorse.ARMOR_MODIFIER_UUID);
            int i = enumhorsearmor.getProtection();

            if (i != 0) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier((new AttributeModifier(EntityHorse.ARMOR_MODIFIER_UUID, "Horse armor bonus", (double) i, 0)).setSaved(false));
            }
        }

    }

    public HorseArmorType getHorseArmorType() {
        return HorseArmorType.getByOrdinal(((Integer) this.dataManager.get(EntityHorse.HORSE_ARMOR)).intValue());
    }

    public void onInventoryChanged(IInventory iinventory) {
        HorseArmorType enumhorsearmor = this.getHorseArmorType();

        super.onInventoryChanged(iinventory);
        HorseArmorType enumhorsearmor1 = this.getHorseArmorType();

        if (this.ticksExisted > 20 && enumhorsearmor != enumhorsearmor1 && enumhorsearmor1 != HorseArmorType.NONE) {
            this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
        }

    }

    protected void playGallopSound(SoundType soundeffecttype) {
        super.playGallopSound(soundeffecttype);
        if (this.rand.nextInt(10) == 0) {
            this.playSound(SoundEvents.ENTITY_HORSE_BREATHE, soundeffecttype.getVolume() * 0.6F, soundeffecttype.getPitch());
        }

    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) this.getModifiedMaxHealth());
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getModifiedMovementSpeed());
        this.getEntityAttribute(EntityHorse.JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.dataManager.isDirty()) {
            this.dataManager.setClean();
            this.resetTexturePrefix();
        }

    }

    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_HORSE_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        super.getHurtSound(damagesource);
        return SoundEvents.ENTITY_HORSE_HURT;
    }

    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.ENTITY_HORSE_ANGRY;
    }

    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_HORSE;
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        boolean flag = !itemstack.isEmpty();

        if (flag && itemstack.getItem() == Items.SPAWN_EGG) {
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

            if (flag) {
                if (this.handleEating(entityhuman, itemstack)) {
                    if (!entityhuman.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }

                    return true;
                }

                if (itemstack.interactWithEntity(entityhuman, (EntityLivingBase) this, enumhand)) {
                    return true;
                }

                if (!this.isTame()) {
                    this.makeMad();
                    return true;
                }

                boolean flag1 = HorseArmorType.getByItemStack(itemstack) != HorseArmorType.NONE;
                boolean flag2 = !this.isChild() && !this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE;

                if (flag1 || flag2) {
                    this.openGUI(entityhuman);
                    return true;
                }
            }

            if (this.isChild()) {
                return super.processInteract(entityhuman, enumhand);
            } else {
                this.mountTo(entityhuman);
                return true;
            }
        }
    }

    public boolean canMateWith(EntityAnimal entityanimal) {
        return entityanimal == this ? false : (!(entityanimal instanceof EntityDonkey) && !(entityanimal instanceof EntityHorse) ? false : this.canMate() && ((AbstractHorse) entityanimal).canMate());
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        Object object;

        if (entityageable instanceof EntityDonkey) {
            object = new EntityMule(this.world);
        } else {
            EntityHorse entityhorse = (EntityHorse) entityageable;

            object = new EntityHorse(this.world);
            int i = this.rand.nextInt(9);
            int j;

            if (i < 4) {
                j = this.getHorseVariant() & 255;
            } else if (i < 8) {
                j = entityhorse.getHorseVariant() & 255;
            } else {
                j = this.rand.nextInt(7);
            }

            int k = this.rand.nextInt(5);

            if (k < 2) {
                j |= this.getHorseVariant() & '\uff00';
            } else if (k < 4) {
                j |= entityhorse.getHorseVariant() & '\uff00';
            } else {
                j |= this.rand.nextInt(5) << 8 & '\uff00';
            }

            ((EntityHorse) object).setHorseVariant(j);
        }

        this.setOffspringAttributes(entityageable, (AbstractHorse) object);
        return (EntityAgeable) object;
    }

    public boolean wearsArmor() {
        return true;
    }

    public boolean isArmor(ItemStack itemstack) {
        return HorseArmorType.isHorseArmor(itemstack.getItem());
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        Object object = super.onInitialSpawn(difficultydamagescaler, groupdataentity);
        int i;

        if (object instanceof EntityHorse.a) {
            i = ((EntityHorse.a) object).a;
        } else {
            i = this.rand.nextInt(7);
            object = new EntityHorse.a(i);
        }

        this.setHorseVariant(i | this.rand.nextInt(5) << 8);
        return (IEntityLivingData) object;
    }

    public static class a implements IEntityLivingData {

        public int a;

        public a(int i) {
            this.a = i;
        }
    }
}
