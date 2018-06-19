package net.minecraft.util;

public enum Rotation {

    NONE("rotate_0"), CLOCKWISE_90("rotate_90"), CLOCKWISE_180("rotate_180"), COUNTERCLOCKWISE_90("rotate_270");

    private final String field_185838_e;
    private static final String[] field_185839_f = new String[values().length];

    private Rotation(String s) {
        this.field_185838_e = s;
    }

    public Rotation func_185830_a(Rotation enumblockrotation) {
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

    public EnumFacing func_185831_a(EnumFacing enumdirection) {
        if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
            return enumdirection;
        } else {
            switch (this) {
            case CLOCKWISE_90:
                return enumdirection.func_176746_e();

            case CLOCKWISE_180:
                return enumdirection.func_176734_d();

            case COUNTERCLOCKWISE_90:
                return enumdirection.func_176735_f();

            default:
                return enumdirection;
            }
        }
    }

    public int func_185833_a(int i, int j) {
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

            Rotation.field_185839_f[i++] = enumblockrotation.field_185838_e;
        }

    }
}
