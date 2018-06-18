package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
// CraftBukkit end

public class BlockMycelium extends Block {

    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    protected BlockMycelium() {
        super(Material.GRASS, MapColor.PURPLE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockMycelium.SNOWY, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        Block block = iblockaccess.getBlockState(blockposition.up()).getBlock();

        return iblockdata.withProperty(BlockMycelium.SNOWY, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.SNOW_LAYER));
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            if (world.getLightFromNeighbors(blockposition.up()) < 4 && world.getBlockState(blockposition.up()).getLightOpacity() > 2) {
                // CraftBukkit start
                // world.setTypeUpdate(blockposition, Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT));
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
                if (world.getLightFromNeighbors(blockposition.up()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockposition1 = blockposition.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                        IBlockState iblockdata1 = world.getBlockState(blockposition1);
                        IBlockState iblockdata2 = world.getBlockState(blockposition1.up());

                        if (iblockdata1.getBlock() == Blocks.DIRT && iblockdata1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && world.getLightFromNeighbors(blockposition1.up()) >= 4 && iblockdata2.getLightOpacity() <= 2) {
                            // CraftBukkit start
                            // world.setTypeUpdate(blockposition1, this.getBlockData());
                            org.bukkit.World bworld = world.getWorld();
                            BlockState blockState = bworld.getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ()).getState();
                            blockState.setType(CraftMagicNumbers.getMaterial(this));

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

    public int getMetaFromState(IBlockState iblockdata) {
        return 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockMycelium.SNOWY});
    }
}
