package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySkeleton extends AbstractSkeleton {

    public EntitySkeleton(World world) {
        super(world);
    }

    public static void func_189772_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntitySkeleton.class);
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186385_aj;
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187854_fc;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187864_fh;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187856_fd;
    }

    SoundEvent func_190727_o() {
        return SoundEvents.field_187868_fj;
    }

    public void func_70645_a(DamageSource damagesource) {
        // super.die(damagesource); // CraftBukkit
        if (damagesource.func_76346_g() instanceof EntityCreeper) {
            EntityCreeper entitycreeper = (EntityCreeper) damagesource.func_76346_g();

            if (entitycreeper.func_70830_n() && entitycreeper.func_70650_aV()) {
                entitycreeper.func_175493_co();
                this.func_70099_a(new ItemStack(Items.field_151144_bL, 1, 0), 0.0F);
            }
        }
        super.func_70645_a(damagesource); // CraftBukkit - moved from above

    }

    protected EntityArrow func_190726_a(float f) {
        ItemStack itemstack = this.func_184582_a(EntityEquipmentSlot.OFFHAND);

        if (itemstack.func_77973_b() == Items.field_185166_h) {
            EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(this.field_70170_p, this);

            entityspectralarrow.func_190547_a((EntityLivingBase) this, f);
            return entityspectralarrow;
        } else {
            EntityArrow entityarrow = super.func_190726_a(f);

            if (itemstack.func_77973_b() == Items.field_185167_i && entityarrow instanceof EntityTippedArrow) {
                ((EntityTippedArrow) entityarrow).func_184555_a(itemstack);
            }

            return entityarrow;
        }
    }
}
