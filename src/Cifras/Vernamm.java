package Cifras;

public class Vernamm {
    // Método para cifrar uma mensagem usando a cifra de Vernam
    public static String cifrar(String mensagem, String chave) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < mensagem.length(); i++) {
            char caractere = mensagem.charAt(i);
            // Garante que a chave seja repetida, se necessário
            char chaveChar = chave.charAt(i % chave.length());
            // XOR para cifrar
            char cifrado = (char) (caractere ^ chaveChar);
            resultado.append(cifrado);
        }
        return resultado.toString();
    }

    // Método para decifrar uma mensagem cifrada usando a cifra de Vernam
    public static String decifrar(String mensagemCifrada, String chave) {
        // Como Vernam é simétrico, cifrar e decifrar são os mesmos
        return cifrar(mensagemCifrada, chave);
    }
}
