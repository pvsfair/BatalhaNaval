@SuppressWarnings("WeakerAccess")
public class Partida extends Thread {

    @Override
    public void run() {
        boolean registeringJ1 = true;
        sendMessageToJ1("registerBoards");
        sendMessageToJ2("wait");
        while(registeringJ1){
            String msg = listenFromJ1();
            if(msg.equals("boardRegisterEnd")) {
                registeringJ1 = false;
                break;
            }else{
                String[] navioInfo = msg.split(":");
                this.j1.addShip(new Navio(Integer.parseInt(navioInfo[0]), Integer.parseInt(navioInfo[1]),
                                          Integer.parseInt(navioInfo[2]), Integer.parseInt(navioInfo[3])));
            }
        }
        sendMessageToJ2("registerBoards");
        while (!registeringJ1){
            String msg = listenFromJ2();
            if(msg.equals("boardRegisterEnd")) {
                break;
            }else{
                String[] navioInfo = msg.split(":");
                this.j2.addShip(new Navio(Integer.parseInt(navioInfo[0]), Integer.parseInt(navioInfo[1]),
                        Integer.parseInt(navioInfo[2]), Integer.parseInt(navioInfo[3])));
            }
        }

        boolean isJ1Turn = true;
        boolean isGameOver = false;

        System.out.println("Thread do Jogo Iniciada");
        while(!isGameOver){
            boolean missedShot = false;
            if(isJ1Turn) {
                sendMessageToJ1("yourTurn");
                sendMessageToJ2("notYourTurn");

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
        Server.sendStringToClient(j1.getSocket(), message);
    }

    private void sendMessageToJ2(String message){
        Server.sendStringToClient(j2.getSocket(), message);
    }

    private String listenFromJ1(){
        return Server.readStringFromCliente(j1.getSocket());
    }

    private String listenFromJ2(){
        return Server.readStringFromCliente(j2.getSocket());
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

    public Partida(Jogador j1, String codPartida) {
        this.j1 = j1;
        this.j2 = null;
        this.codPartida = codPartida;
    }

    public Jogador getJ1() {
        return j1;
    }

    public Jogador getJ2() {
        return j2;
    }

    public void setJ2(Jogador j2) {
        this.codPartida = "=====";
        this.j2 = j2;

    }

    public String getCodPartida() {
        return codPartida;
    }

    public boolean isThreadRunning() {
        return threadRunning;
    }

    public void setThreadRunning(boolean threadRunning) {
        this.threadRunning = threadRunning;
    }

    private Jogador j1;
    private Jogador j2;
    private String codPartida;
    private boolean threadRunning;
}
