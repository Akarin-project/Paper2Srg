package org.bukkit.craftbukkit.entity;

import net.minecraft.init.Blocks;
import net.minecraft.entity.item.EntityMinecart;

import net.minecraft.block.state.IBlockState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public abstract class CraftMinecart extends CraftVehicle implements Minecart {
    public CraftMinecart(CraftServer server, EntityMinecart entity) {
        super(server, entity);
    }

    public void setDamage(double damage) {
        getHandle().func_70492_c((float) damage);
    }

    public double getDamage() {
        return getHandle().func_70491_i();
    }

    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    public boolean isSlowWhenEmpty() {
        return getHandle().slowWhenEmpty;
    }

    public void setSlowWhenEmpty(boolean slow) {
        getHandle().slowWhenEmpty = slow;
    }

    public Vector getFlyingVelocityMod() {
        return getHandle().getFlyingVelocityMod();
    }

    public void setFlyingVelocityMod(Vector flying) {
        getHandle().setFlyingVelocityMod(flying);
    }

    public Vector getDerailedVelocityMod() {
        return getHandle().getDerailedVelocityMod();
    }

    public void setDerailedVelocityMod(Vector derailed) {
        getHandle().setDerailedVelocityMod(derailed);
    }

    @Override
    public EntityMinecart getHandle() {
        return (EntityMinecart) entity;
    }

    public void setDisplayBlock(MaterialData material) {
        if(material != null) {
            IBlockState block = CraftMagicNumbers.getBlock(material.getItemTypeId()).func_176203_a(material.getData());
            this.getHandle().func_174899_a(block);
        } else {
            // Set block to air (default) and set the flag to not have a display block.
            this.getHandle().func_174899_a(Blocks.field_150350_a.func_176223_P());
            this.getHandle().func_94096_e(false);
        }
    }

    public MaterialData getDisplayBlock() {
        IBlockState blockData = getHandle().func_174897_t();
        return CraftMagicNumbers.getMaterial(blockData.func_177230_c()).getNewData((byte) blockData.func_177230_c().func_176201_c(blockData));
    }

    public void setDisplayBlockOffset(int offset) {
        getHandle().func_94086_l(offset);
    }

    public int getDisplayBlockOffset() {
        return getHandle().func_94099_q();
    }
}
