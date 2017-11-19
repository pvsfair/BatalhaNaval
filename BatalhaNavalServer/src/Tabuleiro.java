import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {

    public Tabuleiro() {
        navios = new ArrayList<>();

        navios.add(new Navio(1,2,5,0));
        navios.add(new Navio(2,4,4,1));
        navios.add(new Navio(7,0,4,1));
        navios.add(new Navio(4,4,3,0));
        navios.add(new Navio(5,6,3,1));
        navios.add(new Navio(9,3,3,1));
        navios.add(new Navio(0,0,2,0));
        navios.add(new Navio(4,0,2,0));
        navios.add(new Navio(0,9,2,0));
        navios.add(new Navio(7,6,2,0));

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

    private List<Navio> navios;
    private List<Integer> tiros;
}
