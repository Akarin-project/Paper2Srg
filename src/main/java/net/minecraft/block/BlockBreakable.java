package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;


public class BlockBreakable extends Block {

    private final boolean ignoreSimilarity;

    protected BlockBreakable(Material material, boolean flag) {
        this(material, flag, material.getMaterialMapColor());
    }

    protected BlockBreakable(Material material, boolean flag, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.ignoreSimilarity = flag;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }
}
