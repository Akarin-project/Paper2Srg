package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.InventoryView;

// CraftBukkit start
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.InventoryView;
// CraftBukkit end

public class EntitySheep extends EntityAnimal {

    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);
    private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container() {
        public boolean canInteractWith(EntityPlayer entityhuman) {
            return false;
        }

        // CraftBukkit start
        @Override
        public InventoryView getBukkitView() {
            return null; // TODO: O.O
        }
        // CraftBukkit end
    }, 2, 1);
    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass;

    private static float[] createSheepColor(EnumDyeColor enumcolor) {
        float[] afloat = enumcolor.getColorComponentValues();
        float f = 0.75F;

        return new float[] { afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
    }

    public EntitySheep(World world) {
        super(world);
        this.setSize(0.9F, 1.3F);
        this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.DYE));
        this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.DYE));
        this.inventoryCrafting.resultInventory = new InventoryCraftResult(); // CraftBukkit - add result slot for event
    }

    protected void initEntityAI() {
        this.entityAIEatGrass = new EntityAIEatGrass(this);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.WHEAT, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    protected void updateAITasks() {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.onLivingUpdate();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntitySheep.DYE_COLOR, Byte.valueOf((byte) 0));
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        if (this.getSheared()) {
            return LootTableList.ENTITIES_SHEEP;
        } else {
            switch (this.getFleeceColor()) {
            case WHITE:
            default:
                return LootTableList.ENTITIES_SHEEP_WHITE;

            case ORANGE:
                return LootTableList.ENTITIES_SHEEP_ORANGE;

            case MAGENTA:
                return LootTableList.ENTITIES_SHEEP_MAGENTA;

            case LIGHT_BLUE:
                return LootTableList.ENTITIES_SHEEP_LIGHT_BLUE;

            case YELLOW:
                return LootTableList.ENTITIES_SHEEP_YELLOW;

            case LIME:
                return LootTableList.ENTITIES_SHEEP_LIME;

            case PINK:
                return LootTableList.ENTITIES_SHEEP_PINK;

            case GRAY:
                return LootTableList.ENTITIES_SHEEP_GRAY;

            case SILVER:
                return LootTableList.ENTITIES_SHEEP_SILVER;

            case CYAN:
                return LootTableList.ENTITIES_SHEEP_CYAN;

            case PURPLE:
                return LootTableList.ENTITIES_SHEEP_PURPLE;

            case BLUE:
                return LootTableList.ENTITIES_SHEEP_BLUE;

            case BROWN:
                return LootTableList.ENTITIES_SHEEP_BROWN;

            case GREEN:
                return LootTableList.ENTITIES_SHEEP_GREEN;

            case RED:
                return LootTableList.ENTITIES_SHEEP_RED;

            case BLACK:
                return LootTableList.ENTITIES_SHEEP_BLACK;
            }
        }
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (itemstack.getItem() == Items.SHEARS && !this.getSheared() && !this.isChild()) {
            if (!this.world.isRemote) {
                // CraftBukkit start
                PlayerShearEntityEvent event = new PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity());
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }
                // CraftBukkit end

                this.setSheared(true);
                int i = 1 + this.rand.nextInt(3);

                for (int j = 0; j < i; ++j) {
                    this.forceDrops = true; // CraftBukkit
                    EntityItem entityitem = this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, this.getFleeceColor().getMetadata()), 1.0F);
                    this.forceDrops = false; // CraftBukkit

                    entityitem.motionY += (double) (this.rand.nextFloat() * 0.05F);
                    entityitem.motionX += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                    entityitem.motionZ += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                }
            }

            itemstack.damageItem(1, entityhuman);
            this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        }

        return super.processInteract(entityhuman, enumhand);
    }

    public static void registerFixesSheep(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntitySheep.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Sheared", this.getSheared());
        nbttagcompound.setByte("Color", (byte) this.getFleeceColor().getMetadata());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setSheared(nbttagcompound.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(nbttagcompound.getByte("Color")));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    protected void playStepSound(BlockPos blockposition, Block block) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    public EnumDyeColor getFleeceColor() {
        return EnumDyeColor.byMetadata(((Byte) this.dataManager.get(EntitySheep.DYE_COLOR)).byteValue() & 15);
    }

    public void setFleeceColor(EnumDyeColor enumcolor) {
        byte b0 = ((Byte) this.dataManager.get(EntitySheep.DYE_COLOR)).byteValue();

        this.dataManager.set(EntitySheep.DYE_COLOR, Byte.valueOf((byte) (b0 & 240 | enumcolor.getMetadata() & 15)));
    }

    public boolean getSheared() {
        return (((Byte) this.dataManager.get(EntitySheep.DYE_COLOR)).byteValue() & 16) != 0;
    }

    public void setSheared(boolean flag) {
        byte b0 = ((Byte) this.dataManager.get(EntitySheep.DYE_COLOR)).byteValue();

        if (flag) {
            this.dataManager.set(EntitySheep.DYE_COLOR, Byte.valueOf((byte) (b0 | 16)));
        } else {
            this.dataManager.set(EntitySheep.DYE_COLOR, Byte.valueOf((byte) (b0 & -17)));
        }

    }

    public static EnumDyeColor getRandomSheepColor(Random random) {
        int i = random.nextInt(100);

        return i < 5 ? EnumDyeColor.BLACK : (i < 10 ? EnumDyeColor.GRAY : (i < 15 ? EnumDyeColor.SILVER : (i < 18 ? EnumDyeColor.BROWN : (random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
    }

    public EntitySheep createChild(EntityAgeable entityageable) {
        EntitySheep entitysheep = (EntitySheep) entityageable;
        EntitySheep entitysheep1 = new EntitySheep(this.world);

        entitysheep1.setFleeceColor(this.getDyeColorMixFromParents((EntityAnimal) this, (EntityAnimal) entitysheep));
        return entitysheep1;
    }

    public void eatGrassBonus() {
        // CraftBukkit start
        SheepRegrowWoolEvent event = new SheepRegrowWoolEvent((org.bukkit.entity.Sheep) this.getBukkitEntity());
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        // CraftBukkit end
        this.setSheared(false);
        if (this.isChild()) {
            this.addGrowth(60);
        }

    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        groupdataentity = super.onInitialSpawn(difficultydamagescaler, groupdataentity);
        this.setFleeceColor(getRandomSheepColor(this.world.rand));
        return groupdataentity;
    }

    private EnumDyeColor getDyeColorMixFromParents(EntityAnimal entityanimal, EntityAnimal entityanimal1) {
        int i = ((EntitySheep) entityanimal).getFleeceColor().getDyeDamage();
        int j = ((EntitySheep) entityanimal1).getFleeceColor().getDyeDamage();

        this.inventoryCrafting.getStackInSlot(0).setItemDamage(i);
        this.inventoryCrafting.getStackInSlot(1).setItemDamage(j);
        ItemStack itemstack = CraftingManager.findMatchingResult(this.inventoryCrafting, ((EntitySheep) entityanimal).world);
        int k;

        if (itemstack.getItem() == Items.DYE) {
            k = itemstack.getMetadata();
        } else {
            k = this.world.rand.nextBoolean() ? i : j;
        }

        return EnumDyeColor.byDyeDamage(k);
    }

    public float getEyeHeight() {
        return 0.95F * this.height;
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.createChild(entityageable);
    }

    static {
        EnumDyeColor[] aenumcolor = EnumDyeColor.values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumDyeColor enumcolor = aenumcolor[j];

            EntitySheep.DYE_TO_RGB.put(enumcolor, createSheepColor(enumcolor));
        }

        EntitySheep.DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] { 0.9019608F, 0.9019608F, 0.9019608F});
    }
}
