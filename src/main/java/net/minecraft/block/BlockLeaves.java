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

    public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
    public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
    protected boolean leavesFancy;
    int[] surroundings;

    public BlockLeaves() {
        super(Material.LEAVES);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = true;
        boolean flag1 = true;
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();

        if (world.isAreaLoaded(new BlockPos(i - 2, j - 2, k - 2), new BlockPos(i + 2, j + 2, k + 2))) {
            for (int l = -1; l <= 1; ++l) {
                for (int i1 = -1; i1 <= 1; ++i1) {
                    for (int j1 = -1; j1 <= 1; ++j1) {
                        BlockPos blockposition1 = blockposition.add(l, i1, j1);
                        IBlockState iblockdata1 = world.getBlockState(blockposition1);

                        if (iblockdata1.getMaterial() == Material.LEAVES && !((Boolean) iblockdata1.getValue(BlockLeaves.CHECK_DECAY)).booleanValue()) {
                            world.setBlockState(blockposition1, iblockdata1.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4);
                        }
                    }
                }
            }
        }

    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            if (((Boolean) iblockdata.getValue(BlockLeaves.CHECK_DECAY)).booleanValue() && ((Boolean) iblockdata.getValue(BlockLeaves.DECAYABLE)).booleanValue()) {
                boolean flag = true;
                boolean flag1 = true;
                int i = blockposition.getX();
                int j = blockposition.getY();
                int k = blockposition.getZ();
                boolean flag2 = true;
                boolean flag3 = true;
                boolean flag4 = true;

                if (this.surroundings == null) {
                    this.surroundings = new int['\u8000'];
                }

                if (world.isAreaLoaded(new BlockPos(i - 5, j - 5, k - 5), new BlockPos(i + 5, j + 5, k + 5))) {
                    BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

                    int l;
                    int i1;
                    int j1;

                    for (l = -4; l <= 4; ++l) {
                        for (i1 = -4; i1 <= 4; ++i1) {
                            for (j1 = -4; j1 <= 4; ++j1) {
                                IBlockState iblockdata1 = world.getBlockState(blockposition_mutableblockposition.setPos(i + l, j + i1, k + j1));
                                Block block = iblockdata1.getBlock();

                                if (block != Blocks.LOG && block != Blocks.LOG2) {
                                    if (iblockdata1.getMaterial() == Material.LEAVES) {
                                        this.surroundings[(l + 16) * 1024 + (i1 + 16) * 32 + j1 + 16] = -2;
                                    } else {
                                        this.surroundings[(l + 16) * 1024 + (i1 + 16) * 32 + j1 + 16] = -1;
                                    }
                                } else {
                                    this.surroundings[(l + 16) * 1024 + (i1 + 16) * 32 + j1 + 16] = 0;
                                }
                            }
                        }
                    }

                    for (l = 1; l <= 4; ++l) {
                        for (i1 = -4; i1 <= 4; ++i1) {
                            for (j1 = -4; j1 <= 4; ++j1) {
                                for (int k1 = -4; k1 <= 4; ++k1) {
                                    if (this.surroundings[(i1 + 16) * 1024 + (j1 + 16) * 32 + k1 + 16] == l - 1) {
                                        if (this.surroundings[(i1 + 16 - 1) * 1024 + (j1 + 16) * 32 + k1 + 16] == -2) {
                                            this.surroundings[(i1 + 16 - 1) * 1024 + (j1 + 16) * 32 + k1 + 16] = l;
                                        }

                                        if (this.surroundings[(i1 + 16 + 1) * 1024 + (j1 + 16) * 32 + k1 + 16] == -2) {
                                            this.surroundings[(i1 + 16 + 1) * 1024 + (j1 + 16) * 32 + k1 + 16] = l;
                                        }

                                        if (this.surroundings[(i1 + 16) * 1024 + (j1 + 16 - 1) * 32 + k1 + 16] == -2) {
                                            this.surroundings[(i1 + 16) * 1024 + (j1 + 16 - 1) * 32 + k1 + 16] = l;
                                        }

                                        if (this.surroundings[(i1 + 16) * 1024 + (j1 + 16 + 1) * 32 + k1 + 16] == -2) {
                                            this.surroundings[(i1 + 16) * 1024 + (j1 + 16 + 1) * 32 + k1 + 16] = l;
                                        }

                                        if (this.surroundings[(i1 + 16) * 1024 + (j1 + 16) * 32 + (k1 + 16 - 1)] == -2) {
                                            this.surroundings[(i1 + 16) * 1024 + (j1 + 16) * 32 + (k1 + 16 - 1)] = l;
                                        }

                                        if (this.surroundings[(i1 + 16) * 1024 + (j1 + 16) * 32 + k1 + 16 + 1] == -2) {
                                            this.surroundings[(i1 + 16) * 1024 + (j1 + 16) * 32 + k1 + 16 + 1] = l;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int l1 = this.surroundings[16912];

                if (l1 >= 0) {
                    world.setBlockState(blockposition, iblockdata.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)), 4);
                } else {
                    this.destroy(world, blockposition);
                }
            }

        }
    }

    private void destroy(World world, BlockPos blockposition) {
        // CraftBukkit start
        LeavesDecayEvent event = new LeavesDecayEvent(world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
        world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled() || world.getBlockState(blockposition).getBlock() != this) {
            return;
        }
        // CraftBukkit end
        this.dropBlockAsItem(world, blockposition, world.getBlockState(blockposition), 0);
        world.setBlockToAir(blockposition);
    }

    public int quantityDropped(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.SAPLING);
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.isRemote) {
            int j = this.getSaplingDropChance(iblockdata);

            if (i > 0) {
                j -= 2 << i;
                if (j < 10) {
                    j = 10;
                }
            }

            if (world.rand.nextInt(j) == 0) {
                Item item = this.getItemDropped(iblockdata, world.rand, i);

                spawnAsEntity(world, blockposition, new ItemStack(item, 1, this.damageDropped(iblockdata)));
            }

            j = 200;
            if (i > 0) {
                j -= 10 << i;
                if (j < 40) {
                    j = 40;
                }
            }

            this.dropApple(world, blockposition, iblockdata, j);
        }

    }

    protected void dropApple(World world, BlockPos blockposition, IBlockState iblockdata, int i) {}

    protected int getSaplingDropChance(IBlockState iblockdata) {
        return 20;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return !this.leavesFancy;
    }

    public boolean causesSuffocation(IBlockState iblockdata) {
        return false;
    }

    public abstract BlockPlanks.EnumType getWoodType(int i);
}
