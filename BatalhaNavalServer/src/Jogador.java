import java.net.Socket;

public class Jogador {
    public Jogador(Socket socket, String nome) {
        this.socket = socket;
        this.nome = nome;
        this.tabuleiro = new Tabuleiro();
    }

    public String getNome() {
        return nome;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean takeShotAt(int x, int y){
        return tabuleiro.takeShotAt(x, y);
    }

    public boolean allShipsDistroyed(){
        if(tabuleiro.getPedacosDeNavioSemTiro() == 0)
            return true;
        return false;
    }

    public void addShip(Navio navio){
        System.out.println(navio);
        tabuleiro.addNavio(navio);
    }

    private String nome;
    private Socket socket;
    private Tabuleiro tabuleiro;
}
