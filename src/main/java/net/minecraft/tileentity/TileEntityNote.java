package net.minecraft.tileentity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class TileEntityNote extends TileEntity {

    public byte note;
    public boolean previousRedstoneState;

    public TileEntityNote() {}

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("note", this.note);
        nbttagcompound.setBoolean("powered", this.previousRedstoneState);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.note = nbttagcompound.getByte("note");
        this.note = (byte) MathHelper.clamp(this.note, 0, 24);
        this.previousRedstoneState = nbttagcompound.getBoolean("powered");
    }

    public void changePitch() {
        this.note = (byte) ((this.note + 1) % 25);
        this.markDirty();
    }

    public void triggerNote(World world, BlockPos blockposition) {
        if (world.getBlockState(blockposition.up()).getMaterial() == Material.AIR) {
            IBlockState iblockdata = world.getBlockState(blockposition.down());
            Material material = iblockdata.getMaterial();
            byte b0 = 0;

            if (material == Material.ROCK) {
                b0 = 1;
            }

            if (material == Material.SAND) {
                b0 = 2;
            }

            if (material == Material.GLASS) {
                b0 = 3;
            }

            if (material == Material.WOOD) {
                b0 = 4;
            }

            Block block = iblockdata.getBlock();

            if (block == Blocks.CLAY) {
                b0 = 5;
            }

            if (block == Blocks.GOLD_BLOCK) {
                b0 = 6;
            }

            if (block == Blocks.WOOL) {
                b0 = 7;
            }

            if (block == Blocks.PACKED_ICE) {
                b0 = 8;
            }

            if (block == Blocks.BONE_BLOCK) {
                b0 = 9;
            }

            // CraftBukkit start
            org.bukkit.event.block.NotePlayEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callNotePlayEvent(this.world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), b0, this.note);
            if (!event.isCancelled()) {
                world.addBlockEvent(blockposition, Blocks.NOTEBLOCK, event.getInstrument().getType(), event.getNote().getId());
            }
            // CraftBukkit end
        }
    }
}
