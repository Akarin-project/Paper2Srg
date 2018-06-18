package net.minecraft.world.gen.structure.template;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRotationProcessor implements ITemplateProcessor {

    private final float chance;
    private final Random random;

    public BlockRotationProcessor(BlockPos blockposition, PlacementSettings definedstructureinfo) {
        this.chance = definedstructureinfo.getIntegrity();
        this.random = definedstructureinfo.getRandom(blockposition);
    }

    @Nullable
    public Template.BlockInfo processBlock(World world, BlockPos blockposition, Template.BlockInfo definedstructure_blockinfo) {
        return this.chance < 1.0F && this.random.nextFloat() > this.chance ? null : definedstructure_blockinfo;
    }
}
