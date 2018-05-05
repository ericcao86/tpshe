package com.cmos.tpshe.wh.task.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author cyh
 * @Date 10:19 2018/4/23
 * @description
 * @since 2.0
 */
public class PBESecret {

    /**
     * 定义加密方式
     * 支持以下任意一种算法
     * <p/>
     * <pre>
     * PBEWithMD5AndDES
     * PBEWithMD5AndTripleDES
     * PBEWithSHA1AndDESede
     * PBEWithSHA1AndRC2_40
     * </pre>
     */
    private final static String KEY_PBE = "PBEWITHMD5andDES";

    private final static int SALT_COUNT = 100;

    private static final Logger logger = LoggerFactory.getLogger(PBESecret.class);

    /**
     * 初始化盐（salt）
     *
     * @return
     */
    public static byte[] initSalt() {
        byte[] salt =  new byte[]{1,2,3,4,6,7,8,9};
        return salt;
    }

    /**
     * 转换密钥
     *
     * @param key 字符串
     * @return
     */
    public static Key stringToKey(String key) {
        SecretKey secretKey = null;
        try {
            PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_PBE);
            secretKey = factory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException e) {
            logger.error("stringToKey", "数据转换异常", e);
        } catch (InvalidKeySpecException e) {
            logger.error("stringToKey", "数据转换异常", e);
        }
        return secretKey;
    }

    /**
     * PBE 加密
     *
     * @param data 需要加密的字节数组
     * @param key  密钥
     * @param salt 盐
     * @return
     */
    public static byte[] encryptPBE(byte[] data, String key, byte[] salt) {
        byte[] bytes = null;
        try {
            // 获取密钥
            Key k = stringToKey(key);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, SALT_COUNT);
            Cipher cipher = Cipher.getInstance(KEY_PBE);
            cipher.init(Cipher.ENCRYPT_MODE, k, parameterSpec);
            bytes = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            logger.error("encryptPBE", "数据加密异常", e);
        } catch (NoSuchPaddingException e) {
            logger.error("encryptPBE", "数据加密异常", e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("encryptPBE", "数据加密异常", e);
        } catch (InvalidKeyException e) {
            logger.error("encryptPBE", "数据加密异常", e);
        } catch (BadPaddingException e) {
            logger.error("encryptPBE", "数据加密异常", e);
        } catch (IllegalBlockSizeException e) {
            logger.error("encryptPBE", "数据加密异常", e);
        }
        return bytes;
    }

    /**
     * PBE 解密
     *
     * @param data 需要解密的字节数组
     * @param key  密钥
     * @param salt 盐
     * @return
     */
    public static byte[] decryptPBE(byte[] data, String key, byte[] salt) {
        byte[] bytes = null;
        try {
            // 获取密钥
            Key k = stringToKey(key);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, SALT_COUNT);
            Cipher cipher = Cipher.getInstance(KEY_PBE);
            cipher.init(Cipher.DECRYPT_MODE, k, parameterSpec);
            bytes = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            logger.error("decryptPBE", "数据解密异常", e);
        } catch (NoSuchPaddingException e) {
            logger.error("decryptPBE", "数据解密异常", e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("decryptPBE", "数据解密异常", e);
        } catch (InvalidKeyException e) {
            logger.error("decryptPBE", "数据解密异常", e);
        } catch (BadPaddingException e) {
            logger.error("decryptPBE", "数据解密异常", e);
        } catch (IllegalBlockSizeException e) {
            logger.error("decryptPBE", "数据解密异常", e);
        }
        return bytes;
    }

    /**
     * BASE64 加密
     *
     * @param key 需要加密的字节数组
     * @return 字符串
     */
    public static String encryptBase64(byte[] key) {
        return Base64.encodeBase64URLSafeString(key);
    }

    /**
     * BASE64  解密
     *
     * @param base64String 需要解密的字符串
     * @return 字节数组
     */
    public static byte[] decryptBase64(String base64String) {
        return Base64.decodeBase64(base64String);
    }
}
