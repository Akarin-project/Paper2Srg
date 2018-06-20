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

    private final Predicate<BlockWorldState>[][][] field_177689_a;
    private final int field_177687_b;
    private final int field_177688_c;
    private final int field_177686_d;

    public BlockPattern(Predicate<BlockWorldState>[][][] apredicate) {
        this.field_177689_a = apredicate;
        this.field_177687_b = apredicate.length;
        if (this.field_177687_b > 0) {
            this.field_177688_c = apredicate[0].length;
            if (this.field_177688_c > 0) {
                this.field_177686_d = apredicate[0][0].length;
            } else {
                this.field_177686_d = 0;
            }
        } else {
            this.field_177688_c = 0;
            this.field_177686_d = 0;
        }

    }

    public int func_185922_a() {
        return this.field_177687_b;
    }

    public int func_177685_b() {
        return this.field_177688_c;
    }

    public int func_177684_c() {
        return this.field_177686_d;
    }

    @Nullable
    private BlockPattern.PatternHelper func_177682_a(BlockPos blockposition, EnumFacing enumdirection, EnumFacing enumdirection1, LoadingCache<BlockPos, BlockWorldState> loadingcache) {
        for (int i = 0; i < this.field_177686_d; ++i) {
            for (int j = 0; j < this.field_177688_c; ++j) {
                for (int k = 0; k < this.field_177687_b; ++k) {
                    if (!this.field_177689_a[k][j][i].apply(loadingcache.getUnchecked(func_177683_a(blockposition, enumdirection, enumdirection1, i, j, k)))) {
                        return null;
                    }
                }
            }
        }

        return new BlockPattern.PatternHelper(blockposition, enumdirection, enumdirection1, loadingcache, this.field_177686_d, this.field_177688_c, this.field_177687_b);
    }

    @Nullable
    public BlockPattern.PatternHelper func_177681_a(World world, BlockPos blockposition) {
        LoadingCache loadingcache = func_181627_a(world, false);
        int i = Math.max(Math.max(this.field_177686_d, this.field_177688_c), this.field_177687_b);
        Iterator iterator = BlockPos.func_177980_a(blockposition, blockposition.func_177982_a(i - 1, i - 1, i - 1)).iterator();

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

                    if (enumdirection1 != enumdirection && enumdirection1 != enumdirection.func_176734_d()) {
                        BlockPattern.PatternHelper shapedetector_shapedetectorcollection = this.func_177682_a(blockposition1, enumdirection, enumdirection1, loadingcache);

                        if (shapedetector_shapedetectorcollection != null) {
                            return shapedetector_shapedetectorcollection;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static LoadingCache<BlockPos, BlockWorldState> func_181627_a(World world, boolean flag) {
        return CacheBuilder.newBuilder().build(new BlockPattern.CacheLoader(world, flag));
    }

    protected static BlockPos func_177683_a(BlockPos blockposition, EnumFacing enumdirection, EnumFacing enumdirection1, int i, int j, int k) {
        if (enumdirection != enumdirection1 && enumdirection != enumdirection1.func_176734_d()) {
            Vec3i baseblockposition = new Vec3i(enumdirection.func_82601_c(), enumdirection.func_96559_d(), enumdirection.func_82599_e());
            Vec3i baseblockposition1 = new Vec3i(enumdirection1.func_82601_c(), enumdirection1.func_96559_d(), enumdirection1.func_82599_e());
            Vec3i baseblockposition2 = baseblockposition.func_177955_d(baseblockposition1);

            return blockposition.func_177982_a(baseblockposition1.func_177958_n() * -j + baseblockposition2.func_177958_n() * i + baseblockposition.func_177958_n() * k, baseblockposition1.func_177956_o() * -j + baseblockposition2.func_177956_o() * i + baseblockposition.func_177956_o() * k, baseblockposition1.func_177952_p() * -j + baseblockposition2.func_177952_p() * i + baseblockposition.func_177952_p() * k);
        } else {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
    }

    public static class PatternHelper {

        private final BlockPos field_177674_a;
        private final EnumFacing field_177672_b;
        private final EnumFacing field_177673_c;
        private final LoadingCache<BlockPos, BlockWorldState> field_177671_d;
        private final int field_181120_e;
        private final int field_181121_f;
        private final int field_181122_g;

        public PatternHelper(BlockPos blockposition, EnumFacing enumdirection, EnumFacing enumdirection1, LoadingCache<BlockPos, BlockWorldState> loadingcache, int i, int j, int k) {
            this.field_177674_a = blockposition;
            this.field_177672_b = enumdirection;
            this.field_177673_c = enumdirection1;
            this.field_177671_d = loadingcache;
            this.field_181120_e = i;
            this.field_181121_f = j;
            this.field_181122_g = k;
        }

        public BlockPos func_181117_a() {
            return this.field_177674_a;
        }

        public EnumFacing func_177669_b() {
            return this.field_177672_b;
        }

        public EnumFacing func_177668_c() {
            return this.field_177673_c;
        }

        public int func_181118_d() {
            return this.field_181120_e;
        }

        public int func_181119_e() {
            return this.field_181121_f;
        }

        public BlockWorldState func_177670_a(int i, int j, int k) {
            return this.field_177671_d.getUnchecked(BlockPattern.func_177683_a(this.field_177674_a, this.func_177669_b(), this.func_177668_c(), i, j, k));
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("up", this.field_177673_c).add("forwards", this.field_177672_b).add("frontTopLeft", this.field_177674_a).toString();
        }
    }

    static class CacheLoader extends com.google.common.cache.CacheLoader<BlockPos, BlockWorldState> {

        private final World field_177680_a;
        private final boolean field_181626_b;

        public CacheLoader(World world, boolean flag) {
            this.field_177680_a = world;
            this.field_181626_b = flag;
        }

        @Override
        public BlockWorldState load(BlockPos blockposition) throws Exception {
            return new BlockWorldState(this.field_177680_a, blockposition, this.field_181626_b);
        }
    }
}
