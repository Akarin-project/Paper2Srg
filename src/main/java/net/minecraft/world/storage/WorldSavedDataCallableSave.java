package net.minecraft.world.storage;

public class WorldSavedDataCallableSave implements Runnable {

    private final WorldSavedData field_186338_a;

    public WorldSavedDataCallableSave(WorldSavedData persistentbase) {
        this.field_186338_a = persistentbase;
    }

    public void run() {
        this.field_186338_a.func_76185_a();
    }
}
