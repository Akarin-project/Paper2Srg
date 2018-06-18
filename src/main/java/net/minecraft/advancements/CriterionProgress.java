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

    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final AdvancementProgress advancementProgress;
    private Date obtained;

    public CriterionProgress(AdvancementProgress advancementprogress) {
        this.advancementProgress = advancementprogress;
    }

    public boolean isObtained() {
        return this.obtained != null;
    }

    public void obtain() {
        this.obtained = new Date();
    }

    public void reset() {
        this.obtained = null;
    }

    public Date getObtained() {
        return this.obtained;
    }

    public String toString() {
        return "CriterionProgress{obtained=" + (this.obtained == null ? "false" : this.obtained) + '}';
    }

    public void write(PacketBuffer packetdataserializer) {
        packetdataserializer.writeBoolean(this.obtained != null);
        if (this.obtained != null) {
            packetdataserializer.writeTime(this.obtained);
        }

    }

    public JsonElement serialize() {
        return (JsonElement) (this.obtained != null ? new JsonPrimitive(CriterionProgress.DATE_TIME_FORMATTER.format(this.obtained)) : JsonNull.INSTANCE);
    }

    public static CriterionProgress read(PacketBuffer packetdataserializer, AdvancementProgress advancementprogress) {
        CriterionProgress criterionprogress = new CriterionProgress(advancementprogress);

        if (packetdataserializer.readBoolean()) {
            criterionprogress.obtained = packetdataserializer.readTime();
        }

        return criterionprogress;
    }

    public static CriterionProgress fromDateTime(AdvancementProgress advancementprogress, String s) {
        CriterionProgress criterionprogress = new CriterionProgress(advancementprogress);

        try {
            criterionprogress.obtained = CriterionProgress.DATE_TIME_FORMATTER.parse(s);
            return criterionprogress;
        } catch (ParseException parseexception) {
            throw new JsonSyntaxException("Invalid datetime: " + s, parseexception);
        }
    }
}
