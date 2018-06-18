package net.minecraft.block.state.pattern;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class BlockPattern {

    private final Predicate<BlockWorldState>[][][] blockMatches;
    private final int fingerLength;
    private final int thumbLength;
    private final int palmLength;

    public BlockPattern(Predicate<BlockWorldState>[][][] apredicate) {
        this.blockMatches = apredicate;
        this.fingerLength = apredicate.length;
        if (this.fingerLength > 0) {
            this.thumbLength = apredicate[0].length;
            if (this.thumbLength > 0) {
                this.palmLength = apredicate[0][0].length;
            } else {
                this.palmLength = 0;
            }
        } else {
            this.thumbLength = 0;
            this.palmLength = 0;
        }

    }

    public int getFingerLength() {
        return this.fingerLength;
    }

    public int getThumbLength() {
        return this.thumbLength;
    }

    public int getPalmLength() {
        return this.palmLength;
    }

    @Nullable
    private BlockPattern.PatternHelper checkPatternAt(BlockPos blockposition, EnumFacing enumdirection, EnumFacing enumdirection1, LoadingCache<BlockPos, BlockWorldState> loadingcache) {
        for (int i = 0; i < this.palmLength; ++i) {
            for (int j = 0; j < this.thumbLength; ++j) {
                for (int k = 0; k < this.fingerLength; ++k) {
                    if (!this.blockMatches[k][j][i].apply(loadingcache.getUnchecked(translateOffset(blockposition, enumdirection, enumdirection1, i, j, k)))) {
                        return null;
                    }
                }
            }
        }

        return new BlockPattern.PatternHelper(blockposition, enumdirection, enumdirection1, loadingcache, this.palmLength, this.thumbLength, this.fingerLength);
    }

    @Nullable
    public BlockPattern.PatternHelper match(World world, BlockPos blockposition) {
        LoadingCache loadingcache = createLoadingCache(world, false);
        int i = Math.max(Math.max(this.palmLength, this.thumbLength), this.fingerLength);
        Iterator iterator = BlockPos.getAllInBox(blockposition, blockposition.add(i - 1, i - 1, i - 1)).iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition1 = (BlockPos) iterator.next();
            EnumFacing[] aenumdirection = EnumFacing.values();
            int j = aenumdirection.length;

            for (int k = 0; k < j; ++k) {
                EnumFacing enumdirection = aenumdirection[k];
                EnumFacing[] aenumdirection1 = EnumFacing.values();
                int l = aenumdirection1.length;

                for (int i1 = 0; i1 < l; ++i1) {
                    EnumFacing enumdirection1 = aenumdirection1[i1];

                    if (enumdirection1 != enumdirection && enumdirection1 != enumdirection.getOpposite()) {
                        BlockPattern.PatternHelper shapedetector_shapedetectorcollection = this.checkPatternAt(blockposition1, enumdirection, enumdirection1, loadingcache);

                        if (shapedetector_shapedetectorcollection != null) {
                            return shapedetector_shapedetectorcollection;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static LoadingCache<BlockPos, BlockWorldState> createLoadingCache(World world, boolean flag) {
        return CacheBuilder.newBuilder().build(new BlockPattern.CacheLoader(world, flag));
    }

    protected static BlockPos translateOffset(BlockPos blockposition, EnumFacing enumdirection, EnumFacing enumdirection1, int i, int j, int k) {
        if (enumdirection != enumdirection1 && enumdirection != enumdirection1.getOpposite()) {
            Vec3i baseblockposition = new Vec3i(enumdirection.getFrontOffsetX(), enumdirection.getFrontOffsetY(), enumdirection.getFrontOffsetZ());
            Vec3i baseblockposition1 = new Vec3i(enumdirection1.getFrontOffsetX(), enumdirection1.getFrontOffsetY(), enumdirection1.getFrontOffsetZ());
            Vec3i baseblockposition2 = baseblockposition.crossProduct(baseblockposition1);

            return blockposition.add(baseblockposition1.getX() * -j + baseblockposition2.getX() * i + baseblockposition.getX() * k, baseblockposition1.getY() * -j + baseblockposition2.getY() * i + baseblockposition.getY() * k, baseblockposition1.getZ() * -j + baseblockposition2.getZ() * i + baseblockposition.getZ() * k);
        } else {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
    }

    public static class PatternHelper {

        private final BlockPos frontTopLeft;
        private final EnumFacing forwards;
        private final EnumFacing up;
        private final LoadingCache<BlockPos, BlockWorldState> lcache;
        private final int width;
        private final int height;
        private final int depth;

        public PatternHelper(BlockPos blockposition, EnumFacing enumdirection, EnumFacing enumdirection1, LoadingCache<BlockPos, BlockWorldState> loadingcache, int i, int j, int k) {
            this.frontTopLeft = blockposition;
            this.forwards = enumdirection;
            this.up = enumdirection1;
            this.lcache = loadingcache;
            this.width = i;
            this.height = j;
            this.depth = k;
        }

        public BlockPos getFrontTopLeft() {
            return this.frontTopLeft;
        }

        public EnumFacing getForwards() {
            return this.forwards;
        }

        public EnumFacing getUp() {
            return this.up;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public BlockWorldState translateOffset(int i, int j, int k) {
            return (BlockWorldState) this.lcache.getUnchecked(BlockPattern.translateOffset(this.frontTopLeft, this.getForwards(), this.getUp(), i, j, k));
        }

        public String toString() {
            return MoreObjects.toStringHelper(this).add("up", this.up).add("forwards", this.forwards).add("frontTopLeft", this.frontTopLeft).toString();
        }
    }

    static class CacheLoader extends CacheLoader<BlockPos, BlockWorldState> {

        private final World world;
        private final boolean forceLoad;

        public CacheLoader(World world, boolean flag) {
            this.world = world;
            this.forceLoad = flag;
        }

        public BlockWorldState load(BlockPos blockposition) throws Exception {
            return new BlockWorldState(this.world, blockposition, this.forceLoad);
        }

        public Object load(Object object) throws Exception {
            return this.load((BlockPos) object);
        }
    }
}
