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
import javax.swing.JPanel;
/**
 * Created by Alex on 11/17/14.
 */
public abstract class GameCanvas extends JPanel implements KeyListener, MouseListener {

    public GameCanvas(){
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        if(false){
            BuffereredImage blankCursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImage, new Point(0,0), null);
            this.setCursor(blankCursor);
        }

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

}
