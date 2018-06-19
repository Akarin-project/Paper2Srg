package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.EntityLlama;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventoryLlama;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.LlamaInventory;

import org.bukkit.entity.Llama.Color;

public class CraftLlama extends CraftChestedHorse implements Llama {

    public CraftLlama(CraftServer server, EntityLlama entity) {
        super(server, entity);
    }

    @Override
    public EntityLlama getHandle() {
        return (EntityLlama) super.getHandle();
    }

    @Override
    public Color getColor() {
        return Color.values()[getHandle().func_190719_dM()];
    }

    @Override
    public void setColor(Color color) {
        Preconditions.checkArgument(color != null, "color");

        getHandle().func_190710_o(color.ordinal());
    }

    @Override
    public LlamaInventory getInventory() {
        return new CraftInventoryLlama(getHandle().field_110296_bG);
    }

    @Override
    public int getStrength() {
       return getHandle().func_190707_dL();
    }

    @Override
    public void setStrength(int strength) {
        Preconditions.checkArgument(1 <= strength && strength <= 5, "strength must be [1,5]");
        if (strength == getStrength()) return;
        getHandle().func_190706_p(strength);
        getHandle().func_110226_cD();
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.LLAMA;
    }

    @Override
    public String toString() {
        return "CraftLlama";
    }

    @Override
    public EntityType getType() {
        return EntityType.LLAMA;
    }
}
