package net.minecraft.world;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;


public class WorldServerMulti extends WorldServer {

    private final WorldServer delegate;

    // CraftBukkit start - Add WorldData, Environment and ChunkGenerator arguments
    public WorldServerMulti(MinecraftServer minecraftserver, ISaveHandler idatamanager, int i, WorldServer worldserver, Profiler methodprofiler, WorldInfo worldData, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(minecraftserver, idatamanager, worldData, i, methodprofiler, env, gen);
        // CraftBukkit end
        this.delegate = worldserver;
        /* CraftBukkit start
        worldserver.getWorldBorder().a(new IWorldBorderListener() {
            public void a(WorldBorder worldborder, double d0) {
                SecondaryWorldServer.this.getWorldBorder().setSize(d0);
            }

            public void a(WorldBorder worldborder, double d0, double d1, long i) {
                SecondaryWorldServer.this.getWorldBorder().transitionSizeBetween(d0, d1, i);
            }

            public void a(WorldBorder worldborder, double d0, double d1) {
                SecondaryWorldServer.this.getWorldBorder().setCenter(d0, d1);
            }

            public void a(WorldBorder worldborder, int i) {
                SecondaryWorldServer.this.getWorldBorder().setWarningTime(i);
            }

            public void b(WorldBorder worldborder, int i) {
                SecondaryWorldServer.this.getWorldBorder().setWarningDistance(i);
            }

            public void b(WorldBorder worldborder, double d0) {
                SecondaryWorldServer.this.getWorldBorder().setDamageAmount(d0);
            }

            public void c(WorldBorder worldborder, double d0) {
                SecondaryWorldServer.this.getWorldBorder().setDamageBuffer(d0);
            }
        });
        // CraftBukkit end */
    }

    // protected void a() {} // CraftBukkit

    public World init() {
        this.mapStorage = this.delegate.getMapStorage();
        this.worldScoreboard = this.delegate.getScoreboard();
        this.lootTable = this.delegate.getLootTableManager();
        this.advancementManager = this.delegate.getAdvancementManager();
        String s = VillageCollection.fileNameForProvider(this.provider);
        VillageCollection persistentvillage = (VillageCollection) this.mapStorage.getOrLoadData(VillageCollection.class, s);

        if (persistentvillage == null) {
            this.villageCollection = new VillageCollection(this);
            this.mapStorage.setData(s, this.villageCollection);
        } else {
            this.villageCollection = persistentvillage;
            this.villageCollection.setWorldsForAll((World) this);
        }

        return super.init(); // CraftBukkit
    }

    public void saveAdditionalData() {
        this.provider.onWorldSave();
    }
}
