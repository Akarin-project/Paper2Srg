package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.IFixableData;


public class PaintingDirection implements IFixableData {

    public PaintingDirection() {}

    public int getFixVersion() {
        return 111;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.getString("id");
        boolean flag = "Painting".equals(s);
        boolean flag1 = "ItemFrame".equals(s);

        if ((flag || flag1) && !nbttagcompound.hasKey("Facing", 99)) {
            EnumFacing enumdirection;

            if (nbttagcompound.hasKey("Direction", 99)) {
                enumdirection = EnumFacing.getHorizontal(nbttagcompound.getByte("Direction"));
                nbttagcompound.setInteger("TileX", nbttagcompound.getInteger("TileX") + enumdirection.getFrontOffsetX());
                nbttagcompound.setInteger("TileY", nbttagcompound.getInteger("TileY") + enumdirection.getFrontOffsetY());
                nbttagcompound.setInteger("TileZ", nbttagcompound.getInteger("TileZ") + enumdirection.getFrontOffsetZ());
                nbttagcompound.removeTag("Direction");
                if (flag1 && nbttagcompound.hasKey("ItemRotation", 99)) {
                    nbttagcompound.setByte("ItemRotation", (byte) (nbttagcompound.getByte("ItemRotation") * 2));
                }
            } else {
                enumdirection = EnumFacing.getHorizontal(nbttagcompound.getByte("Dir"));
                nbttagcompound.removeTag("Dir");
            }

            nbttagcompound.setByte("Facing", (byte) enumdirection.getHorizontalIndex());
        }

        return nbttagcompound;
    }
}
