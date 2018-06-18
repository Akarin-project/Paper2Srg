package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public enum EnumFacing implements IStringSerializable {

    DOWN(0, 1, -1, "down", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Y, new Vec3i(0, -1, 0)), UP(1, 0, -1, "up", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Y, new Vec3i(0, 1, 0)), NORTH(2, 3, 2, "north", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, -1)), SOUTH(3, 2, 0, "south", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, 1)), WEST(4, 5, 1, "west", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X, new Vec3i(-1, 0, 0)), EAST(5, 4, 3, "east", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X, new Vec3i(1, 0, 0));

    private final int index;
    private final int opposite;
    private final int horizontalIndex;
    private final String name;
    private final EnumFacing.Axis axis;
    private final EnumFacing.AxisDirection axisDirection;
    private final Vec3i directionVec;
    private static final EnumFacing[] VALUES = new EnumFacing[6];
    private static final EnumFacing[] HORIZONTALS = new EnumFacing[4];
    private static final Map<String, EnumFacing> NAME_LOOKUP = Maps.newHashMap();

    private EnumFacing(int i, int j, int k, String s, EnumFacing.AxisDirection enumdirection_enumaxisdirection, EnumFacing.Axis enumdirection_enumaxis, Vec3i baseblockposition) {
        this.index = i;
        this.horizontalIndex = k;
        this.opposite = j;
        this.name = s;
        this.axis = enumdirection_enumaxis;
        this.axisDirection = enumdirection_enumaxisdirection;
        this.directionVec = baseblockposition;
    }

    public int getIndex() {
        return this.index;
    }

    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }

    public EnumFacing.AxisDirection getAxisDirection() {
        return this.axisDirection;
    }

    public EnumFacing getOpposite() {
        return getFront(this.opposite);
    }

    public EnumFacing rotateY() {
        switch (this) {
        case NORTH:
            return EnumFacing.EAST;

        case EAST:
            return EnumFacing.SOUTH;

        case SOUTH:
            return EnumFacing.WEST;

        case WEST:
            return EnumFacing.NORTH;

        default:
            throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        }
    }

    public EnumFacing rotateYCCW() {
        switch (this) {
        case NORTH:
            return EnumFacing.WEST;

        case EAST:
            return EnumFacing.NORTH;

        case SOUTH:
            return EnumFacing.EAST;

        case WEST:
            return EnumFacing.SOUTH;

        default:
            throw new IllegalStateException("Unable to get CCW facing of " + this);
        }
    }

    public int getFrontOffsetX() {
        return this.axis == EnumFacing.Axis.X ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetY() {
        return this.axis == EnumFacing.Axis.Y ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetZ() {
        return this.axis == EnumFacing.Axis.Z ? this.axisDirection.getOffset() : 0;
    }

    public String getName2() {
        return this.name;
    }

    public EnumFacing.Axis getAxis() {
        return this.axis;
    }

    public static EnumFacing getFront(int i) {
        return EnumFacing.VALUES[MathHelper.abs(i % EnumFacing.VALUES.length)];
    }

    public static EnumFacing getHorizontal(int i) {
        return EnumFacing.HORIZONTALS[MathHelper.abs(i % EnumFacing.HORIZONTALS.length)];
    }

    public static EnumFacing fromAngle(double d0) {
        return getHorizontal(MathHelper.floor(d0 / 90.0D + 0.5D) & 3);
    }

    public float getHorizontalAngle() {
        return (float) ((this.horizontalIndex & 3) * 90);
    }

    public static EnumFacing random(Random random) {
        return values()[random.nextInt(values().length)];
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public static EnumFacing getFacingFromAxis(EnumFacing.AxisDirection enumdirection_enumaxisdirection, EnumFacing.Axis enumdirection_enumaxis) {
        EnumFacing[] aenumdirection = values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (enumdirection.getAxisDirection() == enumdirection_enumaxisdirection && enumdirection.getAxis() == enumdirection_enumaxis) {
                return enumdirection;
            }
        }

        throw new IllegalArgumentException("No such direction: " + enumdirection_enumaxisdirection + " " + enumdirection_enumaxis);
    }

    public static EnumFacing getDirectionFromEntityLiving(BlockPos blockposition, EntityLivingBase entityliving) {
        if (Math.abs(entityliving.posX - (double) ((float) blockposition.getX() + 0.5F)) < 2.0D && Math.abs(entityliving.posZ - (double) ((float) blockposition.getZ() + 0.5F)) < 2.0D) {
            double d0 = entityliving.posY + (double) entityliving.getEyeHeight();

            if (d0 - (double) blockposition.getY() > 2.0D) {
                return EnumFacing.UP;
            }

            if ((double) blockposition.getY() - d0 > 0.0D) {
                return EnumFacing.DOWN;
            }
        }

        return entityliving.getHorizontalFacing().getOpposite();
    }

    static {
        EnumFacing[] aenumdirection = values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            EnumFacing.VALUES[enumdirection.index] = enumdirection;
            if (enumdirection.getAxis().isHorizontal()) {
                EnumFacing.HORIZONTALS[enumdirection.horizontalIndex] = enumdirection;
            }

            EnumFacing.NAME_LOOKUP.put(enumdirection.getName2().toLowerCase(Locale.ROOT), enumdirection);
        }

    }

    public static enum Plane implements Predicate<EnumFacing>, Iterable<EnumFacing> {

        HORIZONTAL, VERTICAL;

        private Plane() {}

        public EnumFacing[] facings() {
            switch (this) {
            case HORIZONTAL:
                return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};

            case VERTICAL:
                return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN};

            default:
                throw new Error("Someone\'s been tampering with the universe!");
            }
        }

        public EnumFacing random(Random random) {
            EnumFacing[] aenumdirection = this.facings();

            return aenumdirection[random.nextInt(aenumdirection.length)];
        }

        public boolean apply(@Nullable EnumFacing enumdirection) {
            return enumdirection != null && enumdirection.getAxis().getPlane() == this;
        }

        public Iterator<EnumFacing> iterator() {
            return Iterators.forArray(this.facings());
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((EnumFacing) object);
        }
    }

    public static enum AxisDirection {

        POSITIVE(1, "Towards positive"), NEGATIVE(-1, "Towards negative");

        private final int offset;
        private final String description;

        private AxisDirection(int i, String s) {
            this.offset = i;
            this.description = s;
        }

        public int getOffset() {
            return this.offset;
        }

        public String toString() {
            return this.description;
        }
    }

    public static enum Axis implements Predicate<EnumFacing>, IStringSerializable {

        X("x", EnumFacing.Plane.HORIZONTAL), Y("y", EnumFacing.Plane.VERTICAL), Z("z", EnumFacing.Plane.HORIZONTAL);

        private static final Map<String, EnumFacing.Axis> NAME_LOOKUP = Maps.newHashMap();
        private final String name;
        private final EnumFacing.Plane plane;

        private Axis(String s, EnumFacing.Plane enumdirection_enumdirectionlimit) {
            this.name = s;
            this.plane = enumdirection_enumdirectionlimit;
        }

        public String getName2() {
            return this.name;
        }

        public boolean isVertical() {
            return this.plane == EnumFacing.Plane.VERTICAL;
        }

        public boolean isHorizontal() {
            return this.plane == EnumFacing.Plane.HORIZONTAL;
        }

        public String toString() {
            return this.name;
        }

        public boolean apply(@Nullable EnumFacing enumdirection) {
            return enumdirection != null && enumdirection.getAxis() == this;
        }

        public EnumFacing.Plane getPlane() {
            return this.plane;
        }

        public String getName() {
            return this.name;
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((EnumFacing) object);
        }

        static {
            EnumFacing.Axis[] aenumdirection_enumaxis = values();
            int i = aenumdirection_enumaxis.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing.Axis enumdirection_enumaxis = aenumdirection_enumaxis[j];

                EnumFacing.Axis.NAME_LOOKUP.put(enumdirection_enumaxis.getName2().toLowerCase(Locale.ROOT), enumdirection_enumaxis);
            }

        }
    }
}
