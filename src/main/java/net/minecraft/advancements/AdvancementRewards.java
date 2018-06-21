package net.minecraft.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public class AdvancementRewards {

    public static final AdvancementRewards field_192114_a = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.a.a);
    private final int field_192115_b;
    private final ResourceLocation[] field_192116_c;
    private final ResourceLocation[] field_192117_d;
    private final FunctionObject.a field_193129_e;

    public AdvancementRewards(int i, ResourceLocation[] aminecraftkey, ResourceLocation[] aminecraftkey1, FunctionObject.a customfunction_a) {
        this.field_192115_b = i;
        this.field_192116_c = aminecraftkey;
        this.field_192117_d = aminecraftkey1;
        this.field_193129_e = customfunction_a;
    }

    public void func_192113_a(final EntityPlayerMP entityplayer) {
        entityplayer.func_71023_q(this.field_192115_b);
        LootContext loottableinfo = (new LootContext.Builder(entityplayer.func_71121_q())).func_186472_a((Entity) entityplayer).func_186471_a();
        
        boolean flag = false;
        ResourceLocation[] aminecraftkey = this.field_192116_c;
        int i = aminecraftkey.length;

        for (int j = 0; j < i; ++j) {
            ResourceLocation minecraftkey = aminecraftkey[j];
            Iterator iterator = entityplayer.field_70170_p.func_184146_ak().func_186521_a(minecraftkey).func_186462_a(entityplayer.func_70681_au(), loottableinfo).iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();

                if (entityplayer.func_191521_c(itemstack)) {
                    entityplayer.field_70170_p.func_184148_a((EntityPlayer) null, entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v, SoundEvents.field_187638_cR, SoundCategory.PLAYERS, 0.2F, ((entityplayer.func_70681_au().nextFloat() - entityplayer.func_70681_au().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    flag = true;
                } else {
                    EntityItem entityitem = entityplayer.func_71019_a(itemstack, false);

                    if (entityitem != null) {
                        entityitem.func_174868_q();
                        entityitem.func_145797_a(entityplayer.func_70005_c_());
                    }
                }
            }
        }

        if (flag) {
            entityplayer.field_71069_bz.func_75142_b();
        }

        if (this.field_192117_d.length > 0) {
            entityplayer.func_193102_a(this.field_192117_d);
        }

        final MinecraftServer minecraftserver = entityplayer.field_71133_b;
        FunctionObject customfunction = this.field_193129_e.a(minecraftserver.func_193030_aL());

        if (customfunction != null) {
            // CraftBukkit start
            ICommandSender icommandlistener = new AdvancementCommandListener(entityplayer, minecraftserver);

            minecraftserver.func_193030_aL().func_194019_a(customfunction, icommandlistener);
        }

    }

            public static class AdvancementCommandListener implements ICommandSender {

                private final EntityPlayerMP entityplayer;
                private final MinecraftServer minecraftserver;

                public AdvancementCommandListener(EntityPlayerMP entityplayer, MinecraftServer minecraftserver) {
                    this.entityplayer = entityplayer;
                    this.minecraftserver = minecraftserver;
                }

                @Override
                public String func_70005_c_() {
                    return entityplayer.func_70005_c_();
                }

                @Override
                public ITextComponent func_145748_c_() {
                    return entityplayer.func_145748_c_();
                }

                @Override
                public void func_145747_a(ITextComponent ichatbasecomponent) {}

                @Override
                public boolean func_70003_b(int i, String s) {
                    return i <= 2;
                }

                @Override
                public BlockPos func_180425_c() {
                    return entityplayer.func_180425_c();
                }

                @Override
                public Vec3d func_174791_d() {
                    return entityplayer.func_174791_d();
                }

                @Override
                public World func_130014_f_() {
                    return entityplayer.field_70170_p;
                }

                @Override
                public Entity func_174793_f() {
                    return entityplayer;
                }

                @Override
                public boolean func_174792_t_() {
                    return minecraftserver.field_71305_c[0].func_82736_K().func_82766_b("commandBlockOutput");
                }

                @Override
                public void func_174794_a(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
                    entityplayer.func_174794_a(commandobjectiveexecutor_enumcommandresult, i);
                }

                @Override
                public MinecraftServer func_184102_h() {
                    return entityplayer.func_184102_h();
                }
            }
    // CraftBukkit end

    @Override
    public String toString() {
        return "AdvancementRewards{experience=" + this.field_192115_b + ", loot=" + Arrays.toString(this.field_192116_c) + ", recipes=" + Arrays.toString(this.field_192117_d) + ", function=" + this.field_193129_e + '}';
    }

    public static class a implements JsonDeserializer<AdvancementRewards> {

        public a() {}

        public AdvancementRewards a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "rewards");
            int i = JsonUtils.func_151208_a(jsonobject, "experience", 0);
            JsonArray jsonarray = JsonUtils.func_151213_a(jsonobject, "loot", new JsonArray());
            ResourceLocation[] aminecraftkey = new ResourceLocation[jsonarray.size()];

            for (int j = 0; j < aminecraftkey.length; ++j) {
                aminecraftkey[j] = new ResourceLocation(JsonUtils.func_151206_a(jsonarray.get(j), "loot[" + j + "]"));
            }

            JsonArray jsonarray1 = JsonUtils.func_151213_a(jsonobject, "recipes", new JsonArray());
            ResourceLocation[] aminecraftkey1 = new ResourceLocation[jsonarray1.size()];

            for (int k = 0; k < aminecraftkey1.length; ++k) {
                aminecraftkey1[k] = new ResourceLocation(JsonUtils.func_151206_a(jsonarray1.get(k), "recipes[" + k + "]"));
                IRecipe irecipe = CraftingManager.func_193373_a(aminecraftkey1[k]);

                if (irecipe == null) {
                    throw new JsonSyntaxException("Unknown recipe \'" + aminecraftkey1[k] + "\'");
                }
            }

            FunctionObject.a customfunction_a;

            if (jsonobject.has("function")) {
                customfunction_a = new FunctionObject.a(new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "function")));
            } else {
                customfunction_a = FunctionObject.a.a;
            }

            return new AdvancementRewards(i, aminecraftkey, aminecraftkey1, customfunction_a);
        }

        @Override
        public AdvancementRewards deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException { // CraftBukkit - decompile error
            return this.a(jsonelement, type, jsondeserializationcontext);
        }
    }
}
