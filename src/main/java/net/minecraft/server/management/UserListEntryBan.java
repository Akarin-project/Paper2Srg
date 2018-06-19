package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class UserListEntryBan<T> extends UserListEntry<T> {

    public static final SimpleDateFormat field_73698_a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    protected final Date field_73694_d;
    protected final String field_73695_e;
    protected final Date field_73692_f;
    protected final String field_73693_g;

    public UserListEntryBan(T t0, Date date, String s, Date date1, String s1) {
        super(t0);
        this.field_73694_d = date == null ? new Date() : date;
        this.field_73695_e = s == null ? "(Unknown)" : s;
        this.field_73692_f = date1;
        this.field_73693_g = s1 == null ? "Banned by an operator." : s1;
    }

    protected UserListEntryBan(T t0, JsonObject jsonobject) {
        super(checkExpiry(t0, jsonobject), jsonobject);

        Date date;

        try {
            date = jsonobject.has("created") ? UserListEntryBan.field_73698_a.parse(jsonobject.get("created").getAsString()) : new Date();
        } catch (ParseException parseexception) {
            date = new Date();
        }

        this.field_73694_d = date;
        this.field_73695_e = jsonobject.has("source") ? jsonobject.get("source").getAsString() : "(Unknown)";

        Date date1;

        try {
            date1 = jsonobject.has("expires") ? UserListEntryBan.field_73698_a.parse(jsonobject.get("expires").getAsString()) : null;
        } catch (ParseException parseexception1) {
            date1 = null;
        }

        this.field_73692_f = date1;
        this.field_73693_g = jsonobject.has("reason") ? jsonobject.get("reason").getAsString() : "Banned by an operator.";
    }

    public Date func_73680_d() {
        return this.field_73692_f;
    }

    public String func_73686_f() {
        return this.field_73693_g;
    }

    boolean func_73682_e() {
        return this.field_73692_f == null ? false : this.field_73692_f.before(new Date());
    }

    protected void func_152641_a(JsonObject jsonobject) {
        jsonobject.addProperty("created", UserListEntryBan.field_73698_a.format(this.field_73694_d));
        jsonobject.addProperty("source", this.field_73695_e);
        jsonobject.addProperty("expires", this.field_73692_f == null ? "forever" : UserListEntryBan.field_73698_a.format(this.field_73692_f));
        jsonobject.addProperty("reason", this.field_73693_g);
    }

    // CraftBukkit start
    public String getSource() {
        return this.field_73695_e;
    }

    public Date getCreated() {
        return this.field_73694_d;
    }

    private static <T> T checkExpiry(T object, JsonObject jsonobject) {
        Date expires = null;

        try {
            expires = jsonobject.has("expires") ? field_73698_a.parse(jsonobject.get("expires").getAsString()) : null;
        } catch (ParseException ex) {
            // Guess we don't have a date
        }

        if (expires == null || expires.after(new Date())) {
            return object;
        } else {
            return null;
        }
    }
    // CraftBukkit end
}
