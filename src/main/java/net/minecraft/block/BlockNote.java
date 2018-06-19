package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNote extends BlockContainer {

    private static final List<SoundEvent> field_176434_a = Lists.newArrayList(new SoundEvent[] { SoundEvents.field_187682_dG, SoundEvents.field_187676_dE, SoundEvents.field_187688_dI, SoundEvents.field_187685_dH, SoundEvents.field_187679_dF, SoundEvents.field_193809_ey, SoundEvents.field_193807_ew, SoundEvents.field_193810_ez, SoundEvents.field_193808_ex, SoundEvents.field_193785_eE});

    public BlockNote() {
        super(Material.field_151575_d);
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        boolean flag = world.func_175640_z(blockposition);
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityNote) {
            TileEntityNote tileentitynote = (TileEntityNote) tileentity;

            if (tileentitynote.field_145880_i != flag) {
                if (flag) {
                    tileentitynote.func_175108_a(world, blockposition);
                }

                tileentitynote.field_145880_i = flag;
            }
        }

    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityNote) {
                TileEntityNote tileentitynote = (TileEntityNote) tileentity;

                tileentitynote.func_145877_a();
                tileentitynote.func_175108_a(world, blockposition);
                entityhuman.func_71029_a(StatList.field_188087_U);
            }

            return true;
        }
    }

    public void func_180649_a(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        if (!world.field_72995_K) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityNote) {
                ((TileEntityNote) tileentity).func_175108_a(world, blockposition);
                entityhuman.func_71029_a(StatList.field_188086_T);
            }

        }
    }

    public TileEntity func_149915_a(World world, int i) {
        return new TileEntityNote();
    }

    private SoundEvent func_185576_e(int i) {
        if (i < 0 || i >= BlockNote.field_176434_a.size()) {
            i = 0;
        }

        return (SoundEvent) BlockNote.field_176434_a.get(i);
    }

    public boolean func_189539_a(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        float f = (float) Math.pow(2.0D, (double) (j - 12) / 12.0D);

        world.func_184133_a((EntityPlayer) null, blockposition, this.func_185576_e(i), SoundCategory.RECORDS, 3.0F, f);
        world.func_175688_a(EnumParticleTypes.NOTE, (double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 1.2D, (double) blockposition.func_177952_p() + 0.5D, (double) j / 24.0D, 0.0D, 0.0D, new int[0]);
        return true;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }
}
