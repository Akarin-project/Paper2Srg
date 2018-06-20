package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public abstract class EntityGolem extends EntityCreature implements IAnimals {

    public EntityGolem(World world) {
        super(world);
    }

    @Override
    public void func_180430_e(float f, float f1) {}

    @Override
    @Nullable
    protected SoundEvent func_184639_G() {
        return null;
    }

    @Override
    @Nullable
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return null;
    }

    @Override
    @Nullable
    protected SoundEvent func_184615_bR() {
        return null;
    }

    @Override
    public int func_70627_aG() {
        return 120;
    }

    @Override
    public boolean func_70692_ba() {
        return false;
    }
}
