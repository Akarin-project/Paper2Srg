package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;

public class AdvancementProgress implements Comparable<AdvancementProgress> {

    private final Map<String, CriterionProgress> field_192110_a = Maps.newHashMap();
    private String[][] field_192111_b = new String[0][];

    public AdvancementProgress() {}

    public void func_192099_a(Map<String, Criterion> map, String[][] astring) {
        Set set = map.keySet();
        Iterator iterator = this.field_192110_a.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!set.contains(entry.getKey())) {
                iterator.remove();
            }
        }

        iterator = set.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (!this.field_192110_a.containsKey(s)) {
                this.field_192110_a.put(s, new CriterionProgress(this));
            }
        }

        this.field_192111_b = astring;
    }

    public boolean func_192105_a() {
        if (this.field_192111_b.length == 0) {
            return false;
        } else {
            String[][] astring = this.field_192111_b;
            int i = astring.length;
            int j = 0;

            while (j < i) {
                String[] astring1 = astring[j];
                boolean flag = false;
                String[] astring2 = astring1;
                int k = astring1.length;
                int l = 0;

                while (true) {
                    if (l < k) {
                        String s = astring2[l];
                        CriterionProgress criterionprogress = this.func_192106_c(s);

                        if (criterionprogress == null || !criterionprogress.func_192151_a()) {
                            ++l;
                            continue;
                        }

                        flag = true;
                    }

                    if (!flag) {
                        return false;
                    }

                    ++j;
                    break;
                }
            }

            return true;
        }
    }

    public boolean func_192108_b() {
        Iterator iterator = this.field_192110_a.values().iterator();

        CriterionProgress criterionprogress;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            criterionprogress = (CriterionProgress) iterator.next();
        } while (!criterionprogress.func_192151_a());

        return true;
    }

    public boolean func_192109_a(String s) {
        CriterionProgress criterionprogress = this.field_192110_a.get(s);

        if (criterionprogress != null && !criterionprogress.func_192151_a()) {
            criterionprogress.func_192153_b();
            return true;
        } else {
            return false;
        }
    }

    public boolean func_192101_b(String s) {
        CriterionProgress criterionprogress = this.field_192110_a.get(s);

        if (criterionprogress != null && criterionprogress.func_192151_a()) {
            criterionprogress.func_192154_c();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "AdvancementProgress{criteria=" + this.field_192110_a + ", requirements=" + Arrays.deepToString(this.field_192111_b) + '}';
    }

    public void func_192104_a(PacketBuffer packetdataserializer) {
        packetdataserializer.func_150787_b(this.field_192110_a.size());
        Iterator iterator = this.field_192110_a.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            packetdataserializer.func_180714_a((String) entry.getKey());
            ((CriterionProgress) entry.getValue()).func_192150_a(packetdataserializer);
        }

    }

    public static AdvancementProgress func_192100_b(PacketBuffer packetdataserializer) {
        AdvancementProgress advancementprogress = new AdvancementProgress();
        int i = packetdataserializer.func_150792_a();

        for (int j = 0; j < i; ++j) {
            advancementprogress.field_192110_a.put(packetdataserializer.func_150789_c(32767), CriterionProgress.func_192149_a(packetdataserializer, advancementprogress));
        }

        return advancementprogress;
    }

    @Nullable
    public CriterionProgress func_192106_c(String s) {
        return this.field_192110_a.get(s);
    }

    public Iterable<String> func_192107_d() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_192110_a.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!((CriterionProgress) entry.getValue()).func_192151_a()) {
                arraylist.add(entry.getKey());
            }
        }

        return arraylist;
    }

    public Iterable<String> func_192102_e() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_192110_a.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((CriterionProgress) entry.getValue()).func_192151_a()) {
                arraylist.add(entry.getKey());
            }
        }

        return arraylist;
    }

    @Nullable
    public Date func_193128_g() {
        Date date = null;
        Iterator iterator = this.field_192110_a.values().iterator();

        while (iterator.hasNext()) {
            CriterionProgress criterionprogress = (CriterionProgress) iterator.next();

            if (criterionprogress.func_192151_a() && (date == null || criterionprogress.func_193140_d().before(date))) {
                date = criterionprogress.func_193140_d();
            }
        }

        return date;
    }

    @Override
    public int compareTo(AdvancementProgress advancementprogress) {
        Date date = this.func_193128_g();
        Date date1 = advancementprogress.func_193128_g();

        return date == null && date1 != null ? 1 : (date != null && date1 == null ? -1 : (date == null && date1 == null ? 0 : date.compareTo(date1)));
    }

    public static class a implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress> {

        public a() {}

        @Override
        public JsonElement serialize(AdvancementProgress advancementprogress, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();
            JsonObject jsonobject1 = new JsonObject();
            Iterator iterator = advancementprogress.field_192110_a.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                CriterionProgress criterionprogress = (CriterionProgress) entry.getValue();

                if (criterionprogress.func_192151_a()) {
                    jsonobject1.add((String) entry.getKey(), criterionprogress.func_192148_e());
                }
            }

            if (!jsonobject1.entrySet().isEmpty()) {
                jsonobject.add("criteria", jsonobject1);
            }

            jsonobject.addProperty("done", Boolean.valueOf(advancementprogress.func_192105_a()));
            return jsonobject;
        }

        @Override
        public AdvancementProgress deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "advancement");
            JsonObject jsonobject1 = JsonUtils.func_151218_a(jsonobject, "criteria", new JsonObject());
            AdvancementProgress advancementprogress = new AdvancementProgress();
            Iterator iterator = jsonobject1.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                String s = (String) entry.getKey();

                advancementprogress.field_192110_a.put(s, CriterionProgress.func_192152_a(advancementprogress, JsonUtils.func_151206_a((JsonElement) entry.getValue(), s)));
            }

            return advancementprogress;
        }
    }
}
