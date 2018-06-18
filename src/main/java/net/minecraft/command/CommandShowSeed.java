package net.minecraft.command;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;


public class CommandShowSeed extends CommandBase {

    public CommandShowSeed() {}

    public boolean checkPermission(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.isSinglePlayer() || super.checkPermission(minecraftserver, icommandlistener);
    }

    public String getName() {
        return "seed";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.seed.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        Object object = icommandlistener instanceof EntityPlayer ? ((EntityPlayer) icommandlistener).world : minecraftserver.getWorld(0);

        icommandlistener.sendMessage(new TextComponentTranslation("commands.seed.success", new Object[] { Long.valueOf(((World) object).getSeed())}));
    }
}
