package net.minecraft.tileentity;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;


public class TileEntityDropper extends TileEntityDispenser {

    public TileEntityDropper() {}

    public static void func_189679_b(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityDropper.class, new String[] { "Items"})));
    }

    public String func_70005_c_() {
        return this.func_145818_k_() ? this.field_190577_o : "container.dropper";
    }

    public String func_174875_k() {
        return "minecraft:dropper";
    }
}
