package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.util.Date;

public class UserListIPBansEntry extends UserListEntryBan<String> {

    public UserListIPBansEntry(String s) {
        this(s, (Date) null, (String) null, (Date) null, (String) null);
    }

    public UserListIPBansEntry(String s, Date date, String s1, Date date1, String s2) {
        super(s, date, s1, date1, s2);
    }

    public UserListIPBansEntry(JsonObject jsonobject) {
        super(getIPFromJson(jsonobject), jsonobject);
    }

    private static String getIPFromJson(JsonObject jsonobject) {
        return jsonobject.has("ip") ? jsonobject.get("ip").getAsString() : null;
    }

    protected void onSerialization(JsonObject jsonobject) {
        if (this.getValue() != null) {
            jsonobject.addProperty("ip", (String) this.getValue());
            super.onSerialization(jsonobject);
        }
    }
}
