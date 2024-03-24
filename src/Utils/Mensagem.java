package Utils;
import java.io.Serializable;

public class Mensagem<T> implements Serializable{
    
    private T mensagem;
    private String Hmac, criptografada;
    
    private int id, operacao;
    
    public Mensagem(T mensagem, String hmac,String cripto, int operacao) {
        this.mensagem = mensagem;
        Hmac = hmac;
        this.criptografada = cripto;
        //this.id = id;
        this.operacao = operacao;
    }

    public Mensagem(String hmac, String cripto){
        this.Hmac = hmac;
        this.criptografada = cripto;
    }

    public int getOperacao() {
        return operacao;
    }

    public void setOperacao(int operacao) {
        this.operacao = operacao;
    }

    public T getMensagem() {
        return mensagem;
    }
    public void setMensagem(T mensagem) {
        this.mensagem = mensagem;
    }
    public String getHmac() {
        return Hmac;
    }
    public void setHmac(String hmac) {
        Hmac = hmac;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCriptografada() {
        return criptografada;
    }

    public void setCriptografada(String criptografada) {
        this.criptografada = criptografada;
    }

    

}
