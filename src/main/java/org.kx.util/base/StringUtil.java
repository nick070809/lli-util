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
    public static String LowerFirstWord(String str){
        char[] chars=new char[1];
        chars[0]=str.charAt(0);
        String temp=new String(chars);
        if(chars[0]>='A'  &&  chars[0]<='Z') {
            return str.replaceFirst(temp,temp.toLowerCase());
        }
        return str;
    }

    /**
     * 使單詞的首字母大寫
     */
    public static String UpperFirstWord(String str){
        char[] chars=new char[1];
        chars[0]=str.charAt(0);
        String temp=new String(chars);
        if(chars[0]>='a'  &&  chars[0]<='z') {
            return str.replaceFirst(temp,temp.toUpperCase());
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
            throw new BaseException(BaseErrorCode.SystemError,"json转换错误");
        }
    }

    /**
     * String  to json
     */
    public static JSONArray toJSONArray(String str) {
        try {
            return JSONObject.parseArray(str);
        } catch (Exception e) {
            throw new BaseException(BaseErrorCode.SystemError,"json to array 转换错误");
        }
    }

    /**
     * 特殊字符处理
     */


    public static String  handleFileName(String fileName){
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
        for(int i=0;i<AaBb.length();i++){
            char c = AaBb.charAt(i);
            if (!Character.isLowerCase(c)){
                buf.append("_"+c);
            }else{
                buf.append(c);
            }
        }
        String ss = buf.toString();
        if(ss.startsWith("_"))ss = ss.substring(1);
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
       for(String i :strs){
           String s= i.trim();
            if(StringUtils.isNotBlank(s)){
               if(sbt.length() >0 ){
                   sbt.append(",").append(s);
               }else {
                   sbt.append(s);
               }
            }
       }
        return sbt.toString();
    }



    public static void main(String...s){
        String sss = "erwerw\ndwern\neee";
        System.out.println(mergeLine(sss));
    }


}
