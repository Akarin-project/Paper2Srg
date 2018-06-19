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

    private final boolean field_150187_a;

    public BlockRedstoneOre(boolean flag) {
        super(Material.field_151576_e);
        if (flag) {
            this.func_149675_a(true);
        }

        this.field_150187_a = flag;
    }

    public int func_149738_a(World world) {
        return 30;
    }

    public void func_180649_a(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        this.interact(world, blockposition, entityhuman); // CraftBukkit - add entityhuman
        super.func_180649_a(world, blockposition, entityhuman);
    }

    public void func_176199_a(World world, BlockPos blockposition, Entity entity) {
        // CraftBukkit start
        // this.interact(world, blockposition);
        // super.stepOn(world, blockposition, entity);
        if (entity instanceof EntityPlayer) {
            org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
            if (!event.isCancelled()) {
                this.interact(world, blockposition, entity); // add entity
                super.func_176199_a(world, blockposition, entity);
            }
        } else {
            EntityInteractEvent event = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
            world.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.interact(world, blockposition, entity); // add entity
                super.func_176199_a(world, blockposition, entity);
            }
        }
        // CraftBukkit end
    }


    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        this.interact(world, blockposition, entityhuman); // CraftBukkit - add entityhuman
        return super.func_180639_a(world, blockposition, iblockdata, entityhuman, enumhand, enumdirection, f, f1, f2);
    }

    private void interact(World world, BlockPos blockposition, Entity entity) { // CraftBukkit - add Entity
        this.func_180691_e(world, blockposition);
        if (this == Blocks.field_150450_ax) {
            // CraftBukkit start
            if (CraftEventFactory.callEntityChangeBlockEvent(entity, blockposition, Blocks.field_150439_ay, 0).isCancelled()) {
                return;
            }
            // CraftBukkit end
            world.func_175656_a(blockposition, Blocks.field_150439_ay.func_176223_P());
        }

    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (this == Blocks.field_150439_ay) {
            // CraftBukkit start
            if (CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), Blocks.field_150450_ax).isCancelled()) {
                return;
            }
            // CraftBukkit end
            world.func_175656_a(blockposition, Blocks.field_150450_ax.func_176223_P());
        }

    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151137_ax;
    }

    public int func_149679_a(int i, Random random) {
        return this.func_149745_a(random) + random.nextInt(i + 1);
    }

    public int func_149745_a(Random random) {
        return 4 + random.nextInt(2);
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        super.func_180653_a(world, blockposition, iblockdata, f, i);
        /* CraftBukkit start - Delegated to getExpDrop
        if (this.getDropType(iblockdata, world.random, i) != Item.getItemOf(this)) {
            int j = 1 + world.random.nextInt(5);

            this.dropExperience(world, blockposition, j);
        }
        // */

    }

    @Override
    public int getExpDrop(World world, IBlockState data, int i) {
        if (this.func_180660_a(data, world.field_73012_v, i) != Item.func_150898_a(this)) {
            int j = 1 + world.field_73012_v.nextInt(5);

            return j;
        }
        return 0;
        // CraftBukkit end
    }

    private void func_180691_e(World world, BlockPos blockposition) {
        Random random = world.field_73012_v;
        double d0 = 0.0625D;

        for (int i = 0; i < 6; ++i) {
            double d1 = (double) ((float) blockposition.func_177958_n() + random.nextFloat());
            double d2 = (double) ((float) blockposition.func_177956_o() + random.nextFloat());
            double d3 = (double) ((float) blockposition.func_177952_p() + random.nextFloat());

            if (i == 0 && !world.func_180495_p(blockposition.func_177984_a()).func_185914_p()) {
                d2 = (double) blockposition.func_177956_o() + 0.0625D + 1.0D;
            }

            if (i == 1 && !world.func_180495_p(blockposition.func_177977_b()).func_185914_p()) {
                d2 = (double) blockposition.func_177956_o() - 0.0625D;
            }

            if (i == 2 && !world.func_180495_p(blockposition.func_177968_d()).func_185914_p()) {
                d3 = (double) blockposition.func_177952_p() + 0.0625D + 1.0D;
            }

            if (i == 3 && !world.func_180495_p(blockposition.func_177978_c()).func_185914_p()) {
                d3 = (double) blockposition.func_177952_p() - 0.0625D;
            }

            if (i == 4 && !world.func_180495_p(blockposition.func_177974_f()).func_185914_p()) {
                d1 = (double) blockposition.func_177958_n() + 0.0625D + 1.0D;
            }

            if (i == 5 && !world.func_180495_p(blockposition.func_177976_e()).func_185914_p()) {
                d1 = (double) blockposition.func_177958_n() - 0.0625D;
            }

            if (d1 < (double) blockposition.func_177958_n() || d1 > (double) (blockposition.func_177958_n() + 1) || d2 < 0.0D || d2 > (double) (blockposition.func_177956_o() + 1) || d3 < (double) blockposition.func_177952_p() || d3 > (double) (blockposition.func_177952_p() + 1)) {
                world.func_175688_a(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        return new ItemStack(Blocks.field_150450_ax);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Item.func_150898_a(Blocks.field_150450_ax), 1, this.func_180651_a(iblockdata));
    }
}
