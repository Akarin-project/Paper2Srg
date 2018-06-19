package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry<T> {

    private final T field_152642_a;

    public UserListEntry(T t0) {
        this.field_152642_a = t0;
    }

    protected UserListEntry(T t0, JsonObject jsonobject) {
        this.field_152642_a = t0;
    }

    public T func_152640_f() {
        return this.field_152642_a;
    }

    boolean func_73682_e() {
        return false;
    }

    protected void func_152641_a(JsonObject jsonobject) {}
}
