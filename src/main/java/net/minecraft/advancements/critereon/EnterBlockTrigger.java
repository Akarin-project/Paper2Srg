package net.minecraft.advancements.critereon;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.CriterionTriggerEnterBlock.a;
import net.minecraft.server.CriterionTriggerEnterBlock.b;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EnterBlockTrigger implements ICriterionTrigger<CriterionTriggerEnterBlock.b> {

    private static final ResourceLocation ID = new ResourceLocation("enter_block");
    private final Map<PlayerAdvancements, CriterionTriggerEnterBlock.a> listeners = Maps.newHashMap();

    public EnterBlockTrigger() {}

    public ResourceLocation getId() {
        return EnterBlockTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEnterBlock.b> criteriontrigger_a) {
        CriterionTriggerEnterBlock.a criteriontriggerenterblock_a = (CriterionTriggerEnterBlock.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerenterblock_a == null) {
            criteriontriggerenterblock_a = new CriterionTriggerEnterBlock.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerenterblock_a);
        }

        criteriontriggerenterblock_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEnterBlock.b> criteriontrigger_a) {
        CriterionTriggerEnterBlock.a criteriontriggerenterblock_a = (CriterionTriggerEnterBlock.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerenterblock_a != null) {
            criteriontriggerenterblock_a.b(criteriontrigger_a);
            if (criteriontriggerenterblock_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerEnterBlock.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        Block block = null;

        if (jsonobject.has("block")) {
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "block"));

            if (!Block.REGISTRY.containsKey(minecraftkey)) {
                throw new JsonSyntaxException("Unknown block type \'" + minecraftkey + "\'");
            }

            block = (Block) Block.REGISTRY.getObject(minecraftkey);
        }

        HashMap hashmap = null;

        if (jsonobject.has("state")) {
            if (block == null) {
                throw new JsonSyntaxException("Can\'t define block state without a specific block type");
            }

            BlockStateContainer blockstatelist = block.getBlockState();

            IProperty iblockstate;
            Optional optional;

            for (Iterator iterator = JsonUtils.getJsonObject(jsonobject, "state").entrySet().iterator(); iterator.hasNext(); hashmap.put(iblockstate, optional.get())) {
                Entry entry = (Entry) iterator.next();

                iblockstate = blockstatelist.getProperty((String) entry.getKey());
                if (iblockstate == null) {
                    throw new JsonSyntaxException("Unknown block state property \'" + (String) entry.getKey() + "\' for block \'" + Block.REGISTRY.getNameForObject(block) + "\'");
                }

                String s = JsonUtils.getString((JsonElement) entry.getValue(), (String) entry.getKey());

                optional = iblockstate.parseValue(s);
                if (!optional.isPresent()) {
                    throw new JsonSyntaxException("Invalid block state value \'" + s + "\' for property \'" + (String) entry.getKey() + "\' on block \'" + Block.REGISTRY.getNameForObject(block) + "\'");
                }

                if (hashmap == null) {
                    hashmap = Maps.newHashMap();
                }
            }
        }

        return new CriterionTriggerEnterBlock.b(block, hashmap);
    }

    public void trigger(EntityPlayerMP entityplayer, IBlockState iblockdata) {
        CriterionTriggerEnterBlock.a criteriontriggerenterblock_a = (CriterionTriggerEnterBlock.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerenterblock_a != null) {
            criteriontriggerenterblock_a.a(iblockdata);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerEnterBlock.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerEnterBlock.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerEnterBlock.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(IBlockState iblockdata) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerEnterBlock.b) criteriontrigger_a.a()).a(iblockdata)) {
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(criteriontrigger_a);
                }
            }

            if (arraylist != null) {
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                    criteriontrigger_a.a(this.a);
                }
            }

        }
    }

    public static class b extends AbstractCriterionInstance {

        private final Block a;
        private final Map<IProperty<?>, Object> b;

        public b(@Nullable Block block, @Nullable Map<IProperty<?>, Object> map) {
            super(EnterBlockTrigger.ID);
            this.a = block;
            this.b = map;
        }

        public boolean a(IBlockState iblockdata) {
            if (this.a != null && iblockdata.getBlock() != this.a) {
                return false;
            } else {
                if (this.b != null) {
                    Iterator iterator = this.b.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Entry entry = (Entry) iterator.next();

                        if (iblockdata.getValue((IProperty) entry.getKey()) != entry.getValue()) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
    }
}
