import java.net.Socket;

public class Jogador {

    public Socket getSocketJogador() {
        return socketJogador;
    }

    public void setSocketJogador(Socket socketJogador) {
        this.socketJogador = socketJogador;
    }

    private Socket socketJogador;

}
