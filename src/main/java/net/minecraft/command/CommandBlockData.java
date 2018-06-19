package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandBlockData extends CommandBase {

    public CommandBlockData() {}

    public String func_71517_b() {
        return "blockdata";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.blockdata.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 4) {
            throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
        } else {
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = func_175757_a(icommandlistener, astring, 0, false);
            World world = icommandlistener.func_130014_f_();

            if (!world.func_175667_e(blockposition)) {
                throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
            } else {
                IBlockState iblockdata = world.func_180495_p(blockposition);
                TileEntity tileentity = world.func_175625_s(blockposition);

                if (tileentity == null) {
                    throw new CommandException("commands.blockdata.notValid", new Object[0]);
                } else {
                    NBTTagCompound nbttagcompound = tileentity.func_189515_b(new NBTTagCompound());
                    NBTTagCompound nbttagcompound1 = nbttagcompound.func_74737_b();

                    NBTTagCompound nbttagcompound2;

                    try {
                        nbttagcompound2 = JsonToNBT.func_180713_a(func_180529_a(astring, 3));
                    } catch (NBTException mojangsonparseexception) {
                        throw new CommandException("commands.blockdata.tagError", new Object[] { mojangsonparseexception.getMessage()});
                    }

                    nbttagcompound.func_179237_a(nbttagcompound2);
                    nbttagcompound.func_74768_a("x", blockposition.func_177958_n());
                    nbttagcompound.func_74768_a("y", blockposition.func_177956_o());
                    nbttagcompound.func_74768_a("z", blockposition.func_177952_p());
                    if (nbttagcompound.equals(nbttagcompound1)) {
                        throw new CommandException("commands.blockdata.failed", new Object[] { nbttagcompound.toString()});
                    } else {
                        tileentity.func_145839_a(nbttagcompound);
                        tileentity.func_70296_d();
                        world.func_184138_a(blockposition, iblockdata, iblockdata, 3);
                        icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                        func_152373_a(icommandlistener, (ICommand) this, "commands.blockdata.success", new Object[] { nbttagcompound.toString()});
                    }
                }
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockposition) : Collections.emptyList();
    }
}
