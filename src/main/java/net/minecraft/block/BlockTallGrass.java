package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable {

    public static final PropertyEnum<BlockTallGrass.EnumType> TYPE = PropertyEnum.create("type", BlockTallGrass.EnumType.class);
    protected static final AxisAlignedBB TALL_GRASS_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    protected BlockTallGrass() {
        super(Material.VINE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.DEAD_BUSH));
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockTallGrass.TALL_GRASS_AABB;
    }

    public boolean canBlockStay(World world, BlockPos blockposition, IBlockState iblockdata) {
        return this.canSustainBush(world.getBlockState(blockposition.down()));
    }

    public boolean isReplaceable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return random.nextInt(8) == 0 ? Items.WHEAT_SEEDS : Items.AIR;
    }

    public int quantityDroppedWithBonus(int i, Random random) {
        return 1 + random.nextInt(i * 2 + 1);
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.isRemote && itemstack.getItem() == Items.SHEARS) {
            entityhuman.addStat(StatList.getBlockStats((Block) this));
            spawnAsEntity(world, blockposition, new ItemStack(Blocks.TALLGRASS, 1, ((BlockTallGrass.EnumType) iblockdata.getValue(BlockTallGrass.TYPE)).getMeta()));
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }

    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, iblockdata.getBlock().getMetaFromState(iblockdata));
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        for (int i = 1; i < 3; ++i) {
            nonnulllist.add(new ItemStack(this, 1, i));
        }

    }

    public boolean canGrow(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return iblockdata.getValue(BlockTallGrass.TYPE) != BlockTallGrass.EnumType.DEAD_BUSH;
    }

    public boolean canUseBonemeal(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void grow(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = BlockDoublePlant.EnumPlantType.GRASS;

        if (iblockdata.getValue(BlockTallGrass.TYPE) == BlockTallGrass.EnumType.FERN) {
            blocktallplant_enumtallflowervariants = BlockDoublePlant.EnumPlantType.FERN;
        }

        if (Blocks.DOUBLE_PLANT.canPlaceBlockAt(world, blockposition)) {
            Blocks.DOUBLE_PLANT.placeAt(world, blockposition, blocktallplant_enumtallflowervariants, 2);
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockTallGrass.EnumType) iblockdata.getValue(BlockTallGrass.TYPE)).getMeta();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockTallGrass.TYPE});
    }

    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XYZ;
    }

    public static enum EnumType implements IStringSerializable {

        DEAD_BUSH(0, "dead_bush"), GRASS(1, "tall_grass"), FERN(2, "fern");

        private static final BlockTallGrass.EnumType[] META_LOOKUP = new BlockTallGrass.EnumType[values().length];
        private final int meta;
        private final String name;

        private EnumType(int i, String s) {
            this.meta = i;
            this.name = s;
        }

        public int getMeta() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static BlockTallGrass.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockTallGrass.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockTallGrass.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        static {
            BlockTallGrass.EnumType[] ablocklonggrass_enumtallgrasstype = values();
            int i = ablocklonggrass_enumtallgrasstype.length;

            for (int j = 0; j < i; ++j) {
                BlockTallGrass.EnumType blocklonggrass_enumtallgrasstype = ablocklonggrass_enumtallgrasstype[j];

                BlockTallGrass.EnumType.META_LOOKUP[blocklonggrass_enumtallgrasstype.getMeta()] = blocklonggrass_enumtallgrasstype;
            }

        }
    }
}
