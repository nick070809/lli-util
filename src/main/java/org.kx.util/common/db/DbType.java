package org.kx.util.common.db;

/**
 * Created by sunkx on 2017/5/17.
 */
public enum  DbType {
    ORACLE(1),MYSQL(2),SQLSERVER(3)
    ;
    private int code;
    private DbType(int code) {
        this.code = code;
    }

    public static DbType getDbType(int code) {
        for (DbType type : DbType.values()) {
            if (type.getCode() == code ) {
                return type;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
