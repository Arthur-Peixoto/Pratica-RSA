package Utils;

import java.util.HashMap;
import java.util.Map;

public class Banco {

    private static Banco instance;
    private HashMap<String, Conta> banquinho;

    private Banco() {
        banquinho = new HashMap<>();
        banquinho.put("vasco", new Conta("12345678910", "vasco", "qqqq", "rua de baixo", "987654321"));
        banquinho.put("flamengo", new Conta("12345678910", "flamengo", "qqqq", "rua de baixo", "987654321"));
        banquinho.put("fluminense", new Conta("12345678910", "fluminense", "qqqq", "rua de baixo", "987654321"));
    }

    public Map<String, Conta> getBanquinho() {
        return banquinho;
    }

    public void setBanquinho(Conta conta) {
        banquinho.put(conta.getNome(), conta);
    }

    public Conta getConta(String nome) {
        return banquinho.get(nome);
    }

    public void setConta(Conta conta) {
        banquinho.put(conta.getNome(), conta);
    }

    public static Banco getInstance() {
        if(instance==null) instance = new Banco();
        return instance;
    }
}
