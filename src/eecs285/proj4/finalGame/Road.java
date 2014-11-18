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
public class Road {

    public int x;

    public int y;

    private BufferedImage roadImage;

    public int roadWidth;

    public Road(){
        setupRoad();
        loadRoad();
    }

    private void setupRoad(){

    }

    private void loadRoad(){
        try{
            URL roadImagePath = this.getClass().getResource("/resources/road.img");
            roadImage = ImageIO.read(roadImagePath);
            roadWidth = roadImage.getWidth();
        }catch (IOException ex){
            Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Draw(Graphics2D g2d){
        g2d.drawImage(roadImage, x, y, null);
    }
}
