package shared;

import servidor.Navio;
import servidor.Tabuleiro;

import java.io.Serializable;

public class Jogador implements Serializable{
    private String nome;
    private Tabuleiro tabuleiro;
    private boolean ready;

    public Jogador(String nome) {
        this.nome = nome;
        this.tabuleiro = new Tabuleiro();
        ready = false;
    }

    public boolean isReady(){
        return ready;
    }

    public void setReady(boolean ready){
        this.ready = ready;
    }

    public String getNome() {
        return nome;
    }

    public boolean takeShotAt(int x, int y) {
        return tabuleiro.takeShotAt(x, y);
    }

    public boolean allShipsDistroyed() {
        if (tabuleiro.getPedacosDeNavioSemTiro() == 0)
            return true;
        return false;
    }

    public void addShip(Navio navio) {
        //System.out.println(navio);
        tabuleiro.addNavio(navio);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jogador jogador = (Jogador) o;

        return getNome().equals(jogador.getNome());
    }

    @Override
    public int hashCode() {
        return getNome().hashCode();
    }
}
