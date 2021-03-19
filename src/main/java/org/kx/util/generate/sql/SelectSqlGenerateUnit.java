package org.kx.util.generate.sql;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public class SelectSqlGenerateUnit extends AbstractSqlGenerate{

    private String originSql;
    private String lowerSql;
    String[] words;
    String tableName;

    public SelectSqlGenerateUnit(String originSql) {
        this.originSql = originSql;
        this.lowerSql = this.originSql.toLowerCase();
        this.words = parasLine(this.lowerSql, DbChars.kg_, this.originSql);



    }

    @Override
    public String getDmlTyle() {
        return DbOption.select_;
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


    public String[] parasLine(String line, String mask, String sql) {
        String[] t1s = line.split(mask);
        List<String> oi = new ArrayList<>();
        int index = 0;
        for (String word : t1s) {
            if (StringUtils.isNotBlank(word)) {
                String xord = word.trim();
                //todo
                if (index > 0) {
                    if (DbOption.update_.equals(xord) || DbOption.select_.equals(xord) || DbOption.delete_.equals(xord) || DbOption.insert_.equals(xord)) {
                        throw new RuntimeException("sql is error :" + sql);
                    }
                }
                oi.add(getClumnName(xord));
                index++;
            }
        }
        String[] strings = new String[oi.size()];
        oi.toArray(strings);
        return strings;
    }
}
