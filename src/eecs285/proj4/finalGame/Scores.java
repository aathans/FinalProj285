package eecs285.proj4.finalGame;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

/**
 * Created by yeshwanthdevabhaktuni on 12/1/14.
 */
public class Scores extends JPanel {
    private BufferedImage scores;

    Scores(){
       GameLogic.gameState = GameLogic.GameState.SCORE1;
        loadScores();
    }

    //public GameCanvas(){

    //}

    public void loadScores(){
        try{
            URL score = getClass().getResource("/images/scores.png");
            scores = ImageIO.read(score);
        }
        catch(IOException ex){
            Logger.getLogger(GameLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void Draw(Graphics2D g2d){
        g2d.drawImage(scores,0,0,GameLogic.frameWidth,GameLogic.frameHeight,null);

        create(GameLogic.frameWidth/2 - 10,150,"23847234320",g2d);
        create1(GameLogic.frameWidth/7 - 10,150,"23847234320",g2d);
        create(GameLogic.frameWidth/2 - 10,200,"23847234320",g2d);
        create1(GameLogic.frameWidth/7 - 10,200,"23847234320",g2d);
        create(GameLogic.frameWidth/2 - 10,250,"23847234320",g2d);
        create1(GameLogic.frameWidth/7 - 10,250,"23847234320",g2d);
        create(GameLogic.frameWidth/2 - 10,300,"23847234320",g2d);
        create1(GameLogic.frameWidth/7 - 10,300,"23847234320",g2d);
        create(GameLogic.frameWidth/2 - 10,350,"23847234320",g2d);
        create1(GameLogic.frameWidth/7 - 10,350,"23847234320",g2d);
        create(GameLogic.frameWidth/2 - 10,400,"23847234320",g2d);
        create1(GameLogic.frameWidth/7 - 10,400,"23847234320",g2d);
        create(GameLogic.frameWidth/2 - 10,450,"23847234320",g2d);
        create1(GameLogic.frameWidth/7 - 10,450,"23847234320",g2d);
        create(GameLogic.frameWidth/2 - 10,500,"23847234320",g2d);
        create1(GameLogic.frameWidth/7 - 10,500,"23847234320",g2d);

    }

    public void create(int x,int y,String str,Graphics2D g2d){
        Color textColor = Color.GREEN;
        Color bgColor = Color.CYAN;

        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(str, g2d);

        g2d.setColor(textColor);

        Font font = new Font("Serif", Font.PLAIN,30 );
        g2d.setFont(font);

        g2d.drawString(str, x, y);
    }

    public void create1(int x,int y,String str,Graphics2D g2d){
        Color textColor = Color.YELLOW;
        Color bgColor = Color.CYAN;

        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(str, g2d);

        g2d.setColor(textColor);

        Font font = new Font("Serif", Font.PLAIN,30 );
        g2d.setFont(font);

        g2d.drawString(str, x, y);
    }
}
