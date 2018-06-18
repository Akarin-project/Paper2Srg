package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.UUID;

public class UserListBansEntry extends UserListEntryBan<GameProfile> {

    public UserListBansEntry(GameProfile gameprofile) {
        this(gameprofile, (Date) null, (String) null, (Date) null, (String) null);
    }

    public UserListBansEntry(GameProfile gameprofile, Date date, String s, Date date1, String s1) {
        super(gameprofile, date, s, date1, s1); // Spigot
    }

    public UserListBansEntry(JsonObject jsonobject) {
        super(toGameProfile(jsonobject), jsonobject);
    }

    protected void onSerialization(JsonObject jsonobject) {
        if (this.getValue() != null) {
            jsonobject.addProperty("uuid", ((GameProfile) this.getValue()).getId() == null ? "" : ((GameProfile) this.getValue()).getId().toString());
            jsonobject.addProperty("name", ((GameProfile) this.getValue()).getName());
            super.onSerialization(jsonobject);
        }
    }

    private static GameProfile toGameProfile(JsonObject jsonobject) {
        // Spigot start
        // this whole method has to be reworked to account for the fact Bukkit only accepts UUID bans and gives no way for usernames to be stored!
        UUID uuid = null;
        String name = null;
        if (jsonobject.has("uuid")) {
            String s = jsonobject.get("uuid").getAsString();

            try {
                uuid = UUID.fromString(s);
            } catch (Throwable throwable) {
            }

        }
        if ( jsonobject.has("name"))
        {
            name = jsonobject.get("name").getAsString();
        }
        if ( uuid != null || name != null )
        {
            return new GameProfile( uuid, name );
        } else {
            return null;
        }
        // Spigot End
    }
}
