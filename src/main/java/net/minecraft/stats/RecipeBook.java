package net.minecraft.stats;

import java.util.BitSet;
import javax.annotation.Nullable;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RecipeBook {

    protected final BitSet field_194077_a = new BitSet();
    protected final BitSet field_194078_b = new BitSet();
    protected boolean field_192818_b;
    protected boolean field_192819_c;

    public RecipeBook() {}

    public void func_193824_a(RecipeBook recipebook) {
        this.field_194077_a.clear();
        this.field_194078_b.clear();
        this.field_194077_a.or(recipebook.field_194077_a);
        this.field_194078_b.or(recipebook.field_194078_b);
    }

    public void func_194073_a(IRecipe irecipe) {
        if (!irecipe.func_192399_d()) {
            this.field_194077_a.set(func_194075_d(irecipe));
        }

    }

    public boolean func_193830_f(@Nullable IRecipe irecipe) {
        return this.field_194077_a.get(func_194075_d(irecipe));
    }

    public void func_193831_b(IRecipe irecipe) {
        int i = func_194075_d(irecipe);

        this.field_194077_a.clear(i);
        this.field_194078_b.clear(i);
    }

    protected static int func_194075_d(@Nullable IRecipe irecipe) {
        return CraftingManager.field_193380_a.func_148757_b((Object) irecipe);
    }

    public void func_194074_f(IRecipe irecipe) {
        this.field_194078_b.clear(func_194075_d(irecipe));
    }

    public void func_193825_e(IRecipe irecipe) {
        this.field_194078_b.set(func_194075_d(irecipe));
    }

    public void func_192813_a(boolean flag) {
        this.field_192818_b = flag;
    }

    public void func_192810_b(boolean flag) {
        this.field_192819_c = flag;
    }
}
