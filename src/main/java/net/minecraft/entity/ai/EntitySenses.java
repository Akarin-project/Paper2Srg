package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntitySenses {

    EntityLiving entity;
    List<Entity> seenEntities = Lists.newArrayList();
    List<Entity> unseenEntities = Lists.newArrayList();

    public EntitySenses(EntityLiving entityinsentient) {
        this.entity = entityinsentient;
    }

    public void clearSensingCache() {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }

    public boolean canSee(Entity entity) {
        if (this.seenEntities.contains(entity)) {
            return true;
        } else if (this.unseenEntities.contains(entity)) {
            return false;
        } else {
            this.entity.world.profiler.startSection("canSee");
            boolean flag = this.entity.canEntityBeSeen(entity);

            this.entity.world.profiler.endSection();
            if (flag) {
                this.seenEntities.add(entity);
            } else {
                this.unseenEntities.add(entity);
            }

            return flag;
        }
    }
}
