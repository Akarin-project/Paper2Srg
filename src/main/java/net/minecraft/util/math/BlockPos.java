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

    private static final Logger LOGGER = LogManager.getLogger();
    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
    private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = BlockPos.NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - BlockPos.NUM_X_BITS - BlockPos.NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + BlockPos.NUM_Z_BITS;
    private static final int X_SHIFT = BlockPos.Y_SHIFT + BlockPos.NUM_Y_BITS;
    private static final long X_MASK = (1L << BlockPos.NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << BlockPos.NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << BlockPos.NUM_Z_BITS) - 1L;

    public BlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    public BlockPos(double d0, double d1, double d2) {
        super(d0, d1, d2);
    }

    public BlockPos(Entity entity) {
        this(entity.posX, entity.posY, entity.posZ);
    }

    public BlockPos(Vec3d vec3d) {
        this(vec3d.x, vec3d.y, vec3d.z);
    }

    public BlockPos(Vec3i baseblockposition) {
        this(baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ());
    }

    public BlockPos add(double x, double y, double z) { return this.add(x, y, z); } // Paper - OBFHELPER
    public BlockPos add(double d0, double d1, double d2) {
        return d0 == 0.0D && d1 == 0.0D && d2 == 0.0D ? this : new BlockPos((double) this.getX() + d0, (double) this.getY() + d1, (double) this.getZ() + d2);
    }

    public BlockPos add(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new BlockPos(this.getX() + i, this.getY() + j, this.getZ() + k);
    }

    public BlockPos add(Vec3i baseblockposition) {
        return this.add(baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ());
    }

    public BlockPos subtract(Vec3i baseblockposition) {
        return this.add(-baseblockposition.getX(), -baseblockposition.getY(), -baseblockposition.getZ());
    }

    public BlockPos up() {
        return this.up(1);
    }

    public BlockPos up(int i) {
        return this.offset(EnumFacing.UP, i);
    }

    public BlockPos down() {
        return this.down(1);
    }

    public BlockPos down(int i) {
        return this.offset(EnumFacing.DOWN, i);
    }

    public BlockPos north() {
        return this.north(1);
    }

    public BlockPos north(int i) {
        return this.offset(EnumFacing.NORTH, i);
    }

    public BlockPos south() {
        return this.south(1);
    }

    public BlockPos south(int i) {
        return this.offset(EnumFacing.SOUTH, i);
    }

    public BlockPos west() {
        return this.west(1);
    }

    public BlockPos west(int i) {
        return this.offset(EnumFacing.WEST, i);
    }

    public BlockPos east() {
        return this.east(1);
    }

    public BlockPos east(int i) {
        return this.offset(EnumFacing.EAST, i);
    }

    public BlockPos offset(EnumFacing enumdirection) {
        return this.offset(enumdirection, 1);
    }

    public BlockPos offset(EnumFacing enumdirection, int i) {
        return i == 0 ? this : new BlockPos(this.getX() + enumdirection.getFrontOffsetX() * i, this.getY() + enumdirection.getFrontOffsetY() * i, this.getZ() + enumdirection.getFrontOffsetZ() * i);
    }

    public BlockPos rotate(Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case NONE:
        default:
            return this;

        case CLOCKWISE_90:
            return new BlockPos(-this.getZ(), this.getY(), this.getX());

        case CLOCKWISE_180:
            return new BlockPos(-this.getX(), this.getY(), -this.getZ());

        case COUNTERCLOCKWISE_90:
            return new BlockPos(this.getZ(), this.getY(), -this.getX());
        }
    }

    public BlockPos crossProduct(Vec3i baseblockposition) {
        return new BlockPos(this.getY() * baseblockposition.getZ() - this.getZ() * baseblockposition.getY(), this.getZ() * baseblockposition.getX() - this.getX() * baseblockposition.getZ(), this.getX() * baseblockposition.getY() - this.getY() * baseblockposition.getX());
    }

    public long toLong() {
        return ((long) this.getX() & BlockPos.X_MASK) << BlockPos.X_SHIFT | ((long) this.getY() & BlockPos.Y_MASK) << BlockPos.Y_SHIFT | ((long) this.getZ() & BlockPos.Z_MASK) << 0;
    }

    public static BlockPos fromLong(long i) {
        int j = (int) (i << 64 - BlockPos.X_SHIFT - BlockPos.NUM_X_BITS >> 64 - BlockPos.NUM_X_BITS);
        int k = (int) (i << 64 - BlockPos.Y_SHIFT - BlockPos.NUM_Y_BITS >> 64 - BlockPos.NUM_Y_BITS);
        int l = (int) (i << 64 - BlockPos.NUM_Z_BITS >> 64 - BlockPos.NUM_Z_BITS);

        return new BlockPos(j, k, l);
    }

    public static Iterable<BlockPos> getAllInBox(BlockPos blockposition, BlockPos blockposition1) {
        return getAllInBox(Math.min(blockposition.getX(), blockposition1.getX()), Math.min(blockposition.getY(), blockposition1.getY()), Math.min(blockposition.getZ(), blockposition1.getZ()), Math.max(blockposition.getX(), blockposition1.getX()), Math.max(blockposition.getY(), blockposition1.getY()), Math.max(blockposition.getZ(), blockposition1.getZ()));
    }

    public static Iterable<BlockPos> getAllInBox(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        return new Iterable() {
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

                    protected Object computeNext() {
                        return this.a();
                    }
                };
            }
        };
    }

    public BlockPos toImmutable() {
        return this;
    }

    public static Iterable<BlockPos.MutableBlockPos> getAllInBoxMutable(BlockPos blockposition, BlockPos blockposition1) {
        return getAllInBoxMutable(Math.min(blockposition.getX(), blockposition1.getX()), Math.min(blockposition.getY(), blockposition1.getY()), Math.min(blockposition.getZ(), blockposition1.getZ()), Math.max(blockposition.getX(), blockposition1.getX()), Math.max(blockposition.getY(), blockposition1.getY()), Math.max(blockposition.getZ(), blockposition1.getZ()));
    }

    public static Iterable<BlockPos.MutableBlockPos> getAllInBoxMutable(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        return new Iterable() {
            public Iterator<BlockPos.MutableBlockPos> iterator() {
                return new AbstractIterator() {
                    private BlockPos.MutableBlockPos b;

                    protected BlockPos.MutableBlockPos a() {
                        if (this.b == null) {
                            this.b = new BlockPos.MutableBlockPos(i, j, k);
                            return this.b;
                        // Paper start - b, c, d, refer to x, y, z, and as such, a, b, c of BaseBlockPosition
                        } else if (((Vec3i)this.b).x == l && ((Vec3i)this.b).y == i1 && ((Vec3i)this.b).z == j1) {
                            return (BlockPos.MutableBlockPos) this.endOfData();
                        } else {
                            if (((Vec3i) this.b).x < l) {
                                ++((Vec3i) this.b).x;
                            } else if (((Vec3i) this.b).y < i1) {
                                ((Vec3i) this.b).x = i;
                                ++((Vec3i) this.b).y;
                            } else if (((Vec3i) this.b).z < j1) {
                                ((Vec3i) this.b).x = i;
                                ((Vec3i) this.b).y = j;
                                ++((Vec3i) this.b).z;
                            }
                            // Paper end

                            return this.b;
                        }
                    }

                    protected Object computeNext() {
                        return this.a();
                    }
                };
            }
        };
    }

    public Vec3i crossProduct(Vec3i baseblockposition) {
        return this.crossProduct(baseblockposition);
    }

    public static final class PooledMutableBlockPos extends BlockPos.MutableBlockPos {

        private boolean released;
        private static final List<BlockPos.PooledMutableBlockPos> POOL = Lists.newArrayList();

        private PooledMutableBlockPos(int i, int j, int k) {
            super(i, j, k);
        }

        public static BlockPos.PooledMutableBlockPos aquire() { return retain(); } // Paper - OBFHELPER
        public static BlockPos.PooledMutableBlockPos retain() {
            return retain(0, 0, 0);
        }

        public static BlockPos.PooledMutableBlockPos retain(double d0, double d1, double d2) {
            return retain(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
        }

        public static BlockPos.PooledMutableBlockPos retain(int i, int j, int k) {
            List list = BlockPos.PooledMutableBlockPos.POOL;

            synchronized (BlockPos.PooledMutableBlockPos.POOL) {
                if (!BlockPos.PooledMutableBlockPos.POOL.isEmpty()) {
                    BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = (BlockPos.PooledMutableBlockPos) BlockPos.PooledMutableBlockPos.POOL.remove(BlockPos.PooledMutableBlockPos.POOL.size() - 1);

                    if (blockposition_pooledblockposition != null && blockposition_pooledblockposition.released) {
                        blockposition_pooledblockposition.released = false;
                        blockposition_pooledblockposition.setPos(i, j, k);
                        return blockposition_pooledblockposition;
                    }
                }
            }

            return new BlockPos.PooledMutableBlockPos(i, j, k);
        }

        public void free() { release(); } // Paper - OBFHELPER
        public void release() {
            List list = BlockPos.PooledMutableBlockPos.POOL;

            synchronized (BlockPos.PooledMutableBlockPos.POOL) {
                if (BlockPos.PooledMutableBlockPos.POOL.size() < 100) {
                    BlockPos.PooledMutableBlockPos.POOL.add(this);
                }

                this.released = true;
            }
        }

        public BlockPos.PooledMutableBlockPos setPos(int i, int j, int k) {
            if (this.released) {
                BlockPos.LOGGER.error("PooledMutableBlockPosition modified after it was released.", new Throwable());
                this.released = false;
            }

            return (BlockPos.PooledMutableBlockPos) super.setPos(i, j, k);
        }

        public BlockPos.PooledMutableBlockPos setPos(double d0, double d1, double d2) {
            return (BlockPos.PooledMutableBlockPos) super.setPos(d0, d1, d2);
        }

        public BlockPos.PooledMutableBlockPos setPos(Vec3i baseblockposition) {
            return (BlockPos.PooledMutableBlockPos) super.setPos(baseblockposition);
        }

        public BlockPos.PooledMutableBlockPos move(EnumFacing enumdirection) {
            return (BlockPos.PooledMutableBlockPos) super.move(enumdirection);
        }

        public BlockPos.PooledMutableBlockPos move(EnumFacing enumdirection, int i) {
            return (BlockPos.PooledMutableBlockPos) super.move(enumdirection, i);
        }

        public BlockPos.MutableBlockPos move(EnumFacing enumdirection, int i) {
            return this.move(enumdirection, i);
        }

        public BlockPos.MutableBlockPos move(EnumFacing enumdirection) {
            return this.move(enumdirection);
        }

        public BlockPos.MutableBlockPos setPos(Vec3i baseblockposition) {
            return this.setPos(baseblockposition);
        }

        public BlockPos.MutableBlockPos setPos(double d0, double d1, double d2) {
            return this.setPos(d0, d1, d2);
        }

        public BlockPos.MutableBlockPos setPos(int i, int j, int k) {
            return this.setPos(i, j, k);
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
            this(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        }

        public MutableBlockPos(int i, int j, int k) {
            super(0, 0, 0);
            // Paper start - Modify base position variables
            ((Vec3i) this).x = i;
            ((Vec3i) this).y = j;
            ((Vec3i) this).z = k;
            // Paper end
        }

        public BlockPos add(double d0, double d1, double d2) {
            return super.add(d0, d1, d2).toImmutable();
        }

        public BlockPos add(int i, int j, int k) {
            return super.add(i, j, k).toImmutable();
        }

        public BlockPos offset(EnumFacing enumdirection, int i) {
            return super.offset(enumdirection, i).toImmutable();
        }

        public BlockPos rotate(Rotation enumblockrotation) {
            return super.rotate(enumblockrotation).toImmutable();
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

        public void setValues(int x, int y, int z) { setPos(x, y, z); } // Paper - OBFHELPER
        public BlockPos.MutableBlockPos setPos(int i, int j, int k) {
            // Paper start - Modify base position variables
            ((Vec3i) this).x = i;
            ((Vec3i) this).y = j;
            ((Vec3i) this).z = k;
            // Paper end
            return this;
        }

        public BlockPos.MutableBlockPos setPos(double d0, double d1, double d2) {
            return this.setPos(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
        }

        public BlockPos.MutableBlockPos setPos(Vec3i baseblockposition) {
            return this.setPos(baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ());
        }

        public BlockPos.MutableBlockPos move(EnumFacing enumdirection) {
            return this.move(enumdirection, 1);
        }

        public BlockPos.MutableBlockPos move(EnumFacing enumdirection, int i) {
            return this.setPos(this.getX() + enumdirection.getFrontOffsetX() * i, this.getY() + enumdirection.getFrontOffsetY() * i, this.getZ() + enumdirection.getFrontOffsetZ() * i); // Paper - USE THE BLEEPING GETTERS
        }

        public void setY(int i) {
            ((Vec3i) this).y = i; // Paper - Modify base variable
        }

        public BlockPos toImmutable() {
            return new BlockPos(this);
        }

        public Vec3i crossProduct(Vec3i baseblockposition) {
            return super.crossProduct(baseblockposition);
        }
    }
}
