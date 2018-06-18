package net.minecraft.block;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockFromToEvent;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockFromToEvent;
// CraftBukkit end

public class BlockDynamicLiquid extends BlockLiquid {

    int adjacentSourceBlocks;

    protected BlockDynamicLiquid(Material material) {
        super(material);
    }

    private void placeStaticBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.setBlockState(blockposition, getStaticBlock(this.blockMaterial).getDefaultState().withProperty(BlockDynamicLiquid.LEVEL, iblockdata.getValue(BlockDynamicLiquid.LEVEL)), 2);
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        int i = ((Integer) iblockdata.getValue(BlockDynamicLiquid.LEVEL)).intValue();
        byte b0 = 1;

        if (this.blockMaterial == Material.LAVA && !world.provider.doesWaterVaporize()) {
            b0 = 2;
        }

        int j = this.getFlowSpeed(world, blockposition); // Paper
        int k;

        if (i > 0) {
            int l = -100;

            this.adjacentSourceBlocks = 0;

            EnumFacing enumdirection;

            for (Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator(); iterator.hasNext(); l = this.checkAdjacentBlock(world, blockposition.offset(enumdirection), l)) {
                enumdirection = (EnumFacing) iterator.next();
            }

            int i1 = l + b0;

            if (i1 >= 8 || l < 0) {
                i1 = -1;
            }

            k = this.getDepth(world.getBlockState(blockposition.up()));
            if (k >= 0) {
                if (k >= 8) {
                    i1 = k;
                } else {
                    i1 = k + 8;
                }
            }

            if (this.adjacentSourceBlocks >= 2 && this.blockMaterial == Material.WATER) {
                IBlockState iblockdata1 = world.getBlockState(blockposition.down());

                if (iblockdata1.getMaterial().isSolid()) {
                    i1 = 0;
                } else if (iblockdata1.getMaterial() == this.blockMaterial && ((Integer) iblockdata1.getValue(BlockDynamicLiquid.LEVEL)).intValue() == 0) {
                    i1 = 0;
                }
            }

            if (!world.paperConfig.fastDrainLava && this.blockMaterial == Material.LAVA && i < 8 && i1 < 8 && i1 > i && random.nextInt(4) != 0) { // Paper
                j *= 4;
            }

            if (i1 == i) {
                this.placeStaticBlock(world, blockposition, iblockdata);
            } else {
                i = i1;
                if (i1 < 0 || canFastDrain(world, blockposition)) { // Paper - Fast draining
                    world.setBlockToAir(blockposition);
                } else {
                    iblockdata = iblockdata.withProperty(BlockDynamicLiquid.LEVEL, Integer.valueOf(i1));
                    world.setBlockState(blockposition, iblockdata, 2);
                    world.scheduleUpdate(blockposition, (Block) this, j);
                    world.notifyNeighborsOfStateChange(blockposition, this, false);
                }
            }
        } else {
            this.placeStaticBlock(world, blockposition, iblockdata);
        }

        if (world.getBlockState(blockposition).getBlock().getDefaultState().getMaterial() != blockMaterial) return; // Paper - Stop updating flowing block if material has changed
        org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()); // CraftBukkit
        IBlockState iblockdata2 = world.getBlockState(blockposition.down());

        if (this.canFlowInto(world, blockposition.down(), iblockdata2)) {
            // CraftBukkit start
            if (!canFlowTo(world, source, BlockFace.DOWN)) { return; } // Paper
            BlockFromToEvent event = new BlockFromToEvent(source, BlockFace.DOWN);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (this.blockMaterial == Material.LAVA && world.getBlockState(blockposition.down()).getMaterial() == Material.WATER) {
                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition.down(), Blocks.STONE.getDefaultState(), null)) {
                    this.triggerMixEffects(world, blockposition.down());
                }
                // CraftBukkit end
                return;
            }

            if (i >= 8) {
                this.tryFlowInto(world, blockposition.down(), iblockdata2, i);
            } else {
                this.tryFlowInto(world, blockposition.down(), iblockdata2, i + 8);
            }
        } else if (i >= 0 && (i == 0 || this.isBlocked(world, blockposition.down(), iblockdata2))) {
            Set set = this.getPossibleFlowDirections(world, blockposition);

            k = i + b0;
            if (i >= 8) {
                k = 1;
            }

            if (k >= 8) {
                return;
            }

            Iterator iterator1 = set.iterator();

            while (iterator1.hasNext()) {
                EnumFacing enumdirection1 = (EnumFacing) iterator1.next();

                // CraftBukkit start
                if (!canFlowTo(world, source, org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(enumdirection1))) { continue; } // Paper
                BlockFromToEvent event = new BlockFromToEvent(source, org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(enumdirection1));
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.tryFlowInto(world, blockposition.offset(enumdirection1), world.getBlockState(blockposition.offset(enumdirection1)), k);
                }
                // CraftBukkit end
            }
        }

    }

    // Paper start
    private boolean canFlowTo(World world, org.bukkit.block.Block source, BlockFace face) {
        return source.getWorld().isChunkLoaded((source.getX() + face.getModX()) >> 4, (source.getZ() + face.getModZ()) >> 4);
    }
    // Paper end

    private void tryFlowInto(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        if (/*world.isLoaded(blockposition) &&*/ this.canFlowInto(world, blockposition, iblockdata)) { // CraftBukkit - add isLoaded check // Paper - Already checked before we get here for isLoaded
            if (iblockdata.getMaterial() != Material.AIR) {
                if (this.blockMaterial == Material.LAVA) {
                    this.triggerMixEffects(world, blockposition);
                } else {
                    iblockdata.getBlock().dropBlockAsItem(world, blockposition, iblockdata, 0);
                }
            }

            world.setBlockState(blockposition, this.getDefaultState().withProperty(BlockDynamicLiquid.LEVEL, Integer.valueOf(i)), 3);
        }

    }

    private int getSlopeDistance(World world, BlockPos blockposition, int i, EnumFacing enumdirection) {
        int j = 1000;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection1 = (EnumFacing) iterator.next();

            if (enumdirection1 != enumdirection) {
                BlockPos blockposition1 = blockposition.offset(enumdirection1);
                IBlockState iblockdata = world.getBlockState(blockposition1);

                if (!this.isBlocked(world, blockposition1, iblockdata) && (iblockdata.getMaterial() != this.blockMaterial || ((Integer) iblockdata.getValue(BlockDynamicLiquid.LEVEL)).intValue() > 0)) {
                    if (!this.isBlocked(world, blockposition1.down(), iblockdata)) {
                        return i;
                    }

                    if (i < this.getSlopeFindDistance(world)) {
                        int k = this.getSlopeDistance(world, blockposition1, i + 1, enumdirection1.getOpposite());

                        if (k < j) {
                            j = k;
                        }
                    }
                }
            }
        }

        return j;
    }

    private int getSlopeFindDistance(World world) {
        return this.blockMaterial == Material.LAVA && !world.provider.doesWaterVaporize() ? 2 : 4;
    }

    private Set<EnumFacing> getPossibleFlowDirections(World world, BlockPos blockposition) {
        int i = 1000;
        EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            BlockPos blockposition1 = blockposition.offset(enumdirection);
            IBlockState iblockdata = world.getBlockState(blockposition1);

            if (!this.isBlocked(world, blockposition1, iblockdata) && (iblockdata.getMaterial() != this.blockMaterial || ((Integer) iblockdata.getValue(BlockDynamicLiquid.LEVEL)).intValue() > 0)) {
                int j;

                if (this.isBlocked(world, blockposition1.down(), world.getBlockState(blockposition1.down()))) {
                    j = this.getSlopeDistance(world, blockposition1, 1, enumdirection.getOpposite());
                } else {
                    j = 0;
                }

                if (j < i) {
                    enumset.clear();
                }

                if (j <= i) {
                    enumset.add(enumdirection);
                    i = j;
                }
            }
        }

        return enumset;
    }

    private boolean isBlocked(World world, BlockPos blockposition, IBlockState iblockdata) {
        Block block = world.getBlockState(blockposition).getBlock();

        return !(block instanceof BlockDoor) && block != Blocks.STANDING_SIGN && block != Blocks.LADDER && block != Blocks.REEDS ? (block.blockMaterial != Material.PORTAL && block.blockMaterial != Material.STRUCTURE_VOID ? block.blockMaterial.blocksMovement() : true) : true;
    }

    protected int checkAdjacentBlock(World world, BlockPos blockposition, int i) {
        int j = this.getDepth(world.getBlockState(blockposition));

        if (j < 0) {
            return i;
        } else {
            if (j == 0) {
                ++this.adjacentSourceBlocks;
            }

            if (j >= 8) {
                j = 0;
            }

            return i >= 0 && j >= i ? i : j;
        }
    }

    private boolean canFlowInto(World world, BlockPos blockposition, IBlockState iblockdata) {
        Material material = iblockdata.getMaterial();

        return material != this.blockMaterial && material != Material.LAVA && !this.isBlocked(world, blockposition, iblockdata);
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.checkForMixing(world, blockposition, iblockdata)) {
            world.scheduleUpdate(blockposition, (Block) this, this.getFlowSpeed(world, blockposition)); // Paper
        }

    }

    // Paper start
    /**
     * Paper - Get flow speed. Throttle if its water and flowing adjacent to lava
     */
    public int getFlowSpeed(World world, BlockPos blockposition) {
        if (this.blockMaterial == Material.LAVA) {
            return world.provider.isSkyMissing() ? world.paperConfig.lavaFlowSpeedNether : world.paperConfig.lavaFlowSpeedNormal;
        }
        if (this.blockMaterial == Material.WATER && (
                world.getBlockState(blockposition.north(1)).getBlock().blockMaterial == Material.LAVA ||
                        world.getBlockState(blockposition.south(1)).getBlock().blockMaterial == Material.LAVA ||
                        world.getBlockState(blockposition.west(1)).getBlock().blockMaterial == Material.LAVA ||
                        world.getBlockState(blockposition.east(1)).getBlock().blockMaterial == Material.LAVA)) {
            return world.paperConfig.waterOverLavaFlowSpeed;
        }
        return super.tickRate(world);
    }

    private int getFluidLevel(IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockaccess.getBlockState(blockposition).getMaterial() == this.blockMaterial ? iblockaccess.getBlockState(blockposition).getValue(BlockLiquid.LEVEL) : -1;
    }

    /**
     * Paper - Data check method for fast draining
     */
    public int getData(World world, BlockPos position) {
        int data = this.getFluidLevel((IBlockAccess) world, position);
        return data < 8 ? data : 0;
    }

    /**
     * Paper - Checks surrounding blocks to determine if block can be fast drained
     */
    public boolean canFastDrain(World world, BlockPos position) {
        boolean result = false;
        int data = getData(world, position);
        if (this.blockMaterial == Material.WATER) {
            if (world.paperConfig.fastDrainWater) {
                result = true;
                if (getData(world, position.down()) < 0) {
                    result = false;
                } else if (world.getBlockState(position.north()).getBlock().getDefaultState().getMaterial() == Material.WATER && getData(world, position.north()) < data) {
                    result = false;
                } else if (world.getBlockState(position.south()).getBlock().getDefaultState().getMaterial() == Material.WATER && getData(world, position.south()) < data) {
                    result = false;
                } else if (world.getBlockState(position.west()).getBlock().getDefaultState().getMaterial() == Material.WATER && getData(world, position.west()) < data) {
                    result = false;
                } else if (world.getBlockState(position.east()).getBlock().getDefaultState().getMaterial() == Material.WATER && getData(world, position.east()) < data) {
                    result = false;
                }
            }
        } else if (this.blockMaterial == Material.LAVA) {
            if (world.paperConfig.fastDrainLava) {
                result = true;
                if (getData(world, position.down()) < 0 || world.getBlockState(position.up()).getBlock().getDefaultState().getMaterial() != Material.AIR) {
                    result = false;
                } else if (world.getBlockState(position.north()).getBlock().getDefaultState().getMaterial() == Material.LAVA && getData(world, position.north()) < data) {
                    result = false;
                } else if (world.getBlockState(position.south()).getBlock().getDefaultState().getMaterial() == Material.LAVA && getData(world, position.south()) < data) {
                    result = false;
                } else if (world.getBlockState(position.west()).getBlock().getDefaultState().getMaterial() == Material.LAVA && getData(world, position.west()) < data) {
                    result = false;
                } else if (world.getBlockState(position.east()).getBlock().getDefaultState().getMaterial() == Material.LAVA && getData(world, position.east()) < data) {
                    result = false;
                }
            }
        }
        return result;
    }
    // Paper end
}
