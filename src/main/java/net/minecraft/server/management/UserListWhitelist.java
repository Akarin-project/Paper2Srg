package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListWhitelist extends UserList<GameProfile, UserListWhitelistEntry> {

    public UserListWhitelist(File file) {
        super(file);
    }

    protected UserListEntry<GameProfile> createEntry(JsonObject jsonobject) {
        return new UserListWhitelistEntry(jsonobject);
    }

    public boolean isWhitelisted(GameProfile gameprofile) {
        return this.hasEntry(gameprofile);
    }

    public String[] getKeys() {
        String[] astring = new String[this.getValues().size()];
        int i = 0;

        UserListWhitelistEntry whitelistentry;

        for (Iterator iterator = this.getValues().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile) whitelistentry.getValue()).getName()) {
            whitelistentry = (UserListWhitelistEntry) iterator.next();
        }

        return astring;
    }

    protected String getObjectKey(GameProfile gameprofile) {
        return gameprofile.getId().toString();
    }

    public GameProfile getByName(String s) {
        Iterator iterator = this.getValues().values().iterator();

        UserListWhitelistEntry whitelistentry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            whitelistentry = (UserListWhitelistEntry) iterator.next();
        } while (!s.equalsIgnoreCase(((GameProfile) whitelistentry.getValue()).getName()));

        return (GameProfile) whitelistentry.getValue();
    }

    protected String getObjectKey(Object object) {
        return this.getObjectKey((GameProfile) object);
    }
}
