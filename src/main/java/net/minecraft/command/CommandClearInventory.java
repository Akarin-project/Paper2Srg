package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClearInventory extends CommandBase {

    public CommandClearInventory() {}

    public String getName() {
        return "clear";
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.clear.usage";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        EntityPlayerMP entityplayer = astring.length == 0 ? getCommandSenderAsPlayer(icommandlistener) : getPlayer(minecraftserver, icommandlistener, astring[0]);
        Item item = astring.length >= 2 ? getItemByText(icommandlistener, astring[1]) : null;
        int i = astring.length >= 3 ? parseInt(astring[2], -1) : -1;
        int j = astring.length >= 4 ? parseInt(astring[3], -1) : -1;
        NBTTagCompound nbttagcompound = null;

        if (astring.length >= 5) {
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(buildString(astring, 4));
            } catch (NBTException mojangsonparseexception) {
                throw new CommandException("commands.clear.tagError", new Object[] { mojangsonparseexception.getMessage()});
            }
        }

        if (astring.length >= 2 && item == null) {
            throw new CommandException("commands.clear.failure", new Object[] { entityplayer.getName()});
        } else {
            int k = entityplayer.inventory.clearMatchingItems(item, i, j, nbttagcompound);

            entityplayer.inventoryContainer.detectAndSendChanges();
            if (!entityplayer.capabilities.isCreativeMode) {
                entityplayer.updateHeldItem();
            }

            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
            if (k == 0) {
                throw new CommandException("commands.clear.failure", new Object[] { entityplayer.getName()});
            } else {
                if (j == 0) {
                    icommandlistener.sendMessage(new TextComponentTranslation("commands.clear.testing", new Object[] { entityplayer.getName(), Integer.valueOf(k)}));
                } else {
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.clear.success", new Object[] { entityplayer.getName(), Integer.valueOf(k)});
                }

            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, (Collection) Item.REGISTRY.getKeys()) : Collections.emptyList());
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}
