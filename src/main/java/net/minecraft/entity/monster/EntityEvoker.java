package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEvoker extends EntitySpellcasterIllager {

    private EntitySheep field_190763_bw;

    public EntityEvoker(World world) {
        super(world);
        this.func_70105_a(0.6F, 1.95F);
        this.field_70728_aV = 10;
    }

    @Override
    protected void func_184651_r() {
        super.func_184651_r();
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(1, new EntityEvoker.b(null));
        this.field_70714_bg.func_75776_a(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
        this.field_70714_bg.func_75776_a(4, new EntityEvoker.c(null));
        this.field_70714_bg.func_75776_a(5, new EntityEvoker.a(null));
        this.field_70714_bg.func_75776_a(6, new EntityEvoker.d());
        this.field_70714_bg.func_75776_a(8, new EntityAIWander(this, 0.6D));
        this.field_70714_bg.func_75776_a(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.field_70714_bg.func_75776_a(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityEvoker.class}));
        this.field_70715_bh.func_75776_a(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).func_190882_b(300));
        this.field_70715_bh.func_75776_a(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).func_190882_b(300));
        this.field_70715_bh.func_75776_a(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
    }

    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.5D);
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(12.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(24.0D);
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
    }

    public static void func_190759_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityEvoker.class);
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
    }

    @Override
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_191185_au;
    }

    @Override
    protected void func_70619_bc() {
        super.func_70619_bc();
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
    }

    @Override
    public boolean func_184191_r(Entity entity) {
        return entity == null ? false : (entity == this ? true : (super.func_184191_r(entity) ? true : (entity instanceof EntityVex ? this.func_184191_r(((EntityVex) entity).func_190645_o()) : (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).func_70668_bt() == EnumCreatureAttribute.ILLAGER ? this.func_96124_cp() == null && entity.func_96124_cp() == null : false))));
    }

    @Override
    protected SoundEvent func_184639_G() {
        return SoundEvents.field_191243_bm;
    }

    @Override
    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_191245_bo;
    }

    @Override
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_191246_bp;
    }

    private void func_190748_a(@Nullable EntitySheep entitysheep) {
        this.field_190763_bw = entitysheep;
    }

    @Nullable
    private EntitySheep func_190751_dj() {
        return this.field_190763_bw;
    }

    @Override
    protected SoundEvent func_193086_dk() {
        return SoundEvents.field_191244_bn;
    }

    public class d extends EntitySpellcasterIllager.c {

        final Predicate<EntitySheep> a = new Predicate() {
            public boolean a(EntitySheep entitysheep) {
                return entitysheep.func_175509_cj() == EnumDyeColor.BLUE;
            }

            @Override
            public boolean apply(Object object) {
                return this.a((EntitySheep) object);
            }
        };

        public d() {
            super();
        }

        @Override
        public boolean func_75250_a() {
            if (EntityEvoker.this.func_70638_az() != null) {
                return false;
            } else if (EntityEvoker.this.func_193082_dl()) {
                return false;
            } else if (EntityEvoker.this.field_70173_aa < this.d) {
                return false;
            } else if (!EntityEvoker.this.field_70170_p.func_82736_K().func_82766_b("mobGriefing")) {
                return false;
            } else {
                List list = EntityEvoker.this.field_70170_p.func_175647_a(EntitySheep.class, EntityEvoker.this.func_174813_aQ().func_72314_b(16.0D, 4.0D, 16.0D), this.a);

                if (list.isEmpty()) {
                    return false;
                } else {
                    EntityEvoker.this.func_190748_a((EntitySheep) list.get(EntityEvoker.this.field_70146_Z.nextInt(list.size())));
                    return true;
                }
            }
        }

        @Override
        public boolean func_75253_b() {
            return EntityEvoker.this.func_190751_dj() != null && this.c > 0;
        }

        @Override
        public void func_75251_c() {
            super.func_75251_c();
            EntityEvoker.this.func_190748_a((EntitySheep) null);
        }

        @Override
        protected void j() {
            EntitySheep entitysheep = EntityEvoker.this.func_190751_dj();

            if (entitysheep != null && entitysheep.func_70089_S()) {
                entitysheep.func_175512_b(EnumDyeColor.RED);
            }

        }

        @Override
        protected int m() {
            return 40;
        }

        @Override
        protected int f() {
            return 60;
        }

        @Override
        protected int i() {
            return 140;
        }

        @Override
        protected SoundEvent k() {
            return SoundEvents.field_191249_bs;
        }

        @Override
        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.WOLOLO;
        }
    }

    class c extends EntitySpellcasterIllager.c {

        private c() {
            super();
        }

        @Override
        public boolean func_75250_a() {
            if (!super.func_75250_a()) {
                return false;
            } else {
                int i = EntityEvoker.this.field_70170_p.func_72872_a(EntityVex.class, EntityEvoker.this.func_174813_aQ().func_186662_g(16.0D)).size();

                return EntityEvoker.this.field_70146_Z.nextInt(8) + 1 > i;
            }
        }

        @Override
        protected int f() {
            return 100;
        }

        @Override
        protected int i() {
            return 340;
        }

        @Override
        protected void j() {
            for (int i = 0; i < 3; ++i) {
                BlockPos blockposition = (new BlockPos(EntityEvoker.this)).func_177982_a(-2 + EntityEvoker.this.field_70146_Z.nextInt(5), 1, -2 + EntityEvoker.this.field_70146_Z.nextInt(5));
                EntityVex entityvex = new EntityVex(EntityEvoker.this.field_70170_p);

                entityvex.func_174828_a(blockposition, 0.0F, 0.0F);
                entityvex.func_180482_a(EntityEvoker.this.field_70170_p.func_175649_E(blockposition), (IEntityLivingData) null);
                entityvex.func_190658_a(EntityEvoker.this);
                entityvex.func_190651_g(blockposition);
                entityvex.func_190653_a(20 * (30 + EntityEvoker.this.field_70146_Z.nextInt(90)));
                EntityEvoker.this.field_70170_p.func_72838_d(entityvex);
            }

        }

        @Override
        protected SoundEvent k() {
            return SoundEvents.field_191248_br;
        }

        @Override
        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
        }

        c(Object object) {
            this();
        }
    }

    class a extends EntitySpellcasterIllager.c {

        private a() {
            super();
        }

        @Override
        protected int f() {
            return 40;
        }

        @Override
        protected int i() {
            return 100;
        }

        @Override
        protected void j() {
            EntityLivingBase entityliving = EntityEvoker.this.func_70638_az();
            double d0 = Math.min(entityliving.field_70163_u, EntityEvoker.this.field_70163_u);
            double d1 = Math.max(entityliving.field_70163_u, EntityEvoker.this.field_70163_u) + 1.0D;
            float f = (float) MathHelper.func_181159_b(entityliving.field_70161_v - EntityEvoker.this.field_70161_v, entityliving.field_70165_t - EntityEvoker.this.field_70165_t);
            int i;

            if (EntityEvoker.this.func_70068_e(entityliving) < 9.0D) {
                float f1;

                for (i = 0; i < 5; ++i) {
                    f1 = f + i * 3.1415927F * 0.4F;
                    this.a(EntityEvoker.this.field_70165_t + MathHelper.func_76134_b(f1) * 1.5D, EntityEvoker.this.field_70161_v + MathHelper.func_76126_a(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (i = 0; i < 8; ++i) {
                    f1 = f + i * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
                    this.a(EntityEvoker.this.field_70165_t + MathHelper.func_76134_b(f1) * 2.5D, EntityEvoker.this.field_70161_v + MathHelper.func_76126_a(f1) * 2.5D, d0, d1, f1, 3);
                }
            } else {
                for (i = 0; i < 16; ++i) {
                    double d2 = 1.25D * (i + 1);
                    int j = 1 * i;

                    this.a(EntityEvoker.this.field_70165_t + MathHelper.func_76134_b(f) * d2, EntityEvoker.this.field_70161_v + MathHelper.func_76126_a(f) * d2, d0, d1, f, j);
                }
            }

        }

        private void a(double d0, double d1, double d2, double d3, float f, int i) {
            BlockPos blockposition = new BlockPos(d0, d3, d1);
            boolean flag = false;
            double d4 = 0.0D;

            do {
                if (!EntityEvoker.this.field_70170_p.func_175677_d(blockposition, true) && EntityEvoker.this.field_70170_p.func_175677_d(blockposition.func_177977_b(), true)) {
                    if (!EntityEvoker.this.field_70170_p.func_175623_d(blockposition)) {
                        IBlockState iblockdata = EntityEvoker.this.field_70170_p.func_180495_p(blockposition);
                        AxisAlignedBB axisalignedbb = iblockdata.func_185890_d(EntityEvoker.this.field_70170_p, blockposition);

                        if (axisalignedbb != null) {
                            d4 = axisalignedbb.field_72337_e;
                        }
                    }

                    flag = true;
                    break;
                }

                blockposition = blockposition.func_177977_b();
            } while (blockposition.func_177956_o() >= MathHelper.func_76128_c(d2) - 1);

            if (flag) {
                EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(EntityEvoker.this.field_70170_p, d0, blockposition.func_177956_o() + d4, d1, f, i, EntityEvoker.this);

                EntityEvoker.this.field_70170_p.func_72838_d(entityevokerfangs);
            }

        }

        @Override
        protected SoundEvent k() {
            return SoundEvents.field_191247_bq;
        }

        @Override
        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.FANGS;
        }

        a(Object object) {
            this();
        }
    }

    class b extends EntitySpellcasterIllager.b {

        private b() {
            super();
        }

        @Override
        public void func_75246_d() {
            if (EntityEvoker.this.func_70638_az() != null) {
                EntityEvoker.this.func_70671_ap().func_75651_a(EntityEvoker.this.func_70638_az(), EntityEvoker.this.func_184649_cE(), EntityEvoker.this.func_70646_bf());
            } else if (EntityEvoker.this.func_190751_dj() != null) {
                EntityEvoker.this.func_70671_ap().func_75651_a(EntityEvoker.this.func_190751_dj(), EntityEvoker.this.func_184649_cE(), EntityEvoker.this.func_70646_bf());
            }

        }

        b(Object object) {
            this();
        }
    }
}
