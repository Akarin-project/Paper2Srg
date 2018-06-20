package net.minecraft.util;

import java.util.List;
import java.util.Random;

public class WeightedRandom {

    public static int func_76272_a(List<? extends WeightedRandom.Item> list) {
        int i = 0;
        int j = 0;

        for (int k = list.size(); j < k; ++j) {
            WeightedRandom.Item weightedrandom_weightedrandomchoice = list.get(j);

            i += weightedrandom_weightedrandomchoice.field_76292_a;
        }

        return i;
    }

    public static <T extends WeightedRandom.Item> T func_76273_a(Random random, List<T> list, int i) {
        if (i <= 0) {
            throw new IllegalArgumentException();
        } else {
            int j = random.nextInt(i);

            return func_180166_a(list, j);
        }
    }

    public static <T extends WeightedRandom.Item> T func_180166_a(List<T> list, int i) {
        int j = 0;

        for (int k = list.size(); j < k; ++j) {
            WeightedRandom.Item weightedrandom_weightedrandomchoice = list.get(j);

            i -= weightedrandom_weightedrandomchoice.field_76292_a;
            if (i < 0) {
                return (T) weightedrandom_weightedrandomchoice;
            }
        }

        return null;
    }

    public static <T extends WeightedRandom.Item> T func_76271_a(Random random, List<T> list) {
        return func_76273_a(random, list, func_76272_a(list));
    }

    public static class Item {

        protected int field_76292_a;

        public Item(int i) {
            this.field_76292_a = i;
        }
    }
}
