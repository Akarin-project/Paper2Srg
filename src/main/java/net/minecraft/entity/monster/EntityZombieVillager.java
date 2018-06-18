package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieVillager extends EntityZombie {

    private static final DataParameter<Boolean> CONVERTING = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.VARINT);
    private int conversionTime;
    private UUID converstionStarter;
    private int lastTick = MinecraftServer.currentTick; // CraftBukkit - add field

    public EntityZombieVillager(World world) {
        super(world);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityZombieVillager.CONVERTING, Boolean.valueOf(false));
        this.dataManager.register(EntityZombieVillager.PROFESSION, Integer.valueOf(0));
    }

    public void setProfession(int i) {
        this.dataManager.set(EntityZombieVillager.PROFESSION, Integer.valueOf(i));
    }

    public int getProfession() {
        return Math.max(((Integer) this.dataManager.get(EntityZombieVillager.PROFESSION)).intValue() % 6, 0);
    }

    public static void registerFixesZombieVillager(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityZombieVillager.class);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Profession", this.getProfession());
        nbttagcompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        if (this.converstionStarter != null) {
            nbttagcompound.setUniqueId("ConversionPlayer", this.converstionStarter);
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setProfession(nbttagcompound.getInteger("Profession"));
        if (nbttagcompound.hasKey("ConversionTime", 99) && nbttagcompound.getInteger("ConversionTime") > -1) {
            this.startConverting(nbttagcompound.hasUniqueId("ConversionPlayer") ? nbttagcompound.getUniqueId("ConversionPlayer") : null, nbttagcompound.getInteger("ConversionTime"));
        }

    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.setProfession(this.world.rand.nextInt(6));
        return super.onInitialSpawn(difficultydamagescaler, groupdataentity);
    }

    public void onUpdate() {
        if (!this.world.isRemote && this.isConverting()) {
            int i = this.getConversionProgress();
            // CraftBukkit start - Use wall time instead of ticks for villager conversion
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            this.lastTick = MinecraftServer.currentTick;
            i *= elapsedTicks;
            // CraftBukkit end

            this.conversionTime -= i;
            if (this.conversionTime <= 0) {
                this.finishConversion();
            }
        }

        super.onUpdate();
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (itemstack.getItem() == Items.GOLDEN_APPLE && itemstack.getMetadata() == 0 && this.isPotionActive(MobEffects.WEAKNESS)) {
            if (!entityhuman.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            if (!this.world.isRemote) {
                this.startConverting(entityhuman.getUniqueID(), this.rand.nextInt(2401) + 3600);
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean canDespawn() {
        return !this.isConverting();
    }

    public boolean isConverting() {
        return ((Boolean) this.getDataManager().get(EntityZombieVillager.CONVERTING)).booleanValue();
    }

    protected void startConverting(@Nullable UUID uuid, int i) {
        this.converstionStarter = uuid;
        this.conversionTime = i;
        this.getDataManager().set(EntityZombieVillager.CONVERTING, Boolean.valueOf(true));
        this.removePotionEffect(MobEffects.WEAKNESS);
        this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, i, Math.min(this.world.getDifficulty().getDifficultyId() - 1, 0)));
        this.world.setEntityState(this, (byte) 16);
    }

    protected void finishConversion() {
        EntityVillager entityvillager = new EntityVillager(this.world);

        entityvillager.copyLocationAndAnglesFrom(this);
        entityvillager.setProfession(this.getProfession());
        entityvillager.finalizeMobSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData) null, false);
        entityvillager.setLookingForHome();
        if (this.isChild()) {
            entityvillager.setGrowingAge(-24000);
        }

        this.world.removeEntity(this);
        entityvillager.setNoAI(this.isAIDisabled());
        if (this.hasCustomName()) {
            entityvillager.setCustomNameTag(this.getCustomNameTag());
            entityvillager.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
        }

        this.world.addEntity(entityvillager, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CURED); // CraftBukkit - add SpawnReason
        if (this.converstionStarter != null) {
            EntityPlayer entityhuman = this.world.getPlayerEntityByUUID(this.converstionStarter);

            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((EntityPlayerMP) entityhuman, this, entityvillager);
            }
        }

        entityvillager.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
        this.world.playEvent((EntityPlayer) null, 1027, new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ), 0);
    }

    protected int getConversionProgress() {
        int i = 1;

        if (this.rand.nextFloat() < 0.01F) {
            int j = 0;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

            for (int k = (int) this.posX - 4; k < (int) this.posX + 4 && j < 14; ++k) {
                for (int l = (int) this.posY - 4; l < (int) this.posY + 4 && j < 14; ++l) {
                    for (int i1 = (int) this.posZ - 4; i1 < (int) this.posZ + 4 && j < 14; ++i1) {
                        Block block = this.world.getBlockState(blockposition_mutableblockposition.setPos(k, l, i1)).getBlock();

                        if (block == Blocks.IRON_BARS || block == Blocks.BED) {
                            if (this.rand.nextFloat() < 0.3F) {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    protected float getSoundPitch() {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 2.0F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
    }

    public SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT;
    }

    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH;
    }

    public SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE_VILLAGER;
    }

    protected ItemStack getSkullDrop() {
        return ItemStack.EMPTY;
    }
}
