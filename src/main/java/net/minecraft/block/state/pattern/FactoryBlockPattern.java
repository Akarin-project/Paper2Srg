package net.minecraft.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.state.BlockWorldState;

public class FactoryBlockPattern {

    private static final Joiner COMMA_JOIN = Joiner.on(",");
    private final List<String[]> depth = Lists.newArrayList();
    private final Map<Character, Predicate<BlockWorldState>> symbolMap = Maps.newHashMap();
    private int aisleHeight;
    private int rowWidth;

    private FactoryBlockPattern() {
        this.symbolMap.put(Character.valueOf(' '), Predicates.alwaysTrue());
    }

    public FactoryBlockPattern aisle(String... astring) {
        if (!ArrayUtils.isEmpty(astring) && !StringUtils.isEmpty(astring[0])) {
            if (this.depth.isEmpty()) {
                this.aisleHeight = astring.length;
                this.rowWidth = astring[0].length();
            }

            if (astring.length != this.aisleHeight) {
                throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + astring.length + ")");
            } else {
                String[] astring1 = astring;
                int i = astring.length;

                for (int j = 0; j < i; ++j) {
                    String s = astring1[j];

                    if (s.length() != this.rowWidth) {
                        throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + s.length() + ")");
                    }

                    char[] achar = s.toCharArray();
                    int k = achar.length;

                    for (int l = 0; l < k; ++l) {
                        char c0 = achar[l];

                        if (!this.symbolMap.containsKey(Character.valueOf(c0))) {
                            this.symbolMap.put(Character.valueOf(c0), (Object) null);
                        }
                    }
                }

                this.depth.add(astring);
                return this;
            }
        } else {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
    }

    public static FactoryBlockPattern start() {
        return new FactoryBlockPattern();
    }

    public FactoryBlockPattern where(char c0, Predicate<BlockWorldState> predicate) {
        this.symbolMap.put(Character.valueOf(c0), predicate);
        return this;
    }

    public BlockPattern build() {
        return new BlockPattern(this.makePredicateArray());
    }

    private Predicate<BlockWorldState>[][][] makePredicateArray() {
        this.checkMissingPredicates();
        Predicate[][][] apredicate = (Predicate[][][]) ((Predicate[][][]) Array.newInstance(Predicate.class, new int[] { this.depth.size(), this.aisleHeight, this.rowWidth}));

        for (int i = 0; i < this.depth.size(); ++i) {
            for (int j = 0; j < this.aisleHeight; ++j) {
                for (int k = 0; k < this.rowWidth; ++k) {
                    apredicate[i][j][k] = (Predicate) this.symbolMap.get(Character.valueOf(((String[]) this.depth.get(i))[j].charAt(k)));
                }
            }
        }

        return apredicate;
    }

    private void checkMissingPredicates() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.symbolMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (entry.getValue() == null) {
                arraylist.add(entry.getKey());
            }
        }

        if (!arraylist.isEmpty()) {
            throw new IllegalStateException("Predicates for character(s) " + FactoryBlockPattern.COMMA_JOIN.join(arraylist) + " are missing");
        }
    }
}
