package net.minecraft.potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;


public class PotionAbsorption extends Potion {

    protected PotionAbsorption(boolean flag, int i) {
        super(flag, i);
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityliving, AbstractAttributeMap attributemapbase, int i) {
        entityliving.setAbsorptionAmount(entityliving.getAbsorptionAmount() - (float) (4 * (i + 1)));
        super.removeAttributesModifiersFromEntity(entityliving, attributemapbase, i);
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase entityliving, AbstractAttributeMap attributemapbase, int i) {
        entityliving.setAbsorptionAmount(entityliving.getAbsorptionAmount() + (float) (4 * (i + 1)));
        super.applyAttributesModifiersToEntity(entityliving, attributemapbase, i);
    }
}
