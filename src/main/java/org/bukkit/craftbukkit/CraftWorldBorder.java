package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class CraftWorldBorder implements WorldBorder {

    private final World world;
    private final net.minecraft.world.border.WorldBorder handle;

    public CraftWorldBorder(CraftWorld world) {
        this.world = world;
        this.handle = world.getHandle().func_175723_af();
    }

    @Override
    public void reset() {
        this.setSize(6.0E7D);
        this.setDamageAmount(0.2D);
        this.setDamageBuffer(5.0D);
        this.setWarningDistance(5);
        this.setWarningTime(15);
        this.setCenter(0, 0);
    }

    @Override
    public double getSize() {
        return this.handle.func_177741_h();
    }

    @Override
    public void setSize(double newSize) {
        this.setSize(newSize, 0L);
    }

    @Override
    public void setSize(double newSize, long time) {
        // PAIL: TODO: Magic Values
        newSize = Math.min(6.0E7D, Math.max(1.0D, newSize));
        time = Math.min(9223372036854775L, Math.max(0L, time));

        if (time > 0L) {
            this.handle.func_177738_a(this.handle.func_177741_h(), newSize, time * 1000L);
        } else {
            this.handle.func_177750_a(newSize); 
        }
    }

    @Override
    public Location getCenter() {
        double x = this.handle.func_177731_f(); 
        double z = this.handle.func_177721_g(); 

        return new Location(this.world, x, 0, z);
    }

    @Override
    public void setCenter(double x, double z) {
        // PAIL: TODO: Magic Values
        x = Math.min(3.0E7D, Math.max(-3.0E7D, x));
        z = Math.min(3.0E7D, Math.max(-3.0E7D, z));

        this.handle.func_177739_c(x, z); 
    }

    @Override
    public void setCenter(Location location) {
        this.setCenter(location.getX(), location.getZ());
    }

    @Override
    public double getDamageBuffer() {
        return this.handle.func_177742_m();
    }

    @Override
    public void setDamageBuffer(double blocks) {
        this.handle.func_177724_b(blocks);
    }

    @Override
    public double getDamageAmount() {
        return this.handle.func_177727_n();
    }

    @Override
    public void setDamageAmount(double damage) {
        this.handle.func_177744_c(damage);
    }

    @Override
    public int getWarningTime() {
        return this.handle.func_177740_p();
    }

    @Override
    public void setWarningTime(int time) {
        this.handle.func_177723_b(time);
    }

    @Override
    public int getWarningDistance() {
        return this.handle.func_177748_q();
    }

    @Override
    public void setWarningDistance(int distance) {
        this.handle.func_177747_c(distance); 
    }

    @Override
    public boolean isInside(Location location) {
        Preconditions.checkArgument(location != null, "location");

        return location.getWorld().equals(this.world) && this.handle.func_177746_a(new BlockPos(location.getX(), location.getY(), location.getZ()));
    }
}
