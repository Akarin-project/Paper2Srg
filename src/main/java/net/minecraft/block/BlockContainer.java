package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

public abstract class BlockContainer extends Block implements ITileEntityProvider {

    protected BlockContainer(Material material) {
        this(material, material.func_151565_r());
    }

    protected BlockContainer(Material material, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.field_149758_A = true;
    }

    protected boolean func_181086_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return world.func_180495_p(blockposition.func_177972_a(enumdirection)).func_185904_a() == Material.field_151570_A;
    }

    protected boolean func_181087_e(World world, BlockPos blockposition) {
        return this.func_181086_a(world, blockposition, EnumFacing.NORTH) || this.func_181086_a(world, blockposition, EnumFacing.SOUTH) || this.func_181086_a(world, blockposition, EnumFacing.WEST) || this.func_181086_a(world, blockposition, EnumFacing.EAST);
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.INVISIBLE;
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_180663_b(world, blockposition, iblockdata);
        world.func_175713_t(blockposition);
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (tileentity instanceof IWorldNameable && ((IWorldNameable) tileentity).func_145818_k_()) {
            entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
            entityhuman.func_71020_j(0.005F);
            if (world.field_72995_K) {
                return;
            }

            int i = EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, itemstack);
            Item item = this.func_180660_a(iblockdata, world.field_73012_v, i);

            if (item == Items.field_190931_a) {
                return;
            }

            ItemStack itemstack1 = new ItemStack(item, this.func_149745_a(world.field_73012_v));

            itemstack1.func_151001_c(((IWorldNameable) tileentity).func_70005_c_());
            func_180635_a(world, blockposition, itemstack1);
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, (TileEntity) null, itemstack);
        }

    }

    public boolean func_189539_a(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        super.func_189539_a(iblockdata, world, blockposition, i, j);
        TileEntity tileentity = world.func_175625_s(blockposition);

        return tileentity == null ? false : tileentity.func_145842_c(i, j);
    }
}
