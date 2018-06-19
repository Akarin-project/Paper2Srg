package net.minecraft.block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockBarrier extends Block {

    protected BlockBarrier() {
        super(Material.field_175972_I);
        this.func_149722_s();
        this.func_149752_b(6000001.0F);
        this.func_149649_H();
        this.field_149785_s = true;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.INVISIBLE;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {}
}
