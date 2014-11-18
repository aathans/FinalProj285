package eecs285.proj4.finalGame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Created by Alex on 11/17/14.
 */
public class Window extends JFrame {

    private Window(){
        this.setTitle("Game Name");

        this.setSize(500,1000);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new GameLogic());
        this.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
