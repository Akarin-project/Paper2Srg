package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieHorse extends AbstractHorse {

    public EntityZombieHorse(World world) {
        super(world);
    }

    public static void func_190693_b(DataFixer dataconvertermanager) {
        AbstractHorse.func_190683_c(dataconvertermanager, EntityZombieHorse.class);
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(15.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.20000000298023224D);
        this.func_110148_a(EntityZombieHorse.field_110271_bv).func_111128_a(this.func_110245_cM());
    }

    public EnumCreatureAttribute func_70668_bt() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected SoundEvent func_184639_G() {
        super.func_184639_G();
        return SoundEvents.field_187931_he;
    }

    protected SoundEvent func_184615_bR() {
        super.func_184615_bR();
        return SoundEvents.field_187932_hf;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        super.func_184601_bQ(damagesource);
        return SoundEvents.field_187933_hg;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186397_E;
    }

    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        boolean flag = !itemstack.func_190926_b();

        if (flag && itemstack.func_77973_b() == Items.field_151063_bx) {
            return super.func_184645_a(entityhuman, enumhand);
        } else if (!this.func_110248_bS()) {
            return false;
        } else if (this.func_70631_g_()) {
            return super.func_184645_a(entityhuman, enumhand);
        } else if (entityhuman.func_70093_af()) {
            this.func_110199_f(entityhuman);
            return true;
        } else if (this.func_184207_aI()) {
            return super.func_184645_a(entityhuman, enumhand);
        } else {
            if (flag) {
                if (!this.func_110257_ck() && itemstack.func_77973_b() == Items.field_151141_av) {
                    this.func_110199_f(entityhuman);
                    return true;
                }

                if (itemstack.func_111282_a(entityhuman, (EntityLivingBase) this, enumhand)) {
                    return true;
                }
            }

            this.func_110237_h(entityhuman);
            return true;
        }
    }
}
