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

    public SPacketCombatEvent.Event field_179776_a;
    public int field_179774_b;
    public int field_179775_c;
    public int field_179772_d;
    public ITextComponent field_179773_e;

    public SPacketCombatEvent() {}

    public SPacketCombatEvent(CombatTracker combattracker, SPacketCombatEvent.Event packetplayoutcombatevent_enumcombateventtype) {
        this(combattracker, packetplayoutcombatevent_enumcombateventtype, true);
    }

    public SPacketCombatEvent(CombatTracker combattracker, SPacketCombatEvent.Event packetplayoutcombatevent_enumcombateventtype, boolean flag) {
        this.field_179776_a = packetplayoutcombatevent_enumcombateventtype;
        EntityLivingBase entityliving = combattracker.func_94550_c();

        switch (packetplayoutcombatevent_enumcombateventtype) {
        case END_COMBAT:
            this.field_179772_d = combattracker.func_180134_f();
            this.field_179775_c = entityliving == null ? -1 : entityliving.func_145782_y();
            break;

        case ENTITY_DIED:
            this.field_179774_b = combattracker.func_180135_h().func_145782_y();
            this.field_179775_c = entityliving == null ? -1 : entityliving.func_145782_y();
            if (flag) {
                this.field_179773_e = combattracker.func_151521_b();
            } else {
                this.field_179773_e = new TextComponentString("");
            }
        }

    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_179776_a = (SPacketCombatEvent.Event) packetdataserializer.func_179257_a(SPacketCombatEvent.Event.class);
        if (this.field_179776_a == SPacketCombatEvent.Event.END_COMBAT) {
            this.field_179772_d = packetdataserializer.func_150792_a();
            this.field_179775_c = packetdataserializer.readInt();
        } else if (this.field_179776_a == SPacketCombatEvent.Event.ENTITY_DIED) {
            this.field_179774_b = packetdataserializer.func_150792_a();
            this.field_179775_c = packetdataserializer.readInt();
            this.field_179773_e = packetdataserializer.func_179258_d();
        }

    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_179249_a((Enum) this.field_179776_a);
        if (this.field_179776_a == SPacketCombatEvent.Event.END_COMBAT) {
            packetdataserializer.func_150787_b(this.field_179772_d);
            packetdataserializer.writeInt(this.field_179775_c);
        } else if (this.field_179776_a == SPacketCombatEvent.Event.ENTITY_DIED) {
            packetdataserializer.func_150787_b(this.field_179774_b);
            packetdataserializer.writeInt(this.field_179775_c);
            packetdataserializer.func_179256_a(this.field_179773_e);
        }

    }

    public void func_148833_a(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.func_175098_a(this);
    }

    public static enum Event {

        ENTER_COMBAT, END_COMBAT, ENTITY_DIED;

        private Event() {}
    }
}
