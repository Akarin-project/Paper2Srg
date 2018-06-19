package net.minecraft.world.gen;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

import net.minecraft.init.Biomes;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.biome.Biome;

public class ChunkGeneratorSettings {

    public final float field_177811_a;
    public final float field_177809_b;
    public final float field_177810_c;
    public final float field_177806_d;
    public final float field_177808_e;
    public final float field_177803_f;
    public final float field_177804_g;
    public final float field_177825_h;
    public final float field_177827_i;
    public final float field_177821_j;
    public final float field_177823_k;
    public final float field_177817_l;
    public final float field_177819_m;
    public final float field_177813_n;
    public final float field_177815_o;
    public final float field_177843_p;
    public final int field_177841_q;
    public final boolean field_177839_r;
    public final boolean field_177837_s;
    public final int field_177835_t;
    public final boolean field_177833_u;
    public final boolean field_177831_v;
    public final boolean field_177829_w;
    public final boolean field_177854_x;
    public final boolean field_177852_y;
    public final boolean field_191077_z;
    public final boolean field_177850_z;
    public final boolean field_177781_A;
    public final int field_177782_B;
    public final boolean field_177783_C;
    public final int field_177777_D;
    public final boolean field_177778_E;
    public final int field_177779_F;
    public final int field_177780_G;
    public final int field_177788_H;
    public final int field_177789_I;
    public final int field_177790_J;
    public final int field_177791_K;
    public final int field_177784_L;
    public final int field_177785_M;
    public final int field_177786_N;
    public final int field_177787_O;
    public final int field_177797_P;
    public final int field_177796_Q;
    public final int field_177799_R;
    public final int field_177798_S;
    public final int field_177793_T;
    public final int field_177792_U;
    public final int field_177795_V;
    public final int field_177794_W;
    public final int field_177801_X;
    public final int field_177800_Y;
    public final int field_177802_Z;
    public final int field_177846_aa;
    public final int field_177847_ab;
    public final int field_177844_ac;
    public final int field_177845_ad;
    public final int field_177851_ae;
    public final int field_177853_af;
    public final int field_177848_ag;
    public final int field_177849_ah;
    public final int field_177832_ai;
    public final int field_177834_aj;
    public final int field_177828_ak;
    public final int field_177830_al;
    public final int field_177840_am;
    public final int field_177842_an;
    public final int field_177836_ao;
    public final int field_177838_ap;
    public final int field_177818_aq;
    public final int field_177816_ar;
    public final int field_177814_as;
    public final int field_177812_at;
    public final int field_177826_au;
    public final int field_177824_av;
    public final int field_177822_aw;
    public final int field_177820_ax;
    public final int field_177807_ay;
    public final int field_177805_az;

    private ChunkGeneratorSettings(ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings) {
        this.field_177811_a = customworldsettingsfinal_customworldsettings.field_177899_b;
        this.field_177809_b = customworldsettingsfinal_customworldsettings.field_177900_c;
        this.field_177810_c = customworldsettingsfinal_customworldsettings.field_177896_d;
        this.field_177806_d = customworldsettingsfinal_customworldsettings.field_177898_e;
        this.field_177808_e = customworldsettingsfinal_customworldsettings.field_177893_f;
        this.field_177803_f = customworldsettingsfinal_customworldsettings.field_177894_g;
        this.field_177804_g = customworldsettingsfinal_customworldsettings.field_177915_h;
        this.field_177825_h = customworldsettingsfinal_customworldsettings.field_177917_i;
        this.field_177827_i = customworldsettingsfinal_customworldsettings.field_177911_j;
        this.field_177821_j = customworldsettingsfinal_customworldsettings.field_177913_k;
        this.field_177823_k = customworldsettingsfinal_customworldsettings.field_177907_l;
        this.field_177817_l = customworldsettingsfinal_customworldsettings.field_177909_m;
        this.field_177819_m = customworldsettingsfinal_customworldsettings.field_177903_n;
        this.field_177813_n = customworldsettingsfinal_customworldsettings.field_177905_o;
        this.field_177815_o = customworldsettingsfinal_customworldsettings.field_177933_p;
        this.field_177843_p = customworldsettingsfinal_customworldsettings.field_177931_q;
        this.field_177841_q = customworldsettingsfinal_customworldsettings.field_177929_r;
        this.field_177839_r = customworldsettingsfinal_customworldsettings.field_177927_s;
        this.field_177837_s = customworldsettingsfinal_customworldsettings.field_177925_t;
        this.field_177835_t = customworldsettingsfinal_customworldsettings.field_177923_u;
        this.field_177833_u = customworldsettingsfinal_customworldsettings.field_177921_v;
        this.field_177831_v = customworldsettingsfinal_customworldsettings.field_177919_w;
        this.field_177829_w = customworldsettingsfinal_customworldsettings.field_177944_x;
        this.field_177854_x = customworldsettingsfinal_customworldsettings.field_177942_y;
        this.field_177852_y = customworldsettingsfinal_customworldsettings.field_177940_z;
        this.field_191077_z = customworldsettingsfinal_customworldsettings.field_191076_A;
        this.field_177850_z = customworldsettingsfinal_customworldsettings.field_177870_A;
        this.field_177781_A = customworldsettingsfinal_customworldsettings.field_177871_B;
        this.field_177782_B = customworldsettingsfinal_customworldsettings.field_177872_C;
        this.field_177783_C = customworldsettingsfinal_customworldsettings.field_177866_D;
        this.field_177777_D = customworldsettingsfinal_customworldsettings.field_177867_E;
        this.field_177778_E = customworldsettingsfinal_customworldsettings.field_177868_F;
        this.field_177779_F = customworldsettingsfinal_customworldsettings.field_177869_G;
        this.field_177780_G = customworldsettingsfinal_customworldsettings.field_177877_H;
        this.field_177788_H = customworldsettingsfinal_customworldsettings.field_177878_I;
        this.field_177789_I = customworldsettingsfinal_customworldsettings.field_177879_J;
        this.field_177790_J = customworldsettingsfinal_customworldsettings.field_177880_K;
        this.field_177791_K = customworldsettingsfinal_customworldsettings.field_177873_L;
        this.field_177784_L = customworldsettingsfinal_customworldsettings.field_177874_M;
        this.field_177785_M = customworldsettingsfinal_customworldsettings.field_177875_N;
        this.field_177786_N = customworldsettingsfinal_customworldsettings.field_177876_O;
        this.field_177787_O = customworldsettingsfinal_customworldsettings.field_177886_P;
        this.field_177797_P = customworldsettingsfinal_customworldsettings.field_177885_Q;
        this.field_177796_Q = customworldsettingsfinal_customworldsettings.field_177888_R;
        this.field_177799_R = customworldsettingsfinal_customworldsettings.field_177887_S;
        this.field_177798_S = customworldsettingsfinal_customworldsettings.field_177882_T;
        this.field_177793_T = customworldsettingsfinal_customworldsettings.field_177881_U;
        this.field_177792_U = customworldsettingsfinal_customworldsettings.field_177884_V;
        this.field_177795_V = customworldsettingsfinal_customworldsettings.field_177883_W;
        this.field_177794_W = customworldsettingsfinal_customworldsettings.field_177891_X;
        this.field_177801_X = customworldsettingsfinal_customworldsettings.field_177890_Y;
        this.field_177800_Y = customworldsettingsfinal_customworldsettings.field_177892_Z;
        this.field_177802_Z = customworldsettingsfinal_customworldsettings.field_177936_aa;
        this.field_177846_aa = customworldsettingsfinal_customworldsettings.field_177937_ab;
        this.field_177847_ab = customworldsettingsfinal_customworldsettings.field_177934_ac;
        this.field_177844_ac = customworldsettingsfinal_customworldsettings.field_177935_ad;
        this.field_177845_ad = customworldsettingsfinal_customworldsettings.field_177941_ae;
        this.field_177851_ae = customworldsettingsfinal_customworldsettings.field_177943_af;
        this.field_177853_af = customworldsettingsfinal_customworldsettings.field_177938_ag;
        this.field_177848_ag = customworldsettingsfinal_customworldsettings.field_177939_ah;
        this.field_177849_ah = customworldsettingsfinal_customworldsettings.field_177922_ai;
        this.field_177832_ai = customworldsettingsfinal_customworldsettings.field_177924_aj;
        this.field_177834_aj = customworldsettingsfinal_customworldsettings.field_177918_ak;
        this.field_177828_ak = customworldsettingsfinal_customworldsettings.field_177920_al;
        this.field_177830_al = customworldsettingsfinal_customworldsettings.field_177930_am;
        this.field_177840_am = customworldsettingsfinal_customworldsettings.field_177932_an;
        this.field_177842_an = customworldsettingsfinal_customworldsettings.field_177926_ao;
        this.field_177836_ao = customworldsettingsfinal_customworldsettings.field_177928_ap;
        this.field_177838_ap = customworldsettingsfinal_customworldsettings.field_177908_aq;
        this.field_177818_aq = customworldsettingsfinal_customworldsettings.field_177906_ar;
        this.field_177816_ar = customworldsettingsfinal_customworldsettings.field_177904_as;
        this.field_177814_as = customworldsettingsfinal_customworldsettings.field_177902_at;
        this.field_177812_at = customworldsettingsfinal_customworldsettings.field_177916_au;
        this.field_177826_au = customworldsettingsfinal_customworldsettings.field_177914_av;
        this.field_177824_av = customworldsettingsfinal_customworldsettings.field_177912_aw;
        this.field_177822_aw = customworldsettingsfinal_customworldsettings.field_177910_ax;
        this.field_177820_ax = customworldsettingsfinal_customworldsettings.field_177897_ay;
        this.field_177807_ay = customworldsettingsfinal_customworldsettings.field_177895_az;
        this.field_177805_az = customworldsettingsfinal_customworldsettings.field_177889_aA;
    }

    ChunkGeneratorSettings(ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings, Object object) {
        this(customworldsettingsfinal_customworldsettings);
    }

    public static class Serializer implements JsonDeserializer<ChunkGeneratorSettings.Factory>, JsonSerializer<ChunkGeneratorSettings.Factory> {

        public Serializer() {}

        public ChunkGeneratorSettings.Factory deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings = new ChunkGeneratorSettings.Factory();

            try {
                customworldsettingsfinal_customworldsettings.field_177899_b = JsonUtils.func_151221_a(jsonobject, "coordinateScale", customworldsettingsfinal_customworldsettings.field_177899_b);
                customworldsettingsfinal_customworldsettings.field_177900_c = JsonUtils.func_151221_a(jsonobject, "heightScale", customworldsettingsfinal_customworldsettings.field_177900_c);
                customworldsettingsfinal_customworldsettings.field_177898_e = JsonUtils.func_151221_a(jsonobject, "lowerLimitScale", customworldsettingsfinal_customworldsettings.field_177898_e);
                customworldsettingsfinal_customworldsettings.field_177896_d = JsonUtils.func_151221_a(jsonobject, "upperLimitScale", customworldsettingsfinal_customworldsettings.field_177896_d);
                customworldsettingsfinal_customworldsettings.field_177893_f = JsonUtils.func_151221_a(jsonobject, "depthNoiseScaleX", customworldsettingsfinal_customworldsettings.field_177893_f);
                customworldsettingsfinal_customworldsettings.field_177894_g = JsonUtils.func_151221_a(jsonobject, "depthNoiseScaleZ", customworldsettingsfinal_customworldsettings.field_177894_g);
                customworldsettingsfinal_customworldsettings.field_177915_h = JsonUtils.func_151221_a(jsonobject, "depthNoiseScaleExponent", customworldsettingsfinal_customworldsettings.field_177915_h);
                customworldsettingsfinal_customworldsettings.field_177917_i = JsonUtils.func_151221_a(jsonobject, "mainNoiseScaleX", customworldsettingsfinal_customworldsettings.field_177917_i);
                customworldsettingsfinal_customworldsettings.field_177911_j = JsonUtils.func_151221_a(jsonobject, "mainNoiseScaleY", customworldsettingsfinal_customworldsettings.field_177911_j);
                customworldsettingsfinal_customworldsettings.field_177913_k = JsonUtils.func_151221_a(jsonobject, "mainNoiseScaleZ", customworldsettingsfinal_customworldsettings.field_177913_k);
                customworldsettingsfinal_customworldsettings.field_177907_l = JsonUtils.func_151221_a(jsonobject, "baseSize", customworldsettingsfinal_customworldsettings.field_177907_l);
                customworldsettingsfinal_customworldsettings.field_177909_m = JsonUtils.func_151221_a(jsonobject, "stretchY", customworldsettingsfinal_customworldsettings.field_177909_m);
                customworldsettingsfinal_customworldsettings.field_177903_n = JsonUtils.func_151221_a(jsonobject, "biomeDepthWeight", customworldsettingsfinal_customworldsettings.field_177903_n);
                customworldsettingsfinal_customworldsettings.field_177905_o = JsonUtils.func_151221_a(jsonobject, "biomeDepthOffset", customworldsettingsfinal_customworldsettings.field_177905_o);
                customworldsettingsfinal_customworldsettings.field_177933_p = JsonUtils.func_151221_a(jsonobject, "biomeScaleWeight", customworldsettingsfinal_customworldsettings.field_177933_p);
                customworldsettingsfinal_customworldsettings.field_177931_q = JsonUtils.func_151221_a(jsonobject, "biomeScaleOffset", customworldsettingsfinal_customworldsettings.field_177931_q);
                customworldsettingsfinal_customworldsettings.field_177929_r = JsonUtils.func_151208_a(jsonobject, "seaLevel", customworldsettingsfinal_customworldsettings.field_177929_r);
                customworldsettingsfinal_customworldsettings.field_177927_s = JsonUtils.func_151209_a(jsonobject, "useCaves", customworldsettingsfinal_customworldsettings.field_177927_s);
                customworldsettingsfinal_customworldsettings.field_177925_t = JsonUtils.func_151209_a(jsonobject, "useDungeons", customworldsettingsfinal_customworldsettings.field_177925_t);
                customworldsettingsfinal_customworldsettings.field_177923_u = JsonUtils.func_151208_a(jsonobject, "dungeonChance", customworldsettingsfinal_customworldsettings.field_177923_u);
                customworldsettingsfinal_customworldsettings.field_177921_v = JsonUtils.func_151209_a(jsonobject, "useStrongholds", customworldsettingsfinal_customworldsettings.field_177921_v);
                customworldsettingsfinal_customworldsettings.field_177919_w = JsonUtils.func_151209_a(jsonobject, "useVillages", customworldsettingsfinal_customworldsettings.field_177919_w);
                customworldsettingsfinal_customworldsettings.field_177944_x = JsonUtils.func_151209_a(jsonobject, "useMineShafts", customworldsettingsfinal_customworldsettings.field_177944_x);
                customworldsettingsfinal_customworldsettings.field_177942_y = JsonUtils.func_151209_a(jsonobject, "useTemples", customworldsettingsfinal_customworldsettings.field_177942_y);
                customworldsettingsfinal_customworldsettings.field_177940_z = JsonUtils.func_151209_a(jsonobject, "useMonuments", customworldsettingsfinal_customworldsettings.field_177940_z);
                customworldsettingsfinal_customworldsettings.field_191076_A = JsonUtils.func_151209_a(jsonobject, "useMansions", customworldsettingsfinal_customworldsettings.field_191076_A);
                customworldsettingsfinal_customworldsettings.field_177870_A = JsonUtils.func_151209_a(jsonobject, "useRavines", customworldsettingsfinal_customworldsettings.field_177870_A);
                customworldsettingsfinal_customworldsettings.field_177871_B = JsonUtils.func_151209_a(jsonobject, "useWaterLakes", customworldsettingsfinal_customworldsettings.field_177871_B);
                customworldsettingsfinal_customworldsettings.field_177872_C = JsonUtils.func_151208_a(jsonobject, "waterLakeChance", customworldsettingsfinal_customworldsettings.field_177872_C);
                customworldsettingsfinal_customworldsettings.field_177866_D = JsonUtils.func_151209_a(jsonobject, "useLavaLakes", customworldsettingsfinal_customworldsettings.field_177866_D);
                customworldsettingsfinal_customworldsettings.field_177867_E = JsonUtils.func_151208_a(jsonobject, "lavaLakeChance", customworldsettingsfinal_customworldsettings.field_177867_E);
                customworldsettingsfinal_customworldsettings.field_177868_F = JsonUtils.func_151209_a(jsonobject, "useLavaOceans", customworldsettingsfinal_customworldsettings.field_177868_F);
                customworldsettingsfinal_customworldsettings.field_177869_G = JsonUtils.func_151208_a(jsonobject, "fixedBiome", customworldsettingsfinal_customworldsettings.field_177869_G);
                if (customworldsettingsfinal_customworldsettings.field_177869_G < 38 && customworldsettingsfinal_customworldsettings.field_177869_G >= -1) {
                    if (customworldsettingsfinal_customworldsettings.field_177869_G >= Biome.func_185362_a(Biomes.field_76778_j)) {
                        customworldsettingsfinal_customworldsettings.field_177869_G += 2;
                    }
                } else {
                    customworldsettingsfinal_customworldsettings.field_177869_G = -1;
                }

                customworldsettingsfinal_customworldsettings.field_177877_H = JsonUtils.func_151208_a(jsonobject, "biomeSize", customworldsettingsfinal_customworldsettings.field_177877_H);
                customworldsettingsfinal_customworldsettings.field_177878_I = JsonUtils.func_151208_a(jsonobject, "riverSize", customworldsettingsfinal_customworldsettings.field_177878_I);
                customworldsettingsfinal_customworldsettings.field_177879_J = JsonUtils.func_151208_a(jsonobject, "dirtSize", customworldsettingsfinal_customworldsettings.field_177879_J);
                customworldsettingsfinal_customworldsettings.field_177880_K = JsonUtils.func_151208_a(jsonobject, "dirtCount", customworldsettingsfinal_customworldsettings.field_177880_K);
                customworldsettingsfinal_customworldsettings.field_177873_L = JsonUtils.func_151208_a(jsonobject, "dirtMinHeight", customworldsettingsfinal_customworldsettings.field_177873_L);
                customworldsettingsfinal_customworldsettings.field_177874_M = JsonUtils.func_151208_a(jsonobject, "dirtMaxHeight", customworldsettingsfinal_customworldsettings.field_177874_M);
                customworldsettingsfinal_customworldsettings.field_177875_N = JsonUtils.func_151208_a(jsonobject, "gravelSize", customworldsettingsfinal_customworldsettings.field_177875_N);
                customworldsettingsfinal_customworldsettings.field_177876_O = JsonUtils.func_151208_a(jsonobject, "gravelCount", customworldsettingsfinal_customworldsettings.field_177876_O);
                customworldsettingsfinal_customworldsettings.field_177886_P = JsonUtils.func_151208_a(jsonobject, "gravelMinHeight", customworldsettingsfinal_customworldsettings.field_177886_P);
                customworldsettingsfinal_customworldsettings.field_177885_Q = JsonUtils.func_151208_a(jsonobject, "gravelMaxHeight", customworldsettingsfinal_customworldsettings.field_177885_Q);
                customworldsettingsfinal_customworldsettings.field_177888_R = JsonUtils.func_151208_a(jsonobject, "graniteSize", customworldsettingsfinal_customworldsettings.field_177888_R);
                customworldsettingsfinal_customworldsettings.field_177887_S = JsonUtils.func_151208_a(jsonobject, "graniteCount", customworldsettingsfinal_customworldsettings.field_177887_S);
                customworldsettingsfinal_customworldsettings.field_177882_T = JsonUtils.func_151208_a(jsonobject, "graniteMinHeight", customworldsettingsfinal_customworldsettings.field_177882_T);
                customworldsettingsfinal_customworldsettings.field_177881_U = JsonUtils.func_151208_a(jsonobject, "graniteMaxHeight", customworldsettingsfinal_customworldsettings.field_177881_U);
                customworldsettingsfinal_customworldsettings.field_177884_V = JsonUtils.func_151208_a(jsonobject, "dioriteSize", customworldsettingsfinal_customworldsettings.field_177884_V);
                customworldsettingsfinal_customworldsettings.field_177883_W = JsonUtils.func_151208_a(jsonobject, "dioriteCount", customworldsettingsfinal_customworldsettings.field_177883_W);
                customworldsettingsfinal_customworldsettings.field_177891_X = JsonUtils.func_151208_a(jsonobject, "dioriteMinHeight", customworldsettingsfinal_customworldsettings.field_177891_X);
                customworldsettingsfinal_customworldsettings.field_177890_Y = JsonUtils.func_151208_a(jsonobject, "dioriteMaxHeight", customworldsettingsfinal_customworldsettings.field_177890_Y);
                customworldsettingsfinal_customworldsettings.field_177892_Z = JsonUtils.func_151208_a(jsonobject, "andesiteSize", customworldsettingsfinal_customworldsettings.field_177892_Z);
                customworldsettingsfinal_customworldsettings.field_177936_aa = JsonUtils.func_151208_a(jsonobject, "andesiteCount", customworldsettingsfinal_customworldsettings.field_177936_aa);
                customworldsettingsfinal_customworldsettings.field_177937_ab = JsonUtils.func_151208_a(jsonobject, "andesiteMinHeight", customworldsettingsfinal_customworldsettings.field_177937_ab);
                customworldsettingsfinal_customworldsettings.field_177934_ac = JsonUtils.func_151208_a(jsonobject, "andesiteMaxHeight", customworldsettingsfinal_customworldsettings.field_177934_ac);
                customworldsettingsfinal_customworldsettings.field_177935_ad = JsonUtils.func_151208_a(jsonobject, "coalSize", customworldsettingsfinal_customworldsettings.field_177935_ad);
                customworldsettingsfinal_customworldsettings.field_177941_ae = JsonUtils.func_151208_a(jsonobject, "coalCount", customworldsettingsfinal_customworldsettings.field_177941_ae);
                customworldsettingsfinal_customworldsettings.field_177943_af = JsonUtils.func_151208_a(jsonobject, "coalMinHeight", customworldsettingsfinal_customworldsettings.field_177943_af);
                customworldsettingsfinal_customworldsettings.field_177938_ag = JsonUtils.func_151208_a(jsonobject, "coalMaxHeight", customworldsettingsfinal_customworldsettings.field_177938_ag);
                customworldsettingsfinal_customworldsettings.field_177939_ah = JsonUtils.func_151208_a(jsonobject, "ironSize", customworldsettingsfinal_customworldsettings.field_177939_ah);
                customworldsettingsfinal_customworldsettings.field_177922_ai = JsonUtils.func_151208_a(jsonobject, "ironCount", customworldsettingsfinal_customworldsettings.field_177922_ai);
                customworldsettingsfinal_customworldsettings.field_177924_aj = JsonUtils.func_151208_a(jsonobject, "ironMinHeight", customworldsettingsfinal_customworldsettings.field_177924_aj);
                customworldsettingsfinal_customworldsettings.field_177918_ak = JsonUtils.func_151208_a(jsonobject, "ironMaxHeight", customworldsettingsfinal_customworldsettings.field_177918_ak);
                customworldsettingsfinal_customworldsettings.field_177920_al = JsonUtils.func_151208_a(jsonobject, "goldSize", customworldsettingsfinal_customworldsettings.field_177920_al);
                customworldsettingsfinal_customworldsettings.field_177930_am = JsonUtils.func_151208_a(jsonobject, "goldCount", customworldsettingsfinal_customworldsettings.field_177930_am);
                customworldsettingsfinal_customworldsettings.field_177932_an = JsonUtils.func_151208_a(jsonobject, "goldMinHeight", customworldsettingsfinal_customworldsettings.field_177932_an);
                customworldsettingsfinal_customworldsettings.field_177926_ao = JsonUtils.func_151208_a(jsonobject, "goldMaxHeight", customworldsettingsfinal_customworldsettings.field_177926_ao);
                customworldsettingsfinal_customworldsettings.field_177928_ap = JsonUtils.func_151208_a(jsonobject, "redstoneSize", customworldsettingsfinal_customworldsettings.field_177928_ap);
                customworldsettingsfinal_customworldsettings.field_177908_aq = JsonUtils.func_151208_a(jsonobject, "redstoneCount", customworldsettingsfinal_customworldsettings.field_177908_aq);
                customworldsettingsfinal_customworldsettings.field_177906_ar = JsonUtils.func_151208_a(jsonobject, "redstoneMinHeight", customworldsettingsfinal_customworldsettings.field_177906_ar);
                customworldsettingsfinal_customworldsettings.field_177904_as = JsonUtils.func_151208_a(jsonobject, "redstoneMaxHeight", customworldsettingsfinal_customworldsettings.field_177904_as);
                customworldsettingsfinal_customworldsettings.field_177902_at = JsonUtils.func_151208_a(jsonobject, "diamondSize", customworldsettingsfinal_customworldsettings.field_177902_at);
                customworldsettingsfinal_customworldsettings.field_177916_au = JsonUtils.func_151208_a(jsonobject, "diamondCount", customworldsettingsfinal_customworldsettings.field_177916_au);
                customworldsettingsfinal_customworldsettings.field_177914_av = JsonUtils.func_151208_a(jsonobject, "diamondMinHeight", customworldsettingsfinal_customworldsettings.field_177914_av);
                customworldsettingsfinal_customworldsettings.field_177912_aw = JsonUtils.func_151208_a(jsonobject, "diamondMaxHeight", customworldsettingsfinal_customworldsettings.field_177912_aw);
                customworldsettingsfinal_customworldsettings.field_177910_ax = JsonUtils.func_151208_a(jsonobject, "lapisSize", customworldsettingsfinal_customworldsettings.field_177910_ax);
                customworldsettingsfinal_customworldsettings.field_177897_ay = JsonUtils.func_151208_a(jsonobject, "lapisCount", customworldsettingsfinal_customworldsettings.field_177897_ay);
                customworldsettingsfinal_customworldsettings.field_177895_az = JsonUtils.func_151208_a(jsonobject, "lapisCenterHeight", customworldsettingsfinal_customworldsettings.field_177895_az);
                customworldsettingsfinal_customworldsettings.field_177889_aA = JsonUtils.func_151208_a(jsonobject, "lapisSpread", customworldsettingsfinal_customworldsettings.field_177889_aA);
            } catch (Exception exception) {
                ;
            }

            return customworldsettingsfinal_customworldsettings;
        }

        public JsonElement serialize(ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.addProperty("coordinateScale", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177899_b));
            jsonobject.addProperty("heightScale", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177900_c));
            jsonobject.addProperty("lowerLimitScale", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177898_e));
            jsonobject.addProperty("upperLimitScale", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177896_d));
            jsonobject.addProperty("depthNoiseScaleX", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177893_f));
            jsonobject.addProperty("depthNoiseScaleZ", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177894_g));
            jsonobject.addProperty("depthNoiseScaleExponent", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177915_h));
            jsonobject.addProperty("mainNoiseScaleX", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177917_i));
            jsonobject.addProperty("mainNoiseScaleY", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177911_j));
            jsonobject.addProperty("mainNoiseScaleZ", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177913_k));
            jsonobject.addProperty("baseSize", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177907_l));
            jsonobject.addProperty("stretchY", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177909_m));
            jsonobject.addProperty("biomeDepthWeight", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177903_n));
            jsonobject.addProperty("biomeDepthOffset", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177905_o));
            jsonobject.addProperty("biomeScaleWeight", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177933_p));
            jsonobject.addProperty("biomeScaleOffset", Float.valueOf(customworldsettingsfinal_customworldsettings.field_177931_q));
            jsonobject.addProperty("seaLevel", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177929_r));
            jsonobject.addProperty("useCaves", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177927_s));
            jsonobject.addProperty("useDungeons", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177925_t));
            jsonobject.addProperty("dungeonChance", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177923_u));
            jsonobject.addProperty("useStrongholds", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177921_v));
            jsonobject.addProperty("useVillages", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177919_w));
            jsonobject.addProperty("useMineShafts", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177944_x));
            jsonobject.addProperty("useTemples", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177942_y));
            jsonobject.addProperty("useMonuments", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177940_z));
            jsonobject.addProperty("useMansions", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_191076_A));
            jsonobject.addProperty("useRavines", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177870_A));
            jsonobject.addProperty("useWaterLakes", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177871_B));
            jsonobject.addProperty("waterLakeChance", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177872_C));
            jsonobject.addProperty("useLavaLakes", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177866_D));
            jsonobject.addProperty("lavaLakeChance", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177867_E));
            jsonobject.addProperty("useLavaOceans", Boolean.valueOf(customworldsettingsfinal_customworldsettings.field_177868_F));
            jsonobject.addProperty("fixedBiome", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177869_G));
            jsonobject.addProperty("biomeSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177877_H));
            jsonobject.addProperty("riverSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177878_I));
            jsonobject.addProperty("dirtSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177879_J));
            jsonobject.addProperty("dirtCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177880_K));
            jsonobject.addProperty("dirtMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177873_L));
            jsonobject.addProperty("dirtMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177874_M));
            jsonobject.addProperty("gravelSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177875_N));
            jsonobject.addProperty("gravelCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177876_O));
            jsonobject.addProperty("gravelMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177886_P));
            jsonobject.addProperty("gravelMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177885_Q));
            jsonobject.addProperty("graniteSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177888_R));
            jsonobject.addProperty("graniteCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177887_S));
            jsonobject.addProperty("graniteMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177882_T));
            jsonobject.addProperty("graniteMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177881_U));
            jsonobject.addProperty("dioriteSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177884_V));
            jsonobject.addProperty("dioriteCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177883_W));
            jsonobject.addProperty("dioriteMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177891_X));
            jsonobject.addProperty("dioriteMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177890_Y));
            jsonobject.addProperty("andesiteSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177892_Z));
            jsonobject.addProperty("andesiteCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177936_aa));
            jsonobject.addProperty("andesiteMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177937_ab));
            jsonobject.addProperty("andesiteMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177934_ac));
            jsonobject.addProperty("coalSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177935_ad));
            jsonobject.addProperty("coalCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177941_ae));
            jsonobject.addProperty("coalMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177943_af));
            jsonobject.addProperty("coalMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177938_ag));
            jsonobject.addProperty("ironSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177939_ah));
            jsonobject.addProperty("ironCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177922_ai));
            jsonobject.addProperty("ironMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177924_aj));
            jsonobject.addProperty("ironMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177918_ak));
            jsonobject.addProperty("goldSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177920_al));
            jsonobject.addProperty("goldCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177930_am));
            jsonobject.addProperty("goldMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177932_an));
            jsonobject.addProperty("goldMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177926_ao));
            jsonobject.addProperty("redstoneSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177928_ap));
            jsonobject.addProperty("redstoneCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177908_aq));
            jsonobject.addProperty("redstoneMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177906_ar));
            jsonobject.addProperty("redstoneMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177904_as));
            jsonobject.addProperty("diamondSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177902_at));
            jsonobject.addProperty("diamondCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177916_au));
            jsonobject.addProperty("diamondMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177914_av));
            jsonobject.addProperty("diamondMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177912_aw));
            jsonobject.addProperty("lapisSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177910_ax));
            jsonobject.addProperty("lapisCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177897_ay));
            jsonobject.addProperty("lapisCenterHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177895_az));
            jsonobject.addProperty("lapisSpread", Integer.valueOf(customworldsettingsfinal_customworldsettings.field_177889_aA));
            return jsonobject;
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.deserialize(jsonelement, type, jsondeserializationcontext);
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.serialize((ChunkGeneratorSettings.Factory) object, type, jsonserializationcontext);
        }
    }

    public static class Factory {

        @VisibleForTesting
        static final Gson field_177901_a = (new GsonBuilder()).registerTypeAdapter(ChunkGeneratorSettings.Factory.class, new ChunkGeneratorSettings.Serializer()).create();
        public float field_177899_b = 684.412F;
        public float field_177900_c = 684.412F;
        public float field_177896_d = 512.0F;
        public float field_177898_e = 512.0F;
        public float field_177893_f = 200.0F;
        public float field_177894_g = 200.0F;
        public float field_177915_h = 0.5F;
        public float field_177917_i = 80.0F;
        public float field_177911_j = 160.0F;
        public float field_177913_k = 80.0F;
        public float field_177907_l = 8.5F;
        public float field_177909_m = 12.0F;
        public float field_177903_n = 1.0F;
        public float field_177905_o;
        public float field_177933_p = 1.0F;
        public float field_177931_q;
        public int field_177929_r = 63;
        public boolean field_177927_s = true;
        public boolean field_177925_t = true;
        public int field_177923_u = 8;
        public boolean field_177921_v = true;
        public boolean field_177919_w = true;
        public boolean field_177944_x = true;
        public boolean field_177942_y = true;
        public boolean field_177940_z = true;
        public boolean field_191076_A = true;
        public boolean field_177870_A = true;
        public boolean field_177871_B = true;
        public int field_177872_C = 4;
        public boolean field_177866_D = true;
        public int field_177867_E = 80;
        public boolean field_177868_F;
        public int field_177869_G = -1;
        public int field_177877_H = 4;
        public int field_177878_I = 4;
        public int field_177879_J = 33;
        public int field_177880_K = 10;
        public int field_177873_L;
        public int field_177874_M = 256;
        public int field_177875_N = 33;
        public int field_177876_O = 8;
        public int field_177886_P;
        public int field_177885_Q = 256;
        public int field_177888_R = 33;
        public int field_177887_S = 10;
        public int field_177882_T;
        public int field_177881_U = 80;
        public int field_177884_V = 33;
        public int field_177883_W = 10;
        public int field_177891_X;
        public int field_177890_Y = 80;
        public int field_177892_Z = 33;
        public int field_177936_aa = 10;
        public int field_177937_ab;
        public int field_177934_ac = 80;
        public int field_177935_ad = 17;
        public int field_177941_ae = 20;
        public int field_177943_af;
        public int field_177938_ag = 128;
        public int field_177939_ah = 9;
        public int field_177922_ai = 20;
        public int field_177924_aj;
        public int field_177918_ak = 64;
        public int field_177920_al = 9;
        public int field_177930_am = 2;
        public int field_177932_an;
        public int field_177926_ao = 32;
        public int field_177928_ap = 8;
        public int field_177908_aq = 8;
        public int field_177906_ar;
        public int field_177904_as = 16;
        public int field_177902_at = 8;
        public int field_177916_au = 1;
        public int field_177914_av;
        public int field_177912_aw = 16;
        public int field_177910_ax = 7;
        public int field_177897_ay = 1;
        public int field_177895_az = 16;
        public int field_177889_aA = 16;

        public static ChunkGeneratorSettings.Factory func_177865_a(String s) {
            if (s.isEmpty()) {
                return new ChunkGeneratorSettings.Factory();
            } else {
                try {
                    return (ChunkGeneratorSettings.Factory) JsonUtils.func_188178_a(ChunkGeneratorSettings.Factory.field_177901_a, s, ChunkGeneratorSettings.Factory.class);
                } catch (Exception exception) {
                    return new ChunkGeneratorSettings.Factory();
                }
            }
        }

        public String toString() {
            return ChunkGeneratorSettings.Factory.field_177901_a.toJson(this);
        }

        public Factory() {
            this.func_177863_a();
        }

        public void func_177863_a() {
            this.field_177899_b = 684.412F;
            this.field_177900_c = 684.412F;
            this.field_177896_d = 512.0F;
            this.field_177898_e = 512.0F;
            this.field_177893_f = 200.0F;
            this.field_177894_g = 200.0F;
            this.field_177915_h = 0.5F;
            this.field_177917_i = 80.0F;
            this.field_177911_j = 160.0F;
            this.field_177913_k = 80.0F;
            this.field_177907_l = 8.5F;
            this.field_177909_m = 12.0F;
            this.field_177903_n = 1.0F;
            this.field_177905_o = 0.0F;
            this.field_177933_p = 1.0F;
            this.field_177931_q = 0.0F;
            this.field_177929_r = 63;
            this.field_177927_s = true;
            this.field_177925_t = true;
            this.field_177923_u = 8;
            this.field_177921_v = true;
            this.field_177919_w = true;
            this.field_177944_x = true;
            this.field_177942_y = true;
            this.field_177940_z = true;
            this.field_191076_A = true;
            this.field_177870_A = true;
            this.field_177871_B = true;
            this.field_177872_C = 4;
            this.field_177866_D = true;
            this.field_177867_E = 80;
            this.field_177868_F = false;
            this.field_177869_G = -1;
            this.field_177877_H = 4;
            this.field_177878_I = 4;
            this.field_177879_J = 33;
            this.field_177880_K = 10;
            this.field_177873_L = 0;
            this.field_177874_M = 256;
            this.field_177875_N = 33;
            this.field_177876_O = 8;
            this.field_177886_P = 0;
            this.field_177885_Q = 256;
            this.field_177888_R = 33;
            this.field_177887_S = 10;
            this.field_177882_T = 0;
            this.field_177881_U = 80;
            this.field_177884_V = 33;
            this.field_177883_W = 10;
            this.field_177891_X = 0;
            this.field_177890_Y = 80;
            this.field_177892_Z = 33;
            this.field_177936_aa = 10;
            this.field_177937_ab = 0;
            this.field_177934_ac = 80;
            this.field_177935_ad = 17;
            this.field_177941_ae = 20;
            this.field_177943_af = 0;
            this.field_177938_ag = 128;
            this.field_177939_ah = 9;
            this.field_177922_ai = 20;
            this.field_177924_aj = 0;
            this.field_177918_ak = 64;
            this.field_177920_al = 9;
            this.field_177930_am = 2;
            this.field_177932_an = 0;
            this.field_177926_ao = 32;
            this.field_177928_ap = 8;
            this.field_177908_aq = 8;
            this.field_177906_ar = 0;
            this.field_177904_as = 16;
            this.field_177902_at = 8;
            this.field_177916_au = 1;
            this.field_177914_av = 0;
            this.field_177912_aw = 16;
            this.field_177910_ax = 7;
            this.field_177897_ay = 1;
            this.field_177895_az = 16;
            this.field_177889_aA = 16;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object != null && this.getClass() == object.getClass()) {
                ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings = (ChunkGeneratorSettings.Factory) object;

                return this.field_177936_aa != customworldsettingsfinal_customworldsettings.field_177936_aa ? false : (this.field_177934_ac != customworldsettingsfinal_customworldsettings.field_177934_ac ? false : (this.field_177937_ab != customworldsettingsfinal_customworldsettings.field_177937_ab ? false : (this.field_177892_Z != customworldsettingsfinal_customworldsettings.field_177892_Z ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177907_l, this.field_177907_l) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177905_o, this.field_177905_o) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177903_n, this.field_177903_n) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177931_q, this.field_177931_q) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177933_p, this.field_177933_p) != 0 ? false : (this.field_177877_H != customworldsettingsfinal_customworldsettings.field_177877_H ? false : (this.field_177941_ae != customworldsettingsfinal_customworldsettings.field_177941_ae ? false : (this.field_177938_ag != customworldsettingsfinal_customworldsettings.field_177938_ag ? false : (this.field_177943_af != customworldsettingsfinal_customworldsettings.field_177943_af ? false : (this.field_177935_ad != customworldsettingsfinal_customworldsettings.field_177935_ad ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177899_b, this.field_177899_b) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177915_h, this.field_177915_h) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177893_f, this.field_177893_f) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177894_g, this.field_177894_g) != 0 ? false : (this.field_177916_au != customworldsettingsfinal_customworldsettings.field_177916_au ? false : (this.field_177912_aw != customworldsettingsfinal_customworldsettings.field_177912_aw ? false : (this.field_177914_av != customworldsettingsfinal_customworldsettings.field_177914_av ? false : (this.field_177902_at != customworldsettingsfinal_customworldsettings.field_177902_at ? false : (this.field_177883_W != customworldsettingsfinal_customworldsettings.field_177883_W ? false : (this.field_177890_Y != customworldsettingsfinal_customworldsettings.field_177890_Y ? false : (this.field_177891_X != customworldsettingsfinal_customworldsettings.field_177891_X ? false : (this.field_177884_V != customworldsettingsfinal_customworldsettings.field_177884_V ? false : (this.field_177880_K != customworldsettingsfinal_customworldsettings.field_177880_K ? false : (this.field_177874_M != customworldsettingsfinal_customworldsettings.field_177874_M ? false : (this.field_177873_L != customworldsettingsfinal_customworldsettings.field_177873_L ? false : (this.field_177879_J != customworldsettingsfinal_customworldsettings.field_177879_J ? false : (this.field_177923_u != customworldsettingsfinal_customworldsettings.field_177923_u ? false : (this.field_177869_G != customworldsettingsfinal_customworldsettings.field_177869_G ? false : (this.field_177930_am != customworldsettingsfinal_customworldsettings.field_177930_am ? false : (this.field_177926_ao != customworldsettingsfinal_customworldsettings.field_177926_ao ? false : (this.field_177932_an != customworldsettingsfinal_customworldsettings.field_177932_an ? false : (this.field_177920_al != customworldsettingsfinal_customworldsettings.field_177920_al ? false : (this.field_177887_S != customworldsettingsfinal_customworldsettings.field_177887_S ? false : (this.field_177881_U != customworldsettingsfinal_customworldsettings.field_177881_U ? false : (this.field_177882_T != customworldsettingsfinal_customworldsettings.field_177882_T ? false : (this.field_177888_R != customworldsettingsfinal_customworldsettings.field_177888_R ? false : (this.field_177876_O != customworldsettingsfinal_customworldsettings.field_177876_O ? false : (this.field_177885_Q != customworldsettingsfinal_customworldsettings.field_177885_Q ? false : (this.field_177886_P != customworldsettingsfinal_customworldsettings.field_177886_P ? false : (this.field_177875_N != customworldsettingsfinal_customworldsettings.field_177875_N ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177900_c, this.field_177900_c) != 0 ? false : (this.field_177922_ai != customworldsettingsfinal_customworldsettings.field_177922_ai ? false : (this.field_177918_ak != customworldsettingsfinal_customworldsettings.field_177918_ak ? false : (this.field_177924_aj != customworldsettingsfinal_customworldsettings.field_177924_aj ? false : (this.field_177939_ah != customworldsettingsfinal_customworldsettings.field_177939_ah ? false : (this.field_177895_az != customworldsettingsfinal_customworldsettings.field_177895_az ? false : (this.field_177897_ay != customworldsettingsfinal_customworldsettings.field_177897_ay ? false : (this.field_177910_ax != customworldsettingsfinal_customworldsettings.field_177910_ax ? false : (this.field_177889_aA != customworldsettingsfinal_customworldsettings.field_177889_aA ? false : (this.field_177867_E != customworldsettingsfinal_customworldsettings.field_177867_E ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177898_e, this.field_177898_e) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177917_i, this.field_177917_i) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177911_j, this.field_177911_j) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177913_k, this.field_177913_k) != 0 ? false : (this.field_177908_aq != customworldsettingsfinal_customworldsettings.field_177908_aq ? false : (this.field_177904_as != customworldsettingsfinal_customworldsettings.field_177904_as ? false : (this.field_177906_ar != customworldsettingsfinal_customworldsettings.field_177906_ar ? false : (this.field_177928_ap != customworldsettingsfinal_customworldsettings.field_177928_ap ? false : (this.field_177878_I != customworldsettingsfinal_customworldsettings.field_177878_I ? false : (this.field_177929_r != customworldsettingsfinal_customworldsettings.field_177929_r ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177909_m, this.field_177909_m) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.field_177896_d, this.field_177896_d) != 0 ? false : (this.field_177927_s != customworldsettingsfinal_customworldsettings.field_177927_s ? false : (this.field_177925_t != customworldsettingsfinal_customworldsettings.field_177925_t ? false : (this.field_177866_D != customworldsettingsfinal_customworldsettings.field_177866_D ? false : (this.field_177868_F != customworldsettingsfinal_customworldsettings.field_177868_F ? false : (this.field_177944_x != customworldsettingsfinal_customworldsettings.field_177944_x ? false : (this.field_177870_A != customworldsettingsfinal_customworldsettings.field_177870_A ? false : (this.field_177921_v != customworldsettingsfinal_customworldsettings.field_177921_v ? false : (this.field_177942_y != customworldsettingsfinal_customworldsettings.field_177942_y ? false : (this.field_177940_z != customworldsettingsfinal_customworldsettings.field_177940_z ? false : (this.field_191076_A != customworldsettingsfinal_customworldsettings.field_191076_A ? false : (this.field_177919_w != customworldsettingsfinal_customworldsettings.field_177919_w ? false : (this.field_177871_B != customworldsettingsfinal_customworldsettings.field_177871_B ? false : this.field_177872_C == customworldsettingsfinal_customworldsettings.field_177872_C)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))));
            } else {
                return false;
            }
        }

        public int hashCode() {
            int i = this.field_177899_b == 0.0F ? 0 : Float.floatToIntBits(this.field_177899_b);

            i = 31 * i + (this.field_177900_c == 0.0F ? 0 : Float.floatToIntBits(this.field_177900_c));
            i = 31 * i + (this.field_177896_d == 0.0F ? 0 : Float.floatToIntBits(this.field_177896_d));
            i = 31 * i + (this.field_177898_e == 0.0F ? 0 : Float.floatToIntBits(this.field_177898_e));
            i = 31 * i + (this.field_177893_f == 0.0F ? 0 : Float.floatToIntBits(this.field_177893_f));
            i = 31 * i + (this.field_177894_g == 0.0F ? 0 : Float.floatToIntBits(this.field_177894_g));
            i = 31 * i + (this.field_177915_h == 0.0F ? 0 : Float.floatToIntBits(this.field_177915_h));
            i = 31 * i + (this.field_177917_i == 0.0F ? 0 : Float.floatToIntBits(this.field_177917_i));
            i = 31 * i + (this.field_177911_j == 0.0F ? 0 : Float.floatToIntBits(this.field_177911_j));
            i = 31 * i + (this.field_177913_k == 0.0F ? 0 : Float.floatToIntBits(this.field_177913_k));
            i = 31 * i + (this.field_177907_l == 0.0F ? 0 : Float.floatToIntBits(this.field_177907_l));
            i = 31 * i + (this.field_177909_m == 0.0F ? 0 : Float.floatToIntBits(this.field_177909_m));
            i = 31 * i + (this.field_177903_n == 0.0F ? 0 : Float.floatToIntBits(this.field_177903_n));
            i = 31 * i + (this.field_177905_o == 0.0F ? 0 : Float.floatToIntBits(this.field_177905_o));
            i = 31 * i + (this.field_177933_p == 0.0F ? 0 : Float.floatToIntBits(this.field_177933_p));
            i = 31 * i + (this.field_177931_q == 0.0F ? 0 : Float.floatToIntBits(this.field_177931_q));
            i = 31 * i + this.field_177929_r;
            i = 31 * i + (this.field_177927_s ? 1 : 0);
            i = 31 * i + (this.field_177925_t ? 1 : 0);
            i = 31 * i + this.field_177923_u;
            i = 31 * i + (this.field_177921_v ? 1 : 0);
            i = 31 * i + (this.field_177919_w ? 1 : 0);
            i = 31 * i + (this.field_177944_x ? 1 : 0);
            i = 31 * i + (this.field_177942_y ? 1 : 0);
            i = 31 * i + (this.field_177940_z ? 1 : 0);
            i = 31 * i + (this.field_191076_A ? 1 : 0);
            i = 31 * i + (this.field_177870_A ? 1 : 0);
            i = 31 * i + (this.field_177871_B ? 1 : 0);
            i = 31 * i + this.field_177872_C;
            i = 31 * i + (this.field_177866_D ? 1 : 0);
            i = 31 * i + this.field_177867_E;
            i = 31 * i + (this.field_177868_F ? 1 : 0);
            i = 31 * i + this.field_177869_G;
            i = 31 * i + this.field_177877_H;
            i = 31 * i + this.field_177878_I;
            i = 31 * i + this.field_177879_J;
            i = 31 * i + this.field_177880_K;
            i = 31 * i + this.field_177873_L;
            i = 31 * i + this.field_177874_M;
            i = 31 * i + this.field_177875_N;
            i = 31 * i + this.field_177876_O;
            i = 31 * i + this.field_177886_P;
            i = 31 * i + this.field_177885_Q;
            i = 31 * i + this.field_177888_R;
            i = 31 * i + this.field_177887_S;
            i = 31 * i + this.field_177882_T;
            i = 31 * i + this.field_177881_U;
            i = 31 * i + this.field_177884_V;
            i = 31 * i + this.field_177883_W;
            i = 31 * i + this.field_177891_X;
            i = 31 * i + this.field_177890_Y;
            i = 31 * i + this.field_177892_Z;
            i = 31 * i + this.field_177936_aa;
            i = 31 * i + this.field_177937_ab;
            i = 31 * i + this.field_177934_ac;
            i = 31 * i + this.field_177935_ad;
            i = 31 * i + this.field_177941_ae;
            i = 31 * i + this.field_177943_af;
            i = 31 * i + this.field_177938_ag;
            i = 31 * i + this.field_177939_ah;
            i = 31 * i + this.field_177922_ai;
            i = 31 * i + this.field_177924_aj;
            i = 31 * i + this.field_177918_ak;
            i = 31 * i + this.field_177920_al;
            i = 31 * i + this.field_177930_am;
            i = 31 * i + this.field_177932_an;
            i = 31 * i + this.field_177926_ao;
            i = 31 * i + this.field_177928_ap;
            i = 31 * i + this.field_177908_aq;
            i = 31 * i + this.field_177906_ar;
            i = 31 * i + this.field_177904_as;
            i = 31 * i + this.field_177902_at;
            i = 31 * i + this.field_177916_au;
            i = 31 * i + this.field_177914_av;
            i = 31 * i + this.field_177912_aw;
            i = 31 * i + this.field_177910_ax;
            i = 31 * i + this.field_177897_ay;
            i = 31 * i + this.field_177895_az;
            i = 31 * i + this.field_177889_aA;
            return i;
        }

        public ChunkGeneratorSettings func_177864_b() {
            return new ChunkGeneratorSettings(this, null);
        }
    }
}
