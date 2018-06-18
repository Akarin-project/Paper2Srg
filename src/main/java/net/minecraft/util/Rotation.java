package net.minecraft.util;

public enum Rotation {

    NONE("rotate_0"), CLOCKWISE_90("rotate_90"), CLOCKWISE_180("rotate_180"), COUNTERCLOCKWISE_90("rotate_270");

    private final String name;
    private static final String[] rotationNames = new String[values().length];

    private Rotation(String s) {
        this.name = s;
    }

    public Rotation add(Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch (this) {
            case NONE:
                return Rotation.CLOCKWISE_180;

            case CLOCKWISE_90:
                return Rotation.COUNTERCLOCKWISE_90;

            case CLOCKWISE_180:
                return Rotation.NONE;

            case COUNTERCLOCKWISE_90:
                return Rotation.CLOCKWISE_90;
            }

        case COUNTERCLOCKWISE_90:
            switch (this) {
            case NONE:
                return Rotation.COUNTERCLOCKWISE_90;

            case CLOCKWISE_90:
                return Rotation.NONE;

            case CLOCKWISE_180:
                return Rotation.CLOCKWISE_90;

            case COUNTERCLOCKWISE_90:
                return Rotation.CLOCKWISE_180;
            }

        case CLOCKWISE_90:
            switch (this) {
            case NONE:
                return Rotation.CLOCKWISE_90;

            case CLOCKWISE_90:
                return Rotation.CLOCKWISE_180;

            case CLOCKWISE_180:
                return Rotation.COUNTERCLOCKWISE_90;

            case COUNTERCLOCKWISE_90:
                return Rotation.NONE;
            }

        default:
            return this;
        }
    }

    public EnumFacing rotate(EnumFacing enumdirection) {
        if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
            return enumdirection;
        } else {
            switch (this) {
            case CLOCKWISE_90:
                return enumdirection.rotateY();

            case CLOCKWISE_180:
                return enumdirection.getOpposite();

            case COUNTERCLOCKWISE_90:
                return enumdirection.rotateYCCW();

            default:
                return enumdirection;
            }
        }
    }

    public int rotate(int i, int j) {
        switch (this) {
        case CLOCKWISE_90:
            return (i + j / 4) % j;

        case CLOCKWISE_180:
            return (i + j / 2) % j;

        case COUNTERCLOCKWISE_90:
            return (i + j * 3 / 4) % j;

        default:
            return i;
        }
    }

    static {
        int i = 0;
        Rotation[] aenumblockrotation = values();
        int j = aenumblockrotation.length;

        for (int k = 0; k < j; ++k) {
            Rotation enumblockrotation = aenumblockrotation[k];

            Rotation.rotationNames[i++] = enumblockrotation.name;
        }

    }
}
