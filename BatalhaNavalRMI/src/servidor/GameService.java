package servidor;

import shared.IGameService;
import shared.Jogador;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameService extends UnicastRemoteObject implements IGameService {

    private static List<Partida> partidas;

    private static List<Jogador> jogadores;

    protected GameService() throws RemoteException {
        super();
        partidas = new ArrayList<>();
        jogadores = new ArrayList<>();
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

        try {
            Naming.rebind("//localhost/partida/" + partida.getCodPartida(), partida);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return roomCode;
    }

    @Override
    public boolean joinRoom(Jogador jogador, String code) throws RemoteException {
        for (Partida p : partidas) {
            if (p.getCodPartida().toUpperCase().equals(code.toUpperCase())) {
                p.setJ2(jogador);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasGameStarted(Jogador jogador) throws RemoteException {
        for (Partida p : partidas) {
            if (p.getJ1().equals(jogador)) {
                return p.getJ2() != null;
            }
        }
        return false;
    }

    @Override
    public void registerShipForPlayer(Jogador jogador, int posX, int posY, int tamanho, int rotacao) throws RemoteException {
        Navio n = new Navio(posX, posY, tamanho, rotacao);
        jogador.addShip(n);
        for (Partida p : partidas) {
            if (p.getJ1().equals(jogador)) {
                p.getJ1().addShip(n);
                return;
            }else if (p.getJ2().equals(jogador)) {
                p.getJ2().addShip(n);
                return;
            }
        }
    }

    @Override
    public boolean checkPlayersReady(Jogador jogador) throws RemoteException {
        for (Partida p : partidas) {
            if (p.getJ1().equals(jogador)) {
                p.getJ1().setReady(true);
                return p.getJ2().isReady();
            }else if(p.getJ2().equals(jogador)) {
                p.getJ2().setReady(true);
                return p.getJ1().isReady();
            }
        }
        return false;
    }
    @Override
    public boolean isMyTurn(Jogador jogador) throws RemoteException {
        return getPartida(jogador).isMyTurn(jogador);
    }

    @Override
    public boolean shotAt(Jogador jogador, String posTiro) throws RemoteException {
        return getPartida(jogador).shotAt(jogador, posTiro);
    }

    @Override
    public String shotTaken(Jogador jogador) throws RemoteException {
        return getPartida(jogador).shotTaken(jogador);
    }

    @Override
    public boolean iLost(Jogador jogador) throws RemoteException {
        return getPartida(jogador).iLost(jogador);
    }

    @Override
    public boolean iWon(Jogador jogador) throws RemoteException {
        return getPartida(jogador).iWon(jogador);
    }

    private Partida getPartida(Jogador jogador){
        for (Partida p : partidas) {
            if (p.getJ1().equals(jogador) || p.getJ2().equals(jogador)) {
                return p;
            }
        }
        return null;
    }
}
