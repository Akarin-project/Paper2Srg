package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;

public class SPacketUpdateBossInfo implements Packet<INetHandlerPlayClient> {

    private UUID uniqueId;
    private SPacketUpdateBossInfo.Operation operation;
    private ITextComponent name;
    private float percent;
    private BossInfo.Color color;
    private BossInfo.Overlay overlay;
    private boolean darkenSky;
    private boolean playEndBossMusic;
    private boolean createFog;

    public SPacketUpdateBossInfo() {}

    public SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation packetplayoutboss_action, BossInfo bossbattle) {
        this.operation = packetplayoutboss_action;
        this.uniqueId = bossbattle.getUniqueId();
        this.name = bossbattle.getName();
        this.percent = bossbattle.getPercent();
        this.color = bossbattle.getColor();
        this.overlay = bossbattle.getOverlay();
        this.darkenSky = bossbattle.shouldDarkenSky();
        this.playEndBossMusic = bossbattle.shouldPlayEndBossMusic();
        this.createFog = bossbattle.shouldCreateFog();
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.uniqueId = packetdataserializer.readUniqueId();
        this.operation = (SPacketUpdateBossInfo.Operation) packetdataserializer.readEnumValue(SPacketUpdateBossInfo.Operation.class);
        switch (this.operation) {
        case ADD:
            this.name = packetdataserializer.readTextComponent();
            this.percent = packetdataserializer.readFloat();
            this.color = (BossInfo.Color) packetdataserializer.readEnumValue(BossInfo.Color.class);
            this.overlay = (BossInfo.Overlay) packetdataserializer.readEnumValue(BossInfo.Overlay.class);
            this.setFlags(packetdataserializer.readUnsignedByte());

        case REMOVE:
        default:
            break;

        case UPDATE_PCT:
            this.percent = packetdataserializer.readFloat();
            break;

        case UPDATE_NAME:
            this.name = packetdataserializer.readTextComponent();
            break;

        case UPDATE_STYLE:
            this.color = (BossInfo.Color) packetdataserializer.readEnumValue(BossInfo.Color.class);
            this.overlay = (BossInfo.Overlay) packetdataserializer.readEnumValue(BossInfo.Overlay.class);
            break;

        case UPDATE_PROPERTIES:
            this.setFlags(packetdataserializer.readUnsignedByte());
        }

    }

    private void setFlags(int i) {
        this.darkenSky = (i & 1) > 0;
        this.playEndBossMusic = (i & 2) > 0;
        this.createFog = (i & 2) > 0;
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeUniqueId(this.uniqueId);
        packetdataserializer.writeEnumValue((Enum) this.operation);
        switch (this.operation) {
        case ADD:
            packetdataserializer.writeTextComponent(this.name);
            packetdataserializer.writeFloat(this.percent);
            packetdataserializer.writeEnumValue((Enum) this.color);
            packetdataserializer.writeEnumValue((Enum) this.overlay);
            packetdataserializer.writeByte(this.getFlags());

        case REMOVE:
        default:
            break;

        case UPDATE_PCT:
            packetdataserializer.writeFloat(this.percent);
            break;

        case UPDATE_NAME:
            packetdataserializer.writeTextComponent(this.name);
            break;

        case UPDATE_STYLE:
            packetdataserializer.writeEnumValue((Enum) this.color);
            packetdataserializer.writeEnumValue((Enum) this.overlay);
            break;

        case UPDATE_PROPERTIES:
            packetdataserializer.writeByte(this.getFlags());
        }

    }

    private int getFlags() {
        int i = 0;

        if (this.darkenSky) {
            i |= 1;
        }

        if (this.playEndBossMusic) {
            i |= 2;
        }

        if (this.createFog) {
            i |= 2;
        }

        return i;
    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleUpdateBossInfo(this);
    }

    public static enum Operation {

        ADD, REMOVE, UPDATE_PCT, UPDATE_NAME, UPDATE_STYLE, UPDATE_PROPERTIES;

        private Operation() {}
    }
}
