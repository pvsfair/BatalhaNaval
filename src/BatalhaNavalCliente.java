import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BatalhaNavalCliente {
    public static void main(String[] args) throws InterruptedException {
        /*System.out.println(new Tabuleiro());
        Thread.sleep(1000);
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(new Tabuleiro());*/
        Socket cliente = connect();
        while(true) {

            int opt = printMenuAndReadOption();

            sendIntToServer(cliente, opt);

            clearConsole();

            switch (opt) {
                case 1:
                    if (readStringFromServer(cliente).equals("sendName")) {
                        String nome = readStringFromConsole("Digite o seu nome: ");
                        sendStringToServer(cliente, nome);

                        String roomCode = readStringFromServer(cliente);

                        System.out.println("Codigo da Partida: " + roomCode);

                        waitForGameToStart(cliente);
                        startGameLoop(cliente);
                    }
                    break;
                case 2:
                    if (readStringFromServer(cliente).equals("sendName")) {
                        String nome = readStringFromConsole("Digite o seu nome: ");
                        sendStringToServer(cliente, nome);
                        boolean roomFound = false;

                        while (!roomFound) {
                            String roomCode = readStringFromConsole("Digite o codigo da Partida ou digite V para voltar ao menu: ", true);
                            if (roomCode.toUpperCase().equals("V")) {
                                sendStringToServer(cliente, "listenMenu");
                                break;
                            }
                            sendStringToServer(cliente, roomCode.toUpperCase());

                            switch (readStringFromServer(cliente)) {
                                case "startMatch":
                                    roomFound = true;
                                    break;
                                case "matchNotFound":
                                    System.out.println("Nao foi possivel encontrar a partida escolhida\n");
                                    readStringFromConsole("Pressione enter para digitar novamente o código da sala.");
                                    break;
                            }
                        }

                        if(roomFound){
                            startGameLoop(cliente);
                        }

                    }
                    break;
                case 3:
                    clearConsole();
                    System.out.println("FECHANDO O JOGO");
                    return;
            }
        }
    }

    private static Socket connect(){
        try{
            return new Socket("127.0.0.1",12010);
        }
        catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    private static void sendStringToServer(Socket cliente, String msg){
        try {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeUTF(msg);
            saida.flush();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void sendIntToServer(Socket cliente, int msg){
        try {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeInt(msg);
            saida.flush();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static String readStringFromServer(Socket cliente){
        try {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            return entrada.readUTF();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return "";
        }
    }

    private static int printMenuAndReadOption(){
        clearConsole();
        int opt;
        System.out.println("Você se conectou ao servidor, escolha uma das opcoes abaixo:\n" +
                "1 - Iniciar uma nova partida.\n" +
                "2 - Se juntar a uma partida existente.\n" +
                "3 - Fechar o jogo.");

        Scanner reader = new Scanner(System.in);

        System.out.println("Escolha uma opcao: ");
        opt = reader.nextInt();

        while(opt < 1 || opt > 3) {
            clearConsole();
            System.out.println("Você deve escolher uma opcao entre 1 e 3 como descrito abaixo:\n" +
                    "1 - Iniciar uma nova partida.\n" +
                    "2 - Se juntar a uma partida existente.\n" +
                    "3 - Fechar o jogo.");

            System.out.println("Escolha uma opcao: ");
            opt = reader.nextInt();
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

    private static void waitForGameToStart(Socket client){
        String msg;
        do {
            msg = readStringFromServer(client);
        } while (!msg.equals("startMatch"));
    }

    private static void startGameLoop(Socket client){
        System.out.println("INICIANDO O JOGOOOOOO");
        boolean jogando = true;
        Tabuleiro tabuleiro = new Tabuleiro();
        while(jogando){
            String isMyTurn = readStringFromServer(client);
            clearConsole();
            System.out.println(tabuleiro);
            switch (isMyTurn){
                case "yourTurn":
                    String posTiro = readStringFromConsole("Digite a posicao na qual deseja atirar: ").toUpperCase();
                    while(!treatPosTiro(posTiro)) {
                        readStringFromConsole("Tiro invalido, tente novamente (Exemplo: A3)\nPressione enter para tentar novamente");
                        posTiro = readStringFromConsole("Digite a posicao na qual deseja atirar: ").toUpperCase();
                    }
                    sendStringToServer(client, posTiro);

                    String acerto = readStringFromServer(client);
                    switch (acerto){
                        case "gotIt":
                            tabuleiro.shotSent(posTiro, true);
                            System.out.println("Acertou em cheio!");
                            break;
                        case "miss":
                            tabuleiro.shotSent(posTiro, false);
                            System.out.println("Tiro caiu na agua.");
                            break;
                    }

                    String gameWonOrEndTurn = readStringFromServer(client);
                    switch (gameWonOrEndTurn){
                        case "youWin":
                            System.out.println("VOCE VENCEU!!");
                            jogando = false;
                            break;
                        case "turnEnd":
                            System.out.println("Fim de turno");
                            break;
                    }
                    break;
                case "notYourTurn":
                    System.out.println("Turno do adversario, aguarde enquanto ele atira.");
                    String shotTaken = readStringFromServer(client);

                    tabuleiro.shotTaken(shotTaken);

                    clearConsole();
                    System.out.println(tabuleiro);

                    System.out.println("Turno do adversario, aguarde enquanto ele atira.");

                    String gameLostOrEndTurn = readStringFromServer(client);
                    switch (gameLostOrEndTurn){
                        case "youLose":
                            System.out.println("Voce perdeu!!");
                            jogando = false;
                            break;
                        case "turnEnd":
                            System.out.println("Fim de turno");
                            break;
                    }
                    break;
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

    private static boolean treatPosTiro(String posTiro) {
        if (posTiro.length() == 2 && Character.isLetter(posTiro.charAt(0)) && Character.isDigit(posTiro.charAt(1))) {
            if (posTiro.charAt(0) >= 'A' && posTiro.charAt(0) <= 'J') {
                return true;
            }
        }
        return false;
    }
}
