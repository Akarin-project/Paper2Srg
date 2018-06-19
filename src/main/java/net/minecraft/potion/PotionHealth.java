package net.minecraft.potion;

public class PotionHealth extends Potion {

    public PotionHealth(boolean flag, int i) {
        super(flag, i);
    }

    public boolean func_76403_b() {
        return true;
    }

    public boolean func_76397_a(int i, int j) {
        return i >= 1;
    }
}
