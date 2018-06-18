package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoublePlant extends BlockBush implements IGrowable {

    public static final PropertyEnum<BlockDoublePlant.EnumPlantType> VARIANT = PropertyEnum.create("variant", BlockDoublePlant.EnumPlantType.class);
    public static final PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockDoublePlant.EnumBlockHalf.class);
    public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;

    public BlockDoublePlant() {
        super(Material.VINE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SUNFLOWER).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.FACING, EnumFacing.NORTH));
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.setUnlocalizedName("doublePlant");
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockDoublePlant.FULL_BLOCK_AABB;
    }

    private BlockDoublePlant.EnumPlantType getType(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        if (iblockdata.getBlock() == this) {
            iblockdata = iblockdata.getActualState(iblockaccess, blockposition);
            return (BlockDoublePlant.EnumPlantType) iblockdata.getValue(BlockDoublePlant.VARIANT);
        } else {
            return BlockDoublePlant.EnumPlantType.FERN;
        }
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return super.canPlaceBlockAt(world, blockposition) && world.isAirBlock(blockposition.up());
    }

    public boolean isReplaceable(IBlockAccess iblockaccess, BlockPos blockposition) {
        IBlockState iblockdata = iblockaccess.getBlockState(blockposition);

        if (iblockdata.getBlock() != this) {
            return true;
        } else {
            BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = (BlockDoublePlant.EnumPlantType) iblockdata.getActualState(iblockaccess, blockposition).getValue(BlockDoublePlant.VARIANT);

            return blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.FERN || blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.GRASS;
        }
    }

    protected void checkAndDropBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.canBlockStay(world, blockposition, iblockdata)) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPhysicsEvent(world, blockposition).isCancelled()) {
                return;
            }
            // CraftBukkit end
            boolean flag = iblockdata.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER;
            BlockPos blockposition1 = flag ? blockposition : blockposition.up();
            BlockPos blockposition2 = flag ? blockposition.down() : blockposition;
            Object object = flag ? this : world.getBlockState(blockposition1).getBlock();
            Object object1 = flag ? world.getBlockState(blockposition2).getBlock() : this;

            if (object == this) {
                world.setBlockState(blockposition1, Blocks.AIR.getDefaultState(), 2);
            }

            if (object1 == this) {
                world.setBlockState(blockposition2, Blocks.AIR.getDefaultState(), 3);
                if (!flag) {
                    this.dropBlockAsItem(world, blockposition2, iblockdata, 0);
                }
            }

        }
    }

    public boolean canBlockStay(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (iblockdata.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            return world.getBlockState(blockposition.down()).getBlock() == this;
        } else {
            IBlockState iblockdata1 = world.getBlockState(blockposition.up());

            return iblockdata1.getBlock() == this && super.canBlockStay(world, blockposition, iblockdata1);
        }
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        if (iblockdata.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            return Items.AIR;
        } else {
            BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = (BlockDoublePlant.EnumPlantType) iblockdata.getValue(BlockDoublePlant.VARIANT);

            return blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.FERN ? Items.AIR : (blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.GRASS ? (random.nextInt(8) == 0 ? Items.WHEAT_SEEDS : Items.AIR) : super.getItemDropped(iblockdata, random, i));
        }
    }

    public int damageDropped(IBlockState iblockdata) {
        return iblockdata.getValue(BlockDoublePlant.HALF) != BlockDoublePlant.EnumBlockHalf.UPPER && iblockdata.getValue(BlockDoublePlant.VARIANT) != BlockDoublePlant.EnumPlantType.GRASS ? ((BlockDoublePlant.EnumPlantType) iblockdata.getValue(BlockDoublePlant.VARIANT)).getMeta() : 0;
    }

    public void placeAt(World world, BlockPos blockposition, BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants, int i) {
        world.setBlockState(blockposition, this.getDefaultState().withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT, blocktallplant_enumtallflowervariants), i);
        world.setBlockState(blockposition.up(), this.getDefaultState().withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), i);
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.setBlockState(blockposition.up(), this.getDefaultState().withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (world.isRemote || itemstack.getItem() != Items.SHEARS || iblockdata.getValue(BlockDoublePlant.HALF) != BlockDoublePlant.EnumBlockHalf.LOWER || !this.onHarvest(world, blockposition, iblockdata, entityhuman)) {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }
    }

    public void onBlockHarvested(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (iblockdata.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            if (world.getBlockState(blockposition.down()).getBlock() == this) {
                if (entityhuman.capabilities.isCreativeMode) {
                    world.setBlockToAir(blockposition.down());
                } else {
                    IBlockState iblockdata1 = world.getBlockState(blockposition.down());
                    BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = (BlockDoublePlant.EnumPlantType) iblockdata1.getValue(BlockDoublePlant.VARIANT);

                    if (blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.FERN && blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.GRASS) {
                        world.destroyBlock(blockposition.down(), true);
                    } else if (world.isRemote) {
                        world.setBlockToAir(blockposition.down());
                    } else if (!entityhuman.getHeldItemMainhand().isEmpty() && entityhuman.getHeldItemMainhand().getItem() == Items.SHEARS) {
                        this.onHarvest(world, blockposition, iblockdata1, entityhuman);
                        world.setBlockToAir(blockposition.down());
                    } else {
                        world.destroyBlock(blockposition.down(), true);
                    }
                }
            }
        } else if (world.getBlockState(blockposition.up()).getBlock() == this) {
            world.setBlockState(blockposition.up(), Blocks.AIR.getDefaultState(), 2);
        }

        super.onBlockHarvested(world, blockposition, iblockdata, entityhuman);
    }

    private boolean onHarvest(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = (BlockDoublePlant.EnumPlantType) iblockdata.getValue(BlockDoublePlant.VARIANT);

        if (blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.FERN && blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.GRASS) {
            return false;
        } else {
            entityhuman.addStat(StatList.getBlockStats((Block) this));
            int i = (blocktallplant_enumtallflowervariants == BlockDoublePlant.EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();

            spawnAsEntity(world, blockposition, new ItemStack(Blocks.TALLGRASS, 2, i));
            return true;
        }
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockDoublePlant.EnumPlantType[] ablocktallplant_enumtallflowervariants = BlockDoublePlant.EnumPlantType.values();
        int i = ablocktallplant_enumtallflowervariants.length;

        for (int j = 0; j < i; ++j) {
            BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = ablocktallplant_enumtallflowervariants[j];

            nonnulllist.add(new ItemStack(this, 1, blocktallplant_enumtallflowervariants.getMeta()));
        }

    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, this.getType((IBlockAccess) world, blockposition, iblockdata).getMeta());
    }

    public boolean canGrow(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = this.getType((IBlockAccess) world, blockposition, iblockdata);

        return blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.GRASS && blocktallplant_enumtallflowervariants != BlockDoublePlant.EnumPlantType.FERN;
    }

    public boolean canUseBonemeal(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void grow(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        spawnAsEntity(world, blockposition, new ItemStack(this, 1, this.getType((IBlockAccess) world, blockposition, iblockdata).getMeta()));
    }

    public IBlockState getStateFromMeta(int i) {
        return (i & 8) > 0 ? this.getDefaultState().withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER) : this.getDefaultState().withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.byMetadata(i & 7));
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (iblockdata.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            IBlockState iblockdata1 = iblockaccess.getBlockState(blockposition.down());

            if (iblockdata1.getBlock() == this) {
                iblockdata = iblockdata.withProperty(BlockDoublePlant.VARIANT, iblockdata1.getValue(BlockDoublePlant.VARIANT));
            }
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return iblockdata.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER ? 8 | ((EnumFacing) iblockdata.getValue(BlockDoublePlant.FACING)).getHorizontalIndex() : ((BlockDoublePlant.EnumPlantType) iblockdata.getValue(BlockDoublePlant.VARIANT)).getMeta();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockDoublePlant.HALF, BlockDoublePlant.VARIANT, BlockDoublePlant.FACING});
    }

    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumBlockHalf implements IStringSerializable {

        UPPER, LOWER;

        private EnumBlockHalf() {}

        public String toString() {
            return this.getName();
        }

        public String getName() {
            return this == BlockDoublePlant.EnumBlockHalf.UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumPlantType implements IStringSerializable {

        SUNFLOWER(0, "sunflower"), SYRINGA(1, "syringa"), GRASS(2, "double_grass", "grass"), FERN(3, "double_fern", "fern"), ROSE(4, "double_rose", "rose"), PAEONIA(5, "paeonia");

        private static final BlockDoublePlant.EnumPlantType[] META_LOOKUP = new BlockDoublePlant.EnumPlantType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumPlantType(int i, String s) {
            this(i, s, s);
        }

        private EnumPlantType(int i, String s, String s1) {
            this.meta = i;
            this.name = s;
            this.unlocalizedName = s1;
        }

        public int getMeta() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static BlockDoublePlant.EnumPlantType byMetadata(int i) {
            if (i < 0 || i >= BlockDoublePlant.EnumPlantType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockDoublePlant.EnumPlantType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockDoublePlant.EnumPlantType[] ablocktallplant_enumtallflowervariants = values();
            int i = ablocktallplant_enumtallflowervariants.length;

            for (int j = 0; j < i; ++j) {
                BlockDoublePlant.EnumPlantType blocktallplant_enumtallflowervariants = ablocktallplant_enumtallflowervariants[j];

                BlockDoublePlant.EnumPlantType.META_LOOKUP[blocktallplant_enumtallflowervariants.getMeta()] = blocktallplant_enumtallflowervariants;
            }

        }
    }
}
