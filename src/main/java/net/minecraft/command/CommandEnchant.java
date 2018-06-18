package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandEnchant extends CommandBase {

    public CommandEnchant() {}

    public String getName() {
        return "enchant";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.enchant.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        } else {
            EntityLivingBase entityliving = (EntityLivingBase) getEntity(minecraftserver, icommandlistener, astring[0], EntityLivingBase.class);

            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);

            Enchantment enchantment;

            try {
                enchantment = Enchantment.getEnchantmentByID(parseInt(astring[1], 0));
            } catch (NumberInvalidException exceptioninvalidnumber) {
                enchantment = Enchantment.getEnchantmentByLocation(astring[1]);
            }

            if (enchantment == null) {
                throw new NumberInvalidException("commands.enchant.notFound", new Object[] { astring[1]});
            } else {
                int i = 1;
                ItemStack itemstack = entityliving.getHeldItemMainhand();

                if (itemstack.isEmpty()) {
                    throw new CommandException("commands.enchant.noItem", new Object[0]);
                } else if (!enchantment.canApply(itemstack)) {
                    throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
                } else {
                    if (astring.length >= 3) {
                        i = parseInt(astring[2], enchantment.getMinLevel(), enchantment.getMaxLevel());
                    }

                    if (itemstack.hasTagCompound()) {
                        NBTTagList nbttaglist = itemstack.getEnchantmentTagList();

                        for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                            short short0 = nbttaglist.getCompoundTagAt(j).getShort("id");

                            if (Enchantment.getEnchantmentByID(short0) != null) {
                                Enchantment enchantment1 = Enchantment.getEnchantmentByID(short0);

                                if (!enchantment.isCompatibleWith(enchantment1)) {
                                    throw new CommandException("commands.enchant.cantCombine", new Object[] { enchantment.getTranslatedName(i), enchantment1.getTranslatedName(nbttaglist.getCompoundTagAt(j).getShort("lvl"))});
                                }
                            }
                        }
                    }

                    itemstack.addEnchantment(enchantment, i);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.enchant.success", new Object[0]);
                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, (Collection) Enchantment.REGISTRY.getKeys()) : Collections.emptyList());
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}
