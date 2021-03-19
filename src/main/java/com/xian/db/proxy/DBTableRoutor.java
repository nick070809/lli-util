package com.xian.db.proxy;

/**
 * Description ：数据库，表 路由
 * Created by  xianguang.skx
 * Since 2020/12/7
 */

public class DBTableRoutor {


    /**
     * 表路由
     * @param routeKey
     * @param tableName
     */
    public static String routeTable(long routeKey, String tableName) {
       return tableName + "_" +  tableByUserId(routeKey);
    }


    public static String routeDB(long routeKey){
        return dbByUserId(routeKey);
    }


    /**
     * 取数字后4位
     */
    public static String tableByUserId(long userId ) {
            return String.format("%04d", Math.abs(userId) % 1000);
    }

    /**
     * 取数字后4位
     */
    public static String dbByUserId(long userId ) {
        Long tableId = Math.abs(userId) % 1000 ;
        return  "db_" + tableId /(1000/ DBInstanceFactory.dbSize);
    }

    public static void main(String[] args) {
        System.out.println(102/(1000/ DBInstanceFactory.dbSize));
    }
}
