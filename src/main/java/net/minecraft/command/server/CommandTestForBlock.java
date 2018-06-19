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

    public String func_71517_b() {
        return "testforblock";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.testforblock.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 4) {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        } else {
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = func_175757_a(icommandlistener, astring, 0, false);
            Block block = func_147180_g(icommandlistener, astring[3]);

            if (block == null) {
                throw new NumberInvalidException("commands.setblock.notFound", new Object[] { astring[3]});
            } else {
                World world = icommandlistener.func_130014_f_();

                if (!world.func_175667_e(blockposition)) {
                    throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
                } else {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    boolean flag = false;

                    if (astring.length >= 6 && block.func_149716_u()) {
                        String s = func_180529_a(astring, 5);

                        try {
                            nbttagcompound = JsonToNBT.func_180713_a(s);
                            flag = true;
                        } catch (NBTException mojangsonparseexception) {
                            throw new CommandException("commands.setblock.tagError", new Object[] { mojangsonparseexception.getMessage()});
                        }
                    }

                    IBlockState iblockdata = world.func_180495_p(blockposition);
                    Block block1 = iblockdata.func_177230_c();

                    if (block1 != block) {
                        throw new CommandException("commands.testforblock.failed.tile", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p()), block1.func_149732_F(), block.func_149732_F()});
                    } else if (astring.length >= 5 && !CommandBase.func_190791_b(block, astring[4]).apply(iblockdata)) {
                        try {
                            int i = iblockdata.func_177230_c().func_176201_c(iblockdata);

                            throw new CommandException("commands.testforblock.failed.data", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p()), Integer.valueOf(i), Integer.valueOf(Integer.parseInt(astring[4]))});
                        } catch (NumberFormatException numberformatexception) {
                            throw new CommandException("commands.testforblock.failed.data", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p()), iblockdata.toString(), astring[4]});
                        }
                    } else {
                        if (flag) {
                            TileEntity tileentity = world.func_175625_s(blockposition);

                            if (tileentity == null) {
                                throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p())});
                            }

                            NBTTagCompound nbttagcompound1 = tileentity.func_189515_b(new NBTTagCompound());

                            if (!NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true)) {
                                throw new CommandException("commands.testforblock.failed.nbt", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p())});
                            }
                        }

                        icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                        func_152373_a(icommandlistener, (ICommand) this, "commands.testforblock.success", new Object[] { Integer.valueOf(blockposition.func_177958_n()), Integer.valueOf(blockposition.func_177956_o()), Integer.valueOf(blockposition.func_177952_p())});
                    }
                }
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockposition) : (astring.length == 4 ? func_175762_a(astring, (Collection) Block.field_149771_c.func_148742_b()) : Collections.emptyList());
    }
}
