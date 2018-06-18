package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.server.CriterionTrigger;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class PlayerAdvancements {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.a()).registerTypeAdapter(ResourceLocation.class, new MinecraftKey.a()).setPrettyPrinting().create();
    private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> MAP_TOKEN = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() { // CraftBukkit - decompile error
    };
    private final MinecraftServer server;
    private final File progressFile;
    public final Map<Advancement, AdvancementProgress> progress = Maps.newLinkedHashMap();
    private final Set<Advancement> visible = Sets.newLinkedHashSet();
    private final Set<Advancement> visibilityChanged = Sets.newLinkedHashSet();
    private final Set<Advancement> progressChanged = Sets.newLinkedHashSet();
    private EntityPlayerMP player;
    @Nullable
    private Advancement lastSelectedTab;
    private boolean isFirstPacket = true;

    public PlayerAdvancements(MinecraftServer minecraftserver, File file, EntityPlayerMP entityplayer) {
        this.server = minecraftserver;
        this.progressFile = file;
        this.player = entityplayer;
        this.load();
    }

    public void setPlayer(EntityPlayerMP entityplayer) {
        this.player = entityplayer;
    }

    public void dispose() {
        Iterator iterator = CriteriaTriggers.getAll().iterator();

        while (iterator.hasNext()) {
            ICriterionTrigger criteriontrigger = (ICriterionTrigger) iterator.next();

            criteriontrigger.removeAllListeners(this);
        }

    }

    public void reload() {
        this.dispose();
        this.progress.clear();
        this.visible.clear();
        this.visibilityChanged.clear();
        this.progressChanged.clear();
        this.isFirstPacket = true;
        this.lastSelectedTab = null;
        this.load();
    }

    private void registerListeners() {
        Iterator iterator = this.server.getAdvancementManager().getAdvancements().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.registerListeners(advancement);
        }

    }

    private void ensureAllVisible() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.progress.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Advancement, AdvancementProgress> entry = (Entry) iterator.next(); // CraftBukkit - decompile error

            if (entry.getValue().isDone()) {
                arraylist.add(entry.getKey());
                this.progressChanged.add(entry.getKey());
            }
        }

        iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.ensureVisibility(advancement);
        }

    }

    private void checkForAutomaticTriggers() {
        Iterator iterator = this.server.getAdvancementManager().getAdvancements().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            if (advancement.getCriteria().isEmpty()) {
                this.grantCriterion(advancement, "");
                advancement.getRewards().apply(this.player);
            }
        }

    }

    private void load() {
        if (this.progressFile.isFile()) {
            try {
                String s = Files.toString(this.progressFile, StandardCharsets.UTF_8);
                Map<ResourceLocation, AdvancementProgress> map = (Map) JsonUtils.gsonDeserialize(PlayerAdvancements.GSON, s, PlayerAdvancements.MAP_TOKEN.getType()); // CraftBukkit

                if (map == null) {
                    throw new JsonParseException("Found null for advancements");
                }

                Stream stream = map.entrySet().stream().sorted(Comparator.comparing(Entry::getValue));
                Iterator iterator = ((List) stream.collect(Collectors.toList())).iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    Advancement advancement = this.server.getAdvancementManager().getAdvancement((ResourceLocation) entry.getKey());

                    if (advancement == null) {
                        // CraftBukkit start
                        if (((ResourceLocation) entry.getKey()).getResourceDomain().equals("minecraft")) {
                            PlayerAdvancements.LOGGER.warn("Ignored advancement \'" + entry.getKey() + "\' in progress file " + this.progressFile + " - it doesn\'t exist anymore?");
                        }
                        // CraftBukkit end
                    } else {
                        this.startProgress(advancement, (AdvancementProgress) entry.getValue());
                    }
                }
            } catch (JsonParseException jsonparseexception) {
                PlayerAdvancements.LOGGER.error("Couldn\'t parse player advancements in " + this.progressFile, jsonparseexception);
            } catch (IOException ioexception) {
                PlayerAdvancements.LOGGER.error("Couldn\'t access player advancements in " + this.progressFile, ioexception);
            }
        }

        this.checkForAutomaticTriggers();
        this.ensureAllVisible();
        this.registerListeners();
    }

    public void save() {
        if (org.spigotmc.SpigotConfig.disableAdvancementSaving) return;
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = this.progress.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            AdvancementProgress advancementprogress = (AdvancementProgress) entry.getValue();

            if (advancementprogress.hasProgress()) {
                hashmap.put(((Advancement) entry.getKey()).getId(), advancementprogress);
            }
        }

        if (this.progressFile.getParentFile() != null) {
            this.progressFile.getParentFile().mkdirs();
        }

        try {
            Files.write(PlayerAdvancements.GSON.toJson(hashmap), this.progressFile, StandardCharsets.UTF_8);
        } catch (IOException ioexception) {
            PlayerAdvancements.LOGGER.error("Couldn\'t save player advancements to " + this.progressFile, ioexception);
        }

    }

    public boolean grantCriterion(Advancement advancement, String s) {
        boolean flag = false;
        AdvancementProgress advancementprogress = this.getProgress(advancement);
        boolean flag1 = advancementprogress.isDone();

        if (advancementprogress.grantCriterion(s)) {
            // Paper start
            if (!new com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent(this.player.getBukkitEntity(), advancement.bukkit, s).callEvent()) {
                advancementprogress.revokeCriterion(s);
                return false;
            }
            // Paper end
            this.unregisterListeners(advancement);
            this.progressChanged.add(advancement);
            flag = true;
            if (!flag1 && advancementprogress.isDone()) {
                this.player.world.getServer().getPluginManager().callEvent(new org.bukkit.event.player.PlayerAdvancementDoneEvent(this.player.getBukkitEntity(), advancement.bukkit)); // CraftBukkit
                advancement.getRewards().apply(this.player);
                if (advancement.getDisplay() != null && advancement.getDisplay().shouldAnnounceToChat() && this.player.world.getGameRules().getBoolean("announceAdvancements")) {
                    this.server.getPlayerList().sendMessage(new TextComponentTranslation("chat.type.advancement." + advancement.getDisplay().getFrame().getName(), new Object[] { this.player.getDisplayName(), advancement.getDisplayText()}));
                }
            }
        }

        if (advancementprogress.isDone()) {
            this.ensureVisibility(advancement);
        }

        return flag;
    }

    public boolean revokeCriterion(Advancement advancement, String s) {
        boolean flag = false;
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (advancementprogress.revokeCriterion(s)) {
            this.registerListeners(advancement);
            this.progressChanged.add(advancement);
            flag = true;
        }

        if (!advancementprogress.hasProgress()) {
            this.ensureVisibility(advancement);
        }

        return flag;
    }

    private void registerListeners(Advancement advancement) {
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (!advancementprogress.isDone()) {
            Iterator iterator = advancement.getCriteria().entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                CriterionProgress criterionprogress = advancementprogress.getCriterionProgress((String) entry.getKey());

                if (criterionprogress != null && !criterionprogress.isObtained()) {
                    ICriterionInstance criterioninstance = ((Criterion) entry.getValue()).getCriterionInstance();

                    if (criterioninstance != null) {
                        ICriterionTrigger criteriontrigger = CriteriaTriggers.get(criterioninstance.getId());

                        if (criteriontrigger != null) {
                            criteriontrigger.a(this, new CriterionTrigger.a(criterioninstance, advancement, (String) entry.getKey()));
                        }
                    }
                }
            }

        }
    }

    private void unregisterListeners(Advancement advancement) {
        AdvancementProgress advancementprogress = this.getProgress(advancement);
        Iterator iterator = advancement.getCriteria().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            CriterionProgress criterionprogress = advancementprogress.getCriterionProgress((String) entry.getKey());

            if (criterionprogress != null && (criterionprogress.isObtained() || advancementprogress.isDone())) {
                ICriterionInstance criterioninstance = ((Criterion) entry.getValue()).getCriterionInstance();

                if (criterioninstance != null) {
                    ICriterionTrigger criteriontrigger = CriteriaTriggers.get(criterioninstance.getId());

                    if (criteriontrigger != null) {
                        criteriontrigger.b(this, new CriterionTrigger.a(criterioninstance, advancement, (String) entry.getKey()));
                    }
                }
            }
        }

    }

    public void flushDirty(EntityPlayerMP entityplayer) {
        if (!this.visibilityChanged.isEmpty() || !this.progressChanged.isEmpty()) {
            HashMap hashmap = Maps.newHashMap();
            LinkedHashSet linkedhashset = Sets.newLinkedHashSet();
            LinkedHashSet linkedhashset1 = Sets.newLinkedHashSet();
            Iterator iterator = this.progressChanged.iterator();

            Advancement advancement;

            while (iterator.hasNext()) {
                advancement = (Advancement) iterator.next();
                if (this.visible.contains(advancement)) {
                    hashmap.put(advancement.getId(), this.progress.get(advancement));
                }
            }

            iterator = this.visibilityChanged.iterator();

            while (iterator.hasNext()) {
                advancement = (Advancement) iterator.next();
                if (this.visible.contains(advancement)) {
                    linkedhashset.add(advancement);
                } else {
                    linkedhashset1.add(advancement.getId());
                }
            }

            if (!hashmap.isEmpty() || !linkedhashset.isEmpty() || !linkedhashset1.isEmpty()) {
                entityplayer.connection.sendPacket(new SPacketAdvancementInfo(this.isFirstPacket, linkedhashset, linkedhashset1, hashmap));
                this.visibilityChanged.clear();
                this.progressChanged.clear();
            }
        }

        this.isFirstPacket = false;
    }

    public void setSelectedTab(@Nullable Advancement advancement) {
        Advancement advancement1 = this.lastSelectedTab;

        if (advancement != null && advancement.getParent() == null && advancement.getDisplay() != null) {
            this.lastSelectedTab = advancement;
        } else {
            this.lastSelectedTab = null;
        }

        if (advancement1 != this.lastSelectedTab) {
            this.player.connection.sendPacket(new SPacketSelectAdvancementsTab(this.lastSelectedTab == null ? null : this.lastSelectedTab.getId()));
        }

    }

    public AdvancementProgress getProgress(Advancement advancement) {
        AdvancementProgress advancementprogress = this.progress.get(advancement);

        if (advancementprogress == null) {
            advancementprogress = new AdvancementProgress();
            this.startProgress(advancement, advancementprogress);
        }

        return advancementprogress;
    }

    private void startProgress(Advancement advancement, AdvancementProgress advancementprogress) {
        advancementprogress.update(advancement.getCriteria(), advancement.getRequirements());
        this.progress.put(advancement, advancementprogress);
    }

    private void ensureVisibility(Advancement advancement) {
        boolean flag = this.shouldBeVisible(advancement);
        boolean flag1 = this.visible.contains(advancement);

        if (flag && !flag1) {
            this.visible.add(advancement);
            this.visibilityChanged.add(advancement);
            if (this.progress.containsKey(advancement)) {
                this.progressChanged.add(advancement);
            }
        } else if (!flag && flag1) {
            this.visible.remove(advancement);
            this.visibilityChanged.add(advancement);
        }

        if (flag != flag1 && advancement.getParent() != null) {
            this.ensureVisibility(advancement.getParent());
        }

        Iterator iterator = advancement.getChildren().iterator();

        while (iterator.hasNext()) {
            Advancement advancement1 = (Advancement) iterator.next();

            this.ensureVisibility(advancement1);
        }

    }

    private boolean shouldBeVisible(Advancement advancement) {
        for (int i = 0; advancement != null && i <= 2; ++i) {
            if (i == 0 && this.hasCompletedChildrenOrSelf(advancement)) {
                return true;
            }

            if (advancement.getDisplay() == null) {
                return false;
            }

            AdvancementProgress advancementprogress = this.getProgress(advancement);

            if (advancementprogress.isDone()) {
                return true;
            }

            if (advancement.getDisplay().isHidden()) {
                return false;
            }

            advancement = advancement.getParent();
        }

        return false;
    }

    private boolean hasCompletedChildrenOrSelf(Advancement advancement) {
        AdvancementProgress advancementprogress = this.getProgress(advancement);

        if (advancementprogress.isDone()) {
            return true;
        } else {
            Iterator iterator = advancement.getChildren().iterator();

            Advancement advancement1;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                advancement1 = (Advancement) iterator.next();
            } while (!this.hasCompletedChildrenOrSelf(advancement1));

            return true;
        }
    }
}
