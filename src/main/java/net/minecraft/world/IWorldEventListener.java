package net.minecraft.world;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public interface IWorldEventListener {

    void func_184376_a(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1, int i);

    void func_174959_b(BlockPos blockposition);

    void func_147585_a(int i, int j, int k, int l, int i1, int j1);

    void func_184375_a(@Nullable EntityPlayer entityhuman, SoundEvent soundeffect, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1);

    void func_184377_a(SoundEvent soundeffect, BlockPos blockposition);

    void func_180442_a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint);

    void func_190570_a(int i, boolean flag, boolean flag1, double d0, double d1, double d2, double d3, double d4, double d5, int... aint);

    void func_72703_a(Entity entity);

    void func_72709_b(Entity entity);

    void func_180440_a(int i, BlockPos blockposition, int j);

    void func_180439_a(EntityPlayer entityhuman, int i, BlockPos blockposition, int j);

    void func_180441_b(int i, BlockPos blockposition, int j);
}
