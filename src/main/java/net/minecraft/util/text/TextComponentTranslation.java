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

    private final String field_150276_d;
    private final Object[] field_150277_e;
    private final Object field_150274_f = new Object();
    private long field_150275_g = -1L;
    @VisibleForTesting
    List<ITextComponent> field_150278_b = Lists.newArrayList();
    public static final Pattern field_150279_c = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TextComponentTranslation(String s, Object... aobject) {
        this.field_150276_d = s;
        this.field_150277_e = aobject;
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object instanceof ITextComponent) {
                ((ITextComponent) object).func_150256_b().func_150221_a(this.func_150256_b());
            }
        }

    }

    @VisibleForTesting
    synchronized void func_150270_g() {
        Object object = this.field_150274_f;

        synchronized (this.field_150274_f) {
            long i = I18n.func_150827_a();

            if (i == this.field_150275_g) {
                return;
            }

            this.field_150275_g = i;
            this.field_150278_b.clear();
        }

        try {
            this.func_150269_b(I18n.func_74838_a(this.field_150276_d));
        } catch (TextComponentTranslationFormatException chatmessageexception) {
            this.field_150278_b.clear();

            try {
                this.func_150269_b(I18n.func_150826_b(this.field_150276_d));
            } catch (TextComponentTranslationFormatException chatmessageexception1) {
                throw chatmessageexception;
            }
        }

    }

    protected void func_150269_b(String s) {
        boolean flag = false;
        Matcher matcher = TextComponentTranslation.field_150279_c.matcher(s);
        int i = 0;
        int j = 0;

        try {
            int k;

            for (; matcher.find(j); j = k) {
                int l = matcher.start();

                k = matcher.end();
                if (l > j) {
                    TextComponentString chatcomponenttext = new TextComponentString(String.format(s.substring(j, l), new Object[0]));

                    chatcomponenttext.func_150256_b().func_150221_a(this.func_150256_b());
                    this.field_150278_b.add(chatcomponenttext);
                }

                String s1 = matcher.group(2);
                String s2 = s.substring(l, k);

                if ("%".equals(s1) && "%%".equals(s2)) {
                    TextComponentString chatcomponenttext1 = new TextComponentString("%");

                    chatcomponenttext1.func_150256_b().func_150221_a(this.func_150256_b());
                    this.field_150278_b.add(chatcomponenttext1);
                } else {
                    if (!"s".equals(s1)) {
                        throw new TextComponentTranslationFormatException(this, "Unsupported format: \'" + s2 + "\'");
                    }

                    String s3 = matcher.group(1);
                    int i1 = s3 != null ? Integer.parseInt(s3) - 1 : i++;

                    if (i1 < this.field_150277_e.length) {
                        this.field_150278_b.add(this.func_150272_a(i1));
                    }
                }
            }

            if (j < s.length()) {
                TextComponentString chatcomponenttext2 = new TextComponentString(String.format(s.substring(j), new Object[0]));

                chatcomponenttext2.func_150256_b().func_150221_a(this.func_150256_b());
                this.field_150278_b.add(chatcomponenttext2);
            }

        } catch (IllegalFormatException illegalformatexception) {
            throw new TextComponentTranslationFormatException(this, illegalformatexception);
        }
    }

    private ITextComponent func_150272_a(int i) {
        if (i >= this.field_150277_e.length) {
            throw new TextComponentTranslationFormatException(this, i);
        } else {
            Object object = this.field_150277_e[i];
            Object object1;

            if (object instanceof ITextComponent) {
                object1 = (ITextComponent) object;
            } else {
                object1 = new TextComponentString(object == null ? "null" : object.toString());
                ((ITextComponent) object1).func_150256_b().func_150221_a(this.func_150256_b());
            }

            return (ITextComponent) object1;
        }
    }

    public ITextComponent func_150255_a(Style chatmodifier) {
        super.func_150255_a(chatmodifier);
        Object[] aobject = this.field_150277_e;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject[j];

            if (object instanceof ITextComponent) {
                ((ITextComponent) object).func_150256_b().func_150221_a(this.func_150256_b());
            }
        }

        if (this.field_150275_g > -1L) {
            Iterator iterator = this.field_150278_b.iterator();

            while (iterator.hasNext()) {
                ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

                ichatbasecomponent.func_150256_b().func_150221_a(chatmodifier);
            }
        }

        return this;
    }

    public Iterator<ITextComponent> iterator() {
        this.func_150270_g();
        return Iterators.concat(func_150262_a((Iterable) this.field_150278_b), func_150262_a((Iterable) this.field_150264_a));
    }

    public String func_150261_e() {
        this.func_150270_g();
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.field_150278_b.iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            stringbuilder.append(ichatbasecomponent.func_150261_e());
        }

        return stringbuilder.toString();
    }

    public TextComponentTranslation func_150259_f() {
        Object[] aobject = new Object[this.field_150277_e.length];

        for (int i = 0; i < this.field_150277_e.length; ++i) {
            if (this.field_150277_e[i] instanceof ITextComponent) {
                aobject[i] = ((ITextComponent) this.field_150277_e[i]).func_150259_f();
            } else {
                aobject[i] = this.field_150277_e[i];
            }
        }

        TextComponentTranslation chatmessage = new TextComponentTranslation(this.field_150276_d, aobject);

        chatmessage.func_150255_a(this.func_150256_b().func_150232_l());
        Iterator iterator = this.func_150253_a().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatmessage.func_150257_a(ichatbasecomponent.func_150259_f());
        }

        return chatmessage;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TextComponentTranslation)) {
            return false;
        } else {
            TextComponentTranslation chatmessage = (TextComponentTranslation) object;

            return Arrays.equals(this.field_150277_e, chatmessage.field_150277_e) && this.field_150276_d.equals(chatmessage.field_150276_d) && super.equals(object);
        }
    }

    public int hashCode() {
        int i = super.hashCode();

        i = 31 * i + this.field_150276_d.hashCode();
        i = 31 * i + Arrays.hashCode(this.field_150277_e);
        return i;
    }

    public String toString() {
        return "TranslatableComponent{key=\'" + this.field_150276_d + '\'' + ", args=" + Arrays.toString(this.field_150277_e) + ", siblings=" + this.field_150264_a + ", style=" + this.func_150256_b() + '}';
    }

    public String func_150268_i() {
        return this.field_150276_d;
    }

    public Object[] func_150271_j() {
        return this.field_150277_e;
    }

    public ITextComponent func_150259_f() {
        return this.func_150259_f();
    }
}
