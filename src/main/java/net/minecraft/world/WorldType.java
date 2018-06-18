package net.minecraft.world;

public class WorldType {

    public static final WorldType[] WORLD_TYPES = new WorldType[16];
    public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).setVersioned();
    public static final WorldType FLAT = new WorldType(1, "flat");
    public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
    public static final WorldType AMPLIFIED = (new WorldType(3, "amplified")).enableInfoNotice();
    public static final WorldType CUSTOMIZED = new WorldType(4, "customized");
    public static final WorldType DEBUG_ALL_BLOCK_STATES = new WorldType(5, "debug_all_block_states");
    public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);
    private final int id;
    private final String name;
    private final int version;
    private boolean canBeCreated;
    private boolean versioned;
    private boolean hasInfoNotice;

    private WorldType(int i, String s) {
        this(i, s, 0);
    }

    private WorldType(int i, String s, int j) {
        this.name = s;
        this.version = j;
        this.canBeCreated = true;
        this.id = i;
        WorldType.WORLD_TYPES[i] = this;
    }

    public String getName() {
        return this.name;
    }

    public int getVersion() {
        return this.version;
    }

    public WorldType getWorldTypeForGeneratorVersion(int i) {
        return this == WorldType.DEFAULT && i == 0 ? WorldType.DEFAULT_1_1 : this;
    }

    private WorldType setCanBeCreated(boolean flag) {
        this.canBeCreated = flag;
        return this;
    }

    private WorldType setVersioned() {
        this.versioned = true;
        return this;
    }

    public boolean isVersioned() {
        return this.versioned;
    }

    public static WorldType parseWorldType(String s) {
        WorldType[] aworldtype = WorldType.WORLD_TYPES;
        int i = aworldtype.length;

        for (int j = 0; j < i; ++j) {
            WorldType worldtype = aworldtype[j];

            if (worldtype != null && worldtype.name.equalsIgnoreCase(s)) {
                return worldtype;
            }
        }

        return null;
    }

    public int getId() {
        return this.id;
    }

    private WorldType enableInfoNotice() {
        this.hasInfoNotice = true;
        return this;
    }
}
