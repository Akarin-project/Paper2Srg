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

    private static final ResourceLocation field_192196_a = new ResourceLocation("enter_block");
    private final Map<PlayerAdvancements, CriterionTriggerEnterBlock.a> field_192197_b = Maps.newHashMap();

    public EnterBlockTrigger() {}

    public ResourceLocation func_192163_a() {
        return EnterBlockTrigger.field_192196_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEnterBlock.b> criteriontrigger_a) {
        CriterionTriggerEnterBlock.a criteriontriggerenterblock_a = (CriterionTriggerEnterBlock.a) this.field_192197_b.get(advancementdataplayer);

        if (criteriontriggerenterblock_a == null) {
            criteriontriggerenterblock_a = new CriterionTriggerEnterBlock.a(advancementdataplayer);
            this.field_192197_b.put(advancementdataplayer, criteriontriggerenterblock_a);
        }

        criteriontriggerenterblock_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerEnterBlock.b> criteriontrigger_a) {
        CriterionTriggerEnterBlock.a criteriontriggerenterblock_a = (CriterionTriggerEnterBlock.a) this.field_192197_b.get(advancementdataplayer);

        if (criteriontriggerenterblock_a != null) {
            criteriontriggerenterblock_a.b(criteriontrigger_a);
            if (criteriontriggerenterblock_a.a()) {
                this.field_192197_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192197_b.remove(advancementdataplayer);
    }

    public CriterionTriggerEnterBlock.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        Block block = null;

        if (jsonobject.has("block")) {
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "block"));

            if (!Block.field_149771_c.func_148741_d(minecraftkey)) {
                throw new JsonSyntaxException("Unknown block type \'" + minecraftkey + "\'");
            }

            block = (Block) Block.field_149771_c.func_82594_a(minecraftkey);
        }

        HashMap hashmap = null;

        if (jsonobject.has("state")) {
            if (block == null) {
                throw new JsonSyntaxException("Can\'t define block state without a specific block type");
            }

            BlockStateContainer blockstatelist = block.func_176194_O();

            IProperty iblockstate;
            Optional optional;

            for (Iterator iterator = JsonUtils.func_152754_s(jsonobject, "state").entrySet().iterator(); iterator.hasNext(); hashmap.put(iblockstate, optional.get())) {
                Entry entry = (Entry) iterator.next();

                iblockstate = blockstatelist.func_185920_a((String) entry.getKey());
                if (iblockstate == null) {
                    throw new JsonSyntaxException("Unknown block state property \'" + (String) entry.getKey() + "\' for block \'" + Block.field_149771_c.func_177774_c(block) + "\'");
                }

                String s = JsonUtils.func_151206_a((JsonElement) entry.getValue(), (String) entry.getKey());

                optional = iblockstate.func_185929_b(s);
                if (!optional.isPresent()) {
                    throw new JsonSyntaxException("Invalid block state value \'" + s + "\' for property \'" + (String) entry.getKey() + "\' on block \'" + Block.field_149771_c.func_177774_c(block) + "\'");
                }

                if (hashmap == null) {
                    hashmap = Maps.newHashMap();
                }
            }
        }

        return new CriterionTriggerEnterBlock.b(block, hashmap);
    }

    public void func_192193_a(EntityPlayerMP entityplayer, IBlockState iblockdata) {
        CriterionTriggerEnterBlock.a criteriontriggerenterblock_a = (CriterionTriggerEnterBlock.a) this.field_192197_b.get(entityplayer.func_192039_O());

        if (criteriontriggerenterblock_a != null) {
            criteriontriggerenterblock_a.a(iblockdata);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
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
            super(EnterBlockTrigger.field_192196_a);
            this.a = block;
            this.b = map;
        }

        public boolean a(IBlockState iblockdata) {
            if (this.a != null && iblockdata.func_177230_c() != this.a) {
                return false;
            } else {
                if (this.b != null) {
                    Iterator iterator = this.b.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Entry entry = (Entry) iterator.next();

                        if (iblockdata.func_177229_b((IProperty) entry.getKey()) != entry.getValue()) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
    }
}
