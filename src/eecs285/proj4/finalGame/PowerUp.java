package eecs285.proj4.finalGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by Alex on 11/28/14.
 */
public class PowerUp {
    protected int xPos;
    protected int yPos;
    public boolean inPlay;
    public boolean wasRetrieved;
    public boolean wasUsed;

    protected int powerUpHeight;
    protected int powerUpWidth;
    protected BufferedImage powerUpImage;
    protected int startLane;

    private Random randomGenerator;

    PowerUp(){
        randomGenerator = new Random();
        reset();
    }

    public void reset(){
        startLane = randomGenerator.nextInt(3);
        xPos = GameLogic.frameWidth/3*startLane + 30;
        yPos = -125;
        inPlay = false;
        wasRetrieved = false;
        wasUsed = false;
    }

    public void update(Graphics2D g2d, int speed){
        yPos += speed;
        if(yPos > GameLogic.frameHeight){
            reset();
        }
        Draw(g2d);
    }

    public void setPosition(int inX, int inY){
        xPos = inX;
        yPos = inY;
    }

    protected void Draw(Graphics2D g2d){
            g2d.drawImage(powerUpImage, xPos, yPos, powerUpWidth, powerUpHeight, null);
    }

    public boolean checkForCollision(int inX, int inY, int inWidth, int inHeight){

        if((inX + inWidth < xPos) || (xPos + powerUpWidth < inX)) {
            return false;
        }
        if((inY + inHeight < yPos) || (yPos + powerUpHeight < inY)) {
            return false;
        }

        return true;
    }

    public int getxPos(){
        return xPos;
    }

    public int getyPos(){
        return yPos;
    }

    public int getPowerUpHeight(){
        return powerUpHeight;
    }

    public int getPowerUpWidth(){
        return powerUpWidth;
    }

}
