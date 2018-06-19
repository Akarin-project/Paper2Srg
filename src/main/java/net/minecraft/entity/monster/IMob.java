package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals {

    Predicate<Entity> field_82192_a = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof IMob;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    Predicate<Entity> field_175450_e = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof IMob && !entity.func_82150_aj();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
}
