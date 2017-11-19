import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {

    public Tabuleiro() {
        navios = new ArrayList<>();

        navios.add(new Navio(0,0,5,1));
        navios.add(new Navio(1,0,4,1));
        navios.add(new Navio(2,0,4,1));
        navios.add(new Navio(3,0,3,1));
        navios.add(new Navio(4,0,3,1));
        navios.add(new Navio(5,0,3,1));
        navios.add(new Navio(6,0,2,1));
        navios.add(new Navio(7,0,2,1));
        navios.add(new Navio(8,0,2,1));
        navios.add(new Navio(9,0,2,1));
    }

    @Override
    public String toString() {
        List<String> toReturn = new ArrayList<>();
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        toReturn.add("░░░░░░░░░░░░░░░░░░░░");
        for (Navio n : navios) {
            switch (n.getRotacao()){
                case 0: // Direita
                    for(int i = n.getPosX() * 2; i < (n.getPosX() + n.getTamanho()) * 2; i += 2){
                        char[] chars = toReturn.get(n.getPosY()).toCharArray();// .charAt(i);
                        chars[i] = '█';
                        chars[i+1] = '█';
                        toReturn.set(n.getPosY(), new String(chars));
                    }
                    break;
                case 1: // Baixo
                    for(int i = n.getPosY(); i < n.getPosY() + n.getTamanho(); i++){
                        char[] chars = toReturn.get(i).toCharArray();// .charAt(n.getPosX());
                        chars[n.getPosX() * 2] = '█';
                        chars[(n.getPosX() * 2) + 1] = '█';
                        toReturn.set(i, new String(chars));
                    }
                    break;
            }
        }
        String ret = "";
        for (String s : toReturn) {
            ret += s + '\n';
        }
        return ret;
    }

    private List<Navio> navios;
}
