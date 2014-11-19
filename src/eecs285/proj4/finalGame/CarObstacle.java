package eecs285.proj4.finalGame;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alex on 11/18/14.
 */
public class CarObstacle extends Obstacle {

    CarObstacle(){
        super();
        setupCar();
    }

    private void setupCar(){
        try {
            URL carPath = this.getClass().getResource("/images/blue_car_mini.png");
            obstacleImage = ImageIO.read(carPath);
        } catch(IOException ex){
            Logger.getLogger(CarObstacle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
