package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals {

    Predicate<Entity> MOB_SELECTOR = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof IMob;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    Predicate<Entity> VISIBLE_MOB_SELECTOR = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof IMob && !entity.isInvisible();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
}
