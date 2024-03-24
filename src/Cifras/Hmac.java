package Cifras;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Hmac {
    
    public static final String ALG = "HmacSHA256";

    public static String hMac(String key, String message) throws Exception {

        Mac shaHMAC = Mac.getInstance(ALG);

        SecretKey chaveMAC = new SecretKeySpec(key.getBytes("UTF-8"), ALG);

        shaHMAC.init(chaveMAC);

        byte[] bytesHMAC = shaHMAC.doFinal(message.getBytes("UTF-8"));

        return byte2Hex(bytesHMAC);

    }

    private static String byte2Hex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for(byte b : bytes) {
            sb.append(String.format("%02x",b));
        }
        return sb.toString();

    }

}