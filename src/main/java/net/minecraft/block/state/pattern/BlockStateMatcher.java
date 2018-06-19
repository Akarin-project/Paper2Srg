package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockStateMatcher implements Predicate<IBlockState> {

    public static final Predicate<IBlockState> field_185928_a = new Predicate() {
        public boolean a(@Nullable IBlockState iblockdata) {
            return true;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((IBlockState) object);
        }
    };
    private final BlockStateContainer field_177641_a;
    private final Map<IProperty<?>, Predicate<?>> field_177640_b = Maps.newHashMap();

    private BlockStateMatcher(BlockStateContainer blockstatelist) {
        this.field_177641_a = blockstatelist;
    }

    public static BlockStateMatcher func_177638_a(Block block) {
        return new BlockStateMatcher(block.func_176194_O());
    }

    public boolean apply(@Nullable IBlockState iblockdata) {
        if (iblockdata != null && iblockdata.func_177230_c().equals(this.field_177641_a.func_177622_c())) {
            if (this.field_177640_b.isEmpty()) {
                return true;
            } else {
                Iterator iterator = this.field_177640_b.entrySet().iterator();

                Entry entry;

                do {
                    if (!iterator.hasNext()) {
                        return true;
                    }

                    entry = (Entry) iterator.next();
                } while (this.func_185927_a(iblockdata, (IProperty) entry.getKey(), (Predicate) entry.getValue()));

                return false;
            }
        } else {
            return false;
        }
    }

    protected <T extends Comparable<T>> boolean func_185927_a(IBlockState iblockdata, IProperty<T> iblockstate, Predicate<?> predicate) {
        return predicate.apply(iblockdata.func_177229_b(iblockstate));
    }

    public <V extends Comparable<V>> BlockStateMatcher func_177637_a(IProperty<V> iblockstate, Predicate<? extends V> predicate) {
        if (!this.field_177641_a.func_177623_d().contains(iblockstate)) {
            throw new IllegalArgumentException(this.field_177641_a + " cannot support property " + iblockstate);
        } else {
            this.field_177640_b.put(iblockstate, predicate);
            return this;
        }
    }

    public boolean apply(@Nullable Object object) {
        return this.apply((IBlockState) object);
    }
}
