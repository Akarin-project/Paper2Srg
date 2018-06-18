package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.storage.MapDecoration;

public class SPacketMaps implements Packet<INetHandlerPlayClient> {

    private int mapId;
    private byte mapScale;
    private boolean trackingPosition;
    private MapDecoration[] icons;
    private int minX;
    private int minZ;
    private int columns;
    private int rows;
    private byte[] mapDataBytes;

    public SPacketMaps() {}

    public SPacketMaps(int i, byte b0, boolean flag, Collection<MapDecoration> collection, byte[] abyte, int j, int k, int l, int i1) {
        this.mapId = i;
        this.mapScale = b0;
        this.trackingPosition = flag;
        this.icons = (MapDecoration[]) collection.toArray(new MapDecoration[collection.size()]);
        this.minX = j;
        this.minZ = k;
        this.columns = l;
        this.rows = i1;
        this.mapDataBytes = new byte[l * i1];

        for (int j1 = 0; j1 < l; ++j1) {
            for (int k1 = 0; k1 < i1; ++k1) {
                this.mapDataBytes[j1 + k1 * l] = abyte[j + j1 + (k + k1) * 128];
            }
        }

    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.mapId = packetdataserializer.readVarInt();
        this.mapScale = packetdataserializer.readByte();
        this.trackingPosition = packetdataserializer.readBoolean();
        this.icons = new MapDecoration[packetdataserializer.readVarInt()];

        for (int i = 0; i < this.icons.length; ++i) {
            short short0 = (short) packetdataserializer.readByte();

            this.icons[i] = new MapDecoration(MapDecoration.Type.byIcon((byte) (short0 >> 4 & 15)), packetdataserializer.readByte(), packetdataserializer.readByte(), (byte) (short0 & 15));
        }

        this.columns = packetdataserializer.readUnsignedByte();
        if (this.columns > 0) {
            this.rows = packetdataserializer.readUnsignedByte();
            this.minX = packetdataserializer.readUnsignedByte();
            this.minZ = packetdataserializer.readUnsignedByte();
            this.mapDataBytes = packetdataserializer.readByteArray();
        }

    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeVarInt(this.mapId);
        packetdataserializer.writeByte(this.mapScale);
        packetdataserializer.writeBoolean(this.trackingPosition);
        packetdataserializer.writeVarInt(this.icons.length);
        MapDecoration[] amapicon = this.icons;
        int i = amapicon.length;

        for (int j = 0; j < i; ++j) {
            MapDecoration mapicon = amapicon[j];

            packetdataserializer.writeByte((mapicon.getImage() & 15) << 4 | mapicon.getRotation() & 15);
            packetdataserializer.writeByte(mapicon.getX());
            packetdataserializer.writeByte(mapicon.getY());
        }

        packetdataserializer.writeByte(this.columns);
        if (this.columns > 0) {
            packetdataserializer.writeByte(this.rows);
            packetdataserializer.writeByte(this.minX);
            packetdataserializer.writeByte(this.minZ);
            packetdataserializer.writeByteArray(this.mapDataBytes);
        }

    }

    public void processPacket(INetHandlerPlayClient packetlistenerplayout) {
        packetlistenerplayout.handleMaps(this);
    }
}
