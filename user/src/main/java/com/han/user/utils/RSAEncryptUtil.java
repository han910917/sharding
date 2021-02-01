package com.han.user.utils;

import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

public class RSAEncryptUtil {

    /**
    * @description: 生成密匙对
    * @author: hgm
    * @date: 2021/2/1 11:48
    * @return: java.util.Map<java.lang.String,java.lang.Object>
    */
    public static Map<String, Object> genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());

        Map<String, Object> map = Maps.newHashMap();
        map.put("publicKey", publicKeyStr);
        map.put("privateKey", privateKeyStr);

        ServletRequestUtil.getRequest().getSession().setAttribute("publicKey", publicKeyStr);
        ServletRequestUtil.getRequest().getSession().setAttribute("privateKey", privateKey);
        return map;
    }

    /**
    * @description: 解密
    * @author: hgm
    * @date: 2021/2/1 11:53
    * @return: java.lang.String
    */
    public static  String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));

        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);

        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        return new String(cipher.doFinal(inputByte));
    }

    /**
    * @description: 加密
    * @author: hgm
    * @date: 2021/2/1 11:53
    * @return: java.lang.String
    */
    public static String encrypt(String str, String publicKey) throws Exception {
        byte[] bytes = Base64.decodeBase64(publicKey);

        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);

        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }


}
