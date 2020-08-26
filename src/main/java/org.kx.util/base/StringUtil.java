package org.kx.util.base;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
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
                if(s.startsWith("\'") || s.startsWith("'")){
                    s = s.substring(1);
                }
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
     * 多行合并为等长度数行
     */
    public static String mergeLine(String str,long LengthLimit) {
        //System.out.println(str);
        StringBuilder sbt = new StringBuilder();
        String[] strs = str.split("\n");
        long currentLenth = 1;
        for (String i : strs) {
            String s = i.trim();
            if (StringUtils.isNotBlank(s)) {
                if(currentLenth != 1){
                    currentLenth = currentLenth + s.length() +1 ;
                    if(currentLenth >= LengthLimit){
                        sbt.append("\n");
                        currentLenth = 0l;
                    }else {
                        sbt.append(",");
                    }
                    sbt.append(s);
                }else {
                    sbt.append(s);
                }
                currentLenth = currentLenth + s.length() +1 ;
            }
        }
        return sbt.toString();
    }






    /**
     *  一行 转多行
     */
    public static String toLine(String str) {
        StringBuilder sbt = new StringBuilder();
        String[] sss= str.split(",");
        for (String i : sss) {
            String s = i.trim();
            if (StringUtils.isNotBlank(s)) {
                if (sbt.length() > 0) {
                    sbt.append("\n").append(s);
                } else {
                    sbt.append(s);
                }
            }
        }
        return sbt.toString();
    }


    /**
     * 一行 转多行
     */
    public static String getMiddleString(String line, String prx, String sux) {

        if (StringUtils.isBlank(sux)) {
            if (line.length() > prx.length()) {
                return line.substring(prx.length());
            }
            return null;
        }
        if (line.contains(sux)) {
            String ssss = line.substring(prx.length());
            return ssss.split(sux)[0];
        }
        return null;
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


    /**
     * 判断第一个字母是大写
     * @param field
     * @return
     */
    public static boolean firstIsUpper(String field){

        if(StringUtils.isBlank(field)){
            throw new RuntimeException("field is null");
        }
        return  Character.isUpperCase(field.charAt(0));
    }


    public static String generateMethodFiled(String field){
        if(StringUtils.isBlank(field)){
            throw new RuntimeException("field is null");
        }
        if(field.length() ==1){
            return field.toUpperCase() ;
        }
        return field.substring(0,1).toUpperCase() +field.substring(1) ;
    }


    /**
     * 对象属性名称构造数据库列名称
     */
    public static String generateColumn(String field){
        if(field == null){
            return  null;
        }
        StringBuilder sbt = new StringBuilder();
        for(int i=0;i<field.length();i++){
            char c = field.charAt(i);
            if (Character.isUpperCase(c)) {
                sbt.append("_"+ (char)(c +32));
            }else if(Character.isDigit(c)){
                sbt.append("_"+ c);
            }else{
                sbt.append(c);
            }
        }

        String ss = sbt.toString();
        if(ss.startsWith("_"))ss = ss.substring(1);
        return ss.replace("__", "_").trim();
    }



    /**
     * 数据库列名称构造对象属性名称
     */
    public static String generateFild(String column){
        if(column == null){
            return  null;
        }
        String []  xd = column.split("_");
        StringBuilder sbt = new StringBuilder();
        sbt.append(xd[0]);


        for(int i = 1; i<xd.length; i++){

            sbt.append(generateMethodFiled(xd[i]));

        }

        return sbt.toString();
    }



    /**
     * 去重
     */
    public static String removeRepeat(String str){
        StringBuilder sbt = new StringBuilder();
        String[] strings = str.split(",");
        HashMap<String,Integer> hashMap = new HashMap();
        for(String id :strings){
            if(StringUtils.isBlank(id)){
                continue;
            }
            hashMap.put(id,1);
        }
        for (String key : hashMap.keySet()) {
           if(sbt.length() == 0){
               sbt.append(key);
           }else {
               sbt.append(",").append(key);
           }

            System.out.println("\""+key+"\":\"1584694800000_6\",");

        }
        return sbt.toString();
    }








    public static void main(String... s) {
        String sss ="2201413717562,2200657715182,2641546987,2631942246,2201225963721,2205884707813,3980771770,4073846757,2206781751396,2200657974488,2085938070,3200271173,4074914497,3925705463,2204191394870,2452830586,2316003304,2138379800,2170968104,2206446213812,2549599360,3586492189,2255459295,4229074189,2368797506,2031979990,2611862987,4028870603,4263377845,4093769125,2206372202026,3917814910,2206445356761,4050264010,2206386276113,2200784687700,4038544812,2201230376636,2760506346,2980713334,2200540802479,2200647497505,2974720004,2201168176213,2159616682,3038267337,2201471019974,2205223260746,2201444244244,2200757967709,3984972463,4154678310,4033682043,2064657420,3008786874,2206407472115,2849136065,3230463886,4130369596,2201479466820,2200727015554,2166290049,2201196294013,3487097004,3319205504,3840241650,2095455386,4277857023,2206465258082,3684403239,2175912342,2634266092,4044374317,2206438938204,2424228634,2200722976014,2206377715400,2536205015,2206521377582,2201114660145,4020590985,2200736505364,2202893664639,4069918541,3247051043,4065259533,2200709858418,2201506998074,2206436598338,2206397949433,2207284114499,2200700305888,2916259684,2206353607607,2200657974488,2200657715182" ;//strTo16(sss) ;


        System.out.println(removeRepeat(sss));
    }


}
