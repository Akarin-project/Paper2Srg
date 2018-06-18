package net.minecraft.tileentity;

import javax.annotation.Nullable;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TileEntitySign.ISignCommandListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class TileEntitySign extends TileEntity {

    public final ITextComponent[] signText = new ITextComponent[] { new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("")};
    public int lineBeingEdited = -1;
    public boolean isEditable = true;
    private EntityPlayer player;
    private final CommandResultStats stats = new CommandResultStats();

    public TileEntitySign() {}

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        for (int i = 0; i < 4; ++i) {
            String s = ITextComponent.Serializer.componentToJson(this.signText[i]);

            nbttagcompound.setString("Text" + (i + 1), s);
        }

        // CraftBukkit start
        if (Boolean.getBoolean("convertLegacySigns")) {
            nbttagcompound.setBoolean("Bukkit.isConverted", true);
        }
        // CraftBukkit end

        this.stats.writeStatsToNBT(nbttagcompound);
        return nbttagcompound;
    }

    protected void setWorldCreate(World world) {
        this.setWorld(world);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.isEditable = false;
        super.readFromNBT(nbttagcompound);
        ICommandSender icommandlistener = new ISignCommandListener() { // Paper
            public String getName() {
                return "Sign";
            }

            public boolean canUseCommand(int i, String s) {
                return true;
            }

            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }

            public Vec3d getPositionVector() {
                return new Vec3d((double) TileEntitySign.this.pos.getX() + 0.5D, (double) TileEntitySign.this.pos.getY() + 0.5D, (double) TileEntitySign.this.pos.getZ() + 0.5D);
            }

            public World getEntityWorld() {
                return TileEntitySign.this.world;
            }

            public MinecraftServer getServer() {
                return MinecraftServer.getServer(); // Paper - world may be null
            }
        };

        // CraftBukkit start - Add an option to convert signs correctly
        // This is done with a flag instead of all the time because
        // we have no way to tell whether a sign is from 1.7.10 or 1.8

        boolean oldSign = Boolean.getBoolean("convertLegacySigns") && !nbttagcompound.getBoolean("Bukkit.isConverted");

        for (int i = 0; i < 4; ++i) {
            String s = nbttagcompound.getString("Text" + (i + 1));
            if (s != null && s.length() > 2048) {
                s = "\"\"";
            }

            try {
                //IChatBaseComponent ichatbasecomponent = IChatBaseComponent.ChatSerializer.a(s); // Paper - move down - the old format might throw a json error

                if (oldSign && !isLoadingStructure) { // Paper - saved structures will be in the new format, but will not have isConverted
                    signText[i] = org.bukkit.craftbukkit.util.CraftChatMessage.fromString(s)[0];
                    continue;
                }
                // CraftBukkit end
                ITextComponent ichatbasecomponent = ITextComponent.Serializer.jsonToComponent(s); // Paper - after old sign

                try {
                    this.signText[i] = TextComponentUtils.processComponent(icommandlistener, ichatbasecomponent, (Entity) null);
                } catch (CommandException commandexception) {
                    this.signText[i] = ichatbasecomponent;
                }
            } catch (com.google.gson.JsonParseException jsonparseexception) {
                this.signText[i] = new TextComponentString(s);
            }
        }

        this.stats.readStatsFromNBT(nbttagcompound);
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 9, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    public boolean getIsEditable() {
        return this.isEditable;
    }

    public void setPlayer(EntityPlayer entityhuman) {
        this.player = entityhuman;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public boolean executeCommand(final EntityPlayer entityhuman) {
        ICommandSender icommandlistener = new ISignCommandListener() { // Paper
            public String getName() {
                return entityhuman.getName();
            }

            public ITextComponent getDisplayName() {
                return entityhuman.getDisplayName();
            }

            public void sendMessage(ITextComponent ichatbasecomponent) {}

            public boolean canUseCommand(int i, String s) {
                return i <= 2;
            }

            public BlockPos getPosition() {
                return TileEntitySign.this.pos;
            }

            public Vec3d getPositionVector() {
                return new Vec3d((double) TileEntitySign.this.pos.getX() + 0.5D, (double) TileEntitySign.this.pos.getY() + 0.5D, (double) TileEntitySign.this.pos.getZ() + 0.5D);
            }

            public World getEntityWorld() {
                return entityhuman.getEntityWorld();
            }

            public Entity getCommandSenderEntity() {
                return entityhuman;
            }

            public boolean sendCommandFeedback() {
                return false;
            }

            public void setCommandStat(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
                if (TileEntitySign.this.world != null && !TileEntitySign.this.world.isRemote) {
                    TileEntitySign.this.stats.setCommandStatForSender(TileEntitySign.this.world.getMinecraftServer(), this, commandobjectiveexecutor_enumcommandresult, i);
                }

            }

            public MinecraftServer getServer() {
                return entityhuman.getServer();
            }
        };
        ITextComponent[] aichatbasecomponent = this.signText;
        int i = aichatbasecomponent.length;

        for (int j = 0; j < i; ++j) {
            ITextComponent ichatbasecomponent = aichatbasecomponent[j];
            Style chatmodifier = ichatbasecomponent == null ? null : ichatbasecomponent.getStyle();

            if (chatmodifier != null && chatmodifier.getClickEvent() != null) {
                ClickEvent chatclickable = chatmodifier.getClickEvent();

                if (chatclickable.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    // CraftBukkit start
                    // entityhuman.C_().getCommandHandler().a(icommandlistener, chatclickable.b());
                    CommandBlockBaseLogic.executeSafely(icommandlistener, new org.bukkit.craftbukkit.command.ProxiedNativeCommandSender(
                            icommandlistener,
                            new org.bukkit.craftbukkit.command.CraftBlockCommandSender(icommandlistener),
                            entityhuman.getBukkitEntity()
                    ), chatclickable.getValue());
                    // CraftBukkit end
                }
            }
        }

        return true;
    }

    public CommandResultStats getStats() {
        return this.stats;
    }
    public interface ISignCommandListener extends ICommandSender {} // Paper
}
