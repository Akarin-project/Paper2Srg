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

    public static ITextComponent processComponent(ICommandSender icommandlistener, ITextComponent ichatbasecomponent, Entity entity) throws CommandException {
        Object object;

        if (ichatbasecomponent instanceof TextComponentScore) {
            TextComponentScore chatcomponentscore = (TextComponentScore) ichatbasecomponent;
            String s = chatcomponentscore.getName();

            if (EntitySelector.isSelector(s)) {
                List list = EntitySelector.matchEntities(icommandlistener, s, Entity.class);

                if (list.size() != 1) {
                    throw new EntityNotFoundException("commands.generic.selector.notFound", new Object[] { s});
                }

                Entity entity1 = (Entity) list.get(0);

                if (entity1 instanceof EntityPlayer) {
                    s = entity1.getName();
                } else {
                    s = entity1.getCachedUniqueIdString();
                }
            }

            String s1 = entity != null && s.equals("*") ? entity.getName() : s;

            object = new TextComponentScore(s1, chatcomponentscore.getObjective());
            ((TextComponentScore) object).setValue(chatcomponentscore.getUnformattedComponentText());
            ((TextComponentScore) object).resolve(icommandlistener);
        } else if (ichatbasecomponent instanceof TextComponentSelector) {
            String s2 = ((TextComponentSelector) ichatbasecomponent).getSelector();

            object = EntitySelector.matchEntitiesToTextComponent(icommandlistener, s2);
            if (object == null) {
                object = new TextComponentString("");
            }
        } else if (ichatbasecomponent instanceof TextComponentString) {
            object = new TextComponentString(((TextComponentString) ichatbasecomponent).getText());
        } else if (ichatbasecomponent instanceof TextComponentKeybind) {
            object = new TextComponentKeybind(((TextComponentKeybind) ichatbasecomponent).getKeybind());
        } else {
            if (!(ichatbasecomponent instanceof TextComponentTranslation)) {
                return ichatbasecomponent;
            }

            Object[] aobject = ((TextComponentTranslation) ichatbasecomponent).getFormatArgs();

            for (int i = 0; i < aobject.length; ++i) {
                Object object1 = aobject[i];

                if (object1 instanceof ITextComponent) {
                    aobject[i] = processComponent(icommandlistener, (ITextComponent) object1, entity);
                }
            }

            object = new TextComponentTranslation(((TextComponentTranslation) ichatbasecomponent).getKey(), aobject);
        }

        Style chatmodifier = ichatbasecomponent.getStyle();

        if (chatmodifier != null) {
            ((ITextComponent) object).setStyle(chatmodifier.createShallowCopy());
        }

        Iterator iterator = ichatbasecomponent.getSiblings().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent1 = (ITextComponent) iterator.next();

            ((ITextComponent) object).appendSibling(processComponent(icommandlistener, ichatbasecomponent1, entity));
        }

        return (ITextComponent) object;
    }
}
