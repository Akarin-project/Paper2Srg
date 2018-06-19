package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityDonkey extends AbstractChestHorse {

    public EntityDonkey(World world) {
        super(world);
    }

    public static void func_190699_b(DataFixer dataconvertermanager) {
        AbstractChestHorse.func_190694_b(dataconvertermanager, EntityDonkey.class);
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_191190_H;
    }

    protected SoundEvent func_184639_G() {
        super.func_184639_G();
        return SoundEvents.field_187580_av;
    }

    protected SoundEvent func_184615_bR() {
        super.func_184615_bR();
        return SoundEvents.field_187586_ay;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        super.func_184601_bQ(damagesource);
        return SoundEvents.field_187588_az;
    }

    public boolean func_70878_b(EntityAnimal entityanimal) {
        return entityanimal == this ? false : (!(entityanimal instanceof EntityDonkey) && !(entityanimal instanceof EntityHorse) ? false : this.func_110200_cJ() && ((AbstractHorse) entityanimal).func_110200_cJ());
    }

    public EntityAgeable func_90011_a(EntityAgeable entityageable) {
        Object object = entityageable instanceof EntityHorse ? new EntityMule(this.field_70170_p) : new EntityDonkey(this.field_70170_p);

        this.func_190681_a(entityageable, (AbstractHorse) object);
        return (EntityAgeable) object;
    }
}
