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

    public static final PropertyEnum<BlockSilverfish.EnumType> field_176378_a = PropertyEnum.func_177709_a("variant", BlockSilverfish.EnumType.class);

    public BlockSilverfish() {
        super(Material.field_151571_B);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockSilverfish.field_176378_a, BlockSilverfish.EnumType.STONE));
        this.func_149711_c(0.0F);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public static boolean func_176377_d(IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();

        return iblockdata == Blocks.field_150348_b.func_176223_P().func_177226_a(BlockStone.field_176247_a, BlockStone.EnumType.STONE) || block == Blocks.field_150347_e || block == Blocks.field_150417_aV;
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        switch ((BlockSilverfish.EnumType) iblockdata.func_177229_b(BlockSilverfish.field_176378_a)) {
        case COBBLESTONE:
            return new ItemStack(Blocks.field_150347_e);

        case STONEBRICK:
            return new ItemStack(Blocks.field_150417_aV);

        case MOSSY_STONEBRICK:
            return new ItemStack(Blocks.field_150417_aV, 1, BlockStoneBrick.EnumType.MOSSY.func_176612_a());

        case CRACKED_STONEBRICK:
            return new ItemStack(Blocks.field_150417_aV, 1, BlockStoneBrick.EnumType.CRACKED.func_176612_a());

        case CHISELED_STONEBRICK:
            return new ItemStack(Blocks.field_150417_aV, 1, BlockStoneBrick.EnumType.CHISELED.func_176612_a());

        default:
            return new ItemStack(Blocks.field_150348_b);
        }
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.field_72995_K && world.func_82736_K().func_82766_b("doTileDrops")) {
            EntitySilverfish entitysilverfish = new EntitySilverfish(world);

            entitysilverfish.func_70012_b((double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p() + 0.5D, 0.0F, 0.0F);
            world.addEntity(entitysilverfish, SpawnReason.SILVERFISH_BLOCK); // CraftBukkit - add SpawnReason
            entitysilverfish.func_70656_aK();
        }

    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, iblockdata.func_177230_c().func_176201_c(iblockdata));
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockSilverfish.EnumType[] ablockmonstereggs_enummonstereggvarient = BlockSilverfish.EnumType.values();
        int i = ablockmonstereggs_enummonstereggvarient.length;

        for (int j = 0; j < i; ++j) {
            BlockSilverfish.EnumType blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];

            nonnulllist.add(new ItemStack(this, 1, blockmonstereggs_enummonstereggvarient.func_176881_a()));
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockSilverfish.field_176378_a, BlockSilverfish.EnumType.func_176879_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockSilverfish.EnumType) iblockdata.func_177229_b(BlockSilverfish.field_176378_a)).func_176881_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockSilverfish.field_176378_a});
    }

    public static enum EnumType implements IStringSerializable {

        STONE(0, "stone") {;
            public IBlockState func_176883_d() {
                return Blocks.field_150348_b.func_176223_P().func_177226_a(BlockStone.field_176247_a, BlockStone.EnumType.STONE);
            }
        }, COBBLESTONE(1, "cobblestone", "cobble") {;
    public IBlockState func_176883_d() {
        return Blocks.field_150347_e.func_176223_P();
    }
}, STONEBRICK(2, "stone_brick", "brick") {;
    public IBlockState func_176883_d() {
        return Blocks.field_150417_aV.func_176223_P().func_177226_a(BlockStoneBrick.field_176249_a, BlockStoneBrick.EnumType.DEFAULT);
    }
}, MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {;
    public IBlockState func_176883_d() {
        return Blocks.field_150417_aV.func_176223_P().func_177226_a(BlockStoneBrick.field_176249_a, BlockStoneBrick.EnumType.MOSSY);
    }
}, CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {;
    public IBlockState func_176883_d() {
        return Blocks.field_150417_aV.func_176223_P().func_177226_a(BlockStoneBrick.field_176249_a, BlockStoneBrick.EnumType.CRACKED);
    }
}, CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {;
    public IBlockState func_176883_d() {
        return Blocks.field_150417_aV.func_176223_P().func_177226_a(BlockStoneBrick.field_176249_a, BlockStoneBrick.EnumType.CHISELED);
    }
};

        private static final BlockSilverfish.EnumType[] field_176885_g = new BlockSilverfish.EnumType[values().length];
        private final int field_176893_h;
        private final String field_176894_i;
        private final String field_176891_j;

        private EnumType(int i, String s) {
            this(i, s, s);
        }

        private EnumType(int i, String s, String s1) {
            this.field_176893_h = i;
            this.field_176894_i = s;
            this.field_176891_j = s1;
        }

        public int func_176881_a() {
            return this.field_176893_h;
        }

        public String toString() {
            return this.field_176894_i;
        }

        public static BlockSilverfish.EnumType func_176879_a(int i) {
            if (i < 0 || i >= BlockSilverfish.EnumType.field_176885_g.length) {
                i = 0;
            }

            return BlockSilverfish.EnumType.field_176885_g[i];
        }

        public String func_176610_l() {
            return this.field_176894_i;
        }

        public String func_176882_c() {
            return this.field_176891_j;
        }

        public abstract IBlockState func_176883_d();

        public static BlockSilverfish.EnumType func_176878_a(IBlockState iblockdata) {
            BlockSilverfish.EnumType[] ablockmonstereggs_enummonstereggvarient = values();
            int i = ablockmonstereggs_enummonstereggvarient.length;

            for (int j = 0; j < i; ++j) {
                BlockSilverfish.EnumType blockmonstereggs_enummonstereggvarient = ablockmonstereggs_enummonstereggvarient[j];

                if (iblockdata == blockmonstereggs_enummonstereggvarient.func_176883_d()) {
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

                BlockSilverfish.EnumType.field_176885_g[blockmonstereggs_enummonstereggvarient.func_176881_a()] = blockmonstereggs_enummonstereggvarient;
            }

        }
    }
}
