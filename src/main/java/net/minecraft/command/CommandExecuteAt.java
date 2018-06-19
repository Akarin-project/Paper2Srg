package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.command.ProxiedNativeCommandSender;

// CraftBukkit start
import org.bukkit.craftbukkit.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
// CraftBukkit end

public class CommandExecuteAt extends CommandBase {

    public CommandExecuteAt() {}

    public String func_71517_b() {
        return "execute";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.execute.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 5) {
            throw new WrongUsageException("commands.execute.usage", new Object[0]);
        } else {
            Entity entity = func_184884_a(minecraftserver, icommandlistener, astring[0], Entity.class);
            double d0 = func_175761_b(entity.field_70165_t, astring[1], false);
            double d1 = func_175761_b(entity.field_70163_u, astring[2], false);
            double d2 = func_175761_b(entity.field_70161_v, astring[3], false);

            new BlockPos(d0, d1, d2);
            byte b0 = 4;

            if ("detect".equals(astring[4]) && astring.length > 10) {
                World world = entity.func_130014_f_();
                double d3 = func_175761_b(d0, astring[5], false);
                double d4 = func_175761_b(d1, astring[6], false);
                double d5 = func_175761_b(d2, astring[7], false);
                Block block = func_147180_g(icommandlistener, astring[8]);
                BlockPos blockposition = new BlockPos(d3, d4, d5);

                if (!world.func_175667_e(blockposition)) {
                    throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.func_70005_c_()});
                }

                IBlockState iblockdata = world.func_180495_p(blockposition);

                if (iblockdata.func_177230_c() != block) {
                    throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.func_70005_c_()});
                }

                if (!CommandBase.func_190791_b(block, astring[9]).apply(iblockdata)) {
                    throw new CommandException("commands.execute.failed", new Object[] { "detect", entity.func_70005_c_()});
                }

                b0 = 10;
            }

            String s = func_180529_a(astring, b0);
            CommandSenderWrapper commandlistenerwrapper = CommandSenderWrapper.func_193998_a(icommandlistener).func_193997_a(entity, new Vec3d(d0, d1, d2)).func_194001_a(minecraftserver.worlds.get(0).func_82736_K().func_82766_b("commandBlockOutput")); // CraftBukkit
            ICommandManager icommandhandler = minecraftserver.func_71187_D();

            try {
                // CraftBukkit start
                org.bukkit.command.CommandSender sender = CommandBlockBaseLogic.unwrapSender(icommandlistener);
                int i = CommandBlockBaseLogic.executeCommand(commandlistenerwrapper, new ProxiedNativeCommandSender(commandlistenerwrapper, sender, entity.getBukkitEntity()), s); 
                // CraftBukkit end

                if (i < 1) {
                    throw new CommandException("commands.execute.allInvocationsFailed", new Object[] { s});
                }
            } catch (Throwable throwable) {
                // CraftBukkit start
                if (throwable instanceof CommandException) {
                    throw (CommandException) throwable;
                }
                // CraftBukkit end
                throw new CommandException("commands.execute.failed", new Object[] { s, entity.func_70005_c_()});
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length > 1 && astring.length <= 4 ? func_175771_a(astring, 1, blockposition) : (astring.length > 5 && astring.length <= 8 && "detect".equals(astring[4]) ? func_175771_a(astring, 5, blockposition) : (astring.length == 9 && "detect".equals(astring[4]) ? func_175762_a(astring, (Collection) Block.field_149771_c.func_148742_b()) : Collections.<String>emptyList()))); // CraftBukkit - decompile error
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }

    // CraftBukkit start - fix decompiler error
    @Override
    public int compareTo(ICommand o) {
        return compareTo((ICommand) o);
    }
    // CraftBukkit end
}
