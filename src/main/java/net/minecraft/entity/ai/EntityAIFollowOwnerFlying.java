package net.minecraft.entity.ai;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.BlockPos;


public class EntityAIFollowOwnerFlying extends EntityAIFollowOwner {

    public EntityAIFollowOwnerFlying(EntityTameable entitytameableanimal, double d0, float f, float f1) {
        super(entitytameableanimal, d0, f, f1);
    }

    protected boolean isTeleportFriendlyBlock(int i, int j, int k, int l, int i1) {
        IBlockState iblockdata = this.world.getBlockState(new BlockPos(i + l, k - 1, j + i1));

        return (iblockdata.isTopSolid() || iblockdata.getMaterial() == Material.LEAVES) && this.world.isAirBlock(new BlockPos(i + l, k, j + i1)) && this.world.isAirBlock(new BlockPos(i + l, k + 1, j + i1));
    }
}
