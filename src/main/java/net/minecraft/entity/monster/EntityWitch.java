package net.minecraft.entity.monster;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWitch extends EntityMob implements IRangedAttackMob {

    private static final UUID field_110184_bp = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier field_110185_bq = (new AttributeModifier(EntityWitch.field_110184_bp, "Drinking speed penalty", -0.25D, 0)).func_111168_a(false);
    private static final DataParameter<Boolean> field_184731_c = EntityDataManager.func_187226_a(EntityWitch.class, DataSerializers.field_187198_h);
    private int field_82200_e;

    public EntityWitch(World world) {
        super(world);
        this.func_70105_a(0.6F, 1.95F);
    }

    public static void func_189776_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityWitch.class);
    }

    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(1, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
        this.field_70714_bg.func_75776_a(2, new EntityAIWanderAvoidWater(this, 1.0D));
        this.field_70714_bg.func_75776_a(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.field_70714_bg.func_75776_a(3, new EntityAILookIdle(this));
        this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.func_184212_Q().func_187214_a(EntityWitch.field_184731_c, Boolean.valueOf(false));
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187920_gt;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187923_gw;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187921_gu;
    }

    public void func_82197_f(boolean flag) {
        this.func_184212_Q().func_187227_b(EntityWitch.field_184731_c, Boolean.valueOf(flag));
    }

    public boolean func_184730_o() {
        return ((Boolean) this.func_184212_Q().func_187225_a(EntityWitch.field_184731_c)).booleanValue();
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(26.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.25D);
    }

    public void func_70636_d() {
        if (!this.field_70170_p.field_72995_K) {
            if (this.func_184730_o()) {
                if (this.field_82200_e-- <= 0) {
                    this.func_82197_f(false);
                    ItemStack itemstack = this.func_184614_ca();

                    this.func_184201_a(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
                    if (itemstack.func_77973_b() == Items.field_151068_bn) {
                        // Paper start
                        com.destroystokyo.paper.event.entity.WitchConsumePotionEvent event = new com.destroystokyo.paper.event.entity.WitchConsumePotionEvent((org.bukkit.entity.Witch) this.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemstack));

                        List list = event.callEvent() ? PotionUtils.func_185189_a(org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(event.getPotion())) : null;
                        // Paper end

                        if (list != null) {
                            Iterator iterator = list.iterator();

                            while (iterator.hasNext()) {
                                PotionEffect mobeffect = (PotionEffect) iterator.next();

                                this.func_70690_d(new PotionEffect(mobeffect));
                            }
                        }
                    }

                    this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111124_b(EntityWitch.field_110185_bq);
                }
            } else {
                PotionType potionregistry = null;

                if (this.field_70146_Z.nextFloat() < 0.15F && this.func_70055_a(Material.field_151586_h) && !this.func_70644_a(MobEffects.field_76427_o)) {
                    potionregistry = PotionTypes.field_185248_t;
                } else if (this.field_70146_Z.nextFloat() < 0.15F && (this.func_70027_ad() || this.func_189748_bU() != null && this.func_189748_bU().func_76347_k()) && !this.func_70644_a(MobEffects.field_76426_n)) {
                    potionregistry = PotionTypes.field_185241_m;
                } else if (this.field_70146_Z.nextFloat() < 0.05F && this.func_110143_aJ() < this.func_110138_aP()) {
                    potionregistry = PotionTypes.field_185250_v;
                } else if (this.field_70146_Z.nextFloat() < 0.5F && this.func_70638_az() != null && !this.func_70644_a(MobEffects.field_76424_c) && this.func_70638_az().func_70068_e(this) > 121.0D) {
                    potionregistry = PotionTypes.field_185243_o;
                }

                if (potionregistry != null) {
                    // Paper start
                    ItemStack potion = PotionUtils.func_185188_a(new ItemStack(Items.field_151068_bn), potionregistry);
                    org.bukkit.inventory.ItemStack bukkitStack = com.destroystokyo.paper.event.entity.WitchReadyPotionEvent.process((org.bukkit.entity.Witch) this.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(potion));
                    this.func_184201_a(EntityEquipmentSlot.MAINHAND, org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(bukkitStack));
                    // Paper end
                    this.field_82200_e = this.func_184614_ca().func_77988_m();
                    this.func_82197_f(true);
                    this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187922_gv, this.func_184176_by(), 1.0F, 0.8F + this.field_70146_Z.nextFloat() * 0.4F);
                    IAttributeInstance attributeinstance = this.func_110148_a(SharedMonsterAttributes.field_111263_d);

                    attributeinstance.func_111124_b(EntityWitch.field_110185_bq);
                    attributeinstance.func_111121_a(EntityWitch.field_110185_bq);
                }
            }

            if (this.field_70146_Z.nextFloat() < 7.5E-4F) {
                this.field_70170_p.func_72960_a(this, (byte) 15);
            }
        }

        super.func_70636_d();
    }

    protected float func_70672_c(DamageSource damagesource, float f) {
        f = super.func_70672_c(damagesource, f);
        if (damagesource.func_76346_g() == this) {
            f = 0.0F;
        }

        if (damagesource.func_82725_o()) {
            f = (float) ((double) f * 0.15D);
        }

        return f;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186432_n;
    }

    public void func_82196_d(EntityLivingBase entityliving, float f) {
        if (!this.func_184730_o()) {
            double d0 = entityliving.field_70163_u + (double) entityliving.func_70047_e() - 1.100000023841858D;
            double d1 = entityliving.field_70165_t + entityliving.field_70159_w - this.field_70165_t;
            double d2 = d0 - this.field_70163_u;
            double d3 = entityliving.field_70161_v + entityliving.field_70179_y - this.field_70161_v;
            float f1 = MathHelper.func_76133_a(d1 * d1 + d3 * d3);
            PotionType potionregistry = PotionTypes.field_185252_x;

            if (f1 >= 8.0F && !entityliving.func_70644_a(MobEffects.field_76421_d)) {
                potionregistry = PotionTypes.field_185246_r;
            } else if (entityliving.func_110143_aJ() >= 8.0F && !entityliving.func_70644_a(MobEffects.field_76436_u)) {
                potionregistry = PotionTypes.field_185254_z;
            } else if (f1 <= 3.0F && !entityliving.func_70644_a(MobEffects.field_76437_t) && this.field_70146_Z.nextFloat() < 0.25F) {
                potionregistry = PotionTypes.field_185226_I;
            }

            // Paper start
            ItemStack potion = PotionUtils.func_185188_a(new ItemStack(Items.field_185155_bH), potionregistry);
            com.destroystokyo.paper.event.entity.WitchThrowPotionEvent event = new com.destroystokyo.paper.event.entity.WitchThrowPotionEvent((org.bukkit.entity.Witch) this.getBukkitEntity(), (org.bukkit.entity.LivingEntity) entityliving.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(potion));
            if (!event.callEvent()) {
                return;
            }
            potion = org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(event.getPotion());
            EntityPotion entitypotion = new EntityPotion(this.field_70170_p, this, potion);
            // Paper end

            entitypotion.field_70125_A -= -20.0F;
            entitypotion.func_70186_c(d1, d2 + (double) (f1 * 0.2F), d3, 0.75F, 8.0F);
            this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187924_gx, this.func_184176_by(), 1.0F, 0.8F + this.field_70146_Z.nextFloat() * 0.4F);
            this.field_70170_p.func_72838_d(entitypotion);
        }
    }

    public float func_70047_e() {
        return 1.62F;
    }

    public void func_184724_a(boolean flag) {}
}
