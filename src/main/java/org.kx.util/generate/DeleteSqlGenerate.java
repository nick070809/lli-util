package org.kx.util.generate;

import java.util.Iterator;
import java.util.Map;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/28
 */

public class DeleteSqlGenerate {

    public static  String getDeleteSql(String tableName ,Long id  ,Long userId){

        StringBuilder sql =  new StringBuilder("delete  from "+tableName+" where id="+id ) ;
        if(userId != null){
            sql.append(" and user_id = '"+userId+"'");
        }
        sql.append(";");
        return sql.toString();
    }


    public static  String getDeleteSql(String tableName ,String key,String value  ,Long userId){

        StringBuilder sql =  new StringBuilder("delete  from "+tableName+" where  " ) ;
        sql.append( key +" = '"+value+"'");
        if(userId != null){
            sql.append(" and user_id = '"+userId+"'");
        }
        sql.append(";");
        return sql.toString();
    }


    public static  String getDeleteSql(String tableName , Map<String, String> map,  Long userId){

        StringBuilder sql =  new StringBuilder("delete  from "+tableName+" where  1 =1 " ) ;
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            sql.append(" and " + entry.getKey() +" = '"+entry.getValue()+"'");
        }
        if(userId != null){
            sql.append(" and user_id = '"+userId+"'");
        }
        sql.append(";");
        return sql.toString();
    }


}
