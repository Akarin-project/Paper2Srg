package net.minecraft.world.storage;
import net.minecraft.util.math.MathHelper;


public class MapDecoration {

    private final MapDecoration.Type field_191181_a;
    private byte field_176115_b;
    private byte field_176116_c;
    private byte field_176114_d;

    public MapDecoration(MapDecoration.Type mapicon_type, byte b0, byte b1, byte b2) {
        this.field_191181_a = mapicon_type;
        this.field_176115_b = b0;
        this.field_176116_c = b1;
        this.field_176114_d = b2;
    }

    public byte func_176110_a() {
        return this.field_191181_a.func_191163_a();
    }

    public MapDecoration.Type func_191179_b() {
        return this.field_191181_a;
    }

    public byte func_176112_b() {
        return this.field_176115_b;
    }

    public byte func_176113_c() {
        return this.field_176116_c;
    }

    public byte func_176111_d() {
        return this.field_176114_d;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof MapDecoration)) {
            return false;
        } else {
            MapDecoration mapicon = (MapDecoration) object;

            return this.field_191181_a != mapicon.field_191181_a ? false : (this.field_176114_d != mapicon.field_176114_d ? false : (this.field_176115_b != mapicon.field_176115_b ? false : this.field_176116_c == mapicon.field_176116_c));
        }
    }

    public int hashCode() {
        byte b0 = this.field_191181_a.func_191163_a();
        int i = 31 * b0 + this.field_176115_b;

        i = 31 * i + this.field_176116_c;
        i = 31 * i + this.field_176114_d;
        return i;
    }

    public static enum Type {

        PLAYER(false), FRAME(true), RED_MARKER(false), BLUE_MARKER(false), TARGET_X(true), TARGET_POINT(true), PLAYER_OFF_MAP(false), PLAYER_OFF_LIMITS(false), MANSION(true, 5393476), MONUMENT(true, 3830373);

        private final byte field_191175_k;
        private final boolean field_191176_l;
        private final int field_191177_m;

        private Type(boolean flag) {
            this(flag, -1);
        }

        private Type(boolean flag, int i) {
            this.field_191175_k = (byte) this.ordinal();
            this.field_191176_l = flag;
            this.field_191177_m = i;
        }

        public byte func_191163_a() {
            return this.field_191175_k;
        }

        public boolean func_191162_c() {
            return this.field_191177_m >= 0;
        }

        public int func_191161_d() {
            return this.field_191177_m;
        }

        public static MapDecoration.Type func_191159_a(byte b0) {
            return values()[MathHelper.func_76125_a(b0, 0, values().length - 1)];
        }
    }
}
