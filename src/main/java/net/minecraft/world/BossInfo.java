package net.minecraft.world;

import java.util.UUID;

import net.minecraft.util.text.ITextComponent;

public abstract class BossInfo {

    private final UUID field_186756_h;
    public ITextComponent field_186749_a;
    protected float field_186750_b;
    public BossInfo.Color field_186751_c;
    public BossInfo.Overlay field_186752_d;
    protected boolean field_186753_e;
    protected boolean field_186754_f;
    protected boolean field_186755_g;

    public BossInfo(UUID uuid, ITextComponent ichatbasecomponent, BossInfo.Color bossbattle_barcolor, BossInfo.Overlay bossbattle_barstyle) {
        this.field_186756_h = uuid;
        this.field_186749_a = ichatbasecomponent;
        this.field_186751_c = bossbattle_barcolor;
        this.field_186752_d = bossbattle_barstyle;
        this.field_186750_b = 1.0F;
    }

    public UUID func_186737_d() {
        return this.field_186756_h;
    }

    public ITextComponent func_186744_e() {
        return this.field_186749_a;
    }

    public void func_186739_a(ITextComponent ichatbasecomponent) {
        this.field_186749_a = ichatbasecomponent;
    }

    public float func_186738_f() {
        return this.field_186750_b;
    }

    public void func_186735_a(float f) {
        this.field_186750_b = f;
    }

    public BossInfo.Color func_186736_g() {
        return this.field_186751_c;
    }

    public BossInfo.Overlay func_186740_h() {
        return this.field_186752_d;
    }

    public boolean func_186734_i() {
        return this.field_186753_e;
    }

    public BossInfo func_186741_a(boolean flag) {
        this.field_186753_e = flag;
        return this;
    }

    public boolean func_186747_j() {
        return this.field_186754_f;
    }

    public BossInfo func_186742_b(boolean flag) {
        this.field_186754_f = flag;
        return this;
    }

    public BossInfo func_186743_c(boolean flag) {
        this.field_186755_g = flag;
        return this;
    }

    public boolean func_186748_k() {
        return this.field_186755_g;
    }

    public static enum Overlay {

        PROGRESS, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20;

        private Overlay() {}
    }

    public static enum Color {

        PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE;

        private Color() {}
    }
}
