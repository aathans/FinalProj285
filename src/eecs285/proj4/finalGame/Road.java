package eecs285.proj4.finalGame;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Created by Alex on 11/17/14.
 */
public class Road
{

  public int xPos;

  public int yPos;

  private BufferedImage roadImage;

  private BufferedImage[] leftLines;

  private BufferedImage[] rightLines;

  public int roadWidth;

  public int lineHeight;

  public Road()
  {
    setupRoad();
    loadRoad();
  }

  private void setupRoad()
  {
    leftLines = new BufferedImage[2];
    rightLines = new BufferedImage[2];
  }

  private void loadRoad()
  {
    try
    {
      URL roadImagePath = this.getClass().getResource("/resources/road.img");
      roadImage = ImageIO.read(roadImagePath);
      roadWidth = roadImage.getWidth();

      URL linesImagePath = this.getClass().getResource("/resources/lines.jpg");
      leftLines[0] = ImageIO.read(linesImagePath);
      leftLines[1] = ImageIO.read(linesImagePath);
      rightLines[0] = ImageIO.read(linesImagePath);
      rightLines[1] = ImageIO.read(linesImagePath);
      lineHeight = leftLines[0].getHeight();

    }
    catch( IOException ex )
    {
      Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void Draw(Graphics2D g2d)
  {
    g2d.drawImage(roadImage, xPos, yPos, null);
    g2d.drawImage(leftLines[0], GameLogic.frameWidth / 3, 0, null);
    g2d.drawImage(leftLines[1], GameLogic.frameWidth / 3, -1 * lineHeight,
        null);
    g2d.drawImage(rightLines[0], 2 * GameLogic.frameWidth / 3, 0, null);
    g2d.drawImage(rightLines[1], 2 * GameLogic.frameWidth / 3, -1 * lineHeight,
        null);
  }
}
