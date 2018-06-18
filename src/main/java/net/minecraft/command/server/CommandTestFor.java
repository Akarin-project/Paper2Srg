package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTestFor extends CommandBase {

    public CommandTestFor() {}

    public String getName() {
        return "testfor";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.testfor.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.testfor.usage", new Object[0]);
        } else {
            Entity entity = getEntity(minecraftserver, icommandlistener, astring[0]);
            NBTTagCompound nbttagcompound = null;

            if (astring.length >= 2) {
                try {
                    nbttagcompound = JsonToNBT.getTagFromJson(buildString(astring, 1));
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.testfor.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            if (nbttagcompound != null) {
                NBTTagCompound nbttagcompound1 = entityToNBT(entity);

                if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true)) {
                    throw new CommandException("commands.testfor.failure", new Object[] { entity.getName()});
                }
            }

            notifyCommandListener(icommandlistener, (ICommand) this, "commands.testfor.success", new Object[] { entity.getName()});
        }
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
