package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare extends CommandBase {

    public CommandCompare() {}

    public String getName() {
        return "testforblocks";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.compare.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 9) {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        } else {
            icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = parseBlockPos(icommandlistener, astring, 0, false);
            BlockPos blockposition1 = parseBlockPos(icommandlistener, astring, 3, false);
            BlockPos blockposition2 = parseBlockPos(icommandlistener, astring, 6, false);
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockposition, blockposition1);
            StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockposition2, blockposition2.add(structureboundingbox.getLength()));
            int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();

            if (i > 524288) {
                throw new CommandException("commands.compare.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf(524288)});
            } else if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256 && structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256) {
                World world = icommandlistener.getEntityWorld();

                if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox1)) {
                    boolean flag = false;

                    if (astring.length > 9 && "masked".equals(astring[9])) {
                        flag = true;
                    }

                    i = 0;
                    BlockPos blockposition3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);
                    BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();
                    BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();

                    for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; ++j) {
                        for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; ++k) {
                            for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; ++l) {
                                blockposition_mutableblockposition.setPos(l, k, j);
                                blockposition_mutableblockposition1.setPos(l + blockposition3.getX(), k + blockposition3.getY(), j + blockposition3.getZ());
                                boolean flag1 = false;
                                IBlockState iblockdata = world.getBlockState(blockposition_mutableblockposition);

                                if (!flag || iblockdata.getBlock() != Blocks.AIR) {
                                    if (iblockdata == world.getBlockState(blockposition_mutableblockposition1)) {
                                        TileEntity tileentity = world.getTileEntity(blockposition_mutableblockposition);
                                        TileEntity tileentity1 = world.getTileEntity(blockposition_mutableblockposition1);

                                        if (tileentity != null && tileentity1 != null) {
                                            NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());

                                            nbttagcompound.removeTag("x");
                                            nbttagcompound.removeTag("y");
                                            nbttagcompound.removeTag("z");
                                            NBTTagCompound nbttagcompound1 = tileentity1.writeToNBT(new NBTTagCompound());

                                            nbttagcompound1.removeTag("x");
                                            nbttagcompound1.removeTag("y");
                                            nbttagcompound1.removeTag("z");
                                            if (!nbttagcompound.equals(nbttagcompound1)) {
                                                flag1 = true;
                                            }
                                        } else if (tileentity != null) {
                                            flag1 = true;
                                        }
                                    } else {
                                        flag1 = true;
                                    }

                                    ++i;
                                    if (flag1) {
                                        throw new CommandException("commands.compare.failed", new Object[0]);
                                    }
                                }
                            }
                        }
                    }

                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.compare.success", new Object[] { Integer.valueOf(i)});
                } else {
                    throw new CommandException("commands.compare.outOfWorld", new Object[0]);
                }
            } else {
                throw new CommandException("commands.compare.outOfWorld", new Object[0]);
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? getTabCompletionCoordinate(astring, 0, blockposition) : (astring.length > 3 && astring.length <= 6 ? getTabCompletionCoordinate(astring, 3, blockposition) : (astring.length > 6 && astring.length <= 9 ? getTabCompletionCoordinate(astring, 6, blockposition) : (astring.length == 10 ? getListOfStringsMatchingLastWord(astring, new String[] { "masked", "all"}) : Collections.emptyList())));
    }
}
