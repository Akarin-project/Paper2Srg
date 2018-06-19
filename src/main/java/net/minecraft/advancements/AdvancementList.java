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

    private static final Logger LOGGER = LogManager.getLogger();
    public final Map<ResourceLocation, Advancement> advancements = Maps.newHashMap();
    private final Set<Advancement> roots = Sets.newLinkedHashSet();
    private final Set<Advancement> nonRoots = Sets.newLinkedHashSet();
    private AdvancementList.a listener;

    public AdvancementList() {}

    public void loadAdvancements(Map<ResourceLocation, Advancement.Builder> map) {
        Function function = Functions.forMap(this.advancements, (Object) null);

        label42:
        while (!map.isEmpty()) {
            boolean flag = false;
            Iterator iterator = map.entrySet().iterator();

            Entry entry;

            while (iterator.hasNext()) {
                entry = (Entry) iterator.next();
                ResourceLocation minecraftkey = (ResourceLocation) entry.getKey();
                Advancement.Builder advancement_serializedadvancement = (Advancement.Builder) entry.getValue();

                if (advancement_serializedadvancement.resolveParent(function)) {
                    Advancement advancement = advancement_serializedadvancement.build(minecraftkey);

                    this.advancements.put(minecraftkey, advancement);
                    flag = true;
                    iterator.remove();
                    if (advancement.getParent() == null) {
                        this.roots.add(advancement);
                        if (this.listener != null) {
                            this.listener.a(advancement);
                        }
                    } else {
                        this.nonRoots.add(advancement);
                        if (this.listener != null) {
                            this.listener.c(advancement);
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
                    AdvancementList.LOGGER.error("Couldn\'t load advancement " + entry.getKey() + ": " + entry.getValue());
                }
            }
        }

        // Advancements.a.info("Loaded " + this.b.size() + " advancements"); // CraftBukkit - moved to AdvancementDataWorld#reload
    }

    public void clear() {
        this.advancements.clear();
        this.roots.clear();
        this.nonRoots.clear();
        if (this.listener != null) {
            this.listener.a();
        }

    }

    public Iterable<Advancement> getRoots() {
        return this.roots;
    }

    public Iterable<Advancement> getAdvancements() {
        return this.advancements.values();
    }

    @Nullable
    public Advancement getAdvancement(ResourceLocation minecraftkey) {
        return this.advancements.get(minecraftkey);
    }

    public interface a {

        void a(Advancement advancement);

        void c(Advancement advancement);

        void a();
    }
}
