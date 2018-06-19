package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CommandPlaySound extends CommandBase {

    public CommandPlaySound() {}

    public String func_71517_b() {
        return "playsound";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.playsound.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException(this.func_71518_a(icommandlistener), new Object[0]);
        } else {
            byte b0 = 0;
            int i = b0 + 1;
            String s = astring[b0];
            String s1 = astring[i++];
            SoundCategory soundcategory = SoundCategory.func_187950_a(s1);

            if (soundcategory == null) {
                throw new CommandException("commands.playsound.unknownSoundSource", new Object[] { s1});
            } else {
                EntityPlayerMP entityplayer = func_184888_a(minecraftserver, icommandlistener, astring[i++]);
                Vec3d vec3d = icommandlistener.func_174791_d();
                double d0 = astring.length > i ? func_175761_b(vec3d.field_72450_a, astring[i++], true) : vec3d.field_72450_a;
                double d1 = astring.length > i ? func_175769_b(vec3d.field_72448_b, astring[i++], 0, 0, false) : vec3d.field_72448_b;
                double d2 = astring.length > i ? func_175761_b(vec3d.field_72449_c, astring[i++], true) : vec3d.field_72449_c;
                double d3 = astring.length > i ? func_175756_a(astring[i++], 0.0D, 3.4028234663852886E38D) : 1.0D;
                double d4 = astring.length > i ? func_175756_a(astring[i++], 0.0D, 2.0D) : 1.0D;
                double d5 = astring.length > i ? func_175756_a(astring[i], 0.0D, 1.0D) : 0.0D;
                double d6 = d3 > 1.0D ? d3 * 16.0D : 16.0D;
                double d7 = entityplayer.func_70011_f(d0, d1, d2);

                if (d7 > d6) {
                    if (d5 <= 0.0D) {
                        throw new CommandException("commands.playsound.playerTooFar", new Object[] { entityplayer.func_70005_c_()});
                    }

                    double d8 = d0 - entityplayer.field_70165_t;
                    double d9 = d1 - entityplayer.field_70163_u;
                    double d10 = d2 - entityplayer.field_70161_v;
                    double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);

                    if (d11 > 0.0D) {
                        d0 = entityplayer.field_70165_t + d8 / d11 * 2.0D;
                        d1 = entityplayer.field_70163_u + d9 / d11 * 2.0D;
                        d2 = entityplayer.field_70161_v + d10 / d11 * 2.0D;
                    }

                    d3 = d5;
                }

                entityplayer.field_71135_a.func_147359_a(new SPacketCustomSound(s, soundcategory, d0, d1, d2, (float) d3, (float) d4));
                func_152373_a(icommandlistener, (ICommand) this, "commands.playsound.success", new Object[] { s, entityplayer.func_70005_c_()});
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_175762_a(astring, (Collection) SoundEvent.field_187505_a.func_148742_b()) : (astring.length == 2 ? func_175762_a(astring, (Collection) SoundCategory.func_187949_b()) : (astring.length == 3 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length > 3 && astring.length <= 6 ? func_175771_a(astring, 3, blockposition) : Collections.emptyList())));
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 2;
    }
}
