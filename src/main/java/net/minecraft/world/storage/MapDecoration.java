package net.minecraft.world.storage;
import net.minecraft.util.math.MathHelper;


public class MapDecoration {

    private final MapDecoration.Type type;
    private byte x;
    private byte y;
    private byte rotation;

    public MapDecoration(MapDecoration.Type mapicon_type, byte b0, byte b1, byte b2) {
        this.type = mapicon_type;
        this.x = b0;
        this.y = b1;
        this.rotation = b2;
    }

    public byte getImage() {
        return this.type.getIcon();
    }

    public MapDecoration.Type getType() {
        return this.type;
    }

    public byte getX() {
        return this.x;
    }

    public byte getY() {
        return this.y;
    }

    public byte getRotation() {
        return this.rotation;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof MapDecoration)) {
            return false;
        } else {
            MapDecoration mapicon = (MapDecoration) object;

            return this.type != mapicon.type ? false : (this.rotation != mapicon.rotation ? false : (this.x != mapicon.x ? false : this.y == mapicon.y));
        }
    }

    public int hashCode() {
        byte b0 = this.type.getIcon();
        int i = 31 * b0 + this.x;

        i = 31 * i + this.y;
        i = 31 * i + this.rotation;
        return i;
    }

    public static enum Type {

        PLAYER(false), FRAME(true), RED_MARKER(false), BLUE_MARKER(false), TARGET_X(true), TARGET_POINT(true), PLAYER_OFF_MAP(false), PLAYER_OFF_LIMITS(false), MANSION(true, 5393476), MONUMENT(true, 3830373);

        private final byte icon;
        private final boolean renderedOnFrame;
        private final int mapColor;

        private Type(boolean flag) {
            this(flag, -1);
        }

        private Type(boolean flag, int i) {
            this.icon = (byte) this.ordinal();
            this.renderedOnFrame = flag;
            this.mapColor = i;
        }

        public byte getIcon() {
            return this.icon;
        }

        public boolean hasMapColor() {
            return this.mapColor >= 0;
        }

        public int getMapColor() {
            return this.mapColor;
        }

        public static MapDecoration.Type byIcon(byte b0) {
            return values()[MathHelper.clamp(b0, 0, values().length - 1)];
        }
    }
}
