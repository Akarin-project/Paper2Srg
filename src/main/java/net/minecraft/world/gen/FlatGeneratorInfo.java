package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class FlatGeneratorInfo {

    private final List<FlatLayerInfo> flatLayers = Lists.newArrayList();
    private final Map<String, Map<String, String>> worldFeatures = Maps.newHashMap();
    private int biomeToUse;

    public FlatGeneratorInfo() {}

    public int getBiome() {
        return this.biomeToUse;
    }

    public void setBiome(int i) {
        this.biomeToUse = i;
    }

    public Map<String, Map<String, String>> getWorldFeatures() {
        return this.worldFeatures;
    }

    public List<FlatLayerInfo> getFlatLayers() {
        return this.flatLayers;
    }

    public void updateLayers() {
        int i = 0;

        FlatLayerInfo worldgenflatlayerinfo;

        for (Iterator iterator = this.flatLayers.iterator(); iterator.hasNext(); i += worldgenflatlayerinfo.getLayerCount()) {
            worldgenflatlayerinfo = (FlatLayerInfo) iterator.next();
            worldgenflatlayerinfo.setMinY(i);
        }

    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(3);
        stringbuilder.append(";");

        int i;

        for (i = 0; i < this.flatLayers.size(); ++i) {
            if (i > 0) {
                stringbuilder.append(",");
            }

            stringbuilder.append(this.flatLayers.get(i));
        }

        stringbuilder.append(";");
        stringbuilder.append(this.biomeToUse);
        if (this.worldFeatures.isEmpty()) {
            stringbuilder.append(";");
        } else {
            stringbuilder.append(";");
            i = 0;
            Iterator iterator = this.worldFeatures.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                if (i++ > 0) {
                    stringbuilder.append(",");
                }

                stringbuilder.append(((String) entry.getKey()).toLowerCase(Locale.ROOT));
                Map map = (Map) entry.getValue();

                if (!map.isEmpty()) {
                    stringbuilder.append("(");
                    int j = 0;
                    Iterator iterator1 = map.entrySet().iterator();

                    while (iterator1.hasNext()) {
                        Entry entry1 = (Entry) iterator1.next();

                        if (j++ > 0) {
                            stringbuilder.append(" ");
                        }

                        stringbuilder.append((String) entry1.getKey());
                        stringbuilder.append("=");
                        stringbuilder.append((String) entry1.getValue());
                    }

                    stringbuilder.append(")");
                }
            }
        }

        return stringbuilder.toString();
    }

    private static FlatLayerInfo getLayerFromString(int i, String s, int j) {
        String[] astring = i >= 3 ? s.split("\\*", 2) : s.split("x", 2);
        int k = 1;
        int l = 0;

        if (astring.length == 2) {
            try {
                k = Integer.parseInt(astring[0]);
                if (j + k >= 256) {
                    k = 256 - j;
                }

                if (k < 0) {
                    k = 0;
                }
            } catch (Throwable throwable) {
                return null;
            }
        }

        Block block;

        try {
            String s1 = astring[astring.length - 1];

            if (i < 3) {
                astring = s1.split(":", 2);
                if (astring.length > 1) {
                    l = Integer.parseInt(astring[1]);
                }

                block = Block.getBlockById(Integer.parseInt(astring[0]));
            } else {
                astring = s1.split(":", 3);
                block = astring.length > 1 ? Block.getBlockFromName(astring[0] + ":" + astring[1]) : null;
                if (block != null) {
                    l = astring.length > 2 ? Integer.parseInt(astring[2]) : 0;
                } else {
                    block = Block.getBlockFromName(astring[0]);
                    if (block != null) {
                        l = astring.length > 1 ? Integer.parseInt(astring[1]) : 0;
                    }
                }

                if (block == null) {
                    return null;
                }
            }

            if (block == Blocks.AIR) {
                l = 0;
            }

            if (l < 0 || l > 15) {
                l = 0;
            }
        } catch (Throwable throwable1) {
            return null;
        }

        FlatLayerInfo worldgenflatlayerinfo = new FlatLayerInfo(i, k, block, l);

        worldgenflatlayerinfo.setMinY(j);
        return worldgenflatlayerinfo;
    }

    private static List<FlatLayerInfo> getLayersFromString(int i, String s) {
        if (s != null && s.length() >= 1) {
            ArrayList arraylist = Lists.newArrayList();
            String[] astring = s.split(",");
            int j = 0;
            String[] astring1 = astring;
            int k = astring.length;

            for (int l = 0; l < k; ++l) {
                String s1 = astring1[l];
                FlatLayerInfo worldgenflatlayerinfo = getLayerFromString(i, s1, j);

                if (worldgenflatlayerinfo == null) {
                    return null;
                }

                arraylist.add(worldgenflatlayerinfo);
                j += worldgenflatlayerinfo.getLayerCount();
            }

            return arraylist;
        } else {
            return null;
        }
    }

    public static FlatGeneratorInfo createFlatGeneratorFromString(String s) {
        if (s == null) {
            return getDefaultFlatGenerator();
        } else {
            String[] astring = s.split(";", -1);
            int i = astring.length == 1 ? 0 : MathHelper.getInt(astring[0], 0);

            if (i >= 0 && i <= 3) {
                FlatGeneratorInfo worldgenflatinfo = new FlatGeneratorInfo();
                int j = astring.length == 1 ? 0 : 1;
                List list = getLayersFromString(i, astring[j++]);

                if (list != null && !list.isEmpty()) {
                    worldgenflatinfo.getFlatLayers().addAll(list);
                    worldgenflatinfo.updateLayers();
                    int k = Biome.getIdForBiome(Biomes.PLAINS);

                    if (i > 0 && astring.length > j) {
                        k = MathHelper.getInt(astring[j++], k);
                    }

                    worldgenflatinfo.setBiome(k);
                    if (i > 0 && astring.length > j) {
                        String[] astring1 = astring[j++].toLowerCase(Locale.ROOT).split(",");
                        String[] astring2 = astring1;
                        int l = astring1.length;

                        for (int i1 = 0; i1 < l; ++i1) {
                            String s1 = astring2[i1];
                            String[] astring3 = s1.split("\\(", 2);
                            HashMap hashmap = Maps.newHashMap();

                            if (!astring3[0].isEmpty()) {
                                worldgenflatinfo.getWorldFeatures().put(astring3[0], hashmap);
                                if (astring3.length > 1 && astring3[1].endsWith(")") && astring3[1].length() > 1) {
                                    String[] astring4 = astring3[1].substring(0, astring3[1].length() - 1).split(" ");
                                    String[] astring5 = astring4;
                                    int j1 = astring4.length;

                                    for (int k1 = 0; k1 < j1; ++k1) {
                                        String s2 = astring5[k1];
                                        String[] astring6 = s2.split("=", 2);

                                        if (astring6.length == 2) {
                                            hashmap.put(astring6[0], astring6[1]);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        worldgenflatinfo.getWorldFeatures().put("village", Maps.newHashMap());
                    }

                    return worldgenflatinfo;
                } else {
                    return getDefaultFlatGenerator();
                }
            } else {
                return getDefaultFlatGenerator();
            }
        }
    }

    public static FlatGeneratorInfo getDefaultFlatGenerator() {
        FlatGeneratorInfo worldgenflatinfo = new FlatGeneratorInfo();

        worldgenflatinfo.setBiome(Biome.getIdForBiome(Biomes.PLAINS));
        worldgenflatinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.BEDROCK));
        worldgenflatinfo.getFlatLayers().add(new FlatLayerInfo(2, Blocks.DIRT));
        worldgenflatinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.GRASS));
        worldgenflatinfo.updateLayers();
        worldgenflatinfo.getWorldFeatures().put("village", Maps.newHashMap());
        return worldgenflatinfo;
    }
}
