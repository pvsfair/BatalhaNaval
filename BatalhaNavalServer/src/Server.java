import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        try {
            // Instancia o Server Socket ouvindo na porta 12010
            ServerSocket servidor = new ServerSocket(12010);
            System.out.println("Servidor ouvindo na porta 12345");

            while(true){
                // o método accept() bloqueia a execução até que
                // o servidor receba um pedido de conexão
                Socket cliente = servidor.accept();
                System.out.println("Cliente Conectado: " + cliente.getInetAddress().getHostAddress());
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeUTF("TESTE");// .writeObject(new Date());
                saida.close();
                cliente.close();
            }
        }catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
