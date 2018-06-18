package net.minecraft.potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;


public class PotionHealthBoost extends Potion {

    public PotionHealthBoost(boolean flag, int i) {
        super(flag, i);
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityliving, AbstractAttributeMap attributemapbase, int i) {
        super.removeAttributesModifiersFromEntity(entityliving, attributemapbase, i);
        if (entityliving.getHealth() > entityliving.getMaxHealth()) {
            entityliving.setHealth(entityliving.getMaxHealth());
        }

    }
}
