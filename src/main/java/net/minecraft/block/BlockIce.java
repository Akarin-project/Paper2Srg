package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockIce extends BlockBreakable {

    public BlockIce() {
        super(Material.field_151588_w, false);
        this.field_149765_K = 0.98F;
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
        entityhuman.func_71020_j(0.005F);
        if (this.func_149700_E() && EnchantmentHelper.func_77506_a(Enchantments.field_185306_r, itemstack) > 0) {
            func_180635_a(world, blockposition, this.func_180643_i(iblockdata));
        } else {
            if (world.field_73011_w.func_177500_n()) {
                world.func_175698_g(blockposition);
                return;
            }

            int i = EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, itemstack);

            this.func_176226_b(world, blockposition, iblockdata, i);
            Material material = world.func_180495_p(blockposition.func_177977_b()).func_185904_a();

            if (material.func_76230_c() || material.func_76224_d()) {
                world.func_175656_a(blockposition, Blocks.field_150358_i.func_176223_P());
            }
        }

    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.func_175642_b(EnumSkyBlock.BLOCK, blockposition) > 11 - this.func_176223_P().func_185891_c()) {
            this.func_185679_b(world, blockposition);
        }

    }

    protected void func_185679_b(World world, BlockPos blockposition) {
        // CraftBukkit start
        if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), world.field_73011_w.func_177500_n() ? Blocks.field_150350_a : Blocks.field_150355_j).isCancelled()) {
            return;
        }
        // CraftBukkit end
        if (world.field_73011_w.func_177500_n()) {
            world.func_175698_g(blockposition);
        } else {
            this.func_176226_b(world, blockposition, world.func_180495_p(blockposition), 0);
            world.func_175656_a(blockposition, Blocks.field_150355_j.func_176223_P());
            world.func_190524_a(blockposition, (Block) Blocks.field_150355_j, blockposition);
        }
    }

    public EnumPushReaction func_149656_h(IBlockState iblockdata) {
        return EnumPushReaction.NORMAL;
    }
}
