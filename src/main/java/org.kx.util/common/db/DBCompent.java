package org.kx.util.common.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by sunkx on 2017/3/25.
 * http://blog.csdn.net/yunnysunny/article/details/8657095/
 */

public abstract class DBCompent {

    public DbType getDataBaseType(Connection connection) throws SQLException {
        String driverName = connection.getMetaData().getDriverName().toUpperCase();

        //通过driverName是否包含关键字判断
        if (driverName.indexOf(DbType.MYSQL.toString()) != -1) {
            return DbType.MYSQL;
        } else if (driverName.indexOf(DbType.ORACLE.toString()) != -1) {
            return DbType.ORACLE;
        } else if (driverName.indexOf(DbType.SQLSERVER.toString()) != -1) {
            return DbType.SQLSERVER;
        }
        return null;
    }

    public ModelWapper getModelWapper(DbType dbType, Class model, NameRule nameRule, NameProfix nameProfix) {
        ModelWapper modelWapper = null;
        modelWapper = new ModelWapper(model, nameRule, nameProfix, dbType);
        modelWapper.getTableName();
        modelWapper.getModelFileds();

        return modelWapper;
    }


}
