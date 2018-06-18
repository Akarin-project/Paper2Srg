package net.minecraft.util;

public enum Mirror {

    NONE("no_mirror"), LEFT_RIGHT("mirror_left_right"), FRONT_BACK("mirror_front_back");

    private final String name;
    private static final String[] mirrorNames = new String[values().length];

    private Mirror(String s) {
        this.name = s;
    }

    public int mirrorRotation(int i, int j) {
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

    public Rotation toRotation(EnumFacing enumdirection) {
        EnumFacing.Axis enumdirection_enumaxis = enumdirection.getAxis();

        return (this != Mirror.LEFT_RIGHT || enumdirection_enumaxis != EnumFacing.Axis.Z) && (this != Mirror.FRONT_BACK || enumdirection_enumaxis != EnumFacing.Axis.X) ? Rotation.NONE : Rotation.CLOCKWISE_180;
    }

    public EnumFacing mirror(EnumFacing enumdirection) {
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

            Mirror.mirrorNames[i++] = enumblockmirror.name;
        }

    }
}
