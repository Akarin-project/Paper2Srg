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

    private final ITextComponent title;
    private final ITextComponent description;
    private final ItemStack icon;
    private final ResourceLocation background;
    private final FrameType frame;
    private final boolean showToast;
    private final boolean announceToChat;
    private final boolean hidden;
    private float x;
    private float y;

    public DisplayInfo(ItemStack itemstack, ITextComponent ichatbasecomponent, ITextComponent ichatbasecomponent1, @Nullable ResourceLocation minecraftkey, FrameType advancementframetype, boolean flag, boolean flag1, boolean flag2) {
        this.title = ichatbasecomponent;
        this.description = ichatbasecomponent1;
        this.icon = itemstack;
        this.background = minecraftkey;
        this.frame = advancementframetype;
        this.showToast = flag;
        this.announceToChat = flag1;
        this.hidden = flag2;
    }

    public void setPosition(float f, float f1) {
        this.x = f;
        this.y = f1;
    }

    public ITextComponent getTitle() {
        return this.title;
    }

    public ITextComponent getDescription() {
        return this.description;
    }

    public FrameType getFrame() {
        return this.frame;
    }

    public boolean shouldAnnounceToChat() {
        return this.announceToChat;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public static DisplayInfo deserialize(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ITextComponent ichatbasecomponent = (ITextComponent) JsonUtils.deserializeClass(jsonobject, "title", jsondeserializationcontext, ITextComponent.class);
        ITextComponent ichatbasecomponent1 = (ITextComponent) JsonUtils.deserializeClass(jsonobject, "description", jsondeserializationcontext, ITextComponent.class);

        if (ichatbasecomponent != null && ichatbasecomponent1 != null) {
            ItemStack itemstack = deserializeIcon(JsonUtils.getJsonObject(jsonobject, "icon"));
            ResourceLocation minecraftkey = jsonobject.has("background") ? new ResourceLocation(JsonUtils.getString(jsonobject, "background")) : null;
            FrameType advancementframetype = jsonobject.has("frame") ? FrameType.byName(JsonUtils.getString(jsonobject, "frame")) : FrameType.TASK;
            boolean flag = JsonUtils.getBoolean(jsonobject, "show_toast", true);
            boolean flag1 = JsonUtils.getBoolean(jsonobject, "announce_to_chat", true);
            boolean flag2 = JsonUtils.getBoolean(jsonobject, "hidden", false);

            return new DisplayInfo(itemstack, ichatbasecomponent, ichatbasecomponent1, minecraftkey, advancementframetype, flag, flag1, flag2);
        } else {
            throw new JsonSyntaxException("Both title and description must be set");
        }
    }

    private static ItemStack deserializeIcon(JsonObject jsonobject) {
        if (!jsonobject.has("item")) {
            throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add \'item\' key)");
        } else {
            Item item = JsonUtils.getItem(jsonobject, "item");
            int i = JsonUtils.getInt(jsonobject, "data", 0);

            return new ItemStack(item, 1, i);
        }
    }

    public void write(PacketBuffer packetdataserializer) {
        packetdataserializer.writeTextComponent(this.title);
        packetdataserializer.writeTextComponent(this.description);
        packetdataserializer.writeItemStack(this.icon);
        packetdataserializer.writeEnumValue((Enum) this.frame);
        int i = 0;

        if (this.background != null) {
            i |= 1;
        }

        if (this.showToast) {
            i |= 2;
        }

        if (this.hidden) {
            i |= 4;
        }

        packetdataserializer.writeInt(i);
        if (this.background != null) {
            packetdataserializer.writeResourceLocation(this.background);
        }

        packetdataserializer.writeFloat(this.x);
        packetdataserializer.writeFloat(this.y);
    }

    public static DisplayInfo read(PacketBuffer packetdataserializer) {
        ITextComponent ichatbasecomponent = packetdataserializer.readTextComponent();
        ITextComponent ichatbasecomponent1 = packetdataserializer.readTextComponent();
        ItemStack itemstack = packetdataserializer.readItemStack();
        FrameType advancementframetype = (FrameType) packetdataserializer.readEnumValue(FrameType.class);
        int i = packetdataserializer.readInt();
        ResourceLocation minecraftkey = (i & 1) != 0 ? packetdataserializer.readResourceLocation() : null;
        boolean flag = (i & 2) != 0;
        boolean flag1 = (i & 4) != 0;
        DisplayInfo advancementdisplay = new DisplayInfo(itemstack, ichatbasecomponent, ichatbasecomponent1, minecraftkey, advancementframetype, flag, false, flag1);

        advancementdisplay.setPosition(packetdataserializer.readFloat(), packetdataserializer.readFloat());
        return advancementdisplay;
    }
}
