package org.kx.util.generate.sql;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public class DeleteSqlGenerateUnit extends AbstractSqlGenerate{

    private String originSql;
    private String lowerSql;
    String[] words;

    public DeleteSqlGenerateUnit(String originSql) {
        this.originSql = originSql;
        this.lowerSql = this.originSql.toLowerCase();
        this.words = parasLine(this.lowerSql, DbChars.kg_, this.originSql);
    }

    @Override
    public String getDmlTyle() {
        return DbOption.delete_;
    }

    @Override
    public String getOriginSql() {
        return null;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public boolean isShareTable() {
        return false;
    }

    @Override
    public String getBizTableName() {
        return null;
    }

    @Override
    public <T> T getObject(Class<T> instance) {
        return null;
    }
}
