package net.minecraft.util.text;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.text.translation.I18n;

public class TextComponentTranslation extends TextComponentBase {

    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock = new Object();
    private long lastTranslationUpdateTimeInMilliseconds = -1L;
    @VisibleForTesting
    List<ITextComponent> children = Lists.newArrayList();
    public static final Pattern STRING_VARIABLE_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TextComponentTranslation(String s, Object... aobject) {
        this.key = s;
        this.formatArgs = aobject;
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object instanceof ITextComponent) {
                ((ITextComponent) object).getStyle().setParentStyle(this.getStyle());
            }
        }

    }

    @VisibleForTesting
    synchronized void ensureInitialized() {
        Object object = this.syncLock;

        synchronized (this.syncLock) {
            long i = I18n.getLastTranslationUpdateTimeInMilliseconds();

            if (i == this.lastTranslationUpdateTimeInMilliseconds) {
                return;
            }

            this.lastTranslationUpdateTimeInMilliseconds = i;
            this.children.clear();
        }

        try {
            this.initializeFromFormat(I18n.translateToLocal(this.key));
        } catch (TextComponentTranslationFormatException chatmessageexception) {
            this.children.clear();

            try {
                this.initializeFromFormat(I18n.translateToFallback(this.key));
            } catch (TextComponentTranslationFormatException chatmessageexception1) {
                throw chatmessageexception;
            }
        }

    }

    protected void initializeFromFormat(String s) {
        boolean flag = false;
        Matcher matcher = TextComponentTranslation.STRING_VARIABLE_PATTERN.matcher(s);
        int i = 0;
        int j = 0;

        try {
            int k;

            for (; matcher.find(j); j = k) {
                int l = matcher.start();

                k = matcher.end();
                if (l > j) {
                    TextComponentString chatcomponenttext = new TextComponentString(String.format(s.substring(j, l), new Object[0]));

                    chatcomponenttext.getStyle().setParentStyle(this.getStyle());
                    this.children.add(chatcomponenttext);
                }

                String s1 = matcher.group(2);
                String s2 = s.substring(l, k);

                if ("%".equals(s1) && "%%".equals(s2)) {
                    TextComponentString chatcomponenttext1 = new TextComponentString("%");

                    chatcomponenttext1.getStyle().setParentStyle(this.getStyle());
                    this.children.add(chatcomponenttext1);
                } else {
                    if (!"s".equals(s1)) {
                        throw new TextComponentTranslationFormatException(this, "Unsupported format: \'" + s2 + "\'");
                    }

                    String s3 = matcher.group(1);
                    int i1 = s3 != null ? Integer.parseInt(s3) - 1 : i++;

                    if (i1 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(i1));
                    }
                }
            }

            if (j < s.length()) {
                TextComponentString chatcomponenttext2 = new TextComponentString(String.format(s.substring(j), new Object[0]));

                chatcomponenttext2.getStyle().setParentStyle(this.getStyle());
                this.children.add(chatcomponenttext2);
            }

        } catch (IllegalFormatException illegalformatexception) {
            throw new TextComponentTranslationFormatException(this, illegalformatexception);
        }
    }

    private ITextComponent getFormatArgumentAsComponent(int i) {
        if (i >= this.formatArgs.length) {
            throw new TextComponentTranslationFormatException(this, i);
        } else {
            Object object = this.formatArgs[i];
            Object object1;

            if (object instanceof ITextComponent) {
                object1 = object;
            } else {
                object1 = new TextComponentString(object == null ? "null" : object.toString());
                ((ITextComponent) object1).getStyle().setParentStyle(this.getStyle());
            }

            return (ITextComponent) object1;
        }
    }

    @Override
    public ITextComponent setStyle(Style chatmodifier) {
        super.setStyle(chatmodifier);
        Object[] aobject = this.formatArgs;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject[j];

            if (object instanceof ITextComponent) {
                ((ITextComponent) object).getStyle().setParentStyle(this.getStyle());
            }
        }

        if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
            Iterator iterator = this.children.iterator();

            while (iterator.hasNext()) {
                ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

                ichatbasecomponent.getStyle().setParentStyle(chatmodifier);
            }
        }

        return this;
    }

    @Override
    public Iterator<ITextComponent> iterator() {
        this.ensureInitialized();
        return Iterators.concat(createDeepCopyIterator(this.children), createDeepCopyIterator(this.siblings));
    }

    @Override
    public String getUnformattedComponentText() {
        this.ensureInitialized();
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.children.iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            stringbuilder.append(ichatbasecomponent.getUnformattedComponentText());
        }

        return stringbuilder.toString();
    }

    @Override
    public TextComponentTranslation createCopy() {
        Object[] aobject = new Object[this.formatArgs.length];

        for (int i = 0; i < this.formatArgs.length; ++i) {
            if (this.formatArgs[i] instanceof ITextComponent) {
                aobject[i] = ((ITextComponent) this.formatArgs[i]).createCopy();
            } else {
                aobject[i] = this.formatArgs[i];
            }
        }

        TextComponentTranslation chatmessage = new TextComponentTranslation(this.key, aobject);

        chatmessage.setStyle(this.getStyle().createShallowCopy());
        Iterator iterator = this.getSiblings().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatmessage.appendSibling(ichatbasecomponent.createCopy());
        }

        return chatmessage;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TextComponentTranslation)) {
            return false;
        } else {
            TextComponentTranslation chatmessage = (TextComponentTranslation) object;

            return Arrays.equals(this.formatArgs, chatmessage.formatArgs) && this.key.equals(chatmessage.key) && super.equals(object);
        }
    }

    @Override
    public int hashCode() {
        int i = super.hashCode();

        i = 31 * i + this.key.hashCode();
        i = 31 * i + Arrays.hashCode(this.formatArgs);
        return i;
    }

    @Override
    public String toString() {
        return "TranslatableComponent{key=\'" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getFormatArgs() {
        return this.formatArgs;
    }
}
