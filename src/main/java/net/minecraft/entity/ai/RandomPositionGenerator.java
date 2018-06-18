package net.minecraft.entity.ai;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RandomPositionGenerator {

    private static Vec3d staticVector = Vec3d.ZERO;

    @Nullable
    public static Vec3d findRandomTarget(EntityCreature entitycreature, int i, int j) {
        return findRandomTargetBlock(entitycreature, i, j, (Vec3d) null);
    }

    @Nullable
    public static Vec3d getLandPos(EntityCreature entitycreature, int i, int j) {
        return generateRandomPos(entitycreature, i, j, (Vec3d) null, false);
    }

    @Nullable
    public static Vec3d findRandomTargetBlockTowards(EntityCreature entitycreature, int i, int j, Vec3d vec3d) {
        RandomPositionGenerator.staticVector = vec3d.subtract(entitycreature.posX, entitycreature.posY, entitycreature.posZ);
        return findRandomTargetBlock(entitycreature, i, j, RandomPositionGenerator.staticVector);
    }

    @Nullable
    public static Vec3d findRandomTargetBlockAwayFrom(EntityCreature entitycreature, int i, int j, Vec3d vec3d) {
        RandomPositionGenerator.staticVector = (new Vec3d(entitycreature.posX, entitycreature.posY, entitycreature.posZ)).subtract(vec3d);
        return findRandomTargetBlock(entitycreature, i, j, RandomPositionGenerator.staticVector);
    }

    @Nullable
    private static Vec3d findRandomTargetBlock(EntityCreature entitycreature, int i, int j, @Nullable Vec3d vec3d) {
        return generateRandomPos(entitycreature, i, j, vec3d, true);
    }

    @Nullable
    private static Vec3d generateRandomPos(EntityCreature entitycreature, int i, int j, @Nullable Vec3d vec3d, boolean flag) {
        PathNavigate navigationabstract = entitycreature.getNavigator();
        Random random = entitycreature.getRNG();
        boolean flag1;

        if (entitycreature.hasHome()) {
            double d0 = entitycreature.getHomePosition().distanceSq((double) MathHelper.floor(entitycreature.posX), (double) MathHelper.floor(entitycreature.posY), (double) MathHelper.floor(entitycreature.posZ)) + 4.0D;
            double d1 = (double) (entitycreature.getMaximumHomeDistance() + (float) i);

            flag1 = d0 < d1 * d1;
        } else {
            flag1 = false;
        }

        boolean flag2 = false;
        float f = -99999.0F;
        int k = 0;
        int l = 0;
        int i1 = 0;

        for (int j1 = 0; j1 < 10; ++j1) {
            int k1 = random.nextInt(2 * i + 1) - i;
            int l1 = random.nextInt(2 * j + 1) - j;
            int i2 = random.nextInt(2 * i + 1) - i;

            if (vec3d == null || (double) k1 * vec3d.x + (double) i2 * vec3d.z >= 0.0D) {
                BlockPos blockposition;

                if (entitycreature.hasHome() && i > 1) {
                    blockposition = entitycreature.getHomePosition();
                    if (entitycreature.posX > (double) blockposition.getX()) {
                        k1 -= random.nextInt(i / 2);
                    } else {
                        k1 += random.nextInt(i / 2);
                    }

                    if (entitycreature.posZ > (double) blockposition.getZ()) {
                        i2 -= random.nextInt(i / 2);
                    } else {
                        i2 += random.nextInt(i / 2);
                    }
                }

                blockposition = new BlockPos((double) k1 + entitycreature.posX, (double) l1 + entitycreature.posY, (double) i2 + entitycreature.posZ);
                if ((!flag1 || entitycreature.isWithinHomeDistanceFromPosition(blockposition)) && navigationabstract.canEntityStandOnPos(blockposition)) {
                    if (!flag) {
                        blockposition = moveAboveSolid(blockposition, entitycreature);
                        if (isWaterDestination(blockposition, entitycreature)) {
                            continue;
                        }
                    }

                    float f1 = entitycreature.getBlockPathWeight(blockposition);

                    if (f1 > f) {
                        f = f1;
                        k = k1;
                        l = l1;
                        i1 = i2;
                        flag2 = true;
                    }
                }
            }
        }

        if (flag2) {
            return new Vec3d((double) k + entitycreature.posX, (double) l + entitycreature.posY, (double) i1 + entitycreature.posZ);
        } else {
            return null;
        }
    }

    private static BlockPos moveAboveSolid(BlockPos blockposition, EntityCreature entitycreature) {
        if (!entitycreature.world.getBlockState(blockposition).getMaterial().isSolid()) {
            return blockposition;
        } else {
            BlockPos blockposition1;

            for (blockposition1 = blockposition.up(); blockposition1.getY() < entitycreature.world.getHeight() && entitycreature.world.getBlockState(blockposition1).getMaterial().isSolid(); blockposition1 = blockposition1.up()) {
                ;
            }

            return blockposition1;
        }
    }

    private static boolean isWaterDestination(BlockPos blockposition, EntityCreature entitycreature) {
        return entitycreature.world.getBlockState(blockposition).getMaterial() == Material.WATER;
    }
}
