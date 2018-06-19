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

    public String func_71517_b() {
        return "testforblocks";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.compare.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 9) {
            throw new WrongUsageException("commands.compare.usage", new Object[0]);
        } else {
            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
            BlockPos blockposition = func_175757_a(icommandlistener, astring, 0, false);
            BlockPos blockposition1 = func_175757_a(icommandlistener, astring, 3, false);
            BlockPos blockposition2 = func_175757_a(icommandlistener, astring, 6, false);
            StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockposition, blockposition1);
            StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockposition2, blockposition2.func_177971_a(structureboundingbox.func_175896_b()));
            int i = structureboundingbox.func_78883_b() * structureboundingbox.func_78882_c() * structureboundingbox.func_78880_d();

            if (i > 524288) {
                throw new CommandException("commands.compare.tooManyBlocks", new Object[] { Integer.valueOf(i), Integer.valueOf(524288)});
            } else if (structureboundingbox.field_78895_b >= 0 && structureboundingbox.field_78894_e < 256 && structureboundingbox1.field_78895_b >= 0 && structureboundingbox1.field_78894_e < 256) {
                World world = icommandlistener.func_130014_f_();

                if (world.func_175711_a(structureboundingbox) && world.func_175711_a(structureboundingbox1)) {
                    boolean flag = false;

                    if (astring.length > 9 && "masked".equals(astring[9])) {
                        flag = true;
                    }

                    i = 0;
                    BlockPos blockposition3 = new BlockPos(structureboundingbox1.field_78897_a - structureboundingbox.field_78897_a, structureboundingbox1.field_78895_b - structureboundingbox.field_78895_b, structureboundingbox1.field_78896_c - structureboundingbox.field_78896_c);
                    BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();
                    BlockPos.MutableBlockPos blockposition_mutableblockposition1 = new BlockPos.MutableBlockPos();

                    for (int j = structureboundingbox.field_78896_c; j <= structureboundingbox.field_78892_f; ++j) {
                        for (int k = structureboundingbox.field_78895_b; k <= structureboundingbox.field_78894_e; ++k) {
                            for (int l = structureboundingbox.field_78897_a; l <= structureboundingbox.field_78893_d; ++l) {
                                blockposition_mutableblockposition.func_181079_c(l, k, j);
                                blockposition_mutableblockposition1.func_181079_c(l + blockposition3.func_177958_n(), k + blockposition3.func_177956_o(), j + blockposition3.func_177952_p());
                                boolean flag1 = false;
                                IBlockState iblockdata = world.func_180495_p(blockposition_mutableblockposition);

                                if (!flag || iblockdata.func_177230_c() != Blocks.field_150350_a) {
                                    if (iblockdata == world.func_180495_p(blockposition_mutableblockposition1)) {
                                        TileEntity tileentity = world.func_175625_s(blockposition_mutableblockposition);
                                        TileEntity tileentity1 = world.func_175625_s(blockposition_mutableblockposition1);

                                        if (tileentity != null && tileentity1 != null) {
                                            NBTTagCompound nbttagcompound = tileentity.func_189515_b(new NBTTagCompound());

                                            nbttagcompound.func_82580_o("x");
                                            nbttagcompound.func_82580_o("y");
                                            nbttagcompound.func_82580_o("z");
                                            NBTTagCompound nbttagcompound1 = tileentity1.func_189515_b(new NBTTagCompound());

                                            nbttagcompound1.func_82580_o("x");
                                            nbttagcompound1.func_82580_o("y");
                                            nbttagcompound1.func_82580_o("z");
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

                    icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_BLOCKS, i);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.compare.success", new Object[] { Integer.valueOf(i)});
                } else {
                    throw new CommandException("commands.compare.outOfWorld", new Object[0]);
                }
            } else {
                throw new CommandException("commands.compare.outOfWorld", new Object[0]);
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length > 0 && astring.length <= 3 ? func_175771_a(astring, 0, blockposition) : (astring.length > 3 && astring.length <= 6 ? func_175771_a(astring, 3, blockposition) : (astring.length > 6 && astring.length <= 9 ? func_175771_a(astring, 6, blockposition) : (astring.length == 10 ? func_71530_a(astring, new String[] { "masked", "all"}) : Collections.emptyList())));
    }
}
