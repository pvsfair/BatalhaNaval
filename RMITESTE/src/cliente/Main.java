package cliente;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        Mensageiro gerente = (Mensageiro) Naming.lookup("//localhost/msg");

        System.out.println(gerente.hello("FAUSTOPASSO"));

    }

}
