package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListOpsEntry extends UserListEntry<GameProfile> {

    private final int permissionLevel;
    private final boolean bypassesPlayerLimit;

    public UserListOpsEntry(GameProfile gameprofile, int i, boolean flag) {
        super(gameprofile);
        this.permissionLevel = i;
        this.bypassesPlayerLimit = flag;
    }

    public UserListOpsEntry(JsonObject jsonobject) {
        super(constructProfile(jsonobject), jsonobject);
        this.permissionLevel = jsonobject.has("level") ? jsonobject.get("level").getAsInt() : 0;
        this.bypassesPlayerLimit = jsonobject.has("bypassesPlayerLimit") && jsonobject.get("bypassesPlayerLimit").getAsBoolean();
    }

    public int getPermissionLevel() {
        return this.permissionLevel;
    }

    public boolean bypassesPlayerLimit() {
        return this.bypassesPlayerLimit;
    }

    protected void onSerialization(JsonObject jsonobject) {
        if (this.getValue() != null) {
            jsonobject.addProperty("uuid", ((GameProfile) this.getValue()).getId() == null ? "" : ((GameProfile) this.getValue()).getId().toString());
            jsonobject.addProperty("name", ((GameProfile) this.getValue()).getName());
            super.onSerialization(jsonobject);
            jsonobject.addProperty("level", Integer.valueOf(this.permissionLevel));
            jsonobject.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.bypassesPlayerLimit));
        }
    }

    private static GameProfile constructProfile(JsonObject jsonobject) {
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
