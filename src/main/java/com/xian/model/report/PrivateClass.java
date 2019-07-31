package com.xian.model.report;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/30
 */

public class PrivateClass {
    private String id = "127.0.0.1";
    private String port = "8080";

    private String getUrl() {
        return id + ":" + port;
    }
    private String getUrl(String sx) {
        return id + ":" + port+"---"+sx;
    }
    private String getUrl(String sx,Integer xx) {
        return id + ":" + port+"---"+xx;
    }

    private String seturl() {
        return id + ":" + port;
    }
    private String seturl(String sx) {
        return id + ":" + port+"---"+sx;
    }
}