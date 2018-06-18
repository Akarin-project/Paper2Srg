package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class CommandGive extends CommandBase {

    public CommandGive() {}

    public String getName() {
        return "give";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.give.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer = getPlayer(minecraftserver, icommandlistener, astring[0]);
            Item item = getItemByText(icommandlistener, astring[1]);
            int i = astring.length >= 3 ? parseInt(astring[2], 1, item.getItemStackLimit()) : 1;
            int j = astring.length >= 4 ? parseInt(astring[3]) : 0;
            ItemStack itemstack = new ItemStack(item, i, j);

            if (astring.length >= 5) {
                String s = buildString(astring, 4);

                try {
                    itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.give.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);

            if (flag) {
                entityplayer.world.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.inventoryContainer.detectAndSendChanges();
            }

            EntityItem entityitem;

            if (flag && itemstack.isEmpty()) {
                itemstack.setCount(1);
                icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
                entityitem = entityplayer.dropItem(itemstack, false);
                if (entityitem != null) {
                    entityitem.makeFakeItem();
                }
            } else {
                icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.getCount());
                entityitem = entityplayer.dropItem(itemstack, false);
                if (entityitem != null) {
                    entityitem.setNoPickupDelay();
                    entityitem.setOwner(entityplayer.getName());
                }
            }

            notifyCommandListener(icommandlistener, (ICommand) this, "commands.give.success", new Object[] { itemstack.getTextComponent(), Integer.valueOf(i), entityplayer.getName()});
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, (Collection) Item.REGISTRY.getKeys()) : Collections.emptyList());
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}
