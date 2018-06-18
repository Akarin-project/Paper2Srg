package net.minecraft.util.datafix.fixes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class AddBedTileEntity implements IFixableData {

    private static final Logger LOGGER = LogManager.getLogger();

    public AddBedTileEntity() {}

    public int getFixVersion() {
        return 1125;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        boolean flag = true;

        try {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Level");
            int i = nbttagcompound1.getInteger("xPos");
            int j = nbttagcompound1.getInteger("zPos");
            NBTTagList nbttaglist = nbttagcompound1.getTagList("TileEntities", 10);
            NBTTagList nbttaglist1 = nbttagcompound1.getTagList("Sections", 10);

            for (int k = 0; k < nbttaglist1.tagCount(); ++k) {
                NBTTagCompound nbttagcompound2 = nbttaglist1.getCompoundTagAt(k);
                byte b0 = nbttagcompound2.getByte("Y");
                byte[] abyte = nbttagcompound2.getByteArray("Blocks");

                for (int l = 0; l < abyte.length; ++l) {
                    if (416 == (abyte[l] & 255) << 4) {
                        int i1 = l & 15;
                        int j1 = l >> 8 & 15;
                        int k1 = l >> 4 & 15;
                        NBTTagCompound nbttagcompound3 = new NBTTagCompound();

                        nbttagcompound3.setString("id", "bed");
                        nbttagcompound3.setInteger("x", i1 + (i << 4));
                        nbttagcompound3.setInteger("y", j1 + (b0 << 4));
                        nbttagcompound3.setInteger("z", k1 + (j << 4));
                        nbttaglist.appendTag(nbttagcompound3);
                    }
                }
            }
        } catch (Exception exception) {
            AddBedTileEntity.LOGGER.warn("Unable to datafix Bed blocks, level format may be missing tags.");
        }

        return nbttagcompound;
    }
}
