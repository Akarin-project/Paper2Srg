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

    public String func_71517_b() {
        return "fill";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.fill.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 7) {
            throw new WrongUsageException("commands.fill.usage", new Object[0]);
        } else {
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = func_175757_a(icommandlistener, astring, 0, false);
            BlockPos blockposition1 = func_175757_a(icommandlistener, astring, 3, false);
            Block block = CommandBase.func_147180_g(icommandlistener, astring[6]);
            IBlockState iblockdata;

            if (astring.length >= 8) {
                iblockdata = func_190794_a(block, astring[7]);
            } else {
                iblockdata = block.func_176223_P();
            }

            BlockPos blockposition2 = new BlockPos(Math.min(blockposition.func_177958_n(), blockposition1.func_177958_n()), Math.min(blockposition.func_177956_o(), blockposition1.func_177956_o()), Math.min(blockposition.func_177952_p(), blockposition1.func_177952_p()));
            BlockPos blockposition3 = new BlockPos(Math.max(blockposition.func_177958_n(), blockposition1.func_177958_n()), Math.max(blockposition.func_177956_o(), blockposition1.func_177956_o()), Math.max(blockposition.func_177952_p(), blockposition1.func_177952_p()));
            int i = (blockposition3.func_177958_n() - blockposition2.func_177958_n() + 1) * (blockposition3.func_177956_o() - blockposition2.func_177956_o() + 1) * (blockposition3.func_177952_p() - blockposition2.func_177952_p() + 1);

            if (i > '\u8000') {
                throw new CommandException("commands.fill.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf('\u8000')});
            } else if (blockposition2.func_177956_o() >= 0 && blockposition3.func_177956_o() < 256) {
                World world = icommandlistener.func_130014_f_();

                for (int j = blockposition2.func_177952_p(); j <= blockposition3.func_177952_p(); j += 16) {
                    for (int k = blockposition2.func_177958_n(); k <= blockposition3.func_177958_n(); k += 16) {
                        if (!world.func_175667_e(new BlockPos(k, blockposition3.func_177956_o() - blockposition2.func_177956_o(), j))) {
                            throw new CommandException("commands.fill.outOfWorld", new Object[0]);
                        }
                    }
                }

                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (astring.length >= 10 && block.func_149716_u()) {
                    String s = func_180529_a(astring, 9);

                    try {
                        nbttagcompound = JsonToNBT.func_180713_a(s);
                        flag = true;
                    } catch (NBTException mojangsonparseexception) {
                        throw new CommandException("commands.fill.tagError", new Object[] { mojangsonparseexception.getMessage()});
                    }
                }

                ArrayList arraylist = Lists.newArrayList();

                i = 0;

                for (int l = blockposition2.func_177952_p(); l <= blockposition3.func_177952_p(); ++l) {
                    for (int i1 = blockposition2.func_177956_o(); i1 <= blockposition3.func_177956_o(); ++i1) {
                        for (int j1 = blockposition2.func_177958_n(); j1 <= blockposition3.func_177958_n(); ++j1) {
                            BlockPos blockposition4 = new BlockPos(j1, i1, l);

                            if (astring.length >= 9) {
                                if (!"outline".equals(astring[8]) && !"hollow".equals(astring[8])) {
                                    if ("destroy".equals(astring[8])) {
                                        world.func_175655_b(blockposition4, true);
                                    } else if ("keep".equals(astring[8])) {
                                        if (!world.func_175623_d(blockposition4)) {
                                            continue;
                                        }
                                    } else if ("replace".equals(astring[8]) && !block.func_149716_u() && astring.length > 9) {
                                        Block block1 = CommandBase.func_147180_g(icommandlistener, astring[9]);

                                        if (world.func_180495_p(blockposition4).func_177230_c() != block1 || astring.length > 10 && !"-1".equals(astring[10]) && !"*".equals(astring[10]) && !CommandBase.func_190791_b(block1, astring[10]).apply(world.func_180495_p(blockposition4))) {
                                            continue;
                                        }
                                    }
                                } else if (j1 != blockposition2.func_177958_n() && j1 != blockposition3.func_177958_n() && i1 != blockposition2.func_177956_o() && i1 != blockposition3.func_177956_o() && l != blockposition2.func_177952_p() && l != blockposition3.func_177952_p()) {
                                    if ("hollow".equals(astring[8])) {
                                        world.func_180501_a(blockposition4, Blocks.field_150350_a.func_176223_P(), 2);
                                        arraylist.add(blockposition4);
                                    }
                                    continue;
                                }
                            }

                            TileEntity tileentity = world.func_175625_s(blockposition4);

                            if (tileentity != null && tileentity instanceof IInventory) {
                                ((IInventory) tileentity).func_174888_l();
                            }

                            if (world.func_180501_a(blockposition4, iblockdata, 2)) {
                                arraylist.add(blockposition4);
                                ++i;
                                if (flag) {
                                    TileEntity tileentity1 = world.func_175625_s(blockposition4);

                                    if (tileentity1 != null) {
                                        nbttagcompound.func_74768_a("x", blockposition4.func_177958_n());
                                        nbttagcompound.func_74768_a("y", blockposition4.func_177956_o());
                                        nbttagcompound.func_74768_a("z", blockposition4.func_177952_p());
                                        tileentity1.func_145839_a(nbttagcompound);
                                    }
                                }
                            }
                        }
                    }
                }

                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    BlockPos blockposition5 = (BlockPos) iterator.next();
                    Block block2 = world.func_180495_p(blockposition5).func_177230_c();

                    world.func_175722_b(blockposition5, block2, false);
                }

                if (i <= 0) {
                    throw new CommandException("commands.fill.failed", new Object[0]);
                } else {
                    icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.fill.success", new Object[] { Integer.valueOf(i)});
                }
            } else {
                throw new CommandException("commands.fill.outOfWorld", new Object[0]);
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockposition) : (astring.length > 3 && astring.length <= 6 ? func_175771_a(astring, 3, blockposition) : (astring.length == 7 ? func_175762_a(astring, (Collection) Block.field_149771_c.func_148742_b()) : (astring.length == 9 ? func_71530_a(astring, new String[] { "replace", "destroy", "keep", "hollow", "outline"}) : (astring.length == 10 && "replace".equals(astring[8]) ? func_175762_a(astring, (Collection) Block.field_149771_c.func_148742_b()) : Collections.emptyList()))));
    }
}
