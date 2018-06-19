package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public abstract class BlockBasePressurePlate extends Block {

    protected static final AxisAlignedBB field_185509_a = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.03125D, 0.9375D);
    protected static final AxisAlignedBB field_185510_b = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);
    protected static final AxisAlignedBB field_185511_c = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.25D, 0.875D);

    protected BlockBasePressurePlate(Material material) {
        this(material, material.func_151565_r());
    }

    protected BlockBasePressurePlate(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.func_149647_a(CreativeTabs.field_78028_d);
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        boolean flag = this.func_176576_e(iblockdata) > 0;

        return flag ? BlockBasePressurePlate.field_185509_a : BlockBasePressurePlate.field_185510_b;
    }

    public int func_149738_a(World world) {
        return 20;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockBasePressurePlate.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public boolean func_181623_g() {
        return true;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return this.func_176577_m(world, blockposition.func_177977_b());
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_176577_m(world, blockposition.func_177977_b())) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
        }

    }

    private boolean func_176577_m(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition).func_185896_q() || world.func_180495_p(blockposition).func_177230_c() instanceof BlockFence;
    }

    public void func_180645_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            int i = this.func_176576_e(iblockdata);

            if (i > 0) {
                this.func_180666_a(world, blockposition, iblockdata, i);
            }

        }
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.field_72995_K) {
            int i = this.func_176576_e(iblockdata);

            if (i == 0) {
                this.func_180666_a(world, blockposition, iblockdata, i);
            }

        }
    }

    protected void func_180666_a(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        int j = this.func_180669_e(world, blockposition);
        boolean flag = i > 0;
        boolean flag1 = j > 0;

        // CraftBukkit start - Interact Pressure Plate
        org.bukkit.World bworld = world.getWorld();
        org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();

        if (flag != flag1) {
            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bworld.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), i, j);
            manager.callEvent(eventRedstone);

            flag1 = eventRedstone.getNewCurrent() > 0;
            j = eventRedstone.getNewCurrent();
        }
        // CraftBukkit end

        if (i != j) {
            iblockdata = this.func_176575_a(iblockdata, j);
            world.func_180501_a(blockposition, iblockdata, 2);
            this.func_176578_d(world, blockposition);
            world.func_175704_b(blockposition, blockposition);
        }

        if (!flag1 && flag) {
            this.func_185508_c(world, blockposition);
        } else if (flag1 && !flag) {
            this.func_185507_b(world, blockposition);
        }

        if (flag1) {
            world.func_175684_a(new BlockPos(blockposition), (Block) this, this.func_149738_a(world));
        }

    }

    protected abstract void func_185507_b(World world, BlockPos blockposition);

    protected abstract void func_185508_c(World world, BlockPos blockposition);

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.func_176576_e(iblockdata) > 0) {
            this.func_176578_d(world, blockposition);
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    protected void func_176578_d(World world, BlockPos blockposition) {
        world.func_175685_c(blockposition, this, false);
        world.func_175685_c(blockposition.func_177977_b(), this, false);
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return this.func_176576_e(iblockdata);
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? this.func_176576_e(iblockdata) : 0;
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public EnumPushReaction func_149656_h(IBlockState iblockdata) {
        return EnumPushReaction.DESTROY;
    }

    protected abstract int func_180669_e(World world, BlockPos blockposition);

    protected abstract int func_176576_e(IBlockState iblockdata);

    protected abstract IBlockState func_176575_a(IBlockState iblockdata, int i);

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
