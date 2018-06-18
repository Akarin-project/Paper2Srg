package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListOps extends UserList<GameProfile, UserListOpsEntry> {

    public UserListOps(File file) {
        super(file);
    }

    protected UserListEntry<GameProfile> createEntry(JsonObject jsonobject) {
        return new UserListOpsEntry(jsonobject);
    }

    public String[] getKeys() {
        String[] astring = new String[this.getValues().size()];
        int i = 0;

        UserListOpsEntry oplistentry;

        for (Iterator iterator = this.getValues().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile) oplistentry.getValue()).getName()) {
            oplistentry = (UserListOpsEntry) iterator.next();
        }

        return astring;
    }

    public int getObjectKey(GameProfile gameprofile) {
        UserListOpsEntry oplistentry = (UserListOpsEntry) this.getEntry(gameprofile);

        return oplistentry != null ? oplistentry.getPermissionLevel() : 0;
    }

    public boolean bypassesPlayerLimit(GameProfile gameprofile) {
        UserListOpsEntry oplistentry = (UserListOpsEntry) this.getEntry(gameprofile);

        return oplistentry != null ? oplistentry.bypassesPlayerLimit() : false;
    }

    protected String getObjectKey(GameProfile gameprofile) {
        return gameprofile.getId().toString();
    }

    public GameProfile getGameProfileFromName(String s) {
        Iterator iterator = this.getValues().values().iterator();

        UserListOpsEntry oplistentry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            oplistentry = (UserListOpsEntry) iterator.next();
        } while (!s.equalsIgnoreCase(((GameProfile) oplistentry.getValue()).getName()));

        return (GameProfile) oplistentry.getValue();
    }

    protected String getObjectKey(Object object) {
        return this.getObjectKey((GameProfile) object);
    }
}
