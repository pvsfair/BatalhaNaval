package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameService extends Remote{
    public Jogador register(String name) throws RemoteException;

    public String createRoom(Jogador jogador) throws RemoteException;

    public boolean joinRoom(Jogador jogador, String code) throws RemoteException;

    public boolean hasGameStarted(Jogador jogador) throws RemoteException;

    public void registerShipForPlayer(Jogador jogador, int posX, int posY, int tamanho, int rotacao) throws RemoteException;

    public boolean checkPlayersReady(Jogador jogador) throws RemoteException;

    public boolean isMyTurn(Jogador jogador) throws RemoteException;

    public boolean shotAt(Jogador jogador, String posTiro) throws RemoteException;

    public String shotTaken(Jogador jogador) throws RemoteException;

    public boolean iLost(Jogador jogador) throws RemoteException;

    public boolean iWon(Jogador jogador) throws RemoteException;
}
