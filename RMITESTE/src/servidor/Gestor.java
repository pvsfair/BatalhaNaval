package servidor;

import cliente.Mensageiro;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class Gestor extends UnicastRemoteObject implements Mensageiro {

    protected Gestor() throws RemoteException {
        super();
    }

    @Override
    public String hello(String name) throws RemoteException {
        String host = "";
        try {
            host = RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        return "FAUSTOPP " + name + "|" + host;
    }
}
