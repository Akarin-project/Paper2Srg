package net.minecraft.world.storage;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import org.bukkit.Bukkit;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
// CraftBukkit end

public class WorldInfo {

    private String field_186349_b;
    private int field_186350_c;
    private boolean field_186351_d;
    public static final EnumDifficulty field_176156_a = EnumDifficulty.NORMAL;
    private long field_76100_a;
    private WorldType field_76098_b;
    private String field_82576_c;
    private int field_76099_c;
    private int field_76096_d;
    private int field_76097_e;
    private long field_82575_g;
    private long field_76094_f;
    private long field_76095_g;
    private long field_76107_h;
    private NBTTagCompound field_76108_i;
    private int field_76105_j;
    private String field_76106_k;
    private int field_76103_l;
    private int field_176157_p;
    private boolean field_76104_m;
    private int field_76101_n;
    private boolean field_76102_o;
    private int field_76114_p;
    private GameType field_76113_q;
    private boolean field_76112_r;
    private boolean field_76111_s;
    private boolean field_76110_t;
    private boolean field_76109_u;
    private EnumDifficulty field_176158_z;
    private boolean field_176150_A;
    private double field_176151_B;
    private double field_176152_C;
    private double field_176146_D;
    private long field_176147_E;
    private double field_176148_F;
    private double field_176149_G;
    private double field_176153_H;
    private int field_176154_I;
    private int field_176155_J;
    private final Map<DimensionType, NBTTagCompound> field_186348_N;
    private GameRules field_82577_x;
    public WorldServer world; // CraftBukkit

    protected WorldInfo() {
        this.field_76098_b = WorldType.field_77137_b;
        this.field_82576_c = "";
        this.field_176146_D = 6.0E7D;
        this.field_176149_G = 5.0D;
        this.field_176153_H = 0.2D;
        this.field_176154_I = 5;
        this.field_176155_J = 15;
        this.field_186348_N = Maps.newEnumMap(DimensionType.class);
        this.field_82577_x = new GameRules();
    }

    public static void func_189967_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.LEVEL, new IDataWalker() {
            public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (nbttagcompound.func_150297_b("Player", 10)) {
                    nbttagcompound.func_74782_a("Player", dataconverter.func_188251_a(FixTypes.PLAYER, nbttagcompound.func_74775_l("Player"), i));
                }

                return nbttagcompound;
            }
        });
    }

    public WorldInfo(NBTTagCompound nbttagcompound) {
        this.field_76098_b = WorldType.field_77137_b;
        this.field_82576_c = "";
        this.field_176146_D = 6.0E7D;
        this.field_176149_G = 5.0D;
        this.field_176153_H = 0.2D;
        this.field_176154_I = 5;
        this.field_176155_J = 15;
        this.field_186348_N = Maps.newEnumMap(DimensionType.class);
        this.field_82577_x = new GameRules();
        NBTTagCompound nbttagcompound1;

        if (nbttagcompound.func_150297_b("Version", 10)) {
            nbttagcompound1 = nbttagcompound.func_74775_l("Version");
            this.field_186349_b = nbttagcompound1.func_74779_i("Name");
            this.field_186350_c = nbttagcompound1.func_74762_e("Id");
            this.field_186351_d = nbttagcompound1.func_74767_n("Snapshot");
        }

        this.field_76100_a = nbttagcompound.func_74763_f("RandomSeed");
        if (nbttagcompound.func_150297_b("generatorName", 8)) {
            String s = nbttagcompound.func_74779_i("generatorName");

            this.field_76098_b = WorldType.func_77130_a(s);
            if (this.field_76098_b == null) {
                this.field_76098_b = WorldType.field_77137_b;
            } else if (this.field_76098_b.func_77125_e()) {
                int i = 0;

                if (nbttagcompound.func_150297_b("generatorVersion", 99)) {
                    i = nbttagcompound.func_74762_e("generatorVersion");
                }

                this.field_76098_b = this.field_76098_b.func_77132_a(i);
            }

            if (nbttagcompound.func_150297_b("generatorOptions", 8)) {
                this.field_82576_c = nbttagcompound.func_74779_i("generatorOptions");
            }
        }

        this.field_76113_q = GameType.func_77146_a(nbttagcompound.func_74762_e("GameType"));
        if (nbttagcompound.func_150297_b("MapFeatures", 99)) {
            this.field_76112_r = nbttagcompound.func_74767_n("MapFeatures");
        } else {
            this.field_76112_r = true;
        }

        this.field_76099_c = nbttagcompound.func_74762_e("SpawnX");
        this.field_76096_d = nbttagcompound.func_74762_e("SpawnY");
        this.field_76097_e = nbttagcompound.func_74762_e("SpawnZ");
        this.field_82575_g = nbttagcompound.func_74763_f("Time");
        if (nbttagcompound.func_150297_b("DayTime", 99)) {
            this.field_76094_f = nbttagcompound.func_74763_f("DayTime");
        } else {
            this.field_76094_f = this.field_82575_g;
        }

        this.field_76095_g = nbttagcompound.func_74763_f("LastPlayed");
        this.field_76107_h = nbttagcompound.func_74763_f("SizeOnDisk");
        this.field_76106_k = nbttagcompound.func_74779_i("LevelName");
        this.field_76103_l = nbttagcompound.func_74762_e("version");
        this.field_176157_p = nbttagcompound.func_74762_e("clearWeatherTime");
        this.field_76101_n = nbttagcompound.func_74762_e("rainTime");
        this.field_76104_m = nbttagcompound.func_74767_n("raining");
        this.field_76114_p = nbttagcompound.func_74762_e("thunderTime");
        this.field_76102_o = nbttagcompound.func_74767_n("thundering");
        this.field_76111_s = nbttagcompound.func_74767_n("hardcore");
        if (nbttagcompound.func_150297_b("initialized", 99)) {
            this.field_76109_u = nbttagcompound.func_74767_n("initialized");
        } else {
            this.field_76109_u = true;
        }

        if (nbttagcompound.func_150297_b("allowCommands", 99)) {
            this.field_76110_t = nbttagcompound.func_74767_n("allowCommands");
        } else {
            this.field_76110_t = this.field_76113_q == GameType.CREATIVE;
        }

        if (nbttagcompound.func_150297_b("Player", 10)) {
            this.field_76108_i = nbttagcompound.func_74775_l("Player");
            this.field_76105_j = this.field_76108_i.func_74762_e("Dimension");
        }

        if (nbttagcompound.func_150297_b("GameRules", 10)) {
            this.field_82577_x.func_82768_a(nbttagcompound.func_74775_l("GameRules"));
        }

        if (nbttagcompound.func_150297_b("Difficulty", 99)) {
            this.field_176158_z = EnumDifficulty.func_151523_a(nbttagcompound.func_74771_c("Difficulty"));
        }

        if (nbttagcompound.func_150297_b("DifficultyLocked", 1)) {
            this.field_176150_A = nbttagcompound.func_74767_n("DifficultyLocked");
        }

        if (nbttagcompound.func_150297_b("BorderCenterX", 99)) {
            this.field_176151_B = nbttagcompound.func_74769_h("BorderCenterX");
        }

        if (nbttagcompound.func_150297_b("BorderCenterZ", 99)) {
            this.field_176152_C = nbttagcompound.func_74769_h("BorderCenterZ");
        }

        if (nbttagcompound.func_150297_b("BorderSize", 99)) {
            this.field_176146_D = nbttagcompound.func_74769_h("BorderSize");
        }

        if (nbttagcompound.func_150297_b("BorderSizeLerpTime", 99)) {
            this.field_176147_E = nbttagcompound.func_74763_f("BorderSizeLerpTime");
        }

        if (nbttagcompound.func_150297_b("BorderSizeLerpTarget", 99)) {
            this.field_176148_F = nbttagcompound.func_74769_h("BorderSizeLerpTarget");
        }

        if (nbttagcompound.func_150297_b("BorderSafeZone", 99)) {
            this.field_176149_G = nbttagcompound.func_74769_h("BorderSafeZone");
        }

        if (nbttagcompound.func_150297_b("BorderDamagePerBlock", 99)) {
            this.field_176153_H = nbttagcompound.func_74769_h("BorderDamagePerBlock");
        }

        if (nbttagcompound.func_150297_b("BorderWarningBlocks", 99)) {
            this.field_176154_I = nbttagcompound.func_74762_e("BorderWarningBlocks");
        }

        if (nbttagcompound.func_150297_b("BorderWarningTime", 99)) {
            this.field_176155_J = nbttagcompound.func_74762_e("BorderWarningTime");
        }

        if (nbttagcompound.func_150297_b("DimensionData", 10)) {
            nbttagcompound1 = nbttagcompound.func_74775_l("DimensionData");
            Iterator iterator = nbttagcompound1.func_150296_c().iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                this.field_186348_N.put(DimensionType.func_186069_a(Integer.parseInt(s1)), nbttagcompound1.func_74775_l(s1));
            }
        }

    }

    public WorldInfo(WorldSettings worldsettings, String s) {
        this.field_76098_b = WorldType.field_77137_b;
        this.field_82576_c = "";
        this.field_176146_D = 6.0E7D;
        this.field_176149_G = 5.0D;
        this.field_176153_H = 0.2D;
        this.field_176154_I = 5;
        this.field_176155_J = 15;
        this.field_186348_N = Maps.newEnumMap(DimensionType.class);
        this.field_82577_x = new GameRules();
        this.func_176127_a(worldsettings);
        this.field_76106_k = s;
        this.field_176158_z = WorldInfo.field_176156_a;
        this.field_76109_u = false;
    }

    public void func_176127_a(WorldSettings worldsettings) {
        this.field_76100_a = worldsettings.func_77160_d();
        this.field_76113_q = worldsettings.func_77162_e();
        this.field_76112_r = worldsettings.func_77164_g();
        this.field_76111_s = worldsettings.func_77158_f();
        this.field_76098_b = worldsettings.func_77165_h();
        this.field_82576_c = worldsettings.func_82749_j();
        this.field_76110_t = worldsettings.func_77163_i();
    }

    public WorldInfo(WorldInfo worlddata) {
        this.field_76098_b = WorldType.field_77137_b;
        this.field_82576_c = "";
        this.field_176146_D = 6.0E7D;
        this.field_176149_G = 5.0D;
        this.field_176153_H = 0.2D;
        this.field_176154_I = 5;
        this.field_176155_J = 15;
        this.field_186348_N = Maps.newEnumMap(DimensionType.class);
        this.field_82577_x = new GameRules();
        this.field_76100_a = worlddata.field_76100_a;
        this.field_76098_b = worlddata.field_76098_b;
        this.field_82576_c = worlddata.field_82576_c;
        this.field_76113_q = worlddata.field_76113_q;
        this.field_76112_r = worlddata.field_76112_r;
        this.field_76099_c = worlddata.field_76099_c;
        this.field_76096_d = worlddata.field_76096_d;
        this.field_76097_e = worlddata.field_76097_e;
        this.field_82575_g = worlddata.field_82575_g;
        this.field_76094_f = worlddata.field_76094_f;
        this.field_76095_g = worlddata.field_76095_g;
        this.field_76107_h = worlddata.field_76107_h;
        this.field_76108_i = worlddata.field_76108_i;
        this.field_76105_j = worlddata.field_76105_j;
        this.field_76106_k = worlddata.field_76106_k;
        this.field_76103_l = worlddata.field_76103_l;
        this.field_76101_n = worlddata.field_76101_n;
        this.field_76104_m = worlddata.field_76104_m;
        this.field_76114_p = worlddata.field_76114_p;
        this.field_76102_o = worlddata.field_76102_o;
        this.field_76111_s = worlddata.field_76111_s;
        this.field_76110_t = worlddata.field_76110_t;
        this.field_76109_u = worlddata.field_76109_u;
        this.field_82577_x = worlddata.field_82577_x;
        this.field_176158_z = worlddata.field_176158_z;
        this.field_176150_A = worlddata.field_176150_A;
        this.field_176151_B = worlddata.field_176151_B;
        this.field_176152_C = worlddata.field_176152_C;
        this.field_176146_D = worlddata.field_176146_D;
        this.field_176147_E = worlddata.field_176147_E;
        this.field_176148_F = worlddata.field_176148_F;
        this.field_176149_G = worlddata.field_176149_G;
        this.field_176153_H = worlddata.field_176153_H;
        this.field_176155_J = worlddata.field_176155_J;
        this.field_176154_I = worlddata.field_176154_I;
    }

    public NBTTagCompound func_76082_a(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound == null) {
            nbttagcompound = this.field_76108_i;
        }

        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        this.func_76064_a(nbttagcompound1, nbttagcompound);
        return nbttagcompound1;
    }

    private void func_76064_a(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1) {
        NBTTagCompound nbttagcompound2 = new NBTTagCompound();

        nbttagcompound2.func_74778_a("Name", "1.12.2");
        nbttagcompound2.func_74768_a("Id", 1343);
        nbttagcompound2.func_74757_a("Snapshot", false);
        nbttagcompound.func_74782_a("Version", nbttagcompound2);
        nbttagcompound.func_74768_a("DataVersion", 1343);
        nbttagcompound.func_74772_a("RandomSeed", this.field_76100_a);
        nbttagcompound.func_74778_a("generatorName", this.field_76098_b.func_77127_a());
        nbttagcompound.func_74768_a("generatorVersion", this.field_76098_b.func_77131_c());
        nbttagcompound.func_74778_a("generatorOptions", this.field_82576_c);
        nbttagcompound.func_74768_a("GameType", this.field_76113_q.func_77148_a());
        nbttagcompound.func_74757_a("MapFeatures", this.field_76112_r);
        nbttagcompound.func_74768_a("SpawnX", this.field_76099_c);
        nbttagcompound.func_74768_a("SpawnY", this.field_76096_d);
        nbttagcompound.func_74768_a("SpawnZ", this.field_76097_e);
        nbttagcompound.func_74772_a("Time", this.field_82575_g);
        nbttagcompound.func_74772_a("DayTime", this.field_76094_f);
        nbttagcompound.func_74772_a("SizeOnDisk", this.field_76107_h);
        nbttagcompound.func_74772_a("LastPlayed", MinecraftServer.func_130071_aq());
        nbttagcompound.func_74778_a("LevelName", this.field_76106_k);
        nbttagcompound.func_74768_a("version", this.field_76103_l);
        nbttagcompound.func_74768_a("clearWeatherTime", this.field_176157_p);
        nbttagcompound.func_74768_a("rainTime", this.field_76101_n);
        nbttagcompound.func_74757_a("raining", this.field_76104_m);
        nbttagcompound.func_74768_a("thunderTime", this.field_76114_p);
        nbttagcompound.func_74757_a("thundering", this.field_76102_o);
        nbttagcompound.func_74757_a("hardcore", this.field_76111_s);
        nbttagcompound.func_74757_a("allowCommands", this.field_76110_t);
        nbttagcompound.func_74757_a("initialized", this.field_76109_u);
        nbttagcompound.func_74780_a("BorderCenterX", this.field_176151_B);
        nbttagcompound.func_74780_a("BorderCenterZ", this.field_176152_C);
        nbttagcompound.func_74780_a("BorderSize", this.field_176146_D);
        nbttagcompound.func_74772_a("BorderSizeLerpTime", this.field_176147_E);
        nbttagcompound.func_74780_a("BorderSafeZone", this.field_176149_G);
        nbttagcompound.func_74780_a("BorderDamagePerBlock", this.field_176153_H);
        nbttagcompound.func_74780_a("BorderSizeLerpTarget", this.field_176148_F);
        nbttagcompound.func_74780_a("BorderWarningBlocks", (double) this.field_176154_I);
        nbttagcompound.func_74780_a("BorderWarningTime", (double) this.field_176155_J);
        if (this.field_176158_z != null) {
            nbttagcompound.func_74774_a("Difficulty", (byte) this.field_176158_z.func_151525_a());
        }

        nbttagcompound.func_74757_a("DifficultyLocked", this.field_176150_A);
        nbttagcompound.func_74782_a("GameRules", this.field_82577_x.func_82770_a());
        NBTTagCompound nbttagcompound3 = new NBTTagCompound();
        Iterator iterator = this.field_186348_N.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            nbttagcompound3.func_74782_a(String.valueOf(((DimensionType) entry.getKey()).func_186068_a()), (NBTBase) entry.getValue());
        }

        nbttagcompound.func_74782_a("DimensionData", nbttagcompound3);
        if (nbttagcompound1 != null) {
            nbttagcompound.func_74782_a("Player", nbttagcompound1);
        }

    }

    public long func_76063_b() {
        return this.field_76100_a;
    }

    public int func_76079_c() {
        return this.field_76099_c;
    }

    public int func_76075_d() {
        return this.field_76096_d;
    }

    public int func_76074_e() {
        return this.field_76097_e;
    }

    public long func_82573_f() {
        return this.field_82575_g;
    }

    public long func_76073_f() {
        return this.field_76094_f;
    }

    public NBTTagCompound func_76072_h() {
        return this.field_76108_i;
    }

    public void func_82572_b(long i) {
        this.field_82575_g = i;
    }

    public void func_76068_b(long i) {
        this.field_76094_f = i;
    }

    public void func_176143_a(BlockPos blockposition) {
        this.field_76099_c = blockposition.func_177958_n();
        this.field_76096_d = blockposition.func_177956_o();
        this.field_76097_e = blockposition.func_177952_p();
    }

    public String func_76065_j() {
        return this.field_76106_k;
    }

    public void func_76062_a(String s) {
        this.field_76106_k = s;
    }

    public int func_76088_k() {
        return this.field_76103_l;
    }

    public void func_76078_e(int i) {
        this.field_76103_l = i;
    }

    public int func_176133_A() {
        return this.field_176157_p;
    }

    public void func_176142_i(int i) {
        this.field_176157_p = i;
    }

    public boolean func_76061_m() {
        return this.field_76102_o;
    }

    public void func_76069_a(boolean flag) {
        // CraftBukkit start
        org.bukkit.World world = Bukkit.getWorld(func_76065_j());
        if (world != null) {
            ThunderChangeEvent thunder = new ThunderChangeEvent(world, flag);
            Bukkit.getServer().getPluginManager().callEvent(thunder);
            if (thunder.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end
        this.field_76102_o = flag;
    }

    public int func_76071_n() {
        return this.field_76114_p;
    }

    public void func_76090_f(int i) {
        this.field_76114_p = i;
    }

    public boolean func_76059_o() {
        return this.field_76104_m;
    }

    public void func_76084_b(boolean flag) {
        // CraftBukkit start
        org.bukkit.World world = Bukkit.getWorld(func_76065_j());
        if (world != null) {
            WeatherChangeEvent weather = new WeatherChangeEvent(world, flag);
            Bukkit.getServer().getPluginManager().callEvent(weather);
            if (weather.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end
        this.field_76104_m = flag;
    }

    public int func_76083_p() {
        return this.field_76101_n;
    }

    public void func_76080_g(int i) {
        this.field_76101_n = i;
    }

    public GameType func_76077_q() {
        return this.field_76113_q;
    }

    public boolean func_76089_r() {
        return this.field_76112_r;
    }

    public void func_176128_f(boolean flag) {
        this.field_76112_r = flag;
    }

    public void func_76060_a(GameType enumgamemode) {
        this.field_76113_q = enumgamemode;
    }

    public boolean func_76093_s() {
        return this.field_76111_s;
    }

    public void func_176119_g(boolean flag) {
        this.field_76111_s = flag;
    }

    public WorldType func_76067_t() {
        return this.field_76098_b;
    }

    public void func_76085_a(WorldType worldtype) {
        this.field_76098_b = worldtype;
    }

    public String func_82571_y() {
        return this.field_82576_c == null ? "" : this.field_82576_c;
    }

    public boolean func_76086_u() {
        return this.field_76110_t;
    }

    public void func_176121_c(boolean flag) {
        this.field_76110_t = flag;
    }

    public boolean func_76070_v() {
        return this.field_76109_u;
    }

    public void func_76091_d(boolean flag) {
        this.field_76109_u = flag;
    }

    public GameRules func_82574_x() {
        return this.field_82577_x;
    }

    public double func_176120_C() {
        return this.field_176151_B;
    }

    public double func_176126_D() {
        return this.field_176152_C;
    }

    public double func_176137_E() {
        return this.field_176146_D;
    }

    public void func_176145_a(double d0) {
        this.field_176146_D = d0;
    }

    public long func_176134_F() {
        return this.field_176147_E;
    }

    public void func_176135_e(long i) {
        this.field_176147_E = i;
    }

    public double func_176132_G() {
        return this.field_176148_F;
    }

    public void func_176118_b(double d0) {
        this.field_176148_F = d0;
    }

    public void func_176141_c(double d0) {
        this.field_176152_C = d0;
    }

    public void func_176124_d(double d0) {
        this.field_176151_B = d0;
    }

    public double func_176138_H() {
        return this.field_176149_G;
    }

    public void func_176129_e(double d0) {
        this.field_176149_G = d0;
    }

    public double func_176140_I() {
        return this.field_176153_H;
    }

    public void func_176125_f(double d0) {
        this.field_176153_H = d0;
    }

    public int func_176131_J() {
        return this.field_176154_I;
    }

    public int func_176139_K() {
        return this.field_176155_J;
    }

    public void func_176122_j(int i) {
        this.field_176154_I = i;
    }

    public void func_176136_k(int i) {
        this.field_176155_J = i;
    }

    public EnumDifficulty func_176130_y() {
        return this.field_176158_z;
    }

    public void func_176144_a(EnumDifficulty enumdifficulty) {
        this.field_176158_z = enumdifficulty;
        // CraftBukkit start
        SPacketServerDifficulty packet = new SPacketServerDifficulty(this.func_176130_y(), this.func_176123_z());
        for (EntityPlayerMP player : (java.util.List<EntityPlayerMP>) (java.util.List) world.field_73010_i) {
            player.field_71135_a.func_147359_a(packet);
        }
        // CraftBukkit end
    }

    public boolean func_176123_z() {
        return this.field_176150_A;
    }

    public void func_180783_e(boolean flag) {
        this.field_176150_A = flag;
    }

    public void func_85118_a(CrashReportCategory crashreportsystemdetails) {
        crashreportsystemdetails.func_189529_a("Level seed", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.valueOf(WorldInfo.this.func_76063_b());
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Level generator", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.format("ID %02d - %s, ver %d. Features enabled: %b", new Object[] { Integer.valueOf(WorldInfo.this.field_76098_b.func_82747_f()), WorldInfo.this.field_76098_b.func_77127_a(), Integer.valueOf(WorldInfo.this.field_76098_b.func_77131_c()), Boolean.valueOf(WorldInfo.this.field_76112_r)});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Level generator options", new ICrashReportDetail() {
            public String a() throws Exception {
                return WorldInfo.this.field_82576_c;
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Level spawn location", new ICrashReportDetail() {
            public String a() throws Exception {
                return CrashReportCategory.func_184876_a(WorldInfo.this.field_76099_c, WorldInfo.this.field_76096_d, WorldInfo.this.field_76097_e);
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Level time", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.format("%d game time, %d day time", new Object[] { Long.valueOf(WorldInfo.this.field_82575_g), Long.valueOf(WorldInfo.this.field_76094_f)});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Level dimension", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.valueOf(WorldInfo.this.field_76105_j);
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Level storage version", new ICrashReportDetail() {
            public String a() throws Exception {
                String s = "Unknown?";

                try {
                    switch (WorldInfo.this.field_76103_l) {
                    case 19132:
                        s = "McRegion";
                        break;

                    case 19133:
                        s = "Anvil";
                    }
                } catch (Throwable throwable) {
                    ;
                }

                return String.format("0x%05X - %s", new Object[] { Integer.valueOf(WorldInfo.this.field_76103_l), s});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Level weather", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] { Integer.valueOf(WorldInfo.this.field_76101_n), Boolean.valueOf(WorldInfo.this.field_76104_m), Integer.valueOf(WorldInfo.this.field_76114_p), Boolean.valueOf(WorldInfo.this.field_76102_o)});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Level game mode", new ICrashReportDetail() {
            public String a() throws Exception {
                return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] { WorldInfo.this.field_76113_q.func_77149_b(), Integer.valueOf(WorldInfo.this.field_76113_q.func_77148_a()), Boolean.valueOf(WorldInfo.this.field_76111_s), Boolean.valueOf(WorldInfo.this.field_76110_t)});
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
    }

    public NBTTagCompound func_186347_a(DimensionType dimensionmanager) {
        NBTTagCompound nbttagcompound = (NBTTagCompound) this.field_186348_N.get(dimensionmanager);

        return nbttagcompound == null ? new NBTTagCompound() : nbttagcompound;
    }

    public void func_186345_a(DimensionType dimensionmanager, NBTTagCompound nbttagcompound) {
        this.field_186348_N.put(dimensionmanager, nbttagcompound);
    }

    // CraftBukkit start - Check if the name stored in NBT is the correct one
    public void checkName( String name ) {
        if ( !this.field_76106_k.equals( name ) ) {
            this.field_76106_k = name;
        }
    }
    // CraftBukkit end
}
