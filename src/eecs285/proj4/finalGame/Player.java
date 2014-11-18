package eecs285.proj4.finalGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 * Created by Alex on 11/17/14.
 */
public class Player {

    private Random randX;

    private int xPos;

    private int yPos;

    public boolean crashed;

    private int speed;

    private int speedX;

    private int speedY;

    private int score;

    private int livesRemaining;

    private BufferedImage carImage;

    private BufferedImage carCrashImage;

    public int carWidth;

    public int carHeight;


    public Player(){
        setup();
        //loadPlayer();

        xPos = GameLogic.frameWidth/2;

    }

    private void setup(){
        reset();
        score = 0;
        livesRemaining = 3;
    }

    private void loadPlayer(){
        try{
            URL carPath = this.getClass().getResource("/resources/car.jpg");
            carImage = ImageIO.read(carPath);

            URL carCrashPath = this.getClass().getResource("/resources/crash.jpg");
            carCrashImage = ImageIO.read(carCrashPath);

        } catch(IOException ex){
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reset(){
        crashed = false;

        xPos = GameLogic.frameWidth/2;
        yPos = GameLogic.frameHeight - 50;

        speedX = 0;
        speedY = 0;
    }

    public void update(){
        if(GameCanvas.keyboardKeyState(KeyEvent.VK_UP) && (yPos > carHeight)){
            speedY += 10;
        } else if(yPos < (GameLogic.frameHeight - carHeight)){
            speedY -= 10;
        } else {
            speedY = 0;
        }

        if(GameCanvas.keyboardKeyState(KeyEvent.VK_RIGHT) && (xPos < GameLogic.frameWidth - carWidth)){
            speedX += 10;
        } else if(xPos > carWidth){
            speedX -= 10;
        } else {
            speedX = 0;
        }

        xPos += speedX;
        yPos += speedY;
    }

    public void Draw(Graphics2D g2d){
        g2d.setColor(Color.white);
        g2d.drawString("Score: " + score, 5, 15 );

        if(crashed){
            g2d.drawImage(carCrashImage, xPos, yPos, null);
        }
    }

}
