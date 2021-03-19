package org.kx.util.base.str;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/12
 */

public class SqlParse {

    public JSONObject parseInsertLine(String insertSql){

        return  null;
    }

    public JSONArray parseXlsLine(String content){


        return  null;
    }

    public  static JSONArray parseTable(String content_,String title_){
        String[] columns = content_.split("\t");
        String[] titles = "id\tgmt_create\tgmt_modified\tbiz_type\tmodule_code\tmodule_desc\tmodule_plugin\tmodule_config\textend_info\tstart_time\tend_time\town_sign\tstatus\tis_delete\tdb_remark\tgmt_greate_nick\tgmt_modified_nick\tbiz_config_id\tcountry".split("\t");
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(jsonObject);
        int t = 0;
        for(int i =0;i<columns.length;i++){
            if((i+1) % titles.length ==0 ){
                t = 0;
                jsonObject = new JSONObject();
                array.add(jsonObject);
            }
            jsonObject.put(titles[t],columns[i]);
            t ++ ;
        }

        return array ;

    }


    @Test
    public void xxx(){

    }
}
