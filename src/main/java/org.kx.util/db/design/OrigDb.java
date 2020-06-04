package org.kx.util.db.design;

import com.alibaba.fastjson.JSON;
import org.kx.util.base.DataNotExsitException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/25
 */

public class OrigDb {


    private OrigDb() {
    }

    private static class SingleTonHoler {
        private static OrigDb INSTANCE = new OrigDb();
    }

    public static OrigDb getInstance() {
        return OrigDb.SingleTonHoler.INSTANCE;
    }


    //索引
    private Map<String ,Long> indexs = new HashMap();



    //行列
    private Map<Long ,Map<String ,String>> datas = new HashMap();



    public   int update(String tableName , Long index , String key , String value){

        Map<String ,String>  data = datas.get(index);
        if(data == null){
            throw  new DataNotExsitException(tableName +",index at " +index);
        }
        data.put(key,value);
        return 1;
    }



    public  int insert(String tableName , Long index , String key , String value){

        Map<String ,String>  data = datas.get(index);
        if(data != null){
            throw  new DataNotExsitException(tableName +",duplicate index " +index);
        }
        data = new HashMap<>();
        datas.put(index,data);
        data.put(key,value);
        indexs.put(key,index);
        return 1;
    }


    public  Map<String, String> queryByKey(String indexKey, String key, String value) {

        Long index = indexs.get(indexKey);
        if (index != null) {
            return  datas.get(index);
         }

        Iterator<Map.Entry<Long, Map<String, String>>> entries = datas.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Long, Map<String, String>> entry = entries.next();

            Iterator<Map.Entry<String, String>> lines = entry.getValue().entrySet().iterator();
            while (lines.hasNext()) {
                Map.Entry<String, String> lineEntry = lines.next();
                if (lineEntry.getKey().equals(key) &&  lineEntry.getValue().equals(value)) {
                    index = entry.getKey();
                    return  datas.get(index);
                }


            }
        }
        return  null;
    }


    public static void main(String[] args) {
        OrigDb.getInstance().insert("li_user",1L,"name","skx");
        OrigDb.getInstance().update("li_user",1L,"age","18");
        OrigDb.getInstance().update("li_user",1L,"sex","boy");

        System.out.println(JSON.toJSONString(OrigDb.getInstance().queryByKey("skx_18","name","skx")));
        System.out.println(JSON.toJSONString(OrigDb.getInstance().queryByKey("skx","name","skx")));

    }
}
