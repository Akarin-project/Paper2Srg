package net.minecraft.entity.boss.dragon.phase;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import net.minecraft.entity.boss.EntityDragon;

public class PhaseList<T extends IPhase> {

    private static PhaseList<?>[] phases = new PhaseList[0];
    public static final PhaseList<PhaseHoldingPattern> HOLDING_PATTERN = create(PhaseHoldingPattern.class, "HoldingPattern");
    public static final PhaseList<PhaseStrafePlayer> STRAFE_PLAYER = create(PhaseStrafePlayer.class, "StrafePlayer");
    public static final PhaseList<PhaseLandingApproach> LANDING_APPROACH = create(PhaseLandingApproach.class, "LandingApproach");
    public static final PhaseList<PhaseLanding> LANDING = create(PhaseLanding.class, "Landing");
    public static final PhaseList<PhaseTakeoff> TAKEOFF = create(PhaseTakeoff.class, "Takeoff");
    public static final PhaseList<PhaseSittingFlaming> SITTING_FLAMING = create(PhaseSittingFlaming.class, "SittingFlaming");
    public static final PhaseList<PhaseSittingScanning> SITTING_SCANNING = create(PhaseSittingScanning.class, "SittingScanning");
    public static final PhaseList<PhaseSittingAttacking> SITTING_ATTACKING = create(PhaseSittingAttacking.class, "SittingAttacking");
    public static final PhaseList<PhaseChargingPlayer> CHARGING_PLAYER = create(PhaseChargingPlayer.class, "ChargingPlayer");
    public static final PhaseList<PhaseDying> DYING = create(PhaseDying.class, "Dying");
    public static final PhaseList<PhaseHover> HOVER = create(PhaseHover.class, "Hover");
    private final Class<? extends IPhase> clazz;
    private final int id;
    private final String name;

    private PhaseList(int i, Class<? extends IPhase> oclass, String s) {
        this.id = i;
        this.clazz = oclass;
        this.name = s;
    }

    public IPhase createPhase(EntityDragon entityenderdragon) {
        try {
            Constructor constructor = this.getConstructor();

            return (IPhase) constructor.newInstance(new Object[] { entityenderdragon});
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }

    protected Constructor<? extends IPhase> getConstructor() throws NoSuchMethodException {
        return this.clazz.getConstructor(new Class[] { EntityDragon.class});
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.name + " (#" + this.id + ")";
    }

    public static PhaseList<?> getById(int i) {
        return i >= 0 && i < PhaseList.phases.length ? PhaseList.phases[i] : PhaseList.HOLDING_PATTERN;
    }

    public static int getTotalPhases() {
        return PhaseList.phases.length;
    }

    private static <T extends IPhase> PhaseList<T> create(Class<T> oclass, String s) {
        PhaseList dragoncontrollerphase = new PhaseList(PhaseList.phases.length, oclass, s);

        PhaseList.phases = (PhaseList[]) Arrays.copyOf(PhaseList.phases, PhaseList.phases.length + 1);
        PhaseList.phases[dragoncontrollerphase.getId()] = dragoncontrollerphase;
        return dragoncontrollerphase;
    }
}
