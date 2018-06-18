package net.minecraft.network.rcon;

import java.nio.charset.StandardCharsets;

public class RConUtils {

    public static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getBytesAsString(byte[] abyte, int i, int j) {
        int k = j - 1;

        int l;

        for (l = i > k ? k : i; 0 != abyte[l] && l < k; ++l) {
            ;
        }

        return new String(abyte, i, l - i, StandardCharsets.UTF_8);
    }

    public static int getRemainingBytesAsLEInt(byte[] abyte, int i) {
        return getBytesAsLEInt(abyte, i, abyte.length);
    }

    public static int getBytesAsLEInt(byte[] abyte, int i, int j) {
        return 0 > j - i - 4 ? 0 : abyte[i + 3] << 24 | (abyte[i + 2] & 255) << 16 | (abyte[i + 1] & 255) << 8 | abyte[i] & 255;
    }

    public static int getBytesAsBEint(byte[] abyte, int i, int j) {
        return 0 > j - i - 4 ? 0 : abyte[i] << 24 | (abyte[i + 1] & 255) << 16 | (abyte[i + 2] & 255) << 8 | abyte[i + 3] & 255;
    }

    public static String getByteAsHexString(byte b0) {
        return "" + RConUtils.HEX_DIGITS[(b0 & 240) >>> 4] + RConUtils.HEX_DIGITS[b0 & 15];
    }
}
