package eecs285.proj4.finalGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by Alex on 11/18/14.
 * The base class for obstacles that can end the game if a player collides
 * with them.
 */
public class Obstacle
{

  private int xPos;
  private int yPos;
  public boolean inPlay;

  protected int obstacleHeight;
  protected int obstacleWidth;
  protected BufferedImage obstacleImage;
  protected int startLane;

  private Random randomGenerator;

  Obstacle()
  {
    randomGenerator = new Random();
    reset();
  }

  public void reset()
  {
    startLane = randomGenerator.nextInt(3);
    xPos = GameLogic.frameWidth / 3 * startLane + 30;
    yPos = -125;
    inPlay = false;
  }

  public void update(Graphics2D g2d, int speed)
  {
    yPos += speed;
    if( yPos > GameLogic.frameHeight )
    {
      reset();
    }
    Draw(g2d);
  }

  private void Draw(Graphics2D g2d)
  {
    g2d.drawImage(obstacleImage, xPos, yPos, obstacleWidth, obstacleHeight,
        null);
  }

  public boolean checkForCollision(int inX, int inY, int inWidth, int inHeight)
  {

    if( (inX + inWidth - 10 < xPos) || (xPos + obstacleWidth - 10 < inX) )
    {
      return false;
    }
    if( (inY + inHeight - 10 < yPos) || (yPos + obstacleHeight - 10 < inY) )
    {
      return false;
    }

    return true;
  }
}
