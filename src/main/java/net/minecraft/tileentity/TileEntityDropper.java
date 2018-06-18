package net.minecraft.tileentity;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;


public class TileEntityDropper extends TileEntityDispenser {

    public TileEntityDropper() {}

    public static void registerFixesDropper(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityDropper.class, new String[] { "Items"})));
    }

    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dropper";
    }

    public String getGuiID() {
        return "minecraft:dropper";
    }
}
