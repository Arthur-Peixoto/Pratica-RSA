package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Map.Entry;

import Cifras.AES;
import Cifras.Hmac;
import Cifras.RSA;
import Cifras.Vernamm;
import Cifras.gerarChaves;
import Utils.Banco;
import Utils.Conta;
import Utils.Mensagem;

public class ImplServer implements Runnable {

    RSA rsa = new RSA();
    String hmacKey =  "hmacKey";
    String vernKey = "chavevernam";
    String aesKey = "gR6@L2#Np8!TzQ7x";

    ServerSocket servidor;
    int porta;
    Socket cliente;
    ObjectInputStream entrada;
    ObjectOutputStream saida;
    boolean condicao = true;
    gerarChaves chaves;
    Banco banquinho = Banco.getInstance();
    BigInteger privateKey;
    BigInteger module;

    public ImplServer(Socket cliente) {
        this.cliente = cliente;
        this.run(); 
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);

        Mensagem retorno = new Mensagem<>(hmacKey, "");

        try {
            System.out.println("Nova conexão com " +
                    cliente.getInetAddress().getHostAddress());
            saida = new ObjectOutputStream(cliente.getOutputStream());
            entrada = new ObjectInputStream(cliente.getInputStream());

            while (condicao) {
                Mensagem mensagem = (Mensagem) entrada.readObject();
                String assinatura = mensagem.getAssinatura();
                    chaves = mensagem.getChaves();
                privateKey = chaves.getPrivatekeybBigInteger();
                module = chaves.getmodulebBigInteger();
                if(assinado(assinatura,privateKey)){
                    System.out.println("Invasor detectado!");
                }
                else{
                String cripto = mensagem.getCriptografada();
                cripto = decodifica(cripto);
                Conta continha = (Conta) mensagem.getMensagem();
                switch (mensagem.getOperacao()) {
                    case 1:
                        System.out.println(cripto);
                        String retornoCripto = "tranferência";
                        retornoCripto = codifica(retornoCripto);
                        retorno.setCriptografada(retornoCripto);
                        saida.writeObject(retorno);
                        saida.flush();
                        break;
                    case 2:
                        System.out.println("saldo\n"+cripto);
                        String retornoSaldo = "Saldo: " + continha.getSaldo();
                        System.out.println(retornoSaldo);
                        String retornoCriptoSaldo = Vernamm.cifrar(retornoSaldo, vernKey);
                        retorno.setCriptografada(retornoCriptoSaldo);
                        saida.writeObject(retorno);
                        saida.flush();
                        break;
                    case 3:
                        System.out.println("investimentos\n"+cripto);
                        String retornoInvestimentos = "Investimentos realizados!";
                        codifica(retornoInvestimentos);
                        retorno.setCriptografada(retornoInvestimentos);
                        saida.writeObject(retorno);
                        saida.flush();
                        break;
                    case 4:
                        System.out.println("saque\n"+cripto);
                        int sald = continha.getSaldo();
                        int saque = scanner.nextInt();
                        String retornoSaque;
                        if (sald < saque) {
                            System.out.println("Você não está tão rico assim");
                            retornoSaque = "Você não está tão rico assim";
                        } else {
                            continha.setSaldo(sald - saque);
                            retornoSaque = "Saque realizado com sucesso!";
                        }
                        retornoSaque = "Saque realizado com sucesso!";
                        retornoSaque = codifica(retornoSaque);
                        
                        retorno.setCriptografada(retornoSaque);
                        saida.writeObject(retorno);
                        saida.flush();
                        break;
                    case 5:
                        System.out.println("deposito\n"+cripto);
                        String retornoDeposito = "Depósito realizado com sucesso!";
                        retornoDeposito = codifica(retornoDeposito);
                        retorno.setCriptografada(retornoDeposito);
                        saida.writeObject(retorno);
                        saida.flush();
                        break;
                    case 6:
                        banquinho.setBanquinho(continha);
                        String retornoCriacaoConta = "Conta criada com sucesso!";
                        retornoCriacaoConta = codifica(retornoCriacaoConta);
                        retorno.setCriptografada(retornoCriacaoConta);
                        saida.writeObject(retorno);
                        saida.flush();
                        break;

                    default:
                        String retornoInvalido = "Operação inválida!";
                        retorno.setCriptografada(retornoInvalido);
                        saida.writeObject(retorno);
                        saida.flush();
                        break;
                }
            }
        }
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        scanner.close();
    }

   public String decodifica(String mensagem){
        try {
            mensagem = AES.descriptografar(aesKey,mensagem);
            mensagem = Vernamm.decifrar(mensagem, vernKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
       

        return mensagem;
    }

    public String codifica(String recebeMensagem) {    
        try {
            recebeMensagem = Vernamm.cifrar(recebeMensagem, vernKey);
            recebeMensagem = AES.criptografar(aesKey,recebeMensagem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recebeMensagem;
    }

    public boolean assinado(String mensagem, BigInteger privateKey){
        try {
            String descripto = rsa.decrypt(mensagem, privateKey, module);
        if(descripto.equals(chaves.getHmacKey())){
            return true;
        }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }


}
