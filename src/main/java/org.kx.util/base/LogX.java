package org.kx.util.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.kx.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/28
 */

public class LogX {

    public  final static String SP_LOG = "sp";
    public  final static String SOA_LOG = "soa";
    public  final static String SC_LOG = "sc";


    @Deprecated
    public  static  void   log(String info , String app){
        Logger   logger = LoggerFactory.getLogger(app);
        logger.info(info);
    }


    public  static  void   log2(String info2 , String app) {
        try {
            String filePath =  "/Users/xianguang/temp/log/base.log";
            if(SP_LOG.equals(app)){
                filePath ="/Users/xianguang/temp/log/sp.log";
            }else if(SOA_LOG.equals(app)){
                filePath ="/Users/xianguang/temp/log/soa.log";
            }else if(SC_LOG.equals(app)){
                filePath ="/Users/xianguang/temp/log/sc.log";
            }
            FileUtil.appedStringToFile("\n"+info2,filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public  static  void   recordData(String key,String value , String app) throws IOException {
        String file_path = getDataPath(app) ;
        JSONObject ooo = readAllData(file_path);
        ooo.put(key,value);
        FileUtil.writeStringToFile(JSON.toJSONString(ooo), file_path);
        log2("put key : "+ key +" ,value : " + value,app);
    }


    public static String readData(String key, String app) throws IOException {
        String file_path = getDataPath(app) ;
        JSONObject ooo = readAllData(file_path);
        return (String)ooo.get(key);
    }



    public static JSONObject readAllData(String file_path) throws IOException {

        String id_str = FileUtil.readFile(file_path);
        if(id_str.endsWith("\n")){
            id_str = id_str.substring(0,id_str.length()-1) ;
        }

        if(StringUtils.isBlank(id_str)){
            return  new JSONObject();
        }
        return  JSONObject.parseObject(id_str);

    }

    private static  String getDataPath(String app){
        String filePath =  "/Users/xianguang/temp/data/base.log";
        if(SP_LOG.equals(app)){
            filePath ="/Users/xianguang/temp/data/sp.log";
        }else if(SOA_LOG.equals(app)){
            filePath ="/Users/xianguang/temp/data/soa.log";
        }else if(SC_LOG.equals(app)){
            filePath ="/Users/xianguang/temp/data/sc.log";
        }
        return filePath;
    }

    public static void main(String[] args) throws IOException {
        log2("hello l","dsp");
    }
}
