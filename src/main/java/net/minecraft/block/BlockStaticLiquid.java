package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockStaticLiquid extends BlockLiquid {

    protected BlockStaticLiquid(Material material) {
        super(material);
        this.func_149675_a(false);
        if (material == Material.field_151587_i) {
            this.func_149675_a(true);
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_176365_e(world, blockposition, iblockdata)) {
            this.func_176370_f(world, blockposition, iblockdata);
        }

    }

    private void func_176370_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        BlockDynamicLiquid blockflowing = func_176361_a(this.field_149764_J);

        world.func_180501_a(blockposition, blockflowing.func_176223_P().func_177226_a(BlockStaticLiquid.field_176367_b, iblockdata.func_177229_b(BlockStaticLiquid.field_176367_b)), 2);
        world.func_175684_a(blockposition, (Block) blockflowing, this.func_149738_a(world));
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (this.field_149764_J == Material.field_151587_i) {
            if (world.func_82736_K().func_82766_b("doFireTick")) {
                int i = random.nextInt(3);

                if (i > 0) {
                    BlockPos blockposition1 = blockposition;

                    for (int j = 0; j < i; ++j) {
                        blockposition1 = blockposition1.func_177982_a(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                        if (blockposition1.func_177956_o() >= 0 && blockposition1.func_177956_o() < 256 && !world.func_175667_e(blockposition1)) {
                            return;
                        }

                        Block block = world.func_180495_p(blockposition1).func_177230_c();

                        if (block.field_149764_J == Material.field_151579_a) {
                            if (this.func_176369_e(world, blockposition1)) {
                                 // CraftBukkit start - Prevent lava putting something on fire
                                if (world.func_180495_p(blockposition1) != Blocks.field_150480_ab) {
                                    if (CraftEventFactory.callBlockIgniteEvent(world, blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()).isCancelled()) {
                                        continue;
                                    }
                                }
                                // CraftBukkit end
                                world.func_175656_a(blockposition1, Blocks.field_150480_ab.func_176223_P());
                                return;
                            }
                        } else if (block.field_149764_J.func_76230_c()) {
                            return;
                        }
                    }
                } else {
                    for (int k = 0; k < 3; ++k) {
                        BlockPos blockposition2 = blockposition.func_177982_a(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);

                        if (blockposition2.func_177956_o() >= 0 && blockposition2.func_177956_o() < 256 && !world.func_175667_e(blockposition2)) {
                            return;
                        }

                        if (world.func_175623_d(blockposition2.func_177984_a()) && this.func_176368_m(world, blockposition2)) {
                            // CraftBukkit start - Prevent lava putting something on fire
                            BlockPos up = blockposition2.func_177984_a();
                            if (world.func_180495_p(up) != Blocks.field_150480_ab) {
                                if (CraftEventFactory.callBlockIgniteEvent(world, up.func_177958_n(), up.func_177956_o(), up.func_177952_p(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()).isCancelled()) {
                                    continue;
                                }
                            }
                            // CraftBukkit end
                            world.func_175656_a(blockposition2.func_177984_a(), Blocks.field_150480_ab.func_176223_P());
                        }
                    }
                }

            }
        }
    }

    protected boolean func_176369_e(World world, BlockPos blockposition) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (this.func_176368_m(world, blockposition.func_177972_a(enumdirection))) {
                return true;
            }
        }

        return false;
    }

    private boolean func_176368_m(World world, BlockPos blockposition) {
        return blockposition.func_177956_o() >= 0 && blockposition.func_177956_o() < 256 && !world.func_175667_e(blockposition) ? false : world.func_180495_p(blockposition).func_185904_a().func_76217_h();
    }
}
