package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JsonToNBT {

    private static final Pattern field_193615_a = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern field_193616_b = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern field_193617_c = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern field_193618_d = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern field_193619_e = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern field_193620_f = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern field_193621_g = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    private final String field_193622_h;
    private int field_193623_i;

    public static NBTTagCompound func_180713_a(String s) throws NBTException {
        return (new JsonToNBT(s)).func_193609_a();
    }

    @VisibleForTesting
    NBTTagCompound func_193609_a() throws NBTException {
        NBTTagCompound nbttagcompound = this.func_193593_f();

        this.func_193607_l();
        if (this.func_193612_g()) {
            ++this.field_193623_i;
            throw this.func_193602_b("Trailing data found");
        } else {
            return nbttagcompound;
        }
    }

    @VisibleForTesting
    JsonToNBT(String s) {
        this.field_193622_h = s;
    }

    protected String func_193601_b() throws NBTException {
        this.func_193607_l();
        if (!this.func_193612_g()) {
            throw this.func_193602_b("Expected key");
        } else {
            return this.func_193598_n() == 34 ? this.func_193595_h() : this.func_193614_i();
        }
    }

    private NBTException func_193602_b(String s) {
        return new NBTException(s, this.field_193622_h, this.field_193623_i);
    }

    protected NBTBase func_193611_c() throws NBTException {
        this.func_193607_l();
        if (this.func_193598_n() == 34) {
            return new NBTTagString(this.func_193595_h());
        } else {
            String s = this.func_193614_i();

            if (s.isEmpty()) {
                throw this.func_193602_b("Expected value");
            } else {
                return this.func_193596_c(s);
            }
        }
    }

    private NBTBase func_193596_c(String s) {
        try {
            if (JsonToNBT.field_193617_c.matcher(s).matches()) {
                return new NBTTagFloat(Float.parseFloat(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.field_193618_d.matcher(s).matches()) {
                return new NBTTagByte(Byte.parseByte(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.field_193619_e.matcher(s).matches()) {
                return new NBTTagLong(Long.parseLong(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.field_193620_f.matcher(s).matches()) {
                return new NBTTagShort(Short.parseShort(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.field_193621_g.matcher(s).matches()) {
                return new NBTTagInt(Integer.parseInt(s));
            }

            if (JsonToNBT.field_193616_b.matcher(s).matches()) {
                return new NBTTagDouble(Double.parseDouble(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.field_193615_a.matcher(s).matches()) {
                return new NBTTagDouble(Double.parseDouble(s));
            }

            if ("true".equalsIgnoreCase(s)) {
                return new NBTTagByte((byte) 1);
            }

            if ("false".equalsIgnoreCase(s)) {
                return new NBTTagByte((byte) 0);
            }
        } catch (NumberFormatException numberformatexception) {
            ;
        }

        return new NBTTagString(s);
    }

    private String func_193595_h() throws NBTException {
        int i = ++this.field_193623_i;
        StringBuilder stringbuilder = null;
        boolean flag = false;

        while (this.func_193612_g()) {
            char c0 = this.func_193594_o();

            if (flag) {
                if (c0 != 92 && c0 != 34) {
                    throw this.func_193602_b("Invalid escape of \'" + c0 + "\'");
                }

                flag = false;
            } else {
                if (c0 == 92) {
                    flag = true;
                    if (stringbuilder == null) {
                        stringbuilder = new StringBuilder(this.field_193622_h.substring(i, this.field_193623_i - 1));
                    }
                    continue;
                }

                if (c0 == 34) {
                    return stringbuilder == null ? this.field_193622_h.substring(i, this.field_193623_i - 1) : stringbuilder.toString();
                }
            }

            if (stringbuilder != null) {
                stringbuilder.append(c0);
            }
        }

        throw this.func_193602_b("Missing termination quote");
    }

    private String func_193614_i() {
        int i;

        for (i = this.field_193623_i; this.func_193612_g() && this.func_193599_a(this.func_193598_n()); ++this.field_193623_i) {
            ;
        }

        return this.field_193622_h.substring(i, this.field_193623_i);
    }

    protected NBTBase func_193610_d() throws NBTException {
        this.func_193607_l();
        if (!this.func_193612_g()) {
            throw this.func_193602_b("Expected value");
        } else {
            char c0 = this.func_193598_n();

            return (NBTBase) (c0 == 123 ? this.func_193593_f() : (c0 == 91 ? this.func_193605_e() : this.func_193611_c()));
        }
    }

    protected NBTBase func_193605_e() throws NBTException {
        return this.func_193608_a((int) 2) && this.func_193597_b((int) 1) != 34 && this.func_193597_b((int) 2) == 59 ? this.func_193606_k() : this.func_193600_j();
    }

    protected NBTTagCompound func_193593_f() throws NBTException {
        this.func_193604_b('{');
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.func_193607_l();

        while (this.func_193612_g() && this.func_193598_n() != 125) {
            String s = this.func_193601_b();

            if (s.isEmpty()) {
                throw this.func_193602_b("Expected non-empty key");
            }

            this.func_193604_b(':');
            nbttagcompound.func_74782_a(s, this.func_193610_d());
            if (!this.func_193613_m()) {
                break;
            }

            if (!this.func_193612_g()) {
                throw this.func_193602_b("Expected key");
            }
        }

        this.func_193604_b('}');
        return nbttagcompound;
    }

    private NBTBase func_193600_j() throws NBTException {
        this.func_193604_b('[');
        this.func_193607_l();
        if (!this.func_193612_g()) {
            throw this.func_193602_b("Expected value");
        } else {
            NBTTagList nbttaglist = new NBTTagList();
            byte b0 = -1;

            while (this.func_193598_n() != 93) {
                NBTBase nbtbase = this.func_193610_d();
                byte b1 = nbtbase.func_74732_a();

                if (b0 < 0) {
                    b0 = b1;
                } else if (b1 != b0) {
                    throw this.func_193602_b("Unable to insert " + NBTBase.func_193581_j(b1) + " into ListTag of type " + NBTBase.func_193581_j(b0));
                }

                nbttaglist.func_74742_a(nbtbase);
                if (!this.func_193613_m()) {
                    break;
                }

                if (!this.func_193612_g()) {
                    throw this.func_193602_b("Expected value");
                }
            }

            this.func_193604_b(']');
            return nbttaglist;
        }
    }

    private NBTBase func_193606_k() throws NBTException {
        this.func_193604_b('[');
        char c0 = this.func_193594_o();

        this.func_193594_o();
        this.func_193607_l();
        if (!this.func_193612_g()) {
            throw this.func_193602_b("Expected value");
        } else if (c0 == 66) {
            return new NBTTagByteArray(this.func_193603_a((byte) 7, (byte) 1));
        } else if (c0 == 76) {
            return new NBTTagLongArray(this.func_193603_a((byte) 12, (byte) 4));
        } else if (c0 == 73) {
            return new NBTTagIntArray(this.func_193603_a((byte) 11, (byte) 3));
        } else {
            throw this.func_193602_b("Invalid array type \'" + c0 + "\' found");
        }
    }

    private <T extends Number> List<T> func_193603_a(byte b0, byte b1) throws NBTException {
        ArrayList arraylist = Lists.newArrayList();

        while (true) {
            if (this.func_193598_n() != 93) {
                NBTBase nbtbase = this.func_193610_d();
                byte b2 = nbtbase.func_74732_a();

                if (b2 != b1) {
                    throw this.func_193602_b("Unable to insert " + NBTBase.func_193581_j(b2) + " into " + NBTBase.func_193581_j(b0));
                }

                if (b1 == 1) {
                    arraylist.add(Byte.valueOf(((NBTPrimitive) nbtbase).func_150290_f()));
                } else if (b1 == 4) {
                    arraylist.add(Long.valueOf(((NBTPrimitive) nbtbase).func_150291_c()));
                } else {
                    arraylist.add(Integer.valueOf(((NBTPrimitive) nbtbase).func_150287_d()));
                }

                if (this.func_193613_m()) {
                    if (!this.func_193612_g()) {
                        throw this.func_193602_b("Expected value");
                    }
                    continue;
                }
            }

            this.func_193604_b(']');
            return arraylist;
        }
    }

    private void func_193607_l() {
        while (this.func_193612_g() && Character.isWhitespace(this.func_193598_n())) {
            ++this.field_193623_i;
        }

    }

    private boolean func_193613_m() {
        this.func_193607_l();
        if (this.func_193612_g() && this.func_193598_n() == 44) {
            ++this.field_193623_i;
            this.func_193607_l();
            return true;
        } else {
            return false;
        }
    }

    private void func_193604_b(char c0) throws NBTException {
        this.func_193607_l();
        boolean flag = this.func_193612_g();

        if (flag && this.func_193598_n() == c0) {
            ++this.field_193623_i;
        } else {
            throw new NBTException("Expected \'" + c0 + "\' but got \'" + (flag ? Character.valueOf(this.func_193598_n()) : "<EOF>") + "\'", this.field_193622_h, this.field_193623_i + 1);
        }
    }

    protected boolean func_193599_a(char c0) {
        return c0 >= 48 && c0 <= 57 || c0 >= 65 && c0 <= 90 || c0 >= 97 && c0 <= 122 || c0 == 95 || c0 == 45 || c0 == 46 || c0 == 43;
    }

    private boolean func_193608_a(int i) {
        return this.field_193623_i + i < this.field_193622_h.length();
    }

    boolean func_193612_g() {
        return this.func_193608_a((int) 0);
    }

    private char func_193597_b(int i) {
        return this.field_193622_h.charAt(this.field_193623_i + i);
    }

    private char func_193598_n() {
        return this.func_193597_b((int) 0);
    }

    private char func_193594_o() {
        return this.field_193622_h.charAt(this.field_193623_i++);
    }
}
