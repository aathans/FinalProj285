package eecs285.proj4.finalGame;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Created by yeshwanthdevabhaktuni on 12/1/14.
 */
public class Scores {
    private BufferedImage scores;

    Scores(){
       GameLogic.gameState = GameLogic.GameState.SCORE1;
        loadScores();
    }

    public void loadScores(){
        try{
            URL settingsPage = getClass().getResource("/images/scores.png");
            scores = ImageIO.read(settingsPage);
        }
        catch(IOException ex){
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Draw(Graphics2D g2d){
        g2d.drawImage(scores,0,0,GameLogic.frameWidth,GameLogic.frameHeight,null);
    }
}
