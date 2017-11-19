import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BatalhaNavalCliente {
    public static void main(String[] args) throws InterruptedException {
        /*System.out.println(new Tabuleiro());
        System.out.print("\033[H\033[2J");
        System.out.flush();
        Thread.sleep(1000);
        System.out.println(new Tabuleiro());
        */
        Socket cliente = connect("127.0.0.1",12010);

        int opt = printMenuAndReadOption();

        sendIntToClient(cliente, opt);

        if(opt == 3){
            clearConsole();
            System.out.println("FECHANDO O JOGO");
            return;
        }else if(opt == 1){
            if(readStringFromCliente(cliente).equals("sendName")){
                String nome = readStringFromConsole("Digite o seu nome: ");
                sendStringToClient(cliente, nome);
                String roomCode = readStringFromCliente(cliente);
                System.out.println("Codigo da sala: " + roomCode);
                waitForGameToStart(cliente);
                startGameLoop(cliente);
            }
        }

        String READ = readStringFromCliente(cliente);

        System.out.println("Conectado na partida de numero: " + READ);
    }

    public static Socket connect(String host, int porta){
        try{
            Socket cliente = new Socket("127.0.0.1",12010);
            return cliente;
        }
        catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public static void sendStringToClient(Socket cliente, String msg){
        try {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeUTF(msg);
            saida.flush();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void sendIntToClient(Socket cliente, int msg){
        try {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeInt(msg);
            saida.flush();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static String readStringFromCliente(Socket cliente){
        try {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            String ret = entrada.readUTF();
            return ret;
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return "";
        }
    }

    public static int printMenuAndReadOption(){
        int opt = 0;
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

    public static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String readStringFromConsole(String message){
        Scanner reader = new Scanner(System.in);

        System.out.println(message);
        return reader.nextLine();
    }

    public static void waitForGameToStart(Socket client){
        String msg;
        do {
            msg = readStringFromCliente(client);
        } while (!msg.equals("startMatch"));
    }

    public static void startGameLoop(Socket client){
        System.out.println("INICIANDO O JOGOOOOOO");
    }
}
