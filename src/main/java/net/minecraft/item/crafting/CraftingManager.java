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

    private static final Logger LOGGER = LogManager.getLogger();
    private static int nextAvailableId;
    public static RegistryNamespaced<ResourceLocation, IRecipe> REGISTRY = new RegistryNamespaced();

    public CraftingManager() {}

    public static boolean init() {
        try {
            CraftingManager.nextAvailableId = 0; // Reset recipe ID count
            register("armordye", new RecipesArmorDyes());
            register("bookcloning", new RecipeBookCloning());
            register("mapcloning", new RecipesMapCloning());
            register("mapextending", new RecipesMapExtending());
            register("fireworks", new RecipeFireworks());
            register("repairitem", new RecipeRepairItem());
            register("tippedarrow", new RecipeTippedArrow());
            register("bannerduplicate", new RecipesBanners.RecipeDuplicatePattern());
            register("banneraddpattern", new RecipesBanners.RecipeAddPattern());
            register("shielddecoration", new ShieldRecipes.Decoration());
            register("shulkerboxcoloring", new ShulkerBoxRecipes.ShulkerBoxColoring());
            return parseJsonRecipes();
        } catch (Throwable throwable) {
            return false;
        }
    }

    private static boolean parseJsonRecipes() {
        FileSystem filesystem = null;
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

        try {
            boolean flag;

            try {
                URL url = CraftingManager.class.getResource("/assets/.mcassetsroot");

                if (url == null) {
                    CraftingManager.LOGGER.error("Couldn\'t find .mcassetsroot");
                    flag = false;
                    return flag;
                }

                URI uri = url.toURI();
                java.nio.file.Path java_nio_file_path;

                if ("file".equals(uri.getScheme())) {
                    java_nio_file_path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/recipes").toURI());
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        CraftingManager.LOGGER.error("Unsupported scheme " + uri + " trying to list all recipes");
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
                                register(s, parseRecipeJson((JsonObject) JsonUtils.fromJson(gson, (Reader) bufferedreader, JsonObject.class)));
                            } catch (JsonParseException jsonparseexception) {
                                CraftingManager.LOGGER.error("Parsing error loading recipe " + minecraftkey, jsonparseexception);
                                flag2 = false;
                                return flag2;
                            } catch (IOException ioexception) {
                                CraftingManager.LOGGER.error("Couldn\'t read recipe " + minecraftkey + " from " + java_nio_file_path1, ioexception);
                                flag2 = false;
                                return flag2;
                            }
                        } finally {
                            IOUtils.closeQuietly(bufferedreader);
                        }
                    }
                }
            } catch (IOException | URISyntaxException urisyntaxexception) {
                CraftingManager.LOGGER.error("Couldn\'t get a list of all recipe files", urisyntaxexception);
                flag = false;
                return flag;
            }
        } finally {
            IOUtils.closeQuietly(filesystem);
        }

        return true;
    }

    private static IRecipe parseRecipeJson(JsonObject jsonobject) {
        String s = JsonUtils.getString(jsonobject, "type");

        if ("crafting_shaped".equals(s)) {
            return ShapedRecipes.deserialize(jsonobject);
        } else if ("crafting_shapeless".equals(s)) {
            return ShapelessRecipes.deserialize(jsonobject);
        } else {
            throw new JsonSyntaxException("Invalid or unsupported recipe type \'" + s + "\'");
        }
    }

    public static void register(String s, IRecipe irecipe) {
        register(new ResourceLocation(s), irecipe);
    }

    public static void register(ResourceLocation minecraftkey, IRecipe irecipe) {
        if (CraftingManager.REGISTRY.containsKey(minecraftkey)) {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + minecraftkey);
        } else {
            irecipe.setKey(minecraftkey); // CraftBukkit
            CraftingManager.REGISTRY.register(CraftingManager.nextAvailableId++, minecraftkey, irecipe);
        }
    }

    public static ItemStack findMatchingResult(InventoryCrafting inventorycrafting, World world) {
        Iterator iterator = CraftingManager.REGISTRY.iterator();

        IRecipe irecipe;

        do {
            if (!iterator.hasNext()) {
                inventorycrafting.currentRecipe = null; // CraftBukkit - Clear recipe when no recipe is found
                return ItemStack.EMPTY;
            }

            irecipe = (IRecipe) iterator.next();
        } while (!irecipe.matches(inventorycrafting, world));

        inventorycrafting.currentRecipe = irecipe; // CraftBukkit
        return irecipe.getCraftingResult(inventorycrafting);
    }

    @Nullable
    public static IRecipe findMatchingRecipe(InventoryCrafting inventorycrafting, World world) {
        Iterator iterator = CraftingManager.REGISTRY.iterator();

        IRecipe irecipe;

        do {
            if (!iterator.hasNext()) {
                inventorycrafting.currentRecipe = null; // CraftBukkit - Clear recipe when no recipe is found
                return null;
            }

            irecipe = (IRecipe) iterator.next();
        } while (!irecipe.matches(inventorycrafting, world));

        inventorycrafting.currentRecipe = irecipe; // CraftBukkit
        return irecipe;
    }

    public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventorycrafting, World world) {
        Iterator iterator = CraftingManager.REGISTRY.iterator();

        while (iterator.hasNext()) {
            IRecipe irecipe = (IRecipe) iterator.next();

            if (irecipe.matches(inventorycrafting, world)) {
                return irecipe.getRemainingItems(inventorycrafting);
            }
        }

        NonNullList nonnulllist = NonNullList.withSize(inventorycrafting.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, inventorycrafting.getStackInSlot(i));
        }

        return nonnulllist;
    }

    @Nullable
    public static IRecipe getRecipe(ResourceLocation minecraftkey) {
        return (IRecipe) CraftingManager.REGISTRY.getObject(minecraftkey);
    }

    public static int getIDForRecipe(IRecipe irecipe) {
        return CraftingManager.REGISTRY.getIDForObject(irecipe); // CraftBukkit - decompile error
    }

    @Nullable
    public static IRecipe getRecipeById(int i) {
        return (IRecipe) CraftingManager.REGISTRY.getObjectById(i);
    }
}
