package net.minecraft.entity.ai;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.profiler.Profiler;

public class EntityAITasks {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Set<EntityAITasks.EntityAITaskEntry> taskEntries = Sets.newLinkedHashSet();
    private final Set<EntityAITasks.EntityAITaskEntry> executingTaskEntries = Sets.newLinkedHashSet();
    private final Profiler profiler;
    private int tickCount;
    private int tickRate = 3;
    private int disabledControlFlags;

    public EntityAITasks(Profiler methodprofiler) {
        this.profiler = methodprofiler;
    }

    public void addTask(int i, EntityAIBase pathfindergoal) {
        this.taskEntries.add(new EntityAITasks.EntityAITaskEntry(i, pathfindergoal));
    }

    public void removeTask(EntityAIBase pathfindergoal) {
        Iterator iterator = this.taskEntries.iterator();

        EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem;
        EntityAIBase pathfindergoal1;

        do {
            if (!iterator.hasNext()) {
                return;
            }

            pathfindergoalselector_pathfindergoalselectoritem = (EntityAITasks.EntityAITaskEntry) iterator.next();
            pathfindergoal1 = pathfindergoalselector_pathfindergoalselectoritem.action;
        } while (pathfindergoal1 != pathfindergoal);

        if (pathfindergoalselector_pathfindergoalselectoritem.using) {
            pathfindergoalselector_pathfindergoalselectoritem.using = false;
            pathfindergoalselector_pathfindergoalselectoritem.action.resetTask();
            this.executingTaskEntries.remove(pathfindergoalselector_pathfindergoalselectoritem);
        }

        iterator.remove();
    }

    public void onUpdateTasks() {
        this.profiler.startSection("goalSetup");
        Iterator iterator;
        EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem;

        if (this.tickCount++ % this.tickRate == 0) {
            iterator = this.taskEntries.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselector_pathfindergoalselectoritem = (EntityAITasks.EntityAITaskEntry) iterator.next();
                if (pathfindergoalselector_pathfindergoalselectoritem.using) {
                    if (!this.canUse(pathfindergoalselector_pathfindergoalselectoritem) || !this.canContinue(pathfindergoalselector_pathfindergoalselectoritem)) {
                        pathfindergoalselector_pathfindergoalselectoritem.using = false;
                        pathfindergoalselector_pathfindergoalselectoritem.action.resetTask();
                        this.executingTaskEntries.remove(pathfindergoalselector_pathfindergoalselectoritem);
                    }
                } else if (this.canUse(pathfindergoalselector_pathfindergoalselectoritem) && pathfindergoalselector_pathfindergoalselectoritem.action.shouldExecute()) {
                    pathfindergoalselector_pathfindergoalselectoritem.using = true;
                    pathfindergoalselector_pathfindergoalselectoritem.action.startExecuting();
                    this.executingTaskEntries.add(pathfindergoalselector_pathfindergoalselectoritem);
                }
            }
        } else {
            iterator = this.executingTaskEntries.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselector_pathfindergoalselectoritem = (EntityAITasks.EntityAITaskEntry) iterator.next();
                if (!this.canContinue(pathfindergoalselector_pathfindergoalselectoritem)) {
                    pathfindergoalselector_pathfindergoalselectoritem.using = false;
                    pathfindergoalselector_pathfindergoalselectoritem.action.resetTask();
                    iterator.remove();
                }
            }
        }

        this.profiler.endSection();
        if (!this.executingTaskEntries.isEmpty()) {
            this.profiler.startSection("goalTick");
            iterator = this.executingTaskEntries.iterator();

            while (iterator.hasNext()) {
                pathfindergoalselector_pathfindergoalselectoritem = (EntityAITasks.EntityAITaskEntry) iterator.next();
                pathfindergoalselector_pathfindergoalselectoritem.action.updateTask();
            }

            this.profiler.endSection();
        }

    }

    private boolean canContinue(EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem) {
        return pathfindergoalselector_pathfindergoalselectoritem.action.shouldContinueExecuting();
    }

    private boolean canUse(EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem) {
        if (this.executingTaskEntries.isEmpty()) {
            return true;
        } else if (this.isControlFlagDisabled(pathfindergoalselector_pathfindergoalselectoritem.action.getMutexBits())) {
            return false;
        } else {
            Iterator iterator = this.executingTaskEntries.iterator();

            while (iterator.hasNext()) {
                EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem1 = (EntityAITasks.EntityAITaskEntry) iterator.next();

                if (pathfindergoalselector_pathfindergoalselectoritem1 != pathfindergoalselector_pathfindergoalselectoritem) {
                    if (pathfindergoalselector_pathfindergoalselectoritem.priority >= pathfindergoalselector_pathfindergoalselectoritem1.priority) {
                        if (!this.areTasksCompatible(pathfindergoalselector_pathfindergoalselectoritem, pathfindergoalselector_pathfindergoalselectoritem1)) {
                            return false;
                        }
                    } else if (!pathfindergoalselector_pathfindergoalselectoritem1.action.isInterruptible()) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem, EntityAITasks.EntityAITaskEntry pathfindergoalselector_pathfindergoalselectoritem1) {
        return (pathfindergoalselector_pathfindergoalselectoritem.action.getMutexBits() & pathfindergoalselector_pathfindergoalselectoritem1.action.getMutexBits()) == 0;
    }

    public boolean isControlFlagDisabled(int i) {
        return (this.disabledControlFlags & i) > 0;
    }

    public void disableControlFlag(int i) {
        this.disabledControlFlags |= i;
    }

    public void enableControlFlag(int i) {
        this.disabledControlFlags &= ~i;
    }

    public void setControlFlag(int i, boolean flag) {
        if (flag) {
            this.enableControlFlag(i);
        } else {
            this.disableControlFlag(i);
        }

    }

    class EntityAITaskEntry {

        public final EntityAIBase action;
        public final int priority;
        public boolean using;

        public EntityAITaskEntry(int i, EntityAIBase pathfindergoal) {
            this.priority = i;
            this.action = pathfindergoal;
        }

        public boolean equals(@Nullable Object object) {
            return this == object ? true : (object != null && this.getClass() == object.getClass() ? this.action.equals(((EntityAITasks.EntityAITaskEntry) object).action) : false);
        }

        public int hashCode() {
            return this.action.hashCode();
        }
    }
}
