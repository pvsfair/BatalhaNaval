import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BatalhaNavalCliente {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(new Tabuleiro());
        System.out.print("\033[H\033[2J");
        System.out.flush();
        Thread.sleep(1000);
        System.out.println(new Tabuleiro());
        Socket cliente = connect("127.0.0.1",12010);


        Scanner reader = new Scanner(System.in);

        System.out.println("Digite o seu nome: ");
        String nome = reader.nextLine();

        sendStringToClient(cliente, nome);

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
}
