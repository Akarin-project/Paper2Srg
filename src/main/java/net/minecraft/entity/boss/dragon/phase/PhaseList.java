package net.minecraft.entity.boss.dragon.phase;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import net.minecraft.entity.boss.EntityDragon;

public class PhaseList<T extends IPhase> {

    private static PhaseList<?>[] field_188752_l = new PhaseList[0];
    public static final PhaseList<PhaseHoldingPattern> field_188741_a = func_188735_a(PhaseHoldingPattern.class, "HoldingPattern");
    public static final PhaseList<PhaseStrafePlayer> field_188742_b = func_188735_a(PhaseStrafePlayer.class, "StrafePlayer");
    public static final PhaseList<PhaseLandingApproach> field_188743_c = func_188735_a(PhaseLandingApproach.class, "LandingApproach");
    public static final PhaseList<PhaseLanding> field_188744_d = func_188735_a(PhaseLanding.class, "Landing");
    public static final PhaseList<PhaseTakeoff> field_188745_e = func_188735_a(PhaseTakeoff.class, "Takeoff");
    public static final PhaseList<PhaseSittingFlaming> field_188746_f = func_188735_a(PhaseSittingFlaming.class, "SittingFlaming");
    public static final PhaseList<PhaseSittingScanning> field_188747_g = func_188735_a(PhaseSittingScanning.class, "SittingScanning");
    public static final PhaseList<PhaseSittingAttacking> field_188748_h = func_188735_a(PhaseSittingAttacking.class, "SittingAttacking");
    public static final PhaseList<PhaseChargingPlayer> field_188749_i = func_188735_a(PhaseChargingPlayer.class, "ChargingPlayer");
    public static final PhaseList<PhaseDying> field_188750_j = func_188735_a(PhaseDying.class, "Dying");
    public static final PhaseList<PhaseHover> field_188751_k = func_188735_a(PhaseHover.class, "Hover");
    private final Class<? extends IPhase> field_188753_m;
    private final int field_188754_n;
    private final String field_188755_o;

    private PhaseList(int i, Class<? extends IPhase> oclass, String s) {
        this.field_188754_n = i;
        this.field_188753_m = oclass;
        this.field_188755_o = s;
    }

    public IPhase func_188736_a(EntityDragon entityenderdragon) {
        try {
            Constructor constructor = this.func_188737_a();

            return (IPhase) constructor.newInstance(new Object[] { entityenderdragon});
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }

    protected Constructor<? extends IPhase> func_188737_a() throws NoSuchMethodException {
        return this.field_188753_m.getConstructor(new Class[] { EntityDragon.class});
    }

    public int func_188740_b() {
        return this.field_188754_n;
    }

    public String toString() {
        return this.field_188755_o + " (#" + this.field_188754_n + ")";
    }

    public static PhaseList<?> func_188738_a(int i) {
        return i >= 0 && i < PhaseList.field_188752_l.length ? PhaseList.field_188752_l[i] : PhaseList.field_188741_a;
    }

    public static int func_188739_c() {
        return PhaseList.field_188752_l.length;
    }

    private static <T extends IPhase> PhaseList<T> func_188735_a(Class<T> oclass, String s) {
        PhaseList dragoncontrollerphase = new PhaseList(PhaseList.field_188752_l.length, oclass, s);

        PhaseList.field_188752_l = (PhaseList[]) Arrays.copyOf(PhaseList.field_188752_l, PhaseList.field_188752_l.length + 1);
        PhaseList.field_188752_l[dragoncontrollerphase.func_188740_b()] = dragoncontrollerphase;
        return dragoncontrollerphase;
    }
}
