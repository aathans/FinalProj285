package eecs285.proj4.finalGame;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.security.Key;
import javax.swing.JPanel;

/**
 * Created by Alex on 11/17/14.
 */
public abstract class GameCanvas extends JPanel implements KeyListener, MouseListener {

    private static boolean[] keyboardState = new boolean[525];
    private static boolean[] mouseState = new boolean[3];

    public GameCanvas(){
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        this.addKeyListener(this);
        this.addMouseListener(this);
    }

    public abstract void Draw(Graphics2D g2d);

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        Draw(g2d);
    }


    public static boolean keyboardKeyState(int key){
        return keyboardState[key];
    }

    @Override
    public void keyPressed(KeyEvent event){
        keyboardState[event.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent event){
        keyboardState[event.getKeyCode()] = false;
        keyReleasedLogic(event);
    }

    @Override
    public void keyTyped(KeyEvent event){

    }

    public abstract void keyReleasedLogic(KeyEvent event);

    @Override
    public void mousePressed(MouseEvent event){

    }

    @Override
    public void mouseReleased(MouseEvent event){

    }

    @Override
    public void mouseClicked(MouseEvent event){

    }

    @Override
    public void mouseEntered(MouseEvent event){

    }

    @Override
    public void mouseExited(MouseEvent event){

    }


}
