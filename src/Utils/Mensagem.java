package Utils;
import java.io.Serializable;

import Cifras.gerarChaves;

public class Mensagem<T> implements Serializable{
    
    private T mensagem;
    private String Assinatura, criptografada;
    private gerarChaves chaves;
    public gerarChaves getChaves() {
        return chaves;
    }

    public void setChaves(gerarChaves chaves) {
        this.chaves = chaves;
    }

    private int id, operacao;
    
    public Mensagem(T mensagem, String assinatura,String cripto, int operacao, gerarChaves chaves) {
        this.mensagem = mensagem;
        Assinatura = assinatura;
        this.criptografada = cripto;
        //this.id = id;
        this.operacao = operacao;
        this.chaves = chaves;
    }

    public Mensagem(String assinatura, String cripto){
        this.Assinatura = assinatura;
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
    public String getAssinatura() {
        return Assinatura;
    }
    public void setAssinatura(String assinatura) {
        this.Assinatura = assinatura;
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
