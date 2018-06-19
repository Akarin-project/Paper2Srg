package net.minecraft.pathfinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class PathNavigateClimber extends PathNavigateGround {

    private BlockPos field_179696_f;

    public PathNavigateClimber(EntityLiving entityinsentient, World world) {
        super(entityinsentient, world);
    }

    public Path func_179680_a(BlockPos blockposition) {
        this.field_179696_f = blockposition;
        return super.func_179680_a(blockposition);
    }

    public Path func_75494_a(Entity entity) {
        this.field_179696_f = new BlockPos(entity);
        return super.func_75494_a(entity);
    }

    public boolean func_75497_a(Entity entity, double d0) {
        Path pathentity = this.func_75494_a(entity);

        if (pathentity != null) {
            return this.func_75484_a(pathentity, d0);
        } else {
            this.field_179696_f = new BlockPos(entity);
            this.field_75511_d = d0;
            return true;
        }
    }

    public void func_75501_e() {
        if (!this.func_75500_f()) {
            super.func_75501_e();
        } else {
            if (this.field_179696_f != null) {
                double d0 = (double) (this.field_75515_a.field_70130_N * this.field_75515_a.field_70130_N);

                if (this.field_75515_a.func_174831_c(this.field_179696_f) >= d0 && (this.field_75515_a.field_70163_u <= (double) this.field_179696_f.func_177956_o() || this.field_75515_a.func_174831_c(new BlockPos(this.field_179696_f.func_177958_n(), MathHelper.func_76128_c(this.field_75515_a.field_70163_u), this.field_179696_f.func_177952_p())) >= d0)) {
                    this.field_75515_a.func_70605_aq().func_75642_a((double) this.field_179696_f.func_177958_n(), (double) this.field_179696_f.func_177956_o(), (double) this.field_179696_f.func_177952_p(), this.field_75511_d);
                } else {
                    this.field_179696_f = null;
                }
            }

        }
    }
}
