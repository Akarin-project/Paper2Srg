package net.minecraft.entity.ai;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;


public class EntityAISkeletonRiders extends EntityAIBase {

    private final EntitySkeletonHorse horse;

    public EntityAISkeletonRiders(EntitySkeletonHorse entityhorseskeleton) {
        this.horse = entityhorseskeleton;
    }

    public boolean shouldExecute() {
        return this.horse.world.isAnyPlayerWithinRangeAt(this.horse.posX, this.horse.posY, this.horse.posZ, 10.0D);
    }

    public void updateTask() {
        DifficultyInstance difficultydamagescaler = this.horse.world.getDifficultyForLocation(new BlockPos(this.horse));

        this.horse.setTrap(false);
        this.horse.setHorseTamed(true);
        this.horse.setGrowingAge(0);
        this.horse.world.addWeatherEffect(new EntityLightningBolt(this.horse.world, this.horse.posX, this.horse.posY, this.horse.posZ, true));
        EntitySkeleton entityskeleton = this.createSkeleton(difficultydamagescaler, this.horse);

        if (entityskeleton != null) entityskeleton.startRiding(this.horse); // CraftBukkit

        for (int i = 0; i < 3; ++i) {
            AbstractHorse entityhorseabstract = this.createHorse(difficultydamagescaler);
            if (entityhorseabstract == null) continue; // CraftBukkit
            EntitySkeleton entityskeleton1 = this.createSkeleton(difficultydamagescaler, entityhorseabstract);

            if (entityskeleton1 != null) entityskeleton1.startRiding(entityhorseabstract); // CraftBukkit
            entityhorseabstract.addVelocity(this.horse.getRNG().nextGaussian() * 0.5D, 0.0D, this.horse.getRNG().nextGaussian() * 0.5D);
        }

    }

    private AbstractHorse createHorse(DifficultyInstance difficultydamagescaler) {
        EntitySkeletonHorse entityhorseskeleton = new EntitySkeletonHorse(this.horse.world);

        entityhorseskeleton.onInitialSpawn(difficultydamagescaler, (IEntityLivingData) null);
        entityhorseskeleton.setPosition(this.horse.posX, this.horse.posY, this.horse.posZ);
        entityhorseskeleton.hurtResistantTime = 60;
        entityhorseskeleton.enablePersistence();
        entityhorseskeleton.setHorseTamed(true);
        entityhorseskeleton.setGrowingAge(0);
        if (!entityhorseskeleton.world.addEntity(entityhorseskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.TRAP)) return null; // CraftBukkit
        return entityhorseskeleton;
    }

    private EntitySkeleton createSkeleton(DifficultyInstance difficultydamagescaler, AbstractHorse entityhorseabstract) {
        EntitySkeleton entityskeleton = new EntitySkeleton(entityhorseabstract.world);

        entityskeleton.onInitialSpawn(difficultydamagescaler, (IEntityLivingData) null);
        entityskeleton.setPosition(entityhorseabstract.posX, entityhorseabstract.posY, entityhorseabstract.posZ);
        entityskeleton.hurtResistantTime = 60;
        entityskeleton.enablePersistence();
        if (entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }

        entityskeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getHeldItemMainhand(), (int) (5.0F + difficultydamagescaler.getClampedAdditionalDifficulty() * (float) entityskeleton.getRNG().nextInt(18)), false));
        entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD), (int) (5.0F + difficultydamagescaler.getClampedAdditionalDifficulty() * (float) entityskeleton.getRNG().nextInt(18)), false));
        if (!entityskeleton.world.addEntity(entityskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.JOCKEY)) return null; // CraftBukkit
        return entityskeleton;
    }
}
