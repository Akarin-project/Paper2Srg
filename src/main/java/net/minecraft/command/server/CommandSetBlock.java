package net.minecraft.command.server;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandSetBlock extends CommandBase {

    public CommandSetBlock() {}

    public String getName() {
        return "setblock";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.setblock.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 4) {
            throw new WrongUsageException("commands.setblock.usage", new Object[0]);
        } else {
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = parseBlockPos(icommandlistener, astring, 0, false);
            Block block = CommandBase.getBlockByText(icommandlistener, astring[3]);
            IBlockState iblockdata;

            if (astring.length >= 5) {
                iblockdata = convertArgToBlockState(block, astring[4]);
            } else {
                iblockdata = block.getDefaultState();
            }

            World world = icommandlistener.getEntityWorld();

            if (!world.isBlockLoaded(blockposition)) {
                throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
            } else {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (astring.length >= 7 && block.hasTileEntity()) {
                    String s = buildString(astring, 6);

                    try {
                        nbttagcompound = JsonToNBT.getTagFromJson(s);
                        flag = true;
                    } catch (NBTException mojangsonparseexception) {
                        throw new CommandException("commands.setblock.tagError", new Object[] { mojangsonparseexception.getMessage()});
                    }
                }

                if (astring.length >= 6) {
                    if ("destroy".equals(astring[5])) {
                        world.destroyBlock(blockposition, true);
                        if (block == Blocks.AIR) {
                            notifyCommandListener(icommandlistener, (ICommand) this, "commands.setblock.success", new Object[0]);
                            return;
                        }
                    } else if ("keep".equals(astring[5]) && !world.isAirBlock(blockposition)) {
                        throw new CommandException("commands.setblock.noChange", new Object[0]);
                    }
                }

                TileEntity tileentity = world.getTileEntity(blockposition);

                if (tileentity != null && tileentity instanceof IInventory) {
                    ((IInventory) tileentity).clear();
                }

                if (!world.setBlockState(blockposition, iblockdata, 2)) {
                    throw new CommandException("commands.setblock.noChange", new Object[0]);
                } else {
                    if (flag) {
                        TileEntity tileentity1 = world.getTileEntity(blockposition);

                        if (tileentity1 != null) {
                            nbttagcompound.setInteger("x", blockposition.getX());
                            nbttagcompound.setInteger("y", blockposition.getY());
                            nbttagcompound.setInteger("z", blockposition.getZ());
                            tileentity1.readFromNBT(nbttagcompound);
                        }
                    }

                    world.notifyNeighborsRespectDebug(blockposition, iblockdata.getBlock(), false);
                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.setblock.success", new Object[0]);
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? getTabCompletionCoordinate(astring, 0, blockposition) : (astring.length == 4 ? getListOfStringsMatchingLastWord(astring, (Collection) Block.REGISTRY.getKeys()) : (astring.length == 6 ? getListOfStringsMatchingLastWord(astring, new String[] { "replace", "destroy", "keep"}) : Collections.emptyList()));
    }
}
