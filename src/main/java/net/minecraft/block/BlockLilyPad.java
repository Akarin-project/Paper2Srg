package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLilyPad extends BlockBush {

    protected static final AxisAlignedBB LILY_PAD_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.09375D, 0.9375D);

    protected BlockLilyPad() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!(entity instanceof EntityBoat)) {
            addCollisionBoxToList(blockposition, axisalignedbb, list, BlockLilyPad.LILY_PAD_AABB);
        }

    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        super.onEntityCollidedWithBlock(world, blockposition, iblockdata, entity);
        if (entity instanceof EntityBoat && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, Blocks.AIR, 0).isCancelled()) { // CraftBukkit
            world.destroyBlock(new BlockPos(blockposition), true);
        }

    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockLilyPad.LILY_PAD_AABB;
    }

    protected boolean canSustainBush(IBlockState iblockdata) {
        return iblockdata.getBlock() == Blocks.WATER || iblockdata.getMaterial() == Material.ICE;
    }

    public boolean canBlockStay(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (blockposition.getY() >= 0 && blockposition.getY() < 256) {
            IBlockState iblockdata1 = world.getBlockState(blockposition.down());
            Material material = iblockdata1.getMaterial();

            return material == Material.WATER && ((Integer) iblockdata1.getValue(BlockLiquid.LEVEL)).intValue() == 0 || material == Material.ICE;
        } else {
            return false;
        }
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return 0;
    }
}
