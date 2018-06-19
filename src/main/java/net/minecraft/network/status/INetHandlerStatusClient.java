package net.minecraft.network.status;
import net.minecraft.network.INetHandler;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;


public interface INetHandlerStatusClient extends INetHandler {

    void func_147397_a(SPacketServerInfo packetstatusoutserverinfo);

    void func_147398_a(SPacketPong packetstatusoutpong);
}
