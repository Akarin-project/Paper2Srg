package net.minecraft.network.login;
import net.minecraft.network.INetHandler;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;


public interface INetHandlerLoginClient extends INetHandler {

    void func_147389_a(SPacketEncryptionRequest packetloginoutencryptionbegin);

    void func_147390_a(SPacketLoginSuccess packetloginoutsuccess);

    void func_147388_a(SPacketDisconnect packetloginoutdisconnect);

    void func_180464_a(SPacketEnableCompression packetloginoutsetcompression);
}
