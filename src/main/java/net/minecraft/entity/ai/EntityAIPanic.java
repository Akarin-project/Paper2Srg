package net.minecraft.entity.ai;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIPanic extends EntityAIBase {

    protected final EntityCreature creature;
    protected double speed;
    protected double randPosX;
    protected double randPosY;
    protected double randPosZ;

    public EntityAIPanic(EntityCreature entitycreature, double d0) {
        this.creature = entitycreature;
        this.speed = d0;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.creature.getRevengeTarget() == null && !this.creature.isBurning()) {
            return false;
        } else {
            if (this.creature.isBurning()) {
                BlockPos blockposition = this.getRandPos(this.creature.world, this.creature, 5, 4);

                if (blockposition != null) {
                    this.randPosX = (double) blockposition.getX();
                    this.randPosY = (double) blockposition.getY();
                    this.randPosZ = (double) blockposition.getZ();
                    return true;
                }
            }

            return this.findRandomPosition();
        }
    }

    protected boolean findRandomPosition() {
        Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 5, 4);

        if (vec3d == null) {
            return false;
        } else {
            this.randPosX = vec3d.x;
            this.randPosY = vec3d.y;
            this.randPosZ = vec3d.z;
            return true;
        }
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
    }

    public boolean shouldContinueExecuting() {
        // CraftBukkit start - introduce a temporary timeout hack until this is fixed properly
        if ((this.creature.ticksExisted - this.creature.revengeTimer) > 100) {
            this.creature.onKillEntity((EntityLivingBase) null);
            return false;
        }
        // CraftBukkit end
        return !this.creature.getNavigator().noPath();
    }

    @Nullable
    private BlockPos getRandPos(World world, Entity entity, int i, int j) {
        BlockPos blockposition = new BlockPos(entity);
        int k = blockposition.getX();
        int l = blockposition.getY();
        int i1 = blockposition.getZ();
        float f = (float) (i * i * j * 2);
        BlockPos blockposition1 = null;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (int j1 = k - i; j1 <= k + i; ++j1) {
            for (int k1 = l - j; k1 <= l + j; ++k1) {
                for (int l1 = i1 - i; l1 <= i1 + i; ++l1) {
                    blockposition_mutableblockposition.setPos(j1, k1, l1);
                    IBlockState iblockdata = world.getBlockState(blockposition_mutableblockposition);

                    if (iblockdata.getMaterial() == Material.WATER) {
                        float f1 = (float) ((j1 - k) * (j1 - k) + (k1 - l) * (k1 - l) + (l1 - i1) * (l1 - i1));

                        if (f1 < f) {
                            f = f1;
                            blockposition1 = new BlockPos(blockposition_mutableblockposition);
                        }
                    }
                }
            }
        }

        return blockposition1;
    }
}
