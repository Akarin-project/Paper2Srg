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
        super(func_152647_b(jsonobject), jsonobject);
    }

    private static String func_152647_b(JsonObject jsonobject) {
        return jsonobject.has("ip") ? jsonobject.get("ip").getAsString() : null;
    }

    protected void func_152641_a(JsonObject jsonobject) {
        if (this.func_152640_f() != null) {
            jsonobject.addProperty("ip", (String) this.func_152640_f());
            super.func_152641_a(jsonobject);
        }
    }
}
