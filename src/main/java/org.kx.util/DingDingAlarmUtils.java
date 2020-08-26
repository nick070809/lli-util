package org.kx.util;

import com.alibaba.fastjson.JSON;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/8/11
 */

public class DingDingAlarmUtils {

    public static String sendAlarmMsg(String url, String msg, List<String> atMobiles) {
        Map<String,String> ddMsgMap = new HashMap<String, String>();
        ddMsgMap.put("msgtype", "text");
        Map<String,String> msgMap = new HashMap<String, String>();
        msgMap.put("content", msg);
        ddMsgMap.put("text", JSON.toJSONString(msgMap));
        if(atMobiles != null && !atMobiles.isEmpty()) {
            Map<String,Object> atMap = new HashMap<String, Object>();
            atMap.put("atMobiles", JSON.toJSONString(atMobiles));
            atMap.put("isAtAll", false);
            ddMsgMap.put("at", JSON.toJSONString(atMap));
        }
        String json = JSON.toJSONString(ddMsgMap);
        String encoding = "UTF-8";
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-type", "application/json");
            //bug fixed for: java.security.cert.CertificateException: No subject alternative DNS name matching
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            conn.getOutputStream().write(json.getBytes(encoding));

            int respCode = conn.getResponseCode();
            String resp = null;

            if (HttpsURLConnection.HTTP_OK == respCode) {
                resp = inputStream2String(conn.getInputStream(), encoding);
            } else {
                if(conn.getErrorStream() == null) {
                    return "Possible dingding robot have been closed.";
                }
                resp = inputStream2String(conn.getErrorStream(), encoding);
                throw new RuntimeException(resp);
            }
            return resp;
        } catch(Throwable e) {
            return "send DingDing alarm message is error,"+e.getMessage();
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
    }


    private static String inputStream2String(InputStream is, String encoding) throws IOException {
        if(is == null) {
            return "InputStream is null.";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i=-1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return new String(baos.toByteArray(),encoding);
    }

    public static void main(String[] args) {
        String result = sendAlarmMsg(url, msg, null);
        System.out.println(result);
    }


}
