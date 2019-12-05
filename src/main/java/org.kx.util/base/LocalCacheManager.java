package org.kx.util.base;

import org.kx.util.rsa.RsaUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2019/12/4
 */

public class LocalCacheManager {

    private static volatile LocalCacheManager instance ;

    private Map<String,Object> LocalCache = new HashMap<>();

    private String  publicKey ;
    private String privateKey;



    private  LocalCacheManager(){}

    public Object  getCache(String key){
        return  LocalCache.get(key);
    }

    public boolean  addCache(String key,String value){
        LocalCache.put(key,value);
        return  true ;
    }

    @Deprecated
    public boolean  addSecurityCache(String key,String value) throws IOException {


        byte[] data = key.getBytes();
        byte[] sv =  RsaUtil.encryptByPrivateKey(data,privateKey) ;

        LocalCache.put(key,sv);
        return  true ;
    }


    public String  getSecurityCache(String key) throws IOException {
        byte[] sv = (byte[]) LocalCache.get(key);
        byte[] data  = RsaUtil.decryptByPublicKey(sv,privateKey) ;
        return  new String(data);


    }




    public boolean  removeCache(String key){
        LocalCache.remove(key);
        return  true ;
    }

    public  static LocalCacheManager  getInstance() throws IOException {
        if (instance == null) {
            synchronized (LocalCacheManager.class) {
                if (instance == null) {
                    instance = new LocalCacheManager();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) throws IOException {

        LocalCacheManager localCacheManager = LocalCacheManager.getInstance();
        localCacheManager.addSecurityCache("sv","998877");
        System.out.println( localCacheManager.getSecurityCache("sv"));



    }

}
