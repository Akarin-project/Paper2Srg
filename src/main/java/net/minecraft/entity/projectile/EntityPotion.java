package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

// CraftBukkit start
import java.util.HashMap;
import java.util.Map;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
// CraftBukkit end

public class EntityPotion extends EntityThrowable {

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityPotion.class, DataSerializers.ITEM_STACK);
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Predicate<EntityLivingBase> WATER_SENSITIVE = new Predicate() {
        public boolean a(@Nullable EntityLivingBase entityliving) {
            return EntityPotion.isWaterSensitiveEntity(entityliving);
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EntityLivingBase) object);
        }
    };

    public EntityPotion(World world) {
        super(world);
    }

    public EntityPotion(World world, EntityLivingBase entityliving, ItemStack itemstack) {
        super(world, entityliving);
        this.setItem(itemstack);
    }

    public EntityPotion(World world, double d0, double d1, double d2, ItemStack itemstack) {
        super(world, d0, d1, d2);
        if (!itemstack.isEmpty()) {
            this.setItem(itemstack);
        }

    }

    protected void entityInit() {
        this.getDataManager().register(EntityPotion.ITEM, ItemStack.EMPTY);
    }

    public ItemStack getPotion() {
        ItemStack itemstack = (ItemStack) this.getDataManager().get(EntityPotion.ITEM);

        if (itemstack.getItem() != Items.SPLASH_POTION && itemstack.getItem() != Items.LINGERING_POTION) {
            if (this.world != null) {
                EntityPotion.LOGGER.error("ThrownPotion entity {} has no item?!", Integer.valueOf(this.getEntityId()));
            }

            return new ItemStack(Items.SPLASH_POTION);
        } else {
            return itemstack;
        }
    }

    public void setItem(ItemStack itemstack) {
        this.getDataManager().set(EntityPotion.ITEM, itemstack);
        this.getDataManager().setDirty(EntityPotion.ITEM);
    }

    protected float getGravityVelocity() {
        return 0.05F;
    }

    protected void onImpact(RayTraceResult movingobjectposition) {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getPotion();
            PotionType potionregistry = PotionUtils.getPotionFromItem(itemstack);
            List list = PotionUtils.getEffectsFromStack(itemstack);
            boolean flag = potionregistry == PotionTypes.WATER && list.isEmpty();

            if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK && flag) {
                BlockPos blockposition = movingobjectposition.getBlockPos().offset(movingobjectposition.sideHit);

                this.extinguishFires(blockposition, movingobjectposition.sideHit);
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection = (EnumFacing) iterator.next();

                    this.extinguishFires(blockposition.offset(enumdirection), enumdirection);
                }
            }

            if (flag) {
                this.applyWater();
            } else if (true || !list.isEmpty()) { // CraftBukkit - Call event even if no effects to apply
                if (this.isLingering()) {
                    this.makeAreaOfEffectCloud(itemstack, potionregistry);
                } else {
                    this.applySplash(movingobjectposition, list);
                }
            }

            int i = potionregistry.hasInstantEffect() ? 2007 : 2002;

            this.world.playEvent(i, new BlockPos(this), PotionUtils.getColor(itemstack));
            this.setDead();
        }
    }

    private void applyWater() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, EntityPotion.WATER_SENSITIVE);

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLivingBase entityliving = (EntityLivingBase) iterator.next();
                double d0 = this.getDistanceSq(entityliving);

                if (d0 < 16.0D && isWaterSensitiveEntity(entityliving)) {
                    entityliving.attackEntityFrom(DamageSource.DROWN, 1.0F);
                }
            }
        }

    }

    private void applySplash(RayTraceResult movingobjectposition, List<PotionEffect> list) {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List list1 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        Map<LivingEntity, Double> affected = new HashMap<LivingEntity, Double>(); // CraftBukkit

        if (!list1.isEmpty()) {
            Iterator iterator = list1.iterator();

            while (iterator.hasNext()) {
                EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                if (entityliving.canBeHitWithPotion()) {
                    double d0 = this.getDistanceSq(entityliving);

                    if (d0 < 16.0D) {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                        if (entityliving == movingobjectposition.entityHit) {
                            d1 = 1.0D;
                        }

                        // CraftBukkit start
                        affected.put((LivingEntity) entityliving.getBukkitEntity(), d1);
                    }
                }
            }
        }

        org.bukkit.event.entity.PotionSplashEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPotionSplashEvent(this, affected);
        if (!event.isCancelled() && list != null && !list.isEmpty()) { // do not process effects if there are no effects to process
            for (LivingEntity victim : event.getAffectedEntities()) {
                if (!(victim instanceof CraftLivingEntity)) {
                    continue;
                }

                EntityLivingBase entityliving = ((CraftLivingEntity) victim).getHandle();
                double d1 = event.getIntensity(victim);
                // CraftBukkit end

                Iterator iterator1 = list.iterator();

                while (iterator1.hasNext()) {
                    PotionEffect mobeffect = (PotionEffect) iterator1.next();
                    Potion mobeffectlist = mobeffect.getPotion();
                    // CraftBukkit start - Abide by PVP settings - for players only!
                    if (!this.world.pvpMode && this.getThrower() instanceof EntityPlayerMP && entityliving instanceof EntityPlayerMP && entityliving != this.getThrower()) {
                        int i = Potion.getIdFromPotion(mobeffectlist);
                        // Block SLOWER_MOVEMENT, SLOWER_DIG, HARM, BLINDNESS, HUNGER, WEAKNESS and POISON potions
                        if (i == 2 || i == 4 || i == 7 || i == 15 || i == 17 || i == 18 || i == 19) {
                            continue;
                        }
                    }
                    // CraftBukkit end

                    if (mobeffectlist.isInstant()) {
                        mobeffectlist.affectEntity(this, this.getThrower(), entityliving, mobeffect.getAmplifier(), d1);
                    } else {
                        int i = (int) (d1 * (double) mobeffect.getDuration() + 0.5D);

                        if (i > 20) {
                            entityliving.addPotionEffect(new PotionEffect(mobeffectlist, i, mobeffect.getAmplifier(), mobeffect.getIsAmbient(), mobeffect.doesShowParticles()));
                        }
                    }
                }
            }
        }

    }

    private void makeAreaOfEffectCloud(ItemStack itemstack, PotionType potionregistry) {
        EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);

        entityareaeffectcloud.setOwner(this.getThrower());
        entityareaeffectcloud.setRadius(3.0F);
        entityareaeffectcloud.setRadiusOnUse(-0.5F);
        entityareaeffectcloud.setWaitTime(10);
        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());
        entityareaeffectcloud.setPotion(potionregistry);
        Iterator iterator = PotionUtils.getFullEffectsFromItem(itemstack).iterator();

        while (iterator.hasNext()) {
            PotionEffect mobeffect = (PotionEffect) iterator.next();

            entityareaeffectcloud.addEffect(new PotionEffect(mobeffect));
        }

        NBTTagCompound nbttagcompound = itemstack.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99)) {
            entityareaeffectcloud.setColor(nbttagcompound.getInteger("CustomPotionColor"));
        }

        // CraftBukkit start
        org.bukkit.event.entity.LingeringPotionSplashEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callLingeringPotionSplashEvent(this, entityareaeffectcloud);
        if (!(event.isCancelled() || entityareaeffectcloud.isDead)) {
            this.world.spawnEntity(entityareaeffectcloud);
        } else {
            entityareaeffectcloud.isDead = true;
        }
        // CraftBukkit end
    }

    public boolean isLingering() {
        return this.getPotion().getItem() == Items.LINGERING_POTION;
    }

    private void extinguishFires(BlockPos blockposition, EnumFacing enumdirection) {
        if (this.world.getBlockState(blockposition).getBlock() == Blocks.FIRE) {
            this.world.extinguishFire((EntityPlayer) null, blockposition.offset(enumdirection), enumdirection.getOpposite());
        }

    }

    public static void registerFixesPotion(DataFixer dataconvertermanager) {
        EntityThrowable.registerFixesThrowable(dataconvertermanager, "ThrownPotion");
        dataconvertermanager.registerWalker(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityPotion.class, new String[] { "Potion"})));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        ItemStack itemstack = new ItemStack(nbttagcompound.getCompoundTag("Potion"));

        if (itemstack.isEmpty()) {
            this.setDead();
        } else {
            this.setItem(itemstack);
        }

    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        ItemStack itemstack = this.getPotion();

        if (!itemstack.isEmpty()) {
            nbttagcompound.setTag("Potion", itemstack.writeToNBT(new NBTTagCompound()));
        }

    }

    private static boolean isWaterSensitiveEntity(EntityLivingBase entityliving) {
        return entityliving instanceof EntityEnderman || entityliving instanceof EntityBlaze;
    }
}
