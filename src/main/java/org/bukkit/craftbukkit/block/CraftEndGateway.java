package org.bukkit.craftbukkit.block;

import java.util.Objects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntityEndGateway;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;

public class CraftEndGateway extends CraftBlockEntityState<TileEntityEndGateway> implements EndGateway {

    public CraftEndGateway(Block block) {
        super(block, TileEntityEndGateway.class);
    }

    public CraftEndGateway(final Material material, TileEntityEndGateway te) {
        super(material, te);
    }

    @Override
    public Location getExitLocation() {
        BlockPos pos = this.getSnapshot().field_184317_h;
        return pos == null ? null : new Location(this.isPlaced() ? this.getWorld() : null, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
    }

    @Override
    public void setExitLocation(Location location) {
        if (location == null) {
            this.getSnapshot().field_184317_h = null;
        } else if (!Objects.equals(location.getWorld(), this.isPlaced() ? this.getWorld() : null)) {
            throw new IllegalArgumentException("Cannot set exit location to different world");
        } else {
            this.getSnapshot().field_184317_h = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }
    }

    @Override
    public boolean isExactTeleport() {
        return this.getSnapshot().field_184318_i;
    }

    @Override
    public void setExactTeleport(boolean exact) {
        this.getSnapshot().field_184318_i = exact;
    }

    @Override
    public void applyTo(TileEntityEndGateway endGateway) {
        super.applyTo(endGateway);

        if (this.getSnapshot().field_184317_h == null) {
            endGateway.field_184317_h = null;
        }
    }
}
