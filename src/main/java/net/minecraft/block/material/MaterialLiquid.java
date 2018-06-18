package net.minecraft.block.material;

public class MaterialLiquid extends Material {

    public MaterialLiquid(MapColor materialmapcolor) {
        super(materialmapcolor);
        this.setReplaceable();
        this.setNoPushMobility();
    }

    public boolean isLiquid() {
        return true;
    }

    public boolean blocksMovement() {
        return false;
    }

    public boolean isSolid() {
        return false;
    }
}
