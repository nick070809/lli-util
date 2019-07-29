package org.kx.util.common.db;

/**
 * Created by sunkx on 2017/5/17.
 * 无规则
 */
public class UnderlineNameRule implements NameRule {
    @Override
    public String getName(String name) {
        return name;
    }
}
