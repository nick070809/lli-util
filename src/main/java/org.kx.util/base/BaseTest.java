package org.kx.util.base;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/24
 */

public class BaseTest {

    @Test
    public void postRequest() {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> parms = new HashMap<>();
        parms.put("content", "test");

        //headers.put("dataType", "text");
        //headers.put("path", "test");
        HttpClient.sendModel("http://127.0.0.1:10080/util/save", JSON.toJSONString(parms), headers);
    }


    @Test
    public void tt() {
        System.out.println(10 < 1244350009 % 10000 );
    }
}
