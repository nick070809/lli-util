package org.kx.util.rsa;

import org.apache.commons.codec.binary.Base64;
import org.kx.util.config.SysConf;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/16
 */

public class DESUtil {
    static Cipher cipher = null;
    static Key convertSecretKey = null;
    static {
        try {
            String ss = SysConf.bj3des;
            byte[] bytesKey = ss.getBytes("UTF-8");
            //KEY转换
            DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            convertSecretKey = factory.generateSecret(desKeySpec);
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //加密
    public static String encrypt(String src) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
            byte[] result = cipher.doFinal(src.getBytes());
            return  org.apache.commons.codec.binary.Base64.encodeBase64String(result) ;
        } catch (Exception x) {
            throw new RuntimeException(src,x);
        }
    }

    //解密
    public static String decrypt(String sec) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
            byte[] sresult = cipher.doFinal(Base64.decodeBase64(sec) );
            return  new String(sresult);
        } catch (Exception x) {

            throw new RuntimeException(sec,x);
        }
    }
}
