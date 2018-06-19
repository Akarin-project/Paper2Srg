package net.minecraft.command;

import io.netty.buffer.Unpooled;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class CommandStopSound extends CommandBase {

    public CommandStopSound() {}

    public String func_71517_b() {
        return "stopsound";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.stopsound.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length >= 1 && astring.length <= 3) {
            byte b0 = 0;
            int i = b0 + 1;
            EntityPlayerMP entityplayer = func_184888_a(minecraftserver, icommandlistener, astring[b0]);
            String s = "";
            String s1 = "";

            if (astring.length >= 2) {
                String s2 = astring[i++];
                SoundCategory soundcategory = SoundCategory.func_187950_a(s2);

                if (soundcategory == null) {
                    throw new CommandException("commands.stopsound.unknownSoundSource", new Object[] { s2});
                }

                s = soundcategory.func_187948_a();
            }

            if (astring.length == 3) {
                s1 = astring[i++];
            }

            PacketBuffer packetdataserializer = new PacketBuffer(Unpooled.buffer());

            packetdataserializer.func_180714_a(s);
            packetdataserializer.func_180714_a(s1);
            entityplayer.field_71135_a.func_147359_a(new SPacketCustomPayload("MC|StopSound", packetdataserializer));
            if (s.isEmpty() && s1.isEmpty()) {
                func_152373_a(icommandlistener, (ICommand) this, "commands.stopsound.success.all", new Object[] { entityplayer.func_70005_c_()});
            } else if (s1.isEmpty()) {
                func_152373_a(icommandlistener, (ICommand) this, "commands.stopsound.success.soundSource", new Object[] { s, entityplayer.func_70005_c_()});
            } else {
                func_152373_a(icommandlistener, (ICommand) this, "commands.stopsound.success.individualSound", new Object[] { s1, s, entityplayer.func_70005_c_()});
            }

        } else {
            throw new WrongUsageException(this.func_71518_a(icommandlistener), new Object[0]);
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length == 2 ? func_175762_a(astring, (Collection) SoundCategory.func_187949_b()) : (astring.length == 3 ? func_175762_a(astring, (Collection) SoundEvent.field_187505_a.func_148742_b()) : Collections.emptyList()));
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
