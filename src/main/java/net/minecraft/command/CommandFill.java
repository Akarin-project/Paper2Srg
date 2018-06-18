package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandFill extends CommandBase {

    public CommandFill() {}

    public String getName() {
        return "fill";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.fill.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 7) {
            throw new WrongUsageException("commands.fill.usage", new Object[0]);
        } else {
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = parseBlockPos(icommandlistener, astring, 0, false);
            BlockPos blockposition1 = parseBlockPos(icommandlistener, astring, 3, false);
            Block block = CommandBase.getBlockByText(icommandlistener, astring[6]);
            IBlockState iblockdata;

            if (astring.length >= 8) {
                iblockdata = convertArgToBlockState(block, astring[7]);
            } else {
                iblockdata = block.getDefaultState();
            }

            BlockPos blockposition2 = new BlockPos(Math.min(blockposition.getX(), blockposition1.getX()), Math.min(blockposition.getY(), blockposition1.getY()), Math.min(blockposition.getZ(), blockposition1.getZ()));
            BlockPos blockposition3 = new BlockPos(Math.max(blockposition.getX(), blockposition1.getX()), Math.max(blockposition.getY(), blockposition1.getY()), Math.max(blockposition.getZ(), blockposition1.getZ()));
            int i = (blockposition3.getX() - blockposition2.getX() + 1) * (blockposition3.getY() - blockposition2.getY() + 1) * (blockposition3.getZ() - blockposition2.getZ() + 1);

            if (i > '\u8000') {
                throw new CommandException("commands.fill.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf('\u8000')});
            } else if (blockposition2.getY() >= 0 && blockposition3.getY() < 256) {
                World world = icommandlistener.getEntityWorld();

                for (int j = blockposition2.getZ(); j <= blockposition3.getZ(); j += 16) {
                    for (int k = blockposition2.getX(); k <= blockposition3.getX(); k += 16) {
                        if (!world.isBlockLoaded(new BlockPos(k, blockposition3.getY() - blockposition2.getY(), j))) {
                            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                        }
                    }
                }

                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (astring.length >= 10 && block.hasTileEntity()) {
                    String s = buildString(astring, 9);

                    try {
                        nbttagcompound = JsonToNBT.getTagFromJson(s);
                        flag = true;
                    } catch (NBTException mojangsonparseexception) {
                        throw new CommandException("commands.fill.tagError", new Object[] { mojangsonparseexception.getMessage()});
                    }
                }

                ArrayList arraylist = Lists.newArrayList();

                i = 0;

                for (int l = blockposition2.getZ(); l <= blockposition3.getZ(); ++l) {
                    for (int i1 = blockposition2.getY(); i1 <= blockposition3.getY(); ++i1) {
                        for (int j1 = blockposition2.getX(); j1 <= blockposition3.getX(); ++j1) {
                            BlockPos blockposition4 = new BlockPos(j1, i1, l);

                            if (astring.length >= 9) {
                                if (!"outline".equals(astring[8]) && !"hollow".equals(astring[8])) {
                                    if ("destroy".equals(astring[8])) {
                                        world.destroyBlock(blockposition4, true);
                                    } else if ("keep".equals(astring[8])) {
                                        if (!world.isAirBlock(blockposition4)) {
                                            continue;
                                        }
                                    } else if ("replace".equals(astring[8]) && !block.hasTileEntity() && astring.length > 9) {
                                        Block block1 = CommandBase.getBlockByText(icommandlistener, astring[9]);

                                        if (world.getBlockState(blockposition4).getBlock() != block1 || astring.length > 10 && !"-1".equals(astring[10]) && !"*".equals(astring[10]) && !CommandBase.convertArgToBlockStatePredicate(block1, astring[10]).apply(world.getBlockState(blockposition4))) {
                                            continue;
                                        }
                                    }
                                } else if (j1 != blockposition2.getX() && j1 != blockposition3.getX() && i1 != blockposition2.getY() && i1 != blockposition3.getY() && l != blockposition2.getZ() && l != blockposition3.getZ()) {
                                    if ("hollow".equals(astring[8])) {
                                        world.setBlockState(blockposition4, Blocks.AIR.getDefaultState(), 2);
                                        arraylist.add(blockposition4);
                                    }
                                    continue;
                                }
                            }

                            TileEntity tileentity = world.getTileEntity(blockposition4);

                            if (tileentity != null && tileentity instanceof IInventory) {
                                ((IInventory) tileentity).clear();
                            }

                            if (world.setBlockState(blockposition4, iblockdata, 2)) {
                                arraylist.add(blockposition4);
                                ++i;
                                if (flag) {
                                    TileEntity tileentity1 = world.getTileEntity(blockposition4);

                                    if (tileentity1 != null) {
                                        nbttagcompound.setInteger("x", blockposition4.getX());
                                        nbttagcompound.setInteger("y", blockposition4.getY());
                                        nbttagcompound.setInteger("z", blockposition4.getZ());
                                        tileentity1.readFromNBT(nbttagcompound);
                                    }
                                }
                            }
                        }
                    }
                }

                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    BlockPos blockposition5 = (BlockPos) iterator.next();
                    Block block2 = world.getBlockState(blockposition5).getBlock();

                    world.notifyNeighborsRespectDebug(blockposition5, block2, false);
                }

                if (i <= 0) {
                    throw new CommandException("commands.fill.failed", new Object[0]);
                } else {
                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.fill.success", new Object[] { Integer.valueOf(i)});
                }
            } else {
                throw new CommandException("commands.fill.outOfWorld", new Object[0]);
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? getTabCompletionCoordinate(astring, 0, blockposition) : (astring.length > 3 && astring.length <= 6 ? getTabCompletionCoordinate(astring, 3, blockposition) : (astring.length == 7 ? getListOfStringsMatchingLastWord(astring, (Collection) Block.REGISTRY.getKeys()) : (astring.length == 9 ? getListOfStringsMatchingLastWord(astring, new String[] { "replace", "destroy", "keep", "hollow", "outline"}) : (astring.length == 10 && "replace".equals(astring[8]) ? getListOfStringsMatchingLastWord(astring, (Collection) Block.REGISTRY.getKeys()) : Collections.emptyList()))));
    }
}
