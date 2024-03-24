package Server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    int porta;
    ServerSocket servidor;

    public Server(int p){
        this.porta = p;
        this.run();
    } 

    @Override
    public void run() {
        try {
            servidor = new ServerSocket(porta);
            System.out.println("Servidor rodando em " +
                    InetAddress.getLocalHost().getHostName() +
                    ":" +
                    servidor.getLocalPort());
            while (true) {
                Socket cliente = servidor.accept();
                ImplServer handler = new ImplServer(cliente);
                Thread t1 = new Thread(handler);
                t1.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
        } 
    }

    public static void main(String[] args) {
        Server serverzinho = new Server(5000);
        serverzinho.run();   
    }
    
}
