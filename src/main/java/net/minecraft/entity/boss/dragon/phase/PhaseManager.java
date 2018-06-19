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

    private static final Logger field_188759_a = LogManager.getLogger();
    private final EntityDragon field_188760_b;
    private final IPhase[] field_188761_c = new IPhase[PhaseList.func_188739_c()];
    private IPhase field_188762_d;

    public PhaseManager(EntityDragon entityenderdragon) {
        this.field_188760_b = entityenderdragon;
        this.func_188758_a(PhaseList.field_188751_k);
    }

    public void func_188758_a(PhaseList<?> dragoncontrollerphase) {
        if (this.field_188762_d == null || dragoncontrollerphase != this.field_188762_d.func_188652_i()) {
            if (this.field_188762_d != null) {
                this.field_188762_d.func_188658_e();
            }

            // CraftBukkit start - Call EnderDragonChangePhaseEvent
            EnderDragonChangePhaseEvent event = new EnderDragonChangePhaseEvent(
                    (CraftEnderDragon) this.field_188760_b.getBukkitEntity(),
                    (this.field_188762_d == null) ? null : CraftEnderDragon.getBukkitPhase(this.field_188762_d.func_188652_i()),
                    CraftEnderDragon.getBukkitPhase(dragoncontrollerphase)
            );
            this.field_188760_b.field_70170_p.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            dragoncontrollerphase = CraftEnderDragon.getMinecraftPhase(event.getNewPhase());
            // CraftBukkit end

            this.field_188762_d = this.func_188757_b(dragoncontrollerphase);
            if (!this.field_188760_b.field_70170_p.field_72995_K) {
                this.field_188760_b.func_184212_Q().func_187227_b(EntityDragon.field_184674_a, Integer.valueOf(dragoncontrollerphase.func_188740_b()));
            }

            PhaseManager.field_188759_a.debug("Dragon is now in phase {} on the {}", dragoncontrollerphase, this.field_188760_b.field_70170_p.field_72995_K ? "client" : "server");
            this.field_188762_d.func_188660_d();
        }
    }

    public IPhase func_188756_a() {
        return this.field_188762_d;
    }

    public <T extends IPhase> T func_188757_b(PhaseList<T> dragoncontrollerphase) {
        int i = dragoncontrollerphase.func_188740_b();

        if (this.field_188761_c[i] == null) {
            this.field_188761_c[i] = dragoncontrollerphase.func_188736_a(this.field_188760_b);
        }

        return (T) this.field_188761_c[i]; // CraftBukkit - decompile error
    }
}
