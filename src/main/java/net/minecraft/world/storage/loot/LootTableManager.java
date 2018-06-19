package net.minecraft.world.storage.loot;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;

public class LootTableManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(RandomValueRange.class, new RandomValueRange.a()).registerTypeAdapter(LootPool.class, new LootPool.a()).registerTypeAdapter(LootTable.class, new LootTable.a()).registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.a()).registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.a()).registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.a()).registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.a()).create();
    private final LoadingCache<ResourceLocation, LootTable> registeredLootTables = CacheBuilder.newBuilder().build(new LootTableManager.a());
    private final File baseFolder;

    public LootTableManager(@Nullable File file) {
        this.baseFolder = file;
        this.reloadLootTables();
    }

    public LootTable getLootTableFromLocation(ResourceLocation minecraftkey) {
        return this.registeredLootTables.getUnchecked(minecraftkey);
    }

    public void reloadLootTables() {
        this.registeredLootTables.invalidateAll();
        Iterator iterator = LootTableList.getAll().iterator();

        while (iterator.hasNext()) {
            ResourceLocation minecraftkey = (ResourceLocation) iterator.next();

            this.getLootTableFromLocation(minecraftkey);
        }

    }

    class a extends CacheLoader<ResourceLocation, LootTable> {

        private a() {}

        public LootTable a(ResourceLocation minecraftkey) throws Exception {
            if (minecraftkey.getResourcePath().contains(".")) {
                LootTableManager.LOGGER.debug("Invalid loot table name \'{}\' (can\'t contain periods)", minecraftkey);
                return LootTable.EMPTY_LOOT_TABLE;
            } else {
                LootTable loottable = this.b(minecraftkey);

                if (loottable == null) {
                    loottable = this.c(minecraftkey);
                }

                if (loottable == null) {
                    loottable = LootTable.EMPTY_LOOT_TABLE;
                    LootTableManager.LOGGER.warn("Couldn\'t find resource table {}", minecraftkey);
                }

                return loottable;
            }
        }

        @Nullable
        private LootTable b(ResourceLocation minecraftkey) {
            if (LootTableManager.this.baseFolder == null) {
                return null;
            } else {
                File file = new File(new File(LootTableManager.this.baseFolder, minecraftkey.getResourceDomain()), minecraftkey.getResourcePath() + ".json");

                if (file.exists()) {
                    if (file.isFile()) {
                        String s;

                        try {
                            s = Files.toString(file, StandardCharsets.UTF_8);
                        } catch (IOException ioexception) {
                            LootTableManager.LOGGER.warn("Couldn\'t load loot table {} from {}", minecraftkey, file, ioexception);
                            return LootTable.EMPTY_LOOT_TABLE;
                        }

                        try {
                            return JsonUtils.gsonDeserialize(LootTableManager.GSON_INSTANCE, s, LootTable.class);
                        } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                            LootTableManager.LOGGER.error("Couldn\'t load loot table {} from {}", minecraftkey, file, jsonparseexception);
                            return LootTable.EMPTY_LOOT_TABLE;
                        }
                    } else {
                        LootTableManager.LOGGER.warn("Expected to find loot table {} at {} but it was a folder.", minecraftkey, file);
                        return LootTable.EMPTY_LOOT_TABLE;
                    }
                } else {
                    return null;
                }
            }
        }

        @Nullable
        private LootTable c(ResourceLocation minecraftkey) {
            URL url = LootTableManager.class.getResource("/assets/" + minecraftkey.getResourceDomain() + "/loot_tables/" + minecraftkey.getResourcePath() + ".json");

            if (url != null) {
                String s;

                try {
                    s = Resources.toString(url, StandardCharsets.UTF_8);
                } catch (IOException ioexception) {
                    LootTableManager.LOGGER.warn("Couldn\'t load loot table {} from {}", minecraftkey, url, ioexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }

                try {
                    return JsonUtils.gsonDeserialize(LootTableManager.GSON_INSTANCE, s, LootTable.class);
                } catch (JsonParseException jsonparseexception) {
                    LootTableManager.LOGGER.error("Couldn\'t load loot table {} from {}", minecraftkey, url, jsonparseexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }
            } else {
                return null;
            }
        }

        @Override
        public LootTable load(ResourceLocation object) throws Exception {
            return this.a(object);
        }

        a(Object object) {
            this();
        }
    }
}
