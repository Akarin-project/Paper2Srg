package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlayerAbilities implements Packet<INetHandlerPlayServer> {

    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;

    public CPacketPlayerAbilities() {}

    public CPacketPlayerAbilities(PlayerCapabilities playerabilities) {
        this.setInvulnerable(playerabilities.disableDamage);
        this.setFlying(playerabilities.isFlying);
        this.setAllowFlying(playerabilities.allowFlying);
        this.setCreativeMode(playerabilities.isCreativeMode);
        this.setFlySpeed(playerabilities.getFlySpeed());
        this.setWalkSpeed(playerabilities.getWalkSpeed());
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        byte b0 = packetdataserializer.readByte();

        this.setInvulnerable((b0 & 1) > 0);
        this.setFlying((b0 & 2) > 0);
        this.setAllowFlying((b0 & 4) > 0);
        this.setCreativeMode((b0 & 8) > 0);
        this.setFlySpeed(packetdataserializer.readFloat());
        this.setWalkSpeed(packetdataserializer.readFloat());
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        byte b0 = 0;

        if (this.isInvulnerable()) {
            b0 = (byte) (b0 | 1);
        }

        if (this.isFlying()) {
            b0 = (byte) (b0 | 2);
        }

        if (this.isAllowFlying()) {
            b0 = (byte) (b0 | 4);
        }

        if (this.isCreativeMode()) {
            b0 = (byte) (b0 | 8);
        }

        packetdataserializer.writeByte(b0);
        packetdataserializer.writeFloat(this.flySpeed);
        packetdataserializer.writeFloat(this.walkSpeed);
    }

    public void processPacket(INetHandlerPlayServer packetlistenerplayin) {
        packetlistenerplayin.processPlayerAbilities(this);
    }

    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean flag) {
        this.invulnerable = flag;
    }

    public boolean isFlying() {
        return this.flying;
    }

    public void setFlying(boolean flag) {
        this.flying = flag;
    }

    public boolean isAllowFlying() {
        return this.allowFlying;
    }

    public void setAllowFlying(boolean flag) {
        this.allowFlying = flag;
    }

    public boolean isCreativeMode() {
        return this.creativeMode;
    }

    public void setCreativeMode(boolean flag) {
        this.creativeMode = flag;
    }

    public void setFlySpeed(float f) {
        this.flySpeed = f;
    }

    public void setWalkSpeed(float f) {
        this.walkSpeed = f;
    }
}
