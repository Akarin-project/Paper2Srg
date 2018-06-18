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
        this(Material.ROCK.getMaterialMapColor());
    }

    public BlockOre(MapColor materialmapcolor) {
        super(Material.ROCK, materialmapcolor);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return this == Blocks.COAL_ORE ? Items.COAL : (this == Blocks.DIAMOND_ORE ? Items.DIAMOND : (this == Blocks.LAPIS_ORE ? Items.DYE : (this == Blocks.EMERALD_ORE ? Items.EMERALD : (this == Blocks.QUARTZ_ORE ? Items.QUARTZ : Item.getItemFromBlock(this)))));
    }

    public int quantityDropped(Random random) {
        return this == Blocks.LAPIS_ORE ? 4 + random.nextInt(5) : 1;
    }

    public int quantityDroppedWithBonus(int i, Random random) {
        if (i > 0 && Item.getItemFromBlock(this) != this.getItemDropped((IBlockState) this.getBlockState().getValidStates().iterator().next(), random, i)) {
            int j = random.nextInt(i + 2) - 1;

            if (j < 0) {
                j = 0;
            }

            return this.quantityDropped(random) * (j + 1);
        } else {
            return this.quantityDropped(random);
        }
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        super.dropBlockAsItemWithChance(world, blockposition, iblockdata, f, i);
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
        if (this.getItemDropped(iblockdata, world.rand, i) != Item.getItemFromBlock(this)) {
            int j = 0;

            if (this == Blocks.COAL_ORE) {
                j = MathHelper.getInt(world.rand, 0, 2);
            } else if (this == Blocks.DIAMOND_ORE) {
                j = MathHelper.getInt(world.rand, 3, 7);
            } else if (this == Blocks.EMERALD_ORE) {
                j = MathHelper.getInt(world.rand, 3, 7);
            } else if (this == Blocks.LAPIS_ORE) {
                j = MathHelper.getInt(world.rand, 2, 5);
            } else if (this == Blocks.QUARTZ_ORE) {
                j = MathHelper.getInt(world.rand, 2, 5);
            }

            return j;
        }

        return 0;
        // CraftBukkit end
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this);
    }

    public int damageDropped(IBlockState iblockdata) {
        return this == Blocks.LAPIS_ORE ? EnumDyeColor.BLUE.getDyeDamage() : 0;
    }
}
