package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class EntityAreaEffectCloud extends Entity {

    private static final DataParameter<Float> field_184498_a = EntityDataManager.func_187226_a(EntityAreaEffectCloud.class, DataSerializers.field_187193_c);
    private static final DataParameter<Integer> field_184499_b = EntityDataManager.func_187226_a(EntityAreaEffectCloud.class, DataSerializers.field_187192_b);
    private static final DataParameter<Boolean> field_184500_c = EntityDataManager.func_187226_a(EntityAreaEffectCloud.class, DataSerializers.field_187198_h);
    private static final DataParameter<Integer> field_184501_d = EntityDataManager.func_187226_a(EntityAreaEffectCloud.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer> field_189736_e = EntityDataManager.func_187226_a(EntityAreaEffectCloud.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer> field_189737_f = EntityDataManager.func_187226_a(EntityAreaEffectCloud.class, DataSerializers.field_187192_b);
    private PotionType field_184502_e;
    public List<PotionEffect> field_184503_f;
    private final Map<Entity, Integer> field_184504_g;
    private int field_184505_h;
    public int field_184506_as;
    public int field_184507_at;
    private boolean field_184508_au;
    public int field_184509_av;
    public float field_184510_aw;
    public float field_184511_ax;
    private EntityLivingBase field_184512_ay;
    private UUID field_184513_az;

    public EntityAreaEffectCloud(World world) {
        super(world);
        this.field_184502_e = PotionTypes.field_185229_a;
        this.field_184503_f = Lists.newArrayList();
        this.field_184504_g = Maps.newHashMap();
        this.field_184505_h = 600;
        this.field_184506_as = 20;
        this.field_184507_at = 20;
        this.field_70145_X = true;
        this.field_70178_ae = true;
        this.func_184483_a(3.0F);
    }

    public EntityAreaEffectCloud(World world, double d0, double d1, double d2) {
        this(world);
        this.func_70107_b(d0, d1, d2);
    }

    protected void func_70088_a() {
        this.func_184212_Q().func_187214_a(EntityAreaEffectCloud.field_184499_b, Integer.valueOf(0));
        this.func_184212_Q().func_187214_a(EntityAreaEffectCloud.field_184498_a, Float.valueOf(0.5F));
        this.func_184212_Q().func_187214_a(EntityAreaEffectCloud.field_184500_c, Boolean.valueOf(false));
        this.func_184212_Q().func_187214_a(EntityAreaEffectCloud.field_184501_d, Integer.valueOf(EnumParticleTypes.SPELL_MOB.func_179348_c()));
        this.func_184212_Q().func_187214_a(EntityAreaEffectCloud.field_189736_e, Integer.valueOf(0));
        this.func_184212_Q().func_187214_a(EntityAreaEffectCloud.field_189737_f, Integer.valueOf(0));
    }

    public void func_184483_a(float f) {
        double d0 = this.field_70165_t;
        double d1 = this.field_70163_u;
        double d2 = this.field_70161_v;

        this.func_70105_a(f * 2.0F, 0.5F);
        this.func_70107_b(d0, d1, d2);
        if (!this.field_70170_p.field_72995_K) {
            this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_184498_a, Float.valueOf(f));
        }

    }

    public float func_184490_j() {
        return ((Float) this.func_184212_Q().func_187225_a(EntityAreaEffectCloud.field_184498_a)).floatValue();
    }

    public void func_184484_a(PotionType potionregistry) {
        this.field_184502_e = potionregistry;
        if (!this.field_184508_au) {
            this.func_190618_C();
        }

    }

    private void func_190618_C() {
        if (this.field_184502_e == PotionTypes.field_185229_a && this.field_184503_f.isEmpty()) {
            this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_184499_b, Integer.valueOf(0));
        } else {
            this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_184499_b, Integer.valueOf(PotionUtils.func_185181_a((Collection) PotionUtils.func_185186_a(this.field_184502_e, (Collection) this.field_184503_f))));
        }

    }

    public void func_184496_a(PotionEffect mobeffect) {
        this.field_184503_f.add(mobeffect);
        if (!this.field_184508_au) {
            this.func_190618_C();
        }

    }

    // CraftBukkit start accessor methods
    public void refreshEffects() {
        if (!this.field_184508_au) {
            this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_184499_b, Integer.valueOf(PotionUtils.func_185181_a((Collection) PotionUtils.func_185186_a(this.field_184502_e, (Collection) this.field_184503_f)))); // PAIL: rename
        }
    }

    public String getType() {
        return ((ResourceLocation) PotionType.field_185176_a.func_177774_c(this.field_184502_e)).toString(); // PAIL: rename
    }

    public void setType(String string) {
        func_184484_a(PotionType.field_185176_a.func_82594_a(new ResourceLocation(string))); // PAIL: rename
    }
    // CraftBukkit end

    public int func_184492_k() {
        return ((Integer) this.func_184212_Q().func_187225_a(EntityAreaEffectCloud.field_184499_b)).intValue();
    }

    public void func_184482_a(int i) {
        this.field_184508_au = true;
        this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_184499_b, Integer.valueOf(i));
    }

    public EnumParticleTypes func_184493_l() {
        return EnumParticleTypes.func_179342_a(((Integer) this.func_184212_Q().func_187225_a(EntityAreaEffectCloud.field_184501_d)).intValue());
    }

    public void func_184491_a(EnumParticleTypes enumparticle) {
        this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_184501_d, Integer.valueOf(enumparticle.func_179348_c()));
    }

    public int func_189733_n() {
        return ((Integer) this.func_184212_Q().func_187225_a(EntityAreaEffectCloud.field_189736_e)).intValue();
    }

    public void func_189734_b(int i) {
        this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_189736_e, Integer.valueOf(i));
    }

    public int func_189735_o() {
        return ((Integer) this.func_184212_Q().func_187225_a(EntityAreaEffectCloud.field_189737_f)).intValue();
    }

    public void func_189732_d(int i) {
        this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_189737_f, Integer.valueOf(i));
    }

    protected void func_184488_a(boolean flag) {
        this.func_184212_Q().func_187227_b(EntityAreaEffectCloud.field_184500_c, Boolean.valueOf(flag));
    }

    public boolean func_184497_n() {
        return ((Boolean) this.func_184212_Q().func_187225_a(EntityAreaEffectCloud.field_184500_c)).booleanValue();
    }

    public int func_184489_o() {
        return this.field_184505_h;
    }

    public void func_184486_b(int i) {
        this.field_184505_h = i;
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        boolean flag = this.func_184497_n();
        float f = this.func_184490_j();

        if (this.field_70170_p.field_72995_K) {
            EnumParticleTypes enumparticle = this.func_184493_l();
            int[] aint = new int[enumparticle.func_179345_d()];

            if (aint.length > 0) {
                aint[0] = this.func_189733_n();
            }

            if (aint.length > 1) {
                aint[1] = this.func_189735_o();
            }

            float f1;
            float f2;
            float f3;
            int i;
            int j;
            int k;

            if (flag) {
                if (this.field_70146_Z.nextBoolean()) {
                    for (int l = 0; l < 2; ++l) {
                        float f4 = this.field_70146_Z.nextFloat() * 6.2831855F;

                        f1 = MathHelper.func_76129_c(this.field_70146_Z.nextFloat()) * 0.2F;
                        f2 = MathHelper.func_76134_b(f4) * f1;
                        f3 = MathHelper.func_76126_a(f4) * f1;
                        if (enumparticle == EnumParticleTypes.SPELL_MOB) {
                            int i1 = this.field_70146_Z.nextBoolean() ? 16777215 : this.func_184492_k();

                            i = i1 >> 16 & 255;
                            j = i1 >> 8 & 255;
                            k = i1 & 255;
                            this.field_70170_p.func_190523_a(EnumParticleTypes.SPELL_MOB.func_179348_c(), this.field_70165_t + (double) f2, this.field_70163_u, this.field_70161_v + (double) f3, (double) ((float) i / 255.0F), (double) ((float) j / 255.0F), (double) ((float) k / 255.0F), new int[0]);
                        } else {
                            this.field_70170_p.func_190523_a(enumparticle.func_179348_c(), this.field_70165_t + (double) f2, this.field_70163_u, this.field_70161_v + (double) f3, 0.0D, 0.0D, 0.0D, aint);
                        }
                    }
                }
            } else {
                float f5 = 3.1415927F * f * f;

                for (int j1 = 0; (float) j1 < f5; ++j1) {
                    f1 = this.field_70146_Z.nextFloat() * 6.2831855F;
                    f2 = MathHelper.func_76129_c(this.field_70146_Z.nextFloat()) * f;
                    f3 = MathHelper.func_76134_b(f1) * f2;
                    float f6 = MathHelper.func_76126_a(f1) * f2;

                    if (enumparticle == EnumParticleTypes.SPELL_MOB) {
                        i = this.func_184492_k();
                        j = i >> 16 & 255;
                        k = i >> 8 & 255;
                        int k1 = i & 255;

                        this.field_70170_p.func_190523_a(EnumParticleTypes.SPELL_MOB.func_179348_c(), this.field_70165_t + (double) f3, this.field_70163_u, this.field_70161_v + (double) f6, (double) ((float) j / 255.0F), (double) ((float) k / 255.0F), (double) ((float) k1 / 255.0F), new int[0]);
                    } else {
                        this.field_70170_p.func_190523_a(enumparticle.func_179348_c(), this.field_70165_t + (double) f3, this.field_70163_u, this.field_70161_v + (double) f6, (0.5D - this.field_70146_Z.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.field_70146_Z.nextDouble()) * 0.15D, aint);
                    }
                }
            }
        } else {
            if (this.field_70173_aa >= this.field_184506_as + this.field_184505_h) {
                this.func_70106_y();
                return;
            }

            boolean flag1 = this.field_70173_aa < this.field_184506_as;

            if (flag != flag1) {
                this.func_184488_a(flag1);
            }

            if (flag1) {
                return;
            }

            if (this.field_184511_ax != 0.0F) {
                f += this.field_184511_ax;
                if (f < 0.5F) {
                    this.func_70106_y();
                    return;
                }

                this.func_184483_a(f);
            }

            if (this.field_70173_aa % 5 == 0) {
                Iterator iterator = this.field_184504_g.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();

                    if (this.field_70173_aa >= ((Integer) entry.getValue()).intValue()) {
                        iterator.remove();
                    }
                }

                ArrayList arraylist = Lists.newArrayList();
                Iterator iterator1 = this.field_184502_e.func_185170_a().iterator();

                while (iterator1.hasNext()) {
                    PotionEffect mobeffect = (PotionEffect) iterator1.next();

                    arraylist.add(new PotionEffect(mobeffect.func_188419_a(), mobeffect.func_76459_b() / 4, mobeffect.func_76458_c(), mobeffect.func_82720_e(), mobeffect.func_188418_e()));
                }

                arraylist.addAll(this.field_184503_f);
                if (arraylist.isEmpty()) {
                    this.field_184504_g.clear();
                } else {
                    List list = this.field_70170_p.func_72872_a(EntityLivingBase.class, this.func_174813_aQ());

                    if (!list.isEmpty()) {
                        Iterator iterator2 = list.iterator();

                        List<LivingEntity> entities = new ArrayList<LivingEntity>(); // CraftBukkit
                        while (iterator2.hasNext()) {
                            EntityLivingBase entityliving = (EntityLivingBase) iterator2.next();

                            if (!this.field_184504_g.containsKey(entityliving) && entityliving.func_184603_cC()) {
                                double d0 = entityliving.field_70165_t - this.field_70165_t;
                                double d1 = entityliving.field_70161_v - this.field_70161_v;
                                double d2 = d0 * d0 + d1 * d1;

                                if (d2 <= (double) (f * f)) {
                                    // CraftBukkit start
                                    entities.add((LivingEntity) entityliving.getBukkitEntity());
                                }
                            }
                        }
                        org.bukkit.event.entity.AreaEffectCloudApplyEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callAreaEffectCloudApplyEvent(this, entities);
                        if (true) { // Preserve NMS spacing and bracket count for smallest diff
                            for (LivingEntity entity : event.getAffectedEntities()) {
                                if (entity instanceof CraftLivingEntity) {
                                    EntityLivingBase entityliving = ((CraftLivingEntity) entity).getHandle();
                                    // CraftBukkit end
                                    this.field_184504_g.put(entityliving, Integer.valueOf(this.field_70173_aa + this.field_184507_at));
                                    Iterator iterator3 = arraylist.iterator();

                                    while (iterator3.hasNext()) {
                                        PotionEffect mobeffect1 = (PotionEffect) iterator3.next();

                                        if (mobeffect1.func_188419_a().func_76403_b()) {
                                            mobeffect1.func_188419_a().func_180793_a(this, this.func_184494_w(), entityliving, mobeffect1.func_76458_c(), 0.5D);
                                        } else {
                                            entityliving.func_70690_d(new PotionEffect(mobeffect1));
                                        }
                                    }

                                    if (this.field_184510_aw != 0.0F) {
                                        f += this.field_184510_aw;
                                        if (f < 0.5F) {
                                            this.func_70106_y();
                                            return;
                                        }

                                        this.func_184483_a(f);
                                    }

                                    if (this.field_184509_av != 0) {
                                        this.field_184505_h += this.field_184509_av;
                                        if (this.field_184505_h <= 0) {
                                            this.func_70106_y();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public void func_184495_b(float f) {
        this.field_184510_aw = f;
    }

    public void func_184487_c(float f) {
        this.field_184511_ax = f;
    }

    public void func_184485_d(int i) {
        this.field_184506_as = i;
    }

    public void func_184481_a(@Nullable EntityLivingBase entityliving) {
        this.field_184512_ay = entityliving;
        this.field_184513_az = entityliving == null ? null : entityliving.func_110124_au();
    }

    @Nullable
    public EntityLivingBase func_184494_w() {
        if (this.field_184512_ay == null && this.field_184513_az != null && this.field_70170_p instanceof WorldServer) {
            Entity entity = ((WorldServer) this.field_70170_p).func_175733_a(this.field_184513_az);

            if (entity instanceof EntityLivingBase) {
                this.field_184512_ay = (EntityLivingBase) entity;
            }
        }

        return this.field_184512_ay;
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        this.field_70173_aa = nbttagcompound.func_74762_e("Age");
        this.field_184505_h = nbttagcompound.func_74762_e("Duration");
        this.field_184506_as = nbttagcompound.func_74762_e("WaitTime");
        this.field_184507_at = nbttagcompound.func_74762_e("ReapplicationDelay");
        this.field_184509_av = nbttagcompound.func_74762_e("DurationOnUse");
        this.field_184510_aw = nbttagcompound.func_74760_g("RadiusOnUse");
        this.field_184511_ax = nbttagcompound.func_74760_g("RadiusPerTick");
        this.func_184483_a(nbttagcompound.func_74760_g("Radius"));
        this.field_184513_az = nbttagcompound.func_186857_a("OwnerUUID");
        if (nbttagcompound.func_150297_b("Particle", 8)) {
            EnumParticleTypes enumparticle = EnumParticleTypes.func_186831_a(nbttagcompound.func_74779_i("Particle"));

            if (enumparticle != null) {
                this.func_184491_a(enumparticle);
                this.func_189734_b(nbttagcompound.func_74762_e("ParticleParam1"));
                this.func_189732_d(nbttagcompound.func_74762_e("ParticleParam2"));
            }
        }

        if (nbttagcompound.func_150297_b("Color", 99)) {
            this.func_184482_a(nbttagcompound.func_74762_e("Color"));
        }

        if (nbttagcompound.func_150297_b("Potion", 8)) {
            this.func_184484_a(PotionUtils.func_185187_c(nbttagcompound));
        }

        if (nbttagcompound.func_150297_b("Effects", 9)) {
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("Effects", 10);

            this.field_184503_f.clear();

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                PotionEffect mobeffect = PotionEffect.func_82722_b(nbttaglist.func_150305_b(i));

                if (mobeffect != null) {
                    this.func_184496_a(mobeffect);
                }
            }
        }

    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("Age", this.field_70173_aa);
        nbttagcompound.func_74768_a("Duration", this.field_184505_h);
        nbttagcompound.func_74768_a("WaitTime", this.field_184506_as);
        nbttagcompound.func_74768_a("ReapplicationDelay", this.field_184507_at);
        nbttagcompound.func_74768_a("DurationOnUse", this.field_184509_av);
        nbttagcompound.func_74776_a("RadiusOnUse", this.field_184510_aw);
        nbttagcompound.func_74776_a("RadiusPerTick", this.field_184511_ax);
        nbttagcompound.func_74776_a("Radius", this.func_184490_j());
        nbttagcompound.func_74778_a("Particle", this.func_184493_l().func_179346_b());
        nbttagcompound.func_74768_a("ParticleParam1", this.func_189733_n());
        nbttagcompound.func_74768_a("ParticleParam2", this.func_189735_o());
        if (this.field_184513_az != null) {
            nbttagcompound.func_186854_a("OwnerUUID", this.field_184513_az);
        }

        if (this.field_184508_au) {
            nbttagcompound.func_74768_a("Color", this.func_184492_k());
        }

        if (this.field_184502_e != PotionTypes.field_185229_a && this.field_184502_e != null) {
            nbttagcompound.func_74778_a("Potion", ((ResourceLocation) PotionType.field_185176_a.func_177774_c(this.field_184502_e)).toString());
        }

        if (!this.field_184503_f.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.field_184503_f.iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                nbttaglist.func_74742_a(mobeffect.func_82719_a(new NBTTagCompound()));
            }

            nbttagcompound.func_74782_a("Effects", nbttaglist);
        }

    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityAreaEffectCloud.field_184498_a.equals(datawatcherobject)) {
            this.func_184483_a(this.func_184490_j());
        }

        super.func_184206_a(datawatcherobject);
    }

    public EnumPushReaction func_184192_z() {
        return EnumPushReaction.IGNORE;
    }
}
