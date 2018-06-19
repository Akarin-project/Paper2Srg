package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Random;


import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstoneTorch extends BlockTorch {

    private static final Map<World, List<BlockRedstoneTorch.Toggle>> field_150112_b = new java.util.WeakHashMap(); // Spigot
    private final boolean field_150113_a;

    private boolean func_176598_a(World world, BlockPos blockposition, boolean flag) {
        if (!BlockRedstoneTorch.field_150112_b.containsKey(world)) {
            BlockRedstoneTorch.field_150112_b.put(world, Lists.<BlockRedstoneTorch.Toggle>newArrayList()); // CraftBukkit - fix decompile error
        }

        List list = (List) BlockRedstoneTorch.field_150112_b.get(world);

        if (flag) {
            list.add(new BlockRedstoneTorch.Toggle(blockposition, world.func_82737_E()));
        }

        int i = 0;

        for (int j = 0; j < list.size(); ++j) {
            BlockRedstoneTorch.Toggle blockredstonetorch_redstoneupdateinfo = (BlockRedstoneTorch.Toggle) list.get(j);

            if (blockredstonetorch_redstoneupdateinfo.field_180111_a.equals(blockposition)) {
                ++i;
                if (i >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(boolean flag) {
        this.field_150113_a = flag;
        this.func_149675_a(true);
        this.func_149647_a((CreativeTabs) null);
    }

    public int func_149738_a(World world) {
        return 2;
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.field_150113_a) {
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.func_175685_c(blockposition.func_177972_a(enumdirection), this, false);
            }
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.field_150113_a) {
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.func_175685_c(blockposition.func_177972_a(enumdirection), this, false);
            }
        }

    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return this.field_150113_a && iblockdata.func_177229_b(BlockRedstoneTorch.field_176596_a) != enumdirection ? 15 : 0;
    }

    private boolean func_176597_g(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = ((EnumFacing) iblockdata.func_177229_b(BlockRedstoneTorch.field_176596_a)).func_176734_d();

        return world.func_175709_b(blockposition.func_177972_a(enumdirection), enumdirection);
    }

    public void func_180645_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        boolean flag = this.func_176597_g(world, blockposition, iblockdata);
        List list = (List) BlockRedstoneTorch.field_150112_b.get(world);

        // Paper start
        if (list != null) {
            int index = 0;
            while (index < list.size() && world.func_82737_E() - ((BlockRedstoneTorch.Toggle) list.get(index)).getTime() > 60L) {
                index++;
            }
            if (index > 0) {
                list.subList(0, index).clear();
            }
        }
        // Paper end

        // CraftBukkit start
        org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
        int oldCurrent = this.field_150113_a ? 15 : 0;

        BlockRedstoneEvent event = new BlockRedstoneEvent(block, oldCurrent, oldCurrent);
        // CraftBukkit end

        if (this.field_150113_a) {
            if (flag) {
                // CraftBukkit start
                if (oldCurrent != 0) {
                    event.setNewCurrent(0);
                    manager.callEvent(event);
                    if (event.getNewCurrent() != 0) {
                        return;
                    }
                }
                // CraftBukkit end
                world.func_180501_a(blockposition, Blocks.field_150437_az.func_176223_P().func_177226_a(BlockRedstoneTorch.field_176596_a, iblockdata.func_177229_b(BlockRedstoneTorch.field_176596_a)), 3);
                if (this.func_176598_a(world, blockposition, true)) {
                    world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187745_eA, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.8F);

                    for (int i = 0; i < 5; ++i) {
                        double d0 = (double) blockposition.func_177958_n() + random.nextDouble() * 0.6D + 0.2D;
                        double d1 = (double) blockposition.func_177956_o() + random.nextDouble() * 0.6D + 0.2D;
                        double d2 = (double) blockposition.func_177952_p() + random.nextDouble() * 0.6D + 0.2D;

                        world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
                    }

                    world.func_175684_a(blockposition, world.func_180495_p(blockposition).func_177230_c(), 160);
                }
            }
        } else if (!flag && !this.func_176598_a(world, blockposition, false)) {
            // CraftBukkit start
            if (oldCurrent != 15) {
                event.setNewCurrent(15);
                manager.callEvent(event);
                if (event.getNewCurrent() != 15) {
                    return;
                }
            }
            // CraftBukkit end
            world.func_180501_a(blockposition, Blocks.field_150429_aA.func_176223_P().func_177226_a(BlockRedstoneTorch.field_176596_a, iblockdata.func_177229_b(BlockRedstoneTorch.field_176596_a)), 3);
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_176592_e(world, blockposition, iblockdata)) {
            if (this.field_150113_a == this.func_176597_g(world, blockposition, iblockdata)) {
                world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
            }

        }
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? iblockdata.func_185911_a(iblockaccess, blockposition, enumdirection) : 0;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_150429_aA);
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.field_150429_aA);
    }

    public boolean func_149667_c(Block block) {
        return block == Blocks.field_150437_az || block == Blocks.field_150429_aA;
    }

    static class Toggle {

        BlockPos field_180111_a;
        long field_150844_d; final long getTime() { return this.field_150844_d; } // Paper - OBFHELPER

        public Toggle(BlockPos blockposition, long i) {
            this.field_180111_a = blockposition;
            this.field_150844_d = i;
        }
    }
}
