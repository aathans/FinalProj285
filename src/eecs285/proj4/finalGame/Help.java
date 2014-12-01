package eecs285.proj4.finalGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yeshwanthdevabhaktuni on 11/30/14.
 */
public class Help {
  private BufferedImage help;

  Help(){
    GameLogic.gameState = GameLogic.gameState.HELP1;
    loadHelp();
  }

  public void loadHelp(){
    try{
      URL settingsPage = getClass().getResource("/images/help.png");
      help = ImageIO.read(settingsPage);
    }
    catch(IOException ex){
      Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void Draw(Graphics2D g2d){
    g2d.drawImage(help,0,0,GameLogic.frameWidth,GameLogic.frameHeight,null);
  }
}
