import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class Server extends Thread{

    private static List<Partida> partidas;

    private Partida partida;

    private Jogador jogador;

    private Socket client;

    public Server(Socket s){
        client = s;
    }

    @Override
    public void run() {
        System.out.println("Cliente Conectado: " + client.getInetAddress().getHostAddress());
        boolean listenToMenu = true;
        while(listenToMenu) {
            int opt = readIntFromCliente(client);
            String nomeJogador, roomCode;
            switch (opt) {
                case 1:
                    sendStringToClient(client, "sendName");
                    nomeJogador = readStringFromCliente(client);
                    roomCode = generateRoomCode();
                    this.jogador = new Jogador(client, nomeJogador);

                    this.partida = new Partida(this.jogador, roomCode);

                    partidas.add(this.partida);

                    sendStringToClient(client, roomCode);

                    waitForOtherPlayerToJoin(this.partida);

                    sendStringToClient(client, "startMatch");

                    startGameLoop();
                    return;
                case 2:
                    sendStringToClient(client, "sendName");
                    nomeJogador = readStringFromCliente(client);
                    boolean matchFound = false;
                    while (!matchFound) {
                        roomCode = readStringFromCliente(client);
                        if (roomCode.equals("listenMenu")) {
                            break;
                        }
                        for (Partida p : partidas) {
                            if (p.getCodPartida().equals(roomCode)) {
                                this.jogador = new Jogador(client, nomeJogador);
                                p.setJ2(this.jogador);
                                matchFound = true;
                                this.partida = p;
                                break;
                            }
                        }

                        if (!matchFound) {
                            sendStringToClient(client, "matchNotFound");
                        } else {
                            sendStringToClient(client, "startMatch");
                            matchFound = true;
                        }
                    }

                    if(matchFound) {
//                        startGameLoop();
                        return;
                    }
                    break;
                case 3:
                default:
                    if(this.jogador != null) {
                        System.out.println("Desconectando cliente: " + this.jogador.getNome() + " Com ip: " + client.getInetAddress().getHostAddress());
                    }else{
                        System.out.println("Desconectando cliente: " + client.getInetAddress().getHostAddress());
                    }
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
            }
        }

/*        String nomeJogador = readStringFromCliente(client);
        System.out.println("O nome do jogador conectado é: " + nomeJogador);

        sendStringToClient(client,generateRoomCode());*/
    }

    private void startGameLoop() {
        if (!this.partida.isThreadRunning()) {
            System.out.println("INICIANDO O JOGOOOOOO");
            this.partida.setThreadRunning(true);
            Thread t = new Thread(this.partida);
            t.start();
        }
        System.out.println("A partida já foi iniciada");
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
        //noinspection InfiniteLoopStatement
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
            return entrada.readUTF();
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
        StringBuilder codPartida = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for(int i = 0; i < 5; i++){
            codPartida.append(chars.charAt(random.nextInt(chars.length())));
        }
        return codPartida.toString();
    }
}
