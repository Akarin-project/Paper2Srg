package net.minecraft.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DisplayInfo {

    private final ITextComponent field_192300_a;
    private final ITextComponent field_193225_b;
    private final ItemStack field_192301_b;
    private final ResourceLocation field_192302_c;
    private final FrameType field_192303_d;
    private final boolean field_193226_f;
    private final boolean field_193227_g;
    private final boolean field_193228_h;
    private float field_192304_e;
    private float field_192305_f;

    public DisplayInfo(ItemStack itemstack, ITextComponent ichatbasecomponent, ITextComponent ichatbasecomponent1, @Nullable ResourceLocation minecraftkey, FrameType advancementframetype, boolean flag, boolean flag1, boolean flag2) {
        this.field_192300_a = ichatbasecomponent;
        this.field_193225_b = ichatbasecomponent1;
        this.field_192301_b = itemstack;
        this.field_192302_c = minecraftkey;
        this.field_192303_d = advancementframetype;
        this.field_193226_f = flag;
        this.field_193227_g = flag1;
        this.field_193228_h = flag2;
    }

    public void func_192292_a(float f, float f1) {
        this.field_192304_e = f;
        this.field_192305_f = f1;
    }

    public ITextComponent func_192297_a() {
        return this.field_192300_a;
    }

    public ITextComponent func_193222_b() {
        return this.field_193225_b;
    }

    public FrameType func_192291_d() {
        return this.field_192303_d;
    }

    public boolean func_193220_i() {
        return this.field_193227_g;
    }

    public boolean func_193224_j() {
        return this.field_193228_h;
    }

    public static DisplayInfo func_192294_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ITextComponent ichatbasecomponent = (ITextComponent) JsonUtils.func_188174_a(jsonobject, "title", jsondeserializationcontext, ITextComponent.class);
        ITextComponent ichatbasecomponent1 = (ITextComponent) JsonUtils.func_188174_a(jsonobject, "description", jsondeserializationcontext, ITextComponent.class);

        if (ichatbasecomponent != null && ichatbasecomponent1 != null) {
            ItemStack itemstack = func_193221_a(JsonUtils.func_152754_s(jsonobject, "icon"));
            ResourceLocation minecraftkey = jsonobject.has("background") ? new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "background")) : null;
            FrameType advancementframetype = jsonobject.has("frame") ? FrameType.func_192308_a(JsonUtils.func_151200_h(jsonobject, "frame")) : FrameType.TASK;
            boolean flag = JsonUtils.func_151209_a(jsonobject, "show_toast", true);
            boolean flag1 = JsonUtils.func_151209_a(jsonobject, "announce_to_chat", true);
            boolean flag2 = JsonUtils.func_151209_a(jsonobject, "hidden", false);

            return new DisplayInfo(itemstack, ichatbasecomponent, ichatbasecomponent1, minecraftkey, advancementframetype, flag, flag1, flag2);
        } else {
            throw new JsonSyntaxException("Both title and description must be set");
        }
    }

    private static ItemStack func_193221_a(JsonObject jsonobject) {
        if (!jsonobject.has("item")) {
            throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add \'item\' key)");
        } else {
            Item item = JsonUtils.func_188180_i(jsonobject, "item");
            int i = JsonUtils.func_151208_a(jsonobject, "data", 0);

            return new ItemStack(item, 1, i);
        }
    }

    public void func_192290_a(PacketBuffer packetdataserializer) {
        packetdataserializer.func_179256_a(this.field_192300_a);
        packetdataserializer.func_179256_a(this.field_193225_b);
        packetdataserializer.func_150788_a(this.field_192301_b);
        packetdataserializer.func_179249_a((Enum) this.field_192303_d);
        int i = 0;

        if (this.field_192302_c != null) {
            i |= 1;
        }

        if (this.field_193226_f) {
            i |= 2;
        }

        if (this.field_193228_h) {
            i |= 4;
        }

        packetdataserializer.writeInt(i);
        if (this.field_192302_c != null) {
            packetdataserializer.func_192572_a(this.field_192302_c);
        }

        packetdataserializer.writeFloat(this.field_192304_e);
        packetdataserializer.writeFloat(this.field_192305_f);
    }

    public static DisplayInfo func_192295_b(PacketBuffer packetdataserializer) {
        ITextComponent ichatbasecomponent = packetdataserializer.func_179258_d();
        ITextComponent ichatbasecomponent1 = packetdataserializer.func_179258_d();
        ItemStack itemstack = packetdataserializer.func_150791_c();
        FrameType advancementframetype = (FrameType) packetdataserializer.func_179257_a(FrameType.class);
        int i = packetdataserializer.readInt();
        ResourceLocation minecraftkey = (i & 1) != 0 ? packetdataserializer.func_192575_l() : null;
        boolean flag = (i & 2) != 0;
        boolean flag1 = (i & 4) != 0;
        DisplayInfo advancementdisplay = new DisplayInfo(itemstack, ichatbasecomponent, ichatbasecomponent1, minecraftkey, advancementframetype, flag, false, flag1);

        advancementdisplay.func_192292_a(packetdataserializer.readFloat(), packetdataserializer.readFloat());
        return advancementdisplay;
    }
}
