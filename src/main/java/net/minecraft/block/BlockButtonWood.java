package net.minecraft.block;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockButtonWood extends BlockButton {

    protected BlockButtonWood() {
        super(true);
    }

    protected void func_185615_a(@Nullable EntityPlayer entityhuman, World world, BlockPos blockposition) {
        world.func_184133_a(entityhuman, blockposition, SoundEvents.field_187885_gS, SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    protected void func_185617_b(World world, BlockPos blockposition) {
        world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187883_gR, SoundCategory.BLOCKS, 0.3F, 0.5F);
    }
}
