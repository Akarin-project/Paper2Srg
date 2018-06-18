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

    protected void playClickSound(@Nullable EntityPlayer entityhuman, World world, BlockPos blockposition) {
        world.playSound(entityhuman, blockposition, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    protected void playReleaseSound(World world, BlockPos blockposition) {
        world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
    }
}
