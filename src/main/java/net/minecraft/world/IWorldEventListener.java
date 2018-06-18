package net.minecraft.world;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public interface IWorldEventListener {

    void notifyBlockUpdate(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1, int i);

    void notifyLightSet(BlockPos blockposition);

    void markBlockRangeForRenderUpdate(int i, int j, int k, int l, int i1, int j1);

    void playSoundToAllNearExcept(@Nullable EntityPlayer entityhuman, SoundEvent soundeffect, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1);

    void playRecord(SoundEvent soundeffect, BlockPos blockposition);

    void spawnParticle(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint);

    void spawnParticle(int i, boolean flag, boolean flag1, double d0, double d1, double d2, double d3, double d4, double d5, int... aint);

    void onEntityAdded(Entity entity);

    void onEntityRemoved(Entity entity);

    void broadcastSound(int i, BlockPos blockposition, int j);

    void playEvent(EntityPlayer entityhuman, int i, BlockPos blockposition, int j);

    void sendBlockBreakProgress(int i, BlockPos blockposition, int j);
}
