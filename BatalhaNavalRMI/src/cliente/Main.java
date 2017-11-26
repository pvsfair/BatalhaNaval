package cliente;

import shared.Jogador;
import shared.IGameService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IGameService gameService = (IGameService) Naming.lookup("//localhost/gameService");

        String nome = readStringFromConsole("Insira o seu nome de jogador: ");

        Jogador jogador = gameService.register(nome);

        while(true){
            int opt = printMenuAndReadOption();

            switch (opt) {
                case 1:
                    String roomCode = gameService.createRoom(jogador);


                    boolean waiter = false;

                    while (!gameService.hasGameStarted(jogador)){
                        clearConsole();
                        System.out.print("\n\nAguardando outro jogador se juntar a partida");
                        if(waiter)
                            System.out.print("\\");
                        else
                            System.out.print("/");
                        waiter = !waiter;

                        System.out.println("Codigo da Partida: " + roomCode);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case 2:
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
}
