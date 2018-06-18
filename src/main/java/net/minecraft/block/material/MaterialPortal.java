package net.minecraft.block.material;

public class MaterialPortal extends Material {

    public MaterialPortal(MapColor materialmapcolor) {
        super(materialmapcolor);
    }

    public boolean isSolid() {
        return false;
    }

    public boolean blocksLight() {
        return false;
    }

    public boolean blocksMovement() {
        return false;
    }
}
