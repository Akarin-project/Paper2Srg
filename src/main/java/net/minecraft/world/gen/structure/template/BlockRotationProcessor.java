package net.minecraft.world.gen.structure.template;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRotationProcessor implements ITemplateProcessor {

    private final float field_189944_a;
    private final Random field_189945_b;

    public BlockRotationProcessor(BlockPos blockposition, PlacementSettings definedstructureinfo) {
        this.field_189944_a = definedstructureinfo.func_189948_f();
        this.field_189945_b = definedstructureinfo.func_189947_a(blockposition);
    }

    @Nullable
    public Template.BlockInfo func_189943_a(World world, BlockPos blockposition, Template.BlockInfo definedstructure_blockinfo) {
        return this.field_189944_a < 1.0F && this.field_189945_b.nextFloat() > this.field_189944_a ? null : definedstructure_blockinfo;
    }
}
