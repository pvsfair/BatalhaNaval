package cliente;

import shared.Jogador;
import shared.IGameService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static IGameService gameService;
    static String roomCode = "";

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        gameService = (IGameService) Naming.lookup("//localhost/gameService");

        String nome = readStringFromConsole("Insira o seu nome de jogador: ");

        Jogador jogador = gameService.register(nome);

        while(true){
            int opt = printMenuAndReadOption();
            switch (opt) {
                case 1:
                    roomCode = gameService.createRoom(jogador);

                    boolean waiter = false;

                    while (!gameService.hasGameStarted(jogador)){
                        clearConsole();
                        System.out.print("\n\nAguardando outro jogador se juntar a partida");
                        if(waiter)
                            System.out.print("\\");
                        else
                            System.out.print("/");
                        waiter = !waiter;

                        System.out.println("\nCodigo da Partida: " + roomCode);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    startGameLoop(jogador);
                    break;
                case 2:
                    boolean joinedRoom = true;
                    do{
                        if(!joinedRoom){
                            System.out.println("Nao foi possivel encontrar a partida escolhida\n");
                            readStringFromConsole("Pressione enter para digitar novamente o código da sala.");
                        }
                        roomCode = readStringFromConsole("Digite o codigo da Partida ou digite V para voltar ao menu: ", true).toUpperCase();
                        if (roomCode.toUpperCase().equals("V")) {
                            joinedRoom = false;
                            break;
                        }
                    }while (!(joinedRoom = gameService.joinRoom(jogador, roomCode)));
                    if(!joinedRoom)
                        break;
                    else{
                        startGameLoop(jogador);
                    }
                    break;
                case 3:
                    clearConsole();
                    System.out.println("FECHANDO O JOGO");
                    return;
            }
        }
    }

    private static int printMenuAndReadOption(){
        clearConsole();
        int opt;
        System.out.println("Você se conectou ao servidor, escolha uma das opcoes abaixo:\n" +
                "1 - Iniciar uma nova partida.\n" +
                "2 - Se juntar a uma partida existente.\n" +
                "3 - Fechar o jogo.");

        opt = readIntFromConsole("Escolha uma opcao: ");


        while(opt < 1 || opt > 3) {
            clearConsole();
            System.out.println("Você deve escolher uma opcao entre 1 e 3 como descrito abaixo:\n" +
                    "1 - Iniciar uma nova partida.\n" +
                    "2 - Se juntar a uma partida existente.\n" +
                    "3 - Fechar o jogo.");

            opt = readIntFromConsole("Escolha uma opcao: ");
        }
        return opt;
    }

    private static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static String readStringFromConsole(String message, boolean clearConsole){
        if(clearConsole) clearConsole();
        return readStringFromConsole(message);
    }

    private static String readStringFromConsole(String message){
        Scanner reader = new Scanner(System.in);

        System.out.println(message);
        return reader.nextLine();
    }

    private static int readIntFromConsole(String message){
        Scanner reader = new Scanner(System.in);

        System.out.println(message);
        return reader.nextInt();
    }

    private static Tabuleiro posShips(Jogador jogador) throws RemoteException {
        Tabuleiro tabuleiro = new Tabuleiro();
        List<Navio> navios = new ArrayList<>();

        navios.add(new Navio(0,0,5,0));
        navios.add(new Navio(0,0,4,0));
        navios.add(new Navio(0,0,4,0));
        navios.add(new Navio(0,0,3,0));
        navios.add(new Navio(0,0,3,0));
        navios.add(new Navio(0,0,3,0));
        navios.add(new Navio(0,0,2,0));
        navios.add(new Navio(0,0,2,0));
        navios.add(new Navio(0,0,2,0));
        navios.add(new Navio(0,0,2,0));

        while (!navios.isEmpty()) {
            clearConsole();
            System.out.println("Posicionando frota");
            System.out.println(tabuleiro.getMyTabuleiro());
            Navio n = navios.remove(0);
            System.out.println("Posicione o seu navio de tamanho " + n.getTamanho());
            System.out.println("Informe a celula em que ele deve ser posicionado e a direcao\n" +
                    "para a qual ele sera rotacionado (0:esquerda | 1:baixo)");
            String posNavio = "";
            int rotNavio = 0;
            boolean shipOK = false;

            posNavio = readStringFromConsole("Informe a posicao (Exemplo: A3)").toUpperCase();
            while (!treatPosTiro(posNavio)) {
                System.out.println("Posicao incorreta ou indisponivel");
                posNavio = readStringFromConsole("Informe a posicao novamente").toUpperCase();
            }

            rotNavio = readIntFromConsole("Informe a rotacao");
            while (rotNavio < 0 || rotNavio > 1) {
                System.out.println("Rotacao incorreta");
                rotNavio = readIntFromConsole("Informe a rotacao novamente (Exemplo: 0)");
            }
            shipOK = checkShipOK(posNavio, rotNavio, n.getTamanho(), tabuleiro);


            while(!shipOK) {
                System.out.println("Posicao incorreta ou indisponivel");
                posNavio = readStringFromConsole("Informe uma nova posicao (Exemplo: A3)").toUpperCase();
                while (!treatPosTiro(posNavio)) {
                    posNavio = readStringFromConsole("Informe a posicao novamente").toUpperCase();
                }

                rotNavio = readIntFromConsole("Informe a rotacao");
                while (rotNavio < 0 || rotNavio > 1) {
                    System.out.println("Rotacao incorreta");
                    rotNavio = readIntFromConsole("Informe a rotacao novamente (Exemplo: 0)");
                }
                shipOK = checkShipOK(posNavio, rotNavio, n.getTamanho(), tabuleiro);
            }
            int posI = Tabuleiro.convertPosToXY(posNavio);
            int x = posI / 10;
            int y = posI % 10;
            gameService.registerShipForPlayer(jogador, x, y, n.getTamanho(), rotNavio);
            tabuleiro.addNavio(n, posNavio, rotNavio);
        }
        return tabuleiro;
    }

    private static boolean checkShipOK(String pos, int rot, int length, Tabuleiro tabuleiro){
        int posI = Tabuleiro.convertPosToXY(pos);
        int x = posI / 10;
        int y = posI % 10;
        if(x > 9 || x < 0 || y > 9 || y < 0) return false;


        for(int l = 0; l < length; l++) {
            if(rot == 0) {
                if(!posAvailable(x + l, y, tabuleiro))
                    return false;
            }else if(rot == 1) {
                if (!posAvailable(x, y + l, tabuleiro))
                    return false;
            }
        }
        return true;
    }

    private static boolean treatPosTiro(String posTiro) {
        if(posTiro.length() == 2) {
            if (Character.isLetter(posTiro.charAt(0)) && Character.isDigit(posTiro.charAt(1))) {
                if (posTiro.charAt(0) >= 'A' && posTiro.charAt(0) <= 'J') {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean posAvailable(int x, int y, Tabuleiro tabuleiro){
        if(x > 9 || x < 0 || y > 9 || y < 0) return false;
        for (Navio n : tabuleiro.getNavios()) {
            if(n.getRotacao() == 0){
                if(y == n.getPosY() && x >= n.getPosX() && x < n.getPosX() + n.getTamanho()){
                    return false;
                }
            }else if(n.getRotacao() == 1){
                if(x == n.getPosX() && y >= n.getPosY() && y < n.getPosY() + n.getTamanho()){
                    return false;
                }
            }
        }
        return true;
    }

    private static void startGameLoop(Jogador jogador) throws RemoteException {
        System.out.println("Posicionar os navios");
        Tabuleiro tabuleiro = posShips(jogador);
        System.out.println("Esperando o outro jogador");

        while(!gameService.checkPlayersReady(jogador)){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("INICIANDO O JOGO");
        boolean jogando = true;

        while(jogando){
            clearConsole();
            System.out.println(tabuleiro);

            boolean isMyTurn = gameService.isMyTurn(jogador);

            if(isMyTurn){
                String posTiro = readStringFromConsole("Digite a posicao na qual deseja atirar: ").toUpperCase();
                while(!treatPosTiro(posTiro)) {
                    readStringFromConsole("Tiro invalido, tente novamente (Exemplo: A3)\nPressione enter para tentar novamente");
                    posTiro = readStringFromConsole("Digite a posicao na qual deseja atirar: ").toUpperCase();
                }

                if(gameService.shotAt(jogador, posTiro)){
                    tabuleiro.shotSent(posTiro, true);
                    System.out.println("Acertou em cheio!");
                }else {
                    tabuleiro.shotSent(posTiro, false);
                    System.out.println("Tiro caiu na agua.");
                }

                if(gameService.iWon(jogador)) {
                    System.out.println("VOCE VENCEU!!");
                    jogando = false;
                }else {
                    System.out.println("Fim de turno");
                }

            }else{
                System.out.println("Turno do adversario, aguarde enquanto ele atira.");

                String shotsTaken = "";
                while(!gameService.isMyTurn(jogador)) {
                    //System.out.println("asuhduashduasd");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                shotsTaken = gameService.shotTaken(jogador);
                String[] split = shotsTaken.split(":");

                for (String shot : split) {
                    tabuleiro.shotTaken(shot);
                }

                clearConsole();
                System.out.println(tabuleiro);

                if(gameService.iLost(jogador)){
                    System.out.println("Voce perdeu!!");
                    jogando = false;
                }else{
                    System.out.println("Fim de turno");
                }
            }
        }
        try {
            Thread.sleep(500);
            System.out.println("Fechando o jogo");
            Thread.sleep(1000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
