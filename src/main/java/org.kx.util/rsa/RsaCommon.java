package org.kx.util.rsa;

import com.alibaba.fastjson.JSONObject;
import org.kx.util.FileUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/4
 */

public class RsaCommon {

    private static JSONObject rasMap ;

    public static JSONObject getKeys() {
        try {
            if(rasMap != null){
                return  rasMap;
            }else {
                String filePath = "";
                return getKeys(filePath);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



    public static JSONObject getKeys(String filePath) {
        try {
            if(rasMap != null){
                return  rasMap;
            }else {
                String content = FileUtil.readFile(filePath);
                rasMap =JSONObject.parseObject(content);
                return rasMap;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }



    public static PrivateKey getPrivateKey() throws Exception {
        if(rasMap ==null){
            getKeys();
        }
        String privateKeyStr = (String) rasMap.get("PrivateKey");
        PrivateKey privateKey = RSAUtils.parseString2PrivateKey(privateKeyStr);
        // System.out.println("私钥：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        return privateKey;
    }


    public static PublicKey getPublicKey() throws Exception {
        if(rasMap ==null){
            getKeys();
        }
        String publicKeyStr = (String) rasMap.get("PublicKey");
        PublicKey publicKey = RSAUtils.parseString2PublicKey(publicKeyStr);
        //System.out.println("公钥：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        return publicKey;
    }

    //私钥加密
    public static String encryptByPrivateKey(String info) {
        try {
            String str = Base64.getEncoder().encodeToString(RSAUtils.encryptByPrivateKey(info.getBytes(), getPrivateKey()));
            return str;
        } catch (Exception x) {
            throw new RuntimeException(x);
        }

    }




    //公钥解密
    public static String decryptByPublicKey(String info) {
        try {
            String resultInfo = new String(RSAUtils.decryptByPublicKey(Base64.getDecoder().decode(info), getPublicKey()));
            return resultInfo;
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }


    //====
    //公钥加密
    public static String encryptByPublicKey(String info) {
        try {
            String str = Base64.getEncoder().encodeToString(RSAUtils.encryptByPublicKey(info.getBytes(), getPublicKey()));
            return str;
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    //私钥解密
    public static String decryptByPrivateKey(String info) {
        try {
            String resultInfo = new String(RSAUtils.decryptByPrivateKey(Base64.getDecoder().decode(info), getPrivateKey()));
            return resultInfo;
        } catch (Exception x) {
            throw new RuntimeException(x);
        }

    }


    public static void main(String[] args) {
        String info = "hello world!";
        System.out.println("原文为：" + info);
        String ddd = encryptByPrivateKey(info);
        System.out.println("密文为：" + ddd);
        System.out.println("解密为：" + decryptByPublicKey(ddd));
    }

}

