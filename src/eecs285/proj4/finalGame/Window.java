package eecs285.proj4.finalGame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Created by Alex on 11/17/14.
 */
public class Window extends JFrame {
    private static JFrame frame = new JFrame();

    private Window(){
        int width = 500;
        int height = 750;

        frame.setTitle("STREET RACER");

        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GameLogic());
        frame.setVisible(true);
    }

    public static void setDisabled()
    {
        frame.setEnabled(false);
    }

    public static void setEnabled()
    {
        frame.setEnabled(true);
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
