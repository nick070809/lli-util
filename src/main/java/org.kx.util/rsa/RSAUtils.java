package org.kx.util.rsa;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2019/12/4
 */

public class RSAUtils {
    public static final String PUBLIC_KEY = "PUBLIC_KEY";
    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    private static final Base64.Encoder base64Encoder = Base64.getEncoder();
    private static final Base64.Decoder base64Decoder = Base64.getDecoder();

    private static final String ALGORITHM = "RSA";
    /**
     * 签名算法
     */
    private static final String SIGN_TYPE = "SHA1withRSA";
    /**
     * 密钥长度
     */
    private static final Integer KEY_LENGTH = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成秘钥对，公钥和私钥
     *
     * @return 秘钥键值对
     * @throws Exception 创建秘钥对异常
     */
    public static Map<String, Key> genKeyPair() throws Exception {
        Map<String, Key> keyMap = new HashMap<>();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_LENGTH); // 秘钥字节数
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 公钥加密
     *
     * @param data      加密前数据
     * @param publicKey 公钥
     * @return 加密后数据
     * @throws Exception 加密异常
     */
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        // 加密数据，分段加密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKeyBase64Encoded) throws Exception {
        return encryptByPublicKey(data, parseString2PublicKey(publicKeyBase64Encoded));
    }

    /**
     * 私钥解密
     *
     * @param data       解密前数据
     * @param privateKey 私钥
     * @return 解密后数据
     * @throws Exception 解密异常
     */
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        // 解密数据，分段解密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static byte[] decryptByPrivateKey(byte[] data, String privateKeyBase64Encoded) throws Exception {
        return decryptByPrivateKey(data, parseString2PrivateKey(privateKeyBase64Encoded));
    }

    /**
     * 创建签名
     *
     * @param source     要签名的信息
     * @param privateKey 私钥
     * @return 签名
     * @throws Exception 签名异常
     */
    public static byte[] createSign(String source, PrivateKey privateKey) throws Exception {
        Signature signet = Signature.getInstance(SIGN_TYPE);
        signet.initSign(privateKey);
        signet.update(source.getBytes());
        return signet.sign();
    }

    public static byte[] createSign(String source, String privateKeyBase64Encoded) throws Exception {
        return createSign(source, parseString2PrivateKey(privateKeyBase64Encoded));
    }

    /**
     * 校验签名
     *
     * @param expected  期望信息
     * @param sign      签名
     * @param publicKey 公钥
     * @return 结果
     * @throws Exception 校验异常
     */
    public static boolean checkSign(String expected, byte[] sign, PublicKey publicKey) throws Exception {
        Signature signetCheck = Signature.getInstance(SIGN_TYPE);
        signetCheck.initVerify(publicKey);
        signetCheck.update(expected.getBytes());
        return signetCheck.verify(sign);
    }

    public static boolean checkSign(String expected, byte[] sign, String publicKeyBase64Encoded) throws Exception {
        return checkSign(expected, sign, parseString2PublicKey(publicKeyBase64Encoded));
    }

    /**
     * 将base64格式的公钥转换为对象
     *
     * @param publicKeyBase64Encoded base64的公钥
     * @return 公钥
     * @throws Exception 转换异常
     */
    public static PublicKey parseString2PublicKey(String publicKeyBase64Encoded) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePublic(
                new X509EncodedKeySpec(base64Decoder.decode(publicKeyBase64Encoded)));
    }

    /**
     * 将base64格式的私钥转换为对象
     *
     * @param privateKeyBase64Encoded base64的私钥
     * @return 私钥
     * @throws Exception 转换异常
     */
    public static PrivateKey parseString2PrivateKey(String privateKeyBase64Encoded) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(
                new PKCS8EncodedKeySpec(base64Decoder.decode(privateKeyBase64Encoded)));
    }


    public static void main(String[] args) throws Exception {
        // write your code here

        // 创建密钥对
        Map<String, Key> map = RSAUtils.genKeyPair();

        PublicKey publicKey = (PublicKey) map.get(RSAUtils.PUBLIC_KEY);
        PrivateKey privateKey = (PrivateKey) map.get(RSAUtils.PRIVATE_KEY);

        System.out.println("创建的密钥对：");
        System.out.println("公钥：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("私钥：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        String info = "hello world!";

        System.out.println("原文为：" + info);

        String str = Base64.getEncoder().encodeToString(RSAUtils.encryptByPublicKey(info.getBytes(), publicKey));
        String sign = Base64.getEncoder().encodeToString(RSAUtils.createSign(info, privateKey));

        System.out.println(">>>>>>>>>>>");
        System.out.println("密文为：" + str);
        System.out.println("签名为：" + sign);
        System.out.println(">>>>>>>>>>>");

        String resultInfo = new String(RSAUtils.decryptByPrivateKey(Base64.getDecoder().decode(str), privateKey));
        Boolean resultSign = RSAUtils.checkSign(info, Base64.getDecoder().decode(sign), publicKey);

        System.out.println(String.format("解密结果：%s，签名校验结果：%s", resultInfo, resultSign));

    }


}
