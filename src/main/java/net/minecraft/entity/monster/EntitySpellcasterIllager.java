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

    private static final DataParameter<Byte> SPELL = EntityDataManager.createKey(EntitySpellcasterIllager.class, DataSerializers.BYTE);
    protected int spellTicks;
    private EntitySpellcasterIllager.SpellType activeSpell;

    public EntitySpellcasterIllager(World world) {
        super(world);
        this.activeSpell = EntitySpellcasterIllager.SpellType.NONE;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntitySpellcasterIllager.SPELL, Byte.valueOf((byte) 0));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.spellTicks = nbttagcompound.getInteger("SpellTicks");
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("SpellTicks", this.spellTicks);
    }

    public boolean isSpellcasting() {
        return this.world.isRemote ? ((Byte) this.dataManager.get(EntitySpellcasterIllager.SPELL)).byteValue() > 0 : this.spellTicks > 0;
    }

    public void setSpellType(EntitySpellcasterIllager.SpellType entityillagerwizard_spell) {
        this.activeSpell = entityillagerwizard_spell;
        this.dataManager.set(EntitySpellcasterIllager.SPELL, Byte.valueOf((byte) entityillagerwizard_spell.id));
    }

    public EntitySpellcasterIllager.SpellType getSpellType() {
        return !this.world.isRemote ? this.activeSpell : EntitySpellcasterIllager.SpellType.getFromId(((Byte) this.dataManager.get(EntitySpellcasterIllager.SPELL)).byteValue());
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }

    }

    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.isSpellcasting()) {
            EntitySpellcasterIllager.SpellType entityillagerwizard_spell = this.getSpellType();
            double d0 = entityillagerwizard_spell.particleSpeed[0];
            double d1 = entityillagerwizard_spell.particleSpeed[1];
            double d2 = entityillagerwizard_spell.particleSpeed[2];
            float f = this.renderYawOffset * 0.017453292F + MathHelper.cos((float) this.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);

            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double) f1 * 0.6D, this.posY + 1.8D, this.posZ + (double) f2 * 0.6D, d0, d1, d2, new int[0]);
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - (double) f1 * 0.6D, this.posY + 1.8D, this.posZ - (double) f2 * 0.6D, d0, d1, d2, new int[0]);
        }

    }

    protected int getSpellTicks() {
        return this.spellTicks;
    }

    protected abstract SoundEvent getSpellSound();

    public static enum SpellType {

        NONE(0, 0.0D, 0.0D, 0.0D), SUMMON_VEX(1, 0.7D, 0.7D, 0.8D), FANGS(2, 0.4D, 0.3D, 0.35D), WOLOLO(3, 0.7D, 0.5D, 0.2D), DISAPPEAR(4, 0.3D, 0.3D, 0.8D), BLINDNESS(5, 0.1D, 0.1D, 0.2D);

        private final int id;
        private final double[] particleSpeed;

        private SpellType(int i, double d0, double param5, double d1) {
            this.id = i;
            this.particleSpeed = new double[] { d0, d1, d2};
        }

        public static EntitySpellcasterIllager.SpellType getFromId(int i) {
            EntitySpellcasterIllager.SpellType[] aentityillagerwizard_spell = values();
            int j = aentityillagerwizard_spell.length;

            for (int k = 0; k < j; ++k) {
                EntitySpellcasterIllager.SpellType entityillagerwizard_spell = aentityillagerwizard_spell[k];

                if (i == entityillagerwizard_spell.id) {
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

        public boolean shouldExecute() {
            return EntitySpellcasterIllager.this.getAttackTarget() == null ? false : (EntitySpellcasterIllager.this.isSpellcasting() ? false : EntitySpellcasterIllager.this.ticksExisted >= this.d);
        }

        public boolean shouldContinueExecuting() {
            return EntitySpellcasterIllager.this.getAttackTarget() != null && this.c > 0;
        }

        public void startExecuting() {
            this.c = this.m();
            EntitySpellcasterIllager.this.spellTicks = this.f();
            this.d = EntitySpellcasterIllager.this.ticksExisted + this.i();
            SoundEvent soundeffect = this.k();

            if (soundeffect != null) {
                EntitySpellcasterIllager.this.playSound(soundeffect, 1.0F, 1.0F);
            }

            EntitySpellcasterIllager.this.setSpellType(this.l());
        }

        public void updateTask() {
            --this.c;
            if (this.c == 0) {
                this.j();
                EntitySpellcasterIllager.this.playSound(EntitySpellcasterIllager.this.getSpellSound(), 1.0F, 1.0F);
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
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            return EntitySpellcasterIllager.this.getSpellTicks() > 0;
        }

        public void startExecuting() {
            super.startExecuting();
            EntitySpellcasterIllager.this.navigator.clearPath();
        }

        public void resetTask() {
            super.resetTask();
            EntitySpellcasterIllager.this.setSpellType(EntitySpellcasterIllager.SpellType.NONE);
        }

        public void updateTask() {
            if (EntitySpellcasterIllager.this.getAttackTarget() != null) {
                EntitySpellcasterIllager.this.getLookHelper().setLookPositionWithEntity(EntitySpellcasterIllager.this.getAttackTarget(), (float) EntitySpellcasterIllager.this.getHorizontalFaceSpeed(), (float) EntitySpellcasterIllager.this.getVerticalFaceSpeed());
            }

        }
    }
}
