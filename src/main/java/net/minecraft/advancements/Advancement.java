package net.minecraft.advancements;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;

public class Advancement {

    private final Advancement field_192076_a;
    private final DisplayInfo field_192077_b;
    private final AdvancementRewards field_192078_c;
    private final ResourceLocation field_192079_d;
    private final Map<String, Criterion> field_192080_e;
    private final String[][] field_192081_f;
    private final Set<Advancement> field_192082_g = Sets.newLinkedHashSet();
    private final ITextComponent field_193125_h;
    public final org.bukkit.advancement.Advancement bukkit = new org.bukkit.craftbukkit.advancement.CraftAdvancement(this); // CraftBukkit

    public Advancement(ResourceLocation minecraftkey, @Nullable Advancement advancement, @Nullable DisplayInfo advancementdisplay, AdvancementRewards advancementrewards, Map<String, Criterion> map, String[][] astring) {
        this.field_192079_d = minecraftkey;
        this.field_192077_b = advancementdisplay;
        this.field_192080_e = ImmutableMap.copyOf(map);
        this.field_192076_a = advancement;
        this.field_192078_c = advancementrewards;
        this.field_192081_f = astring;
        if (advancement != null) {
            advancement.func_192071_a(this);
        }

        if (advancementdisplay == null) {
            this.field_193125_h = new TextComponentString(minecraftkey.toString());
        } else {
            this.field_193125_h = new TextComponentString("[");
            this.field_193125_h.func_150256_b().func_150238_a(advancementdisplay.func_192291_d().func_193229_c());
            ITextComponent ichatbasecomponent = advancementdisplay.func_192297_a().func_150259_f();
            TextComponentString chatcomponenttext = new TextComponentString("");
            ITextComponent ichatbasecomponent1 = ichatbasecomponent.func_150259_f();

            ichatbasecomponent1.func_150256_b().func_150238_a(advancementdisplay.func_192291_d().func_193229_c());
            chatcomponenttext.func_150257_a(ichatbasecomponent1);
            chatcomponenttext.func_150258_a("\n");
            chatcomponenttext.func_150257_a(advancementdisplay.func_193222_b());
            ichatbasecomponent.func_150256_b().func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, chatcomponenttext));
            this.field_193125_h.func_150257_a(ichatbasecomponent);
            this.field_193125_h.func_150258_a("]");
        }

    }

    public Advancement.Builder func_192075_a() {
        return new Advancement.Builder(this.field_192076_a == null ? null : this.field_192076_a.func_192067_g(), this.field_192077_b, this.field_192078_c, this.field_192080_e, this.field_192081_f);
    }

    @Nullable
    public Advancement func_192070_b() {
        return this.field_192076_a;
    }

    @Nullable
    public DisplayInfo func_192068_c() {
        return this.field_192077_b;
    }

    public AdvancementRewards func_192072_d() {
        return this.field_192078_c;
    }

    public String toString() {
        return "SimpleAdvancement{id=" + this.func_192067_g() + ", parent=" + (this.field_192076_a == null ? "null" : this.field_192076_a.func_192067_g()) + ", display=" + this.field_192077_b + ", rewards=" + this.field_192078_c + ", criteria=" + this.field_192080_e + ", requirements=" + Arrays.deepToString(this.field_192081_f) + '}';
    }

    public Iterable<Advancement> func_192069_e() {
        return this.field_192082_g;
    }

    public Map<String, Criterion> func_192073_f() {
        return this.field_192080_e;
    }

    public void func_192071_a(Advancement advancement) {
        this.field_192082_g.add(advancement);
    }

    public ResourceLocation func_192067_g() {
        return this.field_192079_d;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Advancement)) {
            return false;
        } else {
            Advancement advancement = (Advancement) object;

            return this.field_192079_d.equals(advancement.field_192079_d);
        }
    }

    public int hashCode() {
        return this.field_192079_d.hashCode();
    }

    public String[][] func_192074_h() {
        return this.field_192081_f;
    }

    public ITextComponent func_193123_j() {
        return this.field_193125_h;
    }

    public static class Builder {

        private final ResourceLocation field_192061_a;
        private Advancement field_192062_b;
        private final DisplayInfo field_192063_c;
        private final AdvancementRewards field_192064_d;
        private final Map<String, Criterion> field_192065_e;
        private final String[][] field_192066_f;

        Builder(@Nullable ResourceLocation minecraftkey, @Nullable DisplayInfo advancementdisplay, AdvancementRewards advancementrewards, Map<String, Criterion> map, String[][] astring) {
            this.field_192061_a = minecraftkey;
            this.field_192063_c = advancementdisplay;
            this.field_192064_d = advancementrewards;
            this.field_192065_e = map;
            this.field_192066_f = astring;
        }

        public boolean func_192058_a(Function<ResourceLocation, Advancement> function) {
            if (this.field_192061_a == null) {
                return true;
            } else {
                this.field_192062_b = (Advancement) function.apply(this.field_192061_a);
                return this.field_192062_b != null;
            }
        }

        public Advancement func_192056_a(ResourceLocation minecraftkey) {
            return new Advancement(minecraftkey, this.field_192062_b, this.field_192063_c, this.field_192064_d, this.field_192065_e, this.field_192066_f);
        }

        public void func_192057_a(PacketBuffer packetdataserializer) {
            if (this.field_192061_a == null) {
                packetdataserializer.writeBoolean(false);
            } else {
                packetdataserializer.writeBoolean(true);
                packetdataserializer.func_192572_a(this.field_192061_a);
            }

            if (this.field_192063_c == null) {
                packetdataserializer.writeBoolean(false);
            } else {
                packetdataserializer.writeBoolean(true);
                this.field_192063_c.func_192290_a(packetdataserializer);
            }

            Criterion.func_192141_a(this.field_192065_e, packetdataserializer);
            packetdataserializer.func_150787_b(this.field_192066_f.length);
            String[][] astring = this.field_192066_f;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String[] astring1 = astring[j];

                packetdataserializer.func_150787_b(astring1.length);
                String[] astring2 = astring1;
                int k = astring1.length;

                for (int l = 0; l < k; ++l) {
                    String s = astring2[l];

                    packetdataserializer.func_180714_a(s);
                }
            }

        }

        public String toString() {
            return "Task Advancement{parentId=" + this.field_192061_a + ", display=" + this.field_192063_c + ", rewards=" + this.field_192064_d + ", criteria=" + this.field_192065_e + ", requirements=" + Arrays.deepToString(this.field_192066_f) + '}';
        }

        public static Advancement.Builder func_192059_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            ResourceLocation minecraftkey = jsonobject.has("parent") ? new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "parent")) : null;
            DisplayInfo advancementdisplay = jsonobject.has("display") ? DisplayInfo.func_192294_a(JsonUtils.func_152754_s(jsonobject, "display"), jsondeserializationcontext) : null;
            AdvancementRewards advancementrewards = (AdvancementRewards) JsonUtils.func_188177_a(jsonobject, "rewards", AdvancementRewards.field_192114_a, jsondeserializationcontext, AdvancementRewards.class);
            Map map = Criterion.func_192144_b(JsonUtils.func_152754_s(jsonobject, "criteria"), jsondeserializationcontext);

            if (map.isEmpty()) {
                throw new JsonSyntaxException("Advancement criteria cannot be empty");
            } else {
                JsonArray jsonarray = JsonUtils.func_151213_a(jsonobject, "requirements", new JsonArray());
                String[][] astring = new String[jsonarray.size()][];

                int i;
                int j;

                for (i = 0; i < jsonarray.size(); ++i) {
                    JsonArray jsonarray1 = JsonUtils.func_151207_m(jsonarray.get(i), "requirements[" + i + "]");

                    astring[i] = new String[jsonarray1.size()];

                    for (j = 0; j < jsonarray1.size(); ++j) {
                        astring[i][j] = JsonUtils.func_151206_a(jsonarray1.get(j), "requirements[" + i + "][" + j + "]");
                    }
                }

                if (astring.length == 0) {
                    astring = new String[map.size()][];
                    i = 0;

                    String s;

                    for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); astring[i++] = new String[] { s}) {
                        s = (String) iterator.next();
                    }
                }

                String[][] astring1 = astring;
                int k = astring.length;

                int l;

                for (j = 0; j < k; ++j) {
                    String[] astring2 = astring1[j];

                    if (astring2.length == 0 && map.isEmpty()) {
                        throw new JsonSyntaxException("Requirement entry cannot be empty");
                    }

                    String[] astring3 = astring2;

                    l = astring2.length;

                    for (int i1 = 0; i1 < l; ++i1) {
                        String s1 = astring3[i1];

                        if (!map.containsKey(s1)) {
                            throw new JsonSyntaxException("Unknown required criterion \'" + s1 + "\'");
                        }
                    }
                }

                Iterator iterator1 = map.keySet().iterator();

                while (iterator1.hasNext()) {
                    String s2 = (String) iterator1.next();
                    boolean flag = false;
                    String[][] astring4 = astring;
                    int j1 = astring.length;

                    l = 0;

                    while (true) {
                        if (l < j1) {
                            String[] astring5 = astring4[l];

                            if (!ArrayUtils.contains(astring5, s2)) {
                                ++l;
                                continue;
                            }

                            flag = true;
                        }

                        if (!flag) {
                            throw new JsonSyntaxException("Criterion \'" + s2 + "\' isn\'t a requirement for completion. This isn\'t supported behaviour, all criteria must be required.");
                        }
                        break;
                    }
                }

                return new Advancement.Builder(minecraftkey, advancementdisplay, advancementrewards, map, astring);
            }
        }

        public static Advancement.Builder func_192060_b(PacketBuffer packetdataserializer) {
            ResourceLocation minecraftkey = packetdataserializer.readBoolean() ? packetdataserializer.func_192575_l() : null;
            DisplayInfo advancementdisplay = packetdataserializer.readBoolean() ? DisplayInfo.func_192295_b(packetdataserializer) : null;
            Map map = Criterion.func_192142_c(packetdataserializer);
            String[][] astring = new String[packetdataserializer.func_150792_a()][];

            for (int i = 0; i < astring.length; ++i) {
                astring[i] = new String[packetdataserializer.func_150792_a()];

                for (int j = 0; j < astring[i].length; ++j) {
                    astring[i][j] = packetdataserializer.func_150789_c(32767);
                }
            }

            return new Advancement.Builder(minecraftkey, advancementdisplay, AdvancementRewards.field_192114_a, map, astring);
        }
    }
}
