/***********************************************************
 * Created by:   Adolfo Herrera                                      
 *
 * Date:     March 30, 2019
 *
 * Purpose:  Server that will handle mancala game play communication
 ********************************************************/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerMain {

    private ServerSocket serverSocket;

    //Server can only have at max to players
    static List<ClientHandler> ar = new ArrayList<>();

    /**
     * Create a client socket(ClientHandler) for each connection.
     * Server Must have 2 clients to play.
     *
     * Reject any clients to connect if 2 are already playing. Notify that server is full and close
     * their connections
     */
    public ServerMain(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                if (ar.size() < 2) {
                    new ClientHandler(serverSocket.accept()).start();
                } else {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter os = new PrintWriter(clientSocket.getOutputStream(), true);
                    os.println("Game Lobby is full!");
                    os.close();
                    clientSocket.close();
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ClientHandler class will handle each connection to server via threads.
     * For each new client a thread is spawned.
     */
    private static class ClientHandler extends Thread {

        private final Socket clientSocket;

        private PrintWriter os; // OutputStream to client
        private BufferedReader is; //InputStream from client

        //Keep player number and opponent number
        private int playerNumber;

        /**
         * Default Constructor
         */
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.playerNumber = ar.size()+1;
        }

        @Override
        public void run() {
            try {
                //Save input and output streams for client.
                os = new PrintWriter(clientSocket.getOutputStream(), true);
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //Add client to list of clients connected to server
                ar.add(this);

                //Wait until lobby is full
//                while (ar.size() <= 1) {
//                    try {
//                        synchronized (this) {
//                            os.println("Waiting for opponent...");
//                            wait();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                ar.get(0).notify();

                os.println("Welcome To Mancala! \nYou are Player " + playerNumber);
                os.println("Waiting for opponent...");
//                synchronized (this) {
//                    while (true) {
//                        if (ar.size() == 2) {
//                            break;
//                        }
//                    }
//                }

                if (ar.size() == 2) {
                    Broadcast("DISPLAY");
                }


                while(true) {
                    String line = is.readLine();
                    if (line.startsWith("OPPONENT")) {
                        Broadcast("OPPONENT " + line.substring(9));
                    } else if (line.startsWith("DONE")) {
                        Broadcast("DONE");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void Broadcast(String msg) {
            synchronized (this) {
                for (int i = 0; i < 2; i++) {
                    if (ar.get(i) != this) {
                        ar.get(i).os.println(msg);
                    }
                }
            }
        }

    }

}
