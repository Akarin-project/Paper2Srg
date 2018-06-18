package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockFlowerPot extends BlockContainer {

    public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
    public static final PropertyEnum<BlockFlowerPot.EnumFlowerType> CONTENTS = PropertyEnum.create("contents", BlockFlowerPot.EnumFlowerType.class);
    protected static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);

    public BlockFlowerPot() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerType.EMPTY).withProperty(BlockFlowerPot.LEGACY_DATA, Integer.valueOf(0)));
    }

    public String getLocalizedName() {
        return I18n.translateToLocal("item.flowerPot.name");
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockFlowerPot.FLOWER_POT_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(world, blockposition);

        if (tileentityflowerpot == null) {
            return false;
        } else {
            ItemStack itemstack1 = tileentityflowerpot.getFlowerItemStack();

            if (itemstack1.isEmpty()) {
                if (!this.canBePotted(itemstack)) {
                    return false;
                }

                tileentityflowerpot.setItemStack(itemstack);
                entityhuman.addStat(StatList.FLOWER_POTTED);
                if (!entityhuman.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
            } else {
                if (itemstack.isEmpty()) {
                    entityhuman.setHeldItem(enumhand, itemstack1);
                } else if (!entityhuman.addItemStackToInventory(itemstack1)) {
                    entityhuman.dropItem(itemstack1, false);
                }

                tileentityflowerpot.setItemStack(ItemStack.EMPTY);
            }

            tileentityflowerpot.markDirty();
            world.notifyBlockUpdate(blockposition, iblockdata, iblockdata, 3);
            return true;
        }
    }

    private boolean canBePotted(ItemStack itemstack) {
        Block block = Block.getBlockFromItem(itemstack.getItem());

        if (block != Blocks.YELLOW_FLOWER && block != Blocks.RED_FLOWER && block != Blocks.CACTUS && block != Blocks.BROWN_MUSHROOM && block != Blocks.RED_MUSHROOM && block != Blocks.SAPLING && block != Blocks.DEADBUSH) {
            int i = itemstack.getMetadata();

            return block == Blocks.TALLGRASS && i == BlockTallGrass.EnumType.FERN.getMeta();
        } else {
            return true;
        }
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(world, blockposition);

        if (tileentityflowerpot != null) {
            ItemStack itemstack = tileentityflowerpot.getFlowerItemStack();

            if (!itemstack.isEmpty()) {
                return itemstack;
            }
        }

        return new ItemStack(Items.FLOWER_POT);
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return super.canPlaceBlockAt(world, blockposition) && world.getBlockState(blockposition.down()).isTopSolid();
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.getBlockState(blockposition.down()).isTopSolid()) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(world, blockposition);

        if (tileentityflowerpot != null && tileentityflowerpot.getFlowerPotItem() != null) {
            spawnAsEntity(world, blockposition, new ItemStack(tileentityflowerpot.getFlowerPotItem(), 1, tileentityflowerpot.getFlowerPotData()));
        }

        super.breakBlock(world, blockposition, iblockdata);
    }

    public void onBlockHarvested(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        super.onBlockHarvested(world, blockposition, iblockdata, entityhuman);
        if (entityhuman.capabilities.isCreativeMode) {
            TileEntityFlowerPot tileentityflowerpot = this.getTileEntity(world, blockposition);

            if (tileentityflowerpot != null) {
                tileentityflowerpot.setItemStack(ItemStack.EMPTY);
            }
        }

    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.FLOWER_POT;
    }

    @Nullable
    private TileEntityFlowerPot getTileEntity(World world, BlockPos blockposition) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity instanceof TileEntityFlowerPot ? (TileEntityFlowerPot) tileentity : null;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        Object object = null;
        int j = 0;

        switch (i) {
        case 1:
            object = Blocks.RED_FLOWER;
            j = BlockFlower.EnumFlowerType.POPPY.getMeta();
            break;

        case 2:
            object = Blocks.YELLOW_FLOWER;
            break;

        case 3:
            object = Blocks.SAPLING;
            j = BlockPlanks.EnumType.OAK.getMetadata();
            break;

        case 4:
            object = Blocks.SAPLING;
            j = BlockPlanks.EnumType.SPRUCE.getMetadata();
            break;

        case 5:
            object = Blocks.SAPLING;
            j = BlockPlanks.EnumType.BIRCH.getMetadata();
            break;

        case 6:
            object = Blocks.SAPLING;
            j = BlockPlanks.EnumType.JUNGLE.getMetadata();
            break;

        case 7:
            object = Blocks.RED_MUSHROOM;
            break;

        case 8:
            object = Blocks.BROWN_MUSHROOM;
            break;

        case 9:
            object = Blocks.CACTUS;
            break;

        case 10:
            object = Blocks.DEADBUSH;
            break;

        case 11:
            object = Blocks.TALLGRASS;
            j = BlockTallGrass.EnumType.FERN.getMeta();
            break;

        case 12:
            object = Blocks.SAPLING;
            j = BlockPlanks.EnumType.ACACIA.getMetadata();
            break;

        case 13:
            object = Blocks.SAPLING;
            j = BlockPlanks.EnumType.DARK_OAK.getMetadata();
        }

        return new TileEntityFlowerPot(Item.getItemFromBlock((Block) object), j);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockFlowerPot.CONTENTS, BlockFlowerPot.LEGACY_DATA});
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockFlowerPot.LEGACY_DATA)).intValue();
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockFlowerPot.EnumFlowerType blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.EMPTY;
        TileEntity tileentity = iblockaccess instanceof ChunkCache ? ((ChunkCache) iblockaccess).getTileEntity(blockposition, Chunk.EnumCreateEntityType.CHECK) : iblockaccess.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityFlowerPot) {
            TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot) tileentity;
            Item item = tileentityflowerpot.getFlowerPotItem();

            if (item instanceof ItemBlock) {
                int i = tileentityflowerpot.getFlowerPotData();
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.SAPLING) {
                    switch (BlockPlanks.EnumType.byMetadata(i)) {
                    case OAK:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.OAK_SAPLING;
                        break;

                    case SPRUCE:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.SPRUCE_SAPLING;
                        break;

                    case BIRCH:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.BIRCH_SAPLING;
                        break;

                    case JUNGLE:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.JUNGLE_SAPLING;
                        break;

                    case ACACIA:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.ACACIA_SAPLING;
                        break;

                    case DARK_OAK:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.DARK_OAK_SAPLING;
                        break;

                    default:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.EMPTY;
                    }
                } else if (block == Blocks.TALLGRASS) {
                    switch (i) {
                    case 0:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
                        break;

                    case 2:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.FERN;
                        break;

                    default:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.EMPTY;
                    }
                } else if (block == Blocks.YELLOW_FLOWER) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.DANDELION;
                } else if (block == Blocks.RED_FLOWER) {
                    switch (BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, i)) {
                    case POPPY:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.POPPY;
                        break;

                    case BLUE_ORCHID:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.BLUE_ORCHID;
                        break;

                    case ALLIUM:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.ALLIUM;
                        break;

                    case HOUSTONIA:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.HOUSTONIA;
                        break;

                    case RED_TULIP:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.RED_TULIP;
                        break;

                    case ORANGE_TULIP:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.ORANGE_TULIP;
                        break;

                    case WHITE_TULIP:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.WHITE_TULIP;
                        break;

                    case PINK_TULIP:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.PINK_TULIP;
                        break;

                    case OXEYE_DAISY:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.OXEYE_DAISY;
                        break;

                    default:
                        blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.EMPTY;
                    }
                } else if (block == Blocks.RED_MUSHROOM) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.MUSHROOM_RED;
                } else if (block == Blocks.BROWN_MUSHROOM) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.MUSHROOM_BROWN;
                } else if (block == Blocks.DEADBUSH) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
                } else if (block == Blocks.CACTUS) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.CACTUS;
                }
            }
        }

        return iblockdata.withProperty(BlockFlowerPot.CONTENTS, blockflowerpot_enumflowerpotcontents);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumFlowerType implements IStringSerializable {

        EMPTY("empty"), POPPY("rose"), BLUE_ORCHID("blue_orchid"), ALLIUM("allium"), HOUSTONIA("houstonia"), RED_TULIP("red_tulip"), ORANGE_TULIP("orange_tulip"), WHITE_TULIP("white_tulip"), PINK_TULIP("pink_tulip"), OXEYE_DAISY("oxeye_daisy"), DANDELION("dandelion"), OAK_SAPLING("oak_sapling"), SPRUCE_SAPLING("spruce_sapling"), BIRCH_SAPLING("birch_sapling"), JUNGLE_SAPLING("jungle_sapling"), ACACIA_SAPLING("acacia_sapling"), DARK_OAK_SAPLING("dark_oak_sapling"), MUSHROOM_RED("mushroom_red"), MUSHROOM_BROWN("mushroom_brown"), DEAD_BUSH("dead_bush"), FERN("fern"), CACTUS("cactus");

        private final String name;

        private EnumFlowerType(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
