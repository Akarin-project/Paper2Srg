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

    public int func_188216_a() {
        return 165;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("minecraft:written_book".equals(nbttagcompound.func_74779_i("id"))) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("tag");

            if (nbttagcompound1.func_150297_b("pages", 9)) {
                NBTTagList nbttaglist = nbttagcompound1.func_150295_c("pages", 8);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    String s = nbttaglist.func_150307_f(i);
                    Object object = null;

                    if (!"null".equals(s) && !StringUtils.func_151246_b(s)) {
                        if ((s.charAt(0) != 34 || s.charAt(s.length() - 1) != 34) && (s.charAt(0) != 123 || s.charAt(s.length() - 1) != 125)) {
                            object = new TextComponentString(s);
                        } else {
                            try {
                                object = (ITextComponent) JsonUtils.func_188176_a(SignStrictJSON.field_188225_a, s, ITextComponent.class, true);
                                if (object == null) {
                                    object = new TextComponentString("");
                                }
                            } catch (JsonParseException jsonparseexception) {
                                ;
                            }

                            if (object == null) {
                                try {
                                    object = ITextComponent.Serializer.func_150699_a(s);
                                } catch (JsonParseException jsonparseexception1) {
                                    ;
                                }
                            }

                            if (object == null) {
                                try {
                                    object = ITextComponent.Serializer.func_186877_b(s);
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

                    nbttaglist.func_150304_a(i, new NBTTagString(ITextComponent.Serializer.func_150696_a((ITextComponent) object)));
                }

                nbttagcompound1.func_74782_a("pages", nbttaglist);
            }
        }

        return nbttagcompound;
    }
}
