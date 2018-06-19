package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Sets;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class EntityHealth implements IFixableData {

    private static final Set<String> field_188218_a = Sets.newHashSet(new String[] { "ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie"});

    public EntityHealth() {}

    public int func_188216_a() {
        return 109;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if (EntityHealth.field_188218_a.contains(nbttagcompound.func_74779_i("id"))) {
            float f;

            if (nbttagcompound.func_150297_b("HealF", 99)) {
                f = nbttagcompound.func_74760_g("HealF");
                nbttagcompound.func_82580_o("HealF");
            } else {
                if (!nbttagcompound.func_150297_b("Health", 99)) {
                    return nbttagcompound;
                }

                f = nbttagcompound.func_74760_g("Health");
            }

            nbttagcompound.func_74776_a("Health", f);
        }

        return nbttagcompound;
    }
}
