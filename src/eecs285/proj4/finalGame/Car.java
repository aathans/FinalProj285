package eecs285.proj4.finalGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.BatchUpdateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yeshwanthdevabhaktuni on 11/30/14.
 */
public class Car {
    private BufferedImage car;

    Car(){
        GameLogic.gameState = GameLogic.GameState.CAR1;
        LoadSettings();
    }

    public void LoadSettings(){
        try{
            URL settingsPage = getClass().getResource("/images/car6.png");
            car = ImageIO.read(settingsPage);
        }
        catch(IOException ex){
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Draw(Graphics2D g2d){
        g2d.drawImage(car,0,0,GameLogic.frameWidth,GameLogic.frameHeight,null);
    }

}
