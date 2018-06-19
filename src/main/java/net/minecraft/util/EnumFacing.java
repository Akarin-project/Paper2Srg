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

    private final int field_176748_g;
    private final int field_176759_h;
    private final int field_176760_i;
    private final String field_176757_j;
    private final EnumFacing.Axis field_176758_k;
    private final EnumFacing.AxisDirection field_176755_l;
    private final Vec3i field_176756_m;
    private static final EnumFacing[] field_82609_l = new EnumFacing[6];
    private static final EnumFacing[] field_176754_o = new EnumFacing[4];
    private static final Map<String, EnumFacing> field_176761_p = Maps.newHashMap();

    private EnumFacing(int i, int j, int k, String s, EnumFacing.AxisDirection enumdirection_enumaxisdirection, EnumFacing.Axis enumdirection_enumaxis, Vec3i baseblockposition) {
        this.field_176748_g = i;
        this.field_176760_i = k;
        this.field_176759_h = j;
        this.field_176757_j = s;
        this.field_176758_k = enumdirection_enumaxis;
        this.field_176755_l = enumdirection_enumaxisdirection;
        this.field_176756_m = baseblockposition;
    }

    public int func_176745_a() {
        return this.field_176748_g;
    }

    public int func_176736_b() {
        return this.field_176760_i;
    }

    public EnumFacing.AxisDirection func_176743_c() {
        return this.field_176755_l;
    }

    public EnumFacing func_176734_d() {
        return func_82600_a(this.field_176759_h);
    }

    public EnumFacing func_176746_e() {
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

    public EnumFacing func_176735_f() {
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

    public int func_82601_c() {
        return this.field_176758_k == EnumFacing.Axis.X ? this.field_176755_l.func_179524_a() : 0;
    }

    public int func_96559_d() {
        return this.field_176758_k == EnumFacing.Axis.Y ? this.field_176755_l.func_179524_a() : 0;
    }

    public int func_82599_e() {
        return this.field_176758_k == EnumFacing.Axis.Z ? this.field_176755_l.func_179524_a() : 0;
    }

    public String func_176742_j() {
        return this.field_176757_j;
    }

    public EnumFacing.Axis func_176740_k() {
        return this.field_176758_k;
    }

    public static EnumFacing func_82600_a(int i) {
        return EnumFacing.field_82609_l[MathHelper.func_76130_a(i % EnumFacing.field_82609_l.length)];
    }

    public static EnumFacing func_176731_b(int i) {
        return EnumFacing.field_176754_o[MathHelper.func_76130_a(i % EnumFacing.field_176754_o.length)];
    }

    public static EnumFacing func_176733_a(double d0) {
        return func_176731_b(MathHelper.func_76128_c(d0 / 90.0D + 0.5D) & 3);
    }

    public float func_185119_l() {
        return (float) ((this.field_176760_i & 3) * 90);
    }

    public static EnumFacing func_176741_a(Random random) {
        return values()[random.nextInt(values().length)];
    }

    public String toString() {
        return this.field_176757_j;
    }

    public String func_176610_l() {
        return this.field_176757_j;
    }

    public static EnumFacing func_181076_a(EnumFacing.AxisDirection enumdirection_enumaxisdirection, EnumFacing.Axis enumdirection_enumaxis) {
        EnumFacing[] aenumdirection = values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (enumdirection.func_176743_c() == enumdirection_enumaxisdirection && enumdirection.func_176740_k() == enumdirection_enumaxis) {
                return enumdirection;
            }
        }

        throw new IllegalArgumentException("No such direction: " + enumdirection_enumaxisdirection + " " + enumdirection_enumaxis);
    }

    public static EnumFacing func_190914_a(BlockPos blockposition, EntityLivingBase entityliving) {
        if (Math.abs(entityliving.field_70165_t - (double) ((float) blockposition.func_177958_n() + 0.5F)) < 2.0D && Math.abs(entityliving.field_70161_v - (double) ((float) blockposition.func_177952_p() + 0.5F)) < 2.0D) {
            double d0 = entityliving.field_70163_u + (double) entityliving.func_70047_e();

            if (d0 - (double) blockposition.func_177956_o() > 2.0D) {
                return EnumFacing.UP;
            }

            if ((double) blockposition.func_177956_o() - d0 > 0.0D) {
                return EnumFacing.DOWN;
            }
        }

        return entityliving.func_174811_aO().func_176734_d();
    }

    static {
        EnumFacing[] aenumdirection = values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            EnumFacing.field_82609_l[enumdirection.field_176748_g] = enumdirection;
            if (enumdirection.func_176740_k().func_176722_c()) {
                EnumFacing.field_176754_o[enumdirection.field_176760_i] = enumdirection;
            }

            EnumFacing.field_176761_p.put(enumdirection.func_176742_j().toLowerCase(Locale.ROOT), enumdirection);
        }

    }

    public static enum Plane implements Predicate<EnumFacing>, Iterable<EnumFacing> {

        HORIZONTAL, VERTICAL;

        private Plane() {}

        public EnumFacing[] func_179516_a() {
            switch (this) {
            case HORIZONTAL:
                return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};

            case VERTICAL:
                return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN};

            default:
                throw new Error("Someone\'s been tampering with the universe!");
            }
        }

        public EnumFacing func_179518_a(Random random) {
            EnumFacing[] aenumdirection = this.func_179516_a();

            return aenumdirection[random.nextInt(aenumdirection.length)];
        }

        public boolean apply(@Nullable EnumFacing enumdirection) {
            return enumdirection != null && enumdirection.func_176740_k().func_176716_d() == this;
        }

        public Iterator<EnumFacing> iterator() {
            return Iterators.forArray(this.func_179516_a());
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((EnumFacing) object);
        }
    }

    public static enum AxisDirection {

        POSITIVE(1, "Towards positive"), NEGATIVE(-1, "Towards negative");

        private final int field_179528_c;
        private final String field_179525_d;

        private AxisDirection(int i, String s) {
            this.field_179528_c = i;
            this.field_179525_d = s;
        }

        public int func_179524_a() {
            return this.field_179528_c;
        }

        public String toString() {
            return this.field_179525_d;
        }
    }

    public static enum Axis implements Predicate<EnumFacing>, IStringSerializable {

        X("x", EnumFacing.Plane.HORIZONTAL), Y("y", EnumFacing.Plane.VERTICAL), Z("z", EnumFacing.Plane.HORIZONTAL);

        private static final Map<String, EnumFacing.Axis> field_176725_d = Maps.newHashMap();
        private final String field_176726_e;
        private final EnumFacing.Plane field_176723_f;

        private Axis(String s, EnumFacing.Plane enumdirection_enumdirectionlimit) {
            this.field_176726_e = s;
            this.field_176723_f = enumdirection_enumdirectionlimit;
        }

        public String func_176719_a() {
            return this.field_176726_e;
        }

        public boolean func_176720_b() {
            return this.field_176723_f == EnumFacing.Plane.VERTICAL;
        }

        public boolean func_176722_c() {
            return this.field_176723_f == EnumFacing.Plane.HORIZONTAL;
        }

        public String toString() {
            return this.field_176726_e;
        }

        public boolean apply(@Nullable EnumFacing enumdirection) {
            return enumdirection != null && enumdirection.func_176740_k() == this;
        }

        public EnumFacing.Plane func_176716_d() {
            return this.field_176723_f;
        }

        public String func_176610_l() {
            return this.field_176726_e;
        }

        public boolean apply(@Nullable Object object) {
            return this.apply((EnumFacing) object);
        }

        static {
            EnumFacing.Axis[] aenumdirection_enumaxis = values();
            int i = aenumdirection_enumaxis.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing.Axis enumdirection_enumaxis = aenumdirection_enumaxis[j];

                EnumFacing.Axis.field_176725_d.put(enumdirection_enumaxis.func_176719_a().toLowerCase(Locale.ROOT), enumdirection_enumaxis);
            }

        }
    }
}
