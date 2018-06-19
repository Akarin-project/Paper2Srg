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

    private final ICriterionInstance field_192147_a;

    public Criterion(ICriterionInstance criterioninstance) {
        this.field_192147_a = criterioninstance;
    }

    public Criterion() {
        this.field_192147_a = null;
    }

    public void func_192140_a(PacketBuffer packetdataserializer) {}

    public static Criterion func_192145_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "trigger"));
        ICriterionTrigger criteriontrigger = CriteriaTriggers.func_192119_a(minecraftkey);

        if (criteriontrigger == null) {
            throw new JsonSyntaxException("Invalid criterion trigger: " + minecraftkey);
        } else {
            ICriterionInstance criterioninstance = criteriontrigger.func_192166_a(JsonUtils.func_151218_a(jsonobject, "conditions", new JsonObject()), jsondeserializationcontext);

            return new Criterion(criterioninstance);
        }
    }

    public static Criterion func_192146_b(PacketBuffer packetdataserializer) {
        return new Criterion();
    }

    public static Map<String, Criterion> func_192144_b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = jsonobject.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            hashmap.put(entry.getKey(), func_192145_a(JsonUtils.func_151210_l((JsonElement) entry.getValue(), "criterion"), jsondeserializationcontext));
        }

        return hashmap;
    }

    public static Map<String, Criterion> func_192142_c(PacketBuffer packetdataserializer) {
        HashMap hashmap = Maps.newHashMap();
        int i = packetdataserializer.func_150792_a();

        for (int j = 0; j < i; ++j) {
            hashmap.put(packetdataserializer.func_150789_c(32767), func_192146_b(packetdataserializer));
        }

        return hashmap;
    }

    public static void func_192141_a(Map<String, Criterion> map, PacketBuffer packetdataserializer) {
        packetdataserializer.func_150787_b(map.size());
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            packetdataserializer.func_180714_a((String) entry.getKey());
            ((Criterion) entry.getValue()).func_192140_a(packetdataserializer);
        }

    }

    @Nullable
    public ICriterionInstance func_192143_a() {
        return this.field_192147_a;
    }
}
