package net.minecraft.entity.ai;

import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWaterFlying extends EntityAIWanderAvoidWater {

    public EntityAIWanderAvoidWaterFlying(EntityCreature entitycreature, double d0) {
        super(entitycreature, d0);
    }

    @Nullable
    protected Vec3d getPosition() {
        Vec3d vec3d = null;

        if (this.entity.isInWater() || this.entity.isOverWater()) {
            vec3d = RandomPositionGenerator.getLandPos(this.entity, 15, 15);
        }

        if (this.entity.getRNG().nextFloat() >= this.probability) {
            vec3d = this.getTreePos();
        }

        return vec3d == null ? super.getPosition() : vec3d;
    }

    @Nullable
    private Vec3d getTreePos() {
        BlockPos blockposition = new BlockPos(this.entity);
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();
        Iterable iterable = BlockPos.MutableBlockPos.getAllInBoxMutable(MathHelper.floor(this.entity.posX - 3.0D), MathHelper.floor(this.entity.posY - 6.0D), MathHelper.floor(this.entity.posZ - 3.0D), MathHelper.floor(this.entity.posX + 3.0D), MathHelper.floor(this.entity.posY + 6.0D), MathHelper.floor(this.entity.posZ + 3.0D));
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition1 = (BlockPos) iterator.next();

            if (!blockposition.equals(blockposition1)) {
                Block block = this.entity.world.getBlockState(blockposition_mutableblockposition1.setPos(blockposition1).move(EnumFacing.DOWN)).getBlock();
                boolean flag = block instanceof BlockLeaves || block == Blocks.LOG || block == Blocks.LOG2;

                if (flag && this.entity.world.isAirBlock(blockposition1) && this.entity.world.isAirBlock(blockposition_mutableblockposition.setPos(blockposition1).move(EnumFacing.UP))) {
                    return new Vec3d((double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
                }
            }
        }

        return null;
    }
}
