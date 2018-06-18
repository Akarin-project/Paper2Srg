package net.minecraft.util;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CombatTracker {

    private final List<CombatEntry> combatEntries = Lists.newArrayList();
    private final EntityLivingBase fighter;
    private int lastDamageTime;
    private int combatStartTime;
    private int combatEndTime;
    private boolean inCombat;
    private boolean takingDamage;
    private String fallSuffix;

    public CombatTracker(EntityLivingBase entityliving) {
        this.fighter = entityliving;
    }

    public void calculateFallSuffix() {
        this.resetFallSuffix();
        if (this.fighter.isOnLadder()) {
            Block block = this.fighter.world.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();

            if (block == Blocks.LADDER) {
                this.fallSuffix = "ladder";
            } else if (block == Blocks.VINE) {
                this.fallSuffix = "vines";
            }
        } else if (this.fighter.isInWater()) {
            this.fallSuffix = "water";
        }

    }

    public void trackDamage(DamageSource damagesource, float f, float f1) {
        this.reset();
        this.calculateFallSuffix();
        CombatEntry combatentry = new CombatEntry(damagesource, this.fighter.ticksExisted, f, f1, this.fallSuffix, this.fighter.fallDistance);

        this.combatEntries.add(combatentry);
        this.lastDamageTime = this.fighter.ticksExisted;
        this.takingDamage = true;
        if (combatentry.isLivingDamageSrc() && !this.inCombat && this.fighter.isEntityAlive()) {
            this.inCombat = true;
            this.combatStartTime = this.fighter.ticksExisted;
            this.combatEndTime = this.combatStartTime;
            this.fighter.sendEnterCombat();
        }

    }

    public ITextComponent getDeathMessage() {
        if (this.combatEntries.isEmpty()) {
            return new TextComponentTranslation("death.attack.generic", new Object[] { this.fighter.getDisplayName()});
        } else {
            CombatEntry combatentry = this.getBestCombatEntry();
            CombatEntry combatentry1 = (CombatEntry) this.combatEntries.get(this.combatEntries.size() - 1);
            ITextComponent ichatbasecomponent = combatentry1.getDamageSrcDisplayName();
            Entity entity = combatentry1.getDamageSrc().getTrueSource();
            Object object;

            if (combatentry != null && combatentry1.getDamageSrc() == DamageSource.FALL) {
                ITextComponent ichatbasecomponent1 = combatentry.getDamageSrcDisplayName();

                if (combatentry.getDamageSrc() != DamageSource.FALL && combatentry.getDamageSrc() != DamageSource.OUT_OF_WORLD) {
                    if (ichatbasecomponent1 != null && (ichatbasecomponent == null || !ichatbasecomponent1.equals(ichatbasecomponent))) {
                        Entity entity1 = combatentry.getDamageSrc().getTrueSource();
                        ItemStack itemstack = entity1 instanceof EntityLivingBase ? ((EntityLivingBase) entity1).getHeldItemMainhand() : ItemStack.EMPTY;

                        if (!itemstack.isEmpty() && itemstack.hasDisplayName()) {
                            object = new TextComponentTranslation("death.fell.assist.item", new Object[] { this.fighter.getDisplayName(), ichatbasecomponent1, itemstack.getTextComponent()});
                        } else {
                            object = new TextComponentTranslation("death.fell.assist", new Object[] { this.fighter.getDisplayName(), ichatbasecomponent1});
                        }
                    } else if (ichatbasecomponent != null) {
                        ItemStack itemstack1 = entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getHeldItemMainhand() : ItemStack.EMPTY;

                        if (!itemstack1.isEmpty() && itemstack1.hasDisplayName()) {
                            object = new TextComponentTranslation("death.fell.finish.item", new Object[] { this.fighter.getDisplayName(), ichatbasecomponent, itemstack1.getTextComponent()});
                        } else {
                            object = new TextComponentTranslation("death.fell.finish", new Object[] { this.fighter.getDisplayName(), ichatbasecomponent});
                        }
                    } else {
                        object = new TextComponentTranslation("death.fell.killer", new Object[] { this.fighter.getDisplayName()});
                    }
                } else {
                    object = new TextComponentTranslation("death.fell.accident." + this.getFallSuffix(combatentry), new Object[] { this.fighter.getDisplayName()});
                }
            } else {
                object = combatentry1.getDamageSrc().getDeathMessage(this.fighter);
            }

            return (ITextComponent) object;
        }
    }

    @Nullable
    public EntityLivingBase getBestAttacker() {
        EntityLivingBase entityliving = null;
        EntityPlayer entityhuman = null;
        float f = 0.0F;
        float f1 = 0.0F;
        Iterator iterator = this.combatEntries.iterator();

        while (iterator.hasNext()) {
            CombatEntry combatentry = (CombatEntry) iterator.next();

            if (combatentry.getDamageSrc().getTrueSource() instanceof EntityPlayer && (entityhuman == null || combatentry.getDamage() > f1)) {
                f1 = combatentry.getDamage();
                entityhuman = (EntityPlayer) combatentry.getDamageSrc().getTrueSource();
            }

            if (combatentry.getDamageSrc().getTrueSource() instanceof EntityLivingBase && (entityliving == null || combatentry.getDamage() > f)) {
                f = combatentry.getDamage();
                entityliving = (EntityLivingBase) combatentry.getDamageSrc().getTrueSource();
            }
        }

        if (entityhuman != null && f1 >= f / 3.0F) {
            return entityhuman;
        } else {
            return entityliving;
        }
    }

    @Nullable
    private CombatEntry getBestCombatEntry() {
        CombatEntry combatentry = null;
        CombatEntry combatentry1 = null;
        float f = 0.0F;
        float f1 = 0.0F;

        for (int i = 0; i < this.combatEntries.size(); ++i) {
            CombatEntry combatentry2 = (CombatEntry) this.combatEntries.get(i);
            CombatEntry combatentry3 = i > 0 ? (CombatEntry) this.combatEntries.get(i - 1) : null;

            if ((combatentry2.getDamageSrc() == DamageSource.FALL || combatentry2.getDamageSrc() == DamageSource.OUT_OF_WORLD) && combatentry2.getDamageAmount() > 0.0F && (combatentry == null || combatentry2.getDamageAmount() > f1)) {
                if (i > 0) {
                    combatentry = combatentry3;
                } else {
                    combatentry = combatentry2;
                }

                f1 = combatentry2.getDamageAmount();
            }

            if (combatentry2.getFallSuffix() != null && (combatentry1 == null || combatentry2.getDamage() > f)) {
                combatentry1 = combatentry2;
                f = combatentry2.getDamage();
            }
        }

        if (f1 > 5.0F && combatentry != null) {
            return combatentry;
        } else if (f > 5.0F && combatentry1 != null) {
            return combatentry1;
        } else {
            return null;
        }
    }

    private String getFallSuffix(CombatEntry combatentry) {
        return combatentry.getFallSuffix() == null ? "generic" : combatentry.getFallSuffix();
    }

    public int getCombatDuration() {
        return this.inCombat ? this.fighter.ticksExisted - this.combatStartTime : this.combatEndTime - this.combatStartTime;
    }

    private void resetFallSuffix() {
        this.fallSuffix = null;
    }

    public void reset() {
        int i = this.inCombat ? 300 : 100;

        if (this.takingDamage && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.lastDamageTime > i)) {
            boolean flag = this.inCombat;

            this.takingDamage = false;
            this.inCombat = false;
            this.combatEndTime = this.fighter.ticksExisted;
            if (flag) {
                this.fighter.sendEndCombat();
            }

            this.combatEntries.clear();
        }

    }

    public EntityLivingBase getFighter() {
        return this.fighter;
    }
}
