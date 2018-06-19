package net.minecraft.potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;


public class PotionAbsorption extends Potion {

    protected PotionAbsorption(boolean flag, int i) {
        super(flag, i);
    }

    public void func_111187_a(EntityLivingBase entityliving, AbstractAttributeMap attributemapbase, int i) {
        entityliving.func_110149_m(entityliving.func_110139_bj() - (float) (4 * (i + 1)));
        super.func_111187_a(entityliving, attributemapbase, i);
    }

    public void func_111185_a(EntityLivingBase entityliving, AbstractAttributeMap attributemapbase, int i) {
        entityliving.func_110149_m(entityliving.func_110139_bj() + (float) (4 * (i + 1)));
        super.func_111185_a(entityliving, attributemapbase, i);
    }
}
