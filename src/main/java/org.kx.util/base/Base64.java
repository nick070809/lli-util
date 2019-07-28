package org.kx.util.base;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.UnsupportedEncodingException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64 {
    public Base64() {
    }

    public static String getBASE64(String s) {
        if (s == null) {
            return null;
        } else {
            try {
                return (new BASE64Encoder()).encode(s.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    public static String getBASE64(byte[] b) {
        return (new BASE64Encoder()).encode(b);
    }

    public static String getFromBASE64(String s) {
        if (s == null) {
            return null;
        } else {
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] b = decoder.decodeBuffer(s);
                return new String(b, "UTF-8");
            } catch (Exception var3) {
                return null;
            }
        }
    }

    public static byte[] getBytesBASE64(String s) {
        if (s == null) {
            return null;
        } else {
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] b = decoder.decodeBuffer(s);
                return b;
            } catch (Exception var3) {
                return null;
            }
        }
    }
}
