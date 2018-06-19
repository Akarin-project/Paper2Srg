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
        super(func_152648_b(jsonobject), jsonobject);
    }

    protected void func_152641_a(JsonObject jsonobject) {
        if (this.func_152640_f() != null) {
            jsonobject.addProperty("uuid", ((GameProfile) this.func_152640_f()).getId() == null ? "" : ((GameProfile) this.func_152640_f()).getId().toString());
            jsonobject.addProperty("name", ((GameProfile) this.func_152640_f()).getName());
            super.func_152641_a(jsonobject);
        }
    }

    private static GameProfile func_152648_b(JsonObject jsonobject) {
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
