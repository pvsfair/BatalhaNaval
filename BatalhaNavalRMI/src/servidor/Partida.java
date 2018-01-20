package servidor;

import shared.Jogador;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Partida extends UnicastRemoteObject {

    private Jogador j1;
    private Jogador j2;
    private String codPartida;

    private String shot = "";

    private boolean isJ1Turn = true;
    private boolean isGameOver = false;

    public Partida(Jogador j1, String codPartida) throws RemoteException {
        super();
        this.j1 = j1;
        this.j2 = null;
        this.codPartida = codPartida;
    }

    public void run() {
        boolean isJ1Turn = true;
        boolean isGameOver = false;

        System.out.println("Thread do Jogo Iniciada");
        while(!isGameOver){
            boolean missedShot = false;
            if(isJ1Turn) {
                sendMessageToJ1("yourTurn");
                sendMessageToJ2("notYourTurn");

                // Tiro
                String posTiro = listenFromJ1();

                boolean acerto = shootJ2(posTiro);
                if(acerto) {
                    sendMessageToJ1("gotIt");
                    missedShot = false;
                }
                else {
                    sendMessageToJ1("miss");
                    missedShot = true;
                }
                //fim tiro

                sendMessageToJ2(posTiro);

                boolean j1won = j2.allShipsDistroyed();

                if(j1won){
                    sendMessageToJ1("youWin");
                    sendMessageToJ2("youLose");
                    isGameOver = true;
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMessageToJ1("turnEnd");
                    sendMessageToJ2("turnEnd");
                }
            }else{
                sendMessageToJ2("yourTurn");
                sendMessageToJ1("notYourTurn");

                String posTiro = listenFromJ2();

                boolean acerto = shootJ1(posTiro);
                if(acerto) {
                    sendMessageToJ2("gotIt");
                    missedShot = false;
                }
                else {
                    sendMessageToJ2("miss");
                    missedShot = true;
                }

                sendMessageToJ1(posTiro);

                boolean j2won = j1.allShipsDistroyed();

                if(j2won){
                    sendMessageToJ2("youWin");
                    sendMessageToJ1("youLose");
                    isGameOver = true;
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMessageToJ2("turnEnd");
                    sendMessageToJ1("turnEnd");
                }
            }
            if(missedShot)
                isJ1Turn = !isJ1Turn;
        }
    }

    private void sendMessageToJ1(String message){
//        Server.sendStringToClient(j1.getSocket(), message);
    }

    private void sendMessageToJ2(String message){
//        Server.sendStringToClient(j2.getSocket(), message);
    }

    private String listenFromJ1(){
//        return Server.readStringFromCliente(j1.getSocket());
        return null;
    }

    private String listenFromJ2(){
//        return Server.readStringFromCliente(j2.getSocket());
        return null;
    }

    private boolean shootJ1(String pos){
        int x;
        int y = Integer.parseInt("" + pos.charAt(1));
        char xC = pos.charAt(0);
        switch (xC){
            case 'A':
                x = 0;
                break;
            case 'B':
                x = 1;
                break;
            case 'C':
                x = 2;
                break;
            case 'D':
                x = 3;
                break;
            case 'E':
                x = 4;
                break;
            case 'F':
                x = 5;
                break;
            case 'G':
                x = 6;
                break;
            case 'H':
                x = 7;
                break;
            case 'I':
                x = 8;
                break;
            case 'J':
                x = 9;
                break;
            default:
                x = 0;
        }
        return j1.takeShotAt(x, y);
    }

    private boolean shootJ2(String pos){
        int x;
        int y = Integer.parseInt("" + pos.charAt(1));
        char xC = pos.charAt(0);
        switch (xC){
            case 'A':
                x = 0;
                break;
            case 'B':
                x = 1;
                break;
            case 'C':
                x = 2;
                break;
            case 'D':
                x = 3;
                break;
            case 'E':
                x = 4;
                break;
            case 'F':
                x = 5;
                break;
            case 'G':
                x = 6;
                break;
            case 'H':
                x = 7;
                break;
            case 'I':
                x = 8;
                break;
            case 'J':
                x = 9;
                break;
            default:
                x = 0;
        }
        return j2.takeShotAt(x, y);
    }

    public Jogador getJ1() {
        return j1;
    }

    public Jogador getJ2() {
        return j2;
    }

    public void setJ2(Jogador j2) {
        this.codPartida += "=";
        this.j2 = j2;

    }

    public String getCodPartida() {
        return codPartida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partida partida = (Partida) o;

        return getCodPartida().equals(partida.getCodPartida());
    }

    @Override
    public int hashCode() {
        return getCodPartida().hashCode();
    }

    public boolean isMyTurn(Jogador jogador) throws RemoteException {
        return (this.j1.equals(jogador) && isJ1Turn) || (this.j2.equals(jogador) && !isJ1Turn);
    }

    public boolean shotAt(Jogador jogador, String posTiro) throws RemoteException {
        shot += posTiro;
        boolean acerto = false;
        System.out.println("waiting: " + shot);
        if(isJogadorJ1(jogador)){
            if(!isJ1Turn) return false;
            acerto = shootJ2(posTiro);
        }
        else{
            if(isJ1Turn) return true;
            acerto = shootJ1(posTiro);
        }
        if(!acerto)
            isJ1Turn = !isJ1Turn;
        else {
            if(this.iWon(jogador)){
                isJ1Turn = !isJ1Turn;
                return acerto;
            }
            shot += ':';
        }
        return acerto;
    }

    public String shotTaken(Jogador jogador) throws RemoteException {
        String s = shot;
        shot = "";
        return s;
    }

    public boolean iLost(Jogador jogador) throws RemoteException {
        if(isJogadorJ1(jogador)) {
            boolean j1lost = j1.allShipsDistroyed();

            if (j1lost) {
                isGameOver = true;
            }
            return j1lost;
        }else{
            boolean j2lost = j2.allShipsDistroyed();

            if (j2lost) {
                isGameOver = true;
            }
            return j2lost;
        }
    }

    public boolean iWon(Jogador jogador) throws RemoteException {
        if(isJogadorJ1(jogador)) {
            boolean j1won = j2.allShipsDistroyed();
            System.out.println("Check Victory: " + j1won);

            if (j1won) {
                isGameOver = true;
            }
            return j1won;
        }else{
            boolean j2won = j1.allShipsDistroyed();
            System.out.println("Check Victory: " + j2won);

            if (j2won) {
                isGameOver = true;
            }
            return j2won;
        }
    }

    private boolean isJogadorJ1(Jogador jogador){
        return (this.j1.equals(jogador));
    }
}
