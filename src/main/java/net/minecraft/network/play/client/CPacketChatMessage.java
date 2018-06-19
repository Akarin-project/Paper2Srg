package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketChatMessage implements Packet<INetHandlerPlayServer> {

    private String field_149440_a;

    public CPacketChatMessage() {}

    public CPacketChatMessage(String s) {
        if (s.length() > 256) {
            s = s.substring(0, 256);
        }

        this.field_149440_a = s;
    }

    public void func_148837_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_149440_a = packetdataserializer.func_150789_c(256);
    }

    public void func_148840_b(PacketBuffer packetdataserializer) throws IOException {
        packetdataserializer.func_180714_a(this.field_149440_a);
    }

    // Spigot Start
    private static final java.util.concurrent.ExecutorService executors = java.util.concurrent.Executors.newCachedThreadPool(
            new com.google.common.util.concurrent.ThreadFactoryBuilder().setDaemon( true ).setNameFormat( "Async Chat Thread - #%d" ).build() );
    public void func_148833_a(final INetHandlerPlayServer packetlistenerplayin) {
        if ( !field_149440_a.startsWith("/") )
        {
            executors.submit( new Runnable()
            {

                @Override
                public void run()
                {
                    packetlistenerplayin.func_147354_a( CPacketChatMessage.this );
                }
            } );
            return;
        }
        // Spigot End
        packetlistenerplayin.func_147354_a(this);
    }

    public String func_149439_c() {
        return this.field_149440_a;
    }
}
