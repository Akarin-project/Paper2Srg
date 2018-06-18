package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JsonToNBT {

    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    private final String string;
    private int cursor;

    public static NBTTagCompound getTagFromJson(String s) throws NBTException {
        return (new JsonToNBT(s)).readSingleStruct();
    }

    @VisibleForTesting
    NBTTagCompound readSingleStruct() throws NBTException {
        NBTTagCompound nbttagcompound = this.readStruct();

        this.skipWhitespace();
        if (this.canRead()) {
            ++this.cursor;
            throw this.exception("Trailing data found");
        } else {
            return nbttagcompound;
        }
    }

    @VisibleForTesting
    JsonToNBT(String s) {
        this.string = s;
    }

    protected String readKey() throws NBTException {
        this.skipWhitespace();
        if (!this.canRead()) {
            throw this.exception("Expected key");
        } else {
            return this.peek() == 34 ? this.readQuotedString() : this.readString();
        }
    }

    private NBTException exception(String s) {
        return new NBTException(s, this.string, this.cursor);
    }

    protected NBTBase readTypedValue() throws NBTException {
        this.skipWhitespace();
        if (this.peek() == 34) {
            return new NBTTagString(this.readQuotedString());
        } else {
            String s = this.readString();

            if (s.isEmpty()) {
                throw this.exception("Expected value");
            } else {
                return this.type(s);
            }
        }
    }

    private NBTBase type(String s) {
        try {
            if (JsonToNBT.FLOAT_PATTERN.matcher(s).matches()) {
                return new NBTTagFloat(Float.parseFloat(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.BYTE_PATTERN.matcher(s).matches()) {
                return new NBTTagByte(Byte.parseByte(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.LONG_PATTERN.matcher(s).matches()) {
                return new NBTTagLong(Long.parseLong(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.SHORT_PATTERN.matcher(s).matches()) {
                return new NBTTagShort(Short.parseShort(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.INT_PATTERN.matcher(s).matches()) {
                return new NBTTagInt(Integer.parseInt(s));
            }

            if (JsonToNBT.DOUBLE_PATTERN.matcher(s).matches()) {
                return new NBTTagDouble(Double.parseDouble(s.substring(0, s.length() - 1)));
            }

            if (JsonToNBT.DOUBLE_PATTERN_NOSUFFIX.matcher(s).matches()) {
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

    private String readQuotedString() throws NBTException {
        int i = ++this.cursor;
        StringBuilder stringbuilder = null;
        boolean flag = false;

        while (this.canRead()) {
            char c0 = this.pop();

            if (flag) {
                if (c0 != 92 && c0 != 34) {
                    throw this.exception("Invalid escape of \'" + c0 + "\'");
                }

                flag = false;
            } else {
                if (c0 == 92) {
                    flag = true;
                    if (stringbuilder == null) {
                        stringbuilder = new StringBuilder(this.string.substring(i, this.cursor - 1));
                    }
                    continue;
                }

                if (c0 == 34) {
                    return stringbuilder == null ? this.string.substring(i, this.cursor - 1) : stringbuilder.toString();
                }
            }

            if (stringbuilder != null) {
                stringbuilder.append(c0);
            }
        }

        throw this.exception("Missing termination quote");
    }

    private String readString() {
        int i;

        for (i = this.cursor; this.canRead() && this.isAllowedInKey(this.peek()); ++this.cursor) {
            ;
        }

        return this.string.substring(i, this.cursor);
    }

    protected NBTBase readValue() throws NBTException {
        this.skipWhitespace();
        if (!this.canRead()) {
            throw this.exception("Expected value");
        } else {
            char c0 = this.peek();

            return (NBTBase) (c0 == 123 ? this.readStruct() : (c0 == 91 ? this.readList() : this.readTypedValue()));
        }
    }

    protected NBTBase readList() throws NBTException {
        return this.canRead((int) 2) && this.peek((int) 1) != 34 && this.peek((int) 2) == 59 ? this.readArrayTag() : this.readListTag();
    }

    protected NBTTagCompound readStruct() throws NBTException {
        this.expect('{');
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.skipWhitespace();

        while (this.canRead() && this.peek() != 125) {
            String s = this.readKey();

            if (s.isEmpty()) {
                throw this.exception("Expected non-empty key");
            }

            this.expect(':');
            nbttagcompound.setTag(s, this.readValue());
            if (!this.hasElementSeparator()) {
                break;
            }

            if (!this.canRead()) {
                throw this.exception("Expected key");
            }
        }

        this.expect('}');
        return nbttagcompound;
    }

    private NBTBase readListTag() throws NBTException {
        this.expect('[');
        this.skipWhitespace();
        if (!this.canRead()) {
            throw this.exception("Expected value");
        } else {
            NBTTagList nbttaglist = new NBTTagList();
            byte b0 = -1;

            while (this.peek() != 93) {
                NBTBase nbtbase = this.readValue();
                byte b1 = nbtbase.getId();

                if (b0 < 0) {
                    b0 = b1;
                } else if (b1 != b0) {
                    throw this.exception("Unable to insert " + NBTBase.getTagTypeName(b1) + " into ListTag of type " + NBTBase.getTagTypeName(b0));
                }

                nbttaglist.appendTag(nbtbase);
                if (!this.hasElementSeparator()) {
                    break;
                }

                if (!this.canRead()) {
                    throw this.exception("Expected value");
                }
            }

            this.expect(']');
            return nbttaglist;
        }
    }

    private NBTBase readArrayTag() throws NBTException {
        this.expect('[');
        char c0 = this.pop();

        this.pop();
        this.skipWhitespace();
        if (!this.canRead()) {
            throw this.exception("Expected value");
        } else if (c0 == 66) {
            return new NBTTagByteArray(this.readArray((byte) 7, (byte) 1));
        } else if (c0 == 76) {
            return new NBTTagLongArray(this.readArray((byte) 12, (byte) 4));
        } else if (c0 == 73) {
            return new NBTTagIntArray(this.readArray((byte) 11, (byte) 3));
        } else {
            throw this.exception("Invalid array type \'" + c0 + "\' found");
        }
    }

    private <T extends Number> List<T> readArray(byte b0, byte b1) throws NBTException {
        ArrayList arraylist = Lists.newArrayList();

        while (true) {
            if (this.peek() != 93) {
                NBTBase nbtbase = this.readValue();
                byte b2 = nbtbase.getId();

                if (b2 != b1) {
                    throw this.exception("Unable to insert " + NBTBase.getTagTypeName(b2) + " into " + NBTBase.getTagTypeName(b0));
                }

                if (b1 == 1) {
                    arraylist.add(Byte.valueOf(((NBTPrimitive) nbtbase).getByte()));
                } else if (b1 == 4) {
                    arraylist.add(Long.valueOf(((NBTPrimitive) nbtbase).getLong()));
                } else {
                    arraylist.add(Integer.valueOf(((NBTPrimitive) nbtbase).getInt()));
                }

                if (this.hasElementSeparator()) {
                    if (!this.canRead()) {
                        throw this.exception("Expected value");
                    }
                    continue;
                }
            }

            this.expect(']');
            return arraylist;
        }
    }

    private void skipWhitespace() {
        while (this.canRead() && Character.isWhitespace(this.peek())) {
            ++this.cursor;
        }

    }

    private boolean hasElementSeparator() {
        this.skipWhitespace();
        if (this.canRead() && this.peek() == 44) {
            ++this.cursor;
            this.skipWhitespace();
            return true;
        } else {
            return false;
        }
    }

    private void expect(char c0) throws NBTException {
        this.skipWhitespace();
        boolean flag = this.canRead();

        if (flag && this.peek() == c0) {
            ++this.cursor;
        } else {
            throw new NBTException("Expected \'" + c0 + "\' but got \'" + (flag ? Character.valueOf(this.peek()) : "<EOF>") + "\'", this.string, this.cursor + 1);
        }
    }

    protected boolean isAllowedInKey(char c0) {
        return c0 >= 48 && c0 <= 57 || c0 >= 65 && c0 <= 90 || c0 >= 97 && c0 <= 122 || c0 == 95 || c0 == 45 || c0 == 46 || c0 == 43;
    }

    private boolean canRead(int i) {
        return this.cursor + i < this.string.length();
    }

    boolean canRead() {
        return this.canRead((int) 0);
    }

    private char peek(int i) {
        return this.string.charAt(this.cursor + i);
    }

    private char peek() {
        return this.peek((int) 0);
    }

    private char pop() {
        return this.string.charAt(this.cursor++);
    }
}
