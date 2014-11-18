package eecs285.proj4.finalGame;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Created by Alex on 11/17/14.
 */
public class GameLogic extends GameCanvas {
    public static int frameWidth;

    public static int frameHeight;

    public static final long nanosPerSecond = 1000000000L;

    public static final long millisPerSecond = 1000000L;

    private final int FPS = 20;

    private final long UPDATE_DELAY = nanosPerSecond / FPS;

    public static enum GameState {STARTING, SHOWING, LOADING, MENU, OPTIONS, PLAYING, ENDED, CRASHED}

    public static GameState gameState;

    //Nanos
    private long elapsedTime;

    private long prevTime;

    private Game game;

    private BufferedImage menuImage;

    public GameLogic(){
        super();
        gameState = GameState.SHOWING;

        Thread mainThread = new Thread(){
            @Override
            public void run(){
               listenGame();
            }
        };
        mainThread.start();
    }

    private void setup(){

    }

    private void loadMenu(){

     try{
         URL menuImagePath = this.getClass().getResource("/resources/menu.jpg");
         menuImage = ImageIO.read(menuImagePath);
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

        while(true){
            beginTime = System.nanoTime();

            switch(gameState){
                case STARTING:
                    setup();
                    loadMenu();
                    gameState = GameState.MENU;
                    break;
                case SHOWING:
                    break;
                case LOADING:
                    break;
                case MENU:
                    break;
                case OPTIONS:
                    break;
                case PLAYING:
                    break;
                case ENDED:
                    break;
                case CRASHED:
                    break;

            }

            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (UPDATE_DELAY - timeTaken)/nanosPerSecond;

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

    }

    @Override
    public void keyReleasedLogic(KeyEvent event){

    }


}
