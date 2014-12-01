package eecs285.proj4.finalGame;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Created by yeshwanthdevabhaktuni on 11/26/14.
 */


public class gameSettings{

    private BufferedImage settingsImage;

    JPanel panel;
    JLabel label;

    gameSettings(){
        GameLogic.gameState = GameLogic.GameState.SETTINGS1;
        LoadSettings();
    }


    public void LoadSettings(){
        try{
            URL settingsPage = getClass().getResource("/images/settings.png");
            settingsImage = ImageIO.read(settingsPage);
        }
        catch(IOException ex){
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Draw(Graphics2D g2d){
        g2d.drawImage(settingsImage,0,0,GameLogic.frameWidth,GameLogic.frameHeight,null);
    }
}