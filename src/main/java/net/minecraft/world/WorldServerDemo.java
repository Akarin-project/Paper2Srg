package net.minecraft.world;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;


public class WorldServerDemo extends WorldServer {

    private static final long field_73072_L = (long) "North Carolina".hashCode();
    public static final WorldSettings field_73071_a = (new WorldSettings(WorldServerDemo.field_73072_L, GameType.SURVIVAL, true, false, WorldType.field_77137_b)).func_77159_a();

    public WorldServerDemo(MinecraftServer minecraftserver, ISaveHandler idatamanager, WorldInfo worlddata, int i, Profiler methodprofiler) {
        super(minecraftserver, idatamanager, worlddata, i, methodprofiler);
        this.field_72986_A.func_176127_a(WorldServerDemo.field_73071_a);
    }
}
