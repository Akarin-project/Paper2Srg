package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockFadeEvent;
// CraftBukkit end

public class BlockGrass extends Block implements IGrowable {

    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    protected BlockGrass() {
        super(Material.GRASS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockGrass.SNOWY, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        Block block = iblockaccess.getBlockState(blockposition.up()).getBlock();

        return iblockdata.withProperty(BlockGrass.SNOWY, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.SNOW_LAYER));
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.paperConfig.grassUpdateRate != 1 && (world.paperConfig.grassUpdateRate < 1 || (MinecraftServer.currentTick + blockposition.hashCode()) % world.paperConfig.grassUpdateRate != 0)) { return; } // Paper
        if (!world.isRemote) {
            int lightLevel = -1; // Paper
            if (world.getBlockState(blockposition.up()).getLightOpacity() > 2 && (lightLevel = world.getLightFromNeighbors(blockposition.up())) < 4) { // Paper - move light check to end to avoid unneeded light lookups
                // CraftBukkit start
                // world.setTypeUpdate(blockposition, Blocks.DIRT.getBlockData());
                org.bukkit.World bworld = world.getWorld();
                BlockState blockState = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()).getState();
                blockState.setType(CraftMagicNumbers.getMaterial(Blocks.DIRT));

                BlockFadeEvent event = new BlockFadeEvent(blockState.getBlock(), blockState);
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    blockState.update(true);
                }
                // CraftBukkit end
            } else {
                // Paper start
                // If light was calculated above, reuse it, else grab it
                if (lightLevel == -1) {
                    lightLevel = world.getLightFromNeighbors(blockposition.up());
                }
                if (lightLevel >= 9) {
                    // Paper end
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockposition1 = blockposition.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                        IBlockState iblockdata2 = world.getTypeIfLoaded(blockposition1); // Paper - moved up
                        if (iblockdata2 == null) { // Paper
                            return;
                        }

                        IBlockState iblockdata1 = world.getBlockState(blockposition1.up());
                        //IBlockData iblockdata2 = world.getTypeIfLoaded(blockposition1); // Paper - moved up

                        if (iblockdata2.getBlock() == Blocks.DIRT && iblockdata2.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && iblockdata1.getLightOpacity() <= 2 && world.isLightLevel(blockposition1.up(), 4)) { // Paper - move last check before isLightLevel to avoid unneeded light checks
                            // CraftBukkit start
                            // world.setTypeUpdate(blockposition1, Blocks.GRASS.getBlockData());
                            org.bukkit.World bworld = world.getWorld();
                            BlockState blockState = bworld.getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ()).getState();
                            blockState.setType(CraftMagicNumbers.getMaterial(Blocks.GRASS));

                            BlockSpreadEvent event = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), blockState);
                            world.getServer().getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                blockState.update(true);
                            }
                            // CraftBukkit end
                        }
                    }
                }

            }
        }
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), random, i);
    }

    public boolean canGrow(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return true;
    }

    public boolean canUseBonemeal(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void grow(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        BlockPos blockposition1 = blockposition.up();
        int i = 0;

        while (i < 128) {
            BlockPos blockposition2 = blockposition1;
            int j = 0;

            while (true) {
                if (j < i / 16) {
                    blockposition2 = blockposition2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                    if (world.getBlockState(blockposition2.down()).getBlock() == Blocks.GRASS && !world.getBlockState(blockposition2).isNormalCube()) {
                        ++j;
                        continue;
                    }
                } else if (world.getBlockState(blockposition2).getBlock().blockMaterial == Material.AIR) {
                    if (random.nextInt(8) == 0) {
                        BlockFlower.EnumFlowerType blockflowers_enumflowervarient = world.getBiome(blockposition2).pickRandomFlower(random, blockposition2);
                        BlockFlower blockflowers = blockflowers_enumflowervarient.getBlockType().getBlock();
                        IBlockState iblockdata1 = blockflowers.getDefaultState().withProperty(blockflowers.getTypeProperty(), blockflowers_enumflowervarient);

                        if (blockflowers.canBlockStay(world, blockposition2, iblockdata1)) {
                            // world.setTypeAndData(blockposition2, iblockdata1, 3); // CraftBukkit
                            CraftEventFactory.handleBlockGrowEvent(world, blockposition2.getX(), blockposition2.getY(), blockposition2.getZ(), iblockdata1.getBlock(), iblockdata1.getBlock().getMetaFromState(iblockdata1)); // CraftBukkit
                        }
                    } else {
                        IBlockState iblockdata2 = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);

                        if (Blocks.TALLGRASS.canBlockStay(world, blockposition2, iblockdata2)) {
                            // world.setTypeAndData(blockposition2, iblockdata2, 3); // CraftBukkit
                            CraftEventFactory.handleBlockGrowEvent(world, blockposition2.getX(), blockposition2.getY(), blockposition2.getZ(), iblockdata2.getBlock(), iblockdata2.getBlock().getMetaFromState(iblockdata2)); // CraftBukkit
                        }
                    }
                }

                ++i;
                break;
            }
        }

    }

    public int getMetaFromState(IBlockState iblockdata) {
        return 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockGrass.SNOWY});
    }
}
