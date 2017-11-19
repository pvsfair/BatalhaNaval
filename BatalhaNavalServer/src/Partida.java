public class Partida {
    public Partida(Jogador j1, String codPartida) {
        this.j1 = j1;
        this.j2 = null;
        this.codPartida = codPartida;
    }

    public Jogador getJ1() {
        return j1;
    }

    public Jogador getJ2() {
        return j2;
    }

    public void setJ2(Jogador j2) {
        this.j2 = j2;
    }

    public String getCodPartida() {
        return codPartida;
    }

    private Jogador j1;
    private Jogador j2;
    private String codPartida;
}
