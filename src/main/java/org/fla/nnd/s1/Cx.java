package org.fla.nnd.s1;

import org.kx.util.rsa.RsaCommon;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/4
 */

public class Cx {
    public static String show(String encryptStr){
        String info =  RsaCommon.decryptByPublicKey(encryptStr);
        //System.out.println("明文为："+info);
        return  info ;
    }

    public static String encrypt(String info){
        return RsaCommon.encryptByPrivateKey(info);
    }


    public static  void encryptAndShow(String info){
        String encryptStr = RsaCommon.encryptByPrivateKey(info);
        //System.out.println("密文为："+encryptStr);
        System.out.println("明文为："+RsaCommon.decryptByPublicKey(encryptStr));
    }
}
