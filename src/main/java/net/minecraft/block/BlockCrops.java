package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockCrops extends BlockBush implements IGrowable {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    protected BlockCrops() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs) null);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCrops.CROPS_AABB[((Integer) iblockdata.getValue(this.getAgeProperty())).intValue()];
    }

    protected boolean canSustainBush(IBlockState iblockdata) {
        return iblockdata.getBlock() == Blocks.FARMLAND;
    }

    protected PropertyInteger getAgeProperty() {
        return BlockCrops.AGE;
    }

    public int getMaxAge() {
        return 7;
    }

    protected int getAge(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(this.getAgeProperty())).intValue();
    }

    public IBlockState withAge(int i) {
        return this.getDefaultState().withProperty(this.getAgeProperty(), Integer.valueOf(i));
    }

    public boolean isMaxAge(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(this.getAgeProperty())).intValue() >= this.getMaxAge();
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        super.updateTick(world, blockposition, iblockdata, random);
        if (world.isLightLevel(blockposition.up(), 9)) { // Paper
            int i = this.getAge(iblockdata);

            if (i < this.getMaxAge()) {
                float f = getGrowthChance((Block) this, world, blockposition);

                if (random.nextInt((int) ((100.0F / world.spigotConfig.wheatModifier) * (25.0F / f)) + 1) == 0) { // Spigot
                    // CraftBukkit start
                    IBlockState data = this.withAge(i + 1);
                    CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, getMetaFromState(data));
                    // CraftBukkit end
                }
            }
        }

    }

    public void grow(World world, BlockPos blockposition, IBlockState iblockdata) {
        int i = this.getAge(iblockdata) + this.getBonemealAgeIncrease(world);
        int j = this.getMaxAge();

        if (i > j) {
            i = j;
        }

        // CraftBukkit start
        IBlockState data = this.withAge(i);
        CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, getMetaFromState(data));
        // CraftBukkit end
    }

    protected int getBonemealAgeIncrease(World world) {
        return MathHelper.getInt(world.rand, 2, 5);
    }

    protected static float getGrowthChance(Block block, World world, BlockPos blockposition) {
        float f = 1.0F;
        BlockPos blockposition1 = blockposition.down();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                IBlockState iblockdata = world.getBlockState(blockposition1.add(i, 0, j));

                if (iblockdata.getBlock() == Blocks.FARMLAND) {
                    f1 = 1.0F;
                    if (((Integer) iblockdata.getValue(BlockFarmland.MOISTURE)).intValue() > 0) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockposition2 = blockposition.north();
        BlockPos blockposition3 = blockposition.south();
        BlockPos blockposition4 = blockposition.west();
        BlockPos blockposition5 = blockposition.east();
        boolean flag = block == world.getBlockState(blockposition4).getBlock() || block == world.getBlockState(blockposition5).getBlock();
        boolean flag1 = block == world.getBlockState(blockposition2).getBlock() || block == world.getBlockState(blockposition3).getBlock();

        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = block == world.getBlockState(blockposition4.north()).getBlock() || block == world.getBlockState(blockposition5.north()).getBlock() || block == world.getBlockState(blockposition5.south()).getBlock() || block == world.getBlockState(blockposition4.south()).getBlock();

            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    public boolean canBlockStay(World world, BlockPos blockposition, IBlockState iblockdata) {
        return (world.getLight(blockposition) >= 8 || world.canSeeSky(blockposition)) && this.canSustainBush(world.getBlockState(blockposition.down()));
    }

    protected Item getSeed() {
        return Items.WHEAT_SEEDS;
    }

    protected Item getCrop() {
        return Items.WHEAT;
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        super.dropBlockAsItemWithChance(world, blockposition, iblockdata, f, 0);
        if (!world.isRemote) {
            int j = this.getAge(iblockdata);

            if (j >= this.getMaxAge()) {
                int k = 3 + i;

                for (int l = 0; l < k; ++l) {
                    if (world.rand.nextInt(2 * this.getMaxAge()) <= j) {
                        spawnAsEntity(world, blockposition, new ItemStack(this.getSeed()));
                    }
                }
            }

        }
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return this.isMaxAge(iblockdata) ? this.getCrop() : this.getSeed();
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this.getSeed());
    }

    public boolean canGrow(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return !this.isMaxAge(iblockdata);
    }

    public boolean canUseBonemeal(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void grow(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        this.grow(world, blockposition, iblockdata);
    }

    public IBlockState getStateFromMeta(int i) {
        return this.withAge(i);
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return this.getAge(iblockdata);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockCrops.AGE});
    }
}
