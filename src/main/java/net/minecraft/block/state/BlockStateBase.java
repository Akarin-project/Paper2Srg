package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public abstract class BlockStateBase implements IBlockState {

    private static final Joiner field_177234_a = Joiner.on(',');
    private static final Function<Entry<IProperty<?>, Comparable<?>>, String> field_177233_b = new Function() {
        @Nullable
        public String a(@Nullable Entry<IProperty<?>, Comparable<?>> entry) {
            if (entry == null) {
                return "<NULL>";
            } else {
                IProperty iblockstate = (IProperty) entry.getKey();

                return iblockstate.func_177701_a() + "=" + this.a(iblockstate, (Comparable) entry.getValue());
            }
        }

        private <T extends Comparable<T>> String a(IProperty<T> iblockstate, Comparable<?> comparable) {
            return iblockstate.func_177702_a(comparable);
        }

        @Nullable
        public Object apply(@Nullable Object object) {
            return this.a((Entry) object);
        }
    };

    public BlockStateBase() {}

    public <T extends Comparable<T>> IBlockState func_177231_a(IProperty<T> iblockstate) {
        return this.func_177226_a(iblockstate, (Comparable) func_177232_a(iblockstate.func_177700_c(), (Object) this.func_177229_b(iblockstate)));
    }

    protected static <T> T func_177232_a(Collection<T> collection, T t0) {
        Iterator iterator = collection.iterator();

        do {
            if (!iterator.hasNext()) {
                return iterator.next();
            }
        } while (!iterator.next().equals(t0));

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return collection.iterator().next();
        }
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(Block.field_149771_c.func_177774_c(this.func_177230_c()));
        if (!this.func_177228_b().isEmpty()) {
            stringbuilder.append("[");
            BlockStateBase.field_177234_a.appendTo(stringbuilder, Iterables.transform(this.func_177228_b().entrySet(), BlockStateBase.field_177233_b));
            stringbuilder.append("]");
        }

        return stringbuilder.toString();
    }
}
