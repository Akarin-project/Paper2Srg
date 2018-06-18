package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEffect extends CommandBase {

    public CommandEffect() {}

    public String getName() {
        return "effect";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.effect.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        } else {
            EntityLivingBase entityliving = (EntityLivingBase) getEntity(minecraftserver, icommandlistener, astring[0], EntityLivingBase.class);

            if ("clear".equals(astring[1])) {
                if (entityliving.getActivePotionEffects().isEmpty()) {
                    throw new CommandException("commands.effect.failure.notActive.all", new Object[] { entityliving.getName()});
                } else {
                    entityliving.clearActivePotions();
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.effect.success.removed.all", new Object[] { entityliving.getName()});
                }
            } else {
                Potion mobeffectlist;

                try {
                    mobeffectlist = Potion.getPotionById(parseInt(astring[1], 1));
                } catch (NumberInvalidException exceptioninvalidnumber) {
                    mobeffectlist = Potion.getPotionFromResourceLocation(astring[1]);
                }

                if (mobeffectlist == null) {
                    throw new NumberInvalidException("commands.effect.notFound", new Object[] { astring[1]});
                } else {
                    int i = 600;
                    int j = 30;
                    int k = 0;

                    if (astring.length >= 3) {
                        j = parseInt(astring[2], 0, 1000000);
                        if (mobeffectlist.isInstant()) {
                            i = j;
                        } else {
                            i = j * 20;
                        }
                    } else if (mobeffectlist.isInstant()) {
                        i = 1;
                    }

                    if (astring.length >= 4) {
                        k = parseInt(astring[3], 0, 255);
                    }

                    boolean flag = true;

                    if (astring.length >= 5 && "true".equalsIgnoreCase(astring[4])) {
                        flag = false;
                    }

                    if (j > 0) {
                        PotionEffect mobeffect = new PotionEffect(mobeffectlist, i, k, false, flag);

                        entityliving.addPotionEffect(mobeffect);
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.effect.success", new Object[] { new TextComponentTranslation(mobeffect.getEffectName(), new Object[0]), Integer.valueOf(Potion.getIdFromPotion(mobeffectlist)), Integer.valueOf(k), entityliving.getName(), Integer.valueOf(j)});
                    } else if (entityliving.isPotionActive(mobeffectlist)) {
                        entityliving.removePotionEffect(mobeffectlist);
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.effect.success.removed", new Object[] { new TextComponentTranslation(mobeffectlist.getName(), new Object[0]), entityliving.getName()});
                    } else {
                        throw new CommandException("commands.effect.failure.notActive", new Object[] { new TextComponentTranslation(mobeffectlist.getName(), new Object[0]), entityliving.getName()});
                    }
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, minecraftserver.getOnlinePlayerNames()) : (astring.length == 2 ? getListOfStringsMatchingLastWord(astring, (Collection) Potion.REGISTRY.getKeys()) : (astring.length == 5 ? getListOfStringsMatchingLastWord(astring, new String[] { "true", "false"}) : Collections.emptyList()));
    }

    public boolean isUsernameIndex(String[] astring, int i) {
        return i == 0;
    }
}
