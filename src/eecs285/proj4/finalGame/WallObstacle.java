package eecs285.proj4.finalGame;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alex on 11/18/14.
 */
public class WallObstacle extends Obstacle{

    WallObstacle(){
        super();
        setupWallObstacle();
    }

    private void setupWallObstacle(){
        try {
            URL wallPath = this.getClass().getResource("/images/wall.png");
            obstacleImage = ImageIO.read(wallPath);
            obstacleHeight = obstacleImage.getHeight();
            obstacleWidth = obstacleImage.getWidth();
        } catch(IOException ex){
            Logger.getLogger(WallObstacle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
