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

    private static final Joiner field_177667_a = Joiner.on(",");
    private final List<String[]> field_177665_b = Lists.newArrayList();
    private final Map<Character, Predicate<BlockWorldState>> field_177666_c = Maps.newHashMap();
    private int field_177663_d;
    private int field_177664_e;

    private FactoryBlockPattern() {
        this.field_177666_c.put(Character.valueOf(' '), Predicates.alwaysTrue());
    }

    public FactoryBlockPattern func_177659_a(String... astring) {
        if (!ArrayUtils.isEmpty(astring) && !StringUtils.isEmpty(astring[0])) {
            if (this.field_177665_b.isEmpty()) {
                this.field_177663_d = astring.length;
                this.field_177664_e = astring[0].length();
            }

            if (astring.length != this.field_177663_d) {
                throw new IllegalArgumentException("Expected aisle with height of " + this.field_177663_d + ", but was given one with a height of " + astring.length + ")");
            } else {
                String[] astring1 = astring;
                int i = astring.length;

                for (int j = 0; j < i; ++j) {
                    String s = astring1[j];

                    if (s.length() != this.field_177664_e) {
                        throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.field_177664_e + ", found one with " + s.length() + ")");
                    }

                    char[] achar = s.toCharArray();
                    int k = achar.length;

                    for (int l = 0; l < k; ++l) {
                        char c0 = achar[l];

                        if (!this.field_177666_c.containsKey(Character.valueOf(c0))) {
                            this.field_177666_c.put(Character.valueOf(c0), null);
                        }
                    }
                }

                this.field_177665_b.add(astring);
                return this;
            }
        } else {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
    }

    public static FactoryBlockPattern func_177660_a() {
        return new FactoryBlockPattern();
    }

    public FactoryBlockPattern func_177662_a(char c0, Predicate<BlockWorldState> predicate) {
        this.field_177666_c.put(Character.valueOf(c0), predicate);
        return this;
    }

    public BlockPattern func_177661_b() {
        return new BlockPattern(this.func_177658_c());
    }

    private Predicate<BlockWorldState>[][][] func_177658_c() {
        this.func_177657_d();
        Predicate[][][] apredicate = ((Predicate[][][]) Array.newInstance(Predicate.class, new int[] { this.field_177665_b.size(), this.field_177663_d, this.field_177664_e}));

        for (int i = 0; i < this.field_177665_b.size(); ++i) {
            for (int j = 0; j < this.field_177663_d; ++j) {
                for (int k = 0; k < this.field_177664_e; ++k) {
                    apredicate[i][j][k] = this.field_177666_c.get(Character.valueOf(this.field_177665_b.get(i)[j].charAt(k)));
                }
            }
        }

        return apredicate;
    }

    private void func_177657_d() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_177666_c.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (entry.getValue() == null) {
                arraylist.add(entry.getKey());
            }
        }

        if (!arraylist.isEmpty()) {
            throw new IllegalStateException("Predicates for character(s) " + FactoryBlockPattern.field_177667_a.join(arraylist) + " are missing");
        }
    }
}
