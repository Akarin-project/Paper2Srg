package net.minecraft.world;
import net.minecraft.world.storage.WorldInfo;


public final class WorldSettings {

    private final long field_77174_a;
    private final GameType field_77172_b;
    private final boolean field_77173_c;
    private final boolean field_77170_d;
    private final WorldType field_77171_e;
    private boolean field_77168_f;
    private boolean field_77169_g;
    private String field_82751_h;

    public WorldSettings(long i, GameType enumgamemode, boolean flag, boolean flag1, WorldType worldtype) {
        this.field_82751_h = "";
        this.field_77174_a = i;
        this.field_77172_b = enumgamemode;
        this.field_77173_c = flag;
        this.field_77170_d = flag1;
        this.field_77171_e = worldtype;
    }

    public WorldSettings(WorldInfo worlddata) {
        this(worlddata.func_76063_b(), worlddata.func_76077_q(), worlddata.func_76089_r(), worlddata.func_76093_s(), worlddata.func_76067_t());
    }

    public WorldSettings func_77159_a() {
        this.field_77169_g = true;
        return this;
    }

    public WorldSettings func_82750_a(String s) {
        this.field_82751_h = s;
        return this;
    }

    public boolean func_77167_c() {
        return this.field_77169_g;
    }

    public long func_77160_d() {
        return this.field_77174_a;
    }

    public GameType func_77162_e() {
        return this.field_77172_b;
    }

    public boolean func_77158_f() {
        return this.field_77170_d;
    }

    public boolean func_77164_g() {
        return this.field_77173_c;
    }

    public WorldType func_77165_h() {
        return this.field_77171_e;
    }

    public boolean func_77163_i() {
        return this.field_77168_f;
    }

    public static GameType func_77161_a(int i) {
        return GameType.func_77146_a(i);
    }

    public String func_82749_j() {
        return this.field_82751_h;
    }
}
