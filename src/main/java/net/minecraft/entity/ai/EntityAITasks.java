package net.minecraft.entity.ai;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.profiler.Profiler;

public class EntityAITasks {

    private static final Logger field_151506_a = LogManager.getLogger();
    private final Set<EntityAITasks.EntityAITaskEntry> field_75782_a = Sets.newLinkedHashSet();
    private final Set<EntityAITasks.EntityAITaskEntry> field_75780_b = Sets.newLinkedHashSet();
    private final Profiler field_75781_c;
    private int field_75778_d;
    private int field_75779_e = 3;
    private int field_188529_g;

    public EntityAITasks(Profiler methodprofiler) {
        this.field_75781_c = methodprofiler;
    }

    public void func_75776_a(int i, EntityAIBase pathfindergoal) {
        this.field_75782_a.add(new EntityAITasks.EntityAITaskEntry(i, pathfindergoal));
    }

    public void func_85156_a(EntityAIBase pathfindergoal) {
        Iterator iterator = this.field_75782_a.iterator();

        EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem;
        EntityAIBase pathfindergoal1;

        do {
            if (!iterator.hasNext()) {
                return;
            }

            pathfindergoalselector_pathfindergoalselectoritem = (EntityAITasks.EntityAITaskEntry) iterator.next();
            pathfindergoal1 = pathfindergoalselector_pathfindergoalselectoritem.field_75733_a;
        } while (pathfindergoal1 != pathfindergoal);

        if (pathfindergoalselector_pathfindergoalselectoritem.field_188524_c) {
            pathfindergoalselector_pathfindergoalselectoritem.field_188524_c = false;
            pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75251_c();
            this.field_75780_b.remove(pathfindergoalselector_pathfindergoalselectoritem);
        }

        iterator.remove();
    }

    public void func_75774_a() {
        this.field_75781_c.func_76320_a("goalSetup");
        Iterator iterator;
        EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem;

        if (this.field_75778_d++ % this.field_75779_e == 0) {
            iterator = this.field_75782_a.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselector_pathfindergoalselectoritem = (EntityAITasks.EntityAITaskEntry) iterator.next();
                if (pathfindergoalselector_pathfindergoalselectoritem.field_188524_c) {
                    if (!this.func_75775_b(pathfindergoalselector_pathfindergoalselectoritem) || !this.func_75773_a(pathfindergoalselector_pathfindergoalselectoritem)) {
                        pathfindergoalselector_pathfindergoalselectoritem.field_188524_c = false;
                        pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75251_c();
                        this.field_75780_b.remove(pathfindergoalselector_pathfindergoalselectoritem);
                    }
                } else if (this.func_75775_b(pathfindergoalselector_pathfindergoalselectoritem) && pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75250_a()) {
                    pathfindergoalselector_pathfindergoalselectoritem.field_188524_c = true;
                    pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75249_e();
                    this.field_75780_b.add(pathfindergoalselector_pathfindergoalselectoritem);
                }
            }
        } else {
            iterator = this.field_75780_b.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselector_pathfindergoalselectoritem = (EntityAITasks.EntityAITaskEntry) iterator.next();
                if (!this.func_75773_a(pathfindergoalselector_pathfindergoalselectoritem)) {
                    pathfindergoalselector_pathfindergoalselectoritem.field_188524_c = false;
                    pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75251_c();
                    iterator.remove();
                }
            }
        }

        this.field_75781_c.func_76319_b();
        if (!this.field_75780_b.isEmpty()) {
            this.field_75781_c.func_76320_a("goalTick");
            iterator = this.field_75780_b.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselector_pathfindergoalselectoritem = (EntityAITasks.EntityAITaskEntry) iterator.next();
                pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75246_d();
            }

            this.field_75781_c.func_76319_b();
        }

    }

    private boolean func_75773_a(EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem) {
        return pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75253_b();
    }

    private boolean func_75775_b(EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem) {
        if (this.field_75780_b.isEmpty()) {
            return true;
        } else if (this.func_188528_b(pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75247_h())) {
            return false;
        } else {
            Iterator iterator = this.field_75780_b.iterator();

            while (iterator.hasNext()) {
                EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem1 = (EntityAITasks.EntityAITaskEntry) iterator.next();

                if (pathfindergoalselector_pathfindergoalselectoritem1 != pathfindergoalselector_pathfindergoalselectoritem) {
                    if (pathfindergoalselector_pathfindergoalselectoritem.field_75731_b >= pathfindergoalselector_pathfindergoalselectoritem1.field_75731_b) {
                        if (!this.func_75777_a(pathfindergoalselector_pathfindergoalselectoritem, pathfindergoalselector_pathfindergoalselectoritem1)) {
                            return false;
                        }
                    } else if (!pathfindergoalselector_pathfindergoalselectoritem1.field_75733_a.func_75252_g()) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    private boolean func_75777_a(EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem, EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem1) {
        return (pathfindergoalselector_pathfindergoalselectoritem.field_75733_a.func_75247_h() & pathfindergoalselector_pathfindergoalselectoritem1.field_75733_a.func_75247_h()) == 0;
    }

    public boolean func_188528_b(int i) {
        return (this.field_188529_g & i) > 0;
    }

    public void func_188526_c(int i) {
        this.field_188529_g |= i;
    }

    public void func_188525_d(int i) {
        this.field_188529_g &= ~i;
    }

    public void func_188527_a(int i, boolean flag) {
        if (flag) {
            this.func_188525_d(i);
        } else {
            this.func_188526_c(i);
        }

    }

    class EntityAITaskEntry {

        public final EntityAIBase field_75733_a;
        public final int field_75731_b;
        public boolean field_188524_c;

        public EntityAITaskEntry(int i, EntityAIBase pathfindergoal) {
            this.field_75731_b = i;
            this.field_75733_a = pathfindergoal;
        }

        public boolean equals(@Nullable Object object) {
            return this == object ? true : (object != null && this.getClass() == object.getClass() ? this.field_75733_a.equals(((EntityAITasks.EntityAITaskEntry) object).field_75733_a) : false);
        }

        public int hashCode() {
            return this.field_75733_a.hashCode();
        }
    }
}
