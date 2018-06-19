package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.event.block.LeavesDecayEvent;

public abstract class BlockLeaves extends Block {

    public static final PropertyBool field_176237_a = PropertyBool.func_177716_a("decayable");
    public static final PropertyBool field_176236_b = PropertyBool.func_177716_a("check_decay");
    protected boolean field_185686_c;
    int[] field_150128_a;

    public BlockLeaves() {
        super(Material.field_151584_j);
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78031_c);
        this.func_149711_c(0.2F);
        this.func_149713_g(1);
        this.func_149672_a(SoundType.field_185850_c);
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = true;
        boolean flag1 = true;
        int i = blockposition.func_177958_n();
        int j = blockposition.func_177956_o();
        int k = blockposition.func_177952_p();

        if (world.func_175707_a(new BlockPos(i - 2, j - 2, k - 2), new BlockPos(i + 2, j + 2, k + 2))) {
            for (int l = -1; l <= 1; ++l) {
                for (int i1 = -1; i1 <= 1; ++i1) {
                    for (int j1 = -1; j1 <= 1; ++j1) {
                        BlockPos blockposition1 = blockposition.func_177982_a(l, i1, j1);
                        IBlockState iblockdata1 = world.func_180495_p(blockposition1);

                        if (iblockdata1.func_185904_a() == Material.field_151584_j && !((Boolean) iblockdata1.func_177229_b(BlockLeaves.field_176236_b)).booleanValue()) {
                            world.func_180501_a(blockposition1, iblockdata1.func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(true)), 4);
                        }
                    }
                }
            }
        }

    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            if (((Boolean) iblockdata.func_177229_b(BlockLeaves.field_176236_b)).booleanValue() && ((Boolean) iblockdata.func_177229_b(BlockLeaves.field_176237_a)).booleanValue()) {
                boolean flag = true;
                boolean flag1 = true;
                int i = blockposition.func_177958_n();
                int j = blockposition.func_177956_o();
                int k = blockposition.func_177952_p();
                boolean flag2 = true;
                boolean flag3 = true;
                boolean flag4 = true;

                if (this.field_150128_a == null) {
                    this.field_150128_a = new int['\u8000'];
                }

                if (world.func_175707_a(new BlockPos(i - 5, j - 5, k - 5), new BlockPos(i + 5, j + 5, k + 5))) {
                    BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                    int l;
                    int i1;
                    int j1;

                    for (l = -4; l <= 4; ++l) {
                        for (i1 = -4; i1 <= 4; ++i1) {
                            for (j1 = -4; j1 <= 4; ++j1) {
                                IBlockState iblockdata1 = world.func_180495_p(blockposition_mutableblockposition.func_181079_c(i + l, j + i1, k + j1));
                                Block block = iblockdata1.func_177230_c();

                                if (block != Blocks.field_150364_r && block != Blocks.field_150363_s) {
                                    if (iblockdata1.func_185904_a() == Material.field_151584_j) {
                                        this.field_150128_a[(l + 16) * 1024 + (i1 + 16) * 32 + j1 + 16] = -2;
                                    } else {
                                        this.field_150128_a[(l + 16) * 1024 + (i1 + 16) * 32 + j1 + 16] = -1;
                                    }
                                } else {
                                    this.field_150128_a[(l + 16) * 1024 + (i1 + 16) * 32 + j1 + 16] = 0;
                                }
                            }
                        }
                    }

                    for (l = 1; l <= 4; ++l) {
                        for (i1 = -4; i1 <= 4; ++i1) {
                            for (j1 = -4; j1 <= 4; ++j1) {
                                for (int k1 = -4; k1 <= 4; ++k1) {
                                    if (this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16) * 32 + k1 + 16] == l - 1) {
                                        if (this.field_150128_a[(i1 + 16 - 1) * 1024 + (j1 + 16) * 32 + k1 + 16] == -2) {
                                            this.field_150128_a[(i1 + 16 - 1) * 1024 + (j1 + 16) * 32 + k1 + 16] = l;
                                        }

                                        if (this.field_150128_a[(i1 + 16 + 1) * 1024 + (j1 + 16) * 32 + k1 + 16] == -2) {
                                            this.field_150128_a[(i1 + 16 + 1) * 1024 + (j1 + 16) * 32 + k1 + 16] = l;
                                        }

                                        if (this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16 - 1) * 32 + k1 + 16] == -2) {
                                            this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16 - 1) * 32 + k1 + 16] = l;
                                        }

                                        if (this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16 + 1) * 32 + k1 + 16] == -2) {
                                            this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16 + 1) * 32 + k1 + 16] = l;
                                        }

                                        if (this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16) * 32 + (k1 + 16 - 1)] == -2) {
                                            this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16) * 32 + (k1 + 16 - 1)] = l;
                                        }

                                        if (this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16) * 32 + k1 + 16 + 1] == -2) {
                                            this.field_150128_a[(i1 + 16) * 1024 + (j1 + 16) * 32 + k1 + 16 + 1] = l;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int l1 = this.field_150128_a[16912];

                if (l1 >= 0) {
                    world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false)), 4);
                } else {
                    this.func_176235_d(world, blockposition);
                }
            }

        }
    }

    private void func_176235_d(World world, BlockPos blockposition) {
        // CraftBukkit start
        LeavesDecayEvent event = new LeavesDecayEvent(world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
        world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled() || world.func_180495_p(blockposition).func_177230_c() != this) {
            return;
        }
        // CraftBukkit end
        this.func_176226_b(world, blockposition, world.func_180495_p(blockposition), 0);
        world.func_175698_g(blockposition);
    }

    public int func_149745_a(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(Blocks.field_150345_g);
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.field_72995_K) {
            int j = this.func_176232_d(iblockdata);

            if (i > 0) {
                j -= 2 << i;
                if (j < 10) {
                    j = 10;
                }
            }

            if (world.field_73012_v.nextInt(j) == 0) {
                Item item = this.func_180660_a(iblockdata, world.field_73012_v, i);

                func_180635_a(world, blockposition, new ItemStack(item, 1, this.func_180651_a(iblockdata)));
            }

            j = 200;
            if (i > 0) {
                j -= 10 << i;
                if (j < 40) {
                    j = 40;
                }
            }

            this.func_176234_a(world, blockposition, iblockdata, j);
        }

    }

    protected void func_176234_a(World world, BlockPos blockposition, IBlockState iblockdata, int i) {}

    protected int func_176232_d(IBlockState iblockdata) {
        return 20;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return !this.field_185686_c;
    }

    public boolean func_176214_u(IBlockState iblockdata) {
        return false;
    }

    public abstract BlockPlanks.EnumType func_176233_b(int i);
}
