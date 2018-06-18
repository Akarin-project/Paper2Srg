package net.minecraft.util;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class EntityDamageSourceIndirect extends EntityDamageSource {

    private final Entity indirectEntity;

    public EntityDamageSourceIndirect(String s, Entity entity, @Nullable Entity entity1) {
        super(s, entity);
        this.indirectEntity = entity1;
    }

    @Nullable
    public Entity getImmediateSource() {
        return this.damageSourceEntity;
    }

    @Nullable
    public Entity getTrueSource() {
        return this.indirectEntity;
    }

    public ITextComponent getDeathMessage(EntityLivingBase entityliving) {
        ITextComponent ichatbasecomponent = this.indirectEntity == null ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
        ItemStack itemstack = this.indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase) this.indirectEntity).getHeldItemMainhand() : ItemStack.EMPTY;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".item";

        return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] { entityliving.getDisplayName(), ichatbasecomponent, itemstack.getTextComponent()}) : new TextComponentTranslation(s, new Object[] { entityliving.getDisplayName(), ichatbasecomponent});
    }

    // CraftBukkit start
    public Entity getProximateDamageSource() {
        return super.getTrueSource();
    }
    // CraftBukkit end
}
