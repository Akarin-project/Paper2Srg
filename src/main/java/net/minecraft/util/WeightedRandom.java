package net.minecraft.util;

import java.util.List;
import java.util.Random;

public class WeightedRandom {

    public static int getTotalWeight(List<? extends WeightedRandom.Item> list) {
        int i = 0;
        int j = 0;

        for (int k = list.size(); j < k; ++j) {
            WeightedRandom.Item weightedrandom_weightedrandomchoice = (WeightedRandom.Item) list.get(j);

            i += weightedrandom_weightedrandomchoice.itemWeight;
        }

        return i;
    }

    public static <T extends WeightedRandom.Item> T getRandomItem(Random random, List<T> list, int i) {
        if (i <= 0) {
            throw new IllegalArgumentException();
        } else {
            int j = random.nextInt(i);

            return getRandomItem(list, j);
        }
    }

    public static <T extends WeightedRandom.Item> T getRandomItem(List<T> list, int i) {
        int j = 0;

        for (int k = list.size(); j < k; ++j) {
            WeightedRandom.Item weightedrandom_weightedrandomchoice = (WeightedRandom.Item) list.get(j);

            i -= weightedrandom_weightedrandomchoice.itemWeight;
            if (i < 0) {
                return weightedrandom_weightedrandomchoice;
            }
        }

        return null;
    }

    public static <T extends WeightedRandom.Item> T getRandomItem(Random random, List<T> list) {
        return getRandomItem(random, list, getTotalWeight(list));
    }

    public static class Item {

        protected int itemWeight;

        public Item(int i) {
            this.itemWeight = i;
        }
    }
}
