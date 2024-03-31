package Cifras;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA implements Serializable{
    BigInteger e;
    BigInteger d;
    BigInteger n;

    public RSA() {
        this.init();
    }

    public BigInteger getPrivatekeybBigInteger() {
        return d;
    }

    public BigInteger getPublickeybBigInteger() {
        return e;
    }

    public BigInteger getmodulebBigInteger() {
        return n;
    }

    public String getModule() {
        return new String(n.toString());
    }

    public void init() {
        try {
            // Passo 1: sortear dois números primos aleatórios p e q.
            SecureRandom sr = new SecureRandom();
            BigInteger p = BigInteger.probablePrime(1024, sr);
            BigInteger q = BigInteger.probablePrime(1024, sr);
            // Passo 2: calcular módulo n.
            n = p.multiply(q);
            // Passo 3: calcular a função totiente ( ou phi) de Euler
            // phi(n) = (p – 1) * (q – 1).
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            // Passo 4: calcular o expoente e (para a chave pública)
            // mdc(e, phi(n)) = 1.
            e = BigInteger.probablePrime(512, sr);
            // Passo 5: calcular o expoente d (para a chave privada).
            // d*e mod phi(n) = 1.
            d = e.modInverse(phi);
            // Passo 6: as chaves pública e privada são formadas pelo módulo n e seus
            // expoentes e e d.
        } catch (Exception ignored) {
            // TODO: handle exception
        }
    }

    
    public String encriptografar(String data, BigInteger eExpoente, BigInteger modulo) {
        BigInteger message = new BigInteger(data.getBytes());
        message = message.modPow(eExpoente, modulo); //C = M^e mod n
        return message.toString();
    }

    public String decriptografar(String data, BigInteger dExpoente, BigInteger modulo) throws NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        BigInteger message = new BigInteger(data.getBytes());
        byte[] messageToByte = message.modPow(dExpoente, modulo).toByteArray();//M = C^d mod n.
        return new String(messageToByte);
    }

    public void printKeys() {
        System.out.println("Public Key\n{" + e+"}\n{"+n+"}");
        System.out.println("Private Key\n" + d+"}\n{"+n+"}");
    }

    public String getPublickey() {
        return new String(e.toString());
    }

    public String getPrivatekey() {
        return new String(d.toString());
    }

    public String encrypt(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        BigInteger messageToBig = new BigInteger(message.getBytes());
        messageToBig = messageToBig.modPow(e, n);
        return messageToBig.toString();
    }

    public String decrypt(String encryptedMessage,BigInteger dExpoente, BigInteger modulo) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        BigInteger messageToBig = new BigInteger(encryptedMessage);
        byte[] messageToArray = messageToBig.modPow(d, n).toByteArray(); 
        return new String(messageToArray);
    }


    public static void main(String[] args) {
        RSA rsazinho = new RSA();
        rsazinho.init();
        rsazinho.printKeys();
        String msg = "HmacKey";
        BigInteger publiczinho = rsazinho.getPublickeybBigInteger();
        BigInteger modulozinho = rsazinho.getmodulebBigInteger();
        BigInteger privadazinha = rsazinho.getPrivatekeybBigInteger();
        try {
            // String cifrada = rsazinho.encrypt(msg);
            
            String cifrada = rsazinho.encriptografar(msg, publiczinho, modulozinho);
            System.out.println("Mensagem cifrada: " + cifrada);
            byte[] bytesMensagemCifrada = cifrada.getBytes();
            System.out.println("Bytes da mensagem cifrada: " +
                    Arrays.toString(bytesMensagemCifrada));
            String decifrada = rsazinho.decrypt(cifrada, privadazinha, modulozinho);
            //String decifrada = rsazinho.decriptografar(cifrada, privadazinha, modulozinho);
            System.out.println("Mensagem decifrada: " + decifrada);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
