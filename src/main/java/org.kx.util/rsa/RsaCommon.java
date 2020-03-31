package org.kx.util.rsa;

import com.alibaba.fastjson.JSONObject;
import org.kx.util.FileUtil;

import java.io.File;
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
                String filePath = "/Users/xianguang/temp/ho/default.txt";
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
                File flied = new File(filePath);
                if(flied.exists()){
                    String content = FileUtil.readFile(filePath);
                    rasMap =JSONObject.parseObject(content);
                    return rasMap;
                }
                rasMap = new JSONObject();
                rasMap.put("PrivateKey","MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKfr7l5mv6RSOMhusUC4L6pg08GoIad3Vy7McZZlgmbqBtJL9gUXJO5D3V25jEsqGVGaptuO7OpT7oAZte1R10CwP+j8i67DadYbQr3w+kSs1NyXsCyLZbrWRUtR04Ep7Tzqmcas2P2hPH4BQ+7zeEv/RlsTIi3ntQztpBT81xQxAgMBAAECgYBAATm2WcuqQnzKSQbe+FWSx51vLzrErkRY3ixdewudVo6LEdEQV6YH+24hD1xeOgm0hRIclfnPx3Yc9a/WaV919PTQnUHrDRhC2+/d4ol10NL05VE/VvDDAy/VrULu9hTYtZ2k7UBWiAvxCsf8gPO4tzd3WkDnODwGadnTKFgWwQJBAPAcYYvPnerG6zxO/80OR8yKrh/PL/RSDQakDZiIb88LF4btn1rNt5J50EeQJEjDZ4r/WwnH3DdbzEi93XwwoSUCQQCzCJ8nad9BTEzXdqexUAwvHGp6mnjrW2V1lJtCFHaOSlI10YNR03wVDEpfOH4fqCBlhKCBdYXxaXVh9KLMqJcdAkANWra+LOzuiuO4dfhaMkoiATCQCljzcMDStrse2a/GRyqm0X6EcV6dYfMIl+a8uUl//JXWpGwGFC/3d7/i0V2VAkBTug98RjYDeqRhC3unH7FjAt4FEmLEZszPLT1irOE48Cb8Rkwso0PfMA1D9M5/DYiBEYJiqMyINvKPafIM1jc9AkAol6lMaYj1/F59LC9jK+iZHJQNECR2CrH4Q2A7DUFFS34iVwHxPPlN+0PDFOEwJN5SP/KAJBsqEbKZgv+JqgYa");
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

