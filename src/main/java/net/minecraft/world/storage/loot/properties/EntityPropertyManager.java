package net.minecraft.world.storage.loot.properties;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class EntityPropertyManager {

    private static final Map<ResourceLocation, LootEntityProperty.a<?>> NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
    private static final Map<Class<? extends EntityProperty>, LootEntityProperty.a<?>> CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();

    public static <T extends EntityProperty> void a(LootEntityProperty.a<? extends T> lootentityproperty_a) {
        ResourceLocation minecraftkey = lootentityproperty_a.a();
        Class oclass = lootentityproperty_a.b();

        if (EntityPropertyManager.NAME_TO_SERIALIZER_MAP.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register entity property name " + minecraftkey);
        } else if (EntityPropertyManager.CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register entity property class " + oclass.getName());
        } else {
            EntityPropertyManager.NAME_TO_SERIALIZER_MAP.put(minecraftkey, lootentityproperty_a);
            EntityPropertyManager.CLASS_TO_SERIALIZER_MAP.put(oclass, lootentityproperty_a);
        }
    }

    public static LootEntityProperty.a<?> a(ResourceLocation minecraftkey) {
        LootEntityProperty.a lootentityproperty_a = (LootEntityProperty.a) EntityPropertyManager.NAME_TO_SERIALIZER_MAP.get(minecraftkey);

        if (lootentityproperty_a == null) {
            throw new IllegalArgumentException("Unknown loot entity property \'" + minecraftkey + "\'");
        } else {
            return lootentityproperty_a;
        }
    }

    public static <T extends EntityProperty> LootEntityProperty.a<T> a(T t0) {
        LootEntityProperty.a lootentityproperty_a = (LootEntityProperty.a) EntityPropertyManager.CLASS_TO_SERIALIZER_MAP.get(t0.getClass());

        if (lootentityproperty_a == null) {
            throw new IllegalArgumentException("Unknown loot entity property " + t0);
        } else {
            return lootentityproperty_a;
        }
    }

    static {
        a((LootEntityProperty.a) (new LootEntityPropertyOnFire.a()));
    }
}
