package net.minecraft.entity.passive;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public abstract class AbstractHorse extends EntityAnimal implements IInventoryChangedListener, IJumpingMount {

    private static final Predicate<Entity> field_110276_bu = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof AbstractHorse && ((AbstractHorse) entity).func_110205_ce();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final IAttribute field_110271_bv = (new RangedAttribute((IAttribute) null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D)).func_111117_a("Jump Strength").func_111112_a(true);
    private static final DataParameter<Byte> field_184787_bE = EntityDataManager.func_187226_a(AbstractHorse.class, DataSerializers.field_187191_a);
    private static final DataParameter<Optional<UUID>> field_184790_bH = EntityDataManager.func_187226_a(AbstractHorse.class, DataSerializers.field_187203_m);
    private int field_190689_bJ;
    private int field_110290_bE;
    private int field_110295_bF;
    public int field_110278_bp;
    public int field_110279_bq;
    protected boolean field_110275_br;
    public ContainerHorseChest field_110296_bG;
    protected int field_110274_bs;
    protected float field_110277_bt;
    private boolean field_110294_bI;
    private float field_110283_bJ;
    private float field_110284_bK;
    private float field_110281_bL;
    private float field_110282_bM;
    private float field_110287_bN;
    private float field_110288_bO;
    protected boolean field_190688_bE = true;
    protected int field_110285_bP;
    public int maxDomestication = 100; // CraftBukkit - store max domestication value

    public AbstractHorse(World world) {
        super(world);
        this.func_70105_a(1.3964844F, 1.6F);
        this.field_70138_W = 1.0F;
        this.func_110226_cD();
    }

    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(1, new EntityAIPanic(this, 1.2D));
        this.field_70714_bg.func_75776_a(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.field_70714_bg.func_75776_a(2, new EntityAIMate(this, 1.0D, AbstractHorse.class));
        this.field_70714_bg.func_75776_a(4, new EntityAIFollowParent(this, 1.0D));
        this.field_70714_bg.func_75776_a(6, new EntityAIWanderAvoidWater(this, 0.7D));
        this.field_70714_bg.func_75776_a(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(AbstractHorse.field_184787_bE, Byte.valueOf((byte) 0));
        this.field_70180_af.func_187214_a(AbstractHorse.field_184790_bH, Optional.absent());
    }

    protected boolean func_110233_w(int i) {
        return (((Byte) this.field_70180_af.func_187225_a(AbstractHorse.field_184787_bE)).byteValue() & i) != 0;
    }

    protected void func_110208_b(int i, boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(AbstractHorse.field_184787_bE)).byteValue();

        if (flag) {
            this.field_70180_af.func_187227_b(AbstractHorse.field_184787_bE, Byte.valueOf((byte) (b0 | i)));
        } else {
            this.field_70180_af.func_187227_b(AbstractHorse.field_184787_bE, Byte.valueOf((byte) (b0 & ~i)));
        }

    }

    public boolean func_110248_bS() {
        return this.func_110233_w(2);
    }

    @Nullable
    public UUID func_184780_dh() {
        return (UUID) ((Optional) this.field_70180_af.func_187225_a(AbstractHorse.field_184790_bH)).orNull();
    }

    public void func_184779_b(@Nullable UUID uuid) {
        this.field_70180_af.func_187227_b(AbstractHorse.field_184790_bH, Optional.fromNullable(uuid));
    }

    public float func_110254_bY() {
        return 0.5F;
    }

    public void func_98054_a(boolean flag) {
        this.func_98055_j(flag ? this.func_110254_bY() : 1.0F);
    }

    public boolean func_110246_bZ() {
        return this.field_110275_br;
    }

    public void func_110234_j(boolean flag) {
        this.func_110208_b(2, flag);
    }

    public void func_110255_k(boolean flag) {
        this.field_110275_br = flag;
    }

    public boolean func_184652_a(EntityPlayer entityhuman) {
        return field_70170_p.paperConfig.allowLeashingUndeadHorse ? super.func_184652_a(entityhuman) : super.func_184652_a(entityhuman) && this.func_70668_bt() != EnumCreatureAttribute.UNDEAD; // Paper
    }

    protected void func_142017_o(float f) {
        if (f > 6.0F && this.func_110204_cc()) {
            this.func_110227_p(false);
        }

    }

    public boolean func_110204_cc() {
        return this.func_110233_w(16);
    }

    public boolean func_110209_cd() {
        return this.func_110233_w(32);
    }

    public boolean func_110205_ce() {
        return this.func_110233_w(8);
    }

    public void func_110242_l(boolean flag) {
        this.func_110208_b(8, flag);
    }

    public void func_110251_o(boolean flag) {
        this.func_110208_b(4, flag);
    }

    public int func_110252_cg() {
        return this.field_110274_bs;
    }

    public void func_110238_s(int i) {
        this.field_110274_bs = i;
    }

    public int func_110198_t(int i) {
        int j = MathHelper.func_76125_a(this.func_110252_cg() + i, 0, this.func_190676_dC());

        this.func_110238_s(j);
        return j;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        Entity entity = damagesource.func_76346_g();

        return this.func_184207_aI() && entity != null && this.func_184215_y(entity) ? false : super.func_70097_a(damagesource, f);
    }

    public boolean func_70104_M() {
        return !this.func_184207_aI();
    }

    private void func_110266_cB() {
        this.func_110249_cI();
        if (!this.func_174814_R()) {
            this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187711_cp, this.func_184176_by(), 1.0F, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
        }

    }

    public void func_180430_e(float f, float f1) {
        if (f > 1.0F) {
            this.func_184185_a(SoundEvents.field_187723_ct, 0.4F, 1.0F);
        }

        int i = MathHelper.func_76123_f((f * 0.5F - 3.0F) * f1);

        if (i > 0) {
            this.func_70097_a(DamageSource.field_76379_h, (float) i);
            if (this.func_184207_aI()) {
                Iterator iterator = this.func_184182_bu().iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.func_70097_a(DamageSource.field_76379_h, (float) i);
                }
            }

            IBlockState iblockdata = this.field_70170_p.func_180495_p(new BlockPos(this.field_70165_t, this.field_70163_u - 0.2D - (double) this.field_70126_B, this.field_70161_v));
            Block block = iblockdata.func_177230_c();

            if (iblockdata.func_185904_a() != Material.field_151579_a && !this.func_174814_R()) {
                SoundType soundeffecttype = block.func_185467_w();

                this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, soundeffecttype.func_185844_d(), this.func_184176_by(), soundeffecttype.func_185843_a() * 0.5F, soundeffecttype.func_185847_b() * 0.75F);
            }

        }
    }

    protected int func_190686_di() {
        return 2;
    }

    public void func_110226_cD() {
        ContainerHorseChest inventoryhorsechest = this.field_110296_bG;

        this.field_110296_bG = new ContainerHorseChest("HorseChest", this.func_190686_di(), this); // CraftBukkit
        this.field_110296_bG.func_110133_a(this.func_70005_c_());
        if (inventoryhorsechest != null) {
            inventoryhorsechest.func_110132_b(this);
            int i = Math.min(inventoryhorsechest.func_70302_i_(), this.field_110296_bG.func_70302_i_());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryhorsechest.func_70301_a(j);

                if (!itemstack.func_190926_b()) {
                    this.field_110296_bG.func_70299_a(j, itemstack.func_77946_l());
                }
            }
        }

        this.field_110296_bG.func_110134_a((IInventoryChangedListener) this);
        this.func_110232_cE();
    }

    protected void func_110232_cE() {
        if (!this.field_70170_p.field_72995_K) {
            this.func_110251_o(!this.field_110296_bG.func_70301_a(0).func_190926_b() && this.func_190685_dA());
        }
    }

    public void func_76316_a(IInventory iinventory) {
        boolean flag = this.func_110257_ck();

        this.func_110232_cE();
        if (this.field_70173_aa > 20 && !flag && this.func_110257_ck()) {
            this.func_184185_a(SoundEvents.field_187726_cu, 0.5F, 1.0F);
        }

    }

    @Nullable
    protected AbstractHorse func_110250_a(Entity entity, double d0) {
        double d1 = Double.MAX_VALUE;
        Entity entity1 = null;
        List list = this.field_70170_p.func_175674_a(entity, entity.func_174813_aQ().func_72321_a(d0, d0, d0), AbstractHorse.field_110276_bu);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity2 = (Entity) iterator.next();
            double d2 = entity2.func_70092_e(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v);

            if (d2 < d1) {
                entity1 = entity2;
                d1 = d2;
            }
        }

        return (AbstractHorse) entity1;
    }

    public double func_110215_cj() {
        return this.func_110148_a(AbstractHorse.field_110271_bv).func_111126_e();
    }

    @Nullable
    protected SoundEvent func_184615_bR() {
        this.func_110249_cI();
        return null;
    }

    @Nullable
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        this.func_110249_cI();
        if (this.field_70146_Z.nextInt(3) == 0) {
            this.func_110220_cK();
        }

        return null;
    }

    @Nullable
    protected SoundEvent func_184639_G() {
        this.func_110249_cI();
        if (this.field_70146_Z.nextInt(10) == 0 && !this.func_70610_aX()) {
            this.func_110220_cK();
        }

        return null;
    }

    public boolean func_190685_dA() {
        return true;
    }

    public boolean func_110257_ck() {
        return this.func_110233_w(4);
    }

    @Nullable
    protected SoundEvent func_184785_dv() {
        this.func_110249_cI();
        this.func_110220_cK();
        return null;
    }

    protected void func_180429_a(BlockPos blockposition, Block block) {
        if (!block.func_176223_P().func_185904_a().func_76224_d()) {
            SoundType soundeffecttype = block.func_185467_w();

            if (this.field_70170_p.func_180495_p(blockposition.func_177984_a()).func_177230_c() == Blocks.field_150431_aC) {
                soundeffecttype = Blocks.field_150431_aC.func_185467_w();
            }

            if (this.func_184207_aI() && this.field_190688_bE) {
                ++this.field_110285_bP;
                if (this.field_110285_bP > 5 && this.field_110285_bP % 3 == 0) {
                    this.func_190680_a(soundeffecttype);
                } else if (this.field_110285_bP <= 5) {
                    this.func_184185_a(SoundEvents.field_187732_cw, soundeffecttype.func_185843_a() * 0.15F, soundeffecttype.func_185847_b());
                }
            } else if (soundeffecttype == SoundType.field_185848_a) {
                this.func_184185_a(SoundEvents.field_187732_cw, soundeffecttype.func_185843_a() * 0.15F, soundeffecttype.func_185847_b());
            } else {
                this.func_184185_a(SoundEvents.field_187729_cv, soundeffecttype.func_185843_a() * 0.15F, soundeffecttype.func_185847_b());
            }

        }
    }

    protected void func_190680_a(SoundType soundeffecttype) {
        this.func_184185_a(SoundEvents.field_187714_cq, soundeffecttype.func_185843_a() * 0.15F, soundeffecttype.func_185847_b());
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110140_aT().func_111150_b(AbstractHorse.field_110271_bv);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(53.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.22499999403953552D);
    }

    public int func_70641_bl() {
        return 6;
    }

    public int func_190676_dC() {
        return this.maxDomestication; // CraftBukkit - return stored max domestication instead of 100
    }

    protected float func_70599_aP() {
        return 0.8F;
    }

    public int func_70627_aG() {
        return 400;
    }

    public void func_110199_f(EntityPlayer entityhuman) {
        if (!this.field_70170_p.field_72995_K && (!this.func_184207_aI() || this.func_184196_w(entityhuman)) && this.func_110248_bS()) {
            this.field_110296_bG.func_110133_a(this.func_70005_c_());
            entityhuman.func_184826_a(this, this.field_110296_bG);
        }

    }

    protected boolean func_190678_b(EntityPlayer entityhuman, ItemStack itemstack) {
        boolean flag = false;
        float f = 0.0F;
        short short0 = 0;
        byte b0 = 0;
        Item item = itemstack.func_77973_b();

        if (item == Items.field_151015_O) {
            f = 2.0F;
            short0 = 20;
            b0 = 3;
        } else if (item == Items.field_151102_aT) {
            f = 1.0F;
            short0 = 30;
            b0 = 3;
        } else if (item == Item.func_150898_a(Blocks.field_150407_cf)) {
            f = 20.0F;
            short0 = 180;
        } else if (item == Items.field_151034_e) {
            f = 3.0F;
            short0 = 60;
            b0 = 3;
        } else if (item == Items.field_151150_bK) {
            f = 4.0F;
            short0 = 60;
            b0 = 5;
            if (this.func_110248_bS() && this.func_70874_b() == 0 && !this.func_70880_s()) {
                flag = true;
                this.func_146082_f(entityhuman);
            }
        } else if (item == Items.field_151153_ao) {
            f = 10.0F;
            short0 = 240;
            b0 = 10;
            if (this.func_110248_bS() && this.func_70874_b() == 0 && !this.func_70880_s()) {
                flag = true;
                this.func_146082_f(entityhuman);
            }
        }

        if (this.func_110143_aJ() < this.func_110138_aP() && f > 0.0F) {
            this.heal(f, RegainReason.EATING); // CraftBukkit
            flag = true;
        }

        if (this.func_70631_g_() && short0 > 0) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.VILLAGER_HAPPY, this.field_70165_t + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, this.field_70163_u + 0.5D + (double) (this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, 0.0D, 0.0D, 0.0D, new int[0]);
            if (!this.field_70170_p.field_72995_K) {
                this.func_110195_a(short0);
            }

            flag = true;
        }

        if (b0 > 0 && (flag || !this.func_110248_bS()) && this.func_110252_cg() < this.func_190676_dC()) {
            flag = true;
            if (!this.field_70170_p.field_72995_K) {
                this.func_110198_t(b0);
            }
        }

        if (flag) {
            this.func_110266_cB();
        }

        return flag;
    }

    protected void func_110237_h(EntityPlayer entityhuman) {
        entityhuman.field_70177_z = this.field_70177_z;
        entityhuman.field_70125_A = this.field_70125_A;
        this.func_110227_p(false);
        this.func_110219_q(false);
        if (!this.field_70170_p.field_72995_K) {
            entityhuman.func_184220_m(this);
        }

    }

    protected boolean func_70610_aX() {
        return super.func_70610_aX() && this.func_184207_aI() && this.func_110257_ck() || this.func_110204_cc() || this.func_110209_cd();
    }

    public boolean func_70877_b(ItemStack itemstack) {
        return false;
    }

    private void func_110210_cH() {
        this.field_110278_bp = 1;
    }

    public void func_70645_a(DamageSource damagesource) {
        // super.die(damagesource); // Moved down
        if (!this.field_70170_p.field_72995_K && this.field_110296_bG != null) {
            for (int i = 0; i < this.field_110296_bG.func_70302_i_(); ++i) {
                ItemStack itemstack = this.field_110296_bG.func_70301_a(i);

                if (!itemstack.func_190926_b()) {
                    this.func_70099_a(itemstack, 0.0F);
                }
            }

        }
        super.func_70645_a(damagesource); // CraftBukkit
    }

    public void func_70636_d() {
        if (this.field_70146_Z.nextInt(200) == 0) {
            this.func_110210_cH();
        }

        super.func_70636_d();
        if (!this.field_70170_p.field_72995_K) {
            if (this.field_70146_Z.nextInt(900) == 0 && this.field_70725_aQ == 0) {
                this.heal(1.0F, RegainReason.REGEN); // CraftBukkit
            }

            if (this.func_190684_dE()) {
                if (!this.func_110204_cc() && !this.func_184207_aI() && this.field_70146_Z.nextInt(300) == 0 && this.field_70170_p.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_70165_t), MathHelper.func_76128_c(this.field_70163_u) - 1, MathHelper.func_76128_c(this.field_70161_v))).func_177230_c() == Blocks.field_150349_c) {
                    this.func_110227_p(true);
                }

                if (this.func_110204_cc() && ++this.field_190689_bJ > 50) {
                    this.field_190689_bJ = 0;
                    this.func_110227_p(false);
                }
            }

            this.func_190679_dD();
        }
    }

    protected void func_190679_dD() {
        if (this.func_110205_ce() && this.func_70631_g_() && !this.func_110204_cc()) {
            AbstractHorse entityhorseabstract = this.func_110250_a(this, 16.0D);

            if (entityhorseabstract != null && this.func_70068_e((Entity) entityhorseabstract) > 4.0D) {
                this.field_70699_by.func_75494_a((Entity) entityhorseabstract);
            }
        }

    }

    public boolean func_190684_dE() {
        return true;
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_110290_bE > 0 && ++this.field_110290_bE > 30) {
            this.field_110290_bE = 0;
            this.func_110208_b(64, false);
        }

        if (this.func_184186_bw() && this.field_110295_bF > 0 && ++this.field_110295_bF > 20) {
            this.field_110295_bF = 0;
            this.func_110219_q(false);
        }

        if (this.field_110278_bp > 0 && ++this.field_110278_bp > 8) {
            this.field_110278_bp = 0;
        }

        if (this.field_110279_bq > 0) {
            ++this.field_110279_bq;
            if (this.field_110279_bq > 300) {
                this.field_110279_bq = 0;
            }
        }

        this.field_110284_bK = this.field_110283_bJ;
        if (this.func_110204_cc()) {
            this.field_110283_bJ += (1.0F - this.field_110283_bJ) * 0.4F + 0.05F;
            if (this.field_110283_bJ > 1.0F) {
                this.field_110283_bJ = 1.0F;
            }
        } else {
            this.field_110283_bJ += (0.0F - this.field_110283_bJ) * 0.4F - 0.05F;
            if (this.field_110283_bJ < 0.0F) {
                this.field_110283_bJ = 0.0F;
            }
        }

        this.field_110282_bM = this.field_110281_bL;
        if (this.func_110209_cd()) {
            this.field_110283_bJ = 0.0F;
            this.field_110284_bK = this.field_110283_bJ;
            this.field_110281_bL += (1.0F - this.field_110281_bL) * 0.4F + 0.05F;
            if (this.field_110281_bL > 1.0F) {
                this.field_110281_bL = 1.0F;
            }
        } else {
            this.field_110294_bI = false;
            this.field_110281_bL += (0.8F * this.field_110281_bL * this.field_110281_bL * this.field_110281_bL - this.field_110281_bL) * 0.6F - 0.05F;
            if (this.field_110281_bL < 0.0F) {
                this.field_110281_bL = 0.0F;
            }
        }

        this.field_110288_bO = this.field_110287_bN;
        if (this.func_110233_w(64)) {
            this.field_110287_bN += (1.0F - this.field_110287_bN) * 0.7F + 0.05F;
            if (this.field_110287_bN > 1.0F) {
                this.field_110287_bN = 1.0F;
            }
        } else {
            this.field_110287_bN += (0.0F - this.field_110287_bN) * 0.7F - 0.05F;
            if (this.field_110287_bN < 0.0F) {
                this.field_110287_bN = 0.0F;
            }
        }

    }

    private void func_110249_cI() {
        if (!this.field_70170_p.field_72995_K) {
            this.field_110290_bE = 1;
            this.func_110208_b(64, true);
        }

    }

    public void func_110227_p(boolean flag) {
        this.func_110208_b(16, flag);
    }

    public void func_110219_q(boolean flag) {
        if (flag) {
            this.func_110227_p(false);
        }

        this.func_110208_b(32, flag);
    }

    private void func_110220_cK() {
        if (this.func_184186_bw()) {
            this.field_110295_bF = 1;
            this.func_110219_q(true);
        }

    }

    public void func_190687_dF() {
        this.func_110220_cK();
        SoundEvent soundeffect = this.func_184785_dv();

        if (soundeffect != null) {
            this.func_184185_a(soundeffect, this.func_70599_aP(), this.func_70647_i());
        }

    }

    public boolean func_110263_g(EntityPlayer entityhuman) {
        this.func_184779_b(entityhuman.func_110124_au());
        this.func_110234_j(true);
        if (entityhuman instanceof EntityPlayerMP) {
            CriteriaTriggers.field_193136_w.func_193178_a((EntityPlayerMP) entityhuman, (EntityAnimal) this);
        }

        this.field_70170_p.func_72960_a(this, (byte) 7);
        return true;
    }

    public void func_191986_a(float f, float f1, float f2) {
        if (this.func_184207_aI() && this.func_82171_bF() && this.func_110257_ck()) {
            EntityLivingBase entityliving = (EntityLivingBase) this.func_184179_bs();

            this.field_70177_z = entityliving.field_70177_z;
            this.field_70126_B = this.field_70177_z;
            this.field_70125_A = entityliving.field_70125_A * 0.5F;
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
            this.field_70761_aq = this.field_70177_z;
            this.field_70759_as = this.field_70761_aq;
            f = entityliving.field_70702_br * 0.5F;
            f2 = entityliving.field_191988_bg;
            if (f2 <= 0.0F) {
                f2 *= 0.25F;
                this.field_110285_bP = 0;
            }

            if (this.field_70122_E && this.field_110277_bt == 0.0F && this.func_110209_cd() && !this.field_110294_bI) {
                f = 0.0F;
                f2 = 0.0F;
            }

            if (this.field_110277_bt > 0.0F && !this.func_110246_bZ() && this.field_70122_E) {
                this.field_70181_x = this.func_110215_cj() * (double) this.field_110277_bt;
                if (this.func_70644_a(MobEffects.field_76430_j)) {
                    this.field_70181_x += (double) ((float) (this.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1F);
                }

                this.func_110255_k(true);
                this.field_70160_al = true;
                if (f2 > 0.0F) {
                    float f3 = MathHelper.func_76126_a(this.field_70177_z * 0.017453292F);
                    float f4 = MathHelper.func_76134_b(this.field_70177_z * 0.017453292F);

                    this.field_70159_w += (double) (-0.4F * f3 * this.field_110277_bt);
                    this.field_70179_y += (double) (0.4F * f4 * this.field_110277_bt);
                    this.func_184185_a(SoundEvents.field_187720_cs, 0.4F, 1.0F);
                }

                this.field_110277_bt = 0.0F;
            }

            this.field_70747_aH = this.func_70689_ay() * 0.1F;
            if (this.func_184186_bw()) {
                this.func_70659_e((float) this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e());
                super.func_191986_a(f, f1, f2);
            } else if (entityliving instanceof EntityPlayer) {
                this.field_70159_w = 0.0D;
                this.field_70181_x = 0.0D;
                this.field_70179_y = 0.0D;
            }

            if (this.field_70122_E) {
                this.field_110277_bt = 0.0F;
                this.func_110255_k(false);
            }

            this.field_184618_aE = this.field_70721_aZ;
            double d0 = this.field_70165_t - this.field_70169_q;
            double d1 = this.field_70161_v - this.field_70166_s;
            float f5 = MathHelper.func_76133_a(d0 * d0 + d1 * d1) * 4.0F;

            if (f5 > 1.0F) {
                f5 = 1.0F;
            }

            this.field_70721_aZ += (f5 - this.field_70721_aZ) * 0.4F;
            this.field_184619_aG += this.field_70721_aZ;
        } else {
            this.field_70747_aH = 0.02F;
            super.func_191986_a(f, f1, f2);
        }
    }

    public static void func_190683_c(DataFixer dataconvertermanager, Class<?> oclass) {
        EntityLiving.func_189752_a(dataconvertermanager, oclass);
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(oclass, new String[] { "SaddleItem"})));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74757_a("EatingHaystack", this.func_110204_cc());
        nbttagcompound.func_74757_a("Bred", this.func_110205_ce());
        nbttagcompound.func_74768_a("Temper", this.func_110252_cg());
        nbttagcompound.func_74757_a("Tame", this.func_110248_bS());
        if (this.func_184780_dh() != null) {
            nbttagcompound.func_74778_a("OwnerUUID", this.func_184780_dh().toString());
        }
        nbttagcompound.func_74768_a("Bukkit.MaxDomestication", this.maxDomestication); // CraftBukkit

        if (!this.field_110296_bG.func_70301_a(0).func_190926_b()) {
            nbttagcompound.func_74782_a("SaddleItem", this.field_110296_bG.func_70301_a(0).func_77955_b(new NBTTagCompound()));
        }

    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_110227_p(nbttagcompound.func_74767_n("EatingHaystack"));
        this.func_110242_l(nbttagcompound.func_74767_n("Bred"));
        this.func_110238_s(nbttagcompound.func_74762_e("Temper"));
        this.func_110234_j(nbttagcompound.func_74767_n("Tame"));
        String s;

        if (nbttagcompound.func_150297_b("OwnerUUID", 8)) {
            s = nbttagcompound.func_74779_i("OwnerUUID");
        } else {
            String s1 = nbttagcompound.func_74779_i("Owner");

            s = PreYggdrasilConverter.func_187473_a(this.func_184102_h(), s1);
        }

        if (!s.isEmpty()) {
            this.func_184779_b(UUID.fromString(s));
        }
        // CraftBukkit start
        if (nbttagcompound.func_74764_b("Bukkit.MaxDomestication")) {
            this.maxDomestication = nbttagcompound.func_74762_e("Bukkit.MaxDomestication");
        }
        // CraftBukkit end

        IAttributeInstance attributeinstance = this.func_110140_aT().func_111152_a("Speed");

        if (attributeinstance != null) {
            this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(attributeinstance.func_111125_b() * 0.25D);
        }

        if (nbttagcompound.func_150297_b("SaddleItem", 10)) {
            ItemStack itemstack = new ItemStack(nbttagcompound.func_74775_l("SaddleItem"));

            if (itemstack.func_77973_b() == Items.field_151141_av) {
                this.field_110296_bG.func_70299_a(0, itemstack);
            }
        }

        this.func_110232_cE();
    }

    public boolean func_70878_b(EntityAnimal entityanimal) {
        return false;
    }

    protected boolean func_110200_cJ() {
        return !this.func_184207_aI() && !this.func_184218_aH() && this.func_110248_bS() && !this.func_70631_g_() && this.func_110143_aJ() >= this.func_110138_aP() && this.func_70880_s();
    }

    @Nullable
    public EntityAgeable func_90011_a(EntityAgeable entityageable) {
        return null;
    }

    protected void func_190681_a(EntityAgeable entityageable, AbstractHorse entityhorseabstract) {
        double d0 = this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() + entityageable.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() + (double) this.func_110267_cL();

        entityhorseabstract.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(d0 / 3.0D);
        double d1 = this.func_110148_a(AbstractHorse.field_110271_bv).func_111125_b() + entityageable.func_110148_a(AbstractHorse.field_110271_bv).func_111125_b() + this.func_110245_cM();

        entityhorseabstract.func_110148_a(AbstractHorse.field_110271_bv).func_111128_a(d1 / 3.0D);
        double d2 = this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111125_b() + entityageable.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111125_b() + this.func_110203_cN();

        entityhorseabstract.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(d2 / 3.0D);
    }

    public boolean func_82171_bF() {
        return this.func_184179_bs() instanceof EntityLivingBase;
    }

    public boolean func_184776_b() {
        return this.func_110257_ck();
    }

    public void func_184775_b(int i) {
        // CraftBukkit start
        float power;
        if (i >= 90) {
            power = 1.0F;
        } else {
            power = 0.4F + 0.4F * (float) i / 90.0F;
        }
        org.bukkit.event.entity.HorseJumpEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callHorseJumpEvent(this, power);
        if (event.isCancelled()) {
            return;
        }
        // CraftBukkit end
        this.field_110294_bI = true;
        this.func_110220_cK();
    }

    public void func_184777_r_() {}

    public void func_184232_k(Entity entity) {
        super.func_184232_k(entity);
        if (entity instanceof EntityLiving) {
            EntityLiving entityinsentient = (EntityLiving) entity;

            this.field_70761_aq = entityinsentient.field_70761_aq;
        }

        if (this.field_110282_bM > 0.0F) {
            float f = MathHelper.func_76126_a(this.field_70761_aq * 0.017453292F);
            float f1 = MathHelper.func_76134_b(this.field_70761_aq * 0.017453292F);
            float f2 = 0.7F * this.field_110282_bM;
            float f3 = 0.15F * this.field_110282_bM;

            entity.func_70107_b(this.field_70165_t + (double) (f2 * f), this.field_70163_u + this.func_70042_X() + entity.func_70033_W() + (double) f3, this.field_70161_v - (double) (f2 * f1));
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).field_70761_aq = this.field_70761_aq;
            }
        }

    }

    protected float func_110267_cL() {
        return 15.0F + (float) this.field_70146_Z.nextInt(8) + (float) this.field_70146_Z.nextInt(9);
    }

    protected double func_110245_cM() {
        return 0.4000000059604645D + this.field_70146_Z.nextDouble() * 0.2D + this.field_70146_Z.nextDouble() * 0.2D + this.field_70146_Z.nextDouble() * 0.2D;
    }

    protected double func_110203_cN() {
        return (0.44999998807907104D + this.field_70146_Z.nextDouble() * 0.3D + this.field_70146_Z.nextDouble() * 0.3D + this.field_70146_Z.nextDouble() * 0.3D) * 0.25D;
    }

    public boolean func_70617_f_() {
        return false;
    }

    public float func_70047_e() {
        return this.field_70131_O;
    }

    public boolean func_190677_dK() {
        return false;
    }

    public boolean func_190682_f(ItemStack itemstack) {
        return false;
    }

    public boolean func_174820_d(int i, ItemStack itemstack) {
        int j = i - 400;

        if (j >= 0 && j < 2 && j < this.field_110296_bG.func_70302_i_()) {
            if (j == 0 && itemstack.func_77973_b() != Items.field_151141_av) {
                return false;
            } else if (j == 1 && (!this.func_190677_dK() || !this.func_190682_f(itemstack))) {
                return false;
            } else {
                this.field_110296_bG.func_70299_a(j, itemstack);
                this.func_110232_cE();
                return true;
            }
        } else {
            int k = i - 500 + 2;

            if (k >= 2 && k < this.field_110296_bG.func_70302_i_()) {
                this.field_110296_bG.func_70299_a(k, itemstack);
                return true;
            } else {
                return false;
            }
        }
    }

    @Nullable
    public Entity func_184179_bs() {
        return this.func_184188_bt().isEmpty() ? null : (Entity) this.func_184188_bt().get(0);
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        groupdataentity = super.func_180482_a(difficultydamagescaler, groupdataentity);
        if (this.field_70146_Z.nextInt(5) == 0) {
            this.func_70873_a(-24000);
        }

        return groupdataentity;
    }
}
