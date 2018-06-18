package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator {

    private boolean crystalInvulnerable;
    private WorldGenSpikes.EndSpike spike;
    private BlockPos beamTarget;

    public WorldGenSpikes() {}

    public void setSpike(WorldGenSpikes.EndSpike worldgenender_spike) {
        this.spike = worldgenender_spike;
    }

    public void setCrystalInvulnerable(boolean flag) {
        this.crystalInvulnerable = flag;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        if (this.spike == null) {
            throw new IllegalStateException("Decoration requires priming with a spike");
        } else {
            int i = this.spike.getRadius();
            Iterator iterator = BlockPos.getAllInBoxMutable(new BlockPos(blockposition.getX() - i, 0, blockposition.getZ() - i), new BlockPos(blockposition.getX() + i, this.spike.getHeight() + 10, blockposition.getZ() + i)).iterator();

            while (iterator.hasNext()) {
                BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();

                if (blockposition_mutableblockposition.distanceSq((double) blockposition.getX(), (double) blockposition_mutableblockposition.getY(), (double) blockposition.getZ()) <= (double) (i * i + 1) && blockposition_mutableblockposition.getY() < this.spike.getHeight()) {
                    this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.OBSIDIAN.getDefaultState());
                } else if (blockposition_mutableblockposition.getY() > 65) {
                    this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.AIR.getDefaultState());
                }
            }

            if (this.spike.isGuarded()) {
                for (int j = -2; j <= 2; ++j) {
                    for (int k = -2; k <= 2; ++k) {
                        if (MathHelper.abs(j) == 2 || MathHelper.abs(k) == 2) {
                            this.setBlockAndNotifyAdequately(world, new BlockPos(blockposition.getX() + j, this.spike.getHeight(), blockposition.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                            this.setBlockAndNotifyAdequately(world, new BlockPos(blockposition.getX() + j, this.spike.getHeight() + 1, blockposition.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                            this.setBlockAndNotifyAdequately(world, new BlockPos(blockposition.getX() + j, this.spike.getHeight() + 2, blockposition.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                        }

                        this.setBlockAndNotifyAdequately(world, new BlockPos(blockposition.getX() + j, this.spike.getHeight() + 3, blockposition.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                    }
                }
            }

            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(world);

            entityendercrystal.setBeamTarget(this.beamTarget);
            entityendercrystal.setEntityInvulnerable(this.crystalInvulnerable);
            entityendercrystal.setLocationAndAngles((double) ((float) blockposition.getX() + 0.5F), (double) (this.spike.getHeight() + 1), (double) ((float) blockposition.getZ() + 0.5F), random.nextFloat() * 360.0F, 0.0F);
            world.spawnEntity(entityendercrystal);
            this.setBlockAndNotifyAdequately(world, new BlockPos(blockposition.getX(), this.spike.getHeight(), blockposition.getZ()), Blocks.BEDROCK.getDefaultState());
            return true;
        }
    }

    public void setBeamTarget(@Nullable BlockPos blockposition) {
        this.beamTarget = blockposition;
    }

    public static class EndSpike {

        private final int centerX;
        private final int centerZ;
        private final int radius;
        private final int height;
        private final boolean guarded;
        private final AxisAlignedBB topBoundingBox;

        public EndSpike(int i, int j, int k, int l, boolean flag) {
            this.centerX = i;
            this.centerZ = j;
            this.radius = k;
            this.height = l;
            this.guarded = flag;
            this.topBoundingBox = new AxisAlignedBB((double) (i - k), 0.0D, (double) (j - k), (double) (i + k), 256.0D, (double) (j + k));
        }

        public boolean doesStartInChunk(BlockPos blockposition) {
            int i = this.centerX - this.radius;
            int j = this.centerZ - this.radius;

            return blockposition.getX() == (i & -16) && blockposition.getZ() == (j & -16);
        }

        public int getCenterX() {
            return this.centerX;
        }

        public int getCenterZ() {
            return this.centerZ;
        }

        public int getRadius() {
            return this.radius;
        }

        public int getHeight() {
            return this.height;
        }

        public boolean isGuarded() {
            return this.guarded;
        }

        public AxisAlignedBB getTopBoundingBox() {
            return this.topBoundingBox;
        }
    }
}
