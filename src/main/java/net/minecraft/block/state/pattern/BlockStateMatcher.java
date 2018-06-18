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

    public static final Predicate<IBlockState> ANY = new Predicate() {
        public boolean a(@Nullable IBlockState iblockdata) {
            return true;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((IBlockState) object);
        }
    };
    private final BlockStateContainer blockstate;
    private final Map<IProperty<?>, Predicate<?>> propertyPredicates = Maps.newHashMap();

    private BlockStateMatcher(BlockStateContainer blockstatelist) {
        this.blockstate = blockstatelist;
    }

    public static BlockStateMatcher forBlock(Block block) {
        return new BlockStateMatcher(block.getBlockState());
    }

    public boolean apply(@Nullable IBlockState iblockdata) {
        if (iblockdata != null && iblockdata.getBlock().equals(this.blockstate.getBlock())) {
            if (this.propertyPredicates.isEmpty()) {
                return true;
            } else {
                Iterator iterator = this.propertyPredicates.entrySet().iterator();

                Entry entry;

                do {
                    if (!iterator.hasNext()) {
                        return true;
                    }

                    entry = (Entry) iterator.next();
                } while (this.matches(iblockdata, (IProperty) entry.getKey(), (Predicate) entry.getValue()));

                return false;
            }
        } else {
            return false;
        }
    }

    protected <T extends Comparable<T>> boolean matches(IBlockState iblockdata, IProperty<T> iblockstate, Predicate<?> predicate) {
        return predicate.apply(iblockdata.getValue(iblockstate));
    }

    public <V extends Comparable<V>> BlockStateMatcher where(IProperty<V> iblockstate, Predicate<? extends V> predicate) {
        if (!this.blockstate.getProperties().contains(iblockstate)) {
            throw new IllegalArgumentException(this.blockstate + " cannot support property " + iblockstate);
        } else {
            this.propertyPredicates.put(iblockstate, predicate);
            return this;
        }
    }

    public boolean apply(@Nullable Object object) {
        return this.apply((IBlockState) object);
    }
}
