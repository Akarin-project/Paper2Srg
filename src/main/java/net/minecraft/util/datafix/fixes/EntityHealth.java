package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Sets;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class EntityHealth implements IFixableData {

    private static final Set<String> ENTITY_LIST = Sets.newHashSet(new String[] { "ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie"});

    public EntityHealth() {}

    public int getFixVersion() {
        return 109;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if (EntityHealth.ENTITY_LIST.contains(nbttagcompound.getString("id"))) {
            float f;

            if (nbttagcompound.hasKey("HealF", 99)) {
                f = nbttagcompound.getFloat("HealF");
                nbttagcompound.removeTag("HealF");
            } else {
                if (!nbttagcompound.hasKey("Health", 99)) {
                    return nbttagcompound;
                }

                f = nbttagcompound.getFloat("Health");
            }

            nbttagcompound.setFloat("Health", f);
        }

        return nbttagcompound;
    }
}
