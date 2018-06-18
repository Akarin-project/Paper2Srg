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

    public String getName() {
        return "blockdata";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.blockdata.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 4) {
            throw new WrongUsageException("commands.blockdata.usage", new Object[0]);
        } else {
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = parseBlockPos(icommandlistener, astring, 0, false);
            World world = icommandlistener.getEntityWorld();

            if (!world.isBlockLoaded(blockposition)) {
                throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
            } else {
                IBlockState iblockdata = world.getBlockState(blockposition);
                TileEntity tileentity = world.getTileEntity(blockposition);

                if (tileentity == null) {
                    throw new CommandException("commands.blockdata.notValid", new Object[0]);
                } else {
                    NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                    NBTTagCompound nbttagcompound1 = nbttagcompound.copy();

                    NBTTagCompound nbttagcompound2;

                    try {
                        nbttagcompound2 = JsonToNBT.getTagFromJson(buildString(astring, 3));
                    } catch (NBTException mojangsonparseexception) {
                        throw new CommandException("commands.blockdata.tagError", new Object[] { mojangsonparseexception.getMessage()});
                    }

                    nbttagcompound.merge(nbttagcompound2);
                    nbttagcompound.setInteger("x", blockposition.getX());
                    nbttagcompound.setInteger("y", blockposition.getY());
                    nbttagcompound.setInteger("z", blockposition.getZ());
                    if (nbttagcompound.equals(nbttagcompound1)) {
                        throw new CommandException("commands.blockdata.failed", new Object[] { nbttagcompound.toString()});
                    } else {
                        tileentity.readFromNBT(nbttagcompound);
                        tileentity.markDirty();
                        world.notifyBlockUpdate(blockposition, iblockdata, iblockdata, 3);
                        icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.blockdata.success", new Object[] { nbttagcompound.toString()});
                    }
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? getTabCompletionCoordinate(astring, 0, blockposition) : Collections.emptyList();
    }
}
