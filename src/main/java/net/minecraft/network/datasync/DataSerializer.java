package net.minecraft.network.datasync;
import net.minecraft.network.PacketBuffer;


public interface DataSerializer<T> {

    void write(PacketBuffer packetdataserializer, T t0);

    T read(PacketBuffer packetdataserializer);

    DataParameter<T> createKey(int i);

    T copyValue(T t0);
}
