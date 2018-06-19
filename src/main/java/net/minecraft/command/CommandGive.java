package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class CommandGive extends CommandBase {

    public CommandGive() {}

    public String func_71517_b() {
        return "give";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.give.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        } else {
            EntityPlayerMP entityplayer = func_184888_a(minecraftserver, icommandlistener, astring[0]);
            Item item = func_147179_f(icommandlistener, astring[1]);
            int i = astring.length >= 3 ? func_175764_a(astring[2], 1, item.func_77639_j()) : 1;
            int j = astring.length >= 4 ? func_175755_a(astring[3]) : 0;
            ItemStack itemstack = new ItemStack(item, i, j);

            if (astring.length >= 5) {
                String s = func_180529_a(astring, 4);

                try {
                    itemstack.func_77982_d(JsonToNBT.func_180713_a(s));
                } catch (NBTException mojangsonparseexception) {
                    throw new CommandException("commands.give.tagError", new Object[] { mojangsonparseexception.getMessage()});
                }
            }

            boolean flag = entityplayer.field_71071_by.func_70441_a(itemstack);

            if (flag) {
                entityplayer.field_70170_p.func_184148_a((EntityPlayer) null, entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v, SoundEvents.field_187638_cR, SoundCategory.PLAYERS, 0.2F, ((entityplayer.func_70681_au().nextFloat() - entityplayer.func_70681_au().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.field_71069_bz.func_75142_b();
            }

            EntityItem entityitem;

            if (flag && itemstack.func_190926_b()) {
                itemstack.func_190920_e(1);
                icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, i);
                entityitem = entityplayer.func_71019_a(itemstack, false);
                if (entityitem != null) {
                    entityitem.func_174870_v();
                }
            } else {
                icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.func_190916_E());
                entityitem = entityplayer.func_71019_a(itemstack, false);
                if (entityitem != null) {
                    entityitem.func_174868_q();
                    entityitem.func_145797_a(entityplayer.func_70005_c_());
                }
            }

            func_152373_a(icommandlistener, (ICommand) this, "commands.give.success", new Object[] { itemstack.func_151000_E(), Integer.valueOf(i), entityplayer.func_70005_c_()});
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length == 2 ? func_175762_a(astring, (Collection) Item.field_150901_e.func_148742_b()) : Collections.emptyList());
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
