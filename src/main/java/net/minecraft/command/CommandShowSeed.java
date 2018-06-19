package net.minecraft.command;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;


public class CommandShowSeed extends CommandBase {

    public CommandShowSeed() {}

    public boolean func_184882_a(MinecraftServer minecraftserver, ICommandSender icommandlistener) {
        return minecraftserver.func_71264_H() || super.func_184882_a(minecraftserver, icommandlistener);
    }

    public String func_71517_b() {
        return "seed";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.seed.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        Object object = icommandlistener instanceof EntityPlayer ? ((EntityPlayer) icommandlistener).field_70170_p : minecraftserver.func_71218_a(0);

        icommandlistener.func_145747_a(new TextComponentTranslation("commands.seed.success", new Object[] { Long.valueOf(((World) object).func_72905_C())}));
    }
}
