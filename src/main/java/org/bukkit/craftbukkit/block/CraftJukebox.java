package org.bukkit.craftbukkit.block;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockEntityState<TileEntityJukebox> implements Jukebox {

    public CraftJukebox(final Block block) {
        super(block, TileEntityJukebox.class);
    }

    public CraftJukebox(final Material material, TileEntityJukebox te) {
        super(material, te);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced() && this.getType() == Material.JUKEBOX) {
            CraftWorld world = (CraftWorld) this.getWorld();
            Material record = this.getPlaying();
            if (record == Material.AIR) {
                world.getHandle().func_180501_a(new BlockPos(this.getX(), this.getY(), this.getZ()),
                    Blocks.field_150421_aI.func_176223_P()
                        .func_177226_a(BlockJukebox.field_176432_a, false), 3);
            } else {
                world.getHandle().func_180501_a(new BlockPos(this.getX(), this.getY(), this.getZ()),
                    Blocks.field_150421_aI.func_176223_P()
                        .func_177226_a(BlockJukebox.field_176432_a, true), 3);
            }
            world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record.getId());
        }

        return result;
    }

    @Override
    public Material getPlaying() {
        ItemStack record = this.getSnapshot().func_145856_a();
        if (record.func_190926_b()) {
            return Material.AIR;
        }
        return CraftMagicNumbers.getMaterial(record.func_77973_b());
    }

    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
        }

        this.getSnapshot().func_145857_a(new ItemStack(CraftMagicNumbers.getItem(record), 1));
        if (record == Material.AIR) {
            setRawData((byte) 0);
        } else {
            setRawData((byte) 1);
        }
    }

    @Override
    public boolean isPlaying() {
        return getRawData() == 1;
    }

    @Override
    public boolean eject() {
        requirePlaced();
        TileEntity tileEntity = this.getTileEntityFromWorld();
        if (!(tileEntity instanceof TileEntityJukebox)) return false;

        TileEntityJukebox jukebox = (TileEntityJukebox) tileEntity;
        boolean result = !jukebox.func_145856_a().func_190926_b();
        CraftWorld world = (CraftWorld) this.getWorld();
        ((BlockJukebox) Blocks.field_150421_aI).func_180678_e(world.getHandle(), new BlockPos(getX(), getY(), getZ()), null);
        return result;
    }
}
