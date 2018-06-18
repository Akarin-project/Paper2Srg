package net.minecraft.block.state;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPistonStructureHelper {

    private final World world;
    private final BlockPos pistonPos;
    private final BlockPos blockToMove;
    private final EnumFacing moveDirection;
    private final List<BlockPos> toMove = Lists.newArrayList();
    private final List<BlockPos> toDestroy = Lists.newArrayList();

    public BlockPistonStructureHelper(World world, BlockPos blockposition, EnumFacing enumdirection, boolean flag) {
        this.world = world;
        this.pistonPos = blockposition;
        if (flag) {
            this.moveDirection = enumdirection;
            this.blockToMove = blockposition.offset(enumdirection);
        } else {
            this.moveDirection = enumdirection.getOpposite();
            this.blockToMove = blockposition.offset(enumdirection, 2);
        }

    }

    public boolean canMove() {
        this.toMove.clear();
        this.toDestroy.clear();
        IBlockState iblockdata = this.world.getBlockState(this.blockToMove);

        if (!BlockPistonBase.canPush(iblockdata, this.world, this.blockToMove, this.moveDirection, false, this.moveDirection)) {
            if (iblockdata.getMobilityFlag() == EnumPushReaction.DESTROY) {
                this.toDestroy.add(this.blockToMove);
                return true;
            } else {
                return false;
            }
        } else if (!this.addBlockLine(this.blockToMove, this.moveDirection)) {
            return false;
        } else {
            for (int i = 0; i < this.toMove.size(); ++i) {
                BlockPos blockposition = (BlockPos) this.toMove.get(i);

                if (this.world.getBlockState(blockposition).getBlock() == Blocks.SLIME_BLOCK && !this.addBranchingBlocks(blockposition)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean addBlockLine(BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = this.world.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        if (iblockdata.getMaterial() == Material.AIR) {
            return true;
        } else if (!BlockPistonBase.canPush(iblockdata, this.world, blockposition, this.moveDirection, false, enumdirection)) {
            return true;
        } else if (blockposition.equals(this.pistonPos)) {
            return true;
        } else if (this.toMove.contains(blockposition)) {
            return true;
        } else {
            int i = 1;

            if (i + this.toMove.size() > 12) {
                return false;
            } else {
                while (block == Blocks.SLIME_BLOCK) {
                    BlockPos blockposition1 = blockposition.offset(this.moveDirection.getOpposite(), i);

                    iblockdata = this.world.getBlockState(blockposition1);
                    block = iblockdata.getBlock();
                    if (iblockdata.getMaterial() == Material.AIR || !BlockPistonBase.canPush(iblockdata, this.world, blockposition1, this.moveDirection, false, this.moveDirection.getOpposite()) || blockposition1.equals(this.pistonPos)) {
                        break;
                    }

                    ++i;
                    if (i + this.toMove.size() > 12) {
                        return false;
                    }
                }

                int j = 0;

                int k;

                for (k = i - 1; k >= 0; --k) {
                    this.toMove.add(blockposition.offset(this.moveDirection.getOpposite(), k));
                    ++j;
                }

                k = 1;

                while (true) {
                    BlockPos blockposition2 = blockposition.offset(this.moveDirection, k);
                    int l = this.toMove.indexOf(blockposition2);

                    if (l > -1) {
                        this.reorderListAtCollision(j, l);

                        for (int i1 = 0; i1 <= l + j; ++i1) {
                            BlockPos blockposition3 = (BlockPos) this.toMove.get(i1);

                            if (this.world.getBlockState(blockposition3).getBlock() == Blocks.SLIME_BLOCK && !this.addBranchingBlocks(blockposition3)) {
                                return false;
                            }
                        }

                        return true;
                    }

                    iblockdata = this.world.getBlockState(blockposition2);
                    if (iblockdata.getMaterial() == Material.AIR) {
                        return true;
                    }

                    if (!BlockPistonBase.canPush(iblockdata, this.world, blockposition2, this.moveDirection, true, this.moveDirection) || blockposition2.equals(this.pistonPos)) {
                        return false;
                    }

                    if (iblockdata.getMobilityFlag() == EnumPushReaction.DESTROY) {
                        this.toDestroy.add(blockposition2);
                        return true;
                    }

                    if (this.toMove.size() >= 12) {
                        return false;
                    }

                    this.toMove.add(blockposition2);
                    ++j;
                    ++k;
                }
            }
        }
    }

    private void reorderListAtCollision(int i, int j) {
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList();
        ArrayList arraylist2 = Lists.newArrayList();

        arraylist.addAll(this.toMove.subList(0, j));
        arraylist1.addAll(this.toMove.subList(this.toMove.size() - i, this.toMove.size()));
        arraylist2.addAll(this.toMove.subList(j, this.toMove.size() - i));
        this.toMove.clear();
        this.toMove.addAll(arraylist);
        this.toMove.addAll(arraylist1);
        this.toMove.addAll(arraylist2);
    }

    private boolean addBranchingBlocks(BlockPos blockposition) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (enumdirection.getAxis() != this.moveDirection.getAxis() && !this.addBlockLine(blockposition.offset(enumdirection), enumdirection)) {
                return false;
            }
        }

        return true;
    }

    public List<BlockPos> getBlocksToMove() {
        return this.toMove;
    }

    public List<BlockPos> getBlocksToDestroy() {
        return this.toDestroy;
    }
}
