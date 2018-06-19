package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.IFixableData;


public class PaintingDirection implements IFixableData {

    public PaintingDirection() {}

    public int func_188216_a() {
        return 111;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.func_74779_i("id");
        boolean flag = "Painting".equals(s);
        boolean flag1 = "ItemFrame".equals(s);

        if ((flag || flag1) && !nbttagcompound.func_150297_b("Facing", 99)) {
            EnumFacing enumdirection;

            if (nbttagcompound.func_150297_b("Direction", 99)) {
                enumdirection = EnumFacing.func_176731_b(nbttagcompound.func_74771_c("Direction"));
                nbttagcompound.func_74768_a("TileX", nbttagcompound.func_74762_e("TileX") + enumdirection.func_82601_c());
                nbttagcompound.func_74768_a("TileY", nbttagcompound.func_74762_e("TileY") + enumdirection.func_96559_d());
                nbttagcompound.func_74768_a("TileZ", nbttagcompound.func_74762_e("TileZ") + enumdirection.func_82599_e());
                nbttagcompound.func_82580_o("Direction");
                if (flag1 && nbttagcompound.func_150297_b("ItemRotation", 99)) {
                    nbttagcompound.func_74774_a("ItemRotation", (byte) (nbttagcompound.func_74771_c("ItemRotation") * 2));
                }
            } else {
                enumdirection = EnumFacing.func_176731_b(nbttagcompound.func_74771_c("Dir"));
                nbttagcompound.func_82580_o("Dir");
            }

            nbttagcompound.func_74774_a("Facing", (byte) enumdirection.func_176736_b());
        }

        return nbttagcompound;
    }
}
