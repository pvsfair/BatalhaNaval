package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameService extends Remote{
    public Jogador register(String name) throws RemoteException;

    public String createRoom(Jogador jogador) throws RemoteException;

    public boolean joinRoom(Jogador jogador, String code) throws RemoteException;

    public boolean unregister(Jogador jogador) throws RemoteException;

    public boolean hasGameStarted(Jogador jogador) throws RemoteException;
}
