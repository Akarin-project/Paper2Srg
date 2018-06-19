package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import java.util.Set;

import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

import org.bukkit.entity.EnderDragon.Phase;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {
    public CraftEnderDragon(CraftServer server, EntityDragon entity) {
        super(server, entity);
    }

    public Set<ComplexEntityPart> getParts() {
        Builder<ComplexEntityPart> builder = ImmutableSet.builder();

        for (MultiPartEntityPart part : getHandle().field_70977_g) {
            builder.add((ComplexEntityPart) part.getBukkitEntity());
        }

        return builder.build();
    }

    @Override
    public EntityDragon getHandle() {
        return (EntityDragon) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragon";
    }

    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public Phase getPhase() {
        return Phase.values()[getHandle().func_184212_Q().func_187225_a(EntityDragon.field_184674_a)];
    }

    @Override
    public void setPhase(Phase phase) {
        getHandle().func_184670_cT().func_188758_a(getMinecraftPhase(phase));
    }
    
    public static Phase getBukkitPhase(PhaseList phase) {
        return Phase.values()[phase.func_188740_b()];
    }
    
    public static PhaseList getMinecraftPhase(Phase phase) {
        return PhaseList.func_188738_a(phase.ordinal());
    }
}
