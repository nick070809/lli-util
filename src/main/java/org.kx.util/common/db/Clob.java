package org.kx.util.common.db;

import java.io.Serializable;

/**
 * Created by sunkx on 2017/5/17.
 * 补充文字性大数据
 */
public class Clob implements Serializable{
    private  String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
