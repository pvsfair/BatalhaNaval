package servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        GameService gameService = new GameService();

        Naming.rebind("//localhost/gameService", gameService);
        System.out.println("Up and Running");

    }
}
