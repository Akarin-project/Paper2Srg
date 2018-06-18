package net.minecraft.util.datafix.walkers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

public class EntityTag implements IDataWalker {

    private static final Logger LOGGER = LogManager.getLogger();

    public EntityTag() {}

    public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("tag");

        if (nbttagcompound1.hasKey("EntityTag", 10)) {
            NBTTagCompound nbttagcompound2 = nbttagcompound1.getCompoundTag("EntityTag");
            String s = nbttagcompound.getString("id");
            String s1;

            if ("minecraft:armor_stand".equals(s)) {
                s1 = i < 515 ? "ArmorStand" : "minecraft:armor_stand";
            } else {
                if (!"minecraft:spawn_egg".equals(s)) {
                    return nbttagcompound;
                }

                s1 = nbttagcompound2.getString("id");
            }

            boolean flag;

            if (s1 == null) {
                EntityTag.LOGGER.warn("Unable to resolve Entity for ItemInstance: {}", s);
                flag = false;
            } else {
                flag = !nbttagcompound2.hasKey("id", 8);
                nbttagcompound2.setString("id", s1);
            }

            dataconverter.process(FixTypes.ENTITY, nbttagcompound2, i);
            if (flag) {
                nbttagcompound2.removeTag("id");
            }
        }

        return nbttagcompound;
    }
}
