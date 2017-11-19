import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server extends Thread{

    private static List<Partida> partidas;

    private Socket client;

    public Server(Socket s){
        client = s;
    }

    @Override
    public void run() {
        System.out.println("Cliente Conectado: " + client.getInetAddress().getHostAddress());

        int opt = readIntFromCliente(client);

        switch (opt){
            case 1:
                sendStringToClient(client, "sendName");
                String nomeJogador = readStringFromCliente(client);
                String roomCode = generateRoomCode();
                Jogador jogador = new Jogador(client, nomeJogador);

                Partida partida = new Partida(jogador,roomCode);

                partidas.add(partida);

                sendStringToClient(client, roomCode);
                waitForOtherPlayerToJoin(partida);
                break;
            case 2:

                break;
            case 3:
            default:
                return;
        }

        String nomeJogador = readStringFromCliente(client);
        System.out.println("O nome do jogador conectado é: " + nomeJogador);

        sendStringToClient(client,generateRoomCode());
    }

    public void waitForOtherPlayerToJoin(Partida partida){
        while (partida.getJ2() == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerSocket servidor = createServerSocket(12010);
        partidas = new ArrayList<>();
        while(true){
            Server handler = new Server(serverAccept(servidor));
            Thread t = new Thread(handler);
            t.start();
        }
    }

    public static ServerSocket createServerSocket(int porta){
        try{
            // Instancia o Server Socket ouvindo na porta @porta
            ServerSocket servidor = new ServerSocket(porta);
            System.out.println("Servidor ouvindo na porta " + porta);
            return servidor;
        }
        catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public static Socket serverAccept(ServerSocket server){
        try {
            // o método accept() bloqueia a execução até que
            // o servidor receba um pedido de conexão
            return server.accept();
        } catch (IOException e) {
            e.printStackTrace();
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

    public static int readIntFromCliente(Socket cliente){
        try {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            return entrada.readInt();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return 0;
        }
    }

    public static String generateRoomCode(){

        Random random = new Random();
        String codPartida = "";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for(int i = 0; i < 5; i++){
            codPartida += chars.charAt(random.nextInt(chars.length()));
        }
        return codPartida;
    }
}
