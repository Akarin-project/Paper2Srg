package net.minecraft.util;

public enum Mirror {

    NONE("no_mirror"), LEFT_RIGHT("mirror_left_right"), FRONT_BACK("mirror_front_back");

    private final String field_185807_d;
    private static final String[] field_185808_e = new String[values().length];

    private Mirror(String s) {
        this.field_185807_d = s;
    }

    public int func_185802_a(int i, int j) {
        int k = j / 2;
        int l = i > k ? i - j : i;

        switch (this) {
        case FRONT_BACK:
            return (j - l) % j;

        case LEFT_RIGHT:
            return (k - l + j) % j;

        default:
            return i;
        }
    }

    public Rotation func_185800_a(EnumFacing enumdirection) {
        EnumFacing.Axis enumdirection_enumaxis = enumdirection.func_176740_k();

        return (this != Mirror.LEFT_RIGHT || enumdirection_enumaxis != EnumFacing.Axis.Z) && (this != Mirror.FRONT_BACK || enumdirection_enumaxis != EnumFacing.Axis.X) ? Rotation.NONE : Rotation.CLOCKWISE_180;
    }

    public EnumFacing func_185803_b(EnumFacing enumdirection) {
        switch (this) {
        case FRONT_BACK:
            if (enumdirection == EnumFacing.WEST) {
                return EnumFacing.EAST;
            } else {
                if (enumdirection == EnumFacing.EAST) {
                    return EnumFacing.WEST;
                }

                return enumdirection;
            }

        case LEFT_RIGHT:
            if (enumdirection == EnumFacing.NORTH) {
                return EnumFacing.SOUTH;
            } else {
                if (enumdirection == EnumFacing.SOUTH) {
                    return EnumFacing.NORTH;
                }

                return enumdirection;
            }

        default:
            return enumdirection;
        }
    }

    static {
        int i = 0;
        Mirror[] aenumblockmirror = values();
        int j = aenumblockmirror.length;

        for (int k = 0; k < j; ++k) {
            Mirror enumblockmirror = aenumblockmirror[k];

            Mirror.field_185808_e[i++] = enumblockmirror.field_185807_d;
        }

    }
}
