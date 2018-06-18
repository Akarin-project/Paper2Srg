package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class BlockSilverfish extends Block {

    public static final PropertyEnum<BlockSilverfish.EnumType> VARIANT = PropertyEnum.create("variant", BlockSilverfish.EnumType.class);

    public BlockSilverfish() {
        super(Material.CLAY);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE));
        this.setHardness(0.0F);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public static boolean canContainSilverfish(IBlockState iblockdata) {
        Block block = iblockdata.getBlock();

        return iblockdata == Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE) || block == Blocks.COBBLESTONE || block == Blocks.STONEBRICK;
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        switch ((BlockSilverfish.EnumType) iblockdata.getValue(BlockSilverfish.VARIANT)) {
        case COBBLESTONE:
            return new ItemStack(Blocks.COBBLESTONE);

        case STONEBRICK:
            return new ItemStack(Blocks.STONEBRICK);

        case MOSSY_STONEBRICK:
            return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());

        case CRACKED_STONEBRICK:
            return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());

        case CHISELED_STONEBRICK:
            return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());

        default:
            return new ItemStack(Blocks.STONE);
        }
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
            EntitySilverfish entitysilverfish = new EntitySilverfish(world);

            entitysilverfish.setLocationAndAngles((double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, 0.0F, 0.0F);
            world.addEntity(entitysilverfish, SpawnReason.SILVERFISH_BLOCK); // CraftBukkit - add SpawnReason
            entitysilverfish.spawnExplosionParticle();
        }

    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, iblockdata.getBlock().getMetaFromState(iblockdata));
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockSilverfish.EnumType[] ablockmonstereggs_enummonstereggvarient = BlockSilverfish.EnumType.values();
        int i = ablockmonstereggs_enummonstereggvarient.length;

        for (int j = 0; j < i; ++j) {
            BlockSilverfish.EnumType blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];

            nonnulllist.add(new ItemStack(this, 1, blockmonstereggs_enummonstereggvarient.getMetadata()));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockSilverfish.EnumType) iblockdata.getValue(BlockSilverfish.VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockSilverfish.VARIANT});
    }

    public static enum EnumType implements IStringSerializable {

        STONE(0, "stone") {;
            public IBlockState getModelBlock() {
                return Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
            }
        }, COBBLESTONE(1, "cobblestone", "cobble") {;
    public IBlockState getModelBlock() {
        return Blocks.COBBLESTONE.getDefaultState();
    }
}, STONEBRICK(2, "stone_brick", "brick") {;
    public IBlockState getModelBlock() {
        return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
    }
}, MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {;
    public IBlockState getModelBlock() {
        return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
    }
}, CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {;
    public IBlockState getModelBlock() {
        return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
    }
}, CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {;
    public IBlockState getModelBlock() {
        return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
    }
};

        private static final BlockSilverfish.EnumType[] META_LOOKUP = new BlockSilverfish.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumType(int i, String s) {
            this(i, s, s);
        }

        private EnumType(int i, String s, String s1) {
            this.meta = i;
            this.name = s;
            this.unlocalizedName = s1;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static BlockSilverfish.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockSilverfish.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockSilverfish.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public abstract IBlockState getModelBlock();

        public static BlockSilverfish.EnumType forModelBlock(IBlockState iblockdata) {
            BlockSilverfish.EnumType[] ablockmonstereggs_enummonstereggvarient = values();
            int i = ablockmonstereggs_enummonstereggvarient.length;

            for (int j = 0; j < i; ++j) {
                BlockSilverfish.EnumType blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];

                if (iblockdata == blockmonstereggs_enummonstereggvarient.getModelBlock()) {
                    return blockmonstereggs_enummonstereggvarient;
                }
            }

            return BlockSilverfish.EnumType.STONE;
        }

        EnumType(int i, String s, Object object) {
            this(i, s);
        }

        EnumType(int i, String s, String s1, Object object) {
            this(i, s, s1);
        }

        static {
            BlockSilverfish.EnumType[] ablockmonstereggs_enummonstereggvarient = values();
            int i = ablockmonstereggs_enummonstereggvarient.length;

            for (int j = 0; j < i; ++j) {
                BlockSilverfish.EnumType blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];

                BlockSilverfish.EnumType.META_LOOKUP[blockmonstereggs_enummonstereggvarient.getMetadata()] = blockmonstereggs_enummonstereggvarient;
            }

        }
    }
}
