package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class MinecartEntityTypes implements IFixableData {

    private static final List<String> MINECART_TYPE_LIST = Lists.newArrayList(new String[] { "MinecartRideable", "MinecartChest", "MinecartFurnace", "MinecartTNT", "MinecartSpawner", "MinecartHopper", "MinecartCommandBlock"});

    public MinecartEntityTypes() {}

    public int getFixVersion() {
        return 106;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("Minecart".equals(nbttagcompound.getString("id"))) {
            String s = "MinecartRideable";
            int i = nbttagcompound.getInteger("Type");

            if (i > 0 && i < MinecartEntityTypes.MINECART_TYPE_LIST.size()) {
                s = (String) MinecartEntityTypes.MINECART_TYPE_LIST.get(i);
            }

            nbttagcompound.setString("id", s);
            nbttagcompound.removeTag("Type");
        }

        return nbttagcompound;
    }
}
