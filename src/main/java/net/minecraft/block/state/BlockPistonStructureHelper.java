package net.minecraft.block.state;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPistonStructureHelper {

    private final World field_177261_a;
    private final BlockPos field_177259_b;
    private final BlockPos field_177260_c;
    private final EnumFacing field_177257_d;
    private final List<BlockPos> field_177258_e = Lists.newArrayList();
    private final List<BlockPos> field_177256_f = Lists.newArrayList();

    public BlockPistonStructureHelper(World world, BlockPos blockposition, EnumFacing enumdirection, boolean flag) {
        this.field_177261_a = world;
        this.field_177259_b = blockposition;
        if (flag) {
            this.field_177257_d = enumdirection;
            this.field_177260_c = blockposition.func_177972_a(enumdirection);
        } else {
            this.field_177257_d = enumdirection.func_176734_d();
            this.field_177260_c = blockposition.func_177967_a(enumdirection, 2);
        }

    }

    public boolean func_177253_a() {
        this.field_177258_e.clear();
        this.field_177256_f.clear();
        IBlockState iblockdata = this.field_177261_a.func_180495_p(this.field_177260_c);

        if (!BlockPistonBase.func_185646_a(iblockdata, this.field_177261_a, this.field_177260_c, this.field_177257_d, false, this.field_177257_d)) {
            if (iblockdata.func_185905_o() == EnumPushReaction.DESTROY) {
                this.field_177256_f.add(this.field_177260_c);
                return true;
            } else {
                return false;
            }
        } else if (!this.func_177251_a(this.field_177260_c, this.field_177257_d)) {
            return false;
        } else {
            for (int i = 0; i < this.field_177258_e.size(); ++i) {
                BlockPos blockposition = (BlockPos) this.field_177258_e.get(i);

                if (this.field_177261_a.func_180495_p(blockposition).func_177230_c() == Blocks.field_180399_cE && !this.func_177250_b(blockposition)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean func_177251_a(BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = this.field_177261_a.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();

        if (iblockdata.func_185904_a() == Material.field_151579_a) {
            return true;
        } else if (!BlockPistonBase.func_185646_a(iblockdata, this.field_177261_a, blockposition, this.field_177257_d, false, enumdirection)) {
            return true;
        } else if (blockposition.equals(this.field_177259_b)) {
            return true;
        } else if (this.field_177258_e.contains(blockposition)) {
            return true;
        } else {
            int i = 1;

            if (i + this.field_177258_e.size() > 12) {
                return false;
            } else {
                while (block == Blocks.field_180399_cE) {
                    BlockPos blockposition1 = blockposition.func_177967_a(this.field_177257_d.func_176734_d(), i);

                    iblockdata = this.field_177261_a.func_180495_p(blockposition1);
                    block = iblockdata.func_177230_c();
                    if (iblockdata.func_185904_a() == Material.field_151579_a || !BlockPistonBase.func_185646_a(iblockdata, this.field_177261_a, blockposition1, this.field_177257_d, false, this.field_177257_d.func_176734_d()) || blockposition1.equals(this.field_177259_b)) {
                        break;
                    }

                    ++i;
                    if (i + this.field_177258_e.size() > 12) {
                        return false;
                    }
                }

                int j = 0;

                int k;

                for (k = i - 1; k >= 0; --k) {
                    this.field_177258_e.add(blockposition.func_177967_a(this.field_177257_d.func_176734_d(), k));
                    ++j;
                }

                k = 1;

                while (true) {
                    BlockPos blockposition2 = blockposition.func_177967_a(this.field_177257_d, k);
                    int l = this.field_177258_e.indexOf(blockposition2);

                    if (l > -1) {
                        this.func_177255_a(j, l);

                        for (int i1 = 0; i1 <= l + j; ++i1) {
                            BlockPos blockposition3 = (BlockPos) this.field_177258_e.get(i1);

                            if (this.field_177261_a.func_180495_p(blockposition3).func_177230_c() == Blocks.field_180399_cE && !this.func_177250_b(blockposition3)) {
                                return false;
                            }
                        }

                        return true;
                    }

                    iblockdata = this.field_177261_a.func_180495_p(blockposition2);
                    if (iblockdata.func_185904_a() == Material.field_151579_a) {
                        return true;
                    }

                    if (!BlockPistonBase.func_185646_a(iblockdata, this.field_177261_a, blockposition2, this.field_177257_d, true, this.field_177257_d) || blockposition2.equals(this.field_177259_b)) {
                        return false;
                    }

                    if (iblockdata.func_185905_o() == EnumPushReaction.DESTROY) {
                        this.field_177256_f.add(blockposition2);
                        return true;
                    }

                    if (this.field_177258_e.size() >= 12) {
                        return false;
                    }

                    this.field_177258_e.add(blockposition2);
                    ++j;
                    ++k;
                }
            }
        }
    }

    private void func_177255_a(int i, int j) {
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList();
        ArrayList arraylist2 = Lists.newArrayList();

        arraylist.addAll(this.field_177258_e.subList(0, j));
        arraylist1.addAll(this.field_177258_e.subList(this.field_177258_e.size() - i, this.field_177258_e.size()));
        arraylist2.addAll(this.field_177258_e.subList(j, this.field_177258_e.size() - i));
        this.field_177258_e.clear();
        this.field_177258_e.addAll(arraylist);
        this.field_177258_e.addAll(arraylist1);
        this.field_177258_e.addAll(arraylist2);
    }

    private boolean func_177250_b(BlockPos blockposition) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (enumdirection.func_176740_k() != this.field_177257_d.func_176740_k() && !this.func_177251_a(blockposition.func_177972_a(enumdirection), enumdirection)) {
                return false;
            }
        }

        return true;
    }

    public List<BlockPos> func_177254_c() {
        return this.field_177258_e;
    }

    public List<BlockPos> func_177252_d() {
        return this.field_177256_f;
    }
}
