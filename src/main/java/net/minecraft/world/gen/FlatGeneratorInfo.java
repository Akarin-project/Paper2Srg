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

    private final List<FlatLayerInfo> field_82655_a = Lists.newArrayList();
    private final Map<String, Map<String, String>> field_82653_b = Maps.newHashMap();
    private int field_82654_c;

    public FlatGeneratorInfo() {}

    public int func_82648_a() {
        return this.field_82654_c;
    }

    public void func_82647_a(int i) {
        this.field_82654_c = i;
    }

    public Map<String, Map<String, String>> func_82644_b() {
        return this.field_82653_b;
    }

    public List<FlatLayerInfo> func_82650_c() {
        return this.field_82655_a;
    }

    public void func_82645_d() {
        int i = 0;

        FlatLayerInfo worldgenflatlayerinfo;

        for (Iterator iterator = this.field_82655_a.iterator(); iterator.hasNext(); i += worldgenflatlayerinfo.func_82657_a()) {
            worldgenflatlayerinfo = (FlatLayerInfo) iterator.next();
            worldgenflatlayerinfo.func_82660_d(i);
        }

    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(3);
        stringbuilder.append(";");

        int i;

        for (i = 0; i < this.field_82655_a.size(); ++i) {
            if (i > 0) {
                stringbuilder.append(",");
            }

            stringbuilder.append(this.field_82655_a.get(i));
        }

        stringbuilder.append(";");
        stringbuilder.append(this.field_82654_c);
        if (this.field_82653_b.isEmpty()) {
            stringbuilder.append(";");
        } else {
            stringbuilder.append(";");
            i = 0;
            Iterator iterator = this.field_82653_b.entrySet().iterator();

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

    private static FlatLayerInfo func_180715_a(int i, String s, int j) {
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

                block = Block.func_149729_e(Integer.parseInt(astring[0]));
            } else {
                astring = s1.split(":", 3);
                block = astring.length > 1 ? Block.func_149684_b(astring[0] + ":" + astring[1]) : null;
                if (block != null) {
                    l = astring.length > 2 ? Integer.parseInt(astring[2]) : 0;
                } else {
                    block = Block.func_149684_b(astring[0]);
                    if (block != null) {
                        l = astring.length > 1 ? Integer.parseInt(astring[1]) : 0;
                    }
                }

                if (block == null) {
                    return null;
                }
            }

            if (block == Blocks.field_150350_a) {
                l = 0;
            }

            if (l < 0 || l > 15) {
                l = 0;
            }
        } catch (Throwable throwable1) {
            return null;
        }

        FlatLayerInfo worldgenflatlayerinfo = new FlatLayerInfo(i, k, block, l);

        worldgenflatlayerinfo.func_82660_d(j);
        return worldgenflatlayerinfo;
    }

    private static List<FlatLayerInfo> func_180716_a(int i, String s) {
        if (s != null && s.length() >= 1) {
            ArrayList arraylist = Lists.newArrayList();
            String[] astring = s.split(",");
            int j = 0;
            String[] astring1 = astring;
            int k = astring.length;

            for (int l = 0; l < k; ++l) {
                String s1 = astring1[l];
                FlatLayerInfo worldgenflatlayerinfo = func_180715_a(i, s1, j);

                if (worldgenflatlayerinfo == null) {
                    return null;
                }

                arraylist.add(worldgenflatlayerinfo);
                j += worldgenflatlayerinfo.func_82657_a();
            }

            return arraylist;
        } else {
            return null;
        }
    }

    public static FlatGeneratorInfo func_82651_a(String s) {
        if (s == null) {
            return func_82649_e();
        } else {
            String[] astring = s.split(";", -1);
            int i = astring.length == 1 ? 0 : MathHelper.func_82715_a(astring[0], 0);

            if (i >= 0 && i <= 3) {
                FlatGeneratorInfo worldgenflatinfo = new FlatGeneratorInfo();
                int j = astring.length == 1 ? 0 : 1;
                List list = func_180716_a(i, astring[j++]);

                if (list != null && !list.isEmpty()) {
                    worldgenflatinfo.func_82650_c().addAll(list);
                    worldgenflatinfo.func_82645_d();
                    int k = Biome.func_185362_a(Biomes.field_76772_c);

                    if (i > 0 && astring.length > j) {
                        k = MathHelper.func_82715_a(astring[j++], k);
                    }

                    worldgenflatinfo.func_82647_a(k);
                    if (i > 0 && astring.length > j) {
                        String[] astring1 = astring[j++].toLowerCase(Locale.ROOT).split(",");
                        String[] astring2 = astring1;
                        int l = astring1.length;

                        for (int i1 = 0; i1 < l; ++i1) {
                            String s1 = astring2[i1];
                            String[] astring3 = s1.split("\\(", 2);
                            HashMap hashmap = Maps.newHashMap();

                            if (!astring3[0].isEmpty()) {
                                worldgenflatinfo.func_82644_b().put(astring3[0], hashmap);
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
                        worldgenflatinfo.func_82644_b().put("village", Maps.newHashMap());
                    }

                    return worldgenflatinfo;
                } else {
                    return func_82649_e();
                }
            } else {
                return func_82649_e();
            }
        }
    }

    public static FlatGeneratorInfo func_82649_e() {
        FlatGeneratorInfo worldgenflatinfo = new FlatGeneratorInfo();

        worldgenflatinfo.func_82647_a(Biome.func_185362_a(Biomes.field_76772_c));
        worldgenflatinfo.func_82650_c().add(new FlatLayerInfo(1, Blocks.field_150357_h));
        worldgenflatinfo.func_82650_c().add(new FlatLayerInfo(2, Blocks.field_150346_d));
        worldgenflatinfo.func_82650_c().add(new FlatLayerInfo(1, Blocks.field_150349_c));
        worldgenflatinfo.func_82645_d();
        worldgenflatinfo.func_82644_b().put("village", Maps.newHashMap());
        return worldgenflatinfo;
    }
}
