package net.minecraft.util.datafix.fixes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class AddBedTileEntity implements IFixableData {

    private static final Logger field_193842_a = LogManager.getLogger();

    public AddBedTileEntity() {}

    public int func_188216_a() {
        return 1125;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        boolean flag = true;

        try {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Level");
            int i = nbttagcompound1.func_74762_e("xPos");
            int j = nbttagcompound1.func_74762_e("zPos");
            NBTTagList nbttaglist = nbttagcompound1.func_150295_c("TileEntities", 10);
            NBTTagList nbttaglist1 = nbttagcompound1.func_150295_c("Sections", 10);

            for (int k = 0; k < nbttaglist1.func_74745_c(); ++k) {
                NBTTagCompound nbttagcompound2 = nbttaglist1.func_150305_b(k);
                byte b0 = nbttagcompound2.func_74771_c("Y");
                byte[] abyte = nbttagcompound2.func_74770_j("Blocks");

                for (int l = 0; l < abyte.length; ++l) {
                    if (416 == (abyte[l] & 255) << 4) {
                        int i1 = l & 15;
                        int j1 = l >> 8 & 15;
                        int k1 = l >> 4 & 15;
                        NBTTagCompound nbttagcompound3 = new NBTTagCompound();

                        nbttagcompound3.func_74778_a("id", "bed");
                        nbttagcompound3.func_74768_a("x", i1 + (i << 4));
                        nbttagcompound3.func_74768_a("y", j1 + (b0 << 4));
                        nbttagcompound3.func_74768_a("z", k1 + (j << 4));
                        nbttaglist.func_74742_a(nbttagcompound3);
                    }
                }
            }
        } catch (Exception exception) {
            AddBedTileEntity.field_193842_a.warn("Unable to datafix Bed blocks, level format may be missing tags.");
        }

        return nbttagcompound;
    }
}
