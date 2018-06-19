package net.minecraft.command.server;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public class CommandSummon extends CommandBase {

    public CommandSummon() {}

    public String func_71517_b() {
        return "summon";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.summon.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        } else {
            String s = astring[0];
            BlockPos blockposition = icommandlistener.func_180425_c();
            Vec3d vec3d = icommandlistener.func_174791_d();
            double d0 = vec3d.field_72450_a;
            double d1 = vec3d.field_72448_b;
            double d2 = vec3d.field_72449_c;

            if (astring.length >= 4) {
                d0 = func_175761_b(d0, astring[1], true);
                d1 = func_175761_b(d1, astring[2], false);
                d2 = func_175761_b(d2, astring[3], true);
                blockposition = new BlockPos(d0, d1, d2);
            }

            World world = icommandlistener.func_130014_f_();

            if (!world.func_175667_e(blockposition)) {
                throw new CommandException("commands.summon.outOfWorld", new Object[0]);
            } else if (EntityList.field_191307_a.equals(new ResourceLocation(s))) {
                world.func_72942_c(new EntityLightningBolt(world, d0, d1, d2, false));
                func_152373_a(icommandlistener, (ICommand) this, "commands.summon.success", new Object[0]);
            } else {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (astring.length >= 5) {
                    String s1 = func_180529_a(astring, 4);

                    try {
                        nbttagcompound = JsonToNBT.func_180713_a(s1);
                        flag = true;
                    } catch (NBTException mojangsonparseexception) {
                        throw new CommandException("commands.summon.tagError", new Object[] { mojangsonparseexception.getMessage()});
                    }
                }

                nbttagcompound.func_74778_a("id", s);
                Entity entity = AnvilChunkLoader.func_186054_a(nbttagcompound, world, d0, d1, d2, true);

                if (entity == null) {
                    throw new CommandException("commands.summon.failed", new Object[0]);
                } else {
                    entity.func_70012_b(d0, d1, d2, entity.field_70177_z, entity.field_70125_A);
                    if (!flag && entity instanceof EntityLiving) {
                        ((EntityLiving) entity).func_180482_a(world.func_175649_E(new BlockPos(entity)), (IEntityLivingData) null);
                    }

                    func_152373_a(icommandlistener, (ICommand) this, "commands.summon.success", new Object[0]);
                }
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_175762_a(astring, (Collection) EntityList.func_180124_b()) : (astring.length > 1 && astring.length <= 4 ? func_175771_a(astring, 1, blockposition) : Collections.emptyList());
    }
}
