package net.minecraft.util.text;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TextComponentUtils {

    public static ITextComponent func_179985_a(ICommandSender icommandlistener, ITextComponent ichatbasecomponent, Entity entity) throws CommandException {
        Object object;

        if (ichatbasecomponent instanceof TextComponentScore) {
            TextComponentScore chatcomponentscore = (TextComponentScore) ichatbasecomponent;
            String s = chatcomponentscore.func_179995_g();

            if (EntitySelector.func_82378_b(s)) {
                List list = EntitySelector.func_179656_b(icommandlistener, s, Entity.class);

                if (list.size() != 1) {
                    throw new EntityNotFoundException("commands.generic.selector.notFound", new Object[] { s});
                }

                Entity entity1 = (Entity) list.get(0);

                if (entity1 instanceof EntityPlayer) {
                    s = entity1.func_70005_c_();
                } else {
                    s = entity1.func_189512_bd();
                }
            }

            String s1 = entity != null && s.equals("*") ? entity.func_70005_c_() : s;

            object = new TextComponentScore(s1, chatcomponentscore.func_179994_h());
            ((TextComponentScore) object).func_179997_b(chatcomponentscore.func_150261_e());
            ((TextComponentScore) object).func_186876_a(icommandlistener);
        } else if (ichatbasecomponent instanceof TextComponentSelector) {
            String s2 = ((TextComponentSelector) ichatbasecomponent).func_179992_g();

            object = EntitySelector.func_150869_b(icommandlistener, s2);
            if (object == null) {
                object = new TextComponentString("");
            }
        } else if (ichatbasecomponent instanceof TextComponentString) {
            object = new TextComponentString(((TextComponentString) ichatbasecomponent).func_150265_g());
        } else if (ichatbasecomponent instanceof TextComponentKeybind) {
            object = new TextComponentKeybind(((TextComponentKeybind) ichatbasecomponent).func_193633_h());
        } else {
            if (!(ichatbasecomponent instanceof TextComponentTranslation)) {
                return ichatbasecomponent;
            }

            Object[] aobject = ((TextComponentTranslation) ichatbasecomponent).func_150271_j();

            for (int i = 0; i < aobject.length; ++i) {
                Object object1 = aobject[i];

                if (object1 instanceof ITextComponent) {
                    aobject[i] = func_179985_a(icommandlistener, (ITextComponent) object1, entity);
                }
            }

            object = new TextComponentTranslation(((TextComponentTranslation) ichatbasecomponent).func_150268_i(), aobject);
        }

        Style chatmodifier = ichatbasecomponent.func_150256_b();

        if (chatmodifier != null) {
            ((ITextComponent) object).func_150255_a(chatmodifier.func_150232_l());
        }

        Iterator iterator = ichatbasecomponent.func_150253_a().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent1 = (ITextComponent) iterator.next();

            ((ITextComponent) object).func_150257_a(func_179985_a(icommandlistener, ichatbasecomponent1, entity));
        }

        return (ITextComponent) object;
    }
}
