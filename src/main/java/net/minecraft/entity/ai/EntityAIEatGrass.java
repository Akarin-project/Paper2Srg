package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Material;
import org.bukkit.craftbukkit.event.CraftEventFactory;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.Material;
// CraftBukkit end

public class EntityAIEatGrass extends EntityAIBase {

    private static final Predicate<IBlockState> IS_TALL_GRASS = BlockStateMatcher.forBlock((Block) Blocks.TALLGRASS).where(BlockTallGrass.TYPE, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));
    private final EntityLiving grassEaterEntity;
    private final World entityWorld;
    int eatingGrassTimer;

    public EntityAIEatGrass(EntityLiving entityinsentient) {
        this.grassEaterEntity = entityinsentient;
        this.entityWorld = entityinsentient.world;
        this.setMutexBits(7);
    }

    public boolean shouldExecute() {
        if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos blockposition = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);

            return EntityAIEatGrass.IS_TALL_GRASS.apply(this.entityWorld.getBlockState(blockposition)) ? true : this.entityWorld.getBlockState(blockposition.down()).getBlock() == Blocks.GRASS;
        }
    }

    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.grassEaterEntity, (byte) 10);
        this.grassEaterEntity.getNavigator().clearPath();
    }

    public void resetTask() {
        this.eatingGrassTimer = 0;
    }

    public boolean shouldContinueExecuting() {
        return this.eatingGrassTimer > 0;
    }

    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }

    public void updateTask() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer == 4) {
            BlockPos blockposition = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);

            if (EntityAIEatGrass.IS_TALL_GRASS.apply(this.entityWorld.getBlockState(blockposition))) {
                // CraftBukkit
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.grassEaterEntity, this.grassEaterEntity.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), Material.AIR, !this.entityWorld.getGameRules().getBoolean("mobGriefing")).isCancelled()) {
                    this.entityWorld.destroyBlock(blockposition, false);
                }

                this.grassEaterEntity.eatGrassBonus();
            } else {
                BlockPos blockposition1 = blockposition.down();

                if (this.entityWorld.getBlockState(blockposition1).getBlock() == Blocks.GRASS) {
                    // CraftBukkit
                    if (!CraftEventFactory.callEntityChangeBlockEvent(this.grassEaterEntity, this.grassEaterEntity.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), Material.AIR, !this.entityWorld.getGameRules().getBoolean("mobGriefing")).isCancelled()) {
                        this.entityWorld.playEvent(2001, blockposition1, Block.getIdFromBlock(Blocks.GRASS));
                        this.entityWorld.setBlockState(blockposition1, Blocks.DIRT.getDefaultState(), 2);
                    }

                    this.grassEaterEntity.eatGrassBonus();
                }
            }

        }
    }
}
