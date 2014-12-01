package eecs285.proj4.finalGame;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alex on 11/18/14.
 * The blue cars that come towards a player when they are racing.
 * These obstacles can collide with the player and end the game.
 */
public class CarObstacle extends Obstacle
{

  CarObstacle()
  {
    super();
    setupCar();
  }

  private void setupCar()
  {
    try
    {
      URL carPath = this.getClass().getResource("/images/blue_car_mini.png");
      obstacleImage = ImageIO.read(carPath);
      obstacleHeight = obstacleImage.getHeight();
      obstacleWidth = obstacleImage.getWidth();
    }
    catch( IOException ex )
    {
      Logger.getLogger(CarObstacle.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
