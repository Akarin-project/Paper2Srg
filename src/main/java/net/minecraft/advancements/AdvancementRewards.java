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

    public static final AdvancementRewards EMPTY = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.a.a);
    private final int experience;
    private final ResourceLocation[] loot;
    private final ResourceLocation[] recipes;
    private final FunctionObject.a function;

    public AdvancementRewards(int i, ResourceLocation[] aminecraftkey, ResourceLocation[] aminecraftkey1, FunctionObject.a customfunction_a) {
        this.experience = i;
        this.loot = aminecraftkey;
        this.recipes = aminecraftkey1;
        this.function = customfunction_a;
    }

    public void apply(final EntityPlayerMP entityplayer) {
        entityplayer.addExperience(this.experience);
        LootContext loottableinfo = (new LootContext.a(entityplayer.getServerWorld())).a((Entity) entityplayer).a();
        boolean flag = false;
        ResourceLocation[] aminecraftkey = this.loot;
        int i = aminecraftkey.length;

        for (int j = 0; j < i; ++j) {
            ResourceLocation minecraftkey = aminecraftkey[j];
            Iterator iterator = entityplayer.world.getLootTableManager().getLootTableFromLocation(minecraftkey).generateLootForPools(entityplayer.getRNG(), loottableinfo).iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();

                if (entityplayer.addItemStackToInventory(itemstack)) {
                    entityplayer.world.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    flag = true;
                } else {
                    EntityItem entityitem = entityplayer.dropItem(itemstack, false);

                    if (entityitem != null) {
                        entityitem.setNoPickupDelay();
                        entityitem.setOwner(entityplayer.getName());
                    }
                }
            }
        }

        if (flag) {
            entityplayer.inventoryContainer.detectAndSendChanges();
        }

        if (this.recipes.length > 0) {
            entityplayer.unlockRecipes(this.recipes);
        }

        final MinecraftServer minecraftserver = entityplayer.mcServer;
        FunctionObject customfunction = this.function.a(minecraftserver.getFunctionManager());

        if (customfunction != null) {
            // CraftBukkit start
            ICommandSender icommandlistener = new AdvancementCommandListener(entityplayer, minecraftserver);

            minecraftserver.getFunctionManager().execute(customfunction, icommandlistener);
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
                public String getName() {
                    return entityplayer.getName();
                }

                @Override
                public ITextComponent getDisplayName() {
                    return entityplayer.getDisplayName();
                }

                @Override
                public void sendMessage(ITextComponent ichatbasecomponent) {}

                @Override
                public boolean canUseCommand(int i, String s) {
                    return i <= 2;
                }

                @Override
                public BlockPos getPosition() {
                    return entityplayer.getPosition();
                }

                @Override
                public Vec3d getPositionVector() {
                    return entityplayer.getPositionVector();
                }

                @Override
                public World getEntityWorld() {
                    return entityplayer.world;
                }

                @Override
                public Entity getCommandSenderEntity() {
                    return entityplayer;
                }

                @Override
                public boolean sendCommandFeedback() {
                    return minecraftserver.worlds[0].getGameRules().getBoolean("commandBlockOutput");
                }

                @Override
                public void setCommandStat(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
                    entityplayer.setCommandStat(commandobjectiveexecutor_enumcommandresult, i);
                }

                @Override
                public MinecraftServer getServer() {
                    return entityplayer.getServer();
                }
            }
    // CraftBukkit end

    @Override
    public String toString() {
        return "AdvancementRewards{experience=" + this.experience + ", loot=" + Arrays.toString(this.loot) + ", recipes=" + Arrays.toString(this.recipes) + ", function=" + this.function + '}';
    }

    public static class a implements JsonDeserializer<AdvancementRewards> {

        public a() {}

        public AdvancementRewards a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "rewards");
            int i = JsonUtils.getInt(jsonobject, "experience", 0);
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "loot", new JsonArray());
            ResourceLocation[] aminecraftkey = new ResourceLocation[jsonarray.size()];

            for (int j = 0; j < aminecraftkey.length; ++j) {
                aminecraftkey[j] = new ResourceLocation(JsonUtils.getString(jsonarray.get(j), "loot[" + j + "]"));
            }

            JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "recipes", new JsonArray());
            ResourceLocation[] aminecraftkey1 = new ResourceLocation[jsonarray1.size()];

            for (int k = 0; k < aminecraftkey1.length; ++k) {
                aminecraftkey1[k] = new ResourceLocation(JsonUtils.getString(jsonarray1.get(k), "recipes[" + k + "]"));
                IRecipe irecipe = CraftingManager.getRecipe(aminecraftkey1[k]);

                if (irecipe == null) {
                    throw new JsonSyntaxException("Unknown recipe \'" + aminecraftkey1[k] + "\'");
                }
            }

            FunctionObject.a customfunction_a;

            if (jsonobject.has("function")) {
                customfunction_a = new FunctionObject.a(new ResourceLocation(JsonUtils.getString(jsonobject, "function")));
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
