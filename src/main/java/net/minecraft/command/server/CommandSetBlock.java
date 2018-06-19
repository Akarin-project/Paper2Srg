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

    public String func_71517_b() {
        return "setblock";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.setblock.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 4) {
            throw new WrongUsageException("commands.setblock.usage", new Object[0]);
        } else {
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = func_175757_a(icommandlistener, astring, 0, false);
            Block block = CommandBase.func_147180_g(icommandlistener, astring[3]);
            IBlockState iblockdata;

            if (astring.length >= 5) {
                iblockdata = func_190794_a(block, astring[4]);
            } else {
                iblockdata = block.func_176223_P();
            }

            World world = icommandlistener.func_130014_f_();

            if (!world.func_175667_e(blockposition)) {
                throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
            } else {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (astring.length >= 7 && block.func_149716_u()) {
                    String s = func_180529_a(astring, 6);

                    try {
                        nbttagcompound = JsonToNBT.func_180713_a(s);
                        flag = true;
                    } catch (NBTException mojangsonparseexception) {
                        throw new CommandException("commands.setblock.tagError", new Object[] { mojangsonparseexception.getMessage()});
                    }
                }

                if (astring.length >= 6) {
                    if ("destroy".equals(astring[5])) {
                        world.func_175655_b(blockposition, true);
                        if (block == Blocks.field_150350_a) {
                            func_152373_a(icommandlistener, (ICommand) this, "commands.setblock.success", new Object[0]);
                            return;
                        }
                    } else if ("keep".equals(astring[5]) && !world.func_175623_d(blockposition)) {
                        throw new CommandException("commands.setblock.noChange", new Object[0]);
                    }
                }

                TileEntity tileentity = world.func_175625_s(blockposition);

                if (tileentity != null && tileentity instanceof IInventory) {
                    ((IInventory) tileentity).func_174888_l();
                }

                if (!world.func_180501_a(blockposition, iblockdata, 2)) {
                    throw new CommandException("commands.setblock.noChange", new Object[0]);
                } else {
                    if (flag) {
                        TileEntity tileentity1 = world.func_175625_s(blockposition);

                        if (tileentity1 != null) {
                            nbttagcompound.func_74768_a("x", blockposition.func_177958_n());
                            nbttagcompound.func_74768_a("y", blockposition.func_177956_o());
                            nbttagcompound.func_74768_a("z", blockposition.func_177952_p());
                            tileentity1.func_145839_a(nbttagcompound);
                        }
                    }

                    world.func_175722_b(blockposition, iblockdata.func_177230_c(), false);
                    icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.setblock.success", new Object[0]);
                }
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockposition) : (astring.length == 4 ? func_175762_a(astring, (Collection) Block.field_149771_c.func_148742_b()) : (astring.length == 6 ? func_71530_a(astring, new String[] { "replace", "destroy", "keep"}) : Collections.emptyList()));
    }
}
