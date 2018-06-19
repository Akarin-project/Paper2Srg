package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockMatcher implements Predicate<IBlockState> {

    private final Block block;

    private BlockMatcher(Block block) {
        this.block = block;
    }

    public static BlockMatcher forBlock(Block block) {
        return new BlockMatcher(block);
    }

    @Override
    public boolean apply(@Nullable IBlockState iblockdata) {
        return iblockdata != null && iblockdata.getBlock() == this.block;
    }
}
