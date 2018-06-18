package net.minecraft.potion;

import com.google.common.collect.ComparisonChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class PotionEffect implements Comparable<PotionEffect> {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Potion potion;
    private int duration;
    private int amplifier;
    private boolean isSplashPotion;
    private boolean isAmbient;
    private boolean showParticles;

    public PotionEffect(Potion mobeffectlist) {
        this(mobeffectlist, 0, 0);
    }

    public PotionEffect(Potion mobeffectlist, int i) {
        this(mobeffectlist, i, 0);
    }

    public PotionEffect(Potion mobeffectlist, int i, int j) {
        this(mobeffectlist, i, j, false, true);
    }

    public PotionEffect(Potion mobeffectlist, int i, int j, boolean flag, boolean flag1) {
        this.potion = mobeffectlist;
        this.duration = i;
        this.amplifier = j;
        this.isAmbient = flag;
        this.showParticles = flag1;
    }

    public PotionEffect(PotionEffect mobeffect) {
        this.potion = mobeffect.potion;
        this.duration = mobeffect.duration;
        this.amplifier = mobeffect.amplifier;
        this.isAmbient = mobeffect.isAmbient;
        this.showParticles = mobeffect.showParticles;
    }

    public void combine(PotionEffect mobeffect) {
        if (this.potion != mobeffect.potion) {
            PotionEffect.LOGGER.warn("This method should only be called for matching effects!");
        }

        if (mobeffect.amplifier > this.amplifier) {
            this.amplifier = mobeffect.amplifier;
            this.duration = mobeffect.duration;
        } else if (mobeffect.amplifier == this.amplifier && this.duration < mobeffect.duration) {
            this.duration = mobeffect.duration;
        } else if (!mobeffect.isAmbient && this.isAmbient) {
            this.isAmbient = mobeffect.isAmbient;
        }

        this.showParticles = mobeffect.showParticles;
    }

    public Potion getPotion() {
        return this.potion;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public boolean getIsAmbient() {
        return this.isAmbient;
    }

    public boolean doesShowParticles() {
        return this.showParticles;
    }

    public boolean onUpdate(EntityLivingBase entityliving) {
        if (this.duration > 0) {
            if (this.potion.isReady(this.duration, this.amplifier)) {
                this.performEffect(entityliving);
            }

            this.deincrementDuration();
        }

        return this.duration > 0;
    }

    private int deincrementDuration() {
        return --this.duration;
    }

    public void performEffect(EntityLivingBase entityliving) {
        if (this.duration > 0) {
            this.potion.performEffect(entityliving, this.amplifier);
        }

    }

    public String getEffectName() {
        return this.potion.getName();
    }

    public String toString() {
        String s;

        if (this.amplifier > 0) {
            s = this.getEffectName() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration;
        } else {
            s = this.getEffectName() + ", Duration: " + this.duration;
        }

        if (this.isSplashPotion) {
            s = s + ", Splash: true";
        }

        if (!this.showParticles) {
            s = s + ", Particles: false";
        }

        return s;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof PotionEffect)) {
            return false;
        } else {
            PotionEffect mobeffect = (PotionEffect) object;

            return this.duration == mobeffect.duration && this.amplifier == mobeffect.amplifier && this.isSplashPotion == mobeffect.isSplashPotion && this.isAmbient == mobeffect.isAmbient && this.potion.equals(mobeffect.potion);
        }
    }

    public int hashCode() {
        int i = this.potion.hashCode();

        i = 31 * i + this.duration;
        i = 31 * i + this.amplifier;
        i = 31 * i + (this.isSplashPotion ? 1 : 0);
        i = 31 * i + (this.isAmbient ? 1 : 0);
        return i;
    }

    public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Id", (byte) Potion.getIdFromPotion(this.getPotion()));
        nbttagcompound.setByte("Amplifier", (byte) this.getAmplifier());
        nbttagcompound.setInteger("Duration", this.getDuration());
        nbttagcompound.setBoolean("Ambient", this.getIsAmbient());
        nbttagcompound.setBoolean("ShowParticles", this.doesShowParticles());
        return nbttagcompound;
    }

    public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbttagcompound) {
        byte b0 = nbttagcompound.getByte("Id");
        Potion mobeffectlist = Potion.getPotionById(b0);

        if (mobeffectlist == null) {
            return null;
        } else {
            byte b1 = nbttagcompound.getByte("Amplifier");
            int i = nbttagcompound.getInteger("Duration");
            boolean flag = nbttagcompound.getBoolean("Ambient");
            boolean flag1 = true;

            if (nbttagcompound.hasKey("ShowParticles", 1)) {
                flag1 = nbttagcompound.getBoolean("ShowParticles");
            }

            return new PotionEffect(mobeffectlist, i, b1 < 0 ? 0 : b1, flag, flag1);
        }
    }

    public int compareTo(PotionEffect mobeffect) {
        boolean flag = true;

        return (this.getDuration() <= 32147 || mobeffect.getDuration() <= 32147) && (!this.getIsAmbient() || !mobeffect.getIsAmbient()) ? ComparisonChain.start().compare(Boolean.valueOf(this.getIsAmbient()), Boolean.valueOf(mobeffect.getIsAmbient())).compare(this.getDuration(), mobeffect.getDuration()).compare(this.getPotion().getLiquidColor(), mobeffect.getPotion().getLiquidColor()).result() : ComparisonChain.start().compare(Boolean.valueOf(this.getIsAmbient()), Boolean.valueOf(mobeffect.getIsAmbient())).compare(this.getPotion().getLiquidColor(), mobeffect.getPotion().getLiquidColor()).result();
    }

    public int compareTo(Object object) {
        return this.compareTo((PotionEffect) object);
    }
}
