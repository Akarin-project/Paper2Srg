package net.minecraft.potion;

import com.google.common.collect.ComparisonChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class PotionEffect implements Comparable<PotionEffect> {

    private static final Logger field_180155_a = LogManager.getLogger();
    private final Potion field_188420_b;
    private int field_76460_b;
    private int field_76461_c;
    private boolean field_82723_d;
    private boolean field_82724_e;
    private boolean field_188421_h;

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
        this.field_188420_b = mobeffectlist;
        this.field_76460_b = i;
        this.field_76461_c = j;
        this.field_82724_e = flag;
        this.field_188421_h = flag1;
    }

    public PotionEffect(PotionEffect mobeffect) {
        this.field_188420_b = mobeffect.field_188420_b;
        this.field_76460_b = mobeffect.field_76460_b;
        this.field_76461_c = mobeffect.field_76461_c;
        this.field_82724_e = mobeffect.field_82724_e;
        this.field_188421_h = mobeffect.field_188421_h;
    }

    public void func_76452_a(PotionEffect mobeffect) {
        if (this.field_188420_b != mobeffect.field_188420_b) {
            PotionEffect.field_180155_a.warn("This method should only be called for matching effects!");
        }

        if (mobeffect.field_76461_c > this.field_76461_c) {
            this.field_76461_c = mobeffect.field_76461_c;
            this.field_76460_b = mobeffect.field_76460_b;
        } else if (mobeffect.field_76461_c == this.field_76461_c && this.field_76460_b < mobeffect.field_76460_b) {
            this.field_76460_b = mobeffect.field_76460_b;
        } else if (!mobeffect.field_82724_e && this.field_82724_e) {
            this.field_82724_e = mobeffect.field_82724_e;
        }

        this.field_188421_h = mobeffect.field_188421_h;
    }

    public Potion func_188419_a() {
        return this.field_188420_b;
    }

    public int func_76459_b() {
        return this.field_76460_b;
    }

    public int func_76458_c() {
        return this.field_76461_c;
    }

    public boolean func_82720_e() {
        return this.field_82724_e;
    }

    public boolean func_188418_e() {
        return this.field_188421_h;
    }

    public boolean func_76455_a(EntityLivingBase entityliving) {
        if (this.field_76460_b > 0) {
            if (this.field_188420_b.func_76397_a(this.field_76460_b, this.field_76461_c)) {
                this.func_76457_b(entityliving);
            }

            this.func_76454_e();
        }

        return this.field_76460_b > 0;
    }

    private int func_76454_e() {
        return --this.field_76460_b;
    }

    public void func_76457_b(EntityLivingBase entityliving) {
        if (this.field_76460_b > 0) {
            this.field_188420_b.func_76394_a(entityliving, this.field_76461_c);
        }

    }

    public String func_76453_d() {
        return this.field_188420_b.func_76393_a();
    }

    public String toString() {
        String s;

        if (this.field_76461_c > 0) {
            s = this.func_76453_d() + " x " + (this.field_76461_c + 1) + ", Duration: " + this.field_76460_b;
        } else {
            s = this.func_76453_d() + ", Duration: " + this.field_76460_b;
        }

        if (this.field_82723_d) {
            s = s + ", Splash: true";
        }

        if (!this.field_188421_h) {
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

            return this.field_76460_b == mobeffect.field_76460_b && this.field_76461_c == mobeffect.field_76461_c && this.field_82723_d == mobeffect.field_82723_d && this.field_82724_e == mobeffect.field_82724_e && this.field_188420_b.equals(mobeffect.field_188420_b);
        }
    }

    public int hashCode() {
        int i = this.field_188420_b.hashCode();

        i = 31 * i + this.field_76460_b;
        i = 31 * i + this.field_76461_c;
        i = 31 * i + (this.field_82723_d ? 1 : 0);
        i = 31 * i + (this.field_82724_e ? 1 : 0);
        return i;
    }

    public NBTTagCompound func_82719_a(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74774_a("Id", (byte) Potion.func_188409_a(this.func_188419_a()));
        nbttagcompound.func_74774_a("Amplifier", (byte) this.func_76458_c());
        nbttagcompound.func_74768_a("Duration", this.func_76459_b());
        nbttagcompound.func_74757_a("Ambient", this.func_82720_e());
        nbttagcompound.func_74757_a("ShowParticles", this.func_188418_e());
        return nbttagcompound;
    }

    public static PotionEffect func_82722_b(NBTTagCompound nbttagcompound) {
        byte b0 = nbttagcompound.func_74771_c("Id");
        Potion mobeffectlist = Potion.func_188412_a(b0);

        if (mobeffectlist == null) {
            return null;
        } else {
            byte b1 = nbttagcompound.func_74771_c("Amplifier");
            int i = nbttagcompound.func_74762_e("Duration");
            boolean flag = nbttagcompound.func_74767_n("Ambient");
            boolean flag1 = true;

            if (nbttagcompound.func_150297_b("ShowParticles", 1)) {
                flag1 = nbttagcompound.func_74767_n("ShowParticles");
            }

            return new PotionEffect(mobeffectlist, i, b1 < 0 ? 0 : b1, flag, flag1);
        }
    }

    public int compareTo(PotionEffect mobeffect) {
        boolean flag = true;

        return (this.func_76459_b() <= 32147 || mobeffect.func_76459_b() <= 32147) && (!this.func_82720_e() || !mobeffect.func_82720_e()) ? ComparisonChain.start().compare(Boolean.valueOf(this.func_82720_e()), Boolean.valueOf(mobeffect.func_82720_e())).compare(this.func_76459_b(), mobeffect.func_76459_b()).compare(this.func_188419_a().func_76401_j(), mobeffect.func_188419_a().func_76401_j()).result() : ComparisonChain.start().compare(Boolean.valueOf(this.func_82720_e()), Boolean.valueOf(mobeffect.func_82720_e())).compare(this.func_188419_a().func_76401_j(), mobeffect.func_188419_a().func_76401_j()).result();
    }

    public int compareTo(Object object) {
        return this.compareTo((PotionEffect) object);
    }
}
