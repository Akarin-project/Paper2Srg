package net.minecraft.entity;

public interface IRangedAttackMob {

    void attackEntityWithRangedAttack(EntityLivingBase entityliving, float f);

    void setSwingingArms(boolean flag);
}
