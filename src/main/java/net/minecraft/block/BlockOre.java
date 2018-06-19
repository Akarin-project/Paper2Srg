package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends Block {

    public BlockOre() {
        this(Material.field_151576_e.func_151565_r());
    }

    public BlockOre(MapColor materialmapcolor) {
        super(Material.field_151576_e, materialmapcolor);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return this == Blocks.field_150365_q ? Items.field_151044_h : (this == Blocks.field_150482_ag ? Items.field_151045_i : (this == Blocks.field_150369_x ? Items.field_151100_aR : (this == Blocks.field_150412_bA ? Items.field_151166_bC : (this == Blocks.field_150449_bY ? Items.field_151128_bU : Item.func_150898_a(this)))));
    }

    public int func_149745_a(Random random) {
        return this == Blocks.field_150369_x ? 4 + random.nextInt(5) : 1;
    }

    public int func_149679_a(int i, Random random) {
        if (i > 0 && Item.func_150898_a(this) != this.func_180660_a((IBlockState) this.func_176194_O().func_177619_a().iterator().next(), random, i)) {
            int j = random.nextInt(i + 2) - 1;

            if (j < 0) {
                j = 0;
            }

            return this.func_149745_a(random) * (j + 1);
        } else {
            return this.func_149745_a(random);
        }
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        super.func_180653_a(world, blockposition, iblockdata, f, i);
        /* CraftBukkit start - Delegated to getExpDrop
        if (this.getDropType(iblockdata, world.random, i) != Item.getItemOf(this)) {
            int j = 0;

            if (this == Blocks.COAL_ORE) {
                j = MathHelper.nextInt(world.random, 0, 2);
            } else if (this == Blocks.DIAMOND_ORE) {
                j = MathHelper.nextInt(world.random, 3, 7);
            } else if (this == Blocks.EMERALD_ORE) {
                j = MathHelper.nextInt(world.random, 3, 7);
            } else if (this == Blocks.LAPIS_ORE) {
                j = MathHelper.nextInt(world.random, 2, 5);
            } else if (this == Blocks.QUARTZ_ORE) {
                j = MathHelper.nextInt(world.random, 2, 5);
            }

            this.dropExperience(world, blockposition, j);
        }
        // */

    }

    @Override
    public int getExpDrop(World world, IBlockState iblockdata, int i) {
        if (this.func_180660_a(iblockdata, world.field_73012_v, i) != Item.func_150898_a(this)) {
            int j = 0;

            if (this == Blocks.field_150365_q) {
                j = MathHelper.func_76136_a(world.field_73012_v, 0, 2);
            } else if (this == Blocks.field_150482_ag) {
                j = MathHelper.func_76136_a(world.field_73012_v, 3, 7);
            } else if (this == Blocks.field_150412_bA) {
                j = MathHelper.func_76136_a(world.field_73012_v, 3, 7);
            } else if (this == Blocks.field_150369_x) {
                j = MathHelper.func_76136_a(world.field_73012_v, 2, 5);
            } else if (this == Blocks.field_150449_bY) {
                j = MathHelper.func_76136_a(world.field_73012_v, 2, 5);
            }

            return j;
        }

        return 0;
        // CraftBukkit end
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this);
    }

    public int func_180651_a(IBlockState iblockdata) {
        return this == Blocks.field_150369_x ? EnumDyeColor.BLUE.func_176767_b() : 0;
    }
}
