package org.kx.util.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Splitter;
import org.kx.util.base.str.StringUtil;
import org.kx.util.config.SysConf;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/6
 */

public class URLUtil {

    /**
     * url解码 UTF-8 /gb2312
     */
    public static String URLDecoderString(String str) {

        return URLDecoderString(str,null);
    }

    /**
     * url解码 UTF-8 /gb2312
     */
    public static String URLDecoderString(String str,String enc) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            if(enc == null){
                enc = "UTF-8";
            }
            result = java.net.URLDecoder.decode(str, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }



    public static String getParam(String url, String name) {
        Map<String, String> split = getParams(url);
        return split.get(name);
    }


    public static Map<String, String> getParams(String url) {
        String params = url.substring(url.indexOf("?") + 1, url.length());
         return Splitter.on("&").withKeyValueSeparator("=").split(params);
    }


    public  static  String getLhString(String urlStr){
        Map<String, String> params = URLUtil.getParams(urlStr);
        JSONObject jsonParams = new JSONObject();

        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String  key = entry.getKey();
            String  value = URLUtil.URLDecoderString(entry.getValue()) ;
            if(key.equals(SysConf.dc)){
                jsonParams.put(key, StringUtil.toJSONArray(value)) ;
            }else {
                jsonParams.put(key,value) ;
            }
        }

        return JSON.toJSONString(jsonParams, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);

    }


    public  static  String getLhUrl(String urlStr,String enc){
        Map<String, String> params = URLUtil.getParams(urlStr);
        String title = urlStr.substring(0, urlStr.indexOf("?") );

        JSONObject jsonParams = new JSONObject();

        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String  key = entry.getKey();
            String  value = URLUtil.URLDecoderString(entry.getValue(),enc) ;
            if(key.equals(SysConf.dc)){
                jsonParams.put(key,StringUtil.toJSONArray(value)) ;
            }else {
                jsonParams.put(key,value) ;
            }
        }
        jsonParams.put("_hosts_",title);
        return JSON.toJSONString(jsonParams, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);

    }

}
