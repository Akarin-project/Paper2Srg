package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityPortalEnterEvent;

public class BlockEndPortal extends BlockContainer {

    protected static final AxisAlignedBB field_185568_a = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    protected BlockEndPortal(Material material) {
        super(material);
        this.func_149715_a(1.0F);
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityEndPortal();
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockEndPortal.field_185568_a;
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {}

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.field_72995_K && !entity.func_184218_aH() && !entity.func_184207_aI() && entity.func_184222_aU() && entity.func_174813_aQ().func_72326_a(iblockdata.func_185900_c(world, blockposition).func_186670_a(blockposition))) {
            // CraftBukkit start - Entity in portal
            EntityPortalEnterEvent event = new EntityPortalEnterEvent(entity.getBukkitEntity(), new org.bukkit.Location(world.getWorld(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
            world.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
            entity.func_184204_a(1);
        }

    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return ItemStack.field_190927_a;
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.field_151646_E;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
