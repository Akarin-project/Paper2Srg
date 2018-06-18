package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatisticsManagerServer extends StatisticsManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private final MinecraftServer mcServer;
    private final File statsFile;
    private final Set<StatBase> dirty = Sets.newHashSet();
    private int lastStatRequest = -300;

    public StatisticsManagerServer(MinecraftServer minecraftserver, File file) {
        this.mcServer = minecraftserver;
        this.statsFile = file;
        // Spigot start
        for ( String name : org.spigotmc.SpigotConfig.forcedStats.keySet() )
        {
            TupleIntJsonSerializable wrapper = new TupleIntJsonSerializable();
            wrapper.setJsonSerializableValue( org.spigotmc.SpigotConfig.forcedStats.get( name ) );
            statsData.put( StatList.getOneShotStat( name ), wrapper );
        }
        // Spigot end
    }

    public void readStatFile() {
        if (this.statsFile.isFile()) {
            try {
                this.statsData.clear();
                this.statsData.putAll(this.parseJson(FileUtils.readFileToString(this.statsFile)));
            } catch (IOException ioexception) {
                StatisticsManagerServer.LOGGER.error("Couldn\'t read statistics file {}", this.statsFile, ioexception);
            } catch (JsonParseException jsonparseexception) {
                StatisticsManagerServer.LOGGER.error("Couldn\'t parse statistics file {}", this.statsFile, jsonparseexception);
            }
        }

    }

    public void saveStatFile() {
        if ( org.spigotmc.SpigotConfig.disableStatSaving ) return; // Spigot
        try {
            FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
        } catch (IOException ioexception) {
            StatisticsManagerServer.LOGGER.error("Couldn\'t save stats", ioexception);
        }

    }

    public void unlockAchievement(EntityPlayer entityhuman, StatBase statistic, int i) {
        if ( org.spigotmc.SpigotConfig.disableStatSaving ) return; // Spigot
        super.unlockAchievement(entityhuman, statistic, i);
        this.dirty.add(statistic);
    }

    private Set<StatBase> getDirty() {
        HashSet hashset = Sets.newHashSet(this.dirty);

        this.dirty.clear();
        return hashset;
    }

    public Map<StatBase, TupleIntJsonSerializable> parseJson(String s) {
        JsonElement jsonelement = (new JsonParser()).parse(s);

        if (!jsonelement.isJsonObject()) {
            return Maps.newHashMap();
        } else {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            HashMap hashmap = Maps.newHashMap();
            Iterator iterator = jsonobject.entrySet().iterator();
            java.util.List<String> invalidStats = com.google.common.collect.Lists.newArrayList(); // Paper

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                StatBase statistic = StatList.getOneShotStat((String) entry.getKey());

                if (statistic != null) {
                    TupleIntJsonSerializable statisticwrapper = new TupleIntJsonSerializable();

                    if (((JsonElement) entry.getValue()).isJsonPrimitive() && ((JsonElement) entry.getValue()).getAsJsonPrimitive().isNumber()) {
                        statisticwrapper.setIntegerValue(((JsonElement) entry.getValue()).getAsInt());
                    } else if (((JsonElement) entry.getValue()).isJsonObject()) {
                        JsonObject jsonobject1 = ((JsonElement) entry.getValue()).getAsJsonObject();

                        if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber()) {
                            statisticwrapper.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                        }

                        if (jsonobject1.has("progress") && statistic.getSerializableClazz() != null) {
                            try {
                                Constructor constructor = statistic.getSerializableClazz().getConstructor(new Class[0]);
                                IJsonSerializable ijsonstatistic = (IJsonSerializable) constructor.newInstance(new Object[0]);

                                ijsonstatistic.fromJson(jsonobject1.get("progress"));
                                statisticwrapper.setJsonSerializableValue(ijsonstatistic);
                            } catch (Throwable throwable) {
                                StatisticsManagerServer.LOGGER.warn("Invalid statistic progress in {}", this.statsFile, throwable);
                            }
                        }
                    }

                    hashmap.put(statistic, statisticwrapper);
                } else {
                    StatisticsManagerServer.LOGGER.warn("Invalid statistic in {}: Don\'t know what {} is", this.statsFile, entry.getKey());
                    if (com.destroystokyo.paper.PaperConfig.removeInvalidStatistics) invalidStats.add((String) entry.getKey()); // Paper
                }
            }

            // Paper start - Remove invalid statistics
            for (String invalid : invalidStats) {
                jsonobject.remove(invalid);
                StatisticsManagerServer.LOGGER.info("Removing invalid statistic: " + invalid);
            }
            // Paper end

            return hashmap;
        }
    }

    public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> map) {
        JsonObject jsonobject = new JsonObject();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((TupleIntJsonSerializable) entry.getValue()).getJsonSerializableValue() != null) {
                JsonObject jsonobject1 = new JsonObject();

                jsonobject1.addProperty("value", Integer.valueOf(((TupleIntJsonSerializable) entry.getValue()).getIntegerValue()));

                try {
                    jsonobject1.add("progress", ((TupleIntJsonSerializable) entry.getValue()).getJsonSerializableValue().getSerializableElement());
                } catch (Throwable throwable) {
                    StatisticsManagerServer.LOGGER.warn("Couldn\'t save statistic {}: error serializing progress", ((StatBase) entry.getKey()).getStatName(), throwable);
                }

                jsonobject.add(((StatBase) entry.getKey()).statId, jsonobject1);
            } else {
                jsonobject.addProperty(((StatBase) entry.getKey()).statId, Integer.valueOf(((TupleIntJsonSerializable) entry.getValue()).getIntegerValue()));
            }
        }

        return jsonobject.toString();
    }

    public void markAllDirty() {
        this.dirty.addAll(this.statsData.keySet());
    }

    public void sendStats(EntityPlayerMP entityplayer) {
        int i = this.mcServer.getTickCounter();
        HashMap hashmap = Maps.newHashMap();

        if (i - this.lastStatRequest > 300) {
            this.lastStatRequest = i;
            Iterator iterator = this.getDirty().iterator();

            while (iterator.hasNext()) {
                StatBase statistic = (StatBase) iterator.next();

                hashmap.put(statistic, Integer.valueOf(this.readStat(statistic)));
            }
        }

        entityplayer.connection.sendPacket(new SPacketStatistics(hashmap));
    }
}
