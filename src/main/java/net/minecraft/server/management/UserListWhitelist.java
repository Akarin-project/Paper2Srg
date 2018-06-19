package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListWhitelist extends UserList<GameProfile, UserListWhitelistEntry> {

    public UserListWhitelist(File file) {
        super(file);
    }

    protected UserListEntry<GameProfile> func_152682_a(JsonObject jsonobject) {
        return new UserListWhitelistEntry(jsonobject);
    }

    public boolean func_152705_a(GameProfile gameprofile) {
        return this.func_152692_d(gameprofile);
    }

    public String[] func_152685_a() {
        String[] astring = new String[this.func_152688_e().size()];
        int i = 0;

        UserListWhitelistEntry whitelistentry;

        for (Iterator iterator = this.func_152688_e().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile) whitelistentry.func_152640_f()).getName()) {
            whitelistentry = (UserListWhitelistEntry) iterator.next();
        }

        return astring;
    }

    protected String func_152681_a(GameProfile gameprofile) {
        return gameprofile.getId().toString();
    }

    public GameProfile func_152706_a(String s) {
        Iterator iterator = this.func_152688_e().values().iterator();

        UserListWhitelistEntry whitelistentry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            whitelistentry = (UserListWhitelistEntry) iterator.next();
        } while (!s.equalsIgnoreCase(((GameProfile) whitelistentry.func_152640_f()).getName()));

        return (GameProfile) whitelistentry.func_152640_f();
    }

    protected String func_152681_a(Object object) {
        return this.func_152681_a((GameProfile) object);
    }
}
