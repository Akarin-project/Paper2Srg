package net.minecraft.network.rcon;

import java.nio.charset.StandardCharsets;

public class RConUtils {

    public static final char[] field_72666_a = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String func_72661_a(byte[] abyte, int i, int j) {
        int k = j - 1;

        int l;

        for (l = i > k ? k : i; 0 != abyte[l] && l < k; ++l) {
            ;
        }

        return new String(abyte, i, l - i, StandardCharsets.UTF_8);
    }

    public static int func_72662_b(byte[] abyte, int i) {
        return func_72665_b(abyte, i, abyte.length);
    }

    public static int func_72665_b(byte[] abyte, int i, int j) {
        return 0 > j - i - 4 ? 0 : abyte[i + 3] << 24 | (abyte[i + 2] & 255) << 16 | (abyte[i + 1] & 255) << 8 | abyte[i] & 255;
    }

    public static int func_72664_c(byte[] abyte, int i, int j) {
        return 0 > j - i - 4 ? 0 : abyte[i] << 24 | (abyte[i + 1] & 255) << 16 | (abyte[i + 2] & 255) << 8 | abyte[i + 3] & 255;
    }

    public static String func_72663_a(byte b0) {
        return "" + RConUtils.field_72666_a[(b0 & 240) >>> 4] + RConUtils.field_72666_a[b0 & 15];
    }
}
