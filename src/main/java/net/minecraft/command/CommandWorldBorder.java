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

    public String func_71517_b() {
        return "worldborder";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.worldborder.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        } else {
            WorldBorder worldborder = this.func_184931_a(minecraftserver);
            double d0;
            double d1;
            long i;

            if ("set".equals(astring[0])) {
                if (astring.length != 2 && astring.length != 3) {
                    throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
                }

                d0 = worldborder.func_177751_j();
                d1 = func_175756_a(astring[1], 1.0D, 6.0E7D);
                i = astring.length > 2 ? func_175760_a(astring[2], 0L, 9223372036854775L) * 1000L : 0L;
                if (i > 0L) {
                    worldborder.func_177738_a(d0, d1, i);
                    if (d0 > d1) {
                        func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)}), Long.toString(i / 1000L)});
                    } else {
                        func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)}), Long.toString(i / 1000L)});
                    }
                } else {
                    worldborder.func_177750_a(d1);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)})});
                }
            } else if ("add".equals(astring[0])) {
                if (astring.length != 2 && astring.length != 3) {
                    throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
                }

                d0 = worldborder.func_177741_h();
                d1 = d0 + func_175756_a(astring[1], -d0, 6.0E7D - d0);
                i = worldborder.func_177732_i() + (astring.length > 2 ? func_175760_a(astring[2], 0L, 9223372036854775L) * 1000L : 0L);
                if (i > 0L) {
                    worldborder.func_177738_a(d0, d1, i);
                    if (d0 > d1) {
                        func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)}), Long.toString(i / 1000L)});
                    } else {
                        func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)}), Long.toString(i / 1000L)});
                    }
                } else {
                    worldborder.func_177750_a(d1);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d1)}), String.format("%.1f", new Object[] { Double.valueOf(d0)})});
                }
            } else if ("center".equals(astring[0])) {
                if (astring.length != 3) {
                    throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
                }

                BlockPos blockposition = icommandlistener.func_180425_c();
                double d2 = func_175761_b((double) blockposition.func_177958_n() + 0.5D, astring[1], true);
                double d3 = func_175761_b((double) blockposition.func_177952_p() + 0.5D, astring[2], true);

                worldborder.func_177739_c(d2, d3);
                func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.center.success", new Object[] { Double.valueOf(d2), Double.valueOf(d3)});
            } else if ("damage".equals(astring[0])) {
                if (astring.length < 2) {
                    throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
                }

                if ("buffer".equals(astring[1])) {
                    if (astring.length != 3) {
                        throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                    }

                    d0 = func_180526_a(astring[2], 0.0D);
                    d1 = worldborder.func_177742_m();
                    worldborder.func_177724_b(d0);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.damage.buffer.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d0)}), String.format("%.1f", new Object[] { Double.valueOf(d1)})});
                } else if ("amount".equals(astring[1])) {
                    if (astring.length != 3) {
                        throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                    }

                    d0 = func_180526_a(astring[2], 0.0D);
                    d1 = worldborder.func_177727_n();
                    worldborder.func_177744_c(d0);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.damage.amount.success", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d0)}), String.format("%.2f", new Object[] { Double.valueOf(d1)})});
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

                    j = func_180528_a(astring[2], 0);
                    k = worldborder.func_177740_p();
                    worldborder.func_177723_b(j);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.warning.time.success", new Object[] { Integer.valueOf(j), Integer.valueOf(k)});
                } else if ("distance".equals(astring[1])) {
                    if (astring.length != 3) {
                        throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                    }

                    j = func_180528_a(astring[2], 0);
                    k = worldborder.func_177748_q();
                    worldborder.func_177747_c(j);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.worldborder.warning.distance.success", new Object[] { Integer.valueOf(j), Integer.valueOf(k)});
                }
            } else {
                if (!"get".equals(astring[0])) {
                    throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
                }

                d0 = worldborder.func_177741_h();
                icommandlistener.func_174794_a(CommandResultStats.Type.QUERY_RESULT, MathHelper.func_76128_c(d0 + 0.5D));
                icommandlistener.func_145747_a(new TextComponentTranslation("commands.worldborder.get.success", new Object[] { String.format("%.0f", new Object[] { Double.valueOf(d0)})}));
            }

        }
    }

    protected WorldBorder func_184931_a(MinecraftServer minecraftserver) {
        return minecraftserver.field_71305_c[0].func_175723_af();
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, new String[] { "set", "center", "damage", "warning", "add", "get"}) : (astring.length == 2 && "damage".equals(astring[0]) ? func_71530_a(astring, new String[] { "buffer", "amount"}) : (astring.length >= 2 && astring.length <= 3 && "center".equals(astring[0]) ? func_181043_b(astring, 1, blockposition) : (astring.length == 2 && "warning".equals(astring[0]) ? func_71530_a(astring, new String[] { "time", "distance"}) : Collections.emptyList())));
    }
}
