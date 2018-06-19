package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClearInventory extends CommandBase {

    public CommandClearInventory() {}

    public String func_71517_b() {
        return "clear";
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.clear.usage";
    }

    public int func_82362_a() {
        return 2;
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        EntityPlayerMP entityplayer = astring.length == 0 ? func_71521_c(icommandlistener) : func_184888_a(minecraftserver, icommandlistener, astring[0]);
        Item item = astring.length >= 2 ? func_147179_f(icommandlistener, astring[1]) : null;
        int i = astring.length >= 3 ? func_180528_a(astring[2], -1) : -1;
        int j = astring.length >= 4 ? func_180528_a(astring[3], -1) : -1;
        NBTTagCompound nbttagcompound = null;

        if (astring.length >= 5) {
            try {
                nbttagcompound = JsonToNBT.func_180713_a(func_180529_a(astring, 4));
            } catch (NBTException mojangsonparseexception) {
                throw new CommandException("commands.clear.tagError", new Object[] { mojangsonparseexception.getMessage()});
            }
        }

        if (astring.length >= 2 && item == null) {
            throw new CommandException("commands.clear.failure", new Object[] { entityplayer.func_70005_c_()});
        } else {
            int k = entityplayer.field_71071_by.func_174925_a(item, i, j, nbttagcompound);

            entityplayer.field_71069_bz.func_75142_b();
            if (!entityplayer.field_71075_bZ.field_75098_d) {
                entityplayer.func_71113_k();
            }

            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, k);
            if (k == 0) {
                throw new CommandException("commands.clear.failure", new Object[] { entityplayer.func_70005_c_()});
            } else {
                if (j == 0) {
                    icommandlistener.func_145747_a(new TextComponentTranslation("commands.clear.testing", new Object[] { entityplayer.func_70005_c_(), Integer.valueOf(k)}));
                } else {
                    func_152373_a(icommandlistener, (ICommand) this, "commands.clear.success", new Object[] { entityplayer.func_70005_c_(), Integer.valueOf(k)});
                }

            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length == 2 ? func_175762_a(astring, (Collection) Item.field_150901_e.func_148742_b()) : Collections.emptyList());
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
