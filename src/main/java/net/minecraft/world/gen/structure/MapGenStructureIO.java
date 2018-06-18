package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MapGenStructureIO {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, Class<? extends StructureStart>> startNameToClassMap = Maps.newHashMap();
    private static final Map<Class<? extends StructureStart>, String> startClassToNameMap = Maps.newHashMap();
    private static final Map<String, Class<? extends StructureComponent>> componentNameToClassMap = Maps.newHashMap();
    private static final Map<Class<? extends StructureComponent>, String> componentClassToNameMap = Maps.newHashMap();

    private static void registerStructure(Class<? extends StructureStart> oclass, String s) {
        MapGenStructureIO.startNameToClassMap.put(s, oclass);
        MapGenStructureIO.startClassToNameMap.put(oclass, s);
    }

    static void registerStructureComponent(Class<? extends StructureComponent> oclass, String s) {
        MapGenStructureIO.componentNameToClassMap.put(s, oclass);
        MapGenStructureIO.componentClassToNameMap.put(oclass, s);
    }

    public static String getStructureStartName(StructureStart structurestart) {
        return (String) MapGenStructureIO.startClassToNameMap.get(structurestart.getClass());
    }

    public static String getStructureComponentName(StructureComponent structurepiece) {
        return (String) MapGenStructureIO.componentClassToNameMap.get(structurepiece.getClass());
    }

    @Nullable
    public static StructureStart getStructureStart(NBTTagCompound nbttagcompound, World world) {
        StructureStart structurestart = null;

        try {
            Class oclass = (Class) MapGenStructureIO.startNameToClassMap.get(nbttagcompound.getString("id"));

            if (oclass != null) {
                structurestart = (StructureStart) oclass.newInstance();
            }
        } catch (Exception exception) {
            MapGenStructureIO.LOGGER.warn("Failed Start with id {}", nbttagcompound.getString("id"));
            exception.printStackTrace();
        }

        if (structurestart != null) {
            structurestart.readStructureComponentsFromNBT(world, nbttagcompound);
        } else {
            MapGenStructureIO.LOGGER.warn("Skipping Structure with id {}", nbttagcompound.getString("id"));
        }

        return structurestart;
    }

    public static StructureComponent getStructureComponent(NBTTagCompound nbttagcompound, World world) {
        StructureComponent structurepiece = null;

        try {
            Class oclass = (Class) MapGenStructureIO.componentNameToClassMap.get(nbttagcompound.getString("id"));

            if (oclass != null) {
                structurepiece = (StructureComponent) oclass.newInstance();
            }
        } catch (Exception exception) {
            MapGenStructureIO.LOGGER.warn("Failed Piece with id {}", nbttagcompound.getString("id"));
            exception.printStackTrace();
        }

        if (structurepiece != null) {
            structurepiece.readStructureBaseNBT(world, nbttagcompound);
        } else {
            MapGenStructureIO.LOGGER.warn("Skipping Piece with id {}", nbttagcompound.getString("id"));
        }

        return structurepiece;
    }

    static {
        registerStructure(StructureMineshaftStart.class, "Mineshaft");
        registerStructure(MapGenVillage.Start.class, "Village");
        registerStructure(MapGenNetherBridge.Start.class, "Fortress");
        registerStructure(MapGenStronghold.Start.class, "Stronghold");
        registerStructure(MapGenScatteredFeature.Start.class, "Temple");
        registerStructure(StructureOceanMonument.StartMonument.class, "Monument");
        registerStructure(MapGenEndCity.Start.class, "EndCity");
        registerStructure(WorldGenWoodlandMansion.a.class, "Mansion");
        StructureMineshaftPieces.registerStructurePieces();
        StructureVillagePieces.registerVillagePieces();
        StructureNetherBridgePieces.registerNetherFortressPieces();
        StructureStrongholdPieces.registerStrongholdPieces();
        ComponentScatteredFeaturePieces.registerScatteredFeaturePieces();
        StructureOceanMonumentPieces.registerOceanMonumentPieces();
        StructureEndCityPieces.registerPieces();
        WoodlandMansionPieces.registerWoodlandMansionPieces();
    }
}
