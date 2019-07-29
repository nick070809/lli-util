package org.kx.util.common.db;

import org.kx.util.base.StringUtil;

/**
 * Created by sunkx on 2017/5/17.
 * 驼峰规则
 */
public class CamelNameRule implements NameRule {
    @Override
    public String getName(String name) {
        return StringUtil.Aa_Bb(name);
    }
}
