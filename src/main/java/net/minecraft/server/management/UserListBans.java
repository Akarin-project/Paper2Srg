package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListBans extends UserList<GameProfile, UserListBansEntry> {

    public UserListBans(File file) {
        super(file);
    }

    @Override
    protected UserListEntry<GameProfile> func_152682_a(JsonObject jsonobject) {
        return new UserListBansEntry(jsonobject);
    }

    public boolean func_152702_a(GameProfile gameprofile) {
        return this.func_152692_d(gameprofile);
    }

    @Override
    public String[] func_152685_a() {
        String[] astring = new String[this.func_152688_e().size()];
        int i = 0;

        UserListBansEntry gameprofilebanentry;

        for (Iterator iterator = this.func_152688_e().values().iterator(); iterator.hasNext(); astring[i++] = gameprofilebanentry.func_152640_f().getName()) {
            gameprofilebanentry = (UserListBansEntry) iterator.next();
        }

        return astring;
    }

    @Override
    protected String func_152681_a(GameProfile gameprofile) {
        return gameprofile.getId().toString();
    }

    public GameProfile func_152703_a(String s) {
        Iterator iterator = this.func_152688_e().values().iterator();

        UserListBansEntry gameprofilebanentry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            gameprofilebanentry = (UserListBansEntry) iterator.next();
        } while (!s.equalsIgnoreCase(gameprofilebanentry.func_152640_f().getName()));

        return gameprofilebanentry.func_152640_f();
    }
}
