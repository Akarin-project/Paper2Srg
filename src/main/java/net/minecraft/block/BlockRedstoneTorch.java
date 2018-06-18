package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Random;


import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstoneTorch extends BlockTorch {

    private static final Map<World, List<BlockRedstoneTorch.Toggle>> toggles = new java.util.WeakHashMap(); // Spigot
    private final boolean isOn;

    private boolean isBurnedOut(World world, BlockPos blockposition, boolean flag) {
        if (!BlockRedstoneTorch.toggles.containsKey(world)) {
            BlockRedstoneTorch.toggles.put(world, Lists.<BlockRedstoneTorch.Toggle>newArrayList()); // CraftBukkit - fix decompile error
        }

        List list = (List) BlockRedstoneTorch.toggles.get(world);

        if (flag) {
            list.add(new BlockRedstoneTorch.Toggle(blockposition, world.getTotalWorldTime()));
        }

        int i = 0;

        for (int j = 0; j < list.size(); ++j) {
            BlockRedstoneTorch.Toggle blockredstonetorch_redstoneupdateinfo = (BlockRedstoneTorch.Toggle) list.get(j);

            if (blockredstonetorch_redstoneupdateinfo.pos.equals(blockposition)) {
                ++i;
                if (i >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(boolean flag) {
        this.isOn = flag;
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs) null);
    }

    public int tickRate(World world) {
        return 2;
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.isOn) {
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.notifyNeighborsOfStateChange(blockposition.offset(enumdirection), this, false);
            }
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.isOn) {
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.notifyNeighborsOfStateChange(blockposition.offset(enumdirection), this, false);
            }
        }

    }

    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return this.isOn && iblockdata.getValue(BlockRedstoneTorch.FACING) != enumdirection ? 15 : 0;
    }

    private boolean shouldBeOff(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = ((EnumFacing) iblockdata.getValue(BlockRedstoneTorch.FACING)).getOpposite();

        return world.isSidePowered(blockposition.offset(enumdirection), enumdirection);
    }

    public void randomTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        boolean flag = this.shouldBeOff(world, blockposition, iblockdata);
        List list = (List) BlockRedstoneTorch.toggles.get(world);

        // Paper start
        if (list != null) {
            int index = 0;
            while (index < list.size() && world.getTotalWorldTime() - ((BlockRedstoneTorch.Toggle) list.get(index)).getTime() > 60L) {
                index++;
            }
            if (index > 0) {
                list.subList(0, index).clear();
            }
        }
        // Paper end

        // CraftBukkit start
        org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
        int oldCurrent = this.isOn ? 15 : 0;

        BlockRedstoneEvent event = new BlockRedstoneEvent(block, oldCurrent, oldCurrent);
        // CraftBukkit end

        if (this.isOn) {
            if (flag) {
                // CraftBukkit start
                if (oldCurrent != 0) {
                    event.setNewCurrent(0);
                    manager.callEvent(event);
                    if (event.getNewCurrent() != 0) {
                        return;
                    }
                }
                // CraftBukkit end
                world.setBlockState(blockposition, Blocks.UNLIT_REDSTONE_TORCH.getDefaultState().withProperty(BlockRedstoneTorch.FACING, iblockdata.getValue(BlockRedstoneTorch.FACING)), 3);
                if (this.isBurnedOut(world, blockposition, true)) {
                    world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int i = 0; i < 5; ++i) {
                        double d0 = (double) blockposition.getX() + random.nextDouble() * 0.6D + 0.2D;
                        double d1 = (double) blockposition.getY() + random.nextDouble() * 0.6D + 0.2D;
                        double d2 = (double) blockposition.getZ() + random.nextDouble() * 0.6D + 0.2D;

                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
                    }

                    world.scheduleUpdate(blockposition, world.getBlockState(blockposition).getBlock(), 160);
                }
            }
        } else if (!flag && !this.isBurnedOut(world, blockposition, false)) {
            // CraftBukkit start
            if (oldCurrent != 15) {
                event.setNewCurrent(15);
                manager.callEvent(event);
                if (event.getNewCurrent() != 15) {
                    return;
                }
            }
            // CraftBukkit end
            world.setBlockState(blockposition, Blocks.REDSTONE_TORCH.getDefaultState().withProperty(BlockRedstoneTorch.FACING, iblockdata.getValue(BlockRedstoneTorch.FACING)), 3);
        }

    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.onNeighborChangeInternal(world, blockposition, iblockdata)) {
            if (this.isOn == this.shouldBeOff(world, blockposition, iblockdata)) {
                world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
            }

        }
    }

    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? iblockdata.getWeakPower(iblockaccess, blockposition, enumdirection) : 0;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.REDSTONE_TORCH);
    }

    public boolean canProvidePower(IBlockState iblockdata) {
        return true;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.REDSTONE_TORCH);
    }

    public boolean isAssociatedBlock(Block block) {
        return block == Blocks.UNLIT_REDSTONE_TORCH || block == Blocks.REDSTONE_TORCH;
    }

    static class Toggle {

        BlockPos pos;
        long time; final long getTime() { return this.time; } // Paper - OBFHELPER

        public Toggle(BlockPos blockposition, long i) {
            this.pos = blockposition;
            this.time = i;
        }
    }
}
