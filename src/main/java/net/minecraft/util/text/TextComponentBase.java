package net.minecraft.util.text;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public abstract class TextComponentBase implements ITextComponent {

    protected List<ITextComponent> field_150264_a = Lists.newArrayList();
    private Style field_150263_b;

    public TextComponentBase() {}

    public ITextComponent func_150257_a(ITextComponent ichatbasecomponent) {
        ichatbasecomponent.func_150256_b().func_150221_a(this.func_150256_b());
        this.field_150264_a.add(ichatbasecomponent);
        return this;
    }

    public List<ITextComponent> func_150253_a() {
        return this.field_150264_a;
    }

    public ITextComponent func_150258_a(String s) {
        return this.func_150257_a(new TextComponentString(s));
    }

    public ITextComponent func_150255_a(Style chatmodifier) {
        this.field_150263_b = chatmodifier;
        Iterator iterator = this.field_150264_a.iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            ichatbasecomponent.func_150256_b().func_150221_a(this.func_150256_b());
        }

        return this;
    }

    public Style func_150256_b() {
        if (this.field_150263_b == null) {
            this.field_150263_b = new Style();
            Iterator iterator = this.field_150264_a.iterator();

            while (iterator.hasNext()) {
                ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

                ichatbasecomponent.func_150256_b().func_150221_a(this.field_150263_b);
            }
        }

        return this.field_150263_b;
    }

    public Iterator<ITextComponent> iterator() {
        return Iterators.concat(Iterators.forArray(new TextComponentBase[] { this}), func_150262_a(this.field_150264_a));
    }

    public final String func_150260_c() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            stringbuilder.append(ichatbasecomponent.func_150261_e());
        }

        return stringbuilder.toString();
    }

    public static Iterator<ITextComponent> func_150262_a(Iterable<ITextComponent> iterable) {
        Iterator iterator = Iterators.concat(Iterators.transform(iterable.iterator(), new Function() {
            public Iterator<ITextComponent> a(@Nullable ITextComponent ichatbasecomponent) {
                return ichatbasecomponent.iterator();
            }

            public Object apply(@Nullable Object object) {
                return this.a((ITextComponent) object);
            }
        }));

        iterator = Iterators.transform(iterator, new Function() {
            public ITextComponent a(@Nullable ITextComponent ichatbasecomponent) {
                ITextComponent ichatbasecomponent1 = ichatbasecomponent.func_150259_f();

                ichatbasecomponent1.func_150255_a(ichatbasecomponent1.func_150256_b().func_150206_m());
                return ichatbasecomponent1;
            }

            public Object apply(@Nullable Object object) {
                return this.a((ITextComponent) object);
            }
        });
        return iterator;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TextComponentBase)) {
            return false;
        } else {
            TextComponentBase chatbasecomponent = (TextComponentBase) object;

            return this.field_150264_a.equals(chatbasecomponent.field_150264_a) && this.func_150256_b().equals(chatbasecomponent.func_150256_b());
        }
    }

    public int hashCode() {
        return 31 * this.func_150256_b().hashCode() + this.field_150264_a.hashCode(); // CraftBukkit - fix null pointer
    }

    public String toString() {
        return "BaseComponent{style=" + this.field_150263_b + ", siblings=" + this.field_150264_a + '}';
    }
}
