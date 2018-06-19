package net.minecraft.world.gen.structure.template;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITemplateProcessor {

    @Nullable
    Template.BlockInfo func_189943_a(World world, BlockPos blockposition, Template.BlockInfo definedstructure_blockinfo);
}
