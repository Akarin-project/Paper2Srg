package net.minecraft.block.state;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWorldState {

    private final World world;
    private final BlockPos pos;
    private final boolean forceLoad;
    private IBlockState state;
    private TileEntity tileEntity;
    private boolean tileEntityInitialized;

    public BlockWorldState(World world, BlockPos blockposition, boolean flag) {
        this.world = world;
        this.pos = blockposition;
        this.forceLoad = flag;
    }

    public IBlockState getBlockState() {
        if (this.state == null && (this.forceLoad || this.world.isBlockLoaded(this.pos))) {
            this.state = this.world.getBlockState(this.pos);
        }

        return this.state;
    }

    @Nullable
    public TileEntity getTileEntity() {
        if (this.tileEntity == null && !this.tileEntityInitialized) {
            this.tileEntity = this.world.getTileEntity(this.pos);
            this.tileEntityInitialized = true;
        }

        return this.tileEntity;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public static Predicate<BlockWorldState> hasState(final Predicate<IBlockState> predicate) {
        return new Predicate() {
            public boolean a(@Nullable BlockWorldState shapedetectorblock) {
                return shapedetectorblock != null && predicate.apply(shapedetectorblock.getBlockState());
            }

            public boolean apply(@Nullable Object object) {
                return this.a((BlockWorldState) object);
            }
        };
    }
}
