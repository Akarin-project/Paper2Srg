package net.minecraft.network.status;
import net.minecraft.network.INetHandler;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;


public interface INetHandlerStatusServer extends INetHandler {

    void processPing(CPacketPing packetstatusinping);

    void processServerQuery(CPacketServerQuery packetstatusinstart);
}
