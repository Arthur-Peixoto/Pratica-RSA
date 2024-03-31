package Cifras;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Hmac implements Serializable {
    
    public static final String ALG = "HmacSHA256";

    public static String hMac(String key, String message) throws Exception {

        Mac shaHMAC = Mac.getInstance(ALG);

        SecretKey chaveMAC = new SecretKeySpec(key.getBytes("UTF-8"), ALG);

        shaHMAC.init(chaveMAC);

        byte[] bytesHMAC = shaHMAC.doFinal(message.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(bytesHMAC);

    }

}