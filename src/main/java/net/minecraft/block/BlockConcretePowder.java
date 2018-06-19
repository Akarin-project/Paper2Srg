package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockConcretePowder extends BlockFalling {

    public static final PropertyEnum<EnumDyeColor> field_192426_a = PropertyEnum.func_177709_a("color", EnumDyeColor.class);

    public BlockConcretePowder() {
        super(Material.field_151595_p);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockConcretePowder.field_192426_a, EnumDyeColor.WHITE));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public void func_176502_a_(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1) {
        if (iblockdata1.func_185904_a().func_76224_d() && world.func_180495_p(blockposition).func_177230_c() != Blocks.field_192443_dR) { // CraftBukkit - don't double concrete
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.field_192443_dR.func_176223_P().func_177226_a(BlockColored.field_176581_a, iblockdata.func_177229_b(BlockConcretePowder.field_192426_a)), null); // CraftBukkit
        }

    }

    protected boolean func_192425_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = false;
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (enumdirection != EnumFacing.DOWN) {
                BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);

                if (world.func_180495_p(blockposition1).func_185904_a() == Material.field_151586_h) {
                    flag = true;
                    break;
                }
            }
        }

        if (flag) {
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.field_192443_dR.func_176223_P().func_177226_a(BlockColored.field_176581_a, iblockdata.func_177229_b(BlockConcretePowder.field_192426_a)), null); // CraftBukkit
        }

        return flag;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_192425_e(world, blockposition, iblockdata)) {
            super.func_189540_a(iblockdata, world, blockposition, block, blockposition1);
        }

    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.func_192425_e(world, blockposition, iblockdata)) {
            super.func_176213_c(world, blockposition, iblockdata);
        }

    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.func_177229_b(BlockConcretePowder.field_192426_a)).func_176765_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        EnumDyeColor[] aenumcolor = EnumDyeColor.values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumDyeColor enumcolor = aenumcolor[j];

            nonnulllist.add(new ItemStack(this, 1, enumcolor.func_176765_a()));
        }

    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.func_193558_a((EnumDyeColor) iblockdata.func_177229_b(BlockConcretePowder.field_192426_a));
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockConcretePowder.field_192426_a, EnumDyeColor.func_176764_b(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.func_177229_b(BlockConcretePowder.field_192426_a)).func_176765_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockConcretePowder.field_192426_a});
    }
}
