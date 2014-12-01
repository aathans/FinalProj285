package eecs285.proj4.finalGame;

import javafx.scene.transform.Rotate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 * Created by Alex on 11/17/14.
 */
public class Player
{

  private Random randX;

  private int xPos;

  private int yPos;

  public boolean crashed;

  private int speedX;

  private int speedY;

  private int score;

  private int livesRemaining;

  private int highScore;

  private BufferedImage carImage;

  private BufferedImage carCrashImage;

  public int carWidth;

  public int carHeight;

  private int carColour;

  private boolean hasPowerUp;

  private PowerUp currentPowerUp;




  public Player(final int inCarColour)
  {
    carColour = inCarColour;
    setup();
    loadPlayer();
  }

  private void setup()
  {
    reset();
    score = 0;
    livesRemaining = 3;
    highScore = 0;
  }

  private void loadPlayer()
  {
    try
    {
      setCarColor(carColour);

      URL carCrashPath = this.getClass().getResource("/images/explosion.png");
      carCrashImage = ImageIO.read(carCrashPath);

    }
    catch( IOException ex )
    {
      Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void reset()
  {
    crashed = false;
    hasPowerUp = false;
    if( highScore < score )
    {
      highScore = score;
    }
    score = 0;
    xPos = GameLogic.frameWidth / 2 - 75;
    yPos = GameLogic.frameHeight - 150;

    speedX = 0;
    speedY = 2;
  }

  public void update()
  {

    if( GameCanvas.keyboardKeyState(KeyEvent.VK_UP) && (yPos > carHeight) )
    {
      speedY = 4 + score / 5000;
    }
    else if( GameCanvas.keyboardKeyState(KeyEvent.VK_DOWN) )
    {
      speedY = 1 + score / 5000;
    }
    else
    {
      speedY = 2 + score / 5000;
    }

    if( GameCanvas.keyboardKeyState(KeyEvent.VK_RIGHT) )
    {
      speedX += 1;
    }
    else if( GameCanvas.keyboardKeyState(KeyEvent.VK_LEFT) )
    {
      speedX -= 1;
    }
    else
    {
      speedX = 0;
    }


    xPos += speedX;
    if( xPos < 0 )
    {
      xPos = 0;
      speedX = 0;
    }
    else if( xPos > GameLogic.frameWidth - carWidth )
    {
      xPos = GameLogic.frameWidth - carWidth;
      speedX = 0;
    }
    //yPos += speedY;
    score += speedY;
  }

  public void Draw(Graphics2D g2d)
  {
    //g2d.drawString("High Score: " + highScore, 5, 35);
    g2d.drawImage(carImage, xPos, yPos, carWidth, carHeight, null);

    if( crashed )
    {
      g2d.drawImage(carCrashImage, xPos, yPos, null);
    }
  }

  public void addPowerUp(PowerUp inPowerUp)
  {
    if( hasPowerUp )
    {
      currentPowerUp.reset();
    }
    hasPowerUp = true;
    inPowerUp.wasRetrieved = true;
    currentPowerUp = inPowerUp;
  }

  public PowerUp usePowerUp()
  {
    if( hasPowerUp )
    {
      MissilePowerUp missile = (MissilePowerUp) currentPowerUp;
      missile.setPosition(xPos, yPos);
      missile.wasUsed = true;
      hasPowerUp = false;
      return currentPowerUp;
    }
    return null;
  }

  public void incrementScoreBy(int value)
  {
    score += value;
  }

  public int getSpeedY()
  {
    return speedY;
  }

  public int getxPos()
  {
    return xPos;
  }

  public int getyPos()
  {
    return yPos;
  }

  public int getScore()
  {
    return score;
  }

  public int getCarWidth()
  {
    return carWidth;
  }

  public int getCarHeight()
  {
    return carHeight;
  }

  public void setCarColor(int colorIndex)
  {
    try
    {
      if( colorIndex == 0 )
      {
        URL carPath = this.getClass().getResource("/images/car.png");
        carImage = ImageIO.read(carPath);
        carHeight = carImage.getHeight();
        carWidth = carImage.getWidth();
      }
      else if( colorIndex == 1 )
      {
        URL carPath = this.getClass().getResource("/images/car2.png");
        carImage = ImageIO.read(carPath);
        carHeight = carImage.getHeight();
        carWidth = carImage.getWidth();
      }
      else
      {
        URL carPath = this.getClass().getResource("/images/car3.png");
        carImage = ImageIO.read(carPath);
        carHeight = carImage.getHeight();
        carWidth = carImage.getWidth();
      }
    }catch(IOException ex){

    }
  }
}
