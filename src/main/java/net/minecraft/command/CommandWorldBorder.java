package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder extends CommandBase {

    public CommandWorldBorder() {}

    public String getName() {
        return "worldborder";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.worldborder.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        } else {
            WorldBorder worldborder = this.getWorldBorder(minecraftserver);
            double d0;
            double d1;
            long i;

            if ("set".equals(astring[0])) {
                if (astring.length != 2 && astring.length != 3) {
                    throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
                }

                d0 = worldborder.getTargetSize();
                d1 = parseDouble(astring[1], 1.0D, 6.0E7D);
                i = astring.length > 2 ? parseLong(astring[2], 0L, 9223372036854775L) * 1000L : 0L;
                if (i > 0L) {
                    worldborder.setTransition(d0, d1, i);
                    if (d0 > d1) {
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)}), Long.toString(i / 1000L)});
                    } else {
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)}), Long.toString(i / 1000L)});
                    }
                } else {
                    worldborder.setTransition(d1);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)})});
                }
            } else if ("add".equals(astring[0])) {
                if (astring.length != 2 && astring.length != 3) {
                    throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
                }

                d0 = worldborder.getDiameter();
                d1 = d0 + parseDouble(astring[1], -d0, 6.0E7D - d0);
                i = worldborder.getTimeUntilTarget() + (astring.length > 2 ? parseLong(astring[2], 0L, 9223372036854775L) * 1000L : 0L);
                if (i > 0L) {
                    worldborder.setTransition(d0, d1, i);
                    if (d0 > d1) {
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)}), Long.toString(i / 1000L)});
                    } else {
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)}), Long.toString(i / 1000L)});
                    }
                } else {
                    worldborder.setTransition(d1);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)})});
                }
            } else if ("center".equals(astring[0])) {
                if (astring.length != 3) {
                    throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
                }

                BlockPos blockposition = icommandlistener.getPosition();
                double d2 = parseDouble((double) blockposition.getX() + 0.5D, astring[1], true);
                double d3 = parseDouble((double) blockposition.getZ() + 0.5D, astring[2], true);

                worldborder.setCenter(d2, d3);
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.center.success", new Object[] { Double.valueOf(d2), Double.valueOf(d3)});
            } else if ("damage".equals(astring[0])) {
                if (astring.length < 2) {
                    throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
                }

                if ("buffer".equals(astring[1])) {
                    if (astring.length != 3) {
                        throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                    }

                    d0 = parseDouble(astring[2], 0.0D);
                    d1 = worldborder.getDamageBuffer();
                    worldborder.setDamageBuffer(d0);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.damage.buffer.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d0)}), String.format("%.1f", new Object[] { Double.valueOf(d1)})});
                } else if ("amount".equals(astring[1])) {
                    if (astring.length != 3) {
                        throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                    }

                    d0 = parseDouble(astring[2], 0.0D);
                    d1 = worldborder.getDamageAmount();
                    worldborder.setDamageAmount(d0);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.damage.amount.success", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d0)}), String.format("%.2f", new Object[] { Double.valueOf(d1)})});
                }
            } else if ("warning".equals(astring[0])) {
                if (astring.length < 2) {
                    throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
                }

                int j;
                int k;

                if ("time".equals(astring[1])) {
                    if (astring.length != 3) {
                        throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
                    }

                    j = parseInt(astring[2], 0);
                    k = worldborder.getWarningTime();
                    worldborder.setWarningTime(j);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.warning.time.success", new Object[] { Integer.valueOf(j), Integer.valueOf(k)});
                } else if ("distance".equals(astring[1])) {
                    if (astring.length != 3) {
                        throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                    }

                    j = parseInt(astring[2], 0);
                    k = worldborder.getWarningDistance();
                    worldborder.setWarningDistance(j);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.worldborder.warning.distance.success", new Object[] { Integer.valueOf(j), Integer.valueOf(k)});
                }
            } else {
                if (!"get".equals(astring[0])) {
                    throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
                }

                d0 = worldborder.getDiameter();
                icommandlistener.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor(d0 + 0.5D));
                icommandlistener.sendMessage(new TextComponentTranslation("commands.worldborder.get.success", new Object[] { String.format("%.0f", new Object[] { Double.valueOf(d0)})}));
            }

        }
    }

    protected WorldBorder getWorldBorder(MinecraftServer minecraftserver) {
        return minecraftserver.worlds[0].getWorldBorder();
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, new String[] { "set", "center", "damage", "warning", "add", "get"}) : (astring.length == 2 && "damage".equals(astring[0]) ? getListOfStringsMatchingLastWord(astring, new String[] { "buffer", "amount"}) : (astring.length >= 2 && astring.length <= 3 && "center".equals(astring[0]) ? getTabCompletionCoordinateXZ(astring, 1, blockposition) : (astring.length == 2 && "warning".equals(astring[0]) ? getListOfStringsMatchingLastWord(astring, new String[] { "time", "distance"}) : Collections.emptyList())));
    }
}
