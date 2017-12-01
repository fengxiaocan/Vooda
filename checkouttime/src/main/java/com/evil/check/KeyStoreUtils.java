package com.evil.check;

import android.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * The type Key store utils.
 *
 * @author Noah.冯 QQ:1066537317
 * @name： FingerprintLoader
 * @package： com.evil.fingerprintloader
 * @time 2017 /8/9 0009
 * @desc： 一个加密相关工具类
 * @since 1.1
 */
final class KeyStoreUtils {
    private KeyStoreUtils() {
    }


    /**
     * 用公钥解密
     *
     * @param data      加密数据
     * @param publicKey 密钥
     *
     * @return string
     *
     * @throws Exception the exception
     */
    public static byte[] decryptByPublicKey(String data, byte[] publicKey) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key key = keyFactory.generatePublic(x509EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] dataBytes = Base64.decode(data.getBytes(), Base64.DEFAULT);
        byte[] resultBytes = cipher.doFinal(dataBytes);
        return resultBytes;
    }

    /**
     * 用公钥解密
     *
     * @param data      加密数据
     * @param publicKey 密钥
     *
     * @return string
     *
     * @throws Exception the exception
     */
    public static String decryptByPublicKey(String data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey.getBytes(), Base64.DEFAULT);
        byte[] resultBytes = decryptByPublicKey(data, keyBytes);
        return new String(resultBytes, "UTF-8");
    }
}
