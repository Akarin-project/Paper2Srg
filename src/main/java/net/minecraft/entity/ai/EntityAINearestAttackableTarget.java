package net.minecraft.entity.ai;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget {

    protected final Class<T> field_75307_b;
    private final int field_75308_c;
    protected final EntityAINearestAttackableTarget.Sorter field_75306_g;
    protected final Predicate<? super T> field_82643_g;
    protected T field_75309_a;

    public EntityAINearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag) {
        this(entitycreature, oclass, flag, false);
    }

    public EntityAINearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag, boolean flag1) {
        this(entitycreature, oclass, 10, flag, flag1, (Predicate) null);
    }

    public EntityAINearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, int i, boolean flag, boolean flag1, @Nullable final Predicate<? super T> predicate) {
        super(entitycreature, flag, flag1);
        this.field_75307_b = oclass;
        this.field_75308_c = i;
        this.field_75306_g = new EntityAINearestAttackableTarget.Sorter(entitycreature);
        this.func_75248_a(1);
        this.field_82643_g = new Predicate() {
            public boolean a(@Nullable T t0) {
                return t0 == null ? false : (predicate != null && !predicate.apply(t0) ? false : (!EntitySelectors.field_180132_d.apply(t0) ? false : EntityAINearestAttackableTarget.this.func_75296_a(t0, false)));
            }

            @Override
            public boolean apply(@Nullable Object object) {
                return this.a((T) object); // CraftBukkit - fix decompile error
            }
        };
    }

    @Override
    public boolean func_75250_a() {
        if (this.field_75308_c > 0 && this.field_75299_d.func_70681_au().nextInt(this.field_75308_c) != 0) {
            return false;
        } else if (this.field_75307_b != EntityPlayer.class && this.field_75307_b != EntityPlayerMP.class) {
            List list = this.field_75299_d.field_70170_p.func_175647_a(this.field_75307_b, this.func_188511_a(this.func_111175_f()), this.field_82643_g);

            if (list.isEmpty()) {
                return false;
            } else {
                Collections.sort(list, this.field_75306_g);
                this.field_75309_a = (T) list.get(0); // CraftBukkit - fix decompile error
                return true;
            }
        } else {
            this.field_75309_a = (T) this.field_75299_d.field_70170_p.func_184150_a(this.field_75299_d.field_70165_t, this.field_75299_d.field_70163_u + this.field_75299_d.func_70047_e(), this.field_75299_d.field_70161_v, this.func_111175_f(), this.func_111175_f(), new Function<EntityPlayer, Double>() { // CraftBukkit - fix decompile error
                @Nullable
                public Double a(@Nullable EntityPlayer entityhuman) {
                    ItemStack itemstack = entityhuman.func_184582_a(EntityEquipmentSlot.HEAD);

                    if (itemstack.func_77973_b() == Items.field_151144_bL) {
                        int i = itemstack.func_77952_i();
                        boolean flag = EntityAINearestAttackableTarget.this.field_75299_d instanceof EntitySkeleton && i == 0;
                        boolean flag1 = EntityAINearestAttackableTarget.this.field_75299_d instanceof EntityZombie && i == 2;
                        boolean flag2 = EntityAINearestAttackableTarget.this.field_75299_d instanceof EntityCreeper && i == 4;

                        if (flag || flag1 || flag2) {
                            return Double.valueOf(0.5D);
                        }
                    }

                    return Double.valueOf(1.0D);
                }

                @Override
                @Nullable
                public Double apply(@Nullable EntityPlayer object) { // CraftBukkit - fix decompile error
                    return this.a(object);
                }
            }, (Predicate<EntityPlayer>) this.field_82643_g); // CraftBukkit - fix decompile error
            return this.field_75309_a != null;
        }
    }

    protected AxisAlignedBB func_188511_a(double d0) {
        return this.field_75299_d.func_174813_aQ().func_72314_b(d0, 4.0D, d0);
    }

    @Override
    public void func_75249_e() {
        this.field_75299_d.setGoalTarget(this.field_75309_a, field_75309_a instanceof EntityPlayerMP ? org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_PLAYER : org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true); // Craftbukkit - reason
        super.func_75249_e();
    }

    public static class Sorter implements Comparator<Entity> {

        private final Entity field_75459_b;

        public Sorter(Entity entity) {
            this.field_75459_b = entity;
        }

        @Override
        public int compare(Entity entity, Entity entity1) {
            double d0 = this.field_75459_b.func_70068_e(entity);
            double d1 = this.field_75459_b.func_70068_e(entity1);

            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}
