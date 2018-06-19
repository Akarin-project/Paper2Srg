package net.minecraft.util.datafix.walkers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

public class EntityTag implements IDataWalker {

    private static final Logger field_188270_a = LogManager.getLogger();

    public EntityTag() {}

    public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("tag");

        if (nbttagcompound1.func_150297_b("EntityTag", 10)) {
            NBTTagCompound nbttagcompound2 = nbttagcompound1.func_74775_l("EntityTag");
            String s = nbttagcompound.func_74779_i("id");
            String s1;

            if ("minecraft:armor_stand".equals(s)) {
                s1 = i < 515 ? "ArmorStand" : "minecraft:armor_stand";
            } else {
                if (!"minecraft:spawn_egg".equals(s)) {
                    return nbttagcompound;
                }

                s1 = nbttagcompound2.func_74779_i("id");
            }

            boolean flag;

            if (s1 == null) {
                EntityTag.field_188270_a.warn("Unable to resolve Entity for ItemInstance: {}", s);
                flag = false;
            } else {
                flag = !nbttagcompound2.func_150297_b("id", 8);
                nbttagcompound2.func_74778_a("id", s1);
            }

            dataconverter.func_188251_a(FixTypes.ENTITY, nbttagcompound2, i);
            if (flag) {
                nbttagcompound2.func_82580_o("id");
            }
        }

        return nbttagcompound;
    }
}
