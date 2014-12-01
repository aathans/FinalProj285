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
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * Created by Alex on 11/17/14.
 */
public class GameLogic extends GameCanvas {

    public static int frameWidth;

    public static int frameHeight;

    public static final long nanosPerSecond = 1000000000L;

    public static final long nanosPerMili = nanosPerSecond/1000L;

    private final int FPS = 125;

    private final long UPDATE_DELAY = nanosPerSecond / FPS;

    public static enum GameState {STARTING, SHOWING, LOADING, MENU, OPTIONS, PLAYING, ENDED, CRASHED, RACE, MULTIPLAYER,SETTINGS,SCORE,QUIT,SETTINGS1,MULTIPLAYER1,FREQUENCY,CAR,HELP,BACK,FREQUENCY1,CAR1,HELP1,SCORE1,HIGH1,MEDIUM1,LOW1}

    public static GameState gameState;

    //Nanos
    private long elapsedTime;

    private long prevTime;

    private Game game;

    private gameSettings settings;

    private BufferedImage menuScreen;

    private SongPlayer songPlayer;

    private BufferedImage menuScreen1,menuScreen2,menuScreen3,menuScreen4,menuScreen5,settings1,settings2,settings3,settings4,settings5,frequency1,frequency2,frequency3;

    private Frequency frequencies;

    private Car car;

    private Help help;

    private Scores scores;

    public int frequency;

    public static JPanel panel;
    public static JLabel label;


    public GameLogic(){

        super();

        gameState = GameState.SHOWING;
        songPlayer = new SongPlayer("/FuzzionOutlaws.wav");
        Thread mainThread = new Thread(){
            @Override
            public void run(){
               listenGame();
            }
        };

        mainThread.start();
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip backgroundSong = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            this.getClass().getResourceAsStream(url));
                    backgroundSong.open(inputStream);
                    backgroundSong.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception e) {
                    System.out.println("Error with background song: " + e.getMessage());
                }
            }
        }).start();
    }

    private void setup(){

    }

    private void loadMenu(){

     try{
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

     } catch(IOException ex){
         Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
     }

    }

    private void listenGame(){

        long showingTime = 0;
        long lastShowingTime = System.nanoTime();
        long beginTime;
        long timeTaken;
        long timeLeft;
        boolean obstacleAdded = false;
        boolean powerupAdded = false;
        boolean collision = false;

        Random randomGenerator  = new Random();
        while(true){
            beginTime = System.nanoTime();

            switch(gameState){
                case STARTING:
                    setup();
                    loadMenu();
                    gameState = GameState.MENU;
                    break;
                case SHOWING:
                    if(this.getWidth() > 1 && showingTime > nanosPerSecond){
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        gameState = GameState.STARTING;
                    }else{
                        showingTime += System.nanoTime() - lastShowingTime;
                        lastShowingTime = System.nanoTime();
                    }
                    break;
                case LOADING:
                    break;
                case MENU:
                    break;
                case OPTIONS:
                    break;
                case PLAYING:
                    elapsedTime += System.nanoTime() - prevTime;
                    prevTime = System.nanoTime();
                    long timeInSeconds = elapsedTime/nanosPerSecond;
                    if(timeInSeconds == 0){
                        break;
                    }
                    if(timeInSeconds % 2 == 0 && !obstacleAdded){
                        obstacleAdded = true;
                        int nextObstacleType = randomGenerator.nextInt(2);
                        game.addObstacle(nextObstacleType);
                    }else if(timeInSeconds % 2 != 0){
                        obstacleAdded = false;
                    }

                    if(timeInSeconds % 9 == 0 && !powerupAdded){
                        powerupAdded = true;
                        game.addPowerUp();
                    }else if(timeInSeconds % 9 != 0){
                        powerupAdded = false;
                    }

                    collision = game.updateGame();

                    break;
                case ENDED:
                    break;
                case CRASHED:
                    restart();
                    gameState = GameState.ENDED;
                    break;

            }
            repaint();
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (UPDATE_DELAY - timeTaken)/nanosPerMili;

            if (collision){
                collision = false;
                gameState = GameState.CRASHED;
                repaint();
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException ex){

                }
            }

            if(timeLeft < 10){
                timeLeft = 10;
            }

            try{
                Thread.sleep(timeLeft);
            } catch(InterruptedException ex){

            }
        }

    }

    @Override
    public void Draw(Graphics2D g2d){
        switch (gameState){
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
            case MULTIPLAYER1:
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
            case HIGH1:
                g2d.setColor(Color.white);
                g2d.drawImage(frequency1,0,0,frameWidth,frameHeight,null);
                break;
            case MEDIUM1:
                g2d.setColor(Color.white);
                g2d.drawImage(frequency2,0,0,frameWidth,frameHeight,null);
                break;
            case LOW1:
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
    public void mousePressed(MouseEvent event){
        Point b = mousePos();
        int xVal = (int) b.getX();
        int yVal = (int) b.getY();

        System.out.println(xVal);
        System.out.println(yVal);

        switch(gameState) {
            case MENU:
             if (xVal >= 121 && xVal <= 384 && yVal >= 119 && yVal <= 174) {
                gameState = GameState.RACE;
                 System.out.println("No!");
             }
             else if (xVal >= 121 && xVal <= 384 && yVal >= 200 && yVal <= 254 ) {
                 gameState = GameState.MULTIPLAYER;
             }
             else if (xVal >= 121 && xVal <= 384 && yVal >= 286 && yVal <= 341) {
                 gameState = GameState.SETTINGS;
             }
             else if(xVal >= 121 && xVal <= 384 && yVal >= 384 && yVal <= 442){
                gameState = GameState.SCORE;
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
                else if(xVal>=88 && xVal <= 426 && yVal >= 335    && yVal <= 396){
                  gameState = GameState.HELP;
              }
                else if(xVal>=97 && xVal <= 425 && yVal >=  453 && yVal<= 511){
                  gameState = GameState.BACK;
              }
                break;
            case FREQUENCY1:
                if(xVal>=107 && xVal <= 434 && yVal >= 161 && yVal<= 235){
                   gameState = GameState.HIGH1;
                }
                if(xVal>=113 && xVal <= 426 && yVal >= 285 && yVal <= 359){
                    gameState = GameState.MEDIUM1;
                }
                if(xVal>=122 && xVal <= 421 && yVal >= 420 && yVal <= 471){
                    gameState = GameState.LOW1;
                }
                if(xVal >= 107 && xVal <= 434 && yVal >= 558 && yVal <= 614){
                    gameState = GameState.SETTINGS1;
                }
                break;
            case CAR1:

                break;
            case HELP1:
                if(xVal >= 122 && xVal <= 375 && yVal >= 534 && yVal <= 595){
                    gameState = GameState.SETTINGS1;
                }
                break;
            case SCORE1:
                if(xVal >= 84 && xVal <= 414 && yVal >= 526 && yVal <= 580){
                    gameState = GameState.MENU;
                }
                break;
         }
    }

    public void mouseReleased(MouseEvent event){
        Point b = mousePos();
        int xVal = (int) b.getX();
        int yVal = (int) b.getY();

        switch(gameState) {
           case RACE:
               newGame();
               break;
           case MULTIPLAYER:

               break;
           case SCORE:
               newScores();
               break;
           case SETTINGS:
               Settings();
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
           case HELP:
               newHelp();
               break;
           case BACK:
               gameState = GameState.MENU;
               break;
           case HIGH1:
               gameState = GameState.SETTINGS1;
               break;
            case MEDIUM1:
                gameState = GameState.SETTINGS1;
                break;
            case LOW1:
                gameState = GameState.SETTINGS1;
                break;
        }
    }

    private void newGame(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        game = new Game();
    }

    private void newScores(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        panel = new JPanel();
        label = new JLabel("This is it!");
        panel.add(label);
        this.add(panel);

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

    private void multiplayer1(){
        int selectedOption = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to switch to singlePlayer?",
                "Choose",
                JOptionPane.YES_NO_OPTION);
        if(selectedOption == JOptionPane.YES_OPTION){
            gameState = GameState.MULTIPLAYER1;
            return;
        }
        gameState = GameState.MENU;
    }

    private void newFrequencies(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        frequencies = new Frequency();
    }

    private void Settings(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        settings = new gameSettings();
    }

    private void restart(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        game.restart();

        gameState = GameState.PLAYING;
    }

    @Override
    public void keyReleasedLogic(KeyEvent event){
        switch(gameState){
            case MENU:
                newGame();
                break;
            case ENDED:
                //restart();
                break;
        }
    }

    @Override
    public void keyPressedLogic(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.VK_P){
            SongPlayer.toggleSound();
        }

        switch(gameState){
            case PLAYING:
                if(event.getKeyCode() == KeyEvent.VK_SPACE){
                    game.usePowerUp();
                }
                break;
            case ENDED:
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException e){

                }
                restart();
                break;
        }
    }

    private void quitGame(){
        System.exit(1);
    }

    private Point mousePos(){
        try{
            Point mousePointer = this.getMousePosition();
            if(mousePointer != null){
                return mousePointer;
            }else{
                return new Point(0,0);
            }
        }catch (Exception event){
            return new Point(0,0);
        }
    }

}


