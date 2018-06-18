package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveThroughVillage extends EntityAIBase {

    private final EntityCreature entity;
    private final double movementSpeed;
    private Path path;
    private VillageDoorInfo doorInfo;
    private final boolean isNocturnal;
    private final List<VillageDoorInfo> doorList = Lists.newArrayList();

    public EntityAIMoveThroughVillage(EntityCreature entitycreature, double d0, boolean flag) {
        this.entity = entitycreature;
        this.movementSpeed = d0;
        this.isNocturnal = flag;
        this.setMutexBits(1);
        if (!(entitycreature.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }

    public boolean shouldExecute() {
        this.resizeDoorList();
        if (this.isNocturnal && this.entity.world.isDaytime()) {
            return false;
        } else {
            Village village = this.entity.world.getVillageCollection().getNearestVillage(new BlockPos(this.entity), 0);

            if (village == null) {
                return false;
            } else {
                this.doorInfo = this.findNearestDoor(village);
                if (this.doorInfo == null) {
                    return false;
                } else {
                    PathNavigateGround navigation = (PathNavigateGround) this.entity.getNavigator();
                    boolean flag = navigation.getEnterDoors();

                    navigation.setBreakDoors(false);
                    this.path = navigation.getPathToPos(this.doorInfo.getDoorBlockPos());
                    navigation.setBreakDoors(flag);
                    if (this.path != null) {
                        return true;
                    } else {
                        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 10, 7, new Vec3d((double) this.doorInfo.getDoorBlockPos().getX(), (double) this.doorInfo.getDoorBlockPos().getY(), (double) this.doorInfo.getDoorBlockPos().getZ()));

                        if (vec3d == null) {
                            return false;
                        } else {
                            navigation.setBreakDoors(false);
                            this.path = this.entity.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                            navigation.setBreakDoors(flag);
                            return this.path != null;
                        }
                    }
                }
            }
        }
    }

    public boolean shouldContinueExecuting() {
        if (this.entity.getNavigator().noPath()) {
            return false;
        } else {
            float f = this.entity.width + 4.0F;

            return this.entity.getDistanceSq(this.doorInfo.getDoorBlockPos()) > (double) (f * f);
        }
    }

    public void startExecuting() {
        this.entity.getNavigator().setPath(this.path, this.movementSpeed);
    }

    public void resetTask() {
        if (this.entity.getNavigator().noPath() || this.entity.getDistanceSq(this.doorInfo.getDoorBlockPos()) < 16.0D) {
            this.doorList.add(this.doorInfo);
        }

    }

    private VillageDoorInfo findNearestDoor(Village village) {
        VillageDoorInfo villagedoor = null;
        int i = Integer.MAX_VALUE;
        List list = village.getVillageDoorInfoList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor1 = (VillageDoorInfo) iterator.next();
            int j = villagedoor1.getDistanceSquared(MathHelper.floor(this.entity.posX), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ));

            if (j < i && !this.doesDoorListContain(villagedoor1)) {
                villagedoor = villagedoor1;
                i = j;
            }
        }

        return villagedoor;
    }

    private boolean doesDoorListContain(VillageDoorInfo villagedoor) {
        Iterator iterator = this.doorList.iterator();

        VillageDoorInfo villagedoor1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            villagedoor1 = (VillageDoorInfo) iterator.next();
        } while (!villagedoor.getDoorBlockPos().equals(villagedoor1.getDoorBlockPos()));

        return true;
    }

    private void resizeDoorList() {
        if (this.doorList.size() > 15) {
            this.doorList.remove(0);
        }

    }
}
