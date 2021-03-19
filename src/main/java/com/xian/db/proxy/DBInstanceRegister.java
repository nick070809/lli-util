package com.xian.db.proxy;

import lombok.Data;

import java.util.List;

/**
 * Description ：多数据源管理
 * Created by  xianguang.skx
 * Since 2020/12/7
 */

@Data
public class DBInstanceRegister {

    private static DBInstanceRegister instanceRegister = new DBInstanceRegister();

    public static DBInstanceRegister getInstance() {
        return instanceRegister;
    }

    //db库信息
    private List<DBInstance> dbList = DBInstanceFactory.mockDBnodes();


    public DBInstance getDBInstance(long routeKey) {
        String dbName = DBTableRoutor.routeDB(routeKey);

        for (DBInstance dbInstance : dbList) {
            if (dbInstance.getDbName().equals(dbName)) {
                return dbInstance;
            }
        }
        System.out.println("prepect db : "+dbName);
        throw new RuntimeException("not found dbinstance");
    }


}
