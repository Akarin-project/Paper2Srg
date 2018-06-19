package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockSpreadEvent;

// CraftBukkit start
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockSpreadEvent;
// CraftBukkit end

public class BlockMushroom extends BlockBush implements IGrowable {

    protected static final AxisAlignedBB field_185518_a = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.4000000059604645D, 0.699999988079071D);

    protected BlockMushroom() {
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockMushroom.field_185518_a;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        final int sourceX = blockposition.func_177958_n(), sourceY = blockposition.func_177956_o(), sourceZ = blockposition.func_177952_p(); // CraftBukkit
        if (random.nextInt(Math.max(1, (int) (100.0F / world.spigotConfig.mushroomModifier) * 25)) == 0) { // Spigot
            int i = 5;
            boolean flag = true;
            Iterator iterator = BlockPos.func_177975_b(blockposition.func_177982_a(-4, -1, -4), blockposition.func_177982_a(4, 1, 4)).iterator();

            while (iterator.hasNext()) {
                BlockPos blockposition1 = (BlockPos) iterator.next();

                if (world.func_180495_p(blockposition1).func_177230_c() == this) {
                    --i;
                    if (i <= 0) {
                        return;
                    }
                }
            }

            BlockPos blockposition2 = blockposition.func_177982_a(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            for (int j = 0; j < 4; ++j) {
                if (world.func_175623_d(blockposition2) && this.func_180671_f(world, blockposition2, this.func_176223_P())) {
                    blockposition = blockposition2;
                }

                blockposition2 = blockposition.func_177982_a(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }

            if (world.func_175623_d(blockposition2) && this.func_180671_f(world, blockposition2, this.func_176223_P())) {
                // CraftBukkit start
                // world.setTypeAndData(blockposition2, this.getBlockData(), 2);
                org.bukkit.World bworld = world.getWorld();
                BlockState blockState = bworld.getBlockAt(blockposition2.func_177958_n(), blockposition2.func_177956_o(), blockposition2.func_177952_p()).getState();
                blockState.setType(org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(this)); // nms: this.id, 0, 2

                BlockSpreadEvent event = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(sourceX, sourceY, sourceZ), blockState);
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    blockState.update(true);
                }
                // CraftBukkit end
            }
        }

    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return super.func_176196_c(world, blockposition) && this.func_180671_f(world, blockposition, this.func_176223_P());
    }

    protected boolean func_185514_i(IBlockState iblockdata) {
        return iblockdata.func_185913_b();
    }

    public boolean func_180671_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (blockposition.func_177956_o() >= 0 && blockposition.func_177956_o() < 256) {
            IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177977_b());

            return iblockdata1.func_177230_c() == Blocks.field_150391_bh ? true : (iblockdata1.func_177230_c() == Blocks.field_150346_d && iblockdata1.func_177229_b(BlockDirt.field_176386_a) == BlockDirt.DirtType.PODZOL ? true : world.func_175699_k(blockposition) < 13 && this.func_185514_i(iblockdata1));
        } else {
            return false;
        }
    }

    public boolean func_176485_d(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        world.func_175698_g(blockposition);
        WorldGenBigMushroom worldgenhugemushroom = null;

        if (this == Blocks.field_150338_P) {
            BlockSapling.treeType = TreeType.BROWN_MUSHROOM; // CraftBukkit
            worldgenhugemushroom = new WorldGenBigMushroom(Blocks.field_150420_aW);
        } else if (this == Blocks.field_150337_Q) {
            BlockSapling.treeType = TreeType.RED_MUSHROOM; // CraftBukkit
            worldgenhugemushroom = new WorldGenBigMushroom(Blocks.field_150419_aX);
        }

        if (worldgenhugemushroom != null && worldgenhugemushroom.func_180709_b(world, random, blockposition)) {
            return true;
        } else {
            world.func_180501_a(blockposition, iblockdata, 3);
            return false;
        }
    }

    public boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return true;
    }

    public boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return (double) random.nextFloat() < 0.4D;
    }

    public void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176485_d(world, blockposition, iblockdata, random);
    }
}
