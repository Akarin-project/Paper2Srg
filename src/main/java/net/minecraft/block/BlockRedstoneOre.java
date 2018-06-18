package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityInteractEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityInteractEvent;
// CraftBukkit end

public class BlockRedstoneOre extends Block {

    private final boolean isOn;

    public BlockRedstoneOre(boolean flag) {
        super(Material.ROCK);
        if (flag) {
            this.setTickRandomly(true);
        }

        this.isOn = flag;
    }

    public int tickRate(World world) {
        return 30;
    }

    public void onBlockClicked(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        this.interact(world, blockposition, entityhuman); // CraftBukkit - add entityhuman
        super.onBlockClicked(world, blockposition, entityhuman);
    }

    public void onEntityWalk(World world, BlockPos blockposition, Entity entity) {
        // CraftBukkit start
        // this.interact(world, blockposition);
        // super.stepOn(world, blockposition, entity);
        if (entity instanceof EntityPlayer) {
            org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
            if (!event.isCancelled()) {
                this.interact(world, blockposition, entity); // add entity
                super.onEntityWalk(world, blockposition, entity);
            }
        } else {
            EntityInteractEvent event = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
            world.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.interact(world, blockposition, entity); // add entity
                super.onEntityWalk(world, blockposition, entity);
            }
        }
        // CraftBukkit end
    }


    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        this.interact(world, blockposition, entityhuman); // CraftBukkit - add entityhuman
        return super.onBlockActivated(world, blockposition, iblockdata, entityhuman, enumhand, enumdirection, f, f1, f2);
    }

    private void interact(World world, BlockPos blockposition, Entity entity) { // CraftBukkit - add Entity
        this.spawnParticles(world, blockposition);
        if (this == Blocks.REDSTONE_ORE) {
            // CraftBukkit start
            if (CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, Blocks.LIT_REDSTONE_ORE, 0).isCancelled()) {
                return;
            }
            // CraftBukkit end
            world.setBlockState(blockposition, Blocks.LIT_REDSTONE_ORE.getDefaultState());
        }

    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (this == Blocks.LIT_REDSTONE_ORE) {
            // CraftBukkit start
            if (CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), Blocks.REDSTONE_ORE).isCancelled()) {
                return;
            }
            // CraftBukkit end
            world.setBlockState(blockposition, Blocks.REDSTONE_ORE.getDefaultState());
        }

    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.REDSTONE;
    }

    public int quantityDroppedWithBonus(int i, Random random) {
        return this.quantityDropped(random) + random.nextInt(i + 1);
    }

    public int quantityDropped(Random random) {
        return 4 + random.nextInt(2);
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        super.dropBlockAsItemWithChance(world, blockposition, iblockdata, f, i);
        /* CraftBukkit start - Delegated to getExpDrop
        if (this.getDropType(iblockdata, world.random, i) != Item.getItemOf(this)) {
            int j = 1 + world.random.nextInt(5);

            this.dropExperience(world, blockposition, j);
        }
        // */

    }

    @Override
    public int getExpDrop(World world, IBlockState data, int i) {
        if (this.getItemDropped(data, world.rand, i) != Item.getItemFromBlock(this)) {
            int j = 1 + world.rand.nextInt(5);

            return j;
        }
        return 0;
        // CraftBukkit end
    }

    private void spawnParticles(World world, BlockPos blockposition) {
        Random random = world.rand;
        double d0 = 0.0625D;

        for (int i = 0; i < 6; ++i) {
            double d1 = (double) ((float) blockposition.getX() + random.nextFloat());
            double d2 = (double) ((float) blockposition.getY() + random.nextFloat());
            double d3 = (double) ((float) blockposition.getZ() + random.nextFloat());

            if (i == 0 && !world.getBlockState(blockposition.up()).isOpaqueCube()) {
                d2 = (double) blockposition.getY() + 0.0625D + 1.0D;
            }

            if (i == 1 && !world.getBlockState(blockposition.down()).isOpaqueCube()) {
                d2 = (double) blockposition.getY() - 0.0625D;
            }

            if (i == 2 && !world.getBlockState(blockposition.south()).isOpaqueCube()) {
                d3 = (double) blockposition.getZ() + 0.0625D + 1.0D;
            }

            if (i == 3 && !world.getBlockState(blockposition.north()).isOpaqueCube()) {
                d3 = (double) blockposition.getZ() - 0.0625D;
            }

            if (i == 4 && !world.getBlockState(blockposition.east()).isOpaqueCube()) {
                d1 = (double) blockposition.getX() + 0.0625D + 1.0D;
            }

            if (i == 5 && !world.getBlockState(blockposition.west()).isOpaqueCube()) {
                d1 = (double) blockposition.getX() - 0.0625D;
            }

            if (d1 < (double) blockposition.getX() || d1 > (double) (blockposition.getX() + 1) || d2 < 0.0D || d2 > (double) (blockposition.getY() + 1) || d3 < (double) blockposition.getZ() || d3 > (double) (blockposition.getZ() + 1)) {
                world.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        return new ItemStack(Blocks.REDSTONE_ORE);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Item.getItemFromBlock(Blocks.REDSTONE_ORE), 1, this.damageDropped(iblockdata));
    }
}
