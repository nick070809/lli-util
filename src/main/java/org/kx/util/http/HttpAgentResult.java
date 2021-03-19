package org.kx.util.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/3/3
 */

public class HttpAgentResult {

    private int statusCode;
    private String ResponseBodyString;
    private String requestUrlString;

    /**
     * 返回结果如：success=y&user=abc&addr=hangzhou的情况下 将结果分拆一下装入一个Map
     *
     * @return Map
     */
    /**
     * @return
     */
    public Map<String, String> getMapFromString() {
        String[] array = ResponseBodyString.split("&");

        HashMap<String, String> retMap = new HashMap<String, String>();

        if (array != null) {
            for (String string : array) {
                String[] value = string.split("=");
                if ((value != null) & (value.length > 1)) {
                    retMap.put(value[0], value[1]);
                }
            }
        }

        return retMap;
    }

    public String getResponseBodyString() {
        return ResponseBodyString;
    }

    /**
     *
     * @param responseBodyString
     */
    public void setResponseBodyString(String responseBodyString) {
        ResponseBodyString = responseBodyString;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRequestUrlString() {
        return requestUrlString;
    }

    public void setRequestUrlString(String requestUrlString) {
        this.requestUrlString = requestUrlString;
    }


}
