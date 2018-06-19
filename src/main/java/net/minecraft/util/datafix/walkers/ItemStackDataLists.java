package net.minecraft.util.datafix.walkers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IDataFixer;


public class ItemStackDataLists extends Filtered {

    private final String[] field_188275_a;

    public ItemStackDataLists(Class<?> oclass, String... astring) {
        super(oclass);
        this.field_188275_a = astring;
    }

    NBTTagCompound func_188271_b(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
        String[] astring = this.field_188275_a;
        int j = astring.length;

        for (int k = 0; k < j; ++k) {
            String s = astring[k];

            nbttagcompound = DataFixesManager.func_188278_b(dataconverter, nbttagcompound, i, s);
        }

        return nbttagcompound;
    }
}
