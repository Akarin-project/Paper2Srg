package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandClone extends CommandBase {

    public CommandClone() {}

    public String getName() {
        return "clone";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.clone.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        } else {
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = parseBlockPos(icommandlistener, astring, 0, false);
            BlockPos blockposition1 = parseBlockPos(icommandlistener, astring, 3, false);
            BlockPos blockposition2 = parseBlockPos(icommandlistener, astring, 6, false);
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockposition, blockposition1);
            StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockposition2, blockposition2.add(structureboundingbox.getLength()));
            int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();

            if (i > '\u8000') {
                throw new CommandException("commands.clone.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf('\u8000')});
            } else {
                boolean flag = false;
                Block block = null;
                Predicate predicate = null;

                if ((astring.length < 11 || !"force".equals(astring[10]) && !"move".equals(astring[10])) && structureboundingbox.intersectsWith(structureboundingbox1)) {
                    throw new CommandException("commands.clone.noOverlap", new Object[0]);
                } else {
                    if (astring.length >= 11 && "move".equals(astring[10])) {
                        flag = true;
                    }

                    if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256 && structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256) {
                        World world = icommandlistener.getEntityWorld();

                        if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox1)) {
                            boolean flag1 = false;

                            if (astring.length >= 10) {
                                if ("masked".equals(astring[9])) {
                                    flag1 = true;
                                } else if ("filtered".equals(astring[9])) {
                                    if (astring.length < 12) {
                                        throw new WrongUsageException("commands.clone.usage", new Object[0]);
                                    }

                                    block = getBlockByText(icommandlistener, astring[11]);
                                    if (astring.length >= 13) {
                                        predicate = convertArgToBlockStatePredicate(block, astring[12]);
                                    }
                                }
                            }

                            ArrayList arraylist = Lists.newArrayList();
                            ArrayList arraylist1 = Lists.newArrayList();
                            ArrayList arraylist2 = Lists.newArrayList();
                            LinkedList linkedlist = Lists.newLinkedList();
                            BlockPos blockposition3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);

                            for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; ++j) {
                                for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; ++k) {
                                    for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; ++l) {
                                        BlockPos blockposition4 = new BlockPos(l, k, j);
                                        BlockPos blockposition5 = blockposition4.add((Vec3i) blockposition3);
                                        IBlockState iblockdata = world.getBlockState(blockposition4);

                                        if ((!flag1 || iblockdata.getBlock() != Blocks.AIR) && (block == null || iblockdata.getBlock() == block && (predicate == null || predicate.apply(iblockdata)))) {
                                            TileEntity tileentity = world.getTileEntity(blockposition4);

                                            if (tileentity != null) {
                                                NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

                                                arraylist1.add(new CommandClone.StaticCloneData(blockposition5, iblockdata, nbttagcompound));
                                                linkedlist.addLast(blockposition4);
                                            } else if (!iblockdata.isFullBlock() && !iblockdata.isFullCube()) {
                                                arraylist2.add(new CommandClone.StaticCloneData(blockposition5, iblockdata, (NBTTagCompound) null));
                                                linkedlist.addFirst(blockposition4);
                                            } else {
                                                arraylist.add(new CommandClone.StaticCloneData(blockposition5, iblockdata, (NBTTagCompound) null));
                                                linkedlist.addLast(blockposition4);
                                            }
                                        }
                                    }
                                }
                            }

                            if (flag) {
                                Iterator iterator;
                                BlockPos blockposition6;

                                for (iterator = linkedlist.iterator(); iterator.hasNext(); world.setBlockState(blockposition6, Blocks.BARRIER.getDefaultState(), 2)) {
                                    blockposition6 = (BlockPos) iterator.next();
                                    TileEntity tileentity1 = world.getTileEntity(blockposition6);

                                    if (tileentity1 instanceof IInventory) {
                                        ((IInventory) tileentity1).clear();
                                    }
                                }

                                iterator = linkedlist.iterator();

                                while (iterator.hasNext()) {
                                    blockposition6 = (BlockPos) iterator.next();
                                    world.setBlockState(blockposition6, Blocks.AIR.getDefaultState(), 3);
                                }
                            }

                            ArrayList arraylist3 = Lists.newArrayList();

                            arraylist3.addAll(arraylist);
                            arraylist3.addAll(arraylist1);
                            arraylist3.addAll(arraylist2);
                            List list = Lists.reverse(arraylist3);

                            Iterator iterator1;
                            CommandClone.StaticCloneData commandclone_commandclonestoredtileentity;
                            TileEntity tileentity2;

                            for (iterator1 = list.iterator(); iterator1.hasNext(); world.setBlockState(commandclone_commandclonestoredtileentity.pos, Blocks.BARRIER.getDefaultState(), 2)) {
                                commandclone_commandclonestoredtileentity = (CommandClone.StaticCloneData) iterator1.next();
                                tileentity2 = world.getTileEntity(commandclone_commandclonestoredtileentity.pos);
                                if (tileentity2 instanceof IInventory) {
                                    ((IInventory) tileentity2).clear();
                                }
                            }

                            i = 0;
                            iterator1 = arraylist3.iterator();

                            while (iterator1.hasNext()) {
                                commandclone_commandclonestoredtileentity = (CommandClone.StaticCloneData) iterator1.next();
                                if (world.setBlockState(commandclone_commandclonestoredtileentity.pos, commandclone_commandclonestoredtileentity.blockState, 2)) {
                                    ++i;
                                }
                            }

                            for (iterator1 = arraylist1.iterator(); iterator1.hasNext(); world.setBlockState(commandclone_commandclonestoredtileentity.pos, commandclone_commandclonestoredtileentity.blockState, 2)) {
                                commandclone_commandclonestoredtileentity = (CommandClone.StaticCloneData) iterator1.next();
                                tileentity2 = world.getTileEntity(commandclone_commandclonestoredtileentity.pos);
                                if (commandclone_commandclonestoredtileentity.nbt != null && tileentity2 != null) {
                                    commandclone_commandclonestoredtileentity.nbt.setInteger("x", commandclone_commandclonestoredtileentity.pos.getX());
                                    commandclone_commandclonestoredtileentity.nbt.setInteger("y", commandclone_commandclonestoredtileentity.pos.getY());
                                    commandclone_commandclonestoredtileentity.nbt.setInteger("z", commandclone_commandclonestoredtileentity.pos.getZ());
                                    tileentity2.readFromNBT(commandclone_commandclonestoredtileentity.nbt);
                                    tileentity2.markDirty();
                                }
                            }

                            iterator1 = list.iterator();

                            while (iterator1.hasNext()) {
                                commandclone_commandclonestoredtileentity = (CommandClone.StaticCloneData) iterator1.next();
                                world.notifyNeighborsRespectDebug(commandclone_commandclonestoredtileentity.pos, commandclone_commandclonestoredtileentity.blockState.getBlock(), false);
                            }

                            List list1 = world.getPendingBlockUpdates(structureboundingbox, false);

                            if (list1 != null) {
                                Iterator iterator2 = list1.iterator();

                                while (iterator2.hasNext()) {
                                    NextTickListEntry nextticklistentry = (NextTickListEntry) iterator2.next();

                                    if (structureboundingbox.isVecInside((Vec3i) nextticklistentry.position)) {
                                        BlockPos blockposition7 = nextticklistentry.position.add((Vec3i) blockposition3);

                                        world.scheduleBlockUpdate(blockposition7, nextticklistentry.getBlock(), (int) (nextticklistentry.scheduledTime - world.getWorldInfo().getWorldTotalTime()), nextticklistentry.priority);
                                    }
                                }
                            }

                            if (i <= 0) {
                                throw new CommandException("commands.clone.failed", new Object[0]);
                            } else {
                                icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                                notifyCommandListener(icommandlistener, (ICommand) this, "commands.clone.success", new Object[] { Integer.valueOf(i)});
                            }
                        } else {
                            throw new CommandException("commands.clone.outOfWorld", new Object[0]);
                        }
                    } else {
                        throw new CommandException("commands.clone.outOfWorld", new Object[0]);
                    }
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? getTabCompletionCoordinate(astring, 0, blockposition) : (astring.length > 3 && astring.length <= 6 ? getTabCompletionCoordinate(astring, 3, blockposition) : (astring.length > 6 && astring.length <= 9 ? getTabCompletionCoordinate(astring, 6, blockposition) : (astring.length == 10 ? getListOfStringsMatchingLastWord(astring, new String[] { "replace", "masked", "filtered"}) : (astring.length == 11 ? getListOfStringsMatchingLastWord(astring, new String[] { "normal", "force", "move"}) : (astring.length == 12 && "filtered".equals(astring[9]) ? getListOfStringsMatchingLastWord(astring, (Collection) Block.REGISTRY.getKeys()) : Collections.emptyList())))));
    }

    static class StaticCloneData {

        public final BlockPos pos;
        public final IBlockState blockState;
        public final NBTTagCompound nbt;

        public StaticCloneData(BlockPos blockposition, IBlockState iblockdata, NBTTagCompound nbttagcompound) {
            this.pos = blockposition;
            this.blockState = iblockdata;
            this.nbt = nbttagcompound;
        }
    }
}
