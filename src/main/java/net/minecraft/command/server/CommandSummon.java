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

    public String getName() {
        return "summon";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender icommandlistener) {
        return "commands.summon.usage";
    }

    public void execute(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 1) {
            throw new WrongUsageException("commands.summon.usage", new Object[0]);
        } else {
            String s = astring[0];
            BlockPos blockposition = icommandlistener.getPosition();
            Vec3d vec3d = icommandlistener.getPositionVector();
            double d0 = vec3d.x;
            double d1 = vec3d.y;
            double d2 = vec3d.z;

            if (astring.length >= 4) {
                d0 = parseDouble(d0, astring[1], true);
                d1 = parseDouble(d1, astring[2], false);
                d2 = parseDouble(d2, astring[3], true);
                blockposition = new BlockPos(d0, d1, d2);
            }

            World world = icommandlistener.getEntityWorld();

            if (!world.isBlockLoaded(blockposition)) {
                throw new CommandException("commands.summon.outOfWorld", new Object[0]);
            } else if (EntityList.LIGHTNING_BOLT.equals(new ResourceLocation(s))) {
                world.addWeatherEffect(new EntityLightningBolt(world, d0, d1, d2, false));
                notifyCommandListener(icommandlistener, (ICommand) this, "commands.summon.success", new Object[0]);
            } else {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (astring.length >= 5) {
                    String s1 = buildString(astring, 4);

                    try {
                        nbttagcompound = JsonToNBT.getTagFromJson(s1);
                        flag = true;
                    } catch (NBTException mojangsonparseexception) {
                        throw new CommandException("commands.summon.tagError", new Object[] { mojangsonparseexception.getMessage()});
                    }
                }

                nbttagcompound.setString("id", s);
                Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, true);

                if (entity == null) {
                    throw new CommandException("commands.summon.failed", new Object[0]);
                } else {
                    entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
                    if (!flag && entity instanceof EntityLiving) {
                        ((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData) null);
                    }

                    notifyCommandListener(icommandlistener, (ICommand) this, "commands.summon.success", new Object[0]);
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? getListOfStringsMatchingLastWord(astring, (Collection) EntityList.getEntityNameList()) : (astring.length > 1 && astring.length <= 4 ? getTabCompletionCoordinate(astring, 1, blockposition) : Collections.emptyList());
    }
}
