package net.minecraft.item.crafting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;

public class CraftingManager {

    private static final Logger field_192422_a = LogManager.getLogger();
    private static int field_193381_c;
    public static RegistryNamespaced<ResourceLocation, IRecipe> field_193380_a = new RegistryNamespaced();

    public CraftingManager() {}

    public static boolean func_193377_a() {
        try {
            CraftingManager.field_193381_c = 0; // Reset recipe ID count
            func_193379_a("armordye", new RecipesArmorDyes());
            func_193379_a("bookcloning", new RecipeBookCloning());
            func_193379_a("mapcloning", new RecipesMapCloning());
            func_193379_a("mapextending", new RecipesMapExtending());
            func_193379_a("fireworks", new RecipeFireworks());
            func_193379_a("repairitem", new RecipeRepairItem());
            func_193379_a("tippedarrow", new RecipeTippedArrow());
            func_193379_a("bannerduplicate", new RecipesBanners.RecipeDuplicatePattern());
            func_193379_a("banneraddpattern", new RecipesBanners.RecipeAddPattern());
            func_193379_a("shielddecoration", new ShieldRecipes.Decoration());
            func_193379_a("shulkerboxcoloring", new ShulkerBoxRecipes.ShulkerBoxColoring());
            return func_192420_c();
        } catch (Throwable throwable) {
            return false;
        }
    }

    private static boolean func_192420_c() {
        FileSystem filesystem = null;
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

        try {
            boolean flag;

            try {
                URL url = CraftingManager.class.getResource("/assets/.mcassetsroot");

                if (url == null) {
                    CraftingManager.field_192422_a.error("Couldn\'t find .mcassetsroot");
                    flag = false;
                    return flag;
                }

                URI uri = url.toURI();
                java.nio.file.Path java_nio_file_path;

                if ("file".equals(uri.getScheme())) {
                    java_nio_file_path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/recipes").toURI());
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        CraftingManager.field_192422_a.error("Unsupported scheme " + uri + " trying to list all recipes");
                        boolean flag1 = false;

                        return flag1;
                    }

                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    java_nio_file_path = filesystem.getPath("/assets/minecraft/recipes", new String[0]);
                }

                Iterator iterator = Files.walk(java_nio_file_path, new FileVisitOption[0]).iterator();

                while (iterator.hasNext()) {
                    java.nio.file.Path java_nio_file_path1 = (java.nio.file.Path) iterator.next();

                    if ("json".equals(FilenameUtils.getExtension(java_nio_file_path1.toString()))) {
                        java.nio.file.Path java_nio_file_path2 = java_nio_file_path.relativize(java_nio_file_path1);
                        String s = FilenameUtils.removeExtension(java_nio_file_path2.toString()).replaceAll("\\\\", "/");
                        ResourceLocation minecraftkey = new ResourceLocation(s);
                        BufferedReader bufferedreader = null;

                        try {
                            boolean flag2;

                            try {
                                bufferedreader = Files.newBufferedReader(java_nio_file_path1);
                                func_193379_a(s, func_193376_a((JsonObject) JsonUtils.func_193839_a(gson, (Reader) bufferedreader, JsonObject.class)));
                            } catch (JsonParseException jsonparseexception) {
                                CraftingManager.field_192422_a.error("Parsing error loading recipe " + minecraftkey, jsonparseexception);
                                flag2 = false;
                                return flag2;
                            } catch (IOException ioexception) {
                                CraftingManager.field_192422_a.error("Couldn\'t read recipe " + minecraftkey + " from " + java_nio_file_path1, ioexception);
                                flag2 = false;
                                return flag2;
                            }
                        } finally {
                            IOUtils.closeQuietly(bufferedreader);
                        }
                    }
                }
            } catch (IOException | URISyntaxException urisyntaxexception) {
                CraftingManager.field_192422_a.error("Couldn\'t get a list of all recipe files", urisyntaxexception);
                flag = false;
                return flag;
            }
        } finally {
            IOUtils.closeQuietly(filesystem);
        }

        return true;
    }

    private static IRecipe func_193376_a(JsonObject jsonobject) {
        String s = JsonUtils.func_151200_h(jsonobject, "type");

        if ("crafting_shaped".equals(s)) {
            return ShapedRecipes.func_193362_a(jsonobject);
        } else if ("crafting_shapeless".equals(s)) {
            return ShapelessRecipes.func_193363_a(jsonobject);
        } else {
            throw new JsonSyntaxException("Invalid or unsupported recipe type \'" + s + "\'");
        }
    }

    public static void func_193379_a(String s, IRecipe irecipe) {
        func_193372_a(new ResourceLocation(s), irecipe);
    }

    public static void func_193372_a(ResourceLocation minecraftkey, IRecipe irecipe) {
        if (CraftingManager.field_193380_a.func_148741_d(minecraftkey)) {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + minecraftkey);
        } else {
            irecipe.setKey(minecraftkey); // CraftBukkit
            CraftingManager.field_193380_a.func_177775_a(CraftingManager.field_193381_c++, minecraftkey, irecipe);
        }
    }

    public static ItemStack func_82787_a(InventoryCrafting inventorycrafting, World world) {
        Iterator iterator = CraftingManager.field_193380_a.iterator();

        IRecipe irecipe;

        do {
            if (!iterator.hasNext()) {
                inventorycrafting.currentRecipe = null; // CraftBukkit - Clear recipe when no recipe is found
                return ItemStack.field_190927_a;
            }

            irecipe = (IRecipe) iterator.next();
        } while (!irecipe.func_77569_a(inventorycrafting, world));

        inventorycrafting.currentRecipe = irecipe; // CraftBukkit
        return irecipe.func_77572_b(inventorycrafting);
    }

    @Nullable
    public static IRecipe func_192413_b(InventoryCrafting inventorycrafting, World world) {
        Iterator iterator = CraftingManager.field_193380_a.iterator();

        IRecipe irecipe;

        do {
            if (!iterator.hasNext()) {
                inventorycrafting.currentRecipe = null; // CraftBukkit - Clear recipe when no recipe is found
                return null;
            }

            irecipe = (IRecipe) iterator.next();
        } while (!irecipe.func_77569_a(inventorycrafting, world));

        inventorycrafting.currentRecipe = irecipe; // CraftBukkit
        return irecipe;
    }

    public static NonNullList<ItemStack> func_180303_b(InventoryCrafting inventorycrafting, World world) {
        Iterator iterator = CraftingManager.field_193380_a.iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();

            if (irecipe.func_77569_a(inventorycrafting, world)) {
                return irecipe.func_179532_b(inventorycrafting);
            }
        }

        NonNullList nonnulllist = NonNullList.func_191197_a(inventorycrafting.func_70302_i_(), ItemStack.field_190927_a);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, inventorycrafting.func_70301_a(i));
        }

        return nonnulllist;
    }

    @Nullable
    public static IRecipe func_193373_a(ResourceLocation minecraftkey) {
        return (IRecipe) CraftingManager.field_193380_a.func_82594_a(minecraftkey);
    }

    public static int func_193375_a(IRecipe irecipe) {
        return CraftingManager.field_193380_a.func_148757_b(irecipe); // CraftBukkit - decompile error
    }

    @Nullable
    public static IRecipe func_193374_a(int i) {
        return (IRecipe) CraftingManager.field_193380_a.func_148754_a(i);
    }
}
