package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityDonkey extends AbstractChestHorse {

    public EntityDonkey(World world) {
        super(world);
    }

    public static void registerFixesDonkey(DataFixer dataconvertermanager) {
        AbstractChestHorse.registerFixesAbstractChestHorse(dataconvertermanager, EntityDonkey.class);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_DONKEY;
    }

    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_DONKEY_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_DONKEY_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        super.getHurtSound(damagesource);
        return SoundEvents.ENTITY_DONKEY_HURT;
    }

    public boolean canMateWith(EntityAnimal entityanimal) {
        return entityanimal == this ? false : (!(entityanimal instanceof EntityDonkey) && !(entityanimal instanceof EntityHorse) ? false : this.canMate() && ((AbstractHorse) entityanimal).canMate());
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        Object object = entityageable instanceof EntityHorse ? new EntityMule(this.world) : new EntityDonkey(this.world);

        this.setOffspringAttributes(entityageable, (AbstractHorse) object);
        return (EntityAgeable) object;
    }
}
