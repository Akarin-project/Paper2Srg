package net.minecraft.world;

public class WorldType {

    public static final WorldType[] field_77139_a = new WorldType[16];
    public static final WorldType field_77137_b = (new WorldType(0, "default", 1)).func_77129_f();
    public static final WorldType field_77138_c = new WorldType(1, "flat");
    public static final WorldType field_77135_d = new WorldType(2, "largeBiomes");
    public static final WorldType field_151360_e = (new WorldType(3, "amplified")).func_151358_j();
    public static final WorldType field_180271_f = new WorldType(4, "customized");
    public static final WorldType field_180272_g = new WorldType(5, "debug_all_block_states");
    public static final WorldType field_77136_e = (new WorldType(8, "default_1_1", 0)).func_77124_a(false);
    private final int field_82748_f;
    private final String field_77133_f;
    private final int field_77134_g;
    private boolean field_77140_h;
    private boolean field_77141_i;
    private boolean field_151361_l;

    private WorldType(int i, String s) {
        this(i, s, 0);
    }

    private WorldType(int i, String s, int j) {
        this.field_77133_f = s;
        this.field_77134_g = j;
        this.field_77140_h = true;
        this.field_82748_f = i;
        WorldType.field_77139_a[i] = this;
    }

    public String func_77127_a() {
        return this.field_77133_f;
    }

    public int func_77131_c() {
        return this.field_77134_g;
    }

    public WorldType func_77132_a(int i) {
        return this == WorldType.field_77137_b && i == 0 ? WorldType.field_77136_e : this;
    }

    private WorldType func_77124_a(boolean flag) {
        this.field_77140_h = flag;
        return this;
    }

    private WorldType func_77129_f() {
        this.field_77141_i = true;
        return this;
    }

    public boolean func_77125_e() {
        return this.field_77141_i;
    }

    public static WorldType func_77130_a(String s) {
        WorldType[] aworldtype = WorldType.field_77139_a;
        int i = aworldtype.length;

        for (int j = 0; j < i; ++j) {
            WorldType worldtype = aworldtype[j];

            if (worldtype != null && worldtype.field_77133_f.equalsIgnoreCase(s)) {
                return worldtype;
            }
        }

        return null;
    }

    public int func_82747_f() {
        return this.field_82748_f;
    }

    private WorldType func_151358_j() {
        this.field_151361_l = true;
        return this;
    }
}
