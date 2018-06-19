package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListOpsEntry extends UserListEntry<GameProfile> {

    private final int field_152645_a;
    private final boolean field_183025_b;

    public UserListOpsEntry(GameProfile gameprofile, int i, boolean flag) {
        super(gameprofile);
        this.field_152645_a = i;
        this.field_183025_b = flag;
    }

    public UserListOpsEntry(JsonObject jsonobject) {
        super(func_152643_b(jsonobject), jsonobject);
        this.field_152645_a = jsonobject.has("level") ? jsonobject.get("level").getAsInt() : 0;
        this.field_183025_b = jsonobject.has("bypassesPlayerLimit") && jsonobject.get("bypassesPlayerLimit").getAsBoolean();
    }

    public int func_152644_a() {
        return this.field_152645_a;
    }

    public boolean func_183024_b() {
        return this.field_183025_b;
    }

    protected void func_152641_a(JsonObject jsonobject) {
        if (this.func_152640_f() != null) {
            jsonobject.addProperty("uuid", ((GameProfile) this.func_152640_f()).getId() == null ? "" : ((GameProfile) this.func_152640_f()).getId().toString());
            jsonobject.addProperty("name", ((GameProfile) this.func_152640_f()).getName());
            super.func_152641_a(jsonobject);
            jsonobject.addProperty("level", Integer.valueOf(this.field_152645_a));
            jsonobject.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.field_183025_b));
        }
    }

    private static GameProfile func_152643_b(JsonObject jsonobject) {
        if (jsonobject.has("uuid") && jsonobject.has("name")) {
            String s = jsonobject.get("uuid").getAsString();

            UUID uuid;

            try {
                uuid = UUID.fromString(s);
            } catch (Throwable throwable) {
                return null;
            }

            return new GameProfile(uuid, jsonobject.get("name").getAsString());
        } else {
            return null;
        }
    }
}
