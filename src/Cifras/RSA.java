package Cifras;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    private BigInteger publickey;
    private BigInteger privatekey;
    int bits;
    BigInteger n;

    public RSA(){
        this.init();
        this.printModule();
    }

    public String getPublickey() {
        return new String(publickey.toString());
    }

    public void printModule(){
        System.out.println("modulo: " + n);
    }

    public void init(){
        try {
            //Passo 1: sortear dois números primos aleatórios p e q.
            SecureRandom random = new SecureRandom();
            BigInteger p = BigInteger.probablePrime(1024, random);
		    BigInteger q = BigInteger.probablePrime(1024, random);
            //Passo 2: calcular módulo n.
            n = p.multiply(q);
            //Passo 3: calcular a função totiente ( ou phi) de Euler
            //phi(n) = (p – 1) * (q – 1).
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            //Passo 4: calcular o expoente e (para a chave pública)
            //mdc(e, phi(n)) = 1.
            BigInteger e = BigInteger.probablePrime(bits/2, random);
            //Passo 5: calcular o expoente d (para a chave privada).
            //d*e mod phi(n) = 1. 
            BigInteger d = e.modInverse(phi);
            //Passo 6: as chaves pública e privada são formadas pelo módulo n e seus expoentes e e d.
            privatekey = e; 
            publickey = d;
        } catch (Exception ignored) {
            // TODO: handle exception
        }
    }

    public String encrypt(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        BigInteger messageToBig = new BigInteger(message.getBytes());
        messageToBig = messageToBig.modPow(publickey, n);
        return messageToBig.toString();
    }

    public String encriptografar(String data, String chave, String modulo){
        BigInteger message = new BigInteger(data.getBytes());
		BigInteger chavePublica = new BigInteger(chave);
		BigInteger moduloCripto = new BigInteger(modulo);
		message = message.modPow(chavePublica, moduloCripto);
        return message.toString();
    }

    public String decrypt(String encryptedMessage) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        BigInteger messageToBig = new BigInteger(encryptedMessage);
        byte[] messageToArray = messageToBig.modPow(publickey, n).toByteArray();
		return new String(messageToArray);
    }

    public byte[] decriptografar(String data, String chave, String modulo){
        BigInteger message = new BigInteger(data.getBytes());
		BigInteger chavePrivada = new BigInteger(chave);
		BigInteger moduloCripto = new BigInteger(modulo);
		byte[] messageToByte = message.modPow(chavePrivada, moduloCripto).toByteArray();
        return messageToByte;
    }

    public void printKeys(){
        // System.out.println("Public Key\n"+enconde(publickey.getEncoded()));
        // System.out.println("Private Key\n"+enconde(privatekey.getEncoded()));
    }
}
