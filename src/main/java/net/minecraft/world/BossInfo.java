package net.minecraft.world;

import java.util.UUID;

import net.minecraft.util.text.ITextComponent;

public abstract class BossInfo {

    private final UUID uniqueId;
    public ITextComponent name;
    protected float percent;
    public BossInfo.Color color;
    public BossInfo.Overlay overlay;
    protected boolean darkenSky;
    protected boolean playEndBossMusic;
    protected boolean createFog;

    public BossInfo(UUID uuid, ITextComponent ichatbasecomponent, BossInfo.Color bossbattle_barcolor, BossInfo.Overlay bossbattle_barstyle) {
        this.uniqueId = uuid;
        this.name = ichatbasecomponent;
        this.color = bossbattle_barcolor;
        this.overlay = bossbattle_barstyle;
        this.percent = 1.0F;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public ITextComponent getName() {
        return this.name;
    }

    public void setName(ITextComponent ichatbasecomponent) {
        this.name = ichatbasecomponent;
    }

    public float getPercent() {
        return this.percent;
    }

    public void setPercent(float f) {
        this.percent = f;
    }

    public BossInfo.Color getColor() {
        return this.color;
    }

    public BossInfo.Overlay getOverlay() {
        return this.overlay;
    }

    public boolean shouldDarkenSky() {
        return this.darkenSky;
    }

    public BossInfo setDarkenSky(boolean flag) {
        this.darkenSky = flag;
        return this;
    }

    public boolean shouldPlayEndBossMusic() {
        return this.playEndBossMusic;
    }

    public BossInfo setPlayEndBossMusic(boolean flag) {
        this.playEndBossMusic = flag;
        return this;
    }

    public BossInfo setCreateFog(boolean flag) {
        this.createFog = flag;
        return this;
    }

    public boolean shouldCreateFog() {
        return this.createFog;
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
