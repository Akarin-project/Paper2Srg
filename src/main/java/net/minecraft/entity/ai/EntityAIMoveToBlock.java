package net.minecraft.entity.ai;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public abstract class EntityAIMoveToBlock extends EntityAIBase {

    private final EntityCreature field_179495_c; public EntityCreature getEntity() { return field_179495_c; } // Paper - OBFHELPER
    private final double field_179492_d;
    protected int field_179496_a;
    private int field_179493_e;
    private int field_179490_f;
    protected BlockPos field_179494_b; public BlockPos getTarget() { return field_179494_b; } public void setTarget(BlockPos pos) { this.field_179494_b = pos; getEntity().movingTarget = pos != BlockPos.field_177992_a ? pos : null; } // Paper - OBFHELPER

    // Paper start
    @Override
    public void onTaskReset() {
        super.onTaskReset();
        setTarget(BlockPos.field_177992_a);
    }
    // Paper end

    private boolean field_179491_g;
    private final int field_179497_h;

    public EntityAIMoveToBlock(EntityCreature entitycreature, double d0, int i) {
        this.field_179494_b = BlockPos.field_177992_a;
        this.field_179495_c = entitycreature;
        this.field_179492_d = d0;
        this.field_179497_h = i;
        this.func_75248_a(5);
    }

    public boolean func_75250_a() {
        if (this.field_179496_a > 0) {
            --this.field_179496_a;
            return false;
        } else {
            this.field_179496_a = 200 + this.field_179495_c.func_70681_au().nextInt(200);
            return this.func_179489_g();
        }
    }

    public boolean func_75253_b() {
        return this.field_179493_e >= -this.field_179490_f && this.field_179493_e <= 1200 && this.func_179488_a(this.field_179495_c.field_70170_p, this.field_179494_b);
    }

    public void func_75249_e() {
        this.field_179495_c.func_70661_as().func_75492_a((double) ((float) this.field_179494_b.func_177958_n()) + 0.5D, (double) (this.field_179494_b.func_177956_o() + 1), (double) ((float) this.field_179494_b.func_177952_p()) + 0.5D, this.field_179492_d);
        this.field_179493_e = 0;
        this.field_179490_f = this.field_179495_c.func_70681_au().nextInt(this.field_179495_c.func_70681_au().nextInt(1200) + 1200) + 1200;
    }

    public void func_75246_d() {
        if (this.field_179495_c.func_174831_c(this.field_179494_b.func_177984_a()) > 1.0D) {
            this.field_179491_g = false;
            ++this.field_179493_e;
            if (this.field_179493_e % 40 == 0) {
                this.field_179495_c.func_70661_as().func_75492_a((double) ((float) this.field_179494_b.func_177958_n()) + 0.5D, (double) (this.field_179494_b.func_177956_o() + 1), (double) ((float) this.field_179494_b.func_177952_p()) + 0.5D, this.field_179492_d);
            }
        } else {
            this.field_179491_g = true;
            --this.field_179493_e;
        }

    }

    protected boolean func_179487_f() {
        return this.field_179491_g;
    }

    private boolean func_179489_g() {
        int i = this.field_179497_h;
        boolean flag = true;
        BlockPos blockposition = new BlockPos(this.field_179495_c);

        for (int j = 0; j <= 1; j = j > 0 ? -j : 1 - j) {
            for (int k = 0; k < i; ++k) {
                for (int l = 0; l <= k; l = l > 0 ? -l : 1 - l) {
                    for (int i1 = l < k && l > -k ? k : 0; i1 <= k; i1 = i1 > 0 ? -i1 : 1 - i1) {
                        BlockPos blockposition1 = blockposition.func_177982_a(l, j - 1, i1);

                        if (this.field_179495_c.func_180485_d(blockposition1) && this.func_179488_a(this.field_179495_c.field_70170_p, blockposition1)) {
                            setTarget(blockposition1); // Paper
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    protected abstract boolean func_179488_a(World world, BlockPos blockposition);
}
