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

    private static final Logger field_150889_b = LogManager.getLogger();
    private final MinecraftServer field_150890_c;
    private final File field_150887_d;
    private final Set<StatBase> field_150888_e = Sets.newHashSet();
    private int field_150885_f = -300;

    public StatisticsManagerServer(MinecraftServer minecraftserver, File file) {
        this.field_150890_c = minecraftserver;
        this.field_150887_d = file;
        // Spigot start
        for ( String name : org.spigotmc.SpigotConfig.forcedStats.keySet() )
        {
            TupleIntJsonSerializable wrapper = new TupleIntJsonSerializable();
            wrapper.func_151190_a( org.spigotmc.SpigotConfig.forcedStats.get( name ) );
            field_150875_a.put( StatList.func_151177_a( name ), wrapper );
        }
        // Spigot end
    }

    public void func_150882_a() {
        if (this.field_150887_d.isFile()) {
            try {
                this.field_150875_a.clear();
                this.field_150875_a.putAll(this.func_150881_a(FileUtils.readFileToString(this.field_150887_d)));
            } catch (IOException ioexception) {
                StatisticsManagerServer.field_150889_b.error("Couldn\'t read statistics file {}", this.field_150887_d, ioexception);
            } catch (JsonParseException jsonparseexception) {
                StatisticsManagerServer.field_150889_b.error("Couldn\'t parse statistics file {}", this.field_150887_d, jsonparseexception);
            }
        }

    }

    public void func_150883_b() {
        if ( org.spigotmc.SpigotConfig.disableStatSaving ) return; // Spigot
        try {
            FileUtils.writeStringToFile(this.field_150887_d, func_150880_a(this.field_150875_a));
        } catch (IOException ioexception) {
            StatisticsManagerServer.field_150889_b.error("Couldn\'t save stats", ioexception);
        }

    }

    public void func_150873_a(EntityPlayer entityhuman, StatBase statistic, int i) {
        if ( org.spigotmc.SpigotConfig.disableStatSaving ) return; // Spigot
        super.func_150873_a(entityhuman, statistic, i);
        this.field_150888_e.add(statistic);
    }

    private Set<StatBase> func_150878_c() {
        HashSet hashset = Sets.newHashSet(this.field_150888_e);

        this.field_150888_e.clear();
        return hashset;
    }

    public Map<StatBase, TupleIntJsonSerializable> func_150881_a(String s) {
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
                StatBase statistic = StatList.func_151177_a((String) entry.getKey());

                if (statistic != null) {
                    TupleIntJsonSerializable statisticwrapper = new TupleIntJsonSerializable();

                    if (((JsonElement) entry.getValue()).isJsonPrimitive() && ((JsonElement) entry.getValue()).getAsJsonPrimitive().isNumber()) {
                        statisticwrapper.func_151188_a(((JsonElement) entry.getValue()).getAsInt());
                    } else if (((JsonElement) entry.getValue()).isJsonObject()) {
                        JsonObject jsonobject1 = ((JsonElement) entry.getValue()).getAsJsonObject();

                        if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber()) {
                            statisticwrapper.func_151188_a(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                        }

                        if (jsonobject1.has("progress") && statistic.func_150954_l() != null) {
                            try {
                                Constructor constructor = statistic.func_150954_l().getConstructor(new Class[0]);
                                IJsonSerializable ijsonstatistic = (IJsonSerializable) constructor.newInstance(new Object[0]);

                                ijsonstatistic.func_152753_a(jsonobject1.get("progress"));
                                statisticwrapper.func_151190_a(ijsonstatistic);
                            } catch (Throwable throwable) {
                                StatisticsManagerServer.field_150889_b.warn("Invalid statistic progress in {}", this.field_150887_d, throwable);
                            }
                        }
                    }

                    hashmap.put(statistic, statisticwrapper);
                } else {
                    StatisticsManagerServer.field_150889_b.warn("Invalid statistic in {}: Don\'t know what {} is", this.field_150887_d, entry.getKey());
                    if (com.destroystokyo.paper.PaperConfig.removeInvalidStatistics) invalidStats.add((String) entry.getKey()); // Paper
                }
            }

            // Paper start - Remove invalid statistics
            for (String invalid : invalidStats) {
                jsonobject.remove(invalid);
                StatisticsManagerServer.field_150889_b.info("Removing invalid statistic: " + invalid);
            }
            // Paper end

            return hashmap;
        }
    }

    public static String func_150880_a(Map<StatBase, TupleIntJsonSerializable> map) {
        JsonObject jsonobject = new JsonObject();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((TupleIntJsonSerializable) entry.getValue()).func_151187_b() != null) {
                JsonObject jsonobject1 = new JsonObject();

                jsonobject1.addProperty("value", Integer.valueOf(((TupleIntJsonSerializable) entry.getValue()).func_151189_a()));

                try {
                    jsonobject1.add("progress", ((TupleIntJsonSerializable) entry.getValue()).func_151187_b().func_151003_a());
                } catch (Throwable throwable) {
                    StatisticsManagerServer.field_150889_b.warn("Couldn\'t save statistic {}: error serializing progress", ((StatBase) entry.getKey()).func_150951_e(), throwable);
                }

                jsonobject.add(((StatBase) entry.getKey()).field_75975_e, jsonobject1);
            } else {
                jsonobject.addProperty(((StatBase) entry.getKey()).field_75975_e, Integer.valueOf(((TupleIntJsonSerializable) entry.getValue()).func_151189_a()));
            }
        }

        return jsonobject.toString();
    }

    public void func_150877_d() {
        this.field_150888_e.addAll(this.field_150875_a.keySet());
    }

    public void func_150876_a(EntityPlayerMP entityplayer) {
        int i = this.field_150890_c.func_71259_af();
        HashMap hashmap = Maps.newHashMap();

        if (i - this.field_150885_f > 300) {
            this.field_150885_f = i;
            Iterator iterator = this.func_150878_c().iterator();

            while (iterator.hasNext()) {
                StatBase statistic = (StatBase) iterator.next();

                hashmap.put(statistic, Integer.valueOf(this.func_77444_a(statistic)));
            }
        }

        entityplayer.field_71135_a.func_147359_a(new SPacketStatistics(hashmap));
    }
}
