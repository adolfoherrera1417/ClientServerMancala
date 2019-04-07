/***********************************************************
 * Created by:   Adolfo Herrera                                      
 *
 * Date:     Jan 23, 2019             
 *
 * Purpose:  Demonstration of a simple program.                                             
 ********************************************************/

import java.io.*;
import java.net.Socket;

public class cli extends Thread {

    private Socket socket;

    private String host;
    private int port;
    private int clientNumber;
    private int opponentNumber;
    private boolean isTurn;

    private PrintWriter os = null;

    private static DataInputStream is = null;
    private static BufferedReader inputLine = null;


    static private GamePlay myGame;


    public cli(String hostAdd, int portNum, int playerNumber) {
        host = hostAdd;
        port = portNum;
        clientNumber = playerNumber;
        SetOpponent();
        isTurn = true;
        myGame = new GamePlay();
        try  {
            socket = new Socket(host,port);
            os = new PrintWriter(socket.getOutputStream(),true);
            is = new DataInputStream(socket.getInputStream());
            inputLine = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
        }
    }

    public void SetOpponent() {
        if (clientNumber == 1) {
            opponentNumber = 2;
        } else {
            opponentNumber = 1;
        }
    }
    //This is essentially your console inputs being sent to server!
    public void startMancala() {
        try {
            while (true) {
                while (isTurn) {
                    System.out.println("Pick a spot 1 - 6: ");
                    String userResponse = inputLine.readLine();
                    while (myGame.playerMove(clientNumber, Integer.parseInt(userResponse))) {
                        System.out.println("Extra Turn!");
                        os.println("OPPONENT " + userResponse);
                        userResponse = inputLine.readLine();
                    }
                    os.println("OPPONENT " + userResponse);
                    os.println("DONE");
                    System.out.println("Opponents Turn...");
                    isTurn = false;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String responseLine;
        try {
            while ((responseLine = is.readLine()) != null) {
                if (responseLine.startsWith("OPPONENT")) {
                    myGame.playerMove(opponentNumber,Integer.parseInt(responseLine.substring(9)));
                } else if (responseLine.startsWith("DONE")) {
                    isTurn = true;
                    startMancala();
                } else if(responseLine.startsWith("DISPLAY")) {
                    myGame.ShowBoard();
                } else {
                    System.out.println(responseLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
