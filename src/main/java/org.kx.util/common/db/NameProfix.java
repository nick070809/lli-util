package org.kx.util.common.db;

/**
 * Created by sunkx on 2017/5/17.
 */
public class NameProfix {
    private String tableProfix;
    private String filedProfix;

    public NameProfix(String tableProfix, String filedProfix) {
        this.tableProfix = tableProfix;
        this.filedProfix = filedProfix;
    }

    public String getTableProfix() {
        return tableProfix;
    }


    public String getFiledProfix() {
        return filedProfix;
    }


}
