package net.minecraft.world.storage.loot.properties;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class EntityPropertyManager {

    private static final Map<ResourceLocation, EntityProperty.a<?>> field_186647_a = Maps.newHashMap();
    private static final Map<Class<? extends EntityProperty>, EntityProperty.a<?>> field_186648_b = Maps.newHashMap();

    public static <T extends EntityProperty> void a(EntityProperty.a<? extends T> EntityProperty_a) {
        ResourceLocation minecraftkey = EntityProperty_a.a();
        Class oclass = EntityProperty_a.b();

        if (EntityPropertyManager.field_186647_a.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register entity property name " + minecraftkey);
        } else if (EntityPropertyManager.field_186648_b.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register entity property class " + oclass.getName());
        } else {
            EntityPropertyManager.field_186647_a.put(minecraftkey, EntityProperty_a);
            EntityPropertyManager.field_186648_b.put(oclass, EntityProperty_a);
        }
    }

    public static EntityProperty.a<?> a(ResourceLocation minecraftkey) {
        EntityProperty.a EntityProperty_a = EntityPropertyManager.field_186647_a.get(minecraftkey);

        if (EntityProperty_a == null) {
            throw new IllegalArgumentException("Unknown loot entity property \'" + minecraftkey + "\'");
        } else {
            return EntityProperty_a;
        }
    }

    public static <T extends EntityProperty> EntityProperty.a<T> a(T t0) {
        EntityProperty.a EntityProperty_a = EntityPropertyManager.field_186648_b.get(t0.getClass());

        if (EntityProperty_a == null) {
            throw new IllegalArgumentException("Unknown loot entity property " + t0);
        } else {
            return EntityProperty_a;
        }
    }

    static {
        a(new EntityOnFire.a());
    }
}
