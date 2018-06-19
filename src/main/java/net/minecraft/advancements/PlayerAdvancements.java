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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class PlayerAdvancements {

    private static final Logger field_192753_a = LogManager.getLogger();
    private static final Gson field_192754_b = (new GsonBuilder()).registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.a()).registerTypeAdapter(ResourceLocation.class, new MinecraftKey.a()).setPrettyPrinting().create();
    private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> field_192755_c = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() { // CraftBukkit - decompile error
    };
    private final MinecraftServer field_192756_d;
    private final File field_192757_e;
    public final Map<Advancement, AdvancementProgress> field_192758_f = Maps.newLinkedHashMap();
    private final Set<Advancement> field_192759_g = Sets.newLinkedHashSet();
    private final Set<Advancement> field_192760_h = Sets.newLinkedHashSet();
    private final Set<Advancement> field_192761_i = Sets.newLinkedHashSet();
    private EntityPlayerMP field_192762_j;
    @Nullable
    private Advancement field_194221_k;
    private boolean field_192763_k = true;

    public PlayerAdvancements(MinecraftServer minecraftserver, File file, EntityPlayerMP entityplayer) {
        this.field_192756_d = minecraftserver;
        this.field_192757_e = file;
        this.field_192762_j = entityplayer;
        this.func_192740_f();
    }

    public void func_192739_a(EntityPlayerMP entityplayer) {
        this.field_192762_j = entityplayer;
    }

    public void func_192745_a() {
        Iterator iterator = CriteriaTriggers.func_192120_a().iterator();

        while (iterator.hasNext()) {
            ICriterionTrigger criteriontrigger = (ICriterionTrigger) iterator.next();

            criteriontrigger.func_192167_a(this);
        }

    }

    public void func_193766_b() {
        this.func_192745_a();
        this.field_192758_f.clear();
        this.field_192759_g.clear();
        this.field_192760_h.clear();
        this.field_192761_i.clear();
        this.field_192763_k = true;
        this.field_194221_k = null;
        this.func_192740_f();
    }

    private void func_192751_c() {
        Iterator iterator = this.field_192756_d.func_191949_aK().func_192780_b().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.func_193764_b(advancement);
        }

    }

    private void func_192752_d() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_192758_f.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Advancement, AdvancementProgress> entry = (Entry) iterator.next(); // CraftBukkit - decompile error

            if (((AdvancementProgress) entry.getValue()).func_192105_a()) {
                arraylist.add(entry.getKey());
                this.field_192761_i.add(entry.getKey());
            }
        }

        iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            this.func_192742_b(advancement);
        }

    }

    private void func_192748_e() {
        Iterator iterator = this.field_192756_d.func_191949_aK().func_192780_b().iterator();

        while (iterator.hasNext()) {
            Advancement advancement = (Advancement) iterator.next();

            if (advancement.func_192073_f().isEmpty()) {
                this.func_192750_a(advancement, "");
                advancement.func_192072_d().func_192113_a(this.field_192762_j);
            }
        }

    }

    private void func_192740_f() {
        if (this.field_192757_e.isFile()) {
            try {
                String s = Files.toString(this.field_192757_e, StandardCharsets.UTF_8);
                Map<ResourceLocation, AdvancementProgress> map = (Map) JsonUtils.func_193840_a(PlayerAdvancements.field_192754_b, s, PlayerAdvancements.field_192755_c.getType()); // CraftBukkit

                if (map == null) {
                    throw new JsonParseException("Found null for advancements");
                }

                Stream stream = map.entrySet().stream().sorted(Comparator.comparing(Entry::getValue));
                Iterator iterator = ((List) stream.collect(Collectors.toList())).iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    Advancement advancement = this.field_192756_d.func_191949_aK().func_192778_a((ResourceLocation) entry.getKey());

                    if (advancement == null) {
                        // CraftBukkit start
                        if (((ResourceLocation) entry.getKey()).func_110624_b().equals("minecraft")) {
                            PlayerAdvancements.field_192753_a.warn("Ignored advancement \'" + entry.getKey() + "\' in progress file " + this.field_192757_e + " - it doesn\'t exist anymore?");
                        }
                        // CraftBukkit end
                    } else {
                        this.func_192743_a(advancement, (AdvancementProgress) entry.getValue());
                    }
                }
            } catch (JsonParseException jsonparseexception) {
                PlayerAdvancements.field_192753_a.error("Couldn\'t parse player advancements in " + this.field_192757_e, jsonparseexception);
            } catch (IOException ioexception) {
                PlayerAdvancements.field_192753_a.error("Couldn\'t access player advancements in " + this.field_192757_e, ioexception);
            }
        }

        this.func_192748_e();
        this.func_192752_d();
        this.func_192751_c();
    }

    public void func_192749_b() {
        if (org.spigotmc.SpigotConfig.disableAdvancementSaving) return;
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = this.field_192758_f.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            AdvancementProgress advancementprogress = (AdvancementProgress) entry.getValue();

            if (advancementprogress.func_192108_b()) {
                hashmap.put(((Advancement) entry.getKey()).func_192067_g(), advancementprogress);
            }
        }

        if (this.field_192757_e.getParentFile() != null) {
            this.field_192757_e.getParentFile().mkdirs();
        }

        try {
            Files.write(PlayerAdvancements.field_192754_b.toJson(hashmap), this.field_192757_e, StandardCharsets.UTF_8);
        } catch (IOException ioexception) {
            PlayerAdvancements.field_192753_a.error("Couldn\'t save player advancements to " + this.field_192757_e, ioexception);
        }

    }

    public boolean func_192750_a(Advancement advancement, String s) {
        boolean flag = false;
        AdvancementProgress advancementprogress = this.func_192747_a(advancement);
        boolean flag1 = advancementprogress.func_192105_a();

        if (advancementprogress.func_192109_a(s)) {
            // Paper start
            if (!new com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent(this.field_192762_j.getBukkitEntity(), advancement.bukkit, s).callEvent()) {
                advancementprogress.func_192101_b(s);
                return false;
            }
            // Paper end
            this.func_193765_c(advancement);
            this.field_192761_i.add(advancement);
            flag = true;
            if (!flag1 && advancementprogress.func_192105_a()) {
                this.field_192762_j.field_70170_p.getServer().getPluginManager().callEvent(new org.bukkit.event.player.PlayerAdvancementDoneEvent(this.field_192762_j.getBukkitEntity(), advancement.bukkit)); // CraftBukkit
                advancement.func_192072_d().func_192113_a(this.field_192762_j);
                if (advancement.func_192068_c() != null && advancement.func_192068_c().func_193220_i() && this.field_192762_j.field_70170_p.func_82736_K().func_82766_b("announceAdvancements")) {
                    this.field_192756_d.func_184103_al().func_148539_a(new TextComponentTranslation("chat.type.advancement." + advancement.func_192068_c().func_192291_d().func_192307_a(), new Object[] { this.field_192762_j.func_145748_c_(), advancement.func_193123_j()}));
                }
            }
        }

        if (advancementprogress.func_192105_a()) {
            this.func_192742_b(advancement);
        }

        return flag;
    }

    public boolean func_192744_b(Advancement advancement, String s) {
        boolean flag = false;
        AdvancementProgress advancementprogress = this.func_192747_a(advancement);

        if (advancementprogress.func_192101_b(s)) {
            this.func_193764_b(advancement);
            this.field_192761_i.add(advancement);
            flag = true;
        }

        if (!advancementprogress.func_192108_b()) {
            this.func_192742_b(advancement);
        }

        return flag;
    }

    private void func_193764_b(Advancement advancement) {
        AdvancementProgress advancementprogress = this.func_192747_a(advancement);

        if (!advancementprogress.func_192105_a()) {
            Iterator iterator = advancement.func_192073_f().entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                CriterionProgress criterionprogress = advancementprogress.func_192106_c((String) entry.getKey());

                if (criterionprogress != null && !criterionprogress.func_192151_a()) {
                    ICriterionInstance criterioninstance = ((Criterion) entry.getValue()).func_192143_a();

                    if (criterioninstance != null) {
                        ICriterionTrigger criteriontrigger = CriteriaTriggers.func_192119_a(criterioninstance.func_192244_a());

                        if (criteriontrigger != null) {
                            criteriontrigger.a(this, new CriterionTrigger.a(criterioninstance, advancement, (String) entry.getKey()));
                        }
                    }
                }
            }

        }
    }

    private void func_193765_c(Advancement advancement) {
        AdvancementProgress advancementprogress = this.func_192747_a(advancement);
        Iterator iterator = advancement.func_192073_f().entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            CriterionProgress criterionprogress = advancementprogress.func_192106_c((String) entry.getKey());

            if (criterionprogress != null && (criterionprogress.func_192151_a() || advancementprogress.func_192105_a())) {
                ICriterionInstance criterioninstance = ((Criterion) entry.getValue()).func_192143_a();

                if (criterioninstance != null) {
                    ICriterionTrigger criteriontrigger = CriteriaTriggers.func_192119_a(criterioninstance.func_192244_a());

                    if (criteriontrigger != null) {
                        criteriontrigger.b(this, new CriterionTrigger.a(criterioninstance, advancement, (String) entry.getKey()));
                    }
                }
            }
        }

    }

    public void func_192741_b(EntityPlayerMP entityplayer) {
        if (!this.field_192760_h.isEmpty() || !this.field_192761_i.isEmpty()) {
            HashMap hashmap = Maps.newHashMap();
            LinkedHashSet linkedhashset = Sets.newLinkedHashSet();
            LinkedHashSet linkedhashset1 = Sets.newLinkedHashSet();
            Iterator iterator = this.field_192761_i.iterator();

            Advancement advancement;

            while (iterator.hasNext()) {
                advancement = (Advancement) iterator.next();
                if (this.field_192759_g.contains(advancement)) {
                    hashmap.put(advancement.func_192067_g(), this.field_192758_f.get(advancement));
                }
            }

            iterator = this.field_192760_h.iterator();

            while (iterator.hasNext()) {
                advancement = (Advancement) iterator.next();
                if (this.field_192759_g.contains(advancement)) {
                    linkedhashset.add(advancement);
                } else {
                    linkedhashset1.add(advancement.func_192067_g());
                }
            }

            if (!hashmap.isEmpty() || !linkedhashset.isEmpty() || !linkedhashset1.isEmpty()) {
                entityplayer.field_71135_a.func_147359_a(new SPacketAdvancementInfo(this.field_192763_k, linkedhashset, linkedhashset1, hashmap));
                this.field_192760_h.clear();
                this.field_192761_i.clear();
            }
        }

        this.field_192763_k = false;
    }

    public void func_194220_a(@Nullable Advancement advancement) {
        Advancement advancement1 = this.field_194221_k;

        if (advancement != null && advancement.func_192070_b() == null && advancement.func_192068_c() != null) {
            this.field_194221_k = advancement;
        } else {
            this.field_194221_k = null;
        }

        if (advancement1 != this.field_194221_k) {
            this.field_192762_j.field_71135_a.func_147359_a(new SPacketSelectAdvancementsTab(this.field_194221_k == null ? null : this.field_194221_k.func_192067_g()));
        }

    }

    public AdvancementProgress func_192747_a(Advancement advancement) {
        AdvancementProgress advancementprogress = (AdvancementProgress) this.field_192758_f.get(advancement);

        if (advancementprogress == null) {
            advancementprogress = new AdvancementProgress();
            this.func_192743_a(advancement, advancementprogress);
        }

        return advancementprogress;
    }

    private void func_192743_a(Advancement advancement, AdvancementProgress advancementprogress) {
        advancementprogress.func_192099_a(advancement.func_192073_f(), advancement.func_192074_h());
        this.field_192758_f.put(advancement, advancementprogress);
    }

    private void func_192742_b(Advancement advancement) {
        boolean flag = this.func_192738_c(advancement);
        boolean flag1 = this.field_192759_g.contains(advancement);

        if (flag && !flag1) {
            this.field_192759_g.add(advancement);
            this.field_192760_h.add(advancement);
            if (this.field_192758_f.containsKey(advancement)) {
                this.field_192761_i.add(advancement);
            }
        } else if (!flag && flag1) {
            this.field_192759_g.remove(advancement);
            this.field_192760_h.add(advancement);
        }

        if (flag != flag1 && advancement.func_192070_b() != null) {
            this.func_192742_b(advancement.func_192070_b());
        }

        Iterator iterator = advancement.func_192069_e().iterator();

        while (iterator.hasNext()) {
            Advancement advancement1 = (Advancement) iterator.next();

            this.func_192742_b(advancement1);
        }

    }

    private boolean func_192738_c(Advancement advancement) {
        for (int i = 0; advancement != null && i <= 2; ++i) {
            if (i == 0 && this.func_192746_d(advancement)) {
                return true;
            }

            if (advancement.func_192068_c() == null) {
                return false;
            }

            AdvancementProgress advancementprogress = this.func_192747_a(advancement);

            if (advancementprogress.func_192105_a()) {
                return true;
            }

            if (advancement.func_192068_c().func_193224_j()) {
                return false;
            }

            advancement = advancement.func_192070_b();
        }

        return false;
    }

    private boolean func_192746_d(Advancement advancement) {
        AdvancementProgress advancementprogress = this.func_192747_a(advancement);

        if (advancementprogress.func_192105_a()) {
            return true;
        } else {
            Iterator iterator = advancement.func_192069_e().iterator();

            Advancement advancement1;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                advancement1 = (Advancement) iterator.next();
            } while (!this.func_192746_d(advancement1));

            return true;
        }
    }
}
