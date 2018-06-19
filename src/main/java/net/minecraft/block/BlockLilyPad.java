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

    protected static final AxisAlignedBB field_185523_a = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.09375D, 0.9375D);

    protected BlockLilyPad() {
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!(entity instanceof EntityBoat)) {
            func_185492_a(blockposition, axisalignedbb, list, BlockLilyPad.field_185523_a);
        }

    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        super.func_180634_a(world, blockposition, iblockdata, entity);
        if (entity instanceof EntityBoat && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, Blocks.field_150350_a, 0).isCancelled()) { // CraftBukkit
            world.func_175655_b(new BlockPos(blockposition), true);
        }

    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockLilyPad.field_185523_a;
    }

    protected boolean func_185514_i(IBlockState iblockdata) {
        return iblockdata.func_177230_c() == Blocks.field_150355_j || iblockdata.func_185904_a() == Material.field_151588_w;
    }

    public boolean func_180671_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (blockposition.func_177956_o() >= 0 && blockposition.func_177956_o() < 256) {
            IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177977_b());
            Material material = iblockdata1.func_185904_a();

            return material == Material.field_151586_h && ((Integer) iblockdata1.func_177229_b(BlockLiquid.field_176367_b)).intValue() == 0 || material == Material.field_151588_w;
        } else {
            return false;
        }
    }

    public int func_176201_c(IBlockState iblockdata) {
        return 0;
    }
}
