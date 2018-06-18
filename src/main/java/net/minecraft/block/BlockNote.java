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

    private static final List<SoundEvent> INSTRUMENTS = Lists.newArrayList(new SoundEvent[] { SoundEvents.BLOCK_NOTE_HARP, SoundEvents.BLOCK_NOTE_BASEDRUM, SoundEvents.BLOCK_NOTE_SNARE, SoundEvents.BLOCK_NOTE_HAT, SoundEvents.BLOCK_NOTE_BASS, SoundEvents.BLOCK_NOTE_FLUTE, SoundEvents.BLOCK_NOTE_BELL, SoundEvents.BLOCK_NOTE_GUITAR, SoundEvents.BLOCK_NOTE_CHIME, SoundEvents.BLOCK_NOTE_XYLOPHONE});

    public BlockNote() {
        super(Material.WOOD);
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        boolean flag = world.isBlockPowered(blockposition);
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityNote) {
            TileEntityNote tileentitynote = (TileEntityNote) tileentity;

            if (tileentitynote.previousRedstoneState != flag) {
                if (flag) {
                    tileentitynote.triggerNote(world, blockposition);
                }

                tileentitynote.previousRedstoneState = flag;
            }
        }

    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityNote) {
                TileEntityNote tileentitynote = (TileEntityNote) tileentity;

                tileentitynote.changePitch();
                tileentitynote.triggerNote(world, blockposition);
                entityhuman.addStat(StatList.NOTEBLOCK_TUNED);
            }

            return true;
        }
    }

    public void onBlockClicked(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        if (!world.isRemote) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityNote) {
                ((TileEntityNote) tileentity).triggerNote(world, blockposition);
                entityhuman.addStat(StatList.NOTEBLOCK_PLAYED);
            }

        }
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityNote();
    }

    private SoundEvent getInstrument(int i) {
        if (i < 0 || i >= BlockNote.INSTRUMENTS.size()) {
            i = 0;
        }

        return (SoundEvent) BlockNote.INSTRUMENTS.get(i);
    }

    public boolean eventReceived(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        float f = (float) Math.pow(2.0D, (double) (j - 12) / 12.0D);

        world.playSound((EntityPlayer) null, blockposition, this.getInstrument(i), SoundCategory.RECORDS, 3.0F, f);
        world.spawnParticle(EnumParticleTypes.NOTE, (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 1.2D, (double) blockposition.getZ() + 0.5D, (double) j / 24.0D, 0.0D, 0.0D, new int[0]);
        return true;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }
}
