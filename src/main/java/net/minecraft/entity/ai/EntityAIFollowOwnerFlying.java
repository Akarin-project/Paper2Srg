package net.minecraft.entity.ai;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.BlockPos;


public class EntityAIFollowOwnerFlying extends EntityAIFollowOwner {

    public EntityAIFollowOwnerFlying(EntityTameable entitytameableanimal, double d0, float f, float f1) {
        super(entitytameableanimal, d0, f, f1);
    }

    protected boolean func_192381_a(int i, int j, int k, int l, int i1) {
        IBlockState iblockdata = this.field_75342_a.func_180495_p(new BlockPos(i + l, k - 1, j + i1));

        return (iblockdata.func_185896_q() || iblockdata.func_185904_a() == Material.field_151584_j) && this.field_75342_a.func_175623_d(new BlockPos(i + l, k, j + i1)) && this.field_75342_a.func_175623_d(new BlockPos(i + l, k + 1, j + i1));
    }
}
