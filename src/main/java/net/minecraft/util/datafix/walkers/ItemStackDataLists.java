package net.minecraft.util.datafix.walkers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IDataFixer;


public class ItemStackDataLists extends Filtered {

    private final String[] matchingTags;

    public ItemStackDataLists(Class<?> oclass, String... astring) {
        super(oclass);
        this.matchingTags = astring;
    }

    NBTTagCompound filteredProcess(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
        String[] astring = this.matchingTags;
        int j = astring.length;

        for (int k = 0; k < j; ++k) {
            String s = astring[k];

            nbttagcompound = DataFixesManager.processInventory(dataconverter, nbttagcompound, i, s);
        }

        return nbttagcompound;
    }
}
