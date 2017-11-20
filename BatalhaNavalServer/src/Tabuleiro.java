import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {

    public Tabuleiro() {
        navios = new ArrayList<>();

        navios.add(new Navio(1,2,5,0));
        pedacosDeNavioSemTiro += 5;
        navios.add(new Navio(2,4,4,1));
        pedacosDeNavioSemTiro += 4;
        navios.add(new Navio(7,0,4,1));
        pedacosDeNavioSemTiro += 4;
        navios.add(new Navio(4,4,3,0));
        pedacosDeNavioSemTiro += 3;
        navios.add(new Navio(5,6,3,1));
        pedacosDeNavioSemTiro += 3;
        navios.add(new Navio(9,3,3,1));
        pedacosDeNavioSemTiro += 3;
        navios.add(new Navio(0,0,2,0));
        pedacosDeNavioSemTiro += 2;
        navios.add(new Navio(4,0,2,0));
        pedacosDeNavioSemTiro += 2;
        navios.add(new Navio(0,9,2,0));
        pedacosDeNavioSemTiro += 2;
        navios.add(new Navio(7,6,2,0));
        pedacosDeNavioSemTiro += 2;

        tiros = new ArrayList<>();

        // Tiros:
        //  - 0: Sem tiro;
        //  - 1: Tiro na água;
        //  - 2: Tiro em navio;

        for(int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                tiros.add(0);
            }
        }
    }

    public int getPedacosDeNavioSemTiro() {
        return pedacosDeNavioSemTiro;
    }

    public boolean takeShotAt(int x, int y){
        for (Navio n : navios) {
            if(n.getRotacao() == 0){
                if(y == n.getPosY() && x >= n.getPosX() && x < n.getPosX() + n.getTamanho()){
                    tiros.set((y*10) + x, 2);
                    pedacosDeNavioSemTiro--;
                    return true;
                }
            }else if(n.getRotacao() == 1){
                if(x == n.getPosX() && y >= n.getPosY() && y < n.getPosY() + n.getTamanho()){
                    tiros.set((y*10) + x, 2);
                    pedacosDeNavioSemTiro--;
                    return true;
                }
            }
        }
        tiros.set((y*10) + x, 1);
        return false;
    }

    private List<Navio> navios;
    private List<Integer> tiros;
    private int pedacosDeNavioSemTiro = 0;
}
