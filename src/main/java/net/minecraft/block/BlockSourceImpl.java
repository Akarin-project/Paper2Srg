package net.minecraft.block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockSourceImpl implements IBlockSource {

    private final World field_82627_a;
    private final BlockPos field_179317_b;

    public BlockSourceImpl(World world, BlockPos blockposition) {
        this.field_82627_a = world;
        this.field_179317_b = blockposition;
    }

    @Override
    public World func_82618_k() {
        return this.field_82627_a;
    }

    @Override
    public double func_82615_a() {
        return this.field_179317_b.func_177958_n() + 0.5D;
    }

    @Override
    public double func_82617_b() {
        return this.field_179317_b.func_177956_o() + 0.5D;
    }

    @Override
    public double func_82616_c() {
        return this.field_179317_b.func_177952_p() + 0.5D;
    }

    @Override
    public BlockPos func_180699_d() {
        return this.field_179317_b;
    }

    @Override
    public IBlockState func_189992_e() {
        return this.field_82627_a.func_180495_p(this.field_179317_b);
    }

    @Override
    public <T extends TileEntity> T func_150835_j() {
        return (T) this.field_82627_a.func_175625_s(this.field_179317_b);
    }
}
