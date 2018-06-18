package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class AdvancementManager {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final Gson GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(Advancement.Builder.class, new JsonDeserializer() {
        public Advancement.Builder a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "advancement");

            return Advancement.Builder.deserialize(jsonobject, jsondeserializationcontext);
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }).registerTypeAdapter(AdvancementRewards.class, new AdvancementRewards.a()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeHierarchyAdapter(Style.class, new Style.Serializer()).registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();
    public static final AdvancementList ADVANCEMENT_LIST = new AdvancementList();
    public final File advancementsDir;
    private boolean hasErrored;

    public AdvancementManager(@Nullable File file) {
        this.advancementsDir = file;
        this.reload();
    }

    public void reload() {
        this.hasErrored = false;
        AdvancementManager.ADVANCEMENT_LIST.clear();
        Map map = this.loadCustomAdvancements();

        this.loadBuiltInAdvancements(map);
        AdvancementManager.ADVANCEMENT_LIST.loadAdvancements(map);
        Iterator iterator = AdvancementManager.ADVANCEMENT_LIST.getRoots().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            if (advancement.getDisplay() != null) {
                AdvancementTreeNode.layout(advancement);
            }
        }

    }

    public boolean hasErrored() {
        return this.hasErrored;
    }

    private Map<ResourceLocation, Advancement.Builder> loadCustomAdvancements() {
        if (this.advancementsDir == null) {
            return Maps.newHashMap();
        } else {
            HashMap hashmap = Maps.newHashMap();

            this.advancementsDir.mkdirs();
            Iterator iterator = FileUtils.listFiles(this.advancementsDir, new String[] { "json"}, true).iterator();

            while (iterator.hasNext()) {
                File file = (File) iterator.next();
                String s = FilenameUtils.removeExtension(this.advancementsDir.toURI().relativize(file.toURI()).toString());
                String[] astring = s.split("/", 2);

                if (astring.length == 2) {
                    ResourceLocation minecraftkey = new ResourceLocation(astring[0], astring[1]);

                    try {
                        Advancement.Builder advancement_serializedadvancement = (Advancement.Builder) JsonUtils.gsonDeserialize(AdvancementManager.GSON, FileUtils.readFileToString(file, StandardCharsets.UTF_8), Advancement.Builder.class);

                        if (advancement_serializedadvancement == null) {
                            AdvancementManager.LOGGER.error("Couldn\'t load custom advancement " + minecraftkey + " from " + file + " as it\'s empty or null");
                        } else {
                            hashmap.put(minecraftkey, advancement_serializedadvancement);
                        }
                    } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                        AdvancementManager.LOGGER.error("Parsing error loading custom advancement " + minecraftkey, jsonparseexception);
                        this.hasErrored = true;
                    } catch (IOException ioexception) {
                        AdvancementManager.LOGGER.error("Couldn\'t read custom advancement " + minecraftkey + " from " + file, ioexception);
                        this.hasErrored = true;
                    }
                }
            }

            return hashmap;
        }
    }

    private void loadBuiltInAdvancements(Map<ResourceLocation, Advancement.Builder> map) {
        FileSystem filesystem = null;

        try {
            URL url = AdvancementManager.class.getResource("/assets/.mcassetsroot");

            if (url == null) {
                AdvancementManager.LOGGER.error("Couldn\'t find .mcassetsroot");
                this.hasErrored = true;
            } else {
                URI uri = url.toURI();
                java.nio.file.Path java_nio_file_path;

                if ("file".equals(uri.getScheme())) {
                    java_nio_file_path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/advancements").toURI());
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        AdvancementManager.LOGGER.error("Unsupported scheme " + uri + " trying to list all built-in advancements (NYI?)");
                        this.hasErrored = true;
                        return;
                    }

                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    java_nio_file_path = filesystem.getPath("/assets/minecraft/advancements", new String[0]);
                }

                Iterator iterator = Files.walk(java_nio_file_path, new FileVisitOption[0]).iterator();

                while (iterator.hasNext()) {
                    java.nio.file.Path java_nio_file_path1 = (java.nio.file.Path) iterator.next();

                    if ("json".equals(FilenameUtils.getExtension(java_nio_file_path1.toString()))) {
                        java.nio.file.Path java_nio_file_path2 = java_nio_file_path.relativize(java_nio_file_path1);
                        String s = FilenameUtils.removeExtension(java_nio_file_path2.toString()).replaceAll("\\\\", "/");
                        ResourceLocation minecraftkey = new ResourceLocation("minecraft", s);
                        // Spigot start
                        if (org.spigotmc.SpigotConfig.disabledAdvancements != null && (org.spigotmc.SpigotConfig.disabledAdvancements.contains("*") || org.spigotmc.SpigotConfig.disabledAdvancements.contains(minecraftkey.toString()))) {
                            continue;
                        }
                        // Spigot end

                        if (!map.containsKey(minecraftkey)) {
                            BufferedReader bufferedreader = null;

                            try {
                                bufferedreader = Files.newBufferedReader(java_nio_file_path1);
                                Advancement.Builder advancement_serializedadvancement = (Advancement.Builder) JsonUtils.fromJson(AdvancementManager.GSON, (Reader) bufferedreader, Advancement.Builder.class);

                                map.put(minecraftkey, advancement_serializedadvancement);
                            } catch (JsonParseException jsonparseexception) {
                                AdvancementManager.LOGGER.error("Parsing error loading built-in advancement " + minecraftkey, jsonparseexception);
                                this.hasErrored = true;
                            } catch (IOException ioexception) {
                                AdvancementManager.LOGGER.error("Couldn\'t read advancement " + minecraftkey + " from " + java_nio_file_path1, ioexception);
                                this.hasErrored = true;
                            } finally {
                                IOUtils.closeQuietly(bufferedreader);
                            }
                        }
                    }
                }

            }
        } catch (IOException | URISyntaxException urisyntaxexception) {
            AdvancementManager.LOGGER.error("Couldn\'t get a list of all built-in advancement files", urisyntaxexception);
            this.hasErrored = true;
        } finally {
            IOUtils.closeQuietly(filesystem);
        }
    }

    @Nullable
    public Advancement getAdvancement(ResourceLocation minecraftkey) {
        return AdvancementManager.ADVANCEMENT_LIST.getAdvancement(minecraftkey);
    }

    public Iterable<Advancement> getAdvancements() {
        return AdvancementManager.ADVANCEMENT_LIST.getAdvancements();
    }
}
