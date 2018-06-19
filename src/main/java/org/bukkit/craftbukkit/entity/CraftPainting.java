package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.world.WorldServer;

import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, EntityPainting entity) {
        super(server, entity);
    }

    public Art getArt() {
        EnumArt art = getHandle().field_70522_e;
        return CraftArt.NotchToBukkit(art);
    }

    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    public boolean setArt(Art art, boolean force) {
        EntityPainting painting = this.getHandle();
        EnumArt oldArt = painting.field_70522_e;
        painting.field_70522_e = CraftArt.BukkitToNotch(art);
        painting.func_174859_a(painting.field_174860_b);
        if (!force && !painting.func_70518_d()) {
            // Revert painting since it doesn't fit
            painting.field_70522_e = oldArt;
            painting.func_174859_a(painting.field_174860_b);
            return false;
        }
        this.update();
        return true;
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            update();
            return true;
        }

        return false;
    }

    private void update() {
        WorldServer world = ((CraftWorld) getWorld()).getHandle();
        EntityPainting painting = new EntityPainting(world);
        painting.field_174861_a = getHandle().field_174861_a;
        painting.field_70522_e = getHandle().field_70522_e;
        painting.func_174859_a(getHandle().field_174860_b);
        getHandle().func_70106_y();
        getHandle().field_70133_I = true; // because this occurs when the painting is broken, so it might be important
        world.func_72838_d(painting);
        this.entity = painting;
    }

    @Override
    public EntityPainting getHandle() {
        return (EntityPainting) entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + getArt() + "}";
    }

    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
