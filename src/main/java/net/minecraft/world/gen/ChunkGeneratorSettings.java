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

    public final float coordinateScale;
    public final float heightScale;
    public final float upperLimitScale;
    public final float lowerLimitScale;
    public final float depthNoiseScaleX;
    public final float depthNoiseScaleZ;
    public final float depthNoiseScaleExponent;
    public final float mainNoiseScaleX;
    public final float mainNoiseScaleY;
    public final float mainNoiseScaleZ;
    public final float baseSize;
    public final float stretchY;
    public final float biomeDepthWeight;
    public final float biomeDepthOffSet;
    public final float biomeScaleWeight;
    public final float biomeScaleOffset;
    public final int seaLevel;
    public final boolean useCaves;
    public final boolean useDungeons;
    public final int dungeonChance;
    public final boolean useStrongholds;
    public final boolean useVillages;
    public final boolean useMineShafts;
    public final boolean useTemples;
    public final boolean useMonuments;
    public final boolean useMansions;
    public final boolean useRavines;
    public final boolean useWaterLakes;
    public final int waterLakeChance;
    public final boolean useLavaLakes;
    public final int lavaLakeChance;
    public final boolean useLavaOceans;
    public final int fixedBiome;
    public final int biomeSize;
    public final int riverSize;
    public final int dirtSize;
    public final int dirtCount;
    public final int dirtMinHeight;
    public final int dirtMaxHeight;
    public final int gravelSize;
    public final int gravelCount;
    public final int gravelMinHeight;
    public final int gravelMaxHeight;
    public final int graniteSize;
    public final int graniteCount;
    public final int graniteMinHeight;
    public final int graniteMaxHeight;
    public final int dioriteSize;
    public final int dioriteCount;
    public final int dioriteMinHeight;
    public final int dioriteMaxHeight;
    public final int andesiteSize;
    public final int andesiteCount;
    public final int andesiteMinHeight;
    public final int andesiteMaxHeight;
    public final int coalSize;
    public final int coalCount;
    public final int coalMinHeight;
    public final int coalMaxHeight;
    public final int ironSize;
    public final int ironCount;
    public final int ironMinHeight;
    public final int ironMaxHeight;
    public final int goldSize;
    public final int goldCount;
    public final int goldMinHeight;
    public final int goldMaxHeight;
    public final int redstoneSize;
    public final int redstoneCount;
    public final int redstoneMinHeight;
    public final int redstoneMaxHeight;
    public final int diamondSize;
    public final int diamondCount;
    public final int diamondMinHeight;
    public final int diamondMaxHeight;
    public final int lapisSize;
    public final int lapisCount;
    public final int lapisCenterHeight;
    public final int lapisSpread;

    private ChunkGeneratorSettings(ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings) {
        this.coordinateScale = customworldsettingsfinal_customworldsettings.coordinateScale;
        this.heightScale = customworldsettingsfinal_customworldsettings.heightScale;
        this.upperLimitScale = customworldsettingsfinal_customworldsettings.upperLimitScale;
        this.lowerLimitScale = customworldsettingsfinal_customworldsettings.lowerLimitScale;
        this.depthNoiseScaleX = customworldsettingsfinal_customworldsettings.depthNoiseScaleX;
        this.depthNoiseScaleZ = customworldsettingsfinal_customworldsettings.depthNoiseScaleZ;
        this.depthNoiseScaleExponent = customworldsettingsfinal_customworldsettings.depthNoiseScaleExponent;
        this.mainNoiseScaleX = customworldsettingsfinal_customworldsettings.mainNoiseScaleX;
        this.mainNoiseScaleY = customworldsettingsfinal_customworldsettings.mainNoiseScaleY;
        this.mainNoiseScaleZ = customworldsettingsfinal_customworldsettings.mainNoiseScaleZ;
        this.baseSize = customworldsettingsfinal_customworldsettings.baseSize;
        this.stretchY = customworldsettingsfinal_customworldsettings.stretchY;
        this.biomeDepthWeight = customworldsettingsfinal_customworldsettings.biomeDepthWeight;
        this.biomeDepthOffSet = customworldsettingsfinal_customworldsettings.biomeDepthOffset;
        this.biomeScaleWeight = customworldsettingsfinal_customworldsettings.biomeScaleWeight;
        this.biomeScaleOffset = customworldsettingsfinal_customworldsettings.biomeScaleOffset;
        this.seaLevel = customworldsettingsfinal_customworldsettings.seaLevel;
        this.useCaves = customworldsettingsfinal_customworldsettings.useCaves;
        this.useDungeons = customworldsettingsfinal_customworldsettings.useDungeons;
        this.dungeonChance = customworldsettingsfinal_customworldsettings.dungeonChance;
        this.useStrongholds = customworldsettingsfinal_customworldsettings.useStrongholds;
        this.useVillages = customworldsettingsfinal_customworldsettings.useVillages;
        this.useMineShafts = customworldsettingsfinal_customworldsettings.useMineShafts;
        this.useTemples = customworldsettingsfinal_customworldsettings.useTemples;
        this.useMonuments = customworldsettingsfinal_customworldsettings.useMonuments;
        this.useMansions = customworldsettingsfinal_customworldsettings.useMansions;
        this.useRavines = customworldsettingsfinal_customworldsettings.useRavines;
        this.useWaterLakes = customworldsettingsfinal_customworldsettings.useWaterLakes;
        this.waterLakeChance = customworldsettingsfinal_customworldsettings.waterLakeChance;
        this.useLavaLakes = customworldsettingsfinal_customworldsettings.useLavaLakes;
        this.lavaLakeChance = customworldsettingsfinal_customworldsettings.lavaLakeChance;
        this.useLavaOceans = customworldsettingsfinal_customworldsettings.useLavaOceans;
        this.fixedBiome = customworldsettingsfinal_customworldsettings.fixedBiome;
        this.biomeSize = customworldsettingsfinal_customworldsettings.biomeSize;
        this.riverSize = customworldsettingsfinal_customworldsettings.riverSize;
        this.dirtSize = customworldsettingsfinal_customworldsettings.dirtSize;
        this.dirtCount = customworldsettingsfinal_customworldsettings.dirtCount;
        this.dirtMinHeight = customworldsettingsfinal_customworldsettings.dirtMinHeight;
        this.dirtMaxHeight = customworldsettingsfinal_customworldsettings.dirtMaxHeight;
        this.gravelSize = customworldsettingsfinal_customworldsettings.gravelSize;
        this.gravelCount = customworldsettingsfinal_customworldsettings.gravelCount;
        this.gravelMinHeight = customworldsettingsfinal_customworldsettings.gravelMinHeight;
        this.gravelMaxHeight = customworldsettingsfinal_customworldsettings.gravelMaxHeight;
        this.graniteSize = customworldsettingsfinal_customworldsettings.graniteSize;
        this.graniteCount = customworldsettingsfinal_customworldsettings.graniteCount;
        this.graniteMinHeight = customworldsettingsfinal_customworldsettings.graniteMinHeight;
        this.graniteMaxHeight = customworldsettingsfinal_customworldsettings.graniteMaxHeight;
        this.dioriteSize = customworldsettingsfinal_customworldsettings.dioriteSize;
        this.dioriteCount = customworldsettingsfinal_customworldsettings.dioriteCount;
        this.dioriteMinHeight = customworldsettingsfinal_customworldsettings.dioriteMinHeight;
        this.dioriteMaxHeight = customworldsettingsfinal_customworldsettings.dioriteMaxHeight;
        this.andesiteSize = customworldsettingsfinal_customworldsettings.andesiteSize;
        this.andesiteCount = customworldsettingsfinal_customworldsettings.andesiteCount;
        this.andesiteMinHeight = customworldsettingsfinal_customworldsettings.andesiteMinHeight;
        this.andesiteMaxHeight = customworldsettingsfinal_customworldsettings.andesiteMaxHeight;
        this.coalSize = customworldsettingsfinal_customworldsettings.coalSize;
        this.coalCount = customworldsettingsfinal_customworldsettings.coalCount;
        this.coalMinHeight = customworldsettingsfinal_customworldsettings.coalMinHeight;
        this.coalMaxHeight = customworldsettingsfinal_customworldsettings.coalMaxHeight;
        this.ironSize = customworldsettingsfinal_customworldsettings.ironSize;
        this.ironCount = customworldsettingsfinal_customworldsettings.ironCount;
        this.ironMinHeight = customworldsettingsfinal_customworldsettings.ironMinHeight;
        this.ironMaxHeight = customworldsettingsfinal_customworldsettings.ironMaxHeight;
        this.goldSize = customworldsettingsfinal_customworldsettings.goldSize;
        this.goldCount = customworldsettingsfinal_customworldsettings.goldCount;
        this.goldMinHeight = customworldsettingsfinal_customworldsettings.goldMinHeight;
        this.goldMaxHeight = customworldsettingsfinal_customworldsettings.goldMaxHeight;
        this.redstoneSize = customworldsettingsfinal_customworldsettings.redstoneSize;
        this.redstoneCount = customworldsettingsfinal_customworldsettings.redstoneCount;
        this.redstoneMinHeight = customworldsettingsfinal_customworldsettings.redstoneMinHeight;
        this.redstoneMaxHeight = customworldsettingsfinal_customworldsettings.redstoneMaxHeight;
        this.diamondSize = customworldsettingsfinal_customworldsettings.diamondSize;
        this.diamondCount = customworldsettingsfinal_customworldsettings.diamondCount;
        this.diamondMinHeight = customworldsettingsfinal_customworldsettings.diamondMinHeight;
        this.diamondMaxHeight = customworldsettingsfinal_customworldsettings.diamondMaxHeight;
        this.lapisSize = customworldsettingsfinal_customworldsettings.lapisSize;
        this.lapisCount = customworldsettingsfinal_customworldsettings.lapisCount;
        this.lapisCenterHeight = customworldsettingsfinal_customworldsettings.lapisCenterHeight;
        this.lapisSpread = customworldsettingsfinal_customworldsettings.lapisSpread;
    }

    ChunkGeneratorSettings(ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings, Object object) {
        this(customworldsettingsfinal_customworldsettings);
    }

    public static class Serializer implements JsonDeserializer<ChunkGeneratorSettings.Factory>, JsonSerializer<ChunkGeneratorSettings.Factory> {

        public Serializer() {}

        @Override
        public ChunkGeneratorSettings.Factory deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings = new ChunkGeneratorSettings.Factory();

            try {
                customworldsettingsfinal_customworldsettings.coordinateScale = JsonUtils.getFloat(jsonobject, "coordinateScale", customworldsettingsfinal_customworldsettings.coordinateScale);
                customworldsettingsfinal_customworldsettings.heightScale = JsonUtils.getFloat(jsonobject, "heightScale", customworldsettingsfinal_customworldsettings.heightScale);
                customworldsettingsfinal_customworldsettings.lowerLimitScale = JsonUtils.getFloat(jsonobject, "lowerLimitScale", customworldsettingsfinal_customworldsettings.lowerLimitScale);
                customworldsettingsfinal_customworldsettings.upperLimitScale = JsonUtils.getFloat(jsonobject, "upperLimitScale", customworldsettingsfinal_customworldsettings.upperLimitScale);
                customworldsettingsfinal_customworldsettings.depthNoiseScaleX = JsonUtils.getFloat(jsonobject, "depthNoiseScaleX", customworldsettingsfinal_customworldsettings.depthNoiseScaleX);
                customworldsettingsfinal_customworldsettings.depthNoiseScaleZ = JsonUtils.getFloat(jsonobject, "depthNoiseScaleZ", customworldsettingsfinal_customworldsettings.depthNoiseScaleZ);
                customworldsettingsfinal_customworldsettings.depthNoiseScaleExponent = JsonUtils.getFloat(jsonobject, "depthNoiseScaleExponent", customworldsettingsfinal_customworldsettings.depthNoiseScaleExponent);
                customworldsettingsfinal_customworldsettings.mainNoiseScaleX = JsonUtils.getFloat(jsonobject, "mainNoiseScaleX", customworldsettingsfinal_customworldsettings.mainNoiseScaleX);
                customworldsettingsfinal_customworldsettings.mainNoiseScaleY = JsonUtils.getFloat(jsonobject, "mainNoiseScaleY", customworldsettingsfinal_customworldsettings.mainNoiseScaleY);
                customworldsettingsfinal_customworldsettings.mainNoiseScaleZ = JsonUtils.getFloat(jsonobject, "mainNoiseScaleZ", customworldsettingsfinal_customworldsettings.mainNoiseScaleZ);
                customworldsettingsfinal_customworldsettings.baseSize = JsonUtils.getFloat(jsonobject, "baseSize", customworldsettingsfinal_customworldsettings.baseSize);
                customworldsettingsfinal_customworldsettings.stretchY = JsonUtils.getFloat(jsonobject, "stretchY", customworldsettingsfinal_customworldsettings.stretchY);
                customworldsettingsfinal_customworldsettings.biomeDepthWeight = JsonUtils.getFloat(jsonobject, "biomeDepthWeight", customworldsettingsfinal_customworldsettings.biomeDepthWeight);
                customworldsettingsfinal_customworldsettings.biomeDepthOffset = JsonUtils.getFloat(jsonobject, "biomeDepthOffset", customworldsettingsfinal_customworldsettings.biomeDepthOffset);
                customworldsettingsfinal_customworldsettings.biomeScaleWeight = JsonUtils.getFloat(jsonobject, "biomeScaleWeight", customworldsettingsfinal_customworldsettings.biomeScaleWeight);
                customworldsettingsfinal_customworldsettings.biomeScaleOffset = JsonUtils.getFloat(jsonobject, "biomeScaleOffset", customworldsettingsfinal_customworldsettings.biomeScaleOffset);
                customworldsettingsfinal_customworldsettings.seaLevel = JsonUtils.getInt(jsonobject, "seaLevel", customworldsettingsfinal_customworldsettings.seaLevel);
                customworldsettingsfinal_customworldsettings.useCaves = JsonUtils.getBoolean(jsonobject, "useCaves", customworldsettingsfinal_customworldsettings.useCaves);
                customworldsettingsfinal_customworldsettings.useDungeons = JsonUtils.getBoolean(jsonobject, "useDungeons", customworldsettingsfinal_customworldsettings.useDungeons);
                customworldsettingsfinal_customworldsettings.dungeonChance = JsonUtils.getInt(jsonobject, "dungeonChance", customworldsettingsfinal_customworldsettings.dungeonChance);
                customworldsettingsfinal_customworldsettings.useStrongholds = JsonUtils.getBoolean(jsonobject, "useStrongholds", customworldsettingsfinal_customworldsettings.useStrongholds);
                customworldsettingsfinal_customworldsettings.useVillages = JsonUtils.getBoolean(jsonobject, "useVillages", customworldsettingsfinal_customworldsettings.useVillages);
                customworldsettingsfinal_customworldsettings.useMineShafts = JsonUtils.getBoolean(jsonobject, "useMineShafts", customworldsettingsfinal_customworldsettings.useMineShafts);
                customworldsettingsfinal_customworldsettings.useTemples = JsonUtils.getBoolean(jsonobject, "useTemples", customworldsettingsfinal_customworldsettings.useTemples);
                customworldsettingsfinal_customworldsettings.useMonuments = JsonUtils.getBoolean(jsonobject, "useMonuments", customworldsettingsfinal_customworldsettings.useMonuments);
                customworldsettingsfinal_customworldsettings.useMansions = JsonUtils.getBoolean(jsonobject, "useMansions", customworldsettingsfinal_customworldsettings.useMansions);
                customworldsettingsfinal_customworldsettings.useRavines = JsonUtils.getBoolean(jsonobject, "useRavines", customworldsettingsfinal_customworldsettings.useRavines);
                customworldsettingsfinal_customworldsettings.useWaterLakes = JsonUtils.getBoolean(jsonobject, "useWaterLakes", customworldsettingsfinal_customworldsettings.useWaterLakes);
                customworldsettingsfinal_customworldsettings.waterLakeChance = JsonUtils.getInt(jsonobject, "waterLakeChance", customworldsettingsfinal_customworldsettings.waterLakeChance);
                customworldsettingsfinal_customworldsettings.useLavaLakes = JsonUtils.getBoolean(jsonobject, "useLavaLakes", customworldsettingsfinal_customworldsettings.useLavaLakes);
                customworldsettingsfinal_customworldsettings.lavaLakeChance = JsonUtils.getInt(jsonobject, "lavaLakeChance", customworldsettingsfinal_customworldsettings.lavaLakeChance);
                customworldsettingsfinal_customworldsettings.useLavaOceans = JsonUtils.getBoolean(jsonobject, "useLavaOceans", customworldsettingsfinal_customworldsettings.useLavaOceans);
                customworldsettingsfinal_customworldsettings.fixedBiome = JsonUtils.getInt(jsonobject, "fixedBiome", customworldsettingsfinal_customworldsettings.fixedBiome);
                if (customworldsettingsfinal_customworldsettings.fixedBiome < 38 && customworldsettingsfinal_customworldsettings.fixedBiome >= -1) {
                    if (customworldsettingsfinal_customworldsettings.fixedBiome >= Biome.getIdForBiome(Biomes.HELL)) {
                        customworldsettingsfinal_customworldsettings.fixedBiome += 2;
                    }
                } else {
                    customworldsettingsfinal_customworldsettings.fixedBiome = -1;
                }

                customworldsettingsfinal_customworldsettings.biomeSize = JsonUtils.getInt(jsonobject, "biomeSize", customworldsettingsfinal_customworldsettings.biomeSize);
                customworldsettingsfinal_customworldsettings.riverSize = JsonUtils.getInt(jsonobject, "riverSize", customworldsettingsfinal_customworldsettings.riverSize);
                customworldsettingsfinal_customworldsettings.dirtSize = JsonUtils.getInt(jsonobject, "dirtSize", customworldsettingsfinal_customworldsettings.dirtSize);
                customworldsettingsfinal_customworldsettings.dirtCount = JsonUtils.getInt(jsonobject, "dirtCount", customworldsettingsfinal_customworldsettings.dirtCount);
                customworldsettingsfinal_customworldsettings.dirtMinHeight = JsonUtils.getInt(jsonobject, "dirtMinHeight", customworldsettingsfinal_customworldsettings.dirtMinHeight);
                customworldsettingsfinal_customworldsettings.dirtMaxHeight = JsonUtils.getInt(jsonobject, "dirtMaxHeight", customworldsettingsfinal_customworldsettings.dirtMaxHeight);
                customworldsettingsfinal_customworldsettings.gravelSize = JsonUtils.getInt(jsonobject, "gravelSize", customworldsettingsfinal_customworldsettings.gravelSize);
                customworldsettingsfinal_customworldsettings.gravelCount = JsonUtils.getInt(jsonobject, "gravelCount", customworldsettingsfinal_customworldsettings.gravelCount);
                customworldsettingsfinal_customworldsettings.gravelMinHeight = JsonUtils.getInt(jsonobject, "gravelMinHeight", customworldsettingsfinal_customworldsettings.gravelMinHeight);
                customworldsettingsfinal_customworldsettings.gravelMaxHeight = JsonUtils.getInt(jsonobject, "gravelMaxHeight", customworldsettingsfinal_customworldsettings.gravelMaxHeight);
                customworldsettingsfinal_customworldsettings.graniteSize = JsonUtils.getInt(jsonobject, "graniteSize", customworldsettingsfinal_customworldsettings.graniteSize);
                customworldsettingsfinal_customworldsettings.graniteCount = JsonUtils.getInt(jsonobject, "graniteCount", customworldsettingsfinal_customworldsettings.graniteCount);
                customworldsettingsfinal_customworldsettings.graniteMinHeight = JsonUtils.getInt(jsonobject, "graniteMinHeight", customworldsettingsfinal_customworldsettings.graniteMinHeight);
                customworldsettingsfinal_customworldsettings.graniteMaxHeight = JsonUtils.getInt(jsonobject, "graniteMaxHeight", customworldsettingsfinal_customworldsettings.graniteMaxHeight);
                customworldsettingsfinal_customworldsettings.dioriteSize = JsonUtils.getInt(jsonobject, "dioriteSize", customworldsettingsfinal_customworldsettings.dioriteSize);
                customworldsettingsfinal_customworldsettings.dioriteCount = JsonUtils.getInt(jsonobject, "dioriteCount", customworldsettingsfinal_customworldsettings.dioriteCount);
                customworldsettingsfinal_customworldsettings.dioriteMinHeight = JsonUtils.getInt(jsonobject, "dioriteMinHeight", customworldsettingsfinal_customworldsettings.dioriteMinHeight);
                customworldsettingsfinal_customworldsettings.dioriteMaxHeight = JsonUtils.getInt(jsonobject, "dioriteMaxHeight", customworldsettingsfinal_customworldsettings.dioriteMaxHeight);
                customworldsettingsfinal_customworldsettings.andesiteSize = JsonUtils.getInt(jsonobject, "andesiteSize", customworldsettingsfinal_customworldsettings.andesiteSize);
                customworldsettingsfinal_customworldsettings.andesiteCount = JsonUtils.getInt(jsonobject, "andesiteCount", customworldsettingsfinal_customworldsettings.andesiteCount);
                customworldsettingsfinal_customworldsettings.andesiteMinHeight = JsonUtils.getInt(jsonobject, "andesiteMinHeight", customworldsettingsfinal_customworldsettings.andesiteMinHeight);
                customworldsettingsfinal_customworldsettings.andesiteMaxHeight = JsonUtils.getInt(jsonobject, "andesiteMaxHeight", customworldsettingsfinal_customworldsettings.andesiteMaxHeight);
                customworldsettingsfinal_customworldsettings.coalSize = JsonUtils.getInt(jsonobject, "coalSize", customworldsettingsfinal_customworldsettings.coalSize);
                customworldsettingsfinal_customworldsettings.coalCount = JsonUtils.getInt(jsonobject, "coalCount", customworldsettingsfinal_customworldsettings.coalCount);
                customworldsettingsfinal_customworldsettings.coalMinHeight = JsonUtils.getInt(jsonobject, "coalMinHeight", customworldsettingsfinal_customworldsettings.coalMinHeight);
                customworldsettingsfinal_customworldsettings.coalMaxHeight = JsonUtils.getInt(jsonobject, "coalMaxHeight", customworldsettingsfinal_customworldsettings.coalMaxHeight);
                customworldsettingsfinal_customworldsettings.ironSize = JsonUtils.getInt(jsonobject, "ironSize", customworldsettingsfinal_customworldsettings.ironSize);
                customworldsettingsfinal_customworldsettings.ironCount = JsonUtils.getInt(jsonobject, "ironCount", customworldsettingsfinal_customworldsettings.ironCount);
                customworldsettingsfinal_customworldsettings.ironMinHeight = JsonUtils.getInt(jsonobject, "ironMinHeight", customworldsettingsfinal_customworldsettings.ironMinHeight);
                customworldsettingsfinal_customworldsettings.ironMaxHeight = JsonUtils.getInt(jsonobject, "ironMaxHeight", customworldsettingsfinal_customworldsettings.ironMaxHeight);
                customworldsettingsfinal_customworldsettings.goldSize = JsonUtils.getInt(jsonobject, "goldSize", customworldsettingsfinal_customworldsettings.goldSize);
                customworldsettingsfinal_customworldsettings.goldCount = JsonUtils.getInt(jsonobject, "goldCount", customworldsettingsfinal_customworldsettings.goldCount);
                customworldsettingsfinal_customworldsettings.goldMinHeight = JsonUtils.getInt(jsonobject, "goldMinHeight", customworldsettingsfinal_customworldsettings.goldMinHeight);
                customworldsettingsfinal_customworldsettings.goldMaxHeight = JsonUtils.getInt(jsonobject, "goldMaxHeight", customworldsettingsfinal_customworldsettings.goldMaxHeight);
                customworldsettingsfinal_customworldsettings.redstoneSize = JsonUtils.getInt(jsonobject, "redstoneSize", customworldsettingsfinal_customworldsettings.redstoneSize);
                customworldsettingsfinal_customworldsettings.redstoneCount = JsonUtils.getInt(jsonobject, "redstoneCount", customworldsettingsfinal_customworldsettings.redstoneCount);
                customworldsettingsfinal_customworldsettings.redstoneMinHeight = JsonUtils.getInt(jsonobject, "redstoneMinHeight", customworldsettingsfinal_customworldsettings.redstoneMinHeight);
                customworldsettingsfinal_customworldsettings.redstoneMaxHeight = JsonUtils.getInt(jsonobject, "redstoneMaxHeight", customworldsettingsfinal_customworldsettings.redstoneMaxHeight);
                customworldsettingsfinal_customworldsettings.diamondSize = JsonUtils.getInt(jsonobject, "diamondSize", customworldsettingsfinal_customworldsettings.diamondSize);
                customworldsettingsfinal_customworldsettings.diamondCount = JsonUtils.getInt(jsonobject, "diamondCount", customworldsettingsfinal_customworldsettings.diamondCount);
                customworldsettingsfinal_customworldsettings.diamondMinHeight = JsonUtils.getInt(jsonobject, "diamondMinHeight", customworldsettingsfinal_customworldsettings.diamondMinHeight);
                customworldsettingsfinal_customworldsettings.diamondMaxHeight = JsonUtils.getInt(jsonobject, "diamondMaxHeight", customworldsettingsfinal_customworldsettings.diamondMaxHeight);
                customworldsettingsfinal_customworldsettings.lapisSize = JsonUtils.getInt(jsonobject, "lapisSize", customworldsettingsfinal_customworldsettings.lapisSize);
                customworldsettingsfinal_customworldsettings.lapisCount = JsonUtils.getInt(jsonobject, "lapisCount", customworldsettingsfinal_customworldsettings.lapisCount);
                customworldsettingsfinal_customworldsettings.lapisCenterHeight = JsonUtils.getInt(jsonobject, "lapisCenterHeight", customworldsettingsfinal_customworldsettings.lapisCenterHeight);
                customworldsettingsfinal_customworldsettings.lapisSpread = JsonUtils.getInt(jsonobject, "lapisSpread", customworldsettingsfinal_customworldsettings.lapisSpread);
            } catch (Exception exception) {
                ;
            }

            return customworldsettingsfinal_customworldsettings;
        }

        @Override
        public JsonElement serialize(ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.addProperty("coordinateScale", Float.valueOf(customworldsettingsfinal_customworldsettings.coordinateScale));
            jsonobject.addProperty("heightScale", Float.valueOf(customworldsettingsfinal_customworldsettings.heightScale));
            jsonobject.addProperty("lowerLimitScale", Float.valueOf(customworldsettingsfinal_customworldsettings.lowerLimitScale));
            jsonobject.addProperty("upperLimitScale", Float.valueOf(customworldsettingsfinal_customworldsettings.upperLimitScale));
            jsonobject.addProperty("depthNoiseScaleX", Float.valueOf(customworldsettingsfinal_customworldsettings.depthNoiseScaleX));
            jsonobject.addProperty("depthNoiseScaleZ", Float.valueOf(customworldsettingsfinal_customworldsettings.depthNoiseScaleZ));
            jsonobject.addProperty("depthNoiseScaleExponent", Float.valueOf(customworldsettingsfinal_customworldsettings.depthNoiseScaleExponent));
            jsonobject.addProperty("mainNoiseScaleX", Float.valueOf(customworldsettingsfinal_customworldsettings.mainNoiseScaleX));
            jsonobject.addProperty("mainNoiseScaleY", Float.valueOf(customworldsettingsfinal_customworldsettings.mainNoiseScaleY));
            jsonobject.addProperty("mainNoiseScaleZ", Float.valueOf(customworldsettingsfinal_customworldsettings.mainNoiseScaleZ));
            jsonobject.addProperty("baseSize", Float.valueOf(customworldsettingsfinal_customworldsettings.baseSize));
            jsonobject.addProperty("stretchY", Float.valueOf(customworldsettingsfinal_customworldsettings.stretchY));
            jsonobject.addProperty("biomeDepthWeight", Float.valueOf(customworldsettingsfinal_customworldsettings.biomeDepthWeight));
            jsonobject.addProperty("biomeDepthOffset", Float.valueOf(customworldsettingsfinal_customworldsettings.biomeDepthOffset));
            jsonobject.addProperty("biomeScaleWeight", Float.valueOf(customworldsettingsfinal_customworldsettings.biomeScaleWeight));
            jsonobject.addProperty("biomeScaleOffset", Float.valueOf(customworldsettingsfinal_customworldsettings.biomeScaleOffset));
            jsonobject.addProperty("seaLevel", Integer.valueOf(customworldsettingsfinal_customworldsettings.seaLevel));
            jsonobject.addProperty("useCaves", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useCaves));
            jsonobject.addProperty("useDungeons", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useDungeons));
            jsonobject.addProperty("dungeonChance", Integer.valueOf(customworldsettingsfinal_customworldsettings.dungeonChance));
            jsonobject.addProperty("useStrongholds", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useStrongholds));
            jsonobject.addProperty("useVillages", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useVillages));
            jsonobject.addProperty("useMineShafts", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useMineShafts));
            jsonobject.addProperty("useTemples", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useTemples));
            jsonobject.addProperty("useMonuments", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useMonuments));
            jsonobject.addProperty("useMansions", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useMansions));
            jsonobject.addProperty("useRavines", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useRavines));
            jsonobject.addProperty("useWaterLakes", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useWaterLakes));
            jsonobject.addProperty("waterLakeChance", Integer.valueOf(customworldsettingsfinal_customworldsettings.waterLakeChance));
            jsonobject.addProperty("useLavaLakes", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useLavaLakes));
            jsonobject.addProperty("lavaLakeChance", Integer.valueOf(customworldsettingsfinal_customworldsettings.lavaLakeChance));
            jsonobject.addProperty("useLavaOceans", Boolean.valueOf(customworldsettingsfinal_customworldsettings.useLavaOceans));
            jsonobject.addProperty("fixedBiome", Integer.valueOf(customworldsettingsfinal_customworldsettings.fixedBiome));
            jsonobject.addProperty("biomeSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.biomeSize));
            jsonobject.addProperty("riverSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.riverSize));
            jsonobject.addProperty("dirtSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.dirtSize));
            jsonobject.addProperty("dirtCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.dirtCount));
            jsonobject.addProperty("dirtMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.dirtMinHeight));
            jsonobject.addProperty("dirtMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.dirtMaxHeight));
            jsonobject.addProperty("gravelSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.gravelSize));
            jsonobject.addProperty("gravelCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.gravelCount));
            jsonobject.addProperty("gravelMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.gravelMinHeight));
            jsonobject.addProperty("gravelMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.gravelMaxHeight));
            jsonobject.addProperty("graniteSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.graniteSize));
            jsonobject.addProperty("graniteCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.graniteCount));
            jsonobject.addProperty("graniteMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.graniteMinHeight));
            jsonobject.addProperty("graniteMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.graniteMaxHeight));
            jsonobject.addProperty("dioriteSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.dioriteSize));
            jsonobject.addProperty("dioriteCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.dioriteCount));
            jsonobject.addProperty("dioriteMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.dioriteMinHeight));
            jsonobject.addProperty("dioriteMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.dioriteMaxHeight));
            jsonobject.addProperty("andesiteSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.andesiteSize));
            jsonobject.addProperty("andesiteCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.andesiteCount));
            jsonobject.addProperty("andesiteMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.andesiteMinHeight));
            jsonobject.addProperty("andesiteMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.andesiteMaxHeight));
            jsonobject.addProperty("coalSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.coalSize));
            jsonobject.addProperty("coalCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.coalCount));
            jsonobject.addProperty("coalMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.coalMinHeight));
            jsonobject.addProperty("coalMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.coalMaxHeight));
            jsonobject.addProperty("ironSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.ironSize));
            jsonobject.addProperty("ironCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.ironCount));
            jsonobject.addProperty("ironMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.ironMinHeight));
            jsonobject.addProperty("ironMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.ironMaxHeight));
            jsonobject.addProperty("goldSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.goldSize));
            jsonobject.addProperty("goldCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.goldCount));
            jsonobject.addProperty("goldMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.goldMinHeight));
            jsonobject.addProperty("goldMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.goldMaxHeight));
            jsonobject.addProperty("redstoneSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.redstoneSize));
            jsonobject.addProperty("redstoneCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.redstoneCount));
            jsonobject.addProperty("redstoneMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.redstoneMinHeight));
            jsonobject.addProperty("redstoneMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.redstoneMaxHeight));
            jsonobject.addProperty("diamondSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.diamondSize));
            jsonobject.addProperty("diamondCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.diamondCount));
            jsonobject.addProperty("diamondMinHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.diamondMinHeight));
            jsonobject.addProperty("diamondMaxHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.diamondMaxHeight));
            jsonobject.addProperty("lapisSize", Integer.valueOf(customworldsettingsfinal_customworldsettings.lapisSize));
            jsonobject.addProperty("lapisCount", Integer.valueOf(customworldsettingsfinal_customworldsettings.lapisCount));
            jsonobject.addProperty("lapisCenterHeight", Integer.valueOf(customworldsettingsfinal_customworldsettings.lapisCenterHeight));
            jsonobject.addProperty("lapisSpread", Integer.valueOf(customworldsettingsfinal_customworldsettings.lapisSpread));
            return jsonobject;
        }

    }

    public static class Factory {

        @VisibleForTesting
        static final Gson JSON_ADAPTER = (new GsonBuilder()).registerTypeAdapter(ChunkGeneratorSettings.Factory.class, new ChunkGeneratorSettings.Serializer()).create();
        public float coordinateScale = 684.412F;
        public float heightScale = 684.412F;
        public float upperLimitScale = 512.0F;
        public float lowerLimitScale = 512.0F;
        public float depthNoiseScaleX = 200.0F;
        public float depthNoiseScaleZ = 200.0F;
        public float depthNoiseScaleExponent = 0.5F;
        public float mainNoiseScaleX = 80.0F;
        public float mainNoiseScaleY = 160.0F;
        public float mainNoiseScaleZ = 80.0F;
        public float baseSize = 8.5F;
        public float stretchY = 12.0F;
        public float biomeDepthWeight = 1.0F;
        public float biomeDepthOffset;
        public float biomeScaleWeight = 1.0F;
        public float biomeScaleOffset;
        public int seaLevel = 63;
        public boolean useCaves = true;
        public boolean useDungeons = true;
        public int dungeonChance = 8;
        public boolean useStrongholds = true;
        public boolean useVillages = true;
        public boolean useMineShafts = true;
        public boolean useTemples = true;
        public boolean useMonuments = true;
        public boolean useMansions = true;
        public boolean useRavines = true;
        public boolean useWaterLakes = true;
        public int waterLakeChance = 4;
        public boolean useLavaLakes = true;
        public int lavaLakeChance = 80;
        public boolean useLavaOceans;
        public int fixedBiome = -1;
        public int biomeSize = 4;
        public int riverSize = 4;
        public int dirtSize = 33;
        public int dirtCount = 10;
        public int dirtMinHeight;
        public int dirtMaxHeight = 256;
        public int gravelSize = 33;
        public int gravelCount = 8;
        public int gravelMinHeight;
        public int gravelMaxHeight = 256;
        public int graniteSize = 33;
        public int graniteCount = 10;
        public int graniteMinHeight;
        public int graniteMaxHeight = 80;
        public int dioriteSize = 33;
        public int dioriteCount = 10;
        public int dioriteMinHeight;
        public int dioriteMaxHeight = 80;
        public int andesiteSize = 33;
        public int andesiteCount = 10;
        public int andesiteMinHeight;
        public int andesiteMaxHeight = 80;
        public int coalSize = 17;
        public int coalCount = 20;
        public int coalMinHeight;
        public int coalMaxHeight = 128;
        public int ironSize = 9;
        public int ironCount = 20;
        public int ironMinHeight;
        public int ironMaxHeight = 64;
        public int goldSize = 9;
        public int goldCount = 2;
        public int goldMinHeight;
        public int goldMaxHeight = 32;
        public int redstoneSize = 8;
        public int redstoneCount = 8;
        public int redstoneMinHeight;
        public int redstoneMaxHeight = 16;
        public int diamondSize = 8;
        public int diamondCount = 1;
        public int diamondMinHeight;
        public int diamondMaxHeight = 16;
        public int lapisSize = 7;
        public int lapisCount = 1;
        public int lapisCenterHeight = 16;
        public int lapisSpread = 16;

        public static ChunkGeneratorSettings.Factory jsonToFactory(String s) {
            if (s.isEmpty()) {
                return new ChunkGeneratorSettings.Factory();
            } else {
                try {
                    return JsonUtils.gsonDeserialize(ChunkGeneratorSettings.Factory.JSON_ADAPTER, s, ChunkGeneratorSettings.Factory.class);
                } catch (Exception exception) {
                    return new ChunkGeneratorSettings.Factory();
                }
            }
        }

        @Override
        public String toString() {
            return ChunkGeneratorSettings.Factory.JSON_ADAPTER.toJson(this);
        }

        public Factory() {
            this.setDefaults();
        }

        public void setDefaults() {
            this.coordinateScale = 684.412F;
            this.heightScale = 684.412F;
            this.upperLimitScale = 512.0F;
            this.lowerLimitScale = 512.0F;
            this.depthNoiseScaleX = 200.0F;
            this.depthNoiseScaleZ = 200.0F;
            this.depthNoiseScaleExponent = 0.5F;
            this.mainNoiseScaleX = 80.0F;
            this.mainNoiseScaleY = 160.0F;
            this.mainNoiseScaleZ = 80.0F;
            this.baseSize = 8.5F;
            this.stretchY = 12.0F;
            this.biomeDepthWeight = 1.0F;
            this.biomeDepthOffset = 0.0F;
            this.biomeScaleWeight = 1.0F;
            this.biomeScaleOffset = 0.0F;
            this.seaLevel = 63;
            this.useCaves = true;
            this.useDungeons = true;
            this.dungeonChance = 8;
            this.useStrongholds = true;
            this.useVillages = true;
            this.useMineShafts = true;
            this.useTemples = true;
            this.useMonuments = true;
            this.useMansions = true;
            this.useRavines = true;
            this.useWaterLakes = true;
            this.waterLakeChance = 4;
            this.useLavaLakes = true;
            this.lavaLakeChance = 80;
            this.useLavaOceans = false;
            this.fixedBiome = -1;
            this.biomeSize = 4;
            this.riverSize = 4;
            this.dirtSize = 33;
            this.dirtCount = 10;
            this.dirtMinHeight = 0;
            this.dirtMaxHeight = 256;
            this.gravelSize = 33;
            this.gravelCount = 8;
            this.gravelMinHeight = 0;
            this.gravelMaxHeight = 256;
            this.graniteSize = 33;
            this.graniteCount = 10;
            this.graniteMinHeight = 0;
            this.graniteMaxHeight = 80;
            this.dioriteSize = 33;
            this.dioriteCount = 10;
            this.dioriteMinHeight = 0;
            this.dioriteMaxHeight = 80;
            this.andesiteSize = 33;
            this.andesiteCount = 10;
            this.andesiteMinHeight = 0;
            this.andesiteMaxHeight = 80;
            this.coalSize = 17;
            this.coalCount = 20;
            this.coalMinHeight = 0;
            this.coalMaxHeight = 128;
            this.ironSize = 9;
            this.ironCount = 20;
            this.ironMinHeight = 0;
            this.ironMaxHeight = 64;
            this.goldSize = 9;
            this.goldCount = 2;
            this.goldMinHeight = 0;
            this.goldMaxHeight = 32;
            this.redstoneSize = 8;
            this.redstoneCount = 8;
            this.redstoneMinHeight = 0;
            this.redstoneMaxHeight = 16;
            this.diamondSize = 8;
            this.diamondCount = 1;
            this.diamondMinHeight = 0;
            this.diamondMaxHeight = 16;
            this.lapisSize = 7;
            this.lapisCount = 1;
            this.lapisCenterHeight = 16;
            this.lapisSpread = 16;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (object != null && this.getClass() == object.getClass()) {
                ChunkGeneratorSettings.Factory customworldsettingsfinal_customworldsettings = (ChunkGeneratorSettings.Factory) object;

                return this.andesiteCount != customworldsettingsfinal_customworldsettings.andesiteCount ? false : (this.andesiteMaxHeight != customworldsettingsfinal_customworldsettings.andesiteMaxHeight ? false : (this.andesiteMinHeight != customworldsettingsfinal_customworldsettings.andesiteMinHeight ? false : (this.andesiteSize != customworldsettingsfinal_customworldsettings.andesiteSize ? false : (Float.compare(customworldsettingsfinal_customworldsettings.baseSize, this.baseSize) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.biomeDepthOffset, this.biomeDepthOffset) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.biomeDepthWeight, this.biomeDepthWeight) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.biomeScaleOffset, this.biomeScaleOffset) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.biomeScaleWeight, this.biomeScaleWeight) != 0 ? false : (this.biomeSize != customworldsettingsfinal_customworldsettings.biomeSize ? false : (this.coalCount != customworldsettingsfinal_customworldsettings.coalCount ? false : (this.coalMaxHeight != customworldsettingsfinal_customworldsettings.coalMaxHeight ? false : (this.coalMinHeight != customworldsettingsfinal_customworldsettings.coalMinHeight ? false : (this.coalSize != customworldsettingsfinal_customworldsettings.coalSize ? false : (Float.compare(customworldsettingsfinal_customworldsettings.coordinateScale, this.coordinateScale) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.depthNoiseScaleExponent, this.depthNoiseScaleExponent) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.depthNoiseScaleX, this.depthNoiseScaleX) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.depthNoiseScaleZ, this.depthNoiseScaleZ) != 0 ? false : (this.diamondCount != customworldsettingsfinal_customworldsettings.diamondCount ? false : (this.diamondMaxHeight != customworldsettingsfinal_customworldsettings.diamondMaxHeight ? false : (this.diamondMinHeight != customworldsettingsfinal_customworldsettings.diamondMinHeight ? false : (this.diamondSize != customworldsettingsfinal_customworldsettings.diamondSize ? false : (this.dioriteCount != customworldsettingsfinal_customworldsettings.dioriteCount ? false : (this.dioriteMaxHeight != customworldsettingsfinal_customworldsettings.dioriteMaxHeight ? false : (this.dioriteMinHeight != customworldsettingsfinal_customworldsettings.dioriteMinHeight ? false : (this.dioriteSize != customworldsettingsfinal_customworldsettings.dioriteSize ? false : (this.dirtCount != customworldsettingsfinal_customworldsettings.dirtCount ? false : (this.dirtMaxHeight != customworldsettingsfinal_customworldsettings.dirtMaxHeight ? false : (this.dirtMinHeight != customworldsettingsfinal_customworldsettings.dirtMinHeight ? false : (this.dirtSize != customworldsettingsfinal_customworldsettings.dirtSize ? false : (this.dungeonChance != customworldsettingsfinal_customworldsettings.dungeonChance ? false : (this.fixedBiome != customworldsettingsfinal_customworldsettings.fixedBiome ? false : (this.goldCount != customworldsettingsfinal_customworldsettings.goldCount ? false : (this.goldMaxHeight != customworldsettingsfinal_customworldsettings.goldMaxHeight ? false : (this.goldMinHeight != customworldsettingsfinal_customworldsettings.goldMinHeight ? false : (this.goldSize != customworldsettingsfinal_customworldsettings.goldSize ? false : (this.graniteCount != customworldsettingsfinal_customworldsettings.graniteCount ? false : (this.graniteMaxHeight != customworldsettingsfinal_customworldsettings.graniteMaxHeight ? false : (this.graniteMinHeight != customworldsettingsfinal_customworldsettings.graniteMinHeight ? false : (this.graniteSize != customworldsettingsfinal_customworldsettings.graniteSize ? false : (this.gravelCount != customworldsettingsfinal_customworldsettings.gravelCount ? false : (this.gravelMaxHeight != customworldsettingsfinal_customworldsettings.gravelMaxHeight ? false : (this.gravelMinHeight != customworldsettingsfinal_customworldsettings.gravelMinHeight ? false : (this.gravelSize != customworldsettingsfinal_customworldsettings.gravelSize ? false : (Float.compare(customworldsettingsfinal_customworldsettings.heightScale, this.heightScale) != 0 ? false : (this.ironCount != customworldsettingsfinal_customworldsettings.ironCount ? false : (this.ironMaxHeight != customworldsettingsfinal_customworldsettings.ironMaxHeight ? false : (this.ironMinHeight != customworldsettingsfinal_customworldsettings.ironMinHeight ? false : (this.ironSize != customworldsettingsfinal_customworldsettings.ironSize ? false : (this.lapisCenterHeight != customworldsettingsfinal_customworldsettings.lapisCenterHeight ? false : (this.lapisCount != customworldsettingsfinal_customworldsettings.lapisCount ? false : (this.lapisSize != customworldsettingsfinal_customworldsettings.lapisSize ? false : (this.lapisSpread != customworldsettingsfinal_customworldsettings.lapisSpread ? false : (this.lavaLakeChance != customworldsettingsfinal_customworldsettings.lavaLakeChance ? false : (Float.compare(customworldsettingsfinal_customworldsettings.lowerLimitScale, this.lowerLimitScale) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.mainNoiseScaleX, this.mainNoiseScaleX) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.mainNoiseScaleY, this.mainNoiseScaleY) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.mainNoiseScaleZ, this.mainNoiseScaleZ) != 0 ? false : (this.redstoneCount != customworldsettingsfinal_customworldsettings.redstoneCount ? false : (this.redstoneMaxHeight != customworldsettingsfinal_customworldsettings.redstoneMaxHeight ? false : (this.redstoneMinHeight != customworldsettingsfinal_customworldsettings.redstoneMinHeight ? false : (this.redstoneSize != customworldsettingsfinal_customworldsettings.redstoneSize ? false : (this.riverSize != customworldsettingsfinal_customworldsettings.riverSize ? false : (this.seaLevel != customworldsettingsfinal_customworldsettings.seaLevel ? false : (Float.compare(customworldsettingsfinal_customworldsettings.stretchY, this.stretchY) != 0 ? false : (Float.compare(customworldsettingsfinal_customworldsettings.upperLimitScale, this.upperLimitScale) != 0 ? false : (this.useCaves != customworldsettingsfinal_customworldsettings.useCaves ? false : (this.useDungeons != customworldsettingsfinal_customworldsettings.useDungeons ? false : (this.useLavaLakes != customworldsettingsfinal_customworldsettings.useLavaLakes ? false : (this.useLavaOceans != customworldsettingsfinal_customworldsettings.useLavaOceans ? false : (this.useMineShafts != customworldsettingsfinal_customworldsettings.useMineShafts ? false : (this.useRavines != customworldsettingsfinal_customworldsettings.useRavines ? false : (this.useStrongholds != customworldsettingsfinal_customworldsettings.useStrongholds ? false : (this.useTemples != customworldsettingsfinal_customworldsettings.useTemples ? false : (this.useMonuments != customworldsettingsfinal_customworldsettings.useMonuments ? false : (this.useMansions != customworldsettingsfinal_customworldsettings.useMansions ? false : (this.useVillages != customworldsettingsfinal_customworldsettings.useVillages ? false : (this.useWaterLakes != customworldsettingsfinal_customworldsettings.useWaterLakes ? false : this.waterLakeChance == customworldsettingsfinal_customworldsettings.waterLakeChance)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))));
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int i = this.coordinateScale == 0.0F ? 0 : Float.floatToIntBits(this.coordinateScale);

            i = 31 * i + (this.heightScale == 0.0F ? 0 : Float.floatToIntBits(this.heightScale));
            i = 31 * i + (this.upperLimitScale == 0.0F ? 0 : Float.floatToIntBits(this.upperLimitScale));
            i = 31 * i + (this.lowerLimitScale == 0.0F ? 0 : Float.floatToIntBits(this.lowerLimitScale));
            i = 31 * i + (this.depthNoiseScaleX == 0.0F ? 0 : Float.floatToIntBits(this.depthNoiseScaleX));
            i = 31 * i + (this.depthNoiseScaleZ == 0.0F ? 0 : Float.floatToIntBits(this.depthNoiseScaleZ));
            i = 31 * i + (this.depthNoiseScaleExponent == 0.0F ? 0 : Float.floatToIntBits(this.depthNoiseScaleExponent));
            i = 31 * i + (this.mainNoiseScaleX == 0.0F ? 0 : Float.floatToIntBits(this.mainNoiseScaleX));
            i = 31 * i + (this.mainNoiseScaleY == 0.0F ? 0 : Float.floatToIntBits(this.mainNoiseScaleY));
            i = 31 * i + (this.mainNoiseScaleZ == 0.0F ? 0 : Float.floatToIntBits(this.mainNoiseScaleZ));
            i = 31 * i + (this.baseSize == 0.0F ? 0 : Float.floatToIntBits(this.baseSize));
            i = 31 * i + (this.stretchY == 0.0F ? 0 : Float.floatToIntBits(this.stretchY));
            i = 31 * i + (this.biomeDepthWeight == 0.0F ? 0 : Float.floatToIntBits(this.biomeDepthWeight));
            i = 31 * i + (this.biomeDepthOffset == 0.0F ? 0 : Float.floatToIntBits(this.biomeDepthOffset));
            i = 31 * i + (this.biomeScaleWeight == 0.0F ? 0 : Float.floatToIntBits(this.biomeScaleWeight));
            i = 31 * i + (this.biomeScaleOffset == 0.0F ? 0 : Float.floatToIntBits(this.biomeScaleOffset));
            i = 31 * i + this.seaLevel;
            i = 31 * i + (this.useCaves ? 1 : 0);
            i = 31 * i + (this.useDungeons ? 1 : 0);
            i = 31 * i + this.dungeonChance;
            i = 31 * i + (this.useStrongholds ? 1 : 0);
            i = 31 * i + (this.useVillages ? 1 : 0);
            i = 31 * i + (this.useMineShafts ? 1 : 0);
            i = 31 * i + (this.useTemples ? 1 : 0);
            i = 31 * i + (this.useMonuments ? 1 : 0);
            i = 31 * i + (this.useMansions ? 1 : 0);
            i = 31 * i + (this.useRavines ? 1 : 0);
            i = 31 * i + (this.useWaterLakes ? 1 : 0);
            i = 31 * i + this.waterLakeChance;
            i = 31 * i + (this.useLavaLakes ? 1 : 0);
            i = 31 * i + this.lavaLakeChance;
            i = 31 * i + (this.useLavaOceans ? 1 : 0);
            i = 31 * i + this.fixedBiome;
            i = 31 * i + this.biomeSize;
            i = 31 * i + this.riverSize;
            i = 31 * i + this.dirtSize;
            i = 31 * i + this.dirtCount;
            i = 31 * i + this.dirtMinHeight;
            i = 31 * i + this.dirtMaxHeight;
            i = 31 * i + this.gravelSize;
            i = 31 * i + this.gravelCount;
            i = 31 * i + this.gravelMinHeight;
            i = 31 * i + this.gravelMaxHeight;
            i = 31 * i + this.graniteSize;
            i = 31 * i + this.graniteCount;
            i = 31 * i + this.graniteMinHeight;
            i = 31 * i + this.graniteMaxHeight;
            i = 31 * i + this.dioriteSize;
            i = 31 * i + this.dioriteCount;
            i = 31 * i + this.dioriteMinHeight;
            i = 31 * i + this.dioriteMaxHeight;
            i = 31 * i + this.andesiteSize;
            i = 31 * i + this.andesiteCount;
            i = 31 * i + this.andesiteMinHeight;
            i = 31 * i + this.andesiteMaxHeight;
            i = 31 * i + this.coalSize;
            i = 31 * i + this.coalCount;
            i = 31 * i + this.coalMinHeight;
            i = 31 * i + this.coalMaxHeight;
            i = 31 * i + this.ironSize;
            i = 31 * i + this.ironCount;
            i = 31 * i + this.ironMinHeight;
            i = 31 * i + this.ironMaxHeight;
            i = 31 * i + this.goldSize;
            i = 31 * i + this.goldCount;
            i = 31 * i + this.goldMinHeight;
            i = 31 * i + this.goldMaxHeight;
            i = 31 * i + this.redstoneSize;
            i = 31 * i + this.redstoneCount;
            i = 31 * i + this.redstoneMinHeight;
            i = 31 * i + this.redstoneMaxHeight;
            i = 31 * i + this.diamondSize;
            i = 31 * i + this.diamondCount;
            i = 31 * i + this.diamondMinHeight;
            i = 31 * i + this.diamondMaxHeight;
            i = 31 * i + this.lapisSize;
            i = 31 * i + this.lapisCount;
            i = 31 * i + this.lapisCenterHeight;
            i = 31 * i + this.lapisSpread;
            return i;
        }

        public ChunkGeneratorSettings build() {
            return new ChunkGeneratorSettings(this, null);
        }
    }
}
