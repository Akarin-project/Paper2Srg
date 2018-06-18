package net.minecraft.block;

import com.google.common.base.Predicate;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public abstract class BlockHorizontal extends Block {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate) EnumFacing.Plane.HORIZONTAL);

    protected BlockHorizontal(Material material) {
        super(material);
    }

    protected BlockHorizontal(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
    }
}
