package net.minecraft.potion;

public class PotionHealth extends Potion {

    public PotionHealth(boolean flag, int i) {
        super(flag, i);
    }

    public boolean isInstant() {
        return true;
    }

    public boolean isReady(int i, int j) {
        return i >= 1;
    }
}
