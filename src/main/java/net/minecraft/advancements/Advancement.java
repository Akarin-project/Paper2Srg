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

    private final Advancement parent;
    private final DisplayInfo display;
    private final AdvancementRewards rewards;
    private final ResourceLocation id;
    private final Map<String, Criterion> criteria;
    private final String[][] requirements;
    private final Set<Advancement> children = Sets.newLinkedHashSet();
    private final ITextComponent displayText;
    public final org.bukkit.advancement.Advancement bukkit = new org.bukkit.craftbukkit.advancement.CraftAdvancement(this); // CraftBukkit

    public Advancement(ResourceLocation minecraftkey, @Nullable Advancement advancement, @Nullable DisplayInfo advancementdisplay, AdvancementRewards advancementrewards, Map<String, Criterion> map, String[][] astring) {
        this.id = minecraftkey;
        this.display = advancementdisplay;
        this.criteria = ImmutableMap.copyOf(map);
        this.parent = advancement;
        this.rewards = advancementrewards;
        this.requirements = astring;
        if (advancement != null) {
            advancement.addChild(this);
        }

        if (advancementdisplay == null) {
            this.displayText = new TextComponentString(minecraftkey.toString());
        } else {
            this.displayText = new TextComponentString("[");
            this.displayText.getStyle().setColor(advancementdisplay.getFrame().getFormat());
            ITextComponent ichatbasecomponent = advancementdisplay.getTitle().createCopy();
            TextComponentString chatcomponenttext = new TextComponentString("");
            ITextComponent ichatbasecomponent1 = ichatbasecomponent.createCopy();

            ichatbasecomponent1.getStyle().setColor(advancementdisplay.getFrame().getFormat());
            chatcomponenttext.appendSibling(ichatbasecomponent1);
            chatcomponenttext.appendText("\n");
            chatcomponenttext.appendSibling(advancementdisplay.getDescription());
            ichatbasecomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, chatcomponenttext));
            this.displayText.appendSibling(ichatbasecomponent);
            this.displayText.appendText("]");
        }

    }

    public Advancement.Builder copy() {
        return new Advancement.Builder(this.parent == null ? null : this.parent.getId(), this.display, this.rewards, this.criteria, this.requirements);
    }

    @Nullable
    public Advancement getParent() {
        return this.parent;
    }

    @Nullable
    public DisplayInfo getDisplay() {
        return this.display;
    }

    public AdvancementRewards getRewards() {
        return this.rewards;
    }

    public String toString() {
        return "SimpleAdvancement{id=" + this.getId() + ", parent=" + (this.parent == null ? "null" : this.parent.getId()) + ", display=" + this.display + ", rewards=" + this.rewards + ", criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + '}';
    }

    public Iterable<Advancement> getChildren() {
        return this.children;
    }

    public Map<String, Criterion> getCriteria() {
        return this.criteria;
    }

    public void addChild(Advancement advancement) {
        this.children.add(advancement);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Advancement)) {
            return false;
        } else {
            Advancement advancement = (Advancement) object;

            return this.id.equals(advancement.id);
        }
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public String[][] getRequirements() {
        return this.requirements;
    }

    public ITextComponent getDisplayText() {
        return this.displayText;
    }

    public static class Builder {

        private final ResourceLocation parentId;
        private Advancement parent;
        private final DisplayInfo display;
        private final AdvancementRewards rewards;
        private final Map<String, Criterion> criteria;
        private final String[][] requirements;

        Builder(@Nullable ResourceLocation minecraftkey, @Nullable DisplayInfo advancementdisplay, AdvancementRewards advancementrewards, Map<String, Criterion> map, String[][] astring) {
            this.parentId = minecraftkey;
            this.display = advancementdisplay;
            this.rewards = advancementrewards;
            this.criteria = map;
            this.requirements = astring;
        }

        public boolean resolveParent(Function<ResourceLocation, Advancement> function) {
            if (this.parentId == null) {
                return true;
            } else {
                this.parent = (Advancement) function.apply(this.parentId);
                return this.parent != null;
            }
        }

        public Advancement build(ResourceLocation minecraftkey) {
            return new Advancement(minecraftkey, this.parent, this.display, this.rewards, this.criteria, this.requirements);
        }

        public void writeTo(PacketBuffer packetdataserializer) {
            if (this.parentId == null) {
                packetdataserializer.writeBoolean(false);
            } else {
                packetdataserializer.writeBoolean(true);
                packetdataserializer.writeResourceLocation(this.parentId);
            }

            if (this.display == null) {
                packetdataserializer.writeBoolean(false);
            } else {
                packetdataserializer.writeBoolean(true);
                this.display.write(packetdataserializer);
            }

            Criterion.serializeToNetwork(this.criteria, packetdataserializer);
            packetdataserializer.writeVarInt(this.requirements.length);
            String[][] astring = this.requirements;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String[] astring1 = astring[j];

                packetdataserializer.writeVarInt(astring1.length);
                String[] astring2 = astring1;
                int k = astring1.length;

                for (int l = 0; l < k; ++l) {
                    String s = astring2[l];

                    packetdataserializer.writeString(s);
                }
            }

        }

        public String toString() {
            return "Task Advancement{parentId=" + this.parentId + ", display=" + this.display + ", rewards=" + this.rewards + ", criteria=" + this.criteria + ", requirements=" + Arrays.deepToString(this.requirements) + '}';
        }

        public static Advancement.Builder deserialize(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            ResourceLocation minecraftkey = jsonobject.has("parent") ? new ResourceLocation(JsonUtils.getString(jsonobject, "parent")) : null;
            DisplayInfo advancementdisplay = jsonobject.has("display") ? DisplayInfo.deserialize(JsonUtils.getJsonObject(jsonobject, "display"), jsondeserializationcontext) : null;
            AdvancementRewards advancementrewards = (AdvancementRewards) JsonUtils.deserializeClass(jsonobject, "rewards", AdvancementRewards.EMPTY, jsondeserializationcontext, AdvancementRewards.class);
            Map map = Criterion.criteriaFromJson(JsonUtils.getJsonObject(jsonobject, "criteria"), jsondeserializationcontext);

            if (map.isEmpty()) {
                throw new JsonSyntaxException("Advancement criteria cannot be empty");
            } else {
                JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "requirements", new JsonArray());
                String[][] astring = new String[jsonarray.size()][];

                int i;
                int j;

                for (i = 0; i < jsonarray.size(); ++i) {
                    JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonarray.get(i), "requirements[" + i + "]");

                    astring[i] = new String[jsonarray1.size()];

                    for (j = 0; j < jsonarray1.size(); ++j) {
                        astring[i][j] = JsonUtils.getString(jsonarray1.get(j), "requirements[" + i + "][" + j + "]");
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

        public static Advancement.Builder readFrom(PacketBuffer packetdataserializer) {
            ResourceLocation minecraftkey = packetdataserializer.readBoolean() ? packetdataserializer.readResourceLocation() : null;
            DisplayInfo advancementdisplay = packetdataserializer.readBoolean() ? DisplayInfo.read(packetdataserializer) : null;
            Map map = Criterion.criteriaFromNetwork(packetdataserializer);
            String[][] astring = new String[packetdataserializer.readVarInt()][];

            for (int i = 0; i < astring.length; ++i) {
                astring[i] = new String[packetdataserializer.readVarInt()];

                for (int j = 0; j < astring[i].length; ++j) {
                    astring[i][j] = packetdataserializer.readString(32767);
                }
            }

            return new Advancement.Builder(minecraftkey, advancementdisplay, AdvancementRewards.EMPTY, map, astring);
        }
    }
}
