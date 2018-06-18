package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenBigTree extends WorldGenAbstractTree {

    private Random rand;
    private World world;
    private BlockPos basePos;
    int heightLimit;
    int height;
    double heightAttenuation;
    double branchSlope;
    double scaleWidth;
    double leafDensity;
    int trunkSize;
    int heightLimitLimit;
    int leafDistanceLimit;
    List<WorldGenBigTree.FoliageCoordinates> foliageCoords;

    public WorldGenBigTree(boolean flag) {
        super(flag);
        this.basePos = BlockPos.ORIGIN;
        this.heightAttenuation = 0.618D;
        this.branchSlope = 0.381D;
        this.scaleWidth = 1.0D;
        this.leafDensity = 1.0D;
        this.trunkSize = 1;
        this.heightLimitLimit = 12;
        this.leafDistanceLimit = 4;
    }

    void generateLeafNodeList() {
        this.height = (int) ((double) this.heightLimit * this.heightAttenuation);
        if (this.height >= this.heightLimit) {
            this.height = this.heightLimit - 1;
        }

        int i = (int) (1.382D + Math.pow(this.leafDensity * (double) this.heightLimit / 13.0D, 2.0D));

        if (i < 1) {
            i = 1;
        }

        int j = this.basePos.getY() + this.height;
        int k = this.heightLimit - this.leafDistanceLimit;

        this.foliageCoords = Lists.newArrayList();
        this.foliageCoords.add(new WorldGenBigTree.FoliageCoordinates(this.basePos.up(k), j));

        for (; k >= 0; --k) {
            float f = this.layerSize(k);

            if (f >= 0.0F) {
                for (int l = 0; l < i; ++l) {
                    double d0 = this.scaleWidth * (double) f * ((double) this.rand.nextFloat() + 0.328D);
                    double d1 = (double) (this.rand.nextFloat() * 2.0F) * 3.141592653589793D;
                    double d2 = d0 * Math.sin(d1) + 0.5D;
                    double d3 = d0 * Math.cos(d1) + 0.5D;
                    BlockPos blockposition = this.basePos.add(d2, (double) (k - 1), d3);
                    BlockPos blockposition1 = blockposition.up(this.leafDistanceLimit);

                    if (this.checkBlockLine(blockposition, blockposition1) == -1) {
                        int i1 = this.basePos.getX() - blockposition.getX();
                        int j1 = this.basePos.getZ() - blockposition.getZ();
                        double d4 = (double) blockposition.getY() - Math.sqrt((double) (i1 * i1 + j1 * j1)) * this.branchSlope;
                        int k1 = d4 > (double) j ? j : (int) d4;
                        BlockPos blockposition2 = new BlockPos(this.basePos.getX(), k1, this.basePos.getZ());

                        if (this.checkBlockLine(blockposition2, blockposition) == -1) {
                            this.foliageCoords.add(new WorldGenBigTree.FoliageCoordinates(blockposition, blockposition2.getY()));
                        }
                    }
                }
            }
        }

    }

    void crosSection(BlockPos blockposition, float f, IBlockState iblockdata) {
        int i = (int) ((double) f + 0.618D);

        for (int j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                if (Math.pow((double) Math.abs(j) + 0.5D, 2.0D) + Math.pow((double) Math.abs(k) + 0.5D, 2.0D) <= (double) (f * f)) {
                    BlockPos blockposition1 = blockposition.add(j, 0, k);
                    Material material = this.world.getBlockState(blockposition1).getMaterial();

                    if (material == Material.AIR || material == Material.LEAVES) {
                        this.setBlockAndNotifyAdequately(this.world, blockposition1, iblockdata);
                    }
                }
            }
        }

    }

    float layerSize(int i) {
        if ((float) i < (float) this.heightLimit * 0.3F) {
            return -1.0F;
        } else {
            float f = (float) this.heightLimit / 2.0F;
            float f1 = f - (float) i;
            float f2 = MathHelper.sqrt(f * f - f1 * f1);

            if (f1 == 0.0F) {
                f2 = f;
            } else if (Math.abs(f1) >= f) {
                return 0.0F;
            }

            return f2 * 0.5F;
        }
    }

    float leafSize(int i) {
        return i >= 0 && i < this.leafDistanceLimit ? (i != 0 && i != this.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
    }

    void generateLeafNode(BlockPos blockposition) {
        for (int i = 0; i < this.leafDistanceLimit; ++i) {
            this.crosSection(blockposition.up(i), this.leafSize(i), Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false)));
        }

    }

    void limb(BlockPos blockposition, BlockPos blockposition1, Block block) {
        BlockPos blockposition2 = blockposition1.add(-blockposition.getX(), -blockposition.getY(), -blockposition.getZ());
        int i = this.getGreatestDistance(blockposition2);
        float f = (float) blockposition2.getX() / (float) i;
        float f1 = (float) blockposition2.getY() / (float) i;
        float f2 = (float) blockposition2.getZ() / (float) i;

        for (int j = 0; j <= i; ++j) {
            BlockPos blockposition3 = blockposition.add((double) (0.5F + (float) j * f), (double) (0.5F + (float) j * f1), (double) (0.5F + (float) j * f2));
            BlockLog.EnumAxis blocklogabstract_enumlogrotation = this.getLogAxis(blockposition, blockposition3);

            this.setBlockAndNotifyAdequately(this.world, blockposition3, block.getDefaultState().withProperty(BlockLog.LOG_AXIS, blocklogabstract_enumlogrotation));
        }

    }

    private int getGreatestDistance(BlockPos blockposition) {
        int i = MathHelper.abs(blockposition.getX());
        int j = MathHelper.abs(blockposition.getY());
        int k = MathHelper.abs(blockposition.getZ());

        return k > i && k > j ? k : (j > i ? j : i);
    }

    private BlockLog.EnumAxis getLogAxis(BlockPos blockposition, BlockPos blockposition1) {
        BlockLog.EnumAxis blocklogabstract_enumlogrotation = BlockLog.EnumAxis.Y;
        int i = Math.abs(blockposition1.getX() - blockposition.getX());
        int j = Math.abs(blockposition1.getZ() - blockposition.getZ());
        int k = Math.max(i, j);

        if (k > 0) {
            if (i == k) {
                blocklogabstract_enumlogrotation = BlockLog.EnumAxis.X;
            } else if (j == k) {
                blocklogabstract_enumlogrotation = BlockLog.EnumAxis.Z;
            }
        }

        return blocklogabstract_enumlogrotation;
    }

    void generateLeaves() {
        Iterator iterator = this.foliageCoords.iterator();

        while (iterator.hasNext()) {
            WorldGenBigTree.FoliageCoordinates worldgenbigtree_position = (WorldGenBigTree.FoliageCoordinates) iterator.next();

            this.generateLeafNode((BlockPos) worldgenbigtree_position);
        }

    }

    boolean leafNodeNeedsBase(int i) {
        return (double) i >= (double) this.heightLimit * 0.2D;
    }

    void generateTrunk() {
        BlockPos blockposition = this.basePos;
        BlockPos blockposition1 = this.basePos.up(this.height);
        Block block = Blocks.LOG;

        this.limb(blockposition, blockposition1, block);
        if (this.trunkSize == 2) {
            this.limb(blockposition.east(), blockposition1.east(), block);
            this.limb(blockposition.east().south(), blockposition1.east().south(), block);
            this.limb(blockposition.south(), blockposition1.south(), block);
        }

    }

    void generateLeafNodeBases() {
        Iterator iterator = this.foliageCoords.iterator();

        while (iterator.hasNext()) {
            WorldGenBigTree.FoliageCoordinates worldgenbigtree_position = (WorldGenBigTree.FoliageCoordinates) iterator.next();
            int i = worldgenbigtree_position.getBranchBase();
            BlockPos blockposition = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());

            if (!blockposition.equals(worldgenbigtree_position) && this.leafNodeNeedsBase(i - this.basePos.getY())) {
                this.limb(blockposition, (BlockPos) worldgenbigtree_position, Blocks.LOG);
            }
        }

    }

    int checkBlockLine(BlockPos blockposition, BlockPos blockposition1) {
        BlockPos blockposition2 = blockposition1.add(-blockposition.getX(), -blockposition.getY(), -blockposition.getZ());
        int i = this.getGreatestDistance(blockposition2);
        float f = (float) blockposition2.getX() / (float) i;
        float f1 = (float) blockposition2.getY() / (float) i;
        float f2 = (float) blockposition2.getZ() / (float) i;

        if (i == 0) {
            return -1;
        } else {
            for (int j = 0; j <= i; ++j) {
                BlockPos blockposition3 = blockposition.add((double) (0.5F + (float) j * f), (double) (0.5F + (float) j * f1), (double) (0.5F + (float) j * f2));

                if (!this.canGrowInto(this.world.getBlockState(blockposition3).getBlock())) {
                    return j;
                }
            }

            return -1;
        }
    }

    public void setDecorationDefaults() {
        this.leafDistanceLimit = 5;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        this.world = world;
        this.basePos = blockposition;
        this.rand = new Random(random.nextLong());
        if (this.heightLimit == 0) {
            this.heightLimit = 5 + this.rand.nextInt(this.heightLimitLimit);
        }

        if (!this.validTreeLocation()) {
            return false;
        } else {
            this.generateLeafNodeList();
            this.generateLeaves();
            this.generateTrunk();
            this.generateLeafNodeBases();
            return true;
        }
    }

    private boolean validTreeLocation() {
        Block block = this.world.getBlockState(this.basePos.down()).getBlock();

        if (block != Blocks.DIRT && block != Blocks.GRASS && block != Blocks.FARMLAND) {
            return false;
        } else {
            int i = this.checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));

            if (i == -1) {
                return true;
            } else if (i < 6) {
                return false;
            } else {
                this.heightLimit = i;
                return true;
            }
        }
    }

    static class FoliageCoordinates extends BlockPos {

        private final int branchBase;

        public FoliageCoordinates(BlockPos blockposition, int i) {
            super(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            this.branchBase = i;
        }

        public int getBranchBase() {
            return this.branchBase;
        }
    }
}
