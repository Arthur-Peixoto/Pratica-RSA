package Cliente;

import Utils.Banco;
import Utils.Conta;
import Utils.Mensagem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map.Entry;

import Cifras.AES;
import Cifras.Hmac;
import Cifras.Vernamm;

import java.util.Scanner;

public class ImplCliente implements Runnable {
    Socket cliente;
    boolean conexao = true;

    int contador = 0;
    int opcao = 2;
    String VernKey, HmacKey, AesKey;
    ObjectInputStream entrada;
    ObjectOutputStream saida;
    AES aes = new AES();
    String retorno;

    public ImplCliente(Socket cliente, String vernKey, String hmacKey, String aesKey) {
        this.cliente = cliente;
        VernKey = vernKey;
        HmacKey = hmacKey;
        AesKey = aesKey;
    }

    @Override
    public void run() {

        Conta conta = null;
        

        contador++;

        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                saida = new ObjectOutputStream(cliente.getOutputStream());
                entrada = new ObjectInputStream(cliente.getInputStream());

                while (opcao != 1) {

                    Banco banquinho = Banco.getInstance();

                    System.out
                            .println("Cliente \n :Escolha uma opção de mensagem\n1-Logar\n2-Cadastrar");
                    opcao = scanner.nextInt();
                    scanner.nextLine();
                    switch (opcao) {
                        case 1:
                            String nome1 = " ", senha1 = " ";
                            boolean isConected = false;
                            System.out.println("Digite seu Nome: ");
                            nome1 = scanner.nextLine();

                            System.out.println("Digite sua senha: ");
                            senha1 = scanner.nextLine();

                            for (Entry<String, Conta> entry : banquinho.getBanquinho().entrySet()) {
                                String chave = entry.getKey();
                                Conta continha = entry.getValue();
                                conta = continha;
                                if (chave.equals(nome1)) {
                                    if(senha1.equals(continha.getSenha()))
                                    isConected = true;
                                    break;
                                }
                            }
                            while (isConected) {

                                System.out.println("1-Tranferência: ");
                                System.out.println("2-Pegar saldo: ");
                                System.out.println("3-Investimento: ");
                                System.out.println("4-Saque: ");
                                System.out.println("5-Depositar: ");
                                int opcao = scanner.nextInt();

                                switch (opcao) {
                                    case 1:
                                        System.out.println("tranferência");
                                        scanner.nextLine();
                                        System.out.println("pra quem você quer transferir?");
                                        String nome = scanner.nextLine();
                                        System.out.println("qual valor você quer transferir?");
                                        int valor = scanner.nextInt();
                                        for (Entry<String, Conta> entry : banquinho.getBanquinho().entrySet()) {
                                            String chave = entry.getKey();
                                            Conta continha = entry.getValue();
                                            if (chave.equals(nome)) {
                                                conta.setSaldo(valor - conta.getSaldo());
                                                continha.setSaldo(valor + continha.getSaldo());
                                                break;
                                            }
                                            int saldoDaConta = conta.getSaldo();
                                            conta.setSaldo(saldoDaConta + valor);

                                            retorno = conta.getNome() + "-" + continha.getNome();
                                        }
                                        String retornoCripto = codifica(retorno);
                                        Mensagem mensagemzinha = new Mensagem<Conta>(conta, HmacKey, retornoCripto, 1);

                                        saida.writeObject(mensagemzinha);
                                        saida.flush();
                                        break;
                                    case 2:
                                        String retornoSaldo = "Saldo: " + conta.getSaldo();
                                        System.out.println(retornoSaldo);
                                        retornoSaldo = codifica(retornoSaldo);
                                        Mensagem mensagemzinha1 = new Mensagem<Conta>(conta, HmacKey, retornoSaldo, 2);

                                        saida.writeObject(mensagemzinha1);
                                        saida.flush();
                                        break;
                                    case 3:
                                        System.out.println("investimentos");
                                        int saldo = conta.getSaldo();
                                        System.out.println(
                                                "na poupança ele renderá:\nEm 3 meses: " + ((saldo * 0.5) * 3 + saldo)
                                                        + "Reais\nEm 6 meses: " + ((saldo * 0.5) * 6 + saldo)
                                                        + "Reais\nEm 12 meses: " + ((saldo * 0.5) * 12 + saldo));
                                        System.out.println("Deseja investir na renda fixa? (SIM(S) / NAO(N))");
                                        scanner.nextLine();
                                        String resp = scanner.nextLine();
                                        if (resp.equals("S")) {
                                            System.out.println("Quanto deseja investir?");
                                            int invest = scanner.nextInt();
                                            System.out.println("na poupança ele renderá:\nEm 3 meses: "
                                                    + ((invest * 1.5) * 3 + invest)
                                                    + "Reais\nEm 6 meses: " + ((invest * 1.5) * 6 + invest)
                                                    + "Reais\nEm 12 meses: " + ((invest * 1.5) * 12 + invest));
                                            System.out.println(
                                                    "Deseja realmente investir essa quantia? (SIM(S) / NAO(N))");
                                                    scanner.nextLine();
                                            String res = scanner.nextLine();
                                            if (res.equals("S")) {
                                                if (saldo < invest) {
                                                    System.out.println("Você não está tão rico assim");
                                                } else {
                                                    conta.setSaldo(saldo - invest);
                                                }

                                            }
                                        }
                                        String retornoInvestimentos = "Investimentos realizados!";
                                        retornoInvestimentos = codifica(retornoInvestimentos);
                                        Mensagem mensagemzinha2 = new Mensagem<Conta>(conta, HmacKey,
                                                retornoInvestimentos, 3);

                                        saida.writeObject(mensagemzinha2);
                                        saida.flush();
                                        break;
                                    case 4:
                                        System.out.println("saque");
                                        System.out.println("Quanto deseja sacar?");
                                        int sald = conta.getSaldo();
                                        int saque = scanner.nextInt();
                                        String retornoSaque;
                                        if (sald < saque) {
                                            retornoSaque = "Você não está tão rico assim";
                                        } else {
                                            conta.setSaldo(sald - saque);
                                            retornoSaque = "Saque realizado com sucesso!";
                                        }
                                        retornoSaque = "Saque realizado com sucesso!";
                                        retornoSaque = codifica(retornoSaque);
                                        Mensagem mensagemzinha3 = new Mensagem<Conta>(conta, HmacKey, retornoSaque, 4);

                                        saida.writeObject(mensagemzinha3);
                                        saida.flush();
                                        break;
                                    case 5:
                                        System.out.println("deposito");
                                        System.out.println("Quanto deseja depositar?");
                                        int deposito = scanner.nextInt();
                                        conta.setSaldo(conta.getSaldo() + deposito);
                                        String retornoDeposito = "Depósito realizado com sucesso!";
                                        retornoDeposito = codifica(retornoDeposito);
                                        Mensagem mensagemzinha4 = new Mensagem<Conta>(conta, HmacKey, retornoDeposito,
                                                5);

                                        saida.writeObject(mensagemzinha4);
                                        saida.flush();
                                        break;

                                    default:
                                        String retornoInvalido = "Operação inválida!";
                                        retornoInvalido = codifica(retornoInvalido);
                                        saida.writeUTF(retornoInvalido);
                                        saida.flush();
                                        break;
                                }

                                //ouvirThread();

                            }

                            break;
                        case 2:
                            System.out.println("Digite seu Nome: ");
                            String nome = scanner.nextLine();

                            System.out.println("Digite seu CPF: ");
                            String cpf = scanner.nextLine();

                            System.out.println("Digite sua senha: ");
                            String senha = scanner.nextLine();

                            System.out.println("Digite sua Endereço: ");
                            String endereco = scanner.nextLine();

                            System.out.println("Digite seu telefone: ");
                            String telefone = scanner.nextLine();

                            conta = new Conta(cpf, nome, senha, endereco, telefone);
                            String concatenate2 = nome + "-" + cpf + "-" + senha + "-" + endereco + "-" + telefone;
                            concatenate2 = codifica(concatenate2);
                            Mensagem mensagemzinha = new Mensagem<Conta>(conta, HmacKey, concatenate2, 6);

                            saida.writeObject(mensagemzinha);
                            saida.flush();

                            //ouvirThread();

                            break;

                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    public String decodifa(String mensagem) {
        try {
            mensagem = AES.descriptografar(AesKey, mensagem);
            mensagem = Vernamm.decifrar(mensagem, VernKey);
            //mensagem = Hmac.hMac(HmacKey, mensagem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mensagem;
    }

    public String codifica(String recebeMensagem) {
        try {
            //recebeMensagem = Hmac.hMac(HmacKey, recebeMensagem);
            recebeMensagem = Vernamm.cifrar(recebeMensagem, VernKey);
            recebeMensagem = aes.criptografar(AesKey, recebeMensagem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recebeMensagem;
    }

    public void ouvirThread() throws ClassNotFoundException {
        try {
            entrada = new ObjectInputStream(cliente.getInputStream());

            while (true) {
                Mensagem mensagem = (Mensagem) entrada.readObject();
                String descripto = decodifa(mensagem.getCriptografada());
                System.out.println(descripto);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
