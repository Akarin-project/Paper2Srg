package net.minecraft.network;


import co.aikar.timings.MinecraftTimings;
import co.aikar.timings.Timing;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {

    // Paper start, fix decompile and add timings
    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packet, final T listener, IThreadListener iasynctaskhandler) throws ThreadQuickExitException {
        if (!iasynctaskhandler.isCallingFromMinecraftThread()) {
            Timing timing = MinecraftTimings.getPacketTiming(packet);
            iasynctaskhandler.addScheduledTask(() -> {
                try (Timing ignored = timing.startTiming()) {
                    packet.processPacket(listener);
                }
            });
            throw ThreadQuickExitException.INSTANCE;
        }
    }
    // Paper end
}
