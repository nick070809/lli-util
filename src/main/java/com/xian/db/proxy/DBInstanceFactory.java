package com.xian.db.proxy;

import com.alibaba.druid.pool.DruidDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/7
 */

public class DBInstanceFactory {

    //25个库
    public  final static int dbSize = 25 ;

    public  static  List<DBInstance> mockDBnodes() {
        List<DBInstance> dbList = new ArrayList<>();
        for (int i = 0; i < dbSize; i++) {
            DruidDataSource ds = new DruidDataSource();
            ds.setUsername("root");
            ds.setPassword("123456");
            ds.setUrl("jdbc:mysql://localhost:3306/sampledb?useUnicode=true&characterEncoding=utf-8");
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            dbList.add(new DBInstance("MYSQL","db_"+i,ds));
        }
        return dbList;
    }

}
