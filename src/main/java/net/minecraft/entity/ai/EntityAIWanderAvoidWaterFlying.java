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
    protected Vec3d func_190864_f() {
        Vec3d vec3d = null;

        if (this.field_75457_a.func_70090_H() || this.field_75457_a.func_191953_am()) {
            vec3d = RandomPositionGenerator.func_191377_b(this.field_75457_a, 15, 15);
        }

        if (this.field_75457_a.func_70681_au().nextFloat() >= this.field_190865_h) {
            vec3d = this.func_192385_j();
        }

        return vec3d == null ? super.func_190864_f() : vec3d;
    }

    @Nullable
    private Vec3d func_192385_j() {
        BlockPos blockposition = new BlockPos(this.field_75457_a);
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();
        Iterable iterable = BlockPos.MutableBlockPos.func_191531_b(MathHelper.func_76128_c(this.field_75457_a.field_70165_t - 3.0D), MathHelper.func_76128_c(this.field_75457_a.field_70163_u - 6.0D), MathHelper.func_76128_c(this.field_75457_a.field_70161_v - 3.0D), MathHelper.func_76128_c(this.field_75457_a.field_70165_t + 3.0D), MathHelper.func_76128_c(this.field_75457_a.field_70163_u + 6.0D), MathHelper.func_76128_c(this.field_75457_a.field_70161_v + 3.0D));
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition1 = (BlockPos) iterator.next();

            if (!blockposition.equals(blockposition1)) {
                Block block = this.field_75457_a.field_70170_p.func_180495_p(blockposition_mutableblockposition1.func_189533_g(blockposition1).func_189536_c(EnumFacing.DOWN)).func_177230_c();
                boolean flag = block instanceof BlockLeaves || block == Blocks.field_150364_r || block == Blocks.field_150363_s;

                if (flag && this.field_75457_a.field_70170_p.func_175623_d(blockposition1) && this.field_75457_a.field_70170_p.func_175623_d(blockposition_mutableblockposition.func_189533_g(blockposition1).func_189536_c(EnumFacing.UP))) {
                    return new Vec3d((double) blockposition1.func_177958_n(), (double) blockposition1.func_177956_o(), (double) blockposition1.func_177952_p());
                }
            }
        }

        return null;
    }
}
