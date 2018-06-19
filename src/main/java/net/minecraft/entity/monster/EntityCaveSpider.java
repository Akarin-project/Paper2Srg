package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityCaveSpider extends EntitySpider {

    public EntityCaveSpider(World world) {
        super(world);
        this.func_70105_a(0.7F, 0.5F);
    }

    public static void func_189775_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityCaveSpider.class);
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(12.0D);
    }

    public boolean func_70652_k(Entity entity) {
        if (super.func_70652_k(entity)) {
            if (entity instanceof EntityLivingBase) {
                byte b0 = 0;

                if (this.field_70170_p.func_175659_aa() == EnumDifficulty.NORMAL) {
                    b0 = 7;
                } else if (this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD) {
                    b0 = 15;
                }

                if (b0 > 0) {
                    ((EntityLivingBase) entity).func_70690_d(new PotionEffect(MobEffects.field_76436_u, b0 * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        return groupdataentity;
    }

    public float func_70047_e() {
        return 0.45F;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186436_r;
    }
}
