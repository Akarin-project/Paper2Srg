package net.minecraft.block;

import java.util.Random;

import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;

// CraftBukkit start
import java.util.List;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class BlockSapling extends BlockBush implements IGrowable {

    public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    public static TreeType treeType; // CraftBukkit

    protected BlockSapling() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.OAK).withProperty(BlockSapling.STAGE, Integer.valueOf(0)));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockSapling.SAPLING_AABB;
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName() + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            super.updateTick(world, blockposition, iblockdata, random);
            if (world.isLightLevel(blockposition.up(), 9) && random.nextInt(Math.max(2, (int) (((100.0F / world.spigotConfig.saplingModifier) * 7) + 0.5F))) == 0) { // Spigot // Paper
                // CraftBukkit start
                world.captureTreeGeneration = true;
                // CraftBukkit end
                this.grow(world, blockposition, iblockdata, random);
                // CraftBukkit start
                world.captureTreeGeneration = false;
                if (world.capturedBlockStates.size() > 0) {
                    TreeType treeType = BlockSapling.treeType;
                    BlockSapling.treeType = null;
                    Location location = new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
                    List<BlockState> blocks = (List<BlockState>) world.capturedBlockStates.clone();
                    world.capturedBlockStates.clear();
                    StructureGrowEvent event = null;
                    if (treeType != null) {
                        event = new StructureGrowEvent(location, treeType, false, null, blocks);
                        org.bukkit.Bukkit.getPluginManager().callEvent(event);
                    }
                    if (event == null || !event.isCancelled()) {
                        for (BlockState blockstate : blocks) {
                            blockstate.update(true);
                        }
                    }
                }
                // CraftBukkit end
            }

        }
    }

    public void grow(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (((Integer) iblockdata.getValue(BlockSapling.STAGE)).intValue() == 0) {
            world.setBlockState(blockposition, iblockdata.cycleProperty((IProperty) BlockSapling.STAGE), 4);
        } else {
            this.generateTree(world, blockposition, iblockdata, random);
        }

    }

    public void generateTree(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        // CraftBukkit start - Turn ternary operator into if statement to set treeType
        // Object object = random.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        Object object;
        if (random.nextInt(10) == 0) {
            treeType = TreeType.BIG_TREE;
            object = new WorldGenBigTree(true);
        } else {
            treeType = TreeType.TREE;
            object = new WorldGenTrees(true);
        }
        // CraftBukkit end
        int i = 0;
        int j = 0;
        boolean flag = false;
        IBlockState iblockdata1;

        switch ((BlockPlanks.EnumType) iblockdata.getValue(BlockSapling.TYPE)) {
        case SPRUCE:
            label66:
            for (i = 0; i >= -1; --i) {
                for (j = 0; j >= -1; --j) {
                    if (this.isTwoByTwoOfType(world, blockposition, i, j, BlockPlanks.EnumType.SPRUCE)) {
                        treeType = TreeType.MEGA_REDWOOD; // CraftBukkit
                        object = new WorldGenMegaPineTree(false, random.nextBoolean());
                        flag = true;
                        break label66;
                    }
                }
            }

            if (!flag) {
                i = 0;
                j = 0;
                treeType = TreeType.REDWOOD; // CraftBukkit
                object = new WorldGenTaiga2(true);
            }
            break;

        case BIRCH:
            treeType = TreeType.BIRCH; // CraftBukkit
            object = new WorldGenBirchTree(true, false);
            break;

        case JUNGLE:
            iblockdata1 = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
            IBlockState iblockdata2 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

            label78:
            for (i = 0; i >= -1; --i) {
                for (j = 0; j >= -1; --j) {
                    if (this.isTwoByTwoOfType(world, blockposition, i, j, BlockPlanks.EnumType.JUNGLE)) {
                        treeType = TreeType.JUNGLE; // CraftBukkit
                        object = new WorldGenMegaJungle(true, 10, 20, iblockdata1, iblockdata2);
                        flag = true;
                        break label78;
                    }
                }
            }

            if (!flag) {
                i = 0;
                j = 0;
                treeType = TreeType.SMALL_JUNGLE; // CraftBukkit
                object = new WorldGenTrees(true, 4 + random.nextInt(7), iblockdata1, iblockdata2, false);
            }
            break;

        case ACACIA:
            treeType = TreeType.ACACIA; // CraftBukkit
            object = new WorldGenSavannaTree(true);
            break;

        case DARK_OAK:
            label90:
            for (i = 0; i >= -1; --i) {
                for (j = 0; j >= -1; --j) {
                    if (this.isTwoByTwoOfType(world, blockposition, i, j, BlockPlanks.EnumType.DARK_OAK)) {
                        treeType = TreeType.DARK_OAK; // CraftBukkit
                        object = new WorldGenCanopyTree(true);
                        flag = true;
                        break label90;
                    }
                }
            }

            if (!flag) {
                return;
            }

        case OAK:
        }

        iblockdata1 = Blocks.AIR.getDefaultState();
        if (flag) {
            world.setBlockState(blockposition.add(i, 0, j), iblockdata1, 4);
            world.setBlockState(blockposition.add(i + 1, 0, j), iblockdata1, 4);
            world.setBlockState(blockposition.add(i, 0, j + 1), iblockdata1, 4);
            world.setBlockState(blockposition.add(i + 1, 0, j + 1), iblockdata1, 4);
        } else {
            world.setBlockState(blockposition, iblockdata1, 4);
        }

        if (!((WorldGenerator) object).generate(world, random, blockposition.add(i, 0, j))) {
            if (flag) {
                world.setBlockState(blockposition.add(i, 0, j), iblockdata, 4);
                world.setBlockState(blockposition.add(i + 1, 0, j), iblockdata, 4);
                world.setBlockState(blockposition.add(i, 0, j + 1), iblockdata, 4);
                world.setBlockState(blockposition.add(i + 1, 0, j + 1), iblockdata, 4);
            } else {
                world.setBlockState(blockposition, iblockdata, 4);
            }
        }

    }

    private boolean isTwoByTwoOfType(World world, BlockPos blockposition, int i, int j, BlockPlanks.EnumType blockwood_enumlogvariant) {
        return this.isTypeAt(world, blockposition.add(i, 0, j), blockwood_enumlogvariant) && this.isTypeAt(world, blockposition.add(i + 1, 0, j), blockwood_enumlogvariant) && this.isTypeAt(world, blockposition.add(i, 0, j + 1), blockwood_enumlogvariant) && this.isTypeAt(world, blockposition.add(i + 1, 0, j + 1), blockwood_enumlogvariant);
    }

    public boolean isTypeAt(World world, BlockPos blockposition, BlockPlanks.EnumType blockwood_enumlogvariant) {
        IBlockState iblockdata = world.getBlockState(blockposition);

        return iblockdata.getBlock() == this && iblockdata.getValue(BlockSapling.TYPE) == blockwood_enumlogvariant;
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockSapling.TYPE)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockPlanks.EnumType[] ablockwood_enumlogvariant = BlockPlanks.EnumType.values();
        int i = ablockwood_enumlogvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockPlanks.EnumType blockwood_enumlogvariant = ablockwood_enumlogvariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockwood_enumlogvariant.getMetadata()));
        }

    }

    public boolean canGrow(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return true;
    }

    public boolean canUseBonemeal(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return (double) world.rand.nextFloat() < 0.45D;
    }

    public void grow(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        this.grow(world, blockposition, iblockdata, random);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.byMetadata(i & 7)).withProperty(BlockSapling.STAGE, Integer.valueOf((i & 8) >> 3));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.getValue(BlockSapling.TYPE)).getMetadata();

        i |= ((Integer) iblockdata.getValue(BlockSapling.STAGE)).intValue() << 3;
        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockSapling.TYPE, BlockSapling.STAGE});
    }
}
