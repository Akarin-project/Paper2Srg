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

    private static final Joiner COMMA_JOINER = Joiner.on(',');
    private static final Function<Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING = new Function() {
        @Nullable
        public String a(@Nullable Entry<IProperty<?>, Comparable<?>> entry) {
            if (entry == null) {
                return "<NULL>";
            } else {
                IProperty iblockstate = entry.getKey();

                return iblockstate.getName() + "=" + this.a(iblockstate, (Comparable) entry.getValue());
            }
        }

        private <T extends Comparable<T>> String a(IProperty<T> iblockstate, Comparable<?> comparable) {
            return iblockstate.getName(comparable);
        }

        @Override
        @Nullable
        public Object apply(@Nullable Object object) {
            return this.a((Entry) object);
        }
    };

    public BlockStateBase() {}

    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> iblockstate) {
        return this.withProperty(iblockstate, (Comparable) cyclePropertyValue(iblockstate.getAllowedValues(), (Object) this.getValue(iblockstate)));
    }

    protected static <T> T cyclePropertyValue(Collection<T> collection, T t0) {
        Iterator<T> iterator = collection.iterator();

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

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(Block.REGISTRY.getNameForObject(this.getBlock()));
        if (!this.getProperties().isEmpty()) {
            stringbuilder.append("[");
            BlockStateBase.COMMA_JOINER.appendTo(stringbuilder, Iterables.transform(this.getProperties().entrySet(), BlockStateBase.MAP_ENTRY_TO_STRING));
            stringbuilder.append("]");
        }

        return stringbuilder.toString();
    }
}
