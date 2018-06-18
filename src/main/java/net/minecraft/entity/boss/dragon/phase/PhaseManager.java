package net.minecraft.entity.boss.dragon.phase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.boss.EntityDragon;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
// CraftBukkit end

public class PhaseManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityDragon dragon;
    private final IPhase[] phases = new IPhase[PhaseList.getTotalPhases()];
    private IPhase phase;

    public PhaseManager(EntityDragon entityenderdragon) {
        this.dragon = entityenderdragon;
        this.setPhase(PhaseList.HOVER);
    }

    public void setPhase(PhaseList<?> dragoncontrollerphase) {
        if (this.phase == null || dragoncontrollerphase != this.phase.getType()) {
            if (this.phase != null) {
                this.phase.removeAreaEffect();
            }

            // CraftBukkit start - Call EnderDragonChangePhaseEvent
            EnderDragonChangePhaseEvent event = new EnderDragonChangePhaseEvent(
                    (CraftEnderDragon) this.dragon.getBukkitEntity(),
                    (this.phase == null) ? null : CraftEnderDragon.getBukkitPhase(this.phase.getType()),
                    CraftEnderDragon.getBukkitPhase(dragoncontrollerphase)
            );
            this.dragon.world.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            dragoncontrollerphase = CraftEnderDragon.getMinecraftPhase(event.getNewPhase());
            // CraftBukkit end

            this.phase = this.getPhase(dragoncontrollerphase);
            if (!this.dragon.world.isRemote) {
                this.dragon.getDataManager().set(EntityDragon.PHASE, Integer.valueOf(dragoncontrollerphase.getId()));
            }

            PhaseManager.LOGGER.debug("Dragon is now in phase {} on the {}", dragoncontrollerphase, this.dragon.world.isRemote ? "client" : "server");
            this.phase.initPhase();
        }
    }

    public IPhase getCurrentPhase() {
        return this.phase;
    }

    public <T extends IPhase> T getPhase(PhaseList<T> dragoncontrollerphase) {
        int i = dragoncontrollerphase.getId();

        if (this.phases[i] == null) {
            this.phases[i] = dragoncontrollerphase.createPhase(this.dragon);
        }

        return (T) this.phases[i]; // CraftBukkit - decompile error
    }
}
