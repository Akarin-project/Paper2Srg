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

    protected final EntityCreature field_75267_a;
    protected double field_75265_b;
    protected double field_75266_c;
    protected double field_75263_d;
    protected double field_75264_e;

    public EntityAIPanic(EntityCreature entitycreature, double d0) {
        this.field_75267_a = entitycreature;
        this.field_75265_b = d0;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        if (this.field_75267_a.func_70643_av() == null && !this.field_75267_a.func_70027_ad()) {
            return false;
        } else {
            if (this.field_75267_a.func_70027_ad()) {
                BlockPos blockposition = this.func_188497_a(this.field_75267_a.field_70170_p, this.field_75267_a, 5, 4);

                if (blockposition != null) {
                    this.field_75266_c = (double) blockposition.func_177958_n();
                    this.field_75263_d = (double) blockposition.func_177956_o();
                    this.field_75264_e = (double) blockposition.func_177952_p();
                    return true;
                }
            }

            return this.func_190863_f();
        }
    }

    protected boolean func_190863_f() {
        Vec3d vec3d = RandomPositionGenerator.func_75463_a(this.field_75267_a, 5, 4);

        if (vec3d == null) {
            return false;
        } else {
            this.field_75266_c = vec3d.field_72450_a;
            this.field_75263_d = vec3d.field_72448_b;
            this.field_75264_e = vec3d.field_72449_c;
            return true;
        }
    }

    public void func_75249_e() {
        this.field_75267_a.func_70661_as().func_75492_a(this.field_75266_c, this.field_75263_d, this.field_75264_e, this.field_75265_b);
    }

    public boolean func_75253_b() {
        // CraftBukkit start - introduce a temporary timeout hack until this is fixed properly
        if ((this.field_75267_a.field_70173_aa - this.field_75267_a.field_70756_c) > 100) {
            this.field_75267_a.func_70074_a((EntityLivingBase) null);
            return false;
        }
        // CraftBukkit end
        return !this.field_75267_a.func_70661_as().func_75500_f();
    }

    @Nullable
    private BlockPos func_188497_a(World world, Entity entity, int i, int j) {
        BlockPos blockposition = new BlockPos(entity);
        int k = blockposition.func_177958_n();
        int l = blockposition.func_177956_o();
        int i1 = blockposition.func_177952_p();
        float f = (float) (i * i * j * 2);
        BlockPos blockposition1 = null;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (int j1 = k - i; j1 <= k + i; ++j1) {
            for (int k1 = l - j; k1 <= l + j; ++k1) {
                for (int l1 = i1 - i; l1 <= i1 + i; ++l1) {
                    blockposition_mutableblockposition.func_181079_c(j1, k1, l1);
                    IBlockState iblockdata = world.func_180495_p(blockposition_mutableblockposition);

                    if (iblockdata.func_185904_a() == Material.field_151586_h) {
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
