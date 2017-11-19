import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class BatalhaNavalCliente {
    public static void main(String[] args) {
        System.out.println(new Tabuleiro());
        try{
            Socket cliente = new Socket("127.0.0.1",12010);
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            Scanner reader = new Scanner(System.in);

            System.out.println("Digite o seu nome: ");
            String nome = reader.nextLine();


            String READ = entrada.readUTF();
            JOptionPane.showMessageDialog(null,"Data recebida do servidor: " + READ);
            //entrada.close();
            //System.out.println("Conexão encerrada");
        }
        catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
