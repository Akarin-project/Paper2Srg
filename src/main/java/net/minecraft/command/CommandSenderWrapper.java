package net.minecraft.command;

import java.util.Objects;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CommandSenderWrapper implements ICommandSender {

    public final ICommandSender delegate;
    @Nullable
    private final Vec3d positionVector;
    @Nullable
    private final BlockPos position;
    @Nullable
    private final Integer permissionLevel;
    @Nullable
    private final Entity entity;
    @Nullable
    private final Boolean sendCommandFeedback;

    public CommandSenderWrapper(ICommandSender icommandlistener, @Nullable Vec3d vec3d, @Nullable BlockPos blockposition, @Nullable Integer integer, @Nullable Entity entity, @Nullable Boolean obool) {
        this.delegate = icommandlistener;
        this.positionVector = vec3d;
        this.position = blockposition;
        this.permissionLevel = integer;
        this.entity = entity;
        this.sendCommandFeedback = obool;
    }

    public static CommandSenderWrapper create(ICommandSender icommandlistener) {
        return icommandlistener instanceof CommandSenderWrapper ? (CommandSenderWrapper) icommandlistener : new CommandSenderWrapper(icommandlistener, (Vec3d) null, (BlockPos) null, (Integer) null, (Entity) null, (Boolean) null);
    }

    public CommandSenderWrapper withEntity(Entity entity, Vec3d vec3d) {
        return this.entity == entity && Objects.equals(this.positionVector, vec3d) ? this : new CommandSenderWrapper(this.delegate, vec3d, new BlockPos(vec3d), this.permissionLevel, entity, this.sendCommandFeedback);
    }

    public CommandSenderWrapper withPermissionLevel(int i) {
        return this.permissionLevel != null && this.permissionLevel.intValue() <= i ? this : new CommandSenderWrapper(this.delegate, this.positionVector, this.position, Integer.valueOf(i), this.entity, this.sendCommandFeedback);
    }

    public CommandSenderWrapper withSendCommandFeedback(boolean flag) {
        return this.sendCommandFeedback != null && (!this.sendCommandFeedback.booleanValue() || flag) ? this : new CommandSenderWrapper(this.delegate, this.positionVector, this.position, this.permissionLevel, this.entity, Boolean.valueOf(flag));
    }

    public CommandSenderWrapper computePositionVector() {
        return this.positionVector != null ? this : new CommandSenderWrapper(this.delegate, this.getPositionVector(), this.getPosition(), this.permissionLevel, this.entity, this.sendCommandFeedback);
    }

    public String getName() {
        return this.entity != null ? this.entity.getName() : this.delegate.getName();
    }

    public ITextComponent getDisplayName() {
        return this.entity != null ? this.entity.getDisplayName() : this.delegate.getDisplayName();
    }

    public void sendMessage(ITextComponent ichatbasecomponent) {
        if (this.sendCommandFeedback == null || this.sendCommandFeedback.booleanValue()) {
            this.delegate.sendMessage(ichatbasecomponent);
        }
    }

    public boolean canUseCommand(int i, String s) {
        return this.permissionLevel != null && this.permissionLevel.intValue() < i ? false : this.delegate.canUseCommand(i, s);
    }

    public BlockPos getPosition() {
        return this.position != null ? this.position : (this.entity != null ? this.entity.getPosition() : this.delegate.getPosition());
    }

    public Vec3d getPositionVector() {
        return this.positionVector != null ? this.positionVector : (this.entity != null ? this.entity.getPositionVector() : this.delegate.getPositionVector());
    }

    public World getEntityWorld() {
        return this.entity != null ? this.entity.getEntityWorld() : this.delegate.getEntityWorld();
    }

    @Nullable
    public Entity getCommandSenderEntity() {
        return this.entity != null ? this.entity.getCommandSenderEntity() : this.delegate.getCommandSenderEntity();
    }

    public boolean sendCommandFeedback() {
        return this.sendCommandFeedback != null ? this.sendCommandFeedback.booleanValue() : this.delegate.sendCommandFeedback();
    }

    public void setCommandStat(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
        if (this.entity != null) {
            this.entity.setCommandStat(commandobjectiveexecutor_enumcommandresult, i);
        } else {
            this.delegate.setCommandStat(commandobjectiveexecutor_enumcommandresult, i);
        }
    }

    @Nullable
    public MinecraftServer getServer() {
        return this.delegate.getServer();
    }
}
