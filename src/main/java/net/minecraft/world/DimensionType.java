package net.minecraft.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum DimensionType {

    OVERWORLD(0, "overworld", "", WorldProviderSurface.class), NETHER(-1, "the_nether", "_nether", WorldProviderHell.class), THE_END(1, "the_end", "_end", WorldProviderEnd.class);

    private final int id;
    private final String name;
    private final String suffix;
    private final Class<? extends WorldProvider> clazz;

    private DimensionType(int i, String s, String s1, Class<? extends WorldProvider> oclass) {
        this.id = i;
        this.name = s;
        this.suffix = s1;
        this.clazz = oclass;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public WorldProvider createDimension() {
        try {
            Constructor constructor = this.clazz.getConstructor(new Class[0]);

            return (WorldProvider) constructor.newInstance(new Object[0]);
        } catch (NoSuchMethodException nosuchmethodexception) {
            throw new Error("Could not create new dimension", nosuchmethodexception);
        } catch (InvocationTargetException invocationtargetexception) {
            throw new Error("Could not create new dimension", invocationtargetexception);
        } catch (InstantiationException instantiationexception) {
            throw new Error("Could not create new dimension", instantiationexception);
        } catch (IllegalAccessException illegalaccessexception) {
            throw new Error("Could not create new dimension", illegalaccessexception);
        }
    }

    public static DimensionType getById(int i) {
        DimensionType[] adimensionmanager = values();
        int j = adimensionmanager.length;

        for (int k = 0; k < j; ++k) {
            DimensionType dimensionmanager = adimensionmanager[k];

            if (dimensionmanager.getId() == i) {
                return dimensionmanager;
            }
        }

        throw new IllegalArgumentException("Invalid dimension id " + i);
    }

    public static DimensionType byName(String s) {
        DimensionType[] adimensionmanager = values();
        int i = adimensionmanager.length;

        for (int j = 0; j < i; ++j) {
            DimensionType dimensionmanager = adimensionmanager[j];

            if (dimensionmanager.getName().equals(s)) {
                return dimensionmanager;
            }
        }

        throw new IllegalArgumentException("Invalid dimension " + s);
    }
}
