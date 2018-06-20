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

    private static final Logger field_186525_a = LogManager.getLogger();
    private static final Gson field_186526_b = (new GsonBuilder()).registerTypeAdapter(RandomValueRange.class, new RandomValueRange.a()).registerTypeAdapter(LootPool.class, new LootPool.a()).registerTypeAdapter(LootTable.class, new LootTable.a()).registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.a()).registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.a()).registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.a()).registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.a()).create();
    private final LoadingCache<ResourceLocation, LootTable> field_186527_c = CacheBuilder.newBuilder().build(new LootTableManager.a(null));
    private final File field_186528_d;

    public LootTableManager(@Nullable File file) {
        this.field_186528_d = file;
        this.func_186522_a();
    }

    public LootTable func_186521_a(ResourceLocation minecraftkey) {
        return this.field_186527_c.getUnchecked(minecraftkey);
    }

    public void func_186522_a() {
        this.field_186527_c.invalidateAll();
        Iterator iterator = LootTableList.func_186374_a().iterator();

        while (iterator.hasNext()) {
            ResourceLocation minecraftkey = (ResourceLocation) iterator.next();

            this.func_186521_a(minecraftkey);
        }

    }

    class a extends CacheLoader<ResourceLocation, LootTable> {

        private a() {}

        @Override
        public LootTable load(ResourceLocation minecraftkey) throws Exception {
            if (minecraftkey.func_110623_a().contains(".")) {
                LootTableManager.field_186525_a.debug("Invalid loot table name \'{}\' (can\'t contain periods)", minecraftkey);
                return LootTable.field_186464_a;
            } else {
                LootTable loottable = this.b(minecraftkey);

                if (loottable == null) {
                    loottable = this.c(minecraftkey);
                }

                if (loottable == null) {
                    loottable = LootTable.field_186464_a;
                    LootTableManager.field_186525_a.warn("Couldn\'t find resource table {}", minecraftkey);
                }

                return loottable;
            }
        }

        @Nullable
        private LootTable b(ResourceLocation minecraftkey) {
            if (LootTableManager.this.field_186528_d == null) {
                return null;
            } else {
                File file = new File(new File(LootTableManager.this.field_186528_d, minecraftkey.func_110624_b()), minecraftkey.func_110623_a() + ".json");

                if (file.exists()) {
                    if (file.isFile()) {
                        String s;

                        try {
                            s = Files.toString(file, StandardCharsets.UTF_8);
                        } catch (IOException ioexception) {
                            LootTableManager.field_186525_a.warn("Couldn\'t load loot table {} from {}", minecraftkey, file, ioexception);
                            return LootTable.field_186464_a;
                        }

                        try {
                            return JsonUtils.func_188178_a(LootTableManager.field_186526_b, s, LootTable.class);
                        } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                            LootTableManager.field_186525_a.error("Couldn\'t load loot table {} from {}", minecraftkey, file, jsonparseexception);
                            return LootTable.field_186464_a;
                        }
                    } else {
                        LootTableManager.field_186525_a.warn("Expected to find loot table {} at {} but it was a folder.", minecraftkey, file);
                        return LootTable.field_186464_a;
                    }
                } else {
                    return null;
                }
            }
        }

        @Nullable
        private LootTable c(ResourceLocation minecraftkey) {
            URL url = LootTableManager.class.getResource("/assets/" + minecraftkey.func_110624_b() + "/loot_tables/" + minecraftkey.func_110623_a() + ".json");

            if (url != null) {
                String s;

                try {
                    s = Resources.toString(url, StandardCharsets.UTF_8);
                } catch (IOException ioexception) {
                    LootTableManager.field_186525_a.warn("Couldn\'t load loot table {} from {}", minecraftkey, url, ioexception);
                    return LootTable.field_186464_a;
                }

                try {
                    return JsonUtils.func_188178_a(LootTableManager.field_186526_b, s, LootTable.class);
                } catch (JsonParseException jsonparseexception) {
                    LootTableManager.field_186525_a.error("Couldn\'t load loot table {} from {}", minecraftkey, url, jsonparseexception);
                    return LootTable.field_186464_a;
                }
            } else {
                return null;
            }
        }

        a(Object object) {
            this();
        }
    }
}
