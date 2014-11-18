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

    private Road road;

    private BufferedImage background;

    private BufferedImage[] leftLines;

    private BufferedImage[] rightLines;

    private int lineHeight;
    private int lineWidth;

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
        //road = new Road();
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

    public void restart(){
        playerOne.reset();
    }

    public void updateGame(long gameTime, Point mousePosition){
        playerOne.update();

    }

    public void Draw(Graphics2D g2d, Point mousePosition){
        g2d.drawImage(background, 0, 0, GameLogic.frameWidth, GameLogic.frameHeight, null);
        g2d.drawImage(leftLines[0], GameLogic.frameWidth/3-lineWidth/2, 0, lineWidth, lineHeight, null);
        g2d.drawImage(leftLines[1], GameLogic.frameWidth/3-lineWidth/2, -1*lineHeight, lineWidth, lineHeight, null);
        g2d.drawImage(rightLines[0], 2*GameLogic.frameWidth/3-lineWidth/2, 0, lineWidth, lineHeight, null);
        g2d.drawImage(rightLines[1], 2*GameLogic.frameWidth/3-lineWidth/2, -1*lineHeight, lineWidth, lineHeight, null);
        //road.Draw(g2d);
        playerOne.Draw(g2d);
    }

    public void DrawEnd(Graphics2D g2d, Point mousePosition){
        Draw(g2d, mousePosition);
        g2d.drawString("GAME OVER", GameLogic.frameWidth/2 - 50, GameLogic.frameHeight/2 + 50);
        g2d.drawString("Press any key to restart", GameLogic.frameWidth/2 - 100, GameLogic.frameHeight/2 + 70);

    }
}
