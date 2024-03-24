package Cliente;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente implements Runnable {
    private Socket cliente;
    InetAddress inet;
    String nome, ip;
    int porta;
    int meuHost;

    String VernKey, HmacKey, AesKey;

    public Cliente( String ip, int porta, int host, String VernKey, String HmacKey, String AesKey) {
        this.ip = ip;
        this.porta = porta;
        this.meuHost = host;
        this.VernKey = VernKey;
        this.AesKey = AesKey;
        this.HmacKey = HmacKey;
    }

    @Override
    public void run() {
        try {
            

            cliente = new Socket(ip, porta);
            System.out.println( "Cliente conectado na porta" + cliente.getPort());
            ImplCliente handlerCliente = new ImplCliente(cliente, VernKey, HmacKey, AesKey);
            Thread t1 = new Thread(handlerCliente);
            t1.start();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        Cliente clientinho = new Cliente("localhost", 5000, 3, "chavevernam", "chavehmac", "gR6@L2#Np8!TzQ7x");
        //Cliente clientinho = new Cliente("localhost", 5001, 0, "chavevernam", "chavehmac", "gR6@L2#Np8!TzQ7x");
        //Cliente clientinho = new Cliente("localhost", 5001, 0, "chavevernam", "chavehmac", "gR6@L2#Np8!TzQ7x");

        clientinho.run();
             
    }

}
