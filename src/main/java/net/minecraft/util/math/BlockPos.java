package net.minecraft.util.math;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

@Immutable
public class BlockPos extends Vec3i {

    private static final Logger field_185335_c = LogManager.getLogger();
    public static final BlockPos field_177992_a = new BlockPos(0, 0, 0);
    private static final int field_177990_b = 1 + MathHelper.func_151239_c(MathHelper.func_151236_b(30000000));
    private static final int field_177991_c = BlockPos.field_177990_b;
    private static final int field_177989_d = 64 - BlockPos.field_177990_b - BlockPos.field_177991_c;
    private static final int field_177987_f = 0 + BlockPos.field_177991_c;
    private static final int field_177988_g = BlockPos.field_177987_f + BlockPos.field_177989_d;
    private static final long field_177994_h = (1L << BlockPos.field_177990_b) - 1L;
    private static final long field_177995_i = (1L << BlockPos.field_177989_d) - 1L;
    private static final long field_177993_j = (1L << BlockPos.field_177991_c) - 1L;

    public BlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    public BlockPos(double d0, double d1, double d2) {
        super(d0, d1, d2);
    }

    public BlockPos(Entity entity) {
        this(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v);
    }

    public BlockPos(Vec3d vec3d) {
        this(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    public BlockPos(Vec3i baseblockposition) {
        this(baseblockposition.func_177958_n(), baseblockposition.func_177956_o(), baseblockposition.func_177952_p());
    }

    public BlockPos add(double x, double y, double z) { return this.func_177963_a(x, y, z); } // Paper - OBFHELPER
    public BlockPos func_177963_a(double d0, double d1, double d2) {
        return d0 == 0.0D && d1 == 0.0D && d2 == 0.0D ? this : new BlockPos(this.func_177958_n() + d0, this.func_177956_o() + d1, this.func_177952_p() + d2);
    }

    public BlockPos func_177982_a(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new BlockPos(this.func_177958_n() + i, this.func_177956_o() + j, this.func_177952_p() + k);
    }

    public BlockPos func_177971_a(Vec3i baseblockposition) {
        return this.func_177982_a(baseblockposition.func_177958_n(), baseblockposition.func_177956_o(), baseblockposition.func_177952_p());
    }

    public BlockPos func_177973_b(Vec3i baseblockposition) {
        return this.func_177982_a(-baseblockposition.func_177958_n(), -baseblockposition.func_177956_o(), -baseblockposition.func_177952_p());
    }

    public BlockPos func_177984_a() {
        return this.func_177981_b(1);
    }

    public BlockPos func_177981_b(int i) {
        return this.func_177967_a(EnumFacing.UP, i);
    }

    public BlockPos func_177977_b() {
        return this.func_177979_c(1);
    }

    public BlockPos func_177979_c(int i) {
        return this.func_177967_a(EnumFacing.DOWN, i);
    }

    public BlockPos func_177978_c() {
        return this.func_177964_d(1);
    }

    public BlockPos func_177964_d(int i) {
        return this.func_177967_a(EnumFacing.NORTH, i);
    }

    public BlockPos func_177968_d() {
        return this.func_177970_e(1);
    }

    public BlockPos func_177970_e(int i) {
        return this.func_177967_a(EnumFacing.SOUTH, i);
    }

    public BlockPos func_177976_e() {
        return this.func_177985_f(1);
    }

    public BlockPos func_177985_f(int i) {
        return this.func_177967_a(EnumFacing.WEST, i);
    }

    public BlockPos func_177974_f() {
        return this.func_177965_g(1);
    }

    public BlockPos func_177965_g(int i) {
        return this.func_177967_a(EnumFacing.EAST, i);
    }

    public BlockPos func_177972_a(EnumFacing enumdirection) {
        return this.func_177967_a(enumdirection, 1);
    }

    public BlockPos func_177967_a(EnumFacing enumdirection, int i) {
        return i == 0 ? this : new BlockPos(this.func_177958_n() + enumdirection.func_82601_c() * i, this.func_177956_o() + enumdirection.func_96559_d() * i, this.func_177952_p() + enumdirection.func_82599_e() * i);
    }

    public BlockPos func_190942_a(Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case NONE:
        default:
            return this;

        case CLOCKWISE_90:
            return new BlockPos(-this.func_177952_p(), this.func_177956_o(), this.func_177958_n());

        case CLOCKWISE_180:
            return new BlockPos(-this.func_177958_n(), this.func_177956_o(), -this.func_177952_p());

        case COUNTERCLOCKWISE_90:
            return new BlockPos(this.func_177952_p(), this.func_177956_o(), -this.func_177958_n());
        }
    }

    @Override
    public BlockPos func_177955_d(Vec3i baseblockposition) {
        return new BlockPos(this.func_177956_o() * baseblockposition.func_177952_p() - this.func_177952_p() * baseblockposition.func_177956_o(), this.func_177952_p() * baseblockposition.func_177958_n() - this.func_177958_n() * baseblockposition.func_177952_p(), this.func_177958_n() * baseblockposition.func_177956_o() - this.func_177956_o() * baseblockposition.func_177958_n());
    }

    public long func_177986_g() {
        return (this.func_177958_n() & BlockPos.field_177994_h) << BlockPos.field_177988_g | (this.func_177956_o() & BlockPos.field_177995_i) << BlockPos.field_177987_f | (this.func_177952_p() & BlockPos.field_177993_j) << 0;
    }

    public static BlockPos func_177969_a(long i) {
        int j = (int) (i << 64 - BlockPos.field_177988_g - BlockPos.field_177990_b >> 64 - BlockPos.field_177990_b);
        int k = (int) (i << 64 - BlockPos.field_177987_f - BlockPos.field_177989_d >> 64 - BlockPos.field_177989_d);
        int l = (int) (i << 64 - BlockPos.field_177991_c >> 64 - BlockPos.field_177991_c);

        return new BlockPos(j, k, l);
    }

    public static Iterable<BlockPos> func_177980_a(BlockPos blockposition, BlockPos blockposition1) {
        return func_191532_a(Math.min(blockposition.func_177958_n(), blockposition1.func_177958_n()), Math.min(blockposition.func_177956_o(), blockposition1.func_177956_o()), Math.min(blockposition.func_177952_p(), blockposition1.func_177952_p()), Math.max(blockposition.func_177958_n(), blockposition1.func_177958_n()), Math.max(blockposition.func_177956_o(), blockposition1.func_177956_o()), Math.max(blockposition.func_177952_p(), blockposition1.func_177952_p()));
    }

    public static Iterable<BlockPos> func_191532_a(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        return new Iterable() {
            @Override
            public Iterator<BlockPos> iterator() {
                return new AbstractIterator() {
                    private boolean b = true;
                    private int c;
                    private int d;
                    private int e;

                    protected BlockPos a() {
                        if (this.b) {
                            this.b = false;
                            this.c = i;
                            this.d = j;
                            this.e = k;
                            return new BlockPos(i, j, k);
                        } else if (this.c == l && this.d == i1 && this.e == j1) {
                            return (BlockPos) this.endOfData();
                        } else {
                            if (this.c < l) {
                                ++this.c;
                            } else if (this.d < i1) {
                                this.c = i;
                                ++this.d;
                            } else if (this.e < j1) {
                                this.c = i;
                                this.d = j;
                                ++this.e;
                            }

                            return new BlockPos(this.c, this.d, this.e);
                        }
                    }

                    @Override
                    protected Object computeNext() {
                        return this.a();
                    }
                };
            }
        };
    }

    public BlockPos func_185334_h() {
        return this;
    }

    public static Iterable<BlockPos.MutableBlockPos> func_177975_b(BlockPos blockposition, BlockPos blockposition1) {
        return func_191531_b(Math.min(blockposition.func_177958_n(), blockposition1.func_177958_n()), Math.min(blockposition.func_177956_o(), blockposition1.func_177956_o()), Math.min(blockposition.func_177952_p(), blockposition1.func_177952_p()), Math.max(blockposition.func_177958_n(), blockposition1.func_177958_n()), Math.max(blockposition.func_177956_o(), blockposition1.func_177956_o()), Math.max(blockposition.func_177952_p(), blockposition1.func_177952_p()));
    }

    public static Iterable<BlockPos.MutableBlockPos> func_191531_b(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        return new Iterable() {
            @Override
            public Iterator<BlockPos.MutableBlockPos> iterator() {
                return new AbstractIterator() {
                    private BlockPos.MutableBlockPos b;

                    protected BlockPos.MutableBlockPos a() {
                        if (this.b == null) {
                            this.b = new BlockPos.MutableBlockPos(i, j, k);
                            return this.b;
                        // Paper start - b, c, d, refer to x, y, z, and as such, a, b, c of BaseBlockPosition
                        } else if (((Vec3i)this.b).field_177962_a == l && ((Vec3i)this.b).field_177960_b == i1 && ((Vec3i)this.b).field_177961_c == j1) {
                            return (BlockPos.MutableBlockPos) this.endOfData();
                        } else {
                            if (((Vec3i) this.b).field_177962_a < l) {
                                ++((Vec3i) this.b).field_177962_a;
                            } else if (((Vec3i) this.b).field_177960_b < i1) {
                                ((Vec3i) this.b).field_177962_a = i;
                                ++((Vec3i) this.b).field_177960_b;
                            } else if (((Vec3i) this.b).field_177961_c < j1) {
                                ((Vec3i) this.b).field_177962_a = i;
                                ((Vec3i) this.b).field_177960_b = j;
                                ++((Vec3i) this.b).field_177961_c;
                            }
                            // Paper end

                            return this.b;
                        }
                    }

                    @Override
                    protected Object computeNext() {
                        return this.a();
                    }
                };
            }
        };
    }

    public static final class PooledMutableBlockPos extends BlockPos.MutableBlockPos {

        private boolean field_185350_f;
        private static final List<BlockPos.PooledMutableBlockPos> field_185351_g = Lists.newArrayList();

        private PooledMutableBlockPos(int i, int j, int k) {
            super(i, j, k);
        }

        public static BlockPos.PooledMutableBlockPos aquire() { return func_185346_s(); } // Paper - OBFHELPER
        public static BlockPos.PooledMutableBlockPos func_185346_s() {
            return func_185339_c(0, 0, 0);
        }

        public static BlockPos.PooledMutableBlockPos func_185345_c(double d0, double d1, double d2) {
            return func_185339_c(MathHelper.func_76128_c(d0), MathHelper.func_76128_c(d1), MathHelper.func_76128_c(d2));
        }

        public static BlockPos.PooledMutableBlockPos func_185339_c(int i, int j, int k) {
            List list = BlockPos.PooledMutableBlockPos.field_185351_g;

            synchronized (BlockPos.PooledMutableBlockPos.field_185351_g) {
                if (!BlockPos.PooledMutableBlockPos.field_185351_g.isEmpty()) {
                    BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.field_185351_g.remove(BlockPos.PooledMutableBlockPos.field_185351_g.size() - 1);

                    if (blockposition_pooledblockposition != null && blockposition_pooledblockposition.field_185350_f) {
                        blockposition_pooledblockposition.field_185350_f = false;
                        blockposition_pooledblockposition.func_181079_c(i, j, k);
                        return blockposition_pooledblockposition;
                    }
                }
            }

            return new BlockPos.PooledMutableBlockPos(i, j, k);
        }

        public void free() { func_185344_t(); } // Paper - OBFHELPER
        public void func_185344_t() {
            List list = BlockPos.PooledMutableBlockPos.field_185351_g;

            synchronized (BlockPos.PooledMutableBlockPos.field_185351_g) {
                if (BlockPos.PooledMutableBlockPos.field_185351_g.size() < 100) {
                    BlockPos.PooledMutableBlockPos.field_185351_g.add(this);
                }

                this.field_185350_f = true;
            }
        }

        @Override
        public BlockPos.PooledMutableBlockPos func_181079_c(int i, int j, int k) {
            if (this.field_185350_f) {
                BlockPos.field_185335_c.error("PooledMutableBlockPosition modified after it was released.", new Throwable());
                this.field_185350_f = false;
            }

            return (BlockPos.PooledMutableBlockPos) super.func_181079_c(i, j, k);
        }

        @Override
        public BlockPos.PooledMutableBlockPos func_189532_c(double d0, double d1, double d2) {
            return (BlockPos.PooledMutableBlockPos) super.func_189532_c(d0, d1, d2);
        }

        @Override
        public BlockPos.PooledMutableBlockPos func_189533_g(Vec3i baseblockposition) {
            return (BlockPos.PooledMutableBlockPos) super.func_189533_g(baseblockposition);
        }

        @Override
        public BlockPos.PooledMutableBlockPos func_189536_c(EnumFacing enumdirection) {
            return (BlockPos.PooledMutableBlockPos) super.func_189536_c(enumdirection);
        }

        @Override
        public BlockPos.PooledMutableBlockPos func_189534_c(EnumFacing enumdirection, int i) {
            return (BlockPos.PooledMutableBlockPos) super.func_189534_c(enumdirection, i);
        }
    }

    public static class MutableBlockPos extends BlockPos {

        // Paper start - Remove variables
        /*
        protected int b;
        protected int c;
        protected int d;
        // Paper start
        @Override
        public boolean isValidLocation() {
            return b >= -30000000 && d >= -30000000 && b < 30000000 && d < 30000000 && c >= 0 && c < 256;
        }
        @Override
        public boolean isInvalidYLocation() {
            return c < 0 || c >= 256;
        }
        */
        // Paper end

        public MutableBlockPos() {
            this(0, 0, 0);
        }

        public MutableBlockPos(BlockPos blockposition) {
            this(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
        }

        public MutableBlockPos(int i, int j, int k) {
            super(0, 0, 0);
            // Paper start - Modify base position variables
            ((Vec3i) this).field_177962_a = i;
            ((Vec3i) this).field_177960_b = j;
            ((Vec3i) this).field_177961_c = k;
            // Paper end
        }

        @Override
        public BlockPos func_177963_a(double d0, double d1, double d2) {
            return super.func_177963_a(d0, d1, d2).func_185334_h();
        }

        @Override
        public BlockPos func_177982_a(int i, int j, int k) {
            return super.func_177982_a(i, j, k).func_185334_h();
        }

        @Override
        public BlockPos func_177967_a(EnumFacing enumdirection, int i) {
            return super.func_177967_a(enumdirection, i).func_185334_h();
        }

        @Override
        public BlockPos func_190942_a(Rotation enumblockrotation) {
            return super.func_190942_a(enumblockrotation).func_185334_h();
        }

        // Paper start - Use superclass methods
        /*
        public int getX() {
            return this.b;
        }

        public int getY() {
            return this.c;
        }

        public int getZ() {
            return this.d;
        }
        */
        // Paper end

        public void setValues(int x, int y, int z) { func_181079_c(x, y, z); } // Paper - OBFHELPER
        public BlockPos.MutableBlockPos func_181079_c(int i, int j, int k) {
            // Paper start - Modify base position variables
            ((Vec3i) this).field_177962_a = i;
            ((Vec3i) this).field_177960_b = j;
            ((Vec3i) this).field_177961_c = k;
            // Paper end
            return this;
        }

        public BlockPos.MutableBlockPos func_189532_c(double d0, double d1, double d2) {
            return this.func_181079_c(MathHelper.func_76128_c(d0), MathHelper.func_76128_c(d1), MathHelper.func_76128_c(d2));
        }

        public BlockPos.MutableBlockPos func_189533_g(Vec3i baseblockposition) {
            return this.func_181079_c(baseblockposition.func_177958_n(), baseblockposition.func_177956_o(), baseblockposition.func_177952_p());
        }

        public BlockPos.MutableBlockPos func_189536_c(EnumFacing enumdirection) {
            return this.func_189534_c(enumdirection, 1);
        }

        public BlockPos.MutableBlockPos func_189534_c(EnumFacing enumdirection, int i) {
            return this.func_181079_c(this.func_177958_n() + enumdirection.func_82601_c() * i, this.func_177956_o() + enumdirection.func_96559_d() * i, this.func_177952_p() + enumdirection.func_82599_e() * i); // Paper - USE THE BLEEPING GETTERS
        }

        public void func_185336_p(int i) {
            ((Vec3i) this).field_177960_b = i; // Paper - Modify base variable
        }

        @Override
        public BlockPos func_185334_h() {
            return new BlockPos(this);
        }

        @Override
        public BlockPos func_177955_d(Vec3i baseblockposition) {
            return super.func_177955_d(baseblockposition);
        }
    }
}
