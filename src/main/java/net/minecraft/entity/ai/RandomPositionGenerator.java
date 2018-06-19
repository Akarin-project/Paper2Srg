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

    private static Vec3d field_75465_a = Vec3d.field_186680_a;

    @Nullable
    public static Vec3d func_75463_a(EntityCreature entitycreature, int i, int j) {
        return func_75462_c(entitycreature, i, j, (Vec3d) null);
    }

    @Nullable
    public static Vec3d func_191377_b(EntityCreature entitycreature, int i, int j) {
        return func_191379_a(entitycreature, i, j, (Vec3d) null, false);
    }

    @Nullable
    public static Vec3d func_75464_a(EntityCreature entitycreature, int i, int j, Vec3d vec3d) {
        RandomPositionGenerator.field_75465_a = vec3d.func_178786_a(entitycreature.field_70165_t, entitycreature.field_70163_u, entitycreature.field_70161_v);
        return func_75462_c(entitycreature, i, j, RandomPositionGenerator.field_75465_a);
    }

    @Nullable
    public static Vec3d func_75461_b(EntityCreature entitycreature, int i, int j, Vec3d vec3d) {
        RandomPositionGenerator.field_75465_a = (new Vec3d(entitycreature.field_70165_t, entitycreature.field_70163_u, entitycreature.field_70161_v)).func_178788_d(vec3d);
        return func_75462_c(entitycreature, i, j, RandomPositionGenerator.field_75465_a);
    }

    @Nullable
    private static Vec3d func_75462_c(EntityCreature entitycreature, int i, int j, @Nullable Vec3d vec3d) {
        return func_191379_a(entitycreature, i, j, vec3d, true);
    }

    @Nullable
    private static Vec3d func_191379_a(EntityCreature entitycreature, int i, int j, @Nullable Vec3d vec3d, boolean flag) {
        PathNavigate navigationabstract = entitycreature.func_70661_as();
        Random random = entitycreature.func_70681_au();
        boolean flag1;

        if (entitycreature.func_110175_bO()) {
            double d0 = entitycreature.func_180486_cf().func_177954_c((double) MathHelper.func_76128_c(entitycreature.field_70165_t), (double) MathHelper.func_76128_c(entitycreature.field_70163_u), (double) MathHelper.func_76128_c(entitycreature.field_70161_v)) + 4.0D;
            double d1 = (double) (entitycreature.func_110174_bM() + (float) i);

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

            if (vec3d == null || (double) k1 * vec3d.field_72450_a + (double) i2 * vec3d.field_72449_c >= 0.0D) {
                BlockPos blockposition;

                if (entitycreature.func_110175_bO() && i > 1) {
                    blockposition = entitycreature.func_180486_cf();
                    if (entitycreature.field_70165_t > (double) blockposition.func_177958_n()) {
                        k1 -= random.nextInt(i / 2);
                    } else {
                        k1 += random.nextInt(i / 2);
                    }

                    if (entitycreature.field_70161_v > (double) blockposition.func_177952_p()) {
                        i2 -= random.nextInt(i / 2);
                    } else {
                        i2 += random.nextInt(i / 2);
                    }
                }

                blockposition = new BlockPos((double) k1 + entitycreature.field_70165_t, (double) l1 + entitycreature.field_70163_u, (double) i2 + entitycreature.field_70161_v);
                if ((!flag1 || entitycreature.func_180485_d(blockposition)) && navigationabstract.func_188555_b(blockposition)) {
                    if (!flag) {
                        blockposition = func_191378_a(blockposition, entitycreature);
                        if (func_191380_b(blockposition, entitycreature)) {
                            continue;
                        }
                    }

                    float f1 = entitycreature.func_180484_a(blockposition);

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
            return new Vec3d((double) k + entitycreature.field_70165_t, (double) l + entitycreature.field_70163_u, (double) i1 + entitycreature.field_70161_v);
        } else {
            return null;
        }
    }

    private static BlockPos func_191378_a(BlockPos blockposition, EntityCreature entitycreature) {
        if (!entitycreature.field_70170_p.func_180495_p(blockposition).func_185904_a().func_76220_a()) {
            return blockposition;
        } else {
            BlockPos blockposition1;

            for (blockposition1 = blockposition.func_177984_a(); blockposition1.func_177956_o() < entitycreature.field_70170_p.func_72800_K() && entitycreature.field_70170_p.func_180495_p(blockposition1).func_185904_a().func_76220_a(); blockposition1 = blockposition1.func_177984_a()) {
                ;
            }

            return blockposition1;
        }
    }

    private static boolean func_191380_b(BlockPos blockposition, EntityCreature entitycreature) {
        return entitycreature.field_70170_p.func_180495_p(blockposition).func_185904_a() == Material.field_151586_h;
    }
}
