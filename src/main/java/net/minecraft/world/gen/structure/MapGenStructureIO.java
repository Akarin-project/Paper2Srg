package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MapGenStructureIO {

    private static final Logger field_151687_a = LogManager.getLogger();
    private static final Map<String, Class<? extends StructureStart>> field_143040_a = Maps.newHashMap();
    private static final Map<Class<? extends StructureStart>, String> field_143038_b = Maps.newHashMap();
    private static final Map<String, Class<? extends StructureComponent>> field_143039_c = Maps.newHashMap();
    private static final Map<Class<? extends StructureComponent>, String> field_143037_d = Maps.newHashMap();

    private static void func_143034_b(Class<? extends StructureStart> oclass, String s) {
        MapGenStructureIO.field_143040_a.put(s, oclass);
        MapGenStructureIO.field_143038_b.put(oclass, s);
    }

    static void func_143031_a(Class<? extends StructureComponent> oclass, String s) {
        MapGenStructureIO.field_143039_c.put(s, oclass);
        MapGenStructureIO.field_143037_d.put(oclass, s);
    }

    public static String func_143033_a(StructureStart structurestart) {
        return MapGenStructureIO.field_143038_b.get(structurestart.getClass());
    }

    public static String func_143036_a(StructureComponent structurepiece) {
        return MapGenStructureIO.field_143037_d.get(structurepiece.getClass());
    }

    @Nullable
    public static StructureStart func_143035_a(NBTTagCompound nbttagcompound, World world) {
        StructureStart structurestart = null;

        try {
            Class oclass = MapGenStructureIO.field_143040_a.get(nbttagcompound.func_74779_i("id"));

            if (oclass != null) {
                structurestart = (StructureStart) oclass.newInstance();
            }
        } catch (Exception exception) {
            MapGenStructureIO.field_151687_a.warn("Failed Start with id {}", nbttagcompound.func_74779_i("id"));
            exception.printStackTrace();
        }

        if (structurestart != null) {
            structurestart.func_143020_a(world, nbttagcompound);
        } else {
            MapGenStructureIO.field_151687_a.warn("Skipping Structure with id {}", nbttagcompound.func_74779_i("id"));
        }

        return structurestart;
    }

    public static StructureComponent func_143032_b(NBTTagCompound nbttagcompound, World world) {
        StructureComponent structurepiece = null;

        try {
            Class oclass = MapGenStructureIO.field_143039_c.get(nbttagcompound.func_74779_i("id"));

            if (oclass != null) {
                structurepiece = (StructureComponent) oclass.newInstance();
            }
        } catch (Exception exception) {
            MapGenStructureIO.field_151687_a.warn("Failed Piece with id {}", nbttagcompound.func_74779_i("id"));
            exception.printStackTrace();
        }

        if (structurepiece != null) {
            structurepiece.func_143009_a(world, nbttagcompound);
        } else {
            MapGenStructureIO.field_151687_a.warn("Skipping Piece with id {}", nbttagcompound.func_74779_i("id"));
        }

        return structurepiece;
    }

    static {
        func_143034_b(StructureMineshaftStart.class, "Mineshaft");
        func_143034_b(MapGenVillage.Start.class, "Village");
        func_143034_b(MapGenNetherBridge.Start.class, "Fortress");
        func_143034_b(MapGenStronghold.Start.class, "Stronghold");
        func_143034_b(MapGenScatteredFeature.Start.class, "Temple");
        func_143034_b(StructureOceanMonument.StartMonument.class, "Monument");
        func_143034_b(MapGenEndCity.Start.class, "EndCity");
        func_143034_b(WoodlandMansion.a.class, "Mansion");
        StructureMineshaftPieces.func_143048_a();
        StructureVillagePieces.func_143016_a();
        StructureNetherBridgePieces.func_143049_a();
        StructureStrongholdPieces.func_143046_a();
        ComponentScatteredFeaturePieces.func_143045_a();
        StructureOceanMonumentPieces.func_175970_a();
        StructureEndCityPieces.func_186200_a();
        WoodlandMansionPieces.func_191153_a();
    }
}
