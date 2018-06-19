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

    protected final Class<T> targetClass;
    private final int targetChance;
    protected final EntityAINearestAttackableTarget.Sorter sorter;
    protected final Predicate<? super T> targetEntitySelector;
    protected T targetEntity;

    public EntityAINearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag) {
        this(entitycreature, oclass, flag, false);
    }

    public EntityAINearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag, boolean flag1) {
        this(entitycreature, oclass, 10, flag, flag1, (Predicate) null);
    }

    public EntityAINearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, int i, boolean flag, boolean flag1, @Nullable final Predicate<? super T> predicate) {
        super(entitycreature, flag, flag1);
        this.targetClass = oclass;
        this.targetChance = i;
        this.sorter = new EntityAINearestAttackableTarget.Sorter(entitycreature);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate() {
            public boolean a(@Nullable T t0) {
                return t0 == null ? false : (predicate != null && !predicate.apply(t0) ? false : (!EntitySelectors.NOT_SPECTATING.apply(t0) ? false : EntityAINearestAttackableTarget.this.isSuitableTarget(t0, false)));
            }

            @Override
            public boolean apply(@Nullable Object object) {
                return this.a((T) object); // CraftBukkit - fix decompile error
            }
        };
    }

    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        } else if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class) {
            List list = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

            if (list.isEmpty()) {
                return false;
            } else {
                Collections.sort(list, this.sorter);
                this.targetEntity = (T) list.get(0); // CraftBukkit - fix decompile error
                return true;
            }
        } else {
            this.targetEntity = (T) this.taskOwner.world.getNearestAttackablePlayer(this.taskOwner.posX, this.taskOwner.posY + this.taskOwner.getEyeHeight(), this.taskOwner.posZ, this.getTargetDistance(), this.getTargetDistance(), new Function<EntityPlayer, Double>() { // CraftBukkit - fix decompile error
                @Nullable
                public Double a(@Nullable EntityPlayer entityhuman) {
                    ItemStack itemstack = entityhuman.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                    if (itemstack.getItem() == Items.SKULL) {
                        int i = itemstack.getItemDamage();
                        boolean flag = EntityAINearestAttackableTarget.this.taskOwner instanceof EntitySkeleton && i == 0;
                        boolean flag1 = EntityAINearestAttackableTarget.this.taskOwner instanceof EntityZombie && i == 2;
                        boolean flag2 = EntityAINearestAttackableTarget.this.taskOwner instanceof EntityCreeper && i == 4;

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
            }, (Predicate<EntityPlayer>) this.targetEntitySelector); // CraftBukkit - fix decompile error
            return this.targetEntity != null;
        }
    }

    protected AxisAlignedBB getTargetableArea(double d0) {
        return this.taskOwner.getEntityBoundingBox().grow(d0, 4.0D, d0);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.setGoalTarget(this.targetEntity, targetEntity instanceof EntityPlayerMP ? org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_PLAYER : org.bukkit.event.entity.EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true); // Craftbukkit - reason
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity> {

        private final Entity entity;

        public Sorter(Entity entity) {
            this.entity = entity;
        }

        @Override
        public int compare(Entity entity, Entity entity1) {
            double d0 = this.entity.getDistanceSq(entity);
            double d1 = this.entity.getDistanceSq(entity1);

            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}
