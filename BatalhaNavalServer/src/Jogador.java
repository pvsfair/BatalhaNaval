import java.net.Socket;

public class Jogador {
    public Jogador(Socket socket, String nome) {
        this.socket = socket;
        this.nome = nome;
        this.tabuleiro = new Tabuleiro();
    }

    private String nome;
    private Socket socket;
    private Tabuleiro tabuleiro;
}