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

    public String func_71517_b() {
        return "clone";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.clone.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 9) {
            throw new WrongUsageException("commands.clone.usage", new Object[0]);
        } else {
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = func_175757_a(icommandlistener, astring, 0, false);
            BlockPos blockposition1 = func_175757_a(icommandlistener, astring, 3, false);
            BlockPos blockposition2 = func_175757_a(icommandlistener, astring, 6, false);
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockposition, blockposition1);
            StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockposition2, blockposition2.func_177971_a(structureboundingbox.func_175896_b()));
            int i = structureboundingbox.func_78883_b() * structureboundingbox.func_78882_c() * structureboundingbox.func_78880_d();

            if (i > '\u8000') {
                throw new CommandException("commands.clone.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf('\u8000')});
            } else {
                boolean flag = false;
                Block block = null;
                Predicate predicate = null;

                if ((astring.length < 11 || !"force".equals(astring[10]) && !"move".equals(astring[10])) && structureboundingbox.func_78884_a(structureboundingbox1)) {
                    throw new CommandException("commands.clone.noOverlap", new Object[0]);
                } else {
                    if (astring.length >= 11 && "move".equals(astring[10])) {
                        flag = true;
                    }

                    if (structureboundingbox.field_78895_b >= 0 && structureboundingbox.field_78894_e < 256 && structureboundingbox1.field_78895_b >= 0 && structureboundingbox1.field_78894_e < 256) {
                        World world = icommandlistener.func_130014_f_();

                        if (world.func_175711_a(structureboundingbox) && world.func_175711_a(structureboundingbox1)) {
                            boolean flag1 = false;

                            if (astring.length >= 10) {
                                if ("masked".equals(astring[9])) {
                                    flag1 = true;
                                } else if ("filtered".equals(astring[9])) {
                                    if (astring.length < 12) {
                                        throw new WrongUsageException("commands.clone.usage", new Object[0]);
                                    }

                                    block = func_147180_g(icommandlistener, astring[11]);
                                    if (astring.length >= 13) {
                                        predicate = func_190791_b(block, astring[12]);
                                    }
                                }
                            }

                            ArrayList arraylist = Lists.newArrayList();
                            ArrayList arraylist1 = Lists.newArrayList();
                            ArrayList arraylist2 = Lists.newArrayList();
                            LinkedList linkedlist = Lists.newLinkedList();
                            BlockPos blockposition3 = new BlockPos(structureboundingbox1.field_78897_a - structureboundingbox.field_78897_a, structureboundingbox1.field_78895_b - structureboundingbox.field_78895_b, structureboundingbox1.field_78896_c - structureboundingbox.field_78896_c);

                            for (int j = structureboundingbox.field_78896_c; j <= structureboundingbox.field_78892_f; ++j) {
                                for (int k = structureboundingbox.field_78895_b; k <= structureboundingbox.field_78894_e; ++k) {
                                    for (int l = structureboundingbox.field_78897_a; l <= structureboundingbox.field_78893_d; ++l) {
                                        BlockPos blockposition4 = new BlockPos(l, k, j);
                                        BlockPos blockposition5 = blockposition4.func_177971_a((Vec3i) blockposition3);
                                        IBlockState iblockdata = world.func_180495_p(blockposition4);

                                        if ((!flag1 || iblockdata.func_177230_c() != Blocks.field_150350_a) && (block == null || iblockdata.func_177230_c() == block && (predicate == null || predicate.apply(iblockdata)))) {
                                            TileEntity tileentity = world.func_175625_s(blockposition4);

                                            if (tileentity != null) {
                                                NBTTagCompound nbttagcompound = tileentity.func_189515_b(new NBTTagCompound());

                                                arraylist1.add(new CommandClone.StaticCloneData(blockposition5, iblockdata, nbttagcompound));
                                                linkedlist.addLast(blockposition4);
                                            } else if (!iblockdata.func_185913_b() && !iblockdata.func_185917_h()) {
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

                                for (iterator = linkedlist.iterator(); iterator.hasNext(); world.func_180501_a(blockposition6, Blocks.field_180401_cv.func_176223_P(), 2)) {
                                    blockposition6 = (BlockPos) iterator.next();
                                    TileEntity tileentity1 = world.func_175625_s(blockposition6);

                                    if (tileentity1 instanceof IInventory) {
                                        ((IInventory) tileentity1).func_174888_l();
                                    }
                                }

                                iterator = linkedlist.iterator();

                                while (iterator.hasNext()) {
                                    blockposition6 = (BlockPos) iterator.next();
                                    world.func_180501_a(blockposition6, Blocks.field_150350_a.func_176223_P(), 3);
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

                            for (iterator1 = list.iterator(); iterator1.hasNext(); world.func_180501_a(commandclone_commandclonestoredtileentity.field_179537_a, Blocks.field_180401_cv.func_176223_P(), 2)) {
                                commandclone_commandclonestoredtileentity = (CommandClone.StaticCloneData) iterator1.next();
                                tileentity2 = world.func_175625_s(commandclone_commandclonestoredtileentity.field_179537_a);
                                if (tileentity2 instanceof IInventory) {
                                    ((IInventory) tileentity2).func_174888_l();
                                }
                            }

                            i = 0;
                            iterator1 = arraylist3.iterator();

                            while (iterator1.hasNext()) {
                                commandclone_commandclonestoredtileentity = (CommandClone.StaticCloneData) iterator1.next();
                                if (world.func_180501_a(commandclone_commandclonestoredtileentity.field_179537_a, commandclone_commandclonestoredtileentity.field_179535_b, 2)) {
                                    ++i;
                                }
                            }

                            for (iterator1 = arraylist1.iterator(); iterator1.hasNext(); world.func_180501_a(commandclone_commandclonestoredtileentity.field_179537_a, commandclone_commandclonestoredtileentity.field_179535_b, 2)) {
                                commandclone_commandclonestoredtileentity = (CommandClone.StaticCloneData) iterator1.next();
                                tileentity2 = world.func_175625_s(commandclone_commandclonestoredtileentity.field_179537_a);
                                if (commandclone_commandclonestoredtileentity.field_184953_c != null && tileentity2 != null) {
                                    commandclone_commandclonestoredtileentity.field_184953_c.func_74768_a("x", commandclone_commandclonestoredtileentity.field_179537_a.func_177958_n());
                                    commandclone_commandclonestoredtileentity.field_184953_c.func_74768_a("y", commandclone_commandclonestoredtileentity.field_179537_a.func_177956_o());
                                    commandclone_commandclonestoredtileentity.field_184953_c.func_74768_a("z", commandclone_commandclonestoredtileentity.field_179537_a.func_177952_p());
                                    tileentity2.func_145839_a(commandclone_commandclonestoredtileentity.field_184953_c);
                                    tileentity2.func_70296_d();
                                }
                            }

                            iterator1 = list.iterator();

                            while (iterator1.hasNext()) {
                                commandclone_commandclonestoredtileentity = (CommandClone.StaticCloneData) iterator1.next();
                                world.func_175722_b(commandclone_commandclonestoredtileentity.field_179537_a, commandclone_commandclonestoredtileentity.field_179535_b.func_177230_c(), false);
                            }

                            List list1 = world.func_175712_a(structureboundingbox, false);

                            if (list1 != null) {
                                Iterator iterator2 = list1.iterator();

                                while (iterator2.hasNext()) {
                                    NextTickListEntry nextticklistentry = (NextTickListEntry) iterator2.next();

                                    if (structureboundingbox.func_175898_b((Vec3i) nextticklistentry.field_180282_a)) {
                                        BlockPos blockposition7 = nextticklistentry.field_180282_a.func_177971_a((Vec3i) blockposition3);

                                        world.func_180497_b(blockposition7, nextticklistentry.func_151351_a(), (int) (nextticklistentry.field_77180_e - world.func_72912_H().func_82573_f()), nextticklistentry.field_82754_f);
                                    }
                                }
                            }

                            if (i <= 0) {
                                throw new CommandException("commands.clone.failed", new Object[0]);
                            } else {
                                icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                                func_152373_a(icommandlistener, (ICommand) this, "commands.clone.success", new Object[] { Integer.valueOf(i)});
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

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockposition) : (astring.length > 3 && astring.length <= 6 ? func_175771_a(astring, 3, blockposition) : (astring.length > 6 && astring.length <= 9 ? func_175771_a(astring, 6, blockposition) : (astring.length == 10 ? func_71530_a(astring, new String[] { "replace", "masked", "filtered"}) : (astring.length == 11 ? func_71530_a(astring, new String[] { "normal", "force", "move"}) : (astring.length == 12 && "filtered".equals(astring[9]) ? func_175762_a(astring, (Collection) Block.field_149771_c.func_148742_b()) : Collections.emptyList())))));
    }

    static class StaticCloneData {

        public final BlockPos field_179537_a;
        public final IBlockState field_179535_b;
        public final NBTTagCompound field_184953_c;

        public StaticCloneData(BlockPos blockposition, IBlockState iblockdata, NBTTagCompound nbttagcompound) {
            this.field_179537_a = blockposition;
            this.field_179535_b = iblockdata;
            this.field_184953_c = nbttagcompound;
        }
    }
}
