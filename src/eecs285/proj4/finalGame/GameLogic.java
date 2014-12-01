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

    public static enum GameState {STARTING, SHOWING, LOADING, MENU, OPTIONS, PLAYING, ENDED, CRASHED, RACE, MULTIPLAYER, SETTINGS,SCORE,QUIT,SETTINGS1}

    public static GameState gameState;

    //Nanos
    private long elapsedTime;

    private long prevTime;

    private Game game;

    private GameSettings settings;

    private BufferedImage menuScreen;

    private SongPlayer songPlayer;

    private BufferedImage menuScreen1,menuScreen2,menuScreen3,menuScreen4,menuScreen5;

    private Multiplayer p;

    private boolean isMultiplayer;

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

    private void setup(){
        isMultiplayer = false;
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
                case MULTIPLAYER:
                    repaint();
                    isMultiplayer = true;
                    newGame();
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
                    if(isMultiplayer) {
                        if(game.opponentHasCrashed()){
                            restart();
                            gameState = GameState.ENDED;
                        }
                    }else {
                        restart();
                        gameState = GameState.ENDED;
                    }
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
                g2d.drawImage(menuScreen2, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.black);
                g2d.drawString("Connecting...", frameWidth/2 - 30, frameHeight/2);
                break;
            case SETTINGS:
                g2d.setColor(Color.white);
                g2d.drawImage(menuScreen3,0,0,frameWidth,frameHeight,null);
                break;
            case SETTINGS1:
                settings.Draw(g2d);
                break;
            case SCORE:
                g2d.setColor(Color.white);
                g2d.drawImage(menuScreen4,0,0,frameWidth,frameHeight,null);
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
               //newGame();
               break;
           case SCORE:

               break;
           case SETTINGS:
               Settings();
               break;
           case QUIT:
               quitGame();
               break;
        }
    }

    private void newGame(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        game = new Game(isMultiplayer);
    }

    private void Settings(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        settings = new GameSettings();
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


