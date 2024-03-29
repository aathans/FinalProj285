package eecs285.proj4.finalGame;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Created by yeshwanthdevabhaktuni on 11/30/14.
 * The screen navigated to from the main menu called frequencies.  This
 * can change the rate at which powerups are created in the game.
 */
public class Frequency {

  private BufferedImage frequencyImage;

  Frequency(){
    GameLogic.gameState = GameLogic.GameState.FREQUENCY1;
    LoadSettings();
  }

  public void LoadSettings(){
    try{
      URL settingsPage = getClass().getResource("/images/frequency.png");
      frequencyImage = ImageIO.read(settingsPage);
    }
    catch(IOException ex){
      Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void Draw(Graphics2D g2d){
    g2d.drawImage(frequencyImage,0,0,GameLogic.frameWidth,GameLogic.frameHeight,null);
  }
}
