package net.minecraft.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEffect extends CommandBase {

    public CommandEffect() {}

    public String func_71517_b() {
        return "effect";
    }

    public int func_82362_a() {
        return 2;
    }

    public String func_71518_a(ICommandSender icommandlistener) {
        return "commands.effect.usage";
    }

    public void func_184881_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring) throws CommandException {
        if (astring.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        } else {
            EntityLivingBase entityliving = (EntityLivingBase) func_184884_a(minecraftserver, icommandlistener, astring[0], EntityLivingBase.class);

            if ("clear".equals(astring[1])) {
                if (entityliving.func_70651_bq().isEmpty()) {
                    throw new CommandException("commands.effect.failure.notActive.all", new Object[] { entityliving.func_70005_c_()});
                } else {
                    entityliving.func_70674_bp();
                    func_152373_a(icommandlistener, (ICommand) this, "commands.effect.success.removed.all", new Object[] { entityliving.func_70005_c_()});
                }
            } else {
                Potion mobeffectlist;

                try {
                    mobeffectlist = Potion.func_188412_a(func_180528_a(astring[1], 1));
                } catch (NumberInvalidException exceptioninvalidnumber) {
                    mobeffectlist = Potion.func_180142_b(astring[1]);
                }

                if (mobeffectlist == null) {
                    throw new NumberInvalidException("commands.effect.notFound", new Object[] { astring[1]});
                } else {
                    int i = 600;
                    int j = 30;
                    int k = 0;

                    if (astring.length >= 3) {
                        j = func_175764_a(astring[2], 0, 1000000);
                        if (mobeffectlist.func_76403_b()) {
                            i = j;
                        } else {
                            i = j * 20;
                        }
                    } else if (mobeffectlist.func_76403_b()) {
                        i = 1;
                    }

                    if (astring.length >= 4) {
                        k = func_175764_a(astring[3], 0, 255);
                    }

                    boolean flag = true;

                    if (astring.length >= 5 && "true".equalsIgnoreCase(astring[4])) {
                        flag = false;
                    }

                    if (j > 0) {
                        PotionEffect mobeffect = new PotionEffect(mobeffectlist, i, k, false, flag);

                        entityliving.func_70690_d(mobeffect);
                        func_152373_a(icommandlistener, (ICommand) this, "commands.effect.success", new Object[] { new TextComponentTranslation(mobeffect.func_76453_d(), new Object[0]), Integer.valueOf(Potion.func_188409_a(mobeffectlist)), Integer.valueOf(k), entityliving.func_70005_c_(), Integer.valueOf(j)});
                    } else if (entityliving.func_70644_a(mobeffectlist)) {
                        entityliving.func_184589_d(mobeffectlist);
                        func_152373_a(icommandlistener, (ICommand) this, "commands.effect.success.removed", new Object[] { new TextComponentTranslation(mobeffectlist.func_76393_a(), new Object[0]), entityliving.func_70005_c_()});
                    } else {
                        throw new CommandException("commands.effect.failure.notActive", new Object[] { new TextComponentTranslation(mobeffectlist.func_76393_a(), new Object[0]), entityliving.func_70005_c_()});
                    }
                }
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer minecraftserver, ICommandSender icommandlistener, String[] astring, @Nullable BlockPos blockposition) {
        return astring.length == 1 ? func_71530_a(astring, minecraftserver.func_71213_z()) : (astring.length == 2 ? func_175762_a(astring, (Collection) Potion.field_188414_b.func_148742_b()) : (astring.length == 5 ? func_71530_a(astring, new String[] { "true", "false"}) : Collections.emptyList()));
    }

    public boolean func_82358_a(String[] astring, int i) {
        return i == 0;
    }
}
