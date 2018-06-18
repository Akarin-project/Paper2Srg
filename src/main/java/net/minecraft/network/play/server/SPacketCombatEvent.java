package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class SPacketCombatEvent implements Packet<INetHandlerPlayClient> {

    public SPacketCombatEvent.Event eventType;
    public int playerId;
    public int entityId;
    public int duration;
    public ITextComponent deathMessage;

    public SPacketCombatEvent() {}

    public SPacketCombatEvent(CombatTracker combattracker, SPacketCombatEvent.Event packetplayoutcombatevent_enumcombateventtype) {
        this(combattracker, packetplayoutcombatevent_enumcombateventtype, true);
    }

    public SPacketCombatEvent(CombatTracker combattracker, SPacketCombatEvent.Event packetplayoutcombatevent_enumcombateventtype, boolean flag) {
        this.eventType = packetplayoutcombatevent_enumcombateventtype;
        EntityLivingBase entityliving = combattracker.getBestAttacker();

        switch (packetplayoutcombatevent_enumcombateventtype) {
        case END_COMBAT:
            this.duration = combattracker.getCombatDuration();
            this.entityId = entityliving == null ? -1 : entityliving.getEntityId();
            break;

        case ENTITY_DIED:
            this.playerId = combattracker.getFighter().getEntityId();
            this.entityId = entityliving == null ? -1 : entityliving.getEntityId();
            if (flag) {
                this.deathMessage = combattracker.getDeathMessage();
            } else {
                this.deathMessage = new TextComponentString("");
            }
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.eventType = (SPacketCombatEvent.Event) packetdataserializer.readEnumValue(SPacketCombatEvent.Event.class);
        if (this.eventType == SPacketCombatEvent.Event.END_COMBAT) {
            this.duration = packetdataserializer.readVarInt();
            this.entityId = packetdataserializer.readInt();
        } else if (this.eventType == SPacketCombatEvent.Event.ENTITY_DIED) {
            this.playerId = packetdataserializer.readVarInt();
            this.entityId = packetdataserializer.readInt();
            this.deathMessage = packetdataserializer.readTextComponent();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeEnumValue((Enum) this.eventType);
        if (this.eventType == SPacketCombatEvent.Event.END_COMBAT) {
            packetdataserializer.writeVarInt(this.duration);
            packetdataserializer.writeInt(this.entityId);
        } else if (this.eventType == SPacketCombatEvent.Event.ENTITY_DIED) {
            packetdataserializer.writeVarInt(this.playerId);
            packetdataserializer.writeInt(this.entityId);
            packetdataserializer.writeTextComponent(this.deathMessage);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleCombatEvent(this);
    }

    public static enum Event {

        ENTER_COMBAT, END_COMBAT, ENTITY_DIED;

        private Event() {}
    }
}
