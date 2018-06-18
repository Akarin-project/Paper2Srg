package net.minecraft.network.rcon;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;


public class RConConsoleSource implements ICommandSender {

    private final StringBuffer buffer = new StringBuffer();
    private final MinecraftServer server;

    public RConConsoleSource(MinecraftServer minecraftserver) {
        this.server = minecraftserver;
    }

    public void resetLog() {
        this.buffer.setLength(0);
    }

    public String getLogContents() {
        return this.buffer.toString();
    }

    public String getName() {
        return "Rcon";
    }

    // CraftBukkit start - Send a String
    public void sendMessage(String message) {
        this.buffer.append(message);
    }
    // CraftBukkit end

    public void sendMessage(ITextComponent ichatbasecomponent) {
        this.buffer.append(ichatbasecomponent.getUnformattedText());
    }

    public boolean canUseCommand(int i, String s) {
        return true;
    }

    public World getEntityWorld() {
        return this.server.getEntityWorld();
    }

    public boolean sendCommandFeedback() {
        return true;
    }

    public MinecraftServer getServer() {
        return this.server;
    }
}
