package Cifras;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    public static String criptografar(String chaveAES, String texto) throws Exception {
        Key chave = new SecretKeySpec(chaveAES.getBytes(), "AES");
        Cipher cifra = Cipher.getInstance("AES");
        cifra.init(Cipher.ENCRYPT_MODE, chave);
        byte[] textoCifrado = cifra.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoCifrado);    
    }

    public static String descriptografar(String chaveAES, String textoCifrado) throws Exception {
        Key chave = new SecretKeySpec(chaveAES.getBytes(), "AES");
        Cipher cifra = Cipher.getInstance("AES");
        cifra.init(Cipher.DECRYPT_MODE, chave);
        byte[] textoDecifrado = cifra.doFinal(Base64.getDecoder().decode(textoCifrado));
        return new String(textoDecifrado);
    }
    
}