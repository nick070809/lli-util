package org.kx.util.base;

import com.lianlianpay.lli.common.BaseErrorCode;
import com.lianlianpay.lli.common.BaseException;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;


/**
 * Created by sunkx on 2017/4/23.
 */

public class Coder {

    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";
    private final static Logger log = Logger.getLogger(Coder.class);

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * MD5加密
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        try {
            MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
            md5.update(data);
            return md5.digest();
        } catch (Exception e) {
            log.error("MD5加密异常," + e.getMessage());
            throw new BaseException(BaseErrorCode.SystemError, e.getMessage());
        }
    }

    /**
     * SHA加密
     */
    public static byte[] encryptSHA(byte[] data) {
        try {
            MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
            sha.update(data);
            return sha.digest();
        } catch (Exception e) {
            log.error("SHA加密异常," + e.getMessage());
            throw new BaseException(BaseErrorCode.SystemError, e.getMessage());
        }
    }
}
