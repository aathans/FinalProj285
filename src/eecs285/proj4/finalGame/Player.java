package eecs285.proj4.finalGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
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
        loadPlayer();
    }

    private void setup(){
        reset();
        score = 0;
        livesRemaining = 3;
    }

    private void loadPlayer(){
        try{
            URL carPath = this.getClass().getResource("/images/car.png");
            carImage = ImageIO.read(carPath);
            carHeight = carImage.getHeight();
            carWidth = carImage.getWidth();

            //URL carCrashPath = this.getClass().getResource("/resources/crash.jpg");
            //carCrashImage = ImageIO.read(carCrashPath);

        } catch(IOException ex){
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reset(){
        crashed = false;

        xPos = GameLogic.frameWidth/2 - 75;
        yPos = GameLogic.frameHeight - 150;

        speedX = 0;
        speedY = 2;
    }

    public void update(){

        if(GameCanvas.keyboardKeyState(KeyEvent.VK_UP) && (yPos > carHeight)){
            speedY = 4;
        } else if(GameCanvas.keyboardKeyState(KeyEvent.VK_DOWN)){
            speedY = 1;
        } else {
            speedY = 2;
        }

        if(GameCanvas.keyboardKeyState(KeyEvent.VK_RIGHT)){
            if(xPos < GameLogic.frameWidth - carWidth){
                speedX += 1;
            } else {
                speedX = 0;
            }
        } else if(GameCanvas.keyboardKeyState(KeyEvent.VK_LEFT)){
            if(xPos > 0){
                speedX -= 1;
            } else {
                speedX = 0;
            }
        } else {
            speedX = 0;
        }


        xPos += speedX;

        //yPos += speedY;
        score += speedY;
    }

    public void Draw(Graphics2D g2d){
        g2d.setColor(Color.white);
        g2d.drawString("Score: " + score, 5, 15 );

        g2d.drawImage(carImage, xPos, yPos, carWidth, carHeight, null);

        if(crashed){
            g2d.drawImage(carCrashImage, xPos, yPos, null);
        }
    }

    public int getSpeedY(){
        return speedY;
    }

}
