package com.tuda.teacher.util;

public class ByteManager {
    public static String toHex(byte[] b) {
        StringBuilder builder = new StringBuilder();
        for (byte i : b) {
            String a = Integer.toHexString(((int) i) & 0xFF);
            if (a.length() == 1)
                builder.append('0');
            builder.append(a);
        }
        return builder.toString();
    }

    public static byte[] fromHex(CharSequence hex) {
        if (hex == null)
            return null;
        final int l = hex.length() / 2;
        if (l == 0)
            return null;
        final byte[] b = new byte[l];
        for (int i = 0; i < l; ++i) {
            b[i] = (byte) Integer.parseInt(hex.subSequence(i * 2, i * 2 + 2).toString(), 16);
        }
        return b;
    }

    public static int getHexToDec(Integer hex) {
        return getHexToDec(hex.toString());
    }
    public static int getHexToDec(String hex) {
        try {
            return Integer.parseInt(hex, 16);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getDecToHex(int dec){
        return Integer.toHexString(dec).toUpperCase();
    }
}
