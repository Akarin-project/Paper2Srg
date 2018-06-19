package net.minecraft.potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;


public class PotionHealthBoost extends Potion {

    public PotionHealthBoost(boolean flag, int i) {
        super(flag, i);
    }

    public void func_111187_a(EntityLivingBase entityliving, AbstractAttributeMap attributemapbase, int i) {
        super.func_111187_a(entityliving, attributemapbase, i);
        if (entityliving.func_110143_aJ() > entityliving.func_110138_aP()) {
            entityliving.func_70606_j(entityliving.func_110138_aP());
        }

    }
}
