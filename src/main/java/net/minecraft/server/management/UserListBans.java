package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListBans extends UserList<GameProfile, UserListBansEntry> {

    public UserListBans(File file) {
        super(file);
    }

    protected UserListEntry<GameProfile> createEntry(JsonObject jsonobject) {
        return new UserListBansEntry(jsonobject);
    }

    public boolean isBanned(GameProfile gameprofile) {
        return this.hasEntry(gameprofile);
    }

    public String[] getKeys() {
        String[] astring = new String[this.getValues().size()];
        int i = 0;

        UserListBansEntry gameprofilebanentry;

        for (Iterator iterator = this.getValues().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile) gameprofilebanentry.getValue()).getName()) {
            gameprofilebanentry = (UserListBansEntry) iterator.next();
        }

        return astring;
    }

    protected String getObjectKey(GameProfile gameprofile) {
        return gameprofile.getId().toString();
    }

    public GameProfile getBannedProfile(String s) {
        Iterator iterator = this.getValues().values().iterator();

        UserListBansEntry gameprofilebanentry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            gameprofilebanentry = (UserListBansEntry) iterator.next();
        } while (!s.equalsIgnoreCase(((GameProfile) gameprofilebanentry.getValue()).getName()));

        return (GameProfile) gameprofilebanentry.getValue();
    }

    protected String getObjectKey(Object object) {
        return this.getObjectKey((GameProfile) object);
    }
}
