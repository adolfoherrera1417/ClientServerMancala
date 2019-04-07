import java.util.Scanner;

/***********************************************************
 * Created by:   Adolfo Herrera                                      
 *
 * Date:     Mar 27, 2019             
 *
 * Purpose:  Demonstration of a simple program.                                             
 ********************************************************/
public class Mancala {

    private String ip = "127.0.0.1";
    private int port = 3000;

    private boolean yourTurn = false;
    private boolean won = false;
    private boolean enemyWon= false;
    private static GamePlay myGame;

    static private cli client;

    public Mancala() {

    }

    public void setIp(String ipAddress) {
        ip = ipAddress;
    }

    public void setPort(int portNumber) {
        port = portNumber;
    }

    public void startGame() {
        if(!connect()) {
            initializeServer();
        }
    }

    private boolean connect() {
        try {
            client = new cli(ip,port,2);
            client.start();
            client.startMancala();

            return true;
        } catch (Exception e) {
            System.out.println("Unable to connect to server. \nWill begin new Server!" );
            return false;
        }
    }

    private void initializeServer() {
        try {
            System.out.println("Server started...Waiting for other players to join!");
            new serverHandler(port).start();
            client = new cli(ip,port,1);
            client.start();
            client.startMancala();


        } catch(Exception e) {
            System.out.println("Print Stacktrace");
        }
    }

    public class serverHandler extends Thread {
        private int port;
        private ServerMain myServer;
        public serverHandler(int port1) {
            port = port1;
        }

        @Override
        public void run() {
            myServer = new ServerMain(port);
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Mancala mancala = new Mancala();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the IP: ");
        mancala.setIp(scanner.nextLine());
        System.out.println("Please input the port: ");
        mancala.setPort(scanner.nextInt());

        mancala.startGame();

    }
}
