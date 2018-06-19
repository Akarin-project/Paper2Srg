package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.EntityIllagerWizard.b;
import net.minecraft.server.EntityIllagerWizard.c;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntitySpellcasterIllager extends AbstractIllager {

    private static final DataParameter<Byte> field_193088_c = EntityDataManager.func_187226_a(EntitySpellcasterIllager.class, DataSerializers.field_187191_a);
    protected int field_193087_b;
    private EntitySpellcasterIllager.SpellType field_193089_bx;

    public EntitySpellcasterIllager(World world) {
        super(world);
        this.field_193089_bx = EntitySpellcasterIllager.SpellType.NONE;
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntitySpellcasterIllager.field_193088_c, Byte.valueOf((byte) 0));
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.field_193087_b = nbttagcompound.func_74762_e("SpellTicks");
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("SpellTicks", this.field_193087_b);
    }

    public boolean func_193082_dl() {
        return this.field_70170_p.field_72995_K ? ((Byte) this.field_70180_af.func_187225_a(EntitySpellcasterIllager.field_193088_c)).byteValue() > 0 : this.field_193087_b > 0;
    }

    public void func_193081_a(EntitySpellcasterIllager.SpellType entityillagerwizard_spell) {
        this.field_193089_bx = entityillagerwizard_spell;
        this.field_70180_af.func_187227_b(EntitySpellcasterIllager.field_193088_c, Byte.valueOf((byte) entityillagerwizard_spell.field_193345_g));
    }

    public EntitySpellcasterIllager.SpellType func_193083_dm() {
        return !this.field_70170_p.field_72995_K ? this.field_193089_bx : EntitySpellcasterIllager.SpellType.func_193337_a(((Byte) this.field_70180_af.func_187225_a(EntitySpellcasterIllager.field_193088_c)).byteValue());
    }

    protected void func_70619_bc() {
        super.func_70619_bc();
        if (this.field_193087_b > 0) {
            --this.field_193087_b;
        }

    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_70170_p.field_72995_K && this.func_193082_dl()) {
            EntitySpellcasterIllager.SpellType entityillagerwizard_spell = this.func_193083_dm();
            double d0 = entityillagerwizard_spell.field_193346_h[0];
            double d1 = entityillagerwizard_spell.field_193346_h[1];
            double d2 = entityillagerwizard_spell.field_193346_h[2];
            float f = this.field_70761_aq * 0.017453292F + MathHelper.func_76134_b((float) this.field_70173_aa * 0.6662F) * 0.25F;
            float f1 = MathHelper.func_76134_b(f);
            float f2 = MathHelper.func_76126_a(f);

            this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t + (double) f1 * 0.6D, this.field_70163_u + 1.8D, this.field_70161_v + (double) f2 * 0.6D, d0, d1, d2, new int[0]);
            this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t - (double) f1 * 0.6D, this.field_70163_u + 1.8D, this.field_70161_v - (double) f2 * 0.6D, d0, d1, d2, new int[0]);
        }

    }

    protected int func_193085_dn() {
        return this.field_193087_b;
    }

    protected abstract SoundEvent func_193086_dk();

    public static enum SpellType {

        NONE(0, 0.0D, 0.0D, 0.0D), SUMMON_VEX(1, 0.7D, 0.7D, 0.8D), FANGS(2, 0.4D, 0.3D, 0.35D), WOLOLO(3, 0.7D, 0.5D, 0.2D), DISAPPEAR(4, 0.3D, 0.3D, 0.8D), BLINDNESS(5, 0.1D, 0.1D, 0.2D);

        private final int field_193345_g;
        private final double[] field_193346_h;

        private SpellType(int i, double d0, double param5, double d1) {
            this.field_193345_g = i;
            this.field_193346_h = new double[] { d0, d1, d2};
        }

        public static EntitySpellcasterIllager.SpellType func_193337_a(int i) {
            EntitySpellcasterIllager.SpellType[] aentityillagerwizard_spell = values();
            int j = aentityillagerwizard_spell.length;

            for (int k = 0; k < j; ++k) {
                EntitySpellcasterIllager.SpellType entityillagerwizard_spell = aentityillagerwizard_spell[k];

                if (i == entityillagerwizard_spell.field_193345_g) {
                    return entityillagerwizard_spell;
                }
            }

            return EntitySpellcasterIllager.SpellType.NONE;
        }
    }

    public abstract class c extends EntityAIBase {

        protected int c;
        protected int d;

        protected c() {}

        public boolean func_75250_a() {
            return EntitySpellcasterIllager.this.func_70638_az() == null ? false : (EntitySpellcasterIllager.this.func_193082_dl() ? false : EntitySpellcasterIllager.this.field_70173_aa >= this.d);
        }

        public boolean func_75253_b() {
            return EntitySpellcasterIllager.this.func_70638_az() != null && this.c > 0;
        }

        public void func_75249_e() {
            this.c = this.m();
            EntitySpellcasterIllager.this.field_193087_b = this.f();
            this.d = EntitySpellcasterIllager.this.field_70173_aa + this.i();
            SoundEvent soundeffect = this.k();

            if (soundeffect != null) {
                EntitySpellcasterIllager.this.func_184185_a(soundeffect, 1.0F, 1.0F);
            }

            EntitySpellcasterIllager.this.func_193081_a(this.l());
        }

        public void func_75246_d() {
            --this.c;
            if (this.c == 0) {
                this.j();
                EntitySpellcasterIllager.this.func_184185_a(EntitySpellcasterIllager.this.func_193086_dk(), 1.0F, 1.0F);
            }

        }

        protected abstract void j();

        protected int m() {
            return 20;
        }

        protected abstract int f();

        protected abstract int i();

        @Nullable
        protected abstract SoundEvent k();

        protected abstract EntitySpellcasterIllager.SpellType l();
    }

    public class b extends EntityAIBase {

        public b() {
            this.func_75248_a(3);
        }

        public boolean func_75250_a() {
            return EntitySpellcasterIllager.this.func_193085_dn() > 0;
        }

        public void func_75249_e() {
            super.func_75249_e();
            EntitySpellcasterIllager.this.field_70699_by.func_75499_g();
        }

        public void func_75251_c() {
            super.func_75251_c();
            EntitySpellcasterIllager.this.func_193081_a(EntitySpellcasterIllager.SpellType.NONE);
        }

        public void func_75246_d() {
            if (EntitySpellcasterIllager.this.func_70638_az() != null) {
                EntitySpellcasterIllager.this.func_70671_ap().func_75651_a(EntitySpellcasterIllager.this.func_70638_az(), (float) EntitySpellcasterIllager.this.func_184649_cE(), (float) EntitySpellcasterIllager.this.func_70646_bf());
            }

        }
    }
}
