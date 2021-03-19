package org.kx.util.base.str;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/12
 */

public class JsonParse {


    public String getValue(String line,String key){
        JSONObject jsonObject = JSONObject.parseObject(line);
       return jsonObject.getString(key);
    }


    public String getMutiValue(String lines,String keys){

        String[] lines_ = lines.split("\n");
        String[] keys_ = keys.split(",");
        List<Map> lvs = new ArrayList<>();
        for(String line :lines_){
            JSONObject jsonObject = JSONObject.parseObject(line);
            Map map = new HashMap();
            for(String key :keys_){
                String v = jsonObject.getString(key);
                map.put(key,v);
            }
            lvs.add(map);
        }

     return JSON.toJSONString(lvs);
    }


    @Test
    public void s(){

        String or ="{ }";
// String getMutiValue(String lines,String keys)
        System.out.println(getMutiValue(or,"spConfig"));
     }
}
