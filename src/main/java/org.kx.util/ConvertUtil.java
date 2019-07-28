package org.kx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * Created by sunkx on 2017/3/27.
 */
public class ConvertUtil {
   // public final ObjectMapper mapper = new ObjectMapper();

    /**
     * json to bean
     * 在使用fastjson时无法将A<T> 装换成目标对象而转换成了A<JsonObject>不是我们想要的
     */
    public static Object JsonToBean(String json){
        return null;
    }

    /**
     * json to map
     */
    public static HashMap<String, Object> JsontoMap(String json) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        JSONObject jsonObject = JSON.parseObject(json);
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        for(Map.Entry<String, Object> entry: set){
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            if(value.startsWith("{")&&value.endsWith("}")){
                data.put(key, JsontoMap(value));
            }else{
                data.put(key, value);
            }
        }
        return data;
    }

    /**
     * map to bean
     */
    public static Object MaptoBean(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        return obj;
    }

    /**
     * order set
     */
    public static Set<String> orderSet(Set<String> set){
        List<String> setList= new ArrayList<String>(set);
        Collections.sort(setList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        set = new LinkedHashSet<String>(setList);
        return set;
    }
    public static Set<String> reservseSet(Set<String> set){
        List<String> setList= new ArrayList<String>(set);
        Collections.sort(setList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.toString().compareTo(o1.toString());
            }
        });
        set = new LinkedHashSet<String>(setList);
        return set;
    }


}
