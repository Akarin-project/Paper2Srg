package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;


public class BlockBreakable extends Block {

    private final boolean field_149996_a;

    protected BlockBreakable(Material material, boolean flag) {
        this(material, flag, material.func_151565_r());
    }

    protected BlockBreakable(Material material, boolean flag, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.field_149996_a = flag;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }
}
