package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.io.File;
import java.net.SocketAddress;

public class UserListIPBans extends UserList<String, UserListIPBansEntry> {

    public UserListIPBans(File file) {
        super(file);
    }

    protected UserListEntry<String> createEntry(JsonObject jsonobject) {
        return new UserListIPBansEntry(jsonobject);
    }

    public boolean isBanned(SocketAddress socketaddress) {
        String s = this.addressToString(socketaddress);

        return this.hasEntry(s);
    }

    public UserListIPBansEntry getBanEntry(SocketAddress socketaddress) {
        String s = this.addressToString(socketaddress);

        return (UserListIPBansEntry) this.getBanEntry((Object) s);
    }

    private String addressToString(SocketAddress socketaddress) {
        String s = socketaddress.toString();

        if (s.contains("/")) {
            s = s.substring(s.indexOf(47) + 1);
        }

        if (s.contains(":")) {
            s = s.substring(0, s.indexOf(58));
        }

        return s;
    }
}
