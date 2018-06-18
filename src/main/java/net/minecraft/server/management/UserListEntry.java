package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry<T> {

    private final T value;

    public UserListEntry(T t0) {
        this.value = t0;
    }

    protected UserListEntry(T t0, JsonObject jsonobject) {
        this.value = t0;
    }

    public T getValue() {
        return this.value;
    }

    boolean hasBanExpired() {
        return false;
    }

    protected void onSerialization(JsonObject jsonobject) {}
}
