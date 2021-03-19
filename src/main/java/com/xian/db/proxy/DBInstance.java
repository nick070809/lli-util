package com.xian.db.proxy;

import lombok.Data;

import javax.sql.DataSource;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/7
 */

@Data
public class DBInstance {

    private String dbType; //MYSQL ,ORCALE
    private String dbName; //cn_01,cn_02 ,
    private DataSource dataSource;


    public DBInstance(String dbType, String dbName, DataSource dataSource) {
        this.dbType = dbType;
        this.dbName = dbName;
        this.dataSource = dataSource;
    }
}
