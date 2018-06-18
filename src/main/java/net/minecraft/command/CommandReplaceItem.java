package net.minecraft.command;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandReplaceItem extends CommandBase {

    private static final Map<String, Integer> SHORTCUTS = Maps.newHashMap();

    public CommandReplaceItem() {}

    public String getName() {
        return "replaceitem";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.replaceitem.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
        } else {
            boolean flag;

            if ("entity".equals(astring[0])) {
                flag = false;
            } else {
                if (!"block".equals(astring[0])) {
                    throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
                }

                flag = true;
            }

            byte b0;

            if (flag) {
                if (astring.length < 6) {
                    throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
                }

                b0 = 4;
            } else {
                if (astring.length < 4) {
                    throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
                }

                b0 = 2;
            }

            String s = astring[b0];
            int i = b0 + 1;
            int j = this.getSlotForShortcut(astring[b0]);

            Item item;

            try {
                item = getItemByText(icommandlistener, astring[i]);
            } catch (NumberInvalidException exceptioninvalidnumber) {
                if (Block.getBlockFromName(astring[i]) != Blocks.AIR) {
                    throw exceptioninvalidnumber;
                }

                item = null;
            }

            ++i;
            int k = astring.length > i ? parseInt(astring[i++], 1, item.getItemStackLimit()) : 1;
            int l = astring.length > i ? parseInt(astring[i++]) : 0;
            ItemStack itemstack = new ItemStack(item, k, l);

            if (astring.length > i) {
                String s1 = buildString(astring, i);

                try {
                    itemstack.setTagCompound(JsonToNBT.getTagFromJson(s1));
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.replaceitem.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            if (flag) {
                icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
                BlockPos blockposition = parseBlockPos(icommandlistener, astring, 1, false);
                World world = icommandlistener.getEntityWorld();
                TileEntity tileentity = world.getTileEntity(blockposition);

                if (tileentity == null || !(tileentity instanceof IInventory)) {
                    throw new CommandException("commands.replaceitem.noContainer", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
                }

                IInventory iinventory = (IInventory) tileentity;

                if (j >= 0 && j < iinventory.getSizeInventory()) {
                    iinventory.setInventorySlotContents(j, itemstack);
                }
            } else {
                Entity entity = getEntity(minecraftserver, icommandlistener, astring[1]);

                icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).inventoryContainer.detectAndSendChanges();
                }

                if (!entity.replaceItemInInventory(j, itemstack)) {
                    throw new CommandException("commands.replaceitem.failed", new Object[] { s, Integer.valueOf(k), itemstack.isEmpty() ? "Air" : itemstack.getTextComponent()});
                }

                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).inventoryContainer.detectAndSendChanges();
                }
            }

            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
            notifyCommandListener(icommandlistener, (ICommand) this, "commands.replaceitem.success", new Object[] { s, Integer.valueOf(k), itemstack.isEmpty() ? "Air" : itemstack.getTextComponent()});
        }
    }

    private int getSlotForShortcut(String s) throws CommandException {
        if (!CommandReplaceItem.SHORTCUTS.containsKey(s)) {
            throw new CommandException("commands.generic.parameter.invalid", new Object[] { s});
        } else {
            return ((Integer) CommandReplaceItem.SHORTCUTS.get(s)).intValue();
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "entity", "block"}) : (astring.length == 2 && "entity".equals(astring[0]) ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length >= 2 && astring.length <= 4 && "block".equals(astring[0]) ? getTabCompletionCoordinate(astring, 1, blockposition) : ((astring.length != 3 || !"entity".equals(astring[0])) && (astring.length != 5 || !"block".equals(astring[0])) ? ((astring.length != 4 || !"entity".equals(astring[0])) && (astring.length != 6 || !"block".equals(astring[0])) ? Collections.emptyList() : getListOfStringsMatchingLastWord(astring, (Collection) Item.REGISTRY.getKeys())) : getListOfStringsMatchingLastWord(astring, (Collection) CommandReplaceItem.SHORTCUTS.keySet()))));
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return astring.length > 0 && "entity".equals(astring[0]) && i == 1;
    }

    static {
        int i;

        for (i = 0; i < 54; ++i) {
            CommandReplaceItem.SHORTCUTS.put("slot.container." + i, Integer.valueOf(i));
        }

        for (i = 0; i < 9; ++i) {
            CommandReplaceItem.SHORTCUTS.put("slot.hotbar." + i, Integer.valueOf(i));
        }

        for (i = 0; i < 27; ++i) {
            CommandReplaceItem.SHORTCUTS.put("slot.inventory." + i, Integer.valueOf(9 + i));
        }

        for (i = 0; i < 27; ++i) {
            CommandReplaceItem.SHORTCUTS.put("slot.enderchest." + i, Integer.valueOf(200 + i));
        }

        for (i = 0; i < 8; ++i) {
            CommandReplaceItem.SHORTCUTS.put("slot.villager." + i, Integer.valueOf(300 + i));
        }

        for (i = 0; i < 15; ++i) {
            CommandReplaceItem.SHORTCUTS.put("slot.horse." + i, Integer.valueOf(500 + i));
        }

        CommandReplaceItem.SHORTCUTS.put("slot.weapon", Integer.valueOf(98));
        CommandReplaceItem.SHORTCUTS.put("slot.weapon.mainhand", Integer.valueOf(98));
        CommandReplaceItem.SHORTCUTS.put("slot.weapon.offhand", Integer.valueOf(99));
        CommandReplaceItem.SHORTCUTS.put("slot.armor.head", Integer.valueOf(100 + EntityEquipmentSlot.HEAD.getIndex()));
        CommandReplaceItem.SHORTCUTS.put("slot.armor.chest", Integer.valueOf(100 + EntityEquipmentSlot.CHEST.getIndex()));
        CommandReplaceItem.SHORTCUTS.put("slot.armor.legs", Integer.valueOf(100 + EntityEquipmentSlot.LEGS.getIndex()));
        CommandReplaceItem.SHORTCUTS.put("slot.armor.feet", Integer.valueOf(100 + EntityEquipmentSlot.FEET.getIndex()));
        CommandReplaceItem.SHORTCUTS.put("slot.horse.saddle", Integer.valueOf(400));
        CommandReplaceItem.SHORTCUTS.put("slot.horse.armor", Integer.valueOf(401));
        CommandReplaceItem.SHORTCUTS.put("slot.horse.chest", Integer.valueOf(499));
    }
}
