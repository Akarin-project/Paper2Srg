package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.io.File;
import java.net.SocketAddress;

public class UserListIPBans extends UserList<String, UserListIPBansEntry> {

    public UserListIPBans(File file) {
        super(file);
    }

    @Override
    protected UserListEntry<String> func_152682_a(JsonObject jsonobject) {
        return new UserListIPBansEntry(jsonobject);
    }

    public boolean func_152708_a(SocketAddress socketaddress) {
        String s = this.func_152707_c(socketaddress);

        return this.func_152692_d(s);
    }

    public UserListIPBansEntry func_152709_b(SocketAddress socketaddress) {
        String s = this.func_152707_c(socketaddress);

        return this.func_152683_b(s);
    }

    private String func_152707_c(SocketAddress socketaddress) {
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
