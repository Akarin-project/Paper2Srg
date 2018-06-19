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

    public byte field_145879_a;
    public boolean field_145880_i;

    public TileEntityNote() {}

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74774_a("note", this.field_145879_a);
        nbttagcompound.func_74757_a("powered", this.field_145880_i);
        return nbttagcompound;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_145879_a = nbttagcompound.func_74771_c("note");
        this.field_145879_a = (byte) MathHelper.func_76125_a(this.field_145879_a, 0, 24);
        this.field_145880_i = nbttagcompound.func_74767_n("powered");
    }

    public void func_145877_a() {
        this.field_145879_a = (byte) ((this.field_145879_a + 1) % 25);
        this.func_70296_d();
    }

    public void func_175108_a(World world, BlockPos blockposition) {
        if (world.func_180495_p(blockposition.func_177984_a()).func_185904_a() == Material.field_151579_a) {
            IBlockState iblockdata = world.func_180495_p(blockposition.func_177977_b());
            Material material = iblockdata.func_185904_a();
            byte b0 = 0;

            if (material == Material.field_151576_e) {
                b0 = 1;
            }

            if (material == Material.field_151595_p) {
                b0 = 2;
            }

            if (material == Material.field_151592_s) {
                b0 = 3;
            }

            if (material == Material.field_151575_d) {
                b0 = 4;
            }

            Block block = iblockdata.func_177230_c();

            if (block == Blocks.field_150435_aG) {
                b0 = 5;
            }

            if (block == Blocks.field_150340_R) {
                b0 = 6;
            }

            if (block == Blocks.field_150325_L) {
                b0 = 7;
            }

            if (block == Blocks.field_150403_cj) {
                b0 = 8;
            }

            if (block == Blocks.field_189880_di) {
                b0 = 9;
            }

            // CraftBukkit start
            org.bukkit.event.block.NotePlayEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callNotePlayEvent(this.field_145850_b, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), b0, this.field_145879_a);
            if (!event.isCancelled()) {
                world.func_175641_c(blockposition, Blocks.field_150323_B, event.getInstrument().getType(), event.getNote().getId());
            }
            // CraftBukkit end
        }
    }
}
