package net.minecraft.world.storage.loot.properties;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class EntityPropertyManager {

    private static final Map<ResourceLocation, LootEntityProperty.a<?>> field_186647_a = Maps.newHashMap();
    private static final Map<Class<? extends EntityProperty>, LootEntityProperty.a<?>> field_186648_b = Maps.newHashMap();

    public static <T extends EntityProperty> void a(LootEntityProperty.a<? extends T> lootentityproperty_a) {
        ResourceLocation minecraftkey = lootentityproperty_a.a();
        Class oclass = lootentityproperty_a.b();

        if (EntityPropertyManager.field_186647_a.containsKey(minecraftkey)) {
            throw new IllegalArgumentException("Can\'t re-register entity property name " + minecraftkey);
        } else if (EntityPropertyManager.field_186648_b.containsKey(oclass)) {
            throw new IllegalArgumentException("Can\'t re-register entity property class " + oclass.getName());
        } else {
            EntityPropertyManager.field_186647_a.put(minecraftkey, lootentityproperty_a);
            EntityPropertyManager.field_186648_b.put(oclass, lootentityproperty_a);
        }
    }

    public static LootEntityProperty.a<?> a(ResourceLocation minecraftkey) {
        LootEntityProperty.a lootentityproperty_a = (LootEntityProperty.a) EntityPropertyManager.field_186647_a.get(minecraftkey);

        if (lootentityproperty_a == null) {
            throw new IllegalArgumentException("Unknown loot entity property \'" + minecraftkey + "\'");
        } else {
            return lootentityproperty_a;
        }
    }

    public static <T extends EntityProperty> LootEntityProperty.a<T> a(T t0) {
        LootEntityProperty.a lootentityproperty_a = (LootEntityProperty.a) EntityPropertyManager.field_186648_b.get(t0.getClass());

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
