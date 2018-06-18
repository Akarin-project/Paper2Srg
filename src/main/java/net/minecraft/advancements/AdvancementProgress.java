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
import net.minecraft.server.AdvancementProgress.a;
import net.minecraft.util.JsonUtils;

public class AdvancementProgress implements Comparable<AdvancementProgress> {

    private final Map<String, CriterionProgress> criteria = Maps.newHashMap();
    private String[][] requirements = new String[0][];

    public AdvancementProgress() {}

    public void update(Map<String, Criterion> map, String[][] astring) {
        Set set = map.keySet();
        Iterator iterator = this.criteria.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!set.contains(entry.getKey())) {
                iterator.remove();
            }
        }

        iterator = set.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (!this.criteria.containsKey(s)) {
                this.criteria.put(s, new CriterionProgress(this));
            }
        }

        this.requirements = astring;
    }

    public boolean isDone() {
        if (this.requirements.length == 0) {
            return false;
        } else {
            String[][] astring = this.requirements;
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
                        CriterionProgress criterionprogress = this.getCriterionProgress(s);

                        if (criterionprogress == null || !criterionprogress.isObtained()) {
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

    public boolean hasProgress() {
        Iterator iterator = this.criteria.values().iterator();

        CriterionProgress criterionprogress;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            criterionprogress = (CriterionProgress) iterator.next();
        } while (!criterionprogress.isObtained());

        return true;
    }

    public boolean grantCriterion(String s) {
        CriterionProgress criterionprogress = (CriterionProgress) this.criteria.get(s);

        if (criterionprogress != null && !criterionprogress.isObtained()) {
            criterionprogress.obtain();
            return true;
        } else {
            return false;
        }
    }

    public boolean revokeCriterion(String s) {
        CriterionProgress criterionprogress = (CriterionProgress) this.criteria.get(s);

        if (criterionprogress != null && criterionprogress.isObtained()) {
            criterionprogress.reset();
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return "AdvancementProgress{criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + '}';
    }

    public void serializeToNetwork(PacketBuffer packetdataserializer) {
        packetdataserializer.writeVarInt(this.criteria.size());
        Iterator iterator = this.criteria.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            packetdataserializer.writeString((String) entry.getKey());
            ((CriterionProgress) entry.getValue()).write(packetdataserializer);
        }

    }

    public static AdvancementProgress fromNetwork(PacketBuffer packetdataserializer) {
        AdvancementProgress advancementprogress = new AdvancementProgress();
        int i = packetdataserializer.readVarInt();

        for (int j = 0; j < i; ++j) {
            advancementprogress.criteria.put(packetdataserializer.readString(32767), CriterionProgress.read(packetdataserializer, advancementprogress));
        }

        return advancementprogress;
    }

    @Nullable
    public CriterionProgress getCriterionProgress(String s) {
        return (CriterionProgress) this.criteria.get(s);
    }

    public Iterable<String> getRemaningCriteria() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.criteria.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!((CriterionProgress) entry.getValue()).isObtained()) {
                arraylist.add(entry.getKey());
            }
        }

        return arraylist;
    }

    public Iterable<String> getCompletedCriteria() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.criteria.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((CriterionProgress) entry.getValue()).isObtained()) {
                arraylist.add(entry.getKey());
            }
        }

        return arraylist;
    }

    @Nullable
    public Date getFirstProgressDate() {
        Date date = null;
        Iterator iterator = this.criteria.values().iterator();

        while (iterator.hasNext()) {
            CriterionProgress criterionprogress = (CriterionProgress) iterator.next();

            if (criterionprogress.isObtained() && (date == null || criterionprogress.getObtained().before(date))) {
                date = criterionprogress.getObtained();
            }
        }

        return date;
    }

    public int compareTo(AdvancementProgress advancementprogress) {
        Date date = this.getFirstProgressDate();
        Date date1 = advancementprogress.getFirstProgressDate();

        return date == null && date1 != null ? 1 : (date != null && date1 == null ? -1 : (date == null && date1 == null ? 0 : date.compareTo(date1)));
    }

    public int compareTo(Object object) {
        return this.compareTo((AdvancementProgress) object);
    }

    public static class a implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress> {

        public a() {}

        public JsonElement a(AdvancementProgress advancementprogress, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();
            JsonObject jsonobject1 = new JsonObject();
            Iterator iterator = advancementprogress.criteria.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                CriterionProgress criterionprogress = (CriterionProgress) entry.getValue();

                if (criterionprogress.isObtained()) {
                    jsonobject1.add((String) entry.getKey(), criterionprogress.serialize());
                }
            }

            if (!jsonobject1.entrySet().isEmpty()) {
                jsonobject.add("criteria", jsonobject1);
            }

            jsonobject.addProperty("done", Boolean.valueOf(advancementprogress.isDone()));
            return jsonobject;
        }

        public AdvancementProgress a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "advancement");
            JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "criteria", new JsonObject());
            AdvancementProgress advancementprogress = new AdvancementProgress();
            Iterator iterator = jsonobject1.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                String s = (String) entry.getKey();

                advancementprogress.criteria.put(s, CriterionProgress.fromDateTime(advancementprogress, JsonUtils.getString((JsonElement) entry.getValue(), s)));
            }

            return advancementprogress;
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
            return this.a((AdvancementProgress) object, type, jsonserializationcontext);
        }
    }
}
