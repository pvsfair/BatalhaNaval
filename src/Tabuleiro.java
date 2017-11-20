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

    public void shotSent(String pos, boolean acerto){
        int posI = convertPosToXY(pos);
        int x = posI / 10;
        int y = posI % 10;
        tirosEnviados.set((y*10) + x, (acerto) ? 2 : 1);
    }

    public void shotTaken(String pos){
        int posI = convertPosToXY(pos);
        int x = posI / 10;
        int y = posI % 10;
        for (Navio n : navios) {
            if(n.getRotacao() == 0){
                if(y == n.getPosY() && x >= n.getPosX() && x < n.getPosX() + n.getTamanho()){
                    tirosRecebidos.set((y*10) + x, 2);
                    return;
                }
            }else if(n.getRotacao() == 1){
                if(x == n.getPosX() && y >= n.getPosY() && y < n.getPosY() + n.getTamanho()){
                    tirosRecebidos.set((y*10) + x, 2);
                    return;
                }
            }
        }
        tirosRecebidos.set((y*10) + x, 1);
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
                        switch(tirosRecebidos.get((n.getPosY() * 10) + (i / 2))){
                            case 0:
                                chars[i] = 'N';
                                break;
                            case 1:
                                chars[i] = 'a';
                                break;
                            case 2:
                                chars[i] = 'D';
                                break;
                        }
                        meuTabuleiro.set(n.getPosY(), new String(chars));
                    }
                    break;
                case 1: // Baixo
                    for(int i = n.getPosY(); i < n.getPosY() + n.getTamanho(); i++){
                        char[] chars = meuTabuleiro.get(i).toCharArray();// .charAt(n.getPosX());
                        switch(tirosRecebidos.get((i * 10) + n.getPosX())){
                            case 0:
                                chars[n.getPosX() * 2] = 'N';
                                break;
                            case 1:
                                chars[n.getPosX() * 2] = 'a';
                                break;
                            case 2:
                                chars[n.getPosX() * 2] = 'D';
                                break;
                        }
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

        for(int i = 0; i < 10; i ++){
            for (int j = 0; j < 10; j++){
                char[] chars = tabuleiroAdversario.get(j).toCharArray();
                switch(tirosEnviados.get((j*10) + i)){
                    case 0:
                        break;
                    case 1:
                        chars[i * 2] = 'a';
                        break;
                    case 2:
                        chars[i * 2] = 'D';
                        break;
                }
                tabuleiroAdversario.set(j, new String(chars));
            }
        }
        String ret = " \033[4m A B C D E F G H I J \033[0m      \033[4m A B C D E F G H I J \033[0m\n";

        for (int i = 0; i < meuTabuleiro.size(); i++) {
            ret += i + "\033[4m│" + meuTabuleiro.get(i) + "\033[0m     " + i + "\033[4m│" + tabuleiroAdversario.get(i) + "\033[0m\n";
        }
        ret += "\033[0m";
        return ret;
    }

    private static int convertPosToXY(String pos){
        int x;
        int y = Integer.parseInt("" + pos.charAt(1));
        char xC = pos.charAt(0);
        switch (xC){
            case 'A':
                x = 0;
                break;
            case 'B':
                x = 1;
                break;
            case 'C':
                x = 2;
                break;
            case 'D':
                x = 3;
                break;
            case 'E':
                x = 4;
                break;
            case 'F':
                x = 5;
                break;
            case 'G':
                x = 6;
                break;
            case 'H':
                x = 7;
                break;
            case 'I':
                x = 8;
                break;
            case 'J':
                x = 9;
                break;
            default:
                x = 0;
        }
        return (x*10) + y;
    }

    private List<Navio> navios;
    private List<Integer> tirosEnviados;
    private List<Integer> tirosRecebidos;
}
