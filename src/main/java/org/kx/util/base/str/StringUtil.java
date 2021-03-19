package org.kx.util.base.str;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.kx.util.FileUtil;
import org.kx.util.base.BaseException;
import org.kx.util.config.SysConf;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


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
            throw new BaseException("json转换错误",e);
        }
    }

    /**
     * String  to json
     */
    public static JSONArray toJSONArray(String str) {
        try {
            return JSONObject.parseArray(str);
        } catch (Exception e) {
            throw new BaseException("json to array 转换错误",e);
        }
    }


    /**
     * String  to json
     */
    public static String toIds(List<Long> ids) {
        StringBuilder sbt = new StringBuilder();
        ids.stream().forEach(id ->{
            sbt.append(id).append(",");
        });

        return   sbt.substring(0,sbt.length()-1) ;
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
     * 反驼峰命名
     */
    public static String aaBb(String Aa_Bb) {
        String [] words = Aa_Bb.split("_");
        StringBuilder sbt = new StringBuilder();
        for(String word :words){
            sbt.append(org.kx.util.base.str.StringUtil.UpperFirstWord(word));
        }
        return org.kx.util.base.str.StringUtil.LowerFirstWord(sbt.toString());
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

        List<String> words = new ArrayList<>();
        StringBuilder sbt = new StringBuilder();
        String[] strs = str.split("\n");

        for (String i : strs) {
            String s = i.trim();
            boolean special = false ;
            if (StringUtils.isNotBlank(s)) {
                if(s.contains(" ")){
                    words.addAll( StringUtil.readWords(s));
                    special = true ;
                }
                if(s.contains("，")){
                    words.addAll( StringUtil.readWords(s,"，"));
                    special = true ;
                }
                if(s.contains(",")){
                    words.addAll( StringUtil.readWords(s,","));
                    special = true ;
                }
                if(s.contains("、")){
                    words.addAll( StringUtil.readWords(s,"、"));
                    special = true ;
                }
                if(!special){
                    words.add(s) ;
                }
            }
        }


        for (String i : words) {
            String s = i.trim();
            if (StringUtils.isNotBlank(s)) {
                if(s.startsWith("\'") || s.startsWith("'")){
                    s = s.substring(1);
                }
                if(s.endsWith(",")){
                    s = s.substring(0,s.length() -1);
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


    public  static List<String> buildList(String str){
        return  Arrays.asList(str.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
    }


    public  static List<String> readWords(String str){
        List<String> words = new ArrayList<>();
        String[] word_ = str.split(" ");
        for(String w :word_){
            if(StringUtils.isNotBlank(w)){
                words.add(w);
            }
        }
        return  words ;

    }

    public  static List<String> readWords(String str,String split){
        List<String> words = new ArrayList<>();
        String[] word_ = str.split(split);
        for(String w :word_){
            if(StringUtils.isNotBlank(w)){
                words.add(w);
            }
        }
        return  words ;

    }


    public  static  String buildJosnHtml(String str){
        try{
            if(StringUtils.isBlank(str)){
                return  "" ;
            }
            StringBuilder title = new StringBuilder();
            Map<String,Object> mapType = JSON.parseObject(str,Map.class);
            for (Object obj : mapType.keySet()){
                title.append(obj+" = "+mapType.get(obj)+"<br/>");
            }
            return title.toString();
        }catch (Exception ex){
            throw  new RuntimeException("source line :" + str ,ex);
        }

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



    //属性生成
    public static String generateTcAttrStr(String attrs){
       // System.out.println("attrs ----> " + attrs);
        String[] lines = attrs.split("\n");
        StringBuilder stv = new StringBuilder();
        for(String line :lines){
            if(StringUtils.isBlank(line)){
                continue;
            }
            String[] atrr = null;
            if(line.contains("\t")){
                atrr = line.split("\t");
               // System.out.println("line.contains t");
            } else {
                atrr = line.split(" ");
               // System.out.println("line.contains ");
            }
            /*System.out.println("(atrr[0] "+ atrr[0]);
            System.out.println("(atrr[1] "+ atrr[1]);*/
            stv.append(atrr[0]).append(":").append(atrr[1]).append(";");
        }
        return  stv.toString();
    }

    //odps  wmconcat
    public static String wmconcatOdps(String attrs){
        String[] lines = attrs.split("\n");
        HashMap<String, BigDecimal>  h = new HashMap<>();


        for(String line :lines){
            if(StringUtils.isBlank(line)){
                continue;
            }

            String[] atrr = line.split("\t");
            BigDecimal fb = new BigDecimal(atrr[0]);
            String str =  sortStr(atrr[1]);
            if(h.get(str) == null){
                h.put(str,fb);
            }else {
                h.put(str,h.get(str).add(fb));
            }
        }

        Iterator<Map.Entry<String, BigDecimal>> entries = h.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, BigDecimal> entry = entries.next();
            System.out.println(entry.getKey() + "\t " + entry.getValue());
        }



        return  JSON.toJSONString(h);
    }


    public static String duplicateStr(String str){
        String[] lines = str.trim().split(",");
        StringBuilder sc = new StringBuilder();
        Map<String,Integer> sd = new HashMap();
        Arrays.asList(lines).forEach( word ->{
            sd.put(word.trim(),1);
        });

        Iterator<Map.Entry<String, Integer>> entries = sd.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            sc.append(entry.getKey()).append(",");
        }

        return  sc.toString() ;
    }


    public static String sortStr(String str){
        String[] lines = str.trim().split(",");
        Arrays.sort(lines);
        return  Arrays.toString(lines) ;
    }

    public static  String  readsuperLongTxt (String filepath) throws IOException {
        int sizee = 2000;
        InputStream is = new FileInputStream(filepath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        StringBuilder stringBuilder = new StringBuilder() ;
        StringBuilder stringBuilder2 = new StringBuilder() ;
        line = reader.readLine(); // 读取第一行
        int size = 1;
        while (!StringUtils.isBlank(line)) { // 如果 line 为空说明读完了
            if(size % sizee ==0 ){
                stringBuilder2.append(stringBuilder.substring(0,stringBuilder.length()-1)).append("\n\n\n") ;
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append(line).append(",");
            size ++ ;
            line = reader.readLine(); // 读取下一行
        }
        stringBuilder2.append(stringBuilder.substring(0,stringBuilder.length()-1)).append("\n") ;
        reader.close();
        is.close();
        return   stringBuilder2.toString() ;
    }


    public static  List<String>  readsuperLongTxt2File (String filepath,Integer sizeSet) throws IOException {
        int sizee = 2000;
        if(sizeSet != null && sizeSet >0){
            sizee = sizeSet;
        }
        List<String> filePaths = new ArrayList<>();
        String fileName =FileUtil.getFileName(filepath);
        String sufix =FileUtil.getSuffix(filepath);

        InputStream is = new FileInputStream(filepath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        StringBuilder stringBuilder = new StringBuilder() ;
         line = reader.readLine(); // 读取第一行
        int size = 0;
        int sufixIndex = 1;
        while (line != null) { // 如果 line 为空说明读完了
            size ++ ;
            if(StringUtils.isBlank(line)){
                line = reader.readLine(); // 读取下一行
                continue;
            }
            if(line.startsWith("'")){
                line = line.substring(1);
            }
            if(size % sizee ==0 ){
                String filePath =  SysConf.upLoadPath+fileName+"_"+sufixIndex+"."+sufix ;
                FileUtil.writeStringToFile(stringBuilder.substring(0,stringBuilder.length()-1), filePath);
                filePaths.add(filePath) ;
                stringBuilder = new StringBuilder();
                sufixIndex ++  ;
            }

            stringBuilder.append(line).append(",");
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        String filePath =  SysConf.upLoadPath+fileName+"_"+sufixIndex+"."+sufix ;
        FileUtil.writeStringToFile(stringBuilder.substring(0,stringBuilder.length()-1), filePath);
        filePaths.add(filePath) ;
        return   filePaths ;
    }

    public static void main(String[] args) throws IOException {
        String str ="1435979270899368073 1239112320264770605 1239018757298768803\n" +
                "1460970936903920722 1238868783570768803 1238747497300333811";
        System.out.println(StringUtil.mergeLine(str));
    }

}
