package net.minecraft.network.datasync;
import net.minecraft.network.PacketBuffer;


public interface DataSerializer<T> {

    void func_187160_a(PacketBuffer packetdataserializer, T t0);

    T func_187159_a(PacketBuffer packetdataserializer);

    DataParameter<T> func_187161_a(int i);

    T func_192717_a(T t0);
}
