package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPigZombie extends EntityZombie {

    private static final UUID field_110189_bq = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier field_110190_br = (new AttributeModifier(EntityPigZombie.field_110189_bq, "Attacking speed boost", 0.05D, 0)).func_111168_a(false);
    public int field_70837_d;
    private int field_70838_e;
    private UUID field_175459_bn;

    public EntityPigZombie(World world) {
        super(world);
        this.field_70178_ae = true;
    }

    public void func_70604_c(@Nullable EntityLivingBase entityliving) {
        super.func_70604_c(entityliving);
        if (entityliving != null) {
            this.field_175459_bn = entityliving.func_110124_au();
        }

    }

    protected void func_175456_n() {
        this.field_70715_bh.func_75776_a(1, new EntityPigZombie.AIHurtByAggressor(this));
        this.field_70715_bh.func_75776_a(2, new EntityPigZombie.AITargetAggressor(this));
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(EntityPigZombie.field_110186_bp).func_111128_a(0.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.23000000417232513D);
        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(5.0D);
    }

    protected void func_70619_bc() {
        IAttributeInstance attributeinstance = this.func_110148_a(SharedMonsterAttributes.field_111263_d);

        if (this.func_175457_ck()) {
            if (!this.func_70631_g_() && !attributeinstance.func_180374_a(EntityPigZombie.field_110190_br)) {
                attributeinstance.func_111121_a(EntityPigZombie.field_110190_br);
            }

            --this.field_70837_d;
        } else if (attributeinstance.func_180374_a(EntityPigZombie.field_110190_br)) {
            attributeinstance.func_111124_b(EntityPigZombie.field_110190_br);
        }

        if (this.field_70838_e > 0 && --this.field_70838_e == 0) {
            this.func_184185_a(SoundEvents.field_187936_hj, this.func_70599_aP() * 2.0F, ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        if (this.field_70837_d > 0 && this.field_175459_bn != null && this.func_70643_av() == null) {
            EntityPlayer entityhuman = this.field_70170_p.func_152378_a(this.field_175459_bn);

            this.func_70604_c((EntityLivingBase) entityhuman);
            this.field_70717_bb = entityhuman;
            this.field_70718_bc = this.func_142015_aE();
        }

        super.func_70619_bc();
    }

    public boolean func_70601_bi() {
        return this.field_70170_p.func_175659_aa() != EnumDifficulty.PEACEFUL;
    }

    public boolean func_70058_J() {
        return this.field_70170_p.func_72917_a(this.func_174813_aQ(), (Entity) this) && this.field_70170_p.func_184144_a(this, this.func_174813_aQ()).isEmpty() && !this.field_70170_p.func_72953_d(this.func_174813_aQ());
    }

    public static void func_189781_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityPigZombie.class);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74777_a("Anger", (short) this.field_70837_d);
        if (this.field_175459_bn != null) {
            nbttagcompound.func_74778_a("HurtBy", this.field_175459_bn.toString());
        } else {
            nbttagcompound.func_74778_a("HurtBy", "");
        }

    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.field_70837_d = nbttagcompound.func_74765_d("Anger");
        String s = nbttagcompound.func_74779_i("HurtBy");

        if (!s.isEmpty()) {
            this.field_175459_bn = UUID.fromString(s);
            EntityPlayer entityhuman = this.field_70170_p.func_152378_a(this.field_175459_bn);

            this.func_70604_c((EntityLivingBase) entityhuman);
            if (entityhuman != null) {
                this.field_70717_bb = entityhuman;
                this.field_70718_bc = this.func_142015_aE();
            }
        }

    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            Entity entity = damagesource.func_76346_g();

            if (entity instanceof EntityPlayer) {
                this.func_70835_c(entity);
            }

            return super.func_70097_a(damagesource, f);
        }
    }

    private void func_70835_c(Entity entity) {
        this.field_70837_d = 400 + this.field_70146_Z.nextInt(400);
        this.field_70838_e = this.field_70146_Z.nextInt(40);
        if (entity instanceof EntityLivingBase) {
            this.func_70604_c((EntityLivingBase) entity);
        }

    }

    public boolean func_175457_ck() {
        return this.field_70837_d > 0;
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187935_hi;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187938_hl;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187937_hk;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186384_ai;
    }

    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        return false;
    }

    protected void func_180481_a(DifficultyInstance difficultydamagescaler) {
        this.func_184201_a(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.field_151010_B));
    }

    protected ItemStack func_190732_dj() {
        return ItemStack.field_190927_a;
    }

    public boolean func_191990_c(EntityPlayer entityhuman) {
        return this.func_175457_ck();
    }

    static class AITargetAggressor extends EntityAINearestAttackableTarget<EntityPlayer> {

        public AITargetAggressor(EntityPigZombie entitypigzombie) {
            super(entitypigzombie, EntityPlayer.class, true);
        }

        public boolean func_75250_a() {
            return ((EntityPigZombie) this.field_75299_d).func_175457_ck() && super.func_75250_a();
        }
    }

    static class AIHurtByAggressor extends EntityAIHurtByTarget {

        public AIHurtByAggressor(EntityPigZombie entitypigzombie) {
            super(entitypigzombie, true, new Class[0]);
        }

        protected void func_179446_a(EntityCreature entitycreature, EntityLivingBase entityliving) {
            super.func_179446_a(entitycreature, entityliving);
            if (entitycreature instanceof EntityPigZombie) {
                ((EntityPigZombie) entitycreature).func_70835_c((Entity) entityliving);
            }

        }
    }
}
