package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityStray extends AbstractSkeleton {

    public EntityStray(World world) {
        super(world);
    }

    public static void registerFixesStray(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityStray.class);
    }

    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.world.canSeeSky(new BlockPos(this));
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_STRAY;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_STRAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_STRAY_DEATH;
    }

    SoundEvent getStepSound() {
        return SoundEvents.ENTITY_STRAY_STEP;
    }

    protected EntityArrow getArrow(float f) {
        EntityArrow entityarrow = super.getArrow(f);

        if (entityarrow instanceof EntityTippedArrow) {
            ((EntityTippedArrow) entityarrow).addEffect(new PotionEffect(MobEffects.SLOWNESS, 600));
        }

        return entityarrow;
    }
}
