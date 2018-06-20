package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockMatcher implements Predicate<IBlockState> {

    private final Block field_177644_a;

    private BlockMatcher(Block block) {
        this.field_177644_a = block;
    }

    public static BlockMatcher func_177642_a(Block block) {
        return new BlockMatcher(block);
    }

    @Override
    public boolean apply(@Nullable IBlockState iblockdata) {
        return iblockdata != null && iblockdata.func_177230_c() == this.field_177644_a;
    }
}
