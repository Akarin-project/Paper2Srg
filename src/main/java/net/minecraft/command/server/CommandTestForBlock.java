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
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandTestForBlock extends CommandBase {

    public CommandTestForBlock() {}

    public String getName() {
        return "testforblock";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.testforblock.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 4) {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        } else {
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = parseBlockPos(icommandlistener, astring, 0, false);
            Block block = getBlockByText(icommandlistener, astring[3]);

            if (block == null) {
                throw new NumberInvalidException("commands.setblock.notFound", new Object[] { astring[3]});
            } else {
                World world = icommandlistener.getEntityWorld();

                if (!world.isBlockLoaded(blockposition)) {
                    throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
                } else {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    boolean flag = false;

                    if (astring.length >= 6 && block.hasTileEntity()) {
                        String s = buildString(astring, 5);

                        try {
                            nbttagcompound = JsonToNBT.getTagFromJson(s);
                            flag = true;
                        } catch (NBTException mojangsonparseexception) {
                            throw new CommandException("commands.setblock.tagError", new Object[] { mojangsonparseexception.getMessage()});
                        }
                    }

                    IBlockState iblockdata = world.getBlockState(blockposition);
                    Block block1 = iblockdata.getBlock();

                    if (block1 != block) {
                        throw new CommandException("commands.testforblock.failed.tile", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ()), block1.getLocalizedName(), block.getLocalizedName()});
                    } else if (astring.length >= 5 && !CommandBase.convertArgToBlockStatePredicate(block, astring[4]).apply(iblockdata)) {
                        try {
                            int i = iblockdata.getBlock().getMetaFromState(iblockdata);

                            throw new CommandException("commands.testforblock.failed.data", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ()), Integer.valueOf(i), Integer.valueOf(Integer.parseInt(astring[4]))});
                        } catch (NumberFormatException numberformatexception) {
                            throw new CommandException("commands.testforblock.failed.data", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ()), iblockdata.toString(), astring[4]});
                        }
                    } else {
                        if (flag) {
                            TileEntity tileentity = world.getTileEntity(blockposition);

                            if (tileentity == null) {
                                throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
                            }

                            NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());

                            if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true)) {
                                throw new CommandException("commands.testforblock.failed.nbt", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
                            }
                        }

                        icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                        notifyCommandListener(icommandlistener, (ICommand) this, "commands.testforblock.success", new Object[] { Integer.valueOf(blockposition.getX()), Integer.valueOf(blockposition.getY()), Integer.valueOf(blockposition.getZ())});
                    }
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? getTabCompletionCoordinate(astring, 0, blockposition) : (astring.length == 4 ? getListOfStringsMatchingLastWord(astring, (Collection) Block.REGISTRY.getKeys()) : Collections.emptyList());
    }
}
