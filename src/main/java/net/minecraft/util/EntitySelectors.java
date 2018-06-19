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

    public static final Predicate<Entity> field_94557_a = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity.func_70089_S();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final Predicate<Entity> field_152785_b = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity.func_70089_S() && !entity.func_184207_aI() && !entity.func_184218_aH();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final Predicate<Entity> field_96566_b = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof IInventory && entity.func_70089_S();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final Predicate<Entity> field_188444_d = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return !(entity instanceof EntityPlayer) || !((EntityPlayer) entity).func_175149_v() && !((EntityPlayer) entity).func_184812_l_();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    public static final Predicate<Entity> field_180132_d = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return !(entity instanceof EntityPlayer) || !((EntityPlayer) entity).func_175149_v();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };

    public static <T extends Entity> Predicate<T> func_188443_a(final double d0, final double d1, final double d2, double d3) {
        final double d4 = d3 * d3;

        return new Predicate() {
            public boolean a(@Nullable T t0) {
                return t0 != null && t0.func_70092_e(d0, d1, d2) <= d3;
            }

            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        };
    }

    public static <T extends Entity> Predicate<T> func_188442_a(final Entity entity) {
        final Team scoreboardteambase = entity.func_96124_cp();
        final Team.CollisionRule scoreboardteambase_enumteampush = scoreboardteambase == null ? Team.CollisionRule.ALWAYS : scoreboardteambase.func_186681_k();

        return scoreboardteambase_enumteampush == Team.CollisionRule.NEVER ? Predicates.alwaysFalse() : Predicates.and(EntitySelectors.field_180132_d, new Predicate() {
            public boolean a(@Nullable Entity entity) {
                if (!entity.func_70104_M()) {
                    return false;
                } else if (entity1.world.isClientSide && (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).func_175144_cb())) {
                    return false;
                } else {
                    Team scoreboardteambase = entity.func_96124_cp();
                    Team.CollisionRule scoreboardteambase_enumteampush = scoreboardteambase == null ? Team.CollisionRule.ALWAYS : scoreboardteambase.func_186681_k();

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

    public static Predicate<Entity> func_191324_b(final Entity entity) {
        return new Predicate() {
            public boolean a(@Nullable Entity entity) {
                while (true) {
                    if (entity.func_184218_aH()) {
                        entity = entity.func_184187_bx();
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

        private final ItemStack field_96567_c;

        public ArmoredMob(ItemStack itemstack) {
            this.field_96567_c = itemstack;
        }

        public boolean apply(@Nullable Entity entity) {
            if (!entity.func_70089_S()) {
                return false;
            } else if (!(entity instanceof EntityLivingBase)) {
                return false;
            } else {
                EntityLivingBase entityliving = (EntityLivingBase) entity;

                return !entityliving.func_184582_a(EntityLiving.func_184640_d(this.field_96567_c)).func_190926_b() ? false : (entityliving instanceof EntityLiving ? ((EntityLiving) entityliving).func_98052_bS() : (entityliving instanceof EntityArmorStand ? true : entityliving instanceof EntityPlayer));
            }
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((Entity) object);
        }
    }
}
