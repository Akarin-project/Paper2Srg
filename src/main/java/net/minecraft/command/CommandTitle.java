package net.minecraft.command;

import com.google.gson.JsonParseException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;

public class CommandTitle extends CommandBase {

    private static final Logger field_175774_a = LogManager.getLogger();

    public CommandTitle() {}

    public String func_71517_b() {
        return "title";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.title.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.title.usage", new Object[0]);
        } else {
            if (astring.length < 3) {
                if ("title".equals(astring[1]) || "subtitle".equals(astring[1]) || "actionbar".equals(astring[1])) {
                    throw new WrongUsageException("commands.title.usage.title", new Object[0]);
                }

                if ("times".equals(astring[1])) {
                    throw new WrongUsageException("commands.title.usage.times", new Object[0]);
                }
            }

            EntityPlayerMP entityplayer = func_184888_a(minecraftserver, icommandlistener, astring[0]);
            SPacketTitle.Type packetplayouttitle_enumtitleaction = SPacketTitle.Type.func_179969_a(astring[1]);

            if (packetplayouttitle_enumtitleaction != SPacketTitle.Type.CLEAR && packetplayouttitle_enumtitleaction != SPacketTitle.Type.RESET) {
                if (packetplayouttitle_enumtitleaction == SPacketTitle.Type.TIMES) {
                    if (astring.length != 5) {
                        throw new WrongUsageException("commands.title.usage", new Object[0]);
                    } else {
                        int i = func_175755_a(astring[2]);
                        int j = func_175755_a(astring[3]);
                        int k = func_175755_a(astring[4]);
                        SPacketTitle packetplayouttitle = new SPacketTitle(i, j, k);

                        entityplayer.field_71135_a.func_147359_a(packetplayouttitle);
                        func_152373_a(icommandlistener, (ICommand) this, "commands.title.success", new Object[0]);
                    }
                } else if (astring.length < 3) {
                    throw new WrongUsageException("commands.title.usage", new Object[0]);
                } else {
                    String s = func_180529_a(astring, 2);

                    ITextComponent ichatbasecomponent;

                    try {
                        ichatbasecomponent = ITextComponent.Serializer.func_150699_a(s);
                    } catch (JsonParseException jsonparseexception) {
                        throw func_184889_a(jsonparseexception);
                    }

                    SPacketTitle packetplayouttitle1 = new SPacketTitle(packetplayouttitle_enumtitleaction, TextComponentUtils.func_179985_a(icommandlistener, ichatbasecomponent, entityplayer));

                    entityplayer.field_71135_a.func_147359_a(packetplayouttitle1);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.title.success", new Object[0]);
                }
            } else if (astring.length != 2) {
                throw new WrongUsageException("commands.title.usage", new Object[0]);
            } else {
                SPacketTitle packetplayouttitle2 = new SPacketTitle(packetplayouttitle_enumtitleaction, (ITextComponent) null);

                entityplayer.field_71135_a.func_147359_a(packetplayouttitle2);
                func_152373_a(icommandlistener, (ICommand) this, "commands.title.success", new Object[0]);
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length == 2 ? func_71530_a(astring, SPacketTitle.Type.func_179971_a()) : Collections.emptyList());
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
