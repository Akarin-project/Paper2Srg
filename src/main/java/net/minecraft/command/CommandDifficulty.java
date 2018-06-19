package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class CommandDifficulty extends CommandBase {

    public CommandDifficulty() {}

    public String func_71517_b() {
        return "difficulty";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.difficulty.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
        } else {
            EnumDifficulty enumdifficulty = this.func_180531_e(astring[0]);

            minecraftserver.func_147139_a(enumdifficulty);
            func_152373_a(icommandlistener, (ICommand) this, "commands.difficulty.success", new Object[] { new TextComponentTranslation(enumdifficulty.func_151526_b(), new Object[0])});
        }
    }

    protected EnumDifficulty func_180531_e(String s) throws NumberInvalidException {
        return !"peaceful".equalsIgnoreCase(s) && !"p".equalsIgnoreCase(s) ? (!"easy".equalsIgnoreCase(s) && !"e".equalsIgnoreCase(s) ? (!"normal".equalsIgnoreCase(s) && !"n".equalsIgnoreCase(s) ? (!"hard".equalsIgnoreCase(s) && !"h".equalsIgnoreCase(s) ? EnumDifficulty.func_151523_a(func_175764_a(s, 0, 3)) : EnumDifficulty.HARD) : EnumDifficulty.NORMAL) : EnumDifficulty.EASY) : EnumDifficulty.PEACEFUL;
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "peaceful", "easy", "normal", "hard"}) : Collections.emptyList();
    }
}
