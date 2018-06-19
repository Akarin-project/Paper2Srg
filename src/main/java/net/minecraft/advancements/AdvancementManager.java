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

    private static final Logger field_192782_a = LogManager.getLogger();
    public static final Gson field_192783_b = (new GsonBuilder()).registerTypeHierarchyAdapter(Advancement.Builder.class, new JsonDeserializer() {
        public Advancement.Builder a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "advancement");

            return Advancement.Builder.func_192059_a(jsonobject, jsondeserializationcontext);
        }

        public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }).registerTypeAdapter(AdvancementRewards.class, new AdvancementRewards.a()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeHierarchyAdapter(Style.class, new Style.Serializer()).registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();
    public static final AdvancementList field_192784_c = new AdvancementList();
    public final File field_192785_d;
    private boolean field_193768_e;

    public AdvancementManager(@Nullable File file) {
        this.field_192785_d = file;
        this.func_192779_a();
    }

    public void func_192779_a() {
        this.field_193768_e = false;
        AdvancementManager.field_192784_c.func_192087_a();
        Map map = this.func_192781_c();

        this.func_192777_a(map);
        AdvancementManager.field_192784_c.func_192083_a(map);
        Iterator iterator = AdvancementManager.field_192784_c.func_192088_b().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            if (advancement.func_192068_c() != null) {
                AdvancementTreeNode.func_192323_a(advancement);
            }
        }

    }

    public boolean func_193767_b() {
        return this.field_193768_e;
    }

    private Map<ResourceLocation, Advancement.Builder> func_192781_c() {
        if (this.field_192785_d == null) {
            return Maps.newHashMap();
        } else {
            HashMap hashmap = Maps.newHashMap();

            this.field_192785_d.mkdirs();
            Iterator iterator = FileUtils.listFiles(this.field_192785_d, new String[] { "json"}, true).iterator();

            while (iterator.hasNext()) {
                File file = (File) iterator.next();
                String s = FilenameUtils.removeExtension(this.field_192785_d.toURI().relativize(file.toURI()).toString());
                String[] astring = s.split("/", 2);

                if (astring.length == 2) {
                    ResourceLocation minecraftkey = new ResourceLocation(astring[0], astring[1]);

                    try {
                        Advancement.Builder advancement_serializedadvancement = (Advancement.Builder) JsonUtils.func_188178_a(AdvancementManager.field_192783_b, FileUtils.readFileToString(file, StandardCharsets.UTF_8), Advancement.Builder.class);

                        if (advancement_serializedadvancement == null) {
                            AdvancementManager.field_192782_a.error("Couldn\'t load custom advancement " + minecraftkey + " from " + file + " as it\'s empty or null");
                        } else {
                            hashmap.put(minecraftkey, advancement_serializedadvancement);
                        }
                    } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                        AdvancementManager.field_192782_a.error("Parsing error loading custom advancement " + minecraftkey, jsonparseexception);
                        this.field_193768_e = true;
                    } catch (IOException ioexception) {
                        AdvancementManager.field_192782_a.error("Couldn\'t read custom advancement " + minecraftkey + " from " + file, ioexception);
                        this.field_193768_e = true;
                    }
                }
            }

            return hashmap;
        }
    }

    private void func_192777_a(Map<ResourceLocation, Advancement.Builder> map) {
        FileSystem filesystem = null;

        try {
            URL url = AdvancementManager.class.getResource("/assets/.mcassetsroot");

            if (url == null) {
                AdvancementManager.field_192782_a.error("Couldn\'t find .mcassetsroot");
                this.field_193768_e = true;
            } else {
                URI uri = url.toURI();
                java.nio.file.Path java_nio_file_path;

                if ("file".equals(uri.getScheme())) {
                    java_nio_file_path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/advancements").toURI());
                } else {
                    if (!"jar".equals(uri.getScheme())) {
                        AdvancementManager.field_192782_a.error("Unsupported scheme " + uri + " trying to list all built-in advancements (NYI?)");
                        this.field_193768_e = true;
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
                                Advancement.Builder advancement_serializedadvancement = (Advancement.Builder) JsonUtils.func_193839_a(AdvancementManager.field_192783_b, (Reader) bufferedreader, Advancement.Builder.class);

                                map.put(minecraftkey, advancement_serializedadvancement);
                            } catch (JsonParseException jsonparseexception) {
                                AdvancementManager.field_192782_a.error("Parsing error loading built-in advancement " + minecraftkey, jsonparseexception);
                                this.field_193768_e = true;
                            } catch (IOException ioexception) {
                                AdvancementManager.field_192782_a.error("Couldn\'t read advancement " + minecraftkey + " from " + java_nio_file_path1, ioexception);
                                this.field_193768_e = true;
                            } finally {
                                IOUtils.closeQuietly(bufferedreader);
                            }
                        }
                    }
                }

            }
        } catch (IOException | URISyntaxException urisyntaxexception) {
            AdvancementManager.field_192782_a.error("Couldn\'t get a list of all built-in advancement files", urisyntaxexception);
            this.field_193768_e = true;
        } finally {
            IOUtils.closeQuietly(filesystem);
        }
    }

    @Nullable
    public Advancement func_192778_a(ResourceLocation minecraftkey) {
        return AdvancementManager.field_192784_c.func_192084_a(minecraftkey);
    }

    public Iterable<Advancement> func_192780_b() {
        return AdvancementManager.field_192784_c.func_192089_c();
    }
}
