import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        ServerSocket servidor = createServerSocket(12010);
        while(true){
            Socket cliente = serverAccept(servidor);
            System.out.println("Cliente Conectado: " + cliente.getInetAddress().getHostAddress());

            String nomeJogador = readStringFromCliente(cliente);
            System.out.println("O nome do jogador conectado é: " + nomeJogador);

            sendStringToClient(cliente,generateRoomCode());
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
