package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILlamaFollowCaravan;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityLlama extends AbstractChestHorse implements IRangedAttackMob {

    private static final DataParameter<Integer> field_190720_bG = EntityDataManager.func_187226_a(EntityLlama.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer> field_190721_bH = EntityDataManager.func_187226_a(EntityLlama.class, DataSerializers.field_187192_b);
    private static final DataParameter<Integer> field_190722_bI = EntityDataManager.func_187226_a(EntityLlama.class, DataSerializers.field_187192_b);
    private boolean field_190723_bJ;
    @Nullable
    private EntityLlama field_190724_bK;
    @Nullable
    private EntityLlama field_190725_bL;

    public EntityLlama(World world) {
        super(world);
        this.func_70105_a(0.9F, 1.87F);
    }

    public void func_190706_p(int i) {
        this.field_70180_af.func_187227_b(EntityLlama.field_190720_bG, Integer.valueOf(Math.max(1, Math.min(5, i))));
    }

    private void func_190705_dT() {
        int i = this.field_70146_Z.nextFloat() < 0.04F ? 5 : 3;

        this.func_190706_p(1 + this.field_70146_Z.nextInt(i));
    }

    public int func_190707_dL() {
        return this.field_70180_af.func_187225_a(EntityLlama.field_190720_bG).intValue();
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("Variant", this.func_190719_dM());
        nbttagcompound.func_74768_a("Strength", this.func_190707_dL());
        if (!this.field_110296_bG.func_70301_a(1).func_190926_b()) {
            nbttagcompound.func_74782_a("DecorItem", this.field_110296_bG.func_70301_a(1).func_77955_b(new NBTTagCompound()));
        }

    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.func_190706_p(nbttagcompound.func_74762_e("Strength"));
        super.func_70037_a(nbttagcompound);
        this.func_190710_o(nbttagcompound.func_74762_e("Variant"));
        if (nbttagcompound.func_150297_b("DecorItem", 10)) {
            this.field_110296_bG.func_70299_a(1, new ItemStack(nbttagcompound.func_74775_l("DecorItem")));
        }

        this.func_110232_cE();
    }

    @Override
    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.field_70714_bg.func_75776_a(2, new EntityAILlamaFollowCaravan(this, 2.0999999046325684D));
        this.field_70714_bg.func_75776_a(3, new EntityAIAttackRanged(this, 1.25D, 40, 20.0F));
        this.field_70714_bg.func_75776_a(3, new EntityAIPanic(this, 1.2D));
        this.field_70714_bg.func_75776_a(4, new EntityAIMate(this, 1.0D));
        this.field_70714_bg.func_75776_a(5, new EntityAIFollowParent(this, 1.0D));
        this.field_70714_bg.func_75776_a(6, new EntityAIWanderAvoidWater(this, 0.7D));
        this.field_70714_bg.func_75776_a(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
        this.field_70715_bh.func_75776_a(1, new EntityLlama.c(this));
        this.field_70715_bh.func_75776_a(2, new EntityLlama.a(this));
    }

    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(40.0D);
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityLlama.field_190720_bG, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityLlama.field_190721_bH, Integer.valueOf(-1));
        this.field_70180_af.func_187214_a(EntityLlama.field_190722_bI, Integer.valueOf(0));
    }

    public int func_190719_dM() {
        return MathHelper.func_76125_a(this.field_70180_af.func_187225_a(EntityLlama.field_190722_bI).intValue(), 0, 3);
    }

    public void func_190710_o(int i) {
        this.field_70180_af.func_187227_b(EntityLlama.field_190722_bI, Integer.valueOf(i));
    }

    @Override
    protected int func_190686_di() {
        return this.func_190695_dh() ? 2 + 3 * this.func_190696_dl() : super.func_190686_di();
    }

    @Override
    public void func_184232_k(Entity entity) {
        if (this.func_184196_w(entity)) {
            float f = MathHelper.func_76134_b(this.field_70761_aq * 0.017453292F);
            float f1 = MathHelper.func_76126_a(this.field_70761_aq * 0.017453292F);
            float f2 = 0.3F;

            entity.func_70107_b(this.field_70165_t + 0.3F * f1, this.field_70163_u + this.func_70042_X() + entity.func_70033_W(), this.field_70161_v - 0.3F * f);
        }
    }

    @Override
    public double func_70042_X() {
        return this.field_70131_O * 0.67D;
    }

    @Override
    public boolean func_82171_bF() {
        return false;
    }

    @Override
    protected boolean func_190678_b(EntityPlayer entityhuman, ItemStack itemstack) {
        byte b0 = 0;
        byte b1 = 0;
        float f = 0.0F;
        boolean flag = false;
        Item item = itemstack.func_77973_b();

        if (item == Items.field_151015_O) {
            b0 = 10;
            b1 = 3;
            f = 2.0F;
        } else if (item == Item.func_150898_a(Blocks.field_150407_cf)) {
            b0 = 90;
            b1 = 6;
            f = 10.0F;
            if (this.func_110248_bS() && this.func_70874_b() == 0) {
                flag = true;
                this.func_146082_f(entityhuman);
            }
        }

        if (this.func_110143_aJ() < this.func_110138_aP() && f > 0.0F) {
            this.func_70691_i(f);
            flag = true;
        }

        if (this.func_70631_g_() && b0 > 0) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.VILLAGER_HAPPY, this.field_70165_t + this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F - this.field_70130_N, this.field_70163_u + 0.5D + this.field_70146_Z.nextFloat() * this.field_70131_O, this.field_70161_v + this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F - this.field_70130_N, 0.0D, 0.0D, 0.0D, new int[0]);
            if (!this.field_70170_p.field_72995_K) {
                this.func_110195_a(b0);
            }

            flag = true;
        }

        if (b1 > 0 && (flag || !this.func_110248_bS()) && this.func_110252_cg() < this.func_190676_dC()) {
            flag = true;
            if (!this.field_70170_p.field_72995_K) {
                this.func_110198_t(b1);
            }
        }

        if (flag && !this.func_174814_R()) {
            this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_191253_dD, this.func_184176_by(), 1.0F, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
        }

        return flag;
    }

    @Override
    protected boolean func_70610_aX() {
        return this.func_110143_aJ() <= 0.0F || this.func_110204_cc();
    }

    @Override
    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        Object object = super.func_180482_a(difficultydamagescaler, groupdataentity);

        this.func_190705_dT();
        int i;

        if (object instanceof EntityLlama.b) {
            i = ((EntityLlama.b) object).a;
        } else {
            i = this.field_70146_Z.nextInt(4);
            object = new EntityLlama.b(i, null);
        }

        this.func_190710_o(i);
        return (IEntityLivingData) object;
    }

    @Override
    protected SoundEvent func_184785_dv() {
        return SoundEvents.field_191250_dA;
    }

    @Override
    protected SoundEvent func_184639_G() {
        return SoundEvents.field_191260_dz;
    }

    @Override
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_191254_dE;
    }

    @Override
    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_191252_dC;
    }

    @Override
    protected void func_180429_a(BlockPos blockposition, Block block) {
        this.func_184185_a(SoundEvents.field_191256_dG, 0.15F, 1.0F);
    }

    @Override
    protected void func_190697_dk() {
        this.func_184185_a(SoundEvents.field_191251_dB, 1.0F, (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public void func_190687_dF() {
        SoundEvent soundeffect = this.func_184785_dv();

        if (soundeffect != null) {
            this.func_184185_a(soundeffect, this.func_70599_aP(), this.func_70647_i());
        }

    }

    @Override
    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_191187_aw;
    }

    @Override
    public int func_190696_dl() {
        return this.func_190707_dL();
    }

    @Override
    public boolean func_190677_dK() {
        return true;
    }

    @Override
    public boolean func_190682_f(ItemStack itemstack) {
        return itemstack.func_77973_b() == Item.func_150898_a(Blocks.field_150404_cg);
    }

    @Override
    public boolean func_190685_dA() {
        return false;
    }

    @Override
    public void func_76316_a(IInventory iinventory) {
        EnumDyeColor enumcolor = this.func_190704_dO();

        super.func_76316_a(iinventory);
        EnumDyeColor enumcolor1 = this.func_190704_dO();

        if (this.field_70173_aa > 20 && enumcolor1 != null && enumcolor1 != enumcolor) {
            this.func_184185_a(SoundEvents.field_191257_dH, 0.5F, 1.0F);
        }

    }

    @Override
    protected void func_110232_cE() {
        if (!this.field_70170_p.field_72995_K) {
            super.func_110232_cE();
            this.func_190702_g(this.field_110296_bG.func_70301_a(1));
        }
    }

    private void func_190711_a(@Nullable EnumDyeColor enumcolor) {
        this.field_70180_af.func_187227_b(EntityLlama.field_190721_bH, Integer.valueOf(enumcolor == null ? -1 : enumcolor.func_176765_a()));
    }

    private void func_190702_g(ItemStack itemstack) {
        if (this.func_190682_f(itemstack)) {
            this.func_190711_a(EnumDyeColor.func_176764_b(itemstack.func_77960_j()));
        } else {
            this.func_190711_a((EnumDyeColor) null);
        }

    }

    @Nullable
    public EnumDyeColor func_190704_dO() {
        int i = this.field_70180_af.func_187225_a(EntityLlama.field_190721_bH).intValue();

        return i == -1 ? null : EnumDyeColor.func_176764_b(i);
    }

    @Override
    public int func_190676_dC() {
        return 30;
    }

    @Override
    public boolean func_70878_b(EntityAnimal entityanimal) {
        return entityanimal != this && entityanimal instanceof EntityLlama && this.func_110200_cJ() && ((EntityLlama) entityanimal).func_110200_cJ();
    }

    @Override
    public EntityLlama func_90011_a(EntityAgeable entityageable) {
        EntityLlama entityllama = new EntityLlama(this.field_70170_p);

        this.func_190681_a(entityageable, entityllama);
        EntityLlama entityllama1 = (EntityLlama) entityageable;
        int i = this.field_70146_Z.nextInt(Math.max(this.func_190707_dL(), entityllama1.func_190707_dL())) + 1;

        if (this.field_70146_Z.nextFloat() < 0.03F) {
            ++i;
        }

        entityllama.func_190706_p(i);
        entityllama.func_190710_o(this.field_70146_Z.nextBoolean() ? this.func_190719_dM() : entityllama1.func_190719_dM());
        return entityllama;
    }

    private void func_190713_e(EntityLivingBase entityliving) {
        EntityLlamaSpit entityllamaspit = new EntityLlamaSpit(this.field_70170_p, this);
        double d0 = entityliving.field_70165_t - this.field_70165_t;
        double d1 = entityliving.func_174813_aQ().field_72338_b + entityliving.field_70131_O / 3.0F - entityllamaspit.field_70163_u;
        double d2 = entityliving.field_70161_v - this.field_70161_v;
        float f = MathHelper.func_76133_a(d0 * d0 + d2 * d2) * 0.2F;

        entityllamaspit.func_70186_c(d0, d1 + f, d2, 1.5F, 10.0F);
        this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_191255_dF, this.func_184176_by(), 1.0F, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F);
        this.field_70170_p.func_72838_d(entityllamaspit);
        this.field_190723_bJ = true;
    }

    private void func_190714_x(boolean flag) {
        this.field_190723_bJ = flag;
    }

    @Override
    public void func_180430_e(float f, float f1) {
        int i = MathHelper.func_76123_f((f * 0.5F - 3.0F) * f1);

        if (i > 0) {
            if (f >= 6.0F) {
                this.func_70097_a(DamageSource.field_76379_h, i);
                if (this.func_184207_aI()) {
                    Iterator iterator = this.func_184182_bu().iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        entity.func_70097_a(DamageSource.field_76379_h, i);
                    }
                }
            }

            IBlockState iblockdata = this.field_70170_p.func_180495_p(new BlockPos(this.field_70165_t, this.field_70163_u - 0.2D - this.field_70126_B, this.field_70161_v));
            Block block = iblockdata.func_177230_c();

            if (iblockdata.func_185904_a() != Material.field_151579_a && !this.func_174814_R()) {
                SoundType soundeffecttype = block.func_185467_w();

                this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, soundeffecttype.func_185844_d(), this.func_184176_by(), soundeffecttype.func_185843_a() * 0.5F, soundeffecttype.func_185847_b() * 0.75F);
            }

        }
    }

    public void func_190709_dP() {
        if (this.field_190724_bK != null) {
            this.field_190724_bK.field_190725_bL = null;
        }

        this.field_190724_bK = null;
    }

    public void func_190715_a(EntityLlama entityllama) {
        this.field_190724_bK = entityllama;
        this.field_190724_bK.field_190725_bL = this;
    }

    public boolean func_190712_dQ() {
        return this.field_190725_bL != null;
    }

    public boolean inCaravan() { return this.func_190718_dR(); } // Paper - OBFHELPER
    public boolean func_190718_dR() {
        return this.field_190724_bK != null;
    }

    @Nullable
    public EntityLlama func_190716_dS() {
        return this.field_190724_bK;
    }

    @Override
    protected double func_190634_dg() {
        return 2.0D;
    }

    @Override
    protected void func_190679_dD() {
        if (!this.func_190718_dR() && this.func_70631_g_()) {
            super.func_190679_dD();
        }

    }

    @Override
    public boolean func_190684_dE() {
        return false;
    }

    @Override
    public void func_82196_d(EntityLivingBase entityliving, float f) {
        this.func_190713_e(entityliving);
    }

    @Override
    public void func_184724_a(boolean flag) {}

    static class a extends EntityAINearestAttackableTarget<EntityWolf> {

        public a(EntityLlama entityllama) {
            super(entityllama, EntityWolf.class, 16, false, true, (Predicate) null);
        }

        @Override
        public boolean func_75250_a() {
            if (super.func_75250_a() && this.field_75309_a != null && !this.field_75309_a.func_70909_n()) {
                return true;
            } else {
                this.field_75299_d.func_70624_b((EntityLivingBase) null);
                return false;
            }
        }

        @Override
        protected double func_111175_f() {
            return super.func_111175_f() * 0.25D;
        }
    }

    static class c extends EntityAIHurtByTarget {

        public c(EntityLlama entityllama) {
            super(entityllama, false, new Class[0]);
        }

        @Override
        public boolean func_75253_b() {
            if (this.field_75299_d instanceof EntityLlama) {
                EntityLlama entityllama = (EntityLlama) this.field_75299_d;

                if (entityllama.field_190723_bJ) {
                    entityllama.func_190714_x(false);
                    return false;
                }
            }

            return super.func_75253_b();
        }
    }

    static class b implements IEntityLivingData {

        public int a;

        private b(int i) {
            this.a = i;
        }

        b(int i, Object object) {
            this(i);
        }
    }
}
