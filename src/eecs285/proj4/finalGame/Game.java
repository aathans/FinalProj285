package eecs285.proj4.finalGame;

import sun.rmi.runtime.Log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Created by Alex on 11/17/14.
 */
public class Game {

    private Player playerOne;

    private Obstacle[] obstacleList;

    private int numObstacles;

    private BufferedImage background;

    private BufferedImage[] leftLines;

    private BufferedImage[] rightLines;

    private int lineHeight;
    private int lineWidth;
    private int lineTop;

    private int lineSpeed;

    public Game(){
        GameLogic.gameState = GameLogic.GameState.LOADING;

        Thread initializeGame = new Thread(){
            @Override
            public void run() {
                setup();
                loadGame();
                GameLogic.gameState = GameLogic.GameState.PLAYING;
            }
        };
        initializeGame.start();
    }

    private void setup(){
        leftLines = new BufferedImage[2];
        rightLines = new BufferedImage[2];
        playerOne = new Player();

        obstacleList = new Obstacle[3];
        numObstacles = 0;

        lineSpeed = 0;
        lineTop = 0;
    }

    private void loadGame(){
        try{
            URL backgroundPath = this.getClass().getResource("/images/road.jpg");
            background  = ImageIO.read(backgroundPath);

            URL linesImagePath = this.getClass().getResource("/images/lines.jpg");
            leftLines[0] = ImageIO.read(linesImagePath);
            leftLines[1] = ImageIO.read(linesImagePath);
            rightLines[0] = ImageIO.read(linesImagePath);
            rightLines[1] = ImageIO.read(linesImagePath);
            lineHeight = leftLines[0].getHeight();
            lineWidth = leftLines[0].getWidth();

        } catch(IOException ex){
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addObstacle(int obstacleType){
        switch(obstacleType){
            case 0:
                obstacleList[numObstacles] = new CarObstacle();
                break;
            case 1:
                obstacleList[numObstacles] = new WallObstacle();
                break;
        }
    }

    public void restart(){
        playerOne.reset();
    }

    public void updateGame(){
        playerOne.update();
        lineSpeed = playerOne.getSpeedY();
        for(Obstacle o : obstacleList) {
            o.update();
        }
        System.out.println(lineSpeed);
    }

    public void Draw(Graphics2D g2d){
        lineTop += lineSpeed;
        if(lineTop > GameLogic.frameHeight + 14){
            lineTop = 0;
        }
        g2d.drawImage(background, 0, 0, GameLogic.frameWidth, GameLogic.frameHeight, null);
        g2d.drawImage(leftLines[0], GameLogic.frameWidth/3-lineWidth/2, lineTop, lineWidth, lineHeight, null);
        g2d.drawImage(leftLines[1], GameLogic.frameWidth/3-lineWidth/2, -1*lineHeight + lineTop, lineWidth, lineHeight, null);
        g2d.drawImage(rightLines[0], 2*GameLogic.frameWidth/3-lineWidth/2, lineTop, lineWidth, lineHeight, null);
        g2d.drawImage(rightLines[1], 2*GameLogic.frameWidth/3-lineWidth/2, -1*lineHeight + lineTop, lineWidth, lineHeight, null);
        //road.Draw(g2d);
        playerOne.Draw(g2d);
    }

    public void DrawEnd(Graphics2D g2d){
        Draw(g2d);
        g2d.drawString("GAME OVER", GameLogic.frameWidth/2 - 50, GameLogic.frameHeight/2 + 50);
        g2d.drawString("Press any key to restart", GameLogic.frameWidth/2 - 100, GameLogic.frameHeight/2 + 70);

    }
}
