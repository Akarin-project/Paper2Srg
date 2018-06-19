package net.minecraft.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minecraft.network.PacketBuffer;

public class CriterionProgress {

    private static final SimpleDateFormat field_192155_a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final AdvancementProgress field_192156_b;
    private Date field_192157_c;

    public CriterionProgress(AdvancementProgress advancementprogress) {
        this.field_192156_b = advancementprogress;
    }

    public boolean func_192151_a() {
        return this.field_192157_c != null;
    }

    public void func_192153_b() {
        this.field_192157_c = new Date();
    }

    public void func_192154_c() {
        this.field_192157_c = null;
    }

    public Date func_193140_d() {
        return this.field_192157_c;
    }

    public String toString() {
        return "CriterionProgress{obtained=" + (this.field_192157_c == null ? "false" : this.field_192157_c) + '}';
    }

    public void func_192150_a(PacketBuffer packetdataserializer) {
        packetdataserializer.writeBoolean(this.field_192157_c != null);
        if (this.field_192157_c != null) {
            packetdataserializer.func_192574_a(this.field_192157_c);
        }

    }

    public JsonElement func_192148_e() {
        return (JsonElement) (this.field_192157_c != null ? new JsonPrimitive(CriterionProgress.field_192155_a.format(this.field_192157_c)) : JsonNull.INSTANCE);
    }

    public static CriterionProgress func_192149_a(PacketBuffer packetdataserializer, AdvancementProgress advancementprogress) {
        CriterionProgress criterionprogress = new CriterionProgress(advancementprogress);

        if (packetdataserializer.readBoolean()) {
            criterionprogress.field_192157_c = packetdataserializer.func_192573_m();
        }

        return criterionprogress;
    }

    public static CriterionProgress func_192152_a(AdvancementProgress advancementprogress, String s) {
        CriterionProgress criterionprogress = new CriterionProgress(advancementprogress);

        try {
            criterionprogress.field_192157_c = CriterionProgress.field_192155_a.parse(s);
            return criterionprogress;
        } catch (ParseException parseexception) {
            throw new JsonSyntaxException("Invalid datetime: " + s, parseexception);
        }
    }
}
