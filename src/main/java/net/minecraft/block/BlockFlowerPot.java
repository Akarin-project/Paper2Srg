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

    public static final PropertyInteger field_176444_a = PropertyInteger.func_177719_a("legacy_data", 0, 15);
    public static final PropertyEnum<BlockFlowerPot.EnumFlowerType> field_176443_b = PropertyEnum.func_177709_a("contents", BlockFlowerPot.EnumFlowerType.class);
    protected static final AxisAlignedBB field_185570_c = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);

    public BlockFlowerPot() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockFlowerPot.field_176443_b, BlockFlowerPot.EnumFlowerType.EMPTY).func_177226_a(BlockFlowerPot.field_176444_a, Integer.valueOf(0)));
    }

    public String func_149732_F() {
        return I18n.func_74838_a("item.flowerPot.name");
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockFlowerPot.field_185570_c;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        TileEntityFlowerPot tileentityflowerpot = this.func_176442_d(world, blockposition);

        if (tileentityflowerpot == null) {
            return false;
        } else {
            ItemStack itemstack1 = tileentityflowerpot.func_184403_b();

            if (itemstack1.func_190926_b()) {
                if (!this.func_190951_a(itemstack)) {
                    return false;
                }

                tileentityflowerpot.func_190614_a(itemstack);
                entityhuman.func_71029_a(StatList.field_188088_V);
                if (!entityhuman.field_71075_bZ.field_75098_d) {
                    itemstack.func_190918_g(1);
                }
            } else {
                if (itemstack.func_190926_b()) {
                    entityhuman.func_184611_a(enumhand, itemstack1);
                } else if (!entityhuman.func_191521_c(itemstack1)) {
                    entityhuman.func_71019_a(itemstack1, false);
                }

                tileentityflowerpot.func_190614_a(ItemStack.field_190927_a);
            }

            tileentityflowerpot.func_70296_d();
            world.func_184138_a(blockposition, iblockdata, iblockdata, 3);
            return true;
        }
    }

    private boolean func_190951_a(ItemStack itemstack) {
        Block block = Block.func_149634_a(itemstack.func_77973_b());

        if (block != Blocks.field_150327_N && block != Blocks.field_150328_O && block != Blocks.field_150434_aF && block != Blocks.field_150338_P && block != Blocks.field_150337_Q && block != Blocks.field_150345_g && block != Blocks.field_150330_I) {
            int i = itemstack.func_77960_j();

            return block == Blocks.field_150329_H && i == BlockTallGrass.EnumType.FERN.func_177044_a();
        } else {
            return true;
        }
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntityFlowerPot tileentityflowerpot = this.func_176442_d(world, blockposition);

        if (tileentityflowerpot != null) {
            ItemStack itemstack = tileentityflowerpot.func_184403_b();

            if (!itemstack.func_190926_b()) {
                return itemstack;
            }
        }

        return new ItemStack(Items.field_151162_bE);
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return super.func_176196_c(world, blockposition) && world.func_180495_p(blockposition.func_177977_b()).func_185896_q();
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.func_180495_p(blockposition.func_177977_b()).func_185896_q()) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntityFlowerPot tileentityflowerpot = this.func_176442_d(world, blockposition);

        if (tileentityflowerpot != null && tileentityflowerpot.func_145965_a() != null) {
            func_180635_a(world, blockposition, new ItemStack(tileentityflowerpot.func_145965_a(), 1, tileentityflowerpot.func_145966_b()));
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        super.func_176208_a(world, blockposition, iblockdata, entityhuman);
        if (entityhuman.field_71075_bZ.field_75098_d) {
            TileEntityFlowerPot tileentityflowerpot = this.func_176442_d(world, blockposition);

            if (tileentityflowerpot != null) {
                tileentityflowerpot.func_190614_a(ItemStack.field_190927_a);
            }
        }

    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151162_bE;
    }

    @Nullable
    private TileEntityFlowerPot func_176442_d(World world, BlockPos blockposition) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        return tileentity instanceof TileEntityFlowerPot ? (TileEntityFlowerPot) tileentity : null;
    }

    public TileEntity func_149915_a(World world, int i) {
        Object object = null;
        int j = 0;

        switch (i) {
        case 1:
            object = Blocks.field_150328_O;
            j = BlockFlower.EnumFlowerType.POPPY.func_176968_b();
            break;

        case 2:
            object = Blocks.field_150327_N;
            break;

        case 3:
            object = Blocks.field_150345_g;
            j = BlockPlanks.EnumType.OAK.func_176839_a();
            break;

        case 4:
            object = Blocks.field_150345_g;
            j = BlockPlanks.EnumType.SPRUCE.func_176839_a();
            break;

        case 5:
            object = Blocks.field_150345_g;
            j = BlockPlanks.EnumType.BIRCH.func_176839_a();
            break;

        case 6:
            object = Blocks.field_150345_g;
            j = BlockPlanks.EnumType.JUNGLE.func_176839_a();
            break;

        case 7:
            object = Blocks.field_150337_Q;
            break;

        case 8:
            object = Blocks.field_150338_P;
            break;

        case 9:
            object = Blocks.field_150434_aF;
            break;

        case 10:
            object = Blocks.field_150330_I;
            break;

        case 11:
            object = Blocks.field_150329_H;
            j = BlockTallGrass.EnumType.FERN.func_177044_a();
            break;

        case 12:
            object = Blocks.field_150345_g;
            j = BlockPlanks.EnumType.ACACIA.func_176839_a();
            break;

        case 13:
            object = Blocks.field_150345_g;
            j = BlockPlanks.EnumType.DARK_OAK.func_176839_a();
        }

        return new TileEntityFlowerPot(Item.func_150898_a((Block) object), j);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockFlowerPot.field_176443_b, BlockFlowerPot.field_176444_a});
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockFlowerPot.field_176444_a)).intValue();
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockFlowerPot.EnumFlowerType blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.EMPTY;
        TileEntity tileentity = iblockaccess instanceof ChunkCache ? ((ChunkCache) iblockaccess).func_190300_a(blockposition, Chunk.EnumCreateEntityType.CHECK) : iblockaccess.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityFlowerPot) {
            TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot) tileentity;
            Item item = tileentityflowerpot.func_145965_a();

            if (item instanceof ItemBlock) {
                int i = tileentityflowerpot.func_145966_b();
                Block block = Block.func_149634_a(item);

                if (block == Blocks.field_150345_g) {
                    switch (BlockPlanks.EnumType.func_176837_a(i)) {
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
                } else if (block == Blocks.field_150329_H) {
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
                } else if (block == Blocks.field_150327_N) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.DANDELION;
                } else if (block == Blocks.field_150328_O) {
                    switch (BlockFlower.EnumFlowerType.func_176967_a(BlockFlower.EnumFlowerColor.RED, i)) {
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
                } else if (block == Blocks.field_150337_Q) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.MUSHROOM_RED;
                } else if (block == Blocks.field_150338_P) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.MUSHROOM_BROWN;
                } else if (block == Blocks.field_150330_I) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
                } else if (block == Blocks.field_150434_aF) {
                    blockflowerpot_enumflowerpotcontents = BlockFlowerPot.EnumFlowerType.CACTUS;
                }
            }
        }

        return iblockdata.func_177226_a(BlockFlowerPot.field_176443_b, blockflowerpot_enumflowerpotcontents);
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumFlowerType implements IStringSerializable {

        EMPTY("empty"), POPPY("rose"), BLUE_ORCHID("blue_orchid"), ALLIUM("allium"), HOUSTONIA("houstonia"), RED_TULIP("red_tulip"), ORANGE_TULIP("orange_tulip"), WHITE_TULIP("white_tulip"), PINK_TULIP("pink_tulip"), OXEYE_DAISY("oxeye_daisy"), DANDELION("dandelion"), OAK_SAPLING("oak_sapling"), SPRUCE_SAPLING("spruce_sapling"), BIRCH_SAPLING("birch_sapling"), JUNGLE_SAPLING("jungle_sapling"), ACACIA_SAPLING("acacia_sapling"), DARK_OAK_SAPLING("dark_oak_sapling"), MUSHROOM_RED("mushroom_red"), MUSHROOM_BROWN("mushroom_brown"), DEAD_BUSH("dead_bush"), FERN("fern"), CACTUS("cactus");

        private final String field_177006_w;

        private EnumFlowerType(String s) {
            this.field_177006_w = s;
        }

        public String toString() {
            return this.field_177006_w;
        }

        public String func_176610_l() {
            return this.field_177006_w;
        }
    }
}
