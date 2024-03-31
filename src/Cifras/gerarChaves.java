package Cifras;

import java.io.Serializable;
import java.math.BigInteger;

public class gerarChaves implements Serializable{
    RSA rsa = new RSA();
    Hmac hmac = new Hmac();
    String hmacKey = "";

    public String getHmacKey() {
        return hmacKey;
    }

    public void setHmacKey(String hmacKey) {
        this.hmacKey = hmacKey;
    }

    public RSA getRsa() {
        return rsa;
    }
    
    public void setRsa(RSA rsa) {
        this.rsa = rsa;
    }



    public gerarChaves(){
        this.rsa.init();
        try {
            this.hmacKey = Hmac.hMac("hmacKey","hmacKey");
        } catch (Exception e) {
            // TODO: handle exception
        }   
    }
    

    public BigInteger getPrivatekeybBigInteger() {
        return rsa.d;
    }

    public BigInteger getPublickeybBigInteger() {
        return rsa.e;
    }

    public BigInteger getmodulebBigInteger() {
        return rsa.n;
    }
}
