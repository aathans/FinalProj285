package eecs285.proj4.finalGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by Alex on 11/18/14.
 */
public class Obstacle {

    private int xPos;
    private int yPos;
    public boolean inPlay;

    protected int obstacleHeight;
    protected int obstacleWidth;
    protected BufferedImage obstacleImage;
    protected int startLane;

    private Random randomGenerator;

    Obstacle(){
        randomGenerator = new Random();
        startLane = randomGenerator.nextInt(3);
        xPos = GameLogic.frameWidth/3*startLane + 30;
        yPos = -125;
        inPlay = false;
    }

    public void update(Graphics2D g2d, int speed){
        yPos += speed;
        if(yPos > GameLogic.frameHeight){
            startLane = randomGenerator.nextInt(3);
            xPos = GameLogic.frameWidth/3*startLane + 30;
            yPos = -125;
            inPlay = false;
        }
        Draw(g2d);
    }

    public void putInPlay(){
        inPlay = true;
    }

    public void Draw(Graphics2D g2d){
        g2d.drawImage(obstacleImage, xPos, yPos, obstacleWidth, obstacleHeight, null);
    }
}
