package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandEnchant extends CommandBase {

    public CommandEnchant() {}

    public String func_71517_b() {
        return "enchant";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.enchant.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
        } else {
            EntityLivingBase entityliving = (EntityLivingBase) func_184884_a(minecraftserver, icommandlistener, astring[0], EntityLivingBase.class);

            icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 0);

            Enchantment enchantment;

            try {
                enchantment = Enchantment.func_185262_c(func_180528_a(astring[1], 0));
            } catch (NumberInvalidException exceptioninvalidnumber) {
                enchantment = Enchantment.func_180305_b(astring[1]);
            }

            if (enchantment == null) {
                throw new NumberInvalidException("commands.enchant.notFound", new Object[] { astring[1]});
            } else {
                int i = 1;
                ItemStack itemstack = entityliving.func_184614_ca();

                if (itemstack.func_190926_b()) {
                    throw new CommandException("commands.enchant.noItem", new Object[0]);
                } else if (!enchantment.func_92089_a(itemstack)) {
                    throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
                } else {
                    if (astring.length >= 3) {
                        i = func_175764_a(astring[2], enchantment.func_77319_d(), enchantment.func_77325_b());
                    }

                    if (itemstack.func_77942_o()) {
                        NBTTagList nbttaglist = itemstack.func_77986_q();

                        for (int j = 0; j < nbttaglist.func_74745_c(); ++j) {
                            short short0 = nbttaglist.func_150305_b(j).func_74765_d("id");

                            if (Enchantment.func_185262_c(short0) != null) {
                                Enchantment enchantment1 = Enchantment.func_185262_c(short0);

                                if (!enchantment.func_191560_c(enchantment1)) {
                                    throw new CommandException("commands.enchant.cantCombine", new Object[] { enchantment.func_77316_c(i), enchantment1.func_77316_c(nbttaglist.func_150305_b(j).func_74765_d("lvl"))});
                                }
                            }
                        }
                    }

                    itemstack.func_77966_a(enchantment, i);
                    func_152373_a(icommandlistener, (ICommand) this, "commands.enchant.success", new Object[0]);
                    icommandlistener.func_174794_a(CommandResultStats.Type.AFFECTED_ITEMS, 1);
                }
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length == 2 ? func_175762_a(astring, (Collection) Enchantment.field_185264_b.func_148742_b()) : Collections.emptyList());
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
