package net.minecraft.network;


import co.aikar.timings.MinecraftTimings;
import co.aikar.timings.Timing;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {

    // Paper start, fix decompile and add timings
    public static <T extends INetHandler> void func_180031_a(final Packet<T> packet, final T listener, IThreadListener iasynctaskhandler) throws ThreadQuickExitException {
        if (!iasynctaskhandler.func_152345_ab()) {
            Timing timing = MinecraftTimings.getPacketTiming(packet);
            iasynctaskhandler.func_152344_a(() -> {
                try (Timing ignored = timing.startTiming()) {
                    packet.func_148833_a(listener);
                }
            });
            throw ThreadQuickExitException.field_179886_a;
        }
    }
    // Paper end
}
