package eecs285.proj4.finalGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Alex on 11/28/14
 * Responsible for setting up the network and
 * communication between the two players.
 */
public class Multiplayer
{

  String ipAddr;
  int port;
  int opponentScore = 0;
  int previousOpponentScore = -1;
  public boolean opponentFinished = false;

  PrintWriter gameWriter;
  BufferedReader gameReader;

  public Multiplayer(String servIPAddr, int servPort)
  {

    this.ipAddr = servIPAddr;
    this.port = servPort;

  }

  public void createConnection()
  {

    try
    {

      Socket sock = new Socket(InetAddress.getByName(ipAddr), port);

      BufferedReader reader = new BufferedReader(
          new InputStreamReader(sock.getInputStream()));

      String command = reader.readLine();

      Socket gameSocket;

      if( command.equals("CREATE") )
      {

        // creates a listening socket and accepts the first incoming connection
        ServerSocket servSocket = new ServerSocket();
        servSocket.setReuseAddress(true);
        servSocket.bind(new InetSocketAddress(4000));
        gameSocket = servSocket.accept();
        servSocket.close();

      }
      else
      {

        // connects to the specified IP address
        Thread.sleep(1000);
        gameSocket = new Socket(
            InetAddress.getByName(reader.readLine().substring(1)), 4000);


      }

      sock.close();

      gameReader = new BufferedReader(
          new InputStreamReader(gameSocket.getInputStream()));
      gameWriter = new PrintWriter(gameSocket.getOutputStream());

      new ReadingThread(gameReader, this).start();

    }
    catch( IOException e )
    {
      e.printStackTrace();
    }
    catch( InterruptedException e )
    {
      e.printStackTrace();
    }

  }

  public void receiveUpdate(String score)
  {

    // do whatever you want here. This is where the opponent score comes in
    previousOpponentScore = opponentScore;

    opponentScore = Integer.parseInt(score);

  }

  public void sendUpdate(String update)
  {
    gameWriter.write(update + "\n");
    gameWriter.flush();
  }

  public void updateFinished(boolean isFinished)
  {
    opponentFinished = isFinished;
  }

  public int getOpponentScore()
  {
    return (opponentScore == -1) ? previousOpponentScore : opponentScore;
  }

  public boolean isOpponentFinished()
  {
    return opponentFinished;
  }

  public class ReadingThread extends Thread
  {

    BufferedReader gameReader;
    Multiplayer p;

    public ReadingThread(BufferedReader gameReader, Multiplayer p)
    {

      this.p = p;
      this.gameReader = gameReader;

    }

    public void run()
    {

      while( true )
      {
        try
        {
          if( opponentScore == -1 )
          {
            p.updateFinished(true);
            break;
          }

          String updatedScore = gameReader.readLine();

          p.receiveUpdate(updatedScore);

        }
        catch( IOException e )
        {
          e.printStackTrace();
        }
      }
      interrupt();
    }
  }

}
