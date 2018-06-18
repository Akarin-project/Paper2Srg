package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketChatMessage implements Packet<INetHandlerPlayServer> {

    private String message;

    public CPacketChatMessage() {}

    public CPacketChatMessage(String s) {
        if (s.length() > 256) {
            s = s.substring(0, 256);
        }

        this.message = s;
    }

    public void readPacketData(PacketBuffer packetdataserializer) throws IOException {
        this.message = packetdataserializer.readString(256);
    }

    public void writePacketData(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.writeString(this.message);
    }

    // Spigot Start
    private static final java.util.concurrent.ExecutorService executors = java.util.concurrent.Executors.newCachedThreadPool(
            new com.google.common.util.concurrent.ThreadFactoryBuilder().setDaemon( true ).setNameFormat( "Async Chat Thread - #%d" ).build() );
    public void processPacket(final INetHandlerPlayServer packetlistenerplayin) {
        if ( !message.startsWith("/") )
        {
            executors.submit( new Runnable()
            {

                @Override
                public void run()
                {
                    packetlistenerplayin.processChatMessage( CPacketChatMessage.this );
                }
            } );
            return;
        }
        // Spigot End
        packetlistenerplayin.processChatMessage(this);
    }

    public String getMessage() {
        return this.message;
    }
}
