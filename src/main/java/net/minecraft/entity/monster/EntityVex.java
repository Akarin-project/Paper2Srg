package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import org.bukkit.event.entity.EntityTargetEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.EntityVex.a;
import net.minecraft.server.EntityVex.b;
import net.minecraft.server.EntityVex.c;
import net.minecraft.server.EntityVex.d;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVex extends EntityMob {

    protected static final DataParameter<Byte> field_190664_a = EntityDataManager.func_187226_a(EntityVex.class, DataSerializers.field_187191_a);
    private EntityLiving field_190665_b;
    @Nullable
    private BlockPos field_190666_c;
    private boolean field_190667_bw;
    private int field_190668_bx;

    public EntityVex(World world) {
        super(world);
        this.field_70178_ae = true;
        this.field_70765_h = new EntityVex.c(this);
        this.func_70105_a(0.4F, 0.8F);
        this.field_70728_aV = 3;
    }

    public void func_70091_d(MoverType enummovetype, double d0, double d1, double d2) {
        super.func_70091_d(enummovetype, d0, d1, d2);
        this.func_145775_I();
    }

    public void func_70071_h_() {
        this.field_70145_X = true;
        super.func_70071_h_();
        this.field_70145_X = false;
        this.func_189654_d(true);
        if (this.field_190667_bw && --this.field_190668_bx <= 0) {
            this.field_190668_bx = 20;
            this.func_70097_a(DamageSource.field_76366_f, 1.0F);
        }

    }

    protected void func_184651_r() {
        super.func_184651_r();
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(4, new EntityVex.a());
        this.field_70714_bg.func_75776_a(8, new EntityVex.d());
        this.field_70714_bg.func_75776_a(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.field_70714_bg.func_75776_a(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityVex.class}));
        this.field_70715_bh.func_75776_a(2, new EntityVex.b(this));
        this.field_70715_bh.func_75776_a(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(14.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(4.0D);
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityVex.field_190664_a, Byte.valueOf((byte) 0));
    }

    public static void func_190663_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityVex.class);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        if (nbttagcompound.func_74764_b("BoundX")) {
            this.field_190666_c = new BlockPos(nbttagcompound.func_74762_e("BoundX"), nbttagcompound.func_74762_e("BoundY"), nbttagcompound.func_74762_e("BoundZ"));
        }

        if (nbttagcompound.func_74764_b("LifeTicks")) {
            this.func_190653_a(nbttagcompound.func_74762_e("LifeTicks"));
        }

    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        if (this.field_190666_c != null) {
            nbttagcompound.func_74768_a("BoundX", this.field_190666_c.func_177958_n());
            nbttagcompound.func_74768_a("BoundY", this.field_190666_c.func_177956_o());
            nbttagcompound.func_74768_a("BoundZ", this.field_190666_c.func_177952_p());
        }

        if (this.field_190667_bw) {
            nbttagcompound.func_74768_a("LifeTicks", this.field_190668_bx);
        }

    }

    public EntityLiving func_190645_o() {
        return this.field_190665_b;
    }

    @Nullable
    public BlockPos func_190646_di() {
        return this.field_190666_c;
    }

    public void func_190651_g(@Nullable BlockPos blockposition) {
        this.field_190666_c = blockposition;
    }

    private boolean func_190656_b(int i) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityVex.field_190664_a)).byteValue();

        return (b0 & i) != 0;
    }

    private void func_190660_a(int i, boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityVex.field_190664_a)).byteValue();
        int j;

        if (flag) {
            j = b0 | i;
        } else {
            j = b0 & ~i;
        }

        this.field_70180_af.func_187227_b(EntityVex.field_190664_a, Byte.valueOf((byte) (j & 255)));
    }

    public boolean func_190647_dj() {
        return this.func_190656_b(1);
    }

    public void func_190648_a(boolean flag) {
        this.func_190660_a(1, flag);
    }

    public void func_190658_a(EntityLiving entityinsentient) {
        this.field_190665_b = entityinsentient;
    }

    public void func_190653_a(int i) {
        this.field_190667_bw = true;
        this.field_190668_bx = i;
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_191264_hc;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_191266_he;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_191267_hf;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_191188_ax;
    }

    public float func_70013_c() {
        return 1.0F;
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.func_180481_a(difficultydamagescaler);
        this.func_180483_b(difficultydamagescaler);
        return super.func_180482_a(difficultydamagescaler, groupdataentity);
    }

    protected void func_180481_a(DifficultyInstance difficultydamagescaler) {
        this.func_184201_a(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.field_151040_l));
        this.func_184642_a(EntityEquipmentSlot.MAINHAND, 0.0F);
    }

    class b extends EntityAITarget {

        public b(EntityCreature entitycreature) {
            super(entitycreature, false);
        }

        public boolean func_75250_a() {
            return EntityVex.this.field_190665_b != null && EntityVex.this.field_190665_b.func_70638_az() != null && this.func_75296_a(EntityVex.this.field_190665_b.func_70638_az(), false);
        }

        public void func_75249_e() {
            EntityVex.this.setGoalTarget(EntityVex.this.field_190665_b.func_70638_az(), EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true); // CraftBukkit
            super.func_75249_e();
        }
    }

    class d extends EntityAIBase {

        public d() {
            this.func_75248_a(1);
        }

        public boolean func_75250_a() {
            return !EntityVex.this.func_70605_aq().func_75640_a() && EntityVex.this.field_70146_Z.nextInt(7) == 0;
        }

        public boolean func_75253_b() {
            return false;
        }

        public void func_75246_d() {
            BlockPos blockposition = EntityVex.this.func_190646_di();

            if (blockposition == null) {
                blockposition = new BlockPos(EntityVex.this);
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockposition1 = blockposition.func_177982_a(EntityVex.this.field_70146_Z.nextInt(15) - 7, EntityVex.this.field_70146_Z.nextInt(11) - 5, EntityVex.this.field_70146_Z.nextInt(15) - 7);

                if (EntityVex.this.field_70170_p.func_175623_d(blockposition1)) {
                    EntityVex.this.field_70765_h.func_75642_a((double) blockposition1.func_177958_n() + 0.5D, (double) blockposition1.func_177956_o() + 0.5D, (double) blockposition1.func_177952_p() + 0.5D, 0.25D);
                    if (EntityVex.this.func_70638_az() == null) {
                        EntityVex.this.func_70671_ap().func_75650_a((double) blockposition1.func_177958_n() + 0.5D, (double) blockposition1.func_177956_o() + 0.5D, (double) blockposition1.func_177952_p() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class a extends EntityAIBase {

        public a() {
            this.func_75248_a(1);
        }

        public boolean func_75250_a() {
            return EntityVex.this.func_70638_az() != null && !EntityVex.this.func_70605_aq().func_75640_a() && EntityVex.this.field_70146_Z.nextInt(7) == 0 ? EntityVex.this.func_70068_e((Entity) EntityVex.this.func_70638_az()) > 4.0D : false;
        }

        public boolean func_75253_b() {
            return EntityVex.this.func_70605_aq().func_75640_a() && EntityVex.this.func_190647_dj() && EntityVex.this.func_70638_az() != null && EntityVex.this.func_70638_az().func_70089_S();
        }

        public void func_75249_e() {
            EntityLivingBase entityliving = EntityVex.this.func_70638_az();
            Vec3d vec3d = entityliving.func_174824_e(1.0F);

            EntityVex.this.field_70765_h.func_75642_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c, 1.0D);
            EntityVex.this.func_190648_a(true);
            EntityVex.this.func_184185_a(SoundEvents.field_191265_hd, 1.0F, 1.0F);
        }

        public void func_75251_c() {
            EntityVex.this.func_190648_a(false);
        }

        public void func_75246_d() {
            EntityLivingBase entityliving = EntityVex.this.func_70638_az();

            if (EntityVex.this.func_174813_aQ().func_72326_a(entityliving.func_174813_aQ())) {
                EntityVex.this.func_70652_k(entityliving);
                EntityVex.this.func_190648_a(false);
            } else {
                double d0 = EntityVex.this.func_70068_e((Entity) entityliving);

                if (d0 < 9.0D) {
                    Vec3d vec3d = entityliving.func_174824_e(1.0F);

                    EntityVex.this.field_70765_h.func_75642_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c, 1.0D);
                }
            }

        }
    }

    class c extends EntityMoveHelper {

        public c(EntityVex entityvex) {
            super(entityvex);
        }

        public void func_75641_c() {
            if (this.field_188491_h == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.field_75646_b - EntityVex.this.field_70165_t;
                double d1 = this.field_75647_c - EntityVex.this.field_70163_u;
                double d2 = this.field_75644_d - EntityVex.this.field_70161_v;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                d3 = (double) MathHelper.func_76133_a(d3);
                if (d3 < EntityVex.this.func_174813_aQ().func_72320_b()) {
                    this.field_188491_h = EntityMoveHelper.Action.WAIT;
                    EntityVex.this.field_70159_w *= 0.5D;
                    EntityVex.this.field_70181_x *= 0.5D;
                    EntityVex.this.field_70179_y *= 0.5D;
                } else {
                    EntityVex.this.field_70159_w += d0 / d3 * 0.05D * this.field_75645_e;
                    EntityVex.this.field_70181_x += d1 / d3 * 0.05D * this.field_75645_e;
                    EntityVex.this.field_70179_y += d2 / d3 * 0.05D * this.field_75645_e;
                    if (EntityVex.this.func_70638_az() == null) {
                        EntityVex.this.field_70177_z = -((float) MathHelper.func_181159_b(EntityVex.this.field_70159_w, EntityVex.this.field_70179_y)) * 57.295776F;
                        EntityVex.this.field_70761_aq = EntityVex.this.field_70177_z;
                    } else {
                        double d4 = EntityVex.this.func_70638_az().field_70165_t - EntityVex.this.field_70165_t;
                        double d5 = EntityVex.this.func_70638_az().field_70161_v - EntityVex.this.field_70161_v;

                        EntityVex.this.field_70177_z = -((float) MathHelper.func_181159_b(d4, d5)) * 57.295776F;
                        EntityVex.this.field_70761_aq = EntityVex.this.field_70177_z;
                    }
                }

            }
        }
    }
}
