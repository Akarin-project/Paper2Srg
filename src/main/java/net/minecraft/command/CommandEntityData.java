package net.minecraft.command;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandEntityData extends CommandBase {

    public CommandEntityData() {}

    public String func_71517_b() {
        return "entitydata";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.entitydata.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        } else {
            Entity entity = func_184885_b(minecraftserver, icommandlistener, astring[0]);

            if (entity instanceof EntityPlayer) {
                throw new CommandException("commands.entitydata.noPlayers", new Object[] { entity.func_145748_c_()});
            } else {
                NBTTagCompound nbttagcompound = func_184887_a(entity);
                NBTTagCompound nbttagcompound1 = nbttagcompound.func_74737_b();

                NBTTagCompound nbttagcompound2;

                try {
                    nbttagcompound2 = JsonToNBT.func_180713_a(func_180529_a(astring, 1));
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.entitydata.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }

                UUID uuid = entity.func_110124_au();

                nbttagcompound.func_179237_a(nbttagcompound2);
                entity.func_184221_a(uuid);
                if (nbttagcompound.equals(nbttagcompound1)) {
                    throw new CommandException("commands.entitydata.failed", new Object[] { nbttagcompound.toString()});
                } else {
                    entity.func_70020_e(nbttagcompound);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.entitydata.success", new Object[] { nbttagcompound.toString()});
                }
            }
        }
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
