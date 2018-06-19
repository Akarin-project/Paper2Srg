package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityInteractEvent;

// CraftBukkit start
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
// CraftBukkit end

public class BlockFarmland extends Block {

    public static final PropertyInteger field_176531_a = PropertyInteger.func_177719_a("moisture", 0, 7);
    protected static final AxisAlignedBB field_185665_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
    protected static final AxisAlignedBB field_194405_c = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected BlockFarmland() {
        super(Material.field_151578_c);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockFarmland.field_176531_a, Integer.valueOf(0)));
        this.func_149675_a(true);
        this.func_149713_g(255);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockFarmland.field_185665_b;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        int i = ((Integer) iblockdata.func_177229_b(BlockFarmland.field_176531_a)).intValue();

        if (!this.func_176530_e(world, blockposition) && !world.func_175727_C(blockposition.func_177984_a())) {
            if (i > 0) {
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockFarmland.field_176531_a, Integer.valueOf(i - 1)), 2);
            } else if (!this.func_176529_d(world, blockposition)) {
                func_190970_b(world, blockposition);
            }
        } else if (i < 7) {
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockFarmland.field_176531_a, Integer.valueOf(7)), 2);
        }

    }

    public void func_180658_a(World world, BlockPos blockposition, Entity entity, float f) {
        super.func_180658_a(world, blockposition, entity, f); // CraftBukkit - moved here as game rules / events shouldn't affect fall damage.
        if (!world.field_72995_K && world.field_73012_v.nextFloat() < f - 0.5F && entity instanceof EntityLivingBase && (entity instanceof EntityPlayer || world.func_82736_K().func_82766_b("mobGriefing")) && entity.field_70130_N * entity.field_70130_N * entity.field_70131_O > 0.512F) {
            // CraftBukkit start - Interact soil
            org.bukkit.event.Cancellable cancellable;
            if (entity instanceof EntityPlayer) {
                cancellable = CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
            } else {
                cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
                world.getServer().getPluginManager().callEvent((EntityInteractEvent) cancellable);
            }

            if (cancellable.isCancelled()) {
                return;
            }

            if (CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, Blocks.field_150346_d, 0).isCancelled()) {
                return;
            }
            // CraftBukkit end
            func_190970_b(world, blockposition);
        }

        // super.fallOn(world, blockposition, entity, f); // CraftBukkit - moved up
    }

    protected static void func_190970_b(World world, BlockPos blockposition) {
        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
        if (CraftEventFactory.callBlockFadeEvent(block, Blocks.field_150346_d).isCancelled()) {
            return;
        }
        // CraftBukkit end
        world.func_175656_a(blockposition, Blocks.field_150346_d.func_176223_P());
        AxisAlignedBB axisalignedbb = BlockFarmland.field_194405_c.func_186670_a(blockposition);
        List list = world.func_72839_b((Entity) null, axisalignedbb);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            double d0 = Math.min(axisalignedbb.field_72337_e - axisalignedbb.field_72338_b, axisalignedbb.field_72337_e - entity.func_174813_aQ().field_72338_b);

            entity.func_70634_a(entity.field_70165_t, entity.field_70163_u + d0 + 0.001D, entity.field_70161_v);
        }

    }

    private boolean func_176529_d(World world, BlockPos blockposition) {
        Block block = world.func_180495_p(blockposition.func_177984_a()).func_177230_c();

        return block instanceof BlockCrops || block instanceof BlockStem;
    }

    private boolean func_176530_e(World world, BlockPos blockposition) {
        Iterator iterator = BlockPos.func_177975_b(blockposition.func_177982_a(-4, 0, -4), blockposition.func_177982_a(4, 1, 4)).iterator();

        BlockPos.MutableBlockPos blockposition_mutableblockposition;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
        } while (world.func_180495_p(blockposition_mutableblockposition).func_185904_a() != Material.field_151586_h);

        return true;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        super.func_189540_a(iblockdata, world, blockposition, block, blockposition1);
        if (world.func_180495_p(blockposition.func_177984_a()).func_185904_a().func_76220_a()) {
            func_190970_b(world, blockposition);
        }

    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_176213_c(world, blockposition, iblockdata);
        if (world.func_180495_p(blockposition.func_177984_a()).func_185904_a().func_76220_a()) {
            func_190970_b(world, blockposition);
        }

    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Blocks.field_150346_d.func_180660_a(Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT), random, i);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockFarmland.field_176531_a, Integer.valueOf(i & 7));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockFarmland.field_176531_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockFarmland.field_176531_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
