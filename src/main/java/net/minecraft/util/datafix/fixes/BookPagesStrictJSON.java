package net.minecraft.util.datafix.fixes;

import com.google.gson.JsonParseException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class BookPagesStrictJSON implements IFixableData {

    public BookPagesStrictJSON() {}

    public int getFixVersion() {
        return 165;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("minecraft:written_book".equals(nbttagcompound.getString("id"))) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("tag");

            if (nbttagcompound1.hasKey("pages", 9)) {
                NBTTagList nbttaglist = nbttagcompound1.getTagList("pages", 8);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    String s = nbttaglist.getStringTagAt(i);
                    Object object = null;

                    if (!"null".equals(s) && !StringUtils.isNullOrEmpty(s)) {
                        if ((s.charAt(0) != 34 || s.charAt(s.length() - 1) != 34) && (s.charAt(0) != 123 || s.charAt(s.length() - 1) != 125)) {
                            object = new TextComponentString(s);
                        } else {
                            try {
                                object = (ITextComponent) JsonUtils.gsonDeserialize(SignStrictJSON.GSON_INSTANCE, s, ITextComponent.class, true);
                                if (object == null) {
                                    object = new TextComponentString("");
                                }
                            } catch (JsonParseException jsonparseexception) {
                                ;
                            }

                            if (object == null) {
                                try {
                                    object = ITextComponent.Serializer.jsonToComponent(s);
                                } catch (JsonParseException jsonparseexception1) {
                                    ;
                                }
                            }

                            if (object == null) {
                                try {
                                    object = ITextComponent.Serializer.fromJsonLenient(s);
                                } catch (JsonParseException jsonparseexception2) {
                                    ;
                                }
                            }

                            if (object == null) {
                                object = new TextComponentString(s);
                            }
                        }
                    } else {
                        object = new TextComponentString("");
                    }

                    nbttaglist.set(i, new NBTTagString(ITextComponent.Serializer.componentToJson((ITextComponent) object)));
                }

                nbttagcompound1.setTag("pages", nbttaglist);
            }
        }

        return nbttagcompound;
    }
}
