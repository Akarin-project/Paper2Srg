package net.minecraft.world;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;


public class WorldServerMulti extends WorldServer {

    private final WorldServer field_175743_a;

    // CraftBukkit start - Add WorldData, Environment and ChunkGenerator arguments
    public WorldServerMulti(MinecraftServer minecraftserver, ISaveHandler idatamanager, int i, WorldServer worldserver, Profiler methodprofiler, WorldInfo worldData, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(minecraftserver, idatamanager, worldData, i, methodprofiler, env, gen);
        // CraftBukkit end
        this.field_175743_a = worldserver;
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

    public World func_175643_b() {
        this.field_72988_C = this.field_175743_a.func_175693_T();
        this.field_96442_D = this.field_175743_a.func_96441_U();
        this.field_184151_B = this.field_175743_a.func_184146_ak();
        this.field_191951_C = this.field_175743_a.func_191952_z();
        String s = VillageCollection.func_176062_a(this.field_73011_w);
        VillageCollection persistentvillage = (VillageCollection) this.field_72988_C.func_75742_a(VillageCollection.class, s);

        if (persistentvillage == null) {
            this.field_72982_D = new VillageCollection(this);
            this.field_72988_C.func_75745_a(s, this.field_72982_D);
        } else {
            this.field_72982_D = persistentvillage;
            this.field_72982_D.func_82566_a((World) this);
        }

        return super.func_175643_b(); // CraftBukkit
    }

    public void func_184166_c() {
        this.field_73011_w.func_186057_q();
    }
}
