package org.kx.util.generate.sql;

import org.kx.util.ddl.IDBSQLFormat;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public class SqlGenerateUtil {


    public static SqlGenerateUnits initSqlGenerateUnits(String sql) {
        sql = sql.trim();
        if (sql.startsWith("\n")) {
            sql = sql.substring(1);
        }
        checkChineseBlank(sql);
        String dmlTyle = sql.substring(0, sql.indexOf(DbChars.kg_)).toLowerCase();
        switch (dmlTyle) {
            case "insert":
                return new InsertSqlGenerateUnit(sql);
            case "update":
                return new UpdateSqlGenerateUnit(sql);
            case "delete":
                return new DeleteSqlGenerateUnit(sql);
            case "select":
                return new SelectSqlGenerateUnit(sql);
            default:
                throw new IllegalStateException("Unexpected dmlTyle: " + dmlTyle);
        }
    }


    public  static  String getDmlTyle(String sql) {
        sql = sql.trim();
        if (sql.startsWith("\n")) {
            sql = sql.substring(1);
        }
        return  sql.substring(0, sql.indexOf(DbChars.kg_)).toLowerCase();
    }



    /**
     * 检测是否有中文空格
     */
    public static void checkChineseBlank(String line) {
        if (line.contains(IDBSQLFormat.CN_)) {
            line = line.replace(IDBSQLFormat.CN_, " [" + IDBSQLFormat.CN_ + "] ");
            throw new RuntimeException("bad sql contains chinese space ; sql is " + line);
        }
    }
 
}
