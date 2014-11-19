package eecs285.proj4.finalGame;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Alex on 11/18/14.
 */
public class Obstacle {

    private int xPos;
    private int yPos;
    protected int obstacleHeight;
    protected int obstacleWidth;

    protected BufferedImage obstacleImage;

    Obstacle(){
        xPos = GameLogic.frameWidth/3 - 60;
        yPos = 0;
    }

    public void update(Graphics2D g2d){
        Draw(g2d);

    }

    public void Draw(Graphics2D g2d){
        g2d.drawImage(obstacleImage, xPos, yPos, obstacleWidth, obstacleHeight, null);
    }
}
