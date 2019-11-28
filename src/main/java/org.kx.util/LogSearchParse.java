package org.kx.util;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.config.MyCnfig;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/28
 */

public class LogSearchParse {
    public static void main(String[] args) throws Exception{
        readDiff(MyCnfig.LOG_FILE);
    }


    public   static String readDiff(String filePath) throws Exception {

        InputStream is = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sbt  = new StringBuilder();
        String ipline = null;
        int ii =0,temp = 0;
        for(String line = reader.readLine(); line != null; line = reader.readLine()) {

            line = line.trim() ;
            if(StringUtils.isBlank(line) ){
                continue;
            }
            if(line.startsWith("---")){
                ipline =line ;
                temp ++ ;
            }
            if(line.endsWith("0")){
                continue;
            }
            String lastChar =line.substring(line.length()-1) ;
            if(!isNumeric(lastChar)){
                continue;
            }
            if(temp != ii){
                sbt.append("$").append(getIp(ipline));
                ii =temp ;
            }
            sbt.append("$").append(line);
        }


        reader.close();
        is.close();
        return  sbt.toString();
    }






    public  static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }


    private static String getIp(String line){
        String[] strs = line.split(" ");
        StringBuilder sbt  = new StringBuilder() ;
        StringBuilder sbt2  = new StringBuilder() ;
        for(String str :strs){
            if(str.trim().equals("---")){
                continue;
            }
            if(str.trim().equals("开始搜索")){
                continue;
            }
            if(sbt.length()>1){
                sbt.append(".").append(str);
            }else {
                sbt.append(str);
            }
        }
        String hj =sbt.toString() ;

        //System.out.println(hj);
        String[] strs2 =hj.split("\\.");
        int ipFlag =getFirstInt(strs2[0]);
        String service = strs2[0].substring(0,ipFlag);
        String ipstr = strs2[0].substring(ipFlag,strs2[0].length());
        sbt2.append(service).append(" ");
        sbt2.append(dealIpStr(ipstr));
        for(int index =1;index < strs2.length;index++){
            sbt2.append(" ").append(strs2[index]);
        }
        //System.out.println(sbt2);
        return  sbt2.toString();
    }


    private  static int getFirstInt(String s){
        for(int i=0;i<s.length();i++){
            char a = s.charAt(i);
            if(a>='0' && a<='9'){
                return i;

            }
        }
        throw  new RuntimeException("not found number");
    }


    private static String dealIpStr(String s){
        //System.out.println( s);
        StringBuilder sbt  = new StringBuilder() ;
        sbt.append(dealSegmentStr(s.substring(0,3))).append(".").
            append(dealSegmentStr(s.substring(3,6))).append(".").
            append(dealSegmentStr(s.substring(6,9))).append(".").
            append(dealSegmentStr(s.substring(9,12)));
        return sbt.toString();
    }

    private static String dealSegmentStr(String s){
        String temp = s.toString() ;
         while (temp.startsWith("0")){
            temp = temp.substring(1,temp.length());
        }
        return temp;
    }

}
