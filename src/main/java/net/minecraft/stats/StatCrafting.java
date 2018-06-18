package net.minecraft.stats;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;


public class StatCrafting extends StatBase {

    private final Item item;

    public StatCrafting(String s, String s1, ITextComponent ichatbasecomponent, Item item) {
        super(s + s1, ichatbasecomponent);
        this.item = item;
    }
}
