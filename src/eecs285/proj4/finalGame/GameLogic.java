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
import java.util.Random;
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

    public static final long nanosPerMili = nanosPerSecond/1000L;

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
         URL menuImagePath = this.getClass().getResource("/images/road.jpg");
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
        boolean obstacleAdded = false;

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
                    if(timeInSeconds % 2 == 0 && !obstacleAdded){
                        obstacleAdded = true;
                        int nextObstacleType = randomGenerator.nextInt(2);
                        game.addObstacle(nextObstacleType);
                    }else if(timeInSeconds % 2 != 0){
                        obstacleAdded = false;
                    }
                    game.updateGame();
                    break;
                case ENDED:
                    break;
                case CRASHED:
                    break;

            }
            repaint();
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
        switch (gameState){
            case LOADING:
                break;
            case MENU:
                g2d.drawImage(menuImage, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.white);
                g2d.drawString("Press any key to start", GameLogic.frameWidth/2 - 70, GameLogic.frameHeight/2);
                break;
            case OPTIONS:
                break;
            case PLAYING:
                game.Draw(g2d);
                g2d.drawString("Time Taken: " + elapsedTime/nanosPerSecond , 0, 50);
                break;
            case ENDED:
                game.DrawEnd(g2d);
                break;
            case CRASHED:
                break;
        }
    }

    private void newGame(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        game = new Game();
    }

    private void restart(){
        elapsedTime = 0;
        prevTime = System.nanoTime();

        game.restart();

        gameState = GameState.PLAYING;
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
    @Override
    public void keyReleasedLogic(KeyEvent event){
        switch(gameState){
            case MENU:
                GameCanvas.option.setVisible(false);
//                if(GameCanvas.diffChoice.getSelectedIndex() == 0)
//                {
//                    //Easy Game
//                    System.out.println("Easy Game");
//                }
//                else if(GameCanvas.diffChoice.getSelectedIndex() == 1)
//                {
//                    //Medium Game
//                    System.out.println("Medium Game");
//
//                }
//                else
//                {
//                    //Hard Game
//                    System.out.println("Hard Game");
//                }

                newGame();
                break;
            case ENDED:
                restart();
                break;
        }
    }


}
