package org.kx.util.base;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by sunkx on 2017/5/11.
 */
public class StringUtil {
    /**
     * 使單詞的首字母小寫
     */
    public static String LowerFirstWord(String str) {
        char[] chars = new char[1];
        chars[0] = str.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            return str.replaceFirst(temp, temp.toLowerCase());
        }
        return str;
    }

    /**
     * 使單詞的首字母大寫
     */
    public static String UpperFirstWord(String str) {
        char[] chars = new char[1];
        chars[0] = str.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            return str.replaceFirst(temp, temp.toUpperCase());
        }
        return str;
    }

    /**
     * String  to json
     */
    public static JSONObject toJSONObject(String str) {
        try {
            return JSONObject.parseObject(str);
        } catch (Exception e) {
            throw new BaseException(BaseErrorCode.SystemError, "json转换错误");
        }
    }

    /**
     * String  to json
     */
    public static JSONArray toJSONArray(String str) {
        try {
            return JSONObject.parseArray(str);
        } catch (Exception e) {
            throw new BaseException(BaseErrorCode.SystemError, "json to array 转换错误");
        }
    }

    /**
     * 特殊字符处理
     */


    public static String handleFileName(String fileName) {
        Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");     //   / \ " : | * ? < >
        // Pattern pattern = Pattern.compile("[\\*\\?\\\"<>\\|]");
        Matcher matcher = pattern.matcher(fileName);
        return matcher.replaceAll("");
    }

    /**
     * 驼峰命名
     */
    public static String Aa_Bb(String AaBb) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < AaBb.length(); i++) {
            char c = AaBb.charAt(i);
            if (!Character.isLowerCase(c)) {
                buf.append("_" + c);
            } else {
                buf.append(c);
            }
        }
        String ss = buf.toString();
        if (ss.startsWith("_")) ss = ss.substring(1);
        return ss.replace("__", "_");
    }


    /**
     * 文本长度
     */
    public static long getStringLenth(String str) {
        return str.length();
    }


    /**
     * 多行合并为一行
     */
    public static String mergeLine(String str) {
        StringBuilder sbt = new StringBuilder();
        String[] strs = str.split("\n");
        for (String i : strs) {
            String s = i.trim();
            if (StringUtils.isNotBlank(s)) {
                if (sbt.length() > 0) {
                    sbt.append(",").append(s);
                } else {
                    sbt.append(s);
                }
            }
        }
        return sbt.toString();
    }


    /**
     * 字符串转化成为16进制字符串
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
            return unicode.toString();
    }


    public static void main(String... s) {
        String sss = "Auth fail";
        String s16 ="0x3233333233343233" ;//strTo16(sss) ;
        System.out.println(s16);

        System.out.println(hexStringToString(s16));
    }


}
