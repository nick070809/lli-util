package com.xian.db.proxy;

import org.junit.Test;

import java.sql.SQLException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/7
 */

public class UserDao  extends  BaseDao{
    String tableName = "x_user" ;

    public  int updateUser(XUser xUser) throws SQLException {

        String realTableName =  routeTable(xUser.getId(),tableName);

        String sql = "update " + realTableName +" set name =" +xUser.getName() +" where id =" + xUser.getId() ;
        return  excuteUpdate(sql, DBInstanceRegister.getInstance().getDBInstance(xUser.getId()));
    }



    @Test
    public void update() throws SQLException {
        XUser xUser = new XUser(120L,"xian.","12343");
        updateUser(xUser);
    }
}
