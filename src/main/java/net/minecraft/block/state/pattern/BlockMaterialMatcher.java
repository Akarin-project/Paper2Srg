package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockMaterialMatcher implements Predicate<IBlockState> {

    private final Material field_189887_a;

    private BlockMaterialMatcher(Material material) {
        this.field_189887_a = material;
    }

    public static BlockMaterialMatcher func_189886_a(Material material) {
        return new BlockMaterialMatcher(material);
    }

    @Override
    public boolean apply(@Nullable IBlockState iblockdata) {
        return iblockdata != null && iblockdata.func_185904_a() == this.field_189887_a;
    }
}
