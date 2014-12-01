package eecs285.proj4.finalGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alex on 11/28/14.
 * A missile power up that can be used to destroy oncoming objacles.
 */
public class MissilePowerUp extends PowerUp
{

  protected int missileFiredHeight;
  protected int missileFiredWidth;
  protected BufferedImage missileFiredImage;

  MissilePowerUp()
  {
    super();
    setup();
  }

  private void setup()
  {
    try
    {
      URL missilePath = this.getClass().getResource("/images/missile.png");
      powerUpImage = ImageIO.read(missilePath);
      powerUpHeight = powerUpImage.getHeight();
      powerUpWidth = powerUpImage.getWidth();

      URL missileFiredPath = this.getClass()
          .getResource("/images/missileFired.png");
      missileFiredImage = ImageIO.read(missileFiredPath);
      missileFiredHeight = missileFiredImage.getHeight();
      missileFiredWidth = missileFiredImage.getWidth();
    }
    catch( IOException ex )
    {
      Logger.getLogger(CarObstacle.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void update(Graphics2D g2d, int speed)
  {
    if( !wasUsed && !wasRetrieved )
    {
      super.update(g2d, speed);
    }
    else if( wasUsed )
    {
      super.update(g2d, -speed);
      if( yPos < 0 )
      {
        reset();
      }
    }
  }

  @Override
  protected void Draw(Graphics2D g2d)
  {
    if( !wasUsed )
    {
      super.Draw(g2d);
    }
    else
    {
      g2d.drawImage(missileFiredImage, xPos, yPos, missileFiredWidth,
          missileFiredHeight, null);
    }
  }

  @Override
  public int getPowerUpHeight()
  {
    if( wasUsed )
    {
      return missileFiredHeight;
    }
    return super.getPowerUpHeight();
  }

  @Override
  public int getPowerUpWidth()
  {
    if( wasUsed )
    {
      return missileFiredWidth;
    }
    return super.getPowerUpWidth();
  }
}
