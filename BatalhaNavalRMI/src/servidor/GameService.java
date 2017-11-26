package servidor;

import shared.IGameService;
import shared.Jogador;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;

public class GameService extends UnicastRemoteObject implements IGameService {

    private static List<Partida> partidas;

    private static List<Jogador> jogadores;

    protected GameService() throws RemoteException {
        super();
    }

    private static String generateRoomCode(){
        Random random = new Random();
        StringBuilder codPartida = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for(int i = 0; i < 5; i++){
            codPartida.append(chars.charAt(random.nextInt(chars.length())));
        }
        return codPartida.toString();
    }

    @Override
    public Jogador register(String name) throws RemoteException {
        Jogador jogador = new Jogador(name);
        if(!jogadores.contains(jogador)){
            jogadores.add(jogador);
        }else{
            jogador = jogadores.get(jogadores.indexOf(jogador));
        }
        return jogador;
    }

    @Override
    public String createRoom(Jogador jogador) throws RemoteException {
        String roomCode = generateRoomCode();

        Partida partida = new Partida(jogador, roomCode);

        partidas.add(partida);

        return roomCode;
    }

    @Override
    public boolean joinRoom(Jogador jogador, String code) throws RemoteException {
        for (Partida p : partidas) {
            if (p.getCodPartida().equals(code)) {
                p.setJ2(jogador);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean unregister(Jogador jogador) throws RemoteException {
        return false;
    }
}
