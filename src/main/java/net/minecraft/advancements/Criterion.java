package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class Criterion {

    private final ICriterionInstance criterionInstance;

    public Criterion(ICriterionInstance criterioninstance) {
        this.criterionInstance = criterioninstance;
    }

    public Criterion() {
        this.criterionInstance = null;
    }

    public void serializeToNetwork(PacketBuffer packetdataserializer) {}

    public static Criterion criterionFromJson(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "trigger"));
        ICriterionTrigger criteriontrigger = CriteriaTriggers.get(minecraftkey);

        if (criteriontrigger == null) {
            throw new JsonSyntaxException("Invalid criterion trigger: " + minecraftkey);
        } else {
            ICriterionInstance criterioninstance = criteriontrigger.deserializeInstance(JsonUtils.getJsonObject(jsonobject, "conditions", new JsonObject()), jsondeserializationcontext);

            return new Criterion(criterioninstance);
        }
    }

    public static Criterion criterionFromNetwork(PacketBuffer packetdataserializer) {
        return new Criterion();
    }

    public static Map<String, Criterion> criteriaFromJson(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = jsonobject.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            hashmap.put(entry.getKey(), criterionFromJson(JsonUtils.getJsonObject((JsonElement) entry.getValue(), "criterion"), jsondeserializationcontext));
        }

        return hashmap;
    }

    public static Map<String, Criterion> criteriaFromNetwork(PacketBuffer packetdataserializer) {
        HashMap hashmap = Maps.newHashMap();
        int i = packetdataserializer.readVarInt();

        for (int j = 0; j < i; ++j) {
            hashmap.put(packetdataserializer.readString(32767), criterionFromNetwork(packetdataserializer));
        }

        return hashmap;
    }

    public static void serializeToNetwork(Map<String, Criterion> map, PacketBuffer packetdataserializer) {
        packetdataserializer.writeVarInt(map.size());
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            packetdataserializer.writeString((String) entry.getKey());
            ((Criterion) entry.getValue()).serializeToNetwork(packetdataserializer);
        }

    }

    @Nullable
    public ICriterionInstance getCriterionInstance() {
        return this.criterionInstance;
    }
}
