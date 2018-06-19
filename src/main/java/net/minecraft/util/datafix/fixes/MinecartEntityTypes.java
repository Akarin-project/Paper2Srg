package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class MinecartEntityTypes implements IFixableData {

    private static final List<String> field_188222_a = Lists.newArrayList(new String[] { "MinecartRideable", "MinecartChest", "MinecartFurnace", "MinecartTNT", "MinecartSpawner", "MinecartHopper", "MinecartCommandBlock"});

    public MinecartEntityTypes() {}

    public int func_188216_a() {
        return 106;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("Minecart".equals(nbttagcompound.func_74779_i("id"))) {
            String s = "MinecartRideable";
            int i = nbttagcompound.func_74762_e("Type");

            if (i > 0 && i < MinecartEntityTypes.field_188222_a.size()) {
                s = (String) MinecartEntityTypes.field_188222_a.get(i);
            }

            nbttagcompound.func_74778_a("id", s);
            nbttagcompound.func_82580_o("Type");
        }

        return nbttagcompound;
    }
}
