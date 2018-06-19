package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class EntityId implements IFixableData {

    private static final Map<String, String> field_191276_a = Maps.newHashMap();

    public EntityId() {}

    public int func_188216_a() {
        return 704;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        String s = (String) EntityId.field_191276_a.get(nbttagcompound.func_74779_i("id"));

        if (s != null) {
            nbttagcompound.func_74778_a("id", s);
        }

        return nbttagcompound;
    }

    static {
        EntityId.field_191276_a.put("AreaEffectCloud", "minecraft:area_effect_cloud");
        EntityId.field_191276_a.put("ArmorStand", "minecraft:armor_stand");
        EntityId.field_191276_a.put("Arrow", "minecraft:arrow");
        EntityId.field_191276_a.put("Bat", "minecraft:bat");
        EntityId.field_191276_a.put("Blaze", "minecraft:blaze");
        EntityId.field_191276_a.put("Boat", "minecraft:boat");
        EntityId.field_191276_a.put("CaveSpider", "minecraft:cave_spider");
        EntityId.field_191276_a.put("Chicken", "minecraft:chicken");
        EntityId.field_191276_a.put("Cow", "minecraft:cow");
        EntityId.field_191276_a.put("Creeper", "minecraft:creeper");
        EntityId.field_191276_a.put("Donkey", "minecraft:donkey");
        EntityId.field_191276_a.put("DragonFireball", "minecraft:dragon_fireball");
        EntityId.field_191276_a.put("ElderGuardian", "minecraft:elder_guardian");
        EntityId.field_191276_a.put("EnderCrystal", "minecraft:ender_crystal");
        EntityId.field_191276_a.put("EnderDragon", "minecraft:ender_dragon");
        EntityId.field_191276_a.put("Enderman", "minecraft:enderman");
        EntityId.field_191276_a.put("Endermite", "minecraft:endermite");
        EntityId.field_191276_a.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
        EntityId.field_191276_a.put("FallingSand", "minecraft:falling_block");
        EntityId.field_191276_a.put("Fireball", "minecraft:fireball");
        EntityId.field_191276_a.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
        EntityId.field_191276_a.put("Ghast", "minecraft:ghast");
        EntityId.field_191276_a.put("Giant", "minecraft:giant");
        EntityId.field_191276_a.put("Guardian", "minecraft:guardian");
        EntityId.field_191276_a.put("Horse", "minecraft:horse");
        EntityId.field_191276_a.put("Husk", "minecraft:husk");
        EntityId.field_191276_a.put("Item", "minecraft:item");
        EntityId.field_191276_a.put("ItemFrame", "minecraft:item_frame");
        EntityId.field_191276_a.put("LavaSlime", "minecraft:magma_cube");
        EntityId.field_191276_a.put("LeashKnot", "minecraft:leash_knot");
        EntityId.field_191276_a.put("MinecartChest", "minecraft:chest_minecart");
        EntityId.field_191276_a.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
        EntityId.field_191276_a.put("MinecartFurnace", "minecraft:furnace_minecart");
        EntityId.field_191276_a.put("MinecartHopper", "minecraft:hopper_minecart");
        EntityId.field_191276_a.put("MinecartRideable", "minecraft:minecart");
        EntityId.field_191276_a.put("MinecartSpawner", "minecraft:spawner_minecart");
        EntityId.field_191276_a.put("MinecartTNT", "minecraft:tnt_minecart");
        EntityId.field_191276_a.put("Mule", "minecraft:mule");
        EntityId.field_191276_a.put("MushroomCow", "minecraft:mooshroom");
        EntityId.field_191276_a.put("Ozelot", "minecraft:ocelot");
        EntityId.field_191276_a.put("Painting", "minecraft:painting");
        EntityId.field_191276_a.put("Pig", "minecraft:pig");
        EntityId.field_191276_a.put("PigZombie", "minecraft:zombie_pigman");
        EntityId.field_191276_a.put("PolarBear", "minecraft:polar_bear");
        EntityId.field_191276_a.put("PrimedTnt", "minecraft:tnt");
        EntityId.field_191276_a.put("Rabbit", "minecraft:rabbit");
        EntityId.field_191276_a.put("Sheep", "minecraft:sheep");
        EntityId.field_191276_a.put("Shulker", "minecraft:shulker");
        EntityId.field_191276_a.put("ShulkerBullet", "minecraft:shulker_bullet");
        EntityId.field_191276_a.put("Silverfish", "minecraft:silverfish");
        EntityId.field_191276_a.put("Skeleton", "minecraft:skeleton");
        EntityId.field_191276_a.put("SkeletonHorse", "minecraft:skeleton_horse");
        EntityId.field_191276_a.put("Slime", "minecraft:slime");
        EntityId.field_191276_a.put("SmallFireball", "minecraft:small_fireball");
        EntityId.field_191276_a.put("SnowMan", "minecraft:snowman");
        EntityId.field_191276_a.put("Snowball", "minecraft:snowball");
        EntityId.field_191276_a.put("SpectralArrow", "minecraft:spectral_arrow");
        EntityId.field_191276_a.put("Spider", "minecraft:spider");
        EntityId.field_191276_a.put("Squid", "minecraft:squid");
        EntityId.field_191276_a.put("Stray", "minecraft:stray");
        EntityId.field_191276_a.put("ThrownEgg", "minecraft:egg");
        EntityId.field_191276_a.put("ThrownEnderpearl", "minecraft:ender_pearl");
        EntityId.field_191276_a.put("ThrownExpBottle", "minecraft:xp_bottle");
        EntityId.field_191276_a.put("ThrownPotion", "minecraft:potion");
        EntityId.field_191276_a.put("Villager", "minecraft:villager");
        EntityId.field_191276_a.put("VillagerGolem", "minecraft:villager_golem");
        EntityId.field_191276_a.put("Witch", "minecraft:witch");
        EntityId.field_191276_a.put("WitherBoss", "minecraft:wither");
        EntityId.field_191276_a.put("WitherSkeleton", "minecraft:wither_skeleton");
        EntityId.field_191276_a.put("WitherSkull", "minecraft:wither_skull");
        EntityId.field_191276_a.put("Wolf", "minecraft:wolf");
        EntityId.field_191276_a.put("XPOrb", "minecraft:xp_orb");
        EntityId.field_191276_a.put("Zombie", "minecraft:zombie");
        EntityId.field_191276_a.put("ZombieHorse", "minecraft:zombie_horse");
        EntityId.field_191276_a.put("ZombieVillager", "minecraft:zombie_villager");
    }
}
