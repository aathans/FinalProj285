package eecs285.proj4.finalGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.sql.Connection;

/**
 * Created by Alex on 11/17/14.
 */
public class GameLogic extends GameCanvas
{
  public static Connection conn;

  public static int frameWidth;

  public static int frameHeight;

  public static final long nanosPerSecond = 1000000000L;

  public static final long nanosPerMili = nanosPerSecond / 1000L;

  private final int FPS = 125;

  private final long UPDATE_DELAY = nanosPerSecond / FPS;

  public static enum GameState
  {
    STARTING, SHOWING, LOADING, MENU, OPTIONS, PLAYING, ENDED, CRASHED, RACE,
    MULTIPLAYER, SETTINGS, SCORE, QUIT, SETTINGS1, FREQUENCY, FREQUENCY1, HIGH,
    MEDIUM, LOW, HELP1, HELP, CAR, CAR1, BACK, SCORE1, GREEN, RED, YELLOW
  }

  public static GameState gameState;

  //Nanos
  private long elapsedTime;

  private long prevTime;

  private Game game;

  private String playerName;

  private int playerScore;

  private GameSettings settings;

  private BufferedImage menuScreen;

  private SongPlayer songPlayer;

  private BufferedImage menuScreen1, menuScreen2, menuScreen3, menuScreen4,
      menuScreen5, settings1, settings2, settings3, settings4,
      frequency1, frequency2, frequency3, car1, car2, car3;

  private Multiplayer p;

  private boolean isMultiplayer;

  private Frequency frequencies;
  private Car car;
  private Help help;
  private Scores scores;

  private int UPGRADE_FREQUENCY = 9;
  private int CAR_COLOUR = 0; //0 = yellow, 1 = red, 2 = green

  private DbEntry[] entries;

  private boolean offlineMode;

  public GameLogic()
  {

    super();

    gameState = GameState.SHOWING;
    songPlayer = new SongPlayer("/FuzzionOutlaws.wav");

    playerName = JOptionPane.showInputDialog("Enter a username:");

    Thread mainThread = new Thread()
    {
      @Override
      public void run()
      {
        listenGame();
      }
    };
    mainThread.start();
  }

  private void setup()
  {
    isMultiplayer = false;
    conn = Database.establishConnection();
    if(conn != null)
    {
      entries = Database.populateScores();
    }else{
      offlineMode = true;
    }
  }

  private void loadMenu()
  {

    try
    {
      URL menuScreenPath = this.getClass().getResource("/images/menuScreen.png");
      menuScreen = ImageIO.read(menuScreenPath);
      URL menuScreenRace = this.getClass().getResource("/images/menuScreen1.png");
      menuScreen1 = ImageIO.read(menuScreenRace);
      URL menuScreenMultiplayer = this.getClass().getResource("/images/menuScreen2.png");
      menuScreen2 = ImageIO.read(menuScreenMultiplayer);
      URL menuScreenSettings = this.getClass().getResource("/images/menuScreen5.png");
      menuScreen3 = ImageIO.read(menuScreenSettings);
      URL menuScreenScores = this.getClass().getResource("/images/menuScreen3.png");
      menuScreen4 = ImageIO.read(menuScreenScores);
      URL menuScreenQuit = this.getClass().getResource("/images/menuScreen4.png");
      menuScreen5 = ImageIO.read(menuScreenQuit);
      URL settingsFrequency = this.getClass().getResource("/images/settings1.png");
      settings1 = ImageIO.read(settingsFrequency);
      URL settingsCars = this.getClass().getResource("/images/settings2.png");
      settings2 = ImageIO.read(settingsCars);
      URL settingsHelp = this.getClass().getResource("/images/settings3.png");
      settings3 = ImageIO.read(settingsHelp);
      URL settingsBack = this.getClass().getResource("/images/settings4.png");
      settings4 = ImageIO.read(settingsBack);
      URL frequencyHigh = this.getClass().getResource("/images/frequency1.png");
      frequency1 = ImageIO.read(frequencyHigh);
      URL frequencyMedium = this.getClass().getResource("/images/frequency2.png");
      frequency2 = ImageIO.read(frequencyMedium);
      URL frequencyLow = this.getClass().getResource("/images/frequency3.png");
      frequency3 = ImageIO.read(frequencyLow);
      URL carGreen = this.getClass().getResource("/images/car1.png");
      car1 = ImageIO.read(carGreen);
      URL carRed = this.getClass().getResource("/images/car4.png");
      car2 = ImageIO.read(carRed);
      URL carYellow = this.getClass().getResource("/images/car5.png");
      car3 = ImageIO.read(carYellow);
    }
    catch( IOException ex )
    {
      Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  private void listenGame()
  {

    long showingTime = 0;
    long lastShowingTime = System.nanoTime();
    long beginTime;
    long timeTaken;
    long timeLeft;
    boolean obstacleAdded = false;
    boolean powerupAdded = false;
    boolean collision = false;

    Random randomGenerator = new Random();
    while( true )
    {
      beginTime = System.nanoTime();

      switch ( gameState )
      {
        case STARTING:
          setup();
          loadMenu();
          gameState = GameState.MENU;
          break;
        case SHOWING:
          if( this.getWidth() > 1 && showingTime > nanosPerSecond )
          {
            frameWidth = this.getWidth();
            frameHeight = this.getHeight();
            gameState = GameState.STARTING;
          }
          else
          {
            showingTime += System.nanoTime() - lastShowingTime;
            lastShowingTime = System.nanoTime();
          }
          break;
        case MULTIPLAYER:
          repaint();
          newGame();
          break;
        case PLAYING:
          elapsedTime += System.nanoTime() - prevTime;
          prevTime = System.nanoTime();
          long timeInSeconds = elapsedTime / nanosPerSecond;
          if( timeInSeconds == 0 )
          {
            break;
          }
          if( timeInSeconds % 2 == 0 && !obstacleAdded )
          {
            obstacleAdded = true;
            int nextObstacleType = randomGenerator.nextInt(2);
            game.addObstacle(nextObstacleType);
          }
          else if( timeInSeconds % 2 != 0 )
          {
            obstacleAdded = false;
          }

          if( timeInSeconds % UPGRADE_FREQUENCY == 0 && !powerupAdded )
          {
            powerupAdded = true;
            game.addPowerUp();
          }
          else if( timeInSeconds % UPGRADE_FREQUENCY != 0 )
          {
            powerupAdded = false;
          }

          collision = game.updateGame();

          break;
        case ENDED:
          repaint();
          try
          {
            Thread.sleep(3000);
          }
          catch( InterruptedException ex )
          {

          }
          if( isMultiplayer )
          {
            gameState = GameState.MENU;
          }
          break;
        case CRASHED:
          if( isMultiplayer )
          {
            if( game.opponentHasCrashed() )
            {
              if(!offlineMode)
              {
                playerScore = game.getPlayerScore();
                Database.insertScore(playerName, playerScore);
              }
              gameState = GameState.ENDED;
            }
          }
          else
          {
            if(!offlineMode)
            {
              playerScore = game.getPlayerScore();
              Database.insertScore(playerName, playerScore);
            }
            gameState = GameState.ENDED;
          }
          break;

      }
      repaint();
      timeTaken = System.nanoTime() - beginTime;
      timeLeft = (UPDATE_DELAY - timeTaken) / nanosPerMili;

      if( collision )
      {
        collision = false;
        gameState = GameState.CRASHED;
        repaint();
        try
        {
          Thread.sleep(1000);
        }
        catch( InterruptedException ex )
        {

        }
      }

      if( timeLeft < 10 )
      {
        timeLeft = 10;
      }

      try
      {
        Thread.sleep(timeLeft);
      }
      catch( InterruptedException ex )
      {

      }
    }

  }

  @Override
  public void Draw(Graphics2D g2d)
  {
    switch ( gameState )
    {
      case LOADING:
        break;
      case MENU:
        g2d.setColor(Color.white);
        g2d.drawImage(menuScreen,0,0,frameWidth,frameHeight,null);
        break;
      case RACE:
        g2d.setColor(Color.white);
        g2d.drawImage(menuScreen1,0,0,frameWidth,frameHeight,null);
        break;
      case MULTIPLAYER:
        g2d.setColor(Color.white);
        g2d.drawImage(menuScreen2,0,0,frameWidth,frameHeight,null);
        break;
      case SETTINGS:
        g2d.setColor(Color.white);
        g2d.drawImage(menuScreen3,0,0,frameWidth,frameHeight,null);
        break;
      case SETTINGS1:
        settings.Draw(g2d);
        break;
      case FREQUENCY:
        g2d.setColor(Color.white);
        g2d.drawImage(settings1,0,0,frameWidth,frameHeight,null);
        break;
      case FREQUENCY1:
        frequencies.Draw(g2d);
        break;
      case HIGH:
        g2d.setColor(Color.white);
        g2d.drawImage(frequency1,0,0,frameWidth,frameHeight,null);
        break;
      case MEDIUM:
        g2d.setColor(Color.white);
        g2d.drawImage(frequency2,0,0,frameWidth,frameHeight,null);
        break;
      case LOW:
        g2d.setColor(Color.white);
        g2d.drawImage(frequency3,0,0,frameWidth,frameHeight,null);
        break;
      case CAR:
        g2d.setColor(Color.white);
        g2d.drawImage(settings2,0,0,frameWidth,frameHeight,null);
        break;
      case CAR1:
        car.Draw(g2d);
        break;
      case GREEN:
        g2d.setColor(Color.white);
        g2d.drawImage(car1,0,0,frameWidth,frameHeight,null);
        break;
      case RED:
        g2d.setColor(Color.white);
        g2d.drawImage(car2,0,0,frameWidth,frameHeight,null);
        break;
      case YELLOW:
        g2d.setColor(Color.white);
        g2d.drawImage(car3,0,0,frameWidth,frameHeight,null);
        break;
      case HELP:
        g2d.setColor(Color.white);
        g2d.drawImage(settings3,0,0,frameWidth,frameHeight,null);
        break;
      case HELP1:
        help.Draw(g2d);
        break;
      case BACK:
        g2d.setBackground(Color.white);
        g2d.drawImage(settings4,0,0,frameWidth,frameHeight,null);
        break;
      case SCORE:
        g2d.setColor(Color.white);
        g2d.drawImage(menuScreen4,0,0,frameWidth,frameHeight,null);
        break;
      case SCORE1:
        scores.Draw(g2d);
        break;
      case QUIT:
        g2d.setColor(Color.white);
        g2d.drawImage(menuScreen5,0,0,frameWidth,frameHeight,null);
        break;
      case OPTIONS:
        break;
      case PLAYING:
        game.Draw(g2d);
        g2d.drawString("Time Taken: " + elapsedTime/nanosPerSecond , 5, 55);
        break;
      case ENDED:
        game.DrawEnd(g2d);
        break;
      case CRASHED:
        game.DrawCrashed(g2d);
        break;
    }
  }


  @Override
  public void mousePressed(MouseEvent event)
  {
    Point b = mousePos();
    int xVal = (int) b.getX();
    int yVal = (int) b.getY();

    switch ( gameState )
    {
      case MENU:
        if( xVal >= 121 && xVal <= 384 && yVal >= 119 && yVal <= 174 )
        {
          isMultiplayer = false;
          gameState = GameState.RACE;
        }
        else if( xVal >= 121 && xVal <= 384 && yVal >= 200 && yVal <= 254 )
        {
          if(!offlineMode)
          {
            isMultiplayer = true;
            gameState = GameState.MULTIPLAYER;
          }
        }
        else if (xVal >= 121 && xVal <= 384 && yVal >= 286 && yVal <= 341) {
          gameState = GameState.SETTINGS;
        }
        else if(xVal >= 121 && xVal <= 384 && yVal >= 384 && yVal <= 442){
          if(!offlineMode)
          {
            gameState = GameState.SCORE;
          }
        }
        else if(xVal>= 2 && xVal <= 231 && yVal >= 660 && yVal <= 708){
          gameState = GameState.QUIT;
        }
        break;

      case SETTINGS1:
        if(xVal>=97 && xVal <= 408 && yVal >= 123 && yVal <= 186){
          gameState = GameState.FREQUENCY;
        }
        else if(xVal>=89 && xVal <= 419 && yVal >= 229 && yVal <= 292){
          gameState = GameState.CAR;
        }
        else if(xVal>=88 && xVal <= 426 && yVal >= 335 && yVal <= 396){
          gameState = GameState.HELP;
        }
        else if(xVal>=97 && xVal <= 425 && yVal >= 453 && yVal<= 511){
          gameState = GameState.BACK;
        }
        break;

      case FREQUENCY1:
        //High Frequency
        if(xVal>=107 && xVal <= 434 && yVal >= 161 && yVal<= 235){
          UPGRADE_FREQUENCY = 5;
          gameState = GameState.SETTINGS1;
        }

        //Medium Frequency
        if(xVal>=113 && xVal <= 426 && yVal >= 285 && yVal <= 359){
          UPGRADE_FREQUENCY = 9;
          gameState = GameState.SETTINGS1;
        }

        //Low Frequency
        if(xVal>=122 && xVal <= 421 && yVal >= 420 && yVal <= 471){
          UPGRADE_FREQUENCY = 15;
          gameState = GameState.SETTINGS1;
        }

        //Back to Settings Menu
        if(xVal >= 107 && xVal <= 434 && yVal >= 558 && yVal <= 614){
          gameState = GameState.SETTINGS1;
        }
        break;

      case CAR1:
        if(xVal >= 43 && xVal <= 158 && yVal >= 154 && yVal <= 291){
          gameState = GameState.GREEN;
        }
        else if(xVal >= 317 && xVal <= 453 && yVal >= 149 && yVal <= 293){
          gameState = GameState.RED;
        }
        else if(xVal >= 174 && xVal <= 314 && yVal >= 360 && yVal <= 508){
          gameState = GameState.YELLOW;
        }
        else if(xVal >= 83 && xVal <= 418 && yVal >= 578 && yVal <= 631)
        {
          gameState = GameState.SETTINGS1;
        }
        break;

      //Help Menu Back Button
      case HELP1:
        if(xVal >= 122 && xVal <= 375 && yVal >= 534 && yVal <= 595){
          gameState = GameState.SETTINGS1;
        }
        break;

      //Score Menu Back Button
      case SCORE1:
        if(xVal >= 84 && xVal <= 414 && yVal >= 526 && yVal <= 580){
          gameState = GameState.MENU;
        }
        break;

    }
  }

  public void mouseReleased(MouseEvent event)
  {
    Point b = mousePos();
    int xVal = (int) b.getX();
    int yVal = (int) b.getY();

    switch ( gameState )
    {
      case RACE:
        newGame();
        break;
      case SETTINGS:
        Settings();
        break;
      case SCORE:
          newScores();
          entries = Database.populateScores();
          scores.updateHighScores(entries);
        break;
      case QUIT:
        quitGame();
        break;
      case FREQUENCY:
        newFrequencies();
        break;
      case CAR:
        newCars();
        break;
      case GREEN:
        CAR_COLOUR = 2;
        gameState = GameState.SETTINGS1;
        break;
      case RED:
        CAR_COLOUR = 1;
        gameState = GameState.SETTINGS1;
        break;
      case YELLOW:
        CAR_COLOUR = 0;
        gameState = GameState.SETTINGS1;
        break;
      case HELP:
        newHelp();
        break;
      case BACK:
        gameState = GameState.MENU;
        break;

    }
  }

  private void newGame()
  {
    elapsedTime = 0;
    prevTime = System.nanoTime();

    game = new Game(isMultiplayer, CAR_COLOUR);
  }

  private void newScores(){
    elapsedTime = 0;
    prevTime = System.nanoTime();
    scores = new Scores();
  }

  private void newHelp(){
    elapsedTime = 0;
    prevTime = System.nanoTime();
    help = new Help();
  }

  private void newCars(){
    elapsedTime = 0;
    prevTime = System.nanoTime();
    car = new Car();
  }

  private void newFrequencies(){
    elapsedTime = 0;
    prevTime = System.nanoTime();
    frequencies = new Frequency();
  }

  private void Settings()
  {
    elapsedTime = 0;
    prevTime = System.nanoTime();

    settings = new GameSettings();
  }

  private void restart()
  {
    elapsedTime = 0;
    prevTime = System.nanoTime();

    game.restart();

    gameState = GameState.PLAYING;
  }

  @Override
  public void keyReleasedLogic(KeyEvent event)
  {
  }

  @Override
  public void keyPressedLogic(KeyEvent event)
  {

    if( event.getKeyCode() == KeyEvent.VK_P )
    {
      SongPlayer.toggleSound();
    }

    switch ( gameState )
    {
      case PLAYING:
        if( event.getKeyCode() == KeyEvent.VK_SPACE )
        {
          game.usePowerUp();
        }
        break;
      case ENDED:
        if( !isMultiplayer )
        {
          if( event.getKeyCode() == KeyEvent.VK_Q )
          {
            gameState = GameState.MENU;
          }
          else
          {
            restart();
          }
        }
        break;
    }
  }



  private void quitGame()
  {
    System.exit(1);
  }

  private Point mousePos()
  {
    try
    {
      Point mousePointer = this.getMousePosition();
      if( mousePointer != null )
      {
        return mousePointer;
      }
      else
      {
        return new Point(0, 0);
      }
    }
    catch( Exception event )
    {
      return new Point(0, 0);
    }
  }

}


