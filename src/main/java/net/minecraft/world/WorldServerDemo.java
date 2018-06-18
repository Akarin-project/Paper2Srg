package net.minecraft.world;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;


public class WorldServerDemo extends WorldServer {

    private static final long DEMO_WORLD_SEED = (long) "North Carolina".hashCode();
    public static final WorldSettings DEMO_WORLD_SETTINGS = (new WorldSettings(WorldServerDemo.DEMO_WORLD_SEED, GameType.SURVIVAL, true, false, WorldType.DEFAULT)).enableBonusChest();

    public WorldServerDemo(MinecraftServer minecraftserver, ISaveHandler idatamanager, WorldInfo worlddata, int i, Profiler methodprofiler) {
        super(minecraftserver, idatamanager, worlddata, i, methodprofiler);
        this.worldInfo.populateFromWorldSettings(WorldServerDemo.DEMO_WORLD_SETTINGS);
    }
}
