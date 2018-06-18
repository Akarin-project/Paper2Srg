package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListWhitelistEntry extends UserListEntry<GameProfile> {

    public UserListWhitelistEntry(GameProfile gameprofile) {
        super(gameprofile);
    }

    public UserListWhitelistEntry(JsonObject jsonobject) {
        super(gameProfileFromJsonObject(jsonobject), jsonobject);
    }

    protected void onSerialization(JsonObject jsonobject) {
        if (this.getValue() != null) {
            jsonobject.addProperty("uuid", ((GameProfile) this.getValue()).getId() == null ? "" : ((GameProfile) this.getValue()).getId().toString());
            jsonobject.addProperty("name", ((GameProfile) this.getValue()).getName());
            super.onSerialization(jsonobject);
        }
    }

    private static GameProfile gameProfileFromJsonObject(JsonObject jsonobject) {
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
