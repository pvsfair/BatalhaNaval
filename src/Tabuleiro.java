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

        tirosRecebidos = new ArrayList<>();

        // Tiros:
        //  - 0: Sem tiro;
        //  - 1: Tiro na água;
        //  - 2: Tiro em navio;

        for(int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                tirosRecebidos.add(0);
            }
        }

        tirosEnviados = new ArrayList<>();

        // Tiros:
        //  - 0: Sem tiro;
        //  - 1: Tiro na água;
        //  - 2: Tiro em navio;

        for(int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                tirosEnviados.add(0);
            }
        }
    }

    @Override
    public String toString() {
        List<String> meuTabuleiro = new ArrayList<>();
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        meuTabuleiro.add("░│░│░│░│░│░│░│░│░│░│");
        for (Navio n : navios) {
            switch (n.getRotacao()){
                case 0: // Direita
                    for(int i = n.getPosX() * 2; i < (n.getPosX() + n.getTamanho()) * 2; i += 2){
                        char[] chars = meuTabuleiro.get(n.getPosY()).toCharArray();// .charAt(i);

                        chars[i] = 'N';
                        meuTabuleiro.set(n.getPosY(), new String(chars));
                    }
                    break;
                case 1: // Baixo
                    for(int i = n.getPosY(); i < n.getPosY() + n.getTamanho(); i++){
                        char[] chars = meuTabuleiro.get(i).toCharArray();// .charAt(n.getPosX());
                        chars[n.getPosX() * 2] = 'N';
                        meuTabuleiro.set(i, new String(chars));
                    }
                    break;
            }
        }

        List<String> tabuleiroAdversario = new ArrayList<>();
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        tabuleiroAdversario.add("░│░│░│░│░│░│░│░│░│░│");
        for (Navio n : navios) {
            switch (n.getRotacao()){
                case 0: // Direita
                    for(int i = n.getPosX() * 2; i < (n.getPosX() + n.getTamanho()) * 2; i += 2){
                        char[] chars = tabuleiroAdversario.get(n.getPosY()).toCharArray();// .charAt(i);

                        chars[i] = 'N';
                        tabuleiroAdversario.set(n.getPosY(), new String(chars));
                    }
                    break;
                case 1: // Baixo
                    for(int i = n.getPosY(); i < n.getPosY() + n.getTamanho(); i++){
                        char[] chars = tabuleiroAdversario.get(i).toCharArray();// .charAt(n.getPosX());
                        chars[n.getPosX() * 2] = 'N';
                        tabuleiroAdversario.set(i, new String(chars));
                    }
                    break;
            }
        }
        String ret = "\033[4m                     \033[0m     \033[4m                     \n";

        for (int i = 0; i < meuTabuleiro.size(); i++) {
            ret += "│" + meuTabuleiro.get(i) + "\033[0m     \033[4m" + "│" + tabuleiroAdversario.get(i) + '\n';
        }
        ret += "\033[0m";
        return ret;
    }

    private List<Navio> navios;
    private List<Integer> tirosEnviados;
    private List<Integer> tirosRecebidos;
}
