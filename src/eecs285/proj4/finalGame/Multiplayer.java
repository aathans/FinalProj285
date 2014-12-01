package eecs285.proj4.finalGame;

/**
 * Created by Sid on 11/30/14.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Multiplayer {

    String ipAddr;
    int port;

    PrintWriter gameWriter;
    BufferedReader gameReader;

    public Multiplayer(String servIPAddr, int servPort){

        this.ipAddr = servIPAddr;
        this.port = servPort;

    }

    public void createConnection(){

        try{

            Socket sock = new Socket(InetAddress.getByName(ipAddr), port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            System.out.println("HERE3");
            String command = reader.readLine();
            System.out.println("VVVV");
            System.out.println("Value is: " + command);

            Socket gameSocket;

            if(command.equals("CREATE")){

                // creates a listening socket and accepts the first incoming connection
                ServerSocket servSocket = new ServerSocket(port);
                gameSocket = servSocket.accept();
                System.out.println("Creating");
                servSocket.close();

            } else {

                // connects to the specified IP address
                gameSocket = new Socket(InetAddress.getByName(reader.readLine()), port);
                System.out.println("Connecting");


            }

            sock.close();

            gameReader = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
            gameWriter = new PrintWriter(gameSocket.getOutputStream());

            new ReadingThread(gameReader, this).start();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void receiveUpdate(String score){

        // do whatever you want here. This is where the opponent score comes in

    }

    public void sendUpdate(String update){
        gameWriter.write(update);
        gameWriter.flush();
    }

    public class ReadingThread extends Thread{

        BufferedReader gameReader;
        Multiplayer p;

        public ReadingThread(BufferedReader gameReader, Multiplayer p){

            this.p = p;
            this.gameReader = gameReader;

        }

        public void run(){

            while(true)
            {
                try{

                    String updatedScore = gameReader.readLine();
                    p.sendUpdate(updatedScore);

                } catch (IOException e){
                    e.printStackTrace();
                }
            }

        }
    }


    public static void main(String[] args){

        System.out.println("HERE1");

        Multiplayer p = new Multiplayer("127.0.0.1", 2000);
        System.out.println("HERE2");

        p.createConnection();

    }

}
