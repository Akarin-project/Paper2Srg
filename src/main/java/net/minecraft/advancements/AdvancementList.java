package net.minecraft.advancements;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;

public class AdvancementList {

    private static final Logger field_192091_a = LogManager.getLogger();
    public final Map<ResourceLocation, Advancement> field_192092_b = Maps.newHashMap();
    private final Set<Advancement> field_192093_c = Sets.newLinkedHashSet();
    private final Set<Advancement> field_192094_d = Sets.newLinkedHashSet();
    private AdvancementList.a field_192095_e;

    public AdvancementList() {}

    public void func_192083_a(Map<ResourceLocation, Advancement.Builder> map) {
        Function function = Functions.forMap(this.field_192092_b, (Object) null);

        label42:
        while (!map.isEmpty()) {
            boolean flag = false;
            Iterator iterator = map.entrySet().iterator();

            Entry entry;

            while (iterator.hasNext()) {
                entry = (Entry) iterator.next();
                ResourceLocation minecraftkey = (ResourceLocation) entry.getKey();
                Advancement.Builder advancement_serializedadvancement = (Advancement.Builder) entry.getValue();

                if (advancement_serializedadvancement.func_192058_a(function)) {
                    Advancement advancement = advancement_serializedadvancement.func_192056_a(minecraftkey);

                    this.field_192092_b.put(minecraftkey, advancement);
                    flag = true;
                    iterator.remove();
                    if (advancement.func_192070_b() == null) {
                        this.field_192093_c.add(advancement);
                        if (this.field_192095_e != null) {
                            this.field_192095_e.a(advancement);
                        }
                    } else {
                        this.field_192094_d.add(advancement);
                        if (this.field_192095_e != null) {
                            this.field_192095_e.c(advancement);
                        }
                    }
                }
            }

            if (!flag) {
                iterator = map.entrySet().iterator();

                while (true) {
                    if (!iterator.hasNext()) {
                        break label42;
                    }

                    entry = (Entry) iterator.next();
                    AdvancementList.field_192091_a.error("Couldn\'t load advancement " + entry.getKey() + ": " + entry.getValue());
                }
            }
        }

        // Advancements.a.info("Loaded " + this.b.size() + " advancements"); // CraftBukkit - moved to AdvancementDataWorld#reload
    }

    public void func_192087_a() {
        this.field_192092_b.clear();
        this.field_192093_c.clear();
        this.field_192094_d.clear();
        if (this.field_192095_e != null) {
            this.field_192095_e.a();
        }

    }

    public Iterable<Advancement> func_192088_b() {
        return this.field_192093_c;
    }

    public Iterable<Advancement> func_192089_c() {
        return this.field_192092_b.values();
    }

    @Nullable
    public Advancement func_192084_a(ResourceLocation minecraftkey) {
        return this.field_192092_b.get(minecraftkey);
    }

    public interface a {

        void a(Advancement advancement);

        void c(Advancement advancement);

        void a();
    }
}
