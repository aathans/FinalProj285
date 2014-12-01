package eecs285.proj4.finalGame;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Created by yeshwanthdevabhaktuni on 11/30/14.
 */
public class Frequency {

    private BufferedImage frequencyImage;

    Frequency(){
        GameLogic.gameState = GameLogic.GameState.FREQUENCY1;
        LoadSettings();
    }

    public void LoadSettings(){
        try{
            URL settingsPage = getClass().getResource("/images/settings.png");
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
