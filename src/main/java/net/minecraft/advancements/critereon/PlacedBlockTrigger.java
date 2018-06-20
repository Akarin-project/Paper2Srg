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
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class PlacedBlockTrigger implements ICriterionTrigger<PlacedBlockTrigger.b> {

    private static final ResourceLocation field_193174_a = new ResourceLocation("placed_block");
    private final Map<PlayerAdvancements, PlacedBlockTrigger.a> field_193175_b = Maps.newHashMap();

    public PlacedBlockTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return PlacedBlockTrigger.field_193174_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<PlacedBlockTrigger.b> criteriontrigger_a) {
        PlacedBlockTrigger.a criteriontriggerplacedblock_a = this.field_193175_b.get(advancementdataplayer);

        if (criteriontriggerplacedblock_a == null) {
            criteriontriggerplacedblock_a = new PlacedBlockTrigger.a(advancementdataplayer);
            this.field_193175_b.put(advancementdataplayer, criteriontriggerplacedblock_a);
        }

        criteriontriggerplacedblock_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<PlacedBlockTrigger.b> criteriontrigger_a) {
        PlacedBlockTrigger.a criteriontriggerplacedblock_a = this.field_193175_b.get(advancementdataplayer);

        if (criteriontriggerplacedblock_a != null) {
            criteriontriggerplacedblock_a.b(criteriontrigger_a);
            if (criteriontriggerplacedblock_a.a()) {
                this.field_193175_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_193175_b.remove(advancementdataplayer);
    }

    public PlacedBlockTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        Block block = null;

        if (jsonobject.has("block")) {
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "block"));

            if (!Block.field_149771_c.func_148741_d(minecraftkey)) {
                throw new JsonSyntaxException("Unknown block type \'" + minecraftkey + "\'");
            }

            block = Block.field_149771_c.func_82594_a(minecraftkey);
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

        LocationPredicate criterionconditionlocation = LocationPredicate.func_193454_a(jsonobject.get("location"));
        ItemPredicate criterionconditionitem = ItemPredicate.func_192492_a(jsonobject.get("item"));

        return new PlacedBlockTrigger.b(block, hashmap, criterionconditionlocation, criterionconditionitem);
    }

    public void func_193173_a(EntityPlayerMP entityplayer, BlockPos blockposition, ItemStack itemstack) {
        IBlockState iblockdata = entityplayer.field_70170_p.func_180495_p(blockposition);
        PlacedBlockTrigger.a criteriontriggerplacedblock_a = this.field_193175_b.get(entityplayer.func_192039_O());

        if (criteriontriggerplacedblock_a != null) {
            criteriontriggerplacedblock_a.a(iblockdata, blockposition, entityplayer.func_71121_q(), itemstack);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<PlacedBlockTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<PlacedBlockTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<PlacedBlockTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(IBlockState iblockdata, BlockPos blockposition, WorldServer worldserver, ItemStack itemstack) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((PlacedBlockTrigger.b) criteriontrigger_a.a()).a(iblockdata, blockposition, worldserver, itemstack)) {
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(criteriontrigger_a);
                }
            }

            if (arraylist != null) {
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                    criteriontrigger_a.a(this.a);
                }
            }

        }
    }

    public static class b extends AbstractCriterionInstance {

        private final Block a;
        private final Map<IProperty<?>, Object> b;
        private final LocationPredicate c;
        private final ItemPredicate d;

        public b(@Nullable Block block, @Nullable Map<IProperty<?>, Object> map, LocationPredicate criterionconditionlocation, ItemPredicate criterionconditionitem) {
            super(PlacedBlockTrigger.field_193174_a);
            this.a = block;
            this.b = map;
            this.c = criterionconditionlocation;
            this.d = criterionconditionitem;
        }

        public boolean a(IBlockState iblockdata, BlockPos blockposition, WorldServer worldserver, ItemStack itemstack) {
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

                return !this.c.func_193453_a(worldserver, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()) ? false : this.d.func_192493_a(itemstack);
            }
        }
    }
}
