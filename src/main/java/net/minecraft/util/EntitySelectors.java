package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;

public final class EntitySelectors {

    public static final Predicate<Entity> IS_ALIVE = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity.isEntityAlive();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final Predicate<Entity> IS_STANDALONE = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity.isEntityAlive() && !entity.isBeingRidden() && !entity.isRiding();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final Predicate<Entity> HAS_INVENTORY = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof IInventory && entity.isEntityAlive();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final Predicate<Entity> CAN_AI_TARGET = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return !(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator() && !((EntityPlayer) entity).isCreative();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final Predicate<Entity> NOT_SPECTATING = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return !(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };

    public static <T extends Entity> Predicate<T> withinRange(final double d0, final double d1, final double d2, double d3) {
        final double d4 = d3 * d3;

        return new Predicate() {
            public boolean a(@Nullable T t0) {
                return t0 != null && t0.getDistanceSq(d0, d1, d2) <= d3;
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        };
    }

    public static <T extends Entity> Predicate<T> getTeamCollisionPredicate(final Entity entity) {
        final Team scoreboardteambase = entity.getTeam();
        final Team.CollisionRule scoreboardteambase_enumteampush = scoreboardteambase == null ? Team.CollisionRule.ALWAYS : scoreboardteambase.getCollisionRule();

        return scoreboardteambase_enumteampush == Team.CollisionRule.NEVER ? Predicates.alwaysFalse() : Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate() {
            public boolean a(@Nullable Entity entity) {
                if (!entity.canBePushed()) {
                    return false;
                } else if (entity1.world.isClientSide && (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isUser())) {
                    return false;
                } else {
                    Team scoreboardteambase = entity.getTeam();
                    Team.CollisionRule scoreboardteambase_enumteampush = scoreboardteambase == null ? Team.CollisionRule.ALWAYS : scoreboardteambase.getCollisionRule();

                    if (scoreboardteambase_enumteampush == Team.CollisionRule.NEVER) {
                        return false;
                    } else {
                        boolean flag = scoreboardteambase1 != null && scoreboardteambase1.isAlly(scoreboardteambase);

                        return (scoreboardteambase_enumteampush1 == Team.CollisionRule.HIDE_FOR_OWN_TEAM || scoreboardteambase_enumteampush == Team.CollisionRule.HIDE_FOR_OWN_TEAM) && flag ? false : scoreboardteambase_enumteampush1 != Team.CollisionRule.HIDE_FOR_OTHER_TEAMS && scoreboardteambase_enumteampush != Team.CollisionRule.HIDE_FOR_OTHER_TEAMS || flag;
                    }
                }
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        });
    }

    public static Predicate<Entity> notRiding(final Entity entity) {
        return new Predicate() {
            public boolean a(@Nullable Entity entity) {
                while (true) {
                    if (entity.isRiding()) {
                        entity = entity.getRidingEntity();
                        if (entity != entity1) {
                            continue;
                        }

                        return false;
                    }

                    return true;
                }
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        };
    }

    public static class ArmoredMob implements Predicate<Entity> {

        private final ItemStack armor;

        public ArmoredMob(ItemStack itemstack) {
            this.armor = itemstack;
        }

        public boolean apply(@Nullable Entity entity) {
            if (!entity.isEntityAlive()) {
                return false;
            } else if (!(entity instanceof EntityLivingBase)) {
                return false;
            } else {
                EntityLivingBase entityliving = (EntityLivingBase) entity;

                return !entityliving.getItemStackFromSlot(EntityLiving.getSlotForItemStack(this.armor)).isEmpty() ? false : (entityliving instanceof EntityLiving ? ((EntityLiving) entityliving).canPickUpLoot() : (entityliving instanceof EntityArmorStand ? true : entityliving instanceof EntityPlayer));
            }
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((Entity) object);
        }
    }
}
