package net.minecraft.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum DimensionType {

    OVERWORLD(0, "overworld", "", WorldProviderSurface.class), NETHER(-1, "the_nether", "_nether", WorldProviderHell.class), THE_END(1, "the_end", "_end", WorldProviderEnd.class);

    private final int field_186074_d;
    private final String field_186075_e;
    private final String field_186076_f;
    private final Class<? extends WorldProvider> field_186077_g;

    private DimensionType(int i, String s, String s1, Class<? extends WorldProvider> oclass) {
        this.field_186074_d = i;
        this.field_186075_e = s;
        this.field_186076_f = s1;
        this.field_186077_g = oclass;
    }

    public int func_186068_a() {
        return this.field_186074_d;
    }

    public String func_186065_b() {
        return this.field_186075_e;
    }

    public String func_186067_c() {
        return this.field_186076_f;
    }

    public WorldProvider func_186070_d() {
        try {
            Constructor constructor = this.field_186077_g.getConstructor(new Class[0]);

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

    public static DimensionType func_186069_a(int i) {
        DimensionType[] adimensionmanager = values();
        int j = adimensionmanager.length;

        for (int k = 0; k < j; ++k) {
            DimensionType dimensionmanager = adimensionmanager[k];

            if (dimensionmanager.func_186068_a() == i) {
                return dimensionmanager;
            }
        }

        throw new IllegalArgumentException("Invalid dimension id " + i);
    }

    public static DimensionType func_193417_a(String s) {
        DimensionType[] adimensionmanager = values();
        int i = adimensionmanager.length;

        for (int j = 0; j < i; ++j) {
            DimensionType dimensionmanager = adimensionmanager[j];

            if (dimensionmanager.func_186065_b().equals(s)) {
                return dimensionmanager;
            }
        }

        throw new IllegalArgumentException("Invalid dimension " + s);
    }
}
