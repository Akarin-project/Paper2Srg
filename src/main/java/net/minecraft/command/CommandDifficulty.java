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

    public String getName() {
        return "difficulty";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.difficulty.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length <= 0) {
            throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
        } else {
            EnumDifficulty enumdifficulty = this.getDifficultyFromCommand(astring[0]);

            minecraftserver.setDifficultyForAllWorlds(enumdifficulty);
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.difficulty.success", new Object[] { new TextComponentTranslation(enumdifficulty.getDifficultyResourceKey(), new Object[0])});
        }
    }

    protected EnumDifficulty getDifficultyFromCommand(String s) throws NumberInvalidException {
        return !"peaceful".equalsIgnoreCase(s) && !"p".equalsIgnoreCase(s) ? (!"easy".equalsIgnoreCase(s) && !"e".equalsIgnoreCase(s) ? (!"normal".equalsIgnoreCase(s) && !"n".equalsIgnoreCase(s) ? (!"hard".equalsIgnoreCase(s) && !"h".equalsIgnoreCase(s) ? EnumDifficulty.getDifficultyEnum(parseInt(s, 0, 3)) : EnumDifficulty.HARD) : EnumDifficulty.NORMAL) : EnumDifficulty.EASY) : EnumDifficulty.PEACEFUL;
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "peaceful", "easy", "normal", "hard"}) : Collections.emptyList();
    }
}
