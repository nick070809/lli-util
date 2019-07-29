package org.kx.util.generate;

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


}