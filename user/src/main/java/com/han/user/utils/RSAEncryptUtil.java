package com.han.user.utils;

import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;

public class RSAEncryptUtil {

    public static final String KEYPAIR = "keyPair";

    public static final java.security.Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();

    public RSAEncryptUtil() {
        java.security.Security.addProvider(provider);
    }

    /**
    * @description: 用给定的keyLength生成密钥对
    * @author: hgm
    * @date: 2021/2/4 11:53
    * @param keyLength: 
    * @return: java.security.KeyPair
    */
    public static KeyPair generateKeypair(int keyLength) throws Exception {
        KeyPairGenerator keyPairGenerator;

        try{
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (Exception e){
            keyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);
        }

        keyPairGenerator.initialize(keyLength);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
    * @description: 解密
    * @author: hgm
    * @date: 2021/2/4 13:54
    * @param encrypted:
     * @param keyPair:
    * @return: java.lang.String
    */
    public static String decrypt(String encrypted, KeyPair keyPair) throws Exception {
        Cipher cipher;
        try{
            cipher = Cipher.getInstance("RSA/NONE/NoPadding");
        }catch (Exception e){
            cipher = Cipher.getInstance("RSA/NONE/NoPadding", provider);
        }

        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

        String[] blocks = encrypted.split("\\s");
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < blocks.length; i++) {
            byte[] data = hexStringToByteArray(blocks[i]);
            byte[] decrytedBlock = cipher.doFinal(data);
            result.append(new String(decrytedBlock));
        }

        return result.reverse().toString().substring(2);
    }

    /**
     * Return public RSA key modulus
     *
     * @param keyPair
     *            RSA keys
     * @return modulus value as hex string
     */
    public static String getPublicKeyModulus(KeyPair keyPair) {
        java.security.interfaces.RSAPublicKey publicKey = (java.security.interfaces.RSAPublicKey) keyPair.getPublic();
        return publicKey.getModulus().toString(16);
    }

    /**
     * Return public RSA key exponent
     *
     * @param keyPair
     *            RSA keys
     * @return public exponent value as hex string
     */
    public static String getPublicKeyExponent(KeyPair keyPair) {
        java.security.interfaces.RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getPublicExponent().toString(16);
    }

    /**
     * Max block size with given key length
     *
     * @param keyLength
     *            length of key
     * @return numeber of digits
     */
    public static int getMaxDigits(int keyLength) {
        return ((keyLength * 2) / 16) + 3;
    }

    /**
     * Convert hex string to byte array
     *
     * @param data
     *            input string data
     * @return bytes
     */
    public static byte[] hexStringToByteArray(String data) {
        int k = 0;
        byte[] results = new byte[data.length() / 2];
        for (int i = 0; i < data.length();) {
            results[k] = (byte) (Character.digit(data.charAt(i++), 16) << 4);
            results[k] += (byte) (Character.digit(data.charAt(i++), 16));
            k++;
        }
        return results;
    }

    public static Object genKeyPair(String uuid) throws Exception {
        KeyPair keyPair = generateKeypair(512);

        String e = getPublicKeyExponent(keyPair);
        String n = getPublicKeyModulus(keyPair);
        String md = String.valueOf(getMaxDigits(512));

        JSONObject json = new JSONObject();
        json.put("e", e);
        json.put("n", n);
        json.put("maxdigits",md);

        String data = JSONObject.toJSONString(keyPair);
        RedisUtil.setStrValue(KEYPAIR+":"+uuid, data, 3);
        return json;
    }

}
