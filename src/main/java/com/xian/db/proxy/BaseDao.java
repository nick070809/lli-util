package com.xian.db.proxy;

import java.sql.SQLException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/7
 */

public class BaseDao {

    public String routeTable(long inputNumber, String tableName) {
      return    DBTableRoutor.routeTable(inputNumber, tableName);
    }

    protected  int excuteUpdate(String sql ,DBInstance dbInstance) throws SQLException {
        System.out.println("sql:" +sql);
        System.out.printf("db_name : " + dbInstance.getDbName());
       /* Connection conn = dbInstance.getDataSource().getConnection() ;
        //conn.setAutoCommit(false);
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.execute();*/
        return   1 ;
    }
}
