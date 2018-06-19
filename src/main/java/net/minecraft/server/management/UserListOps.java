package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListOps extends UserList<GameProfile, UserListOpsEntry> {

    public UserListOps(File file) {
        super(file);
    }

    protected UserListEntry<GameProfile> func_152682_a(JsonObject jsonobject) {
        return new UserListOpsEntry(jsonobject);
    }

    public String[] func_152685_a() {
        String[] astring = new String[this.func_152688_e().size()];
        int i = 0;

        UserListOpsEntry oplistentry;

        for (Iterator iterator = this.func_152688_e().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile) oplistentry.func_152640_f()).getName()) {
            oplistentry = (UserListOpsEntry) iterator.next();
        }

        return astring;
    }

    public int func_152681_a(GameProfile gameprofile) {
        UserListOpsEntry oplistentry = (UserListOpsEntry) this.func_152683_b(gameprofile);

        return oplistentry != null ? oplistentry.func_152644_a() : 0;
    }

    public boolean func_183026_b(GameProfile gameprofile) {
        UserListOpsEntry oplistentry = (UserListOpsEntry) this.func_152683_b(gameprofile);

        return oplistentry != null ? oplistentry.func_183024_b() : false;
    }

    protected String func_152681_a(GameProfile gameprofile) {
        return gameprofile.getId().toString();
    }

    public GameProfile func_152700_a(String s) {
        Iterator iterator = this.func_152688_e().values().iterator();

        UserListOpsEntry oplistentry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            oplistentry = (UserListOpsEntry) iterator.next();
        } while (!s.equalsIgnoreCase(((GameProfile) oplistentry.func_152640_f()).getName()));

        return (GameProfile) oplistentry.func_152640_f();
    }

    protected String func_152681_a(Object object) {
        return this.func_152681_a((GameProfile) object);
    }
}
