package org.kx.util.ddl;

import org.apache.commons.lang3.StringUtils;

/**
 * Description ： Created by  xianguang.skx Since 2019/11/28
 */

public class IDBSQLFormat {
    static String CN_ = " "; //中文空格
    static String EN_ = " "; //英文空格
    static String EN1_ = " "; //英文空格

    public static void main(String[] args) {
        String ss = "update user_db  set status = -1 ,gmt_modified = now() where id =  6305721  ;"
            + "update    fill_db    set status = -1 ,gmt_modified = now()   where id in "
            + "(68910456,68409390, 67939131,67936760,67926705)   ;";

        System.out.println(dealStr(ss));
    }


    private  static  String dealStr(String sqls){
        String[] items_ = sqls.split(";");
        StringBuilder sbt = new StringBuilder();
        for(String i :items_){
            sbt.append(dealSql(i)).append(";\n");
        }
        return sbt.toString();
    }



    private  static  String dealSql(String sql){
        String[] items_ = sql.split(EN_);
        StringBuilder sbt = new StringBuilder();
        for(String i :items_){
            String k = i.trim();
            if(StringUtils.isEmpty(k)){
                continue;
            }else {
                if(!i.equals(EN1_)){
                    sbt.append(i).append(EN_);
                }
            }
        }
        return dealsss(sbt.toString());
    }



    private  static String dealsss(String sql){
        String lastChar = "" ;
        boolean hasWhere = false ;
        //String[] sql_ = sql.split(" ");
        String[] sql_ = sql.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for(String i :sql_){
            if(StringUtils.isNotBlank(i)){
                String k = i.trim();
                if(k.equalsIgnoreCase("where") || k.contains("where") || k.contains("WHERE")){
                    hasWhere = true ;
                }
                if(!hasWhere && lastChar.equals("\'")){
                    stringBuilder.append(k);
                }else   if(lastChar.equals(",") || lastChar.equals("=")|| lastChar.equals("{")
                    || lastChar.equals(":") || lastChar.equals("\"")
                    ){
                    stringBuilder.append(k);
                }else {
                    if(stringBuilder.length() == 0){
                        stringBuilder.append(k);
                    }else {
                        stringBuilder.append(EN_).append(k);
                    }

                }
                lastChar = k.substring(k.length() -1) ;
            }
        }
        return stringBuilder.toString();
    }

}
